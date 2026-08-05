/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.mapper;

import com.wgcloud.entity.MemState;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public interface MemStateMapper {
    public List<MemState> selectAllByParams(Map<String, Object> var1) throws Exception;

    public List<MemState> selectByParams(Map<String, Object> var1) throws Exception;

    public MemState selectById(String var1) throws Exception;

    public MemState selectMaxAvgByHostname(Map<String, Object> var1) throws Exception;

    public Double selectMaxByDate(Map<String, Object> var1) throws Exception;

    public void save(MemState var1) throws Exception;

    public void insertList(List<MemState> var1) throws Exception;

    public int deleteByDate(Map<String, Object> var1) throws Exception;

    public int deleteById(String[] var1) throws Exception;
}

