/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wgcloud.entity.ReportInstance;
import com.wgcloud.mapper.ReportInstanceMapper;
import com.wgcloud.util.UUIDUtil;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReportInstanceService {
    private static final Logger logger = LoggerFactory.getLogger(ReportInstanceService.class);
    @Autowired
    private ReportInstanceMapper reportInstanceMapper;

    public PageInfo selectByParams(Map<String, Object> params, int currPage, int pageSize) throws Exception {
        PageHelper.startPage((int)currPage, (int)pageSize);
        List<ReportInstance> list = this.reportInstanceMapper.selectByParams(params);
        PageInfo pageInfo = new PageInfo(list);
        return pageInfo;
    }

    public void saveRecord(List<ReportInstance> recordList, Date date) throws Exception {
        if (recordList.size() < 1) {
            return;
        }
        try {
            for (ReportInstance as : recordList) {
                as.setId(UUIDUtil.getUUID());
                as.setCreateTime(date);
            }
            this.reportInstanceMapper.insertList(recordList);
        }
        catch (Exception e) {
            logger.error("\u6279\u91cf\u4fdd\u5b58\u5de1\u68c0\u62a5\u544a\u4ece\u8868\u4fe1\u606f\u5f02\u5e38\uff1a", (Throwable)e);
        }
    }

    public void save(ReportInstance reportInstance) {
        try {
            this.reportInstanceMapper.save(reportInstance);
        }
        catch (Exception e) {
            logger.error("\u4fdd\u5b58\u5de1\u68c0\u62a5\u544a\u4ece\u8868\u4fe1\u606f\u5f02\u5e38\uff1a", (Throwable)e);
        }
    }

    public int countByParams(Map<String, Object> params) throws Exception {
        return this.reportInstanceMapper.countByParams(params);
    }

    public int deleteById(String[] id) throws Exception {
        return this.reportInstanceMapper.deleteById(id);
    }

    public ReportInstance selectById(String id) throws Exception {
        return this.reportInstanceMapper.selectById(id);
    }

    public List<ReportInstance> selectAllByParams(Map<String, Object> params) throws Exception {
        return this.reportInstanceMapper.selectAllByParams(params);
    }

    public int deleteByDate(Map<String, Object> map) throws Exception {
        return this.reportInstanceMapper.deleteByDate(map);
    }

    public void mergeReportInsToList(List<ReportInstance> reportInstanceList, String infoKey, String infoContent, String reportInfoId) {
        ReportInstance reportIns = new ReportInstance();
        reportIns.setInfoKey(infoKey);
        reportIns.setInfoContent(infoContent);
        reportIns.setReportInfoId(reportInfoId);
        reportInstanceList.add(reportIns);
    }
}

