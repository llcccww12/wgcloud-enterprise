/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.mapper;

import com.wgcloud.entity.HostGroup;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public interface HostGroupMapper {
    public List<HostGroup> selectAllByParams(Map<String, Object> var1) throws Exception;

    public List<HostGroup> selectByParams(Map<String, Object> var1) throws Exception;

    public HostGroup selectById(String var1) throws Exception;

    public void save(HostGroup var1) throws Exception;

    public int deleteById(String[] var1) throws Exception;

    public int updateById(HostGroup var1) throws Exception;

    public int countByParams(Map<String, Object> var1) throws Exception;
}

