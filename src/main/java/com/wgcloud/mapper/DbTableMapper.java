/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.mapper;

import com.wgcloud.entity.DbTable;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public interface DbTableMapper {
    public List<DbTable> selectAllByParams(Map<String, Object> var1) throws Exception;

    public List<DbTable> selectByParams(Map<String, Object> var1) throws Exception;

    public DbTable selectById(String var1) throws Exception;

    public void save(DbTable var1) throws Exception;

    public int deleteById(String[] var1) throws Exception;

    public int deleteByDbInfoId(String var1) throws Exception;

    public void updateList(List<DbTable> var1) throws Exception;

    public int countByParams(Map<String, Object> var1) throws Exception;

    public Long sumByParams(Map<String, Object> var1) throws Exception;

    public void updateById(DbTable var1) throws Exception;

    public int updateActive(Map<String, Object> var1) throws Exception;
}

