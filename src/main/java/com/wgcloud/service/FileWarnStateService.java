/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wgcloud.entity.FileWarnState;
import com.wgcloud.mapper.FileWarnStateMapper;
import com.wgcloud.util.UUIDUtil;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FileWarnStateService {
    private Logger logger = LoggerFactory.getLogger(FileWarnStateService.class);
    @Autowired
    private FileWarnStateMapper fileWarnStateMapper;

    public PageInfo selectByParams(Map<String, Object> params, int currPage, int pageSize) throws Exception {
        PageHelper.startPage((int)currPage, (int)pageSize);
        List<FileWarnState> list = this.fileWarnStateMapper.selectByParams(params);
        PageInfo pageInfo = new PageInfo(list);
        return pageInfo;
    }

    public Integer countByParams(Map<String, Object> params) throws Exception {
        return this.fileWarnStateMapper.countByParams(params);
    }

    public void save(FileWarnState fileWarnState) throws Exception {
        fileWarnState.setId(UUIDUtil.getUUID());
        fileWarnState.setCreateTime(new Date());
        this.fileWarnStateMapper.save(fileWarnState);
    }

    public void saveRecord(List<FileWarnState> recordList) {
        try {
            if (recordList.size() < 1) {
                return;
            }
            for (FileWarnState as : recordList) {
                as.setId(UUIDUtil.getUUID());
            }
            this.fileWarnStateMapper.insertList(recordList);
        }
        catch (Exception e) {
            this.logger.error("\u65e5\u5fd7\u6587\u4ef6\u76d1\u63a7\u4fdd\u5b58\u9519\u8bef", (Throwable)e);
        }
    }

    public int deleteByFileWarnId(String fileWarnId) throws Exception {
        return this.fileWarnStateMapper.deleteByFileWarnId(fileWarnId);
    }

    public int deleteById(String[] id) throws Exception {
        return this.fileWarnStateMapper.deleteById(id);
    }

    public FileWarnState selectById(String id) throws Exception {
        return this.fileWarnStateMapper.selectById(id);
    }

    public List<FileWarnState> selectAllByParams(Map<String, Object> params) throws Exception {
        return this.fileWarnStateMapper.selectAllByParams(params);
    }

    public int deleteByDate(Map<String, Object> map) throws Exception {
        return this.fileWarnStateMapper.deleteByDate(map);
    }
}

