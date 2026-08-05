/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.controller;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.wgcloud.config.CommonConfig;
import com.wgcloud.config.MailConfig;
import com.wgcloud.util.ResDataUtils;
import com.wgcloud.util.RestUtil;
import com.wgcloud.util.TokenUtils;
import com.wgcloud.util.license.LicenseUtil;
import com.wgcloud.util.staticvar.StaticKeys;
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
@RequestMapping(value={"/license"})
public class LicenseController {
    private static String serverVersionNow = "v3.6.8".toLowerCase();
    private static final Logger logger = LoggerFactory.getLogger(LicenseController.class);
    @Autowired
    private RestUtil restUtil;
    @Autowired
    private CommonConfig commonConfig;
    @Autowired
    private MailConfig mailConfig;
    @Autowired
    private TokenUtils tokenUtils;

    private void testThread() {
        new Thread(() -> logger.info("\u542f\u52a8\u5b50\u7ebf\u7a0b\u6d4b\u8bd5")).start();
    }

    @ResponseBody
    @RequestMapping(value={"getHostLoginWarnMail"})
    public String getHostLoginWarnMail(@RequestBody String paramBean) {
        JSONObject agentJsonObject = (JSONObject)JSONUtil.parse((Object)paramBean);
        if (!this.tokenUtils.checkAgentToken(agentJsonObject)) {
            logger.error("Token is error");
            return ResDataUtils.resetErrorJson("Token is error");
        }
        return this.mailConfig.getHostLoginWarnMail();
    }

    @ResponseBody
    @RequestMapping(value={"get"})
    public String getLicenseState(@RequestBody String paramBean) {
        JSONObject agentJsonObject = (JSONObject)JSONUtil.parse((Object)paramBean);
        if (!this.tokenUtils.checkAgentToken(agentJsonObject)) {
            logger.error("Token is error");
            return ResDataUtils.resetErrorJson("Token is error");
        }
        if (LicenseUtil.checkEnterpriseVersion()) {
            return "S";
        }
        return StaticKeys.LICENSE_STATE;
    }

    @ResponseBody
    @RequestMapping(value={"getDaemon"})
    public String getDaemon(HttpServletRequest httpServletRequest) {
        // 二开：server启动时已计算出自身 jar 的 md5 写入 StaticKeys.WGCLOUD_SERVER_RELEASE_MD5STR，
        // 这里直接返回该值给 agent（替代 wgcloud-daemon-release 守护进程）。
        String md5 = StaticKeys.WGCLOUD_SERVER_RELEASE_MD5STR;
        if (StringUtils.isEmpty(md5)) {
            md5 = "patched";
        }
        return "{\"result\":\"success\",\"md5\":\"" + md5 + "\"}";
    }
}

