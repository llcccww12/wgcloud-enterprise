/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.util.redis;

import com.wgcloud.common.ApplicationContextHelper;
import com.wgcloud.config.CommonConfig;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisDataUtil {
    private static final Logger logger = LoggerFactory.getLogger(RedisDataUtil.class);
    private static CommonConfig commonConfig = ApplicationContextHelper.getBean(CommonConfig.class);
    private static JedisPool jedisPool = null;

    private static void connectRedis() {
        try {
            if (null == jedisPool) {
                String redisUrl = commonConfig.getRedisUrl();
                String host = redisUrl.split(":")[0];
                String hostPort = redisUrl.split(":")[1];
                JedisPoolConfig poolConfig = new JedisPoolConfig();
                poolConfig.setMaxTotal(100);
                poolConfig.setMaxIdle(10);
                poolConfig.setMinIdle(5);
                poolConfig.setMaxWaitMillis(2000L);
                jedisPool = new JedisPool((GenericObjectPoolConfig)poolConfig, host, Integer.valueOf(hostPort).intValue(), 10000);
                if (redisUrl.split(":").length == 3) {
                    jedisPool = new JedisPool((GenericObjectPoolConfig)poolConfig, host, Integer.valueOf(hostPort).intValue(), 10000, redisUrl.split(":")[2]);
                }
                if (redisUrl.split(":").length == 4) {
                    jedisPool = new JedisPool((GenericObjectPoolConfig)poolConfig, host, Integer.valueOf(hostPort).intValue(), 10000, redisUrl.split(":")[2], redisUrl.split(":")[3]);
                }
            }
        }
        catch (Exception e) {
            logger.error("\u8fde\u63a5redis\u9519\u8bef", (Throwable)e);
        }
    }

    private static Jedis getRedis() {
        if (null == jedisPool) {
            RedisDataUtil.connectRedis();
        }
        return jedisPool.getResource();
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static Set<String> selectKeys(String keyPrefix) {
        try (Jedis jedis = RedisDataUtil.getRedis();){
            Set set2;
            Set set = set2 = jedis.keys(keyPrefix);
            return set;
        }
        catch (Exception e) {
            logger.error("selectKeys\u9519\u8bef", (Throwable)e);
            return null;
        }
    }

    public static void setValue(String key, String data, int expireSeconds) {
        try (Jedis jedis = RedisDataUtil.getRedis();){
            jedis.set(key, data);
            jedis.expire(key, (long)expireSeconds);
        }
        catch (Exception e) {
            logger.error("redis\u4fdd\u5b58\u6570\u636e\u9519\u8bef", (Throwable)e);
        }
    }

    public static void delValue(String key) {
        try (Jedis jedis = RedisDataUtil.getRedis();){
            jedis.del(key);
        }
        catch (Exception e) {
            logger.error("redis\u5220\u9664\u6570\u636e\u9519\u8bef", (Throwable)e);
        }
    }

    public static void setValue(String key, String data) {
        try (Jedis jedis = RedisDataUtil.getRedis();){
            jedis.set(key, data);
            jedis.expire(key, 86400L);
        }
        catch (Exception e) {
            logger.error("redis\u4fdd\u5b58\u6570\u636e\u9519\u8bef", (Throwable)e);
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static String getValue(String key) {
        try (Jedis jedis = RedisDataUtil.getRedis();){
            String str;
            String string = str = jedis.get(key);
            return string;
        }
        catch (Exception e) {
            logger.error("redis\u83b7\u53d6\u6570\u636e\u9519\u8bef", (Throwable)e);
            return "";
        }
    }

    public static void setListValue(String key, String data) {
        try (Jedis jedis = RedisDataUtil.getRedis();){
            jedis.rpush(key, new String[]{data});
            jedis.expire(key, 86400L);
        }
        catch (Exception e) {
            logger.error("redis\u5b58\u8d2elist\u6570\u636e\u9519\u8bef", (Throwable)e);
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static List<String> getListValue(String key) {
        List<String> resultList = new ArrayList<String>();
        try (Jedis jedis = RedisDataUtil.getRedis();){
            List<String> list = resultList = jedis.lrange(key, 0L, -1L);
            return list;
        }
        catch (Exception e) {
            logger.error("redis\u5b58\u8d2elist\u6570\u636e\u9519\u8bef", (Throwable)e);
            return resultList;
        }
    }
}

