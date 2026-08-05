/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.github.pagehelper.PageInfo;
import com.wgcloud.config.CommonConfig;
import com.wgcloud.entity.AccountInfo;
import com.wgcloud.entity.SnmpDeepInfo;
import com.wgcloud.entity.SnmpDeepState;
import com.wgcloud.service.DashboardService;
import com.wgcloud.service.ExcelExportService;
import com.wgcloud.service.HostGroupService;
import com.wgcloud.service.LogInfoService;
import com.wgcloud.service.SnmpDeepInfoService;
import com.wgcloud.util.HostUtil;
import com.wgcloud.util.IpUtil;
import com.wgcloud.util.MessageErrorUtils;
import com.wgcloud.util.PageUtil;
import com.wgcloud.util.ResDataUtils;
import com.wgcloud.util.ServerBackupUtil;
import com.wgcloud.util.SnmpDeepUtil;
import com.wgcloud.util.SnmpUtil;
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
@RequestMapping(value={"/snmpDeepInfo"})
public class SnmpDeepInfoController {
    private static final Logger logger = LoggerFactory.getLogger(SnmpDeepInfoController.class);
    @Resource
    private SnmpDeepInfoService snmpDeepInfoService;
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
        if (!this.isAddContinue()) {
            ArrayList snmpDeepInfoList = new ArrayList();
            return ResDataUtils.resetSuccessJson(snmpDeepInfoList);
        }
        HashMap<String, Object> params = new HashMap<String, Object>();
        try {
            if (null != agentJsonObject.get("snmpHostNames") && !StringUtils.isEmpty((CharSequence)agentJsonObject.get("snmpHostNames").toString())) {
                params.put("snmpHostNames", agentJsonObject.get("snmpHostNames").toString().split(","));
            }
            params.put("active", "1");
            List<SnmpDeepInfo> snmpDeepInfoList = this.snmpDeepInfoService.selectAllByParams(params);
            if (null != agentJsonObject.get("SERVER_BACKUP_FLAG") && !StringUtils.isEmpty((CharSequence)agentJsonObject.get("SERVER_BACKUP_FLAG").toString())) {
                ServerBackupUtil.cacheSaveSnmpDeepInfoId(snmpDeepInfoList);
                logger.info("server-backup request SnmpDeepInfo-------" + IpUtil.getIpAddr(request));
            }
            return ResDataUtils.resetSuccessJson(snmpDeepInfoList);
        }
        catch (Exception e) {
            logger.error("agent\u83b7\u53d6snmp\u8bbe\u5907\u6df1\u5ea6\u76d1\u63a7\u4fe1\u606f\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("agent\u83b7\u53d6snmp\u8bbe\u5907\u6df1\u5ea6\u76d1\u63a7\u4fe1\u606f\u9519\u8bef", e.toString(), "2");
            return ResDataUtils.resetErrorJson(e.toString());
        }
    }

    @ResponseBody
    @RequestMapping(value={"agentListState"})
    public String agentListState(@RequestBody String paramBean) {
        JSONObject agentJsonObject = (JSONObject)JSONUtil.parse((Object)paramBean);
        String checkResult = this.tokenUtils.preOpenDataAPICheck(agentJsonObject);
        if (!StringUtils.isEmpty((CharSequence)checkResult)) {
            return checkResult;
        }
        if (!this.isAddContinue()) {
            ArrayList snmpDeepInfoList = new ArrayList();
            return ResDataUtils.resetSuccessJson(snmpDeepInfoList);
        }
        HashMap<String, Object> params = new HashMap<String, Object>();
        try {
            if (null != agentJsonObject.get("snmpDeepInfoIds") && !StringUtils.isEmpty((CharSequence)agentJsonObject.get("snmpDeepInfoIds").toString())) {
                params.put("snmpDeepInfoIds", agentJsonObject.get("snmpDeepInfoIds").toString().split(","));
            }
            List<SnmpDeepState> dbTableList = this.snmpDeepInfoService.selectSnmpDeepStateList(params);
            return ResDataUtils.resetSuccessJson(dbTableList);
        }
        catch (Exception e) {
            logger.error("agent\u83b7\u53d6snmp\u6df1\u5ea6\u76d1\u63a7oid\u4fe1\u606f\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("agent\u83b7\u53d6snmp\u6df1\u5ea6\u76d1\u63a7oid\u4fe1\u606f\u9519\u8bef", e.toString(), "2");
            return ResDataUtils.resetErrorJson(e.toString());
        }
    }

    @ResponseBody
    @RequestMapping(value={"agentListForOpenAPI"})
    public String agentListForOpenAPI(@RequestBody String paramBean, HttpServletRequest request) {
        JSONObject agentJsonObject = (JSONObject)JSONUtil.parse((Object)paramBean);
        String checkResult = this.tokenUtils.preOpenDataAPICheck(agentJsonObject);
        if (!StringUtils.isEmpty((CharSequence)checkResult)) {
            return checkResult;
        }
        if (!this.isAddContinue()) {
            ArrayList snmpDeepInfoList = new ArrayList();
            return ResDataUtils.resetSuccessJson(snmpDeepInfoList);
        }
        HashMap<String, Object> params = new HashMap<String, Object>();
        try {
            if (null != agentJsonObject.get("hostname") && !StringUtils.isEmpty((CharSequence)agentJsonObject.get("hostname").toString())) {
                params.put("hostname", agentJsonObject.getStr("hostname"));
            }
            params.put("active", "1");
            List<SnmpDeepInfo> snmpDeepInfoList = this.snmpDeepInfoService.selectAllByParams(params);
            ArrayList<JSONObject> jsonObjectList = new ArrayList<JSONObject>();
            for (SnmpDeepInfo snmpDeepInfo : snmpDeepInfoList) {
                JSONObject jsonObject = JSONUtil.parseObj((Object)snmpDeepInfo);
                List<SnmpDeepState> snmpDeepStateList = this.snmpDeepInfoService.selectSnmpDeepStateList(snmpDeepInfo.getId());
                for (SnmpDeepState snmpDeepState : snmpDeepStateList) {
                    String oidResult = SnmpDeepUtil.viewSnmpDeepHandler(snmpDeepState.getOidValue());
                    if (StringUtils.isEmpty((CharSequence)oidResult)) {
                        oidResult = "Normal status, waiting for the return of monitoring results";
                    }
                    snmpDeepState.setOidResult(oidResult);
                }
                jsonObject.set("snmpDeepStateList", snmpDeepStateList);
                jsonObjectList.add(jsonObject);
            }
            return ResDataUtils.resetSuccessJson(jsonObjectList);
        }
        catch (Exception e) {
            logger.error("\u6570\u636e\u5f00\u653e\u63a5\u53e3\u83b7\u53d6snmp\u8bbe\u5907\u6df1\u5ea6\u76d1\u63a7\u4fe1\u606f\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("\u6570\u636e\u5f00\u653e\u63a5\u53e3\u83b7\u53d6snmp\u8bbe\u5907\u6df1\u5ea6\u76d1\u63a7\u4fe1\u606f\u9519\u8bef", e.toString(), "2");
            return ResDataUtils.resetErrorJson(e.toString());
        }
    }

    @RequestMapping(value={"list"})
    public String snmpDeepInfoList(SnmpDeepInfo snmpDeepInfo, Model model, HttpServletRequest request) {
        HashMap<String, Object> params = new HashMap<String, Object>();
        try {
            String liceFlage = request.getParameter(StaticKeys.LICENSE_LICE_FLAGE);
            if (!StringUtils.isEmpty((CharSequence)liceFlage)) {
                model.addAttribute("msg", "\u6b64\u529f\u80fd\u9700\u5347\u7ea7\u5230\u4f01\u4e1a\u7248\uff0c\u8bf7\u70b9\u51fb\u9875\u9762\u5e95\u90e8\u7f51\u7ad9\u8054\u7cfb\u6211\u4eec");
            }
            StringBuffer url = new StringBuffer();
            String hostName = null;
            if (!StringUtils.isEmpty((CharSequence)snmpDeepInfo.getHostname())) {
                hostName = snmpDeepInfo.getHostname();
                params.put("hostname", hostName.trim());
                url.append("&hostname=").append(hostName);
            }
            if (!StringUtils.isEmpty((CharSequence)snmpDeepInfo.getAccount())) {
                params.put("account", snmpDeepInfo.getAccount());
                url.append("&account=").append(snmpDeepInfo.getAccount());
            }
            if (!StringUtils.isEmpty((CharSequence)snmpDeepInfo.getGroupId())) {
                params.put("groupId", snmpDeepInfo.getGroupId());
                url.append("&groupId=").append(snmpDeepInfo.getGroupId());
            }
            if (!StringUtils.isEmpty((CharSequence)snmpDeepInfo.getOrderBy())) {
                params.put("orderBy", snmpDeepInfo.getOrderBy());
                params.put("orderType", snmpDeepInfo.getOrderType());
                url.append("&orderBy=").append(snmpDeepInfo.getOrderBy());
                url.append("&orderType=").append(snmpDeepInfo.getOrderType());
            }
            if (!StringUtils.isEmpty((CharSequence)snmpDeepInfo.getState())) {
                params.put("state", snmpDeepInfo.getState());
                url.append("&state=").append(snmpDeepInfo.getState());
            }
            if (!StringUtils.isEmpty((CharSequence)snmpDeepInfo.getActive())) {
                params.put("active", snmpDeepInfo.getActive());
                url.append("&active=").append(snmpDeepInfo.getActive());
            }
            HostUtil.addAccountquery(request, params);
            PageInfo pageInfo = this.snmpDeepInfoService.selectByParams(params, snmpDeepInfo.getPage(), snmpDeepInfo.getPageSize());
            for (SnmpDeepInfo snmpDeepInfo1 : (java.util.List<SnmpDeepInfo>) pageInfo.getList()) {
                snmpDeepInfo1.setStateSize(this.snmpDeepInfoService.countBySnmpDeepInfoId(snmpDeepInfo1.getId()) + "");
            }
            this.snmpDeepInfoService.addServerBackMark(pageInfo.getList());
            PageUtil.initPageNumber(pageInfo, model);
            if ("true".equals(this.commonConfig.getHostGroup())) {
                this.snmpDeepInfoService.setGroupInList(pageInfo.getList(), model, request);
            }
            HostUtil.addAccountListModel(model);
            model.addAttribute("pageUrl", (Object)("/snmpDeepInfo/list?1=1" + url.toString()));
            model.addAttribute("page", (Object)pageInfo);
            model.addAttribute("snmpDeepInfo", (Object)snmpDeepInfo);
        }
        catch (Exception e) {
            logger.error("\u67e5\u8be2snmp\u8bbe\u5907\u76d1\u6d4b\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("\u67e5\u8be2snmp\u8bbe\u5907\u76d1\u6d4b\u9519\u8bef", e.toString(), "2");
        }
        return "snmpDeepInfo/list";
    }

    @ResponseBody
    @RequestMapping(value={"saveGroupId"})
    public String saveGroupId(Model model, HttpServletRequest request) {
        try {
            String ids = request.getParameter("ids");
            String[] groupIdsArr = request.getParameterValues("groupId");
            this.snmpDeepInfoService.saveGroupId(ids, groupIdsArr, request);
        }
        catch (Exception e) {
            logger.error("\u4fdd\u5b58SNMP\u6807\u7b7e\u4fe1\u606f\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("\u4fdd\u5b58SNMP\u6807\u7b7e\u4fe1\u606f\u9519\u8bef", e.toString(), "2");
        }
        return "redirect:/snmpDeepInfo/list";
    }

    @RequestMapping(value={"save"})
    public String saveSnmpDeepInfo(SnmpDeepInfo snmpDeepInfo, Model model, HttpServletRequest request) {
        String errorMsg = "\u4fdd\u5b58snmp\u8bbe\u5907\u76d1\u6d4b\u9519\u8bef";
        try {
            if (StringUtils.isEmpty((CharSequence)snmpDeepInfo.getId())) {
                AccountInfo accountInfo = HostUtil.getAccountByRequest(request);
                if (null != accountInfo && !"admin".equals(accountInfo.getRole())) {
                    snmpDeepInfo.setAccount(accountInfo.getAccount());
                }
                this.snmpDeepInfoService.save(snmpDeepInfo, request);
                this.snmpDeepInfoService.saveLog(request, "\u6dfb\u52a0", snmpDeepInfo);
            } else {
                this.snmpDeepInfoService.updateById(snmpDeepInfo, request);
                this.snmpDeepInfoService.saveLog(request, "\u4fee\u6539", snmpDeepInfo);
            }
        }
        catch (Exception e) {
            logger.error(errorMsg, (Throwable)e);
            this.logInfoService.save(errorMsg, e.toString(), "2");
        }
        return "redirect:/snmpDeepInfo/list";
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
                this.snmpDeepInfoService.updateActive(params);
            }
        }
        catch (Exception e) {
            logger.error(errorMsg, (Throwable)e);
            this.logInfoService.save(errorMsg, e.toString(), "2");
        }
        return "redirect:/snmpDeepInfo/list";
    }

    @RequestMapping(value={"edit"})
    public String edit(Model model, HttpServletRequest request) {
        String errorMsg = "\u7f16\u8f91snmp\u8bbe\u5907\u76d1\u6d4b";
        String id = request.getParameter("id");
        SnmpDeepInfo snmpDeepInfo = new SnmpDeepInfo();
        try {
            if (StringUtils.isEmpty((CharSequence)id)) {
                snmpDeepInfo.setSnmpPort("161");
                snmpDeepInfo.setSnmpCommunity("public");
                snmpDeepInfo.setSnmpVersion("1");
                model.addAttribute("snmpDeepInfo", (Object)snmpDeepInfo);
                if (!this.isAddContinue()) {
                    return "redirect:/snmpDeepInfo/list?liceFlage=1";
                }
                model.addAttribute("dataFromIndex", (Object)0);
                return "snmpDeepInfo/add";
            }
            snmpDeepInfo = this.snmpDeepInfoService.selectById(id);
            List<SnmpDeepState> snmpDeepStateList = this.snmpDeepInfoService.selectSnmpDeepStateList(id);
            model.addAttribute("snmpDeepInfo", (Object)snmpDeepInfo);
            model.addAttribute("snmpDeepStateList", snmpDeepStateList);
            if (!CollectionUtil.isEmpty(snmpDeepStateList)) {
                model.addAttribute("dataFromIndex", (Object)snmpDeepStateList.get(snmpDeepStateList.size() - 1).getOidOrderNum());
            } else {
                model.addAttribute("dataFromIndex", (Object)0);
            }
        }
        catch (Exception e) {
            logger.error(errorMsg, (Throwable)e);
            this.logInfoService.save(errorMsg, e.toString(), "2");
        }
        return "snmpDeepInfo/add";
    }

    @RequestMapping(value={"view"})
    public String view(Model model, HttpServletRequest request) {
        String errorMsg = "\u67e5\u770bsnmp\u8bbe\u5907\u56fe\u8868\u9519\u8bef";
        String id = request.getParameter("id");
        SnmpDeepInfo snmpDeepInfo = new SnmpDeepInfo();
        try {
            snmpDeepInfo = this.snmpDeepInfoService.selectById(id);
            HashMap params = new HashMap();
            model.addAttribute("snmpDeepInfo", (Object)snmpDeepInfo);
            if ("true".equals(this.commonConfig.getHostGroup())) {
                snmpDeepInfo.setGroupId(this.hostGroupService.returnGroupNames(snmpDeepInfo.getGroupId()));
            }
            if ("1".equals(snmpDeepInfo.getActive())) {
                snmpDeepInfo.setActive("\u76d1\u63a7\u4e2d");
            } else {
                snmpDeepInfo.setActive("\u5df2\u505c\u6b62");
            }
            if ("0".equals(snmpDeepInfo.getSnmpVersion())) {
                snmpDeepInfo.setSnmpVersion("version1");
            } else if ("1".equals(snmpDeepInfo.getSnmpVersion())) {
                snmpDeepInfo.setSnmpVersion("version2c");
            } else {
                snmpDeepInfo.setSnmpVersion("version3");
            }
            List<SnmpDeepState> snmpDeepStateList = this.snmpDeepInfoService.selectSnmpDeepStateList(id);
            for (SnmpDeepState snmpDeepState : snmpDeepStateList) {
                String oidResult = SnmpDeepUtil.viewSnmpDeepHandler(snmpDeepState.getId());
                if (StringUtils.isEmpty((CharSequence)oidResult)) {
                    oidResult = "Normal status, waiting for the return of monitoring results";
                }
                snmpDeepState.setOidResult(oidResult);
            }
            model.addAttribute("snmpDeepInfo", (Object)snmpDeepInfo);
            model.addAttribute("snmpDeepStateList", snmpDeepStateList);
            model.addAttribute("snmpDeepInfoErrorMsg", (Object)this.messageErrorUtils.viewErrorMsgHandler(id));
        }
        catch (Exception e) {
            logger.error(errorMsg, (Throwable)e);
            this.logInfoService.save(errorMsg, e.toString(), "2");
        }
        return "snmpDeepInfo/view";
    }

    @RequestMapping(value={"del"})
    public String delete(Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        String errorMsg = "\u5220\u9664snmp\u8bbe\u5907\u76d1\u6d4b\u9519\u8bef";
        SnmpDeepInfo SnmpDeepInfo2 = new SnmpDeepInfo();
        try {
            if (!StringUtils.isEmpty((CharSequence)request.getParameter("id"))) {
                String[] ids;
                for (String id : ids = request.getParameter("id").split(",")) {
                    SnmpDeepInfo2 = this.snmpDeepInfoService.selectById(id);
                    this.snmpDeepInfoService.saveLog(request, "\u5220\u9664", SnmpDeepInfo2);
                }
                this.snmpDeepInfoService.deleteById(ids);
            }
        }
        catch (Exception e) {
            logger.error(errorMsg, (Throwable)e);
            this.logInfoService.save(errorMsg, e.toString(), "2");
        }
        return "redirect:/snmpDeepInfo/list";
    }

    @ResponseBody
    @RequestMapping(value={"testHeath"})
    public String testHeath(Model model, HttpServletRequest request) {
        String errorMsg = "SNMP\u76d1\u63a7\u6d4b\u8bd5\u8fde\u63a5\u9519\u8bef\uff1a";
        String resultMsg = "success";
        try {
            String id = request.getParameter("id");
            if (!StringUtils.isEmpty((CharSequence)id)) {
                SnmpDeepInfo snmpDeepInfo = this.snmpDeepInfoService.selectById(id);
                if (!SnmpUtil.isEthernetConnection(snmpDeepInfo.getHostname())) {
                    return "\u6d4b\u8bd5\u8fde\u63a5\u5931\u8d25";
                }
                SnmpDeepInfo snmpInfoForUpdate = new SnmpDeepInfo();
                snmpInfoForUpdate.setId(id);
                List<SnmpDeepState> snmpDeepStateList = this.snmpDeepInfoService.selectSnmpDeepStateList(snmpDeepInfo.getId());
                ArrayList<SnmpDeepState> getList = new ArrayList<SnmpDeepState>();
                ArrayList<SnmpDeepState> walkList = new ArrayList<SnmpDeepState>();
                for (SnmpDeepState snmpDeepState : snmpDeepStateList) {
                    if ("SNMPGET".equals(snmpDeepState.getOidType())) {
                        getList.add(snmpDeepState);
                        continue;
                    }
                    walkList.add(snmpDeepState);
                }
                String errorMsgGet = SnmpDeepUtil.snmpGet(snmpDeepInfo, getList);
                String errorMsgWalk = SnmpDeepUtil.walkSnmp(snmpDeepInfo, walkList);
                if (!StringUtils.isEmpty((CharSequence)errorMsgGet) || !StringUtils.isEmpty((CharSequence)errorMsgWalk)) {
                    this.messageErrorUtils.setErrorMsgHandler(snmpInfoForUpdate.getId(), errorMsgGet + errorMsgWalk);
                }
                snmpInfoForUpdate.setCreateTime(new Date());
                snmpInfoForUpdate.setState("1");
                this.snmpDeepInfoService.updateById(snmpInfoForUpdate);
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

    @ResponseBody
    @RequestMapping(value={"copySnmpDeepInfo"})
    public String copySnmpDeepInfo(Model model, HttpServletRequest request) {
        try {
            if (!this.isAddContinue()) {
                return "redirect:/snmpDeepInfo/list?liceFlage=1";
            }
            String id = request.getParameter("idCopy");
            String hostname = request.getParameter("hostname");
            if (StringUtils.isEmpty((CharSequence)id) || StringUtils.isEmpty((CharSequence)hostname)) {
                return "";
            }
            this.snmpDeepInfoService.copyData(id, hostname, request);
        }
        catch (Exception e) {
            logger.error("\u590d\u5236SNMP\u6df1\u5ea6\u76d1\u63a7\u6570\u636e\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("\u590d\u5236SNMP\u6df1\u5ea6\u76d1\u63a7\u6570\u636e\u9519\u8bef", e.toString(), "2");
        }
        return "redirect:/snmpDeepInfo/list";
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

