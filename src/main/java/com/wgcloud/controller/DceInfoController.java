/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.controller;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.github.pagehelper.PageInfo;
import com.wgcloud.config.CommonConfig;
import com.wgcloud.entity.AccountInfo;
import com.wgcloud.entity.DceInfo;
import com.wgcloud.entity.HostGroup;
import com.wgcloud.entity.SystemInfo;
import com.wgcloud.service.DashboardService;
import com.wgcloud.service.DceInfoService;
import com.wgcloud.service.DceStateService;
import com.wgcloud.service.ExcelExportService;
import com.wgcloud.service.HostGroupService;
import com.wgcloud.service.LogInfoService;
import com.wgcloud.service.SystemInfoService;
import com.wgcloud.util.HostUtil;
import com.wgcloud.util.IpUtil;
import com.wgcloud.util.MessageErrorUtils;
import com.wgcloud.util.PageUtil;
import com.wgcloud.util.ResDataUtils;
import com.wgcloud.util.ServerBackupUtil;
import com.wgcloud.util.TokenUtils;
import com.wgcloud.util.license.LicenseUtil;
import com.wgcloud.util.staticvar.StaticKeys;
import java.util.ArrayList;
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
import com.wgcloud.entity.DceState;

@Controller
@RequestMapping(value={"/dceInfo"})
public class DceInfoController {
    private static final Logger logger = LoggerFactory.getLogger(DceInfoController.class);
    @Resource
    private DceInfoService dceInfoService;
    @Resource
    private SystemInfoService systemInfoService;
    @Resource
    private DceStateService dceStateService;
    @Resource
    private LogInfoService logInfoService;
    @Resource
    private DashboardService dashboardService;
    @Resource
    private HostGroupService hostGroupService;
    @Resource
    private ExcelExportService excelExportService;
    @Autowired
    private TokenUtils tokenUtils;
    @Autowired
    private CommonConfig commonConfig;
    @Autowired
    private MessageErrorUtils messageErrorUtils;

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
            if (null != agentJsonObject.get("dceHostNames") && !StringUtils.isEmpty((CharSequence)agentJsonObject.get("dceHostNames").toString())) {
                params.put("dceHostNames", agentJsonObject.get("dceHostNames").toString().split(","));
            }
            if (null != agentJsonObject.get("groupName") && !StringUtils.isEmpty((CharSequence)agentJsonObject.get("groupName").toString())) {
                HashMap<String, Object> paramsGroup = new HashMap<String, Object>();
                paramsGroup.put("groupName", agentJsonObject.get("groupName").toString());
                List<HostGroup> hostGroupList = this.hostGroupService.selectAllByParams(paramsGroup, null);
                if (hostGroupList.size() > 0) {
                    params.put("groupId", hostGroupList.get(0).getId());
                } else {
                    logger.error("groupName is error");
                    return ResDataUtils.resetErrorJson("groupName is error");
                }
            }
            params.put("active", "1");
            List<DceInfo> dceInfoList = this.dceInfoService.selectAllByParams(params);
            if (null != agentJsonObject.get("SERVER_BACKUP_FLAG") && !StringUtils.isEmpty((CharSequence)agentJsonObject.get("SERVER_BACKUP_FLAG").toString())) {
                ServerBackupUtil.cacheSaveDceInfoId(dceInfoList);
                logger.info("server-backup request DceInfo-------" + IpUtil.getIpAddr(request));
            }
            return ResDataUtils.resetSuccessJson(dceInfoList);
        }
        catch (Exception e) {
            logger.error("agent\u83b7\u53d6\u6570\u63a7\u8bbe\u5907PING\u76d1\u6d4b\u4fe1\u606f\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("agent\u83b7\u53d6\u6570\u63a7\u8bbe\u5907PING\u76d1\u6d4b\u4fe1\u606f\u9519\u8bef", e.toString(), "2");
            return ResDataUtils.resetErrorJson(e.toString());
        }
    }

    @ResponseBody
    @RequestMapping(value={"checkRepeat"})
    public String checkRepeat(Model model, HttpServletRequest request) {
        try {
            HashMap<String, Object> params = new HashMap<String, Object>();
            String hostname = request.getParameter("hostname");
            if (StringUtils.isEmpty((CharSequence)hostname)) {
                return "0";
            }
            params.put("hostnameEq", hostname.trim());
            List<DceInfo> list = this.dceInfoService.selectAllByParams(params);
            if (list.size() > 0) {
                return "1";
            }
            return "0";
        }
        catch (Exception e) {
            logger.error("\u67e5\u8be2\u8be5IP\u662f\u5426\u5df2\u7ecf\u8bbe\u7f6e\u8fc7\u81ea\u5b9a\u4e49\u544a\u8b66\u9519\u8bef", (Throwable)e);
            return "success";
        }
    }

    @ResponseBody
    @RequestMapping(value={"savePingListHideCols"})
    public String savePingListHideCols(Model model, HttpServletRequest request) {
        try {
            Object[] hostListHideCols = request.getParameterValues("pingListHideCols");
            if (null != hostListHideCols) {
                request.getSession().setAttribute("PingListHideColsInfo", (Object)StringUtils.join((Object[])hostListHideCols, (String)","));
            } else {
                request.getSession().setAttribute("PingListHideColsInfo", "");
            }
        }
        catch (Exception e) {
            logger.error("\u4fdd\u5b58PING\u5217\u8868\u9700\u8981\u9690\u85cf\u7684\u5217\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("\u4fdd\u5b58PING\u5217\u8868\u9700\u8981\u9690\u85cf\u7684\u5217\u9519\u8bef", e.toString(), "2");
        }
        return "redirect:/dceInfo/list";
    }

    @RequestMapping(value={"list"})
    public String dceInfoList(DceInfo dceInfo, Model model, HttpServletRequest request) {
        HashMap<String, Object> params = new HashMap<String, Object>();
        try {
            LicenseUtil.maxLicense_10(model, request, dceInfo);
            StringBuffer url = new StringBuffer();
            String hostName = null;
            String heathStatus = null;
            if (!StringUtils.isEmpty((CharSequence)dceInfo.getHostname())) {
                hostName = dceInfo.getHostname();
                params.put("hostname", hostName.trim());
                url.append("&hostname=").append(hostName);
            }
            if (!StringUtils.isEmpty((CharSequence)dceInfo.getAccount())) {
                params.put("account", dceInfo.getAccount());
                url.append("&account=").append(dceInfo.getAccount());
            }
            if (!StringUtils.isEmpty((CharSequence)dceInfo.getGroupId())) {
                params.put("groupId", dceInfo.getGroupId());
                url.append("&groupId=").append(dceInfo.getGroupId());
            }
            if (!StringUtils.isEmpty((CharSequence)dceInfo.getActive())) {
                params.put("active", dceInfo.getActive());
                url.append("&active=").append(dceInfo.getActive());
            }
            if (!StringUtils.isEmpty((CharSequence)dceInfo.getOrderBy())) {
                params.put("orderBy", dceInfo.getOrderBy());
                params.put("orderType", dceInfo.getOrderType());
                url.append("&orderBy=").append(dceInfo.getOrderBy());
                url.append("&orderType=").append(dceInfo.getOrderType());
            }
            if (!StringUtils.isEmpty((CharSequence)request.getParameter("heathStatus"))) {
                heathStatus = request.getParameter("heathStatus");
                if ("200".equals(heathStatus)) {
                    params.put("resTimesGt", -1);
                }
                if ("500".equals(heathStatus)) {
                    params.put("resTimes", -1);
                }
                url.append("&heathStatus=").append(heathStatus);
            }
            HostUtil.addAccountquery(request, params);
            PageInfo pageInfo = this.dceInfoService.selectByParams(params, dceInfo.getPage(), dceInfo.getPageSize());
            if ("true".equals(this.commonConfig.getShowWarnCount())) {
                for (DceInfo dceInfoTmp : (java.util.List<DceInfo>) pageInfo.getList()) {
                    String warnQueryWd = "PING\u8d85\u65f6\u544a\u8b66\uff1a" + dceInfoTmp.getHostname();
                    this.logInfoService.warnQueryHandle(dceInfoTmp, warnQueryWd);
                }
            }
            if ("true".equals(this.commonConfig.getHostGroup())) {
                this.dceInfoService.setGroupInList(pageInfo.getList(), model, request);
            }
            HostUtil.addAccountListModel(model);
            this.dceInfoService.addServerBackMark(pageInfo.getList());
            PageUtil.initPageNumber(pageInfo, model);
            model.addAttribute("pageUrl", (Object)("/dceInfo/list?1=1" + url.toString()));
            model.addAttribute("page", (Object)pageInfo);
            model.addAttribute("dceInfo", (Object)dceInfo);
            if (null == request.getSession().getAttribute("PingListHideColsInfo")) {
                request.getSession().setAttribute("PingListHideColsInfo", "01");
            }
        }
        catch (Exception e) {
            logger.error("\u67e5\u8be2\u7f51\u7edc\u8bbe\u5907PING\u76d1\u6d4b\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("\u67e5\u8be2\u7f51\u7edc\u8bbe\u5907PING\u76d1\u6d4b\u9519\u8bef", e.toString(), "2");
        }
        return "dce/list";
    }

    @ResponseBody
    @RequestMapping(value={"saveGroupId"})
    public String saveGroupId(Model model, HttpServletRequest request) {
        try {
            String ids = request.getParameter("ids");
            String[] groupIdsArr = request.getParameterValues("groupId");
            this.dceInfoService.saveGroupId(ids, groupIdsArr, request);
        }
        catch (Exception e) {
            logger.error("\u4fdd\u5b58\u7f51\u7edc\u8bbe\u5907\u5206\u7ec4\u4fe1\u606f\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("\u4fdd\u5b58\u7f51\u7edc\u8bbe\u5907\u5206\u7ec4\u4fe1\u606f\u9519\u8bef", e.toString(), "2");
        }
        return "redirect:/dceInfo/list";
    }

    @RequestMapping(value={"updateOrderNum"})
    public String updateOrderNum(Model model, HttpServletRequest request) {
        String errorMsg = "\u8bbe\u7f6ePING\u6392\u5e8f\u5e8f\u53f7\u9519\u8bef";
        try {
            this.dceInfoService.updateOrderNum(request);
        }
        catch (Exception e) {
            logger.error(errorMsg, (Throwable)e);
            this.logInfoService.save(errorMsg, e.toString(), "2");
        }
        return "redirect:/dceInfo/list";
    }

    @ResponseBody
    @RequestMapping(value={"testHeath"})
    public String testHeath(Model model, HttpServletRequest request) {
        String errorMsg = "PING\u76d1\u63a7\u6d4b\u8bd5\u8fde\u63a5\u9519\u8bef\uff1a";
        String resultMsg = "success";
        try {
            if (!StaticKeys.LICENSE_STATE.equals("1")) {
                return "noPro";
            }
            String id = request.getParameter("id");
            if (!StringUtils.isEmpty((CharSequence)id)) {
                DceInfo dceInfo = this.dceInfoService.selectById(id);
                DceInfo dceInfoForUpdate = this.dceInfoService.testDceInfo(dceInfo, new Date());
                if (dceInfoForUpdate.getResTimes() < 0) {
                    resultMsg = "PING\u68c0\u6d4b\u8d85\u65f6\u6216\u5931\u8d25";
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

    @RequestMapping(value={"save"})
    public String saveDceInfo(DceInfo dceInfo, Model model, HttpServletRequest request) {
        String errorMsg = "\u4fdd\u5b58\u7f51\u7edc\u8bbe\u5907PING\u76d1\u6d4b\u9519\u8bef";
        try {
            if (StringUtils.isEmpty((CharSequence)dceInfo.getId())) {
                AccountInfo accountInfo = HostUtil.getAccountByRequest(request);
                if (null != accountInfo && !"admin".equals(accountInfo.getRole())) {
                    dceInfo.setAccount(accountInfo.getAccount());
                }
                this.dceInfoService.save(dceInfo, request);
                this.dceInfoService.saveLog(request, "\u6dfb\u52a0", dceInfo);
            } else {
                this.dceInfoService.updateById(dceInfo);
                this.dceInfoService.saveLog(request, "\u4fee\u6539", dceInfo);
            }
        }
        catch (Exception e) {
            logger.error(errorMsg, (Throwable)e);
            this.logInfoService.save(errorMsg, e.toString(), "2");
        }
        return "redirect:/dceInfo/list";
    }

    @ResponseBody
    @RequestMapping(value={"importHosts"})
    public String importHosts(HttpServletRequest request) {
        try {
            HashMap<String, Object> map = new HashMap<String, Object>();
            HostUtil.addAccountquery(request, map);
            List<SystemInfo> systemInfoList = this.systemInfoService.selectAllByParams(map);
            List<DceInfo> dceInfoQueryList = this.dceInfoService.selectAllByParams(map);
            ArrayList<DceInfo> dceInfoSaveList = new ArrayList<DceInfo>();
            boolean sign = false;
            for (SystemInfo systemInfo : systemInfoList) {
                DceInfo dceInfo = new DceInfo();
                dceInfo.setHostname(systemInfo.getHostname());
                sign = false;
                for (DceInfo d : dceInfoQueryList) {
                    if (!d.getHostname().equals(systemInfo.getHostname())) continue;
                    logger.info("ping\u76d1\u6d4b\u91cc\u5df2\u7ecf\u6709\u6b64ip\u4e86\uff0c\u4e0d\u5bfc\u5165-----" + systemInfo.getHostname());
                    sign = true;
                    break;
                }
                if (sign) continue;
                dceInfo.setAccount(systemInfo.getAccount());
                dceInfo.setGroupId(systemInfo.getGroupId());
                dceInfo.setActive("1");
                dceInfo.setRemark(systemInfo.getRemark());
                dceInfo.setWarnLevel("ERROR");
                dceInfoSaveList.add(dceInfo);
            }
            this.dceInfoService.saveRecord(dceInfoSaveList);
            this.dceInfoService.saveLog(request, "\u5c06\u76d1\u63a7\u4e3b\u673a\u5bfc\u5165", new DceInfo());
        }
        catch (Exception e) {
            logger.error("\u5c06\u76d1\u63a7\u4e3b\u673a\u5bfc\u5165\u5230\u7f51\u7edc\u8bbe\u5907PING\u5217\u8868\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("\u5c06\u76d1\u63a7\u4e3b\u673a\u5bfc\u5165\u5230\u7f51\u7edc\u8bbe\u5907PING\u5217\u8868\u9519\u8bef", e.toString(), "2");
        }
        return "sucess";
    }

    @RequestMapping(value={"edit"})
    public String edit(Model model, HttpServletRequest request) {
        String errorMsg = "\u7f16\u8f91\u7f51\u7edc\u8bbe\u5907PING\u76d1\u6d4b";
        String id = request.getParameter("id");
        DceInfo dceInfo = new DceInfo();
        try {
            if (StringUtils.isEmpty((CharSequence)id)) {
                model.addAttribute("dceInfo", (Object)dceInfo);
                if (!this.isAddContinue()) {
                    return "redirect:/dceInfo/list?liceFlage=1";
                }
                return "dce/add";
            }
            dceInfo = this.dceInfoService.selectById(id);
            model.addAttribute("dceInfo", (Object)dceInfo);
        }
        catch (Exception e) {
            logger.error(errorMsg, (Throwable)e);
            this.logInfoService.save(errorMsg, e.toString(), "2");
        }
        return "dce/add";
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
                this.dceInfoService.updateActive(params);
            }
        }
        catch (Exception e) {
            logger.error(errorMsg, (Throwable)e);
            this.logInfoService.save(errorMsg, e.toString(), "2");
        }
        return "redirect:/dceInfo/list";
    }

    @RequestMapping(value={"view"})
    public String view(Model model, HttpServletRequest request) {
        String errorMsg = "\u67e5\u770b\u7f51\u7edc\u8bbe\u5907PING\u56fe\u8868\u9519\u8bef";
        String id = request.getParameter("id");
        String startTime = request.getParameter("startTime");
        String endTime = request.getParameter("endTime");
        String am = request.getParameter("am");
        DceInfo dceInfo = new DceInfo();
        try {
            dceInfo = this.dceInfoService.selectById(id);
            HashMap<String, Object> params = new HashMap<String, Object>();
            model.addAttribute("dceInfo", (Object)dceInfo);
            if ("true".equals(this.commonConfig.getHostGroup())) {
                dceInfo.setGroupId(this.hostGroupService.returnGroupNames(dceInfo.getGroupId()));
            }
            this.dashboardService.setDateParam(am, startTime, endTime, params, model);
            model.addAttribute("amList", this.dashboardService.getAmList());
            params.put("dceId", dceInfo.getId());
            List<DceState> dceStateList = this.dceStateService.selectAllByParams(params);
            List<DceState> dceStateCompressList = HostUtil.compressChartListData(dceStateList, model);
            this.dceStateService.setSubtitle(model, dceStateCompressList);
            model.addAttribute("dceStateList", (Object)JSONUtil.parseArray(dceStateCompressList));
            model.addAttribute("dceErrorMsg", (Object)this.messageErrorUtils.viewErrorMsgHandler(id));
        }
        catch (Exception e) {
            logger.error(errorMsg, (Throwable)e);
            this.logInfoService.save(errorMsg, e.toString(), "2");
        }
        return "dce/view";
    }

    @RequestMapping(value={"chartExcel"})
    public void chartExcel(Model model, HttpServletRequest request, HttpServletResponse response) {
        String errorMsg = "ping\u8bbe\u5907\u7edf\u8ba1\u56fe\u5bfc\u51faexcel\u9519\u8bef";
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
            params.put("dceId", id);
            this.dashboardService.setDateParam(am, startTime, endTime, params, model);
            DceInfo dceInfo = this.dceInfoService.selectById(id);
            this.excelExportService.exportDceExcel(params, response, dceInfo);
        }
        catch (Exception e) {
            logger.error(errorMsg, (Throwable)e);
            this.logInfoService.save(errorMsg, e.toString(), "2");
        }
    }

    @RequestMapping(value={"del"})
    public String delete(Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        String errorMsg = "\u5220\u9664\u7f51\u7edc\u8bbe\u5907PING\u76d1\u6d4b\u9519\u8bef";
        DceInfo dceInfo = new DceInfo();
        try {
            if (!StringUtils.isEmpty((CharSequence)request.getParameter("id"))) {
                String[] ids;
                for (String id : ids = request.getParameter("id").split(",")) {
                    dceInfo = this.dceInfoService.selectById(id);
                    this.dceInfoService.saveLog(request, "\u5220\u9664", dceInfo);
                }
                this.dceInfoService.deleteById(ids);
            }
        }
        catch (Exception e) {
            logger.error(errorMsg, (Throwable)e);
            this.logInfoService.save(errorMsg, e.toString(), "2");
        }
        return "redirect:/dceInfo/list";
    }

    @RequestMapping(value={"exportListExcel"})
    public void exportListExcel(DceInfo dceInfo, Model model, HttpServletRequest request, HttpServletResponse response) {
        try {
            if (!StaticKeys.LICENSE_STATE.equals("1")) {
                response.setContentType("text/html;charset=UTF-8");
                response.getOutputStream().write("The module needs to professional version. Please contact us at www.wgstart.com".getBytes());
                return;
            }
            HashMap<String, Object> params = new HashMap<String, Object>();
            String hostname = null;
            String heathStatus = null;
            if (!StringUtils.isEmpty((CharSequence)dceInfo.getHostname())) {
                hostname = dceInfo.getHostname();
                params.put("hostname", hostname.trim());
            }
            if (!StringUtils.isEmpty((CharSequence)dceInfo.getAccount())) {
                params.put("account", dceInfo.getAccount());
            }
            if (!StringUtils.isEmpty((CharSequence)dceInfo.getGroupId())) {
                params.put("groupId", dceInfo.getGroupId());
            }
            if (!StringUtils.isEmpty((CharSequence)request.getParameter("heathStatus"))) {
                heathStatus = request.getParameter("heathStatus");
                if ("200".equals(heathStatus)) {
                    params.put("resTimesGt", -1);
                }
                if ("500".equals(heathStatus)) {
                    params.put("resTimes", -1);
                }
            }
            HostUtil.addAccountquery(request, params);
            PageInfo pageInfo = this.dceInfoService.selectByParams(params, 1, 20000);
            if ("true".equals(this.commonConfig.getHostGroup())) {
                this.dceInfoService.setGroupInList(pageInfo.getList(), model, request);
            }
            this.excelExportService.exportDceInfoListExcel(pageInfo.getList(), response);
        }
        catch (Exception e) {
            logger.error("ping\u5217\u8868\u5bfc\u51faexcel\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("ping\u5217\u8868\u5bfc\u51faexcel\u9519\u8bef", e.toString(), "2");
        }
    }

    private boolean isAddContinue() {
        try {
            HashMap<String, Object> params = new HashMap<String, Object>();
            int dbSize = this.dceInfoService.countByParams(params);
            if (!(dbSize < 10 || dbSize < StaticKeys.LICENSE_NUM && StaticKeys.LICENSE_STATE.equals("1"))) {
                return false;
            }
        }
        catch (Exception e) {
            logger.error("isAddContinue error", (Throwable)e);
        }
        return true;
    }
}

