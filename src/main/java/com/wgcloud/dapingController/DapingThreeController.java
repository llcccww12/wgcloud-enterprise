/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.dapingController;

import com.wgcloud.config.CommonConfig;
import com.wgcloud.dto.ChartDapingNewInfo;
import com.wgcloud.entity.AppExceptionInfo;
import com.wgcloud.entity.AppInfo;
import com.wgcloud.entity.CustomInfo;
import com.wgcloud.entity.DbInfo;
import com.wgcloud.entity.DbTable;
import com.wgcloud.entity.DceInfo;
import com.wgcloud.entity.DockerInfo;
import com.wgcloud.entity.FileSafe;
import com.wgcloud.entity.FtpInfo;
import com.wgcloud.entity.HeathMonitor;
import com.wgcloud.entity.PortInfo;
import com.wgcloud.entity.SnmpDeepInfo;
import com.wgcloud.entity.SnmpInfo;
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
import com.wgcloud.service.LogInfoService;
import com.wgcloud.service.PortInfoService;
import com.wgcloud.service.ShellInfoService;
import com.wgcloud.service.SnmpDeepInfoService;
import com.wgcloud.service.SnmpInfoService;
import com.wgcloud.service.SystemInfoService;
import com.wgcloud.util.DateUtil;
import com.wgcloud.util.FormatUtil;
import com.wgcloud.util.HostUtil;
import com.wgcloud.util.staticvar.StaticKeys;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value={"/dapingThree"})
public class DapingThreeController {
    private static final Logger logger = LoggerFactory.getLogger(DapingThreeController.class);
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
    private SnmpDeepInfoService snmpDeepInfoService;
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
    @Autowired
    private CommonConfig commonConfig;

    private void testThread() {
        new Thread(() -> logger.info("\u542f\u52a8\u5b50\u7ebf\u7a0b\u6d4b\u8bd5")).start();
    }

    /*
     * WARNING - void declaration
     */
    @RequestMapping(value={"index"})
    public String index(Model model, HttpServletRequest request) {
        if (!StaticKeys.LICENSE_STATE.equals("1")) {
            return "daping/error";
        }
        HashMap<String, Object> params = new HashMap<String, Object>();
        try {
            String requestParamState = request.getParameter("state");
            Integer onLineNum = 0;
            Integer downLineNum = 0;
            params.put("countBlockNe", "2");
            HostUtil.addAccountqueryDaping(request, params);
            List<SystemInfo> systemInfoList = this.systemInfoService.selectAllByParams(params);
            onLineNum = this.hostAddVal(systemInfoList, model, request);
            downLineNum = systemInfoList.size() - onLineNum;
            ArrayList<SystemInfo> systemInfoResultList = new ArrayList<SystemInfo>();
            for (SystemInfo systemInfo : systemInfoList) {
                if (StringUtils.isEmpty((CharSequence)requestParamState)) {
                    systemInfoResultList.add(systemInfo);
                    continue;
                }
                if (!requestParamState.equals(systemInfo.getState())) continue;
                systemInfoResultList.add(systemInfo);
            }
            model.addAttribute("systemInfoList", systemInfoResultList);
            ArrayList<ChartDapingNewInfo> showList = new ArrayList<ChartDapingNewInfo>();
            List<AppInfo> appInfoList = this.appInfoService.selectAllByParams(params);
            for (AppInfo appInfo : appInfoList) {
                ChartDapingNewInfo chartDapingNewInfo = new ChartDapingNewInfo();
                chartDapingNewInfo.setName("[\u8fdb\u7a0b] " + appInfo.getAppName());
                chartDapingNewInfo.setState(appInfo.getState());
                if ("2".equals(chartDapingNewInfo.getState())) {
                    downLineNum = downLineNum + 1;
                } else {
                    onLineNum = onLineNum + 1;
                }
                if (StringUtils.isEmpty((CharSequence)requestParamState)) {
                    showList.add(chartDapingNewInfo);
                    continue;
                }
                if (!requestParamState.equals(chartDapingNewInfo.getState())) continue;
                showList.add(chartDapingNewInfo);
            }
            List<PortInfo> portInfoList = this.portInfoService.selectAllByParams(params);
            for (PortInfo portInfo : portInfoList) {
                ChartDapingNewInfo chartDapingNewInfo = new ChartDapingNewInfo();
                chartDapingNewInfo.setName("[\u7aef\u53e3] " + portInfo.getPortName());
                chartDapingNewInfo.setState(portInfo.getState());
                if ("2".equals(chartDapingNewInfo.getState())) {
                    downLineNum = downLineNum + 1;
                } else {
                    onLineNum = onLineNum + 1;
                }
                if (StringUtils.isEmpty((CharSequence)requestParamState)) {
                    showList.add(chartDapingNewInfo);
                    continue;
                }
                if (!requestParamState.equals(chartDapingNewInfo.getState())) continue;
                showList.add(chartDapingNewInfo);
            }
            List<DockerInfo> list = this.dockerInfoService.selectAllByParams(params);
            for (DockerInfo dockerInfo : list) {
                ChartDapingNewInfo chartDapingNewInfo = new ChartDapingNewInfo();
                chartDapingNewInfo.setName("[DOCKER] " + dockerInfo.getDockerName());
                chartDapingNewInfo.setState(dockerInfo.getState());
                if ("2".equals(chartDapingNewInfo.getState())) {
                    downLineNum = downLineNum + 1;
                } else {
                    onLineNum = onLineNum + 1;
                }
                if (StringUtils.isEmpty((CharSequence)requestParamState)) {
                    showList.add(chartDapingNewInfo);
                    continue;
                }
                if (!requestParamState.equals(chartDapingNewInfo.getState())) continue;
                showList.add(chartDapingNewInfo);
            }
            List<FileSafe> list2 = this.fileSafeService.selectAllByParams(params);
            for (FileSafe fileSafe : list2) {
                ChartDapingNewInfo chartDapingNewInfo = new ChartDapingNewInfo();
                chartDapingNewInfo.setName("[\u9632\u7be1\u6539] " + fileSafe.getFileName());
                chartDapingNewInfo.setState(fileSafe.getState());
                if ("2".equals(chartDapingNewInfo.getState())) {
                    downLineNum = downLineNum + 1;
                } else {
                    onLineNum = onLineNum + 1;
                }
                if (StringUtils.isEmpty((CharSequence)requestParamState)) {
                    showList.add(chartDapingNewInfo);
                    continue;
                }
                if (!requestParamState.equals(chartDapingNewInfo.getState())) continue;
                showList.add(chartDapingNewInfo);
            }
            List<CustomInfo> list3 = this.customInfoService.selectAllByParams(params);
            for (CustomInfo customInfo : list3) {
                ChartDapingNewInfo chartDapingNewInfo = new ChartDapingNewInfo();
                chartDapingNewInfo.setName("[\u76d1\u63a7\u9879] " + customInfo.getCustomName());
                chartDapingNewInfo.setState(customInfo.getState());
                if ("2".equals(chartDapingNewInfo.getState())) {
                    downLineNum = downLineNum + 1;
                } else {
                    onLineNum = onLineNum + 1;
                }
                if (StringUtils.isEmpty((CharSequence)requestParamState)) {
                    showList.add(chartDapingNewInfo);
                    continue;
                }
                if (!requestParamState.equals(chartDapingNewInfo.getState())) continue;
                showList.add(chartDapingNewInfo);
            }
            List<AppExceptionInfo> list4 = this.appExceptionInfoService.selectAllByParams(params);
            for (AppExceptionInfo appExceptionInfo : list4) {
                ChartDapingNewInfo chartDapingNewInfo = new ChartDapingNewInfo();
                chartDapingNewInfo.setName("[\u5f02\u5e38\u8fdb\u7a0b] " + appExceptionInfo.getAppName());
                chartDapingNewInfo.setState(appExceptionInfo.getState());
                if (StringUtils.isEmpty((CharSequence)requestParamState)) {
                    showList.add(chartDapingNewInfo);
                    continue;
                }
                if (!requestParamState.equals(chartDapingNewInfo.getState())) continue;
                showList.add(chartDapingNewInfo);
            }
            List<HeathMonitor> list5 = this.heathMonitorService.selectAllByParams(params);
            for (HeathMonitor heathMonitor : list5) {
                ChartDapingNewInfo chartDapingNewInfo = new ChartDapingNewInfo();
                chartDapingNewInfo.setName("[\u63a5\u53e3] " + heathMonitor.getAppName());
                if ("200".equals(heathMonitor.getHeathStatus())) {
                    chartDapingNewInfo.setState("1");
                } else {
                    chartDapingNewInfo.setState("2");
                }
                if ("2".equals(chartDapingNewInfo.getState())) {
                    downLineNum = downLineNum + 1;
                } else {
                    onLineNum = onLineNum + 1;
                }
                if (StringUtils.isEmpty((CharSequence)requestParamState)) {
                    showList.add(chartDapingNewInfo);
                    continue;
                }
                if (!requestParamState.equals(chartDapingNewInfo.getState())) continue;
                showList.add(chartDapingNewInfo);
            }
            List<DbInfo> list6 = this.dbInfoService.selectAllByParams(params);
            for (DbInfo dbInfo : list6) {
                ChartDapingNewInfo chartDapingNewInfo = new ChartDapingNewInfo();
                chartDapingNewInfo.setName("[\u6570\u636e\u5e93] " + dbInfo.getAliasName());
                chartDapingNewInfo.setState(dbInfo.getDbState());
                if ("2".equals(chartDapingNewInfo.getState())) {
                    downLineNum = downLineNum + 1;
                } else {
                    onLineNum = onLineNum + 1;
                }
                if (StringUtils.isEmpty((CharSequence)requestParamState)) {
                    showList.add(chartDapingNewInfo);
                    continue;
                }
                if (!requestParamState.equals(chartDapingNewInfo.getState())) continue;
                showList.add(chartDapingNewInfo);
            }
            List<DbTable> list7 = this.dbTableService.selectAllByParams(params);
            for (DbTable dbTable : list7) {
                ChartDapingNewInfo chartDapingNewInfo = new ChartDapingNewInfo();
                chartDapingNewInfo.setName("[\u6570\u636e\u8868] " + dbTable.getRemark());
                chartDapingNewInfo.setState(dbTable.getState());
                if ("2".equals(chartDapingNewInfo.getState())) {
                    downLineNum = downLineNum + 1;
                } else {
                    onLineNum = onLineNum + 1;
                }
                if (StringUtils.isEmpty((CharSequence)requestParamState)) {
                    showList.add(chartDapingNewInfo);
                    continue;
                }
                if (!requestParamState.equals(chartDapingNewInfo.getState())) continue;
                showList.add(chartDapingNewInfo);
            }
            List<DceInfo> list8 = this.dceInfoService.selectAllByParams(params);
            for (DceInfo dceInfo : list8) {
                ChartDapingNewInfo chartDapingNewInfo = new ChartDapingNewInfo();
                if (StringUtils.isEmpty((CharSequence)dceInfo.getRemark())) {
                    chartDapingNewInfo.setName("[PING] " + dceInfo.getHostname());
                } else {
                    chartDapingNewInfo.setName("[PING] " + dceInfo.getRemark());
                }
                if (-1 == dceInfo.getResTimes()) {
                    chartDapingNewInfo.setState("2");
                } else {
                    chartDapingNewInfo.setState("1");
                }
                if ("2".equals(chartDapingNewInfo.getState())) {
                    downLineNum = downLineNum + 1;
                } else {
                    onLineNum = onLineNum + 1;
                }
                if (StringUtils.isEmpty((CharSequence)requestParamState)) {
                    showList.add(chartDapingNewInfo);
                    continue;
                }
                if (!requestParamState.equals(chartDapingNewInfo.getState())) continue;
                showList.add(chartDapingNewInfo);
            }
            List<SnmpInfo> list9 = this.snmpInfoService.selectAllByParams(params);
            for (SnmpInfo snmpInfo : list9) {
                ChartDapingNewInfo chartDapingNewInfo = new ChartDapingNewInfo();
                if (StringUtils.isEmpty((CharSequence)snmpInfo.getRemark())) {
                    chartDapingNewInfo.setName("[SNMP] " + snmpInfo.getHostname());
                } else {
                    chartDapingNewInfo.setName("[SNMP] " + snmpInfo.getRemark());
                }
                chartDapingNewInfo.setState(snmpInfo.getState());
                if ("2".equals(chartDapingNewInfo.getState())) {
                    downLineNum = downLineNum + 1;
                } else {
                    onLineNum = onLineNum + 1;
                }
                if (StringUtils.isEmpty((CharSequence)requestParamState)) {
                    showList.add(chartDapingNewInfo);
                    continue;
                }
                if (!requestParamState.equals(chartDapingNewInfo.getState())) continue;
                showList.add(chartDapingNewInfo);
            }
            List<FtpInfo> list10 = this.ftpInfoService.selectAllByParams(params);
            for (FtpInfo ftpInfo : list10) {
                ChartDapingNewInfo chartDapingNewInfo = new ChartDapingNewInfo();
                if (StringUtils.isEmpty((CharSequence)ftpInfo.getFtpName())) {
                    chartDapingNewInfo.setName("[FTP/SFTP] " + ftpInfo.getFtpHost());
                } else {
                    chartDapingNewInfo.setName("[FTP/SFTP] " + ftpInfo.getFtpName());
                }
                chartDapingNewInfo.setState(ftpInfo.getState());
                if ("2".equals(chartDapingNewInfo.getState())) {
                    downLineNum = downLineNum + 1;
                } else {
                    onLineNum = onLineNum + 1;
                }
                if (StringUtils.isEmpty((CharSequence)requestParamState)) {
                    showList.add(chartDapingNewInfo);
                    continue;
                }
                if (!requestParamState.equals(chartDapingNewInfo.getState())) continue;
                showList.add(chartDapingNewInfo);
            }
            List<SnmpDeepInfo> list11 = this.snmpDeepInfoService.selectAllByParams(params);
            for (SnmpDeepInfo snmpDeepInfo : list11) {
                ChartDapingNewInfo chartDapingNewInfo = new ChartDapingNewInfo();
                if (StringUtils.isEmpty((CharSequence)snmpDeepInfo.getRemark())) {
                    chartDapingNewInfo.setName("[SNMP\u6df1\u5ea6] " + snmpDeepInfo.getHostname());
                } else {
                    chartDapingNewInfo.setName("[SNMP\u6df1\u5ea6] " + snmpDeepInfo.getRemark());
                }
                chartDapingNewInfo.setState(snmpDeepInfo.getState());
                if ("2".equals(chartDapingNewInfo.getState())) {
                    downLineNum = downLineNum + 1;
                } else {
                    onLineNum = onLineNum + 1;
                }
                if (StringUtils.isEmpty((CharSequence)requestParamState)) {
                    showList.add(chartDapingNewInfo);
                    continue;
                }
                if (!requestParamState.equals(chartDapingNewInfo.getState())) continue;
                showList.add(chartDapingNewInfo);
            }
            ArrayList arrayList = new ArrayList();
            List rightList = new ArrayList();
            if (showList.size() > 1) {
                int size = showList.size();
                int index = size / 2;
                arrayList = new ArrayList(showList.subList(0, index));
                rightList = showList.subList(index, showList.size());
            } else {
                arrayList = new ArrayList(showList);
            }
            model.addAttribute("leftList", (Object)arrayList);
            model.addAttribute("rightList", rightList);
            params.clear();
            HostUtil.addAccountqueryDaping(request, params);
            Integer fileWarnSize = this.fileWarnInfoService.countByParams(params);
            onLineNum = onLineNum + fileWarnSize;
            String dateTime = DateUtil.getCurrentDateTime();
            model.addAttribute("allResourceNum", (Object)(onLineNum + downLineNum));
            model.addAttribute("onLineNum", (Object)onLineNum);
            model.addAttribute("downLineNum", (Object)downLineNum);
            HostUtil.addDapingTipMsg(request, model);
            return "daping/indexThree";
        }
        catch (Exception e) {
            logger.error("\u5927\u5c4fv3\u5c55\u793a\u9519\u8bef", (Throwable)e);
            return "";
        }
    }

    private Integer hostAddVal(List<SystemInfo> systemInfoList, Model model, HttpServletRequest request) throws Exception {
        Integer onLineHostNum = 0;
        if ("true".equals(this.commonConfig.getHostGroup())) {
            this.systemInfoService.setGroupInList(systemInfoList, model, request);
        }
        for (SystemInfo systemInfo1 : systemInfoList) {
            systemInfo1.setRxbyt(FormatUtil.kbToM(systemInfo1.getRxbyt()) + "/s");
            systemInfo1.setTxbyt(FormatUtil.kbToM(systemInfo1.getTxbyt()) + "/s");
            if (!StringUtils.isEmpty((CharSequence)systemInfo1.getRemark())) {
                systemInfo1.setHostname(systemInfo1.getHostname() + " (" + systemInfo1.getRemark() + ")");
            }
            if (!"1".equals(systemInfo1.getState())) continue;
            onLineHostNum = onLineHostNum + 1;
        }
        return onLineHostNum;
    }
}

