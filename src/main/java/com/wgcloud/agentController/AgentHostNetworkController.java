/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.agentController;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.wgcloud.dto.NetworkInfoDto;
import com.wgcloud.util.AgentUtils;
import com.wgcloud.util.FormatUtil;
import com.wgcloud.util.HostUtil;
import com.wgcloud.util.TokenUtils;
import java.util.ArrayList;
import java.util.Date;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value={"/agentHostNetworkGo"})
public class AgentHostNetworkController {
    private static final Logger logger = LoggerFactory.getLogger(AgentHostNetworkController.class);
    @Autowired
    private TokenUtils tokenUtils;
    @Autowired
    private AgentUtils agentUtils;

    @ResponseBody
    @RequestMapping(value={"/minTask"})
    public JSONObject minTask(@RequestBody String paramBean) {
        JSONObject agentJsonObject = (JSONObject)JSONUtil.parse((Object)paramBean);
        logger.debug("agent\u4e0a\u62a5\u4e3b\u673a\u7684\u7f51\u5361\u6570\u636e-------------" + agentJsonObject.toString());
        JSONObject resultJson = new JSONObject();
        if (!this.tokenUtils.checkAgentToken(agentJsonObject)) {
            logger.error("Token is error");
            resultJson.set("result", "Token is error");
            return resultJson;
        }
        String bindIp = agentJsonObject.getStr("bindIp");
        JSONArray allnetworkListJson = agentJsonObject.getJSONArray("allNetwork");
        String hostName = agentJsonObject.getStr("hostName");
        Date nowTime = new Date();
        if (StringUtils.isEmpty((CharSequence)(bindIp = this.agentUtils.checkBindIP(bindIp, hostName)))) {
            resultJson.set("result", "error: bindIp is null");
            return resultJson;
        }
        try {
            if (allnetworkListJson != null) {
                ArrayList<NetworkInfoDto> networkInfoDtoList = new ArrayList<NetworkInfoDto>();
                for (Object obj : allnetworkListJson) {
                    JSONObject jsonObject = JSONUtil.parseObj(obj);
                    NetworkInfoDto networkInfoDto = new NetworkInfoDto();
                    networkInfoDto.setName(jsonObject.getStr("name"));
                    Long recvBytes = jsonObject.getLong("bytesRecv");
                    if (null != recvBytes) {
                        double recvGB = FormatUtil.formatDouble((double)recvBytes.longValue() / 1024.0 / 1024.0 / 1024.0, 2);
                        networkInfoDto.setBytesRecv(recvGB + "G");
                    } else {
                        networkInfoDto.setBytesRecv("0G");
                    }
                    Long bytesSent = jsonObject.getLong("bytesSent");
                    if (null != bytesSent) {
                        double sentGB = FormatUtil.formatDouble((double)bytesSent.longValue() / 1024.0 / 1024.0 / 1024.0, 2);
                        networkInfoDto.setBytesSent(sentGB + "G");
                    } else {
                        networkInfoDto.setBytesSent("0G");
                    }
                    networkInfoDtoList.add(networkInfoDto);
                }
                HostUtil.setImportDataHandler(bindIp + "_ALL_NETWORK", JSONUtil.toJsonStr(networkInfoDtoList), nowTime);
            }
            resultJson.set("result", "success");
        }
        catch (Exception e) {
            logger.error("\u89e3\u6790\u4e3b\u673a\u7f51\u5361\u4e0a\u62a5\u6570\u636e\u9519\u8bef", (Throwable)e);
            resultJson.set("result", (Object)("error\uff1a" + e.toString()));
        }
        return resultJson;
    }

    @ResponseBody
    @RequestMapping(value={"/ifconfigTask"})
    public JSONObject ifconfigTask(@RequestBody String paramBean) {
        JSONObject agentJsonObject = (JSONObject)JSONUtil.parse((Object)paramBean);
        logger.debug("agent\u4e0a\u62a5\u4e3b\u673a\u7684ifconfig\u6570\u636e-------------" + agentJsonObject.toString());
        JSONObject resultJson = new JSONObject();
        if (!this.tokenUtils.checkAgentToken(agentJsonObject)) {
            logger.error("Token is error");
            resultJson.set("result", "Token is error");
            return resultJson;
        }
        String bindIp = agentJsonObject.getStr("bindIp");
        String ifconfigDataJson = agentJsonObject.getStr("ifconfigData");
        String hostName = agentJsonObject.getStr("hostName");
        Date nowTime = new Date();
        if (StringUtils.isEmpty((CharSequence)(bindIp = this.agentUtils.checkBindIP(bindIp, hostName)))) {
            resultJson.set("result", "error: bindIp is null");
            return resultJson;
        }
        try {
            if (ifconfigDataJson != null) {
                HostUtil.setImportDataHandler(bindIp + "_IFCONFIG", ifconfigDataJson, nowTime);
            }
            resultJson.set("result", "success");
        }
        catch (Exception e) {
            logger.error("\u89e3\u6790\u4e3b\u673aifconfig\u4e0a\u62a5\u6570\u636e\u9519\u8bef", (Throwable)e);
            resultJson.set("result", (Object)("error\uff1a" + e.toString()));
        }
        return resultJson;
    }
}

