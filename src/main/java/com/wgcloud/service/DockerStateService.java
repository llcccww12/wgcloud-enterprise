/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wgcloud.dto.SubtitleDto;
import com.wgcloud.entity.DockerState;
import com.wgcloud.mapper.DockerStateMapper;
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
public class DockerStateService {
    private static final Logger logger = LoggerFactory.getLogger(DockerStateService.class);
    @Autowired
    private DockerStateMapper dockerStateMapper;

    public PageInfo selectByParams(Map<String, Object> params, int currPage, int pageSize) throws Exception {
        PageHelper.startPage((int)currPage, (int)pageSize);
        List<DockerState> list = this.dockerStateMapper.selectByParams(params);
        PageInfo pageInfo = new PageInfo(list);
        return pageInfo;
    }

    public void save(DockerState dockerState) throws Exception {
        dockerState.setId(UUIDUtil.getUUID());
        dockerState.setCreateTime(new Date());
        this.dockerStateMapper.save(dockerState);
    }

    public void saveRecord(List<DockerState> recordList) {
        try {
            if (recordList.size() < 1) {
                return;
            }
            for (DockerState dockerState : recordList) {
                dockerState.setId(UUIDUtil.getUUID());
                if (null != dockerState.getCpuPer()) continue;
                dockerState.setCpuPer(0.0);
            }
            this.dockerStateMapper.insertList(recordList);
        }
        catch (Exception e) {
            logger.error("DockerState saveRecord error", (Throwable)e);
        }
    }

    public int deleteByAppInfoId(String appInfoId) throws Exception {
        return this.dockerStateMapper.deleteByDockerInfoId(appInfoId);
    }

    public int deleteById(String[] id) throws Exception {
        return this.dockerStateMapper.deleteById(id);
    }

    public DockerState selectById(String id) throws Exception {
        return this.dockerStateMapper.selectById(id);
    }

    public List<DockerState> selectAllByParams(Map<String, Object> params) throws Exception {
        return this.dockerStateMapper.selectAllByParams(params);
    }

    public int deleteByDate(Map<String, Object> map) throws Exception {
        return this.dockerStateMapper.deleteByDate(map);
    }

    public void setSubtitle(Model model, List<DockerState> dockerStateList) {
        Double maxMem = 0.0;
        Double minMem = 100000.0;
        Double avgMem = 0.0;
        Double sumMem = 0.0;
        Double maxCpu = 0.0;
        Double minCpu = 100000.0;
        Double avgCpu = 0.0;
        Double sumCpu = 0.0;
        for (DockerState dockerState : dockerStateList) {
            if (null != dockerState.getMemPer()) {
                if (dockerState.getMemPer() > maxMem) {
                    maxMem = dockerState.getMemPer();
                }
                if (dockerState.getMemPer() < minMem) {
                    minMem = dockerState.getMemPer();
                }
                sumMem = sumMem + dockerState.getMemPer();
            }
            if (null == dockerState.getCpuPer()) continue;
            if (dockerState.getCpuPer() > maxCpu) {
                maxCpu = dockerState.getCpuPer();
            }
            if (dockerState.getCpuPer() < minCpu) {
                minCpu = dockerState.getCpuPer();
            }
            sumCpu = sumCpu + dockerState.getCpuPer();
        }
        if (dockerStateList.size() > 0) {
            avgMem = sumMem / (double)dockerStateList.size();
            avgCpu = sumCpu / (double)dockerStateList.size();
        } else {
            minMem = 0.0;
            minCpu = 0.0;
        }
        SubtitleDto memSubtitleDto = new SubtitleDto();
        memSubtitleDto.setAvgValue(FormatUtil.formatDouble(avgMem, 2) + "");
        memSubtitleDto.setMaxValue(maxMem + "");
        memSubtitleDto.setMinValue(minMem + "");
        model.addAttribute("memSubtitleDto", (Object)memSubtitleDto);
        SubtitleDto cpuSubtitleDto = new SubtitleDto();
        cpuSubtitleDto.setAvgValue(FormatUtil.formatDouble(avgCpu, 2) + "");
        cpuSubtitleDto.setMaxValue(maxCpu + "");
        cpuSubtitleDto.setMinValue(minCpu + "");
        model.addAttribute("cpuSubtitleDto", (Object)cpuSubtitleDto);
    }
}

