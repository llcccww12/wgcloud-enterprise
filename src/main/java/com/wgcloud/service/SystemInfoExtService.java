/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.service;

import cn.hutool.core.collection.CollectionUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wgcloud.entity.SystemInfo;
import com.wgcloud.entity.SystemInfoExt;
import com.wgcloud.mapper.SystemInfoExtMapper;
import com.wgcloud.util.UUIDUtil;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SystemInfoExtService {
    private static final Logger logger = LoggerFactory.getLogger(SystemInfoExtService.class);
    @Autowired
    private SystemInfoExtMapper systemInfoExtMapper;

    public PageInfo selectByParams(Map<String, Object> params, int currPage, int pageSize) throws Exception {
        PageHelper.startPage((int)currPage, (int)pageSize);
        List<SystemInfoExt> list = this.systemInfoExtMapper.selectByParams(params);
        PageInfo pageInfo = new PageInfo(list);
        return pageInfo;
    }

    public void saveRecord(List<SystemInfoExt> recordList) throws Exception {
        if (recordList.size() < 1) {
            return;
        }
        for (SystemInfoExt as : recordList) {
            as.setId(UUIDUtil.getUUID());
        }
        this.systemInfoExtMapper.insertList(recordList);
    }

    public int deleteById(String[] id) throws Exception {
        return this.systemInfoExtMapper.deleteById(id);
    }

    public int deleteByHostname(List<String> recordList) throws Exception {
        if (!CollectionUtil.isEmpty(recordList)) {
            this.systemInfoExtMapper.deleteByHostname(recordList);
        }
        return 1;
    }

    public SystemInfoExt selectById(String id) throws Exception {
        return this.systemInfoExtMapper.selectById(id);
    }

    public List<SystemInfoExt> selectAllByParams(Map<String, Object> params) throws Exception {
        return this.systemInfoExtMapper.selectAllByParams(params);
    }

    public void setSystemInfoExtForList(List<SystemInfo> systemInfoList) {
        try {
            HashMap<String, Object> params = new HashMap<String, Object>();
            List<SystemInfoExt> systemInfoExts = this.selectAllByParams(params);
            block2: for (SystemInfo systemInfo : systemInfoList) {
                for (SystemInfoExt systemInfoExt : systemInfoExts) {
                    if (!systemInfo.getHostname().equals(systemInfoExt.getHostname())) continue;
                    this.copyFieldToSystemInfo(systemInfo, systemInfoExt);
                    continue block2;
                }
            }
        }
        catch (Exception e) {
            logger.error("setSystemInfoExtForList\u9519\u8bef", (Throwable)e);
        }
    }

    public void setSystemInfoExt(SystemInfo systemInfo) {
        HashMap<String, Object> params = new HashMap<String, Object>();
        if (null == systemInfo || StringUtils.isEmpty((CharSequence)systemInfo.getHostname())) {
            return;
        }
        params.put("hostname", systemInfo.getHostname());
        try {
            List<SystemInfoExt> systemInfoExts = this.selectAllByParams(params);
            if (!CollectionUtil.isEmpty(systemInfoExts)) {
                SystemInfoExt systemInfoExt = systemInfoExts.get(0);
                this.copyFieldToSystemInfo(systemInfo, systemInfoExt);
            }
        }
        catch (Exception e) {
            logger.error("setSystemInfoExt\u9519\u8bef", (Throwable)e);
        }
    }

    private void copyFieldToSystemInfo(SystemInfo systemInfo, SystemInfoExt systemInfoExt) {
        systemInfo.setAgentVer(systemInfoExt.getAgentVer());
        systemInfo.setBootTime(systemInfoExt.getBootTime());
        systemInfo.setBytesRecv(systemInfoExt.getBytesRecv());
        systemInfo.setBytesSent(systemInfoExt.getBytesSent());
        systemInfo.setCpuFamily(systemInfoExt.getCpuFamily());
        systemInfo.setCpuMhz(systemInfoExt.getCpuMhz());
        systemInfo.setCpuXh(systemInfoExt.getCpuXh());
        systemInfo.setCpuPhysicalid(systemInfoExt.getCpuPhysicalid());
        systemInfo.setHostnameExt(systemInfoExt.getHostnameExt());
        systemInfo.setKernelArch(systemInfoExt.getKernelArch());
        systemInfo.setSubmitSeconds(systemInfoExt.getSubmitSeconds());
        systemInfo.setUptime(systemInfoExt.getUptime());
        systemInfo.setTotalSwapMem(systemInfoExt.getTotalSwapMem());
        systemInfo.setPlatForm(systemInfoExt.getPlatForm());
        systemInfo.setPlatformVersion(systemInfoExt.getPlatformVersion());
        systemInfo.setSwapMemPer(systemInfoExt.getSwapMemPer());
        if (StringUtils.isEmpty((CharSequence)systemInfoExt.getNetInterfaceName())) {
            systemInfo.setNetInterfaceName("ALL");
        } else {
            systemInfo.setNetInterfaceName(systemInfoExt.getNetInterfaceName());
        }
    }

    public int deleteByDate(Map<String, Object> params) throws Exception {
        return this.systemInfoExtMapper.deleteByDate(params);
    }
}

