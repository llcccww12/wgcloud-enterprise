/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.controller;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.github.pagehelper.PageInfo;
import com.wgcloud.config.CommonConfig;
import com.wgcloud.entity.AccountInfo;
import com.wgcloud.entity.SnmpInfo;
import com.wgcloud.service.DashboardService;
import com.wgcloud.service.ExcelExportService;
import com.wgcloud.service.HostGroupService;
import com.wgcloud.service.LogInfoService;
import com.wgcloud.service.SnmpInfoService;
import com.wgcloud.service.SnmpStateService;
import com.wgcloud.util.FormatUtil;
import com.wgcloud.util.HostUtil;
import com.wgcloud.util.IpUtil;
import com.wgcloud.util.MessageErrorUtils;
import com.wgcloud.util.PageUtil;
import com.wgcloud.util.ResDataUtils;
import com.wgcloud.util.ServerBackupUtil;
import com.wgcloud.util.SnmpTestUtil;
import com.wgcloud.util.SnmpUtil;
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
import com.wgcloud.entity.SnmpState;

@Controller
@RequestMapping(value={"/snmpInfo"})
public class SnmpInfoController {
    private static final Logger logger = LoggerFactory.getLogger(SnmpInfoController.class);
    @Resource
    private SnmpInfoService snmpInfoService;
    @Resource
    private SnmpStateService snmpStateService;
    @Resource
    private LogInfoService logInfoService;
    @Resource
    private DashboardService dashboardService;
    @Resource
    private ExcelExportService excelExportService;
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
    public String agentList(@RequestBody String paramBean, HttpServletRequest request) {
        JSONObject agentJsonObject = (JSONObject)JSONUtil.parse((Object)paramBean);
        String checkResult = this.tokenUtils.preOpenDataAPICheck(agentJsonObject);
        if (!StringUtils.isEmpty((CharSequence)checkResult)) {
            return checkResult;
        }
        HashMap<String, Object> params = new HashMap<String, Object>();
        try {
            if (null != agentJsonObject.get("snmpHostNames") && !StringUtils.isEmpty((CharSequence)agentJsonObject.get("snmpHostNames").toString())) {
                params.put("snmpHostNames", agentJsonObject.get("snmpHostNames").toString().split(","));
            }
            params.put("active", "1");
            List<SnmpInfo> snmpInfoList = this.snmpInfoService.selectAllByParams(params);
            if (null != agentJsonObject.get("SERVER_BACKUP_FLAG") && !StringUtils.isEmpty((CharSequence)agentJsonObject.get("SERVER_BACKUP_FLAG").toString())) {
                ServerBackupUtil.cacheSaveSnmpInfoId(snmpInfoList);
                logger.info("server-backup request SnmpInfo-------" + IpUtil.getIpAddr(request));
            }
            return ResDataUtils.resetSuccessJson(snmpInfoList);
        }
        catch (Exception e) {
            logger.error("agent\u83b7\u53d6snmp\u8bbe\u5907\u76d1\u6d4b\u4fe1\u606f\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("agent\u83b7\u53d6snmp\u8bbe\u5907\u76d1\u6d4b\u4fe1\u606f\u9519\u8bef", e.toString(), "2");
            return ResDataUtils.resetErrorJson(e.toString());
        }
    }

    @RequestMapping(value={"list"})
    public String snmpInfoList(SnmpInfo snmpInfo, Model model, HttpServletRequest request) {
        HashMap<String, Object> params = new HashMap<String, Object>();
        try {
            LicenseUtil.maxLicense_10(model, request, snmpInfo);
            StringBuffer url = new StringBuffer();
            String hostName = null;
            if (!StringUtils.isEmpty((CharSequence)snmpInfo.getHostname())) {
                hostName = snmpInfo.getHostname();
                params.put("hostname", hostName.trim());
                url.append("&hostname=").append(hostName);
            }
            if (!StringUtils.isEmpty((CharSequence)snmpInfo.getAccount())) {
                params.put("account", snmpInfo.getAccount());
                url.append("&account=").append(snmpInfo.getAccount());
            }
            if (!StringUtils.isEmpty((CharSequence)snmpInfo.getGroupId())) {
                params.put("groupId", snmpInfo.getGroupId());
                url.append("&groupId=").append(snmpInfo.getGroupId());
            }
            if (!StringUtils.isEmpty((CharSequence)snmpInfo.getOrderBy())) {
                params.put("orderBy", snmpInfo.getOrderBy());
                params.put("orderType", snmpInfo.getOrderType());
                url.append("&orderBy=").append(snmpInfo.getOrderBy());
                url.append("&orderType=").append(snmpInfo.getOrderType());
            }
            if (!StringUtils.isEmpty((CharSequence)snmpInfo.getState())) {
                params.put("state", snmpInfo.getState());
                url.append("&state=").append(snmpInfo.getState());
            }
            if (!StringUtils.isEmpty((CharSequence)snmpInfo.getActive())) {
                params.put("active", snmpInfo.getActive());
                url.append("&active=").append(snmpInfo.getActive());
            }
            HostUtil.addAccountquery(request, params);
            PageInfo pageInfo = this.snmpInfoService.selectByParams(params, snmpInfo.getPage(), snmpInfo.getPageSize());
            for (SnmpInfo snmpInfoTmp : (java.util.List<SnmpInfo>) pageInfo.getList()) {
                if ("true".equals(this.commonConfig.getShowWarnCount())) {
                    String warnQueryWd = "snmp\u8bbe\u5907\u76d1\u6d4b\u544a\u8b66\uff1a" + snmpInfoTmp.getHostname();
                    this.logInfoService.warnQueryHandle(snmpInfoTmp, warnQueryWd);
                }
                snmpInfoTmp.setBytesRecv(FormatUtil.bytesFormatUnit(snmpInfoTmp.getBytesRecv(), snmpInfoTmp.getSnmpUnit()));
                snmpInfoTmp.setBytesSent(FormatUtil.bytesFormatUnit(snmpInfoTmp.getBytesSent(), snmpInfoTmp.getSnmpUnit()));
                if (StringUtils.isEmpty((CharSequence)snmpInfoTmp.getCpuPerOID())) {
                    snmpInfoTmp.setCpuPer("NaN");
                }
                if (StringUtils.isEmpty((CharSequence)snmpInfoTmp.getMemSizeOID())) {
                    snmpInfoTmp.setMemPer("NaN");
                }
                if (StringUtils.isEmpty((CharSequence)snmpInfoTmp.getDiskPerOid())) {
                    snmpInfoTmp.setDiskPer("NaN");
                }
                if (StringUtils.isEmpty((CharSequence)snmpInfoTmp.getTemperatureOid())) {
                    snmpInfoTmp.setTemperatureValue("NaN");
                }
                if (StringUtils.isEmpty((CharSequence)snmpInfoTmp.getVoltageOid())) {
                    snmpInfoTmp.setVoltageValue("NaN");
                } else {
                    snmpInfoTmp.setVoltageValue(FormatUtil.secondsToDays(snmpInfoTmp.getVoltageValue()));
                }
                if (StringUtils.isEmpty((CharSequence)snmpInfoTmp.getSentOID())) {
                    snmpInfoTmp.setBytesSent("NaN");
                }
                if (StringUtils.isEmpty((CharSequence)snmpInfoTmp.getRecvOID())) {
                    snmpInfoTmp.setBytesRecv("NaN");
                }
                this.snmpInfoService.countAllIfOperStatus(snmpInfoTmp);
            }
            this.snmpInfoService.addServerBackMark(pageInfo.getList());
            PageUtil.initPageNumber(pageInfo, model);
            if ("true".equals(this.commonConfig.getHostGroup())) {
                this.snmpInfoService.setGroupInList(pageInfo.getList(), model, request);
            }
            HostUtil.addAccountListModel(model);
            model.addAttribute("pageUrl", (Object)("/snmpInfo/list?1=1" + url.toString()));
            model.addAttribute("page", (Object)pageInfo);
            model.addAttribute("snmpInfo", (Object)snmpInfo);
        }
        catch (Exception e) {
            logger.error("\u67e5\u8be2snmp\u8bbe\u5907\u76d1\u6d4b\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("\u67e5\u8be2snmp\u8bbe\u5907\u76d1\u6d4b\u9519\u8bef", e.toString(), "2");
        }
        return "snmp/list";
    }

    @ResponseBody
    @RequestMapping(value={"saveGroupId"})
    public String saveGroupId(Model model, HttpServletRequest request) {
        try {
            String ids = request.getParameter("ids");
            String[] groupIdsArr = request.getParameterValues("groupId");
            this.snmpInfoService.saveGroupId(ids, groupIdsArr, request);
        }
        catch (Exception e) {
            logger.error("\u4fdd\u5b58SNMP\u6807\u7b7e\u4fe1\u606f\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("\u4fdd\u5b58SNMP\u6807\u7b7e\u4fe1\u606f\u9519\u8bef", e.toString(), "2");
        }
        return "redirect:/snmpInfo/list";
    }

    @RequestMapping(value={"save"})
    public String saveSnmpInfo(SnmpInfo snmpInfo, Model model, HttpServletRequest request) {
        String errorMsg = "\u4fdd\u5b58snmp\u8bbe\u5907\u76d1\u6d4b\u9519\u8bef";
        try {
            if (StringUtils.isEmpty((CharSequence)snmpInfo.getId())) {
                AccountInfo accountInfo = HostUtil.getAccountByRequest(request);
                if (null != accountInfo && !"admin".equals(accountInfo.getRole())) {
                    snmpInfo.setAccount(accountInfo.getAccount());
                }
                this.snmpInfoService.save(snmpInfo);
                this.snmpInfoService.saveLog(request, "\u6dfb\u52a0", snmpInfo);
            } else {
                this.snmpInfoService.updateById(snmpInfo);
                this.snmpInfoService.saveLog(request, "\u4fee\u6539", snmpInfo);
            }
        }
        catch (Exception e) {
            logger.error(errorMsg, (Throwable)e);
            this.logInfoService.save(errorMsg, e.toString(), "2");
        }
        return "redirect:/snmpInfo/list";
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
                this.snmpInfoService.updateActive(params);
            }
        }
        catch (Exception e) {
            logger.error(errorMsg, (Throwable)e);
            this.logInfoService.save(errorMsg, e.toString(), "2");
        }
        return "redirect:/snmpInfo/list";
    }

    @ResponseBody
    @RequestMapping(value={"testHeath"})
    public String testHeath(Model model, HttpServletRequest request) {
        String errorMsg = "SNMP\u76d1\u63a7\u6d4b\u8bd5\u8fde\u63a5\u9519\u8bef\uff1a";
        String resultMsg = "success";
        try {
            if (!StaticKeys.LICENSE_STATE.equals("1")) {
                return "noPro";
            }
            String id = request.getParameter("id");
            if (!StringUtils.isEmpty((CharSequence)id)) {
                SnmpInfo snmpInfo = this.snmpInfoService.selectById(id);
                if (!SnmpUtil.isEthernetConnection(snmpInfo.getHostname())) {
                    return "\u6d4b\u8bd5\u8fde\u63a5\u5931\u8d25";
                }
                SnmpInfo snmpInfoForUpdate = new SnmpInfo();
                snmpInfoForUpdate.setId(snmpInfo.getId());
                SnmpInfo snmpInfoResult = null;
                snmpInfoResult = SnmpUtil.getAvgSnmpInfo(snmpInfo);
                snmpInfoForUpdate.setCreateTime(new Date());
                snmpInfoForUpdate.setState("1");
                if (null != snmpInfoResult) {
                    snmpInfoForUpdate.setRecvAvg(snmpInfoResult.getRecvAvg());
                    snmpInfoForUpdate.setSentAvg(snmpInfoResult.getSentAvg());
                    snmpInfoForUpdate.setBytesSent(snmpInfoResult.getBytesSent());
                    snmpInfoForUpdate.setBytesRecv(snmpInfoResult.getBytesRecv());
                    snmpInfoForUpdate.setMemPer(snmpInfoResult.getMemPer());
                    snmpInfoForUpdate.setCpuPer(snmpInfoResult.getCpuPer());
                    snmpInfoForUpdate.setDiskPer(snmpInfoResult.getDiskPer());
                    snmpInfoForUpdate.setTemperatureValue(snmpInfoResult.getTemperatureValue());
                    snmpInfoForUpdate.setVoltageValue(snmpInfoResult.getVoltageValue());
                    snmpInfoForUpdate.setIfOperStatusValue(snmpInfoResult.getIfOperStatusValue());
                    snmpInfoForUpdate.setSysDescVal(snmpInfoResult.getSysDescVal());
                    this.messageErrorUtils.setErrorMsgHandler(snmpInfoResult.getId(), snmpInfoResult.getTestErrorMsg());
                }
                if (null == snmpInfoResult) {
                    snmpInfoForUpdate.setCreateTime(null);
                    snmpInfoForUpdate.setState("2");
                    this.messageErrorUtils.setErrorMsgHandler(snmpInfo.getId(), "Cannot ping device");
                }
                if (!StringUtils.isEmpty((CharSequence)snmpInfoResult.getTestErrorMsg())) {
                    resultMsg = snmpInfoResult.getTestErrorMsg();
                }
                try {
                    this.snmpInfoService.updateById(snmpInfoForUpdate);
                }
                catch (Exception e) {
                    e.printStackTrace();
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
        String errorMsg = "\u7f16\u8f91snmp\u8bbe\u5907\u76d1\u6d4b";
        String id = request.getParameter("id");
        SnmpInfo snmpInfo = new SnmpInfo();
        try {
            if (StringUtils.isEmpty((CharSequence)id)) {
                snmpInfo.setSnmpPort("161");
                snmpInfo.setSnmpCommunity("public");
                snmpInfo.setSnmpVersion("1");
                snmpInfo.setRecvOID("1.3.6.1.2.1.2.2.1.10");
                snmpInfo.setSentOID("1.3.6.1.2.1.2.2.1.16");
                model.addAttribute("snmpInfo", (Object)snmpInfo);
                if (!this.isAddContinue()) {
                    return "redirect:/snmpInfo/list?liceFlage=1";
                }
                return "snmp/add";
            }
            snmpInfo = this.snmpInfoService.selectById(id);
            model.addAttribute("snmpInfo", (Object)snmpInfo);
        }
        catch (Exception e) {
            logger.error(errorMsg, (Throwable)e);
            this.logInfoService.save(errorMsg, e.toString(), "2");
        }
        return "snmp/add";
    }

    @RequestMapping(value={"view"})
    public String view(Model model, HttpServletRequest request) {
        String errorMsg = "\u67e5\u770bsnmp\u8bbe\u5907\u56fe\u8868\u9519\u8bef";
        String id = request.getParameter("id");
        String startTime = request.getParameter("startTime");
        String endTime = request.getParameter("endTime");
        String am = request.getParameter("am");
        SnmpInfo snmpInfo = new SnmpInfo();
        try {
            snmpInfo = this.snmpInfoService.selectById(id);
            HashMap<String, Object> params = new HashMap<String, Object>();
            model.addAttribute("snmpInfo", (Object)snmpInfo);
            if ("true".equals(this.commonConfig.getHostGroup())) {
                snmpInfo.setGroupId(this.hostGroupService.returnGroupNames(snmpInfo.getGroupId()));
            }
            snmpInfo.setVoltageValue(FormatUtil.secondsToDays(snmpInfo.getVoltageValue()));
            this.dashboardService.setDateParam(am, startTime, endTime, params, model);
            model.addAttribute("amList", this.dashboardService.getAmList());
            params.put("snmpInfoId", snmpInfo.getId());
            List<SnmpState> snmpStateList = this.snmpStateService.selectAllByParams(params);
            List<SnmpState> snmpStateCompressList = HostUtil.compressChartListData(snmpStateList, model);
            this.snmpStateService.setSubtitle(model, snmpStateCompressList, snmpInfo);
            model.addAttribute("snmpStateList", (Object)JSONUtil.parseArray(snmpStateCompressList));
            this.snmpInfoService.displayAllIfOperStatus(snmpInfo, model);
            this.snmpInfoService.displayAllIfOperSpeed(snmpInfo, model);
            String errorMsgMonitor = this.messageErrorUtils.viewErrorMsgHandler(id);
            if (!StringUtils.isEmpty((CharSequence)errorMsgMonitor) && errorMsgMonitor.startsWith(",")) {
                errorMsgMonitor = errorMsgMonitor.substring(1);
            }
            model.addAttribute("snmpInfoErrorMsg", (Object)errorMsgMonitor);
        }
        catch (Exception e) {
            logger.error(errorMsg, (Throwable)e);
            this.logInfoService.save(errorMsg, e.toString(), "2");
        }
        return "snmp/view";
    }

    @RequestMapping(value={"chartExcel"})
    public void chartExcel(Model model, HttpServletRequest request, HttpServletResponse response) {
        String errorMsg = "snmp\u8bbe\u5907\u7edf\u8ba1\u56fe\u5bfc\u51faexcel\u9519\u8bef";
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
            params.put("snmpInfoId", id);
            SnmpInfo snmpInfo = this.snmpInfoService.selectById(id);
            this.dashboardService.setDateParam(am, startTime, endTime, params, model);
            this.excelExportService.exportSnmpExcel(params, response, snmpInfo);
        }
        catch (Exception e) {
            logger.error(errorMsg, (Throwable)e);
            this.logInfoService.save(errorMsg, e.toString(), "2");
        }
    }

    @RequestMapping(value={"del"})
    public String delete(Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        String errorMsg = "\u5220\u9664snmp\u8bbe\u5907\u76d1\u6d4b\u9519\u8bef";
        SnmpInfo SnmpInfo2 = new SnmpInfo();
        try {
            if (!StringUtils.isEmpty((CharSequence)request.getParameter("id"))) {
                String[] ids;
                for (String id : ids = request.getParameter("id").split(",")) {
                    SnmpInfo2 = this.snmpInfoService.selectById(id);
                    this.snmpInfoService.saveLog(request, "\u5220\u9664", SnmpInfo2);
                }
                this.snmpInfoService.deleteById(ids);
            }
        }
        catch (Exception e) {
            logger.error(errorMsg, (Throwable)e);
            this.logInfoService.save(errorMsg, e.toString(), "2");
        }
        return "redirect:/snmpInfo/list";
    }

    @ResponseBody
    @RequestMapping(value={"testResultData"})
    public String testResultData(SnmpInfo snmpInfo, Model model, HttpServletRequest request) {
        String result = "";
        try {
            String testOid = request.getParameter("testOid");
            String snmpType = request.getParameter("snmpType");
            if (!StringUtils.isEmpty((CharSequence)testOid)) {
                result = "SNMPGET".equals(snmpType) ? SnmpTestUtil.snmpGet(snmpInfo, testOid) : SnmpTestUtil.walkSnmp(snmpInfo, testOid);
            }
        }
        catch (Exception e) {
            logger.error("\u6d4b\u8bd5\u83b7\u53d6OID\u4fe1\u606f\u9519\u8bef", (Throwable)e);
            result = e.toString();
        }
        return result;
    }

    private boolean isAddContinue() {
        try {
            HashMap<String, Object> params = new HashMap<String, Object>();
            int size = this.snmpInfoService.countByParams(params);
            if (!(size < 10 || size < StaticKeys.LICENSE_NUM && StaticKeys.LICENSE_STATE.equals("1"))) {
                return false;
            }
        }
        catch (Exception e) {
            logger.error("isAddContinue error", (Throwable)e);
        }
        return true;
    }
}

