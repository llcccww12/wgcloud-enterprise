/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.alibaba.excel.EasyExcel;
import com.wgcloud.config.CommonConfig;
import com.wgcloud.dto.DceInfoListExcelDto;
import com.wgcloud.dto.HostListExcelDto;
import com.wgcloud.dto.NetworkInfoDto;
import com.wgcloud.dto.llm.AllAppExcelDto;
import com.wgcloud.dto.llm.CpuTemperExcelDto;
import com.wgcloud.dto.llm.DiskIoStateExcelDto;
import com.wgcloud.dto.llm.HostDiskPerExcelDto;
import com.wgcloud.dto.llm.HostDiskStateExcelDto;
import com.wgcloud.dto.llm.HostMacExcelDto;
import com.wgcloud.dto.llm.NetworkExcelDto;
import com.wgcloud.entity.AppExceptionInfo;
import com.wgcloud.entity.CpuTemperatures;
import com.wgcloud.entity.DceInfo;
import com.wgcloud.entity.DiskIoState;
import com.wgcloud.entity.DiskState;
import com.wgcloud.entity.HostGroup;
import com.wgcloud.entity.HostMacInfo;
import com.wgcloud.entity.SystemInfo;
import com.wgcloud.service.AppStateService;
import com.wgcloud.service.CpuStateService;
import com.wgcloud.service.CpuTemperaturesService;
import com.wgcloud.service.CustomStateService;
import com.wgcloud.service.DbTableCountService;
import com.wgcloud.service.DceInfoService;
import com.wgcloud.service.DceStateService;
import com.wgcloud.service.DiskIoStateService;
import com.wgcloud.service.DiskSmartService;
import com.wgcloud.service.DiskStateService;
import com.wgcloud.service.DockerStateService;
import com.wgcloud.service.FileWarnStateService;
import com.wgcloud.service.HeathStateService;
import com.wgcloud.service.HostDiskPerService;
import com.wgcloud.service.HostGroupService;
import com.wgcloud.service.HostMacInfoService;
import com.wgcloud.service.LogInfoService;
import com.wgcloud.service.MemStateService;
import com.wgcloud.service.NetIoStateService;
import com.wgcloud.service.SnmpStateService;
import com.wgcloud.service.SysLoadStateService;
import com.wgcloud.service.SystemInfoService;
import com.wgcloud.util.DateUtil;
import com.wgcloud.util.FileUtils;
import com.wgcloud.util.FormatUtil;
import com.wgcloud.util.redis.RedisDataUtil;
import com.wgcloud.util.staticvar.StaticKeys;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LargeModelService {
    private static final Logger logger = LoggerFactory.getLogger(LargeModelService.class);
    @Autowired
    CpuStateService cpuStateService;
    @Autowired
    CustomStateService customStateService;
    @Autowired
    MemStateService memStateService;
    @Autowired
    NetIoStateService netIoStateService;
    @Autowired
    SysLoadStateService sysLoadStateService;
    @Autowired
    AppStateService appStateService;
    @Autowired
    DbTableCountService dbTableCountService;
    @Autowired
    DockerStateService dockerStateService;
    @Autowired
    HeathStateService heathStateService;
    @Autowired
    DceStateService dceStateService;
    @Autowired
    SnmpStateService snmpStateService;
    @Autowired
    FileWarnStateService fileWarnStateService;
    @Autowired
    private LogInfoService logInfoService;
    @Resource
    private DiskStateService diskStateService;
    @Autowired
    private HostMacInfoService hostMacInfoService;
    @Resource
    private DiskSmartService diskSmartService;
    @Resource
    private CpuTemperaturesService cpuTemperaturesService;
    @Resource
    private HostDiskPerService hostDiskPerService;
    @Autowired
    private DiskIoStateService diskIoStateService;
    @Resource
    private SystemInfoService systemInfoService;
    @Resource
    private DceInfoService dceInfoService;
    @Resource
    private HostGroupService hostGroupService;
    @Autowired
    private CommonConfig commonConfig;

    public void exportHostListSnapExcel() {
        try {
            HashMap<String, Object> params = new HashMap<String, Object>();
            List<SystemInfo> systemInfoList = this.systemInfoService.selectAllByParams(params);
            if ("true".equals(this.commonConfig.getHostGroup())) {
                HashMap paramsGroup = new HashMap();
                List<HostGroup> list = this.hostGroupService.selectAllByParams(paramsGroup, null);
                HashMap<String, String> hostGroupMap = new HashMap<String, String>();
                for (HostGroup hostGroup : list) {
                    hostGroupMap.put(hostGroup.getId(), hostGroup.getGroupName());
                }
                for (SystemInfo systemInfo : systemInfoList) {
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
            }
            for (SystemInfo systemInfo : systemInfoList) {
                systemInfo.setRxbyt(FormatUtil.kbToM(systemInfo.getRxbyt()) + "/s");
                systemInfo.setTxbyt(FormatUtil.kbToM(systemInfo.getTxbyt()) + "/s");
            }
            ArrayList<HostListExcelDto> excelList = new ArrayList<HostListExcelDto>();
            for (SystemInfo systemInfo : systemInfoList) {
                HostListExcelDto dto = new HostListExcelDto();
                dto.setAgentVer(systemInfo.getAgentVer());
                dto.setSubmitSeconds(systemInfo.getSubmitSeconds());
                dto.setBootTimeStr(systemInfo.getBootTimeStr());
                dto.setBytesRecv(systemInfo.getBytesRecv());
                dto.setBytesSent(systemInfo.getBytesSent());
                dto.setFifteenLoad(systemInfo.getFifteenLoad());
                dto.setCpuPer(systemInfo.getCpuPer());
                dto.setMemPer(systemInfo.getMemPer());
                dto.setFiveLoad(systemInfo.getFiveLoad());
                dto.setRxbyt(systemInfo.getRxbyt());
                dto.setTxbyt(systemInfo.getTxbyt());
                dto.setCpuCoreNum(systemInfo.getCpuCoreNum());
                dto.setCpuXh(systemInfo.getCpuXh());
                dto.setCpuFamily(systemInfo.getCpuFamily());
                dto.setCpuMhz(systemInfo.getCpuMhz());
                dto.setCpuPhysicalid(systemInfo.getCpuPhysicalid());
                dto.setDiskPer(systemInfo.getDiskPer());
                dto.setDiskSumSize(systemInfo.getDiskSumSize());
                dto.setGroupId(systemInfo.getGroupId());
                dto.setHostname(systemInfo.getHostname());
                dto.setHostnameExt(systemInfo.getHostnameExt());
                dto.setRemark(systemInfo.getRemark());
                dto.setNetConnections(systemInfo.getNetConnections());
                dto.setPlatformVersion(systemInfo.getPlatformVersion());
                if ("2".equals(systemInfo.getState())) {
                    dto.setState("\u79bb\u7ebf");
                } else {
                    dto.setState("\u5728\u7ebf");
                }
                dto.setPlatForm(systemInfo.getPlatForm());
                dto.setPlatformVersion(systemInfo.getPlatformVersion());
                dto.setProcs(systemInfo.getProcs());
                dto.setWarnCount(systemInfo.getWarnCount());
                dto.setTotalMem(systemInfo.getTotalMem());
                dto.setCreateTime(DateUtil.getDateTimeString(systemInfo.getCreateTime()));
                dto.setUptimeStr(systemInfo.getUptimeStr());
                dto.setTotalSwapMem(systemInfo.getTotalSwapMem());
                dto.setSwapMemPer(systemInfo.getSwapMemPer());
                dto.setAccount(systemInfo.getAccount());
                excelList.add(dto);
            }
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.set(5, calendar.get(5) - 1);
            String yesterdayDate = DateUtil.getString(calendar.getTime(), "yyyy-MM-dd");
            String saveFolderUploadFile = StaticKeys.JAR_PATH + "/uploadFile";
            FileUtils.existsFolder(saveFolderUploadFile);
            String saveFolder = StaticKeys.JAR_PATH + "/uploadFile/llm";
            FileUtils.existsFolder(saveFolder);
            String fileName = yesterdayDate + "_\u5168\u91cf\u4e3b\u673a\u76d1\u63a7\u6570\u636e\u5feb\u7167.xlsx";
            EasyExcel.write((String)(StaticKeys.JAR_PATH + "/uploadFile/llm/" + fileName), HostListExcelDto.class).sheet("sheet").doWrite(excelList);
        }
        catch (Exception e) {
            logger.error("\u5bfc\u51fa\u5168\u91cf\u4e3b\u673a\u76d1\u63a7\u6570\u636e\u5feb\u7167excel\u9519\u8bef", (Throwable)e);
        }
    }

    public void exportDiskStateExcel() {
        try {
            Calendar now = Calendar.getInstance();
            now.setTime(new Date());
            now.set(5, now.get(5) - 1);
            String yesterdayDate = DateUtil.getString(now.getTime(), "yyyy-MM-dd");
            HashMap<String, Object> params = new HashMap<String, Object>();
            ArrayList<HostDiskStateExcelDto> dtoList = new ArrayList<HostDiskStateExcelDto>();
            List<DiskState> diskStateList = this.diskStateService.selectAllByParams(params);
            for (DiskState diskState : diskStateList) {
                HostDiskStateExcelDto dto = new HostDiskStateExcelDto();
                dto.setHostName(diskState.getHostname());
                dto.setAvail(diskState.getAvail());
                dto.setDiskSize(diskState.getDiskSize());
                dto.setDatetime(DateUtil.getDateTimeString(diskState.getCreateTime()));
                dto.setFileSystem(diskState.getFileSystem());
                dto.setUsePer(diskState.getUsePer());
                dto.setUsed(diskState.getUsed());
                dtoList.add(dto);
            }
            String saveFolderUploadFile = StaticKeys.JAR_PATH + "/uploadFile";
            FileUtils.existsFolder(saveFolderUploadFile);
            String saveFolder = StaticKeys.JAR_PATH + "/uploadFile/llm";
            FileUtils.existsFolder(saveFolder);
            String fileName = yesterdayDate + "_\u5168\u91cf\u4e3b\u673a\u78c1\u76d8\u4f7f\u7528\u7387\u6570\u636e.xlsx";
            EasyExcel.write((String)(StaticKeys.JAR_PATH + "/uploadFile/llm/" + fileName), HostDiskStateExcelDto.class).sheet("sheet").doWrite(dtoList);
        }
        catch (Exception e) {
            logger.error("\u5bfc\u51fa\u6240\u6709\u4e3b\u673a\u78c1\u76d8\u4f7f\u7528\u7387\u6570\u636eexcel\u9519\u8bef", (Throwable)e);
        }
    }

    public void exportMacListExcel(HttpServletResponse response) {
        try {
            HashMap<String, Object> params = new HashMap<String, Object>();
            List<HostMacInfo> hostMacInfoList = this.hostMacInfoService.selectAllByParams(params);
            ArrayList<HostMacExcelDto> excelList = new ArrayList<HostMacExcelDto>();
            for (HostMacInfo hostMacInfo : hostMacInfoList) {
                HostMacExcelDto dto = new HostMacExcelDto();
                dto.setCreateTime(DateUtil.getDateTimeString(hostMacInfo.getCreateTime()));
                dto.setMacAddress(hostMacInfo.getMacAddress());
                dto.setMacName(hostMacInfo.getMacName());
                dto.setHostname(hostMacInfo.getHostname());
                if (!StaticKeys.HOST_MAP.containsKey(hostMacInfo.getHostname())) continue;
                excelList.add(dto);
            }
            String fileName = DateUtil.getCurrentDateTimeNoChar() + "_\u4e3b\u673aMAC\u4fe1\u606f.xlsx";
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            String encodedFileName = URLEncoder.encode(fileName, "UTF-8");
            response.addHeader("Content-Disposition", "filename=" + encodedFileName);
            EasyExcel.write((OutputStream)response.getOutputStream(), HostMacExcelDto.class).sheet("sheet").doWrite(excelList);
        }
        catch (Exception e) {
            logger.error("\u5bfc\u51fa\u6240\u6709\u4e3b\u673a\u7684mac\u5730\u5740\u5217\u8868excel\u9519\u8bef", (Throwable)e);
        }
    }

    public void exportNetworkNameListExcel(HttpServletResponse response) {
        try {
            HashMap<String, Object> params = new HashMap<String, Object>();
            List<SystemInfo> hostInfoList = this.systemInfoService.selectAllByParams(params);
            ArrayList<NetworkExcelDto> excelList = new ArrayList<NetworkExcelDto>();
            for (SystemInfo systemInfo : hostInfoList) {
                List<NetworkInfoDto> networkList = this.viewAllNetworkHandler(systemInfo.getHostname());
                for (NetworkInfoDto networkInfoDto : networkList) {
                    NetworkExcelDto excelDto = new NetworkExcelDto();
                    excelDto.setName(networkInfoDto.getName());
                    excelDto.setBytesRecv(networkInfoDto.getBytesRecv());
                    excelDto.setBytesSent(networkInfoDto.getBytesSent());
                    excelDto.setHostName(systemInfo.getHostname());
                    excelList.add(excelDto);
                }
            }
            String fileName = DateUtil.getCurrentDateTimeNoChar() + "_\u4e3b\u673a\u7f51\u5361\u540d\u79f0\u4fe1\u606f.xlsx";
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            String encodedFileName = URLEncoder.encode(fileName, "UTF-8");
            response.addHeader("Content-Disposition", "filename=" + encodedFileName);
            EasyExcel.write((OutputStream)response.getOutputStream(), NetworkExcelDto.class).sheet("sheet").doWrite(excelList);
        }
        catch (Exception e) {
            logger.error("\u5bfc\u51fa\u6240\u6709\u4e3b\u673a\u7684\u7f51\u5361\u540d\u79f0\u5217\u8868excel\u9519\u8bef", (Throwable)e);
        }
    }

    public List<NetworkInfoDto> viewAllNetworkHandler(String hostname) {
        List<NetworkInfoDto> allNetworkList = new ArrayList<NetworkInfoDto>();
        try {
            String jsonString = "";
            if (!StringUtils.isEmpty((CharSequence)this.commonConfig.getRedisUrl())) {
                jsonString = RedisDataUtil.getValue(hostname + "_ALL_NETWORK");
            } else if (null != StaticKeys.HOST_IMPORT_DATA.get(hostname + "_ALL_NETWORK")) {
                jsonString = StaticKeys.HOST_IMPORT_DATA.get(hostname + "_ALL_NETWORK").toString();
            }
            if (!StringUtils.isEmpty((CharSequence)jsonString)) {
                JSONArray jsonArray = JSONUtil.parseArray((String)jsonString);
                allNetworkList = JSONUtil.toList((JSONArray)jsonArray, NetworkInfoDto.class);
            }
        }
        catch (Exception e) {
            logger.error("\u6839\u636eIP\u67e5\u8be2\u670d\u52a1\u5668\u7684\u5168\u90e8\u7f51\u5361\u540d\u79f0\u4fe1\u606f\u9519\u8bef", (Throwable)e);
        }
        return allNetworkList;
    }

    public void exportCpuTemperListExcel(HttpServletResponse response) {
        try {
            HashMap<String, Object> params = new HashMap<String, Object>();
            List<CpuTemperatures> cpuTemperList = this.cpuTemperaturesService.selectAllByParams(params);
            ArrayList<CpuTemperExcelDto> excelList = new ArrayList<CpuTemperExcelDto>();
            for (CpuTemperatures cpuTemper : cpuTemperList) {
                CpuTemperExcelDto dto = new CpuTemperExcelDto();
                dto.setCreateTime(DateUtil.getDateTimeString(cpuTemper.getCreateTime()));
                dto.setCore_index(cpuTemper.getCore_index());
                dto.setCrit(cpuTemper.getCrit());
                dto.setInput(cpuTemper.getInput());
                dto.setMax(cpuTemper.getMax());
                dto.setHostname(cpuTemper.getHostname());
                if (!StaticKeys.HOST_MAP.containsKey(cpuTemper.getHostname())) continue;
                excelList.add(dto);
            }
            String fileName = DateUtil.getCurrentDateTimeNoChar() + "_\u4e3b\u673acpu\u6e29\u5ea6\u4fe1\u606f.xlsx";
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            String encodedFileName = URLEncoder.encode(fileName, "UTF-8");
            response.addHeader("Content-Disposition", "filename=" + encodedFileName);
            EasyExcel.write((OutputStream)response.getOutputStream(), CpuTemperExcelDto.class).sheet("sheet").doWrite(excelList);
        }
        catch (Exception e) {
            logger.error("\u5bfc\u51fa\u6240\u6709\u4e3b\u673a\u7684cpu\u6e29\u5ea6excel\u9519\u8bef", (Throwable)e);
        }
    }

    public void exportHostDiskPerExcel(HttpServletResponse response) {
        try {
            HashMap<String, Object> params = new HashMap<String, Object>();
            List<SystemInfo> systemInfoList = this.systemInfoService.selectAllByParams(params);
            ArrayList<HostDiskPerExcelDto> excelList = new ArrayList<HostDiskPerExcelDto>();
            for (SystemInfo systemInfo : systemInfoList) {
                HostDiskPerExcelDto dto = new HostDiskPerExcelDto();
                dto.setCreateTime(DateUtil.getDateTimeString(systemInfo.getCreateTime()));
                dto.setDiskSumPer(systemInfo.getDiskPer());
                dto.setDiskSumSize(systemInfo.getDiskSumSize());
                dto.setHostname(systemInfo.getHostname());
                excelList.add(dto);
            }
            String fileName = DateUtil.getCurrentDateTimeNoChar() + "_\u4e3b\u673a\u78c1\u76d8\u603b\u4f7f\u7528\u7387\u4fe1\u606f.xlsx";
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            String encodedFileName = URLEncoder.encode(fileName, "UTF-8");
            response.addHeader("Content-Disposition", "filename=" + encodedFileName);
            EasyExcel.write((OutputStream)response.getOutputStream(), HostDiskPerExcelDto.class).sheet("sheet").doWrite(excelList);
        }
        catch (Exception e) {
            logger.error("\u5bfc\u51fa\u6240\u6709\u4e3b\u673a\u7684\u78c1\u76d8\u603b\u4f7f\u7528\u7387excel\u9519\u8bef", (Throwable)e);
        }
    }

    public void exportHostDiskIoStateExcel(HttpServletResponse response) {
        try {
            HashMap<String, Object> params = new HashMap<String, Object>();
            String nowTime = DateUtil.getCurrentDateTime();
            params.put("startTime", DateUtil.beforeHourToNowDate(1));
            params.put("endTime", nowTime);
            List<DiskIoState> diskIoStateList = this.diskIoStateService.selectAllByParams(params);
            ArrayList<DiskIoStateExcelDto> excelList = new ArrayList<DiskIoStateExcelDto>();
            for (DiskIoState diskIoState : diskIoStateList) {
                DiskIoStateExcelDto dto = new DiskIoStateExcelDto();
                dto.setHostname(diskIoState.getHostname());
                dto.setCreateTime(DateUtil.getDateTimeString(diskIoState.getCreateTime()));
                dto.setReadIoAvg(diskIoState.getReadIoAvg() + "MB/s");
                dto.setWriteIoAvg(diskIoState.getWriteIoAvg() + "MB/s");
                if (!StaticKeys.HOST_MAP.containsKey(diskIoState.getHostname())) continue;
                excelList.add(dto);
            }
            String fileName = DateUtil.getCurrentDateTimeNoChar() + "_\u4e3b\u673a\u78c1\u76d8IO\u8bfb\u5199\u901f\u7387\u4fe1\u606f.xlsx";
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            String encodedFileName = URLEncoder.encode(fileName, "UTF-8");
            response.addHeader("Content-Disposition", "filename=" + encodedFileName);
            EasyExcel.write((OutputStream)response.getOutputStream(), DiskIoStateExcelDto.class).sheet("sheet").doWrite(excelList);
        }
        catch (Exception e) {
            logger.error("\u5bfc\u51fa\u6240\u6709\u4e3b\u673a\u7684\u78c1\u76d8io\u8bfb\u5199\u901f\u7387excel\u9519\u8bef", (Throwable)e);
        }
    }

    public void exportDceStateExcel() {
        try {
            Calendar now = Calendar.getInstance();
            now.setTime(new Date());
            now.set(5, now.get(5) - 1);
            String yesterdayDate = DateUtil.getString(now.getTime(), "yyyy-MM-dd");
            HashMap<String, Object> params = new HashMap<String, Object>();
            List<DceInfo> dceInfoList = this.dceInfoService.selectAllByParams(params);
            if ("true".equals(this.commonConfig.getHostGroup())) {
                HashMap<String, Object> paramsGroup = new HashMap<String, Object>();
                List<HostGroup> hostGroupList = this.hostGroupService.selectAllByParams(paramsGroup, null);
                HashMap<String, String> hostGroupMap = new HashMap<String, String>();
                Iterator<HostGroup> iterator = hostGroupList.iterator();
                while (iterator.hasNext()) {
                    HostGroup hostGroup = (HostGroup)iterator.next();
                    hostGroupMap.put(hostGroup.getId(), hostGroup.getGroupName());
                }
                for (DceInfo dceInfo1 : dceInfoList) {
                    if (StringUtils.isEmpty((CharSequence)dceInfo1.getGroupId())) continue;
                    String groupNames = "";
                    for (String groupId : dceInfo1.getGroupId().split(",")) {
                        if (!hostGroupMap.containsKey(groupId)) continue;
                        groupNames = groupNames + (String)hostGroupMap.get(groupId) + ",";
                    }
                    if (groupNames.endsWith(",")) {
                        groupNames = groupNames.substring(0, groupNames.length() - 1);
                    }
                    dceInfo1.setGroupId(groupNames);
                }
            }
            ArrayList<DceInfoListExcelDto> excelList = new ArrayList<DceInfoListExcelDto>();
            for (DceInfo dceInfo : dceInfoList) {
                DceInfoListExcelDto dto = new DceInfoListExcelDto();
                dto.setCreateTime(DateUtil.getDateTimeString(dceInfo.getCreateTime()));
                dto.setGroupId(dceInfo.getGroupId());
                dto.setRemark(dceInfo.getRemark());
                dto.setAccount(dceInfo.getAccount());
                dto.setHostname(dceInfo.getHostname());
                dto.setResTimes(dceInfo.getResTimes() + "");
                excelList.add(dto);
            }
            String saveFolderUploadFile = StaticKeys.JAR_PATH + "/uploadFile";
            FileUtils.existsFolder(saveFolderUploadFile);
            String saveFolder = StaticKeys.JAR_PATH + "/uploadFile/llm";
            FileUtils.existsFolder(saveFolder);
            String fileName = yesterdayDate + "_\u5168\u91cfPING\u76d1\u63a7\u5feb\u7167.xlsx";
            EasyExcel.write((String)(StaticKeys.JAR_PATH + "/uploadFile/llm/" + fileName), DceInfoListExcelDto.class).sheet("sheet").doWrite(excelList);
        }
        catch (Exception e) {
            logger.error("\u5bfc\u51fa\u5168\u91cfPING\u76d1\u63a7\u5feb\u7167\u6570\u636eexcel\u9519\u8bef", (Throwable)e);
        }
    }

    public void exportHostAllProcsExcel() {
        try {
            Calendar now = Calendar.getInstance();
            now.setTime(new Date());
            now.set(5, now.get(5) - 1);
            String yesterdayDate = DateUtil.getString(now.getTime(), "yyyy-MM-dd");
            HashMap<String, Object> params = new HashMap<String, Object>();
            List<SystemInfo> systemInfoList = this.systemInfoService.selectAllByParams(params);
            ArrayList<AllAppExcelDto> dtoList = new ArrayList<AllAppExcelDto>();
            for (SystemInfo systemInfo : systemInfoList) {
                try {
                    List cacheList = new ArrayList();
                    String caijiDateTime = "";
                    if (!StringUtils.isEmpty((CharSequence)this.commonConfig.getRedisUrl())) {
                        String jsonString = RedisDataUtil.getValue(systemInfo.getHostname() + "_ALL_PROCESS");
                        if (!StringUtils.isEmpty((CharSequence)jsonString)) {
                            JSONArray jsonArray = JSONUtil.parseArray((String)jsonString);
                            cacheList = JSONUtil.toList((JSONArray)jsonArray, AppExceptionInfo.class);
                            caijiDateTime = RedisDataUtil.getValue(systemInfo.getHostname() + "_ALL_PROCESS" + "_DATETIME");
                        }
                    } else {
                        cacheList = (List)StaticKeys.HOST_ALL_PROCESS.get(systemInfo.getHostname());
                        caijiDateTime = (String)StaticKeys.HOST_ALL_PROCESS.get(systemInfo.getHostname() + "_DATETIME");
                    }
                    if (CollectionUtil.isEmpty(cacheList)) continue;
                    for (AppExceptionInfo appExceptionInfo : (java.util.List<AppExceptionInfo>)cacheList) {
                        AllAppExcelDto dto = new AllAppExcelDto();
                        dto.setAppCmdLine(appExceptionInfo.getAppCmdLine());
                        dto.setAppName(appExceptionInfo.getAppName());
                        dto.setAppTimes(appExceptionInfo.getAppTimes());
                        dto.setCpuPer(appExceptionInfo.getCpuPer());
                        dto.setMemPer(appExceptionInfo.getMemPer());
                        dto.setHostname(systemInfo.getHostname());
                        dto.setDatetime(caijiDateTime);
                        dto.setGatherPid(appExceptionInfo.getGatherPid());
                        dto.setProUsername(appExceptionInfo.getProUsername());
                        dtoList.add(dto);
                    }
                }
                catch (Exception e) {
                    logger.error("LLM\u751f\u6210\u5168\u91cf\u4e3b\u673a\u8fdb\u7a0bExcel\u9519\u8bef", (Throwable)e);
                }
            }
            String saveFolderUploadFile = StaticKeys.JAR_PATH + "/uploadFile";
            FileUtils.existsFolder(saveFolderUploadFile);
            String saveFolder = StaticKeys.JAR_PATH + "/uploadFile/llm";
            FileUtils.existsFolder(saveFolder);
            String fileName = yesterdayDate + "_\u5168\u91cf\u4e3b\u673a\u8fdb\u7a0b\u5feb\u7167.xlsx";
            EasyExcel.write((String)(StaticKeys.JAR_PATH + "/uploadFile/llm/" + fileName), AllAppExcelDto.class).sheet("sheet").doWrite(dtoList);
        }
        catch (Exception e) {
            logger.error("\u5bfc\u51fa\u6240\u6709\u4e3b\u673a\u5168\u91cf\u4e3b\u673a\u8fdb\u7a0b\u5feb\u7167excel\u9519\u8bef", (Throwable)e);
        }
    }
}

