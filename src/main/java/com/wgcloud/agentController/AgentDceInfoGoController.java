/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.agentController;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.wgcloud.config.CommonConfig;
import com.wgcloud.entity.DceInfo;
import com.wgcloud.entity.DceState;
import com.wgcloud.service.DceInfoService;
import com.wgcloud.service.DceStateService;
import com.wgcloud.service.LogInfoService;
import com.wgcloud.util.MessageErrorUtils;
import com.wgcloud.util.ResDataUtils;
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
@RequestMapping(value={"/agentDceInfoGo"})
public class AgentDceInfoGoController {
    private static final Logger logger = LoggerFactory.getLogger(AgentDceInfoGoController.class);
    @Resource
    private LogInfoService logInfoService;
    @Autowired
    private DceInfoService dceInfoService;
    @Autowired
    private DceStateService dceStateService;
    @Autowired
    private TokenUtils tokenUtils;
    @Autowired
    private CommonConfig commonConfig;
    @Autowired
    private MessageErrorUtils messageErrorUtils;

    @ResponseBody
    @RequestMapping(value={"/minTask"})
    public String minTask(@RequestBody String paramBean) {
        JSONObject agentJsonObject = (JSONObject)JSONUtil.parse((Object)paramBean);
        boolean checkResult = this.tokenUtils.checkAgentToken(agentJsonObject);
        if (!checkResult) {
            logger.error("Token is error");
            return ResDataUtils.resetErrorJson("Token is error");
        }
        logger.debug("server-backup\u76d1\u63a7\u6570\u901aPING\u4e0a\u62a5\u6570\u636e-------------" + agentJsonObject.toString());
        Date nowtime = new Date();
        try {
            JSONArray dceInfosJsonArr = agentJsonObject.getJSONArray("dceInfosUpdate");
            if (dceInfosJsonArr == null) {
                logger.error("dceInfosUpdate is null");
                return ResDataUtils.resetErrorJson("dceInfosUpdate is null");
            }
            List dceInfoList = JSONUtil.toList((JSONArray)dceInfosJsonArr, DceInfo.class);
            for (DceInfo dceInfo : (java.util.List<DceInfo>)dceInfoList) {
                DceInfo dceInfoSaved = this.dceInfoService.selectById(dceInfo.getId());
                dceInfo.setCreateTime(nowtime);
                if (dceInfo.getResTimes() < 0) {
                    dceInfo.setCreateTime(null);
                }
                try {
                    this.dceInfoService.updateById(dceInfo);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                if (!StringUtils.isEmpty((CharSequence)dceInfo.getTestErrorMsg())) {
                    this.messageErrorUtils.setErrorMsgHandler(dceInfo.getId(), dceInfo.getTestErrorMsg());
                }
                if (dceInfo.getResTimes() > 0) {
                    DceState dceState = new DceState();
                    dceState.setResTimes(dceInfo.getResTimes());
                    dceState.setDceId(dceInfo.getId());
                    dceState.setCreateTime(nowtime);
                    BatchData.DCE_STATE_LIST.add(dceState);
                }
                Runnable runnable = () -> {
                    dceInfoSaved.setResTimes(dceInfo.getResTimes());
                    if (dceInfo.getResTimes() < 0) {
                        WarnOtherUtil.sendDceInfo(dceInfoSaved, true);
                    } else if (!StringUtils.isEmpty((CharSequence)WarnPools.getWarnMark(dceInfo.getId()))) {
                        WarnOtherUtil.sendDceInfo(dceInfoSaved, false);
                    }
                };
                ThreadPoolUtil.executor.execute(runnable);
            }
        }
        catch (Exception e) {
            logger.error("\u89e3\u6790server-backup\u76d1\u63a7\u6570\u901aPING\u4e0a\u62a5\u6570\u636e\u9519\u8bef", (Throwable)e);
            return ResDataUtils.resetErrorJson(e.toString());
        }
        return ResDataUtils.resetSuccessJson(null);
    }
}

