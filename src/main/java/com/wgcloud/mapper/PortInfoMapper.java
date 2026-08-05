/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.mapper;

import com.wgcloud.entity.PortInfo;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public interface PortInfoMapper {
    public List<PortInfo> selectAllByParams(Map<String, Object> var1) throws Exception;

    public List<PortInfo> selectByParams(Map<String, Object> var1) throws Exception;

    public PortInfo selectById(String var1) throws Exception;

    public void save(PortInfo var1) throws Exception;

    public void insertList(List<PortInfo> var1) throws Exception;

    public void updateList(List<PortInfo> var1) throws Exception;

    public void downByHostName(List<String> var1) throws Exception;

    public int deleteById(String[] var1) throws Exception;

    public int deleteByHostName(Map<String, Object> var1) throws Exception;

    public int countByParams(Map<String, Object> var1) throws Exception;

    public int updateById(PortInfo var1) throws Exception;

    public int updateActive(Map<String, Object> var1) throws Exception;
}

