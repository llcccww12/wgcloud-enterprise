/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.mapper;

import com.wgcloud.entity.ReportInfo;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportInfoMapper {
    public List<ReportInfo> selectAllByParams(Map<String, Object> var1);

    public int countByParams(Map<String, Object> var1) throws Exception;

    public List<ReportInfo> selectByParams(Map<String, Object> var1) throws Exception;

    public ReportInfo selectById(String var1) throws Exception;

    public void save(ReportInfo var1) throws Exception;

    public int deleteById(String[] var1) throws Exception;

    public int deleteByDate(Map<String, Object> var1) throws Exception;
}

