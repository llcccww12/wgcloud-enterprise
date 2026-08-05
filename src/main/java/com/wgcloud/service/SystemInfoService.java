/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.service;

import cn.hutool.core.collection.CollectionUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wgcloud.config.CommonConfig;
import com.wgcloud.config.MailConfig;
import com.wgcloud.dto.NetIoStateDto;
import com.wgcloud.dto.SubtitleDto;
import com.wgcloud.entity.AccountInfo;
import com.wgcloud.entity.CpuState;
import com.wgcloud.entity.HostGroup;
import com.wgcloud.entity.HostWarnDiy;
import com.wgcloud.entity.MemState;
import com.wgcloud.entity.NetIoState;
import com.wgcloud.entity.SysLoadState;
import com.wgcloud.entity.SystemInfo;
import com.wgcloud.entity.GpuState;
import com.wgcloud.mapper.SystemInfoMapper;
import com.wgcloud.service.DiskStateService;
import com.wgcloud.service.HostGroupService;
import com.wgcloud.service.LogInfoService;
import com.wgcloud.service.SystemInfoExtService;
import com.wgcloud.service.GpuStateService;
import com.wgcloud.util.FormatUtil;
import com.wgcloud.util.HostUtil;
import com.wgcloud.util.UUIDUtil;
import com.wgcloud.util.staticvar.StaticKeys;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

@Service
public class SystemInfoService {
    private static final Logger logger = LoggerFactory.getLogger(SystemInfoService.class);
    @Autowired
    private SystemInfoMapper systemInfoMapper;
    @Autowired
    private CommonConfig commonConfig;
    @Resource
    private DiskStateService diskStateService;
    @Resource
    private LogInfoService logInfoService;
    @Resource
    private HostGroupService hostGroupService;
    @Resource
    private SystemInfoExtService systemInfoExtService;
    @Resource
    private GpuStateService gpuStateService;
    @Autowired
    private MailConfig mailConfig;

    public PageInfo selectByParams(Map<String, Object> params, int currPage, int pageSize) throws Exception {
        PageHelper.startPage((int)currPage, (int)pageSize);
        List<SystemInfo> list = this.systemInfoMapper.selectByParams(params);
        PageInfo pageInfo = new PageInfo(list);
        this.systemInfoExtService.setSystemInfoExtForList(pageInfo.getList());
        return pageInfo;
    }

    public void save(SystemInfo systemInfo) throws Exception {
        systemInfo.setId(UUIDUtil.getUUID());
        systemInfo.setCreateTime(new Date());
        this.systemInfoMapper.save(systemInfo);
    }

    public void downByHostName(List<String> recordList) throws Exception {
        if (recordList.size() < 1) {
            return;
        }
        this.systemInfoMapper.downByHostName(recordList);
    }

    public void updateAccountByHostName(Map<String, Object> params) throws Exception {
        this.systemInfoMapper.updateAccountByHostName(params);
    }

    public void updateToTargetAccount(Map<String, Object> params) throws Exception {
        this.systemInfoMapper.updateToTargetAccount(params);
    }

    @Transactional
    public void saveRecord(List<SystemInfo> recordList) throws Exception {
        if (recordList.size() < 1) {
            return;
        }
        Date nowTime = new Date();
        for (SystemInfo as : recordList) {
            as.setId(UUIDUtil.getUUID());
            as.setCreateTime(nowTime);
        }
        this.systemInfoMapper.insertList(recordList);
    }

    @Transactional
    public void updateRecord(List<SystemInfo> recordList) throws Exception {
        if (recordList.size() < 1) {
            return;
        }
        this.systemInfoMapper.updateList(recordList);
    }

    @Transactional
    public void updateById(SystemInfo SystemInfo2) throws Exception {
        this.systemInfoMapper.updateById(SystemInfo2);
    }

    public int deleteById(String[] id) throws Exception {
        if (id.length > 0) {
            this.systemInfoMapper.deleteById(id);
        }
        return 1;
    }

    public SystemInfo selectById(String id) throws Exception {
        SystemInfo systemInfo = this.systemInfoMapper.selectById(id);
        this.systemInfoExtService.setSystemInfoExt(systemInfo);
        return systemInfo;
    }

    public List<SystemInfo> selectAllByParams(Map<String, Object> params) throws Exception {
        List<SystemInfo> list = this.systemInfoMapper.selectAllByParams(params);
        this.systemInfoExtService.setSystemInfoExtForList(list);
        return list;
    }

    public List<SystemInfo> selectAllByParamsForTask(Map<String, Object> params) throws Exception {
        List<SystemInfo> list = this.systemInfoMapper.selectAllByParams(params);
        return list;
    }

    public List<SystemInfo> selectByIds(String[] id) throws Exception {
        List<SystemInfo> list = this.systemInfoMapper.selectByIds(id);
        this.systemInfoExtService.setSystemInfoExtForList(list);
        return list;
    }

    public List<SystemInfo> selectAllHostNameByParams(Map<String, Object> params) throws Exception {
        List<SystemInfo> list = this.systemInfoMapper.selectAllHostNameByParams(params);
        return list;
    }

    public int countByParams(Map<String, Object> params) throws Exception {
        return this.systemInfoMapper.countByParams(params);
    }

    public SystemInfo selectByHostname(String hostname) throws Exception {
        List<SystemInfo> list = this.systemInfoMapper.selectByHostname(hostname);
        this.systemInfoExtService.setSystemInfoExtForList(list);
        if (list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    public int deleteByAccHname(Map<String, Object> params) throws Exception {
        return this.systemInfoMapper.deleteByAccHname(params);
    }

    public void hostAddVal(PageInfo<SystemInfo> pageInfo, Model model, HttpServletRequest request) throws Exception {
        if ("true".equals(this.commonConfig.getHostGroup())) {
            this.setGroupInList(pageInfo.getList(), model, request);
        }
        for (SystemInfo systemInfo1 : (java.util.List<SystemInfo>) pageInfo.getList()) {
            systemInfo1.setRxbyt(FormatUtil.kbToM(systemInfo1.getRxbyt()) + "/s");
            systemInfo1.setTxbyt(FormatUtil.kbToM(systemInfo1.getTxbyt()) + "/s");
            HostUtil.setSysImage(systemInfo1);
            // 注入最新 GPU 使用率（多卡逗号分隔），用于主机列表 GPU% 列展示
            try {
                GpuState latestGpu = this.gpuStateService.selectLatestByHostname(systemInfo1.getHostname());
                if (latestGpu != null && !StringUtils.isEmpty((CharSequence) latestGpu.getGpuRate())) {
                    systemInfo1.setGpuRate(latestGpu.getGpuRate());
                }
            } catch (Exception e) {
                logger.error("hostAddVal load gpuRate error", (Throwable) e);
            }
            if (!"true".equals(this.commonConfig.getShowWarnCount())) continue;
            String warnQueryWd = systemInfo1.getHostname();
            this.logInfoService.warnQueryHandle(systemInfo1, warnQueryWd);
        }
    }

    public void hideLeftIp(PageInfo<SystemInfo> pageInfo, HttpServletRequest request) {
        if (!"true".equals(this.commonConfig.getDashViewIpHide())) {
            return;
        }
        HttpSession session = request.getSession();
        AccountInfo accountInfo = (AccountInfo)session.getAttribute("LOGIN_KEY");
        if (null == accountInfo) {
            for (SystemInfo systemInfo : (java.util.List<SystemInfo>) pageInfo.getList()) {
                if (StringUtils.isBlank((CharSequence)systemInfo.getHostname())) continue;
                int length = StringUtils.length((CharSequence)systemInfo.getHostname());
                String resHostName = StringUtils.leftPad((String)StringUtils.right((String)systemInfo.getHostname(), (int)(length - 5)), (int)length, (String)"*");
                systemInfo.setHostname(resHostName);
            }
        }
    }

    public void setSubtitle(Model model, List<CpuState> cpuStateList, List<MemState> memStateList) {
        Double maxCpu = 0.0;
        Double avgCpu = 0.0;
        Double minCpu = 1000.0;
        Double sumCpu = 0.0;
        Integer maxProcsNum = 0;
        Double avgProcsNum = 0.0;
        Integer minProcsNum = 100000;
        Integer sumProcsNum = 0;
        Double maxMem = 0.0;
        Double minMem = 1000.0;
        Double avgMem = 0.0;
        Double sumMem = 0.0;
        for (CpuState cpuState : cpuStateList) {
            if (null != cpuState.getSys()) {
                if (cpuState.getSys() > maxCpu) {
                    maxCpu = cpuState.getSys();
                }
                if (cpuState.getSys() < minCpu) {
                    minCpu = cpuState.getSys();
                }
                sumCpu = sumCpu + cpuState.getSys();
            }
            if (null == cpuState.getProcsNum()) continue;
            if (cpuState.getProcsNum() > maxProcsNum) {
                maxProcsNum = cpuState.getProcsNum();
            }
            if (cpuState.getProcsNum() < minProcsNum) {
                minProcsNum = cpuState.getProcsNum();
            }
            sumProcsNum = sumProcsNum + cpuState.getProcsNum();
        }
        if (cpuStateList.size() > 0) {
            avgCpu = sumCpu / (double)cpuStateList.size();
            avgProcsNum = (double)sumProcsNum.intValue() / (double)cpuStateList.size();
        } else {
            minCpu = 0.0;
            avgProcsNum = 0.0;
        }
        SubtitleDto cpuSubtitleDto = new SubtitleDto();
        cpuSubtitleDto.setAvgValue(FormatUtil.formatDouble(avgCpu, 2) + "");
        cpuSubtitleDto.setMaxValue(maxCpu + "");
        cpuSubtitleDto.setMinValue(minCpu + "");
        model.addAttribute("cpuSubtitleDto", (Object)cpuSubtitleDto);
        SubtitleDto procsNumSubtitleDto = new SubtitleDto();
        procsNumSubtitleDto.setAvgValue(FormatUtil.formatDouble(avgProcsNum, 2) + "");
        procsNumSubtitleDto.setMaxValue(maxProcsNum + "");
        procsNumSubtitleDto.setMinValue(minProcsNum + "");
        model.addAttribute("procsNumSubtitleDto", (Object)procsNumSubtitleDto);
        for (MemState memState : memStateList) {
            if (null == memState.getUsePer()) continue;
            if (memState.getUsePer() > maxMem) {
                maxMem = memState.getUsePer();
            }
            if (memState.getUsePer() < minMem) {
                minMem = memState.getUsePer();
            }
            sumMem = sumMem + memState.getUsePer();
        }
        if (memStateList.size() > 0) {
            avgMem = sumMem / (double)memStateList.size();
        } else {
            minMem = 0.0;
        }
        SubtitleDto memSubtitleDto = new SubtitleDto();
        memSubtitleDto.setAvgValue(FormatUtil.formatDouble(avgMem, 2) + "");
        memSubtitleDto.setMaxValue(maxMem + "");
        memSubtitleDto.setMinValue(minMem + "");
        model.addAttribute("memSubtitleDto", (Object)memSubtitleDto);
    }

    public void findLoadMaxVal(List<SysLoadState> sysLoadStateList, Model model) {
        double maxVal = 0.0;
        Double maxOneLoad = 0.0;
        Double avgOneLoad = 0.0;
        Double minOneLoad = 1000.0;
        Double sumOneLoad = 0.0;
        Double maxFiveLoad = 0.0;
        Double avgFiveLoad = 0.0;
        Double minFiveLoad = 1000.0;
        Double sumFiveLoad = 0.0;
        Double maxFifteenLoad = 0.0;
        Double avgFifteenLoad = 0.0;
        Double minFifteenLoad = 1000.0;
        Double sumFifteenLoad = 0.0;
        if (!CollectionUtil.isEmpty(sysLoadStateList)) {
            for (SysLoadState sysLoadState : sysLoadStateList) {
                if (null != sysLoadState.getOneLoad()) {
                    if (sysLoadState.getOneLoad() > maxOneLoad) {
                        maxOneLoad = sysLoadState.getOneLoad();
                    }
                    if (sysLoadState.getOneLoad() < minOneLoad) {
                        minOneLoad = sysLoadState.getOneLoad();
                    }
                    sumOneLoad = sumOneLoad + sysLoadState.getOneLoad();
                }
                if (null != sysLoadState.getFiveLoad()) {
                    if (sysLoadState.getFiveLoad() > maxFiveLoad) {
                        maxFiveLoad = sysLoadState.getFiveLoad();
                    }
                    if (sysLoadState.getFiveLoad() < minFiveLoad) {
                        minFiveLoad = sysLoadState.getFiveLoad();
                    }
                    sumFiveLoad = sumFiveLoad + sysLoadState.getFiveLoad();
                }
                if (null == sysLoadState.getFifteenLoad()) continue;
                if (sysLoadState.getFifteenLoad() > maxFifteenLoad) {
                    maxFifteenLoad = sysLoadState.getFifteenLoad();
                }
                if (sysLoadState.getFifteenLoad() < minFifteenLoad) {
                    minFifteenLoad = sysLoadState.getFifteenLoad();
                }
                sumFifteenLoad = sumFifteenLoad + sysLoadState.getFifteenLoad();
            }
        }
        if (maxOneLoad > maxVal) {
            maxVal = maxOneLoad;
        }
        if (maxFiveLoad > maxVal) {
            maxVal = maxFiveLoad;
        }
        if (maxFifteenLoad > maxVal) {
            maxVal = maxFifteenLoad;
        }
        if (maxVal == 0.0) {
            maxVal = 1.0;
        }
        model.addAttribute("sysLoadStateMaxVal", (Object)Math.ceil(maxVal));
        if (sysLoadStateList.size() > 0) {
            avgOneLoad = sumOneLoad / (double)sysLoadStateList.size();
            avgFiveLoad = sumFiveLoad / (double)sysLoadStateList.size();
            avgFifteenLoad = sumFifteenLoad / (double)sysLoadStateList.size();
        } else {
            minOneLoad = 0.0;
            minFiveLoad = 0.0;
            minFifteenLoad = 0.0;
        }
        SubtitleDto oneLoadSubtitleDto = new SubtitleDto();
        oneLoadSubtitleDto.setAvgValue(FormatUtil.formatDouble(avgOneLoad, 2) + "");
        oneLoadSubtitleDto.setMaxValue(maxOneLoad + "");
        oneLoadSubtitleDto.setMinValue(minOneLoad + "");
        model.addAttribute("oneLoadSubtitleDto", (Object)oneLoadSubtitleDto);
        SubtitleDto fiveLoadSubtitleDto = new SubtitleDto();
        fiveLoadSubtitleDto.setAvgValue(FormatUtil.formatDouble(avgFiveLoad, 2) + "");
        fiveLoadSubtitleDto.setMaxValue(maxFiveLoad + "");
        fiveLoadSubtitleDto.setMinValue(minFiveLoad + "");
        model.addAttribute("fiveLoadSubtitleDto", (Object)fiveLoadSubtitleDto);
        SubtitleDto fifteenLoadSubtitleDto = new SubtitleDto();
        fifteenLoadSubtitleDto.setAvgValue(FormatUtil.formatDouble(avgFifteenLoad, 2) + "");
        fifteenLoadSubtitleDto.setMaxValue(maxFifteenLoad + "");
        fifteenLoadSubtitleDto.setMinValue(minFifteenLoad + "");
        model.addAttribute("fifteenLoadSubtitleDto", (Object)fifteenLoadSubtitleDto);
    }

    public List<NetIoStateDto> toNetIoStateDto(List<NetIoState> netIoStateList) {
        ArrayList<NetIoStateDto> dtoList = new ArrayList<NetIoStateDto>();
        for (NetIoState netIoState : netIoStateList) {
            NetIoStateDto dto = new NetIoStateDto();
            dto.setCreateTime(netIoState.getCreateTime());
            dto.setDateStr(netIoState.getDateStr());
            dto.setHostname(netIoState.getHostname());
            dto.setRxbyt(Double.valueOf(netIoState.getRxbyt()));
            dto.setRxpck(Double.valueOf(netIoState.getRxpck()));
            dto.setTxbyt(Double.valueOf(netIoState.getTxbyt()));
            dto.setTxpck(Double.valueOf(netIoState.getTxpck()));
            dto.setDropin(Double.valueOf(netIoState.getDropin()));
            dto.setDropout(Double.valueOf(netIoState.getDropout()));
            dto.setNetConnections(Integer.valueOf(netIoState.getNetConnections()));
            dtoList.add(dto);
        }
        return dtoList;
    }

    public void updateActive(HttpServletRequest request) throws Exception {
        if (!StringUtils.isEmpty((CharSequence)request.getParameter("id")) && !StringUtils.isEmpty((CharSequence)request.getParameter("active"))) {
            String id = request.getParameter("id");
            String activeValue = request.getParameter("active");
            SystemInfo systemInfo = new SystemInfo();
            systemInfo.setId(id);
            systemInfo.setActive(activeValue);
            systemInfo.setCreateTime(new Date());
            this.systemInfoMapper.updateActive(systemInfo);
            SystemInfo systemInfoTmp = this.systemInfoMapper.selectById(id);
            String activeStr = "\u5f00\u59cb\u76d1\u63a7";
            if ("2".equals(activeValue)) {
                activeStr = "\u505c\u6b62\u76d1\u63a7";
            }
            this.logInfoService.save(HostUtil.getAccountByRequest(request).getAccount() + "\u66f4\u65b0\u4e3b\u673a\u76d1\u63a7\u72b6\u6001\u4e3a\u3010" + activeStr + "\u3011\uff1a" + systemInfoTmp.getHostname(), "\u4e3b\u673a\u5907\u6ce8\uff1a" + systemInfoTmp.getRemark(), "2");
        }
    }

    public void updateDiskPerByHostName(List<SystemInfo> recordList) throws Exception {
        this.systemInfoMapper.updateDiskPerByHostName(recordList);
    }

    public void updateCountBlock(HttpServletRequest request) throws Exception {
        if (!StringUtils.isEmpty((CharSequence)request.getParameter("id")) && !StringUtils.isEmpty((CharSequence)request.getParameter("countBlock"))) {
            String id = request.getParameter("id");
            String countBlock = request.getParameter("countBlock");
            SystemInfo systemInfo = new SystemInfo();
            systemInfo.setId(id);
            systemInfo.setCountBlock(countBlock);
            this.systemInfoMapper.updateCountBlock(systemInfo);
            SystemInfo systemInfoTmp = this.systemInfoMapper.selectById(id);
            String countBlockStr = "\u5f00\u59cb\u7edf\u8ba1";
            if ("2".equals(countBlock)) {
                countBlockStr = "\u505c\u6b62\u7edf\u8ba1";
            }
            this.logInfoService.save(HostUtil.getAccountByRequest(request).getAccount() + "\u66f4\u65b0\u4e3b\u673a\u7edf\u8ba1\u72b6\u6001\u4e3a\u3010" + countBlockStr + "\u3011\uff1a" + systemInfoTmp.getHostname(), "\u4e3b\u673a\u5907\u6ce8\uff1a" + systemInfoTmp.getRemark(), "2");
        }
    }

    public void updateOrderNum(HttpServletRequest request) throws Exception {
        if (!StringUtils.isEmpty((CharSequence)request.getParameter("id"))) {
            String id = request.getParameter("id");
            String orderNum = request.getParameter("orderNum");
            SystemInfo systemInfo = new SystemInfo();
            systemInfo.setId(id);
            if (!StringUtils.isEmpty((CharSequence)orderNum)) {
                systemInfo.setOrderNum(Integer.valueOf(orderNum));
            }
            this.systemInfoMapper.updateOrderNum(systemInfo);
            SystemInfo systemInfoTmp = this.systemInfoMapper.selectById(id);
            this.logInfoService.save(HostUtil.getAccountByRequest(request).getAccount() + "\u66f4\u65b0\u4e3b\u673a\u6392\u5e8f\u5e8f\u53f7\u4e3a\u3010" + orderNum + "\u3011\uff1a" + systemInfoTmp.getHostname(), "\u4e3b\u673a\u5907\u6ce8\uff1a" + systemInfoTmp.getRemark(), "2");
        }
    }

    public void findNetIoStateBytMaxVal(List<NetIoStateDto> netIoStateList, Model model) {
        double maxBytesVal = 0.0;
        double maxPckVal = 0.0;
        double maxDropVal = 0.0;
        Double maxRxbyt = 0.0;
        Double avgRxbyt = 0.0;
        Double minRxbyt = 99999.0;
        Double sumRxbyt = 0.0;
        Double maxTxbyt = 0.0;
        Double minTxbyt = 99999.0;
        Double avgTxbyt = 0.0;
        Double sumTxbyt = 0.0;
        Double maxRxpck = 0.0;
        Double avgRxpck = 0.0;
        Double minRxpck = 99999.0;
        Double sumRxpck = 0.0;
        Double maxTxpck = 0.0;
        Double minTxpck = 99999.0;
        Double avgTxpck = 0.0;
        Double sumTxpck = 0.0;
        Integer maxConns = 0;
        Integer minConns = 99999;
        Double avgConns = 0.0;
        Integer sumConns = 0;
        if (!CollectionUtil.isEmpty(netIoStateList)) {
            for (NetIoStateDto netIoState : netIoStateList) {
                try {
                    if (null != netIoState.getDropin() && netIoState.getDropin() > maxDropVal) {
                        maxDropVal = netIoState.getDropin();
                    }
                    if (null != netIoState.getDropout() && netIoState.getDropout() > maxDropVal) {
                        maxDropVal = netIoState.getDropout();
                    }
                    if (null != netIoState.getRxbyt()) {
                        if (netIoState.getRxbyt() > maxRxbyt) {
                            maxRxbyt = netIoState.getRxbyt();
                        }
                        if (netIoState.getRxbyt() < minRxbyt) {
                            minRxbyt = netIoState.getRxbyt();
                        }
                        sumRxbyt = sumRxbyt + netIoState.getRxbyt();
                    }
                    if (null != netIoState.getTxbyt()) {
                        if (netIoState.getTxbyt() > maxTxbyt) {
                            maxTxbyt = netIoState.getTxbyt();
                        }
                        if (netIoState.getTxbyt() < minTxbyt) {
                            minTxbyt = netIoState.getTxbyt();
                        }
                        sumTxbyt = sumTxbyt + netIoState.getTxbyt();
                    }
                    if (null != netIoState.getRxpck()) {
                        if (netIoState.getRxpck() > maxRxpck) {
                            maxRxpck = netIoState.getRxpck();
                        }
                        if (netIoState.getRxpck() < minRxpck) {
                            minRxpck = netIoState.getRxpck();
                        }
                        sumRxpck = sumRxpck + netIoState.getRxpck();
                    }
                    if (null != netIoState.getTxpck()) {
                        if (netIoState.getTxpck() > maxTxpck) {
                            maxTxpck = netIoState.getTxpck();
                        }
                        if (Double.valueOf(netIoState.getTxpck()) < minTxpck) {
                            minTxpck = netIoState.getTxpck();
                        }
                        sumTxpck = sumTxpck + netIoState.getTxpck();
                    }
                    if (null == netIoState.getNetConnections()) continue;
                    if (netIoState.getNetConnections() > maxConns) {
                        maxConns = netIoState.getNetConnections();
                    }
                    if (Double.valueOf(netIoState.getNetConnections().intValue()) < (double)minConns.intValue()) {
                        minConns = netIoState.getNetConnections();
                    }
                    sumConns = sumConns + netIoState.getNetConnections();
                }
                catch (Exception e) {
                    logger.error("\u8bbe\u7f6e\u7f51\u7edc\u6d41\u91cf\u56fe\u8868\u526f\u6807\u9898\u9519\u8bef", (Throwable)e);
                }
            }
        }
        if (maxRxbyt > maxBytesVal) {
            maxBytesVal = maxRxbyt;
        }
        if (maxTxbyt > maxBytesVal) {
            maxBytesVal = maxTxbyt;
        }
        if (maxRxpck > maxPckVal) {
            maxPckVal = maxRxpck;
        }
        if (maxTxpck > maxPckVal) {
            maxPckVal = maxTxpck;
        }
        if (maxBytesVal == 0.0) {
            maxBytesVal = 1.0;
        }
        model.addAttribute("netIoStateBytMaxVal", (Object)Math.ceil(maxBytesVal));
        if (maxPckVal == 0.0) {
            maxPckVal = 1.0;
        }
        model.addAttribute("netIoStatePckMaxVal", (Object)Math.ceil(maxPckVal));
        if (maxDropVal == 0.0) {
            maxDropVal = 1.0;
        }
        model.addAttribute("netIoStateDropPckMaxVal", (Object)Math.ceil(maxDropVal));
        if (netIoStateList.size() > 0) {
            avgRxbyt = sumRxbyt / (double)netIoStateList.size();
            avgTxbyt = sumTxbyt / (double)netIoStateList.size();
            avgRxpck = sumRxpck / (double)netIoStateList.size();
            avgTxpck = sumTxpck / (double)netIoStateList.size();
            avgConns = (double)sumConns.intValue() / (double)netIoStateList.size();
        } else {
            minRxbyt = 0.0;
            minTxbyt = 0.0;
            minRxpck = 0.0;
            minTxpck = 0.0;
            minConns = 0;
        }
        SubtitleDto rxbytSubtitleDto = new SubtitleDto();
        rxbytSubtitleDto.setAvgValue(FormatUtil.kbToM(FormatUtil.formatDouble(avgRxbyt, 2) + "") + "/s");
        rxbytSubtitleDto.setMaxValue(FormatUtil.kbToM(maxRxbyt + "") + "/s");
        rxbytSubtitleDto.setMinValue(FormatUtil.kbToM(minRxbyt + "") + "/s");
        model.addAttribute("rxbytSubtitleDto", (Object)rxbytSubtitleDto);
        SubtitleDto txbytSubtitleDto = new SubtitleDto();
        txbytSubtitleDto.setAvgValue(FormatUtil.kbToM(FormatUtil.formatDouble(avgTxbyt, 2) + "") + "/s");
        txbytSubtitleDto.setMaxValue(FormatUtil.kbToM(maxTxbyt + "") + "/s");
        txbytSubtitleDto.setMinValue(FormatUtil.kbToM(minTxbyt + "") + "/s");
        model.addAttribute("txbytSubtitleDto", (Object)txbytSubtitleDto);
        SubtitleDto rxpckSubtitleDto = new SubtitleDto();
        rxpckSubtitleDto.setAvgValue(FormatUtil.formatDouble(avgRxpck, 2) + "/s");
        rxpckSubtitleDto.setMaxValue(maxRxpck + "/s");
        rxpckSubtitleDto.setMinValue(minRxpck + "/s");
        model.addAttribute("rxpckSubtitleDto", (Object)rxpckSubtitleDto);
        SubtitleDto txpckSubtitleDto = new SubtitleDto();
        txpckSubtitleDto.setAvgValue(FormatUtil.formatDouble(avgTxpck, 2) + "/s");
        txpckSubtitleDto.setMaxValue(maxTxpck + "/s");
        txpckSubtitleDto.setMinValue(minTxpck + "/s");
        model.addAttribute("txpckSubtitleDto", (Object)txpckSubtitleDto);
        SubtitleDto connsSubtitleDto = new SubtitleDto();
        connsSubtitleDto.setAvgValue(FormatUtil.formatDouble(avgConns, 2) + "");
        connsSubtitleDto.setMaxValue(maxConns + "");
        connsSubtitleDto.setMinValue(minConns + "");
        model.addAttribute("connsSubtitleDto", (Object)connsSubtitleDto);
    }

    public void hideLeftIp(SystemInfo systemInfo, HttpServletRequest request) {
        if (StringUtils.isBlank((CharSequence)systemInfo.getHostname())) {
            return;
        }
        if (!"true".equals(this.commonConfig.getDashViewIpHide())) {
            return;
        }
        HttpSession session = request.getSession();
        AccountInfo accountInfo = (AccountInfo)session.getAttribute("LOGIN_KEY");
        if (null == accountInfo) {
            int length = StringUtils.length((CharSequence)systemInfo.getHostname());
            String resHostName = StringUtils.leftPad((String)StringUtils.right((String)systemInfo.getHostname(), (int)(length - 5)), (int)length, (String)"*");
            systemInfo.setHostname(resHostName);
        }
    }

    public void setChartWarnElement(SystemInfo systemInfo, Model model) {
        try {
            Double memWarnVal = this.mailConfig.getMemWarnVal();
            Double cpuWarnVal = this.mailConfig.getCpuWarnVal();
            Double downSpeedVal = this.mailConfig.getDownSpeedVal();
            Double upSpeedVal = this.mailConfig.getUpSpeedVal();
            Double sysLoadWarnVal = this.mailConfig.getSysLoadWarnVal();
            Double netConnectionsWarnVal = this.mailConfig.getNetConnectionsWarnVal();
            HostWarnDiy hostWarnDiyDto = StaticKeys.HOST_WARN_MAP.get(systemInfo.getHostname());
            model.addAttribute("memWarnVal", "no");
            if (null != hostWarnDiyDto && null != hostWarnDiyDto.getMemWarnVal()) {
                memWarnVal = hostWarnDiyDto.getMemWarnVal();
                if ("yes".equals(hostWarnDiyDto.getMemWarnMail()) && "true".equals(this.mailConfig.getMemWarnMail())) {
                    model.addAttribute("memWarnVal", (Object)memWarnVal);
                }
            } else if ("true".equals(this.mailConfig.getMemWarnMail())) {
                model.addAttribute("memWarnVal", (Object)memWarnVal);
            }
            model.addAttribute("cpuWarnVal", "no");
            if (null != hostWarnDiyDto && null != hostWarnDiyDto.getCpuWarnVal()) {
                cpuWarnVal = hostWarnDiyDto.getCpuWarnVal();
                if ("yes".equals(hostWarnDiyDto.getCpuWarnMail()) && "true".equals(this.mailConfig.getCpuWarnMail())) {
                    model.addAttribute("cpuWarnVal", (Object)cpuWarnVal);
                }
            } else if ("true".equals(this.mailConfig.getCpuWarnMail())) {
                model.addAttribute("cpuWarnVal", (Object)cpuWarnVal);
            }
            model.addAttribute("upSpeedVal", "no");
            if (null != hostWarnDiyDto && null != hostWarnDiyDto.getUpSpeedVal()) {
                upSpeedVal = hostWarnDiyDto.getUpSpeedVal();
                if ("yes".equals(hostWarnDiyDto.getUpSpeedMail()) && "true".equals(this.mailConfig.getUpSpeedMail())) {
                    model.addAttribute("upSpeedVal", (Object)(FormatUtil.kbToM(upSpeedVal + "") + "/s"));
                }
            } else if ("true".equals(this.mailConfig.getUpSpeedMail())) {
                model.addAttribute("upSpeedVal", (Object)(FormatUtil.kbToM(upSpeedVal + "") + "/s"));
            }
            model.addAttribute("downSpeedVal", "no");
            if (null != hostWarnDiyDto && null != hostWarnDiyDto.getDownSpeedVal()) {
                downSpeedVal = hostWarnDiyDto.getDownSpeedVal();
                if ("yes".equals(hostWarnDiyDto.getDownSpeedMail()) && "true".equals(this.mailConfig.getDownSpeedMail())) {
                    model.addAttribute("downSpeedVal", (Object)(FormatUtil.kbToM(downSpeedVal + "") + "/s"));
                }
            } else if ("true".equals(this.mailConfig.getDownSpeedMail())) {
                model.addAttribute("downSpeedVal", (Object)(FormatUtil.kbToM(downSpeedVal + "") + "/s"));
            }
            model.addAttribute("sysLoadWarnVal", "no");
            if (null != hostWarnDiyDto && null != hostWarnDiyDto.getSysLoadWarnVal()) {
                sysLoadWarnVal = hostWarnDiyDto.getSysLoadWarnVal();
                if ("yes".equals(hostWarnDiyDto.getSysLoadWarnMail()) && "true".equals(this.mailConfig.getSysLoadWarnMail())) {
                    model.addAttribute("sysLoadWarnVal", (Object)sysLoadWarnVal);
                }
            } else if ("true".equals(this.mailConfig.getSysLoadWarnMail())) {
                model.addAttribute("sysLoadWarnVal", (Object)sysLoadWarnVal);
            }
            model.addAttribute("netConnectionsWarnVal", "no");
            if (null != hostWarnDiyDto && null != hostWarnDiyDto.getNetConnectionsWarnVal()) {
                netConnectionsWarnVal = hostWarnDiyDto.getNetConnectionsWarnVal();
                if ("yes".equals(hostWarnDiyDto.getNetConnectionsWarnMail()) && "true".equals(this.mailConfig.getNetConnectionsWarnMail())) {
                    model.addAttribute("netConnectionsWarnVal", (Object)netConnectionsWarnVal);
                }
            } else if ("true".equals(this.mailConfig.getNetConnectionsWarnMail())) {
                model.addAttribute("netConnectionsWarnVal", (Object)netConnectionsWarnVal);
            }
        }
        catch (Exception e) {
            logger.error("\u4e3b\u673a\u56fe\u5f62\u62a5\u8868\u8bbe\u7f6e\u544a\u8b66\u7ebf\u9519\u8bef", (Throwable)e);
        }
    }

    public void saveGroupId(String ids, String[] groupIdsArr, HttpServletRequest request) throws Exception {
        List<HostGroup> hostGroupList = new ArrayList();
        if (null != groupIdsArr && groupIdsArr.length > 0) {
            HashMap<String, Object> params = new HashMap<String, Object>();
            params.put("groupIds", groupIdsArr);
            hostGroupList = this.hostGroupService.selectAllByParams(params, request);
        }
        if (!StringUtils.isEmpty((CharSequence)ids)) {
            SystemInfo ho = null;
            for (String id : ids.split(",")) {
                ho = this.selectById(id);
                if (hostGroupList.size() > 0) {
                    ho.setGroupId(StringUtils.join((Object[])groupIdsArr, (String)","));
                } else {
                    ho.setGroupId("");
                }
                this.updateById(ho);
                this.logInfoService.save(HostUtil.getAccountByRequest(request).getAccount() + "\u8bbe\u7f6e\u4e3b\u673a\u6807\u7b7e\uff1a" + ho.getHostname(), "\u6807\u7b7e\uff1a" + this.hostGroupService.returnGroupNames(hostGroupList), "2");
            }
        }
    }

    public List<HostGroup> setGroupInList(List<SystemInfo> recordList, Model model, HttpServletRequest request) throws Exception {
        HashMap<String, Object> params = new HashMap<String, Object>();
        List<HostGroup> hostGroupList = this.hostGroupService.selectAllByParams(params, request);
        model.addAttribute("hostGroupList", hostGroupList);
        HashMap<String, String> hostGroupMap = new HashMap<String, String>();
        for (HostGroup hostGroup : hostGroupList) {
            hostGroupMap.put(hostGroup.getId(), hostGroup.getGroupName());
        }
        for (SystemInfo systemInfo : recordList) {
            if (StringUtils.isEmpty((CharSequence)systemInfo.getGroupId())) continue;
            String groupNames = "";
            for (String groupId : systemInfo.getGroupId().split(",")) {
                if (!hostGroupMap.containsKey(groupId)) continue;
                groupNames = groupNames + (String)hostGroupMap.get(groupId) + ",";
            }
            if (groupNames.endsWith(",")) {
                groupNames = groupNames.substring(0, groupNames.length() - 1);
            }
            systemInfo.setGroupId(groupNames);
        }
        return hostGroupList;
    }
}

