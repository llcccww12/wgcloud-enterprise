/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.controller;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alibaba.excel.EasyExcel;
import com.github.pagehelper.PageInfo;
import com.wgcloud.config.CommonConfig;
import com.wgcloud.dto.TjbbExcelChartDto;
import com.wgcloud.entity.MailSet;
import com.wgcloud.entity.ReportInfo;
import com.wgcloud.entity.ReportInstance;
import com.wgcloud.service.ReportInfoService;
import com.wgcloud.service.ReportInstanceService;
import com.wgcloud.service.TaskUtilService;
import com.wgcloud.util.ExecUtil;
import com.wgcloud.util.PageUtil;
import com.wgcloud.util.ResDataUtils;
import com.wgcloud.util.ThreadPoolUtil;
import com.wgcloud.util.TokenUtils;
import com.wgcloud.util.license.LicenseUtil;
import com.wgcloud.util.msg.WarnOtherUtil;
import com.wgcloud.util.staticvar.StaticKeys;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
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

@Controller
@RequestMapping(value={"/report"})
public class ReportController {
    private static final Logger logger = LoggerFactory.getLogger(ReportController.class);
    @Autowired
    private TaskUtilService taskUtilService;
    @Resource
    private ReportInfoService reportInfoService;
    @Autowired
    private ReportInstanceService reportInstanceService;
    @Autowired
    private CommonConfig commonConfig;
    @Autowired
    private TokenUtils tokenUtils;

    private void testThread() {
        Runnable runnable = () -> logger.info("ReportController----------testThread");
        ThreadPoolUtil.executor.execute(runnable);
    }

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
            String reportType = agentJsonObject.getStr("reportType");
            String groupName = agentJsonObject.getStr("groupName");
            String timePartLike = agentJsonObject.getStr("timePartLike");
            if (!StringUtils.isEmpty((CharSequence)reportType)) {
                params.put("reportType", reportType);
            }
            if (!StringUtils.isEmpty((CharSequence)groupName)) {
                params.put("groupName", groupName);
            }
            if (!StringUtils.isEmpty((CharSequence)timePartLike)) {
                params.put("timePartLike", timePartLike);
            }
            PageInfo cpuStates = this.reportInfoService.selectByParams(params, agentJsonObject.getInt("page"), agentJsonObject.getInt("pageSize"));
            return ResDataUtils.resetSuccessJson(cpuStates);
        }
        catch (Exception e) {
            logger.error("agent\u83b7\u53d6\u5de1\u68c0\u62a5\u544a\u4fe1\u606f\u9519\u8bef", (Throwable)e);
            return ResDataUtils.resetErrorJson(e.toString());
        }
    }

    @ResponseBody
    @RequestMapping(value={"agentReportInstance"})
    public String agentReportInstance(@RequestBody String paramBean) {
        JSONObject agentJsonObject = (JSONObject)JSONUtil.parse((Object)paramBean);
        String checkResult = this.tokenUtils.preOpenDataAPICheck(agentJsonObject);
        if (!StringUtils.isEmpty((CharSequence)checkResult)) {
            return checkResult;
        }
        HashMap<String, Object> params = new HashMap<String, Object>();
        if (null == agentJsonObject.get("reportInfoId") || StringUtils.isEmpty((CharSequence)agentJsonObject.get("reportInfoId").toString())) {
            return ResDataUtils.resetErrorJson("Missing require parameters");
        }
        try {
            params.put("reportInfoId", agentJsonObject.get("reportInfoId").toString());
            List<ReportInstance> reportInstanceList = this.reportInstanceService.selectAllByParams(params);
            return ResDataUtils.resetSuccessJson(reportInstanceList);
        }
        catch (Exception e) {
            logger.error("agent\u83b7\u53d6\u5de1\u68c0\u62a5\u544a\u72b6\u6001\u4fe1\u606f\u9519\u8bef", (Throwable)e);
            return ResDataUtils.resetErrorJson(e.toString());
        }
    }

    @RequestMapping(value={"list"})
    public String reportInfoList(ReportInfo reportInfo, Model model, HttpServletRequest request) {
        HashMap<String, Object> params = new HashMap<String, Object>();
        try {
            LicenseUtil.maxLicense_10(model, request, reportInfo);
            StringBuffer url = new StringBuffer();
            String reportType = "";
            String groupName = "";
            String timePart = null;
            if (!StringUtils.isEmpty((CharSequence)reportInfo.getReportType())) {
                reportType = reportInfo.getReportType();
                params.put("reportType", reportType.trim());
                url.append("&reportType=").append(reportType);
            }
            if (!StringUtils.isEmpty((CharSequence)reportInfo.getGroupName())) {
                groupName = reportInfo.getGroupName();
                params.put("groupName", groupName.trim());
                url.append("&groupName=").append(groupName);
            }
            if (!StringUtils.isEmpty((CharSequence)reportInfo.getTimePart())) {
                timePart = reportInfo.getTimePart();
                params.put("timePartLike", timePart.trim());
                url.append("&timePartLike=").append(timePart);
            }
            PageInfo pageInfo = this.reportInfoService.selectByParams(params, reportInfo.getPage(), reportInfo.getPageSize());
            PageUtil.initPageNumber(pageInfo, model);
            model.addAttribute("pageUrl", (Object)("/report/list?1=1" + url.toString()));
            model.addAttribute("page", (Object)pageInfo);
            model.addAttribute("reportInfo", (Object)reportInfo);
        }
        catch (Exception e) {
            logger.error("\u67e5\u8be2\u5de1\u68c0\u62a5\u544a\u5217\u8868\u9519\u8bef", (Throwable)e);
        }
        return "report/list";
    }

    @RequestMapping(value={"view"})
    public String viewReportInfo(Model model, HttpServletRequest request) {
        String id = request.getParameter("id");
        ReportInfo reportInfo = new ReportInfo();
        try {
            reportInfo = this.reportInfoService.selectById(id);
            model.addAttribute("reportInfo", (Object)reportInfo);
            HashMap<String, Object> params = new HashMap<String, Object>();
            params.put("reportInfoId", id);
            List<ReportInstance> reportInstanceList = this.reportInstanceService.selectAllByParams(params);
            for (ReportInstance reportInstance : reportInstanceList) {
                this.fontColorHandler(reportInstance);
            }
            model.addAttribute("reportInstanceList", reportInstanceList);
        }
        catch (Exception e) {
            logger.error("\u67e5\u770b\u5de1\u68c0\u62a5\u544a\u4fe1\u606f\u9519\u8bef", (Throwable)e);
        }
        return "report/view";
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @RequestMapping(value={"chartExcel"})
    public void chartExcel(Model model, HttpServletRequest request, HttpServletResponse response) {
        OutputStream out = null;
        try {
            if (!StaticKeys.LICENSE_STATE.equals("1")) {
                response.setContentType("text/html;charset=UTF-8");
                response.getOutputStream().write("The module needs to professional version. Please contact us at www.wgstart.com".getBytes());
                return;
            }
            String id = request.getParameter("id");
            HashMap<String, Object> params = new HashMap<String, Object>();
            params.put("reportInfoId", id);
            List<ReportInstance> reportInstanceList = this.reportInstanceService.selectAllByParams(params);
            ArrayList<TjbbExcelChartDto> excelList = new ArrayList<TjbbExcelChartDto>();
            for (int i = 0; i < reportInstanceList.size(); ++i) {
                ReportInstance reportInstance = reportInstanceList.get(i);
                TjbbExcelChartDto excelChartDto = new TjbbExcelChartDto();
                excelChartDto.setInfoKey(reportInstance.getInfoKey());
                excelChartDto.setInfoContent(reportInstance.getInfoContent());
                excelList.add(excelChartDto);
            }
            ReportInfo reportInfo = this.reportInfoService.selectById(id);
            String reportTypeName = "\u65e5\u62a5";
            if ("2".equals(reportInfo.getReportType())) {
                reportTypeName = "\u6708\u62a5";
            }
            if ("1".equals(reportInfo.getReportType())) {
                reportTypeName = "\u5468\u62a5";
            }
            String fileName = reportInfo.getTimePart() + "_" + reportInfo.getGroupName() + "_" + reportTypeName + "_\u5de1\u68c0\u62a5\u544a.xlsx";
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            String encodedFileName = URLEncoder.encode(fileName, "UTF-8");
            response.addHeader("Content-Disposition", "filename=" + encodedFileName);
            EasyExcel.write((OutputStream)response.getOutputStream(), TjbbExcelChartDto.class).sheet("sheet").doWrite(excelList);
        }
        catch (Exception e) {
            logger.error("\u5de1\u68c0\u62a5\u544a\u5bfc\u51faexcel\u9519\u8bef", (Throwable)e);
        }
        finally {
            try {
                if (out != null) {
                    out.close();
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @ResponseBody
    @RequestMapping(value={"sendMail"})
    public String sendMail(MailSet mailSet, Model model, HttpServletRequest request) {
        String result = "success";
        try {
            if (!StaticKeys.LICENSE_STATE.equals("1")) {
                return "The module needs to professional version. Please contact us at www.wgstart.com";
            }
            String id = request.getParameter("id");
            String scriptSend = request.getParameter("scriptSend");
            ReportInfo reportInfo = this.reportInfoService.selectById(id);
            model.addAttribute("reportInfo", (Object)reportInfo);
            HashMap<String, Object> params = new HashMap<String, Object>();
            params.put("reportInfoId", id);
            List<ReportInstance> reportInstanceList = this.reportInstanceService.selectAllByParams(params);
            String reportTypeName = "\u65e5\u62a5";
            if ("2".equals(reportInfo.getReportType())) {
                reportTypeName = "\u6708\u62a5";
            }
            if ("1".equals(reportInfo.getReportType())) {
                reportTypeName = "\u5468\u62a5";
            }
            String title = reportInfo.getTimePart() + "-" + reportInfo.getGroupName() + "-" + reportTypeName + "-\u5de1\u68c0\u62a5\u544a";
            String toMails = request.getParameter("toMails");
            if (!StringUtils.isEmpty((CharSequence)toMails)) {
                StringBuilder table = new StringBuilder();
                table.append("<h3 align=\"center\">" + title + "</h3>");
                table.append("<table align=\"center\" border='1' cellpadding='8' cellspacing='0' style='border-collapse:collapse; width:80%;'>");
                table.append("<tr style='background:#f5f5f5; font-weight:bold;'>");
                table.append("<td>\u5e8f\u53f7</td><td>\u5de1\u68c0\u9879</td><td>\u5de1\u68c0\u7ed3\u679c</td>");
                table.append("</tr>");
                int index = 1;
                for (ReportInstance reportInstance : reportInstanceList) {
                    this.fontColorHandler(reportInstance);
                    table.append("<tr><td>" + index + "</td><td>" + reportInstance.getInfoKey() + "</td><td>" + reportInstance.getInfoContent() + "</td></tr>");
                    ++index;
                }
                table.append("</table>");
                WarnOtherUtil.sendMail(toMails, title, table.toString());
            }
            if ("1".equals(scriptSend)) {
                StringBuilder scriptContent = new StringBuilder();
                scriptContent.append(title);
                scriptContent.append("\\n| \u5de1\u68c0\u9879 | \u5de1\u68c0\u7ed3\u679c  |\\n");
                scriptContent.append("| ---- | ---- |\\n");
                int index2 = 1;
                for (ReportInstance reportInstance : reportInstanceList) {
                    scriptContent.append(index2 + ". " + reportInstance.getInfoKey() + " | " + reportInstance.getInfoContent() + " |\\n");
                    ++index2;
                }
                ExecUtil.runScript(scriptContent.toString(), "", "\u91cd\u8981", "", "");
            }
        }
        catch (Exception e) {
            logger.error("\u5de1\u68c0\u62a5\u544a\u53d1\u9001\u90ae\u4ef6\u9519\u8bef", (Throwable)e);
            result = e.toString();
        }
        return result;
    }

    private void fontColorHandler(ReportInstance reportInstance) {
        if (!StringUtils.isEmpty((CharSequence)reportInstance.getInfoContent())) {
            reportInstance.setInfoContent(reportInstance.getInfoContent().replace("\u6b63\u5e38", "<span style=\"color:oklch(55% .25 285);font-weight:700\">\u6b63\u5e38</span>"));
            reportInstance.setInfoContent(reportInstance.getInfoContent().replace("\u5f02\u5e38", "<span style=\"color:oklch(57.7% .245 27);font-weight:700\">\u5f02\u5e38</span>"));
            reportInstance.setInfoKey(reportInstance.getInfoKey().replace("\u6700\u9ad8\u503c", "<span style=\"background-color:#FFFF00\">\u6700\u9ad8\u503c</span>"));
            reportInstance.setInfoKey(reportInstance.getInfoKey().replace("\u6700\u957f", "<span style=\"background-color:#FFFF00\">\u6700\u957f</span>"));
            reportInstance.setInfoKey(reportInstance.getInfoKey().replace("\u6700\u77ed", "<span style=\"background-color:#FFFF00\">\u6700\u77ed</span>"));
        }
    }
}

