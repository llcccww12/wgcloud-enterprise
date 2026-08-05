/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.agentController;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.wgcloud.config.CommonConfig;
import com.wgcloud.config.MailConfig;
import com.wgcloud.entity.AppInfo;
import com.wgcloud.entity.FileSafe;
import com.wgcloud.entity.FileWarnInfo;
import com.wgcloud.service.AppInfoService;
import com.wgcloud.service.CustomInfoService;
import com.wgcloud.service.FileSafeService;
import com.wgcloud.service.FileWarnInfoService;
import com.wgcloud.service.LogInfoService;
import com.wgcloud.util.DateUtil;
import com.wgcloud.util.HostUtil;
import com.wgcloud.util.MessageErrorUtils;
import com.wgcloud.util.ThreadPoolUtil;
import com.wgcloud.util.TokenUtils;
import com.wgcloud.util.msg.WarnOtherUtil;
import com.wgcloud.util.msg.WarnPools;
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
@RequestMapping(value={"/agentCallBackGo"})
public class AgentCallBackController {
    private static final Logger logger = LoggerFactory.getLogger(AgentCallBackController.class);
    @Resource
    private LogInfoService logInfoService;
    @Resource
    private AppInfoService appInfoService;
    @Resource
    private FileWarnInfoService fileWarnInfoService;
    @Resource
    private CustomInfoService customInfoService;
    @Resource
    private FileSafeService fileSafeService;
    @Autowired
    private MessageErrorUtils messageErrorUtils;
    @Autowired
    private TokenUtils tokenUtils;
    @Autowired
    private CommonConfig commonConfig;
    @Autowired
    private MailConfig mailConfig;
    private static String MONITOR_ERROR_PREFIX = "001-";

    @ResponseBody
    @RequestMapping(value={"/callBackMsgFileWarn"})
    public JSONObject callBackMsgFileWarn(@RequestBody String paramBean, HttpServletRequest request) {
        JSONObject agentJsonObject = (JSONObject)JSONUtil.parse((Object)paramBean);
        logger.debug("agent\u4e0a\u62a5\u6210\u529f\u6267\u884c\u65e5\u5fd7\u544a\u8b66\u7684\u81ea\u5b9a\u4e49\u5904\u7406\u6307\u4ee4-------------" + agentJsonObject.toString());
        JSONObject resultJson = new JSONObject();
        if (!this.tokenUtils.checkAgentToken(agentJsonObject)) {
            logger.error("Token is error");
            resultJson.set("result", "Token is error");
            return resultJson;
        }
        if (!"1".equals(agentJsonObject.get("fromAgent"))) {
            return resultJson;
        }
        String fileWarnInfoId = agentJsonObject.getStr("fileWarnInfoId");
        String filePath = agentJsonObject.getStr("filePath");
        String state = agentJsonObject.getStr("state");
        try {
            FileWarnInfo fileWarnInfo;
            if (!StringUtils.isEmpty((CharSequence)fileWarnInfoId) && null != (fileWarnInfo = this.fileWarnInfoService.selectById(fileWarnInfoId))) {
                String key = fileWarnInfoId + "_CallBackMsg";
                if (!StringUtils.isEmpty((CharSequence)state) && !state.startsWith(MONITOR_ERROR_PREFIX)) {
                    this.messageErrorUtils.setErrorMsgHandler(key, DateUtil.getCurrentDateTime() + " " + state);
                }
                if (!this.checkSendMsg(key, this.mailConfig.getFileLogWarnMail())) {
                    return resultJson;
                }
                Runnable runnable = () -> {
                    try {
                        String fileRemark = "";
                        if (!StringUtils.isEmpty((CharSequence)fileWarnInfo.getRemark())) {
                            fileRemark = fileWarnInfo.getRemark();
                        }
                        String title = "\u65e5\u5fd7\u544a\u8b66\u81ea\u52a8\u5904\u7406\u6307\u4ee4\u5df2\u6267\u884c\u5b8c\u6210\uff1a" + fileWarnInfo.getHostname() + HostUtil.addRemark(fileWarnInfo.getHostname()) + "\uff0c\u65e5\u5fd7\u6587\u4ef6\uff1a" + filePath;
                        String commContent = "\u65e5\u5fd7\u544a\u8b66\u81ea\u52a8\u5904\u7406\u6307\u4ee4\u5df2\u6267\u884c\u5b8c\u6210\uff1a" + fileWarnInfo.getHostname() + HostUtil.addRemark(fileWarnInfo.getHostname()) + "\uff0c\u65e5\u5fd7\u5907\u6ce8\uff1a" + fileRemark + "\uff0c\u65e5\u5fd7\u6587\u4ef6\uff1a" + filePath + "\uff0c\u6267\u884c\u7ed3\u679c\uff1a" + state;
                        if (!StringUtils.isEmpty((CharSequence)state) && state.startsWith(MONITOR_ERROR_PREFIX)) {
                            this.messageErrorUtils.setErrorMsgHandler(fileWarnInfoId, DateUtil.getCurrentDateTime() + " " + state);
                            title = "\u65e5\u5fd7\u544a\u8b66\uff1a" + fileWarnInfo.getHostname() + HostUtil.addRemark(fileWarnInfo.getHostname()) + "\uff0c\u65e5\u5fd7\u6587\u4ef6\uff1a" + filePath;
                            commContent = "\u65e5\u5fd7\u544a\u8b66\uff1a" + fileWarnInfo.getHostname() + HostUtil.addRemark(fileWarnInfo.getHostname()) + "\uff0c\u65e5\u5fd7\u5907\u6ce8\uff1a" + fileRemark + "\uff0c\u65e5\u5fd7\u6587\u4ef6\uff1a" + filePath + "\uff0c\u6267\u884c\u7ed3\u679c\uff1a" + state;
                        }
                        WarnOtherUtil.sendUtil(title, commContent, null, key, true, "INFO", "");
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                };
                ThreadPoolUtil.executor.execute(runnable);
            }
        }
        catch (Exception e) {
            logger.error("agent\u4e0a\u62a5\u6210\u529f\u6267\u884c\u65e5\u5fd7\u544a\u8b66\u7684\u81ea\u5b9a\u4e49\u5904\u7406\u6307\u4ee4\u606f\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("agent\u4e0a\u62a5\u6210\u529f\u6267\u884c\u65e5\u5fd7\u544a\u8b66\u7684\u81ea\u5b9a\u4e49\u5904\u7406\u6307\u4ee4\u606f\u9519\u8bef", e.toString(), "2");
        }
        resultJson.set("result", "success");
        return resultJson;
    }

    @ResponseBody
    @RequestMapping(value={"/callBackMsgFileSafe"})
    public JSONObject callBackMsgFileSafe(@RequestBody String paramBean, HttpServletRequest request) {
        JSONObject agentJsonObject = (JSONObject)JSONUtil.parse((Object)paramBean);
        logger.debug("agent\u4e0a\u62a5\u6210\u529f\u6267\u884c\u6587\u4ef6\u9632\u7be1\u6539\u7684\u81ea\u5b9a\u4e49\u5904\u7406\u6307\u4ee4-------------" + agentJsonObject.toString());
        JSONObject resultJson = new JSONObject();
        if (!this.tokenUtils.checkAgentToken(agentJsonObject)) {
            logger.error("Token is error");
            resultJson.set("result", "Token is error");
            return resultJson;
        }
        if (!"1".equals(agentJsonObject.get("fromAgent"))) {
            return resultJson;
        }
        String appId = agentJsonObject.getStr("appId");
        String state = agentJsonObject.getStr("state");
        try {
            FileSafe fileSafe;
            if (!StringUtils.isEmpty((CharSequence)appId) && null != (fileSafe = this.fileSafeService.selectById(appId))) {
                String key = appId + "_CallBackMsg";
                this.messageErrorUtils.setErrorMsgHandler(key, DateUtil.getCurrentDateTime() + " " + state);
                if (!this.checkSendMsg(key, this.mailConfig.getFileSafeWarnMail())) {
                    return resultJson;
                }
                Runnable runnable = () -> {
                    try {
                        String title = "\u6587\u4ef6\u9632\u7be1\u6539\u81ea\u52a8\u5904\u7406\u6307\u4ee4\u5df2\u6267\u884c\u5b8c\u6210\uff1a" + fileSafe.getHostname() + HostUtil.addRemark(fileSafe.getHostname()) + "\uff0c\u6587\u4ef6\u540d\u79f0" + fileSafe.getFileName();
                        String commContent = "\u6587\u4ef6\u9632\u7be1\u6539\u81ea\u52a8\u5904\u7406\u6307\u4ee4\u5df2\u6267\u884c\u5b8c\u6210\uff1a" + fileSafe.getHostname() + HostUtil.addRemark(fileSafe.getHostname()) + "\uff0c\u6587\u4ef6\u540d\u79f0\uff1a" + fileSafe.getFileName() + "\uff0c\u6587\u4ef6\u8def\u5f84\uff1a" + fileSafe.getFilePath() + "\uff0c\u6267\u884c\u7ed3\u679c\uff1a" + state;
                        WarnOtherUtil.sendUtil(title, commContent, null, key, true, "INFO", "");
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                };
                ThreadPoolUtil.executor.execute(runnable);
            }
        }
        catch (Exception e) {
            logger.error("agent\u4e0a\u62a5\u6210\u529f\u6267\u884c\u6587\u4ef6\u9632\u7be1\u6539\u7684\u81ea\u5b9a\u4e49\u5904\u7406\u6307\u4ee4\u606f\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("agent\u4e0a\u62a5\u6210\u529f\u6267\u884c\u6587\u4ef6\u9632\u7be1\u6539\u7684\u81ea\u5b9a\u4e49\u5904\u7406\u6307\u4ee4\u606f\u9519\u8bef", e.toString(), "2");
        }
        resultJson.set("result", "success");
        return resultJson;
    }

    @ResponseBody
    @RequestMapping(value={"/callBackMsgApp"})
    public JSONObject callBackMsgApp(@RequestBody String paramBean, HttpServletRequest request) {
        JSONObject agentJsonObject = (JSONObject)JSONUtil.parse((Object)paramBean);
        logger.debug("agent\u4e0a\u62a5\u6210\u529f\u6267\u884c\u8fdb\u7a0b\u76d1\u63a7\u7684\u81ea\u5b9a\u4e49\u5904\u7406\u6307\u4ee4-------------" + agentJsonObject.toString());
        JSONObject resultJson = new JSONObject();
        if (!this.tokenUtils.checkAgentToken(agentJsonObject)) {
            logger.error("Token is error");
            resultJson.set("result", "Token is error");
            return resultJson;
        }
        if (!"1".equals(agentJsonObject.get("fromAgent"))) {
            return resultJson;
        }
        String appId = agentJsonObject.getStr("appId");
        String state = agentJsonObject.getStr("state");
        try {
            AppInfo appInfo;
            if (!StringUtils.isEmpty((CharSequence)appId) && null != (appInfo = this.appInfoService.selectById(appId))) {
                String key = appId + "_CallBackMsg";
                if (!StringUtils.isEmpty((CharSequence)state) && !state.startsWith(MONITOR_ERROR_PREFIX)) {
                    this.messageErrorUtils.setErrorMsgHandler(key, DateUtil.getCurrentDateTime() + " " + state);
                }
                if (!this.checkSendMsg(key, this.mailConfig.getAppDownWarnMail())) {
                    return resultJson;
                }
                Runnable runnable = () -> {
                    try {
                        String title = "\u8fdb\u7a0b\u81ea\u52a8\u6062\u590d\u6307\u4ee4\u5df2\u6267\u884c\u5b8c\u6210\uff1a" + appInfo.getHostname() + HostUtil.addRemark(appInfo.getHostname()) + "\uff0c\u8fdb\u7a0b\u540d\u79f0\uff1a" + appInfo.getAppName();
                        String commContent = "\u8fdb\u7a0b\u81ea\u52a8\u6062\u590d\u6307\u4ee4\u5df2\u6267\u884c\u5b8c\u6210\uff1a" + appInfo.getHostname() + HostUtil.addRemark(appInfo.getHostname()) + "\uff0c\u8fdb\u7a0b\u540d\u79f0\uff1a" + appInfo.getAppName() + "\uff0c\u6267\u884c\u7ed3\u679c\uff1a" + state;
                        if (!StringUtils.isEmpty((CharSequence)state) && state.startsWith(MONITOR_ERROR_PREFIX)) {
                            this.messageErrorUtils.setErrorMsgHandler(appId, DateUtil.getCurrentDateTime() + " " + state);
                        }
                        WarnOtherUtil.sendUtil(title, commContent, null, key, true, "INFO", "");
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                };
                ThreadPoolUtil.executor.execute(runnable);
            }
        }
        catch (Exception e) {
            logger.error("agent\u4e0a\u62a5\u6210\u529f\u6267\u884c\u8fdb\u7a0b\u7684\u81ea\u52a8\u6062\u590d\u6307\u4ee4\u606f\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("agent\u4e0a\u62a5\u6210\u529f\u6267\u884c\u8fdb\u7a0b\u7684\u81ea\u52a8\u6062\u590d\u6307\u4ee4\u606f\u9519\u8bef", e.toString(), "2");
        }
        resultJson.set("result", "success");
        return resultJson;
    }

    @ResponseBody
    @RequestMapping(value={"/callBackMsgDocker"})
    public JSONObject callBackMsgDocker(@RequestBody String paramBean, HttpServletRequest request) {
        JSONObject agentJsonObject = (JSONObject)JSONUtil.parse((Object)paramBean);
        logger.debug("agent\u76d1\u63a7docker\u51fa\u73b0\u9519\u8bef-------------" + agentJsonObject.toString());
        JSONObject resultJson = new JSONObject();
        if (!this.tokenUtils.checkAgentToken(agentJsonObject)) {
            logger.error("Token is error");
            resultJson.set("result", "Token is error");
            return resultJson;
        }
        if (!"1".equals(agentJsonObject.get("fromAgent"))) {
            return resultJson;
        }
        String hostName = agentJsonObject.getStr("appId");
        String state = agentJsonObject.getStr("state");
        try {
            if (!StringUtils.isEmpty((CharSequence)hostName)) {
                this.messageErrorUtils.setErrorMsgHandler(hostName + "_DOCKER_MSG", DateUtil.getCurrentDateTime() + " " + state);
            }
        }
        catch (Exception e) {
            logger.error("agent\u4e0a\u62a5docker\u76d1\u63a7\u6267\u884c\u72b6\u6001\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("agent\u4e0a\u62a5docker\u76d1\u63a7\u6267\u884c\u72b6\u6001\u9519\u8bef", e.toString(), "2");
        }
        resultJson.set("result", "success");
        return resultJson;
    }

    @ResponseBody
    @RequestMapping(value={"/callBackMsgPort"})
    public JSONObject callBackMsgPort(@RequestBody String paramBean, HttpServletRequest request) {
        JSONObject agentJsonObject = (JSONObject)JSONUtil.parse((Object)paramBean);
        logger.debug("agent\u76d1\u63a7\u7aef\u53e3\u9519\u8bef-------------" + agentJsonObject.toString());
        JSONObject resultJson = new JSONObject();
        if (!this.tokenUtils.checkAgentToken(agentJsonObject)) {
            logger.error("Token is error");
            resultJson.set("result", "Token is error");
            return resultJson;
        }
        if (!"1".equals(agentJsonObject.get("fromAgent"))) {
            return resultJson;
        }
        String portId = agentJsonObject.getStr("appId");
        String state = agentJsonObject.getStr("state");
        try {
            if (!StringUtils.isEmpty((CharSequence)portId)) {
                this.messageErrorUtils.setErrorMsgHandler(portId, DateUtil.getCurrentDateTime() + " " + state);
            }
        }
        catch (Exception e) {
            logger.error("agent\u4e0a\u62a5\u7aef\u53e3\u76d1\u63a7\u6267\u884c\u72b6\u6001\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("agent\u4e0a\u62a5\u7aef\u53e3\u76d1\u63a7\u6267\u884c\u72b6\u6001\u9519\u8bef", e.toString(), "2");
        }
        resultJson.set("result", "success");
        return resultJson;
    }

    private boolean checkSendMsg(String key, String resourceWarnMail) {
        if ("false".equals(this.mailConfig.getAllWarnMail()) || "false".equals(this.mailConfig.getAutoCallBackWarnMail())) {
            return false;
        }
        if ("false".equals(resourceWarnMail)) {
            return false;
        }
        return !WarnPools.checkWarnCacheTimes(key);
    }
}

