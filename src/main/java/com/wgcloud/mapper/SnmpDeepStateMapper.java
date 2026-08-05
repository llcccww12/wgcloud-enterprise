/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.mapper;

import com.wgcloud.entity.SnmpDeepState;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public interface SnmpDeepStateMapper {
    public List<SnmpDeepState> selectAllByParams(Map<String, Object> var1) throws Exception;

    public List<SnmpDeepState> selectByParams(Map<String, Object> var1) throws Exception;

    public SnmpDeepState selectById(String var1) throws Exception;

    public void save(SnmpDeepState var1) throws Exception;

    public void updateList(List<SnmpDeepState> var1) throws Exception;

    public int deleteById(String[] var1) throws Exception;

    public int countByParams(Map<String, Object> var1) throws Exception;

    public int updateById(SnmpDeepState var1) throws Exception;

    public int deleteBySnmpDeepInfoId(String var1) throws Exception;
}

