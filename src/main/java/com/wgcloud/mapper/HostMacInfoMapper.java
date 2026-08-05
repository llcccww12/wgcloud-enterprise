/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.mapper;

import com.wgcloud.entity.HostMacInfo;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public interface HostMacInfoMapper {
    public List<HostMacInfo> selectAllByParams(Map<String, Object> var1) throws Exception;

    public List<HostMacInfo> selectByParams(Map<String, Object> var1) throws Exception;

    public HostMacInfo selectById(String var1) throws Exception;

    public void save(HostMacInfo var1) throws Exception;

    public void insertList(List<HostMacInfo> var1) throws Exception;

    public int deleteByDate(Map<String, Object> var1) throws Exception;

    public int deleteById(String[] var1) throws Exception;

    public int deleteByAccHname(List<String> var1) throws Exception;
}

