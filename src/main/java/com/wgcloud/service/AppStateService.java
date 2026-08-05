/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wgcloud.dto.SubtitleDto;
import com.wgcloud.entity.AppState;
import com.wgcloud.mapper.AppStateMapper;
import com.wgcloud.util.FormatUtil;
import com.wgcloud.util.UUIDUtil;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

@Service
public class AppStateService {
    private Logger logger = LoggerFactory.getLogger(AppStateService.class);
    @Autowired
    private AppStateMapper appStateMapper;

    public PageInfo selectByParams(Map<String, Object> params, int currPage, int pageSize) throws Exception {
        PageHelper.startPage((int)currPage, (int)pageSize);
        List<AppState> list = this.appStateMapper.selectByParams(params);
        PageInfo pageInfo = new PageInfo(list);
        return pageInfo;
    }

    public void save(AppState appState) throws Exception {
        appState.setId(UUIDUtil.getUUID());
        appState.setCreateTime(new Date());
        this.appStateMapper.save(appState);
    }

    public void saveRecord(List<AppState> recordList) {
        try {
            if (recordList.size() < 1) {
                return;
            }
            for (AppState as : recordList) {
                as.setId(UUIDUtil.getUUID());
            }
            this.appStateMapper.insertList(recordList);
        }
        catch (Exception e) {
            this.logger.error("AppState saveRecord error", (Throwable)e);
        }
    }

    public int deleteByAppInfoId(String appInfoId) throws Exception {
        return this.appStateMapper.deleteByAppInfoId(appInfoId);
    }

    public int deleteById(String[] id) throws Exception {
        return this.appStateMapper.deleteById(id);
    }

    public AppState selectById(String id) throws Exception {
        return this.appStateMapper.selectById(id);
    }

    public List<AppState> selectAllByParams(Map<String, Object> params) throws Exception {
        return this.appStateMapper.selectAllByParams(params);
    }

    public int deleteByDate(Map<String, Object> map) throws Exception {
        return this.appStateMapper.deleteByDate(map);
    }

    public void setSubtitle(Model model, List<AppState> appStateList) {
        Double maxCpu = 0.0;
        Double avgCpu = 0.0;
        Double minCpu = 1000.0;
        Double sumCpu = 0.0;
        Double maxMem = 0.0;
        Double minMem = 1000.0;
        Double avgMem = 0.0;
        Double sumMem = 0.0;
        Integer maxThreads = 0;
        Integer minThreads = 10000;
        Double avgThreads = 0.0;
        Integer sumThreads = 0;
        Integer maxNetConnections = 0;
        Integer minNetConnections = 10000;
        Double avgNetConnections = 0.0;
        Integer sumNetConnections = 0;
        double cpuPerTmp = 0.0;
        double memPerTmp = 0.0;
        Integer threadsTmp = 0;
        Integer netConntctionsTmp = 0;
        for (AppState appState : appStateList) {
            if (null != appState.getCpuPer()) {
                cpuPerTmp = appState.getCpuPer();
                if (cpuPerTmp > maxCpu) {
                    maxCpu = cpuPerTmp;
                }
                if (cpuPerTmp < minCpu) {
                    minCpu = cpuPerTmp;
                }
                sumCpu = sumCpu + cpuPerTmp;
            }
            if (null != appState.getMemPer()) {
                memPerTmp = appState.getMemPer();
                if (memPerTmp > maxMem) {
                    maxMem = memPerTmp;
                }
                if (memPerTmp < minMem) {
                    minMem = memPerTmp;
                }
                sumMem = sumMem + memPerTmp;
            }
            if (!StringUtils.isEmpty((CharSequence)appState.getThreadsNum())) {
                threadsTmp = appState.getThreadsNumInt();
                if (threadsTmp > maxThreads) {
                    maxThreads = threadsTmp;
                }
                if (threadsTmp < minThreads) {
                    minThreads = threadsTmp;
                }
                sumThreads = sumThreads + threadsTmp;
            }
            if (StringUtils.isEmpty((CharSequence)appState.getNetConnections())) continue;
            netConntctionsTmp = appState.getNetConnectionsInt();
            if (netConntctionsTmp > maxNetConnections) {
                maxNetConnections = netConntctionsTmp;
            }
            if (netConntctionsTmp < minNetConnections) {
                minNetConnections = netConntctionsTmp;
            }
            sumNetConnections = sumNetConnections + netConntctionsTmp;
        }
        if (appStateList.size() > 0) {
            avgCpu = sumCpu / (double)appStateList.size();
            avgMem = sumMem / (double)appStateList.size();
            avgThreads = (double)sumThreads.intValue() / (double)appStateList.size();
            avgNetConnections = (double)sumNetConnections.intValue() / (double)appStateList.size();
        } else {
            minCpu = 0.0;
            minMem = 0.0;
            minThreads = 0;
            minNetConnections = 0;
        }
        SubtitleDto cpuSubtitleDto = new SubtitleDto();
        cpuSubtitleDto.setAvgValue(FormatUtil.formatDouble(avgCpu, 2) + "");
        cpuSubtitleDto.setMaxValue(maxCpu + "");
        cpuSubtitleDto.setMinValue(minCpu + "");
        model.addAttribute("cpuSubtitleDto", (Object)cpuSubtitleDto);
        SubtitleDto memSubtitleDto = new SubtitleDto();
        memSubtitleDto.setAvgValue(FormatUtil.formatDouble(avgMem, 2) + "");
        memSubtitleDto.setMaxValue(maxMem + "");
        memSubtitleDto.setMinValue(minMem + "");
        model.addAttribute("memSubtitleDto", (Object)memSubtitleDto);
        SubtitleDto threadsSubtitleDto = new SubtitleDto();
        threadsSubtitleDto.setAvgValue(FormatUtil.formatDouble(avgThreads, 2) + "");
        threadsSubtitleDto.setMaxValue(maxThreads + "");
        threadsSubtitleDto.setMinValue(minThreads + "");
        model.addAttribute("threadsSubtitleDto", (Object)threadsSubtitleDto);
        SubtitleDto netConnectionsSubtitleDto = new SubtitleDto();
        netConnectionsSubtitleDto.setAvgValue(FormatUtil.formatDouble(avgNetConnections, 2) + "");
        netConnectionsSubtitleDto.setMaxValue(maxNetConnections + "");
        netConnectionsSubtitleDto.setMinValue(minNetConnections + "");
        model.addAttribute("netConnectionsSubtitleDto", (Object)netConnectionsSubtitleDto);
    }
}

