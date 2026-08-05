/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.mapper;

import com.wgcloud.entity.SnmpInfo;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public interface SnmpInfoMapper {
    public List<SnmpInfo> selectAllByParams(Map<String, Object> var1) throws Exception;

    public List<SnmpInfo> selectByParams(Map<String, Object> var1) throws Exception;

    public SnmpInfo selectById(String var1) throws Exception;

    public void save(SnmpInfo var1) throws Exception;

    public void updateList(List<SnmpInfo> var1) throws Exception;

    public int deleteById(String[] var1) throws Exception;

    public int countByParams(Map<String, Object> var1) throws Exception;

    public int updateById(SnmpInfo var1) throws Exception;

    public int updateToTargetAccount(Map<String, Object> var1) throws Exception;

    public int updateActive(Map<String, Object> var1) throws Exception;
}

