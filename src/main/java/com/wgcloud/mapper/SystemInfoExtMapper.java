/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.mapper;

import com.wgcloud.entity.SystemInfoExt;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public interface SystemInfoExtMapper {
    public List<SystemInfoExt> selectAllByParams(Map<String, Object> var1) throws Exception;

    public List<SystemInfoExt> selectByParams(Map<String, Object> var1) throws Exception;

    public SystemInfoExt selectById(String var1) throws Exception;

    public void insertList(List<SystemInfoExt> var1) throws Exception;

    public int deleteByDate(Map<String, Object> var1) throws Exception;

    public int deleteById(String[] var1) throws Exception;

    public int deleteByHostname(List<String> var1) throws Exception;
}

