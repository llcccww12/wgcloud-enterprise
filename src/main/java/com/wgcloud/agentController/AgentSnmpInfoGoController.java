/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.agentController;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.wgcloud.config.CommonConfig;
import com.wgcloud.entity.SnmpDeepInfo;
import com.wgcloud.entity.SnmpInfo;
import com.wgcloud.entity.SnmpState;
import com.wgcloud.service.LogInfoService;
import com.wgcloud.service.SnmpDeepInfoService;
import com.wgcloud.service.SnmpInfoService;
import com.wgcloud.service.SnmpStateService;
import com.wgcloud.util.MessageErrorUtils;
import com.wgcloud.util.ResDataUtils;
import com.wgcloud.util.SnmpDeepUtil;
import com.wgcloud.util.ThreadPoolUtil;
import com.wgcloud.util.TokenUtils;
import com.wgcloud.util.msg.WarnOtherUtil;
import com.wgcloud.util.msg.WarnPools;
import com.wgcloud.util.staticvar.BatchData;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value={"/agentSnmpInfoGo"})
public class AgentSnmpInfoGoController {
    private static final Logger logger = LoggerFactory.getLogger(AgentSnmpInfoGoController.class);
    @Resource
    private LogInfoService logInfoService;
    @Autowired
    private SnmpInfoService snmpInfoService;
    @Resource
    private SnmpDeepInfoService snmpDeepInfoService;
    @Autowired
    private SnmpStateService snmpStateService;
    @Autowired
    private MessageErrorUtils messageErrorUtils;
    @Autowired
    private TokenUtils tokenUtils;
    @Autowired
    private CommonConfig commonConfig;

    @ResponseBody
    @RequestMapping(value={"/minTask"})
    public String minTask(@RequestBody String paramBean) {
        JSONObject agentJsonObject = (JSONObject)JSONUtil.parse((Object)paramBean);
        boolean checkResult = this.tokenUtils.checkAgentToken(agentJsonObject);
        if (!checkResult) {
            logger.error("Token is error");
            return ResDataUtils.resetErrorJson("Token is error");
        }
        logger.debug("server-backup\u76d1\u63a7\u7f51\u7edc\u8bbe\u5907SNMP\u4e0a\u62a5\u6570\u636e-------------" + agentJsonObject.toString());
        Date nowtime = new Date();
        try {
            JSONArray snmpInfosJsonArr = agentJsonObject.getJSONArray("snmpInfosUpdate");
            if (snmpInfosJsonArr == null) {
                logger.error("snmpInfosUpdate is null");
                return ResDataUtils.resetErrorJson("snmpInfosUpdate is null");
            }
            List snmpInfoList = JSONUtil.toList((JSONArray)snmpInfosJsonArr, SnmpInfo.class);
            for (SnmpInfo snmpInfo : (java.util.List<SnmpInfo>)snmpInfoList) {
                SnmpInfo snmpInfoSaved = this.snmpInfoService.selectById(snmpInfo.getId());
                snmpInfo.setCreateTime(nowtime);
                this.messageErrorUtils.setErrorMsgHandler(snmpInfo.getId(), snmpInfo.getTestErrorMsg());
                if ("2".equals(snmpInfo.getState())) {
                    snmpInfo.setCreateTime(null);
                    this.messageErrorUtils.setErrorMsgHandler(snmpInfo.getId(), "Cannot ping device");
                }
                try {
                    this.snmpInfoService.updateById(snmpInfo);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                if ("1".equals(snmpInfo.getState())) {
                    SnmpState snmpState = new SnmpState();
                    if (!StringUtils.isEmpty((CharSequence)snmpInfo.getRecvAvgSum())) {
                        snmpState.setRecvAvg(snmpInfo.getRecvAvgSum());
                    } else {
                        snmpState.setRecvAvg("0");
                    }
                    if (!StringUtils.isEmpty((CharSequence)snmpInfo.getSentAvgSum())) {
                        snmpState.setSentAvg(snmpInfo.getSentAvgSum());
                    } else {
                        snmpState.setSentAvg("0");
                    }
                    snmpState.setCpuPer(snmpInfo.getCpuPer());
                    snmpState.setMemPer(snmpInfo.getMemPer());
                    snmpState.setTemperatureValue(snmpInfo.getTemperatureValue());
                    snmpState.setSnmpInfoId(snmpInfo.getId());
                    snmpState.setCreateTime(nowtime);
                    BatchData.SNMP_STATE_LIST.add(snmpState);
                }
                Runnable runnable = () -> {
                    if ("2".equals(snmpInfo.getState())) {
                        WarnOtherUtil.sendSnmpInfo(snmpInfoSaved, true);
                    } else if (!StringUtils.isEmpty((CharSequence)WarnPools.getWarnMark(snmpInfoSaved.getId()))) {
                        WarnOtherUtil.sendSnmpInfo(snmpInfoSaved, false);
                    }
                };
                ThreadPoolUtil.executor.execute(runnable);
            }
        }
        catch (Exception e) {
            logger.error("\u89e3\u6790server-backup\u76d1\u63a7\u7f51\u7edc\u8bbe\u5907SNMP\u4e0a\u62a5\u6570\u636e\u9519\u8bef", (Throwable)e);
            return ResDataUtils.resetErrorJson(e.toString());
        }
        return ResDataUtils.resetSuccessJson(null);
    }

    @ResponseBody
    @RequestMapping(value={"/minTaskDeep"})
    public String minTaskDeep(@RequestBody String paramBean) {
        JSONObject agentJsonObject = (JSONObject)JSONUtil.parse((Object)paramBean);
        boolean checkResult = this.tokenUtils.checkAgentToken(agentJsonObject);
        if (!checkResult) {
            logger.error("Token is error");
            return ResDataUtils.resetErrorJson("Token is error");
        }
        logger.debug("server-backup\u76d1\u63a7\u7f51\u7edc\u8bbe\u5907SNMP\u6df1\u5ea6\u76d1\u63a7\u4e0a\u62a5\u6570\u636e-------------" + agentJsonObject.toString());
        Date nowtime = new Date();
        try {
            JSONArray snmpInfosJsonArr = agentJsonObject.getJSONArray("snmpInfosUpdate");
            if (snmpInfosJsonArr == null) {
                logger.error("snmpInfosUpdate is null");
                return ResDataUtils.resetErrorJson("snmpInfosUpdate is null");
            }
            List snmpInfoList = JSONUtil.toList((JSONArray)snmpInfosJsonArr, SnmpDeepInfo.class);
            for (SnmpDeepInfo snmpInfo : (java.util.List<SnmpDeepInfo>)snmpInfoList) {
                SnmpDeepInfo snmpInfoSaved = this.snmpDeepInfoService.selectById(snmpInfo.getId());
                snmpInfo.setCreateTime(nowtime);
                this.messageErrorUtils.setErrorMsgHandler(snmpInfo.getId(), snmpInfo.getTestErrorMsg());
                if ("2".equals(snmpInfo.getState())) {
                    snmpInfo.setCreateTime(null);
                    this.messageErrorUtils.setErrorMsgHandler(snmpInfo.getId(), "Cannot ping device");
                }
                try {
                    this.snmpDeepInfoService.updateById(snmpInfo);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                Runnable runnable = () -> {
                    if ("2".equals(snmpInfo.getState())) {
                        WarnOtherUtil.sendSnmpDeepInfo(snmpInfoSaved, true);
                    } else if (!StringUtils.isEmpty((CharSequence)WarnPools.getWarnMark(snmpInfoSaved.getId()))) {
                        WarnOtherUtil.sendSnmpDeepInfo(snmpInfoSaved, false);
                    }
                };
                ThreadPoolUtil.executor.execute(runnable);
            }
            JSONArray snmpDeepStateJsonArr = agentJsonObject.getJSONArray("snmpDeepStateList");
            if (snmpDeepStateJsonArr == null) {
                logger.error("snmpDeepStateJsonArr is null");
                return ResDataUtils.resetErrorJson("snmpDeepStateJsonArr is null");
            }
            for (Object snmpDeepStateObj : snmpDeepStateJsonArr) {
                JSONObject snmpDeepStateJson = JSONUtil.parseObj(snmpDeepStateObj);
                SnmpDeepUtil.setSnmpDeepResultHandler(snmpDeepStateJson.getStr("name"), snmpDeepStateJson.getStr("value"));
            }
        }
        catch (Exception e) {
            logger.error("\u89e3\u6790server-backup\u76d1\u63a7\u7f51\u7edc\u8bbe\u5907SNMP\u6df1\u5ea6\u76d1\u63a7\u4e0a\u62a5\u6570\u636e\u9519\u8bef", (Throwable)e);
            return ResDataUtils.resetErrorJson(e.toString());
        }
        return ResDataUtils.resetSuccessJson(null);
    }
}

