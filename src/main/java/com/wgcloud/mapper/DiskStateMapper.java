/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.mapper;

import com.wgcloud.entity.DiskState;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public interface DiskStateMapper {
    public List<DiskState> selectAllByParams(Map<String, Object> var1) throws Exception;

    public List<DiskState> selectByParams(Map<String, Object> var1) throws Exception;

    public DiskState selectById(String var1) throws Exception;

    public void save(DiskState var1) throws Exception;

    public void insertList(List<DiskState> var1) throws Exception;

    public int deleteByDate(Map<String, Object> var1) throws Exception;

    public int deleteById(String[] var1) throws Exception;

    public int deleteByAccHname(List<String> var1) throws Exception;
}

