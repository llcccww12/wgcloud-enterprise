/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wgcloud.entity.ShellNoteInfo;
import com.wgcloud.mapper.ShellNoteInfoMapper;
import com.wgcloud.service.LogInfoService;
import com.wgcloud.util.HostUtil;
import com.wgcloud.util.UUIDUtil;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ShellNoteInfoService {
    @Autowired
    private ShellNoteInfoMapper shellNoteInfoMapper;
    @Autowired
    private LogInfoService logInfoService;

    public PageInfo<ShellNoteInfo> selectByParams(Map<String, Object> params, int currPage, int pageSize) throws Exception {
        PageHelper.startPage((int)currPage, (int)pageSize);
        List<ShellNoteInfo> list = this.shellNoteInfoMapper.selectByParams(params);
        PageInfo pageInfo = new PageInfo(list);
        return pageInfo;
    }

    public void save(ShellNoteInfo shellNoteInfo) throws Exception {
        shellNoteInfo.setId(UUIDUtil.getUUID());
        shellNoteInfo.setCreateTime(new Date());
        this.shellNoteInfoMapper.save(shellNoteInfo);
    }

    @Transactional
    public int deleteById(String[] id) throws Exception {
        return this.shellNoteInfoMapper.deleteById(id);
    }

    public void updateById(ShellNoteInfo shellNoteInfo) throws Exception {
        this.shellNoteInfoMapper.updateById(shellNoteInfo);
    }

    public ShellNoteInfo selectById(String id) throws Exception {
        return this.shellNoteInfoMapper.selectById(id);
    }

    public int countByParams(Map<String, Object> params) throws Exception {
        return this.shellNoteInfoMapper.countByParams(params);
    }

    public void updateToTargetAccount(Map<String, Object> params) throws Exception {
        this.shellNoteInfoMapper.updateToTargetAccount(params);
    }

    public void saveLog(HttpServletRequest request, String action, ShellNoteInfo shellNoteInfo) {
        if (null == shellNoteInfo) {
            return;
        }
        this.logInfoService.save(HostUtil.getAccountByRequest(request).getAccount() + action + "\u5de5\u4f5c\u7b14\u8bb0\uff1a" + shellNoteInfo.getShellTitle(), "\u5de5\u4f5c\u7b14\u8bb0\uff1a" + shellNoteInfo.getShellTitle(), "2");
    }
}

