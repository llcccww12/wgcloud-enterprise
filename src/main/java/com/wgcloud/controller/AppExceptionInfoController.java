/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.controller;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.github.pagehelper.PageInfo;
import com.wgcloud.config.CommonConfig;
import com.wgcloud.entity.AccountInfo;
import com.wgcloud.entity.AppExceptionInfo;
import com.wgcloud.entity.ShellInfo;
import com.wgcloud.service.AppExceptionInfoService;
import com.wgcloud.service.LogInfoService;
import com.wgcloud.service.ShellInfoService;
import com.wgcloud.util.FormatUtil;
import com.wgcloud.util.HostUtil;
import com.wgcloud.util.PageUtil;
import com.wgcloud.util.ResDataUtils;
import com.wgcloud.util.ThreadPoolUtil;
import com.wgcloud.util.TokenUtils;
import com.wgcloud.util.license.LicenseUtil;
import com.wgcloud.util.msg.WarnOtherUtil;
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
@RequestMapping(value={"/appExceptionInfo"})
public class AppExceptionInfoController {
    private static final Logger logger = LoggerFactory.getLogger(AppExceptionInfoController.class);
    @Resource
    private AppExceptionInfoService appExceptionInfoService;
    @Resource
    private LogInfoService logInfoService;
    @Resource
    private ShellInfoService shellInfoService;
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
        HashMap<String, Object> params = new HashMap<String, Object>();
        if (null == agentJsonObject.get("hostname") || StringUtils.isEmpty((CharSequence)agentJsonObject.get("hostname").toString())) {
            return "";
        }
        params.put("hostname", agentJsonObject.get("hostname").toString());
        try {
            List<AppExceptionInfo> appExceptionInfoList = this.appExceptionInfoService.selectAllByParams(params);
            return ResDataUtils.resetSuccessJson(appExceptionInfoList);
        }
        catch (Exception e) {
            logger.error("agent\u83b7\u53d6\u5f02\u5e38\u8fdb\u7a0b\u4fe1\u606f\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("agent\u83b7\u53d6\u5f02\u5e38\u8fdb\u7a0b\u4fe1\u606f\u9519\u8bef", e.toString(), "2");
            return ResDataUtils.resetErrorJson(e.toString());
        }
    }

    @RequestMapping(value={"list"})
    public String appExceptionInfoList(AppExceptionInfo appExceptionInfo, Model model, HttpServletRequest request) {
        HashMap<String, Object> params = new HashMap<String, Object>();
        try {
            LicenseUtil.maxLicense_10(model, request, appExceptionInfo);
            StringBuffer url = new StringBuffer();
            String hostname = null;
            if (!StringUtils.isEmpty((CharSequence)appExceptionInfo.getHostname())) {
                hostname = appExceptionInfo.getHostname();
                params.put("hostname", hostname.trim());
                url.append("&hostname=").append(hostname);
            }
            if (!StringUtils.isEmpty((CharSequence)appExceptionInfo.getAccount())) {
                params.put("account", appExceptionInfo.getAccount());
                url.append("&account=").append(appExceptionInfo.getAccount());
            }
            if (!StringUtils.isEmpty((CharSequence)appExceptionInfo.getOrderBy())) {
                params.put("orderBy", appExceptionInfo.getOrderBy());
                params.put("orderType", appExceptionInfo.getOrderType());
                url.append("&orderBy=").append(appExceptionInfo.getOrderBy());
                url.append("&orderType=").append(appExceptionInfo.getOrderType());
            }
            HostUtil.addAccountquery(request, params);
            PageInfo pageInfo = this.appExceptionInfoService.selectByParams(params, appExceptionInfo.getPage(), appExceptionInfo.getPageSize());
            for (AppExceptionInfo appExceptionInfo1 : (java.util.List<AppExceptionInfo>) pageInfo.getList()) {
                appExceptionInfo1.setWritesBytes(FormatUtil.mToG(appExceptionInfo1.getWritesBytes()));
                appExceptionInfo1.setReadBytes(FormatUtil.mToG(appExceptionInfo1.getReadBytes()));
                if ("true".equals(this.commonConfig.getUserInfoManage())) {
                    appExceptionInfo1.setAccount(HostUtil.getAccount(appExceptionInfo1.getHostname()));
                }
                appExceptionInfo1.setHostState(HostUtil.getHostState(appExceptionInfo1.getHostname()));
                appExceptionInfo1.setHostname(appExceptionInfo1.getHostname() + HostUtil.addRemark(appExceptionInfo1.getHostname()));
            }
            HostUtil.addAccountListModel(model);
            PageUtil.initPageNumber(pageInfo, model);
            model.addAttribute("pageUrl", (Object)("/appExceptionInfo/list?1=1" + url.toString()));
            model.addAttribute("page", (Object)pageInfo);
            model.addAttribute("appExceptionInfo", (Object)appExceptionInfo);
        }
        catch (Exception e) {
            logger.error("\u67e5\u8be2\u5f02\u5e38\u8fdb\u7a0b\u4fe1\u606f\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("\u67e5\u8be2\u5f02\u5e38\u8fdb\u7a0b\u4fe1\u606f\u9519\u8bef", e.toString(), "2");
        }
        return "appException/list";
    }

    @ResponseBody
    @RequestMapping(value={"cancelProcessAjax"})
    public String cancelProcessAjax(HttpServletRequest request) {
        try {
            String id = request.getParameter("id");
            if (!StringUtils.isEmpty((CharSequence)id)) {
                AppExceptionInfo appExceptionInfo = this.appExceptionInfoService.selectById(id);
                appExceptionInfo.setState("2");
                this.appExceptionInfoService.updateById(appExceptionInfo);
                ShellInfo shellInfo = new ShellInfo();
                shellInfo.setShellName("\u624b\u52a8\u7ed3\u675f\u5f02\u5e38\u8fdb\u7a0b\uff1a" + appExceptionInfo.getHostname() + HostUtil.addRemark(appExceptionInfo.getHostname()) + "\uff0c" + appExceptionInfo.getAppName() + "\uff0c" + appExceptionInfo.getGatherPid());
                shellInfo.setShellTime("");
                shellInfo.setShell("cancelProcessAjax:" + appExceptionInfo.getGatherPid());
                shellInfo.setShellType("1");
                AccountInfo accountInfo = HostUtil.getAccountByRequest(request);
                if (!"admin".equals(accountInfo.getRole())) {
                    shellInfo.setAccount(accountInfo.getAccount());
                }
                String[] hostname = new String[]{appExceptionInfo.getHostname()};
                this.shellInfoService.save(shellInfo, hostname, request);
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
                this.logInfoService.save(HostUtil.getAccountByRequest(request).getAccount() + "\u624b\u52a8\u7ed3\u675f\u5f02\u5e38\u8fdb\u7a0b\uff1a" + appExceptionInfo.getHostname() + HostUtil.addRemark(appExceptionInfo.getHostname()) + "\uff0c" + appExceptionInfo.getAppName(), "\u8fdb\u7a0bID\uff1a" + appExceptionInfo.getGatherPid(), "2");
            }
        }
        catch (Exception e) {
            logger.error("\u7ed3\u675f\u5f02\u5e38\u8fdb\u7a0b\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("\u7ed3\u675f\u5f02\u5e38\u8fdb\u7a0b\u9519\u8bef", e.toString(), "2");
        }
        return "success";
    }

    @RequestMapping(value={"del"})
    public String delete(Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        String errorMsg = "\u5220\u9664\u5f02\u5e38\u8fdb\u7a0b\u4fe1\u606f\u9519\u8bef";
        AppExceptionInfo appExceptionInfo = new AppExceptionInfo();
        try {
            if (!StringUtils.isEmpty((CharSequence)request.getParameter("id"))) {
                String[] ids;
                for (String id : ids = request.getParameter("id").split(",")) {
                    appExceptionInfo = this.appExceptionInfoService.selectById(id);
                    this.appExceptionInfoService.saveLog(request, "\u5220\u9664", appExceptionInfo);
                }
                this.appExceptionInfoService.deleteById(request.getParameter("id").split(","));
            }
        }
        catch (Exception e) {
            logger.error(errorMsg, (Throwable)e);
            this.logInfoService.save(errorMsg, e.toString(), "2");
        }
        return "redirect:/appExceptionInfo/list";
    }

    @RequestMapping(value={"view"})
    public String viewChart(Model model, HttpServletRequest request) {
        String errorMsg = "\u67e5\u770b\u5f02\u5e38\u8fdb\u7a0b\u4fe1\u606f\u9519\u8bef";
        String id = request.getParameter("id");
        AppExceptionInfo appExceptionInfo = new AppExceptionInfo();
        try {
            appExceptionInfo = this.appExceptionInfoService.selectById(id);
            appExceptionInfo.setHostname(appExceptionInfo.getHostname() + HostUtil.addRemark(appExceptionInfo.getHostname()));
            appExceptionInfo.setWritesBytes(FormatUtil.mToG(appExceptionInfo.getWritesBytes()));
            appExceptionInfo.setReadBytes(FormatUtil.mToG(appExceptionInfo.getReadBytes()));
            model.addAttribute("appExceptionInfo", (Object)appExceptionInfo);
        }
        catch (Exception e) {
            logger.error(errorMsg, (Throwable)e);
            this.logInfoService.save(errorMsg, e.toString(), "2");
        }
        return "appException/view";
    }
}

