/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.util.redis;

import com.wgcloud.entity.DbInfo;
import com.wgcloud.service.DbInfoService;
import com.wgcloud.service.LogInfoService;
import com.wgcloud.util.ThreadPoolUtil;
import com.wgcloud.util.msg.WarnOtherUtil;
import com.wgcloud.util.msg.WarnPools;
import java.util.Date;
import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

@Component
public class RedisUtil {
    private static final Logger logger = LoggerFactory.getLogger(RedisUtil.class);
    @Resource
    private LogInfoService logInfoService;
    @Resource
    private DbInfoService dbInfoService;

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public String connectRedis(DbInfo dbInfo) throws Exception {
        Jedis jedis = null;
        try {
            long startTimes = System.currentTimeMillis();
            dbInfo.setDbState("1");
            jedis = new Jedis(dbInfo.getDbUrl(), Integer.valueOf(dbInfo.getUserName()).intValue(), 10000);
            if (!StringUtils.isEmpty((CharSequence)dbInfo.getPasswd())) {
                if (dbInfo.getPasswd().indexOf(":") > -1) {
                    jedis.auth(dbInfo.getPasswd().split(":")[0], dbInfo.getPasswd().split(":")[1]);
                } else {
                    jedis.auth(dbInfo.getPasswd());
                }
            }
            String pong = jedis.ping();
            long endTimes = System.currentTimeMillis();
            String resTimes = endTimes - startTimes + "";
            dbInfo.setResTimes(Integer.valueOf(resTimes));
            logger.info("connectRedis-------" + pong);
            if (!StringUtils.isEmpty((CharSequence)WarnPools.getWarnMark(dbInfo.getId()))) {
                Runnable runnable = () -> WarnOtherUtil.sendDbDown(dbInfo, false);
                ThreadPoolUtil.executor.execute(runnable);
            }
            dbInfo.setCreateTime(new Date());
            dbInfo.setPasswd(null);
            this.dbInfoService.updateById(dbInfo);
            String string = pong;
            return string;
        }
        catch (Exception e) {
            dbInfo.setDbState("2");
            logger.error("\u8fde\u63a5redis\u9519\u8bef", (Throwable)e);
            dbInfo.setTestErrorMsg(e.toString());
            this.logInfoService.save("\u8fde\u63a5redis\u9519\u8bef\uff1a" + dbInfo.getAliasName(), "\u6570\u636e\u5e93\u540d\u79f0\uff1a" + dbInfo.getAliasName() + "\uff0c" + e.toString(), "2");
            dbInfo.setPasswd(null);
            this.dbInfoService.updateById(dbInfo);
            Runnable runnable = () -> WarnOtherUtil.sendDbDown(dbInfo, true);
            ThreadPoolUtil.executor.execute(runnable);
        }
        finally {
            if (null != jedis) {
                jedis.close();
            }
        }
        return null;
    }
}

