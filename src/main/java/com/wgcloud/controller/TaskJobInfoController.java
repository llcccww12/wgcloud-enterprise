/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.controller;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.github.pagehelper.PageInfo;
import com.wgcloud.config.CommonConfig;
import com.wgcloud.entity.SystemInfo;
import com.wgcloud.entity.TaskJobInfo;
import com.wgcloud.service.HostGroupService;
import com.wgcloud.service.LogInfoService;
import com.wgcloud.service.ShellInfoService;
import com.wgcloud.service.SystemInfoService;
import com.wgcloud.service.TaskJobInfoService;
import com.wgcloud.util.FormatUtil;
import com.wgcloud.util.HostUtil;
import com.wgcloud.util.MessageErrorUtils;
import com.wgcloud.util.PageUtil;
import com.wgcloud.util.ResDataUtils;
import com.wgcloud.util.TokenUtils;
import com.wgcloud.util.license.LicenseUtil;
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
@RequestMapping(value={"/taskJobInfo"})
public class TaskJobInfoController {
    private static final Logger logger = LoggerFactory.getLogger(TaskJobInfoController.class);
    @Resource
    private TaskJobInfoService taskJobInfoService;
    @Resource
    private LogInfoService logInfoService;
    @Resource
    private SystemInfoService systemInfoService;
    @Resource
    private HostGroupService hostGroupService;
    @Resource
    private ShellInfoService shellInfoService;
    @Autowired
    private MessageErrorUtils messageErrorUtils;
    @Autowired
    private CommonConfig commonConfig;
    @Autowired
    private TokenUtils tokenUtils;

    @ResponseBody
    @RequestMapping(value={"agentList"})
    public String agentList(@RequestBody String paramBean) {
        JSONObject agentJsonObject = (JSONObject)JSONUtil.parse((Object)paramBean);
        if (!this.tokenUtils.checkAgentToken(agentJsonObject)) {
            logger.error("Token is error");
            return ResDataUtils.resetErrorJson("Token is error");
        }
        if (!this.isAddContinue()) {
            ArrayList taskJobInfoList = new ArrayList();
            return ResDataUtils.resetSuccessJson(taskJobInfoList);
        }
        HashMap<String, Object> params = new HashMap<String, Object>();
        if (null == agentJsonObject.get("hostname") || StringUtils.isEmpty((CharSequence)agentJsonObject.get("hostname").toString())) {
            return "";
        }
        params.put("hostnameForAgent", agentJsonObject.get("hostname").toString());
        try {
            params.put("active", "1");
            List<TaskJobInfo> taskJobInfoList = this.taskJobInfoService.selectAllByParams(params);
            String blockKey = "";
            ArrayList<TaskJobInfo> taskJobInfoListResult = new ArrayList<TaskJobInfo>();
            for (TaskJobInfo taskJobInfo : taskJobInfoList) {
                blockKey = FormatUtil.haveBlockDanger(taskJobInfo.getShell(), this.commonConfig.getShellToRunBlock());
                if (!StringUtils.isEmpty((CharSequence)blockKey)) {
                    logger.info(taskJobInfo.getShell() + "\u4e0b\u53d1\u6307\u4ee4\u542b\u6709\u654f\u611f\u5b57\u7b26" + blockKey + "\uff0c\u4e0d\u8fdb\u884c\u4e0b\u53d1");
                    continue;
                }
                taskJobInfo.setShell(taskJobInfo.getShell().replaceAll("\\r\\n", " && "));
                taskJobInfoListResult.add(taskJobInfo);
                if (!"\u51c6\u5907\u4e2d".equals(taskJobInfo.getCallBackState())) continue;
                this.taskJobInfoService.updateSendByIds(taskJobInfo.getId().split(","));
            }
            return ResDataUtils.resetSuccessJson(taskJobInfoList);
        }
        catch (Exception e) {
            logger.error("agent\u83b7\u53d6\u8ba1\u5212\u4efb\u52a1\u4fe1\u606f\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("agent\u83b7\u53d6\u8ba1\u5212\u4efb\u52a1\u4fe1\u606f\u9519\u8bef", e.toString(), "2");
            return ResDataUtils.resetErrorJson(e.toString());
        }
    }

    @ResponseBody
    @RequestMapping(value={"/jobCallback"})
    public String jobCallback(@RequestBody String paramBean) {
        JSONObject agentJsonObject = (JSONObject)JSONUtil.parse((Object)paramBean);
        logger.info("jobCallback-------------" + agentJsonObject.toString());
        if (!this.tokenUtils.checkAgentToken(agentJsonObject)) {
            logger.error("Token is error");
            return ResDataUtils.resetErrorJson("Token is error");
        }
        if (!this.isAddContinue()) {
            logger.error("\u53ea\u6709\u4f01\u4e1a\u7248\u624d\u80fd\u56de\u8c03\u6b64\u5904\u8ba1\u5212\u4efb\u52a1");
            return ResDataUtils.resetErrorJson(null);
        }
        try {
            String taskJobId = agentJsonObject.getStr("id");
            if (StringUtils.isEmpty((CharSequence)taskJobId)) {
                logger.error("taskJobId is null");
                return ResDataUtils.resetErrorJson("taskJobId is null");
            }
            String shellState = agentJsonObject.getStr("state");
            if (!StringUtils.isEmpty((CharSequence)shellState) && shellState.length() > 50) {
                shellState = shellState.substring(0, 50);
            }
            if (StringUtils.isEmpty((CharSequence)shellState)) {
                shellState = "Error";
            }
            TaskJobInfo taskJobInfoCall = this.taskJobInfoService.selectById(taskJobId);
            taskJobInfoCall.setCallBackState(shellState);
            taskJobInfoCall.setCallBackTime(new Date());
            String shellResult = agentJsonObject.getStr("result");
            taskJobInfoCall.setCallBackResult(shellResult);
            this.taskJobInfoService.updateById(taskJobInfoCall);
            return ResDataUtils.resetSuccessJson(null);
        }
        catch (Exception e) {
            logger.error("agent\u6267\u884c\u8ba1\u5212\u4efb\u52a1\u5b8c\u6210\u56de\u8c03\u9519\u8bef", (Throwable)e);
            return ResDataUtils.resetErrorJson(e.toString());
        }
    }

    @RequestMapping(value={"list"})
    public String taskJobInfoList(TaskJobInfo taskJobInfo, Model model, HttpServletRequest request) {
        HashMap<String, Object> params = new HashMap<String, Object>();
        try {
            String liceFlage = request.getParameter(StaticKeys.LICENSE_LICE_FLAGE);
            if (!StringUtils.isEmpty((CharSequence)liceFlage)) {
                model.addAttribute("msg", "\u6b64\u529f\u80fd\u9700\u5347\u7ea7\u5230\u4f01\u4e1a\u7248\uff0c\u8bf7\u70b9\u51fb\u9875\u9762\u5e95\u90e8\u7f51\u7ad9\u8054\u7cfb\u6211\u4eec");
            }
            StringBuffer url = new StringBuffer();
            String hostname = null;
            if (!StringUtils.isEmpty((CharSequence)taskJobInfo.getHostname())) {
                hostname = taskJobInfo.getHostname();
                params.put("hostname", hostname.trim());
                url.append("&hostname=").append(hostname);
            }
            if (!StringUtils.isEmpty((CharSequence)taskJobInfo.getAccount())) {
                params.put("account", taskJobInfo.getAccount());
                url.append("&account=").append(taskJobInfo.getAccount());
            }
            if (!StringUtils.isEmpty((CharSequence)taskJobInfo.getGroupId())) {
                params.put("groupId", taskJobInfo.getGroupId());
                url.append("&groupId=").append(taskJobInfo.getGroupId());
            }
            if (!StringUtils.isEmpty((CharSequence)taskJobInfo.getActive())) {
                params.put("active", taskJobInfo.getActive());
                url.append("&active=").append(taskJobInfo.getActive());
            }
            if (!StringUtils.isEmpty((CharSequence)taskJobInfo.getOrderBy())) {
                params.put("orderBy", taskJobInfo.getOrderBy());
                params.put("orderType", taskJobInfo.getOrderType());
                url.append("&orderBy=").append(taskJobInfo.getOrderBy());
                url.append("&orderType=").append(taskJobInfo.getOrderType());
            }
            HostUtil.addAccountquery(request, params);
            PageInfo pageInfo = this.taskJobInfoService.selectByParams(params, taskJobInfo.getPage(), taskJobInfo.getPageSize());
            for (TaskJobInfo taskJobInfo1 : (java.util.List<TaskJobInfo>) pageInfo.getList()) {
                taskJobInfo1.setHostname(taskJobInfo1.getHostname() + HostUtil.addRemark(taskJobInfo1.getHostname()));
            }
            if ("true".equals(this.commonConfig.getHostGroup())) {
                this.taskJobInfoService.setGroupInList(pageInfo.getList(), model, request);
            }
            HostUtil.addAccountListModel(model);
            PageUtil.initPageNumber(pageInfo, model);
            model.addAttribute("pageUrl", (Object)("/taskJobInfo/list?1=1" + url.toString()));
            model.addAttribute("page", (Object)pageInfo);
            model.addAttribute("taskJobInfo", (Object)taskJobInfo);
        }
        catch (Exception e) {
            logger.error("\u67e5\u8be2\u8ba1\u5212\u4efb\u52a1\u4fe1\u606f\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("\u67e5\u8be2\u8ba1\u5212\u4efb\u52a1\u4fe1\u606f\u9519\u8bef", e.toString(), "2");
        }
        return "taskJobInfo/list";
    }

    @ResponseBody
    @RequestMapping(value={"saveGroupId"})
    public String saveGroupId(Model model, HttpServletRequest request) {
        try {
            String ids = request.getParameter("ids");
            String[] groupIdsArr = request.getParameterValues("groupId");
            this.taskJobInfoService.saveGroupId(ids, groupIdsArr, request);
        }
        catch (Exception e) {
            logger.error("\u4fdd\u5b58\u8ba1\u5212\u4efb\u52a1\u6807\u7b7e\u4fe1\u606f\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("\u4fdd\u5b58\u8ba1\u5212\u4efb\u52a1\u6807\u7b7e\u4fe1\u606f\u9519\u8bef", e.toString(), "2");
        }
        return "redirect:/taskJobInfo/list";
    }

    @RequestMapping(value={"save"})
    public String saveTaskJobInfo(TaskJobInfo taskJobInfo, Model model, HttpServletRequest request) {
        String errorMsg = "\u4fdd\u5b58\u8ba1\u5212\u4efb\u52a1\u4fe1\u606f\u9519\u8bef";
        try {
            String blockKey = FormatUtil.haveBlockDanger(taskJobInfo.getShell(), this.commonConfig.getShellToRunBlock());
            if (!StringUtils.isEmpty((CharSequence)blockKey)) {
                model.addAttribute("taskJobInfo", (Object)taskJobInfo);
                model.addAttribute("msg", (Object)("\u4e0b\u53d1\u6307\u4ee4\u542b\u6709\u654f\u611f\u5b57\u7b26" + blockKey + "\uff0c\u8bf7\u68c0\u67e5"));
                this.shellInfoService.getBlockStr(model);
                HashMap<String, Object> params = new HashMap<String, Object>();
                HostUtil.addAccountquery(request, params);
                List<SystemInfo> systemInfoList = this.systemInfoService.selectAllByParams(params);
                model.addAttribute("systemInfoList", systemInfoList);
                return "taskJobInfo/add";
            }
            if (StringUtils.isEmpty((CharSequence)taskJobInfo.getId())) {
                this.taskJobInfoService.save(taskJobInfo, request);
                this.taskJobInfoService.saveLog(request, "\u6dfb\u52a0", taskJobInfo);
            } else {
                this.taskJobInfoService.updateById(taskJobInfo);
                this.taskJobInfoService.saveLog(request, "\u4fee\u6539", taskJobInfo);
            }
        }
        catch (Exception e) {
            logger.error(errorMsg, (Throwable)e);
            this.logInfoService.save(errorMsg, e.toString(), "2");
        }
        return "redirect:/taskJobInfo/list";
    }

    @RequestMapping(value={"saveBatch"})
    public String saveBatchTaskJobInfo(TaskJobInfo taskJobInfo, Model model, HttpServletRequest request) {
        String errorMsg = "\u6279\u91cf\u4fdd\u5b58\u8ba1\u5212\u4efb\u52a1\u4fe1\u606f\u9519\u8bef";
        try {
            String[] hostnames = request.getParameterValues("hostnames");
            if (null == hostnames || hostnames.length < 1) {
                return "redirect:/taskJobInfo/list";
            }
            String blockKey = FormatUtil.haveBlockDanger(taskJobInfo.getShell(), this.commonConfig.getShellToRunBlock());
            if (!StringUtils.isEmpty((CharSequence)blockKey)) {
                model.addAttribute("taskJobInfo", (Object)taskJobInfo);
                model.addAttribute("selectedHosts", (Object)hostnames);
                HashMap<String, Object> paramsAccount = new HashMap<String, Object>();
                HostUtil.addAccountquery(request, paramsAccount);
                List<SystemInfo> systemInfoList = this.systemInfoService.selectAllByParams(paramsAccount);
                for (SystemInfo systemInfo : systemInfoList) {
                    for (String selectedHost : hostnames) {
                        if (!selectedHost.equals(systemInfo.getHostname())) continue;
                        systemInfo.setSelected("selected");
                    }
                }
                model.addAttribute("systemInfoList", systemInfoList);
                model.addAttribute("msg", (Object)("\u4e0b\u53d1\u6307\u4ee4\u542b\u6709\u654f\u611f\u5b57\u7b26" + blockKey + "\uff0c\u8bf7\u68c0\u67e5"));
                this.shellInfoService.getBlockStr(model);
                return "taskJobInfo/addBatch";
            }
            for (String selectedHost : hostnames) {
                taskJobInfo.setHostname(selectedHost);
                this.taskJobInfoService.save(taskJobInfo, request);
                this.taskJobInfoService.saveLog(request, "\u6dfb\u52a0", taskJobInfo);
            }
        }
        catch (Exception e) {
            logger.error(errorMsg, (Throwable)e);
            this.logInfoService.save(errorMsg, e.toString(), "2");
        }
        return "redirect:/taskJobInfo/list";
    }

    @RequestMapping(value={"edit"})
    public String edit(Model model, HttpServletRequest request) {
        String errorMsg = "\u6dfb\u52a0\u8ba1\u5212\u4efb\u52a1\u4fe1\u606f\u9519\u8bef";
        String id = request.getParameter("id");
        TaskJobInfo taskJobInfo = new TaskJobInfo();
        try {
            HashMap<String, Object> params = new HashMap<String, Object>();
            HostUtil.addAccountquery(request, params);
            List<SystemInfo> systemInfoList = this.systemInfoService.selectAllByParams(params);
            model.addAttribute("systemInfoList", systemInfoList);
            if (StringUtils.isEmpty((CharSequence)id)) {
                model.addAttribute("taskJobInfo", (Object)taskJobInfo);
                if (!this.isAddContinue()) {
                    return "redirect:/taskJobInfo/list?liceFlage=1";
                }
                this.shellInfoService.getBlockStr(model);
                return "taskJobInfo/add";
            }
            taskJobInfo = this.taskJobInfoService.selectById(id);
            model.addAttribute("taskJobInfo", (Object)taskJobInfo);
        }
        catch (Exception e) {
            logger.error(errorMsg, (Throwable)e);
            this.logInfoService.save(errorMsg, e.toString(), "2");
        }
        return "taskJobInfo/add";
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
                this.taskJobInfoService.updateActive(params);
            }
        }
        catch (Exception e) {
            logger.error(errorMsg, (Throwable)e);
            this.logInfoService.save(errorMsg, e.toString(), "2");
        }
        return "redirect:/taskJobInfo/list";
    }

    @RequestMapping(value={"editBatch"})
    public String editBatch(Model model, HttpServletRequest request) {
        String errorMsg = "\u6279\u91cf\u6dfb\u52a0\u8ba1\u5212\u4efb\u52a1\u4fe1\u606f\u9519\u8bef";
        TaskJobInfo taskJobInfo = new TaskJobInfo();
        try {
            if (!this.isAddContinue()) {
                return "redirect:/taskJobInfo/list?liceFlage=2";
            }
            HashMap<String, Object> params = new HashMap<String, Object>();
            HostUtil.addAccountquery(request, params);
            List<SystemInfo> systemInfoList = this.systemInfoService.selectAllByParams(params);
            model.addAttribute("systemInfoList", systemInfoList);
            model.addAttribute("taskJobInfo", (Object)taskJobInfo);
        }
        catch (Exception e) {
            logger.error(errorMsg, (Throwable)e);
            this.logInfoService.save(errorMsg, e.toString(), "2");
        }
        return "taskJobInfo/addBatch";
    }

    @RequestMapping(value={"view"})
    public String viewChart(Model model, HttpServletRequest request) {
        String errorMsg = "\u67e5\u770b\u8ba1\u5212\u4efb\u52a1\u4fe1\u606f\u9519\u8bef";
        String id = request.getParameter("id");
        TaskJobInfo taskJobInfo = new TaskJobInfo();
        try {
            taskJobInfo = this.taskJobInfoService.selectById(id);
            if ("true".equals(this.commonConfig.getHostGroup())) {
                taskJobInfo.setGroupId(this.hostGroupService.returnGroupNames(taskJobInfo.getGroupId()));
            }
            taskJobInfo.setHostname(taskJobInfo.getHostname() + HostUtil.addRemark(taskJobInfo.getHostname()));
            model.addAttribute("taskJobInfo", (Object)taskJobInfo);
        }
        catch (Exception e) {
            logger.error(errorMsg, (Throwable)e);
            this.logInfoService.save(errorMsg, e.toString(), "2");
        }
        return "taskJobInfo/view";
    }

    @RequestMapping(value={"del"})
    public String delete(Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        String errorMsg = "\u5220\u9664\u8ba1\u5212\u4efb\u52a1\u4fe1\u606f\u9519\u8bef";
        TaskJobInfo taskJobInfo = new TaskJobInfo();
        try {
            if (!StringUtils.isEmpty((CharSequence)request.getParameter("id"))) {
                String[] ids;
                for (String id : ids = request.getParameter("id").split(",")) {
                    taskJobInfo = this.taskJobInfoService.selectById(id);
                    this.taskJobInfoService.saveLog(request, "\u5220\u9664", taskJobInfo);
                }
                this.taskJobInfoService.deleteById(ids);
            }
        }
        catch (Exception e) {
            logger.error(errorMsg, (Throwable)e);
            this.logInfoService.save(errorMsg, e.toString(), "2");
        }
        return "redirect:/taskJobInfo/list";
    }

    private boolean isAddContinue() {
        try {
            if (LicenseUtil.checkEnterpriseVersion()) {
                return true;
            }
        }
        catch (Exception e) {
            logger.error("isAddContinue error", (Throwable)e);
        }
        return false;
    }
}

