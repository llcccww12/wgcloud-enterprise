/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.mapper;

import com.wgcloud.entity.DbTableCount;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public interface DbTableCountMapper {
    public List<DbTableCount> selectAllByParams(Map<String, Object> var1) throws Exception;

    public List<DbTableCount> selectByParams(Map<String, Object> var1) throws Exception;

    public DbTableCount selectById(String var1) throws Exception;

    public void save(DbTableCount var1) throws Exception;

    public int deleteById(String[] var1) throws Exception;

    public void insertList(List<DbTableCount> var1) throws Exception;

    public int countByParams(Map<String, Object> var1) throws Exception;

    public int updateById(DbTableCount var1) throws Exception;

    public int deleteByDate(Map<String, Object> var1) throws Exception;
}

