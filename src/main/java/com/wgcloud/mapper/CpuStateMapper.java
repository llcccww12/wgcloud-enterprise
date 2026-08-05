/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.mapper;

import com.wgcloud.entity.CpuState;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public interface CpuStateMapper {
    public List<CpuState> selectAllByParams(Map<String, Object> var1) throws Exception;

    public List<CpuState> selectByParams(Map<String, Object> var1) throws Exception;

    public CpuState selectById(String var1) throws Exception;

    public CpuState selectMaxAvgByHostname(Map<String, Object> var1) throws Exception;

    public Double selectMaxByDate(Map<String, Object> var1) throws Exception;

    public int selectByParamsCount(Map<String, Object> var1);

    public void save(CpuState var1) throws Exception;

    public void insertList(List<CpuState> var1) throws Exception;

    public int deleteByDate(Map<String, Object> var1) throws Exception;

    public int deleteById(String[] var1) throws Exception;
}

