/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.agentController;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.wgcloud.util.AgentUtils;
import com.wgcloud.util.HostUtil;
import com.wgcloud.util.ThreadPoolUtil;
import com.wgcloud.util.TokenUtils;
import com.wgcloud.util.msg.WarnOtherUtil;
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
@RequestMapping(value={"/agentLastlogInfoGo"})
public class AgentLastlogInfoController {
    private static final Logger logger = LoggerFactory.getLogger(AgentLastlogInfoController.class);
    @Autowired
    private TokenUtils tokenUtils;
    @Autowired
    private AgentUtils agentUtils;

    @ResponseBody
    @RequestMapping(value={"/minTask"})
    public JSONObject minTask(@RequestBody String paramBean) {
        JSONObject agentJsonObject = (JSONObject)JSONUtil.parse((Object)paramBean);
        logger.debug("agent\u4e0a\u62a5\u4e3b\u673a\u6700\u540e\u7684\u767b\u5f55\u4fe1\u606f-------------" + agentJsonObject.toString());
        JSONObject resultJson = new JSONObject();
        if (!this.tokenUtils.checkAgentToken(agentJsonObject)) {
            logger.error("Token is error");
            resultJson.set("result", "Token is error");
            return resultJson;
        }
        String bindIp = agentJsonObject.getStr("bindIp");
        String lastlogWarnInfos = agentJsonObject.getStr("lastlogWarnInfos");
        Date nowtime = new Date();
        try {
            if (!StringUtils.isEmpty((CharSequence)lastlogWarnInfos)) {
                Runnable runnable = () -> {
                    try {
                        if (!StringUtils.isEmpty((CharSequence)lastlogWarnInfos)) {
                            WarnOtherUtil.sendLastlogWarnInfo(lastlogWarnInfos, bindIp);
                        }
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                };
                ThreadPoolUtil.executor.execute(runnable);
            }
            resultJson.set("result", "success");
        }
        catch (Exception e) {
            logger.error("\u89e3\u6790\u6700\u65b0\u767b\u5f55\u4e3b\u673a\u4fe1\u606f\u4e0a\u62a5\u6570\u636e\u9519\u8bef", (Throwable)e);
            resultJson.set("result", (Object)("error\uff1a" + e.toString()));
        }
        return resultJson;
    }

    @ResponseBody
    @RequestMapping(value={"/lastUserShowTask"})
    public JSONObject lastUserShowTask(@RequestBody String paramBean) {
        JSONObject agentJsonObject = (JSONObject)JSONUtil.parse((Object)paramBean);
        logger.debug("agent\u4e0a\u62a5\u4e3b\u673a\u6700\u540e\u7684\u7cfb\u7edf\u7528\u6237\u767b\u5f55\u4fe1\u606f-------------" + agentJsonObject.toString());
        JSONObject resultJson = new JSONObject();
        if (!this.tokenUtils.checkAgentToken(agentJsonObject)) {
            logger.error("Token is error");
            resultJson.set("result", "Token is error");
            return resultJson;
        }
        String bindIp = agentJsonObject.getStr("bindIp");
        String hostName = agentJsonObject.getStr("hostName");
        String lastUser = agentJsonObject.getStr("lastUser");
        Date nowTime = new Date();
        if (StringUtils.isEmpty((CharSequence)(bindIp = this.agentUtils.checkBindIP(bindIp, hostName)))) {
            resultJson.set("result", "error: bindIp is null");
            return resultJson;
        }
        try {
            if (lastUser != null) {
                HostUtil.setImportDataHandler(bindIp + "_LASTUSER", lastUser, nowTime);
            }
            resultJson.set("result", "success");
        }
        catch (Exception e) {
            logger.error("\u89e3\u6790\u4e3b\u673alast\u4e0a\u62a5\u6570\u636e\u9519\u8bef", (Throwable)e);
            resultJson.set("result", (Object)("error\uff1a" + e.toString()));
        }
        return resultJson;
    }
}

