/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.controller;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.github.pagehelper.PageInfo;
import com.wgcloud.config.CommonConfig;
import com.wgcloud.entity.PortInfo;
import com.wgcloud.entity.SystemInfo;
import com.wgcloud.service.HostGroupService;
import com.wgcloud.service.LogInfoService;
import com.wgcloud.service.PortInfoService;
import com.wgcloud.service.SystemInfoService;
import com.wgcloud.util.HostUtil;
import com.wgcloud.util.MessageErrorUtils;
import com.wgcloud.util.PageUtil;
import com.wgcloud.util.ResDataUtils;
import com.wgcloud.util.TokenUtils;
import com.wgcloud.util.license.LicenseUtil;
import com.wgcloud.util.staticvar.StaticKeys;
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
@RequestMapping(value={"/portInfo"})
public class PortInfoController {
    private static final Logger logger = LoggerFactory.getLogger(PortInfoController.class);
    @Resource
    private PortInfoService portInfoService;
    @Resource
    private LogInfoService logInfoService;
    @Resource
    private SystemInfoService systemInfoService;
    @Resource
    private HostGroupService hostGroupService;
    @Autowired
    private CommonConfig commonConfig;
    @Autowired
    private TokenUtils tokenUtils;
    @Autowired
    private MessageErrorUtils messageErrorUtils;

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
        params.put("hostnameForAgent", agentJsonObject.get("hostname").toString());
        try {
            params.put("active", "1");
            List<PortInfo> portInfoList = this.portInfoService.selectAllByParams(params);
            return ResDataUtils.resetSuccessJson(portInfoList);
        }
        catch (Exception e) {
            logger.error("agent\u83b7\u53d6\u7aef\u53e3\u4fe1\u606f\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("agent\u83b7\u53d6\u7aef\u53e3\u4fe1\u606f\u9519\u8bef", e.toString(), "2");
            return ResDataUtils.resetErrorJson(e.toString());
        }
    }

    @RequestMapping(value={"list"})
    public String portInfoList(PortInfo portInfo, Model model, HttpServletRequest request) {
        HashMap<String, Object> params = new HashMap<String, Object>();
        try {
            LicenseUtil.maxLicense_10(model, request, portInfo);
            StringBuffer url = new StringBuffer();
            String hostname = null;
            if (!StringUtils.isEmpty((CharSequence)portInfo.getHostname())) {
                hostname = portInfo.getHostname();
                params.put("hostname", hostname.trim());
                url.append("&hostname=").append(hostname);
            }
            if (!StringUtils.isEmpty((CharSequence)portInfo.getAccount())) {
                params.put("account", portInfo.getAccount());
                url.append("&account=").append(portInfo.getAccount());
            }
            if (!StringUtils.isEmpty((CharSequence)portInfo.getState())) {
                params.put("state", portInfo.getState());
                url.append("&state=").append(portInfo.getState());
            }
            if (!StringUtils.isEmpty((CharSequence)portInfo.getGroupId())) {
                params.put("groupId", portInfo.getGroupId());
                url.append("&groupId=").append(portInfo.getGroupId());
            }
            if (!StringUtils.isEmpty((CharSequence)portInfo.getActive())) {
                params.put("active", portInfo.getActive());
                url.append("&active=").append(portInfo.getActive());
            }
            if (!StringUtils.isEmpty((CharSequence)portInfo.getOrderBy())) {
                params.put("orderBy", portInfo.getOrderBy());
                params.put("orderType", portInfo.getOrderType());
                url.append("&orderBy=").append(portInfo.getOrderBy());
                url.append("&orderType=").append(portInfo.getOrderType());
            }
            HostUtil.addAccountquery(request, params);
            PageInfo pageInfo = this.portInfoService.selectByParams(params, portInfo.getPage(), portInfo.getPageSize());
            for (PortInfo portInfo1 : (java.util.List<PortInfo>) pageInfo.getList()) {
                if ("true".equals(this.commonConfig.getUserInfoManage())) {
                    portInfo1.setAccount(HostUtil.getAccount(portInfo1.getHostname()));
                }
                portInfo1.setHostState(HostUtil.getHostState(portInfo1.getHostname()));
                portInfo1.setHostname(portInfo1.getHostname() + HostUtil.addRemark(portInfo1.getHostname()));
                if (!"true".equals(this.commonConfig.getShowWarnCount())) continue;
                String warnQueryWd = portInfo1.getPortName() + "\uff0ctelnet-" + portInfo1.getTelnetIp() + "-" + portInfo1.getPort() + "\uff0c" + portInfo1.getHostname();
                this.logInfoService.warnQueryHandle(portInfo1, warnQueryWd);
            }
            if ("true".equals(this.commonConfig.getHostGroup())) {
                this.portInfoService.setGroupInList(pageInfo.getList(), model, request);
            }
            HostUtil.addAccountListModel(model);
            PageUtil.initPageNumber(pageInfo, model);
            model.addAttribute("pageUrl", (Object)("/portInfo/list?1=1" + url.toString()));
            model.addAttribute("page", (Object)pageInfo);
            model.addAttribute("portInfo", (Object)portInfo);
        }
        catch (Exception e) {
            logger.error("\u67e5\u8be2\u7aef\u53e3\u4fe1\u606f\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("\u67e5\u8be2\u7aef\u53e3\u4fe1\u606f\u9519\u8bef", e.toString(), "2");
        }
        return "port/list";
    }

    @ResponseBody
    @RequestMapping(value={"saveGroupId"})
    public String saveGroupId(Model model, HttpServletRequest request) {
        try {
            String ids = request.getParameter("ids");
            String[] groupIdsArr = request.getParameterValues("groupId");
            this.portInfoService.saveGroupId(ids, groupIdsArr, request);
        }
        catch (Exception e) {
            logger.error("\u4fdd\u5b58\u7aef\u53e3\u6807\u7b7e\u4fe1\u606f\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("\u4fdd\u5b58\u7aef\u53e3\u6807\u7b7e\u4fe1\u606f\u9519\u8bef", e.toString(), "2");
        }
        return "redirect:/portInfo/list";
    }

    @RequestMapping(value={"save"})
    public String savePortInfo(PortInfo portInfo, Model model, HttpServletRequest request) {
        String errorMsg = "\u4fdd\u5b58\u7aef\u53e3\u4fe1\u606f\u9519\u8bef";
        try {
            if (StringUtils.isEmpty((CharSequence)portInfo.getId())) {
                this.portInfoService.save(portInfo, request);
                this.portInfoService.saveLog(request, "\u6dfb\u52a0", portInfo);
            } else {
                this.portInfoService.updateById(portInfo);
                this.portInfoService.saveLog(request, "\u4fee\u6539", portInfo);
            }
        }
        catch (Exception e) {
            logger.error(errorMsg, (Throwable)e);
            this.logInfoService.save(errorMsg, e.toString(), "2");
        }
        return "redirect:/portInfo/list";
    }

    @RequestMapping(value={"saveBatch"})
    public String saveBatchPortInfo(PortInfo portInfo, Model model, HttpServletRequest request) {
        String errorMsg = "\u6279\u91cf\u4fdd\u5b58\u7aef\u53e3\u4fe1\u606f\u9519\u8bef";
        try {
            String[] hostnames = request.getParameterValues("hostnames");
            if (null == hostnames || hostnames.length < 1) {
                return "redirect:/portInfo/list";
            }
            for (String selectedHost : hostnames) {
                portInfo.setHostname(selectedHost);
                this.portInfoService.save(portInfo, request);
                this.portInfoService.saveLog(request, "\u6dfb\u52a0", portInfo);
            }
        }
        catch (Exception e) {
            logger.error(errorMsg, (Throwable)e);
            this.logInfoService.save(errorMsg, e.toString(), "2");
        }
        return "redirect:/portInfo/list";
    }

    @RequestMapping(value={"edit"})
    public String edit(Model model, HttpServletRequest request) {
        String errorMsg = "\u6dfb\u52a0\u7aef\u53e3\u4fe1\u606f\u9519\u8bef";
        String id = request.getParameter("id");
        PortInfo PortInfo2 = new PortInfo();
        try {
            HashMap<String, Object> params = new HashMap<String, Object>();
            HostUtil.addAccountquery(request, params);
            List<SystemInfo> systemInfoList = this.systemInfoService.selectAllByParams(params);
            model.addAttribute("systemInfoList", systemInfoList);
            if (StringUtils.isEmpty((CharSequence)id)) {
                model.addAttribute("portInfo", (Object)PortInfo2);
                if (!this.isAddContinue()) {
                    return "redirect:/portInfo/list?liceFlage=1";
                }
                return "port/add";
            }
            PortInfo2 = this.portInfoService.selectById(id);
            model.addAttribute("portInfo", (Object)PortInfo2);
        }
        catch (Exception e) {
            logger.error(errorMsg, (Throwable)e);
            this.logInfoService.save(errorMsg, e.toString(), "2");
        }
        return "port/add";
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
                this.portInfoService.updateActive(params);
            }
        }
        catch (Exception e) {
            logger.error(errorMsg, (Throwable)e);
            this.logInfoService.save(errorMsg, e.toString(), "2");
        }
        return "redirect:/portInfo/list";
    }

    @RequestMapping(value={"editBatch"})
    public String editBatch(Model model, HttpServletRequest request) {
        String errorMsg = "\u6279\u91cf\u6dfb\u52a0\u7aef\u53e3\u4fe1\u606f\u9519\u8bef";
        PortInfo portInfo = new PortInfo();
        try {
            if (!StaticKeys.LICENSE_STATE.equals("1")) {
                return "redirect:/portInfo/list?liceFlage=2";
            }
            HashMap<String, Object> params = new HashMap<String, Object>();
            HostUtil.addAccountquery(request, params);
            List<SystemInfo> systemInfoList = this.systemInfoService.selectAllByParams(params);
            model.addAttribute("systemInfoList", systemInfoList);
            model.addAttribute("portInfo", (Object)portInfo);
        }
        catch (Exception e) {
            logger.error(errorMsg, (Throwable)e);
            this.logInfoService.save(errorMsg, e.toString(), "2");
        }
        return "port/addBatch";
    }

    @RequestMapping(value={"del"})
    public String delete(Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        String errorMsg = "\u5220\u9664\u7aef\u53e3\u4fe1\u606f\u9519\u8bef";
        PortInfo portInfo = new PortInfo();
        try {
            if (!StringUtils.isEmpty((CharSequence)request.getParameter("id"))) {
                String[] ids;
                for (String id : ids = request.getParameter("id").split(",")) {
                    portInfo = this.portInfoService.selectById(id);
                    this.portInfoService.saveLog(request, "\u5220\u9664", portInfo);
                }
                this.portInfoService.deleteById(ids);
            }
        }
        catch (Exception e) {
            logger.error(errorMsg, (Throwable)e);
            this.logInfoService.save(errorMsg, e.toString(), "2");
        }
        return "redirect:/portInfo/list";
    }

    @RequestMapping(value={"view"})
    public String view(Model model, HttpServletRequest request) {
        String errorMsg = "\u67e5\u770b\u7aef\u53e3\u4fe1\u606f\u9519\u8bef";
        String id = request.getParameter("id");
        PortInfo portInfo = new PortInfo();
        try {
            portInfo = this.portInfoService.selectById(id);
            portInfo.setHostname(portInfo.getHostname() + HostUtil.addRemark(portInfo.getHostname()));
            model.addAttribute("portInfo", (Object)portInfo);
            if ("true".equals(this.commonConfig.getHostGroup())) {
                portInfo.setGroupId(this.hostGroupService.returnGroupNames(portInfo.getGroupId()));
            }
            if ("2".equals(portInfo.getState())) {
                model.addAttribute("portErrorMsg", (Object)this.messageErrorUtils.viewErrorMsgHandler(id));
            } else {
                model.addAttribute("portErrorMsg", "success");
            }
        }
        catch (Exception e) {
            logger.error(errorMsg, (Throwable)e);
            this.logInfoService.save(errorMsg, e.toString(), "2");
        }
        return "port/view";
    }

    private boolean isAddContinue() {
        try {
            HashMap<String, Object> params;
            int dbSize;
            if (!StaticKeys.LICENSE_STATE.equals("1") && (dbSize = this.portInfoService.countByParams(params = new HashMap<String, Object>())) >= 10) {
                return false;
            }
        }
        catch (Exception e) {
            logger.error("isAddContinue error", (Throwable)e);
        }
        return true;
    }
}

