/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wgcloud.entity.KafkaMonitor;
import com.wgcloud.mapper.KafkaMonitorMapper;
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
public class KafkaMonitorService {
    @Autowired
    private KafkaMonitorMapper kafkaMonitorMapper;
    @Autowired
    private LogInfoService logInfoService;

    public PageInfo selectByParams(Map<String, Object> params, int currPage, int pageSize) throws Exception {
        PageHelper.startPage((int)currPage, (int)pageSize);
        List<KafkaMonitor> list = this.kafkaMonitorMapper.selectByParams(params);
        PageInfo pageInfo = new PageInfo(list);
        return pageInfo;
    }

    public void save(KafkaMonitor kafkaMonitor) throws Exception {
        kafkaMonitor.setId(UUIDUtil.getUUID());
        kafkaMonitor.setCreateTime(new Date());
        this.kafkaMonitorMapper.save(kafkaMonitor);
    }

    public void saveRecord(List<KafkaMonitor> recordList) throws Exception {
        if (recordList.size() < 1) {
            return;
        }
        for (KafkaMonitor as : recordList) {
            as.setId(UUIDUtil.getUUID());
        }
        this.kafkaMonitorMapper.insertList(recordList);
    }

    public int deleteByKafkaName(String kafkaName) throws Exception {
        return this.kafkaMonitorMapper.deleteByKafkaName(kafkaName);
    }

    public int downByKafkaName(String kafkaName) throws Exception {
        return this.kafkaMonitorMapper.downByKafkaName(kafkaName);
    }

    public int deleteById(String[] id) throws Exception {
        return this.kafkaMonitorMapper.deleteById(id);
    }

    public KafkaMonitor selectById(String id) throws Exception {
        return this.kafkaMonitorMapper.selectById(id);
    }

    public List<KafkaMonitor> selectAllByParams(Map<String, Object> params) throws Exception {
        return this.kafkaMonitorMapper.selectAllByParams(params);
    }

    public int deleteByDate(Map<String, Object> map) throws Exception {
        return this.kafkaMonitorMapper.deleteByDate(map);
    }

    public int countByParams(Map<String, Object> params) throws Exception {
        return this.kafkaMonitorMapper.countByParams(params);
    }

    public void saveLog(HttpServletRequest request, String action, KafkaMonitor kafkaMonitor) {
        if (null == kafkaMonitor) {
            return;
        }
        this.logInfoService.save(HostUtil.getAccountByRequest(request).getAccount() + action + "\u4e2d\u95f4\u4ef6Kafka\u76d1\u6d4b\u4fe1\u606f\uff1a" + kafkaMonitor.getKafkaName(), "\u5206\u7ec4\uff1a" + kafkaMonitor.getGroupId() + "\uff0c\u4e3b\u9898\uff1a" + kafkaMonitor.getTopicName() + "\uff0c\u5206\u7247\uff1a" + kafkaMonitor.getPartition(), "2");
    }
}

