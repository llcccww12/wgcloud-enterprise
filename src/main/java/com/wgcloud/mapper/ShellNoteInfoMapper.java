/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.mapper;

import com.wgcloud.entity.ShellNoteInfo;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public interface ShellNoteInfoMapper {
    public List<ShellNoteInfo> selectAllByParams(Map<String, Object> var1) throws Exception;

    public List<ShellNoteInfo> selectByParams(Map<String, Object> var1) throws Exception;

    public ShellNoteInfo selectById(String var1) throws Exception;

    public void save(ShellNoteInfo var1) throws Exception;

    public int deleteById(String[] var1) throws Exception;

    public int countByParams(Map<String, Object> var1) throws Exception;

    public int updateById(ShellNoteInfo var1) throws Exception;

    public int updateToTargetAccount(Map<String, Object> var1) throws Exception;
}

