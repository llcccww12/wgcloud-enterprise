/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.mapper;

import com.wgcloud.entity.DceInfo;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public interface DceInfoMapper {
    public List<DceInfo> selectAllByParams(Map<String, Object> var1) throws Exception;

    public List<DceInfo> selectByParams(Map<String, Object> var1) throws Exception;

    public DceInfo selectById(String var1) throws Exception;

    public void save(DceInfo var1) throws Exception;

    public void insertList(List<DceInfo> var1) throws Exception;

    public int deleteById(String[] var1) throws Exception;

    public int countByParams(Map<String, Object> var1) throws Exception;

    public void updateList(List<DceInfo> var1) throws Exception;

    public void updateById(DceInfo var1) throws Exception;

    public int updateToTargetAccount(Map<String, Object> var1) throws Exception;

    public int updateActive(Map<String, Object> var1) throws Exception;

    public int updateOrderNum(DceInfo var1) throws Exception;
}

