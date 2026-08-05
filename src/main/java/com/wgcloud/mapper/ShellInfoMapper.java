/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.mapper;

import com.wgcloud.entity.ShellInfo;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public interface ShellInfoMapper {
    public List<ShellInfo> selectAllByParams(Map<String, Object> var1) throws Exception;

    public List<ShellInfo> selectByParams(Map<String, Object> var1) throws Exception;

    public ShellInfo selectById(String var1) throws Exception;

    public void save(ShellInfo var1) throws Exception;

    public void insertList(List<ShellInfo> var1) throws Exception;

    public void updateList(List<ShellInfo> var1) throws Exception;

    public int deleteById(String[] var1) throws Exception;

    public int countByParams(Map<String, Object> var1) throws Exception;

    public int updateById(ShellInfo var1) throws Exception;
}

