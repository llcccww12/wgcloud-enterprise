/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.util;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.wgcloud.common.ApplicationContextHelper;
import com.wgcloud.config.CommonConfig;
import com.wgcloud.service.AccountInfoService;
import com.wgcloud.service.SystemInfoService;
import com.wgcloud.util.redis.RedisDataUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ActivemqUtil {
    private static final Logger logger = LoggerFactory.getLogger(ActivemqUtil.class);
    private static CommonConfig commonConfig = ApplicationContextHelper.getBean(CommonConfig.class);
    private static AccountInfoService accountInfoService = ApplicationContextHelper.getBean(AccountInfoService.class);
    private static SystemInfoService systemInfoService = ApplicationContextHelper.getBean(SystemInfoService.class);
    public static Map<String, Object> ACTIVEMQ_DATA_MAP = Collections.synchronizedMap(new HashMap());
    private static final String ACTIVEMQ_PREFIX = "ACTIVEMQ_DATA_";

    public static void setActivemqHandler(String key, JSONObject jsonObject) {
        if (StringUtils.isEmpty((CharSequence)commonConfig.getRedisUrl())) {
            ACTIVEMQ_DATA_MAP.put(key, jsonObject);
        } else {
            RedisDataUtil.setValue(ACTIVEMQ_PREFIX + key, jsonObject.toString());
        }
    }

    public static List<JSONObject> viewActivemqHandler() {
        ArrayList<JSONObject> activemqDataList = new ArrayList<JSONObject>();
        try {
            if (!StringUtils.isEmpty((CharSequence)commonConfig.getRedisUrl())) {
                Set<String> set = RedisDataUtil.selectKeys("ACTIVEMQ_DATA_*");
                if (null != set) {
                    for (String element : set) {
                        String value = RedisDataUtil.getValue(element);
                        activemqDataList.add(JSONUtil.parseObj((String)value));
                    }
                }
                return activemqDataList;
            }
            for (String key : ACTIVEMQ_DATA_MAP.keySet()) {
                activemqDataList.add(JSONUtil.parseObj((Object)ACTIVEMQ_DATA_MAP.get(key)));
            }
        }
        catch (Exception e) {
            logger.error("\u67e5\u770bActivemq\u76d1\u63a7\u4fe1\u606f\u9519\u8bef", (Throwable)e);
        }
        return activemqDataList;
    }
}

