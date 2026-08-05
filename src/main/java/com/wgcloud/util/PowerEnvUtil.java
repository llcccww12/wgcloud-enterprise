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

public class PowerEnvUtil {
    private static final Logger logger = LoggerFactory.getLogger(PowerEnvUtil.class);
    private static CommonConfig commonConfig = ApplicationContextHelper.getBean(CommonConfig.class);
    private static AccountInfoService accountInfoService = ApplicationContextHelper.getBean(AccountInfoService.class);
    private static SystemInfoService systemInfoService = ApplicationContextHelper.getBean(SystemInfoService.class);
    public static Map<String, Object> POWER_ENV_DATA_MAP = Collections.synchronizedMap(new HashMap());
    private static final String POWER_ENV_PREFIX = "POWER_ENV_";

    public static void setPowerEnvHandler(String key, JSONObject jsonObject) {
        if (StringUtils.isEmpty((CharSequence)commonConfig.getRedisUrl())) {
            POWER_ENV_DATA_MAP.put(key, jsonObject);
        } else {
            RedisDataUtil.setValue(POWER_ENV_PREFIX + key, jsonObject.toString());
        }
    }

    public static List<JSONObject> viewPowerEnvHandler() throws Exception {
        ArrayList<JSONObject> powerEnvList = new ArrayList<JSONObject>();
        if (!StringUtils.isEmpty((CharSequence)commonConfig.getRedisUrl())) {
            Set<String> set = RedisDataUtil.selectKeys("POWER_ENV_*");
            if (null != set) {
                for (String element : set) {
                    String value = RedisDataUtil.getValue(element);
                    value = value.replace(POWER_ENV_PREFIX, "");
                    powerEnvList.add(JSONUtil.parseObj((String)value));
                }
            }
            return powerEnvList;
        }
        for (String key : POWER_ENV_DATA_MAP.keySet()) {
            powerEnvList.add(JSONUtil.parseObj((Object)POWER_ENV_DATA_MAP.get(key)));
        }
        return powerEnvList;
    }
}

