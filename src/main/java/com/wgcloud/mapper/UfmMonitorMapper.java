/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.mapper;

import com.wgcloud.entity.UfmMonitor;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public interface UfmMonitorMapper {
    public List<UfmMonitor> selectAllByParams(Map<String, Object> var1) throws Exception;

    public List<UfmMonitor> selectByParams(Map<String, Object> var1) throws Exception;

    public UfmMonitor selectById(String var1) throws Exception;

    public int countByParams(Map<String, Object> var1);

    public void save(UfmMonitor var1) throws Exception;

    public int deleteByGuid(String var1) throws Exception;

    public int deleteByDate(Map<String, Object> var1) throws Exception;

    public int deleteById(String[] var1) throws Exception;
}
