/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.mapper;

import com.wgcloud.entity.DiskIoState;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public interface DiskIoStateMapper {
    public List<DiskIoState> selectAllByParams(Map<String, Object> var1) throws Exception;

    public List<DiskIoState> selectByParams(Map<String, Object> var1) throws Exception;

    public DiskIoState selectById(String var1) throws Exception;

    public void save(DiskIoState var1) throws Exception;

    public void insertList(List<DiskIoState> var1) throws Exception;

    public int deleteByDate(Map<String, Object> var1) throws Exception;

    public int deleteById(String[] var1) throws Exception;

    public DiskIoState selectMaxByHostname(Map<String, Object> var1) throws Exception;
}

