/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.mapper;

import com.wgcloud.entity.DiskSmart;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public interface DiskSmartMapper {
    public List<DiskSmart> selectAllByParams(Map<String, Object> var1) throws Exception;

    public List<DiskSmart> selectByParams(Map<String, Object> var1) throws Exception;

    public DiskSmart selectById(String var1) throws Exception;

    public void save(DiskSmart var1) throws Exception;

    public void insertList(List<DiskSmart> var1) throws Exception;

    public int deleteByDate(Map<String, Object> var1) throws Exception;

    public int deleteById(String[] var1) throws Exception;

    public int deleteByAccHname(List<String> var1) throws Exception;
}

