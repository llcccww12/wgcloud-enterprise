/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.controller;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.github.pagehelper.PageInfo;
import com.wgcloud.config.CommonConfig;
import com.wgcloud.entity.K8sMonitor;
import com.wgcloud.service.K8sMonitorService;
import com.wgcloud.service.LogInfoService;
import com.wgcloud.service.SystemInfoService;
import com.wgcloud.util.HostUtil;
import com.wgcloud.util.PageUtil;
import com.wgcloud.util.ResDataUtils;
import com.wgcloud.util.TokenUtils;
import com.wgcloud.util.license.LicenseUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
@RequestMapping(value={"/k8sMonitor"})
public class K8sMonitorController {
    private static final Logger logger = LoggerFactory.getLogger(K8sMonitorController.class);
    @Resource
    private K8sMonitorService k8sMonitorService;
    @Resource
    private LogInfoService logInfoService;
    @Resource
    private SystemInfoService systemInfoService;
    @Autowired
    private TokenUtils tokenUtils;
    @Autowired
    private CommonConfig commonConfig;

    @ResponseBody
    @RequestMapping(value={"agentList"})
    public String agentList(@RequestBody String paramBean) {
        JSONObject agentJsonObject = (JSONObject)JSONUtil.parse((Object)paramBean);
        String checkResult = this.tokenUtils.preOpenDataAPICheck(agentJsonObject);
        if (!StringUtils.isEmpty((CharSequence)checkResult)) {
            return checkResult;
        }
        HashMap<String, Object> params = new HashMap<String, Object>();
        if (null != agentJsonObject.get("dataType") && !StringUtils.isEmpty((CharSequence)agentJsonObject.getStr("dataType"))) {
            params.put("dataType", agentJsonObject.getStr("dataType"));
        }
        if (null != agentJsonObject.get("k8sName") && !StringUtils.isEmpty((CharSequence)agentJsonObject.getStr("k8sName"))) {
            params.put("k8sName", agentJsonObject.getStr("k8sName"));
        }
        try {
            List<K8sMonitor> k8sMonitorList = this.k8sMonitorService.selectAllByParams(params);
            return ResDataUtils.resetSuccessJson(k8sMonitorList);
        }
        catch (Exception e) {
            logger.error("\u6570\u636e\u63a5\u53e3\u83b7\u53d6k8s\u76d1\u6d4b\u4fe1\u606f\u4fe1\u606f\u9519\u8bef", (Throwable)e);
            return ResDataUtils.resetErrorJson(e.toString());
        }
    }

    @RequestMapping(value={"list"})
    public String K8sMonitorList(K8sMonitor k8sMonitor, Model model, HttpServletRequest request) {
        HashMap<String, Object> params = new HashMap<String, Object>();
        String returlView = "k8s/nodeList";
        try {
            LicenseUtil.maxLicense_10(model, request, k8sMonitor);
            String namespaceParam = request.getParameter("namespaceParam");
            StringBuffer url = new StringBuffer();
            String dataType = k8sMonitor.getDataType();
            if (!StringUtils.isEmpty((CharSequence)k8sMonitor.getK8sName())) {
                params.put("k8sName", k8sMonitor.getK8sName().trim());
                url.append("&k8sName=").append(k8sMonitor.getK8sName());
            }
            if (!StringUtils.isEmpty((CharSequence)k8sMonitor.getDataType())) {
                params.put("dataType", k8sMonitor.getDataType().trim());
                url.append("&dataType=").append(k8sMonitor.getDataType());
            }
            if (!StringUtils.isEmpty((CharSequence)namespaceParam)) {
                params.put("dataJson", namespaceParam.trim());
                url.append("&namespaceParam=").append(namespaceParam);
            }
            if (!StringUtils.isEmpty((CharSequence)k8sMonitor.getOrderBy())) {
                params.put("orderBy", k8sMonitor.getOrderBy());
                params.put("orderType", k8sMonitor.getOrderType());
                url.append("&orderBy=").append(k8sMonitor.getOrderBy());
                url.append("&orderType=").append(k8sMonitor.getOrderType());
            }
            ArrayList<JSONObject> resultList = new ArrayList<JSONObject>();
            PageInfo pageInfo = this.k8sMonitorService.selectByParams(params, k8sMonitor.getPage(), k8sMonitor.getPageSize());
            for (K8sMonitor k8sMonitor1 : (java.util.List<K8sMonitor>) pageInfo.getList()) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.set("id", (Object)k8sMonitor1.getId());
                jsonObject.set("k8sName", (Object)k8sMonitor1.getK8sName());
                jsonObject.set("dataType", (Object)k8sMonitor1.getDataType());
                jsonObject.set("createTime", (Object)k8sMonitor1.getCreateTime());
                JSONObject dataObject = JSONUtil.parseObj((String)k8sMonitor1.getDataJson());
                if ("container".equals(dataType)) {
                    if (dataObject.get("containerId") == null) {
                        dataObject.set("containerId", "");
                    }
                    if (dataObject.get("ready") == null) {
                        dataObject.set("ready", "");
                    }
                }
                if ("node".equals(dataType)) {
                    if (dataObject.get("cpu") == null) {
                        dataObject.set("cpu", "1");
                    }
                    if (dataObject.get("memory") == null) {
                        dataObject.set("memory", "0MB");
                    }
                    if (dataObject.get("pods") == null) {
                        dataObject.set("pods", "1");
                    }
                    if (dataObject.get("images") == null) {
                        dataObject.set("images", "1");
                    }
                    if (dataObject.get("k8sVersion") == null) {
                        dataObject.set("k8sVersion", "");
                    }
                    if (dataObject.get("storage") == null) {
                        dataObject.set("storage", "");
                    }
                }
                jsonObject.putAll((Map)dataObject);
                resultList.add(jsonObject);
            }
            PageInfo pageInfo1 = new PageInfo();
            pageInfo1.setList(resultList);
            pageInfo1.setPageSize(pageInfo.getPageSize());
            pageInfo1.setPages(pageInfo.getPages());
            pageInfo1.setTotal(pageInfo.getTotal());
            pageInfo1.setPageNum(pageInfo.getPageNum());
            PageUtil.initPageNumber(pageInfo, model);
            model.addAttribute("pageUrl", (Object)("/k8sMonitor/list?1=1" + url.toString()));
            model.addAttribute("page", (Object)pageInfo1);
            model.addAttribute("k8sMonitor", (Object)k8sMonitor);
            model.addAttribute("namespaceParam", (Object)namespaceParam);
            if ("container".equals(dataType)) {
                returlView = "k8s/containerList";
            }
            if ("namespace".equals(dataType)) {
                returlView = "k8s/namespaceList";
            }
            if ("service".equals(dataType)) {
                returlView = "k8s/serviceList";
            }
            if ("pod".equals(dataType)) {
                returlView = "k8s/podList";
            }
            if ("deployment".equals(dataType)) {
                returlView = "k8s/deploymentList";
            }
        }
        catch (Exception e) {
            logger.error("\u67e5\u8be2k8s\u76d1\u6d4b\u4fe1\u606f\u4fe1\u606f\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("\u67e5\u8be2k8s\u76d1\u6d4b\u4fe1\u606f\u4fe1\u606f\u9519\u8bef", e.toString(), "2");
        }
        return returlView;
    }

    @RequestMapping(value={"del"})
    public String delete(Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        String errorMsg = "\u5220\u9664K8S\u76d1\u6d4b\u9519\u8bef";
        String dataType = request.getParameter("dataType");
        try {
            if (!StringUtils.isEmpty((CharSequence)request.getParameter("id"))) {
                String[] ids = request.getParameter("id").split(",");
                this.k8sMonitorService.deleteById(ids);
            }
            this.logInfoService.save(HostUtil.getAccountByRequest(request).getAccount() + "\u5220\u9664K8S\u76d1\u63a7\u6570\u636e" + dataType, dataType, "2");
        }
        catch (Exception e) {
            logger.error(errorMsg, (Throwable)e);
            this.logInfoService.save(errorMsg, e.toString(), "2");
        }
        return "redirect:/k8sMonitor/list?dataType=" + dataType;
    }

    @RequestMapping(value={"viewContainer"})
    public String viewContainer(Model model, HttpServletRequest request) {
        String id = request.getParameter("id");
        K8sMonitor k8sMonitor = new K8sMonitor();
        try {
            k8sMonitor = this.k8sMonitorService.selectById(id);
            JSONObject jsonObject = new JSONObject();
            jsonObject.set("id", (Object)k8sMonitor.getId());
            jsonObject.set("k8sName", (Object)k8sMonitor.getK8sName());
            jsonObject.set("dataType", (Object)k8sMonitor.getDataType());
            jsonObject.set("createTime", (Object)k8sMonitor.getCreateTime());
            JSONObject dataObject = JSONUtil.parseObj((String)k8sMonitor.getDataJson());
            if (dataObject.get("limitCpu") == null) {
                dataObject.set("limitCpu", "");
            }
            if (dataObject.get("limitMemory") == null) {
                dataObject.set("limitMemory", "");
            }
            if (dataObject.get("requestCpu") == null) {
                dataObject.set("requestCpu", "");
            }
            if (dataObject.get("requestMemory") == null) {
                dataObject.set("requestMemory", "");
            }
            if (dataObject.get("ports") == null) {
                dataObject.set("ports", "");
            }
            jsonObject.putAll((Map)dataObject);
            model.addAttribute("containerInfo", (Object)jsonObject);
        }
        catch (Exception e) {
            logger.error("\u67e5\u770bcontainer\u8be6\u7ec6\u4fe1\u606f\u9519\u8bef", (Throwable)e);
        }
        return "k8s/containerView";
    }
}

