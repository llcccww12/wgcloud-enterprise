/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.github.pagehelper.PageInfo;
import com.wgcloud.config.CommonConfig;
import com.wgcloud.config.LevelConfig;
import com.wgcloud.config.MailConfig;
import com.wgcloud.dto.ChartInfo;
import com.wgcloud.dto.MessageDto;
import com.wgcloud.entity.AppInfo;
import com.wgcloud.entity.CpuState;
import com.wgcloud.entity.CpuTemperatures;
import com.wgcloud.entity.CustomInfo;
import com.wgcloud.entity.DbInfo;
import com.wgcloud.entity.DceInfo;
import com.wgcloud.entity.DiskIo;
import com.wgcloud.entity.DiskSmart;
import com.wgcloud.entity.DiskState;
import com.wgcloud.entity.DockerInfo;
import com.wgcloud.entity.FileSafe;
import com.wgcloud.entity.FileWarnInfo;
import com.wgcloud.entity.HeathMonitor;
import com.wgcloud.entity.HostMacInfo;
import com.wgcloud.entity.MemState;
import com.wgcloud.entity.NetIoState;
import com.wgcloud.entity.PortInfo;
import com.wgcloud.entity.SysLoadState;
import com.wgcloud.entity.SystemInfo;
import com.wgcloud.entity.TaskJobInfo;
import com.wgcloud.service.AppExceptionInfoService;
import com.wgcloud.service.AppInfoService;
import com.wgcloud.service.CpuStateService;
import com.wgcloud.service.CpuTemperaturesService;
import com.wgcloud.service.CustomInfoService;
import com.wgcloud.service.DbInfoService;
import com.wgcloud.service.DbTableService;
import com.wgcloud.service.DceInfoService;
import com.wgcloud.service.DiskIoService;
import com.wgcloud.service.DiskSmartService;
import com.wgcloud.service.DiskStateService;
import com.wgcloud.service.DockerInfoService;
import com.wgcloud.service.FileSafeService;
import com.wgcloud.service.FileWarnInfoService;
import com.wgcloud.service.FileWarnStateService;
import com.wgcloud.service.FtpInfoService;
import com.wgcloud.service.HeathMonitorService;
import com.wgcloud.service.HostDiskPerService;
import com.wgcloud.service.HostGroupService;
import com.wgcloud.service.HostMacInfoService;
import com.wgcloud.service.HostUsersService;
import com.wgcloud.service.LogInfoService;
import com.wgcloud.service.MemStateService;
import com.wgcloud.service.NetIoStateService;
import com.wgcloud.service.PortInfoService;
import com.wgcloud.service.SnmpInfoService;
import com.wgcloud.service.SysLoadStateService;
import com.wgcloud.service.SystemInfoService;
import com.wgcloud.service.TaskJobInfoService;
import com.wgcloud.util.DateUtil;
import com.wgcloud.util.FormatUtil;
import com.wgcloud.util.HostUtil;
import com.wgcloud.util.msg.WarnOtherUtil;
import com.wgcloud.util.staticvar.StaticKeys;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

@Service
public class DashboardService {
    private static final Logger logger = LoggerFactory.getLogger(DashboardService.class);
    @Resource
    DbTableService dbTableService;
    @Resource
    SnmpInfoService snmpInfoService;
    @Resource
    FileSafeService fileSafeService;
    @Resource
    DceInfoService dceInfoService;
    @Resource
    DbInfoService dbInfoService;
    @Resource
    SystemInfoService systemInfoService;
    @Resource
    AppInfoService appInfoService;
    @Resource
    FileWarnInfoService fileWarnInfoService;
    @Resource
    FileWarnStateService fileWarnStateService;
    @Resource
    DockerInfoService dockerInfoService;
    @Resource
    LogInfoService logInfoService;
    @Autowired
    HeathMonitorService heathMonitorService;
    @Autowired
    HostGroupService hostInfoService;
    @Autowired
    CpuStateService cpuStateService;
    @Autowired
    MemStateService memStateService;
    @Autowired
    SysLoadStateService sysLoadStateService;
    @Autowired
    NetIoStateService netIoStateService;
    @Autowired
    DiskIoService diskIoService;
    @Autowired
    DiskStateService diskStateService;
    @Autowired
    DiskSmartService diskSmartService;
    @Resource
    CpuTemperaturesService cpuTemperaturesService;
    @Autowired
    private HostMacInfoService hostMacInfoService;
    @Resource
    private CustomInfoService customInfoService;
    @Resource
    private PortInfoService portInfoService;
    @Resource
    private HostUsersService hostUsersService;
    @Resource
    private FtpInfoService ftpInfoService;
    @Resource
    private HostGroupService hostGroupService;
    @Resource
    private TaskJobInfoService taskJobInfoService;
    @Resource
    private AppExceptionInfoService appExceptionInfoService;
    @Resource
    private HostDiskPerService hostDiskPerService;
    @Autowired
    CommonConfig commonConfig;
    @Autowired
    MailConfig mailConfig;
    @Autowired
    LevelConfig levelConfig;

    public List<MessageDto> getAmList() {
        MessageDto dto1 = new MessageDto();
        dto1.setCode("am1");
        dto1.setMsg("\u6700\u8fd11\u5c0f\u65f6");
        MessageDto dto2 = new MessageDto();
        dto2.setCode("am2");
        dto2.setMsg("\u6700\u8fd12\u5c0f\u65f6");
        MessageDto dto3 = new MessageDto();
        dto3.setCode("am3");
        dto3.setMsg("\u6700\u8fd16\u5c0f\u65f6");
        MessageDto dto4 = new MessageDto();
        dto4.setCode("am4");
        dto4.setMsg("\u6700\u8fd112\u5c0f\u65f6");
        MessageDto dto5 = new MessageDto();
        dto5.setCode("am5");
        dto5.setMsg("\u6700\u8fd124\u5c0f\u65f6");
        ArrayList<MessageDto> timeList = new ArrayList<MessageDto>();
        timeList.add(dto1);
        timeList.add(dto2);
        timeList.add(dto3);
        timeList.add(dto4);
        timeList.add(dto5);
        return timeList;
    }

    public void setDateParam(String am, String startTime, String endTime, Map<String, Object> params, Model model) {
        if ("null".equals(am)) {
            am = "";
        }
        try {
            String nowTime = DateUtil.getCurrentDateTime();
            if (!StringUtils.isEmpty((CharSequence)am) && (StringUtils.isEmpty((CharSequence)startTime) || StringUtils.isEmpty((CharSequence)endTime))) {
                if ("am1".equals(am)) {
                    params.put("startTime", DateUtil.beforeHourToNowDate(1));
                    params.put("endTime", nowTime);
                }
                if ("am2".equals(am)) {
                    params.put("startTime", DateUtil.beforeHourToNowDate(2));
                    params.put("endTime", nowTime);
                }
                if ("am3".equals(am)) {
                    params.put("startTime", DateUtil.beforeHourToNowDate(6));
                    params.put("endTime", nowTime);
                }
                if ("am4".equals(am)) {
                    params.put("startTime", DateUtil.beforeHourToNowDate(12));
                    params.put("endTime", nowTime);
                }
                if ("am5".equals(am)) {
                    params.put("startTime", DateUtil.beforeHourToNowDate(24));
                    params.put("endTime", nowTime);
                }
                model.addAttribute("am", (Object)am);
                return;
            }
            if (StringUtils.isEmpty((CharSequence)am) && !StringUtils.isEmpty((CharSequence)startTime) && !StringUtils.isEmpty((CharSequence)endTime)) {
                params.put("startTime", startTime + ":00");
                params.put("endTime", endTime + ":59");
                model.addAttribute("startTime", (Object)startTime);
                model.addAttribute("endTime", (Object)endTime);
                if (!StaticKeys.LICENSE_STATE.equals("1")) {
                    Date dateTmp = DateUtil.getDate(startTime + ":00");
                    Date nowDate = new Date();
                    long diff = 432000000L;
                    if (nowDate.getTime() - dateTmp.getTime() > diff) {
                        model.addAttribute("msg", "\u4e2a\u4eba\u7248\u6700\u591a\u67e5\u770b\u6700\u8fd15\u5929\u6570\u636e\uff0c\u82e5\u60a8\u9700\u8981\u5e2e\u52a9\uff0c\u8bf7\u8054\u7cfb\u6211\u4eec\u5347\u7ea7\u5230\u4e13\u4e1a\u7248");
                    }
                }
                return;
            }
            params.put("startTime", DateUtil.beforeHourToNowDate(1));
            params.put("endTime", nowTime);
            model.addAttribute("am", "am1");
        }
        catch (Exception e) {
            logger.error("\u67e5\u770b\u56fe\u8868\u7ec4\u88c5\u65e5\u671f\u67e5\u8be2\u6761\u4ef6\u9519\u8bef", (Throwable)e);
        }
    }

    public void setDateParam(String date, Map<String, Object> params) {
        params.put("startTime", date + " 00:00:00");
        params.put("endTime", date + " 23:59:59");
    }

    public void setDashboardTopData(Model model, int totalSystemInfoSize, HttpServletRequest request) {
        try {
            HashMap<String, Object> params = new HashMap<String, Object>();
            HostUtil.addAccountquery(request, params);
            int totalSizeApp = this.appInfoService.countByParams(params);
            model.addAttribute("totalSizeApp", (Object)totalSizeApp);
            params.put("state", "1");
            int onLineAppSize = this.appInfoService.countByParams(params);
            model.addAttribute("onLineAppSize", (Object)onLineAppSize);
            double onLineAppPer = 0.0;
            if (totalSizeApp != 0) {
                onLineAppPer = (double)onLineAppSize / (double)totalSizeApp;
            }
            model.addAttribute("onLineAppPer", (Object)FormatUtil.formatDouble(onLineAppPer * 100.0, 2));
            params.clear();
            HostUtil.addAccountquery(request, params);
            int portSize = this.portInfoService.countByParams(params);
            model.addAttribute("portSize", (Object)portSize);
            params.put("state", "1");
            int portOnLineSize = this.portInfoService.countByParams(params);
            model.addAttribute("portOnLineSize", (Object)portOnLineSize);
            double portOnLinePer = 0.0;
            if (portSize != 0) {
                portOnLinePer = (double)portOnLineSize / (double)portSize;
            }
            model.addAttribute("portOnLinePer", (Object)FormatUtil.formatDouble(portOnLinePer * 100.0, 2));
            params.clear();
            HostUtil.addAccountquery(request, params);
            int heathSize = this.heathMonitorService.countByParams(params);
            model.addAttribute("heathSize", (Object)heathSize);
            params.put("heathStatus", "200");
            int heath200Size = this.heathMonitorService.countByParams(params);
            model.addAttribute("heath200Size", (Object)heath200Size);
            double heathOnLinePer = 0.0;
            if (heathSize != 0) {
                heathOnLinePer = (double)heath200Size / (double)heathSize;
            }
            model.addAttribute("heathOnLinePer", (Object)FormatUtil.formatDouble(heathOnLinePer * 100.0, 2));
            params.clear();
            HostUtil.addAccountquery(request, params);
            int dockerSize = this.dockerInfoService.countByParams(params);
            model.addAttribute("dockerSize", (Object)dockerSize);
            params.put("state", "1");
            int dockerOnLineSize = this.dockerInfoService.countByParams(params);
            model.addAttribute("dockerOnLineSize", (Object)dockerOnLineSize);
            double dockerOnLinePer = 0.0;
            if (dockerSize != 0) {
                dockerOnLinePer = (double)dockerOnLineSize / (double)dockerSize;
            }
            model.addAttribute("dockerOnLinePer", (Object)FormatUtil.formatDouble(dockerOnLinePer * 100.0, 2));
            params.clear();
            HostUtil.addAccountquery(request, params);
            int dceSize = this.dceInfoService.countByParams(params);
            model.addAttribute("dceSize", (Object)dceSize);
            params.put("resTimes", -1);
            int dceDownSize = this.dceInfoService.countByParams(params);
            double dceOnLinePer = 0.0;
            if (dceSize != 0) {
                dceOnLinePer = (double)(dceSize - dceDownSize) / (double)dceSize;
            }
            model.addAttribute("dceOnLineSize", (Object)(dceSize - dceDownSize));
            model.addAttribute("dceOnLinePer", (Object)FormatUtil.formatDouble(dceOnLinePer * 100.0, 2));
            params.clear();
            HostUtil.addAccountquery(request, params);
            int fileWarnSize = this.fileWarnInfoService.countByParams(params);
            model.addAttribute("fileWarnSize", (Object)fileWarnSize);
            params.put("active", "1");
            int fileWarnActiveSize = this.fileWarnInfoService.countByParams(params);
            double fileWarnActivePer = 0.0;
            if (fileWarnSize != 0) {
                fileWarnActivePer = (double)fileWarnActiveSize / (double)fileWarnSize;
            }
            model.addAttribute("fileWarnActiveSize", (Object)fileWarnActiveSize);
            model.addAttribute("fileWarnActivePer", (Object)FormatUtil.formatDouble(fileWarnActivePer * 100.0, 2));
            params.clear();
            HostUtil.addAccountquery(request, params);
            int dbInfoSize = this.dbInfoService.countByParams(params);
            model.addAttribute("dbInfoSize", (Object)dbInfoSize);
            params.put("dbState", "1");
            int dbInfoOnLineSize = this.dbInfoService.countByParams(params);
            model.addAttribute("dbInfoOnLineSize", (Object)dbInfoOnLineSize);
            double dbInfoOnLinePer = 0.0;
            if (dbInfoSize != 0) {
                dbInfoOnLinePer = (double)dbInfoOnLineSize / (double)dbInfoSize;
            }
            model.addAttribute("dbInfoOnLinePer", (Object)FormatUtil.formatDouble(dbInfoOnLinePer * 100.0, 2));
            this.setHuantu(model, totalSystemInfoSize, totalSizeApp, dockerSize, portSize, dceSize, fileWarnSize, dbInfoSize, heathSize, request);
        }
        catch (Exception e) {
            logger.error("\u76d1\u63a7\u6982\u8981\u9875\u9762\u8bbe\u7f6e\u56fe\u8868\u6570\u636e\u9519\u8bef", (Throwable)e);
        }
    }

    public void setHuantu(Model model, int totalSystemInfoSize, int totalSizeApp, int dockerSize, int portSize, int dceSize, int fileWarnSize, int dbInfoSize, int heathSize, HttpServletRequest request) throws Exception {
        HashMap<String, Object> params = new HashMap<String, Object>();
        HostUtil.addAccountquery(request, params);
        int dbTableSize = this.dbTableService.countByParams(params);
        model.addAttribute("dbTableSize", (Object)dbTableSize);
        params.clear();
        HostUtil.addAccountquery(request, params);
        int snmpInfoSize = this.snmpInfoService.countByParams(params);
        model.addAttribute("snmpInfoSize", (Object)snmpInfoSize);
        params.clear();
        HostUtil.addAccountquery(request, params);
        int customInfoSize = this.customInfoService.countByParams(params);
        model.addAttribute("customInfoSize", (Object)customInfoSize);
        params.clear();
        HostUtil.addAccountquery(request, params);
        int fileSafeSize = this.fileSafeService.countByParams(params);
        model.addAttribute("fileSafeSize", (Object)fileSafeSize);
        params.clear();
        HostUtil.addAccountquery(request, params);
        int ftpInfoSize = this.ftpInfoService.countByParams(params);
        int huantuTotalSize = totalSystemInfoSize + totalSizeApp + dockerSize + portSize + dceSize + fileWarnSize + dbInfoSize + heathSize + dbTableSize + snmpInfoSize + customInfoSize + fileSafeSize + ftpInfoSize;
        double total = huantuTotalSize;
        if (0.0 == total) {
            total = 1.0;
        }
        ArrayList<ChartInfo> chartInfos = new ArrayList<ChartInfo>();
        double totalSystemInfoSizePercent = (double)totalSystemInfoSize / total;
        ChartInfo totalSystemInfoChart = new ChartInfo();
        totalSystemInfoChart.setCount(totalSystemInfoSize);
        totalSystemInfoChart.setItem("\u4e3b\u673a");
        totalSystemInfoChart.setPercent(FormatUtil.formatDouble(totalSystemInfoSizePercent, 2));
        if (totalSystemInfoSize > 0) {
            chartInfos.add(totalSystemInfoChart);
        }
        double totalSizeAppPercent = (double)totalSizeApp / total;
        ChartInfo totalSizeAppChart = new ChartInfo();
        totalSizeAppChart.setCount(totalSizeApp);
        totalSizeAppChart.setItem("\u8fdb\u7a0b");
        totalSizeAppChart.setPercent(FormatUtil.formatDouble(totalSizeAppPercent, 2));
        if (totalSizeApp > 0) {
            chartInfos.add(totalSizeAppChart);
        }
        double dockerSizePercent = (double)dockerSize / total;
        ChartInfo dockerSizeChart = new ChartInfo();
        dockerSizeChart.setCount(dockerSize);
        dockerSizeChart.setItem("DOCKER");
        dockerSizeChart.setPercent(FormatUtil.formatDouble(dockerSizePercent, 2));
        if (dockerSize > 0) {
            chartInfos.add(dockerSizeChart);
        }
        double portSizePercent = (double)portSize / total;
        ChartInfo portSizeChart = new ChartInfo();
        portSizeChart.setCount(portSize);
        portSizeChart.setItem("\u7aef\u53e3");
        portSizeChart.setPercent(FormatUtil.formatDouble(portSizePercent, 2));
        if (portSize > 0) {
            chartInfos.add(portSizeChart);
        }
        double dceSizePercent = (double)dceSize / total;
        ChartInfo dceSizeChart = new ChartInfo();
        dceSizeChart.setCount(dceSize);
        dceSizeChart.setItem("\u7f51\u7edcPING");
        dceSizeChart.setPercent(FormatUtil.formatDouble(dceSizePercent, 2));
        if (dceSize > 0) {
            chartInfos.add(dceSizeChart);
        }
        double snmpInfoPercent = (double)snmpInfoSize / total;
        ChartInfo snmpInfoChart = new ChartInfo();
        snmpInfoChart.setCount(snmpInfoSize);
        snmpInfoChart.setItem("\u7f51\u7edcSNMP");
        snmpInfoChart.setPercent(FormatUtil.formatDouble(snmpInfoPercent, 2));
        if (snmpInfoSize > 0) {
            chartInfos.add(snmpInfoChart);
        }
        double fileWarnSizePercent = (double)fileWarnSize / total;
        ChartInfo fileWarnSizeChart = new ChartInfo();
        fileWarnSizeChart.setCount(fileWarnSize);
        fileWarnSizeChart.setItem("\u65e5\u5fd7");
        fileWarnSizeChart.setPercent(FormatUtil.formatDouble(fileWarnSizePercent, 2));
        if (fileWarnSize > 0) {
            chartInfos.add(fileWarnSizeChart);
        }
        double dbInfoSizePercent = (double)dbInfoSize / total;
        ChartInfo dbInfoSizeChart = new ChartInfo();
        dbInfoSizeChart.setCount(dbInfoSize);
        dbInfoSizeChart.setItem("\u6570\u636e\u5e93");
        dbInfoSizeChart.setPercent(FormatUtil.formatDouble(dbInfoSizePercent, 2));
        if (dbInfoSize > 0) {
            chartInfos.add(dbInfoSizeChart);
        }
        double heathSizePercent = (double)heathSize / total;
        ChartInfo heathSizeChart = new ChartInfo();
        heathSizeChart.setCount(heathSize);
        heathSizeChart.setItem("\u670d\u52a1\u63a5\u53e3");
        heathSizeChart.setPercent(FormatUtil.formatDouble(heathSizePercent, 2));
        if (heathSize > 0) {
            chartInfos.add(heathSizeChart);
        }
        double dbTableSizePercent = (double)dbTableSize / total;
        ChartInfo dbTableSizeChart = new ChartInfo();
        dbTableSizeChart.setCount(dbTableSize);
        dbTableSizeChart.setItem("\u6570\u636e\u8868");
        dbTableSizeChart.setPercent(FormatUtil.formatDouble(dbTableSizePercent, 2));
        if (dbTableSize > 0) {
            chartInfos.add(dbTableSizeChart);
        }
        double customInfoSizePercent = (double)customInfoSize / total;
        ChartInfo customInfoSizePercentChart = new ChartInfo();
        customInfoSizePercentChart.setCount(customInfoSize);
        customInfoSizePercentChart.setItem("\u81ea\u5b9a\u4e49\u76d1\u63a7\u9879");
        customInfoSizePercentChart.setPercent(FormatUtil.formatDouble(customInfoSizePercent, 2));
        if (customInfoSize > 0) {
            chartInfos.add(customInfoSizePercentChart);
        }
        double fileSafeSizePercent = (double)fileSafeSize / total;
        ChartInfo fileSafeSizePercentChart = new ChartInfo();
        fileSafeSizePercentChart.setCount(fileSafeSize);
        fileSafeSizePercentChart.setItem("\u6587\u4ef6\u9632\u7be1\u6539");
        fileSafeSizePercentChart.setPercent(FormatUtil.formatDouble(fileSafeSizePercent, 2));
        if (customInfoSize > 0) {
            chartInfos.add(fileSafeSizePercentChart);
        }
        double ftpInfoSizePercent = (double)ftpInfoSize / total;
        ChartInfo ftpInfoSizePercentChart = new ChartInfo();
        ftpInfoSizePercentChart.setCount(ftpInfoSize);
        ftpInfoSizePercentChart.setItem("FTP");
        ftpInfoSizePercentChart.setPercent(FormatUtil.formatDouble(ftpInfoSizePercent, 2));
        if (ftpInfoSize > 0) {
            chartInfos.add(ftpInfoSizePercentChart);
        }
        model.addAttribute("huantuChartInfos", (Object)JSONUtil.parseArray(chartInfos));
        model.addAttribute("huantuTotalSize", (Object)huantuTotalSize);
        this.setDashboardRightBingtu(model, customInfoSize, fileSafeSize, ftpInfoSize, snmpInfoSize, dbTableSize, request);
    }

    public void setMiddleData(Model model, HttpServletRequest request) {
        try {
            HashMap<String, Object> params = new HashMap<String, Object>();
            HostUtil.addAccountquery(request, params);
            params.put("countBlockNe", "2");
            List<SystemInfo> list = this.systemInfoService.selectAllByParams(params);
            Double maxCpu = 0.0;
            Double avgCpu = 0.0;
            Double minCpu = 1000.0;
            Double sumCpu = 0.0;
            Double maxMem = 0.0;
            Double minMem = 1000.0;
            Double avgMem = 0.0;
            Double sumMem = 0.0;
            Double maxRxbyt = 0.0;
            Double avgRxbyt = 0.0;
            Double minRxbyt = 1000.0;
            Double sumRxbyt = 0.0;
            Double maxTxbyt = 0.0;
            Double minTxbyt = 1000.0;
            Double avgTxbyt = 0.0;
            Double sumTxbyt = 0.0;
            Double maxFiveLoad = 0.0;
            Double avgFiveLoad = 0.0;
            Double minFiveLoad = 1000.0;
            Double sumFiveLoad = 0.0;
            Double maxFifteenLoad = 0.0;
            Double minFifteenLoad = 1000.0;
            Double avgFifteenLoad = 0.0;
            Double sumFifteenLoad = 0.0;
            int systemSize = 0;
            int cpuCoresSum = 0;
            double memSum = 0.0;
            int submitSecondsSum = 0;
            double avgSubmitSeconds = 120.0;
            for (SystemInfo systemInfo : list) {
                ++systemSize;
                if (null != systemInfo.getCpuPer()) {
                    if (systemInfo.getCpuPer() > maxCpu) {
                        maxCpu = systemInfo.getCpuPer();
                    }
                    if (systemInfo.getCpuPer() < minCpu) {
                        minCpu = systemInfo.getCpuPer();
                    }
                    sumCpu = sumCpu + systemInfo.getCpuPer();
                }
                if (null != systemInfo.getMemPer()) {
                    if (systemInfo.getMemPer() > maxMem) {
                        maxMem = systemInfo.getMemPer();
                    }
                    if (systemInfo.getMemPer() < minMem) {
                        minMem = systemInfo.getMemPer();
                    }
                    sumMem = sumMem + systemInfo.getMemPer();
                }
                if (null != systemInfo.getRxbyt()) {
                    if (Double.valueOf(systemInfo.getRxbyt()) > maxRxbyt) {
                        maxRxbyt = Double.valueOf(systemInfo.getRxbyt());
                    }
                    if (Double.valueOf(systemInfo.getRxbyt()) < minRxbyt) {
                        minRxbyt = Double.valueOf(systemInfo.getRxbyt());
                    }
                    sumRxbyt = sumRxbyt + Double.valueOf(systemInfo.getRxbyt());
                }
                if (null != systemInfo.getTxbyt()) {
                    if (Double.valueOf(systemInfo.getTxbyt()) > maxTxbyt) {
                        maxTxbyt = Double.valueOf(systemInfo.getTxbyt());
                    }
                    if (Double.valueOf(systemInfo.getTxbyt()) < minTxbyt) {
                        minTxbyt = Double.valueOf(systemInfo.getTxbyt());
                    }
                    sumTxbyt = sumTxbyt + Double.valueOf(systemInfo.getTxbyt());
                }
                if (null != systemInfo.getFiveLoad()) {
                    if (Double.valueOf(systemInfo.getFiveLoad()) > maxFiveLoad) {
                        maxFiveLoad = (double)systemInfo.getFiveLoad();
                    }
                    if (Double.valueOf(systemInfo.getFiveLoad()) < minFiveLoad) {
                        minFiveLoad = (double)systemInfo.getFiveLoad();
                    }
                    sumFiveLoad = sumFiveLoad + Double.valueOf(systemInfo.getFiveLoad());
                }
                if (null != systemInfo.getFifteenLoad()) {
                    if (Double.valueOf(systemInfo.getFifteenLoad()) > maxFifteenLoad) {
                        maxFifteenLoad = (double)systemInfo.getFifteenLoad();
                    }
                    if (Double.valueOf(systemInfo.getFifteenLoad()) < minFifteenLoad) {
                        minFifteenLoad = (double)systemInfo.getFifteenLoad();
                    }
                    sumFifteenLoad = sumFifteenLoad + Double.valueOf(systemInfo.getFifteenLoad());
                }
                try {
                    cpuCoresSum += Integer.valueOf(systemInfo.getCpuCoreNum()).intValue();
                }
                catch (Exception e) {
                    logger.error("\u7edf\u8ba1\u6240\u6709\u4e3b\u673a\u603b\u6838\u6570\u9519\u8bef", (Throwable)e);
                }
                try {
                    memSum += Double.valueOf(systemInfo.getTotalMem().replace("G", "")).doubleValue();
                }
                catch (Exception e) {
                    logger.error("\u7edf\u8ba1\u6240\u6709\u4e3b\u673a\u603b\u5185\u5b58\u9519\u8bef", (Throwable)e);
                }
                try {
                    submitSecondsSum += Integer.valueOf(systemInfo.getSubmitSeconds()).intValue();
                }
                catch (Exception e) {
                    logger.error("\u7edf\u8ba1\u6240\u6709\u4e3b\u673a\u7ec4\u88c5\u4e0a\u62a5\u6570\u636e\u9891\u7387\u603b\u548c\u9519\u8bef", (Throwable)e);
                }
            }
            if (systemSize > 0) {
                avgCpu = sumCpu / (double)list.size();
                avgMem = sumMem / (double)list.size();
                avgRxbyt = sumRxbyt / (double)list.size();
                avgTxbyt = sumTxbyt / (double)list.size();
                avgFifteenLoad = sumFifteenLoad / (double)list.size();
                avgFiveLoad = sumFiveLoad / (double)list.size();
                avgSubmitSeconds = (double)submitSecondsSum / (double)list.size();
            } else {
                minCpu = 0.0;
                minMem = 0.0;
                minRxbyt = 0.0;
                minTxbyt = 0.0;
                avgFifteenLoad = 0.0;
                avgFiveLoad = 0.0;
            }
            model.addAttribute("cpuCoresSum", (Object)cpuCoresSum);
            model.addAttribute("memSum", (Object)FormatUtil.formatDouble(memSum, 2));
            model.addAttribute("avgSubmitSeconds", (Object)FormatUtil.formatDouble(avgSubmitSeconds, 2));
            model.addAttribute("maxCpuVal", (Object)maxCpu);
            model.addAttribute("avgCpuVal", (Object)FormatUtil.formatDouble(avgCpu, 2));
            model.addAttribute("minCpuVal", (Object)minCpu);
            model.addAttribute("maxMemVal", (Object)maxMem);
            model.addAttribute("avgMemVal", (Object)FormatUtil.formatDouble(avgMem, 2));
            model.addAttribute("minMemVal", (Object)minMem);
            Double maxHostDiskPerVal = this.setMaxMinDiskPer(list, model);
            Double maxCpuMemSwapPer = 0.0;
            maxCpuMemSwapPer = maxCpu > maxMem ? maxCpu : maxMem;
            if (maxHostDiskPerVal > maxCpuMemSwapPer) {
                maxCpuMemSwapPer = maxHostDiskPerVal;
            }
            model.addAttribute("maxCpuMemSwapPer", (Object)maxCpuMemSwapPer);
            this.rxtxByteHandle(model, maxRxbyt, minRxbyt, avgRxbyt, sumRxbyt, maxTxbyt, minTxbyt, avgTxbyt, sumTxbyt);
            model.addAttribute("maxFiveLoad", (Object)maxFiveLoad);
            model.addAttribute("minFiveLoad", (Object)minFiveLoad);
            model.addAttribute("avgFiveLoad", (Object)FormatUtil.formatDouble(avgFiveLoad, 2));
            model.addAttribute("maxFifteenLoad", (Object)maxFifteenLoad);
            model.addAttribute("minFifteenLoad", (Object)minFifteenLoad);
            model.addAttribute("avgFifteenLoad", (Object)FormatUtil.formatDouble(avgFifteenLoad, 2));
            if (maxFiveLoad != null && maxFifteenLoad != null) {
                model.addAttribute("maxLoadValue", (Object)(maxFiveLoad > maxFifteenLoad ? maxFiveLoad : maxFifteenLoad));
            }
        }
        catch (Exception e) {
            logger.error("\u76d1\u63a7\u6982\u8981\u9875\u9762\u8bbe\u7f6e\u53c2\u6570\u9519\u8bef", (Throwable)e);
        }
    }

    public void rxtxByteHandle(Model model, Double maxRxbyt, Double minRxbyt, Double avgRxbyt, Double sumRxbyt, Double maxTxbyt, Double minTxbyt, Double avgTxbyt, Double sumTxbyt) {
        model.addAttribute("maxRxbyt", (Object)(FormatUtil.kbToM(maxRxbyt + "") + "/s"));
        model.addAttribute("minRxbyt", (Object)(FormatUtil.kbToM(minRxbyt + "") + "/s"));
        model.addAttribute("avgRxbyt", (Object)(FormatUtil.kbToM(FormatUtil.formatDouble(avgRxbyt, 2) + "") + "/s"));
        model.addAttribute("sumRxbyt", (Object)(FormatUtil.kbToM(FormatUtil.formatDouble(sumRxbyt, 2) + "") + "/s"));
        if (maxRxbyt >= 10000.0 || maxTxbyt >= 10000.0) {
            model.addAttribute("maxRxbytChart", (Object)FormatUtil.kbToMDouble(maxRxbyt + ""));
            model.addAttribute("minRxbytChart", (Object)FormatUtil.kbToMDouble(minRxbyt + ""));
            model.addAttribute("avgRxbytChart", (Object)FormatUtil.kbToMDouble(FormatUtil.formatDouble(avgRxbyt, 2) + ""));
        } else {
            model.addAttribute("maxRxbytChart", (Object)maxRxbyt);
            model.addAttribute("minRxbytChart", (Object)minRxbyt);
            model.addAttribute("avgRxbytChart", (Object)FormatUtil.formatDouble(avgRxbyt, 2));
        }
        model.addAttribute("maxTxbyt", (Object)(FormatUtil.kbToM(maxTxbyt + "") + "/s"));
        model.addAttribute("minTxbyt", (Object)(FormatUtil.kbToM(minTxbyt + "") + "/s"));
        model.addAttribute("avgTxbyt", (Object)(FormatUtil.kbToM(FormatUtil.formatDouble(avgTxbyt, 2) + "") + "/s"));
        model.addAttribute("sumTxbyt", (Object)(FormatUtil.kbToM(FormatUtil.formatDouble(sumTxbyt, 2) + "") + "/s"));
        if (maxRxbyt >= 10000.0 || maxTxbyt >= 10000.0) {
            model.addAttribute("maxTxbytChart", (Object)FormatUtil.kbToMDouble(maxTxbyt + ""));
            model.addAttribute("minTxbytChart", (Object)FormatUtil.kbToMDouble(minTxbyt + ""));
            model.addAttribute("avgTxbytChart", (Object)FormatUtil.kbToMDouble(FormatUtil.formatDouble(avgTxbyt, 2) + ""));
        } else {
            model.addAttribute("maxTxbytChart", (Object)maxTxbyt);
            model.addAttribute("minTxbytChart", (Object)minTxbyt);
            model.addAttribute("avgTxbytChart", (Object)FormatUtil.formatDouble(avgTxbyt, 2));
        }
        if (maxRxbyt != null && maxTxbyt != null) {
            if (maxRxbyt > maxTxbyt) {
                if (maxRxbyt >= 10000.0 || maxTxbyt >= 10000.0) {
                    model.addAttribute("maxByteValue", (Object)FormatUtil.kbToMDouble(maxRxbyt + ""));
                } else {
                    model.addAttribute("maxByteValue", (Object)maxRxbyt);
                }
                model.addAttribute("maxByteSource", (Object)maxRxbyt);
            } else {
                if (maxRxbyt >= 10000.0 || maxTxbyt >= 10000.0) {
                    model.addAttribute("maxByteValue", (Object)FormatUtil.kbToMDouble(maxTxbyt + ""));
                } else {
                    model.addAttribute("maxByteValue", (Object)maxTxbyt);
                }
                model.addAttribute("maxByteSource", (Object)maxTxbyt);
            }
        }
    }

    public Double setMaxMinDiskPer(List<SystemInfo> list, Model model) {
        Double maxDiskPer = 0.0;
        Double minDiskPer = 1000.0;
        Double avgDiskPer = 0.0;
        Double sumDiskPer = 0.0;
        try {
            for (SystemInfo systemInfo : list) {
                if (null != systemInfo.getDiskPer() && systemInfo.getDiskPer() > maxDiskPer) {
                    maxDiskPer = systemInfo.getDiskPer();
                }
                if (null != systemInfo.getDiskPer() && systemInfo.getDiskPer() < minDiskPer) {
                    minDiskPer = systemInfo.getDiskPer();
                }
                if (null == systemInfo.getDiskPer()) continue;
                sumDiskPer = sumDiskPer + systemInfo.getDiskPer();
            }
            if (list.size() > 0) {
                avgDiskPer = sumDiskPer / (double)list.size();
            } else {
                minDiskPer = 0.0;
            }
            model.addAttribute("maxDiskPerIndex", (Object)maxDiskPer);
            model.addAttribute("avgDiskPerIndex", (Object)FormatUtil.formatDouble(avgDiskPer, 2));
            model.addAttribute("minDiskPerIndex", (Object)minDiskPer);
        }
        catch (Exception e) {
            logger.error("\u7ec4\u88c5\u78c1\u76d8\u603b\u4f7f\u7528\u7387\u6700\u5927\u5e73\u5747\u6700\u4f4e\u503c\u9519\u8bef", (Throwable)e);
        }
        return maxDiskPer;
    }

    public void setPieChart(Model model, int totalSystemInfoSize, HttpServletRequest request) {
        try {
            HashMap<String, Object> params = new HashMap<String, Object>();
            ArrayList<ChartInfo> chartInfoList1 = new ArrayList<ChartInfo>();
            ArrayList<ChartInfo> chartInfoList2 = new ArrayList<ChartInfo>();
            HostUtil.addAccountquery(request, params);
            params.put("countBlockNe", "2");
            params.put("memPer", 90);
            int memPerSizeArea1 = this.systemInfoService.countByParams(params);
            double a = 0.0;
            if (totalSystemInfoSize != 0) {
                a = (double)memPerSizeArea1 / (double)totalSystemInfoSize;
            }
            ChartInfo memPerSizeChartArea1 = new ChartInfo();
            memPerSizeChartArea1.setItem("\u5185\u5b58>=90%");
            memPerSizeChartArea1.setCount(memPerSizeArea1);
            memPerSizeChartArea1.setPercent(FormatUtil.formatDouble(a, 2));
            chartInfoList1.add(memPerSizeChartArea1);
            params.clear();
            HostUtil.addAccountquery(request, params);
            params.put("countBlockNe", "2");
            params.put("memPer", 70);
            params.put("memPerLe", 90);
            int memPerSizetArea2 = this.systemInfoService.countByParams(params);
            double a2 = 0.0;
            if (totalSystemInfoSize != 0) {
                a2 = (double)memPerSizetArea2 / (double)totalSystemInfoSize;
            }
            ChartInfo memPerSizeChartArea2 = new ChartInfo();
            memPerSizeChartArea2.setItem("\u5185\u5b58>=70%\u4e14<90%");
            memPerSizeChartArea2.setCount(memPerSizetArea2);
            memPerSizeChartArea2.setPercent(FormatUtil.formatDouble(a2, 2));
            chartInfoList1.add(memPerSizeChartArea2);
            params.clear();
            HostUtil.addAccountquery(request, params);
            params.put("countBlockNe", "2");
            params.put("memPer", 40);
            params.put("memPerLe", 70);
            int memPerSizeArea3 = this.systemInfoService.countByParams(params);
            double b = 0.0;
            if (totalSystemInfoSize != 0) {
                b = (double)memPerSizeArea3 / (double)totalSystemInfoSize;
            }
            ChartInfo memPerSizeChartArea3 = new ChartInfo();
            memPerSizeChartArea3.setItem("\u5185\u5b58>=40%\u4e14<70%");
            memPerSizeChartArea3.setCount(memPerSizeArea3);
            memPerSizeChartArea3.setPercent(FormatUtil.formatDouble(b, 2));
            chartInfoList1.add(memPerSizeChartArea3);
            params.clear();
            HostUtil.addAccountquery(request, params);
            params.put("countBlockNe", "2");
            params.put("memPerLe", 40);
            int memPerSizeArea4 = this.systemInfoService.countByParams(params);
            double bb = 0.0;
            if (totalSystemInfoSize != 0) {
                bb = (double)memPerSizeArea4 / (double)totalSystemInfoSize;
            }
            ChartInfo memPerSizeChartArea4 = new ChartInfo();
            memPerSizeChartArea4.setItem("\u5185\u5b58<40%");
            memPerSizeChartArea4.setCount(memPerSizeArea4);
            memPerSizeChartArea4.setPercent(FormatUtil.formatDouble(bb, 2));
            chartInfoList1.add(memPerSizeChartArea4);
            params.clear();
            model.addAttribute("chartInfoList1", (Object)JSONUtil.parseArray(chartInfoList1));
            HostUtil.addAccountquery(request, params);
            params.put("countBlockNe", "2");
            params.put("cpuPer", 90);
            int cpuPerSizeArea1 = this.systemInfoService.countByParams(params);
            double cArea1 = 0.0;
            if (totalSystemInfoSize != 0) {
                cArea1 = (double)cpuPerSizeArea1 / (double)totalSystemInfoSize;
            }
            ChartInfo cpuPerSizeChartArea1 = new ChartInfo();
            cpuPerSizeChartArea1.setItem("CPU>=90%");
            cpuPerSizeChartArea1.setCount(cpuPerSizeArea1);
            cpuPerSizeChartArea1.setPercent(FormatUtil.formatDouble(cArea1, 2));
            chartInfoList2.add(cpuPerSizeChartArea1);
            params.clear();
            HostUtil.addAccountquery(request, params);
            params.put("countBlockNe", "2");
            params.put("cpuPer", 70);
            params.put("cpuPerLe", 90);
            int cpuPerSizeArea2 = this.systemInfoService.countByParams(params);
            double cArea2 = 0.0;
            if (totalSystemInfoSize != 0) {
                cArea2 = (double)cpuPerSizeArea2 / (double)totalSystemInfoSize;
            }
            ChartInfo cpuPerSizeChartcArea2 = new ChartInfo();
            cpuPerSizeChartcArea2.setItem("CPU>=70%\u4e14<90%");
            cpuPerSizeChartcArea2.setCount(cpuPerSizeArea2);
            cpuPerSizeChartcArea2.setPercent(FormatUtil.formatDouble(cArea2, 2));
            chartInfoList2.add(cpuPerSizeChartcArea2);
            params.clear();
            HostUtil.addAccountquery(request, params);
            params.put("countBlockNe", "2");
            params.put("cpuPerLe", 70);
            params.put("cpuPer", 40);
            int cpuPerSizeArea3 = this.systemInfoService.countByParams(params);
            double e = 0.0;
            if (totalSystemInfoSize != 0) {
                e = (double)cpuPerSizeArea3 / (double)totalSystemInfoSize;
            }
            ChartInfo cpuPerSizeChartArea3 = new ChartInfo();
            cpuPerSizeChartArea3.setItem("CPU>=40%\u4e14<70%");
            cpuPerSizeChartArea3.setCount(cpuPerSizeArea3);
            cpuPerSizeChartArea3.setPercent(FormatUtil.formatDouble(e, 2));
            chartInfoList2.add(cpuPerSizeChartArea3);
            params.clear();
            HostUtil.addAccountquery(request, params);
            params.put("countBlockNe", "2");
            params.put("cpuPerLe", 40);
            int cpuPerSizeArea4 = this.systemInfoService.countByParams(params);
            double f = 0.0;
            if (totalSystemInfoSize != 0) {
                f = (double)cpuPerSizeArea4 / (double)totalSystemInfoSize;
            }
            ChartInfo cpuPerSizeChartArea4 = new ChartInfo();
            cpuPerSizeChartArea4.setItem("CPU<40%");
            cpuPerSizeChartArea4.setCount(cpuPerSizeArea4);
            cpuPerSizeChartArea4.setPercent(FormatUtil.formatDouble(f, 2));
            chartInfoList2.add(cpuPerSizeChartArea4);
            params.clear();
            model.addAttribute("chartInfoList2", (Object)JSONUtil.parseArray(chartInfoList2));
        }
        catch (Exception e) {
            logger.error("\u76d1\u63a7\u6982\u8981\u9875\u9762\u8bbe\u7f6e\u56fe\u8868\u6570\u636e\u9519\u8bef", (Throwable)e);
        }
    }

    public void setMiddleApplicationData(Model model) {
        try {
            HashMap<String, Object> params = new HashMap<String, Object>();
            params.put("hostname", "\u544a\u8b66");
            params.put("startTime", DateUtil.beforeMinutesToNowDate(60));
            params.put("endTime", DateUtil.getCurrentDateTime());
            model.addAttribute("warnCount", (Object)this.logInfoService.countByParams(params));
            model.addAttribute("historyDataOut", (Object)this.commonConfig.getHistoryDataOut());
            String warnType = "\u672a\u8bbe\u7f6e";
            if (StaticKeys.mailSet != null && !StringUtils.isEmpty((CharSequence)this.mailConfig.getWarnScript())) {
                warnType = "\u90ae\u4ef6+\u811a\u672c";
            }
            if (StaticKeys.mailSet == null && !StringUtils.isEmpty((CharSequence)this.mailConfig.getWarnScript())) {
                warnType = "\u811a\u672c";
            }
            if (StaticKeys.mailSet != null && StringUtils.isEmpty((CharSequence)this.mailConfig.getWarnScript())) {
                warnType = "\u90ae\u4ef6";
            }
            model.addAttribute("warnType", (Object)warnType);
            String dapingView = "\u5df2\u5173\u95ed";
            if ("true".equals(this.commonConfig.getDashView())) {
                dapingView = "\u5df2\u5f00\u542f";
            }
            model.addAttribute("dapingView", (Object)dapingView);
            String adminAccount = "\u9ed8\u8ba4";
            if (!"admin".equals(this.commonConfig.getAccount())) {
                adminAccount = "\u5df2\u4fee\u6539";
            }
            model.addAttribute("adminAccount", (Object)adminAccount);
            String adminPwd = "\u9ed8\u8ba4";
            if (!"111111".equals(this.commonConfig.getAccountPwd())) {
                adminPwd = "\u5df2\u4fee\u6539";
            }
            model.addAttribute("adminPwd", (Object)adminPwd);
            model.addAttribute("pwdExpDateShow", (Object)this.commonConfig.getPwdExpDate());
            String wgToken = "\u9ed8\u8ba4";
            if (!"wgcloud".equals(this.commonConfig.getWgToken())) {
                wgToken = "\u5df2\u4fee\u6539";
            }
            model.addAttribute("wgToken", (Object)wgToken);
            String webSSH = "\u5df2\u5173\u95ed";
            if ("true".equals(this.commonConfig.getWebSsh())) {
                webSSH = "\u5df2\u5f00\u542f";
            }
            model.addAttribute("webSSH", (Object)webSSH);
            model.addAttribute("defaultWarnLevel", (Object)WarnOtherUtil.getWarnLevelName(this.levelConfig.getDefaultWarn()));
            String openSSO = "\u5df2\u5173\u95ed";
            if ("true".equals(this.commonConfig.getOpenSSO())) {
                openSSO = "\u5df2\u5f00\u542f";
            }
            model.addAttribute("openSSO", (Object)openSSO);
            String hostGroup = "\u5df2\u5173\u95ed";
            if ("true".equals(this.commonConfig.getHostGroup())) {
                hostGroup = "\u5df2\u5f00\u542f";
            }
            model.addAttribute("hostGroup", (Object)hostGroup);
            String userInfoManage = "\u5df2\u5173\u95ed";
            if ("true".equals(this.commonConfig.getUserInfoManage())) {
                userInfoManage = "\u5df2\u5f00\u542f";
            }
            model.addAttribute("userInfoManage", (Object)userInfoManage);
            model.addAttribute("WARN_LICENSE_CHECK_SIGN", (Object)StaticKeys.WARN_LICENSE_CHECK_SIGN);
        }
        catch (Exception e) {
            logger.error("\u76d1\u63a7\u6982\u8981\u9875\u9762\u8bbe\u7f6e\u53c2\u6570\u6570\u636e\u9519\u8bef", (Throwable)e);
        }
    }

    public void hostDraw(Model model, String systemInfoId, HttpServletRequest request) {
        try {
            SystemInfo systemInfo = this.systemInfoService.selectById(systemInfoId);
            String hostname = systemInfo.getHostname();
            HashMap<String, Object> params = new HashMap<String, Object>();
            params.put("hostname", hostname);
            model.addAttribute("systemInfo", (Object)systemInfo);
            HostUtil.setSysFontAwesome(systemInfo);
            if ("true".equals(this.commonConfig.getHostGroup())) {
                systemInfo.setGroupId(this.hostGroupService.returnGroupNames(systemInfo.getGroupId()));
            }
            DateUtil.setBeginEndTime(params, 7);
            CpuState maxAvgCpuState = this.cpuStateService.selectMaxAvgByHostname(params);
            if (null == maxAvgCpuState) {
                maxAvgCpuState = new CpuState();
                maxAvgCpuState.setSys(0.0);
                maxAvgCpuState.setIdle(0.0);
                maxAvgCpuState.setIowait(0.0);
            }
            model.addAttribute("maxCpuState", (Object)maxAvgCpuState.getSys());
            model.addAttribute("avgCpuState", (Object)FormatUtil.formatDouble(maxAvgCpuState.getIdle(), 2));
            model.addAttribute("minCpuState", (Object)maxAvgCpuState.getIowait());
            MemState maxAvgMemState = this.memStateService.selectMaxAvgByHostname(params);
            if (null == maxAvgMemState) {
                maxAvgMemState = new MemState();
                maxAvgMemState.setUsePer(0.0);
                maxAvgMemState.setUsed("0");
                maxAvgMemState.setFree("0");
            }
            model.addAttribute("maxMemState", (Object)maxAvgMemState.getUsePer());
            model.addAttribute("avgMemState", (Object)FormatUtil.formatDouble(Double.valueOf(maxAvgMemState.getUsed()), 2));
            model.addAttribute("minMemState", (Object)maxAvgMemState.getFree());
            model.addAttribute("historyDataOut", (Object)this.commonConfig.getHistoryDataOut());
            params.clear();
            params.put("hostname", hostname);
            DateUtil.setBeginEndTime(params, 7);
            PageInfo pageInfoLoad = this.sysLoadStateService.selectByParams(params, 1, 1);
            if (!CollectionUtil.isEmpty((Collection)pageInfoLoad.getList())) {
                model.addAttribute("sysLoadState", pageInfoLoad.getList().get(0));
            } else {
                model.addAttribute("sysLoadState", (Object)new SysLoadState());
            }
            SysLoadState sysLoadStateMax = this.sysLoadStateService.selectMaxByHostname(params);
            if (null == sysLoadStateMax) {
                sysLoadStateMax = new SysLoadState();
                sysLoadStateMax.setOneLoad(0.0);
                sysLoadStateMax.setFiveLoad(0.0);
                sysLoadStateMax.setFifteenLoad(0.0);
            }
            model.addAttribute("maxOneLoad", (Object)sysLoadStateMax.getOneLoad());
            model.addAttribute("maxFiveLoad", (Object)sysLoadStateMax.getFiveLoad());
            model.addAttribute("maxFifteenLoad", (Object)sysLoadStateMax.getFifteenLoad());
            SysLoadState sysLoadStateAvg = this.sysLoadStateService.selectAvgByHostname(params);
            if (null == sysLoadStateAvg) {
                sysLoadStateAvg = new SysLoadState();
                sysLoadStateAvg.setOneLoad(0.0);
                sysLoadStateAvg.setFiveLoad(0.0);
                sysLoadStateAvg.setFifteenLoad(0.0);
            }
            model.addAttribute("avgOneLoad", (Object)FormatUtil.formatDouble(sysLoadStateAvg.getOneLoad(), 2));
            model.addAttribute("avgFiveLoad", (Object)FormatUtil.formatDouble(sysLoadStateAvg.getFiveLoad(), 2));
            model.addAttribute("avgFifteenLoad", (Object)FormatUtil.formatDouble(sysLoadStateAvg.getFifteenLoad(), 2));
            SysLoadState sysLoadStateMin = this.sysLoadStateService.selectMinByHostname(params);
            if (null == sysLoadStateMin) {
                sysLoadStateMin = new SysLoadState();
                sysLoadStateMin.setOneLoad(0.0);
                sysLoadStateMin.setFiveLoad(0.0);
                sysLoadStateMin.setFifteenLoad(0.0);
            }
            model.addAttribute("minOneLoad", (Object)sysLoadStateMin.getOneLoad());
            model.addAttribute("minFiveLoad", (Object)sysLoadStateMin.getFiveLoad());
            model.addAttribute("minFifteenLoad", (Object)sysLoadStateMin.getFifteenLoad());
            PageInfo pageInfoNetIo = this.netIoStateService.selectByParams(params, 1, 1);
            NetIoState netIoStateNew = new NetIoState();
            if (!CollectionUtil.isEmpty((Collection)pageInfoNetIo.getList())) {
                netIoStateNew = (NetIoState)pageInfoNetIo.getList().get(0);
                netIoStateNew.setRxbyt(FormatUtil.kbToM(netIoStateNew.getRxbyt()) + "/s");
                netIoStateNew.setTxbyt(FormatUtil.kbToM(netIoStateNew.getTxbyt()) + "/s");
                model.addAttribute("netIoState", (Object)netIoStateNew);
            } else {
                model.addAttribute("netIoState", (Object)netIoStateNew);
            }
            NetIoState netIoStateMax = this.netIoStateService.selectMaxByHostname(params);
            if (null == netIoStateMax) {
                netIoStateMax = new NetIoState();
                netIoStateMax.setTxbyt("0");
                netIoStateMax.setRxbyt("0");
                netIoStateMax.setRxpck("0");
                netIoStateMax.setTxpck("0");
            }
            model.addAttribute("maxRxbyt", (Object)(FormatUtil.kbToM(netIoStateMax.getRxbyt()) + "/s"));
            model.addAttribute("maxTxbyt", (Object)(FormatUtil.kbToM(netIoStateMax.getTxbyt()) + "/s"));
            model.addAttribute("maxRxpck", (Object)netIoStateMax.getRxpck());
            model.addAttribute("maxTxpck", (Object)netIoStateMax.getTxpck());
            NetIoState netIoStateAvg = this.netIoStateService.selectAvgByHostname(params);
            if (null == netIoStateAvg) {
                netIoStateAvg = new NetIoState();
                netIoStateAvg.setTxbyt("0");
                netIoStateAvg.setRxbyt("0");
                netIoStateAvg.setRxpck("0");
                netIoStateAvg.setTxpck("0");
            }
            model.addAttribute("avgRxbyt", (Object)(FormatUtil.kbToM(FormatUtil.formatDouble(netIoStateAvg.getRxbyt(), 2) + "") + "/s"));
            model.addAttribute("avgTxbyt", (Object)(FormatUtil.kbToM(FormatUtil.formatDouble(netIoStateAvg.getTxbyt(), 2) + "") + "/s"));
            model.addAttribute("avgRxpck", (Object)FormatUtil.formatDouble(netIoStateAvg.getRxpck(), 2));
            model.addAttribute("avgTxpck", (Object)FormatUtil.formatDouble(netIoStateAvg.getTxpck(), 2));
            NetIoState netIoStateMin = this.netIoStateService.selectMinByHostname(params);
            if (null == netIoStateMin) {
                netIoStateMin = new NetIoState();
                netIoStateMin.setTxbyt("0");
                netIoStateMin.setRxbyt("0");
                netIoStateMin.setRxpck("0");
                netIoStateMin.setTxpck("0");
            }
            model.addAttribute("minRxbyt", (Object)(FormatUtil.kbToM(netIoStateMin.getRxbyt()) + "/s"));
            model.addAttribute("minTxbyt", (Object)(FormatUtil.kbToM(netIoStateMin.getTxbyt()) + "/s"));
            model.addAttribute("minRxpck", (Object)netIoStateMin.getRxpck());
            model.addAttribute("minTxpck", (Object)netIoStateMin.getTxpck());
            params.clear();
            params.put("hostname", "\u4e3b\u673a\u79bb\u7ebf\u544a\u8b66\uff1a" + hostname);
            DateUtil.setBeginEndTime(params, 7);
            int hostDownSize = this.logInfoService.countByParams(params);
            model.addAttribute("hostDownSize", (Object)hostDownSize);
            params.clear();
            params.put("hostname", hostname);
            List<AppInfo> appInfoList = this.appInfoService.selectAllByParams(params);
            for (AppInfo appInfo1 : appInfoList) {
                appInfo1.setWritesBytes(FormatUtil.mToG(appInfo1.getWritesBytes()));
                appInfo1.setReadBytes(FormatUtil.mToG(appInfo1.getReadBytes()));
            }
            this.appInfoService.setGroupInList(appInfoList, model, request);
            model.addAttribute("appInfoList", appInfoList);
            HostUtil.viewLastUserInfoHandler(model, systemInfoId);
            List<PortInfo> portInfoList = this.portInfoService.selectAllByParams(params);
            this.portInfoService.setGroupInList(portInfoList, model, request);
            model.addAttribute("portInfoList", portInfoList);
            List<FileSafe> fileSafeInfoList = this.fileSafeService.selectAllByParams(params);
            model.addAttribute("fileSafeInfoList", fileSafeInfoList);
            List<FileWarnInfo> fileWarnInfoList = this.fileWarnInfoService.selectAllByParams(params);
            for (FileWarnInfo info : fileWarnInfoList) {
                params.put("fileWarnId", info.getId());
                Integer count = this.fileWarnStateService.countByParams(params);
                info.setWarnDatas(count + "");
                if (StringUtils.isEmpty((CharSequence)info.getFileSize())) continue;
                String fileFormatSize = FormatUtil.bytesFormatUnit(info.getFileSize(), "byte");
                info.setFileSize(fileFormatSize);
            }
            model.addAttribute("fileWarnInfoList", fileWarnInfoList);
            params.clear();
            params.put("hostname", hostname);
            List<DockerInfo> dockerInfoList = this.dockerInfoService.selectAllByParams(params);
            this.dockerInfoService.setGroupInList(dockerInfoList, model, request);
            model.addAttribute("dockerInfoList", dockerInfoList);
            List<DiskState> diskStateList = this.diskStateService.selectAllByParams(params);
            model.addAttribute("diskStateList", diskStateList);
            this.diskStateService.computeDaysForDisk(diskStateList, systemInfo.getHostname());
            HostUtil.setDiskListSumSize(diskStateList);
            List<DiskIo> diskIoList = this.diskIoService.selectAllByParams(params);
            model.addAttribute("diskIoList", diskIoList);
            List<DiskSmart> diskSmartList = this.diskSmartService.selectAllByParams(params);
            model.addAttribute("diskSmartList", diskSmartList);
            List<CpuTemperatures> cpuTemperaturesList = this.cpuTemperaturesService.selectAllByParams(params);
            model.addAttribute("cpuTemperaturesList", cpuTemperaturesList);
            List<HostMacInfo> hostMacInfoList = this.hostMacInfoService.selectAllByParams(params);
            model.addAttribute("hostMacInfoList", hostMacInfoList);
            HostUtil.viewBiosBoardHandler(systemInfo);
            List<CustomInfo> customInfoList = this.customInfoService.selectAllByParams(params);
            model.addAttribute("customInfoList", customInfoList);
            params.clear();
            params.put("hostname", hostname);
            List<TaskJobInfo> taskJobInfoList = this.taskJobInfoService.selectAllByParams(params);
            this.taskJobInfoService.setGroupInList(taskJobInfoList, model, request);
            model.addAttribute("taskJobInfoList", taskJobInfoList);
        }
        catch (Exception e) {
            logger.error("\u4e3b\u673a\u753b\u50cf\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("\u4e3b\u673a\u753b\u50cf\u9519\u8bef", e.toString(), "2");
        }
    }

    public void setDashboardRightBingtu(Model model, int customInfoSize, int fileSafeSize, int ftpInfoSize, int snmpInfoSize, int dbTableSize, HttpServletRequest request) throws Exception {
        ArrayList<ChartInfo> chartInfoList = new ArrayList<ChartInfo>();
        HashMap<String, Object> params = new HashMap<String, Object>();
        HostUtil.addAccountquery(request, params);
        params.put("state", "1");
        int customInfoonLineSize = this.customInfoService.countByParams(params);
        double onLineCustomInfoPer = 0.0;
        if (customInfoSize != 0) {
            onLineCustomInfoPer = (double)customInfoonLineSize / (double)customInfoSize;
        }
        ChartInfo chartInfoCustomInfo = new ChartInfo();
        chartInfoCustomInfo.setItem("\u81ea\u5b9a\u4e49\u76d1\u63a7\u9879\u5728\u7ebf%");
        chartInfoCustomInfo.setPercent(FormatUtil.formatDouble(onLineCustomInfoPer * 100.0, 2));
        chartInfoList.add(chartInfoCustomInfo);
        params.clear();
        HostUtil.addAccountquery(request, params);
        params.put("state", "1");
        int fileSafeOnLineSize = this.fileSafeService.countByParams(params);
        double fileSafeOnLinePer = 0.0;
        if (fileSafeSize != 0) {
            fileSafeOnLinePer = (double)fileSafeOnLineSize / (double)fileSafeSize;
        }
        ChartInfo chartInfoFileSafe = new ChartInfo();
        chartInfoFileSafe.setItem("\u6587\u4ef6\u9632\u7be1\u6539\u5728\u7ebf%");
        chartInfoFileSafe.setPercent(FormatUtil.formatDouble(fileSafeOnLinePer * 100.0, 2));
        chartInfoList.add(chartInfoFileSafe);
        params.clear();
        HostUtil.addAccountquery(request, params);
        params.put("state", "1");
        int dbTableOnLineSize = this.dbTableService.countByParams(params);
        double dbTableOnLinePer = 0.0;
        if (dbTableSize != 0) {
            dbTableOnLinePer = (double)dbTableOnLineSize / (double)dbTableSize;
        }
        ChartInfo chartInfoDbTable = new ChartInfo();
        chartInfoDbTable.setItem("\u6570\u636e\u8868\u5728\u7ebf%");
        chartInfoDbTable.setPercent(FormatUtil.formatDouble(dbTableOnLinePer * 100.0, 2));
        chartInfoList.add(chartInfoDbTable);
        params.clear();
        HostUtil.addAccountquery(request, params);
        params.put("state", "1");
        int snmpOnLineSize = this.snmpInfoService.countByParams(params);
        double snmpOnLinePer = 0.0;
        if (snmpInfoSize != 0) {
            snmpOnLinePer = (double)snmpOnLineSize / (double)snmpInfoSize;
        }
        ChartInfo chartInfoSnmp = new ChartInfo();
        chartInfoSnmp.setItem("SNMP\u5728\u7ebf%");
        chartInfoSnmp.setPercent(FormatUtil.formatDouble(snmpOnLinePer * 100.0, 2));
        chartInfoList.add(chartInfoSnmp);
        params.clear();
        HostUtil.addAccountquery(request, params);
        params.put("state", "1");
        int ftpOnLineSize = this.ftpInfoService.countByParams(params);
        double ftpOnLinePer = 0.0;
        if (ftpInfoSize != 0) {
            ftpOnLinePer = (double)ftpOnLineSize / (double)ftpInfoSize;
        }
        ChartInfo chartInfoFtp = new ChartInfo();
        chartInfoFtp.setItem("FTP\u5728\u7ebf%");
        chartInfoFtp.setPercent(FormatUtil.formatDouble(ftpOnLinePer * 100.0, 2));
        chartInfoList.add(chartInfoFtp);
        params.clear();
        HostUtil.addAccountquery(request, params);
        int appExceptionSize = this.appExceptionInfoService.countByParams(params);
        ChartInfo chartInfoAppException = new ChartInfo();
        chartInfoAppException.setItem("\u5f02\u5e38\u8fdb\u7a0b\u6570\u91cf");
        chartInfoAppException.setPercent(Double.valueOf(appExceptionSize));
        chartInfoList.add(chartInfoAppException);
        params.clear();
        HostUtil.addAccountquery(request, params);
        int jobSize = this.taskJobInfoService.countByParams(params);
        ChartInfo chartInfoJob = new ChartInfo();
        chartInfoJob.setItem("\u8ba1\u5212\u4efb\u52a1\u6570\u91cf");
        chartInfoJob.setPercent(Double.valueOf(jobSize));
        chartInfoList.add(chartInfoJob);
        model.addAttribute("chartInfoDownPer", (Object)JSONUtil.parseArray(chartInfoList));
    }

    public List dbDceHeathInfoHandle(HttpServletRequest request) {
        Object resultList = new ArrayList();
        try {
            JSONObject jsonObject;
            HashMap<String, Object> params = new HashMap<String, Object>();
            HostUtil.addAccountqueryDaping(request, params);
            params.put("orderBy", "CREATE_TIME");
            params.put("orderType", "DESC");
            PageInfo dceInfoList = this.dceInfoService.selectByParams(params, 1, 20);
            PageInfo heathMonitorList = this.heathMonitorService.selectByParamsNoContent(params, 1, 20);
            PageInfo dbInfoList = this.dbInfoService.selectByParams(params, 1, 20);
            JSONArray jsonArray = new JSONArray();
            for (DceInfo dceInfo : (java.util.List<DceInfo>) dceInfoList.getList()) {
                jsonObject = new JSONObject();
                jsonObject.set("remark", (Object)dceInfo.getHostname());
                jsonObject.set("value", (Object)dceInfo.getResTimes());
                jsonArray.add((Object)jsonObject);
            }
            for (HeathMonitor heathMonitor : (java.util.List<HeathMonitor>) heathMonitorList.getList()) {
                jsonObject = new JSONObject();
                jsonObject.set("remark", (Object)heathMonitor.getAppName());
                jsonObject.set("value", (Object)heathMonitor.getResTimes());
                jsonArray.add((Object)jsonObject);
            }
            for (DbInfo dbInfo : (java.util.List<DbInfo>) dbInfoList.getList()) {
                jsonObject = new JSONObject();
                jsonObject.set("remark", (Object)dbInfo.getAliasName());
                jsonObject.set("value", (Object)dbInfo.getResTimes());
                jsonArray.add((Object)jsonObject);
            }
            resultList = jsonArray.size() > 20 ? jsonArray.subList(0, 20) : jsonArray;
        }
        catch (Exception e) {
            logger.error("\u76d1\u63a7\u6982\u8981\u9875\u9762\u5e95\u90e8\u67f1\u72b6\u56fe\u7ec4\u88c5\u6570\u636e\u9519\u8bef", (Throwable)e);
        }
        return (List)resultList;
    }

    public String goLambdaFunc() {
        String[] str = new String[]{"a", "ba", "cc", "dd"};
        Arrays.sort(str, new Comparator<String>(){

            @Override
            public int compare(String o1, String o2) {
                return Integer.compare(o1.length(), o2.length());
            }
        });
        String[] str1 = new String[]{"a", "ba", "cc", "dd"};
        Arrays.sort(str1, (o1, o2) -> Integer.compare(o1.length(), o1.length()));
        return "";
    }
}

