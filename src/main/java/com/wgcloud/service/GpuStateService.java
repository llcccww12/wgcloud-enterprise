/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.service;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wgcloud.dto.SubtitleDto;
import com.wgcloud.entity.GpuState;
import com.wgcloud.mapper.GpuStateMapper;
import com.wgcloud.util.FormatUtil;
import com.wgcloud.util.UUIDUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

@Service
public class GpuStateService {
    private static final Logger logger = LoggerFactory.getLogger(GpuStateService.class);
    @Autowired
    private GpuStateMapper gpuStateMapper;

    public PageInfo selectByParams(Map<String, Object> params, int currPage, int pageSize) throws Exception {
        PageHelper.startPage((int)currPage, (int)pageSize);
        List<GpuState> list = this.gpuStateMapper.selectByParams(params);
        PageInfo pageInfo = new PageInfo(list);
        return pageInfo;
    }

    public void save(GpuState gpuState) throws Exception {
        gpuState.setId(UUIDUtil.getUUID());
        gpuState.setCreateTime(new Date());
        this.gpuStateMapper.save(gpuState);
    }

    public void saveRecord(List<GpuState> recordList) {
        try {
            if (recordList.size() < 1) {
                return;
            }
            for (GpuState as : recordList) {
                as.setId(UUIDUtil.getUUID());
            }
            this.gpuStateMapper.insertList(recordList);
        }
        catch (Exception e) {
            logger.error("GpuState saveRecord error", (Throwable)e);
        }
    }

    public int deleteById(String[] id) throws Exception {
        return this.gpuStateMapper.deleteById(id);
    }

    public GpuState selectById(String id) throws Exception {
        return this.gpuStateMapper.selectById(id);
    }

    public List<GpuState> selectAllByParams(Map<String, Object> params) throws Exception {
        return this.gpuStateMapper.selectAllByParams(params);
    }

    public int deleteByDate(Map<String, Object> params) throws Exception {
        return this.gpuStateMapper.deleteByDate(params);
    }

    // 主机列表展示用：取该主机最近一条 GPU 状态（可能为 null）
    public GpuState selectLatestByHostname(String hostname) {
        try {
            return this.gpuStateMapper.selectLatestByHostname(hostname);
        } catch (Exception e) {
            logger.error("selectLatestByHostname error", (Throwable) e);
            return null;
        }
    }

    public void loadChartData(String hostName, Model model) {
        try {
            Double maxValue = 0.0;
            Double avgValue = 0.0;
            Double minValue = 1000.0;
            Double sumValue = 0.0;
            HashMap<String, Object> params = new HashMap<String, Object>();
            params.put("hostname", hostName);
            List<GpuState> gpuStateList = this.selectAllByParams(params);
            ArrayList<JSONObject> resultList = new ArrayList<JSONObject>();
            double valueTmp = 0.0;
            int count = 0;
            for (GpuState gpuState : gpuStateList) {
                String dataStr = gpuState.getGpuRate();
                if (StringUtils.isEmpty((CharSequence)dataStr)) continue;
                String[] dataArray = dataStr.split(",");
                for (int i = 0; i < dataArray.length; ++i) {
                    if (StringUtils.isEmpty((CharSequence)dataArray[i])) continue;
                    valueTmp = Double.valueOf(dataArray[i]);
                    if (valueTmp > maxValue) {
                        maxValue = valueTmp;
                    }
                    if (valueTmp < minValue) {
                        minValue = valueTmp;
                    }
                    sumValue = sumValue + valueTmp;
                    ++count;
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.set("gpuName", (Object)("GPU-" + i));
                    jsonObject.set("gpuValue", (Object)valueTmp);
                    jsonObject.set("dateStr", (Object)gpuState.getDateStr());
                    resultList.add(jsonObject);
                }
            }
            model.addAttribute("gpuStateList", (Object)JSONUtil.parseArray(resultList));
            if (count > 0) {
                avgValue = sumValue / (double)count;
            } else {
                avgValue = 0.0;
                minValue = 0.0;
            }
            SubtitleDto subtitleDto = new SubtitleDto();
            subtitleDto.setAvgValue(FormatUtil.formatDouble(avgValue, 2) + "");
            subtitleDto.setMaxValue(maxValue + "");
            subtitleDto.setMinValue(minValue + "");
            model.addAttribute("subtitleDto", (Object)subtitleDto);
        }
        catch (Exception e) {
            logger.error("\u88c5\u8f7dgpu\u4f7f\u7528\u7387\u56fe\u8868\u6570\u636e\u9519\u8bef", (Throwable)e);
        }
    }
}

