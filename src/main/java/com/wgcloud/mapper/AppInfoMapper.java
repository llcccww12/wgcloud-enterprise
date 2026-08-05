/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.mapper;

import com.wgcloud.entity.AppInfo;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public interface AppInfoMapper {
    public List<AppInfo> selectAllByParams(Map<String, Object> var1) throws Exception;

    public List<AppInfo> selectByParams(Map<String, Object> var1) throws Exception;

    public AppInfo selectById(String var1) throws Exception;

    public void save(AppInfo var1) throws Exception;

    public void insertList(List<AppInfo> var1) throws Exception;

    public void updateList(List<AppInfo> var1) throws Exception;

    public void downByHostName(List<String> var1) throws Exception;

    public int deleteById(String[] var1) throws Exception;

    public int deleteByHostName(Map<String, Object> var1) throws Exception;

    public int countByParams(Map<String, Object> var1) throws Exception;

    public int updateById(AppInfo var1) throws Exception;

    public int updateActive(Map<String, Object> var1) throws Exception;
}

