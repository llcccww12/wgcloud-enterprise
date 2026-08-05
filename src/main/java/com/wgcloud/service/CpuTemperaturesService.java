/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wgcloud.config.MailConfig;
import com.wgcloud.entity.CpuTemperatures;
import com.wgcloud.entity.HostWarnDiy;
import com.wgcloud.entity.SystemInfo;
import com.wgcloud.mapper.CpuTemperaturesMapper;
import com.wgcloud.util.UUIDUtil;
import com.wgcloud.util.staticvar.StaticKeys;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

@Service
public class CpuTemperaturesService {
    private static final Logger logger = LoggerFactory.getLogger(CpuTemperaturesService.class);
    @Autowired
    private CpuTemperaturesMapper cpuTemperaturesMapper;
    @Autowired
    private MailConfig mailConfig;

    public PageInfo selectByParams(Map<String, Object> params, int currPage, int pageSize) throws Exception {
        PageHelper.startPage((int)currPage, (int)pageSize);
        List<CpuTemperatures> list = this.cpuTemperaturesMapper.selectByParams(params);
        PageInfo pageInfo = new PageInfo(list);
        return pageInfo;
    }

    public void save(CpuTemperatures CpuTemperatures2) throws Exception {
        CpuTemperatures2.setId(UUIDUtil.getUUID());
        CpuTemperatures2.setCreateTime(new Date());
        this.cpuTemperaturesMapper.save(CpuTemperatures2);
    }

    @Transactional
    public void saveRecord(List<CpuTemperatures> recordList) throws Exception {
        if (recordList.size() < 1) {
            return;
        }
        for (CpuTemperatures as : recordList) {
            as.setId(UUIDUtil.getUUID());
        }
        this.cpuTemperaturesMapper.insertList(recordList);
    }

    public int deleteById(String[] id) throws Exception {
        return this.cpuTemperaturesMapper.deleteById(id);
    }

    public CpuTemperatures selectById(String id) throws Exception {
        return this.cpuTemperaturesMapper.selectById(id);
    }

    public List<CpuTemperatures> selectAllByParams(Map<String, Object> params) throws Exception {
        return this.cpuTemperaturesMapper.selectAllByParams(params);
    }

    public int deleteByAccHname(List<String> recordList) throws Exception {
        return this.cpuTemperaturesMapper.deleteByAccHname(recordList);
    }

    public int deleteByDate(Map<String, Object> map) throws Exception {
        return this.cpuTemperaturesMapper.deleteByDate(map);
    }

    public void setWarnValue(SystemInfo systemInfo, Model model) {
        try {
            Double cpuTemperatureWarnVal = this.mailConfig.getCpuTemperatureWarnVal();
            HostWarnDiy hostWarnDiyDto = StaticKeys.HOST_WARN_MAP.get(systemInfo.getHostname());
            model.addAttribute("cpuTemperatureWarnVal", "no");
            if (null != hostWarnDiyDto && null != hostWarnDiyDto.getCpuTemperatureWarnVal()) {
                cpuTemperatureWarnVal = hostWarnDiyDto.getCpuTemperatureWarnVal();
                if ("yes".equals(hostWarnDiyDto.getCpuTemperatureWarnMail()) && "true".equals(this.mailConfig.getCpuTemperatureWarnMail())) {
                    model.addAttribute("cpuTemperatureWarnVal", (Object)(cpuTemperatureWarnVal + "\u2103"));
                }
            } else if ("true".equals(this.mailConfig.getCpuTemperatureWarnMail())) {
                model.addAttribute("cpuTemperatureWarnVal", (Object)(cpuTemperatureWarnVal + "\u2103"));
            }
        }
        catch (Exception e) {
            logger.error("cpu\u6e29\u5ea6\u544a\u8b66\u503c\u5c55\u73b0\u8bbe\u7f6e\u9519\u8bef", (Throwable)e);
        }
    }
}

