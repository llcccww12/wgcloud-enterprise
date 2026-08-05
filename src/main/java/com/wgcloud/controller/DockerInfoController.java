/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.controller;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.github.pagehelper.PageInfo;
import com.wgcloud.config.CommonConfig;
import com.wgcloud.entity.AccountInfo;
import com.wgcloud.entity.DockerInfo;
import com.wgcloud.entity.ShellInfo;
import com.wgcloud.entity.SystemInfo;
import com.wgcloud.service.AccountInfoService;
import com.wgcloud.service.DashboardService;
import com.wgcloud.service.DockerInfoService;
import com.wgcloud.service.DockerStateService;
import com.wgcloud.service.ExcelExportService;
import com.wgcloud.service.HostGroupService;
import com.wgcloud.service.LogInfoService;
import com.wgcloud.service.ShellInfoService;
import com.wgcloud.service.SystemInfoService;
import com.wgcloud.util.HostUtil;
import com.wgcloud.util.MessageErrorUtils;
import com.wgcloud.util.PageUtil;
import com.wgcloud.util.ResDataUtils;
import com.wgcloud.util.ThreadPoolUtil;
import com.wgcloud.util.TokenUtils;
import com.wgcloud.util.license.LicenseUtil;
import com.wgcloud.util.msg.WarnOtherUtil;
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
import com.wgcloud.entity.DockerState;

@Controller
@RequestMapping(value={"/dockerInfo"})
public class DockerInfoController {
    private static final Logger logger = LoggerFactory.getLogger(DockerInfoController.class);
    @Resource
    private DockerInfoService dockerInfoService;
    @Resource
    private DockerStateService dockerStateService;
    @Resource
    private ShellInfoService shellInfoService;
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
    private AccountInfoService accountInfoService;
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
        params.put("hostname", agentJsonObject.get("hostname").toString());
        try {
            params.put("active", "1");
            List<DockerInfo> dockerInfoList = this.dockerInfoService.selectAllByParams(params);
            if ("1".equals(agentJsonObject.get("fromAgent"))) {
                ArrayList<DockerInfo> dockerInfoListResult = new ArrayList<DockerInfo>();
                for (DockerInfo dockerInfo : dockerInfoList) {
                    DockerInfo dockerInfoTmp = new DockerInfo();
                    dockerInfoTmp.setId(dockerInfo.getId());
                    dockerInfoTmp.setAppType(dockerInfo.getAppType());
                    dockerInfoTmp.setDockerId(dockerInfo.getDockerId());
                    dockerInfoListResult.add(dockerInfoTmp);
                }
                return ResDataUtils.resetSuccessJson(dockerInfoListResult);
            }
            return ResDataUtils.resetSuccessJson(dockerInfoList);
        }
        catch (Exception e) {
            logger.error("agent\u83b7\u53d6docker\u4fe1\u606f\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("agent\u83b7\u53d6docker\u4fe1\u606f\u9519\u8bef", e.toString(), "2");
            return ResDataUtils.resetErrorJson(e.toString());
        }
    }

    @RequestMapping(value={"list"})
    public String dockerInfoList(DockerInfo dockerInfo, Model model, HttpServletRequest request) {
        HashMap<String, Object> params = new HashMap<String, Object>();
        try {
            LicenseUtil.maxLicense_10(model, request, dockerInfo);
            StringBuffer url = new StringBuffer();
            String hostname = null;
            if (!StringUtils.isEmpty((CharSequence)dockerInfo.getHostname())) {
                hostname = dockerInfo.getHostname();
                params.put("hostname", hostname.trim());
                url.append("&hostname=").append(hostname);
            }
            if (!StringUtils.isEmpty((CharSequence)dockerInfo.getAccount())) {
                params.put("account", dockerInfo.getAccount());
                url.append("&account=").append(dockerInfo.getAccount());
            }
            if (!StringUtils.isEmpty((CharSequence)dockerInfo.getState())) {
                params.put("state", dockerInfo.getState());
                url.append("&state=").append(dockerInfo.getState());
            }
            if (!StringUtils.isEmpty((CharSequence)dockerInfo.getGroupId())) {
                params.put("groupId", dockerInfo.getGroupId());
                url.append("&groupId=").append(dockerInfo.getGroupId());
            }
            if (!StringUtils.isEmpty((CharSequence)dockerInfo.getActive())) {
                params.put("active", dockerInfo.getActive());
                url.append("&active=").append(dockerInfo.getActive());
            }
            if (!StringUtils.isEmpty((CharSequence)dockerInfo.getOrderBy())) {
                params.put("orderBy", dockerInfo.getOrderBy());
                params.put("orderType", dockerInfo.getOrderType());
                url.append("&orderBy=").append(dockerInfo.getOrderBy());
                url.append("&orderType=").append(dockerInfo.getOrderType());
            }
            HostUtil.addAccountquery(request, params);
            PageInfo pageInfo = this.dockerInfoService.selectByParams(params, dockerInfo.getPage(), dockerInfo.getPageSize());
            for (DockerInfo dockerInfo1 : (java.util.List<DockerInfo>) pageInfo.getList()) {
                if ("true".equals(this.commonConfig.getUserInfoManage())) {
                    dockerInfo1.setAccount(HostUtil.getAccount(dockerInfo1.getHostname()));
                }
                dockerInfo1.setHostState(HostUtil.getHostState(dockerInfo1.getHostname()));
                dockerInfo1.setHostname(dockerInfo1.getHostname() + HostUtil.addRemark(dockerInfo1.getHostname()));
                if (!"true".equals(this.commonConfig.getShowWarnCount())) continue;
                String warnQueryWd = dockerInfo1.getDockerName() + "\uff0c" + dockerInfo1.getHostname();
                this.logInfoService.warnQueryHandle(dockerInfo1, warnQueryWd);
            }
            if ("true".equals(this.commonConfig.getHostGroup())) {
                this.dockerInfoService.setGroupInList(pageInfo.getList(), model, request);
            }
            HostUtil.addAccountListModel(model);
            PageUtil.initPageNumber(pageInfo, model);
            model.addAttribute("pageUrl", (Object)("/dockerInfo/list?1=1" + url.toString()));
            model.addAttribute("page", (Object)pageInfo);
            model.addAttribute("dockerInfo", (Object)dockerInfo);
        }
        catch (Exception e) {
            logger.error("\u67e5\u8be2docker\u4fe1\u606f\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("\u67e5\u8be2docker\u4fe1\u606f\u9519\u8bef", e.toString(), "2");
        }
        return "docker/list";
    }

    @ResponseBody
    @RequestMapping(value={"saveGroupId"})
    public String saveGroupId(Model model, HttpServletRequest request) {
        try {
            String ids = request.getParameter("ids");
            String[] groupIdsArr = request.getParameterValues("groupId");
            this.dockerInfoService.saveGroupId(ids, groupIdsArr, request);
        }
        catch (Exception e) {
            logger.error("\u4fdd\u5b58docker\u5206\u7ec4\u4fe1\u606f\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("\u4fdd\u5b58docker\u5206\u7ec4\u4fe1\u606f\u9519\u8bef", e.toString(), "2");
        }
        return "redirect:/dockerInfo/list";
    }

    @RequestMapping(value={"save"})
    public String saveDockerInfo(DockerInfo dockerInfo, Model model, HttpServletRequest request) {
        String errorMsg = "\u4fdd\u5b58docker\u4fe1\u606f\u9519\u8bef";
        try {
            if (StringUtils.isEmpty((CharSequence)dockerInfo.getId())) {
                this.dockerInfoService.save(dockerInfo, request);
                this.dockerInfoService.saveLog(request, "\u6dfb\u52a0", dockerInfo);
            } else {
                this.dockerInfoService.updateById(dockerInfo);
                this.dockerInfoService.saveLog(request, "\u4fee\u6539", dockerInfo);
            }
        }
        catch (Exception e) {
            logger.error(errorMsg, (Throwable)e);
            this.logInfoService.save(errorMsg, e.toString(), "2");
        }
        return "redirect:/dockerInfo/list";
    }

    @RequestMapping(value={"saveBatch"})
    public String saveBatchDockerInfo(DockerInfo dockerInfo, Model model, HttpServletRequest request) {
        String errorMsg = "\u6279\u91cf\u4fdd\u5b58docker\u4fe1\u606f\u9519\u8bef";
        try {
            String[] hostnames = request.getParameterValues("hostnames");
            if (null == hostnames || hostnames.length < 1) {
                return "redirect:/dockerInfo/list";
            }
            for (String selectedHost : hostnames) {
                dockerInfo.setHostname(selectedHost);
                this.dockerInfoService.save(dockerInfo, request);
                this.dockerInfoService.saveLog(request, "\u6dfb\u52a0", dockerInfo);
            }
        }
        catch (Exception e) {
            logger.error(errorMsg, (Throwable)e);
            this.logInfoService.save(errorMsg, e.toString(), "2");
        }
        return "redirect:/dockerInfo/list";
    }

    @RequestMapping(value={"edit"})
    public String edit(Model model, HttpServletRequest request) {
        String errorMsg = "\u6dfb\u52a0docker\u4fe1\u606f";
        String id = request.getParameter("id");
        DockerInfo DockerInfo2 = new DockerInfo();
        try {
            HashMap<String, Object> params = new HashMap<String, Object>();
            HostUtil.addAccountquery(request, params);
            List<SystemInfo> systemInfoList = this.systemInfoService.selectAllByParams(params);
            model.addAttribute("systemInfoList", systemInfoList);
            if (StringUtils.isEmpty((CharSequence)id)) {
                model.addAttribute("dockerInfo", (Object)DockerInfo2);
                if (!this.isAddContinue()) {
                    return "redirect:/dockerInfo/list?liceFlage=1";
                }
                return "docker/add";
            }
            DockerInfo2 = this.dockerInfoService.selectById(id);
            model.addAttribute("dockerInfo", (Object)DockerInfo2);
        }
        catch (Exception e) {
            logger.error(errorMsg, (Throwable)e);
            this.logInfoService.save(errorMsg, e.toString(), "2");
        }
        return "docker/add";
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
                this.dockerInfoService.updateActive(params);
            }
        }
        catch (Exception e) {
            logger.error(errorMsg, (Throwable)e);
            this.logInfoService.save(errorMsg, e.toString(), "2");
        }
        return "redirect:/dockerInfo/list";
    }

    @RequestMapping(value={"editBatch"})
    public String editBatch(Model model, HttpServletRequest request) {
        String errorMsg = "\u6279\u91cf\u6dfb\u52a0docker\u4fe1\u606f";
        DockerInfo DockerInfo2 = new DockerInfo();
        try {
            if (!StaticKeys.LICENSE_STATE.equals("1")) {
                return "redirect:/dockerInfo/list?liceFlage=1";
            }
            HashMap<String, Object> params = new HashMap<String, Object>();
            HostUtil.addAccountquery(request, params);
            List<SystemInfo> systemInfoList = this.systemInfoService.selectAllByParams(params);
            model.addAttribute("systemInfoList", systemInfoList);
            model.addAttribute("dockerInfo", (Object)DockerInfo2);
        }
        catch (Exception e) {
            logger.error(errorMsg, (Throwable)e);
            this.logInfoService.save(errorMsg, e.toString(), "2");
        }
        return "docker/addBatch";
    }

    @RequestMapping(value={"view"})
    public String viewChart(Model model, HttpServletRequest request) {
        String errorMsg = "\u67e5\u770bdocker\u4fe1\u606f\u56fe\u8868\u9519\u8bef";
        String id = request.getParameter("id");
        String startTime = request.getParameter("startTime");
        String endTime = request.getParameter("endTime");
        String am = request.getParameter("am");
        DockerInfo dockerInfo = new DockerInfo();
        try {
            dockerInfo = this.dockerInfoService.selectById(id);
            String hostNameSource = dockerInfo.getHostname();
            dockerInfo.setHostname(dockerInfo.getHostname() + HostUtil.addRemark(dockerInfo.getHostname()));
            HashMap<String, Object> params = new HashMap<String, Object>();
            this.dashboardService.setDateParam(am, startTime, endTime, params, model);
            model.addAttribute("amList", this.dashboardService.getAmList());
            params.put("dockerInfoId", dockerInfo.getId());
            model.addAttribute("dockerInfo", (Object)dockerInfo);
            if ("true".equals(this.commonConfig.getHostGroup())) {
                dockerInfo.setGroupId(this.hostGroupService.returnGroupNames(dockerInfo.getGroupId()));
            }
            List<DockerState> dockerStateList = this.dockerStateService.selectAllByParams(params);
            List<DockerState> dockerStateCompressList = HostUtil.compressChartListData(dockerStateList, model);
            this.dockerStateService.setSubtitle(model, dockerStateCompressList);
            model.addAttribute("dockerStateList", (Object)JSONUtil.parseArray(dockerStateCompressList));
            if ("2".equals(dockerInfo.getState())) {
                model.addAttribute("dockerErrorMsg", (Object)this.messageErrorUtils.viewErrorMsgHandler(hostNameSource + "_DOCKER_MSG"));
            } else {
                model.addAttribute("dockerErrorMsg", "success");
            }
        }
        catch (Exception e) {
            logger.error(errorMsg, (Throwable)e);
            this.logInfoService.save(errorMsg, e.toString(), "2");
        }
        return "docker/view";
    }

    @RequestMapping(value={"chartExcel"})
    public void chartExcel(Model model, HttpServletRequest request, HttpServletResponse response) {
        String errorMsg = "docker\u4fe1\u606f\u7edf\u8ba1\u56fe\u5bfc\u51faexcel";
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
            params.put("dockerInfoId", id);
            this.dashboardService.setDateParam(am, startTime, endTime, params, model);
            DockerInfo dockerInfo = this.dockerInfoService.selectById(id);
            this.excelExportService.exportDockerExcel(params, response, dockerInfo);
        }
        catch (Exception e) {
            logger.error(errorMsg, (Throwable)e);
            this.logInfoService.save(errorMsg, e.toString(), "2");
        }
    }

    @RequestMapping(value={"del"})
    public String delete(Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        String errorMsg = "\u5220\u9664docker\u4fe1\u606f\u9519\u8bef";
        DockerInfo DockerInfo2 = new DockerInfo();
        try {
            if (!StringUtils.isEmpty((CharSequence)request.getParameter("id"))) {
                String[] ids;
                for (String id : ids = request.getParameter("id").split(",")) {
                    DockerInfo2 = this.dockerInfoService.selectById(id);
                    this.dockerInfoService.saveLog(request, "\u5220\u9664", DockerInfo2);
                }
                this.dockerInfoService.deleteById(ids);
            }
        }
        catch (Exception e) {
            logger.error(errorMsg, (Throwable)e);
            this.logInfoService.save(errorMsg, e.toString(), "2");
        }
        return "redirect:/dockerInfo/list";
    }

    @ResponseBody
    @RequestMapping(value={"run"})
    public String run(Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        String errorMsg = "docker\u4e0b\u53d1\u811a\u672c\u9519\u8bef\uff1a";
        DockerInfo dockerInfo = new DockerInfo();
        String actionName = "";
        try {
            String containerRunAction = request.getParameter("containerRunAction");
            if (!StringUtils.isEmpty((CharSequence)request.getParameter("id")) && !StringUtils.isEmpty((CharSequence)containerRunAction)) {
                if ("1".equals(containerRunAction)) {
                    actionName = "\u505c\u6b62";
                }
                if ("2".equals(containerRunAction)) {
                    actionName = "\u542f\u52a8";
                }
                if ("3".equals(containerRunAction)) {
                    actionName = "\u91cd\u542f";
                }
                if (StringUtils.isEmpty((CharSequence)actionName)) {
                    logger.error("docker\u811a\u672c\u6267\u884c\u52a8\u4f5c\u53c2\u6570\u4e3a\u7a7a\uff1a" + dockerInfo.getDockerName());
                    return "error";
                }
                dockerInfo = this.dockerInfoService.selectById(request.getParameter("id"));
                this.logInfoService.save(HostUtil.getAccountByRequest(request).getAccount() + "\u624b\u52a8" + actionName + "DOCKRE\u5bb9\u5668\uff1a" + dockerInfo.getHostname() + HostUtil.addRemark(dockerInfo.getHostname()) + "\uff0c" + actionName + "\uff0c" + dockerInfo.getDockerName(), "docker\u6267\u884c\u811a\u672c\u4fe1\u606f\uff1a" + dockerInfo.getHostname() + "\uff0c" + actionName + "\uff0c" + dockerInfo.getGatherDockerNames(), "2");
                ShellInfo shellInfo = new ShellInfo();
                shellInfo.setShellName("\u624b\u52a8" + actionName + "DOCKRE\u5bb9\u5668\uff1a" + dockerInfo.getHostname() + HostUtil.addRemark(dockerInfo.getHostname()) + "\uff0c" + dockerInfo.getDockerName());
                shellInfo.setShellTime("");
                shellInfo.setShell(containerRunAction + "runDockerAjax:" + dockerInfo.getDockerId());
                shellInfo.setShellType("1");
                AccountInfo accountInfo = HostUtil.getAccountByRequest(request);
                if (!"admin".equals(accountInfo.getRole())) {
                    shellInfo.setAccount(accountInfo.getAccount());
                }
                String[] hostname = new String[]{dockerInfo.getHostname()};
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
            }
        }
        catch (Exception e) {
            logger.error(errorMsg, (Throwable)e);
            this.logInfoService.save("docker\u4e0b\u53d1\u811a\u672c\uff1a" + dockerInfo.getHostname() + "\uff0c" + actionName + "\uff0c" + dockerInfo.getDockerName(), errorMsg + e.toString(), "2");
        }
        return "success";
    }

    @RequestMapping(value={"hostList"})
    public String systemInfoList(SystemInfo systemInfo, Model model, HttpServletRequest request) {
        HashMap<String, Object> params = new HashMap<String, Object>();
        try {
            if (!LicenseUtil.checkEnterpriseVersion()) {
                model.addAttribute("tipInfo", "\u63d0\u793a: \u5347\u7ea7\u5230\u4f01\u4e1a\u7248\u53ef\u4ee5\u67e5\u770b\u5168\u91cfDocker\u6570\u636e\u54e6");
            }
            StringBuffer url = new StringBuffer();
            String hostname = null;
            if (!StringUtils.isEmpty((CharSequence)systemInfo.getHostname())) {
                hostname = systemInfo.getHostname();
                params.put("hostname", hostname.trim());
                url.append("&hostname=").append(hostname);
            }
            if (!StringUtils.isEmpty((CharSequence)systemInfo.getState())) {
                params.put("state", systemInfo.getState());
                url.append("&state=").append(systemInfo.getState());
            }
            if (!StringUtils.isEmpty((CharSequence)systemInfo.getAccount())) {
                params.put("account", systemInfo.getAccount());
                url.append("&account=").append(systemInfo.getAccount());
            }
            if (!StringUtils.isEmpty((CharSequence)systemInfo.getGroupId())) {
                params.put("groupId", systemInfo.getGroupId());
                url.append("&groupId=").append(systemInfo.getGroupId());
            }
            HostUtil.addAccountquery(request, params);
            PageInfo pageInfo = this.systemInfoService.selectByParams(params, systemInfo.getPage(), systemInfo.getPageSize());
            if ("true".equals(this.commonConfig.getUserInfoManage())) {
                params.clear();
                List<AccountInfo> accountInfoList = this.accountInfoService.selectAllByParams(params);
                model.addAttribute("accountList", accountInfoList);
            }
            for (SystemInfo systemInfo1 : (java.util.List<SystemInfo>) pageInfo.getList()) {
                List<JSONObject> dockerListAll = this.dockerInfoService.getAllDocker(systemInfo1.getHostname());
                systemInfo1.setProcs(dockerListAll.size() + "");
            }
            if ("true".equals(this.commonConfig.getHostGroup())) {
                this.systemInfoService.setGroupInList(pageInfo.getList(), model, request);
            }
            PageUtil.initPageNumber(pageInfo, model);
            model.addAttribute("pageUrl", (Object)("/dockerInfo/hostList?1=1" + url.toString()));
            model.addAttribute("page", (Object)pageInfo);
            model.addAttribute("systemInfo", (Object)systemInfo);
        }
        catch (Exception e) {
            logger.error("\u67e5\u8be2\u4e3b\u673a\u5217\u8868\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("\u67e5\u8be2\u4e3b\u673a\u5217\u8868\u9519\u8bef", e.toString(), "2");
        }
        return "docker/hostList";
    }

    @RequestMapping(value={"viewAllDockerList"})
    public String viewAllDockerList(DockerInfo dockerInfo, Model model, HttpServletRequest request) {
        String id = request.getParameter("id");
        if (StringUtils.isEmpty((CharSequence)id)) {
            return "error/500";
        }
        try {
            this.dockerInfoService.viewAllDockerHandler(model, id);
        }
        catch (Exception e) {
            logger.error("\u4e3b\u673a\u5168\u91cfDocker\u4fe1\u606f\u67e5\u770b\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("\u4e3b\u673a\u5168\u91cfDocker\u4fe1\u606f\u67e5\u770b\u9519\u8bef", e.toString(), "2");
        }
        return "docker/viewAllDocker";
    }

    private boolean isAddContinue() {
        try {
            HashMap<String, Object> params;
            int size;
            if (!StaticKeys.LICENSE_STATE.equals("1") && (size = this.dockerInfoService.countByParams(params = new HashMap<String, Object>())) >= 10) {
                return false;
            }
        }
        catch (Exception e) {
            logger.error("isAddContinue error", (Throwable)e);
        }
        return true;
    }
}

