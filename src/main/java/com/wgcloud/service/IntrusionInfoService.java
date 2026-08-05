/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wgcloud.entity.IntrusionInfo;
import com.wgcloud.mapper.IntrusionInfoMapper;
import com.wgcloud.util.DateUtil;
import com.wgcloud.util.UUIDUtil;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IntrusionInfoService {
    @Autowired
    private IntrusionInfoMapper intrusionInfoMapper;

    public PageInfo selectByParams(Map<String, Object> params, int currPage, int pageSize) throws Exception {
        PageHelper.startPage((int)currPage, (int)pageSize);
        List<IntrusionInfo> list = this.intrusionInfoMapper.selectByParams(params);
        PageInfo pageInfo = new PageInfo(list);
        return pageInfo;
    }

    public void save(IntrusionInfo IntrusionInfo2) throws Exception {
        IntrusionInfo2.setId(UUIDUtil.getUUID());
        IntrusionInfo2.setCreateTime(DateUtil.getNowTime());
        this.intrusionInfoMapper.save(IntrusionInfo2);
    }

    public void saveRecord(List<IntrusionInfo> recordList) throws Exception {
        HashMap<String, Object> map = new HashMap<String, Object>();
        for (IntrusionInfo as : recordList) {
            as.setId(UUIDUtil.getUUID());
            map.put("hostname", as.getHostname());
            this.intrusionInfoMapper.deleteByAccHname(map);
        }
        this.intrusionInfoMapper.insertList(recordList);
    }

    public int deleteById(String[] id) throws Exception {
        return this.intrusionInfoMapper.deleteById(id);
    }

    public IntrusionInfo selectById(String id) throws Exception {
        return this.intrusionInfoMapper.selectById(id);
    }

    public List<IntrusionInfo> selectAllByParams(Map<String, Object> params) throws Exception {
        return this.intrusionInfoMapper.selectAllByParams(params);
    }

    public List<IntrusionInfo> selectByAccountId(String accountId) throws Exception {
        return this.intrusionInfoMapper.selectByAccountId(accountId);
    }
}

