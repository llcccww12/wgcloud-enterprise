/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.mapper;

import com.wgcloud.entity.DockerState;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public interface DockerStateMapper {
    public List<DockerState> selectAllByParams(Map<String, Object> var1) throws Exception;

    public List<DockerState> selectByParams(Map<String, Object> var1) throws Exception;

    public DockerState selectById(String var1) throws Exception;

    public int selectByParamsCount(Map<String, Object> var1);

    public void save(DockerState var1) throws Exception;

    public void insertList(List<DockerState> var1) throws Exception;

    public int deleteByDockerInfoId(String var1) throws Exception;

    public int deleteByDate(Map<String, Object> var1) throws Exception;

    public int deleteById(String[] var1) throws Exception;
}

