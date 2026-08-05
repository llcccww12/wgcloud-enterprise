/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.mapper;

import com.wgcloud.entity.HostPciInfo;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public interface HostPciInfoMapper {
    public List<HostPciInfo> selectAllByParams(Map<String, Object> var1) throws Exception;

    public List<HostPciInfo> selectByParams(Map<String, Object> var1) throws Exception;

    public HostPciInfo selectById(String var1) throws Exception;

    public void save(HostPciInfo var1) throws Exception;

    public void insertList(List<HostPciInfo> var1) throws Exception;

    public int deleteByDate(Map<String, Object> var1) throws Exception;

    public int deleteById(String[] var1) throws Exception;

    public int deleteByHostname(List<String> var1) throws Exception;
}

