/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.dapingController;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.github.pagehelper.PageInfo;
import com.wgcloud.dto.ChartInfo;
import com.wgcloud.entity.SystemInfo;
import com.wgcloud.service.HostDiskPerService;
import com.wgcloud.service.LogInfoService;
import com.wgcloud.service.SystemInfoService;
import com.wgcloud.util.DateUtil;
import com.wgcloud.util.FormatUtil;
import com.wgcloud.util.HostUtil;
import com.wgcloud.util.ThreadPoolUtil;
import com.wgcloud.util.license.LicenseUtil;
import com.wgcloud.util.staticvar.StaticKeys;
import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value={"/daping"})
public class DapingController {
    private static final Logger logger = LoggerFactory.getLogger(DapingController.class);
    @Resource
    private SystemInfoService systemInfoService;
    @Resource
    private LogInfoService logInfoService;
    @Resource
    private HostDiskPerService hostDiskPerService;

    private void testThread() {
        Runnable runnable = () -> logger.info("DapingController----------testThread");
        ThreadPoolUtil.executor.execute(runnable);
    }

    @RequestMapping(value={"list"})
    public String list(Model model, HttpServletRequest request) {
        this.addDapingExtListTask(model);
        return "daping/list";
    }

    public void addDapingExtListTask(Model model) {
        try {
            File file = new File(StaticKeys.JAR_PATH + "/config/dapingExtDiy.json");
            if (file.exists()) {
                if (!LicenseUtil.checkEnterpriseVersion()) {
                    logger.info("\u5f53\u524d\u4e0d\u662f\u4f01\u4e1a\u7248\uff0c\u6240\u4ee5\u4e0d\u89e3\u6790\u5927\u5c4f\u6269\u5c55\u9875\u9762\u914d\u7f6e\u6587\u4ef6dapingExtDiy.json");
                    return;
                }
                logger.info("\u53d1\u73b0\u5e76\u89e3\u6790\u5927\u5c4f\u6269\u5c55\u9875\u9762\u914d\u7f6e\u6587\u4ef6dapingExtDiy.json");
                JSONArray hostJsonArray = JSONUtil.readJSONArray((File)file, (Charset)Charset.forName("utf-8"));
                if (!CollectionUtil.isEmpty((Collection)hostJsonArray)) {
                    model.addAttribute("dapingExtDiyList", (Object)hostJsonArray);
                }
            }
        }
        catch (Exception e) {
            logger.error("dapingExtDiy.json\u89e3\u6790\u9519\u8bef", (Throwable)e);
        }
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
            List<SystemInfo> systemInfoList = this.systemInfoService.selectAllByParams(params);
            List<ChartInfo> systemInfoTypeList = HostUtil.getSystemTypeList(systemInfoList);
            ArrayList<String> systemInfoTypeNameList = new ArrayList<String>();
            ArrayList<Integer> systemInfoTypeValList = new ArrayList<Integer>();
            for (ChartInfo chartInfo : systemInfoTypeList) {
                systemInfoTypeNameList.add(chartInfo.getItem());
                systemInfoTypeValList.add(chartInfo.getCount());
            }
            model.addAttribute("systemInfoTypeNameList", (Object)JSONUtil.parseArray(systemInfoTypeNameList));
            model.addAttribute("systemInfoTypeValList", (Object)JSONUtil.parseArray(systemInfoTypeValList));
            int totalSystemInfoSize = this.systemInfoService.countByParams(params);
            model.addAttribute("totalSystemInfoSize", (Object)totalSystemInfoSize);
            this.setSysPie(model, request);
            this.systemTop10(model, request);
            this.setMiddleData(systemInfoList, model, request);
            this.warnInfoInit(model);
            HostUtil.addDapingTipMsg(request, model);
            return "daping/index";
        }
        catch (Exception e) {
            logger.error("\u5927\u5c4f\u5c55\u793a\u9519\u8bef", (Throwable)e);
            return "";
        }
    }

    private void setSysPie(Model model, HttpServletRequest request) {
        try {
            HashMap<String, Object> params = new HashMap<String, Object>();
            params.put("countBlockNe", "2");
            HostUtil.addAccountqueryDaping(request, params);
            int totalSystemInfoSize = this.systemInfoService.countByParams(params);
            params.put("state", "2");
            int hostDownSize = this.systemInfoService.countByParams(params);
            model.addAttribute("hostDownSize", (Object)hostDownSize);
            model.addAttribute("hostOnSize", (Object)(totalSystemInfoSize - hostDownSize));
        }
        catch (Exception e) {
            logger.error("\u8bbe\u7f6e\u4e3b\u673a\u5728\u7ebf/\u79bb\u7ebf\u997c\u56fe\u9519\u8bef", (Throwable)e);
        }
    }

    private void warnInfoInit(Model model) {
        try {
            HashMap<String, Object> params = new HashMap<String, Object>();
            params.put("startTime", DateUtil.getDateBefore(30));
            params.put("endTime", DateUtil.getCurrentDateTime());
            params.put("hostname", "\u4e3b\u673a\u79bb\u7ebf\u544a\u8b66");
            int hostDownWarnSize = this.logInfoService.countByParams(params);
            model.addAttribute("hostDownWarnSize", (Object)hostDownWarnSize);
            params.clear();
            params.put("hostname", "\u5185\u5b58\u544a\u8b66");
            int memWarnSize = this.logInfoService.countByParams(params);
            model.addAttribute("memWarnSize", (Object)memWarnSize);
            params.clear();
            params.put("hostname", "CPU\u544a\u8b66");
            int cpuWarnSize = this.logInfoService.countByParams(params);
            model.addAttribute("cpuWarnSize", (Object)cpuWarnSize);
            params.clear();
            params.put("hostname", "\u4f20\u8f93\u901f\u7387\u544a\u8b66");
            int netWarnSize = this.logInfoService.countByParams(params);
            model.addAttribute("netWarnSize", (Object)netWarnSize);
            params.clear();
            params.put("hostname", "\u78c1\u76d8\u544a\u8b66");
            int diskWarnSize = this.logInfoService.countByParams(params);
            model.addAttribute("diskWarnSize", (Object)diskWarnSize);
            params.clear();
            params.put("hostname", "\u7cfb\u7edf\u8d1f\u8f7d(5\u5206\u949f)\u544a\u8b66");
            int load5WarnSize = this.logInfoService.countByParams(params);
            model.addAttribute("load5WarnSize", (Object)load5WarnSize);
        }
        catch (Exception e) {
            logger.error("\u8bbe\u7f6e\u544a\u8b66\u7edf\u8ba1\u4fe1\u606f\u96f7\u8fbe\u56fe\u9519\u8bef", (Throwable)e);
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
            ArrayList<Double> cpuTop10ValList = new ArrayList<Double>();
            ArrayList<String> rxbytTop10List = new ArrayList<String>();
            ArrayList<String> txbytTop10List = new ArrayList<String>();
            ArrayList<Double> memTop10ValList = new ArrayList<Double>();
            for (SystemInfo systemInfo : (java.util.List<SystemInfo>) pageInfo.getList()) {
                systemTop10NameList.add(systemInfo.getHostname());
                cpuTop10ValList.add(systemInfo.getCpuPer());
                rxbytTop10List.add(systemInfo.getRxbyt());
                txbytTop10List.add(systemInfo.getTxbyt());
                memTop10ValList.add(systemInfo.getMemPer());
            }
            model.addAttribute("systemTop10NameList", (Object)JSONUtil.parseArray(systemTop10NameList));
            model.addAttribute("cpuTop10ValList", (Object)JSONUtil.parseArray(cpuTop10ValList));
            model.addAttribute("rxbytTop10List", (Object)JSONUtil.parseArray(rxbytTop10List));
            model.addAttribute("txbytTop10List", (Object)JSONUtil.parseArray(txbytTop10List));
            model.addAttribute("memTop10ValList", (Object)JSONUtil.parseArray(memTop10ValList));
        }
        catch (Exception e) {
            logger.error("cpu\u4f7f\u7528\u7387\u6700\u65b0\u4e0a\u62a5\u524d10\u9519\u8bef", (Throwable)e);
        }
    }

    private void setMiddleData(List<SystemInfo> systemInfoList, Model model, HttpServletRequest request) {
        try {
            Double maxCpu = 0.0;
            String maxCpuIp = "";
            Double avgCpu = 0.0;
            Double minCpu = 1000.0;
            String minCpuIp = "";
            Double sumCpu = 0.0;
            Double maxMem = 0.0;
            String maxMemIp = "";
            Double minMem = 1000.0;
            String minMemIp = "";
            Double avgMem = 0.0;
            Double sumMem = 0.0;
            Double maxDiskPer = 0.0;
            String maxDiskPerIp = "";
            Double minDiskPer = 1000.0;
            String minDiskPerIp = "";
            Double avgDiskPer = 0.0;
            Double sumDiskPer = 0.0;
            int systemSize = 0;
            int cpuCoresSum = 0;
            double memSum = 0.0;
            for (SystemInfo systemInfo : systemInfoList) {
                ++systemSize;
                if (null != systemInfo.getCpuPer() && systemInfo.getCpuPer() > maxCpu) {
                    maxCpu = systemInfo.getCpuPer();
                    maxCpuIp = systemInfo.getHostname();
                }
                if (null != systemInfo.getCpuPer() && systemInfo.getCpuPer() < minCpu) {
                    minCpu = systemInfo.getCpuPer();
                    minCpuIp = systemInfo.getHostname();
                }
                if (null != systemInfo.getCpuPer()) {
                    sumCpu = sumCpu + systemInfo.getCpuPer();
                }
                if (null != systemInfo.getMemPer() && systemInfo.getMemPer() > maxMem) {
                    maxMem = systemInfo.getMemPer();
                    maxMemIp = systemInfo.getHostname();
                }
                if (null != systemInfo.getMemPer() && systemInfo.getMemPer() < minMem) {
                    minMem = systemInfo.getMemPer();
                    minMemIp = systemInfo.getHostname();
                }
                if (null != systemInfo.getMemPer()) {
                    sumMem = sumMem + systemInfo.getMemPer();
                }
                try {
                    cpuCoresSum += Integer.valueOf(systemInfo.getCpuCoreNum()).intValue();
                }
                catch (Exception e) {
                    logger.error("\u7edf\u8ba1\u6240\u6709\u4e3b\u673a\u603b\u6838\u6570\u9519\u8bef", (Throwable)e);
                }
                try {
                    memSum += FormatUtil.strToDouble(systemInfo.getTotalMem().replace("G", "")).doubleValue();
                }
                catch (Exception e) {
                    logger.error("\u7edf\u8ba1\u6240\u6709\u4e3b\u673a\u603b\u5185\u5b58\u9519\u8bef", (Throwable)e);
                }
                if (null != systemInfo.getDiskPer() && systemInfo.getDiskPer() > maxDiskPer) {
                    maxDiskPer = systemInfo.getDiskPer();
                    maxDiskPerIp = systemInfo.getHostname();
                }
                if (null != systemInfo.getDiskPer() && systemInfo.getDiskPer() < minDiskPer) {
                    minDiskPer = systemInfo.getDiskPer();
                    minDiskPerIp = systemInfo.getHostname();
                }
                if (null == systemInfo.getDiskPer()) continue;
                sumDiskPer = sumDiskPer + systemInfo.getDiskPer();
            }
            if (systemSize > 0) {
                avgCpu = sumCpu / (double)systemInfoList.size();
                avgMem = sumMem / (double)systemInfoList.size();
                avgDiskPer = sumDiskPer / (double)systemInfoList.size();
            } else {
                minCpu = 0.0;
                minMem = 0.0;
                minDiskPer = 0.0;
            }
            model.addAttribute("cpuCoresSum", (Object)cpuCoresSum);
            model.addAttribute("memSum", (Object)FormatUtil.gToT(memSum + ""));
            ArrayList<String> listIp = new ArrayList<String>();
            ArrayList<Double> list = new ArrayList<Double>();
            list.add(maxCpu);
            listIp.add(maxCpuIp);
            list.add(FormatUtil.formatDouble(avgCpu, 2));
            listIp.add("avgCpu");
            list.add(minCpu);
            listIp.add(minCpuIp);
            list.add(maxMem);
            listIp.add(maxMemIp);
            list.add(FormatUtil.formatDouble(avgMem, 2));
            listIp.add("avgCpu");
            list.add(minMem);
            listIp.add(minMemIp);
            list.add(maxDiskPer);
            listIp.add(maxDiskPerIp);
            list.add(FormatUtil.formatDouble(avgDiskPer, 2));
            listIp.add("avgDiskPer");
            list.add(minDiskPer);
            listIp.add(minDiskPerIp);
            model.addAttribute("middleDataList", (Object)JSONUtil.parseArray(list));
            model.addAttribute("middleDataListIp", (Object)JSONUtil.parseArray(listIp));
        }
        catch (Exception e) {
            logger.error("v1\u7248\u5927\u5c4f\u8bbe\u7f6e\u6e29\u5ea6\u8ba1\u56fe\u8868\u9519\u8bef", (Throwable)e);
        }
    }
}

