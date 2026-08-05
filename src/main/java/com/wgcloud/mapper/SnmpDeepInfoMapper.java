/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.mapper;

import com.wgcloud.entity.SnmpDeepInfo;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public interface SnmpDeepInfoMapper {
    public List<SnmpDeepInfo> selectAllByParams(Map<String, Object> var1) throws Exception;

    public List<SnmpDeepInfo> selectByParams(Map<String, Object> var1) throws Exception;

    public SnmpDeepInfo selectById(String var1) throws Exception;

    public void save(SnmpDeepInfo var1) throws Exception;

    public void updateList(List<SnmpDeepInfo> var1) throws Exception;

    public int deleteById(String[] var1) throws Exception;

    public int countByParams(Map<String, Object> var1) throws Exception;

    public int updateById(SnmpDeepInfo var1) throws Exception;

    public int updateActive(Map<String, Object> var1) throws Exception;

    public int updateToTargetAccount(Map<String, Object> var1) throws Exception;
}

