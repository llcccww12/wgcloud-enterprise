/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.dapingController;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.github.pagehelper.PageInfo;
import com.wgcloud.config.CommonConfig;
import com.wgcloud.entity.AccountInfo;
import com.wgcloud.entity.LogInfo;
import com.wgcloud.entity.SystemInfo;
import com.wgcloud.entity.SystemInfoExt;
import com.wgcloud.service.CpuStateService;
import com.wgcloud.service.DbInfoService;
import com.wgcloud.service.DbTableService;
import com.wgcloud.service.DceInfoService;
import com.wgcloud.service.EquipmentService;
import com.wgcloud.service.HeathMonitorService;
import com.wgcloud.service.HostDiskPerService;
import com.wgcloud.service.LogInfoService;
import com.wgcloud.service.MemStateService;
import com.wgcloud.service.SnmpInfoService;
import com.wgcloud.service.SystemInfoExtService;
import com.wgcloud.service.SystemInfoService;
import com.wgcloud.service.TaskUtilService;
import com.wgcloud.util.DateUtil;
import com.wgcloud.util.FormatUtil;
import com.wgcloud.util.HostUtil;
import com.wgcloud.util.staticvar.StaticKeys;
import java.util.ArrayList;
import java.util.Collections;
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
@RequestMapping(value={"/dapingFour"})
public class DapingFourController {
    private static final Logger logger = LoggerFactory.getLogger(DapingFourController.class);
    @Resource
    private DbTableService dbTableService;
    @Resource
    private DceInfoService dceInfoService;
    @Resource
    private SnmpInfoService snmpInfoService;
    @Resource
    private DbInfoService dbInfoService;
    @Resource
    private SystemInfoService systemInfoService;
    @Resource
    private SystemInfoExtService systemInfoExtService;
    @Autowired
    private CpuStateService cpuStateService;
    @Autowired
    private MemStateService memStateService;
    @Resource
    private LogInfoService logInfoService;
    @Autowired
    private HeathMonitorService heathMonitorService;
    @Resource
    private EquipmentService equipmentService;
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
        if (!StaticKeys.LICENSE_STATE.equals("1")) {
            return "daping/error";
        }
        HashMap<String, Object> params = new HashMap<String, Object>();
        try {
            params.put("countBlockNe", "2");
            HostUtil.addAccountqueryDaping(request, params);
            List<SystemInfo> list = this.systemInfoService.selectAllByParams(params);
            model.addAttribute("hostAllSize", (Object)list.size());
            this.setMiddleData(list, model);
            this.setLogInfo(model);
            this.uptimeTop5(model, request);
            this.setDiskPerTop6(model, request);
            this.setCpuFor10Min(model, request);
            this.setBottomData(model, request);
            HostUtil.addDapingTipMsg(request, model);
            AccountInfo accountInfo = HostUtil.getAccountByRequest(request);
            model.addAttribute("sumDiskSizeCache", this.servletContext.getAttribute("sumDiskSizeCache"));
            if (null != accountInfo && "1".equals(request.getParameter("viewDapingNowAccount")) && "user".equals(accountInfo.getRole())) {
                model.addAttribute("sumDiskSizeCache", (Object)this.taskUtilService.sumDiskSizeCache(request));
            }
            return "daping/indexFour";
        }
        catch (Exception e) {
            logger.error("\u5927\u5c4f\u5c55\u793a\u9519\u8bef", (Throwable)e);
            return "";
        }
    }

    private void setMiddleData(List<SystemInfo> list, Model model) {
        int cpuCoresSum = 0;
        double memSum = 0.0;
        for (SystemInfo systemInfo : list) {
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
        }
        model.addAttribute("cpuCoresSum", (Object)cpuCoresSum);
        model.addAttribute("memSum", (Object)FormatUtil.gToT(memSum + ""));
    }

    private void setLogInfo(Model model) {
        try {
            HashMap<String, Object> params = new HashMap<String, Object>();
            PageInfo logListPage = this.logInfoService.selectByParamsNoContent(params, 1, 100);
            List recordList = logListPage.getList();
            for (LogInfo logInfo : (java.util.List<LogInfo>)recordList) {
                try {
                    if (logInfo.getHostname().length() <= 40) continue;
                    logInfo.setHostname(logInfo.getHostname().substring(0, 40) + "...");
                }
                catch (Exception e) {
                    logger.error("setLogInfo\u9519\u8bef", (Throwable)e);
                }
            }
            model.addAttribute("logList", (Object)recordList);
        }
        catch (Exception e) {
            logger.error("\u7cfb\u7edf\u65e5\u5fd7\u67e5\u8be2\u9519\u8bef", (Throwable)e);
        }
    }

    private void uptimeTop5(Model model, HttpServletRequest request) {
        try {
            HashMap<String, Object> params = new HashMap<String, Object>();
            params.put("orderBy", "UPTIME");
            params.put("orderType", "DESC");
            params.put("countBlockNe", "2");
            HostUtil.addAccountqueryDaping(request, params);
            PageInfo pageInfo = this.systemInfoExtService.selectByParams(params, 1, 5);
            ArrayList<JSONObject> resultList = new ArrayList<JSONObject>();
            ArrayList<String> resultNameList = new ArrayList<String>();
            for (SystemInfoExt systemInfoExt : (java.util.List<SystemInfoExt>) pageInfo.getList()) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.set("name", (Object)systemInfoExt.getHostname());
                resultNameList.add(systemInfoExt.getHostname());
                if (null == systemInfoExt.getUptime()) {
                    jsonObject.set("value", (Object)0);
                } else {
                    String days = FormatUtil.secondsToDays(systemInfoExt.getUptime());
                    if (StringUtils.isEmpty((CharSequence)days)) {
                        days = "0";
                    }
                    jsonObject.set("value", (Object)Integer.valueOf(days.replace("\u5929", "")));
                }
                resultList.add(jsonObject);
            }
            model.addAttribute("hostUptimeList", (Object)JSONUtil.parseArray(resultList).toString());
            model.addAttribute("hostNameUptimeList", (Object)JSONUtil.parseArray(resultNameList).toString());
        }
        catch (Exception e) {
            logger.error("uptimeTop5\u9519\u8bef", (Throwable)e);
        }
    }

    public void setDiskPerTop6(Model model, HttpServletRequest request) {
        try {
            HashMap<String, Object> params = new HashMap<String, Object>();
            params.put("orderBy", "DISK_PER");
            params.put("orderType", "DESC");
            params.put("countBlockNe", "2");
            HostUtil.addAccountqueryDaping(request, params);
            PageInfo pageInfo = this.systemInfoService.selectByParams(params, 1, 6);
            for (int i = 0; i < pageInfo.getList().size(); ++i) {
                if (i == 0) {
                    ((SystemInfo)pageInfo.getList().get(i)).setId("liIn");
                    continue;
                }
                ((SystemInfo)pageInfo.getList().get(i)).setId("liIn liIn" + (i + 1));
            }
            model.addAttribute("hostDiskPerList", (Object)pageInfo.getList());
        }
        catch (Exception e) {
            logger.error("setDiskPerTop5\u9519\u8bef", (Throwable)e);
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
            ArrayList<String> procsTop10ValList = new ArrayList<String>();
            for (SystemInfo systemInfo : (java.util.List<SystemInfo>) pageInfo.getList()) {
                systemTop10NameList.add(systemInfo.getHostname());
                cpuTop10ValList.add(systemInfo.getCpuPer());
                memTop10ValList.add(systemInfo.getMemPer());
                procsTop10ValList.add(systemInfo.getProcs());
            }
            model.addAttribute("resultListCpuTime", (Object)JSONUtil.parseArray(systemTop10NameList).toString());
            model.addAttribute("resultListCpuData", (Object)JSONUtil.parseArray(cpuTop10ValList).toString());
            model.addAttribute("resultListMemData", (Object)JSONUtil.parseArray(memTop10ValList).toString());
            model.addAttribute("resultListProcsData", (Object)JSONUtil.parseArray(procsTop10ValList).toString());
        }
        catch (Exception e) {
            logger.error("setCpuFor10Min\u9519\u8bef", (Throwable)e);
        }
    }

    public void setBottomData(Model model, HttpServletRequest request) {
        try {
            HashMap<String, Object> params = new HashMap<String, Object>();
            HostUtil.addAccountqueryDaping(request, params);
            int heathSize = this.heathMonitorService.countByParams(params);
            model.addAttribute("heathSize", (Object)heathSize);
            int dbInfoSize = this.dbInfoService.countByParams(params);
            model.addAttribute("dbInfoSize", (Object)dbInfoSize);
            Integer dbTableSize = this.dbTableService.countByParams(params);
            model.addAttribute("dbTableSize", (Object)dbTableSize);
            int snmpSize = this.snmpInfoService.countByParams(params);
            model.addAttribute("snmpSize", (Object)snmpSize);
            int dceSize = this.dceInfoService.countByParams(params);
            model.addAttribute("dceSize", (Object)dceSize);
            int equipmentSize = this.equipmentService.countByParams(params);
            model.addAttribute("equipmentSize", (Object)equipmentSize);
        }
        catch (Exception e) {
            logger.error("setBottomData\u9519\u8bef", (Throwable)e);
        }
    }

    private List getLastMin10() {
        ArrayList<String> resultList = new ArrayList<String>();
        String min0 = DateUtil.getCurrentDateTime();
        min0 = min0.substring(0, min0.lastIndexOf(":"));
        resultList.add(min0);
        String min1 = DateUtil.beforeMinutesToNowDate(1);
        min1 = min1.substring(0, min1.lastIndexOf(":"));
        resultList.add(min1);
        String min2 = DateUtil.beforeMinutesToNowDate(2);
        min2 = min2.substring(0, min2.lastIndexOf(":"));
        resultList.add(min2);
        String min3 = DateUtil.beforeMinutesToNowDate(3);
        min3 = min3.substring(0, min3.lastIndexOf(":"));
        resultList.add(min3);
        String min4 = DateUtil.beforeMinutesToNowDate(4);
        min4 = min4.substring(0, min4.lastIndexOf(":"));
        resultList.add(min4);
        String min5 = DateUtil.beforeMinutesToNowDate(5);
        min5 = min5.substring(0, min5.lastIndexOf(":"));
        resultList.add(min5);
        String min6 = DateUtil.beforeMinutesToNowDate(6);
        min6 = min6.substring(0, min6.lastIndexOf(":"));
        resultList.add(min6);
        String min7 = DateUtil.beforeMinutesToNowDate(7);
        min7 = min7.substring(0, min7.lastIndexOf(":"));
        resultList.add(min7);
        String min8 = DateUtil.beforeMinutesToNowDate(8);
        min8 = min8.substring(0, min8.lastIndexOf(":"));
        resultList.add(min8);
        String min9 = DateUtil.beforeMinutesToNowDate(9);
        min9 = min9.substring(0, min9.lastIndexOf(":"));
        resultList.add(min9);
        String min10 = DateUtil.beforeMinutesToNowDate(10);
        min10 = min10.substring(0, min10.lastIndexOf(":"));
        resultList.add(min10);
        Collections.reverse(resultList);
        return resultList;
    }
}

