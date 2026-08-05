/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.agentController;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.wgcloud.config.MailConfig;
import com.wgcloud.entity.HostMacInfo;
import com.wgcloud.service.HostMacInfoService;
import com.wgcloud.util.AgentUtils;
import com.wgcloud.util.ThreadPoolUtil;
import com.wgcloud.util.TokenUtils;
import com.wgcloud.util.msg.WarnMailUtil;
import com.wgcloud.util.staticvar.BatchData;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value={"/agentHostMacGo"})
public class AgentHostMacController {
    private static final Logger logger = LoggerFactory.getLogger(AgentHostMacController.class);
    @Autowired
    private HostMacInfoService hostMacInfoService;
    @Autowired
    private TokenUtils tokenUtils;
    @Autowired
    private MailConfig mailConfig;
    @Autowired
    private AgentUtils agentUtils;

    @ResponseBody
    @RequestMapping(value={"/minTask"})
    public JSONObject minTask(@RequestBody String paramBean) {
        JSONObject agentJsonObject = (JSONObject)JSONUtil.parse((Object)paramBean);
        logger.debug("agent\u4e0a\u62a5\u4e3b\u673a\u7684mac\u5730\u5740\u6570\u636e-------------" + agentJsonObject.toString());
        JSONObject resultJson = new JSONObject();
        if (!this.tokenUtils.checkAgentToken(agentJsonObject)) {
            logger.error("Token is error");
            resultJson.set("result", "Token is error");
            return resultJson;
        }
        String bindIp = agentJsonObject.getStr("bindIp");
        if (this.isExists(bindIp)) {
            logger.error("agentHostMacGo multiple times at the same time: " + bindIp);
            resultJson.set("result", (Object)("agentHostMacGo multiple times at the same time: " + bindIp));
            return resultJson;
        }
        JSONObject macAddressListJson = agentJsonObject.getJSONObject("macAddress");
        String hostName = agentJsonObject.getStr("hostName");
        Date nowtime = new Date();
        if (StringUtils.isEmpty((CharSequence)(bindIp = this.agentUtils.checkBindIP(bindIp, hostName)))) {
            resultJson.set("result", "error: bindIp is null");
            return resultJson;
        }
        try {
            if (macAddressListJson != null) {
                this.addMacAddress(macAddressListJson, nowtime, bindIp);
            }
            resultJson.set("result", "success");
        }
        catch (Exception e) {
            logger.error("\u89e3\u6790mac\u5730\u5740\u4e0a\u62a5\u6570\u636e\u9519\u8bef", (Throwable)e);
            resultJson.set("result", (Object)("error\uff1a" + e.toString()));
        }
        return resultJson;
    }

    private void addMacAddress(JSONObject macAddressListJson, Date nowTime, String bindIp) {
        try {
            ArrayList<HostMacInfo> hostMacInfoList = new ArrayList<HostMacInfo>();
            ArrayList<HostMacInfo> willSaveList = new ArrayList<HostMacInfo>();
            ArrayList<String> keys = new ArrayList<String>(macAddressListJson.keySet());
            for (String macName : keys) {
                String macAddress = macAddressListJson.getStr(macName);
                HostMacInfo bean = new HostMacInfo();
                bean.setMacName(macName);
                bean.setMacAddress(macAddress);
                bean.setHostname(bindIp);
                bean.setCreateTime(nowTime);
                hostMacInfoList.add(bean);
                willSaveList.add(bean);
            }
            this.checkMacInfo(bindIp, hostMacInfoList);
            if (!CollectionUtil.isEmpty(willSaveList)) {
                BatchData.HOST_MAC_LIST.addAll(willSaveList);
            }
        }
        catch (Exception e) {
            logger.error("\u89e3\u6790mac\u5730\u5740\u4e0a\u62a5\u6570\u636e\u9519\u8bef", (Throwable)e);
        }
    }

    private void checkMacInfo(String bindIp, List<HostMacInfo> hostMacInfoListNew) {
        try {
            if ("false".equals(this.mailConfig.getAllWarnMail()) || "false".equals(this.mailConfig.getMacInfoWarnMail())) {
                return;
            }
            HashMap<String, Object> params = new HashMap<String, Object>();
            params.put("hostname", bindIp);
            List<HostMacInfo> hostMacInfoList = this.hostMacInfoService.selectAllByParams(params);
            String delMacAddress = "";
            for (HostMacInfo oldMacInfo : hostMacInfoList) {
                if (StringUtils.isEmpty((CharSequence)oldMacInfo.getMacAddress())) continue;
                boolean isModify = true;
                for (HostMacInfo newMacInfo : hostMacInfoListNew) {
                    if (!oldMacInfo.getMacAddress().equals(newMacInfo.getMacAddress())) continue;
                    isModify = false;
                    break;
                }
                if (!isModify) continue;
                delMacAddress = delMacAddress + oldMacInfo.getMacAddress() + ",";
                break;
            }
            if (!StringUtils.isEmpty((CharSequence)delMacAddress)) {
                String finalDelMacAddress = delMacAddress.substring(0, delMacAddress.length() - 1);
                Runnable runnable = () -> WarnMailUtil.sendHostMacInfoDown(bindIp, finalDelMacAddress);
                ThreadPoolUtil.executor.execute(runnable);
            }
        }
        catch (Exception e) {
            logger.error("\u68c0\u67e5mac\u5730\u5740\u9519\u8bef", (Throwable)e);
        }
    }

    private boolean isExists(String bindIp) {
        if (StringUtils.isEmpty((CharSequence)bindIp)) {
            return true;
        }
        ArrayList<HostMacInfo> hostMacList = new ArrayList<HostMacInfo>();
        hostMacList.addAll(BatchData.HOST_MAC_LIST);
        for (HostMacInfo hostMacInfo : hostMacList) {
            if (!hostMacInfo.getHostname().equals(bindIp)) continue;
            return true;
        }
        return false;
    }
}

