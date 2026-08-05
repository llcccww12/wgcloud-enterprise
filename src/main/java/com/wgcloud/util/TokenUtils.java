/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.util;

import cn.hutool.json.JSONObject;
import com.wgcloud.config.CommonConfig;
import com.wgcloud.util.ResDataUtils;
import com.wgcloud.util.staticvar.StaticKeys;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TokenUtils {
    private static final Logger logger = LoggerFactory.getLogger(TokenUtils.class);
    @Autowired
    private CommonConfig commonConfig;

    public boolean checkAllowOpenData(JSONObject agentJsonObject) {
        if (null == agentJsonObject) {
            return false;
        }
        if (!"1".equals(agentJsonObject.get("fromAgent")) && !"true".equals(this.commonConfig.getOpenDataAPI())) {
            return false;
        }
        return "1".equals(agentJsonObject.get("fromAgent")) || StaticKeys.LICENSE_STATE.equals("1");
    }

    public boolean checkAgentToken(JSONObject agentJsonObject) {
        if (null == agentJsonObject) {
            return false;
        }
        String agentWgToken = agentJsonObject.getStr("wgToken");
        if (StringUtils.isEmpty((CharSequence)agentWgToken)) {
            return false;
        }
        return StaticKeys.SERVER_WGTOKEN_MD5STR.equals(agentWgToken = agentWgToken.toLowerCase());
    }

    public boolean checkAgentToken(HttpServletRequest request) {
        String agentWgToken = request.getParameter("wgToken");
        if (StringUtils.isEmpty((CharSequence)agentWgToken)) {
            return false;
        }
        return StaticKeys.SERVER_WGTOKEN_MD5STR.equals(agentWgToken = agentWgToken.toLowerCase());
    }

    public String preOpenDataAPICheck(JSONObject agentJsonObject) {
        if (!StaticKeys.LICENSE_STATE.equals("1")) {
            return ResDataUtils.resetErrorJson("The module needs to professional version. Please contact us at www.wgstart.com");
        }
        if (!"true".equals(this.commonConfig.getOpenDataAPI())) {
            return ResDataUtils.resetErrorJson("The data API not open");
        }
        if (!this.checkAgentToken(agentJsonObject)) {
            logger.error("Token is error");
            return ResDataUtils.resetErrorJson("Token is error");
        }
        return "";
    }
}

