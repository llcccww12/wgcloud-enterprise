/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.util;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoIterable;
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

@Component
public class MongoDbUtil {
    private static final Logger logger = LoggerFactory.getLogger(MongoDbUtil.class);
    @Resource
    private LogInfoService logInfoService;
    @Resource
    private DbInfoService dbInfoService;

    public void connectMongoDb(DbInfo dbInfo) throws Exception {
        block6: {
            MongoClient mongoClient = null;
            try {
                dbInfo.setDbState("1");
                mongoClient = MongoClients.create((String)dbInfo.getDbUrl());
                logger.info("mongoClient----------" + mongoClient);
                long startTimes = System.currentTimeMillis();
                long endTimes = System.currentTimeMillis();
                if (null != mongoClient) {
                    MongoIterable listDataBaseNames = mongoClient.listDatabaseNames();
                    endTimes = System.currentTimeMillis();
                    MongoCursor mongoCursor = listDataBaseNames.iterator();
                    if (mongoCursor.hasNext()) {
                        String dbName = (String)mongoCursor.next();
                        logger.info("\u8fde\u63a5mongodb\u6210\u529f-------");
                    }
                    mongoClient.close();
                    mongoClient = null;
                    if (!StringUtils.isEmpty((CharSequence)WarnPools.getWarnMark(dbInfo.getId()))) {
                        Runnable runnable = () -> WarnOtherUtil.sendDbDown(dbInfo, false);
                        ThreadPoolUtil.executor.execute(runnable);
                    }
                    dbInfo.setCreateTime(new Date());
                } else {
                    endTimes = System.currentTimeMillis();
                    dbInfo.setDbState("2");
                }
                String resTimes = endTimes - startTimes + "";
                dbInfo.setResTimes(Integer.valueOf(resTimes));
                this.dbInfoService.updateById(dbInfo);
            }
            catch (Exception e) {
                dbInfo.setDbState("2");
                logger.error("\u8fde\u63a5mongodb\u9519\u8bef", (Throwable)e);
                dbInfo.setTestErrorMsg(e.toString());
                this.logInfoService.save("\u8fde\u63a5mongodb\u9519\u8bef\uff1a" + dbInfo.getAliasName(), "\u6570\u636e\u5e93\u540d\u79f0\uff1a" + dbInfo.getAliasName() + "\uff0c" + e.toString(), "2");
                this.dbInfoService.updateById(dbInfo);
                Runnable runnable = () -> WarnOtherUtil.sendDbDown(dbInfo, true);
                ThreadPoolUtil.executor.execute(runnable);
                if (null == mongoClient) break block6;
                mongoClient.close();
                mongoClient = null;
            }
        }
    }
}

