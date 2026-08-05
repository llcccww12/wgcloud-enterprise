/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.agentController;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.wgcloud.config.CommonConfig;
import com.wgcloud.entity.KafkaMonitor;
import com.wgcloud.service.KafkaMonitorService;
import com.wgcloud.service.LogInfoService;
import com.wgcloud.util.ActivemqUtil;
import com.wgcloud.util.DateUtil;
import com.wgcloud.util.IpUtil;
import com.wgcloud.util.MessageErrorUtils;
import com.wgcloud.util.RabbitmqUtil;
import com.wgcloud.util.ResDataUtils;
import com.wgcloud.util.ThreadPoolUtil;
import com.wgcloud.util.TokenUtils;
import com.wgcloud.util.msg.WarnOtherUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value={"/agentKafkaGo"})
public class AgentKafkaGoController {
    private static final Logger logger = LoggerFactory.getLogger(AgentKafkaGoController.class);
    @Resource
    private LogInfoService logInfoService;
    @Resource
    private KafkaMonitorService kafkaMonitorService;
    @Autowired
    private MessageErrorUtils messageErrorUtils;
    @Autowired
    private TokenUtils tokenUtils;
    @Autowired
    private CommonConfig commonConfig;

    @ResponseBody
    @RequestMapping(value={"/minTask"})
    public String minTask(@RequestBody String paramBean, HttpServletRequest request) {
        logger.info("server-backup request Kafka-------" + IpUtil.getIpAddr(request));
        JSONObject agentJsonObject = (JSONObject)JSONUtil.parse((Object)paramBean);
        boolean checkResult = this.tokenUtils.checkAgentToken(agentJsonObject);
        if (!checkResult) {
            logger.error("Token is error");
            return ResDataUtils.resetErrorJson("Token is error");
        }
        logger.debug("server-backup\u76d1\u63a7kafka\u4e0a\u62a5\u6570\u636e-------------" + agentJsonObject.toString());
        Date nowtime = new Date();
        try {
            String code = agentJsonObject.getStr("code");
            String kafkaName = agentJsonObject.getStr("kafkaName");
            if ("2".equals(code)) {
                this.kafkaMonitorService.downByKafkaName(kafkaName);
                String testErrorMsg = agentJsonObject.getStr("testErrorMsg");
                if (!StringUtils.isEmpty((CharSequence)testErrorMsg)) {
                    this.messageErrorUtils.setErrorMsgHandler(kafkaName, testErrorMsg);
                }
                Runnable runnable = () -> WarnOtherUtil.sendMiddlewareInfo("Kafka-" + kafkaName, true);
                ThreadPoolUtil.executor.execute(runnable);
                return ResDataUtils.resetSuccessJson(null);
            }
            this.kafkaMonitorService.deleteByKafkaName(kafkaName);
            JSONArray dataJsonArr = agentJsonObject.getJSONArray("data");
            List kafkaMonitorList = JSONUtil.toList((JSONArray)dataJsonArr, KafkaMonitor.class);
            ArrayList<KafkaMonitor> willSaveList = new ArrayList<KafkaMonitor>();
            for (KafkaMonitor kafkaMonitor : (java.util.List<KafkaMonitor>)kafkaMonitorList) {
                kafkaMonitor.setCreateTime(nowtime);
                kafkaMonitor.setState(code);
                willSaveList.add(kafkaMonitor);
            }
            this.kafkaMonitorService.saveRecord(willSaveList);
        }
        catch (Exception e) {
            logger.error("\u89e3\u6790server-backup\u76d1\u63a7kafka\u4e0a\u62a5\u6570\u636e\u9519\u8bef", (Throwable)e);
            return ResDataUtils.resetErrorJson(e.toString());
        }
        return ResDataUtils.resetSuccessJson(null);
    }

    @ResponseBody
    @RequestMapping(value={"/minTaskRabbitmq"})
    public String minTaskRabbitmq(@RequestBody String paramBean, HttpServletRequest request) {
        logger.info("server-backup request Rabbitmq-------" + IpUtil.getIpAddr(request));
        JSONObject agentJsonObject = (JSONObject)JSONUtil.parse((Object)paramBean);
        boolean checkResult = this.tokenUtils.checkAgentToken(agentJsonObject);
        if (!checkResult) {
            logger.error("Token is error");
            return ResDataUtils.resetErrorJson("Token is error");
        }
        logger.info("server-backup\u76d1\u63a7Rabbitmq\u4e0a\u62a5\u6570\u636e-------------" + agentJsonObject.toString());
        try {
            JSONArray monitorsJsonArr = agentJsonObject.getJSONArray("data");
            for (Object object : monitorsJsonArr) {
                JSONObject monitorJsonObject = (JSONObject)JSONUtil.parse(object);
                String queueName = monitorJsonObject.getStr("queueName");
                String name = monitorJsonObject.getStr("name");
                monitorJsonObject.set("createTime", (Object)DateUtil.getDateTimeString(new Date()));
                RabbitmqUtil.setRabbitmqHandler(name + "_" + queueName, monitorJsonObject);
                String code = monitorJsonObject.getStr("code");
                if (!"2".equals(code)) continue;
                Runnable runnable = () -> WarnOtherUtil.sendMiddlewareInfo("RabbitMQ-" + name, true);
                ThreadPoolUtil.executor.execute(runnable);
            }
        }
        catch (Exception e) {
            logger.error("\u89e3\u6790server-backup\u76d1\u63a7Rabbitmq\u4e0a\u62a5\u6570\u636e\u9519\u8bef", (Throwable)e);
            return ResDataUtils.resetErrorJson(e.toString());
        }
        return ResDataUtils.resetSuccessJson(null);
    }

    @ResponseBody
    @RequestMapping(value={"/minTaskActivemq"})
    public String minTaskActivemq(@RequestBody String paramBean, HttpServletRequest request) {
        logger.info("server-backup request Activemq-------" + IpUtil.getIpAddr(request));
        JSONObject agentJsonObject = (JSONObject)JSONUtil.parse((Object)paramBean);
        boolean checkResult = this.tokenUtils.checkAgentToken(agentJsonObject);
        if (!checkResult) {
            logger.error("Token is error");
            return ResDataUtils.resetErrorJson("Token is error");
        }
        logger.info("server-backup\u76d1\u63a7Activemq\u4e0a\u62a5\u6570\u636e-------------" + agentJsonObject.toString());
        try {
            JSONArray monitorsJsonArr = agentJsonObject.getJSONArray("data");
            for (Object object : monitorsJsonArr) {
                JSONObject monitorJsonObject = (JSONObject)JSONUtil.parse(object);
                String queueName = monitorJsonObject.getStr("queueName");
                String name = monitorJsonObject.getStr("name");
                monitorJsonObject.set("createTime", (Object)DateUtil.getDateTimeString(new Date()));
                ActivemqUtil.setActivemqHandler(name + "_" + queueName, monitorJsonObject);
                String code = monitorJsonObject.getStr("code");
                if (!"2".equals(code)) continue;
                Runnable runnable = () -> WarnOtherUtil.sendMiddlewareInfo("ActiveMQ-" + name, true);
                ThreadPoolUtil.executor.execute(runnable);
            }
        }
        catch (Exception e) {
            logger.error("\u89e3\u6790server-backup\u76d1\u63a7Activemq\u4e0a\u62a5\u6570\u636e\u9519\u8bef", (Throwable)e);
            return ResDataUtils.resetErrorJson(e.toString());
        }
        return ResDataUtils.resetSuccessJson(null);
    }
}

