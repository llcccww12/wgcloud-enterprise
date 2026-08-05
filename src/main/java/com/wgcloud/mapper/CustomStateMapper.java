/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.mapper;

import com.wgcloud.entity.CustomState;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomStateMapper {
    public List<CustomState> selectAllByParams(Map<String, Object> var1) throws Exception;

    public List<CustomState> selectByParams(Map<String, Object> var1) throws Exception;

    public CustomState selectById(String var1) throws Exception;

    public int selectByParamsCount(Map<String, Object> var1);

    public void save(CustomState var1) throws Exception;

    public void insertList(List<CustomState> var1) throws Exception;

    public int deleteByCustomInfoId(String var1) throws Exception;

    public int deleteByDate(Map<String, Object> var1) throws Exception;

    public int deleteById(String[] var1) throws Exception;
}

