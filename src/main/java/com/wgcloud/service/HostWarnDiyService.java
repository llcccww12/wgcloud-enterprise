/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wgcloud.config.LevelConfig;
import com.wgcloud.config.MailConfig;
import com.wgcloud.entity.HostWarnDiy;
import com.wgcloud.entity.SystemInfo;
import com.wgcloud.mapper.HostWarnDiyMapper;
import com.wgcloud.service.LogInfoService;
import com.wgcloud.util.HostUtil;
import com.wgcloud.util.RestUtil;
import com.wgcloud.util.UUIDUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class HostWarnDiyService {
    @Autowired
    private HostWarnDiyMapper hostWarnDiyMapper;
    @Autowired
    private LogInfoService logInfoService;
    @Autowired
    private RestUtil restUtil;
    @Autowired
    private MailConfig mailConfig;
    @Autowired
    private LevelConfig levelConfig;
    private static final Logger logger = LoggerFactory.getLogger(HostWarnDiyService.class);

    public PageInfo selectByParams(Map<String, Object> params, int currPage, int pageSize) throws Exception {
        PageHelper.startPage((int)currPage, (int)pageSize);
        List<HostWarnDiy> list = this.hostWarnDiyMapper.selectByParams(params);
        PageInfo pageInfo = new PageInfo(list);
        return pageInfo;
    }

    public void save(HostWarnDiy hostWarnDiy) throws Exception {
        hostWarnDiy.setId(UUIDUtil.getUUID());
        hostWarnDiy.setCreateTime(new Date());
        if (!StringUtils.isEmpty((CharSequence)hostWarnDiy.getHostDownWarnCount())) {
            hostWarnDiy.setHostDownWarnCount(hostWarnDiy.getHostDownWarnCount().trim());
        }
        if (!StringUtils.isEmpty((CharSequence)hostWarnDiy.getCustomWarnAccountKey())) {
            hostWarnDiy.setCustomWarnAccountKey(hostWarnDiy.getCustomWarnAccountKey().trim());
        }
        if (!StringUtils.isEmpty((CharSequence)hostWarnDiy.getCustomWarnMail())) {
            hostWarnDiy.setCustomWarnMail(hostWarnDiy.getCustomWarnMail().trim());
        }
        this.hostWarnDiyMapper.save(hostWarnDiy);
    }

    @Transactional
    public void saveRecord(List<HostWarnDiy> recordList) throws Exception {
        if (recordList.size() < 1) {
            return;
        }
        for (HostWarnDiy as : recordList) {
            as.setId(UUIDUtil.getUUID());
        }
        this.hostWarnDiyMapper.insertList(recordList);
    }

    public int countByParams(Map<String, Object> params) throws Exception {
        return this.hostWarnDiyMapper.countByParams(params);
    }

    public int updateActive(Map<String, Object> params) throws Exception {
        return this.hostWarnDiyMapper.updateActive(params);
    }

    @Transactional
    public int deleteById(String[] id) throws Exception {
        return this.hostWarnDiyMapper.deleteById(id);
    }

    public void updateById(HostWarnDiy hostWarnDiy) throws Exception {
        this.hostWarnDiyMapper.updateById(hostWarnDiy);
    }

    public HostWarnDiy selectById(String id) throws Exception {
        return this.hostWarnDiyMapper.selectById(id);
    }

    @Transactional
    public void updateRecord(List<HostWarnDiy> recordList) throws Exception {
        this.hostWarnDiyMapper.updateList(recordList);
    }

    public List<HostWarnDiy> selectAllByParams(Map<String, Object> params) throws Exception {
        return this.hostWarnDiyMapper.selectAllByParams(params);
    }

    public List<SystemInfo> getNoAddHostWarnDiyList(List<SystemInfo> systemInfoList, String additionalHostName) throws Exception {
        HashMap<String, Object> params = new HashMap<String, Object>();
        if (!StringUtils.isEmpty((CharSequence)additionalHostName)) {
            params.put("hostnameNe", additionalHostName);
        }
        List<HostWarnDiy> hostWarnDiyList = this.selectAllByParams(params);
        ArrayList<SystemInfo> resultList = new ArrayList<SystemInfo>();
        for (SystemInfo systemInfo : systemInfoList) {
            boolean sign = false;
            for (HostWarnDiy hostWarnDiy : hostWarnDiyList) {
                if (!hostWarnDiy.getHostname().equals(systemInfo.getHostname())) continue;
                sign = true;
                break;
            }
            if (sign) continue;
            HostUtil.setSysImage(systemInfo);
            resultList.add(systemInfo);
        }
        return resultList;
    }

    public void initAddViewValue(HostWarnDiy hostWarnDiy) {
        hostWarnDiy.setMemWarnMail("true".equals(this.mailConfig.getMemWarnMail()) ? "yes" : "no");
        hostWarnDiy.setCpuWarnMail("true".equals(this.mailConfig.getCpuWarnMail()) ? "yes" : "no");
        hostWarnDiy.setDiskWarnMail("true".equals(this.mailConfig.getDiskWarnMail()) ? "yes" : "no");
        hostWarnDiy.setUpSpeedMail("true".equals(this.mailConfig.getUpSpeedMail()) ? "yes" : "no");
        hostWarnDiy.setDownSpeedMail("true".equals(this.mailConfig.getDownSpeedMail()) ? "yes" : "no");
        hostWarnDiy.setSmartWarnMail("true".equals(this.mailConfig.getSmartWarnMail()) ? "yes" : "no");
        hostWarnDiy.setSysLoadWarnMail("true".equals(this.mailConfig.getSysLoadWarnMail()) ? "yes" : "no");
        hostWarnDiy.setCpuTemperatureWarnMail("true".equals(this.mailConfig.getCpuTemperatureWarnMail()) ? "yes" : "no");
        hostWarnDiy.setNetConnectionsWarnMail("true".equals(this.mailConfig.getNetConnectionsWarnMail()) ? "yes" : "no");
        hostWarnDiy.setHostBlockAllWarn("no");
        hostWarnDiy.setHostLoginWarnMail("true".equals(this.mailConfig.getHostLoginWarnMail()) ? "yes" : "no");
        hostWarnDiy.setHostDownWarnMail("true".equals(this.mailConfig.getHostDownWarnMail()) ? "yes" : "no");
        hostWarnDiy.setMemWarnLevel(this.levelConfig.getMemWarn());
        hostWarnDiy.setCpuWarnLevel(this.levelConfig.getCpuWarn());
        hostWarnDiy.setSpeedWarnLevel(this.levelConfig.getSpeedWarn());
        hostWarnDiy.setDiskWarnLevel(this.levelConfig.getDiskWarn());
        hostWarnDiy.setSmartWarnLevel(this.levelConfig.getSmartWarn());
        hostWarnDiy.setCpuTemperatureWarnLevel(this.levelConfig.getCpuTemperatureWarn());
        hostWarnDiy.setSysLoadWarnLevel(this.levelConfig.getSysLoadWarn());
        hostWarnDiy.setHostDownWarnLevel(this.levelConfig.getHostDownWarn());
        hostWarnDiy.setHostLoginWarnLevel(this.levelConfig.getHostLoginWarn());
        hostWarnDiy.setNetConnectionsWarnLevel(this.levelConfig.getNetConnectionsWarn());
        hostWarnDiy.setDiskIoSpeedWarnMail("true".equals(this.mailConfig.getDiskIoSpeedWarnMail()) ? "yes" : "no");
        hostWarnDiy.setCpuWarnVal(this.mailConfig.getCpuWarnVal());
        hostWarnDiy.setMemWarnVal(this.mailConfig.getMemWarnVal());
        hostWarnDiy.setCpuTemperatureWarnVal(this.mailConfig.getCpuTemperatureWarnVal());
        hostWarnDiy.setDiskWarnVal(this.mailConfig.getDiskWarnVal());
        hostWarnDiy.setDiskBlock(this.mailConfig.getDiskBlock());
        hostWarnDiy.setDiskBlockSave(this.mailConfig.getDiskBlockSave());
        hostWarnDiy.setDownSpeedVal(this.mailConfig.getDownSpeedVal());
        hostWarnDiy.setDownSpeedMinVal(this.mailConfig.getDownSpeedMinVal());
        hostWarnDiy.setUpSpeedVal(this.mailConfig.getUpSpeedVal());
        hostWarnDiy.setUpSpeedMinVal(this.mailConfig.getUpSpeedMinVal());
        hostWarnDiy.setSysLoadWarnVal(this.mailConfig.getSysLoadWarnVal());
        hostWarnDiy.setNetConnectionsWarnVal(this.mailConfig.getNetConnectionsWarnVal());
        hostWarnDiy.setHostDownWarnCount(this.mailConfig.getHostDownWarnCount() + "");
        hostWarnDiy.setNetConnectionsWarnVal(this.mailConfig.getNetConnectionsWarnVal());
        hostWarnDiy.setDiskIoSpeedWarnVal(this.mailConfig.getDiskIoSpeedWarnVal());
    }

    public void saveLog(HttpServletRequest request, String action, HostWarnDiy hostWarnDiy) {
        if (null == hostWarnDiy) {
            return;
        }
        this.logInfoService.save(HostUtil.getAccountByRequest(request).getAccount() + action + "\u81ea\u5b9a\u4e49\u544a\u8b66\u4fe1\u606f\uff1a" + hostWarnDiy.getHostname(), "\u81ea\u5b9a\u4e49\u544a\u8b66\u4fe1\u606f\uff1a" + hostWarnDiy.getHostname(), "2");
    }

    public void updateToTargetAccount(Map<String, Object> params) throws Exception {
        this.hostWarnDiyMapper.updateToTargetAccount(params);
    }
}

