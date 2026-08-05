/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.agentController;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.wgcloud.entity.CustomInfo;
import com.wgcloud.entity.CustomState;
import com.wgcloud.service.CustomInfoService;
import com.wgcloud.service.CustomStateService;
import com.wgcloud.service.LogInfoService;
import com.wgcloud.service.SystemInfoService;
import com.wgcloud.util.FormatUtil;
import com.wgcloud.util.MessageErrorUtils;
import com.wgcloud.util.ThreadPoolUtil;
import com.wgcloud.util.TokenUtils;
import com.wgcloud.util.msg.WarnMailUtil;
import com.wgcloud.util.staticvar.BatchData;
import java.util.Date;
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
@RequestMapping(value={"/agentCustomGo"})
public class AgentCustomGoController {
    private static final Logger logger = LoggerFactory.getLogger(AgentCustomGoController.class);
    @Resource
    private LogInfoService logInfoService;
    @Resource
    private SystemInfoService systemInfoService;
    @Autowired
    private CustomStateService customStateService;
    @Autowired
    private CustomInfoService customInfoService;
    @Autowired
    private MessageErrorUtils messageErrorUtils;
    @Autowired
    private TokenUtils tokenUtils;

    @ResponseBody
    @RequestMapping(value={"/minTask"})
    public JSONObject minTask(@RequestBody String paramBean) {
        JSONObject agentJsonObject = (JSONObject)JSONUtil.parse((Object)paramBean);
        logger.debug("agent\u4e0a\u62a5\u81ea\u5b9a\u4e49\u76d1\u63a7\u9879\u6570\u636e-------------" + agentJsonObject.toString());
        JSONObject resultJson = new JSONObject();
        if (!this.tokenUtils.checkAgentToken(agentJsonObject)) {
            logger.error("Token is error");
            resultJson.set("result", "Token is error");
            return resultJson;
        }
        Date nowtime = new Date();
        try {
            JSONObject customInfosJson = agentJsonObject.getJSONObject("customInfos");
            if (customInfosJson == null) {
                logger.error("customInfoList is null");
                resultJson.set("result", "error: customInfoList is null");
                return resultJson;
            }
            for (String id : customInfosJson.keySet()) {
                JSONObject jsonObject = customInfosJson.getJSONObject(id);
                String customValue = jsonObject.getStr("customValue");
                String errorMsg = jsonObject.getStr("errorMsg");
                if (!StringUtils.isEmpty((CharSequence)errorMsg)) {
                    this.messageErrorUtils.setErrorMsgHandler(id, errorMsg);
                }
                CustomState state = new CustomState();
                state.setCreateTime(nowtime);
                state.setCustomInfoId(id);
                state.setCustomValue(customValue);
                if (!StringUtils.isEmpty((CharSequence)customValue)) {
                    BatchData.CUSTOM_STATE_LIST.add(state);
                }
                CustomInfo info = new CustomInfo();
                info.setId(id);
                info.setCreateTime(nowtime);
                info.setCustomValue(customValue);
                info.setState("1");
                BatchData.CUSTOM_INFO_LIST.add(info);
                if (StringUtils.isEmpty((CharSequence)customValue)) continue;
                Runnable runnable = () -> {
                    try {
                        CustomInfo customInfo = this.customInfoService.selectById(info.getId());
                        if (customInfo != null && !StringUtils.isEmpty((CharSequence)customInfo.getResultExp())) {
                            logger.info("\u81ea\u5b9a\u4e49\u76d1\u63a7\u9879---------" + customInfo.getCustomName());
                            Boolean result = FormatUtil.validateExpression(customInfo.getResultExp(), FormatUtil.getExpressionEnv(customValue));
                            if (result.booleanValue()) {
                                info.setState("2");
                                customInfo.setCustomValue(customValue);
                                WarnMailUtil.sendCustomInfoDown(customInfo, true);
                            }
                        }
                    }
                    catch (Exception e) {
                        logger.error("\u81ea\u5b9a\u4e49\u76d1\u63a7\u544a\u8b66\u8868\u8fbe\u5f0f\u5904\u7406\u9519\u8bef", (Throwable)e);
                    }
                };
                ThreadPoolUtil.executor.execute(runnable);
            }
            resultJson.set("result", "success");
        }
        catch (Exception e) {
            logger.error("\u89e3\u6790\u81ea\u5b9a\u4e49\u76d1\u63a7\u9879\u4e0a\u62a5\u6570\u636e\u9519\u8bef", (Throwable)e);
            resultJson.set("result", (Object)("error\uff1a" + e.toString()));
        }
        return resultJson;
    }
}

