/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wgcloud.entity.K8sMonitor;
import com.wgcloud.mapper.K8sMonitorMapper;
import com.wgcloud.util.UUIDUtil;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class K8sMonitorService {
    @Autowired
    private K8sMonitorMapper k8sMonitorMapper;

    public PageInfo selectByParams(Map<String, Object> params, int currPage, int pageSize) throws Exception {
        PageHelper.startPage((int)currPage, (int)pageSize);
        List<K8sMonitor> list = this.k8sMonitorMapper.selectByParams(params);
        PageInfo pageInfo = new PageInfo(list);
        return pageInfo;
    }

    public void save(K8sMonitor K8sMonitor2) throws Exception {
        K8sMonitor2.setId(UUIDUtil.getUUID());
        K8sMonitor2.setCreateTime(new Date());
        this.k8sMonitorMapper.save(K8sMonitor2);
    }

    public void saveRecord(List<K8sMonitor> recordList) throws Exception {
        if (recordList.size() < 1) {
            return;
        }
        for (K8sMonitor as : recordList) {
            as.setId(UUIDUtil.getUUID());
        }
        this.k8sMonitorMapper.insertList(recordList);
    }

    public int deleteByK8sName(String k8sName) throws Exception {
        return this.k8sMonitorMapper.deleteByK8sName(k8sName);
    }

    public int deleteById(String[] id) throws Exception {
        return this.k8sMonitorMapper.deleteById(id);
    }

    public K8sMonitor selectById(String id) throws Exception {
        return this.k8sMonitorMapper.selectById(id);
    }

    public List<K8sMonitor> selectAllByParams(Map<String, Object> params) throws Exception {
        return this.k8sMonitorMapper.selectAllByParams(params);
    }

    public int deleteByDate(Map<String, Object> map) throws Exception {
        return this.k8sMonitorMapper.deleteByDate(map);
    }

    public int countByParams(Map<String, Object> params) throws Exception {
        return this.k8sMonitorMapper.countByParams(params);
    }
}

