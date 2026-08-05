/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.mapper;

import com.wgcloud.entity.HostWarnDiy;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public interface HostWarnDiyMapper {
    public List<HostWarnDiy> selectAllByParams(Map<String, Object> var1) throws Exception;

    public List<HostWarnDiy> selectByParams(Map<String, Object> var1) throws Exception;

    public HostWarnDiy selectById(String var1) throws Exception;

    public void save(HostWarnDiy var1) throws Exception;

    public void insertList(List<HostWarnDiy> var1) throws Exception;

    public int deleteById(String[] var1) throws Exception;

    public int countByParams(Map<String, Object> var1) throws Exception;

    public void updateList(List<HostWarnDiy> var1) throws Exception;

    public void updateById(HostWarnDiy var1) throws Exception;

    public int updateToTargetAccount(Map<String, Object> var1) throws Exception;

    public int updateActive(Map<String, Object> var1) throws Exception;
}

