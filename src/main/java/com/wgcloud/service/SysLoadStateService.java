/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wgcloud.entity.SysLoadState;
import com.wgcloud.mapper.SysLoadStateMapper;
import com.wgcloud.util.UUIDUtil;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysLoadStateService {
    private static final Logger logger = LoggerFactory.getLogger(SysLoadStateService.class);
    @Autowired
    private SysLoadStateMapper sysLoadStateMapper;

    public PageInfo selectByParams(Map<String, Object> params, int currPage, int pageSize) throws Exception {
        PageHelper.startPage((int)currPage, (int)pageSize);
        List<SysLoadState> list = this.sysLoadStateMapper.selectByParams(params);
        PageInfo pageInfo = new PageInfo(list);
        return pageInfo;
    }

    public void save(SysLoadState sysLoadState) throws Exception {
        sysLoadState.setId(UUIDUtil.getUUID());
        sysLoadState.setCreateTime(new Date());
        this.sysLoadStateMapper.save(sysLoadState);
    }

    public void saveRecord(List<SysLoadState> recordList) {
        try {
            if (recordList.size() < 1) {
                return;
            }
            for (SysLoadState as : recordList) {
                as.setId(UUIDUtil.getUUID());
            }
            this.sysLoadStateMapper.insertList(recordList);
        }
        catch (Exception e) {
            logger.error("SysLoadState saveRecord error", (Throwable)e);
        }
    }

    public int deleteById(String[] id) throws Exception {
        return this.sysLoadStateMapper.deleteById(id);
    }

    public SysLoadState selectById(String id) throws Exception {
        return this.sysLoadStateMapper.selectById(id);
    }

    public List<SysLoadState> selectAllByParams(Map<String, Object> params) throws Exception {
        return this.sysLoadStateMapper.selectAllByParams(params);
    }

    public SysLoadState selectMaxByHostname(Map<String, Object> map) throws Exception {
        return this.sysLoadStateMapper.selectMaxByHostname(map);
    }

    public SysLoadState selectAvgByHostname(Map<String, Object> map) throws Exception {
        return this.sysLoadStateMapper.selectAvgByHostname(map);
    }

    public SysLoadState selectMinByHostname(Map<String, Object> map) throws Exception {
        return this.sysLoadStateMapper.selectMinByHostname(map);
    }

    public SysLoadState selectMaxByDate(Map<String, Object> map) throws Exception {
        return this.sysLoadStateMapper.selectMaxByDate(map);
    }

    public int deleteByDate(Map<String, Object> map) throws Exception {
        return this.sysLoadStateMapper.deleteByDate(map);
    }
}

