/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.mapper;

import com.wgcloud.entity.HeathMonitor;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public interface HeathMonitorMapper {
    public List<HeathMonitor> selectAllByParams(Map<String, Object> var1) throws Exception;

    public List<HeathMonitor> selectByParams(Map<String, Object> var1) throws Exception;

    public List<HeathMonitor> selectByParamsNoContent(Map<String, Object> var1) throws Exception;

    public HeathMonitor selectById(String var1) throws Exception;

    public void save(HeathMonitor var1) throws Exception;

    public void insertList(List<HeathMonitor> var1) throws Exception;

    public int deleteById(String[] var1) throws Exception;

    public int countByParams(Map<String, Object> var1) throws Exception;

    public void updateList(List<HeathMonitor> var1) throws Exception;

    public void updateById(HeathMonitor var1) throws Exception;

    public int updateToTargetAccount(Map<String, Object> var1) throws Exception;

    public int updateActive(Map<String, Object> var1) throws Exception;
}

