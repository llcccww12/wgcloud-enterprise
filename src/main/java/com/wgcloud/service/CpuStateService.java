/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wgcloud.entity.CpuState;
import com.wgcloud.mapper.CpuStateMapper;
import com.wgcloud.util.UUIDUtil;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CpuStateService {
    private static final Logger logger = LoggerFactory.getLogger(CpuStateService.class);
    @Autowired
    private CpuStateMapper cpuStateMapper;

    public PageInfo selectByParams(Map<String, Object> params, int currPage, int pageSize) throws Exception {
        PageHelper.startPage((int)currPage, (int)pageSize);
        List<CpuState> list = this.cpuStateMapper.selectByParams(params);
        PageInfo pageInfo = new PageInfo(list);
        return pageInfo;
    }

    public void save(CpuState CpuState2) throws Exception {
        CpuState2.setId(UUIDUtil.getUUID());
        CpuState2.setCreateTime(new Date());
        this.cpuStateMapper.save(CpuState2);
    }

    public void saveRecord(List<CpuState> recordList) {
        try {
            if (recordList.size() < 1) {
                return;
            }
            for (CpuState as : recordList) {
                as.setId(UUIDUtil.getUUID());
            }
            this.cpuStateMapper.insertList(recordList);
        }
        catch (Exception e) {
            logger.error("CpuState saveRecord error", (Throwable)e);
        }
    }

    public int deleteById(String[] id) throws Exception {
        return this.cpuStateMapper.deleteById(id);
    }

    public CpuState selectById(String id) throws Exception {
        return this.cpuStateMapper.selectById(id);
    }

    public CpuState selectMaxAvgByHostname(Map<String, Object> map) throws Exception {
        return this.cpuStateMapper.selectMaxAvgByHostname(map);
    }

    public Double selectMaxByDate(Map<String, Object> map) throws Exception {
        return this.cpuStateMapper.selectMaxByDate(map);
    }

    public List<CpuState> selectAllByParams(Map<String, Object> params) throws Exception {
        return this.cpuStateMapper.selectAllByParams(params);
    }

    public int deleteByDate(Map<String, Object> params) throws Exception {
        return this.cpuStateMapper.deleteByDate(params);
    }
}

