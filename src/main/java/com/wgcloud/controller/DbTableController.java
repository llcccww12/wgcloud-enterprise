/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.controller;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.github.pagehelper.PageInfo;
import com.wgcloud.config.CommonConfig;
import com.wgcloud.entity.DbInfo;
import com.wgcloud.entity.DbTable;
import com.wgcloud.service.CustomStateService;
import com.wgcloud.service.DashboardService;
import com.wgcloud.service.DbInfoService;
import com.wgcloud.service.DbTableCountService;
import com.wgcloud.service.DbTableService;
import com.wgcloud.service.ExcelExportService;
import com.wgcloud.service.LogInfoService;
import com.wgcloud.util.DESUtil;
import com.wgcloud.util.FormatUtil;
import com.wgcloud.util.HostUtil;
import com.wgcloud.util.MessageErrorUtils;
import com.wgcloud.util.PageUtil;
import com.wgcloud.util.ResDataUtils;
import com.wgcloud.util.TokenUtils;
import com.wgcloud.util.jdbc.ConnectionUtil;
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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.wgcloud.entity.DbTableCount;

@Controller
@RequestMapping(value={"/dbTable"})
public class DbTableController {
    private static final Logger logger = LoggerFactory.getLogger(DbTableController.class);
    @Resource
    private DbInfoService dbInfoService;
    @Resource
    private DbTableService dbTableService;
    @Resource
    private DbTableCountService dbTableCountService;
    @Resource
    private LogInfoService logInfoService;
    @Autowired
    private TokenUtils tokenUtils;
    @Resource
    private ExcelExportService excelExportService;
    @Resource
    private DashboardService dashboardService;
    @Resource
    private CustomStateService customStateService;
    @Autowired
    CommonConfig commonConfig;
    @Autowired
    private ConnectionUtil connectionUtil;
    @Autowired
    private MessageErrorUtils messageErrorUtils;

    @ResponseBody
    @RequestMapping(value={"agentList"})
    public String agentList(@RequestBody String paramBean) {
        JSONObject agentJsonObject = (JSONObject)JSONUtil.parse((Object)paramBean);
        String checkResult = this.tokenUtils.preOpenDataAPICheck(agentJsonObject);
        if (!StringUtils.isEmpty((CharSequence)checkResult)) {
            return checkResult;
        }
        HashMap<String, Object> params = new HashMap<String, Object>();
        try {
            params.put("active", "1");
            if (null != agentJsonObject.get("dbInfoIds") && !StringUtils.isEmpty((CharSequence)agentJsonObject.get("dbInfoIds").toString())) {
                params.put("dbInfoIds", agentJsonObject.get("dbInfoIds").toString().split(","));
            }
            List<DbTable> dbTableList = this.dbTableService.selectAllByParams(params);
            ArrayList<DbTable> dbTableListResult = new ArrayList<DbTable>();
            for (DbTable dbTable : dbTableList) {
                dbTable.setWhereVal(DESUtil.encryption(dbTable.getWhereVal()));
                String sqlinkey = FormatUtil.haveSqlDanger(dbTable.getWhereVal(), this.commonConfig.getSqlInKeys());
                if (!StringUtils.isEmpty((CharSequence)sqlinkey)) {
                    logger.error("\u7edf\u8ba1SQL\u8bed\u53e5\u542b\u6709sql\u654f\u611f\u5b57\u7b26" + sqlinkey);
                    continue;
                }
                dbTableListResult.add(dbTable);
            }
            return ResDataUtils.resetSuccessJson(dbTableListResult);
        }
        catch (Exception e) {
            logger.error("agent\u83b7\u53d6\u76d1\u63a7\u6570\u636e\u8868\u4fe1\u606f\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("agent\u83b7\u53d6\u76d1\u63a7\u6570\u636e\u8868\u4fe1\u606f\u9519\u8bef", e.toString(), "2");
            return ResDataUtils.resetErrorJson(e.toString());
        }
    }

    @RequestMapping(value={"list"})
    public String dbTableList(DbTable dbTable, Model model, HttpServletRequest request) {
        HashMap<String, Object> params = new HashMap<String, Object>();
        try {
            LicenseUtil.maxLicense_10(model, request, dbTable);
            StringBuffer url = new StringBuffer();
            String dbInfoId = null;
            if (!StringUtils.isEmpty((CharSequence)dbTable.getDbInfoId())) {
                dbInfoId = dbTable.getDbInfoId();
                params.put("dbInfoId", dbInfoId.trim());
                url.append("&dbInfoId=").append(dbInfoId);
            }
            if (!StringUtils.isEmpty((CharSequence)dbTable.getAccount())) {
                params.put("account", dbTable.getAccount());
                url.append("&account=").append(dbTable.getAccount());
            }
            if (!StringUtils.isEmpty((CharSequence)dbTable.getState())) {
                params.put("state", dbTable.getState());
                url.append("&state=").append(dbTable.getState());
            }
            if (!StringUtils.isEmpty((CharSequence)dbTable.getRemark())) {
                params.put("remark", dbTable.getRemark());
                url.append("&remark=").append(dbTable.getRemark());
            }
            if (!StringUtils.isEmpty((CharSequence)dbTable.getActive())) {
                params.put("active", dbTable.getActive());
                url.append("&active=").append(dbTable.getActive());
            }
            if (!StringUtils.isEmpty((CharSequence)dbTable.getOrderBy())) {
                params.put("orderBy", dbTable.getOrderBy());
                params.put("orderType", dbTable.getOrderType());
                url.append("&orderBy=").append(dbTable.getOrderBy());
                url.append("&orderType=").append(dbTable.getOrderType());
            }
            HostUtil.addAccountquery(request, params);
            PageInfo pageInfo = this.dbTableService.selectByParams(params, dbTable.getPage(), dbTable.getPageSize());
            PageUtil.initPageNumber(pageInfo, model);
            params.clear();
            List<DbInfo> dbInfoList = this.dbInfoService.selectAllByParams(params);
            this.dbInfoService.dbAddLogo(dbInfoList);
            for (DbTable dbTableTemp : (java.util.List<DbTable>) pageInfo.getList()) {
                for (DbInfo dbInfo : dbInfoList) {
                    if (!dbInfo.getId().equals(dbTableTemp.getDbInfoId())) continue;
                    dbTableTemp.setTableName(dbInfo.getAliasName());
                    dbTableTemp.setImage(dbInfo.getImage());
                    dbTableTemp.setResTimesSecond(FormatUtil.msToSecond(dbTableTemp.getResTimes()));
                    if (!"true".equals(this.commonConfig.getUserInfoManage())) continue;
                    dbTableTemp.setAccount(dbInfo.getAccount());
                }
            }
            HostUtil.addAccountListModel(model);
            model.addAttribute("pageUrl", (Object)("/dbTable/list?1=1" + url.toString()));
            model.addAttribute("page", (Object)pageInfo);
            model.addAttribute("dbTable", (Object)dbTable);
            model.addAttribute("dbInfoList", dbInfoList);
        }
        catch (Exception e) {
            logger.error("\u67e5\u8be2\u6570\u636e\u8868\u4fe1\u606f\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("\u67e5\u8be2\u6570\u636e\u8868\u4fe1\u606f\u9519\u8bef", e.toString(), "2");
        }
        return "mysql/tableList";
    }

    @RequestMapping(value={"save"})
    public String saveDbTable(DbTable DbTable2, Model model, HttpServletRequest request) {
        try {
            String sqlinkey = FormatUtil.haveSqlDanger(DbTable2.getWhereVal(), this.commonConfig.getSqlInKeys());
            if (!StringUtils.isEmpty((CharSequence)sqlinkey)) {
                model.addAttribute("dbTable", (Object)DbTable2);
                List<DbInfo> dbInfoList = this.dbInfoService.selectAllByParams(new HashMap<String, Object>());
                model.addAttribute("dbInfoList", dbInfoList);
                model.addAttribute("sqlInKeys", (Object)this.commonConfig.getSqlInKeys());
                model.addAttribute("msg", (Object)("\u7edf\u8ba1SQL\u8bed\u53e5\u542b\u6709sql\u654f\u611f\u5b57\u7b26" + sqlinkey + "\uff0c\u8bf7\u68c0\u67e5"));
                return "mysql/addTable";
            }
            if (StringUtils.isEmpty((CharSequence)DbTable2.getId())) {
                this.dbTableService.save(DbTable2);
                this.dbTableService.saveLog(request, "\u6dfb\u52a0", DbTable2);
            } else {
                this.dbTableService.updateById(DbTable2);
                this.dbTableService.saveLog(request, "\u4fee\u6539", DbTable2);
            }
        }
        catch (Exception e) {
            logger.error("\u4fdd\u5b58\u6570\u636e\u8868\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("\u4fdd\u5b58\u6570\u636e\u8868\u9519\u8bef", e.toString(), "2");
        }
        return "redirect:/dbTable/list";
    }

    @RequestMapping(value={"edit"})
    public String editDbTable(DbTable DbTable2, Model model, HttpServletRequest request) {
        try {
            String id = request.getParameter("id");
            DbTable dbTableInfo = new DbTable();
            if (!StringUtils.isEmpty((CharSequence)id)) {
                dbTableInfo = this.dbTableService.selectById(id);
            }
            if (!this.isAddContinue()) {
                return "redirect:/dbTable/list?liceFlage=1";
            }
            HashMap<String, Object> params = new HashMap<String, Object>();
            HostUtil.addAccountquery(request, params);
            List<DbInfo> dbInfoList = this.dbInfoService.selectAllByParams(params);
            ArrayList<DbInfo> dbInfoListResult = new ArrayList<DbInfo>();
            for (DbInfo dbInfo : dbInfoList) {
                if ("redis".equals(dbInfo.getDbType()) || "mongodb".equals(dbInfo.getDbType())) continue;
                dbInfoListResult.add(dbInfo);
            }
            model.addAttribute("dbInfoList", dbInfoListResult);
            model.addAttribute("dbTable", (Object)dbTableInfo);
            model.addAttribute("sqlInKeys", (Object)this.commonConfig.getSqlInKeys());
        }
        catch (Exception e) {
            logger.error("\u67e5\u770b\u6570\u636e\u8868\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("\u67e5\u770b\u6570\u636e\u8868\u9519\u8bef", e.toString(), "2");
        }
        return "mysql/addTable";
    }

    @ResponseBody
    @RequestMapping(value={"testHeath"})
    public String testHeath(Model model, HttpServletRequest request) {
        String errorMsg = "\u6570\u636e\u76d1\u63a7\u6d4b\u8bd5\u8fde\u63a5\u9519\u8bef\uff1a";
        String resultMsg = "success";
        try {
            if (!StaticKeys.LICENSE_STATE.equals("1")) {
                return "noPro";
            }
            String id = request.getParameter("id");
            if (!StringUtils.isEmpty((CharSequence)id)) {
                DbTable dbTable = this.dbTableService.selectById(id);
                DbInfo dbInfo = this.dbInfoService.selectById(dbTable.getDbInfoId());
                if (!StringUtils.isEmpty((CharSequence)dbInfo.getPasswd())) {
                    dbInfo.setPasswd(DESUtil.decryptForServerDb(dbInfo.getPasswd()));
                }
                JdbcTemplate jdbcTemplate = this.connectionUtil.getJdbcTemplate(dbInfo);
                if (StringUtils.isEmpty((CharSequence)dbTable.getWhereVal())) {
                    return "sql\u8bed\u53e5\u4e3a\u7a7a";
                }
                resultMsg = this.connectionUtil.testQueryTable(dbInfo, jdbcTemplate, dbTable);
                DbTable dbTableForUpdate = new DbTable();
                dbTableForUpdate.setId(dbTable.getId());
                dbTableForUpdate.setCreateTime(new Date());
                dbTableForUpdate.setTableCount(dbTable.getTableCount());
                dbTableForUpdate.setState(dbTable.getState());
                dbTableForUpdate.setResTimes(dbTable.getResTimes());
                this.dbTableService.updateById(dbTableForUpdate);
                this.messageErrorUtils.setErrorMsgHandler(dbTable.getId(), dbTable.getTestErrorMsg());
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
                this.dbTableService.updateActive(params);
            }
        }
        catch (Exception e) {
            logger.error(errorMsg, (Throwable)e);
            this.logInfoService.save(errorMsg, e.toString(), "2");
        }
        return "redirect:/dbTable/list";
    }

    @RequestMapping(value={"viewChart"})
    public String viewChartDbTable(DbTable dbTable, Model model, HttpServletRequest request) {
        try {
            String id = request.getParameter("id");
            String startTime = request.getParameter("startTime");
            String endTime = request.getParameter("endTime");
            String am = request.getParameter("am");
            if (!StringUtils.isEmpty((CharSequence)id)) {
                DbTable dbTableInfo = this.dbTableService.selectById(id);
                dbTableInfo.setResTimesSecond(FormatUtil.msToSecond(dbTableInfo.getResTimes()));
                if (StringUtils.isEmpty((CharSequence)dbTableInfo.getRemarkExt())) {
                    dbTableInfo.setRemarkExt("");
                }
                HashMap<String, Object> params = new HashMap<String, Object>();
                this.dashboardService.setDateParam(am, startTime, endTime, params, model);
                model.addAttribute("amList", this.dashboardService.getAmList());
                params.put("dbTableId", id);
                List<DbTableCount> dbTableCounts = this.dbTableCountService.selectAllByParams(params);
                List<DbTableCount> tableStateCompressList = HostUtil.compressChartListData(dbTableCounts, model);
                JSONArray tableStateJsonArray = this.dbTableCountService.getJsonArray(dbTableInfo, tableStateCompressList);
                this.customStateService.setSubtitle(model, tableStateJsonArray);
                model.addAttribute("dbTableCounts", (Object)tableStateJsonArray);
                model.addAttribute("dbTable", (Object)dbTableInfo);
                if ("2".equals(dbTableInfo.getState())) {
                    model.addAttribute("dbTableInfoErrorMsg", (Object)this.messageErrorUtils.viewErrorMsgHandler(id));
                } else {
                    model.addAttribute("dbTableInfoErrorMsg", "success");
                }
                if (!LicenseUtil.checkEnterpriseVersion() && !StringUtils.isEmpty((CharSequence)dbTableInfo.getTableName()) && dbTableInfo.getTableName().contains(",")) {
                    model.addAttribute("dbTableInfoErrorMsg", "\u5f53\u524d\u7248\u672c\u53ea\u652f\u6301\u663e\u793a\u4e00\u5217\u6570\u636e\uff0c\u5347\u7ea7\u5230\u4f01\u4e1a\u7248\u540e\uff0c\u53ef\u4ee5\u652f\u6301\u76d1\u63a7\u591a\u5217\u540d\u79f0");
                }
                model.addAttribute("sqlResultHtmlTable", (Object)this.messageErrorUtils.viewErrorMsgHandler(id + "_TABLE_HTML"));
            }
        }
        catch (Exception e) {
            logger.error("\u67e5\u770b\u6570\u636e\u8868\u56fe\u8868\u7edf\u8ba1\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("\u67e5\u770b\u6570\u636e\u8868\u56fe\u8868\u7edf\u8ba1\u9519\u8bef", e.toString(), "2");
        }
        return "mysql/tableView";
    }

    @RequestMapping(value={"chartExcel"})
    public void chartExcel(Model model, HttpServletRequest request, HttpServletResponse response) {
        String errorMsg = "\u6570\u636e\u8868\u7edf\u8ba1\u56fe\u5bfc\u51faexcel\u9519\u8bef";
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
            this.dashboardService.setDateParam(am, startTime, endTime, params, model);
            params.put("dbTableId", id);
            DbTable dbTableInfo = this.dbTableService.selectById(id);
            this.excelExportService.exportDbTableExcel(params, response, dbTableInfo);
        }
        catch (Exception e) {
            logger.error(errorMsg, (Throwable)e);
            this.logInfoService.save(errorMsg, e.toString(), "2");
        }
    }

    @RequestMapping(value={"del"})
    public String delete(Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        String errorMsg = "\u5220\u9664\u6570\u636e\u8868\u4fe1\u606f\u9519\u8bef";
        try {
            if (!StringUtils.isEmpty((CharSequence)request.getParameter("id"))) {
                String[] ids;
                for (String id : ids = request.getParameter("id").split(",")) {
                    DbTable dbTable = this.dbTableService.selectById(id);
                    this.dbTableService.saveLog(request, "\u5220\u9664", dbTable);
                }
                this.dbTableService.deleteById(ids);
            }
        }
        catch (Exception e) {
            logger.error(errorMsg, (Throwable)e);
            this.logInfoService.save(errorMsg, e.toString(), "2");
        }
        return "redirect:/dbTable/list";
    }

    private boolean isAddContinue() {
        try {
            HashMap<String, Object> params;
            int dbSize;
            if (!StaticKeys.LICENSE_STATE.equals("1") && (dbSize = this.dbTableService.countByParams(params = new HashMap<String, Object>())) >= 10) {
                return false;
            }
        }
        catch (Exception e) {
            logger.error("isAddContinue error", (Throwable)e);
        }
        return true;
    }
}

