/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.mapper;

import com.wgcloud.entity.AccountInfo;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountInfoMapper {
    public List<AccountInfo> selectAllByParams(Map<String, Object> var1) throws Exception;

    public List<AccountInfo> selectByParams(Map<String, Object> var1) throws Exception;

    public AccountInfo selectById(String var1) throws Exception;

    public void save(AccountInfo var1) throws Exception;

    public int deleteById(String[] var1) throws Exception;

    public int updateById(AccountInfo var1) throws Exception;

    public int countByParams(Map<String, Object> var1) throws Exception;
}

