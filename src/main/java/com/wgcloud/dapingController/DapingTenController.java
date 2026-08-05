/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.dapingController;

import cn.hutool.json.JSONUtil;
import com.github.pagehelper.PageInfo;
import com.wgcloud.config.CommonConfig;
import com.wgcloud.dto.ChartInfo;
import com.wgcloud.entity.AccountInfo;
import com.wgcloud.entity.SystemInfo;
import com.wgcloud.service.AccountInfoService;
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
import com.wgcloud.service.LogInfoService;
import com.wgcloud.service.PortInfoService;
import com.wgcloud.service.ShellInfoService;
import com.wgcloud.service.SnmpDeepInfoService;
import com.wgcloud.service.SnmpInfoService;
import com.wgcloud.service.SystemInfoService;
import com.wgcloud.service.TaskUtilService;
import com.wgcloud.util.FormatUtil;
import com.wgcloud.util.HostUtil;
import com.wgcloud.util.license.LicenseUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
@RequestMapping(value={"/dapingTen"})
public class DapingTenController {
    private static final Logger logger = LoggerFactory.getLogger(DapingTenController.class);
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
    private SnmpDeepInfoService snmpDeepInfoService;
    @Resource
    private FtpInfoService ftpInfoService;
    @Resource
    private HostDiskPerService hostDiskPerService;
    @Autowired
    private CommonConfig commonConfig;
    @Autowired
    private ServletContext servletContext;
    @Autowired
    private TaskUtilService taskUtilService;

    private void testThread() {
        new Thread(() -> logger.info("\u542f\u52a8\u5b50\u7ebf\u7a0b\u6d4b\u8bd5")).start();
    }

    @RequestMapping(value={"index"})
    public String index(Model model, HttpServletRequest request) {
        if (!LicenseUtil.checkEnterpriseVersion()) {
            return "daping/error";
        }
        try {
            HashMap<String, Object> params = new HashMap<String, Object>();
            params.clear();
            HostUtil.addAccountqueryDaping(request, params);
            int dceSize = this.dceInfoService.countByParams(params);
            params.put("resTimes", -1);
            int dceDownSize = this.dceInfoService.countByParams(params);
            params.clear();
            HostUtil.addAccountqueryDaping(request, params);
            int snmpSize = this.snmpInfoService.countByParams(params);
            params.put("state", "2");
            int snmpDownSize = this.snmpInfoService.countByParams(params);
            params.clear();
            HostUtil.addAccountqueryDaping(request, params);
            int snmpDeepSize = this.snmpDeepInfoService.countByParams(params);
            params.put("state", "2");
            int snmpDeepDownSize = this.snmpDeepInfoService.countByParams(params);
            model.addAttribute("dceSnmpSize", (Object)(dceSize + snmpSize + snmpDeepSize));
            model.addAttribute("dceSnmpDownSize", (Object)(dceDownSize + snmpDownSize + snmpDeepDownSize));
            HashMap<String, Object> paramsHost = new HashMap<String, Object>();
            paramsHost.put("countBlockNe", "2");
            HostUtil.addAccountqueryDaping(request, paramsHost);
            List<SystemInfo> hostAllList = this.systemInfoService.selectAllByParams(paramsHost);
            model.addAttribute("systemInfoList", hostAllList);
            this.resourcePieData(model, request);
            List<ChartInfo> systemInfoTypeList = HostUtil.getSystemTypeList(hostAllList);
            ArrayList<String> systemInfoTypeNameList = new ArrayList<String>();
            ArrayList<Integer> systemInfoTypeValList = new ArrayList<Integer>();
            for (ChartInfo chartInfo : systemInfoTypeList) {
                systemInfoTypeNameList.add(chartInfo.getItem());
                systemInfoTypeValList.add(chartInfo.getCount());
            }
            model.addAttribute("systemInfoTypeNameList", (Object)JSONUtil.parseArray(systemInfoTypeNameList));
            model.addAttribute("systemInfoTypeValList", (Object)JSONUtil.parseArray(systemInfoTypeValList));
            this.setMiddleData(hostAllList, model, request);
            AccountInfo accountInfo = HostUtil.getAccountByRequest(request);
            model.addAttribute("sumDiskSizeCache", this.servletContext.getAttribute("sumDiskSizeCache"));
            if (null != accountInfo && "1".equals(request.getParameter("viewDapingNowAccount")) && "user".equals(accountInfo.getRole())) {
                model.addAttribute("sumDiskSizeCache", (Object)this.taskUtilService.sumDiskSizeCache(request));
            }
            HostUtil.addDapingTipMsg(request, model);
            return "daping/indexTen";
        }
        catch (Exception e) {
            logger.error("\u5927\u5c4f\u5c55\u793a\u9519\u8bef", (Throwable)e);
            return "";
        }
    }

    private void resourcePieData(Model model, HttpServletRequest request) {
        try {
            HashMap<String, Object> params = new HashMap<String, Object>();
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
            params.put("state", "2");
            int dbTableDownSize = this.dbTableService.countByParams(params);
            model.addAttribute("dbTableDownSize", (Object)dbTableDownSize);
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
            model.addAttribute("heathDownSize", (Object)(heathSize - heath200Size));
            params.clear();
            HostUtil.addAccountqueryDaping(request, params);
            int shellInfoSize = this.shellInfoService.countByParams(params);
            model.addAttribute("shellInfoSize", (Object)shellInfoSize);
            this.systemTop10(model, request);
        }
        catch (Exception e) {
            logger.error("v10\u5927\u5c4f\u6570\u636e\u67e5\u8be2\u9519\u8bef", (Throwable)e);
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
                if (null != systemInfo.getDiskPer()) {
                    sumDiskPer = sumDiskPer + systemInfo.getDiskPer();
                }
                if (StringUtils.isEmpty((CharSequence)systemInfo.getRemark())) continue;
                systemInfo.setHostname(systemInfo.getHostname() + " (" + systemInfo.getRemark() + ")");
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
            logger.error("\u7ec4\u88c5\u6240\u6709\u4e3b\u673a\u6700\u8fd110\u5206\u949f\u72b6\u6001\u6700\u5927\u5e73\u5747\u6700\u4f4e\u503c\u9519\u8bef", (Throwable)e);
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
            ArrayList<Double> cpuTop10List = new ArrayList<Double>();
            ArrayList<Double> load5mTop10List = new ArrayList<Double>();
            ArrayList<Double> load15mTop10List = new ArrayList<Double>();
            ArrayList<Double> diskPerList = new ArrayList<Double>();
            ArrayList<String> procsTop10ValList = new ArrayList<String>();
            for (SystemInfo systemInfo : (java.util.List<SystemInfo>) pageInfo.getList()) {
                systemTop10NameList.add(systemInfo.getHostname());
                diskPerList.add(systemInfo.getDiskPer());
                cpuTop10List.add(systemInfo.getCpuPer());
                load5mTop10List.add(systemInfo.getFiveLoad());
                load15mTop10List.add(systemInfo.getFifteenLoad());
                procsTop10ValList.add(systemInfo.getProcs());
            }
            model.addAttribute("systemTop10NameList", (Object)JSONUtil.parseArray(systemTop10NameList));
            model.addAttribute("diskPerTop10List", (Object)JSONUtil.parseArray(diskPerList));
            model.addAttribute("cpuTop10List", (Object)JSONUtil.parseArray(cpuTop10List));
            model.addAttribute("load5mTop10List", (Object)JSONUtil.parseArray(load5mTop10List));
            model.addAttribute("load15mTop10List", (Object)JSONUtil.parseArray(load15mTop10List));
            model.addAttribute("procsTop10ValList", (Object)JSONUtil.parseArray(procsTop10ValList));
        }
        catch (Exception e) {
            logger.error("\u67e5\u8be2\u6700\u65b0\u4e0a\u62a5\u524d10\u4e3b\u673a\u9519\u8bef", (Throwable)e);
        }
    }
}

