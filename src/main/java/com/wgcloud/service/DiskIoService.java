/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wgcloud.entity.DiskIo;
import com.wgcloud.mapper.DiskIoMapper;
import com.wgcloud.util.UUIDUtil;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DiskIoService {
    @Autowired
    private DiskIoMapper diskIoMapper;

    public PageInfo selectByParams(Map<String, Object> params, int currPage, int pageSize) throws Exception {
        PageHelper.startPage((int)currPage, (int)pageSize);
        List<DiskIo> list = this.diskIoMapper.selectByParams(params);
        PageInfo pageInfo = new PageInfo(list);
        return pageInfo;
    }

    public void save(DiskIo diskIo) throws Exception {
        diskIo.setId(UUIDUtil.getUUID());
        diskIo.setCreateTime(new Date());
        this.diskIoMapper.save(diskIo);
    }

    @Transactional
    public void saveRecord(List<DiskIo> recordList) throws Exception {
        if (recordList.size() < 1) {
            return;
        }
        for (DiskIo as : recordList) {
            as.setId(UUIDUtil.getUUID());
        }
        this.diskIoMapper.insertList(recordList);
    }

    public int deleteById(String[] id) throws Exception {
        return this.diskIoMapper.deleteById(id);
    }

    public DiskIo selectById(String id) throws Exception {
        return this.diskIoMapper.selectById(id);
    }

    public List<DiskIo> selectAllByParams(Map<String, Object> params) throws Exception {
        return this.diskIoMapper.selectAllByParams(params);
    }

    public int deleteByAccHname(List<String> recordList) throws Exception {
        return this.diskIoMapper.deleteByAccHname(recordList);
    }

    public int deleteByDate(Map<String, Object> map) throws Exception {
        return this.diskIoMapper.deleteByDate(map);
    }
}

