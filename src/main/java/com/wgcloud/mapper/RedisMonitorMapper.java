/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.mapper;

import com.wgcloud.entity.RedisMonitor;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public interface RedisMonitorMapper {
    public List<RedisMonitor> selectAllByParams(Map<String, Object> var1) throws Exception;

    public List<RedisMonitor> selectByParams(Map<String, Object> var1) throws Exception;

    public RedisMonitor selectById(String var1) throws Exception;

    public int countByParams(Map<String, Object> var1);

    public void save(RedisMonitor var1) throws Exception;

    public int deleteByRedisName(String var1) throws Exception;

    public int downByRedisName(String var1) throws Exception;

    public int deleteByDate(Map<String, Object> var1) throws Exception;

    public int deleteById(String[] var1) throws Exception;
}

