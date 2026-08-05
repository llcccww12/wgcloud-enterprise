/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.util;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import com.wgcloud.config.CommonConfig;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class IdGeneratorSnowflake {
    private static final Logger logger = LoggerFactory.getLogger(IdGeneratorSnowflake.class);
    @Autowired
    CommonConfig commonConfig;
    private long workerId = 0L;
    private long datacenterId = 1L;
    private Snowflake snowflake = null;

    @PostConstruct
    public void init() {
        try {
            if (!"master".equals(this.commonConfig.getNodeType())) {
                String slaveId = this.commonConfig.getNodeType().replace("slave", "");
                this.workerId = Long.valueOf(slaveId);
            }
            this.snowflake = IdUtil.createSnowflake((long)this.workerId, (long)this.datacenterId);
            logger.info("\u5f53\u524d\u673a\u5668\u7684workerId: {}", (Object)this.workerId);
        }
        catch (Exception e) {
            this.snowflake = IdUtil.createSnowflake((long)this.workerId, (long)this.datacenterId);
            logger.error("\u5f53\u524d\u673a\u5668\u7684workerId\u83b7\u53d6\u5931\u8d25", (Throwable)e);
        }
    }

    public synchronized long snowflakeId() {
        return this.snowflake.nextId();
    }
}

