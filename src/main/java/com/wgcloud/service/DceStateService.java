/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wgcloud.dto.SubtitleDto;
import com.wgcloud.entity.DceState;
import com.wgcloud.mapper.DceStateMapper;
import com.wgcloud.util.FormatUtil;
import com.wgcloud.util.UUIDUtil;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

@Service
public class DceStateService {
    private static final Logger logger = LoggerFactory.getLogger(DceStateService.class);
    @Autowired
    private DceStateMapper dceStateMapper;

    public PageInfo selectByParams(Map<String, Object> params, int currPage, int pageSize) throws Exception {
        PageHelper.startPage((int)currPage, (int)pageSize);
        List<DceState> list = this.dceStateMapper.selectByParams(params);
        PageInfo pageInfo = new PageInfo(list);
        return pageInfo;
    }

    public void save(DceState DceState2) throws Exception {
        DceState2.setId(UUIDUtil.getUUID());
        DceState2.setCreateTime(new Date());
        this.dceStateMapper.save(DceState2);
    }

    public void saveRecord(List<DceState> recordList) {
        try {
            if (recordList.size() < 1) {
                return;
            }
            for (DceState as : recordList) {
                as.setId(UUIDUtil.getUUID());
            }
            this.dceStateMapper.insertList(recordList);
        }
        catch (Exception e) {
            logger.error("DceState saveRecord error", (Throwable)e);
        }
    }

    public int deleteById(String[] id) throws Exception {
        return this.dceStateMapper.deleteById(id);
    }

    public DceState selectById(String id) throws Exception {
        return this.dceStateMapper.selectById(id);
    }

    public List<DceState> selectAllByParams(Map<String, Object> params) throws Exception {
        return this.dceStateMapper.selectAllByParams(params);
    }

    public int deleteByDate(Map<String, Object> map) throws Exception {
        return this.dceStateMapper.deleteByDate(map);
    }

    public void setSubtitle(Model model, List<DceState> dceStateList) {
        int maxValue = 0;
        int minValue = 20000;
        Double avgValue = 0.0;
        long sumValue = 0L;
        for (DceState dceState : dceStateList) {
            if (null == dceState.getResTimes()) continue;
            if (dceState.getResTimes() > maxValue) {
                maxValue = dceState.getResTimes();
            }
            if (dceState.getResTimes() < minValue) {
                minValue = dceState.getResTimes();
            }
            sumValue += (long)dceState.getResTimes().intValue();
        }
        if (dceStateList.size() > 0) {
            avgValue = (double)sumValue / (double)dceStateList.size();
        } else {
            minValue = 0;
        }
        SubtitleDto dceStateSubtitleDto = new SubtitleDto();
        dceStateSubtitleDto.setAvgValue(FormatUtil.formatDouble(avgValue, 2) + "");
        dceStateSubtitleDto.setMaxValue(maxValue + "");
        dceStateSubtitleDto.setMinValue(minValue + "");
        model.addAttribute("dceStateSubtitleDto", (Object)dceStateSubtitleDto);
    }
}

