/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.mapper;

import com.wgcloud.entity.IntrusionInfo;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public interface IntrusionInfoMapper {
    public List<IntrusionInfo> selectAllByParams(Map<String, Object> var1) throws Exception;

    public List<IntrusionInfo> selectByAccountId(String var1) throws Exception;

    public List<IntrusionInfo> selectByParams(Map<String, Object> var1) throws Exception;

    public IntrusionInfo selectById(String var1) throws Exception;

    public void save(IntrusionInfo var1) throws Exception;

    public void insertList(List<IntrusionInfo> var1) throws Exception;

    public int deleteById(String[] var1) throws Exception;

    public int deleteByDate(Map<String, Object> var1) throws Exception;

    public int deleteByAccHname(Map<String, Object> var1) throws Exception;
}

