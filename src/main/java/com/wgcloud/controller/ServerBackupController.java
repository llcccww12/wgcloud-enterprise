/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.controller;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.wgcloud.config.CommonConfig;
import com.wgcloud.service.LogInfoService;
import com.wgcloud.service.RedisMonitorService;
import com.wgcloud.util.IpUtil;
import com.wgcloud.util.ResDataUtils;
import com.wgcloud.util.ServerBackupUtil;
import com.wgcloud.util.TokenUtils;
import com.wgcloud.util.staticvar.StaticKeys;
import java.util.HashMap;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value={"/serverBackupMonitor"})
public class ServerBackupController {
    private static final Logger logger = LoggerFactory.getLogger(ServerBackupController.class);
    @Resource
    private RedisMonitorService redisMonitorService;
    @Resource
    private LogInfoService logInfoService;
    @Autowired
    private CommonConfig commonConfig;
    @Autowired
    private TokenUtils tokenUtils;

    @ResponseBody
    @RequestMapping(value={"/agentList"})
    public String minTaskMarkServerBackup(@RequestBody String paramBean, HttpServletRequest request) {
        JSONObject agentJsonObject = (JSONObject)JSONUtil.parse((Object)paramBean);
        boolean checkResult = this.tokenUtils.checkAgentToken(agentJsonObject);
        if (!checkResult) {
            logger.error("Token is error");
            return ResDataUtils.resetErrorJson("Token is error");
        }
        logger.debug("server-backup\u4e0a\u62a5IP\u6570\u636e-------------" + agentJsonObject.toString());
        try {
            logger.info("minTaskMarkServerBackup-------" + IpUtil.getIpAddr(request) + "---------" + agentJsonObject.getStr("bindIp"));
            ServerBackupUtil.cacheSaveServerBackupIP(agentJsonObject, request);
        }
        catch (Exception e) {
            logger.error("\u89e3\u6790server-backup\u4e0a\u62a5bindIp\u6570\u636e\u9519\u8bef", (Throwable)e);
            return ResDataUtils.resetErrorJson(e.toString());
        }
        return ResDataUtils.resetSuccessJson(null);
    }

    @RequestMapping(value={"list"})
    public String serverBackupList(Model model, HttpServletRequest request) {
        HashMap params = new HashMap();
        try {
            List<String> serverBackupIpList = ServerBackupUtil.getServerBackupIPList();
            if (serverBackupIpList.size() > 10 && !StaticKeys.LICENSE_STATE.equals("1")) {
                logger.info("serverBackup\u4e2a\u4eba\u7248\u53ea\u80fd\u76d1\u6d4b10\u9879");
                model.addAttribute("msg", (Object)("\u4e2a\u4eba\u7248\u53ea\u80fd\u76d1\u6d4b10\u9879\uff08\u603b\u6570\u91cf" + serverBackupIpList.size() + "\uff09"));
                serverBackupIpList = serverBackupIpList.subList(0, 10);
            }
            model.addAttribute("serverBackupIpList", serverBackupIpList);
        }
        catch (Exception e) {
            logger.error("\u67e5\u8be2serverBackupIp\u76d1\u63a7\u4fe1\u606f\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("\u67e5\u8be2serverBackupIp\u76d1\u63a7\u4fe1\u606f\u9519\u8bef", e.toString(), "2");
        }
        return "serverBackup/list";
    }
}

