/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.mapper;

import com.wgcloud.entity.AgentRunState;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public interface AgentRunStateMapper {
    public List<AgentRunState> selectAllByParams(Map<String, Object> var1) throws Exception;

    public List<AgentRunState> selectByParams(Map<String, Object> var1) throws Exception;

    public AgentRunState selectById(String var1) throws Exception;

    public void save(AgentRunState var1) throws Exception;

    public void insertList(List<AgentRunState> var1) throws Exception;

    public void updateList(List<AgentRunState> var1) throws Exception;

    public int deleteById(String[] var1) throws Exception;

    public int countByParams(Map<String, Object> var1) throws Exception;

    public int updateById(AgentRunState var1) throws Exception;

    public int deleteByHostName(Map<String, Object> var1) throws Exception;
}

