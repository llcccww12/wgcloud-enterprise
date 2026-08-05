/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wgcloud.entity.ShellState;
import com.wgcloud.mapper.ShellStateMapper;
import com.wgcloud.util.UUIDUtil;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ShellStateService {
    @Autowired
    private ShellStateMapper shellStateMapper;

    public PageInfo selectByParams(Map<String, Object> params, int currPage, int pageSize) throws Exception {
        PageHelper.startPage((int)currPage, (int)pageSize);
        List<ShellState> list = this.shellStateMapper.selectByParams(params);
        PageInfo pageInfo = new PageInfo(list);
        return pageInfo;
    }

    public void save(ShellState shellState) throws Exception {
        shellState.setId(UUIDUtil.getUUID());
        shellState.setCreateTime(new Date());
        this.shellStateMapper.save(shellState);
    }

    public void updateById(ShellState ShellState2) throws Exception {
        this.shellStateMapper.updateById(ShellState2);
    }

    public void saveRecord(List<ShellState> recordList) throws Exception {
        if (recordList.size() < 1) {
            return;
        }
        for (ShellState as : recordList) {
            as.setId(UUIDUtil.getUUID());
        }
        this.shellStateMapper.insertList(recordList);
    }

    public int deleteByShellId(String shellId) throws Exception {
        return this.shellStateMapper.deleteByShellId(shellId);
    }

    @Transactional
    public int updateSendByIds(String[] id) throws Exception {
        return this.shellStateMapper.updateSendByIds(id);
    }

    public int countByParams(Map<String, Object> params) throws Exception {
        return this.shellStateMapper.countByParams(params);
    }

    public int deleteById(String[] id) throws Exception {
        return this.shellStateMapper.deleteById(id);
    }

    public ShellState selectById(String id) throws Exception {
        return this.shellStateMapper.selectById(id);
    }

    public List<ShellState> selectAllByParams(Map<String, Object> params) throws Exception {
        return this.shellStateMapper.selectAllByParams(params);
    }

    public int deleteByDate(Map<String, Object> map) throws Exception {
        return this.shellStateMapper.deleteByDate(map);
    }
}

