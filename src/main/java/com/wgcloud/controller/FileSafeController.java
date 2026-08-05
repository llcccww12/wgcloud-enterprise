/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.controller;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.github.pagehelper.PageInfo;
import com.wgcloud.config.CommonConfig;
import com.wgcloud.dto.ShellCheckDto;
import com.wgcloud.entity.FileSafe;
import com.wgcloud.entity.SystemInfo;
import com.wgcloud.service.FileSafeService;
import com.wgcloud.service.HostGroupService;
import com.wgcloud.service.LogInfoService;
import com.wgcloud.service.ShellInfoService;
import com.wgcloud.service.SystemInfoService;
import com.wgcloud.util.FormatUtil;
import com.wgcloud.util.HostUtil;
import com.wgcloud.util.MessageErrorUtils;
import com.wgcloud.util.PageUtil;
import com.wgcloud.util.ResDataUtils;
import com.wgcloud.util.TokenUtils;
import com.wgcloud.util.license.LicenseUtil;
import com.wgcloud.util.staticvar.StaticKeys;
import java.util.ArrayList;
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
@RequestMapping(value={"/fileSafe"})
public class FileSafeController {
    private static final Logger logger = LoggerFactory.getLogger(FileSafeController.class);
    @Resource
    private FileSafeService fileSafeService;
    @Resource
    private LogInfoService logInfoService;
    @Resource
    private SystemInfoService systemInfoService;
    @Resource
    private ShellInfoService shellInfoService;
    @Resource
    private HostGroupService hostGroupService;
    @Autowired
    private MessageErrorUtils messageErrorUtils;
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
        if (!this.tokenUtils.checkAllowOpenData(agentJsonObject)) {
            logger.error("The module needs to professional version. Please contact us at www.wgstart.com");
            return ResDataUtils.resetErrorJson("The module needs to professional version. Please contact us at www.wgstart.com");
        }
        HashMap<String, Object> params = new HashMap<String, Object>();
        if (null == agentJsonObject.get("hostname") || StringUtils.isEmpty((CharSequence)agentJsonObject.get("hostname").toString())) {
            return "";
        }
        params.put("hostname", agentJsonObject.get("hostname").toString());
        try {
            params.put("active", "1");
            List<FileSafe> fileSafeList = this.fileSafeService.selectAllByParams(params);
            if ("1".equals(agentJsonObject.get("fromAgent"))) {
                for (FileSafe fileSafe : fileSafeList) {
                    if (!"refresh".equals(fileSafe.getFileSign())) continue;
                    FileSafe fileSafeUpdate = new FileSafe();
                    fileSafeUpdate.setId(fileSafe.getId());
                    fileSafeUpdate.setFileSign("");
                    this.fileSafeService.updateById(fileSafeUpdate);
                }
            }
            ShellCheckDto shellCheckDto = this.shellInfoService.getShellCheckDto(agentJsonObject.get("hostname").toString());
            String cmdSplitChar = shellCheckDto.getCmdSplitChar();
            String blockKey = "";
            ArrayList<FileSafe> fileSafeListResult = new ArrayList<FileSafe>();
            for (FileSafe fileSafe : fileSafeList) {
                if (StringUtils.isEmpty((CharSequence)fileSafe.getCustomShell())) {
                    fileSafe.setCustomShell("");
                }
                if (!StringUtils.isEmpty((CharSequence)(blockKey = FormatUtil.haveBlockDanger(fileSafe.getCustomShell(), this.commonConfig.getShellToRunBlock())))) {
                    logger.error(fileSafe.getCustomShell() + "\u6587\u4ef6\u9632\u7be1\u6539\u81ea\u52a8\u6062\u590d\u6307\u4ee4\u542b\u6709\u654f\u611f\u5b57\u7b26" + blockKey + "\uff0c\u4e0d\u8fdb\u884c\u4e0b\u53d1");
                    continue;
                }
                fileSafe.setCustomShell(fileSafe.getCustomShell().replaceAll("\\r\\n", cmdSplitChar));
                fileSafeListResult.add(fileSafe);
            }
            return ResDataUtils.resetSuccessJson(fileSafeListResult);
        }
        catch (Exception e) {
            logger.error("agent\u83b7\u53d6\u6587\u4ef6\u9632\u7be1\u6539\u4fe1\u606f\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("agent\u83b7\u53d6\u6587\u4ef6\u9632\u7be1\u6539\u4fe1\u606f\u9519\u8bef", e.toString(), "2");
            return ResDataUtils.resetErrorJson(e.toString());
        }
    }

    @RequestMapping(value={"list"})
    public String fileSafeList(FileSafe fileSafe, Model model, HttpServletRequest request) {
        HashMap<String, Object> params = new HashMap<String, Object>();
        try {
            LicenseUtil.maxLicense_10(model, request, fileSafe);
            StringBuffer url = new StringBuffer();
            String hostname = null;
            if (!StringUtils.isEmpty((CharSequence)fileSafe.getHostname())) {
                hostname = fileSafe.getHostname();
                params.put("hostname", hostname.trim());
                url.append("&hostname=").append(hostname);
            }
            if (!StringUtils.isEmpty((CharSequence)fileSafe.getAccount())) {
                params.put("account", fileSafe.getAccount());
                url.append("&account=").append(fileSafe.getAccount());
            }
            if (!StringUtils.isEmpty((CharSequence)fileSafe.getGroupId())) {
                params.put("groupId", fileSafe.getGroupId());
                url.append("&groupId=").append(fileSafe.getGroupId());
            }
            if (!StringUtils.isEmpty((CharSequence)fileSafe.getState())) {
                params.put("state", fileSafe.getState());
                url.append("&state=").append(fileSafe.getState());
            }
            if (!StringUtils.isEmpty((CharSequence)fileSafe.getActive())) {
                params.put("active", fileSafe.getActive());
                url.append("&active=").append(fileSafe.getActive());
            }
            if (!StringUtils.isEmpty((CharSequence)fileSafe.getOrderBy())) {
                params.put("orderBy", fileSafe.getOrderBy());
                params.put("orderType", fileSafe.getOrderType());
                url.append("&orderBy=").append(fileSafe.getOrderBy());
                url.append("&orderType=").append(fileSafe.getOrderType());
            }
            HostUtil.addAccountquery(request, params);
            PageInfo pageInfo = this.fileSafeService.selectByParams(params, fileSafe.getPage(), fileSafe.getPageSize());
            for (FileSafe fileSafe1 : (java.util.List<FileSafe>) pageInfo.getList()) {
                if ("true".equals(this.commonConfig.getUserInfoManage())) {
                    fileSafe1.setAccount(HostUtil.getAccount(fileSafe1.getHostname()));
                }
                fileSafe1.setHostState(HostUtil.getHostState(fileSafe1.getHostname()));
                fileSafe1.setHostname(fileSafe1.getHostname() + HostUtil.addRemark(fileSafe1.getHostname()));
                if (!"true".equals(this.commonConfig.getShowWarnCount())) continue;
                String warnQueryWd = fileSafe1.getFileName() + "\uff0c" + fileSafe1.getHostname();
                this.logInfoService.warnQueryHandle(fileSafe1, warnQueryWd);
            }
            if ("true".equals(this.commonConfig.getHostGroup())) {
                this.fileSafeService.setGroupInList(pageInfo.getList(), model, request);
            }
            HostUtil.addAccountListModel(model);
            PageUtil.initPageNumber(pageInfo, model);
            model.addAttribute("pageUrl", (Object)("/fileSafe/list?1=1" + url.toString()));
            model.addAttribute("page", (Object)pageInfo);
            model.addAttribute("fileSafe", (Object)fileSafe);
        }
        catch (Exception e) {
            logger.error("\u67e5\u8be2\u6587\u4ef6\u9632\u7be1\u6539\u4fe1\u606f\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("\u67e5\u8be2\u6587\u4ef6\u9632\u7be1\u6539\u4fe1\u606f\u9519\u8bef", e.toString(), "2");
        }
        return "fileSafe/list";
    }

    @ResponseBody
    @RequestMapping(value={"saveGroupId"})
    public String saveGroupId(Model model, HttpServletRequest request) {
        try {
            String ids = request.getParameter("ids");
            String[] groupIdsArr = request.getParameterValues("groupId");
            this.fileSafeService.saveGroupId(ids, groupIdsArr, request);
        }
        catch (Exception e) {
            logger.error("\u4fdd\u5b58\u6587\u4ef6\u9632\u7be1\u6539\u6807\u7b7e\u4fe1\u606f\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("\u4fdd\u5b58\u6587\u4ef6\u9632\u7be1\u6539\u6807\u7b7e\u4fe1\u606f\u9519\u8bef", e.toString(), "2");
        }
        return "redirect:/fileSafe/list";
    }

    @ResponseBody
    @RequestMapping(value={"refreshFileSafeState"})
    public String refreshFileSafeState(Model model, HttpServletRequest request) {
        try {
            String ids = request.getParameter("id");
            this.fileSafeService.refreshState(ids, request);
        }
        catch (Exception e) {
            logger.error("\u91cd\u65b0\u5f00\u59cb\u76d1\u63a7\u6587\u4ef6\u5939\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("\u91cd\u65b0\u5f00\u59cb\u76d1\u63a7\u6587\u4ef6\u5939\u9519\u8bef", e.toString(), "2");
        }
        return "success";
    }

    @RequestMapping(value={"save"})
    public String saveFileSafe(FileSafe fileSafe, Model model, HttpServletRequest request) {
        String errorMsg = "\u4fdd\u5b58\u6587\u4ef6\u9632\u7be1\u6539\u4fe1\u606f\u9519\u8bef";
        try {
            String blockKey = FormatUtil.haveBlockDanger(fileSafe.getCustomShell(), this.commonConfig.getShellToRunBlock());
            if (!StringUtils.isEmpty((CharSequence)blockKey)) {
                model.addAttribute("fileSafe", (Object)fileSafe);
                model.addAttribute("msg", (Object)("\u81ea\u52a8\u6062\u590d\u6307\u4ee4\u542b\u6709\u654f\u611f\u5b57\u7b26" + blockKey + "\uff0c\u8bf7\u68c0\u67e5"));
                this.shellInfoService.getBlockStr(model);
                HashMap<String, Object> params = new HashMap<String, Object>();
                HostUtil.addAccountquery(request, params);
                List<SystemInfo> systemInfoList = this.systemInfoService.selectAllByParams(params);
                model.addAttribute("systemInfoList", systemInfoList);
                return "fileSafe/add";
            }
            if (StringUtils.isEmpty((CharSequence)fileSafe.getId())) {
                this.fileSafeService.save(fileSafe, request);
                this.fileSafeService.saveLog(request, "\u6dfb\u52a0", fileSafe);
            } else {
                this.fileSafeService.updateById(fileSafe);
                this.fileSafeService.saveLog(request, "\u4fee\u6539", fileSafe);
            }
        }
        catch (Exception e) {
            logger.error(errorMsg, (Throwable)e);
            this.logInfoService.save(errorMsg, e.toString(), "2");
        }
        return "redirect:/fileSafe/list";
    }

    @RequestMapping(value={"saveBatch"})
    public String saveBatchFileSafe(FileSafe fileSafe, Model model, HttpServletRequest request) {
        String errorMsg = "\u6279\u91cf\u4fdd\u5b58\u6587\u4ef6\u9632\u7be1\u6539\u4fe1\u606f\u9519\u8bef";
        try {
            String[] hostnames = request.getParameterValues("hostnames");
            if (null == hostnames || hostnames.length < 1) {
                return "redirect:/fileSafe/list";
            }
            String blockKey = FormatUtil.haveBlockDanger(fileSafe.getCustomShell(), this.commonConfig.getShellToRunBlock());
            if (!StringUtils.isEmpty((CharSequence)blockKey)) {
                model.addAttribute("fileSafe", (Object)fileSafe);
                model.addAttribute("selectedHosts", (Object)hostnames);
                List<SystemInfo> systemInfoList = this.systemInfoService.selectAllByParams(new HashMap<String, Object>());
                for (SystemInfo systemInfo : systemInfoList) {
                    for (String selectedHost : hostnames) {
                        if (!selectedHost.equals(systemInfo.getHostname())) continue;
                        systemInfo.setSelected("selected");
                    }
                }
                model.addAttribute("systemInfoList", systemInfoList);
                model.addAttribute("msg", (Object)("\u81ea\u52a8\u6062\u590d\u6307\u4ee4\u542b\u6709\u654f\u611f\u5b57\u7b26" + blockKey + "\uff0c\u8bf7\u68c0\u67e5"));
                this.shellInfoService.getBlockStr(model);
                return "fileSafe/addBatch";
            }
            for (String selectedHost : hostnames) {
                fileSafe.setHostname(selectedHost);
                this.fileSafeService.save(fileSafe, request);
                this.fileSafeService.saveLog(request, "\u6dfb\u52a0", fileSafe);
            }
        }
        catch (Exception e) {
            logger.error(errorMsg, (Throwable)e);
            this.logInfoService.save(errorMsg, e.toString(), "2");
        }
        return "redirect:/fileSafe/list";
    }

    @RequestMapping(value={"edit"})
    public String edit(Model model, HttpServletRequest request) {
        String errorMsg = "\u6dfb\u52a0\u6587\u4ef6\u9632\u7be1\u6539\u4fe1\u606f\u9519\u8bef";
        String id = request.getParameter("id");
        FileSafe fileSafe = new FileSafe();
        try {
            HashMap<String, Object> paramsAccount = new HashMap<String, Object>();
            HostUtil.addAccountquery(request, paramsAccount);
            this.shellInfoService.getBlockStr(model);
            List<SystemInfo> systemInfoList = this.systemInfoService.selectAllByParams(paramsAccount);
            model.addAttribute("systemInfoList", systemInfoList);
            if (StringUtils.isEmpty((CharSequence)id)) {
                model.addAttribute("fileSafe", (Object)fileSafe);
                if (!this.isAddContinue()) {
                    return "redirect:/fileSafe/list?liceFlage=1";
                }
                return "fileSafe/add";
            }
            fileSafe = this.fileSafeService.selectById(id);
            model.addAttribute("fileSafe", (Object)fileSafe);
        }
        catch (Exception e) {
            logger.error(errorMsg, (Throwable)e);
            this.logInfoService.save(errorMsg, e.toString(), "2");
        }
        return "fileSafe/add";
    }

    @RequestMapping(value={"updateActive"})
    public String updateActive(Model model, HttpServletRequest request) {
        String errorMsg = "\u6279\u91cf\u5f00\u59cb\u76d1\u63a7\u548c\u505c\u6b62\u76d1\u63a7\u9519\u8bef";
        try {
            if (!StringUtils.isEmpty((CharSequence)request.getParameter("id"))) {
                String[] ids = request.getParameter("id").split(",");
                HashMap<String, Object> params = new HashMap<String, Object>();
                params.put("ids", ids);
                String activeValue = request.getParameter("active");
                params.put("active", activeValue);
                this.fileSafeService.updateActive(params);
            }
        }
        catch (Exception e) {
            logger.error(errorMsg, (Throwable)e);
            this.logInfoService.save(errorMsg, e.toString(), "2");
        }
        return "redirect:/fileSafe/list";
    }

    @RequestMapping(value={"editBatch"})
    public String editBatch(Model model, HttpServletRequest request) {
        String errorMsg = "\u6279\u91cf\u6dfb\u52a0\u6587\u4ef6\u9632\u7be1\u6539\u4fe1\u606f\u9519\u8bef";
        FileSafe fileSafe = new FileSafe();
        try {
            if (!StaticKeys.LICENSE_STATE.equals("1")) {
                return "redirect:/fileSafe/list?liceFlage=2";
            }
            HashMap<String, Object> paramsAccount = new HashMap<String, Object>();
            HostUtil.addAccountquery(request, paramsAccount);
            this.shellInfoService.getBlockStr(model);
            List<SystemInfo> systemInfoList = this.systemInfoService.selectAllByParams(paramsAccount);
            model.addAttribute("systemInfoList", systemInfoList);
            model.addAttribute("fileSafe", (Object)fileSafe);
        }
        catch (Exception e) {
            logger.error(errorMsg, (Throwable)e);
            this.logInfoService.save(errorMsg, e.toString(), "2");
        }
        return "fileSafe/addBatch";
    }

    @RequestMapping(value={"del"})
    public String delete(Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        String errorMsg = "\u5220\u9664\u6587\u4ef6\u9632\u7be1\u6539\u76d1\u6d4b\u4fe1\u606f\u9519\u8bef";
        FileSafe fileSafe = new FileSafe();
        try {
            if (!StringUtils.isEmpty((CharSequence)request.getParameter("id"))) {
                String[] ids;
                for (String id : ids = request.getParameter("id").split(",")) {
                    fileSafe = this.fileSafeService.selectById(id);
                    this.fileSafeService.saveLog(request, "\u5220\u9664", fileSafe);
                }
                this.fileSafeService.deleteById(ids);
            }
        }
        catch (Exception e) {
            logger.error(errorMsg, (Throwable)e);
            this.logInfoService.save(errorMsg, e.toString(), "2");
        }
        return "redirect:/fileSafe/list";
    }

    @RequestMapping(value={"view"})
    public String view(Model model, HttpServletRequest request) {
        String errorMsg = "\u67e5\u770b\u6587\u4ef6\u9632\u7be1\u6539\u76d1\u63a7\u8be6\u60c5";
        String id = request.getParameter("id");
        FileSafe fileSafe = new FileSafe();
        try {
            fileSafe = this.fileSafeService.selectById(id);
            fileSafe.setHostname(fileSafe.getHostname() + HostUtil.addRemark(fileSafe.getHostname()));
            model.addAttribute("fileSafe", (Object)fileSafe);
            if ("true".equals(this.commonConfig.getHostGroup())) {
                fileSafe.setGroupId(this.hostGroupService.returnGroupNames(fileSafe.getGroupId()));
            }
            model.addAttribute("fileSafeErrorMsg", (Object)this.messageErrorUtils.viewErrorMsgHandler(id));
            if (!StaticKeys.LICENSE_STATE.equals("1") && "1".equals(fileSafe.getFileIsDir())) {
                model.addAttribute("fileSafeErrorMsg", "\u5f53\u524d\u7248\u672c\u53ea\u652f\u6301\u76d1\u63a7\u6587\u4ef6\uff0c\u5347\u7ea7\u5230\u4e13\u4e1a\u7248\u540e\uff0c\u53ef\u4ee5\u652f\u6301\u76d1\u63a7\u6587\u4ef6\u5939\u9632\u7be1\u6539");
            }
            this.messageErrorUtils.getCallBackMsgForShell(model, id, fileSafe.getCustomShell());
        }
        catch (Exception e) {
            logger.error(errorMsg, (Throwable)e);
            this.logInfoService.save(errorMsg, e.toString(), "2");
        }
        return "fileSafe/view";
    }

    private boolean isAddContinue() {
        try {
            HashMap<String, Object> params;
            int dbSize;
            if (!StaticKeys.LICENSE_STATE.equals("1") && (dbSize = this.fileSafeService.countByParams(params = new HashMap<String, Object>())) >= 10) {
                return false;
            }
        }
        catch (Exception e) {
            logger.error("isAddContinue error", (Throwable)e);
        }
        return true;
    }
}

