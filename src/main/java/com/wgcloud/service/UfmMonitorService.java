/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wgcloud.entity.UfmMonitor;
import com.wgcloud.mapper.UfmMonitorMapper;
import com.wgcloud.service.LogInfoService;
import com.wgcloud.util.HostUtil;
import com.wgcloud.util.UUIDUtil;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UfmMonitorService {
    @Autowired
    private UfmMonitorMapper ufmMonitorMapper;
    @Autowired
    private LogInfoService logInfoService;

    public PageInfo selectByParams(Map<String, Object> params, int currPage, int pageSize) throws Exception {
        PageHelper.startPage(currPage, pageSize);
        List<UfmMonitor> list = this.ufmMonitorMapper.selectByParams(params);
        PageInfo pageInfo = new PageInfo(list);
        return pageInfo;
    }

    public void save(UfmMonitor ufmMonitor) throws Exception {
        ufmMonitor.setId(UUIDUtil.getUUID());
        ufmMonitor.setCreateTime(new Date());
        this.ufmMonitorMapper.save(ufmMonitor);
    }

    public int deleteByGuid(String guid) throws Exception {
        return this.ufmMonitorMapper.deleteByGuid(guid);
    }

    public int deleteById(String[] id) throws Exception {
        return this.ufmMonitorMapper.deleteById(id);
    }

    public UfmMonitor selectById(String id) throws Exception {
        return this.ufmMonitorMapper.selectById(id);
    }

    public List<UfmMonitor> selectAllByParams(Map<String, Object> params) throws Exception {
        return this.ufmMonitorMapper.selectAllByParams(params);
    }

    public int deleteByDate(Map<String, Object> map) throws Exception {
        return this.ufmMonitorMapper.deleteByDate(map);
    }

    public int countByParams(Map<String, Object> params) throws Exception {
        return this.ufmMonitorMapper.countByParams(params);
    }

    public void saveLog(HttpServletRequest request, String action, UfmMonitor ufmMonitor) {
        if (null == ufmMonitor) {
            return;
        }
        this.logInfoService.save(HostUtil.getAccountByRequest(request).getAccount() + action + "UFM\u76d1\u63a7\u4fe1\u606f\uff1a" + ufmMonitor.getSystemName(), "\u6307\u5357\uff1a" + ufmMonitor.getGuid(), "2");
    }
}
