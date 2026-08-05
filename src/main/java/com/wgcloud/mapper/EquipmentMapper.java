/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.mapper;

import com.wgcloud.entity.Equipment;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public interface EquipmentMapper {
    public List<Equipment> selectAllByParams(Map<String, Object> var1) throws Exception;

    public List<Equipment> selectByParams(Map<String, Object> var1) throws Exception;

    public Equipment selectById(String var1) throws Exception;

    public void save(Equipment var1) throws Exception;

    public int deleteById(String[] var1) throws Exception;

    public int countByParams(Map<String, Object> var1) throws Exception;

    public int updateById(Equipment var1) throws Exception;

    public int updateToTargetAccount(Map<String, Object> var1) throws Exception;
}

