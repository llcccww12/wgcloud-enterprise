/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.dapingController;

import cn.hutool.json.JSONUtil;
import com.github.pagehelper.PageInfo;
import com.wgcloud.config.CommonConfig;
import com.wgcloud.entity.DbInfo;
import com.wgcloud.entity.HeathMonitor;
import com.wgcloud.entity.SystemInfo;
import com.wgcloud.service.AccountInfoService;
import com.wgcloud.service.AppExceptionInfoService;
import com.wgcloud.service.AppInfoService;
import com.wgcloud.service.CustomInfoService;
import com.wgcloud.service.DbInfoService;
import com.wgcloud.service.DbTableService;
import com.wgcloud.service.DceInfoService;
import com.wgcloud.service.DiskIoStateService;
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
import com.wgcloud.util.FormatUtil;
import com.wgcloud.util.HostUtil;
import com.wgcloud.util.license.LicenseUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value={"/dapingNine"})
public class DapingNineController {
    private static final Logger logger = LoggerFactory.getLogger(DapingNineController.class);
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
    private HostDiskPerService hostDiskPerService;
    @Resource
    private RedisMonitorService redisMonitorService;
    @Resource
    private KafkaMonitorService kafkaMonitorService;
    @Resource
    private K8sMonitorService k8sMonitorService;
    @Autowired
    private DiskIoStateService diskIoStateService;
    @Autowired
    private CommonConfig commonConfig;

    private void testThread() {
        new Thread(() -> logger.info("\u542f\u52a8\u5b50\u7ebf\u7a0b\u6d4b\u8bd5")).start();
    }

    @RequestMapping(value={"index"})
    public String index(Model model, HttpServletRequest request) {
        if (!LicenseUtil.checkEnterpriseVersion()) {
            return "daping/error";
        }
        try {
            this.setMiddleData(model, request);
            this.rightTabelData(model, request);
            this.setDiskIoInfo(model, request);
            this.dbHeathInfoHandle(model, request);
            this.setCpuFor10Min(model, request);
            HostUtil.addDapingTipMsg(request, model);
            return "daping/indexNine";
        }
        catch (Exception e) {
            logger.error("\u5927\u5c4f\u5c55\u793a\u9519\u8bef", (Throwable)e);
            return "";
        }
    }

    public void setCpuFor10Min(Model model, HttpServletRequest request) {
        try {
            HashMap<String, Object> params = new HashMap<String, Object>();
            params.put("orderBy", "CREATE_TIME");
            params.put("orderType", "DESC");
            params.put("countBlockNe", "2");
            HostUtil.addAccountqueryDaping(request, params);
            PageInfo pageInfo = this.systemInfoService.selectByParams(params, 1, 15);
            ArrayList<String> systemTop10NameList = new ArrayList<String>();
            ArrayList<Double> cpuTop10ValList = new ArrayList<Double>();
            ArrayList<Double> memTop10ValList = new ArrayList<Double>();
            ArrayList<Integer> procsTop10ValList = new ArrayList<Integer>();
            for (SystemInfo systemInfo : (java.util.List<SystemInfo>) pageInfo.getList()) {
                systemTop10NameList.add(systemInfo.getHostname());
                cpuTop10ValList.add(systemInfo.getCpuPer());
                memTop10ValList.add(systemInfo.getMemPer());
                procsTop10ValList.add(Integer.valueOf(systemInfo.getProcs()));
            }
            model.addAttribute("resultListCpuTime", (Object)JSONUtil.parseArray(systemTop10NameList));
            model.addAttribute("resultListCpuData", (Object)JSONUtil.parseArray(cpuTop10ValList));
            model.addAttribute("resultListMemData", (Object)JSONUtil.parseArray(memTop10ValList));
            model.addAttribute("resultListProcsData", (Object)JSONUtil.parseArray(procsTop10ValList));
        }
        catch (Exception e) {
            logger.error("v9\u5927\u5c4f\u7ec4\u88c5\u6570\u636esetCpuFor10Min\u9519\u8bef", (Throwable)e);
        }
    }

    private void setMiddleData(Model model, HttpServletRequest request) {
        try {
            HashMap<String, Object> paramsHost = new HashMap<String, Object>();
            paramsHost.put("countBlockNe", "2");
            HostUtil.addAccountqueryDaping(request, paramsHost);
            List<SystemInfo> list = this.systemInfoService.selectAllByParams(paramsHost);
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
            logger.error("v9\u5927\u5c4f\u7ec4\u88c5\u6700\u5927\u5e73\u5747\u6700\u4f4e\u503c\u9519\u8bef", (Throwable)e);
        }
    }

    private void rightTabelData(Model model, HttpServletRequest request) {
        try {
            HashMap<String, Object> params = new HashMap<String, Object>();
            params.put("countBlockNe", "2");
            HostUtil.addAccountqueryDaping(request, params);
            int totalSystemInfoSize = this.systemInfoService.countByParams(params);
            model.addAttribute("totalSystemInfoSize", (Object)totalSystemInfoSize);
            params.put("state", "2");
            int hostDownSize = this.systemInfoService.countByParams(params);
            model.addAttribute("hostDownSize", (Object)hostDownSize);
            double hostOnLinePer = 0.0;
            if (totalSystemInfoSize > 0) {
                hostOnLinePer = (1.0 - (double)hostDownSize / (double)totalSystemInfoSize) * 100.0;
                hostOnLinePer = FormatUtil.formatDouble(hostOnLinePer, 0);
            }
            model.addAttribute("hostOnLinePer", (Object)hostOnLinePer);
            params.clear();
            HostUtil.addAccountqueryDaping(request, params);
            int totalSizeApp = this.appInfoService.countByParams(params);
            model.addAttribute("totalSizeApp", (Object)totalSizeApp);
            params.put("state", "2");
            int downAppSize = this.appInfoService.countByParams(params);
            model.addAttribute("downAppSize", (Object)downAppSize);
            double appOnLinePer = 0.0;
            if (totalSizeApp > 0) {
                appOnLinePer = (1.0 - (double)downAppSize / (double)totalSizeApp) * 100.0;
                appOnLinePer = FormatUtil.formatDouble(appOnLinePer, 0);
            }
            model.addAttribute("appOnLinePer", (Object)appOnLinePer);
            params.clear();
            HostUtil.addAccountqueryDaping(request, params);
            int dockerSize = this.dockerInfoService.countByParams(params);
            model.addAttribute("dockerSize", (Object)dockerSize);
            params.put("state", "2");
            int downDockerSize = this.dockerInfoService.countByParams(params);
            model.addAttribute("downDockerSize", (Object)downDockerSize);
            double dockerOnLinePer = 0.0;
            if (dockerSize > 0) {
                dockerOnLinePer = (1.0 - (double)downDockerSize / (double)dockerSize) * 100.0;
                dockerOnLinePer = FormatUtil.formatDouble(dockerOnLinePer, 0);
            }
            model.addAttribute("dockerOnLinePer", (Object)dockerOnLinePer);
            params.clear();
            HostUtil.addAccountqueryDaping(request, params);
            int portSize = this.portInfoService.countByParams(params);
            model.addAttribute("portSize", (Object)portSize);
            params.put("state", "2");
            int portDownSize = this.portInfoService.countByParams(params);
            model.addAttribute("portDownSize", (Object)portDownSize);
            double portOnLinePer = 0.0;
            if (portSize > 0) {
                portOnLinePer = (1.0 - (double)portDownSize / (double)portSize) * 100.0;
                portOnLinePer = FormatUtil.formatDouble(portOnLinePer, 0);
            }
            model.addAttribute("portOnLinePer", (Object)portOnLinePer);
            params.clear();
            HostUtil.addAccountqueryDaping(request, params);
            int dceSize = this.dceInfoService.countByParams(params);
            model.addAttribute("dceSize", (Object)dceSize);
            params.put("resTimes", -1);
            int dceDownSize = this.dceInfoService.countByParams(params);
            model.addAttribute("dceDownSize", (Object)dceDownSize);
            double dceOnLinePer = 0.0;
            if (dceSize > 0) {
                dceOnLinePer = (1.0 - (double)dceDownSize / (double)dceSize) * 100.0;
                dceOnLinePer = FormatUtil.formatDouble(dceOnLinePer, 0);
            }
            model.addAttribute("dceOnLinePer", (Object)dceOnLinePer);
            params.clear();
            HostUtil.addAccountqueryDaping(request, params);
            int snmpSize = this.snmpInfoService.countByParams(params);
            model.addAttribute("snmpSize", (Object)snmpSize);
            params.put("state", "2");
            int snmpDownSize = this.snmpInfoService.countByParams(params);
            model.addAttribute("snmpDownSize", (Object)snmpDownSize);
            double snmpOnLinePer = 0.0;
            if (snmpSize > 0) {
                snmpOnLinePer = (1.0 - (double)snmpDownSize / (double)snmpSize) * 100.0;
                snmpOnLinePer = FormatUtil.formatDouble(snmpOnLinePer, 0);
            }
            model.addAttribute("snmpOnLinePer", (Object)snmpOnLinePer);
            params.clear();
            HostUtil.addAccountqueryDaping(request, params);
            int snmpDeepSize = this.snmpDeepInfoService.countByParams(params);
            model.addAttribute("snmpDeepSize", (Object)snmpDeepSize);
            params.put("state", "2");
            int snmpDeepDownSize = this.snmpDeepInfoService.countByParams(params);
            model.addAttribute("snmpDeepDownSize", (Object)snmpDeepDownSize);
            model.addAttribute("netTotalSize", (Object)(snmpSize + dceSize + snmpDeepSize));
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
            double customInfoOnLinePer = 0.0;
            if (customInfoSize > 0) {
                customInfoOnLinePer = (1.0 - (double)customInfoDownSize / (double)customInfoSize.intValue()) * 100.0;
                customInfoOnLinePer = FormatUtil.formatDouble(customInfoOnLinePer, 0);
            }
            model.addAttribute("customInfoOnLinePer", (Object)customInfoOnLinePer);
            params.clear();
            HostUtil.addAccountqueryDaping(request, params);
            Integer fileSafeSize = this.fileSafeService.countByParams(params);
            model.addAttribute("fileSafeSize", (Object)(fileSafeSize == null ? 0 : fileSafeSize));
            params.put("state", "2");
            int fileSafeDownSize = this.fileSafeService.countByParams(params);
            model.addAttribute("fileSafeDownSize", (Object)fileSafeDownSize);
            double fileSafeOnLinePer = 0.0;
            if (fileSafeSize > 0) {
                fileSafeOnLinePer = (1.0 - (double)fileSafeDownSize / (double)fileSafeSize.intValue()) * 100.0;
                fileSafeOnLinePer = FormatUtil.formatDouble(fileSafeOnLinePer, 0);
            }
            model.addAttribute("fileSafeOnLinePer", (Object)fileSafeOnLinePer);
            params.clear();
            HostUtil.addAccountqueryDaping(request, params);
            Integer dbTableSize = this.dbTableService.countByParams(params);
            model.addAttribute("dbTableSize", (Object)dbTableSize);
            params.put("state", "2");
            int dbTableDownSize = this.dbTableService.countByParams(params);
            model.addAttribute("dbTableDownSize", (Object)dbTableDownSize);
            double dbTableOnLinePer = 0.0;
            if (dbTableSize > 0) {
                dbTableOnLinePer = (1.0 - (double)dbTableDownSize / (double)dbTableSize.intValue()) * 100.0;
                dbTableOnLinePer = FormatUtil.formatDouble(dbTableOnLinePer, 0);
            }
            model.addAttribute("dbTableOnLinePer", (Object)dbTableOnLinePer);
            params.clear();
            HostUtil.addAccountqueryDaping(request, params);
            int dbInfoSize = this.dbInfoService.countByParams(params);
            model.addAttribute("dbInfoSize", (Object)dbInfoSize);
            params.put("dbState", "2");
            int dbInfoDownSize = this.dbInfoService.countByParams(params);
            model.addAttribute("dbInfoDownSize", (Object)dbInfoDownSize);
            double dbInfoOnLinePer = 0.0;
            if (dbInfoSize > 0) {
                dbInfoOnLinePer = (1.0 - (double)dbInfoDownSize / (double)dbInfoSize) * 100.0;
                dbInfoOnLinePer = FormatUtil.formatDouble(dbInfoOnLinePer, 0);
            }
            model.addAttribute("dbInfoOnLinePer", (Object)dbInfoOnLinePer);
            params.clear();
            HostUtil.addAccountqueryDaping(request, params);
            int heathSize = this.heathMonitorService.countByParams(params);
            model.addAttribute("heathSize", (Object)heathSize);
            params.put("heathStatus", "200");
            int heath200Size = this.heathMonitorService.countByParams(params);
            model.addAttribute("heathDownSize", (Object)(heathSize - heath200Size));
            double heathOnLinePer = 0.0;
            if (heathSize > 0) {
                heathOnLinePer = (double)heath200Size / (double)heathSize * 100.0;
                heathOnLinePer = FormatUtil.formatDouble(heathOnLinePer, 0);
            }
            model.addAttribute("heathOnLinePer", (Object)heathOnLinePer);
            params.clear();
            HostUtil.addAccountqueryDaping(request, params);
            int shellInfoSize = this.shellInfoService.countByParams(params);
            model.addAttribute("shellInfoSize", (Object)shellInfoSize);
            params.clear();
            HostUtil.addAccountqueryDaping(request, params);
            int ftpInfoSize = this.ftpInfoService.countByParams(params);
            model.addAttribute("ftpInfoSize", (Object)ftpInfoSize);
            params.put("state", "2");
            int ftpInfoDownSize = this.ftpInfoService.countByParams(params);
            model.addAttribute("ftpInfoDownSize", (Object)ftpInfoDownSize);
            params.clear();
            HostUtil.addAccountqueryDaping(request, params);
            int taskJobSize = this.taskJobInfoService.countByParams(params);
            model.addAttribute("taskJobSize", (Object)taskJobSize);
            params.clear();
            HostUtil.addAccountqueryDaping(request, params);
            Integer appExceptionSize = this.appExceptionInfoService.countByParams(params);
            model.addAttribute("appExceptionSize", (Object)(appExceptionSize == null ? 0 : appExceptionSize));
            Integer resourceAllSize = totalSystemInfoSize + totalSizeApp + dockerSize + portSize + dceSize + snmpSize + fileWarnSize + dbInfoSize + heathSize + fileSafeSize + dbTableSize + customInfoSize + ftpInfoSize + snmpDeepSize;
            model.addAttribute("resourceAllSize", (Object)resourceAllSize);
        }
        catch (Exception e) {
            logger.error("v9\u5927\u5c4f\u6570\u636e\u67e5\u8be2\u9519\u8bef", (Throwable)e);
        }
    }

    private void setDiskIoInfo(Model model, HttpServletRequest request) {
        try {
            HashMap<String, Object> params = new HashMap<String, Object>();
            HostUtil.addAccountqueryDaping(request, params);
            PageInfo diskIoStateListPage = this.diskIoStateService.selectByParams(params, 1, 100);
            List recordList = diskIoStateListPage.getList();
            model.addAttribute("diskIoStateList", (Object)recordList);
        }
        catch (Exception e) {
            logger.error("v9\u5927\u5c4f\u78c1\u76d8IO\u8bfb\u5199\u901f\u7387\u67e5\u8be2\u9519\u8bef", (Throwable)e);
        }
    }

    private void dbHeathInfoHandle(Model model, HttpServletRequest request) {
        try {
            List<String> nameList = new ArrayList<String>();
            List<Integer> valueList = new ArrayList<Integer>();
            HashMap<String, Object> params = new HashMap<String, Object>();
            HostUtil.addAccountqueryDaping(request, params);
            params.put("orderBy", "CREATE_TIME");
            params.put("orderType", "DESC");
            PageInfo heathMonitorList = this.heathMonitorService.selectByParamsNoContent(params, 1, 20);
            PageInfo dbInfoList = this.dbInfoService.selectByParams(params, 1, 20);
            for (HeathMonitor heathMonitor : (java.util.List<HeathMonitor>) heathMonitorList.getList()) {
                nameList.add(heathMonitor.getAppName());
                valueList.add(heathMonitor.getResTimes());
            }
            for (DbInfo dbInfo : (java.util.List<DbInfo>) dbInfoList.getList()) {
                nameList.add(dbInfo.getAliasName());
                valueList.add(dbInfo.getResTimes());
            }
            if (nameList.size() > 20) {
                nameList = nameList.subList(0, 20);
                valueList = valueList.subList(0, 20);
            }
            model.addAttribute("takeFirst20NameList", (Object)JSONUtil.parseArray(nameList));
            model.addAttribute("takeFirst20ValList", (Object)JSONUtil.parseArray(valueList));
        }
        catch (Exception e) {
            logger.error("v9\u5927\u5c4f\u670d\u52a1\u63a5\u53e3\u548c\u6570\u636e\u5e93\u67e5\u8be2\u9519\u8bef", (Throwable)e);
        }
    }
}

