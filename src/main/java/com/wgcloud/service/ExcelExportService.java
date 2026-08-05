/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.service;

import com.alibaba.excel.EasyExcel;
import com.wgcloud.dto.AppExcelChartDto;
import com.wgcloud.dto.CustomExcelChartDto;
import com.wgcloud.dto.DbTableExcelChartDto;
import com.wgcloud.dto.DceExcelChartDto;
import com.wgcloud.dto.DceInfoListExcelDto;
import com.wgcloud.dto.DockerExcelChartDto;
import com.wgcloud.dto.EquipmentExcelDto;
import com.wgcloud.dto.FileWarnStateExcelDto;
import com.wgcloud.dto.HeathExcelChartDto;
import com.wgcloud.dto.HostChartExcelDto;
import com.wgcloud.dto.HostListExcelDto;
import com.wgcloud.dto.PasswdInfoExcelDto;
import com.wgcloud.dto.SnmpExcelChartDto;
import com.wgcloud.entity.AppInfo;
import com.wgcloud.entity.AppState;
import com.wgcloud.entity.CpuState;
import com.wgcloud.entity.CustomInfo;
import com.wgcloud.entity.CustomState;
import com.wgcloud.entity.DbTable;
import com.wgcloud.entity.DbTableCount;
import com.wgcloud.entity.DceInfo;
import com.wgcloud.entity.DceState;
import com.wgcloud.entity.DockerInfo;
import com.wgcloud.entity.DockerState;
import com.wgcloud.entity.Equipment;
import com.wgcloud.entity.FileWarnInfo;
import com.wgcloud.entity.FileWarnState;
import com.wgcloud.entity.HeathMonitor;
import com.wgcloud.entity.HeathState;
import com.wgcloud.entity.MemState;
import com.wgcloud.entity.NetIoState;
import com.wgcloud.entity.PasswdInfo;
import com.wgcloud.entity.SnmpInfo;
import com.wgcloud.entity.SnmpState;
import com.wgcloud.entity.SysLoadState;
import com.wgcloud.entity.SystemInfo;
import com.wgcloud.service.AppStateService;
import com.wgcloud.service.CpuStateService;
import com.wgcloud.service.CustomStateService;
import com.wgcloud.service.DbTableCountService;
import com.wgcloud.service.DceStateService;
import com.wgcloud.service.DockerStateService;
import com.wgcloud.service.FileWarnStateService;
import com.wgcloud.service.HeathStateService;
import com.wgcloud.service.MemStateService;
import com.wgcloud.service.NetIoStateService;
import com.wgcloud.service.SnmpStateService;
import com.wgcloud.service.SysLoadStateService;
import com.wgcloud.util.DateUtil;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExcelExportService {
    private static final Logger logger = LoggerFactory.getLogger(ExcelExportService.class);
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

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void exportExcel(Map<String, Object> params, HttpServletResponse response) {
        OutputStream out = null;
        try {
            ArrayList<String> datetimeList = new ArrayList<String>();
            List<CpuState> cpuStateList = this.cpuStateService.selectAllByParams(params);
            HashMap<String, CpuState> cpuStateMap = new HashMap<String, CpuState>();
            for (CpuState cpuState : cpuStateList) {
                datetimeList.add(cpuState.getDateStr());
                cpuStateMap.put(cpuState.getDateStr(), cpuState);
            }
            List<MemState> memStateList = this.memStateService.selectAllByParams(params);
            HashMap<String, MemState> memStateMap = new HashMap<String, MemState>();
            for (MemState memState : memStateList) {
                memStateMap.put(memState.getDateStr(), memState);
            }
            List<SysLoadState> ysLoadSstateList = this.sysLoadStateService.selectAllByParams(params);
            HashMap<String, SysLoadState> ysLoadSstateMap = new HashMap<String, SysLoadState>();
            for (SysLoadState sysLoadState : ysLoadSstateList) {
                ysLoadSstateMap.put(sysLoadState.getDateStr(), sysLoadState);
            }
            List<NetIoState> netIoStateList = this.netIoStateService.selectAllByParams(params);
            HashMap<String, NetIoState> netIoStateMap = new HashMap<String, NetIoState>();
            for (NetIoState netIoState : netIoStateList) {
                netIoStateMap.put(netIoState.getDateStr(), netIoState);
            }
            ArrayList<HostChartExcelDto> excelChartList = new ArrayList<HostChartExcelDto>();
            for (String datetimeStr : datetimeList) {
                HostChartExcelDto dto = new HostChartExcelDto();
                dto.setDatetime(datetimeStr);
                if (null != cpuStateMap.get(datetimeStr)) {
                    dto.setCpuPer(((CpuState)cpuStateMap.get(datetimeStr)).getSys());
                    dto.setProcsNum(((CpuState)cpuStateMap.get(datetimeStr)).getProcsNum());
                }
                if (null != memStateMap.get(datetimeStr)) {
                    dto.setMemPer(((MemState)memStateMap.get(datetimeStr)).getUsePer());
                }
                if (null != netIoStateMap.get(datetimeStr)) {
                    dto.setDropin(((NetIoState)netIoStateMap.get(datetimeStr)).getDropin());
                    dto.setDropout(((NetIoState)netIoStateMap.get(datetimeStr)).getDropout());
                    dto.setRxbyt(((NetIoState)netIoStateMap.get(datetimeStr)).getRxbyt());
                    dto.setTxbyt(((NetIoState)netIoStateMap.get(datetimeStr)).getTxbyt());
                    dto.setRxpck(((NetIoState)netIoStateMap.get(datetimeStr)).getRxpck());
                    dto.setTxpck(((NetIoState)netIoStateMap.get(datetimeStr)).getTxpck());
                    dto.setNetConnections(((NetIoState)netIoStateMap.get(datetimeStr)).getNetConnections());
                }
                if (null != ysLoadSstateMap.get(datetimeStr)) {
                    dto.setOneLoad(((SysLoadState)ysLoadSstateMap.get(datetimeStr)).getOneLoad());
                    dto.setFiveLoad(((SysLoadState)ysLoadSstateMap.get(datetimeStr)).getFiveLoad());
                    dto.setFifteenLoad(((SysLoadState)ysLoadSstateMap.get(datetimeStr)).getFifteenLoad());
                }
                excelChartList.add(dto);
            }
            String string = params.get("hostname").toString();
            String fileName = DateUtil.getCurrentDateTimeNoChar() + "_" + string + "\u8d8b\u52bf\u56fe.xlsx";
            response.setContentType("application/vnd.ms-exce");
            response.setCharacterEncoding("utf-8");
            String encodedFileName = URLEncoder.encode(fileName, "UTF-8");
            response.addHeader("Content-Disposition", "filename=" + encodedFileName);
            EasyExcel.write((OutputStream)response.getOutputStream(), HostChartExcelDto.class).sheet("sheet").doWrite(excelChartList);
        }
        catch (Exception e) {
            logger.error("\u4e3b\u673a\u8d8b\u52bf\u56fe\u5bfc\u51faexcel\u9519\u8bef", (Throwable)e);
        }
        finally {
            try {
                if (out != null) {
                    out.close();
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void exportHostListExcel(List<SystemInfo> systemInfoList, HttpServletResponse response) {
        OutputStream out = null;
        try {
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
            String fileName = DateUtil.getCurrentDateTimeNoChar() + "_\u6240\u6709\u4e3b\u673a\u57fa\u7840\u4fe1\u606f\u5217\u8868.xlsx";
            response.setContentType("application/vnd.ms-exce");
            response.setCharacterEncoding("utf-8");
            String encodedFileName = URLEncoder.encode(fileName, "UTF-8");
            response.addHeader("Content-Disposition", "filename=" + encodedFileName);
            EasyExcel.write((OutputStream)response.getOutputStream(), HostListExcelDto.class).sheet("sheet").doWrite(excelList);
        }
        catch (Exception e) {
            logger.error("\u4e3b\u673a\u5217\u8868\u5bfc\u51faexcel\u9519\u8bef", (Throwable)e);
        }
        finally {
            try {
                if (out != null) {
                    out.close();
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void exportAppExcel(Map<String, Object> params, HttpServletResponse response, AppInfo appInfo) {
        OutputStream out = null;
        try {
            List<AppState> appStateList = this.appStateService.selectAllByParams(params);
            ArrayList<AppExcelChartDto> excelChartList = new ArrayList<AppExcelChartDto>();
            for (AppState appState : appStateList) {
                AppExcelChartDto appExcelChartDto = new AppExcelChartDto();
                appExcelChartDto.setDatetime(appState.getDateStr());
                appExcelChartDto.setCpuPer(appState.getCpuPer());
                appExcelChartDto.setMemPer(appState.getMemPer());
                appExcelChartDto.setThreadsNum(appState.getThreadsNum());
                appExcelChartDto.setNetConnections(appState.getNetConnections());
                excelChartList.add(appExcelChartDto);
            }
            String fileName = DateUtil.getCurrentDateTimeNoChar() + "_\u8fdb\u7a0b\u76d1\u63a7" + appInfo.getAppName() + "\u8d8b\u52bf\u56fe.xlsx";
            response.setContentType("application/vnd.ms-exce");
            response.setCharacterEncoding("utf-8");
            String encodedFileName = URLEncoder.encode(fileName, "UTF-8");
            response.addHeader("Content-Disposition", "filename=" + encodedFileName);
            EasyExcel.write((OutputStream)response.getOutputStream(), AppExcelChartDto.class).sheet("sheet").doWrite(excelChartList);
        }
        catch (Exception e) {
            logger.error("\u8fdb\u7a0b\u8d8b\u52bf\u56fe\u5bfc\u51faexcel\u9519\u8bef", (Throwable)e);
        }
        finally {
            try {
                if (out != null) {
                    out.close();
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void exportHeathExcel(Map<String, Object> params, HttpServletResponse response, HeathMonitor heathMonitor) {
        OutputStream out = null;
        try {
            List<HeathState> appStateList = this.heathStateService.selectAllByParams(params);
            ArrayList<HeathExcelChartDto> excelChartList = new ArrayList<HeathExcelChartDto>();
            for (HeathState heathState : appStateList) {
                HeathExcelChartDto heathExcelChartDto = new HeathExcelChartDto();
                heathExcelChartDto.setDatetime(heathState.getDateStr());
                heathExcelChartDto.setResTimes(heathState.getResTimes());
                excelChartList.add(heathExcelChartDto);
            }
            String fileName = DateUtil.getCurrentDateTimeNoChar() + "_\u670d\u52a1\u63a5\u53e3\u76d1\u63a7" + heathMonitor.getAppName() + "\u8d8b\u52bf\u56fe.xlsx";
            response.setContentType("application/vnd.ms-exce");
            response.setCharacterEncoding("utf-8");
            String encodedFileName = URLEncoder.encode(fileName, "UTF-8");
            response.addHeader("Content-Disposition", "filename=" + encodedFileName);
            EasyExcel.write((OutputStream)response.getOutputStream(), HeathExcelChartDto.class).sheet("sheet").doWrite(excelChartList);
        }
        catch (Exception e) {
            logger.error("\u670d\u52a1\u63a5\u53e3\u8d8b\u52bf\u56fe\u5bfc\u51faexcel\u9519\u8bef", (Throwable)e);
        }
        finally {
            try {
                if (out != null) {
                    out.close();
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void exportDceExcel(Map<String, Object> params, HttpServletResponse response, DceInfo dceInfo) {
        OutputStream out = null;
        try {
            List<DceState> appStateList = this.dceStateService.selectAllByParams(params);
            ArrayList<DceExcelChartDto> excelChartList = new ArrayList<DceExcelChartDto>();
            for (DceState dceState : appStateList) {
                DceExcelChartDto dceExcelChartDto = new DceExcelChartDto();
                dceExcelChartDto.setDatetime(dceState.getDateStr());
                dceExcelChartDto.setResTimes(dceState.getResTimes());
                excelChartList.add(dceExcelChartDto);
            }
            String fileName = DateUtil.getCurrentDateTimeNoChar() + "_ping\u76d1\u63a7" + dceInfo.getHostname() + "\u8d8b\u52bf\u56fe.xlsx";
            response.setContentType("application/vnd.ms-exce");
            response.setCharacterEncoding("utf-8");
            String encodedFileName = URLEncoder.encode(fileName, "UTF-8");
            response.addHeader("Content-Disposition", "filename=" + encodedFileName);
            EasyExcel.write((OutputStream)response.getOutputStream(), DceExcelChartDto.class).sheet("sheet").doWrite(excelChartList);
        }
        catch (Exception e) {
            logger.error("ping\u8bbe\u5907\u8d8b\u52bf\u56fe\u5bfc\u51faexcel\u9519\u8bef", (Throwable)e);
        }
        finally {
            try {
                if (out != null) {
                    out.close();
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void exportSnmpExcel(Map<String, Object> params, HttpServletResponse response, SnmpInfo snmpInfo) {
        OutputStream out = null;
        try {
            List<SnmpState> snmpStateList = this.snmpStateService.selectAllByParams(params);
            ArrayList<SnmpExcelChartDto> excelChartList = new ArrayList<SnmpExcelChartDto>();
            Double recvAvgTmp = 0.0;
            Double sentAvgTmp = 0.0;
            for (SnmpState snmpState : snmpStateList) {
                SnmpExcelChartDto snmpExcelChartDto = new SnmpExcelChartDto();
                recvAvgTmp = this.snmpStateService.avgSpeedToMB(snmpState.getRecvAvgDouble(), "KB");
                sentAvgTmp = this.snmpStateService.avgSpeedToMB(snmpState.getSentAvgDouble(), "KB");
                snmpExcelChartDto.setSentAvg(String.valueOf(sentAvgTmp));
                snmpExcelChartDto.setRecvAvg(String.valueOf(recvAvgTmp));
                snmpExcelChartDto.setMemPer(snmpState.getMemPer());
                snmpExcelChartDto.setCpuPer(snmpState.getCpuPer());
                snmpExcelChartDto.setDatetime(snmpState.getDateStr());
                snmpExcelChartDto.setTemperatureValue(snmpState.getTemperatureValue());
                excelChartList.add(snmpExcelChartDto);
            }
            String fileName = DateUtil.getCurrentDateTimeNoChar() + "_SNMP\u76d1\u63a7" + snmpInfo.getHostname() + "\u8d8b\u52bf\u56fe.xlsx";
            response.setContentType("application/vnd.ms-exce");
            response.setCharacterEncoding("utf-8");
            String encodedFileName = URLEncoder.encode(fileName, "UTF-8");
            response.addHeader("Content-Disposition", "filename=" + encodedFileName);
            EasyExcel.write((OutputStream)response.getOutputStream(), SnmpExcelChartDto.class).sheet("sheet").doWrite(excelChartList);
        }
        catch (Exception e) {
            logger.error("snmp\u8bbe\u5907\u8d8b\u52bf\u56fe\u5bfc\u51faexcel\u9519\u8bef", (Throwable)e);
        }
        finally {
            try {
                if (out != null) {
                    out.close();
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void exportCustomExcel(Map<String, Object> params, HttpServletResponse response, CustomInfo customInfo) {
        OutputStream out = null;
        try {
            List<CustomState> customStateList = this.customStateService.selectAllByParams(params);
            ArrayList<CustomExcelChartDto> excelChartList = new ArrayList<CustomExcelChartDto>();
            for (CustomState customState : customStateList) {
                CustomExcelChartDto customExcelChartDto = new CustomExcelChartDto();
                customExcelChartDto.setDatetime(customState.getDateStr());
                customExcelChartDto.setCustomValue(customState.getCustomValue());
                excelChartList.add(customExcelChartDto);
            }
            String fileName = DateUtil.getCurrentDateTimeNoChar() + "_\u81ea\u5b9a\u4e49\u76d1\u63a7\u9879" + customInfo.getCustomName() + "\u8d8b\u52bf\u56fe.xlsx";
            response.setContentType("application/vnd.ms-exce");
            response.setCharacterEncoding("utf-8");
            String encodedFileName = URLEncoder.encode(fileName, "UTF-8");
            response.addHeader("Content-Disposition", "filename=" + encodedFileName);
            EasyExcel.write((OutputStream)response.getOutputStream(), CustomExcelChartDto.class).sheet("sheet").doWrite(excelChartList);
        }
        catch (Exception e) {
            logger.error("\u81ea\u5b9a\u4e49\u76d1\u63a7\u9879\u503c\u8d8b\u52bf\u56fe\u5bfc\u51faexcel\u9519\u8bef", (Throwable)e);
        }
        finally {
            try {
                if (out != null) {
                    out.close();
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void exportDockerExcel(Map<String, Object> params, HttpServletResponse response, DockerInfo dockerInfo) {
        OutputStream out = null;
        try {
            List<DockerState> dockerStateList = this.dockerStateService.selectAllByParams(params);
            ArrayList<DockerExcelChartDto> excelChartList = new ArrayList<DockerExcelChartDto>();
            for (DockerState dockerState : dockerStateList) {
                DockerExcelChartDto dockerExcelChartDto = new DockerExcelChartDto();
                dockerExcelChartDto.setDatetime(dockerState.getDateStr());
                dockerExcelChartDto.setMemPer(dockerState.getMemPer());
                dockerExcelChartDto.setCpuPer(dockerState.getCpuPer());
                excelChartList.add(dockerExcelChartDto);
            }
            String fileName = DateUtil.getCurrentDateTimeNoChar() + "_docker\u76d1\u63a7" + dockerInfo.getDockerName() + "\u8d8b\u52bf\u56fe.xlsx";
            response.setContentType("application/vnd.ms-exce");
            response.setCharacterEncoding("utf-8");
            String encodedFileName = URLEncoder.encode(fileName, "UTF-8");
            response.addHeader("Content-Disposition", "filename=" + encodedFileName);
            EasyExcel.write((OutputStream)response.getOutputStream(), DockerExcelChartDto.class).sheet("sheet").doWrite(excelChartList);
        }
        catch (Exception e) {
            logger.error("docker\u8d8b\u52bf\u56fe\u5bfc\u51faexcel\u9519\u8bef", (Throwable)e);
        }
        finally {
            try {
                if (out != null) {
                    out.close();
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void exportDbTableExcel(Map<String, Object> params, HttpServletResponse response, DbTable dbTableInfo) {
        OutputStream out = null;
        try {
            List<DbTableCount> dbTableCountList = this.dbTableCountService.selectAllByParams(params);
            ArrayList<DbTableExcelChartDto> excelChartList = new ArrayList<DbTableExcelChartDto>();
            for (DbTableCount dbTableCount : dbTableCountList) {
                DbTableExcelChartDto dbTableExcelChartDto = new DbTableExcelChartDto();
                if (null != dbTableCount.getCreateTime()) {
                    dbTableExcelChartDto.setDatetime(DateUtil.getDateTimeString(dbTableCount.getCreateTime()));
                }
                dbTableExcelChartDto.setTableCount(dbTableCount.getTableCount());
                excelChartList.add(dbTableExcelChartDto);
            }
            String fileName = DateUtil.getCurrentDateTimeNoChar() + "_\u6570\u636e\u8868\u76d1\u63a7" + dbTableInfo.getRemark() + "\u8d8b\u52bf\u56fe.xlsx";
            response.setContentType("application/vnd.ms-exce");
            response.setCharacterEncoding("utf-8");
            String encodedFileName = URLEncoder.encode(fileName, "UTF-8");
            response.addHeader("Content-Disposition", "filename=" + encodedFileName);
            EasyExcel.write((OutputStream)response.getOutputStream(), DbTableExcelChartDto.class).sheet("sheet").doWrite(excelChartList);
        }
        catch (Exception e) {
            logger.error("\u6570\u636e\u8868\u8d8b\u52bf\u56fe\u5bfc\u51faexcel\u9519\u8bef", (Throwable)e);
        }
        finally {
            try {
                if (out != null) {
                    out.close();
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void exportFileWarnStateExcel(FileWarnInfo fileWarnInfo, Map<String, Object> params, HttpServletResponse response) {
        OutputStream out = null;
        try {
            List<FileWarnState> fileWarnStateList = this.fileWarnStateService.selectAllByParams(params);
            ArrayList<FileWarnStateExcelDto> excelChartList = new ArrayList<FileWarnStateExcelDto>();
            for (FileWarnState fileWarnState : fileWarnStateList) {
                FileWarnStateExcelDto fileWarnStateExcelDto = new FileWarnStateExcelDto();
                if (null != fileWarnState.getCreateTime()) {
                    fileWarnStateExcelDto.setDatetime(DateUtil.getDateTimeString(fileWarnState.getCreateTime()));
                }
                fileWarnStateExcelDto.setWarContent(fileWarnState.getWarContent());
                excelChartList.add(fileWarnStateExcelDto);
            }
            String fileName = fileWarnInfo.getHostname() + "_\u65e5\u5fd7\u76d1\u63a7.xlsx";
            response.setContentType("application/vnd.ms-exce");
            response.setCharacterEncoding("utf-8");
            String encodedFileName = URLEncoder.encode(fileName, "UTF-8");
            response.addHeader("Content-Disposition", "filename=" + encodedFileName);
            EasyExcel.write((OutputStream)response.getOutputStream(), FileWarnStateExcelDto.class).sheet("sheet").doWrite(excelChartList);
        }
        catch (Exception e) {
            logger.error("\u65e5\u5fd7\u76d1\u63a7\u5217\u8868\u5bfc\u51faexcel\u9519\u8bef", (Throwable)e);
        }
        finally {
            try {
                if (out != null) {
                    out.close();
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void exportEquipmentListExcel(List<Equipment> equipmentList, HttpServletResponse response) {
        OutputStream out = null;
        try {
            ArrayList<EquipmentExcelDto> excelList = new ArrayList<EquipmentExcelDto>();
            for (Equipment equipment : equipmentList) {
                EquipmentExcelDto dto = new EquipmentExcelDto();
                dto.setCreateTime(DateUtil.getDateTimeString(equipment.getCreateTime()));
                dto.setGroupId(equipment.getGroupId());
                dto.setRemark(equipment.getRemark());
                dto.setAccount(equipment.getAccount());
                dto.setCode(equipment.getCode());
                dto.setCaigouDate(equipment.getCaigouDate());
                dto.setDept(equipment.getDept());
                dto.setGongyingshang(equipment.getGongyingshang());
                dto.setName(equipment.getName());
                dto.setPerson(equipment.getPerson());
                dto.setPrice(equipment.getPrice() + "");
                dto.setWeibaoDate(equipment.getWeibaoDate());
                dto.setXinghao(equipment.getXinghao());
                excelList.add(dto);
            }
            String fileName = DateUtil.getCurrentDateTimeNoChar() + "_\u8d44\u4ea7\u5217\u8868.xlsx";
            response.setContentType("application/vnd.ms-exce");
            response.setCharacterEncoding("utf-8");
            String encodedFileName = URLEncoder.encode(fileName, "UTF-8");
            response.addHeader("Content-Disposition", "filename=" + encodedFileName);
            EasyExcel.write((OutputStream)response.getOutputStream(), EquipmentExcelDto.class).sheet("sheet").doWrite(excelList);
        }
        catch (Exception e) {
            logger.error("\u8d44\u4ea7\u5217\u8868\u5bfc\u51faexcel\u9519\u8bef", (Throwable)e);
        }
        finally {
            try {
                if (out != null) {
                    out.close();
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void exportPasswdInfoListExcel(List<PasswdInfo> passwdInfoList, HttpServletResponse response) {
        OutputStream out = null;
        try {
            ArrayList<PasswdInfoExcelDto> excelList = new ArrayList<PasswdInfoExcelDto>();
            for (PasswdInfo passwdInfo : passwdInfoList) {
                PasswdInfoExcelDto dto = new PasswdInfoExcelDto();
                dto.setCreateTime(DateUtil.getDateTimeString(passwdInfo.getCreateTime()));
                dto.setGroupId(passwdInfo.getGroupId());
                dto.setHostRemark(passwdInfo.getHostRemark());
                dto.setAccount(passwdInfo.getAccount());
                dto.setHostname(passwdInfo.getHostname());
                dto.setHostPasswd(passwdInfo.getHostPasswd());
                dto.setHostMark(passwdInfo.getHostMark());
                dto.setHostAccount(passwdInfo.getHostAccount());
                excelList.add(dto);
            }
            String fileName = DateUtil.getCurrentDateTimeNoChar() + "_\u8bbe\u5907\u8d26\u53f7\u5217\u8868.xlsx";
            response.setContentType("application/vnd.ms-exce");
            response.setCharacterEncoding("utf-8");
            String encodedFileName = URLEncoder.encode(fileName, "UTF-8");
            response.addHeader("Content-Disposition", "filename=" + encodedFileName);
            EasyExcel.write((OutputStream)response.getOutputStream(), PasswdInfoExcelDto.class).sheet("sheet").doWrite(excelList);
        }
        catch (Exception e) {
            logger.error("\u8bbe\u5907\u8d26\u53f7\u5217\u8868\u5bfc\u51faexcel\u9519\u8bef", (Throwable)e);
        }
        finally {
            try {
                if (out != null) {
                    out.close();
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void exportDceInfoListExcel(List<DceInfo> dceInfoList, HttpServletResponse response) {
        OutputStream out = null;
        try {
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
            String fileName = DateUtil.getCurrentDateTimeNoChar() + "_ping\u76d1\u63a7\u5217\u8868.xlsx";
            response.setContentType("application/vnd.ms-exce");
            response.setCharacterEncoding("utf-8");
            String encodedFileName = URLEncoder.encode(fileName, "UTF-8");
            response.addHeader("Content-Disposition", "filename=" + encodedFileName);
            EasyExcel.write((OutputStream)response.getOutputStream(), DceInfoListExcelDto.class).sheet("sheet").doWrite(excelList);
        }
        catch (Exception e) {
            logger.error("ping\u5217\u8868\u5bfc\u51faexcel\u9519\u8bef", (Throwable)e);
        }
        finally {
            try {
                if (out != null) {
                    out.close();
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

