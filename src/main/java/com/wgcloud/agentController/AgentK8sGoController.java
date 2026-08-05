/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.agentController;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.wgcloud.config.CommonConfig;
import com.wgcloud.entity.K8sMonitor;
import com.wgcloud.service.K8sMonitorService;
import com.wgcloud.service.LogInfoService;
import com.wgcloud.util.IpUtil;
import com.wgcloud.util.ResDataUtils;
import com.wgcloud.util.TokenUtils;
import java.util.ArrayList;
import java.util.Date;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value={"/agentK8sGo"})
public class AgentK8sGoController {
    private static final Logger logger = LoggerFactory.getLogger(AgentK8sGoController.class);
    @Resource
    private LogInfoService logInfoService;
    @Resource
    private K8sMonitorService k8sMonitorService;
    @Autowired
    private TokenUtils tokenUtils;
    @Autowired
    private CommonConfig commonConfig;

    @ResponseBody
    @RequestMapping(value={"/minTask"})
    public String minTask(@RequestBody String paramBean, HttpServletRequest request) {
        logger.info("server-backup request K8S-------" + IpUtil.getIpAddr(request));
        JSONObject agentJsonObject = (JSONObject)JSONUtil.parse((Object)paramBean);
        boolean checkResult = this.tokenUtils.checkAgentToken(agentJsonObject);
        if (!checkResult) {
            logger.error("Token is error");
            return ResDataUtils.resetErrorJson("Token is error");
        }
        logger.debug("server-backup\u76d1\u63a7k8s\u4e0a\u62a5\u6570\u636e-------------" + agentJsonObject.toString());
        Date nowtime = new Date();
        try {
            K8sMonitor k8sMonitor;
            JSONArray namespaceJsonArr = agentJsonObject.getJSONArray("namespace");
            JSONArray podJsonArr = agentJsonObject.getJSONArray("pod");
            JSONArray serviceJsonArr = agentJsonObject.getJSONArray("service");
            JSONArray containerJsonArr = agentJsonObject.getJSONArray("container");
            JSONArray nodeJsonArr = agentJsonObject.getJSONArray("node");
            JSONArray deploymentJsonArr = agentJsonObject.getJSONArray("deployment");
            String k8sName = agentJsonObject.getStr("k8sName");
            this.k8sMonitorService.deleteByK8sName(k8sName);
            ArrayList<K8sMonitor> willSaveList = new ArrayList<K8sMonitor>();
            for (Object obj : namespaceJsonArr) {
                k8sMonitor = new K8sMonitor();
                k8sMonitor.setK8sName(k8sName);
                k8sMonitor.setDataType("namespace");
                k8sMonitor.setCreateTime(nowtime);
                k8sMonitor.setDataJson(JSONUtil.toJsonStr(obj));
                willSaveList.add(k8sMonitor);
            }
            for (Object obj : podJsonArr) {
                k8sMonitor = new K8sMonitor();
                k8sMonitor.setK8sName(k8sName);
                k8sMonitor.setDataType("pod");
                k8sMonitor.setCreateTime(nowtime);
                k8sMonitor.setDataJson(JSONUtil.toJsonStr(obj));
                willSaveList.add(k8sMonitor);
            }
            for (Object obj : serviceJsonArr) {
                k8sMonitor = new K8sMonitor();
                k8sMonitor.setK8sName(k8sName);
                k8sMonitor.setDataType("service");
                k8sMonitor.setCreateTime(nowtime);
                k8sMonitor.setDataJson(JSONUtil.toJsonStr(obj));
                willSaveList.add(k8sMonitor);
            }
            for (Object obj : containerJsonArr) {
                k8sMonitor = new K8sMonitor();
                k8sMonitor.setK8sName(k8sName);
                k8sMonitor.setDataType("container");
                k8sMonitor.setCreateTime(nowtime);
                k8sMonitor.setDataJson(JSONUtil.toJsonStr(obj));
                willSaveList.add(k8sMonitor);
            }
            for (Object obj : nodeJsonArr) {
                k8sMonitor = new K8sMonitor();
                k8sMonitor.setK8sName(k8sName);
                k8sMonitor.setDataType("node");
                k8sMonitor.setCreateTime(nowtime);
                k8sMonitor.setDataJson(JSONUtil.toJsonStr(obj));
                willSaveList.add(k8sMonitor);
            }
            for (Object obj : deploymentJsonArr) {
                k8sMonitor = new K8sMonitor();
                k8sMonitor.setK8sName(k8sName);
                k8sMonitor.setDataType("deployment");
                k8sMonitor.setCreateTime(nowtime);
                k8sMonitor.setDataJson(JSONUtil.toJsonStr(obj));
                willSaveList.add(k8sMonitor);
            }
            this.k8sMonitorService.saveRecord(willSaveList);
        }
        catch (Exception e) {
            logger.error("\u89e3\u6790server-backup\u76d1\u63a7k8s\u4e0a\u62a5\u6570\u636e\u9519\u8bef", (Throwable)e);
            return ResDataUtils.resetErrorJson(e.toString());
        }
        return ResDataUtils.resetSuccessJson(null);
    }
}

