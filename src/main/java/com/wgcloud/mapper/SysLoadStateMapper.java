/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.mapper;

import com.wgcloud.entity.SysLoadState;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public interface SysLoadStateMapper {
    public List<SysLoadState> selectAllByParams(Map<String, Object> var1) throws Exception;

    public List<SysLoadState> selectByParams(Map<String, Object> var1) throws Exception;

    public SysLoadState selectById(String var1) throws Exception;

    public void save(SysLoadState var1) throws Exception;

    public void insertList(List<SysLoadState> var1) throws Exception;

    public int deleteByDate(Map<String, Object> var1) throws Exception;

    public int deleteById(String[] var1) throws Exception;

    public SysLoadState selectMaxByHostname(Map<String, Object> var1) throws Exception;

    public SysLoadState selectAvgByHostname(Map<String, Object> var1) throws Exception;

    public SysLoadState selectMinByHostname(Map<String, Object> var1) throws Exception;

    public SysLoadState selectMaxByDate(Map<String, Object> var1) throws Exception;
}

