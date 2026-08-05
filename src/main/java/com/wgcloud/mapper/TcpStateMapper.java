/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.mapper;

import com.wgcloud.entity.TcpState;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public interface TcpStateMapper {
    public List<TcpState> selectAllByParams(Map<String, Object> var1) throws Exception;

    public List<TcpState> selectByParams(Map<String, Object> var1) throws Exception;

    public TcpState selectById(String var1) throws Exception;

    public void save(TcpState var1) throws Exception;

    public void insertList(List<TcpState> var1) throws Exception;

    public int deleteByDate(Map<String, Object> var1) throws Exception;

    public int deleteById(String[] var1) throws Exception;
}

