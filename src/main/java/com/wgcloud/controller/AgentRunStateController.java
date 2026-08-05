/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.controller;

import com.github.pagehelper.PageInfo;
import com.wgcloud.config.CommonConfig;
import com.wgcloud.entity.AccountInfo;
import com.wgcloud.entity.AgentRunState;
import com.wgcloud.entity.ShellInfo;
import com.wgcloud.entity.SystemInfo;
import com.wgcloud.service.AgentRunStateService;
import com.wgcloud.service.HostGroupService;
import com.wgcloud.service.LogInfoService;
import com.wgcloud.service.ShellInfoService;
import com.wgcloud.service.SystemInfoService;
import com.wgcloud.util.FormatUtil;
import com.wgcloud.util.HostUtil;
import com.wgcloud.util.MD5Utils;
import com.wgcloud.util.PageUtil;
import com.wgcloud.util.TokenUtils;
import com.wgcloud.util.license.LicenseUtil;
import com.wgcloud.util.staticvar.StaticKeys;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value={"/agentRunState"})
public class AgentRunStateController {
    private static final Logger logger = LoggerFactory.getLogger(AgentRunStateController.class);
    @Resource
    private AgentRunStateService agentRunStateService;
    @Resource
    private LogInfoService logInfoService;
    @Resource
    private SystemInfoService systemInfoService;
    @Resource
    private HostGroupService hostGroupService;
    @Resource
    private ShellInfoService shellInfoService;
    @Autowired
    private CommonConfig commonConfig;
    @Autowired
    private TokenUtils tokenUtils;

    @RequestMapping(value={"list"})
    public String agentRunStateList(AgentRunState agentRunState, Model model, HttpServletRequest request) {
        HashMap<String, Object> params = new HashMap<String, Object>();
        try {
            LicenseUtil.maxLicense_10(model, request, agentRunState);
            if (!StringUtils.isEmpty((CharSequence)request.getParameter(StaticKeys.LICENSE_LICE_FLAGE))) {
                model.addAttribute("msg", "\u6b64\u529f\u80fd\u9700\u5347\u7ea7\u5230\u4f01\u4e1a\u7248\uff0c\u8bf7\u70b9\u51fb\u9875\u9762\u5e95\u90e8\u7f51\u7ad9\u8054\u7cfb\u6211\u4eec");
            }
            StringBuffer url = new StringBuffer();
            String hostname = null;
            if (!StringUtils.isEmpty((CharSequence)agentRunState.getHostname())) {
                hostname = agentRunState.getHostname();
                params.put("hostname", hostname.trim());
                url.append("&hostname=").append(hostname);
            }
            if (!StringUtils.isEmpty((CharSequence)agentRunState.getAccount())) {
                params.put("account", agentRunState.getAccount());
                url.append("&account=").append(agentRunState.getAccount());
            }
            if (!StringUtils.isEmpty((CharSequence)agentRunState.getState())) {
                params.put("state", agentRunState.getState());
                url.append("&state=").append(agentRunState.getState());
            }
            if (!StringUtils.isEmpty((CharSequence)agentRunState.getGroupId())) {
                params.put("groupId", agentRunState.getGroupId());
                url.append("&groupId=").append(agentRunState.getGroupId());
            }
            if (!StringUtils.isEmpty((CharSequence)agentRunState.getOrderBy())) {
                params.put("orderBy", agentRunState.getOrderBy());
                params.put("orderType", agentRunState.getOrderType());
                url.append("&orderBy=").append(agentRunState.getOrderBy());
                url.append("&orderType=").append(agentRunState.getOrderType());
            }
            HostUtil.addAccountquery(request, params);
            PageInfo pageInfo = this.agentRunStateService.selectByParams(params, agentRunState.getPage(), agentRunState.getPageSize());
            this.agentRunStateService.setSystemInfoExtForList(pageInfo.getList());
            for (AgentRunState agentRunState1 : (java.util.List<AgentRunState>) pageInfo.getList()) {
                agentRunState1.setHostname(agentRunState1.getHostname() + HostUtil.addRemark(agentRunState1.getHostname()));
                agentRunState1.setOnlineTimeStr(FormatUtil.formatDouble((double)agentRunState1.getOnlineTime().intValue() / 24.0, 2) + "\u5929");
                agentRunState1.setDownTimeStr(FormatUtil.formatDouble((double)agentRunState1.getDownTime().intValue() / 24.0, 2) + "\u5929");
            }
            if ("true".equals(this.commonConfig.getHostGroup())) {
                this.agentRunStateService.setGroupInList(pageInfo.getList(), model, request);
            }
            HostUtil.addAccountListModel(model);
            PageUtil.initPageNumber(pageInfo, model);
            model.addAttribute("pageUrl", (Object)("/agentRunState/list?1=1" + url.toString()));
            model.addAttribute("page", (Object)pageInfo);
            model.addAttribute("agentRunState", (Object)agentRunState);
            if (!StringUtils.isEmpty((CharSequence)request.getParameter("msg"))) {
                model.addAttribute("msgModifyAgentConfig", "\u6279\u91cf\u4fee\u6539agent\u53c2\u6570\u6210\u529f\uff0c\u5927\u6982\u9700\u89815\u5206\u949f\u5168\u90e8\u4fee\u6539\u5b8c\u6210");
            }
        }
        catch (Exception e) {
            logger.error("\u67e5\u8be2\u7ec8\u7aef\u8fd0\u884c\u4fe1\u606f\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("\u67e5\u8be2\u7ec8\u7aef\u8fd0\u884c\u4fe1\u606f\u9519\u8bef", e.toString(), "2");
        }
        return "agentRunState/list";
    }

    @RequestMapping(value={"del"})
    public String delete(Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        String errorMsg = "\u5220\u9664\u7ec8\u7aefagent\u8fd0\u884c\u4fe1\u606f\u9519\u8bef";
        AgentRunState agentRunState = new AgentRunState();
        try {
            if (!StringUtils.isEmpty((CharSequence)request.getParameter("id"))) {
                String[] ids;
                for (String id : ids = request.getParameter("id").split(",")) {
                    agentRunState = this.agentRunStateService.selectById(id);
                    this.agentRunStateService.saveLog(request, "\u5220\u9664", agentRunState);
                }
                this.agentRunStateService.deleteById(ids);
            }
        }
        catch (Exception e) {
            logger.error(errorMsg, (Throwable)e);
            this.logInfoService.save(errorMsg, e.toString(), "2");
        }
        return "redirect:/agentRunState/list";
    }

    @RequestMapping(value={"exportListExcel"})
    public void exportListExcel(Model model, HttpServletRequest request, HttpServletResponse response) {
        try {
            if (!StaticKeys.LICENSE_STATE.equals("1")) {
                response.setContentType("text/html;charset=UTF-8");
                response.getOutputStream().write("The module needs to professional version. Please contact us at www.wgstart.com".getBytes());
                return;
            }
            this.agentRunStateService.exportListExcel(request, response);
        }
        catch (Exception e) {
            logger.error("\u6240\u6709agent\u7ec8\u7aef\u8fd0\u884c\u6570\u636e\u5bfc\u51faexcel\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("\u6240\u6709agent\u7ec8\u7aef\u8fd0\u884c\u6570\u636e\u5bfc\u51faexcel\u9519\u8bef", e.toString(), "2");
        }
    }

    @ResponseBody
    @RequestMapping(value={"saveAgentBindIp"})
    public String saveAgentBindIp(Model model, HttpServletRequest request) {
        try {
            AccountInfo accountInfoSession = HostUtil.getAccountByRequest(request);
            if ("guest".equals(accountInfoSession.getRole())) {
                logger.error("\u53ea\u8bfb\u8d26\u53f7\u4e0d\u80fd\u8bbe\u7f6e\u4e3b\u673abindIp");
                return "error/500";
            }
            String bindIp = request.getParameter("bindIp");
            String id = request.getParameter("id");
            if (!StringUtils.isEmpty((CharSequence)id) && !StringUtils.isEmpty((CharSequence)bindIp)) {
                bindIp = bindIp.trim();
                AgentRunState agentRunStateOld = this.agentRunStateService.selectById(id);
                if (agentRunStateOld == null) {
                    return "";
                }
                AgentRunState agentRunState = new AgentRunState();
                agentRunState.setId(id);
                agentRunState.setHostname(bindIp);
                this.agentRunStateService.updateById(agentRunState);
                ShellInfo shellInfo = new ShellInfo();
                shellInfo.setShellName("\u4e0b\u53d1\u6307\u4ee4\u4fee\u6539agent\u914d\u7f6e\u53c2\u6570bindIp\uff1a\u539f\u53c2\u6570\u503c" + agentRunStateOld.getHostname() + HostUtil.addRemark(agentRunStateOld.getHostname()) + "\uff0c\u65b0\u53c2\u6570\u503c\uff1a" + bindIp);
                shellInfo.setShellTime("");
                shellInfo.setShell("setBindIpAjax:" + bindIp);
                shellInfo.setShellType("1");
                AccountInfo accountInfo = HostUtil.getAccountByRequest(request);
                if (!"admin".equals(accountInfo.getRole())) {
                    shellInfo.setAccount(accountInfo.getAccount());
                }
                String[] hostname = new String[]{agentRunStateOld.getHostname()};
                this.shellInfoService.save(shellInfo, hostname, request);
                this.logInfoService.save(HostUtil.getAccountByRequest(request).getAccount() + "\u4fee\u6539\u4e3b\u673abindIp\uff1a" + agentRunStateOld.getHostname(), "\u4fee\u6539\u540e\u7684\u4e3b\u673abindIp\uff1a" + bindIp, "2");
            }
        }
        catch (Exception e) {
            logger.error("\u4fdd\u5b58\u4e3b\u673a\u7684\u65b0bindIp\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("\u4fdd\u5b58\u4e3b\u673a\u7684\u65b0bindIp\u9519\u8bef", e.toString(), "2");
        }
        return "redirect:/systemInfo/systemInfoList";
    }

    @RequestMapping(value={"toModifyAgentPage"})
    public String toModifyAgentPage(Model model, HttpServletRequest request) {
        if (!LicenseUtil.checkEnterpriseVersion()) {
            return "redirect:/agentRunState/list?liceFlage=3";
        }
        return "agentRunState/agentConfig";
    }

    @RequestMapping(value={"modifyAgentConfig"})
    public String modifyAgentConfig(Model model, HttpServletRequest request) {
        try {
            AccountInfo accountInfoSession = HostUtil.getAccountByRequest(request);
            if (!"admin".equals(accountInfoSession.getRole())) {
                logger.error("\u53ea\u6709\u7ba1\u7406\u5458\u80fd\u6279\u91cf\u4fee\u6539\u6240\u6709Agent\u7ec8\u7aef\u914d\u7f6e\u53c2\u6570");
                return "error/500";
            }
            String serverUrl = request.getParameter("serverUrl");
            String wgToken = request.getParameter("wgToken");
            String account = request.getParameter("account");
            String passwd = request.getParameter("md5pwd");
            if (!MD5Utils.GetMD5Code(this.commonConfig.getAccountPwd()).equals(passwd) || !this.commonConfig.getAccount().equals(account)) {
                model.addAttribute("serverUrl", (Object)serverUrl);
                model.addAttribute("wgToken", (Object)wgToken);
                model.addAttribute("errorMsg", "\u7ba1\u7406\u5458\u8d26\u53f7\u6216\u8005\u5bc6\u7801\u9519\u8bef");
                return "agentRunState/agentConfig";
            }
            String shellStr = "";
            if (!StringUtils.isEmpty((CharSequence)serverUrl)) {
                serverUrl = serverUrl.trim();
                shellStr = "setServerUrlAjax:" + serverUrl;
            }
            if (!StringUtils.isEmpty((CharSequence)wgToken)) {
                wgToken = wgToken.trim();
                if (!StringUtils.isEmpty((CharSequence)shellStr)) {
                    shellStr = shellStr + ",";
                }
                shellStr = shellStr + "setWgTokenAjax:" + wgToken;
            }
            ShellInfo shellInfo = new ShellInfo();
            shellInfo.setShellName("\u4e0b\u53d1\u6307\u4ee4\u6279\u91cf\u4fee\u6539\u6240\u6709agent\u914d\u7f6e\u53c2\u6570serverUrl\u548cwgToken");
            shellInfo.setShellTime("");
            shellInfo.setShell(shellStr);
            shellInfo.setShellType("1");
            AccountInfo accountInfo = HostUtil.getAccountByRequest(request);
            if (!"admin".equals(accountInfo.getRole())) {
                shellInfo.setAccount(accountInfo.getAccount());
            }
            HashMap<String, Object> params = new HashMap<String, Object>();
            List<SystemInfo> systemInfoList = this.systemInfoService.selectAllByParams(params);
            ArrayList<String> hostnameList = new ArrayList<String>();
            for (SystemInfo systemInfo : systemInfoList) {
                hostnameList.add(systemInfo.getHostname());
            }
            String[] hostname = hostnameList.toArray(new String[0]);
            this.shellInfoService.save(shellInfo, hostname, request);
            this.logInfoService.save(HostUtil.getAccountByRequest(request).getAccount() + "\u6279\u91cf\u4fee\u6539Agent\u7ec8\u7aef\u914d\u7f6e\u53c2\u6570", "\u6279\u91cf\u4fee\u6539\u6240\u6709Agent\u7ec8\u7aef\u914d\u7f6e\u53c2\u6570", "2");
        }
        catch (Exception e) {
            logger.error("\u6279\u91cf\u4fee\u6539\u6240\u6709Agent\u7ec8\u7aef\u914d\u7f6e\u53c2\u6570\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("\u6279\u91cf\u4fee\u6539\u6240\u6709Agent\u7ec8\u7aef\u914d\u7f6e\u53c2\u6570\u9519\u8bef", e.toString(), "2");
        }
        return "redirect:/agentRunState/list?msg=1";
    }
}

