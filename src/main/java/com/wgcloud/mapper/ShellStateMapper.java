/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.mapper;

import com.wgcloud.entity.ShellState;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public interface ShellStateMapper {
    public List<ShellState> selectAllByParams(Map<String, Object> var1) throws Exception;

    public List<ShellState> selectByParams(Map<String, Object> var1) throws Exception;

    public ShellState selectById(String var1) throws Exception;

    public int countByParams(Map<String, Object> var1);

    public void save(ShellState var1) throws Exception;

    public void insertList(List<ShellState> var1) throws Exception;

    public int deleteByShellId(String var1) throws Exception;

    public int deleteByDate(Map<String, Object> var1) throws Exception;

    public int deleteById(String[] var1) throws Exception;

    public int updateSendByIds(String[] var1) throws Exception;

    public int cancelByShellId(String var1) throws Exception;

    public int restartByShellId(String var1, String var2) throws Exception;

    public int updateById(ShellState var1) throws Exception;
}

