/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.controller;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.github.pagehelper.PageInfo;
import com.wgcloud.config.CommonConfig;
import com.wgcloud.entity.AccountInfo;
import com.wgcloud.entity.ShellInfo;
import com.wgcloud.entity.ShellState;
import com.wgcloud.entity.SystemInfo;
import com.wgcloud.service.LogInfoService;
import com.wgcloud.service.ShellInfoService;
import com.wgcloud.service.ShellStateService;
import com.wgcloud.service.SystemInfoService;
import com.wgcloud.util.FormatUtil;
import com.wgcloud.util.HostUtil;
import com.wgcloud.util.PageUtil;
import com.wgcloud.util.ResDataUtils;
import com.wgcloud.util.ThreadPoolUtil;
import com.wgcloud.util.TokenUtils;
import com.wgcloud.util.license.LicenseUtil;
import com.wgcloud.util.msg.WarnOtherUtil;
import com.wgcloud.util.staticvar.StaticKeys;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value={"/shellInfo"})
public class ShellInfoController {
    private static final Logger logger = LoggerFactory.getLogger(ShellInfoController.class);
    @Resource
    private ShellInfoService shellInfoService;
    @Resource
    private ShellStateService shellStateService;
    @Resource
    private LogInfoService logInfoService;
    @Resource
    private SystemInfoService systemInfoService;
    @Autowired
    private TokenUtils tokenUtils;
    @Autowired
    private CommonConfig commonConfig;

    @ResponseBody
    @RequestMapping(value={"agentList"})
    public String agentList(@RequestBody String paramBean) {
        JSONObject agentJsonObject = (JSONObject)JSONUtil.parse((Object)paramBean);
        if (!this.tokenUtils.checkAgentToken(agentJsonObject)) {
            logger.error("Token is error");
            return ResDataUtils.resetErrorJson("Token is error");
        }
        if (!"true".equals(this.commonConfig.getShellToRun())) {
            return ResDataUtils.resetErrorJson("shellToRun is no open");
        }
        HashMap<String, Object> params = new HashMap<String, Object>();
        if (null == agentJsonObject.get("hostname") || StringUtils.isEmpty((CharSequence)agentJsonObject.get("hostname").toString())) {
            return "";
        }
        params.put("hostname", agentJsonObject.get("hostname").toString());
        try {
            params.put("state", "1");
            params.put("shellTime", System.currentTimeMillis());
            List<ShellState> shellStateList = this.shellStateService.selectAllByParams(params);
            String blockKey = "";
            ArrayList<ShellState> shellStateListResult = new ArrayList<ShellState>();
            for (ShellState state : shellStateList) {
                blockKey = FormatUtil.haveBlockDanger(state.getShell(), this.commonConfig.getShellToRunBlock());
                if (!StringUtils.isEmpty((CharSequence)blockKey)) {
                    logger.info(state.getShell() + "\u4e0b\u53d1\u6307\u4ee4\u542b\u6709\u654f\u611f\u5b57\u7b26" + blockKey + "\uff0c\u4e0d\u8fdb\u884c\u4e0b\u53d1");
                    continue;
                }
                state.setShell(state.getShell().replaceAll("\\r\\n", " && "));
                shellStateListResult.add(state);
                this.shellStateService.updateSendByIds(state.getId().split(","));
            }
            return ResDataUtils.resetSuccessJson(shellStateListResult);
        }
        catch (Exception e) {
            logger.error("agent\u83b7\u53d6\u4e0b\u53d1\u6307\u4ee4\u4fe1\u606f\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("agent\u83b7\u53d6\u4e0b\u53d1\u6307\u4ee4\u4fe1\u606f\u9519\u8bef", e.toString(), "2");
            return ResDataUtils.resetErrorJson(e.toString());
        }
    }

    @ResponseBody
    @RequestMapping(value={"/shellCallback"})
    public String appCallback(@RequestBody String paramBean) {
        JSONObject agentJsonObject = (JSONObject)JSONUtil.parse((Object)paramBean);
        logger.info("shellCallback-------------" + agentJsonObject.toString());
        if (!this.tokenUtils.checkAgentToken(agentJsonObject)) {
            logger.error("Token is error");
            return ResDataUtils.resetErrorJson("Token is error");
        }
        try {
            String shellStateId = agentJsonObject.getStr("id");
            if (StringUtils.isEmpty((CharSequence)shellStateId)) {
                logger.error("shellStateId is null");
                return ResDataUtils.resetErrorJson("shellStateId is null");
            }
            String shellState = agentJsonObject.getStr("state");
            String shellResult = agentJsonObject.getStr("shellResult");
            if (!StaticKeys.LICENSE_STATE.equals("1")) {
                shellResult = "\u8bf7\u5347\u7ea7\u5230\u4e13\u4e1a\u7248\u67e5\u770b\u6b64\u6570\u636e";
            } else if (!StringUtils.isEmpty((CharSequence)shellResult) && shellResult.length() > 4000) {
                shellResult = shellResult.substring(0, 4000);
            }
            if (StringUtils.isEmpty((CharSequence)shellState)) {
                shellState = "5";
            }
            ShellState shellStateCall = this.shellStateService.selectById(shellStateId);
            shellStateCall.setState(shellState);
            shellStateCall.setShellResult(shellResult);
            shellStateCall.setCreateTime(new Date());
            this.shellStateService.updateById(shellStateCall);
            return ResDataUtils.resetSuccessJson(null);
        }
        catch (Exception e) {
            logger.error("agent\u6267\u884c\u6307\u4ee4\u5b8c\u6210\u56de\u8c03\u9519\u8bef", (Throwable)e);
            return ResDataUtils.resetErrorJson(e.toString());
        }
    }

    @RequestMapping(value={"list"})
    public String shellInfoList(ShellInfo ShellInfo2, Model model, HttpServletRequest request) {
        HashMap<String, Object> params = new HashMap<String, Object>();
        try {
            LicenseUtil.maxLicense_10(model, request, ShellInfo2);
            StringBuffer url = new StringBuffer();
            String shellName = null;
            if (!StringUtils.isEmpty((CharSequence)ShellInfo2.getShellName())) {
                shellName = ShellInfo2.getShellName();
                params.put("shellName", shellName.trim());
                url.append("&shellName=").append(shellName);
            }
            if (!StringUtils.isEmpty((CharSequence)ShellInfo2.getAccount())) {
                params.put("account", ShellInfo2.getAccount());
                url.append("&account=").append(ShellInfo2.getAccount());
            }
            if (!StringUtils.isEmpty((CharSequence)ShellInfo2.getState())) {
                params.put("state", ShellInfo2.getState());
                url.append("&state=").append(ShellInfo2.getState());
            }
            HostUtil.addAccountquery(request, params);
            PageInfo pageInfo = this.shellInfoService.selectByParams(params, ShellInfo2.getPage(), ShellInfo2.getPageSize());
            HashMap<String, Object> paramsStateCount = new HashMap<String, Object>();
            for (ShellInfo shellInfo1 : (java.util.List<ShellInfo>) pageInfo.getList()) {
                paramsStateCount.clear();
                paramsStateCount.put("shellId", shellInfo1.getId());
                Integer stateAllCount = this.shellStateService.countByParams(paramsStateCount);
                paramsStateCount.put("state", "1");
                Integer state1Count = this.shellStateService.countByParams(paramsStateCount);
                shellInfo1.setState1Count(state1Count);
                shellInfo1.setStateOtherCount(stateAllCount - state1Count);
                if (state1Count != 0 || "2".equals(shellInfo1.getState())) continue;
                shellInfo1.setState("3");
            }
            PageUtil.initPageNumber(pageInfo, model);
            HostUtil.addAccountListModel(model);
            model.addAttribute("pageUrl", (Object)("/shellInfo/list?1=1" + url.toString()));
            model.addAttribute("page", (Object)pageInfo);
            model.addAttribute("shellInfo", (Object)ShellInfo2);
        }
        catch (Exception e) {
            logger.error("\u67e5\u8be2\u4e0b\u53d1\u6307\u4ee4\u4fe1\u606f\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("\u67e5\u8be2\u4e0b\u53d1\u6307\u4ee4\u4fe1\u606f\u9519\u8bef", e.toString(), "2");
        }
        return "shellInfo/list";
    }

    @RequestMapping(value={"save"})
    public String saveShellInfo(ShellInfo shellInfo, Model model, HttpServletRequest request) {
        AccountInfo accountInfo = HostUtil.getAccountByRequest(request);
        if ("guest".equals(accountInfo.getRole())) {
            return "redirect:/common/error/guestError";
        }
        if (!"true".equals(this.commonConfig.getShellToRun())) {
            return ResDataUtils.resetErrorJson("shellToRun is no open");
        }
        String[] selectedHostnames = request.getParameterValues("hostnames");
        try {
            String blockKey = FormatUtil.haveBlockDanger(shellInfo.getShell(), this.commonConfig.getShellToRunBlock());
            if (!StringUtils.isEmpty((CharSequence)blockKey)) {
                model.addAttribute("shellInfo", (Object)shellInfo);
                model.addAttribute("selectedHosts", (Object)selectedHostnames);
                HashMap<String, Object> paramsAccount = new HashMap<String, Object>();
                HostUtil.addAccountquery(request, paramsAccount);
                List<SystemInfo> systemInfoList = this.systemInfoService.selectAllByParams(paramsAccount);
                for (SystemInfo systemInfo : systemInfoList) {
                    for (String selectedHost : selectedHostnames) {
                        if (!selectedHost.equals(systemInfo.getHostname())) continue;
                        systemInfo.setSelected("selected");
                    }
                }
                model.addAttribute("systemInfoList", systemInfoList);
                model.addAttribute("msg", (Object)("\u4e0b\u53d1\u6307\u4ee4\u542b\u6709\u654f\u611f\u5b57\u7b26" + blockKey + "\uff0c\u8bf7\u68c0\u67e5"));
                this.shellInfoService.getBlockStr(model);
                this.shellInfoService.setGroupList(systemInfoList, model, request);
                return "shellInfo/add";
            }
            if ("2".equals(shellInfo.getShellType()) && !StringUtils.isEmpty((CharSequence)shellInfo.getShellTime())) {
                shellInfo.setShellTime(shellInfo.getShellTime() + ":00");
            }
            if (StringUtils.isEmpty((CharSequence)shellInfo.getId())) {
                if (null == selectedHostnames || selectedHostnames.length < 1) {
                    return "redirect:/shellInfo/list";
                }
                if (!"admin".equals(accountInfo.getRole())) {
                    shellInfo.setAccount(accountInfo.getAccount());
                }
                this.shellInfoService.save(shellInfo, selectedHostnames, request);
                Runnable runnable = () -> {
                    try {
                        if (shellInfo != null) {
                            WarnOtherUtil.sendShellInfo(shellInfo, "\u5f00\u59cb\u4e0b\u53d1\u6307\u4ee4", request);
                        }
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                };
                ThreadPoolUtil.executor.execute(runnable);
            } else {
                this.shellInfoService.updateById(shellInfo);
            }
        }
        catch (Exception e) {
            logger.error("\u4fdd\u5b58\u4e0b\u53d1\u6307\u4ee4\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("\u4fdd\u5b58\u4e0b\u53d1\u6307\u4ee4\u9519\u8bef", e.toString(), "2");
        }
        return "redirect:/shellInfo/list";
    }

    @RequestMapping(value={"edit"})
    public String edit(Model model, HttpServletRequest request) {
        AccountInfo accountInfo = HostUtil.getAccountByRequest(request);
        if ("guest".equals(accountInfo.getRole())) {
            return "redirect:/common/error/guestError";
        }
        String errorMsg = "\u6dfb\u52a0\u4e0b\u53d1\u6307\u4ee4";
        String id = request.getParameter("id");
        ShellInfo shellInfo = new ShellInfo();
        try {
            HashMap<String, Object> paramsAccount = new HashMap<String, Object>();
            HostUtil.addAccountquery(request, paramsAccount);
            List<SystemInfo> systemInfoList = this.systemInfoService.selectAllByParams(paramsAccount);
            model.addAttribute("systemInfoList", systemInfoList);
            this.shellInfoService.getBlockStr(model);
            this.shellInfoService.setGroupList(systemInfoList, model, request);
            if (!this.isAddContinue()) {
                return "redirect:/shellInfo/list?liceFlage=1";
            }
            if (StringUtils.isEmpty((CharSequence)id)) {
                model.addAttribute("shellInfo", (Object)shellInfo);
                return "shellInfo/add";
            }
            HashMap<String, Object> params = new HashMap<String, Object>();
            params.put("shellId", id);
            List<ShellState> shellStateList = this.shellStateService.selectAllByParams(params);
            for (SystemInfo systemInfo : systemInfoList) {
                for (ShellState shellState : shellStateList) {
                    if (!shellState.getHostname().equals(systemInfo.getHostname())) continue;
                    systemInfo.setSelected("selected");
                }
            }
            shellInfo = this.shellInfoService.selectById(id);
            model.addAttribute("shellInfo", (Object)shellInfo);
        }
        catch (Exception e) {
            logger.error(errorMsg, (Throwable)e);
            this.logInfoService.save(errorMsg, e.toString(), "2");
        }
        return "shellInfo/add";
    }

    @RequestMapping(value={"view"})
    public String view(Model model, HttpServletRequest request) {
        String errorMsg = "\u67e5\u770b\u6307\u4ee4\u4e0b\u53d1\u6267\u884c\u72b6\u6001\u4fe1\u606f\u9519\u8bef";
        String id = request.getParameter("id");
        ShellInfo ShellInfo2 = new ShellInfo();
        try {
            ShellInfo2 = this.shellInfoService.selectById(id);
            HashMap<String, Object> params = new HashMap<String, Object>();
            params.put("shellId", id);
            List<ShellState> shellStateList = this.shellStateService.selectAllByParams(params);
            for (ShellState shellState1 : shellStateList) {
                shellState1.setHostname(shellState1.getHostname() + HostUtil.addRemark(shellState1.getHostname()));
            }
            model.addAttribute("shellStateList", shellStateList);
            model.addAttribute("shellInfo", (Object)ShellInfo2);
        }
        catch (Exception e) {
            logger.error(errorMsg, (Throwable)e);
            this.logInfoService.save(errorMsg, e.toString(), "2");
        }
        return "shellInfo/stateList";
    }

    @RequestMapping(value={"stateView"})
    public String stateView(Model model, HttpServletRequest request) {
        String errorMsg = "\u67e5\u770b\u4e0b\u53d1\u6307\u4ee4\u7684\u6267\u884c\u7ed3\u679c\u4fe1\u606f\u9519\u8bef";
        String id = request.getParameter("id");
        ShellState shellState = new ShellState();
        try {
            shellState = this.shellStateService.selectById(id);
            shellState.setHostname(shellState.getHostname() + HostUtil.addRemark(shellState.getHostname()));
            model.addAttribute("shellState", (Object)shellState);
            ShellInfo shellInfo = this.shellInfoService.selectById(shellState.getShellId());
            model.addAttribute("shellInfo", (Object)shellInfo);
        }
        catch (Exception e) {
            logger.error(errorMsg, (Throwable)e);
            this.logInfoService.save(errorMsg, e.toString(), "2");
        }
        return "shellInfo/stateView";
    }

    @ResponseBody
    @RequestMapping(value={"cancel"})
    public String cancel(Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        AccountInfo accountInfo = HostUtil.getAccountByRequest(request);
        if ("guest".equals(accountInfo.getRole())) {
            return "redirect:/common/error/guestError";
        }
        String errorMsg = "\u53d6\u6d88\u4e0b\u53d1\u6307\u4ee4\u4fe1\u606f\u9519\u8bef";
        try {
            if (!StringUtils.isEmpty((CharSequence)request.getParameter("id"))) {
                ShellInfo shellInfo = this.shellInfoService.selectById(request.getParameter("id"));
                this.shellInfoService.cancelShell(request.getParameter("id"));
                Runnable runnable = () -> {
                    try {
                        if (shellInfo != null) {
                            WarnOtherUtil.sendShellInfo(shellInfo, "\u53d6\u6d88\u4e0b\u53d1\u6307\u4ee4", request);
                        }
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                };
                ThreadPoolUtil.executor.execute(runnable);
            }
        }
        catch (Exception e) {
            logger.error(errorMsg, (Throwable)e);
            this.logInfoService.save(errorMsg, e.toString(), "2");
        }
        return ResDataUtils.resetSuccessJson(null);
    }

    @RequestMapping(value={"del"})
    public String delete(Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        AccountInfo accountInfo = HostUtil.getAccountByRequest(request);
        if ("guest".equals(accountInfo.getRole())) {
            return "redirect:/common/error/guestError";
        }
        String errorMsg = "\u5220\u9664\u4e0b\u53d1\u6267\u884c\u4fe1\u606f\u9519\u8bef";
        try {
            if (!StringUtils.isEmpty((CharSequence)request.getParameter("id"))) {
                String[] ids;
                for (String id : ids = request.getParameter("id").split(",")) {
                    ShellInfo shellInfo = this.shellInfoService.selectById(id);
                    Runnable runnable = () -> {
                        try {
                            if (shellInfo != null) {
                                WarnOtherUtil.sendShellInfo(shellInfo, "\u5220\u9664\u4e0b\u53d1\u6307\u4ee4", request);
                            }
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                    };
                    ThreadPoolUtil.executor.execute(runnable);
                }
                this.shellInfoService.deleteById(ids);
            }
        }
        catch (Exception e) {
            logger.error(errorMsg, (Throwable)e);
            this.logInfoService.save(errorMsg, e.toString(), "2");
        }
        return "redirect:/shellInfo/list";
    }

    @RequestMapping(value={"sendForHostname"})
    public String sendForHostname(Model model, HttpServletRequest request) {
        AccountInfo accountInfo = HostUtil.getAccountByRequest(request);
        if ("guest".equals(accountInfo.getRole())) {
            return "redirect:/common/error/guestError";
        }
        String errorMsg = "\u4e3b\u673a\u67e5\u770b\u9875\u9762\u6dfb\u52a0\u4e0b\u53d1\u6307\u4ee4";
        String hostname = request.getParameter("hostname");
        if (StringUtils.isEmpty((CharSequence)hostname)) {
            return "redirect:/common/error/guestError";
        }
        ShellInfo shellInfo = new ShellInfo();
        try {
            HashMap<String, Object> paramsAccount = new HashMap<String, Object>();
            HostUtil.addAccountquery(request, paramsAccount);
            List<SystemInfo> systemInfoList = this.systemInfoService.selectAllByParams(paramsAccount);
            model.addAttribute("systemInfoList", systemInfoList);
            this.shellInfoService.getBlockStr(model);
            this.shellInfoService.setGroupList(systemInfoList, model, request);
            for (SystemInfo systemInfo : systemInfoList) {
                if (!hostname.equals(systemInfo.getHostname())) continue;
                systemInfo.setSelected("selected");
            }
            model.addAttribute("shellInfo", (Object)shellInfo);
        }
        catch (Exception e) {
            logger.error(errorMsg, (Throwable)e);
            this.logInfoService.save(errorMsg, e.toString(), "2");
        }
        return "shellInfo/add";
    }

    private boolean isAddContinue() {
        try {
            HashMap<String, Object> params;
            int dbSize;
            if (!StaticKeys.LICENSE_STATE.equals("1") && (dbSize = this.shellInfoService.countByParams(params = new HashMap<String, Object>())) >= 10) {
                return false;
            }
        }
        catch (Exception e) {
            logger.error("isAddContinue error", (Throwable)e);
        }
        return true;
    }
}

