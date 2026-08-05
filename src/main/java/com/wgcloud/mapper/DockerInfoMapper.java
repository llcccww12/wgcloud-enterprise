/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.mapper;

import com.wgcloud.entity.DockerInfo;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public interface DockerInfoMapper {
    public List<DockerInfo> selectAllByParams(Map<String, Object> var1) throws Exception;

    public List<DockerInfo> selectByParams(Map<String, Object> var1) throws Exception;

    public DockerInfo selectById(String var1) throws Exception;

    public void save(DockerInfo var1) throws Exception;

    public void insertList(List<DockerInfo> var1) throws Exception;

    public void updateList(List<DockerInfo> var1) throws Exception;

    public void downByHostName(List<String> var1) throws Exception;

    public int deleteById(String[] var1) throws Exception;

    public int deleteByHostName(Map<String, Object> var1) throws Exception;

    public int countByParams(Map<String, Object> var1) throws Exception;

    public int updateById(DockerInfo var1) throws Exception;

    public int updateActive(Map<String, Object> var1) throws Exception;
}

