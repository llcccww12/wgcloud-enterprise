/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.controller;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.github.pagehelper.PageInfo;
import com.wgcloud.config.CommonConfig;
import com.wgcloud.dto.ShellCheckDto;
import com.wgcloud.entity.AppInfo;
import com.wgcloud.entity.SystemInfo;
import com.wgcloud.service.AppInfoService;
import com.wgcloud.service.AppStateService;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.wgcloud.entity.AppState;

@Controller
@RequestMapping(value={"/appInfo"})
public class AppInfoController {
    private static final Logger logger = LoggerFactory.getLogger(AppInfoController.class);
    @Resource
    private AppInfoService appInfoService;
    @Resource
    private AppStateService appStateService;
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
    private TokenUtils tokenUtils;
    @Autowired
    private CommonConfig commonConfig;
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
            List<AppInfo> appInfoList = this.appInfoService.selectAllByParams(params);
            ShellCheckDto shellCheckDto = this.shellInfoService.getShellCheckDto(agentJsonObject.get("hostname").toString());
            String cmdSplitChar = shellCheckDto.getCmdSplitChar();
            String blockKey = "";
            ArrayList<AppInfo> appInfoListResult = new ArrayList<AppInfo>();
            for (AppInfo state : appInfoList) {
                if (StringUtils.isEmpty((CharSequence)state.getCustomShell())) {
                    state.setCustomShell("");
                }
                if (!StringUtils.isEmpty((CharSequence)(blockKey = FormatUtil.haveBlockDanger(state.getCustomShell(), this.commonConfig.getShellToRunBlock())))) {
                    logger.error(state.getCustomShell() + "\u8fdb\u7a0b\u6062\u590d\u6307\u4ee4\u542b\u6709\u654f\u611f\u5b57\u7b26" + blockKey + "\uff0c\u4e0d\u8fdb\u884c\u4e0b\u53d1");
                    continue;
                }
                state.setCustomShell(state.getCustomShell().replaceAll("\\r\\n", cmdSplitChar));
                appInfoListResult.add(state);
            }
            if ("1".equals(agentJsonObject.get("fromAgent"))) {
                ArrayList<AppInfo> appInfoListResultAgent = new ArrayList<AppInfo>();
                for (AppInfo appInfo : appInfoListResult) {
                    AppInfo appInfoTmp = new AppInfo();
                    appInfoTmp.setId(appInfo.getId());
                    appInfoTmp.setAppType(appInfo.getAppType());
                    appInfoTmp.setCustomShell(appInfo.getCustomShell());
                    appInfoTmp.setState(appInfo.getState());
                    appInfoTmp.setAppPid(appInfo.getAppPid());
                    appInfoListResultAgent.add(appInfoTmp);
                }
                return ResDataUtils.resetSuccessJson(appInfoListResultAgent);
            }
            return ResDataUtils.resetSuccessJson(appInfoListResult);
        }
        catch (Exception e) {
            logger.error("agent\u83b7\u53d6\u8fdb\u7a0b\u4fe1\u606f\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("agent\u83b7\u53d6\u8fdb\u7a0b\u4fe1\u606f\u9519\u8bef", e.toString(), "2");
            return ResDataUtils.resetErrorJson(e.toString());
        }
    }

    @RequestMapping(value={"list"})
    public String appInfoList(AppInfo appInfo, Model model, HttpServletRequest request) {
        HashMap<String, Object> params = new HashMap<String, Object>();
        try {
            LicenseUtil.maxLicense_10(model, request, appInfo);
            StringBuffer url = new StringBuffer();
            String hostname = null;
            if (!StringUtils.isEmpty((CharSequence)appInfo.getHostname())) {
                hostname = appInfo.getHostname();
                params.put("hostname", hostname.trim());
                url.append("&hostname=").append(hostname);
            }
            if (!StringUtils.isEmpty((CharSequence)appInfo.getAccount())) {
                params.put("account", appInfo.getAccount());
                url.append("&account=").append(appInfo.getAccount());
            }
            if (!StringUtils.isEmpty((CharSequence)appInfo.getGroupId())) {
                params.put("groupId", appInfo.getGroupId());
                url.append("&groupId=").append(appInfo.getGroupId());
            }
            if (!StringUtils.isEmpty((CharSequence)appInfo.getOrderBy())) {
                params.put("orderBy", appInfo.getOrderBy());
                params.put("orderType", appInfo.getOrderType());
                url.append("&orderBy=").append(appInfo.getOrderBy());
                url.append("&orderType=").append(appInfo.getOrderType());
            }
            if (!StringUtils.isEmpty((CharSequence)appInfo.getState())) {
                params.put("state", appInfo.getState());
                url.append("&state=").append(appInfo.getState());
            }
            if (!StringUtils.isEmpty((CharSequence)appInfo.getActive())) {
                params.put("active", appInfo.getActive());
                url.append("&active=").append(appInfo.getActive());
            }
            HostUtil.addAccountquery(request, params);
            PageInfo pageInfo = this.appInfoService.selectByParams(params, appInfo.getPage(), appInfo.getPageSize());
            for (AppInfo appInfo1 : (java.util.List<AppInfo>) pageInfo.getList()) {
                appInfo1.setWritesBytes(FormatUtil.mToG(appInfo1.getWritesBytes()));
                appInfo1.setReadBytes(FormatUtil.mToG(appInfo1.getReadBytes()));
                if (StringUtils.isEmpty((CharSequence)appInfo1.getCustomShell())) {
                    appInfo1.setCustomShell("");
                }
                if ("true".equals(this.commonConfig.getUserInfoManage())) {
                    appInfo1.setAccount(HostUtil.getAccount(appInfo1.getHostname()));
                }
                appInfo1.setHostState(HostUtil.getHostState(appInfo1.getHostname()));
                appInfo1.setHostname(appInfo1.getHostname() + HostUtil.addRemark(appInfo1.getHostname()));
                if (!"true".equals(this.commonConfig.getShowWarnCount())) continue;
                String warnQueryWd = appInfo1.getAppName() + "\uff0c" + appInfo1.getHostname();
                this.logInfoService.warnQueryHandle(appInfo1, warnQueryWd);
            }
            if ("true".equals(this.commonConfig.getHostGroup())) {
                this.appInfoService.setGroupInList(pageInfo.getList(), model, request);
            }
            HostUtil.addAccountListModel(model);
            PageUtil.initPageNumber(pageInfo, model);
            model.addAttribute("pageUrl", (Object)("/appInfo/list?1=1" + url.toString()));
            model.addAttribute("page", (Object)pageInfo);
            model.addAttribute("appInfo", (Object)appInfo);
        }
        catch (Exception e) {
            logger.error("\u67e5\u8be2\u8fdb\u7a0b\u4fe1\u606f\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("\u67e5\u8be2\u8fdb\u7a0b\u4fe1\u606f\u9519\u8bef", e.toString(), "2");
        }
        return "app/list";
    }

    @ResponseBody
    @RequestMapping(value={"saveGroupId"})
    public String saveGroupId(Model model, HttpServletRequest request) {
        try {
            String ids = request.getParameter("ids");
            String[] groupIdsArr = request.getParameterValues("groupId");
            this.appInfoService.saveGroupId(ids, groupIdsArr, request);
        }
        catch (Exception e) {
            logger.error("\u4fdd\u5b58\u8fdb\u7a0b\u6807\u7b7e\u4fe1\u606f\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("\u4fdd\u5b58\u8fdb\u7a0b\u6807\u7b7e\u4fe1\u606f\u9519\u8bef", e.toString(), "2");
        }
        return "redirect:/appInfo/list";
    }

    @RequestMapping(value={"save"})
    public String saveAppInfo(AppInfo appInfo, Model model, HttpServletRequest request) {
        String errorMsg = "\u4fdd\u5b58\u8fdb\u7a0b\u9519\u8bef";
        try {
            String blockKey = FormatUtil.haveBlockDanger(appInfo.getCustomShell(), this.commonConfig.getShellToRunBlock());
            if (!StringUtils.isEmpty((CharSequence)blockKey)) {
                model.addAttribute("appInfo", (Object)appInfo);
                model.addAttribute("msg", (Object)("\u81ea\u52a8\u6062\u590d\u6307\u4ee4\u542b\u6709\u654f\u611f\u5b57\u7b26" + blockKey + "\uff0c\u8bf7\u68c0\u67e5"));
                this.shellInfoService.getBlockStr(model);
                HashMap<String, Object> params = new HashMap<String, Object>();
                HostUtil.addAccountquery(request, params);
                List<SystemInfo> systemInfoList = this.systemInfoService.selectAllByParams(params);
                model.addAttribute("systemInfoList", systemInfoList);
                return "app/add";
            }
            if (StringUtils.isEmpty((CharSequence)appInfo.getId())) {
                this.appInfoService.save(appInfo, request);
                this.appInfoService.saveLog(request, "\u6dfb\u52a0", appInfo);
            } else {
                this.appInfoService.updateById(appInfo);
                this.appInfoService.saveLog(request, "\u4fee\u6539", appInfo);
            }
        }
        catch (Exception e) {
            logger.error(errorMsg, (Throwable)e);
            this.logInfoService.save(errorMsg, e.toString(), "2");
        }
        return "redirect:/appInfo/list";
    }

    @RequestMapping(value={"saveBatch"})
    public String saveBatchAppInfo(AppInfo appInfo, Model model, HttpServletRequest request) {
        String errorMsg = "\u6279\u91cf\u4fdd\u5b58\u8fdb\u7a0b\u9519\u8bef";
        try {
            String[] hostnames = request.getParameterValues("hostnames");
            if (null == hostnames || hostnames.length < 1) {
                return "redirect:/appInfo/list";
            }
            String blockKey = FormatUtil.haveBlockDanger(appInfo.getCustomShell(), this.commonConfig.getShellToRunBlock());
            if (!StringUtils.isEmpty((CharSequence)blockKey)) {
                model.addAttribute("appInfo", (Object)appInfo);
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
                return "app/addBatch";
            }
            String customShell = appInfo.getCustomShell();
            for (String selectedHost : hostnames) {
                appInfo.setHostname(selectedHost);
                appInfo.setCustomShell(customShell);
                this.appInfoService.save(appInfo, request);
                this.appInfoService.saveLog(request, "\u6dfb\u52a0", appInfo);
            }
        }
        catch (Exception e) {
            logger.error(errorMsg, (Throwable)e);
            this.logInfoService.save(errorMsg, e.toString(), "2");
        }
        return "redirect:/appInfo/list";
    }

    @RequestMapping(value={"edit"})
    public String edit(Model model, HttpServletRequest request) {
        String errorMsg = "\u6dfb\u52a0\u8fdb\u7a0b\u9519\u8bef";
        String id = request.getParameter("id");
        AppInfo appInfo = new AppInfo();
        try {
            HashMap<String, Object> params = new HashMap<String, Object>();
            HostUtil.addAccountquery(request, params);
            this.shellInfoService.getBlockStr(model);
            List<SystemInfo> systemInfoList = this.systemInfoService.selectAllByParams(params);
            model.addAttribute("systemInfoList", systemInfoList);
            if (StringUtils.isEmpty((CharSequence)id)) {
                model.addAttribute("appInfo", (Object)appInfo);
                if (!this.isAddContinue()) {
                    return "redirect:/appInfo/list?liceFlage=1";
                }
                return "app/add";
            }
            appInfo = this.appInfoService.selectById(id);
            model.addAttribute("appInfo", (Object)appInfo);
        }
        catch (Exception e) {
            logger.error(errorMsg, (Throwable)e);
            this.logInfoService.save(errorMsg, e.toString(), "2");
        }
        return "app/add";
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
                this.appInfoService.updateActive(params);
            }
        }
        catch (Exception e) {
            logger.error(errorMsg, (Throwable)e);
            this.logInfoService.save(errorMsg, e.toString(), "2");
        }
        return "redirect:/appInfo/list";
    }

    @RequestMapping(value={"editBatch"})
    public String editBatch(Model model, HttpServletRequest request) {
        String errorMsg = "\u6279\u91cf\u6dfb\u52a0\u8fdb\u7a0b\u9519\u8bef";
        AppInfo appInfo = new AppInfo();
        try {
            if (!StaticKeys.LICENSE_STATE.equals("1")) {
                return "redirect:/appInfo/list?liceFlage=2";
            }
            HashMap<String, Object> params = new HashMap<String, Object>();
            HostUtil.addAccountquery(request, params);
            this.shellInfoService.getBlockStr(model);
            List<SystemInfo> systemInfoList = this.systemInfoService.selectAllByParams(params);
            model.addAttribute("systemInfoList", systemInfoList);
            model.addAttribute("appInfo", (Object)appInfo);
        }
        catch (Exception e) {
            logger.error(errorMsg, (Throwable)e);
            this.logInfoService.save(errorMsg, e.toString(), "2");
        }
        return "app/addBatch";
    }

    @RequestMapping(value={"view"})
    public String viewChart(Model model, HttpServletRequest request) {
        String errorMsg = "\u67e5\u770b\u8fdb\u7a0b\u56fe\u8868\u9519\u8bef";
        String id = request.getParameter("id");
        String startTime = request.getParameter("startTime");
        String endTime = request.getParameter("endTime");
        String am = request.getParameter("am");
        AppInfo appInfo = new AppInfo();
        try {
            appInfo = this.appInfoService.selectById(id);
            appInfo.setHostname(appInfo.getHostname() + HostUtil.addRemark(appInfo.getHostname()));
            appInfo.setWritesBytes(FormatUtil.mToG(appInfo.getWritesBytes()));
            appInfo.setReadBytes(FormatUtil.mToG(appInfo.getReadBytes()));
            HashMap<String, Object> params = new HashMap<String, Object>();
            this.dashboardService.setDateParam(am, startTime, endTime, params, model);
            model.addAttribute("amList", this.dashboardService.getAmList());
            params.put("appInfoId", appInfo.getId());
            model.addAttribute("appInfo", (Object)appInfo);
            if ("true".equals(this.commonConfig.getHostGroup())) {
                appInfo.setGroupId(this.hostGroupService.returnGroupNames(appInfo.getGroupId()));
            }
            List<AppState> appStateList = this.appStateService.selectAllByParams(params);
            List<AppState> appStateCompressList = HostUtil.compressChartListData(appStateList, model);
            this.appStateService.setSubtitle(model, appStateCompressList);
            model.addAttribute("appStateList", (Object)JSONUtil.parseArray(appStateCompressList));
            if ("2".equals(appInfo.getState())) {
                model.addAttribute("appErrorMsg", (Object)this.messageErrorUtils.viewErrorMsgHandler(id));
            } else {
                model.addAttribute("appErrorMsg", "success");
            }
            this.messageErrorUtils.getCallBackMsgForShell(model, id, appInfo.getCustomShell());
        }
        catch (Exception e) {
            logger.error(errorMsg, (Throwable)e);
            this.logInfoService.save(errorMsg, e.toString(), "2");
        }
        return "app/view";
    }

    @RequestMapping(value={"chartExcel"})
    public void chartExcel(Model model, HttpServletRequest request, HttpServletResponse response) {
        String errorMsg = "\u8fdb\u7a0b\u7edf\u8ba1\u56fe\u5bfc\u51faexcel\u9519\u8bef";
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
            params.put("appInfoId", id);
            this.dashboardService.setDateParam(am, startTime, endTime, params, model);
            AppInfo appInfo = this.appInfoService.selectById(id);
            this.excelExportService.exportAppExcel(params, response, appInfo);
        }
        catch (Exception e) {
            logger.error(errorMsg, (Throwable)e);
            this.logInfoService.save(errorMsg, e.toString(), "2");
        }
    }

    @RequestMapping(value={"del"})
    public String delete(Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        String errorMsg = "\u5220\u9664\u8fdb\u7a0b\u4fe1\u606f\u9519\u8bef";
        AppInfo appInfo = new AppInfo();
        try {
            if (!StringUtils.isEmpty((CharSequence)request.getParameter("id"))) {
                String[] ids;
                for (String id : ids = request.getParameter("id").split(",")) {
                    appInfo = this.appInfoService.selectById(id);
                    this.appInfoService.saveLog(request, "\u5220\u9664", appInfo);
                }
                this.appInfoService.deleteById(request.getParameter("id").split(","));
            }
        }
        catch (Exception e) {
            logger.error(errorMsg, (Throwable)e);
            this.logInfoService.save(errorMsg, e.toString(), "2");
        }
        return "redirect:/appInfo/list";
    }

    private boolean isAddContinue() {
        try {
            HashMap<String, Object> params;
            int dbSize;
            if (!StaticKeys.LICENSE_STATE.equals("1") && (dbSize = this.appInfoService.countByParams(params = new HashMap<String, Object>())) >= 10) {
                return false;
            }
        }
        catch (Exception e) {
            logger.error("isAddContinue error", (Throwable)e);
        }
        return true;
    }
}

