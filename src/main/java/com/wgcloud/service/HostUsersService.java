/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wgcloud.entity.HostUsers;
import com.wgcloud.mapper.HostUsersMapper;
import com.wgcloud.util.UUIDUtil;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class HostUsersService {
    @Autowired
    private HostUsersMapper HostUsersMapper;

    public PageInfo selectByParams(Map<String, Object> params, int currPage, int pageSize) throws Exception {
        PageHelper.startPage((int)currPage, (int)pageSize);
        List<HostUsers> list = this.HostUsersMapper.selectByParams(params);
        PageInfo pageInfo = new PageInfo(list);
        return pageInfo;
    }

    public void save(HostUsers hostUsers) throws Exception {
        hostUsers.setId(UUIDUtil.getUUID());
        hostUsers.setCreateTime(new Date());
        this.HostUsersMapper.save(hostUsers);
    }

    @Transactional
    public void saveRecord(List<HostUsers> recordList) throws Exception {
        if (recordList.size() < 1) {
            return;
        }
        for (HostUsers as : recordList) {
            as.setId(UUIDUtil.getUUID());
        }
        this.HostUsersMapper.insertList(recordList);
    }

    public int deleteById(String[] id) throws Exception {
        return this.HostUsersMapper.deleteById(id);
    }

    public HostUsers selectById(String id) throws Exception {
        return this.HostUsersMapper.selectById(id);
    }

    public List<HostUsers> selectAllByParams(Map<String, Object> params) throws Exception {
        return this.HostUsersMapper.selectAllByParams(params);
    }

    public int deleteByAccHname(List<String> recordList) throws Exception {
        return this.HostUsersMapper.deleteByAccHname(recordList);
    }

    public int deleteByDate(Map<String, Object> map) throws Exception {
        return this.HostUsersMapper.deleteByDate(map);
    }
}

