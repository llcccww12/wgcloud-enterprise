/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.controller;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.github.pagehelper.PageInfo;
import com.wgcloud.config.CommonConfig;
import com.wgcloud.entity.LogInfo;
import com.wgcloud.service.LogInfoService;
import com.wgcloud.util.PageUtil;
import com.wgcloud.util.ResDataUtils;
import com.wgcloud.util.TokenUtils;
import com.wgcloud.util.staticvar.StaticKeys;
import java.util.HashMap;
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
@RequestMapping(value={"/log"})
public class LogInfoController {
    private static final Logger logger = LoggerFactory.getLogger(LogInfoController.class);
    @Resource
    private LogInfoService logInfoService;
    @Autowired
    private CommonConfig commonConfig;
    @Autowired
    private TokenUtils tokenUtils;

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
            if (null != agentJsonObject.get("hostname") && !StringUtils.isEmpty((CharSequence)agentJsonObject.get("hostname").toString())) {
                params.put("hostname", agentJsonObject.get("hostname").toString().trim());
            }
            if (null != agentJsonObject.get("startTime")) {
                params.put("startTime", agentJsonObject.get("startTime").toString().trim());
            }
            if (null != agentJsonObject.get("endTime")) {
                params.put("endTime", agentJsonObject.get("endTime").toString().trim());
            }
            if (null != agentJsonObject.get("state")) {
                params.put("state", agentJsonObject.get("state").toString().trim());
            }
            PageInfo logInfoList = this.logInfoService.selectByParams(params, agentJsonObject.getInt("page"), agentJsonObject.getInt("pageSize"));
            return ResDataUtils.resetSuccessJson(logInfoList);
        }
        catch (Exception e) {
            logger.error("agent\u83b7\u53d6\u7cfb\u7edf\u65e5\u5fd7\u4fe1\u606f\u5217\u8868\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("agent\u83b7\u53d6\u7cfb\u7edf\u65e5\u5fd7\u4fe1\u606f\u5217\u8868\u9519\u8bef", e.toString(), "2");
            return ResDataUtils.resetErrorJson(e.toString());
        }
    }

    @RequestMapping(value={"list"})
    public String LogInfoList(LogInfo logInfo, Model model, HttpServletRequest request) {
        HashMap<String, Object> params = new HashMap<String, Object>();
        try {
            StringBuffer url = new StringBuffer();
            String timeRange = request.getParameter("startTime");
            String hostname = null;
            if (!StringUtils.isEmpty((CharSequence)logInfo.getHostname())) {
                hostname = logInfo.getHostname();
                params.put("hostname", hostname.trim());
                url.append("&hostname=").append(hostname);
            }
            if (!StringUtils.isEmpty((CharSequence)timeRange)) {
                String startTime = timeRange.split(" - ")[0].trim();
                String endTime = timeRange.split(" - ")[1].trim();
                params.put("startTime", startTime + " 00:00:00");
                params.put("endTime", endTime + " 23:59:59");
                url.append("&startTime=").append(timeRange);
                model.addAttribute("startTime", (Object)timeRange);
            }
            if (!StringUtils.isEmpty((CharSequence)logInfo.getState())) {
                params.put("state", logInfo.getState());
                url.append("&state=").append(logInfo.getState());
            }
            PageInfo pageInfo = this.logInfoService.selectByParamsNoContent(params, logInfo.getPage(), logInfo.getPageSize());
            PageUtil.initPageNumber(pageInfo, model);
            model.addAttribute("pageUrl", (Object)("/log/list?1=1" + url.toString()));
            model.addAttribute("page", (Object)pageInfo);
            model.addAttribute("logInfo", (Object)logInfo);
        }
        catch (Exception e) {
            logger.error("\u67e5\u8be2\u65e5\u5fd7\u5217\u8868\u9519\u8bef", (Throwable)e);
        }
        return "log/list";
    }

    @RequestMapping(value={"view"})
    public String viewLogInfo(Model model, HttpServletRequest request) {
        String id = request.getParameter("id");
        LogInfo logInfo = new LogInfo();
        try {
            logInfo = this.logInfoService.selectById(id);
            model.addAttribute("logInfo", (Object)logInfo);
        }
        catch (Exception e) {
            logger.error("\u67e5\u770b\u65e5\u5fd7\u4fe1\u606f\u9519\u8bef", (Throwable)e);
        }
        return "log/view";
    }

    @RequestMapping(value={"exportListExcel"})
    public void exportListExcel(LogInfo logInfo, HttpServletRequest request, HttpServletResponse response) {
        try {
            if (!StaticKeys.LICENSE_STATE.equals("1")) {
                response.setContentType("text/html;charset=UTF-8");
                response.getOutputStream().write("The module needs to professional version. Please contact us at www.wgstart.com".getBytes());
                return;
            }
            HashMap<String, Object> params = new HashMap<String, Object>();
            String timeRange = request.getParameter("startTime");
            String hostname = null;
            if (!StringUtils.isEmpty((CharSequence)logInfo.getHostname())) {
                hostname = logInfo.getHostname();
                params.put("hostname", hostname.trim());
            }
            if (!StringUtils.isEmpty((CharSequence)timeRange)) {
                String startTime = timeRange.split(" - ")[0].trim();
                String endTime = timeRange.split(" - ")[1].trim();
                params.put("startTime", startTime + " 00:00:00");
                params.put("endTime", endTime + " 23:59:59");
            }
            if (!StringUtils.isEmpty((CharSequence)logInfo.getState())) {
                params.put("state", logInfo.getState());
            }
            this.logInfoService.exportListExcel(params, response);
        }
        catch (Exception e) {
            logger.error("\u7cfb\u7edf\u65e5\u5fd7\u6570\u636e\u5bfc\u51faexcel\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("\u7cfb\u7edf\u65e5\u5fd7\u6570\u636e\u5bfc\u51faexcel\u9519\u8bef", e.toString(), "2");
        }
    }
}

