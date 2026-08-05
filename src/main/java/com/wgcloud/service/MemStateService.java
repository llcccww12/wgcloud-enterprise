/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wgcloud.entity.MemState;
import com.wgcloud.mapper.MemStateMapper;
import com.wgcloud.util.UUIDUtil;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MemStateService {
    private static final Logger logger = LoggerFactory.getLogger(MemStateService.class);
    @Autowired
    private MemStateMapper memStateMapper;

    public PageInfo selectByParams(Map<String, Object> params, int currPage, int pageSize) throws Exception {
        PageHelper.startPage((int)currPage, (int)pageSize);
        List<MemState> list = this.memStateMapper.selectByParams(params);
        PageInfo pageInfo = new PageInfo(list);
        return pageInfo;
    }

    public void save(MemState memState) throws Exception {
        memState.setId(UUIDUtil.getUUID());
        memState.setCreateTime(new Date());
        this.memStateMapper.save(memState);
    }

    public void saveRecord(List<MemState> recordList) {
        try {
            if (recordList.size() < 1) {
                return;
            }
            for (MemState as : recordList) {
                as.setId(UUIDUtil.getUUID());
            }
            this.memStateMapper.insertList(recordList);
        }
        catch (Exception e) {
            logger.error("MemState saveRecord error", (Throwable)e);
        }
    }

    public int deleteById(String[] id) throws Exception {
        return this.memStateMapper.deleteById(id);
    }

    public MemState selectById(String id) throws Exception {
        return this.memStateMapper.selectById(id);
    }

    public MemState selectMaxAvgByHostname(Map<String, Object> map) throws Exception {
        return this.memStateMapper.selectMaxAvgByHostname(map);
    }

    public Double selectMaxByDate(Map<String, Object> map) throws Exception {
        return this.memStateMapper.selectMaxByDate(map);
    }

    public List<MemState> selectAllByParams(Map<String, Object> params) throws Exception {
        return this.memStateMapper.selectAllByParams(params);
    }

    public int deleteByDate(Map<String, Object> map) throws Exception {
        return this.memStateMapper.deleteByDate(map);
    }
}

