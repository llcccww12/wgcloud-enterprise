/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.mapper;

import com.wgcloud.entity.NetIoState;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public interface NetIoStateMapper {
    public List<NetIoState> selectAllByParams(Map<String, Object> var1) throws Exception;

    public List<NetIoState> selectByParams(Map<String, Object> var1);

    public List<NetIoState> selectTop3(Map<String, Object> var1);

    public NetIoState selectById(String var1) throws Exception;

    public void save(NetIoState var1) throws Exception;

    public void insertList(List<NetIoState> var1) throws Exception;

    public int deleteByDate(Map<String, Object> var1) throws Exception;

    public int deleteById(String[] var1) throws Exception;

    public NetIoState selectMaxByHostname(Map<String, Object> var1) throws Exception;

    public NetIoState selectAvgByHostname(Map<String, Object> var1) throws Exception;

    public NetIoState selectMinByHostname(Map<String, Object> var1) throws Exception;

    public NetIoState selectMaxByDate(Map<String, Object> var1) throws Exception;
}

