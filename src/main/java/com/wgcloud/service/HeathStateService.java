/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wgcloud.dto.SubtitleDto;
import com.wgcloud.entity.HeathState;
import com.wgcloud.mapper.HeathStateMapper;
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
public class HeathStateService {
    private static final Logger logger = LoggerFactory.getLogger(HeathStateService.class);
    @Autowired
    private HeathStateMapper heathStateMapper;

    public PageInfo selectByParams(Map<String, Object> params, int currPage, int pageSize) throws Exception {
        PageHelper.startPage((int)currPage, (int)pageSize);
        List<HeathState> list = this.heathStateMapper.selectByParams(params);
        PageInfo pageInfo = new PageInfo(list);
        return pageInfo;
    }

    public void save(HeathState heathState) throws Exception {
        heathState.setId(UUIDUtil.getUUID());
        heathState.setCreateTime(new Date());
        this.heathStateMapper.save(heathState);
    }

    public void saveRecord(List<HeathState> recordList) {
        try {
            if (recordList.size() < 1) {
                return;
            }
            for (HeathState as : recordList) {
                as.setId(UUIDUtil.getUUID());
            }
            this.heathStateMapper.insertList(recordList);
        }
        catch (Exception e) {
            logger.error("HeathState saveRecord error", (Throwable)e);
        }
    }

    public int deleteById(String[] id) throws Exception {
        return this.heathStateMapper.deleteById(id);
    }

    public HeathState selectById(String id) throws Exception {
        return this.heathStateMapper.selectById(id);
    }

    public List<HeathState> selectAllByParams(Map<String, Object> params) throws Exception {
        return this.heathStateMapper.selectAllByParams(params);
    }

    public int deleteByDate(Map<String, Object> map) throws Exception {
        return this.heathStateMapper.deleteByDate(map);
    }

    public void setSubtitle(Model model, List<HeathState> heathStateList) {
        int maxValue = 0;
        int minValue = 20000;
        Double avgValue = 0.0;
        long sumValue = 0L;
        for (HeathState heathState : heathStateList) {
            if (null == heathState.getResTimes()) continue;
            if (heathState.getResTimes() > maxValue) {
                maxValue = heathState.getResTimes();
            }
            if (heathState.getResTimes() < minValue) {
                minValue = heathState.getResTimes();
            }
            sumValue += (long)heathState.getResTimes().intValue();
        }
        if (heathStateList.size() > 0) {
            avgValue = (double)sumValue / (double)heathStateList.size();
        } else {
            minValue = 0;
        }
        SubtitleDto heathStateSubtitleDto = new SubtitleDto();
        heathStateSubtitleDto.setAvgValue(FormatUtil.formatDouble(avgValue, 2) + "");
        heathStateSubtitleDto.setMaxValue(maxValue + "");
        heathStateSubtitleDto.setMinValue(minValue + "");
        model.addAttribute("heathStateSubtitleDto", (Object)heathStateSubtitleDto);
    }
}

