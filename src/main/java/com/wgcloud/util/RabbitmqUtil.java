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

public class RabbitmqUtil {
    private static final Logger logger = LoggerFactory.getLogger(RabbitmqUtil.class);
    private static CommonConfig commonConfig = ApplicationContextHelper.getBean(CommonConfig.class);
    private static AccountInfoService accountInfoService = ApplicationContextHelper.getBean(AccountInfoService.class);
    private static SystemInfoService systemInfoService = ApplicationContextHelper.getBean(SystemInfoService.class);
    public static Map<String, Object> RABBITMQ_DATA_MAP = Collections.synchronizedMap(new HashMap());
    private static final String RABBITMQ_PREFIX = "RABBITMQ_DATA_";

    public static void setRabbitmqHandler(String key, JSONObject jsonObject) {
        if (StringUtils.isEmpty((CharSequence)commonConfig.getRedisUrl())) {
            RABBITMQ_DATA_MAP.put(key, jsonObject);
        } else {
            RedisDataUtil.setValue(RABBITMQ_PREFIX + key, jsonObject.toString());
        }
    }

    public static List<JSONObject> viewRabbitmqHandler() {
        ArrayList<JSONObject> rabbitmqDataList = new ArrayList<JSONObject>();
        try {
            if (!StringUtils.isEmpty((CharSequence)commonConfig.getRedisUrl())) {
                Set<String> set = RedisDataUtil.selectKeys("RABBITMQ_DATA_*");
                if (null != set) {
                    for (String element : set) {
                        String value = RedisDataUtil.getValue(element);
                        rabbitmqDataList.add(JSONUtil.parseObj((String)value));
                    }
                }
                return rabbitmqDataList;
            }
            for (String key : RABBITMQ_DATA_MAP.keySet()) {
                rabbitmqDataList.add(JSONUtil.parseObj((Object)RABBITMQ_DATA_MAP.get(key)));
            }
        }
        catch (Exception e) {
            logger.error("\u67e5\u770bRabbitmq\u76d1\u63a7\u4fe1\u606f\u9519\u8bef", (Throwable)e);
        }
        return rabbitmqDataList;
    }
}

