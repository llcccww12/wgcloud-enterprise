/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.mapper;

import com.wgcloud.entity.HeathState;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public interface HeathStateMapper {
    public List<HeathState> selectAllByParams(Map<String, Object> var1) throws Exception;

    public List<HeathState> selectByParams(Map<String, Object> var1) throws Exception;

    public HeathState selectById(String var1) throws Exception;

    public void save(HeathState var1) throws Exception;

    public void insertList(List<HeathState> var1) throws Exception;

    public int deleteByDate(Map<String, Object> var1) throws Exception;

    public int deleteById(String[] var1) throws Exception;
}

