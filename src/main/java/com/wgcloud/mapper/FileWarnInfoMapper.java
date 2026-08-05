/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.mapper;

import com.wgcloud.entity.FileWarnInfo;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public interface FileWarnInfoMapper {
    public List<FileWarnInfo> selectAllByParams(Map<String, Object> var1) throws Exception;

    public List<FileWarnInfo> selectByParams(Map<String, Object> var1) throws Exception;

    public FileWarnInfo selectById(String var1) throws Exception;

    public void save(FileWarnInfo var1) throws Exception;

    public void insertList(List<FileWarnInfo> var1) throws Exception;

    public void updateList(List<FileWarnInfo> var1) throws Exception;

    public int deleteById(String[] var1) throws Exception;

    public int deleteByHostName(Map<String, Object> var1) throws Exception;

    public Integer countByParams(Map<String, Object> var1) throws Exception;

    public int updateById(FileWarnInfo var1) throws Exception;

    public int updateActive(Map<String, Object> var1) throws Exception;
}

