/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.mapper;

import com.wgcloud.entity.LogInfo;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public interface LogInfoMapper {
    public List<LogInfo> selectAllByParams(Map<String, Object> var1);

    public int countByParams(Map<String, Object> var1) throws Exception;

    public List<LogInfo> selectByParams(Map<String, Object> var1) throws Exception;

    public List<LogInfo> selectByParamsNoContent(Map<String, Object> var1) throws Exception;

    public LogInfo selectById(String var1) throws Exception;

    public void save(LogInfo var1) throws Exception;

    public int deleteById(String[] var1) throws Exception;

    public void insertList(List<LogInfo> var1) throws Exception;

    public int deleteByDate(Map<String, Object> var1) throws Exception;
}

