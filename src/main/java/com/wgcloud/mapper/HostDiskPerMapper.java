/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.mapper;

import com.wgcloud.entity.HostDiskPer;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public interface HostDiskPerMapper {
    public List<HostDiskPer> selectAllByParams(Map<String, Object> var1) throws Exception;

    public List<HostDiskPer> selectByParams(Map<String, Object> var1) throws Exception;

    public HostDiskPer selectById(String var1) throws Exception;

    public void save(HostDiskPer var1) throws Exception;

    public void insertList(List<HostDiskPer> var1) throws Exception;

    public int deleteByDate(Map<String, Object> var1) throws Exception;

    public int deleteById(String[] var1) throws Exception;

    public int deleteByAccHname(List<String> var1) throws Exception;
}

