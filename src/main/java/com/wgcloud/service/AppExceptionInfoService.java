/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wgcloud.entity.AppExceptionInfo;
import com.wgcloud.mapper.AppExceptionInfoMapper;
import com.wgcloud.service.HostGroupService;
import com.wgcloud.service.LogInfoService;
import com.wgcloud.util.HostUtil;
import com.wgcloud.util.UUIDUtil;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AppExceptionInfoService {
    private static final Logger logger = LoggerFactory.getLogger(AppExceptionInfoService.class);
    @Autowired
    private AppExceptionInfoMapper appExceptionInfoMapper;
    @Resource
    private HostGroupService hostGroupService;
    @Autowired
    private LogInfoService logInfoService;

    public PageInfo selectByParams(Map<String, Object> params, int currPage, int pageSize) throws Exception {
        PageHelper.startPage((int)currPage, (int)pageSize);
        List<AppExceptionInfo> list = this.appExceptionInfoMapper.selectByParams(params);
        PageInfo pageInfo = new PageInfo(list);
        return pageInfo;
    }

    public void save(AppExceptionInfo appExceptionInfo, HttpServletRequest request) throws Exception {
        appExceptionInfo.setId(UUIDUtil.getUUID());
        this.appExceptionInfoMapper.save(appExceptionInfo);
    }

    public int deleteByHostName(List<String> recordList) throws Exception {
        return this.appExceptionInfoMapper.deleteByHostName(recordList);
    }

    public int deleteByDate(Map<String, Object> map) throws Exception {
        return this.appExceptionInfoMapper.deleteByDate(map);
    }

    @Transactional
    public void saveRecord(List<AppExceptionInfo> recordList) throws Exception {
        if (recordList.size() < 1) {
            return;
        }
        for (AppExceptionInfo as : recordList) {
            as.setId(UUIDUtil.getUUID());
        }
        this.appExceptionInfoMapper.insertList(recordList);
    }

    public void updateById(AppExceptionInfo appExceptionInfo) throws Exception {
        this.appExceptionInfoMapper.updateById(appExceptionInfo);
    }

    public int countByParams(Map<String, Object> params) throws Exception {
        return this.appExceptionInfoMapper.countByParams(params);
    }

    @Transactional
    public int deleteById(String[] id) throws Exception {
        return this.appExceptionInfoMapper.deleteById(id);
    }

    public AppExceptionInfo selectById(String id) throws Exception {
        return this.appExceptionInfoMapper.selectById(id);
    }

    public List<AppExceptionInfo> selectAllByParams(Map<String, Object> params) throws Exception {
        return this.appExceptionInfoMapper.selectAllByParams(params);
    }

    public void saveLog(HttpServletRequest request, String action, AppExceptionInfo appExceptionInfo) {
        if (null == appExceptionInfo) {
            return;
        }
        this.logInfoService.save(HostUtil.getAccountByRequest(request).getAccount() + action + "\u5f02\u5e38\u8fdb\u7a0b\u76d1\u6d4b\u4fe1\u606f\uff1a" + appExceptionInfo.getHostname() + "\uff0c" + appExceptionInfo.getAppName(), "\u8fdb\u7a0bID\uff1a" + appExceptionInfo.getGatherPid(), "2");
    }
}

