/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.mapper;

import com.wgcloud.entity.FtpInfo;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public interface FtpInfoMapper {
    public List<FtpInfo> selectAllByParams(Map<String, Object> var1) throws Exception;

    public List<FtpInfo> selectByParams(Map<String, Object> var1) throws Exception;

    public FtpInfo selectById(String var1) throws Exception;

    public void save(FtpInfo var1) throws Exception;

    public void insertList(List<FtpInfo> var1) throws Exception;

    public void updateList(List<FtpInfo> var1) throws Exception;

    public int deleteById(String[] var1) throws Exception;

    public int countByParams(Map<String, Object> var1) throws Exception;

    public int updateById(FtpInfo var1) throws Exception;

    public int updateToTargetAccount(Map<String, Object> var1) throws Exception;

    public int updateActive(Map<String, Object> var1) throws Exception;
}

