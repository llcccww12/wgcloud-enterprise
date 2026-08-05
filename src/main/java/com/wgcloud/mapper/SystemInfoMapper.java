/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.mapper;

import com.wgcloud.entity.SystemInfo;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public interface SystemInfoMapper {
    public List<SystemInfo> selectAllHostNameByParams(Map<String, Object> var1) throws Exception;

    public List<SystemInfo> selectAllByParams(Map<String, Object> var1) throws Exception;

    public List<SystemInfo> selectByHostname(String var1) throws Exception;

    public List<SystemInfo> selectByParams(Map<String, Object> var1);

    public void insertList(List<SystemInfo> var1) throws Exception;

    public void updateList(List<SystemInfo> var1) throws Exception;

    public SystemInfo selectById(String var1) throws Exception;

    public int updateById(SystemInfo var1) throws Exception;

    public int countByParams(Map<String, Object> var1) throws Exception;

    public void save(SystemInfo var1) throws Exception;

    public int deleteById(String[] var1) throws Exception;

    public void downByHostName(List<String> var1) throws Exception;

    public int deleteByAccHname(Map<String, Object> var1) throws Exception;

    public int updateAccountByHostName(Map<String, Object> var1) throws Exception;

    public int updateToTargetAccount(Map<String, Object> var1) throws Exception;

    public List<SystemInfo> selectByIds(String[] var1) throws Exception;

    public int updateActive(SystemInfo var1) throws Exception;

    public int updateCountBlock(SystemInfo var1) throws Exception;

    public int updateOrderNum(SystemInfo var1) throws Exception;

    public int updateDiskPerByHostName(List<SystemInfo> var1) throws Exception;
}

