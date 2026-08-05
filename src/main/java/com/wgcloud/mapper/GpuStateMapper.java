/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.mapper;

import com.wgcloud.entity.GpuState;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public interface GpuStateMapper {
    public List<GpuState> selectAllByParams(Map<String, Object> var1) throws Exception;

    public List<GpuState> selectByParams(Map<String, Object> var1) throws Exception;

    public GpuState selectById(String var1) throws Exception;

    public int selectByParamsCount(Map<String, Object> var1);

    public void save(GpuState var1) throws Exception;

    public void insertList(List<GpuState> var1) throws Exception;

    public int deleteByDate(Map<String, Object> var1) throws Exception;

    public int deleteById(String[] var1) throws Exception;

    // 按主机名查最近一条 GPU 状态（主机列表展示 GPU% 用）
    public GpuState selectLatestByHostname(String hostname);
}

