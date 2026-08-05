/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.mapper;

import com.wgcloud.entity.FileWarnState;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public interface FileWarnStateMapper {
    public List<FileWarnState> selectAllByParams(Map<String, Object> var1) throws Exception;

    public List<FileWarnState> selectByParams(Map<String, Object> var1) throws Exception;

    public FileWarnState selectById(String var1) throws Exception;

    public Integer countByParams(Map<String, Object> var1);

    public void save(FileWarnState var1) throws Exception;

    public void insertList(List<FileWarnState> var1) throws Exception;

    public int deleteByFileWarnId(String var1) throws Exception;

    public int deleteByDate(Map<String, Object> var1) throws Exception;

    public int deleteById(String[] var1) throws Exception;
}

