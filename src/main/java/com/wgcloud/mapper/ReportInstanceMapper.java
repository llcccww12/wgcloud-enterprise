/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.mapper;

import com.wgcloud.entity.ReportInstance;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportInstanceMapper {
    public List<ReportInstance> selectAllByParams(Map<String, Object> var1);

    public int countByParams(Map<String, Object> var1) throws Exception;

    public List<ReportInstance> selectByParams(Map<String, Object> var1) throws Exception;

    public ReportInstance selectById(String var1) throws Exception;

    public void save(ReportInstance var1) throws Exception;

    public int deleteById(String[] var1) throws Exception;

    public void insertList(List<ReportInstance> var1) throws Exception;

    public int deleteByDate(Map<String, Object> var1) throws Exception;
}

