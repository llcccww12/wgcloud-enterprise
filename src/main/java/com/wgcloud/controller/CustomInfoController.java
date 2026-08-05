/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.controller;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.github.pagehelper.PageInfo;
import com.wgcloud.config.CommonConfig;
import com.wgcloud.entity.CustomInfo;
import com.wgcloud.entity.SystemInfo;
import com.wgcloud.service.CustomInfoService;
import com.wgcloud.service.CustomStateService;
import com.wgcloud.service.DashboardService;
import com.wgcloud.service.ExcelExportService;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.wgcloud.entity.CustomState;

@Controller
@RequestMapping(value={"/customInfo"})
public class CustomInfoController {
    private static final Logger logger = LoggerFactory.getLogger(CustomInfoController.class);
    @Resource
    private CustomInfoService customInfoService;
    @Resource
    private CustomStateService customStateService;
    @Resource
    private LogInfoService logInfoService;
    @Resource
    private SystemInfoService systemInfoService;
    @Resource
    private DashboardService dashboardService;
    @Resource
    private ExcelExportService excelExportService;
    @Resource
    private HostGroupService hostGroupService;
    @Resource
    private ShellInfoService shellInfoService;
    @Autowired
    private MessageErrorUtils messageErrorUtils;
    @Autowired
    private TokenUtils tokenUtils;
    @Autowired
    private CommonConfig commonConfig;

    @ResponseBody
    @RequestMapping(value={"agentList"})
    public String agentList(@RequestBody String paramBean) {
        if (!StaticKeys.LICENSE_STATE.equals("1")) {
            ArrayList customInfoList = new ArrayList();
            return ResDataUtils.resetSuccessJson(customInfoList);
        }
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
            List<CustomInfo> customInfoList = this.customInfoService.selectAllByParams(params);
            String blockKey = "";
            ArrayList<CustomInfo> customInfoListResult = new ArrayList<CustomInfo>();
            for (CustomInfo state : customInfoList) {
                blockKey = FormatUtil.haveBlockDanger(state.getCustomShell(), this.commonConfig.getShellToRunBlock());
                if (!StringUtils.isEmpty((CharSequence)blockKey)) {
                    logger.error(state.getCustomShell() + "\u81ea\u5b9a\u4e49\u76d1\u63a7\u9879\u6307\u4ee4\u542b\u6709\u654f\u611f\u5b57\u7b26" + blockKey + "\uff0c\u4e0d\u8fdb\u884c\u4e0b\u53d1");
                    continue;
                }
                state.setCustomShell(state.getCustomShell().replaceAll("\\r\\n", " && "));
                customInfoListResult.add(state);
            }
            return ResDataUtils.resetSuccessJson(customInfoListResult);
        }
        catch (Exception e) {
            logger.error("agent\u83b7\u53d6\u81ea\u5b9a\u4e49\u76d1\u63a7\u9879\u4fe1\u606f\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("agent\u83b7\u53d6\u81ea\u5b9a\u4e49\u76d1\u63a7\u9879\u4fe1\u606f\u9519\u8bef", e.toString(), "2");
            return ResDataUtils.resetErrorJson(e.toString());
        }
    }

    @RequestMapping(value={"list"})
    public String customInfoList(CustomInfo customInfo, Model model, HttpServletRequest request) {
        HashMap<String, Object> params = new HashMap<String, Object>();
        try {
            String liceFlage = request.getParameter(StaticKeys.LICENSE_LICE_FLAGE);
            if (!StringUtils.isEmpty((CharSequence)liceFlage)) {
                model.addAttribute("msg", "\u6b64\u529f\u80fd\u9700\u5347\u7ea7\u5230\u4e13\u4e1a\u7248\uff0c\u8bf7\u70b9\u51fb\u9875\u9762\u5e95\u90e8\u7f51\u7ad9\u8054\u7cfb\u6211\u4eec");
            }
            StringBuffer url = new StringBuffer();
            String hostname = null;
            if (!StringUtils.isEmpty((CharSequence)customInfo.getHostname())) {
                hostname = customInfo.getHostname();
                params.put("hostname", hostname.trim());
                url.append("&hostname=").append(hostname);
            }
            if (!StringUtils.isEmpty((CharSequence)customInfo.getAccount())) {
                params.put("account", customInfo.getAccount());
                url.append("&account=").append(customInfo.getAccount());
            }
            if (!StringUtils.isEmpty((CharSequence)customInfo.getGroupId())) {
                params.put("groupId", customInfo.getGroupId());
                url.append("&groupId=").append(customInfo.getGroupId());
            }
            if (!StringUtils.isEmpty((CharSequence)customInfo.getState())) {
                params.put("state", customInfo.getState());
                url.append("&state=").append(customInfo.getState());
            }
            if (!StringUtils.isEmpty((CharSequence)customInfo.getActive())) {
                params.put("active", customInfo.getActive());
                url.append("&active=").append(customInfo.getActive());
            }
            HostUtil.addAccountquery(request, params);
            PageInfo pageInfo = this.customInfoService.selectByParams(params, customInfo.getPage(), customInfo.getPageSize());
            for (CustomInfo customInfo1 : (java.util.List<CustomInfo>) pageInfo.getList()) {
                if ("true".equals(this.commonConfig.getUserInfoManage())) {
                    customInfo1.setAccount(HostUtil.getAccount(customInfo1.getHostname()));
                }
                customInfo1.setHostState(HostUtil.getHostState(customInfo1.getHostname()));
                customInfo1.setHostname(customInfo1.getHostname() + HostUtil.addRemark(customInfo1.getHostname()));
                if (!"true".equals(this.commonConfig.getShowWarnCount())) continue;
                String warnQueryWd = customInfo1.getCustomName() + "\uff0c" + customInfo1.getHostname();
                this.logInfoService.warnQueryHandle(customInfo1, warnQueryWd);
            }
            if ("true".equals(this.commonConfig.getHostGroup())) {
                this.customInfoService.setGroupInList(pageInfo.getList(), model, request);
            }
            HostUtil.addAccountListModel(model);
            PageUtil.initPageNumber(pageInfo, model);
            model.addAttribute("pageUrl", (Object)("/customInfo/list?1=1" + url.toString()));
            model.addAttribute("page", (Object)pageInfo);
            model.addAttribute("customInfo", (Object)customInfo);
        }
        catch (Exception e) {
            logger.error("\u67e5\u8be2\u81ea\u5b9a\u4e49\u76d1\u63a7\u9879\u4fe1\u606f\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("\u67e5\u8be2\u81ea\u5b9a\u4e49\u76d1\u63a7\u9879\u4fe1\u606f\u9519\u8bef", e.toString(), "2");
        }
        return "customInfo/list";
    }

    @ResponseBody
    @RequestMapping(value={"saveGroupId"})
    public String saveGroupId(Model model, HttpServletRequest request) {
        try {
            String ids = request.getParameter("ids");
            String[] groupIdsArr = request.getParameterValues("groupId");
            this.customInfoService.saveGroupId(ids, groupIdsArr, request);
        }
        catch (Exception e) {
            logger.error("\u4fdd\u5b58\u8fdb\u7a0b\u6807\u7b7e\u4fe1\u606f\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("\u4fdd\u5b58\u8fdb\u7a0b\u6807\u7b7e\u4fe1\u606f\u9519\u8bef", e.toString(), "2");
        }
        return "redirect:/appInfo/list";
    }

    @RequestMapping(value={"saveBatch"})
    public String saveBatchCustomInfo(CustomInfo customInfo, Model model, HttpServletRequest request) {
        String errorMsg = "\u6279\u91cf\u4fdd\u5b58\u81ea\u5b9a\u4e49\u76d1\u63a7\u9879\u4fe1\u606f\u9519\u8bef";
        try {
            String[] hostnames = request.getParameterValues("hostnames");
            if (null == hostnames || hostnames.length < 1) {
                return "redirect:/customInfo/list";
            }
            String blockKey = FormatUtil.haveBlockDanger(customInfo.getCustomShell(), this.commonConfig.getShellToRunBlock());
            if (!StringUtils.isEmpty((CharSequence)blockKey)) {
                model.addAttribute("customInfo", (Object)customInfo);
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
                model.addAttribute("msg", (Object)("\u6307\u4ee4\u542b\u6709\u654f\u611f\u5b57\u7b26" + blockKey + "\uff0c\u8bf7\u68c0\u67e5"));
                this.shellInfoService.getBlockStr(model);
                return "customInfo/addBatch";
            }
            for (String selectedHost : hostnames) {
                customInfo.setHostname(selectedHost);
                this.customInfoService.save(customInfo, request);
                this.customInfoService.saveLog(request, "\u6dfb\u52a0", customInfo);
            }
        }
        catch (Exception e) {
            logger.error(errorMsg, (Throwable)e);
            this.logInfoService.save(errorMsg, e.toString(), "2");
        }
        return "redirect:/customInfo/list";
    }

    @RequestMapping(value={"save"})
    public String saveCustomInfo(CustomInfo customInfo, Model model, HttpServletRequest request) {
        String errorMsg = "\u4fdd\u5b58\u81ea\u5b9a\u4e49\u76d1\u63a7\u9879\u9519\u8bef";
        try {
            String blockKey = FormatUtil.haveBlockDanger(customInfo.getCustomShell(), this.commonConfig.getShellToRunBlock());
            if (!StringUtils.isEmpty((CharSequence)blockKey)) {
                model.addAttribute("customInfo", (Object)customInfo);
                model.addAttribute("msg", (Object)("\u6307\u4ee4\u542b\u6709\u654f\u611f\u5b57\u7b26" + blockKey + "\uff0c\u8bf7\u68c0\u67e5"));
                this.shellInfoService.getBlockStr(model);
                HashMap<String, Object> params = new HashMap<String, Object>();
                HostUtil.addAccountquery(request, params);
                List<SystemInfo> systemInfoList = this.systemInfoService.selectAllByParams(params);
                model.addAttribute("systemInfoList", systemInfoList);
                return "customInfo/add";
            }
            if (StringUtils.isEmpty((CharSequence)customInfo.getId())) {
                this.customInfoService.save(customInfo, request);
                this.customInfoService.saveLog(request, "\u6dfb\u52a0", customInfo);
            } else {
                this.customInfoService.updateById(customInfo);
                this.customInfoService.saveLog(request, "\u4fee\u6539", customInfo);
            }
        }
        catch (Exception e) {
            logger.error(errorMsg, (Throwable)e);
            this.logInfoService.save(errorMsg, e.toString(), "2");
        }
        return "redirect:/customInfo/list";
    }

    @RequestMapping(value={"edit"})
    public String edit(Model model, HttpServletRequest request) {
        String errorMsg = "\u6dfb\u52a0\u81ea\u5b9a\u4e49\u76d1\u63a7\u9879\u9519\u8bef";
        String id = request.getParameter("id");
        CustomInfo customInfo = new CustomInfo();
        try {
            HashMap<String, Object> params = new HashMap<String, Object>();
            HostUtil.addAccountquery(request, params);
            this.shellInfoService.getBlockStr(model);
            List<SystemInfo> systemInfoList = this.systemInfoService.selectAllByParams(params);
            model.addAttribute("systemInfoList", systemInfoList);
            if (StringUtils.isEmpty((CharSequence)id)) {
                model.addAttribute("customInfo", (Object)customInfo);
                if (!this.isAddContinue()) {
                    return "redirect:/customInfo/list?liceFlage=1";
                }
                return "customInfo/add";
            }
            customInfo = this.customInfoService.selectById(id);
            model.addAttribute("customInfo", (Object)customInfo);
        }
        catch (Exception e) {
            logger.error(errorMsg, (Throwable)e);
            this.logInfoService.save(errorMsg, e.toString(), "2");
        }
        return "customInfo/add";
    }

    @RequestMapping(value={"editBatch"})
    public String editBatch(Model model, HttpServletRequest request) {
        String errorMsg = "\u6279\u91cf\u6dfb\u52a0\u7aef\u53e3\u4fe1\u606f\u9519\u8bef";
        CustomInfo customInfo = new CustomInfo();
        try {
            if (!StaticKeys.LICENSE_STATE.equals("1")) {
                return "redirect:/customInfo/list?liceFlage=2";
            }
            HashMap<String, Object> params = new HashMap<String, Object>();
            HostUtil.addAccountquery(request, params);
            List<SystemInfo> systemInfoList = this.systemInfoService.selectAllByParams(params);
            model.addAttribute("systemInfoList", systemInfoList);
            model.addAttribute("customInfo", (Object)customInfo);
        }
        catch (Exception e) {
            logger.error(errorMsg, (Throwable)e);
            this.logInfoService.save(errorMsg, e.toString(), "2");
        }
        return "customInfo/addBatch";
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
                this.customInfoService.updateActive(params);
            }
        }
        catch (Exception e) {
            logger.error(errorMsg, (Throwable)e);
            this.logInfoService.save(errorMsg, e.toString(), "2");
        }
        return "redirect:/customInfo/list";
    }

    @RequestMapping(value={"view"})
    public String viewChart(Model model, HttpServletRequest request) {
        String errorMsg = "\u67e5\u770b\u81ea\u5b9a\u4e49\u76d1\u63a7\u9879\u56fe\u8868\u9519\u8bef";
        String id = request.getParameter("id");
        String startTime = request.getParameter("startTime");
        String endTime = request.getParameter("endTime");
        String am = request.getParameter("am");
        CustomInfo customInfo = new CustomInfo();
        try {
            customInfo = this.customInfoService.selectById(id);
            customInfo.setHostname(customInfo.getHostname() + HostUtil.addRemark(customInfo.getHostname()));
            HashMap<String, Object> params = new HashMap<String, Object>();
            this.dashboardService.setDateParam(am, startTime, endTime, params, model);
            model.addAttribute("amList", this.dashboardService.getAmList());
            params.put("customInfoId", customInfo.getId());
            model.addAttribute("customInfo", (Object)customInfo);
            if ("true".equals(this.commonConfig.getHostGroup())) {
                customInfo.setGroupId(this.hostGroupService.returnGroupNames(customInfo.getGroupId()));
            }
            List<CustomState> customStateList = this.customStateService.selectAllByParams(params);
            List<CustomState> customStateCompressList = HostUtil.compressChartListData(customStateList, model);
            JSONArray customStateJsonArray = this.customStateService.getJsonArray(customInfo, customStateCompressList);
            this.customStateService.setSubtitle(model, customStateJsonArray);
            model.addAttribute("customStateList", (Object)customStateJsonArray);
            String customStateErrorMsg = "\r\n#--------------\u6307\u4ee4\u5185\u5bb9-----------------------\r\n" + customInfo.getCustomShell() + " \r\n#--------------\u6307\u4ee4\u6267\u884c\u8f93\u51fa-------------------\r\n" + this.messageErrorUtils.viewErrorMsgHandler(id) + " \r\n#--------------\u91c7\u96c6\u7ed3\u679c\u503c---------------------\r\n" + customInfo.getCustomValue();
            model.addAttribute("customStateErrorMsg", (Object)customStateErrorMsg);
        }
        catch (Exception e) {
            logger.error(errorMsg, (Throwable)e);
            this.logInfoService.save(errorMsg, e.toString(), "2");
        }
        return "customInfo/view";
    }

    @RequestMapping(value={"chartExcel"})
    public void chartExcel(Model model, HttpServletRequest request, HttpServletResponse response) {
        String errorMsg = "\u81ea\u5b9a\u4e49\u76d1\u63a7\u9879\u7edf\u8ba1\u56fe\u5bfc\u51faexcel\u9519\u8bef";
        String id = request.getParameter("id");
        String startTime = request.getParameter("startTime");
        String endTime = request.getParameter("endTime");
        String am = request.getParameter("am");
        try {
            if (!StaticKeys.LICENSE_STATE.equals("1")) {
                response.setContentType("text/html;charset=UTF-8");
                response.getOutputStream().write("The module needs to professional version. Please contact us at www.wgstart.com".getBytes());
                return;
            }
            if (StringUtils.isEmpty((CharSequence)id)) {
                response.setContentType("text/html;charset=UTF-8");
                response.getOutputStream().write("Missing require parameters".getBytes());
                return;
            }
            HashMap<String, Object> params = new HashMap<String, Object>();
            params.put("customInfoId", id);
            this.dashboardService.setDateParam(am, startTime, endTime, params, model);
            CustomInfo customInfo = this.customInfoService.selectById(id);
            this.excelExportService.exportCustomExcel(params, response, customInfo);
        }
        catch (Exception e) {
            logger.error(errorMsg, (Throwable)e);
            this.logInfoService.save(errorMsg, e.toString(), "2");
        }
    }

    @RequestMapping(value={"del"})
    public String delete(Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        String errorMsg = "\u5220\u9664\u81ea\u5b9a\u4e49\u76d1\u63a7\u9879\u4fe1\u606f\u9519\u8bef";
        CustomInfo customInfo = new CustomInfo();
        try {
            if (!StringUtils.isEmpty((CharSequence)request.getParameter("id"))) {
                String[] ids;
                for (String id : ids = request.getParameter("id").split(",")) {
                    customInfo = this.customInfoService.selectById(id);
                    this.customInfoService.saveLog(request, "\u5220\u9664", customInfo);
                }
                this.customInfoService.deleteById(request.getParameter("id").split(","));
            }
        }
        catch (Exception e) {
            logger.error(errorMsg, (Throwable)e);
            this.logInfoService.save(errorMsg, e.toString(), "2");
        }
        return "redirect:/customInfo/list";
    }

    private boolean isAddContinue() {
        try {
            if (!StaticKeys.LICENSE_STATE.equals("1")) {
                return false;
            }
        }
        catch (Exception e) {
            logger.error("isAddContinue error", (Throwable)e);
        }
        return true;
    }
}

