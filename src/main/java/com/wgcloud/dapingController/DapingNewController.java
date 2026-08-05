/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.dapingController;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONUtil;
import com.github.pagehelper.PageInfo;
import com.wgcloud.config.CommonConfig;
import com.wgcloud.dto.ChartDapingNewInfo;
import com.wgcloud.entity.AccountInfo;
import com.wgcloud.entity.HeathMonitor;
import com.wgcloud.entity.SystemInfo;
import com.wgcloud.service.AppInfoService;
import com.wgcloud.service.CustomInfoService;
import com.wgcloud.service.DbInfoService;
import com.wgcloud.service.DbTableService;
import com.wgcloud.service.DceInfoService;
import com.wgcloud.service.DiskStateService;
import com.wgcloud.service.DockerInfoService;
import com.wgcloud.service.FileSafeService;
import com.wgcloud.service.FileWarnInfoService;
import com.wgcloud.service.FtpInfoService;
import com.wgcloud.service.HeathMonitorService;
import com.wgcloud.service.HostDiskPerService;
import com.wgcloud.service.LogInfoService;
import com.wgcloud.service.PortInfoService;
import com.wgcloud.service.ShellInfoService;
import com.wgcloud.service.SnmpDeepInfoService;
import com.wgcloud.service.SnmpInfoService;
import com.wgcloud.service.SystemInfoService;
import com.wgcloud.service.TaskJobInfoService;
import com.wgcloud.service.TaskUtilService;
import com.wgcloud.util.DateUtil;
import com.wgcloud.util.FormatUtil;
import com.wgcloud.util.HostUtil;
import com.wgcloud.util.ThreadPoolUtil;
import com.wgcloud.util.staticvar.StaticKeys;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value={"/dapingNew"})
public class DapingNewController {
    private static final Logger logger = LoggerFactory.getLogger(DapingNewController.class);
    @Resource
    private DbTableService dbTableService;
    @Resource
    private PortInfoService portInfoService;
    @Resource
    private FileSafeService fileSafeService;
    @Resource
    private DceInfoService dceInfoService;
    @Resource
    private SnmpInfoService snmpInfoService;
    @Resource
    private DbInfoService dbInfoService;
    @Resource
    private SystemInfoService systemInfoService;
    @Resource
    private AppInfoService appInfoService;
    @Resource
    private FileWarnInfoService fileWarnInfoService;
    @Resource
    private CustomInfoService customInfoService;
    @Resource
    private HostDiskPerService hostDiskPerService;
    @Resource
    private DockerInfoService dockerInfoService;
    @Resource
    private LogInfoService logInfoService;
    @Autowired
    private HeathMonitorService heathMonitorService;
    @Autowired
    private ShellInfoService shellInfoService;
    @Resource
    private FtpInfoService ftpInfoService;
    @Resource
    private SnmpDeepInfoService snmpDeepInfoService;
    @Resource
    private TaskJobInfoService taskJobInfoService;
    @Resource
    private DiskStateService diskStateService;
    @Autowired
    private CommonConfig commonConfig;
    @Autowired
    private ServletContext servletContext;
    @Autowired
    private TaskUtilService taskUtilService;

    private void testThread() {
        Runnable runnable = () -> logger.info("DapingNewController----------testThread");
        ThreadPoolUtil.executor.execute(runnable);
    }

    @RequestMapping(value={"index"})
    public String index(Model model, HttpServletRequest request) {
        if (!StaticKeys.LICENSE_STATE.equals("1")) {
            return "daping/error";
        }
        HashMap<String, Object> params = new HashMap<String, Object>();
        try {
            params.put("countBlockNe", "2");
            HostUtil.addAccountqueryDaping(request, params);
            int totalSystemInfoSize = this.systemInfoService.countByParams(params);
            model.addAttribute("totalSystemInfoSize", (Object)totalSystemInfoSize);
            params.put("state", "2");
            int hostDownSize = this.systemInfoService.countByParams(params);
            model.addAttribute("hostDownSize", (Object)hostDownSize);
            params.clear();
            HostUtil.addAccountqueryDaping(request, params);
            int totalSizeApp = this.appInfoService.countByParams(params);
            model.addAttribute("totalSizeApp", (Object)totalSizeApp);
            params.put("state", "2");
            int downAppSize = this.appInfoService.countByParams(params);
            model.addAttribute("downAppSize", (Object)downAppSize);
            params.clear();
            HostUtil.addAccountqueryDaping(request, params);
            int dockerSize = this.dockerInfoService.countByParams(params);
            model.addAttribute("dockerSize", (Object)dockerSize);
            params.put("state", "2");
            int downDockerSize = this.dockerInfoService.countByParams(params);
            model.addAttribute("downDockerSize", (Object)downDockerSize);
            params.clear();
            HostUtil.addAccountqueryDaping(request, params);
            int portSize = this.portInfoService.countByParams(params);
            model.addAttribute("portSize", (Object)portSize);
            params.put("state", "2");
            int portDownSize = this.portInfoService.countByParams(params);
            model.addAttribute("portDownSize", (Object)portDownSize);
            params.clear();
            HostUtil.addAccountqueryDaping(request, params);
            int dceSize = this.dceInfoService.countByParams(params);
            model.addAttribute("dceSize", (Object)dceSize);
            params.put("resTimes", -1);
            int dceDownSize = this.dceInfoService.countByParams(params);
            model.addAttribute("dceDownSize", (Object)dceDownSize);
            params.clear();
            HostUtil.addAccountqueryDaping(request, params);
            int snmpSize = this.snmpInfoService.countByParams(params);
            model.addAttribute("snmpSize", (Object)snmpSize);
            params.put("state", "2");
            int snmpDownSize = this.snmpInfoService.countByParams(params);
            model.addAttribute("snmpDownSize", (Object)snmpDownSize);
            params.clear();
            HostUtil.addAccountqueryDaping(request, params);
            Integer fileWarnSize = this.fileWarnInfoService.countByParams(params);
            model.addAttribute("fileWarnSize", (Object)(fileWarnSize == null ? 0 : fileWarnSize));
            params.clear();
            HostUtil.addAccountqueryDaping(request, params);
            Integer customInfoSize = this.customInfoService.countByParams(params);
            model.addAttribute("customInfoSize", (Object)(customInfoSize == null ? 0 : customInfoSize));
            params.put("state", "2");
            int customInfoDownSize = this.customInfoService.countByParams(params);
            model.addAttribute("customInfoDownSize", (Object)customInfoDownSize);
            params.clear();
            HostUtil.addAccountqueryDaping(request, params);
            Integer fileSafeSize = this.fileSafeService.countByParams(params);
            model.addAttribute("fileSafeSize", (Object)(fileSafeSize == null ? 0 : fileSafeSize));
            params.put("state", "2");
            int fileSafeDownSize = this.fileSafeService.countByParams(params);
            model.addAttribute("fileSafeDownSize", (Object)fileSafeDownSize);
            params.clear();
            HostUtil.addAccountqueryDaping(request, params);
            Integer dbTableSize = this.dbTableService.countByParams(params);
            model.addAttribute("dbTableSize", (Object)dbTableSize);
            params.clear();
            HostUtil.addAccountqueryDaping(request, params);
            int dbInfoSize = this.dbInfoService.countByParams(params);
            model.addAttribute("dbInfoSize", (Object)dbInfoSize);
            params.put("dbState", "2");
            int dbInfoDownSize = this.dbInfoService.countByParams(params);
            model.addAttribute("dbInfoDownSize", (Object)dbInfoDownSize);
            params.clear();
            HostUtil.addAccountqueryDaping(request, params);
            int heathSize = this.heathMonitorService.countByParams(params);
            model.addAttribute("heathSize", (Object)heathSize);
            params.put("heathStatus", "200");
            int heath200Size = this.heathMonitorService.countByParams(params);
            model.addAttribute("heath200Size", (Object)heath200Size);
            model.addAttribute("heatherrSize", (Object)(heathSize - heath200Size));
            params.clear();
            HostUtil.addAccountqueryDaping(request, params);
            int shellInfoSize = this.shellInfoService.countByParams(params);
            model.addAttribute("shellInfoSize", (Object)shellInfoSize);
            params.clear();
            HostUtil.addAccountqueryDaping(request, params);
            int ftpInfoSize = this.ftpInfoService.countByParams(params);
            params.clear();
            HostUtil.addAccountqueryDaping(request, params);
            int snmpDeepSize = this.snmpDeepInfoService.countByParams(params);
            model.addAttribute("snmpDeepSize", (Object)snmpDeepSize);
            Integer resourceAllSize = totalSystemInfoSize + totalSizeApp + dockerSize + portSize + dceSize + snmpSize + fileWarnSize + dbInfoSize + heathSize + fileSafeSize + dbTableSize + customInfoSize + ftpInfoSize + snmpDeepSize;
            model.addAttribute("resourceAllSize", (Object)resourceAllSize);
            params.clear();
            HostUtil.addAccountqueryDaping(request, params);
            int taskJobSize = this.taskJobInfoService.countByParams(params);
            model.addAttribute("taskJobSize", (Object)taskJobSize);
            params.clear();
            params.put("countBlockNe", "2");
            HostUtil.addAccountqueryDaping(request, params);
            List<SystemInfo> systemInfoList = this.systemInfoService.selectAllByParams(params);
            this.setSystemType(systemInfoList, model);
            this.setTopDownHostChart(model);
            this.setMiddleData(systemInfoList, model, request);
            this.setLogInfo(model);
            this.setHeathMonitorType(model, heathSize);
            this.hostPowerChart(model, totalSystemInfoSize, totalSizeApp, dockerSize, portSize, fileWarnSize, fileSafeSize, customInfoSize);
            this.hostInfoTop6(model, request);
            HostUtil.addDapingTipMsg(request, model);
            AccountInfo accountInfo = HostUtil.getAccountByRequest(request);
            model.addAttribute("sumDiskSizeCache", this.servletContext.getAttribute("sumDiskSizeCache"));
            if (null != accountInfo && "1".equals(request.getParameter("viewDapingNowAccount")) && "user".equals(accountInfo.getRole())) {
                model.addAttribute("sumDiskSizeCache", (Object)this.taskUtilService.sumDiskSizeCache(request));
            }
            return "daping/indexNew";
        }
        catch (Exception e) {
            logger.error("\u5927\u5c4f\u5c55\u793a\u9519\u8bef", (Throwable)e);
            return "";
        }
    }

    private void setHeathMonitorType(Model model, Integer heathSize) {
        HashMap<String, Object> params = new HashMap<String, Object>();
        try {
            params.put("orderBy", "RES_TIMES");
            params.put("orderType", "DESC");
            PageInfo pageInfo = this.heathMonitorService.selectByParamsNoContent(params, 1, 5);
            ArrayList<ChartDapingNewInfo> chartList = new ArrayList<ChartDapingNewInfo>();
            for (HeathMonitor heathMonitor : (java.util.List<HeathMonitor>) pageInfo.getList()) {
                ChartDapingNewInfo chartDapingNewInfo = new ChartDapingNewInfo();
                chartDapingNewInfo.setName(heathMonitor.getAppName());
                chartDapingNewInfo.setValue(heathMonitor.getResTimes());
                chartList.add(chartDapingNewInfo);
            }
            if (CollectionUtil.isEmpty(chartList)) {
                ChartDapingNewInfo chartDapingNewInfo = new ChartDapingNewInfo();
                chartDapingNewInfo.setName("\u6682\u65e0\u670d\u52a1\u63a5\u53e3");
                chartDapingNewInfo.setValue(1);
                chartList.add(chartDapingNewInfo);
            }
            model.addAttribute("heathMonitorTop5List", (Object)JSONUtil.parseArray(chartList));
        }
        catch (Exception e) {
            logger.error("setHeathMonitorType\u9519\u8bef", (Throwable)e);
        }
    }

    private void setSystemType(List<SystemInfo> systemInfoList, Model model) {
        Map<String, Integer> map = HostUtil.getSystemTypeMap(systemInfoList);
        Integer windows = 0;
        Integer centos = 0;
        Integer redhat = 0;
        Integer ubuntu = 0;
        Integer debian = 0;
        Integer others = 0;
        Integer kylin = 0;
        Integer android = 0;
        Integer macOS = 0;
        if (null != map.get("windows")) {
            windows = map.get("windows");
        }
        if (null != map.get("centos")) {
            centos = map.get("centos");
        }
        if (null != map.get("redhat")) {
            redhat = map.get("redhat");
        }
        if (null != map.get("ubuntu")) {
            ubuntu = map.get("ubuntu");
        }
        if (null != map.get("debian")) {
            debian = map.get("debian");
        }
        if (null != map.get("kylin")) {
            kylin = map.get("kylin");
        }
        if (null != map.get("android")) {
            android = map.get("android");
        }
        if (null != map.get("macOS")) {
            macOS = map.get("macOS");
        }
        if (null != map.get("\u5176\u4ed6")) {
            others = map.get("\u5176\u4ed6");
        }
        model.addAttribute("windowsSize", (Object)windows);
        model.addAttribute("centosSize", (Object)centos);
        model.addAttribute("redhatSize", (Object)redhat);
        model.addAttribute("ubuntuSize", (Object)ubuntu);
        model.addAttribute("debianSize", (Object)debian);
        model.addAttribute("othersSize", (Object)(others + macOS));
        model.addAttribute("kylinSize", (Object)kylin);
        model.addAttribute("androidSize", (Object)android);
    }

    private void setTopDownHostChart(Model model) {
        HashMap<String, Object> params = new HashMap<String, Object>();
        ArrayList<ChartDapingNewInfo> chartInfoList = new ArrayList<ChartDapingNewInfo>();
        try {
            int days = 7;
            List<String> dateList = this.getDateList(days);
            String startDay = dateList.get(0);
            String endDay = dateList.get(dateList.size() - 1);
            params.put("startTime", startDay + " 00:00:00");
            params.put("endTime", endDay + " 23:59:59");
            params.put("state", "1");
            int size = this.logInfoService.countByParams(params);
            ChartDapingNewInfo chartInfoYwgj = new ChartDapingNewInfo();
            chartInfoYwgj.setName("\u4e1a\u52a1\u544a\u8b66");
            chartInfoYwgj.setValue(size);
            chartInfoList.add(chartInfoYwgj);
            params.put("state", "3");
            size = this.logInfoService.countByParams(params);
            ChartDapingNewInfo chartInfoYwgjhf = new ChartDapingNewInfo();
            chartInfoYwgjhf.setName("\u4e1a\u52a1\u544a\u8b66\u6062\u590d");
            chartInfoYwgjhf.setValue(size);
            chartInfoList.add(chartInfoYwgjhf);
            params.put("state", "2");
            params.put("hostname", "\u9519\u8bef");
            size = this.logInfoService.countByParams(params);
            ChartDapingNewInfo chartInfoXtcw = new ChartDapingNewInfo();
            chartInfoXtcw.setName("\u7cfb\u7edf\u9519\u8bef");
            chartInfoXtcw.setValue(size);
            chartInfoList.add(chartInfoXtcw);
            params.put("state", "2");
            params.remove("hostname");
            params.put("hostnameNe", "\u9519\u8bef");
            size = this.logInfoService.countByParams(params);
            ChartDapingNewInfo chartInfoXtcz = new ChartDapingNewInfo();
            chartInfoXtcz.setName("\u7cfb\u7edf\u64cd\u4f5c");
            chartInfoXtcz.setValue(size);
            chartInfoList.add(chartInfoXtcz);
            params.remove("state");
            params.put("hostname", "\u4e3b\u673a\u767b\u5f55\u4fe1\u606f\u63d0\u9192");
            params.remove("hostnameNe");
            size = this.logInfoService.countByParams(params);
            ChartDapingNewInfo chartInfoLoginInfo = new ChartDapingNewInfo();
            chartInfoLoginInfo.setName("\u767b\u5f55\u63d0\u9192");
            chartInfoLoginInfo.setValue(size);
            chartInfoList.add(chartInfoLoginInfo);
            params.put("hostname", "\u5f00\u59cb\u4e0b\u53d1\u6307\u4ee4");
            size = this.logInfoService.countByParams(params);
            ChartDapingNewInfo chartInfoShell = new ChartDapingNewInfo();
            chartInfoShell.setName("\u4e0b\u53d1\u6307\u4ee4");
            chartInfoShell.setValue(size);
            chartInfoList.add(chartInfoShell);
            model.addAttribute("topDownHostChart", (Object)JSONUtil.parseArray(chartInfoList));
        }
        catch (Exception e) {
            logger.error("\u65b0\u7248\u5927\u5c4f\u6700\u8fd1\u4e00\u5468\u7cfb\u7edf\u65e5\u5fd7\u5206\u7c7b\u9519\u8bef", (Throwable)e);
        }
    }

    private void setLogInfo(Model model) {
        try {
            ArrayList recordList = new ArrayList();
            HashMap<String, Object> params = new HashMap<String, Object>();
            PageInfo logListPage = this.logInfoService.selectByParamsNoContent(params, 1, 10);
            if (logListPage.getList().size() <= 5) {
                model.addAttribute("logLeftList", (Object)logListPage.getList());
                model.addAttribute("logRightList", recordList);
            }
            if (logListPage.getList().size() > 5) {
                model.addAttribute("logLeftList", logListPage.getList().subList(0, 5));
                model.addAttribute("logRightList", logListPage.getList().subList(5, logListPage.getList().size()));
            }
        }
        catch (Exception e) {
            logger.error("\u7cfb\u7edf\u65e5\u5fd7\u67e5\u8be2\u9519\u8bef", (Throwable)e);
        }
    }

    private void hostPowerChart(Model model, Integer totalSystemInfoSize, Integer totalSizeApp, Integer dockerSize, Integer portSize, Integer fileWarnSize, Integer fileSafeSize, Integer customInfoSize) {
        try {
            int hostPowerMaxSize = 1;
            if (totalSystemInfoSize > hostPowerMaxSize) {
                hostPowerMaxSize = totalSystemInfoSize;
            }
            if (totalSizeApp > hostPowerMaxSize) {
                hostPowerMaxSize = totalSizeApp;
            }
            if (dockerSize > hostPowerMaxSize) {
                hostPowerMaxSize = dockerSize;
            }
            if (portSize > hostPowerMaxSize) {
                hostPowerMaxSize = portSize;
            }
            if (fileWarnSize > hostPowerMaxSize) {
                hostPowerMaxSize = fileWarnSize;
            }
            if (fileSafeSize > hostPowerMaxSize) {
                hostPowerMaxSize = fileSafeSize;
            }
            if (customInfoSize > hostPowerMaxSize) {
                hostPowerMaxSize = customInfoSize;
            }
            model.addAttribute("hostPowerMaxSize", (Object)hostPowerMaxSize);
        }
        catch (Exception e) {
            logger.error("\u8bbe\u7f6e\u8718\u86db\u7f51\u56fe\u9519\u8bef", (Throwable)e);
        }
    }

    private void setMiddleData(List<SystemInfo> list, Model model, HttpServletRequest request) {
        try {
            Double maxCpu = 0.0;
            Double avgCpu = 0.0;
            Double minCpu = 1000.0;
            Double sumCpu = 0.0;
            Double maxMem = 0.0;
            Double minMem = 1000.0;
            Double avgMem = 0.0;
            Double sumMem = 0.0;
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
            long uptimeSum = 0L;
            long uptimeAvg = 0L;
            Double maxDiskPer = 0.0;
            Double minDiskPer = 1000.0;
            Double avgDiskPer = 0.0;
            Double sumDiskPer = 0.0;
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
                if (null != systemInfo.getUptime()) {
                    uptimeSum += systemInfo.getUptime().longValue();
                }
                if (null != systemInfo.getDiskPer() && systemInfo.getDiskPer() > maxDiskPer) {
                    maxDiskPer = systemInfo.getDiskPer();
                }
                if (null != systemInfo.getDiskPer() && systemInfo.getDiskPer() < minDiskPer) {
                    minDiskPer = systemInfo.getDiskPer();
                }
                if (null == systemInfo.getDiskPer()) continue;
                sumDiskPer = sumDiskPer + systemInfo.getDiskPer();
            }
            model.addAttribute("cpuCoresSum", (Object)cpuCoresSum);
            model.addAttribute("memSum", (Object)FormatUtil.gToT(memSum + ""));
            if (systemSize > 0) {
                avgCpu = sumCpu / (double)list.size();
                avgMem = sumMem / (double)list.size();
                avgFifteenLoad = sumFifteenLoad / (double)list.size();
                avgFiveLoad = sumFiveLoad / (double)list.size();
                uptimeAvg = uptimeSum / (long)list.size();
                avgDiskPer = sumDiskPer / (double)list.size();
            } else {
                minCpu = 0.0;
                minMem = 0.0;
                avgFifteenLoad = 0.0;
                avgFiveLoad = 0.0;
                minDiskPer = 0.0;
            }
            model.addAttribute("uptimeSum", (Object)FormatUtil.secondsToDays(uptimeAvg));
            model.addAttribute("maxCpuInfo", (Object)maxCpu);
            model.addAttribute("avgCpuInfo", (Object)FormatUtil.formatDouble(avgCpu, 2));
            model.addAttribute("minCpuInfo", (Object)minCpu);
            model.addAttribute("maxMemInfo", (Object)maxMem);
            model.addAttribute("avgMemInfo", (Object)FormatUtil.formatDouble(avgMem, 2));
            model.addAttribute("minMemInfo", (Object)minMem);
            model.addAttribute("maxFiveLoad", (Object)maxFiveLoad);
            model.addAttribute("minFiveLoad", (Object)minFiveLoad);
            model.addAttribute("avgFiveLoad", (Object)FormatUtil.formatDouble(avgFiveLoad, 2));
            model.addAttribute("maxFifteenLoad", (Object)maxFifteenLoad);
            model.addAttribute("minFifteenLoad", (Object)minFifteenLoad);
            model.addAttribute("avgFifteenLoad", (Object)FormatUtil.formatDouble(avgFifteenLoad, 2));
            model.addAttribute("maxDiskPer", (Object)maxDiskPer);
            model.addAttribute("avgDiskPer", (Object)FormatUtil.formatDouble(avgDiskPer, 2));
            model.addAttribute("minDiskPer", (Object)minDiskPer);
        }
        catch (Exception e) {
            logger.error("v2\u7248\u5927\u5c4f\u7edf\u8ba1\u6240\u6709\u4e3b\u673a\u6700\u9ad8\u6700\u4f4e\u5e73\u5747\u503c\u9519\u8bef", (Throwable)e);
        }
    }

    private void hostInfoTop6(Model model, HttpServletRequest request) {
        try {
            HashMap<String, Object> params = new HashMap<String, Object>();
            params.put("orderBy", "CREATE_TIME");
            params.put("orderType", "DESC");
            params.put("countBlockNe", "2");
            HostUtil.addAccountqueryDaping(request, params);
            PageInfo pageInfo = this.systemInfoService.selectByParams(params, 1, 6);
            ArrayList<String> hostNameTop6List = new ArrayList<String>();
            ArrayList<Double> hostMemTop6List = new ArrayList<Double>();
            ArrayList<Double> hostCpuTop6List = new ArrayList<Double>();
            ArrayList<Integer> hostProcsTop6List = new ArrayList<Integer>();
            ArrayList<Integer> hostNetConnectionsTop6List = new ArrayList<Integer>();
            ArrayList<Double> hostDiskPerTop6List = new ArrayList<Double>();
            ArrayList<Double> hostSwapMemPerTop6List = new ArrayList<Double>();
            HashMap paramsAppInfo = new HashMap();
            for (SystemInfo systemInfo : (java.util.List<SystemInfo>) pageInfo.getList()) {
                hostNameTop6List.add(systemInfo.getHostname());
                hostMemTop6List.add(systemInfo.getMemPer());
                hostCpuTop6List.add(systemInfo.getCpuPer());
                if (!StringUtils.isEmpty((CharSequence)systemInfo.getProcs())) {
                    hostProcsTop6List.add(Integer.valueOf(systemInfo.getProcs()));
                } else {
                    hostProcsTop6List.add(0);
                }
                if (!StringUtils.isEmpty((CharSequence)systemInfo.getNetConnections())) {
                    hostNetConnectionsTop6List.add(Integer.valueOf(systemInfo.getNetConnections()));
                } else {
                    hostNetConnectionsTop6List.add(0);
                }
                if (!StringUtils.isEmpty((CharSequence)systemInfo.getSwapMemPer()) && FormatUtil.isNumber(systemInfo.getSwapMemPer())) {
                    hostSwapMemPerTop6List.add(FormatUtil.strToDouble(systemInfo.getSwapMemPer()));
                } else {
                    hostSwapMemPerTop6List.add(0.0);
                }
                hostDiskPerTop6List.add(systemInfo.getDiskPer());
            }
            model.addAttribute("hostNameTop6List", (Object)JSONUtil.parseArray(hostNameTop6List));
            model.addAttribute("hostMemTop6List", (Object)JSONUtil.parseArray(hostMemTop6List));
            model.addAttribute("hostCpuTop6List", (Object)JSONUtil.parseArray(hostCpuTop6List));
            model.addAttribute("hostProcsTop6List", (Object)JSONUtil.parseArray(hostProcsTop6List));
            model.addAttribute("hostNetConnectionsTop6List", (Object)JSONUtil.parseArray(hostNetConnectionsTop6List));
            model.addAttribute("hostDiskPerTop6List", (Object)JSONUtil.parseArray(hostDiskPerTop6List));
            model.addAttribute("hostSwapMemPerTop6List", (Object)JSONUtil.parseArray(hostSwapMemPerTop6List));
        }
        catch (Exception e) {
            logger.error("\u4e3b\u673a\u6700\u65b0\u4e0a\u62a5\u524d10\u9519\u8bef", (Throwable)e);
        }
    }

    public List<String> getDateList(int days) {
        ArrayList<String> dateList = new ArrayList<String>();
        String sevenDayBefore = DateUtil.getDateBefore(days);
        for (int i = 1; i < days + 1; ++i) {
            sevenDayBefore = DateUtil.getDateBefore(i);
            dateList.add(sevenDayBefore.substring(0, 10));
        }
        CollectionUtil.reverse(dateList);
        return dateList;
    }
}

