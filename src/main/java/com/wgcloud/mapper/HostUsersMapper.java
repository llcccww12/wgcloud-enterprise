/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.mapper;

import com.wgcloud.entity.HostUsers;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public interface HostUsersMapper {
    public List<HostUsers> selectAllByParams(Map<String, Object> var1) throws Exception;

    public List<HostUsers> selectByParams(Map<String, Object> var1) throws Exception;

    public HostUsers selectById(String var1) throws Exception;

    public void save(HostUsers var1) throws Exception;

    public void insertList(List<HostUsers> var1) throws Exception;

    public int deleteByDate(Map<String, Object> var1) throws Exception;

    public int deleteById(String[] var1) throws Exception;

    public int deleteByAccHname(List<String> var1) throws Exception;
}

