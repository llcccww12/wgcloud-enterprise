/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wgcloud.entity.FileWarnInfo;
import com.wgcloud.mapper.FileWarnInfoMapper;
import com.wgcloud.mapper.FileWarnStateMapper;
import com.wgcloud.service.LogInfoService;
import com.wgcloud.util.DateUtil;
import com.wgcloud.util.HostUtil;
import com.wgcloud.util.UUIDUtil;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FileWarnInfoService {
    @Autowired
    private FileWarnInfoMapper fileWarnInfoMapper;
    @Autowired
    private FileWarnStateMapper fileWarnStateMapper;
    @Autowired
    private LogInfoService logInfoService;

    public PageInfo selectByParams(Map<String, Object> params, int currPage, int pageSize) throws Exception {
        PageHelper.startPage((int)currPage, (int)pageSize);
        List<FileWarnInfo> list = this.fileWarnInfoMapper.selectByParams(params);
        PageInfo pageInfo = new PageInfo(list);
        return pageInfo;
    }

    public void save(FileWarnInfo fileWarnInfo) throws Exception {
        fileWarnInfo.setId(UUIDUtil.getUUID());
        fileWarnInfo.setCreateTime(DateUtil.getNowTime());
        if (!StringUtils.isEmpty((CharSequence)fileWarnInfo.getFilePath())) {
            fileWarnInfo.setFilePath(fileWarnInfo.getFilePath().trim());
        }
        this.fileWarnInfoMapper.save(fileWarnInfo);
    }

    public int updateActive(Map<String, Object> params) throws Exception {
        return this.fileWarnInfoMapper.updateActive(params);
    }

    public void saveLog(HttpServletRequest request, String action, FileWarnInfo fileWarnInfo) {
        if (null == fileWarnInfo) {
            return;
        }
        this.logInfoService.save(HostUtil.getAccountByRequest(request).getAccount() + action + "\u65e5\u5fd7\u76d1\u63a7\uff1a" + fileWarnInfo.getHostname() + "\uff0c" + fileWarnInfo.getFilePath(), "\u6587\u4ef6\u8def\u5f84\uff1a" + fileWarnInfo.getFilePath(), "2");
    }

    public int deleteByHostName(Map<String, Object> map) throws Exception {
        return this.fileWarnInfoMapper.deleteByHostName(map);
    }

    @Transactional
    public void saveRecord(List<FileWarnInfo> recordList) throws Exception {
        if (recordList.size() < 1) {
            return;
        }
        for (FileWarnInfo as : recordList) {
            as.setId(UUIDUtil.getUUID());
        }
        this.fileWarnInfoMapper.insertList(recordList);
    }

    public Integer countByParams(Map<String, Object> params) throws Exception {
        return this.fileWarnInfoMapper.countByParams(params);
    }

    @Transactional
    public int deleteById(String[] id) throws Exception {
        return this.fileWarnInfoMapper.deleteById(id);
    }

    @Transactional
    public void updateRecord(List<FileWarnInfo> recordList) throws Exception {
        if (recordList.size() < 1) {
            return;
        }
        this.fileWarnInfoMapper.updateList(recordList);
    }

    public void updateById(FileWarnInfo FileWarnInfo2) throws Exception {
        this.fileWarnInfoMapper.updateById(FileWarnInfo2);
    }

    public FileWarnInfo selectById(String id) throws Exception {
        return this.fileWarnInfoMapper.selectById(id);
    }

    public List<FileWarnInfo> selectAllByParams(Map<String, Object> params) throws Exception {
        return this.fileWarnInfoMapper.selectAllByParams(params);
    }
}

