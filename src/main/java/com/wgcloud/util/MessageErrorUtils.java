/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.util;

import com.wgcloud.config.CommonConfig;
import com.wgcloud.util.redis.RedisDataUtil;
import com.wgcloud.util.staticvar.StaticKeys;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

@Component
public class MessageErrorUtils {
    private static final Logger logger = LoggerFactory.getLogger(MessageErrorUtils.class);
    @Autowired
    private CommonConfig commonConfig;

    public void setErrorMsgHandler(String id, String errorMsg) {
        try {
            if (StringUtils.isEmpty((CharSequence)errorMsg)) {
                errorMsg = "";
            }
            if (StringUtils.isEmpty((CharSequence)this.commonConfig.getRedisUrl())) {
                StaticKeys.MESSAGE_ERROR_MAP.put(id, errorMsg);
            } else {
                RedisDataUtil.setValue("MESSAGE_ERROR_" + id, errorMsg);
            }
        }
        catch (Exception e) {
            logger.error("setErrorMsgHandler\u9519\u8bef", (Throwable)e);
        }
    }

    public String viewErrorMsgHandler(String id) {
        String result = "";
        try {
            result = StringUtils.isEmpty((CharSequence)this.commonConfig.getRedisUrl()) ? StaticKeys.MESSAGE_ERROR_MAP.get(id) : RedisDataUtil.getValue("MESSAGE_ERROR_" + id);
            if (StringUtils.isEmpty((CharSequence)result)) {
                result = "success";
            }
        }
        catch (Exception e) {
            logger.error("viewErrorMsgHandler\u9519\u8bef", (Throwable)e);
        }
        return result;
    }

    public void getCallBackMsgForShell(Model model, String id, String customShell) {
        if (StringUtils.isEmpty((CharSequence)customShell)) {
            model.addAttribute("shellCallBackMsg", "\u672a\u8bbe\u7f6e");
            return;
        }
        if (!StaticKeys.LICENSE_STATE.equals("1") && !StringUtils.isEmpty((CharSequence)customShell)) {
            model.addAttribute("shellCallBackMsg", "\u5f53\u524d\u7248\u672c\u4e0d\u652f\u6301\u81ea\u52a8\u5904\u7406\u6307\u4ee4\uff0c\u9700\u8981\u5347\u7ea7\u5230\u4e13\u4e1a\u7248\u540e\u53ef\u4ee5\u652f\u6301");
            return;
        }
        String result = this.viewErrorMsgHandler(id + "_CallBackMsg");
        if ("success".equals(result)) {
            result = "Ready";
        }
        model.addAttribute("shellCallBackMsg", (Object)result);
    }
}

