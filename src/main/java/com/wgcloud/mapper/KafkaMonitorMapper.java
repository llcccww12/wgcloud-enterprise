/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.mapper;

import com.wgcloud.entity.KafkaMonitor;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public interface KafkaMonitorMapper {
    public List<KafkaMonitor> selectAllByParams(Map<String, Object> var1) throws Exception;

    public List<KafkaMonitor> selectByParams(Map<String, Object> var1) throws Exception;

    public KafkaMonitor selectById(String var1) throws Exception;

    public int countByParams(Map<String, Object> var1);

    public void save(KafkaMonitor var1) throws Exception;

    public void insertList(List<KafkaMonitor> var1) throws Exception;

    public int deleteByKafkaName(String var1) throws Exception;

    public int downByKafkaName(String var1) throws Exception;

    public int deleteByDate(Map<String, Object> var1) throws Exception;

    public int deleteById(String[] var1) throws Exception;
}

