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

public class ScanIPUtil {
    private static final Logger logger = LoggerFactory.getLogger(ScanIPUtil.class);
    private static CommonConfig commonConfig = ApplicationContextHelper.getBean(CommonConfig.class);
    public static Map<String, Object> SCAN_IP_DATA_MAP = Collections.synchronizedMap(new HashMap());
    public static Map<String, String> SCAN_NAME_DATA_MAP = Collections.synchronizedMap(new HashMap());
    private static final String SCAN_IP_PREFIX = "SCAN_IP_DATA_";
    private static final String SCAN_NAME_PREFIX = "SCAN_NAME_DATA_";

    public static void setScanIPHandler(String key, JSONObject jsonObject) {
        if (StringUtils.isEmpty((CharSequence)commonConfig.getRedisUrl())) {
            SCAN_IP_DATA_MAP.put(key, jsonObject);
        } else {
            RedisDataUtil.setValue(SCAN_IP_PREFIX + key, jsonObject.toString());
        }
    }

    public static List<JSONObject> viewScanIPHandler() {
        ArrayList<JSONObject> scanDataList = new ArrayList<JSONObject>();
        try {
            if (!StringUtils.isEmpty((CharSequence)commonConfig.getRedisUrl())) {
                Set<String> set = RedisDataUtil.selectKeys("SCAN_IP_DATA_*");
                if (null != set) {
                    for (String element : set) {
                        String value = RedisDataUtil.getValue(element);
                        scanDataList.add(JSONUtil.parseObj((String)value));
                    }
                }
                return scanDataList;
            }
            for (String key : SCAN_IP_DATA_MAP.keySet()) {
                scanDataList.add(JSONUtil.parseObj((Object)SCAN_IP_DATA_MAP.get(key)));
            }
        }
        catch (Exception e) {
            logger.error("\u67e5\u770b\u81ea\u52a8\u53d1\u73b0\u8bbe\u5907IP\u4fe1\u606f\u9519\u8bef", (Throwable)e);
        }
        return scanDataList;
    }

    public static void setScanNameHandler(String key, String value) {
        if (StringUtils.isEmpty((CharSequence)commonConfig.getRedisUrl())) {
            SCAN_NAME_DATA_MAP.put(key, value);
        } else {
            RedisDataUtil.setValue(SCAN_NAME_PREFIX + key, value);
        }
    }

    public static List<String> viewScanNameHandler() {
        ArrayList<String> scanDataList = new ArrayList<String>();
        try {
            if (!StringUtils.isEmpty((CharSequence)commonConfig.getRedisUrl())) {
                Set<String> set = RedisDataUtil.selectKeys("SCAN_NAME_DATA_*");
                if (null != set) {
                    for (String element : set) {
                        String value = RedisDataUtil.getValue(element);
                        scanDataList.add(value);
                    }
                }
                return scanDataList;
            }
            for (String key : SCAN_NAME_DATA_MAP.keySet()) {
                scanDataList.add(SCAN_NAME_DATA_MAP.get(key));
            }
        }
        catch (Exception e) {
            logger.error("\u67e5\u770b\u81ea\u52a8\u53d1\u73b0\u8bbe\u5907\u7f51\u7edc\u540d\u79f0\u4fe1\u606f\u9519\u8bef", (Throwable)e);
        }
        return scanDataList;
    }
}

