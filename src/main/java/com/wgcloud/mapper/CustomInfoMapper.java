/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.mapper;

import com.wgcloud.entity.CustomInfo;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomInfoMapper {
    public List<CustomInfo> selectAllByParams(Map<String, Object> var1) throws Exception;

    public List<CustomInfo> selectByParams(Map<String, Object> var1) throws Exception;

    public CustomInfo selectById(String var1) throws Exception;

    public void save(CustomInfo var1) throws Exception;

    public void insertList(List<CustomInfo> var1) throws Exception;

    public void updateList(List<CustomInfo> var1) throws Exception;

    public void downByHostName(List<String> var1) throws Exception;

    public int deleteById(String[] var1) throws Exception;

    public int deleteByHostName(Map<String, Object> var1) throws Exception;

    public int countByParams(Map<String, Object> var1) throws Exception;

    public int updateById(CustomInfo var1) throws Exception;

    public int updateActive(Map<String, Object> var1) throws Exception;
}

