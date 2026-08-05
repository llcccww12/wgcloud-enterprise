/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.mapper;

import com.wgcloud.entity.DceState;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public interface DceStateMapper {
    public List<DceState> selectAllByParams(Map<String, Object> var1) throws Exception;

    public List<DceState> selectByParams(Map<String, Object> var1) throws Exception;

    public DceState selectById(String var1) throws Exception;

    public void save(DceState var1) throws Exception;

    public void insertList(List<DceState> var1) throws Exception;

    public int deleteByDate(Map<String, Object> var1) throws Exception;

    public int deleteById(String[] var1) throws Exception;
}

