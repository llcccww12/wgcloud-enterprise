/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.mapper;

import com.wgcloud.entity.PasswdInfo;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public interface PasswdInfoMapper {
    public List<PasswdInfo> selectAllByParams(Map<String, Object> var1) throws Exception;

    public List<PasswdInfo> selectByParams(Map<String, Object> var1) throws Exception;

    public PasswdInfo selectById(String var1) throws Exception;

    public void save(PasswdInfo var1) throws Exception;

    public int deleteById(String[] var1) throws Exception;

    public int countByParams(Map<String, Object> var1) throws Exception;

    public int updateToTargetAccount(Map<String, Object> var1) throws Exception;

    public int updateById(PasswdInfo var1) throws Exception;
}

