/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.mapper;

import com.wgcloud.entity.CpuTemperatures;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public interface CpuTemperaturesMapper {
    public List<CpuTemperatures> selectAllByParams(Map<String, Object> var1) throws Exception;

    public List<CpuTemperatures> selectByParams(Map<String, Object> var1) throws Exception;

    public CpuTemperatures selectById(String var1) throws Exception;

    public void save(CpuTemperatures var1) throws Exception;

    public void insertList(List<CpuTemperatures> var1) throws Exception;

    public int deleteByDate(Map<String, Object> var1) throws Exception;

    public int deleteById(String[] var1) throws Exception;

    public int deleteByAccHname(List<String> var1) throws Exception;
}

