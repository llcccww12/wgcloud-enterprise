/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.mapper;

import com.wgcloud.entity.SnmpState;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public interface SnmpStateMapper {
    public List<SnmpState> selectAllByParams(Map<String, Object> var1) throws Exception;

    public List<SnmpState> selectByParams(Map<String, Object> var1) throws Exception;

    public SnmpState selectById(String var1) throws Exception;

    public int selectByParamsCount(Map<String, Object> var1);

    public void save(SnmpState var1) throws Exception;

    public void insertList(List<SnmpState> var1) throws Exception;

    public int deleteBySnmpInfoId(String var1) throws Exception;

    public int deleteByDate(Map<String, Object> var1) throws Exception;

    public int deleteById(String[] var1) throws Exception;
}

