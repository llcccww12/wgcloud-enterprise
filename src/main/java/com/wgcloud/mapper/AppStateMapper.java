/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.mapper;

import com.wgcloud.entity.AppState;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public interface AppStateMapper {
    public List<AppState> selectAllByParams(Map<String, Object> var1) throws Exception;

    public List<AppState> selectByParams(Map<String, Object> var1) throws Exception;

    public AppState selectById(String var1) throws Exception;

    public int selectByParamsCount(Map<String, Object> var1);

    public void save(AppState var1) throws Exception;

    public void insertList(List<AppState> var1) throws Exception;

    public int deleteByAppInfoId(String var1) throws Exception;

    public int deleteByDate(Map<String, Object> var1) throws Exception;

    public int deleteById(String[] var1) throws Exception;
}

