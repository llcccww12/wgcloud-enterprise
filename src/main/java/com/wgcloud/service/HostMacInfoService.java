/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wgcloud.entity.HostMacInfo;
import com.wgcloud.mapper.HostMacInfoMapper;
import com.wgcloud.util.UUIDUtil;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class HostMacInfoService {
    @Autowired
    private HostMacInfoMapper hostMacInfoMapper;

    public PageInfo selectByParams(Map<String, Object> params, int currPage, int pageSize) throws Exception {
        PageHelper.startPage((int)currPage, (int)pageSize);
        List<HostMacInfo> list = this.hostMacInfoMapper.selectByParams(params);
        PageInfo pageInfo = new PageInfo(list);
        return pageInfo;
    }

    public void save(HostMacInfo hostMacInfo) throws Exception {
        hostMacInfo.setId(UUIDUtil.getUUID());
        hostMacInfo.setCreateTime(new Date());
        this.hostMacInfoMapper.save(hostMacInfo);
    }

    @Transactional
    public void saveRecord(List<HostMacInfo> recordList) throws Exception {
        if (recordList.size() < 1) {
            return;
        }
        for (HostMacInfo as : recordList) {
            as.setId(UUIDUtil.getUUID());
        }
        this.hostMacInfoMapper.insertList(recordList);
    }

    public int deleteById(String[] id) throws Exception {
        return this.hostMacInfoMapper.deleteById(id);
    }

    public HostMacInfo selectById(String id) throws Exception {
        return this.hostMacInfoMapper.selectById(id);
    }

    public List<HostMacInfo> selectAllByParams(Map<String, Object> params) throws Exception {
        List<HostMacInfo> list = this.hostMacInfoMapper.selectAllByParams(params);
        return list;
    }

    public int deleteByAccHname(List<String> recordList) throws Exception {
        return this.hostMacInfoMapper.deleteByAccHname(recordList);
    }

    public int deleteByDate(Map<String, Object> map) throws Exception {
        return this.hostMacInfoMapper.deleteByDate(map);
    }
}

