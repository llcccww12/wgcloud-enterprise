/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.util;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.wgcloud.common.ApplicationContextHelper;
import com.wgcloud.config.CommonConfig;
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

public class TomcatUtil {
    private static final Logger logger = LoggerFactory.getLogger(TomcatUtil.class);
    private static CommonConfig commonConfig = ApplicationContextHelper.getBean(CommonConfig.class);
    public static Map<String, Object> TOMCAT_DATA_MAP = Collections.synchronizedMap(new HashMap());
    private static final String TOMCAT_PREFIX = "TOMCAT_DATA_";

    public static void setTomcatHandler(String key, JSONObject jsonObject) {
        if (StringUtils.isEmpty((CharSequence)commonConfig.getRedisUrl())) {
            TOMCAT_DATA_MAP.put(key, jsonObject);
        } else {
            RedisDataUtil.setValue(TOMCAT_PREFIX + key, jsonObject.toString());
        }
    }

    public static List<JSONObject> viewTomcatHandler() {
        ArrayList<JSONObject> tomcatDataList = new ArrayList<JSONObject>();
        try {
            if (!StringUtils.isEmpty((CharSequence)commonConfig.getRedisUrl())) {
                Set<String> set = RedisDataUtil.selectKeys("TOMCAT_DATA_*");
                if (null != set) {
                    for (String element : set) {
                        String value = RedisDataUtil.getValue(element);
                        tomcatDataList.add(JSONUtil.parseObj((String)value));
                    }
                }
                return tomcatDataList;
            }
            for (String key : TOMCAT_DATA_MAP.keySet()) {
                tomcatDataList.add(JSONUtil.parseObj((Object)TOMCAT_DATA_MAP.get(key)));
            }
        }
        catch (Exception e) {
            logger.error("\u67e5\u770btomcat\u76d1\u63a7\u4fe1\u606f\u9519\u8bef", (Throwable)e);
        }
        return tomcatDataList;
    }
}

