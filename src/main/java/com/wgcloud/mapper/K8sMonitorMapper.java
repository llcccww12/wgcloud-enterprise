/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.mapper;

import com.wgcloud.entity.K8sMonitor;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public interface K8sMonitorMapper {
    public List<K8sMonitor> selectAllByParams(Map<String, Object> var1) throws Exception;

    public List<K8sMonitor> selectByParams(Map<String, Object> var1) throws Exception;

    public K8sMonitor selectById(String var1) throws Exception;

    public int countByParams(Map<String, Object> var1);

    public void save(K8sMonitor var1) throws Exception;

    public void insertList(List<K8sMonitor> var1) throws Exception;

    public int deleteByK8sName(String var1) throws Exception;

    public int deleteByDate(Map<String, Object> var1) throws Exception;

    public int deleteById(String[] var1) throws Exception;
}

