/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wgcloud.entity.NetIoState;
import com.wgcloud.mapper.NetIoStateMapper;
import com.wgcloud.util.UUIDUtil;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NetIoStateService {
    private static final Logger logger = LoggerFactory.getLogger(NetIoStateService.class);
    @Autowired
    private NetIoStateMapper netIoStateMapper;

    public PageInfo selectByParams(Map<String, Object> params, int currPage, int pageSize) throws Exception {
        PageHelper.startPage((int)currPage, (int)pageSize);
        List<NetIoState> list = this.netIoStateMapper.selectByParams(params);
        PageInfo pageInfo = new PageInfo(list);
        return pageInfo;
    }

    public PageInfo selectTop3(Map<String, Object> params, int currPage, int pageSize) throws Exception {
        PageHelper.startPage((int)currPage, (int)pageSize);
        List<NetIoState> list = this.netIoStateMapper.selectTop3(params);
        PageInfo pageInfo = new PageInfo(list);
        return pageInfo;
    }

    public void save(NetIoState netIoState) throws Exception {
        netIoState.setId(UUIDUtil.getUUID());
        netIoState.setCreateTime(new Date());
        this.netIoStateMapper.save(netIoState);
    }

    public void saveRecord(List<NetIoState> recordList) {
        try {
            if (recordList.size() < 1) {
                return;
            }
            for (NetIoState as : recordList) {
                as.setId(UUIDUtil.getUUID());
            }
            this.netIoStateMapper.insertList(recordList);
        }
        catch (Exception e) {
            logger.error("NetIoState saveRecord error", (Throwable)e);
        }
    }

    public int deleteById(String[] id) throws Exception {
        return this.netIoStateMapper.deleteById(id);
    }

    public NetIoState selectById(String id) throws Exception {
        return this.netIoStateMapper.selectById(id);
    }

    public List<NetIoState> selectAllByParams(Map<String, Object> params) throws Exception {
        return this.netIoStateMapper.selectAllByParams(params);
    }

    public NetIoState selectMaxByHostname(Map<String, Object> params) throws Exception {
        return this.netIoStateMapper.selectMaxByHostname(params);
    }

    public NetIoState selectAvgByHostname(Map<String, Object> params) throws Exception {
        return this.netIoStateMapper.selectAvgByHostname(params);
    }

    public NetIoState selectMinByHostname(Map<String, Object> params) throws Exception {
        return this.netIoStateMapper.selectMinByHostname(params);
    }

    public NetIoState selectMaxByDate(Map<String, Object> map) throws Exception {
        return this.netIoStateMapper.selectMaxByDate(map);
    }

    public int deleteByDate(Map<String, Object> map) throws Exception {
        return this.netIoStateMapper.deleteByDate(map);
    }
}

