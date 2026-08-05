/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.dapingController;

import cn.hutool.json.JSONUtil;
import com.github.pagehelper.PageInfo;
import com.wgcloud.config.CommonConfig;
import com.wgcloud.dto.ChartDapingNewInfo;
import com.wgcloud.entity.SystemInfo;
import com.wgcloud.service.AppExceptionInfoService;
import com.wgcloud.service.AppInfoService;
import com.wgcloud.service.CustomInfoService;
import com.wgcloud.service.DbInfoService;
import com.wgcloud.service.DbTableService;
import com.wgcloud.service.DceInfoService;
import com.wgcloud.service.DockerInfoService;
import com.wgcloud.service.EquipmentService;
import com.wgcloud.service.FileSafeService;
import com.wgcloud.service.FileWarnInfoService;
import com.wgcloud.service.FtpInfoService;
import com.wgcloud.service.HeathMonitorService;
import com.wgcloud.service.K8sMonitorService;
import com.wgcloud.service.KafkaMonitorService;
import com.wgcloud.service.LogInfoService;
import com.wgcloud.service.PasswdInfoService;
import com.wgcloud.service.PortInfoService;
import com.wgcloud.service.RedisMonitorService;
import com.wgcloud.service.ShellInfoService;
import com.wgcloud.service.SnmpInfoService;
import com.wgcloud.service.SystemInfoService;
import com.wgcloud.util.HostUtil;
import com.wgcloud.util.ThreadPoolUtil;
import com.wgcloud.util.staticvar.StaticKeys;
import java.util.ArrayList;
import java.util.HashMap;
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
@RequestMapping(value={"/dapingFive"})
public class DapingFiveController {
    private static final Logger logger = LoggerFactory.getLogger(DapingFiveController.class);
    @Resource
    private SystemInfoService systemInfoService;
    @Resource
    private LogInfoService logInfoService;
    @Autowired
    private HeathMonitorService heathMonitorService;
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
    private AppInfoService appInfoService;
    @Resource
    private FileWarnInfoService fileWarnInfoService;
    @Resource
    private CustomInfoService customInfoService;
    @Resource
    private DockerInfoService dockerInfoService;
    @Resource
    private FtpInfoService ftpInfoService;
    @Resource
    private EquipmentService equipmentService;
    @Resource
    private PasswdInfoService passwdInfoService;
    @Autowired
    private ShellInfoService shellInfoService;
    @Resource
    private AppExceptionInfoService appExceptionInfoService;
    @Resource
    private K8sMonitorService k8sMonitorService;
    @Resource
    private KafkaMonitorService kafkaMonitorService;
    @Resource
    private RedisMonitorService redisMonitorService;
    @Autowired
    private CommonConfig commonConfig;

    private void testThread() {
        Runnable runnable = () -> logger.info("DapingController----------testThread");
        ThreadPoolUtil.executor.execute(runnable);
    }

    @RequestMapping(value={"index"})
    public String index(Model model, HttpServletRequest request) {
        if (!StaticKeys.LICENSE_STATE.equals("1")) {
            return "daping/error";
        }
        this.allDataChartHandle(model, request);
        this.hostDaysHandle(model, request);
        HostUtil.addDapingTipMsg(request, model);
        return "daping/indexFive";
    }

    public void allDataChartHandle(Model model, HttpServletRequest request) {
        ArrayList<ChartDapingNewInfo> list = new ArrayList<ChartDapingNewInfo>();
        HashMap<String, Object> params = new HashMap<String, Object>();
        String zongshuliangStr = "\u603b\u6570\u91cf";
        String xiaxianStr = "\u5df2\u79bb\u7ebf";
        try {
            params.put("countBlockNe", "2");
            HostUtil.addAccountqueryDaping(request, params);
            int totalSystemInfoSize = this.systemInfoService.countByParams(params);
            ChartDapingNewInfo dto1 = new ChartDapingNewInfo();
            dto1.setName("\u76d1\u63a7\u4e3b\u673a");
            dto1.setState(zongshuliangStr);
            dto1.setValue(totalSystemInfoSize);
            list.add(dto1);
            params.put("state", "2");
            int hostDownSize = this.systemInfoService.countByParams(params);
            ChartDapingNewInfo dto2 = new ChartDapingNewInfo();
            dto2.setName("\u76d1\u63a7\u4e3b\u673a");
            dto2.setState(xiaxianStr);
            dto2.setValue(hostDownSize);
            list.add(dto2);
            params.clear();
            HostUtil.addAccountqueryDaping(request, params);
            int totalSizeApp = this.appInfoService.countByParams(params);
            ChartDapingNewInfo dto3 = new ChartDapingNewInfo();
            dto3.setName("\u76d1\u63a7\u8fdb\u7a0b");
            dto3.setState(zongshuliangStr);
            dto3.setValue(totalSizeApp);
            list.add(dto3);
            params.put("state", "2");
            int downAppSize = this.appInfoService.countByParams(params);
            ChartDapingNewInfo dto4 = new ChartDapingNewInfo();
            dto4.setName("\u76d1\u63a7\u8fdb\u7a0b");
            dto4.setState(xiaxianStr);
            dto4.setValue(downAppSize);
            list.add(dto4);
            params.clear();
            HostUtil.addAccountqueryDaping(request, params);
            int dockerSize = this.dockerInfoService.countByParams(params);
            ChartDapingNewInfo dto5 = new ChartDapingNewInfo();
            dto5.setName("\u76d1\u63a7Docker");
            dto5.setState(zongshuliangStr);
            dto5.setValue(dockerSize);
            list.add(dto5);
            params.put("state", "2");
            int downDockerSize = this.dockerInfoService.countByParams(params);
            ChartDapingNewInfo dto6 = new ChartDapingNewInfo();
            dto6.setName("\u76d1\u63a7Docker");
            dto6.setState(xiaxianStr);
            dto6.setValue(downDockerSize);
            list.add(dto6);
            params.clear();
            HostUtil.addAccountqueryDaping(request, params);
            int portSize = this.portInfoService.countByParams(params);
            ChartDapingNewInfo dto7 = new ChartDapingNewInfo();
            dto7.setName("\u76d1\u63a7\u7aef\u53e3");
            dto7.setState(zongshuliangStr);
            dto7.setValue(portSize);
            list.add(dto7);
            params.put("state", "2");
            int portDownSize = this.portInfoService.countByParams(params);
            ChartDapingNewInfo dto8 = new ChartDapingNewInfo();
            dto8.setName("\u76d1\u63a7\u7aef\u53e3");
            dto8.setState(xiaxianStr);
            dto8.setValue(portDownSize);
            list.add(dto8);
            params.clear();
            HostUtil.addAccountqueryDaping(request, params);
            Integer fileWarnSize = this.fileWarnInfoService.countByParams(params);
            ChartDapingNewInfo dto9 = new ChartDapingNewInfo();
            dto9.setName("\u65e5\u5fd7\u76d1\u63a7");
            dto9.setState(zongshuliangStr);
            dto9.setValue(fileWarnSize);
            list.add(dto9);
            params.clear();
            HostUtil.addAccountqueryDaping(request, params);
            Integer customInfoSize = this.customInfoService.countByParams(params);
            ChartDapingNewInfo dto10 = new ChartDapingNewInfo();
            dto10.setName("\u81ea\u5b9a\u4e49\u76d1\u63a7\u9879");
            dto10.setState(zongshuliangStr);
            dto10.setValue(customInfoSize);
            list.add(dto10);
            params.put("state", "2");
            int customInfoDownSize = this.customInfoService.countByParams(params);
            ChartDapingNewInfo dto11 = new ChartDapingNewInfo();
            dto11.setName("\u81ea\u5b9a\u4e49\u76d1\u63a7\u9879");
            dto11.setState(xiaxianStr);
            dto11.setValue(customInfoDownSize);
            list.add(dto11);
            params.clear();
            HostUtil.addAccountqueryDaping(request, params);
            Integer fileSafeSize = this.fileSafeService.countByParams(params);
            ChartDapingNewInfo dto12 = new ChartDapingNewInfo();
            dto12.setName("\u6587\u4ef6\u9632\u7be1\u6539");
            dto12.setState(zongshuliangStr);
            dto12.setValue(fileSafeSize);
            list.add(dto12);
            params.put("state", "2");
            int fileSafeDownSize = this.fileSafeService.countByParams(params);
            ChartDapingNewInfo dto13 = new ChartDapingNewInfo();
            dto13.setName("\u6587\u4ef6\u9632\u7be1\u6539");
            dto13.setState(xiaxianStr);
            dto13.setValue(fileSafeDownSize);
            list.add(dto13);
            params.clear();
            HostUtil.addAccountqueryDaping(request, params);
            int dceSize = this.dceInfoService.countByParams(params);
            ChartDapingNewInfo dto14 = new ChartDapingNewInfo();
            dto14.setName("\u7f51\u7edc\u8bbe\u5907ping");
            dto14.setState(zongshuliangStr);
            dto14.setValue(dceSize);
            list.add(dto14);
            params.put("resTimes", -1);
            int dceDownSize = this.dceInfoService.countByParams(params);
            ChartDapingNewInfo dto15 = new ChartDapingNewInfo();
            dto15.setName("\u7f51\u7edc\u8bbe\u5907ping");
            dto15.setState(xiaxianStr);
            dto15.setValue(dceDownSize);
            list.add(dto15);
            params.clear();
            HostUtil.addAccountqueryDaping(request, params);
            int snmpSize = this.snmpInfoService.countByParams(params);
            ChartDapingNewInfo dto16 = new ChartDapingNewInfo();
            dto16.setName("\u7f51\u7edc\u8bbe\u5907snmp");
            dto16.setState(zongshuliangStr);
            dto16.setValue(snmpSize);
            list.add(dto16);
            params.put("state", "2");
            int snmpDownSize = this.snmpInfoService.countByParams(params);
            ChartDapingNewInfo dto17 = new ChartDapingNewInfo();
            dto17.setName("\u7f51\u7edc\u8bbe\u5907snmp");
            dto17.setState(xiaxianStr);
            dto17.setValue(snmpDownSize);
            list.add(dto17);
            params.clear();
            HostUtil.addAccountqueryDaping(request, params);
            Integer dbTableSize = this.dbTableService.countByParams(params);
            ChartDapingNewInfo dto18 = new ChartDapingNewInfo();
            dto18.setName("\u6570\u636e\u8868");
            dto18.setState(zongshuliangStr);
            dto18.setValue(dbTableSize);
            list.add(dto18);
            params.put("state", "2");
            int dbTableDownSize = this.dbTableService.countByParams(params);
            ChartDapingNewInfo dto19 = new ChartDapingNewInfo();
            dto19.setName("\u6570\u636e\u8868");
            dto19.setState(xiaxianStr);
            dto19.setValue(dbTableDownSize);
            list.add(dto19);
            params.clear();
            HostUtil.addAccountqueryDaping(request, params);
            int dbInfoSize = this.dbInfoService.countByParams(params);
            ChartDapingNewInfo dto20 = new ChartDapingNewInfo();
            dto20.setName("\u76d1\u63a7\u6570\u636e\u5e93");
            dto20.setState(zongshuliangStr);
            dto20.setValue(dbInfoSize);
            list.add(dto20);
            params.put("dbState", "2");
            int dbInfoDownSize = this.dbInfoService.countByParams(params);
            ChartDapingNewInfo dto21 = new ChartDapingNewInfo();
            dto21.setName("\u76d1\u63a7\u6570\u636e\u5e93");
            dto21.setState(xiaxianStr);
            dto21.setValue(dbInfoDownSize);
            list.add(dto21);
            params.clear();
            HostUtil.addAccountqueryDaping(request, params);
            int heathSize = this.heathMonitorService.countByParams(params);
            ChartDapingNewInfo dto22 = new ChartDapingNewInfo();
            dto22.setName("\u670d\u52a1\u63a5\u53e3");
            dto22.setState(zongshuliangStr);
            dto22.setValue(heathSize);
            list.add(dto22);
            params.put("heathStatus", "200");
            int heath200Size = this.heathMonitorService.countByParams(params);
            ChartDapingNewInfo dto23 = new ChartDapingNewInfo();
            dto23.setName("\u670d\u52a1\u63a5\u53e3");
            dto23.setState(xiaxianStr);
            dto23.setValue(heathSize - heath200Size);
            list.add(dto23);
            params.clear();
            HostUtil.addAccountqueryDaping(request, params);
            int shellInfoSize = this.shellInfoService.countByParams(params);
            ChartDapingNewInfo dto24 = new ChartDapingNewInfo();
            dto24.setName("\u4e0b\u53d1\u6307\u4ee4");
            dto24.setState(zongshuliangStr);
            dto24.setValue(shellInfoSize);
            list.add(dto24);
            params.clear();
            HostUtil.addAccountqueryDaping(request, params);
            int ftpInfoSize = this.ftpInfoService.countByParams(params);
            ChartDapingNewInfo dto25 = new ChartDapingNewInfo();
            dto25.setName("FTP/SFTP");
            dto25.setState(zongshuliangStr);
            dto25.setValue(ftpInfoSize);
            list.add(dto25);
            params.put("state", "2");
            int ftpInfoDownSize = this.ftpInfoService.countByParams(params);
            ChartDapingNewInfo dto26 = new ChartDapingNewInfo();
            dto26.setName("FTP/SFTP");
            dto26.setState(xiaxianStr);
            dto26.setValue(ftpInfoDownSize);
            list.add(dto26);
            params.clear();
            HostUtil.addAccountqueryDaping(request, params);
            int equipmentInfoSize = this.equipmentService.countByParams(params);
            ChartDapingNewInfo dto27 = new ChartDapingNewInfo();
            dto27.setName("\u8d44\u4ea7\u7ba1\u7406");
            dto27.setState(zongshuliangStr);
            dto27.setValue(equipmentInfoSize);
            list.add(dto27);
            params.clear();
            HostUtil.addAccountqueryDaping(request, params);
            int passwdInfoSize = this.passwdInfoService.countByParams(params);
            ChartDapingNewInfo dto28 = new ChartDapingNewInfo();
            dto28.setName("\u8bbe\u5907\u8d26\u53f7");
            dto28.setState(zongshuliangStr);
            dto28.setValue(passwdInfoSize);
            list.add(dto28);
            params.clear();
            HostUtil.addAccountqueryDaping(request, params);
            int appExceptionInfoSize = this.appExceptionInfoService.countByParams(params);
            if (appExceptionInfoSize > 0) {
                ChartDapingNewInfo dto29 = new ChartDapingNewInfo();
                dto29.setName("\u5f02\u5e38\u8fdb\u7a0b");
                dto29.setState(zongshuliangStr);
                dto29.setValue(appExceptionInfoSize);
                list.add(dto29);
            }
            params.clear();
            HostUtil.addAccountqueryDaping(request, params);
            params.put("dataType", "deployment");
            int k8sDeploymentSize = this.k8sMonitorService.countByParams(params);
            if (k8sDeploymentSize > 0) {
                ChartDapingNewInfo dto30 = new ChartDapingNewInfo();
                dto30.setName("K8S-Deployment");
                dto30.setState(zongshuliangStr);
                dto30.setValue(k8sDeploymentSize);
                list.add(dto30);
            }
            params.clear();
            HostUtil.addAccountqueryDaping(request, params);
            params.put("dataType", "node");
            int k8sNodeSize = this.k8sMonitorService.countByParams(params);
            if (k8sNodeSize > 0) {
                ChartDapingNewInfo dto31 = new ChartDapingNewInfo();
                dto31.setName("K8S-Node");
                dto31.setState(zongshuliangStr);
                dto31.setValue(k8sNodeSize);
                list.add(dto31);
            }
            params.clear();
            HostUtil.addAccountqueryDaping(request, params);
            params.put("dataType", "container");
            int k8sContainerSize = this.k8sMonitorService.countByParams(params);
            if (k8sContainerSize > 0) {
                ChartDapingNewInfo dto32 = new ChartDapingNewInfo();
                dto32.setName("K8S-Container");
                dto32.setState(zongshuliangStr);
                dto32.setValue(k8sContainerSize);
                list.add(dto32);
            }
            params.clear();
            HostUtil.addAccountqueryDaping(request, params);
            params.put("dataType", "service");
            int k8sServiceSize = this.k8sMonitorService.countByParams(params);
            if (k8sServiceSize > 0) {
                ChartDapingNewInfo dto33 = new ChartDapingNewInfo();
                dto33.setName("K8S-Service");
                dto33.setState(zongshuliangStr);
                dto33.setValue(k8sServiceSize);
                list.add(dto33);
            }
            params.clear();
            HostUtil.addAccountqueryDaping(request, params);
            params.put("dataType", "pod");
            int k8sPodSize = this.k8sMonitorService.countByParams(params);
            if (k8sPodSize > 0) {
                ChartDapingNewInfo dto34 = new ChartDapingNewInfo();
                dto34.setName("K8S-Pod");
                dto34.setState(zongshuliangStr);
                dto34.setValue(k8sPodSize);
                list.add(dto34);
            }
            params.clear();
            HostUtil.addAccountqueryDaping(request, params);
            params.put("dataType", "namespace");
            int k8sNamespaceSize = this.k8sMonitorService.countByParams(params);
            if (k8sNamespaceSize > 0) {
                ChartDapingNewInfo dto35 = new ChartDapingNewInfo();
                dto35.setName("K8S-Pod");
                dto35.setState(zongshuliangStr);
                dto35.setValue(k8sNamespaceSize);
                list.add(dto35);
            }
            params.clear();
            HostUtil.addAccountqueryDaping(request, params);
            int kafkaInfoSize = this.kafkaMonitorService.countByParams(params);
            ChartDapingNewInfo dto36 = new ChartDapingNewInfo();
            dto36.setName("Kafka");
            dto36.setState(zongshuliangStr);
            dto36.setValue(kafkaInfoSize);
            list.add(dto36);
            params.put("state", "2");
            int kafkaInfoDownSize = this.kafkaMonitorService.countByParams(params);
            ChartDapingNewInfo dto37 = new ChartDapingNewInfo();
            dto37.setName("Kafka");
            dto37.setState(xiaxianStr);
            dto37.setValue(kafkaInfoDownSize);
            list.add(dto37);
            params.clear();
            HostUtil.addAccountqueryDaping(request, params);
            int redisInfoSize = this.redisMonitorService.countByParams(params);
            ChartDapingNewInfo dto38 = new ChartDapingNewInfo();
            dto38.setName("Redis");
            dto38.setState(zongshuliangStr);
            dto38.setValue(redisInfoSize);
            list.add(dto38);
            params.put("state", "2");
            int redisInfoDownSize = this.redisMonitorService.countByParams(params);
            ChartDapingNewInfo dto39 = new ChartDapingNewInfo();
            dto39.setName("Redis");
            dto39.setState(xiaxianStr);
            dto39.setValue(redisInfoDownSize);
            list.add(dto39);
            model.addAttribute("allDataChartJson", (Object)JSONUtil.parseArray(list));
        }
        catch (Exception e) {
            logger.error("allDataChartHandle\u9519\u8bef", (Throwable)e);
        }
    }

    public void hostDaysHandle(Model model, HttpServletRequest request) {
        try {
            HashMap<String, Object> params = new HashMap<String, Object>();
            params.put("orderBy", "CREATE_TIME");
            params.put("orderType", "DESC");
            params.put("countBlockNe", "2");
            HostUtil.addAccountqueryDaping(request, params);
            PageInfo pageInfo = this.systemInfoService.selectByParams(params, 1, 30);
            ArrayList<String> systemNameList = new ArrayList<String>();
            ArrayList<Integer> dayValList = new ArrayList<Integer>();
            for (SystemInfo systemInfo : (java.util.List<SystemInfo>) pageInfo.getList()) {
                systemNameList.add(systemInfo.getHostname());
                String uptimeStr = systemInfo.getUptimeStr();
                Integer days = 1;
                if (!StringUtils.isEmpty((CharSequence)uptimeStr) && uptimeStr.contains("\u5929")) {
                    try {
                        days = Integer.valueOf(uptimeStr.substring(0, uptimeStr.indexOf("\u5929")));
                    }
                    catch (Exception e) {
                        logger.error("Integer\u8f6c\u6362\u9519\u8bef", (Throwable)e);
                    }
                }
                dayValList.add(days);
            }
            model.addAttribute("systemNameListJson", (Object)JSONUtil.parseArray(systemNameList));
            model.addAttribute("dayValListJson", (Object)JSONUtil.parseArray(dayValList));
        }
        catch (Exception e) {
            logger.error("\u7ec4\u88c5\u5927\u5c4f\u5217\u8868\u9875\u9762\u7684\u5e95\u90e8\u67f1\u72b6\u56fe\u6570\u636e\u9519\u8bef", (Throwable)e);
        }
    }
}

