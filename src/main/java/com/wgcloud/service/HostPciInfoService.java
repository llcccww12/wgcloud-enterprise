/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wgcloud.entity.HostPciInfo;
import com.wgcloud.mapper.HostPciInfoMapper;
import com.wgcloud.util.UUIDUtil;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class HostPciInfoService {
    @Autowired
    private HostPciInfoMapper hostPciInfoMapper;

    public PageInfo selectByParams(Map<String, Object> params, int currPage, int pageSize) throws Exception {
        PageHelper.startPage((int)currPage, (int)pageSize);
        List<HostPciInfo> list = this.hostPciInfoMapper.selectByParams(params);
        PageInfo pageInfo = new PageInfo(list);
        return pageInfo;
    }

    public void save(HostPciInfo hostPciInfo) throws Exception {
        hostPciInfo.setId(UUIDUtil.getUUID());
        hostPciInfo.setCreateTime(new Date());
        this.hostPciInfoMapper.save(hostPciInfo);
    }

    @Transactional
    public void saveRecord(List<HostPciInfo> recordList) throws Exception {
        if (recordList.size() < 1) {
            return;
        }
        for (HostPciInfo as : recordList) {
            as.setId(UUIDUtil.getUUID());
        }
        this.hostPciInfoMapper.insertList(recordList);
    }

    public int deleteById(String[] id) throws Exception {
        return this.hostPciInfoMapper.deleteById(id);
    }

    public HostPciInfo selectById(String id) throws Exception {
        return this.hostPciInfoMapper.selectById(id);
    }

    public List<HostPciInfo> selectAllByParams(Map<String, Object> params) throws Exception {
        return this.hostPciInfoMapper.selectAllByParams(params);
    }

    public int deleteByHostname(List<String> recordList) throws Exception {
        return this.hostPciInfoMapper.deleteByHostname(recordList);
    }

    public int deleteByDate(Map<String, Object> map) throws Exception {
        return this.hostPciInfoMapper.deleteByDate(map);
    }
}

