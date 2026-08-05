/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.dapingController;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.github.pagehelper.PageInfo;
import com.wgcloud.config.CommonConfig;
import com.wgcloud.dto.ChartDapingNewInfo;
import com.wgcloud.entity.AccountInfo;
import com.wgcloud.entity.AgentRunState;
import com.wgcloud.entity.SystemInfo;
import com.wgcloud.service.AccountInfoService;
import com.wgcloud.service.AgentRunStateService;
import com.wgcloud.service.AppExceptionInfoService;
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
import com.wgcloud.service.K8sMonitorService;
import com.wgcloud.service.KafkaMonitorService;
import com.wgcloud.service.LogInfoService;
import com.wgcloud.service.PortInfoService;
import com.wgcloud.service.RedisMonitorService;
import com.wgcloud.service.ShellInfoService;
import com.wgcloud.service.SnmpDeepInfoService;
import com.wgcloud.service.SnmpInfoService;
import com.wgcloud.service.SystemInfoService;
import com.wgcloud.service.TaskJobInfoService;
import com.wgcloud.service.TaskUtilService;
import com.wgcloud.util.ActivemqUtil;
import com.wgcloud.util.FormatUtil;
import com.wgcloud.util.HostUtil;
import com.wgcloud.util.PowerEnvUtil;
import com.wgcloud.util.RabbitmqUtil;
import com.wgcloud.util.license.LicenseUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value={"/dapingEight"})
public class DapingEightController {
    private static final Logger logger = LoggerFactory.getLogger(DapingEightController.class);
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
    private AppExceptionInfoService appExceptionInfoService;
    @Resource
    private DockerInfoService dockerInfoService;
    @Resource
    private LogInfoService logInfoService;
    @Autowired
    private HeathMonitorService heathMonitorService;
    @Autowired
    private ShellInfoService shellInfoService;
    @Resource
    private AccountInfoService accountInfoService;
    @Resource
    private DiskStateService diskStateService;
    @Resource
    private FtpInfoService ftpInfoService;
    @Resource
    private TaskJobInfoService taskJobInfoService;
    @Resource
    private SnmpDeepInfoService snmpDeepInfoService;
    @Resource
    private RedisMonitorService redisMonitorService;
    @Resource
    private KafkaMonitorService kafkaMonitorService;
    @Resource
    private K8sMonitorService k8sMonitorService;
    @Autowired
    private ServletContext servletContext;
    @Autowired
    private TaskUtilService taskUtilService;
    @Resource
    private HostDiskPerService hostDiskPerService;
    @Resource
    private AgentRunStateService agentRunStateService;
    @Autowired
    private CommonConfig commonConfig;

    @RequestMapping(value={"index"})
    public String index(Model model, HttpServletRequest request) {
        if (!LicenseUtil.checkEnterpriseVersion()) {
            return "daping/error";
        }
        try {
            HashMap<String, Object> params = new HashMap<String, Object>();
            params.put("countBlockNe", "2");
            HostUtil.addAccountqueryDaping(request, params);
            int totalSystemInfoSize = this.systemInfoService.countByParams(params);
            model.addAttribute("totalSystemInfoSize", (Object)totalSystemInfoSize);
            params.clear();
            HostUtil.addAccountqueryDaping(request, params);
            int dceSize = this.dceInfoService.countByParams(params);
            int snmpSize = this.snmpInfoService.countByParams(params);
            int snmpDeepSize = this.snmpDeepInfoService.countByParams(params);
            model.addAttribute("pingSnmpSumSize", (Object)(dceSize + snmpSize + snmpDeepSize));
            params.clear();
            params.put("countBlockNe", "2");
            HostUtil.addAccountqueryDaping(request, params);
            List<SystemInfo> systemInfoList = this.systemInfoService.selectAllByParams(params);
            this.setSystemType(systemInfoList, model);
            this.setMiddleData(systemInfoList, model, request);
            this.setAgentRunAvgDay(model, request);
            this.queryProcsHostTop(model, request);
            this.maxCpuMemPerHost(model, request);
            this.setSystemType(systemInfoList, model);
            this.queryDiskperHostTop(model, request);
            this.systemTop10(model, request);
            this.monitorResourceChart(model, request);
            AccountInfo accountInfo = HostUtil.getAccountByRequest(request);
            model.addAttribute("sumDiskSizeCache", this.servletContext.getAttribute("sumDiskSizeCache"));
            if (null != accountInfo && "1".equals(request.getParameter("viewDapingNowAccount")) && "user".equals(accountInfo.getRole())) {
                model.addAttribute("sumDiskSizeCache", (Object)this.taskUtilService.sumDiskSizeCache(request));
            }
            HostUtil.addDapingTipMsg(request, model);
            return "daping/indexEight";
        }
        catch (Exception e) {
            logger.error("\u5927\u5c4f\u5c55\u793a\u9519\u8bef", (Throwable)e);
            return "";
        }
    }

    private void systemTop10(Model model, HttpServletRequest request) {
        try {
            HashMap<String, Object> params = new HashMap<String, Object>();
            params.put("orderBy", "CREATE_TIME");
            params.put("orderType", "DESC");
            params.put("countBlockNe", "2");
            HostUtil.addAccountqueryDaping(request, params);
            PageInfo pageInfo = this.systemInfoService.selectByParams(params, 1, 10);
            ArrayList<String> systemTop10NameList = new ArrayList<String>();
            ArrayList<String> rxbytTop10List = new ArrayList<String>();
            ArrayList<String> txbytTop10List = new ArrayList<String>();
            for (SystemInfo systemInfo : (java.util.List<SystemInfo>) pageInfo.getList()) {
                systemTop10NameList.add(systemInfo.getHostname());
                rxbytTop10List.add(systemInfo.getRxbyt());
                txbytTop10List.add(systemInfo.getTxbyt());
            }
            model.addAttribute("systemTop10NameList", (Object)JSONUtil.parseArray(systemTop10NameList));
            model.addAttribute("rxbytTop10List", (Object)JSONUtil.parseArray(rxbytTop10List));
            model.addAttribute("txbytTop10List", (Object)JSONUtil.parseArray(txbytTop10List));
        }
        catch (Exception e) {
            logger.error("\u67e5\u8be2\u4e0a\u4e0b\u884c\u901f\u7387\u6700\u65b0\u4e0a\u62a5\u524d10\u4e3b\u673a\u9519\u8bef", (Throwable)e);
        }
    }

    private void setAgentRunAvgDay(Model model, HttpServletRequest request) {
        try {
            HashMap<String, Object> params = new HashMap<String, Object>();
            HostUtil.addAccountqueryDaping(request, params);
            List<AgentRunState> agentRunList = this.agentRunStateService.selectAllByParams(params);
            Double avgDays = 0.0;
            Double agentSum = 0.0;
            for (AgentRunState agentRunState : agentRunList) {
                agentSum = agentSum + (double)agentRunState.getOnlineTime().intValue();
            }
            if (agentRunList.size() > 0) {
                avgDays = agentSum / (double)agentRunList.size();
            }
            model.addAttribute("avgAgentRunDays", (Object)(FormatUtil.formatDouble(avgDays, 2) + "\u5929"));
        }
        catch (Exception e) {
            logger.error("\u8ba1\u7b97\u4e3b\u673a\u7ec8\u7aefagent\u5e73\u5747\u8fd0\u884c\u65f6\u957f\u9519\u8bef", (Throwable)e);
        }
    }

    private void maxCpuMemPerHost(Model model, HttpServletRequest request) {
        try {
            HashMap<String, Object> params = new HashMap<String, Object>();
            params.put("orderBy", "CPU_PER");
            params.put("orderType", "DESC");
            HostUtil.addAccountqueryDaping(request, params);
            PageInfo pageInfo = this.systemInfoService.selectByParams(params, 1, 5);
            ArrayList<String> cpuMaxNameList = new ArrayList<String>();
            ArrayList<Double> cpuMaxValList = new ArrayList<Double>();
            for (SystemInfo systemInfo : (java.util.List<SystemInfo>) pageInfo.getList()) {
                cpuMaxNameList.add(systemInfo.getHostname());
                cpuMaxValList.add(systemInfo.getCpuPer());
            }
            params.put("orderBy", "MEM_PER");
            PageInfo pageInfoMem = this.systemInfoService.selectByParams(params, 1, 5);
            ArrayList<String> memMaxNameList = new ArrayList<String>();
            ArrayList<Double> memMaxValList = new ArrayList<Double>();
            for (SystemInfo systemInfo : (java.util.List<SystemInfo>) pageInfoMem.getList()) {
                memMaxNameList.add(systemInfo.getHostname());
                memMaxValList.add(systemInfo.getMemPer());
            }
            model.addAttribute("cpuMaxNameList", (Object)JSONUtil.parseArray(cpuMaxNameList));
            model.addAttribute("cpuMaxValList", (Object)JSONUtil.parseArray(cpuMaxValList));
            model.addAttribute("memMaxNameList", (Object)JSONUtil.parseArray(memMaxNameList));
            model.addAttribute("memMaxValList", (Object)JSONUtil.parseArray(memMaxValList));
        }
        catch (Exception e) {
            logger.error("cpu\u4f7f\u7528\u7387\u6700\u65b0\u4e0a\u62a5\u524d10\u9519\u8bef", (Throwable)e);
        }
    }

    private void queryProcsHostTop(Model model, HttpServletRequest request) {
        try {
            HashMap<String, Object> params = new HashMap<String, Object>();
            HostUtil.addAccountqueryDaping(request, params);
            params.put("orderBy", "PROCS");
            params.put("orderType", "DESC");
            params.put("countBlockNe", "2");
            ArrayList<String> procsTopNameList = new ArrayList<String>();
            ArrayList<Double> procsTopValList = new ArrayList<Double>();
            PageInfo pageInfo = this.systemInfoService.selectByParams(params, 1, 5);
            for (SystemInfo systemInfo : (java.util.List<SystemInfo>) pageInfo.getList()) {
                procsTopNameList.add(systemInfo.getHostname());
                procsTopValList.add(Double.valueOf(systemInfo.getProcs()));
            }
            model.addAttribute("procsTopNameList", (Object)JSONUtil.parseArray(procsTopNameList));
            model.addAttribute("procsTopValList", (Object)JSONUtil.parseArray(procsTopValList));
        }
        catch (Exception e) {
            logger.error("\u67e5\u8be2\u8fd0\u884c\u8fdb\u7a0b\u6570\u91cf\u6700\u591a\u76845\u4e2a\u4e3b\u673a\u9519\u8bef", (Throwable)e);
        }
    }

    private void queryDiskperHostTop(Model model, HttpServletRequest request) {
        try {
            HashMap<String, Object> params = new HashMap<String, Object>();
            HostUtil.addAccountqueryDaping(request, params);
            params.put("orderBy", "DISK_PER");
            params.put("orderType", "DESC");
            PageInfo pageInfo = this.systemInfoService.selectByParams(params, 1, 5);
            ArrayList<String> hostNameList = new ArrayList<String>();
            ArrayList<Double> hostValList = new ArrayList<Double>();
            for (SystemInfo hostDiskPer : (java.util.List<SystemInfo>) pageInfo.getList()) {
                hostNameList.add(hostDiskPer.getHostname());
                hostValList.add(hostDiskPer.getDiskPer());
            }
            model.addAttribute("hostDiskperNameList", (Object)JSONUtil.parseArray(hostNameList));
            model.addAttribute("hostDiskperValList", (Object)JSONUtil.parseArray(hostValList));
        }
        catch (Exception e) {
            logger.error("\u67e5\u8be2\u8fd0\u884c\u78c1\u76d8\u4f7f\u7528\u7387%\u6700\u591a\u76845\u4e2a\u4e3b\u673a\u9519\u8bef", (Throwable)e);
        }
    }

    private void setSystemType(List<SystemInfo> systemInfoList, Model model) {
        try {
            ArrayList<ChartDapingNewInfo> chartInfoList = new ArrayList<ChartDapingNewInfo>();
            Map<String, Integer> map = HostUtil.getSystemTypeMap(systemInfoList);
            for (Map.Entry<String, Integer> entry : map.entrySet()) {
                ChartDapingNewInfo chartInfo = new ChartDapingNewInfo();
                chartInfo.setName(entry.getKey());
                chartInfo.setValue(entry.getValue());
                chartInfoList.add(chartInfo);
            }
            model.addAttribute("systemTypeList", (Object)JSONUtil.parseArray(chartInfoList));
        }
        catch (Exception e) {
            logger.error("\u8bbe\u7f6e\u6bcf\u4e2a\u7cfb\u7edf\u7c7b\u578b\u7684\u6570\u91cf\u9519\u8bef", (Throwable)e);
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
            Double maxSwapMem = 0.0;
            Double avgSwapMem = 0.0;
            Double minSwapMem = 1000.0;
            Double sumSwapMem = 0.0;
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
                Double nowSwapMemPer = 0.0;
                try {
                    nowSwapMemPer = Double.valueOf(systemInfo.getSwapMemPer());
                }
                catch (Exception e) {
                    logger.error("\u7ec4\u88c5\u4ea4\u6362\u533a\u5185\u5b58\u4f7f\u7528\u7387\u9519\u8bef", (Throwable)e);
                }
                if (null != nowSwapMemPer) {
                    if (nowSwapMemPer > maxSwapMem) {
                        maxSwapMem = nowSwapMemPer;
                    }
                    if (nowSwapMemPer < minSwapMem) {
                        minSwapMem = nowSwapMemPer;
                    }
                    sumSwapMem = sumSwapMem + nowSwapMemPer;
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
                avgSwapMem = sumSwapMem / (double)list.size();
                avgDiskPer = sumDiskPer / (double)list.size();
            } else {
                minCpu = 0.0;
                minMem = 0.0;
                avgFifteenLoad = 0.0;
                avgFiveLoad = 0.0;
                avgSwapMem = 0.0;
                minDiskPer = 0.0;
            }
            model.addAttribute("uptimeSum", (Object)FormatUtil.secondsToDays(uptimeAvg));
            model.addAttribute("maxCpuInfo", (Object)maxCpu);
            model.addAttribute("avgCpuInfo", (Object)FormatUtil.formatDouble(avgCpu, 2));
            model.addAttribute("minCpuInfo", (Object)minCpu);
            model.addAttribute("maxMemInfo", (Object)maxMem);
            model.addAttribute("avgMemInfo", (Object)FormatUtil.formatDouble(avgMem, 2));
            model.addAttribute("minMemInfo", (Object)minMem);
            model.addAttribute("maxSwapMemInfo", (Object)maxSwapMem);
            model.addAttribute("avgSwapMemInfo", (Object)FormatUtil.formatDouble(avgSwapMem, 2));
            model.addAttribute("minSwapMemInfo", (Object)minSwapMem);
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
            logger.error("v8\u5927\u5c4f\u7ec4\u88c5\u6700\u5927\u5e73\u5747\u6700\u4f4e\u503c\u9519\u8bef", (Throwable)e);
        }
    }

    private void monitorResourceChart(Model model, HttpServletRequest request) {
        try {
            ArrayList<ChartDapingNewInfo> chartInfoList = new ArrayList<ChartDapingNewInfo>();
            HashMap<String, Object> params = new HashMap<String, Object>();
            params.put("countBlockNe", "2");
            HostUtil.addAccountqueryDaping(request, params);
            int totalSystemInfoSize = this.systemInfoService.countByParams(params);
            ChartDapingNewInfo chartInfo = new ChartDapingNewInfo();
            chartInfo.setName("\u76d1\u63a7\u4e3b\u673a");
            chartInfo.setValue(totalSystemInfoSize);
            chartInfoList.add(chartInfo);
            params.clear();
            HostUtil.addAccountqueryDaping(request, params);
            int dceSize = this.dceInfoService.countByParams(params);
            ChartDapingNewInfo chartInfoPing = new ChartDapingNewInfo();
            chartInfoPing.setName("\u76d1\u63a7PING");
            chartInfoPing.setValue(dceSize);
            chartInfoList.add(chartInfoPing);
            params.clear();
            HostUtil.addAccountqueryDaping(request, params);
            int snmpSize = this.snmpInfoService.countByParams(params);
            ChartDapingNewInfo chartInfoSnmp = new ChartDapingNewInfo();
            chartInfoSnmp.setName("\u76d1\u63a7SNMP");
            chartInfoSnmp.setValue(snmpSize);
            chartInfoList.add(chartInfoSnmp);
            params.clear();
            HostUtil.addAccountqueryDaping(request, params);
            int dbInfoSize = this.dbInfoService.countByParams(params);
            ChartDapingNewInfo chartInfoDbInfo = new ChartDapingNewInfo();
            chartInfoDbInfo.setName("\u76d1\u63a7\u6570\u636e\u5e93");
            chartInfoDbInfo.setValue(dbInfoSize);
            chartInfoList.add(chartInfoDbInfo);
            params.clear();
            HostUtil.addAccountqueryDaping(request, params);
            int totalSizeApp = this.appInfoService.countByParams(params);
            ChartDapingNewInfo chartInfoApp = new ChartDapingNewInfo();
            chartInfoApp.setName("\u76d1\u63a7\u8fdb\u7a0b");
            chartInfoApp.setValue(totalSizeApp);
            chartInfoList.add(chartInfoApp);
            params.clear();
            HostUtil.addAccountqueryDaping(request, params);
            int dockerSize = this.dockerInfoService.countByParams(params);
            ChartDapingNewInfo chartInfoDocker = new ChartDapingNewInfo();
            chartInfoDocker.setName("\u76d1\u63a7Docker");
            chartInfoDocker.setValue(dockerSize);
            chartInfoList.add(chartInfoDocker);
            params.clear();
            HostUtil.addAccountqueryDaping(request, params);
            int portSize = this.portInfoService.countByParams(params);
            ChartDapingNewInfo chartInfoPort = new ChartDapingNewInfo();
            chartInfoPort.setName("\u76d1\u63a7\u7aef\u53e3");
            chartInfoPort.setValue(portSize);
            chartInfoList.add(chartInfoPort);
            params.clear();
            HostUtil.addAccountqueryDaping(request, params);
            Integer fileWarnSize = this.fileWarnInfoService.countByParams(params);
            ChartDapingNewInfo chartInfoFileWarn = new ChartDapingNewInfo();
            chartInfoFileWarn.setName("\u76d1\u63a7\u65e5\u5fd7");
            chartInfoFileWarn.setValue(fileWarnSize);
            chartInfoList.add(chartInfoFileWarn);
            params.clear();
            HostUtil.addAccountqueryDaping(request, params);
            Integer customInfoSize = this.customInfoService.countByParams(params);
            ChartDapingNewInfo chartInfoCustom = new ChartDapingNewInfo();
            chartInfoCustom.setName("\u81ea\u5b9a\u4e49\u76d1\u63a7\u9879");
            chartInfoCustom.setValue(customInfoSize);
            chartInfoList.add(chartInfoCustom);
            params.clear();
            HostUtil.addAccountqueryDaping(request, params);
            Integer fileSafeSize = this.fileSafeService.countByParams(params);
            ChartDapingNewInfo chartInfoFileSafe = new ChartDapingNewInfo();
            chartInfoFileSafe.setName("\u6587\u4ef6\u9632\u7be1\u6539");
            chartInfoFileSafe.setValue(fileSafeSize);
            chartInfoList.add(chartInfoFileSafe);
            params.clear();
            HostUtil.addAccountqueryDaping(request, params);
            Integer dbTableSize = this.dbTableService.countByParams(params);
            ChartDapingNewInfo chartInfoDbTable = new ChartDapingNewInfo();
            chartInfoDbTable.setName("\u6570\u636e\u8868\u76d1\u63a7");
            chartInfoDbTable.setValue(dbTableSize);
            chartInfoList.add(chartInfoDbTable);
            params.clear();
            HostUtil.addAccountqueryDaping(request, params);
            int heathSize = this.heathMonitorService.countByParams(params);
            ChartDapingNewInfo chartInfoHeath = new ChartDapingNewInfo();
            chartInfoHeath.setName("\u63a5\u53e3\u76d1\u63a7");
            chartInfoHeath.setValue(heathSize);
            chartInfoList.add(chartInfoHeath);
            params.clear();
            HostUtil.addAccountqueryDaping(request, params);
            int ftpInfoSize = this.ftpInfoService.countByParams(params);
            ChartDapingNewInfo chartInfoFTP = new ChartDapingNewInfo();
            chartInfoFTP.setName("FTP\u76d1\u63a7");
            chartInfoFTP.setValue(ftpInfoSize);
            chartInfoList.add(chartInfoFTP);
            params.clear();
            HostUtil.addAccountqueryDaping(request, params);
            int taskJobSize = this.taskJobInfoService.countByParams(params);
            ChartDapingNewInfo chartInfoTaskJob = new ChartDapingNewInfo();
            chartInfoTaskJob.setName("\u8ba1\u5212\u4efb\u52a1\u76d1\u63a7");
            chartInfoTaskJob.setValue(taskJobSize);
            chartInfoList.add(chartInfoTaskJob);
            params.clear();
            HostUtil.addAccountqueryDaping(request, params);
            int snmpDeepSize = this.snmpDeepInfoService.countByParams(params);
            ChartDapingNewInfo chartInfoSnmpDeep = new ChartDapingNewInfo();
            chartInfoSnmpDeep.setName("SNMP\u6df1\u5ea6\u76d1\u63a7");
            chartInfoSnmpDeep.setValue(snmpDeepSize);
            chartInfoList.add(chartInfoSnmpDeep);
            params.clear();
            int redisMonitorSize = this.redisMonitorService.countByParams(params);
            ChartDapingNewInfo chartInfoRedis = new ChartDapingNewInfo();
            chartInfoRedis.setName("Redis\u76d1\u63a7");
            chartInfoRedis.setValue(redisMonitorSize);
            chartInfoList.add(chartInfoRedis);
            params.clear();
            int kafkaSize = this.kafkaMonitorService.countByParams(params);
            ChartDapingNewInfo chartInfoKafka = new ChartDapingNewInfo();
            chartInfoKafka.setName("Kafka\u76d1\u63a7");
            chartInfoKafka.setValue(kafkaSize);
            chartInfoList.add(chartInfoKafka);
            List<JSONObject> powerEnvList = PowerEnvUtil.viewPowerEnvHandler();
            ChartDapingNewInfo chartInfoPowerEnv = new ChartDapingNewInfo();
            chartInfoPowerEnv.setName("\u52a8\u73af\u76d1\u63a7");
            chartInfoPowerEnv.setValue(powerEnvList.size());
            chartInfoList.add(chartInfoPowerEnv);
            List<JSONObject> rabbitmqList = RabbitmqUtil.viewRabbitmqHandler();
            ChartDapingNewInfo chartInfoRabbitmq = new ChartDapingNewInfo();
            chartInfoRabbitmq.setName("RabbitMQ\u76d1\u63a7");
            chartInfoRabbitmq.setValue(rabbitmqList.size());
            chartInfoList.add(chartInfoRabbitmq);
            List<JSONObject> activemqList = ActivemqUtil.viewActivemqHandler();
            ChartDapingNewInfo chartInfoActivemq = new ChartDapingNewInfo();
            chartInfoActivemq.setName("ActiveMQ\u76d1\u63a7");
            chartInfoActivemq.setValue(activemqList.size());
            chartInfoList.add(chartInfoActivemq);
            int k8sSize = this.k8sMonitorService.countByParams(params);
            ChartDapingNewInfo chartInfoK8s = new ChartDapingNewInfo();
            chartInfoK8s.setName("K8s\u76d1\u63a7");
            chartInfoK8s.setValue(k8sSize);
            chartInfoList.add(chartInfoK8s);
            model.addAttribute("monitorResourceList", (Object)JSONUtil.parseArray(chartInfoList));
        }
        catch (Exception e) {
            logger.error("\u76d1\u63a7\u8d44\u6e90\u6570\u91cf\u5206\u5e03\u7edf\u8ba1\u9519\u8bef", (Throwable)e);
        }
    }
}

