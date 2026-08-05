/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.mapper;

import com.wgcloud.entity.DbInfo;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public interface DbInfoMapper {
    public List<DbInfo> selectAllByParams(Map<String, Object> var1) throws Exception;

    public List<DbInfo> selectByParams(Map<String, Object> var1) throws Exception;

    public DbInfo selectById(String var1) throws Exception;

    public void save(DbInfo var1) throws Exception;

    public int deleteById(String[] var1) throws Exception;

    public int countByParams(Map<String, Object> var1) throws Exception;

    public int updateById(DbInfo var1) throws Exception;

    public int updateToTargetAccount(Map<String, Object> var1) throws Exception;

    public int updateActive(Map<String, Object> var1) throws Exception;
}

