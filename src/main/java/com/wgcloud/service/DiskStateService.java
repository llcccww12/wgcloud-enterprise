/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.service;

import cn.hutool.core.collection.CollectionUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wgcloud.config.MailConfig;
import com.wgcloud.entity.DiskState;
import com.wgcloud.entity.HostWarnDiy;
import com.wgcloud.entity.SystemInfo;
import com.wgcloud.mapper.DiskStateMapper;
import com.wgcloud.mapper.SystemInfoMapper;
import com.wgcloud.util.FormatUtil;
import com.wgcloud.util.HostUtil;
import com.wgcloud.util.UUIDUtil;
import com.wgcloud.util.staticvar.BatchData;
import com.wgcloud.util.staticvar.StaticKeys;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

@Service
public class DiskStateService {
    private static final Logger logger = LoggerFactory.getLogger(DiskStateService.class);
    @Autowired
    private DiskStateMapper diskStateMapper;
    @Autowired
    private SystemInfoMapper systemInfoMapper;
    @Autowired
    private MailConfig mailConfig;

    public PageInfo selectByParams(Map<String, Object> params, int currPage, int pageSize) throws Exception {
        PageHelper.startPage((int)currPage, (int)pageSize);
        List<DiskState> list = this.diskStateMapper.selectByParams(params);
        PageInfo pageInfo = new PageInfo(list);
        return pageInfo;
    }

    public void save(DiskState DiskState2) throws Exception {
        DiskState2.setId(UUIDUtil.getUUID());
        DiskState2.setCreateTime(new Date());
        this.diskStateMapper.save(DiskState2);
    }

    public void initDiskComputeCache() {
        try {
            HashMap<String, Object> params = new HashMap<String, Object>();
            List<DiskState> list = this.selectAllByParams(params);
            HostUtil.DISK_LIST_COMPUTE.clear();
            for (DiskState diskState : list) {
                DiskState diskStateCache = new DiskState();
                diskStateCache.setHostname(diskState.getHostname());
                diskStateCache.setFileSystem(diskState.getFileSystem());
                diskStateCache.setAvail(diskState.getAvail());
                diskStateCache.setCreateTime(diskState.getCreateTime());
                HostUtil.DISK_LIST_COMPUTE.add(diskStateCache);
            }
        }
        catch (Exception e) {
            logger.error("\u5904\u7406\u78c1\u76d8\u7a7a\u95f4\u7684\u7f13\u5b58\u6570\u636e\u9519\u8bef", (Throwable)e);
        }
    }

    public void setWarnDisk(SystemInfo systemInfo, Model model) {
        try {
            Double diskWarnVal = this.mailConfig.getDiskWarnVal();
            HostWarnDiy hostWarnDiyDto = StaticKeys.HOST_WARN_MAP.get(systemInfo.getHostname());
            model.addAttribute("diskWarnVal", "no");
            if (null != hostWarnDiyDto && null != hostWarnDiyDto.getDiskWarnVal()) {
                diskWarnVal = hostWarnDiyDto.getDiskWarnVal();
                if ("yes".equals(hostWarnDiyDto.getDiskWarnMail()) && "true".equals(this.mailConfig.getDiskWarnMail())) {
                    model.addAttribute("diskWarnVal", (Object)diskWarnVal);
                }
            } else if ("true".equals(this.mailConfig.getDiskWarnMail())) {
                model.addAttribute("diskWarnVal", (Object)diskWarnVal);
            }
        }
        catch (Exception e) {
            logger.error("\u78c1\u76d8\u544a\u8b66\u503c\u8bbe\u7f6e\u9519\u8bef", (Throwable)e);
        }
    }

    public void computeDaysForDisk(List<DiskState> diskStateList, String hostName) {
        try {
            if (CollectionUtil.isEmpty(HostUtil.DISK_LIST_COMPUTE)) {
                this.initDiskComputeCache();
            }
            ArrayList<DiskState> listCache = new ArrayList<DiskState>();
            for (DiskState diskState : HostUtil.DISK_LIST_COMPUTE) {
                if (!diskState.getHostname().equals(hostName)) continue;
                listCache.add(diskState);
            }
            for (DiskState diskState : diskStateList) {
                for (DiskState diskStateCache : listCache) {
                    if (!diskState.getFileSystem().equals(diskStateCache.getFileSystem())) continue;
                    long diffTimes = diskState.getCreateTime().getTime() - diskStateCache.getCreateTime().getTime();
                    if (diffTimes <= 3600000L) {
                        diskState.setDateStr("\u5927\u4e8e1\u5e74");
                        continue;
                    }
                    double hours = FormatUtil.formatDouble((double)diffTimes / 3600000.0, 2);
                    double diffSize = Double.valueOf(diskStateCache.getAvail().replace("G", "")) - Double.valueOf(diskState.getAvail().replace("G", ""));
                    if (diffSize <= 0.0) {
                        diskState.setDateStr("\u5927\u4e8e1\u5e74");
                        continue;
                    }
                    double sizeEveryHour = FormatUtil.formatDouble(diffSize / hours, 2);
                    if (sizeEveryHour <= 0.0) {
                        diskState.setDateStr("\u5927\u4e8e1\u5e74");
                        continue;
                    }
                    double availDays = FormatUtil.formatDouble(Double.valueOf(diskState.getAvail().replace("G", "")) / sizeEveryHour / 24.0, 2);
                    diskState.setDateStr(availDays + "\u5929");
                }
            }
        }
        catch (Exception e) {
            logger.error("\u8ba1\u7b97\u4e3b\u673a\u7684\u6bcf\u4e2a\u78c1\u76d8\u9884\u8ba1\u53ef\u7528\u65f6\u95f4\u9519\u8bef", (Throwable)e);
        }
    }

    @Transactional
    public void saveRecord(List<DiskState> recordList) throws Exception {
        if (recordList.size() < 1) {
            return;
        }
        for (DiskState as : recordList) {
            as.setId(UUIDUtil.getUUID());
        }
        this.diskStateMapper.insertList(recordList);
    }

    public int deleteById(String[] id) throws Exception {
        return this.diskStateMapper.deleteById(id);
    }

    public DiskState selectById(String id) throws Exception {
        return this.diskStateMapper.selectById(id);
    }

    public List<DiskState> selectAllByParams(Map<String, Object> params) throws Exception {
        return this.diskStateMapper.selectAllByParams(params);
    }

    public int deleteByAccHname(List<String> recordList) throws Exception {
        return this.diskStateMapper.deleteByAccHname(recordList);
    }

    public int deleteByDate(Map<String, Object> map) throws Exception {
        return this.diskStateMapper.deleteByDate(map);
    }

    public void addToUpdateSystemDisk(List<SystemInfo> savedList) {
        try {
            if (null == savedList || savedList.size() < 1) {
                return;
            }
            if (BatchData.HOST_DISK_SUM_LIST.size() < 1) {
                return;
            }
            ArrayList<SystemInfo> HOST_DISK_SUM_LIST = new ArrayList<SystemInfo>();
            HOST_DISK_SUM_LIST.addAll(BatchData.HOST_DISK_SUM_LIST);
            BatchData.HOST_DISK_SUM_LIST.clear();
            ArrayList<SystemInfo> updateList = new ArrayList<SystemInfo>();
            for (SystemInfo systemInfo : HOST_DISK_SUM_LIST) {
                if (StringUtils.isEmpty((CharSequence)systemInfo.getHostname())) continue;
                for (SystemInfo systemInfoS : savedList) {
                    if (!systemInfoS.getHostname().equals(systemInfo.getHostname())) continue;
                    systemInfo.setId(systemInfoS.getId());
                    updateList.add(systemInfo);
                }
            }
            if (updateList.size() > 0) {
                this.systemInfoMapper.updateDiskPerByHostName(updateList);
            }
        }
        catch (Exception e) {
            logger.error("\u6279\u91cf\u66f4\u65b0\u4e3b\u673a\u78c1\u76d8\u6570\u636e\u9519\u8bef", (Throwable)e);
        }
    }
}

