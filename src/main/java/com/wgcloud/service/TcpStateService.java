/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wgcloud.entity.TcpState;
import com.wgcloud.mapper.TcpStateMapper;
import com.wgcloud.util.DateUtil;
import com.wgcloud.util.UUIDUtil;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TcpStateService {
    @Autowired
    private TcpStateMapper tcpStateMapper;

    public PageInfo selectByParams(Map<String, Object> params, int currPage, int pageSize) throws Exception {
        PageHelper.startPage((int)currPage, (int)pageSize);
        List<TcpState> list = this.tcpStateMapper.selectByParams(params);
        PageInfo pageInfo = new PageInfo(list);
        return pageInfo;
    }

    public void save(TcpState TcpState2) throws Exception {
        TcpState2.setId(UUIDUtil.getUUID());
        TcpState2.setCreateTime(DateUtil.getNowTime());
        TcpState2.setDateStr(DateUtil.getDateTimeString(TcpState2.getCreateTime()));
        this.tcpStateMapper.save(TcpState2);
    }

    public void saveRecord(List<TcpState> recordList) throws Exception {
        if (recordList.size() < 1) {
            return;
        }
        for (TcpState as : recordList) {
            as.setId(UUIDUtil.getUUID());
            as.setDateStr(DateUtil.getDateTimeString(as.getCreateTime()));
        }
        this.tcpStateMapper.insertList(recordList);
    }

    public int deleteById(String[] id) throws Exception {
        return this.tcpStateMapper.deleteById(id);
    }

    public TcpState selectById(String id) throws Exception {
        return this.tcpStateMapper.selectById(id);
    }

    public List<TcpState> selectAllByParams(Map<String, Object> params) throws Exception {
        return this.tcpStateMapper.selectAllByParams(params);
    }
}

