/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.util.msg;

import com.wgcloud.common.ApplicationContextHelper;
import com.wgcloud.config.CommonConfig;
import com.wgcloud.config.MailConfig;
import com.wgcloud.util.redis.RedisDataUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import net.jodah.expiringmap.ExpirationPolicy;
import net.jodah.expiringmap.ExpiringMap;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WarnPools {
    private static final Logger logger = LoggerFactory.getLogger(WarnPools.class);
    private static CommonConfig commonConfig = ApplicationContextHelper.getBean(CommonConfig.class);
    private static MailConfig mailConfig = ApplicationContextHelper.getBean(MailConfig.class);
    public static ExpiringMap<String, String> MEM_WARN_MAP = null;
    public static List<Integer> WARN_COUNT_LIST = Collections.synchronizedList(new ArrayList());
    public static ExpiringMap<String, Integer> HEATH_WARN_COUNT_MAP = null;
    public static ExpiringMap<String, Integer> PING_WARN_COUNT_MAP = null;
    public static ExpiringMap<String, Integer> SNMP_WARN_COUNT_MAP = null;
    public static ExpiringMap<String, Integer> HOST_WARN_COUNT_MAP = null;
    public static Integer NGINX_CHECK_DAY_COUNT = 0;

    public static void clearOldData() {
        logger.info("\u6e05\u7a7a\u544a\u8b66\u7f13\u5b58map");
        if (null != MEM_WARN_MAP) {
            MEM_WARN_MAP.clear();
        }
        logger.info("\u6e05\u7a7a\u544a\u8b66\u4fe1\u606f\u8ba1\u6570\u5668");
        WARN_COUNT_LIST.clear();
        logger.info("\u6e05\u7a7a\u670d\u52a1\u63a5\u53e3\uff0cPING\uff0c\u4e3b\u673a\u5185\u5b58\u4f7f\u7528\u7387\uff0c\u4e3b\u673aCPU\u4f7f\u7528\u7387\uff0c\u544a\u8b66\u5931\u8d25\u6b21\u6570\u7684\u7f13\u5b58\u8bb0\u5f55");
        if (null != HEATH_WARN_COUNT_MAP) {
            HEATH_WARN_COUNT_MAP.clear();
        }
        if (null != PING_WARN_COUNT_MAP) {
            PING_WARN_COUNT_MAP.clear();
        }
        if (null != SNMP_WARN_COUNT_MAP) {
            SNMP_WARN_COUNT_MAP.clear();
        }
        if (null != HOST_WARN_COUNT_MAP) {
            HOST_WARN_COUNT_MAP.clear();
        }
        logger.info("\u91cd\u7f6enginx\u65e5\u5fd7\u68c0\u6d4b\u6b21\u6570\u4e3a0");
        NGINX_CHECK_DAY_COUNT = 0;
    }

    public static boolean checkWarnCacheTimes(String key) {
        if (StringUtils.isEmpty((CharSequence)key)) {
            return false;
        }
        Integer warnCacheTimes = commonConfig.getWarnCacheTimes();
        try {
            if (null == warnCacheTimes) {
                warnCacheTimes = 7200;
            }
            if (null == MEM_WARN_MAP) {
                MEM_WARN_MAP = ExpiringMap.builder().expiration((long)warnCacheTimes.intValue(), TimeUnit.SECONDS).expirationPolicy(ExpirationPolicy.CREATED).build();
            }
            if (!StringUtils.isEmpty((CharSequence)WarnPools.getWarnMark(key))) {
                return true;
            }
        }
        catch (Exception e) {
            logger.error("\u5224\u65ad\u544a\u8b66\u7f13\u5b58\u65f6\u95f4\u8fc7\u671f\u9519\u8bef", (Throwable)e);
        }
        return false;
    }

    public static void initWarnCountMap() {
        try {
            if (null == HEATH_WARN_COUNT_MAP) {
                long heathOutTimes = commonConfig.getHeathTimes() * mailConfig.getHeathWarnCount();
                HEATH_WARN_COUNT_MAP = ExpiringMap.builder().expiration(heathOutTimes += 10L, TimeUnit.SECONDS).expirationPolicy(ExpirationPolicy.CREATED).build();
            }
            if (null == PING_WARN_COUNT_MAP) {
                long pingOutTimes = commonConfig.getDceTimes() * mailConfig.getDceWarnCount();
                PING_WARN_COUNT_MAP = ExpiringMap.builder().expiration(pingOutTimes += 10L, TimeUnit.SECONDS).expirationPolicy(ExpirationPolicy.CREATED).build();
            }
            if (null == SNMP_WARN_COUNT_MAP) {
                long snmpOutTimes = commonConfig.getSnmpTimes() * mailConfig.getSnmpWarnCount();
                SNMP_WARN_COUNT_MAP = ExpiringMap.builder().expiration(snmpOutTimes += 10L, TimeUnit.SECONDS).expirationPolicy(ExpirationPolicy.CREATED).build();
            }
            if (null == HOST_WARN_COUNT_MAP) {
                long cpuOutTimes = 1200L;
                HOST_WARN_COUNT_MAP = ExpiringMap.builder().expiration(cpuOutTimes, TimeUnit.SECONDS).expirationPolicy(ExpirationPolicy.CREATED).build();
            }
        }
        catch (Exception e) {
            logger.error("\u5224\u65ad\u544a\u8b66\u7f13\u5b58\u65f6\u95f4\u9519\u8bef", (Throwable)e);
        }
    }

    public static void addWarnMark(String key) {
        try {
            if (StringUtils.isEmpty((CharSequence)commonConfig.getRedisUrl())) {
                MEM_WARN_MAP.put((String)(Object)key, "1");
            } else {
                RedisDataUtil.setValue(key + "_WARN", "1", commonConfig.getWarnCacheTimes());
            }
        }
        catch (Exception e) {
            logger.error("\u6dfb\u52a0\u544a\u8b66\u7f13\u5b58\u6807\u8bb0\u9519\u8bef", (Throwable)e);
        }
    }

    public static void removeWarnMark(String key) {
        try {
            if (StringUtils.isEmpty((CharSequence)commonConfig.getRedisUrl())) {
                MEM_WARN_MAP.remove(key);
            } else {
                RedisDataUtil.delValue(key + "_WARN");
            }
        }
        catch (Exception e) {
            logger.error("\u79fb\u9664\u544a\u8b66\u7f13\u5b58\u6807\u8bb0\u9519\u8bef", (Throwable)e);
        }
    }

    public static String getWarnMark(String key) {
        String result = "";
        try {
            result = StringUtils.isEmpty((CharSequence)commonConfig.getRedisUrl()) ? (String)MEM_WARN_MAP.get(key) : RedisDataUtil.getValue(key + "_WARN");
        }
        catch (Exception e) {
            logger.error("\u67e5\u627e\u544a\u8b66\u7f13\u5b58\u6807\u8bb0\u9519\u8bef", (Throwable)e);
        }
        return result;
    }
}

