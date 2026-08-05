/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.mapper;

import com.wgcloud.entity.AppExceptionInfo;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public interface AppExceptionInfoMapper {
    public List<AppExceptionInfo> selectAllByParams(Map<String, Object> var1) throws Exception;

    public List<AppExceptionInfo> selectByParams(Map<String, Object> var1) throws Exception;

    public AppExceptionInfo selectById(String var1) throws Exception;

    public void save(AppExceptionInfo var1) throws Exception;

    public void insertList(List<AppExceptionInfo> var1) throws Exception;

    public int deleteById(String[] var1) throws Exception;

    public int deleteByDate(Map<String, Object> var1) throws Exception;

    public int deleteByHostName(List<String> var1) throws Exception;

    public int countByParams(Map<String, Object> var1) throws Exception;

    public int updateById(AppExceptionInfo var1) throws Exception;
}

