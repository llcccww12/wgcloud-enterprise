/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.service;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wgcloud.dto.SubtitleDto;
import com.wgcloud.entity.CustomInfo;
import com.wgcloud.entity.CustomState;
import com.wgcloud.mapper.CustomStateMapper;
import com.wgcloud.util.DateUtil;
import com.wgcloud.util.FormatUtil;
import com.wgcloud.util.UUIDUtil;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

@Service
public class CustomStateService {
    private static final Logger logger = LoggerFactory.getLogger(CustomStateService.class);
    @Autowired
    private CustomStateMapper customStateMapper;

    public PageInfo selectByParams(Map<String, Object> params, int currPage, int pageSize) throws Exception {
        PageHelper.startPage((int)currPage, (int)pageSize);
        List<CustomState> list = this.customStateMapper.selectByParams(params);
        PageInfo pageInfo = new PageInfo(list);
        return pageInfo;
    }

    public void save(CustomState customState) throws Exception {
        customState.setId(UUIDUtil.getUUID());
        customState.setCreateTime(DateUtil.getNowTime());
        this.customStateMapper.save(customState);
    }

    public void saveRecord(List<CustomState> recordList) {
        try {
            if (recordList.size() < 1) {
                return;
            }
            for (CustomState as : recordList) {
                as.setId(UUIDUtil.getUUID());
                if (StringUtils.isEmpty((CharSequence)as.getCustomValue()) || as.getCustomValue().length() <= 50) continue;
                as.setCustomValue(as.getCustomValue().substring(0, 50));
            }
            this.customStateMapper.insertList(recordList);
        }
        catch (Exception e) {
            logger.error("CustomState saveRecord error", (Throwable)e);
        }
    }

    public int deleteByCustomInfoId(String customInfoId) throws Exception {
        return this.customStateMapper.deleteByCustomInfoId(customInfoId);
    }

    public int deleteById(String[] id) throws Exception {
        return this.customStateMapper.deleteById(id);
    }

    public CustomState selectById(String id) throws Exception {
        return this.customStateMapper.selectById(id);
    }

    public List<CustomState> selectAllByParams(Map<String, Object> params) throws Exception {
        return this.customStateMapper.selectAllByParams(params);
    }

    public int deleteByDate(Map<String, Object> map) throws Exception {
        return this.customStateMapper.deleteByDate(map);
    }

    public JSONArray getJsonArray(CustomInfo customInfo, List<CustomState> customStateList) {
        JSONArray jsonArray = new JSONArray();
        String names = customInfo.getCustomName();
        if (StringUtils.isEmpty((CharSequence)names)) {
            return jsonArray;
        }
        String[] customNameArr = names.split(",");
        for (int i = 0; i < customNameArr.length; ++i) {
            for (CustomState customState : customStateList) {
                String customValue = customState.getCustomValue();
                if (StringUtils.isEmpty((CharSequence)customValue)) continue;
                JSONObject jsonObject = new JSONObject();
                jsonObject.set("customName", (Object)customNameArr[i]);
                String[] customValueArr = customValue.split(",");
                jsonObject.set("dateStr", (Object)customState.getDateStr());
                jsonObject.set("customValueDouble", (Object)FormatUtil.getCustomValue(i, customValueArr));
                jsonArray.add((Object)jsonObject);
            }
        }
        return jsonArray;
    }

    public void setSubtitle(Model model, JSONArray jsonArray) {
        Double maxValue = 0.0;
        Double avgValue = 0.0;
        Double minValue = 1.0E7;
        Double sumValue = 0.0;
        double valueTmp = 0.0;
        for (Object object : jsonArray) {
            JSONObject jsonObject = (JSONObject)object;
            if (null == jsonObject.getDouble("customValueDouble")) continue;
            valueTmp = jsonObject.getDouble("customValueDouble");
            if (valueTmp > maxValue) {
                maxValue = valueTmp;
            }
            if (valueTmp < minValue) {
                minValue = valueTmp;
            }
            sumValue = sumValue + valueTmp;
        }
        if (jsonArray.size() > 0) {
            avgValue = sumValue / (double)jsonArray.size();
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
}

