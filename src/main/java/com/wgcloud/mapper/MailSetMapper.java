/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.mapper;

import com.wgcloud.entity.MailSet;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public interface MailSetMapper {
    public List<MailSet> selectAllByParams(Map<String, Object> var1) throws Exception;

    public void save(MailSet var1) throws Exception;

    public int deleteById(String[] var1) throws Exception;

    public int updateById(MailSet var1) throws Exception;
}

