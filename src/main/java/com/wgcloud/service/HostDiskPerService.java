/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wgcloud.dto.SubtitleDto;
import com.wgcloud.entity.HostDiskPer;
import com.wgcloud.mapper.HostDiskPerMapper;
import com.wgcloud.util.FormatUtil;
import com.wgcloud.util.UUIDUtil;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

@Service
public class HostDiskPerService {
    @Autowired
    private HostDiskPerMapper hostDiskPerMapper;

    public PageInfo selectByParams(Map<String, Object> params, int currPage, int pageSize) throws Exception {
        PageHelper.startPage((int)currPage, (int)pageSize);
        List<HostDiskPer> list = this.hostDiskPerMapper.selectByParams(params);
        PageInfo pageInfo = new PageInfo(list);
        return pageInfo;
    }

    public void save(HostDiskPer HostDiskPer2) throws Exception {
        HostDiskPer2.setId(UUIDUtil.getUUID());
        HostDiskPer2.setCreateTime(new Date());
        this.hostDiskPerMapper.save(HostDiskPer2);
    }

    @Transactional
    public void saveRecord(List<HostDiskPer> recordList) throws Exception {
        if (recordList.size() < 1) {
            return;
        }
        for (HostDiskPer as : recordList) {
            as.setId(UUIDUtil.getUUID());
        }
        this.hostDiskPerMapper.insertList(recordList);
    }

    public int deleteById(String[] id) throws Exception {
        return this.hostDiskPerMapper.deleteById(id);
    }

    public HostDiskPer selectById(String id) throws Exception {
        return this.hostDiskPerMapper.selectById(id);
    }

    public List<HostDiskPer> selectAllByParams(Map<String, Object> params) throws Exception {
        return this.hostDiskPerMapper.selectAllByParams(params);
    }

    public int deleteByAccHname(List<String> recordList) throws Exception {
        return this.hostDiskPerMapper.deleteByAccHname(recordList);
    }

    public int deleteByDate(Map<String, Object> map) throws Exception {
        return this.hostDiskPerMapper.deleteByDate(map);
    }

    public void setSubtitle(Model model, List<HostDiskPer> hostDiskPerList) {
        Double maxDiskPer = 0.0;
        Double minDiskPer = 1000.0;
        Double avgDiskPer = 0.0;
        Double sumDiskPer = 0.0;
        for (HostDiskPer hostDiskPer : hostDiskPerList) {
            if (null == hostDiskPer.getDiskSumPer()) continue;
            if (hostDiskPer.getDiskSumPer() > maxDiskPer) {
                maxDiskPer = hostDiskPer.getDiskSumPer();
            }
            if (hostDiskPer.getDiskSumPer() < minDiskPer) {
                minDiskPer = hostDiskPer.getDiskSumPer();
            }
            sumDiskPer = sumDiskPer + hostDiskPer.getDiskSumPer();
        }
        if (hostDiskPerList.size() > 0) {
            avgDiskPer = sumDiskPer / (double)hostDiskPerList.size();
        } else {
            minDiskPer = 0.0;
        }
        SubtitleDto hostDiskPerSubtitleDto = new SubtitleDto();
        hostDiskPerSubtitleDto.setAvgValue(FormatUtil.formatDouble(avgDiskPer, 2) + "");
        hostDiskPerSubtitleDto.setMaxValue(maxDiskPer + "");
        hostDiskPerSubtitleDto.setMinValue(minDiskPer + "");
        model.addAttribute("hostDiskPerSubtitleDto", (Object)hostDiskPerSubtitleDto);
    }
}

