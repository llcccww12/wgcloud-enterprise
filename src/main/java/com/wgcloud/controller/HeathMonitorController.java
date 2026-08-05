/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.controller;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.github.pagehelper.PageInfo;
import com.wgcloud.config.CommonConfig;
import com.wgcloud.entity.AccountInfo;
import com.wgcloud.entity.HeathMonitor;
import com.wgcloud.service.AccountInfoService;
import com.wgcloud.service.DashboardService;
import com.wgcloud.service.ExcelExportService;
import com.wgcloud.service.HeathMonitorService;
import com.wgcloud.service.HeathStateService;
import com.wgcloud.service.HostGroupService;
import com.wgcloud.service.LogInfoService;
import com.wgcloud.util.HostUtil;
import com.wgcloud.util.IpUtil;
import com.wgcloud.util.PageUtil;
import com.wgcloud.util.ResDataUtils;
import com.wgcloud.util.ServerBackupUtil;
import com.wgcloud.util.TokenUtils;
import com.wgcloud.util.license.LicenseUtil;
import com.wgcloud.util.staticvar.StaticKeys;
import java.util.Date;
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
import com.wgcloud.entity.HeathState;

@Controller
@RequestMapping(value={"/heathMonitor"})
public class HeathMonitorController {
    private static final Logger logger = LoggerFactory.getLogger(HeathMonitorController.class);
    @Resource
    private HeathMonitorService heathMonitorService;
    @Resource
    private HeathStateService heathStateService;
    @Resource
    private LogInfoService logInfoService;
    @Resource
    private DashboardService dashboardService;
    @Resource
    private AccountInfoService accountInfoService;
    @Resource
    private HostGroupService hostGroupService;
    @Resource
    private ExcelExportService excelExportService;
    @Autowired
    private TokenUtils tokenUtils;
    @Autowired
    private CommonConfig commonConfig;

    @ResponseBody
    @RequestMapping(value={"agentList"})
    public String agentList(@RequestBody String paramBean, HttpServletRequest request) {
        JSONObject agentJsonObject = (JSONObject)JSONUtil.parse((Object)paramBean);
        String checkResult = this.tokenUtils.preOpenDataAPICheck(agentJsonObject);
        if (!StringUtils.isEmpty((CharSequence)checkResult)) {
            return checkResult;
        }
        HashMap<String, Object> params = new HashMap<String, Object>();
        try {
            if (null != agentJsonObject.get("heathNames") && !StringUtils.isEmpty((CharSequence)agentJsonObject.get("heathNames").toString())) {
                params.put("heathNames", agentJsonObject.get("heathNames").toString().split(","));
            }
            params.put("active", "1");
            List<HeathMonitor> heathMonitorList = this.heathMonitorService.selectAllByParams(params);
            if (null != agentJsonObject.get("SERVER_BACKUP_FLAG") && !StringUtils.isEmpty((CharSequence)agentJsonObject.get("SERVER_BACKUP_FLAG").toString())) {
                ServerBackupUtil.cacheSaveHeathMonitorId(heathMonitorList);
                logger.info("server-backup request HeathMonitor-------" + IpUtil.getIpAddr(request));
            }
            return ResDataUtils.resetSuccessJson(heathMonitorList);
        }
        catch (Exception e) {
            logger.error("agent\u83b7\u53d6\u76d1\u63a7\u670d\u52a1\u63a5\u53e3\u4fe1\u606f\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("agent\u83b7\u53d6\u76d1\u63a7\u670d\u52a1\u63a5\u53e3\u4fe1\u606f\u9519\u8bef", e.toString(), "2");
            return ResDataUtils.resetErrorJson(e.toString());
        }
    }

    @RequestMapping(value={"list"})
    public String heathMonitorList(HeathMonitor heathMonitor, Model model, HttpServletRequest request) {
        HashMap<String, Object> params = new HashMap<String, Object>();
        try {
            LicenseUtil.maxLicense_10(model, request, heathMonitor);
            StringBuffer url = new StringBuffer();
            String appName = null;
            String heathStatus = null;
            if (!StringUtils.isEmpty((CharSequence)heathMonitor.getAppName())) {
                appName = heathMonitor.getAppName();
                params.put("appName", appName.trim());
                url.append("&appName=").append(appName);
            }
            if (!StringUtils.isEmpty((CharSequence)heathMonitor.getAccount())) {
                params.put("account", heathMonitor.getAccount());
                url.append("&account=").append(heathMonitor.getAccount());
            }
            if (!StringUtils.isEmpty((CharSequence)heathMonitor.getGroupId())) {
                params.put("groupId", heathMonitor.getGroupId());
                url.append("&groupId=").append(heathMonitor.getGroupId());
            }
            if (!StringUtils.isEmpty((CharSequence)heathMonitor.getHeathStatus())) {
                heathStatus = heathMonitor.getHeathStatus();
                params.put("heathStatus", heathStatus.trim());
                url.append("&heathStatus=").append(heathStatus);
            }
            if (!StringUtils.isEmpty((CharSequence)heathMonitor.getActive())) {
                params.put("active", heathMonitor.getActive());
                url.append("&active=").append(heathMonitor.getActive());
            }
            if (!StringUtils.isEmpty((CharSequence)heathMonitor.getOrderBy())) {
                params.put("orderBy", heathMonitor.getOrderBy());
                params.put("orderType", heathMonitor.getOrderType());
                url.append("&orderBy=").append(heathMonitor.getOrderBy());
                url.append("&orderType=").append(heathMonitor.getOrderType());
            }
            HostUtil.addAccountquery(request, params);
            PageInfo pageInfo = this.heathMonitorService.selectByParamsNoContent(params, heathMonitor.getPage(), heathMonitor.getPageSize());
            for (HeathMonitor heathMonitorTmp : (java.util.List<HeathMonitor>) pageInfo.getList()) {
                if (StringUtils.isEmpty((CharSequence)heathMonitorTmp.getResKeyword())) {
                    heathMonitorTmp.setResKeyword("");
                }
                if (null == heathMonitorTmp.getResTimes()) {
                    heathMonitorTmp.setResTimes(0);
                }
                if (!"true".equals(this.commonConfig.getShowWarnCount())) continue;
                String warnQueryWd = "\u670d\u52a1\u63a5\u53e3\u68c0\u6d4b\u544a\u8b66\uff1a" + heathMonitorTmp.getAppName();
                this.logInfoService.warnQueryHandle(heathMonitorTmp, warnQueryWd);
            }
            this.heathMonitorService.addServerBackMark(pageInfo.getList());
            PageUtil.initPageNumber(pageInfo, model);
            if ("true".equals(this.commonConfig.getHostGroup())) {
                this.heathMonitorService.setGroupInList(pageInfo.getList(), model, request);
            }
            HostUtil.addAccountListModel(model);
            model.addAttribute("pageUrl", (Object)("/heathMonitor/list?1=1" + url.toString()));
            model.addAttribute("page", (Object)pageInfo);
            model.addAttribute("heathMonitor", (Object)heathMonitor);
        }
        catch (Exception e) {
            logger.error("\u67e5\u8be2\u670d\u52a1\u63a5\u53e3\u76d1\u63a7\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("\u67e5\u8be2\u670d\u52a1\u63a5\u53e3\u76d1\u63a7\u9519\u8bef", e.toString(), "2");
        }
        return "heath/list";
    }

    @ResponseBody
    @RequestMapping(value={"saveGroupId"})
    public String saveGroupId(Model model, HttpServletRequest request) {
        try {
            String ids = request.getParameter("ids");
            String[] groupIdsArr = request.getParameterValues("groupId");
            this.heathMonitorService.saveGroupId(ids, groupIdsArr, request);
        }
        catch (Exception e) {
            logger.error("\u4fdd\u5b58\u670d\u52a1\u63a5\u53e3\u6807\u7b7e\u4fe1\u606f\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("\u4fdd\u5b58\u670d\u52a1\u63a5\u53e3\u6807\u7b7e\u4fe1\u606f\u9519\u8bef", e.toString(), "2");
        }
        return "redirect:/heath/list";
    }

    @RequestMapping(value={"save"})
    public String saveHeathMonitor(HeathMonitor heathMonitor, Model model, HttpServletRequest request) {
        try {
            if (StringUtils.isEmpty((CharSequence)heathMonitor.getId())) {
                AccountInfo accountInfo = HostUtil.getAccountByRequest(request);
                if (null != accountInfo && !"admin".equals(accountInfo.getRole())) {
                    heathMonitor.setAccount(accountInfo.getAccount());
                }
                this.heathMonitorService.save(heathMonitor);
                this.heathMonitorService.saveLog(request, "\u6dfb\u52a0", heathMonitor);
            } else {
                this.heathMonitorService.updateById(heathMonitor);
                this.heathMonitorService.saveLog(request, "\u4fee\u6539", heathMonitor);
            }
        }
        catch (Exception e) {
            logger.error("\u4fdd\u5b58\u670d\u52a1\u5fc3\u8df3\u76d1\u63a7\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("\u4fdd\u5b58\u670d\u52a1\u5fc3\u8df3\u76d1\u63a7\u9519\u8bef", e.toString(), "2");
        }
        return "redirect:/heathMonitor/list";
    }

    @ResponseBody
    @RequestMapping(value={"testHeath"})
    public String testHeath(Model model, HttpServletRequest request) {
        String errorMsg = "\u670d\u52a1\u63a5\u53e3\u76d1\u63a7\u6d4b\u8bd5\u8fde\u63a5\u9519\u8bef\uff1a";
        String resultMsg = "success";
        try {
            if (!StaticKeys.LICENSE_STATE.equals("1")) {
                return "noPro";
            }
            String id = request.getParameter("id");
            if (!StringUtils.isEmpty((CharSequence)id)) {
                HeathMonitor heathMonitor = this.heathMonitorService.selectById(id);
                HeathMonitor heathMonitorForUpdate = this.heathMonitorService.testHeathMonitor(heathMonitor, new Date());
                int status = Integer.valueOf(heathMonitorForUpdate.getHeathStatus());
                if (status != 200) {
                    resultMsg = "\u6d4b\u8bd5\u5b8c\u6210,\u8fd4\u56de\u72b6\u6001\u7801" + status;
                }
                if (!StringUtils.isEmpty((CharSequence)heathMonitorForUpdate.getErrorMsg())) {
                    resultMsg = resultMsg + "\uff0c" + heathMonitorForUpdate.getErrorMsg();
                }
            } else {
                resultMsg = "\u7ed3\u679c\u597d\u50cf\u662f\u7a7a\u7684";
            }
        }
        catch (Exception e) {
            logger.error(errorMsg, (Throwable)e);
            resultMsg = "\u6d4b\u8bd5\u9519\u8bef:" + e.toString();
        }
        return resultMsg;
    }

    @RequestMapping(value={"edit"})
    public String edit(Model model, HttpServletRequest request) {
        String errorMsg = "\u7f16\u8f91\u670d\u52a1\u63a5\u53e3\u76d1\u63a7";
        String id = request.getParameter("id");
        HeathMonitor heathMonitor = new HeathMonitor();
        try {
            if (StringUtils.isEmpty((CharSequence)id)) {
                model.addAttribute("heathMonitor", (Object)heathMonitor);
                if (!this.isAddContinue()) {
                    return "redirect:/heathMonitor/list?liceFlage=1";
                }
                return "heath/add";
            }
            heathMonitor = this.heathMonitorService.selectById(id);
            this.heathMonitorService.displayHeaderJson(heathMonitor);
            this.heathMonitorService.displayFormJson(heathMonitor);
            model.addAttribute("heathMonitor", (Object)heathMonitor);
        }
        catch (Exception e) {
            logger.error(errorMsg, (Throwable)e);
            this.logInfoService.save(errorMsg, e.toString(), "2");
        }
        return "heath/add";
    }

    @RequestMapping(value={"viewToPage"})
    public String viewToPage(Model model, HttpServletRequest request) {
        String errorMsg = "\u67e5\u770b\u670d\u52a1\u63a5\u53e3\u76d1\u63a7";
        String id = request.getParameter("id");
        HeathMonitor heathMonitor = new HeathMonitor();
        try {
            heathMonitor = this.heathMonitorService.selectById(id);
            this.heathMonitorService.displayHeaderJson(heathMonitor);
            if ("true".equals(this.commonConfig.getHostGroup())) {
                heathMonitor.setGroupId(this.hostGroupService.returnGroupNames(heathMonitor.getGroupId()));
            }
            this.heathMonitorService.displayFormJson(heathMonitor);
            model.addAttribute("heathMonitor", (Object)heathMonitor);
        }
        catch (Exception e) {
            logger.error(errorMsg, (Throwable)e);
            this.logInfoService.save(errorMsg, e.toString(), "2");
        }
        return "heath/viewPage";
    }

    @RequestMapping(value={"view"})
    public String view(Model model, HttpServletRequest request) {
        String errorMsg = "\u67e5\u770b\u670d\u52a1\u63a5\u53e3\u7edf\u8ba1\u56fe\u8868\u9519\u8bef";
        String id = request.getParameter("id");
        String startTime = request.getParameter("startTime");
        String endTime = request.getParameter("endTime");
        String am = request.getParameter("am");
        HeathMonitor heathMonitor = new HeathMonitor();
        try {
            heathMonitor = this.heathMonitorService.selectById(id);
            HashMap<String, Object> params = new HashMap<String, Object>();
            model.addAttribute("heathMonitor", (Object)heathMonitor);
            this.dashboardService.setDateParam(am, startTime, endTime, params, model);
            model.addAttribute("amList", this.dashboardService.getAmList());
            params.put("heathId", heathMonitor.getId());
            List<HeathState> heathStateList = this.heathStateService.selectAllByParams(params);
            List<HeathState> heathStateCompressList = HostUtil.compressChartListData(heathStateList, model);
            this.heathStateService.setSubtitle(model, heathStateCompressList);
            model.addAttribute("heathStateList", (Object)JSONUtil.parseArray(heathStateCompressList));
            model.addAttribute("heathMonitorResDto", (Object)this.heathMonitorService.getHeathMonitorResFromMem(id));
        }
        catch (Exception e) {
            logger.error(errorMsg, (Throwable)e);
            this.logInfoService.save(errorMsg, e.toString(), "2");
        }
        return "heath/view";
    }

    @RequestMapping(value={"chartExcel"})
    public void chartExcel(Model model, HttpServletRequest request, HttpServletResponse response) {
        String errorMsg = "\u670d\u52a1\u63a5\u53e3\u7edf\u8ba1\u56fe\u5bfc\u51faexcel\u9519\u8bef";
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
            params.put("heathId", id);
            this.dashboardService.setDateParam(am, startTime, endTime, params, model);
            HeathMonitor heathMonitor = this.heathMonitorService.selectById(id);
            this.excelExportService.exportHeathExcel(params, response, heathMonitor);
        }
        catch (Exception e) {
            logger.error(errorMsg, (Throwable)e);
            this.logInfoService.save(errorMsg, e.toString(), "2");
        }
    }

    @RequestMapping(value={"del"})
    public String delete(Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        String errorMsg = "\u5220\u9664\u670d\u52a1\u63a5\u53e3\u76d1\u63a7\u9519\u8bef";
        HeathMonitor HeathMonitor2 = new HeathMonitor();
        try {
            if (!StringUtils.isEmpty((CharSequence)request.getParameter("id"))) {
                String[] ids;
                for (String id : ids = request.getParameter("id").split(",")) {
                    HeathMonitor2 = this.heathMonitorService.selectById(id);
                    this.heathMonitorService.saveLog(request, "\u5220\u9664", HeathMonitor2);
                }
                this.heathMonitorService.deleteById(ids);
            }
        }
        catch (Exception e) {
            logger.error(errorMsg, (Throwable)e);
            this.logInfoService.save(errorMsg, e.toString(), "2");
        }
        return "redirect:/heathMonitor/list";
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
                this.heathMonitorService.updateActive(params);
            }
        }
        catch (Exception e) {
            logger.error(errorMsg, (Throwable)e);
            this.logInfoService.save(errorMsg, e.toString(), "2");
        }
        return "redirect:/heathMonitor/list";
    }

    private boolean isAddContinue() {
        try {
            HashMap<String, Object> params;
            int dbSize;
            if (!StaticKeys.LICENSE_STATE.equals("1") && (dbSize = this.heathMonitorService.countByParams(params = new HashMap<String, Object>())) >= 10) {
                return false;
            }
        }
        catch (Exception e) {
            logger.error("isAddContinue error", (Throwable)e);
        }
        return true;
    }
}

