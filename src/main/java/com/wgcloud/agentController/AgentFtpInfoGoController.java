/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.agentController;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.wgcloud.config.CommonConfig;
import com.wgcloud.entity.FtpInfo;
import com.wgcloud.service.FtpInfoService;
import com.wgcloud.service.LogInfoService;
import com.wgcloud.util.MessageErrorUtils;
import com.wgcloud.util.ResDataUtils;
import com.wgcloud.util.ThreadPoolUtil;
import com.wgcloud.util.TokenUtils;
import com.wgcloud.util.msg.WarnOtherUtil;
import com.wgcloud.util.msg.WarnPools;
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
@RequestMapping(value={"/agentFtpInfoGo"})
public class AgentFtpInfoGoController {
    private static final Logger logger = LoggerFactory.getLogger(AgentFtpInfoGoController.class);
    @Resource
    private LogInfoService logInfoService;
    @Resource
    private FtpInfoService ftpInfoService;
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
        logger.debug("server-backup\u76d1\u63a7ftp/sftp\u4e0a\u62a5\u6570\u636e-------------" + agentJsonObject.toString());
        Date nowtime = new Date();
        try {
            JSONArray ftpInfosJsonArr = agentJsonObject.getJSONArray("ftpInfosUpdate");
            if (ftpInfosJsonArr == null) {
                logger.error("ftpInfosUpdate is null");
                return ResDataUtils.resetErrorJson("ftpInfosUpdate is null");
            }
            List ftpInfoList = JSONUtil.toList((JSONArray)ftpInfosJsonArr, FtpInfo.class);
            for (FtpInfo f : (java.util.List<FtpInfo>)ftpInfoList) {
                Runnable runnable;
                FtpInfo ftpInfoSaved = this.ftpInfoService.selectById(f.getId());
                f.setCreateTime(nowtime);
                if ("2".equals(f.getState())) {
                    f.setCreateTime(null);
                }
                try {
                    this.ftpInfoService.updateById(f);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                this.messageErrorUtils.setErrorMsgHandler(f.getId(), f.getTestErrorMsg());
                ftpInfoSaved.setState(f.getState());
                if ("2".equals(f.getState())) {
                    runnable = () -> WarnOtherUtil.sendFtpInfo(ftpInfoSaved, true);
                    ThreadPoolUtil.executor.execute(runnable);
                    continue;
                }
                if (StringUtils.isEmpty((CharSequence)WarnPools.getWarnMark(ftpInfoSaved.getId()))) continue;
                runnable = () -> WarnOtherUtil.sendFtpInfo(ftpInfoSaved, false);
                ThreadPoolUtil.executor.execute(runnable);
            }
        }
        catch (Exception e) {
            logger.error("\u89e3\u6790server-backup\u76d1\u63a7ftp/sftp\u4e0a\u62a5\u6570\u636e\u9519\u8bef", (Throwable)e);
            return ResDataUtils.resetErrorJson(e.toString());
        }
        return ResDataUtils.resetSuccessJson(null);
    }
}

