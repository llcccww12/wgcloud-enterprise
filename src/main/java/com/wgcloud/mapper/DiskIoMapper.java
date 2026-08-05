/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.mapper;

import com.wgcloud.entity.DiskIo;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public interface DiskIoMapper {
    public List<DiskIo> selectAllByParams(Map<String, Object> var1) throws Exception;

    public List<DiskIo> selectByParams(Map<String, Object> var1) throws Exception;

    public DiskIo selectById(String var1) throws Exception;

    public void save(DiskIo var1) throws Exception;

    public void insertList(List<DiskIo> var1) throws Exception;

    public int deleteByDate(Map<String, Object> var1) throws Exception;

    public int deleteById(String[] var1) throws Exception;

    public int deleteByAccHname(List<String> var1) throws Exception;
}

