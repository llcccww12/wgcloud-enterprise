/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.agentController;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.wgcloud.config.CommonConfig;
import com.wgcloud.service.LogInfoService;
import com.wgcloud.service.RedisMonitorService;
import com.wgcloud.util.DateUtil;
import com.wgcloud.util.IpUtil;
import com.wgcloud.util.PowerEnvUtil;
import com.wgcloud.util.ResDataUtils;
import com.wgcloud.util.TokenUtils;
import com.wgcloud.util.license.LicenseUtil;
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
@RequestMapping(value={"/agentPowerEnvGo"})
public class AgentPowerEnvGoController {
    private static final Logger logger = LoggerFactory.getLogger(AgentPowerEnvGoController.class);
    @Resource
    private LogInfoService logInfoService;
    @Resource
    private RedisMonitorService redisMonitorService;
    @Autowired
    private TokenUtils tokenUtils;
    @Autowired
    private CommonConfig commonConfig;

    @ResponseBody
    @RequestMapping(value={"/minTask"})
    public String minTask(@RequestBody String paramBean, HttpServletRequest request) {
        logger.info("server-backup request PowerEnv-------" + IpUtil.getIpAddr(request));
        JSONObject agentJsonObject = (JSONObject)JSONUtil.parse((Object)paramBean);
        boolean checkResult = this.tokenUtils.checkAgentToken(agentJsonObject);
        if (!checkResult) {
            logger.error("Token is error");
            return ResDataUtils.resetErrorJson("Token is error");
        }
        if (!LicenseUtil.checkEnterpriseVersion()) {
            logger.error("\u76d1\u63a7\u52a8\u73af\u6570\u636e\u9700\u8981\u5347\u7ea7\u5230\u4f01\u4e1a\u7248");
            agentJsonObject.set("data", "\u8bf7\u5347\u7ea7\u5230\u4f01\u4e1a\u7248\u67e5\u770b\u6b64\u6570\u636e");
            agentJsonObject.set("createTime", (Object)DateUtil.getDateTimeString(new Date()));
            PowerEnvUtil.setPowerEnvHandler(agentJsonObject.getStr("name"), agentJsonObject);
            return ResDataUtils.resetErrorJson("\u8bf7\u5347\u7ea7\u5230\u4f01\u4e1a\u7248\u67e5\u770b\u6b64\u6570\u636e");
        }
        logger.debug("server-backup\u76d1\u63a7\u52a8\u73af\u4e0a\u62a5\u6570\u636e-------------" + agentJsonObject.toString());
        try {
            String name = agentJsonObject.getStr("name");
            agentJsonObject.set("createTime", (Object)DateUtil.getDateTimeString(new Date()));
            PowerEnvUtil.setPowerEnvHandler(name, agentJsonObject);
        }
        catch (Exception e) {
            logger.error("\u89e3\u6790server-backup\u76d1\u63a7\u52a8\u73af\u4e0a\u62a5\u6570\u636e\u9519\u8bef", (Throwable)e);
            return ResDataUtils.resetErrorJson(e.toString());
        }
        return ResDataUtils.resetSuccessJson(null);
    }
}

