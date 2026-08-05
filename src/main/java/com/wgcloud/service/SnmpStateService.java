/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.service;

import cn.hutool.core.collection.CollectionUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wgcloud.dto.SubtitleDto;
import com.wgcloud.entity.SnmpInfo;
import com.wgcloud.entity.SnmpState;
import com.wgcloud.mapper.SnmpStateMapper;
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
public class SnmpStateService {
    private static final Logger logger = LoggerFactory.getLogger(SnmpStateService.class);
    @Autowired
    private SnmpStateMapper snmpStateMapper;

    public PageInfo selectByParams(Map<String, Object> params, int currPage, int pageSize) throws Exception {
        PageHelper.startPage((int)currPage, (int)pageSize);
        List<SnmpState> list = this.snmpStateMapper.selectByParams(params);
        PageInfo pageInfo = new PageInfo(list);
        return pageInfo;
    }

    public void save(SnmpState SnmpState2) throws Exception {
        SnmpState2.setId(UUIDUtil.getUUID());
        SnmpState2.setCreateTime(new Date());
        this.snmpStateMapper.save(SnmpState2);
    }

    public void saveRecord(List<SnmpState> recordList) {
        try {
            if (recordList.size() < 1) {
                return;
            }
            for (SnmpState as : recordList) {
                as.setId(UUIDUtil.getUUID());
            }
            this.snmpStateMapper.insertList(recordList);
        }
        catch (Exception e) {
            logger.error("SnmpState saveRecord error", (Throwable)e);
        }
    }

    public int deleteById(String[] id) throws Exception {
        return this.snmpStateMapper.deleteById(id);
    }

    public SnmpState selectById(String id) throws Exception {
        return this.snmpStateMapper.selectById(id);
    }

    public List<SnmpState> selectAllByParams(Map<String, Object> params) throws Exception {
        return this.snmpStateMapper.selectAllByParams(params);
    }

    public int deleteByDate(Map<String, Object> map) throws Exception {
        return this.snmpStateMapper.deleteByDate(map);
    }

    public Double avgSpeedToMB(String speedStr, String snmpUnit) {
        Double avgSpeedDouble = 0.0;
        try {
            avgSpeedDouble = !StringUtils.isEmpty((CharSequence)speedStr) ? Double.valueOf(speedStr) : Double.valueOf(0.0);
            avgSpeedDouble = this.avgSpeedToMB(avgSpeedDouble, snmpUnit);
        }
        catch (Exception e) {
            logger.error("SNMP\u6d41\u91cf\u901f\u7387\u5904\u7406\u9519\u8bef", (Throwable)e);
        }
        return avgSpeedDouble;
    }

    public Double avgSpeedToMB(Double speedDou, String snmpUnit) {
        Double avgSpeedDouble = 0.0;
        try {
            avgSpeedDouble = "byte".equals(snmpUnit) ? Double.valueOf(speedDou / 1024.0 / 1024.0) : Double.valueOf(speedDou / 1024.0);
            avgSpeedDouble = FormatUtil.formatDouble(avgSpeedDouble, 2);
        }
        catch (Exception e) {
            logger.error("SNMP\u6d41\u91cf\u901f\u7387\u5904\u7406\u9519\u8bef", (Throwable)e);
        }
        return avgSpeedDouble;
    }

    public void setSubtitle(Model model, List<SnmpState> snmpStateList, SnmpInfo snmpInfo) {
        double maxval = 0.0;
        Double maxRxbyt = 0.0;
        Double avgRxbyt = 0.0;
        Double minRxbyt = 99999.0;
        Double sumRxbyt = 0.0;
        Double maxSent = 0.0;
        Double minSent = 99999.0;
        Double avgSent = 0.0;
        Double sumSent = 0.0;
        Double maxCpuPer = 0.0;
        Double minCpuPer = 99999.0;
        Double avgCpuPer = 0.0;
        Double sumCpuPer = 0.0;
        Double maxMemSize = 0.0;
        Double minMemSize = 99999.0;
        Double avgMemSize = 0.0;
        Double sumMemSize = 0.0;
        Double maxTemperature = 0.0;
        Double minTemperature = 99999.0;
        Double avgTemperature = 0.0;
        Double sumTemperature = 0.0;
        Double recvAvgTmp = 0.0;
        Double sentAvgTmp = 0.0;
        Double cpuPerTmp = 0.0;
        Double memSizeTmp = 0.0;
        Double temperatureTmp = 0.0;
        if (!CollectionUtil.isEmpty(snmpStateList)) {
            for (SnmpState snmpState : snmpStateList) {
                recvAvgTmp = snmpState.getRecvAvgDouble();
                recvAvgTmp = this.avgSpeedToMB(recvAvgTmp, "KB");
                sentAvgTmp = snmpState.getSentAvgDouble();
                sentAvgTmp = this.avgSpeedToMB(sentAvgTmp, "KB");
                snmpState.setSentAvg(String.valueOf(sentAvgTmp));
                snmpState.setRecvAvg(String.valueOf(recvAvgTmp));
                if (null != recvAvgTmp) {
                    if (recvAvgTmp > maxRxbyt) {
                        maxRxbyt = recvAvgTmp;
                    }
                    if (recvAvgTmp < minRxbyt) {
                        minRxbyt = recvAvgTmp;
                    }
                    sumRxbyt = sumRxbyt + recvAvgTmp;
                }
                if (null != sentAvgTmp) {
                    if (sentAvgTmp > maxSent) {
                        maxSent = sentAvgTmp;
                    }
                    if (sentAvgTmp < minSent) {
                        minSent = sentAvgTmp;
                    }
                    sumSent = sumSent + sentAvgTmp;
                }
                if (null != (cpuPerTmp = snmpState.getCpuPerDouble())) {
                    if (cpuPerTmp > maxCpuPer) {
                        maxCpuPer = cpuPerTmp;
                    }
                    if (cpuPerTmp < minCpuPer) {
                        minCpuPer = cpuPerTmp;
                    }
                    sumCpuPer = sumCpuPer + cpuPerTmp;
                }
                if (null != (memSizeTmp = snmpState.getMemPerDouble())) {
                    if (memSizeTmp > maxMemSize) {
                        maxMemSize = memSizeTmp;
                    }
                    if (memSizeTmp < minMemSize) {
                        minMemSize = memSizeTmp;
                    }
                    sumMemSize = sumMemSize + memSizeTmp;
                }
                if (null == (temperatureTmp = snmpState.getTemperatureValueDouble())) continue;
                if (temperatureTmp > maxTemperature) {
                    maxTemperature = temperatureTmp;
                }
                if (temperatureTmp < minTemperature) {
                    minTemperature = temperatureTmp;
                }
                sumTemperature = sumTemperature + temperatureTmp;
            }
        }
        if (maxRxbyt > maxval) {
            maxval = maxRxbyt;
        }
        if (maxSent > maxval) {
            maxval = maxSent;
        }
        if (maxval == 0.0) {
            maxval = 1.0;
        }
        model.addAttribute("snmpAvgMax", (Object)Math.ceil(maxval));
        if (snmpStateList.size() > 0) {
            avgRxbyt = sumRxbyt / (double)snmpStateList.size();
            avgSent = sumSent / (double)snmpStateList.size();
            avgCpuPer = sumCpuPer / (double)snmpStateList.size();
            avgMemSize = sumMemSize / (double)snmpStateList.size();
            avgTemperature = sumTemperature / (double)snmpStateList.size();
        } else {
            minRxbyt = 0.0;
            minSent = 0.0;
            minCpuPer = 0.0;
            minMemSize = 0.0;
            minTemperature = 0.0;
        }
        SubtitleDto rxbytSubtitleDto = new SubtitleDto();
        rxbytSubtitleDto.setAvgValue(FormatUtil.formatDouble(avgRxbyt, 2) + "MB/s");
        rxbytSubtitleDto.setMaxValue(maxRxbyt + "MB/s");
        rxbytSubtitleDto.setMinValue(minRxbyt + "MB/s");
        model.addAttribute("rxbytSubtitleDto", (Object)rxbytSubtitleDto);
        SubtitleDto sentSubtitleDto = new SubtitleDto();
        sentSubtitleDto.setAvgValue(FormatUtil.formatDouble(avgSent, 2) + "MB/s");
        sentSubtitleDto.setMaxValue(maxSent + "MB/s");
        sentSubtitleDto.setMinValue(minSent + "MB/s");
        model.addAttribute("sentSubtitleDto", (Object)sentSubtitleDto);
        SubtitleDto cpuPerSubtitleDto = new SubtitleDto();
        cpuPerSubtitleDto.setAvgValue(FormatUtil.formatDouble(avgCpuPer, 2) + "%");
        cpuPerSubtitleDto.setMaxValue(maxCpuPer + "%");
        cpuPerSubtitleDto.setMinValue(minCpuPer + "%");
        model.addAttribute("cpuPerSubtitleDto", (Object)cpuPerSubtitleDto);
        SubtitleDto memSizeSubtitleDto = new SubtitleDto();
        memSizeSubtitleDto.setAvgValue(FormatUtil.formatDouble(avgMemSize, 2) + "%");
        memSizeSubtitleDto.setMaxValue(maxMemSize + "%");
        memSizeSubtitleDto.setMinValue(minMemSize + "%");
        model.addAttribute("memSizeSubtitleDto", (Object)memSizeSubtitleDto);
        SubtitleDto temperatureSubtitleDto = new SubtitleDto();
        temperatureSubtitleDto.setAvgValue(FormatUtil.formatDouble(avgTemperature, 2) + "\u2103");
        temperatureSubtitleDto.setMaxValue(maxTemperature + "\u2103");
        temperatureSubtitleDto.setMinValue(minTemperature + "\u2103");
        model.addAttribute("temperatureSubtitleDto", (Object)temperatureSubtitleDto);
    }
}

