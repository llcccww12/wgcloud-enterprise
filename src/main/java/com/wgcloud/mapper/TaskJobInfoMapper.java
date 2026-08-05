/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.mapper;

import com.wgcloud.entity.TaskJobInfo;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskJobInfoMapper {
    public List<TaskJobInfo> selectAllByParams(Map<String, Object> var1) throws Exception;

    public List<TaskJobInfo> selectByParams(Map<String, Object> var1) throws Exception;

    public TaskJobInfo selectById(String var1) throws Exception;

    public void save(TaskJobInfo var1) throws Exception;

    public int deleteById(String[] var1) throws Exception;

    public int countByParams(Map<String, Object> var1) throws Exception;

    public int updateById(TaskJobInfo var1) throws Exception;

    public int updateToTargetAccount(Map<String, Object> var1) throws Exception;

    public int updateActive(Map<String, Object> var1) throws Exception;

    public int updateSendByIds(String[] var1) throws Exception;
}

