/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.task;

import cn.hutool.core.collection.CollectionUtil;
import com.wgcloud.config.CommonConfig;
import com.wgcloud.config.MailConfig;
import com.wgcloud.entity.AccountInfo;
import com.wgcloud.entity.AgentRunState;
import com.wgcloud.entity.AppInfo;
import com.wgcloud.entity.CustomInfo;
import com.wgcloud.entity.DceInfo;
import com.wgcloud.entity.DockerInfo;
import com.wgcloud.entity.FileSafe;
import com.wgcloud.entity.HeathMonitor;
import com.wgcloud.entity.HostWarnDiy;
import com.wgcloud.entity.MailSet;
import com.wgcloud.entity.PortInfo;
import com.wgcloud.entity.SnmpDeepInfo;
import com.wgcloud.entity.SnmpInfo;
import com.wgcloud.entity.SystemInfo;
import com.wgcloud.service.AccountInfoService;
import com.wgcloud.service.AgentRunStateService;
import com.wgcloud.service.AppInfoService;
import com.wgcloud.service.CustomInfoService;
import com.wgcloud.service.DbTableService;
import com.wgcloud.service.DceInfoService;
import com.wgcloud.service.DockerInfoService;
import com.wgcloud.service.EquipmentService;
import com.wgcloud.service.FileSafeService;
import com.wgcloud.service.FtpInfoService;
import com.wgcloud.service.HeathMonitorService;
import com.wgcloud.service.HostWarnDiyService;
import com.wgcloud.service.LogInfoService;
import com.wgcloud.service.MailSetService;
import com.wgcloud.service.PasswdInfoService;
import com.wgcloud.service.PortInfoService;
import com.wgcloud.service.SnmpDeepInfoService;
import com.wgcloud.service.SnmpInfoService;
import com.wgcloud.service.SystemInfoService;
import com.wgcloud.service.TaskUtilService;
import com.wgcloud.service.UfmMonitorService;
import com.wgcloud.util.DESUtil;
import com.wgcloud.util.DateUtil;
import com.wgcloud.util.HostUtil;
import com.wgcloud.util.ServerBackupUtil;
import com.wgcloud.util.SnmpDeepUtil;
import com.wgcloud.util.SnmpUtil;
import com.wgcloud.util.ThreadPoolUtil;
import com.wgcloud.util.license.LicenseUtil;
import com.wgcloud.util.msg.WarnMailUtil;
import com.wgcloud.util.msg.WarnPools;
import com.wgcloud.util.staticvar.BatchData;
import com.wgcloud.util.staticvar.StaticKeys;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.ServletContext;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTask {
    private Logger logger = LoggerFactory.getLogger(ScheduledTask.class);
    @Autowired
    private SystemInfoService systemInfoService;
    @Autowired
    private LogInfoService logInfoService;
    @Autowired
    private AppInfoService appInfoService;
    @Autowired
    private FileSafeService fileSafeService;
    @Autowired
    private DockerInfoService dockerInfoService;
    @Autowired
    private PortInfoService portInfoService;
    @Autowired
    private MailSetService mailSetService;
    @Autowired
    private AccountInfoService accountInfoService;
    @Autowired
    private TaskUtilService taskUtilService;
    @Autowired
    private CustomInfoService customInfoService;
    @Autowired
    private PasswdInfoService passwdInfoService;
    @Autowired
    private EquipmentService equipmentService;
    @Autowired
    private DbTableService dbTableService;
    @Autowired
    private FtpInfoService ftpInfoService;
    @Autowired
    private HeathMonitorService heathMonitorService;
    @Autowired
    private DceInfoService dceInfoService;
    @Autowired
    private SnmpInfoService snmpInfoService;
    @Resource
    private HostWarnDiyService hostWarnDiyService;
    @Resource
    private AgentRunStateService agentRunStateService;
    @Resource
    private SnmpDeepInfoService snmpDeepInfoService;
    @Resource
    private UfmMonitorService ufmMonitorService;
    @Autowired
    private CommonConfig commonConfig;
    @Autowired
    private MailConfig mailConfig;
    @Autowired
    private ServletContext servletContext;

    @Scheduled(initialDelay=5000L, fixedRate=86400000L)
    public void validateLicense() {
        this.logger.info("validateLicense-v3.6.8------------" + DateUtil.getDateTimeString(new Date()));
        try {
            this.servletContext.setAttribute("icoUrl", (Object)(StaticKeys.SERVER_SERVLET_CONTEXT_PATH + "/static/logincss/favicon.png"));
            this.servletContext.setAttribute("logoUrl", (Object)(StaticKeys.SERVER_SERVLET_CONTEXT_PATH + "/static/logincss/logo.png"));
            this.servletContext.setAttribute("wgName", "WGCLOUD");
            this.servletContext.setAttribute("wgShortName", "WGCLOUD");
            this.servletContext.setAttribute("warnSoundName", (Object)(StaticKeys.SERVER_SERVLET_CONTEXT_PATH + "/static/common/sounds/warn.mp3"));
            this.taskUtilService.initMenuNames();
            HashMap<String, Object> params = new HashMap<String, Object>();
            int listAgentNum = this.systemInfoService.countByParams(params);
            int listPingNum = this.dceInfoService.countByParams(params);
            int listSnmpInfoNum = this.snmpInfoService.countByParams(params);
            StaticKeys.LICENSE_STATE = LicenseUtil.validateLicense(listAgentNum, this.commonConfig.getPageSize(), listPingNum, listSnmpInfoNum);
            this.servletContext.setAttribute("LICENSE_STATE", (Object)StaticKeys.LICENSE_STATE);
            LicenseUtil.sendStopWarnMail(listAgentNum);
            LicenseUtil.footerLicenseHandle(this.servletContext, this.commonConfig.getShowVersion());
            this.servletContext.setAttribute("copyRight", "true");
            this.servletContext.setAttribute("copyRightLoginContent", "");
            this.servletContext.setAttribute("copyRightMainContent", "");
            if (StaticKeys.LICENSE_STATE.equals("1")) {
                if (!StringUtils.isEmpty((CharSequence)this.commonConfig.getIcoName())) {
                    this.servletContext.setAttribute("icoUrl", (Object)(StaticKeys.SERVER_SERVLET_CONTEXT_PATH + "/resources/" + this.commonConfig.getIcoName()));
                }
                if (!StringUtils.isEmpty((CharSequence)this.commonConfig.getLogoName())) {
                    this.servletContext.setAttribute("logoUrl", (Object)(StaticKeys.SERVER_SERVLET_CONTEXT_PATH + "/resources/" + this.commonConfig.getLogoName()));
                }
                if (!StringUtils.isEmpty((CharSequence)this.commonConfig.getWgName())) {
                    this.servletContext.setAttribute("wgName", (Object)this.commonConfig.getWgName());
                }
                if (!StringUtils.isEmpty((CharSequence)this.commonConfig.getWgShortName())) {
                    this.servletContext.setAttribute("wgShortName", (Object)this.commonConfig.getWgShortName());
                }
                this.servletContext.setAttribute("copyRight", (Object)this.commonConfig.getCopyRight());
                if (!StringUtils.isEmpty((CharSequence)this.commonConfig.getCopyRightLoginContent()) && "yes".equals(StaticKeys.LICENSE_COPYRIGHT_UPDATE)) {
                    this.servletContext.setAttribute("copyRightLoginContent", (Object)this.commonConfig.getCopyRightLoginContent());
                }
                if (!StringUtils.isEmpty((CharSequence)this.commonConfig.getCopyRightMainContent()) && "yes".equals(StaticKeys.LICENSE_COPYRIGHT_UPDATE)) {
                    this.servletContext.setAttribute("copyRightMainContent", (Object)this.commonConfig.getCopyRightMainContent());
                }
                if (!StringUtils.isEmpty((CharSequence)this.commonConfig.getWarnSoundName())) {
                    this.servletContext.setAttribute("warnSoundName", (Object)(StaticKeys.SERVER_SERVLET_CONTEXT_PATH + "/resources/" + this.commonConfig.getWarnSoundName()));
                }
            }
            if (!(StaticKeys.LICENSE_STATE.equals("1") && "yes".equals(StaticKeys.LICENSE_DIY_CONTEXT_PATH) || "/wgcloud".equals(StaticKeys.SERVER_SERVLET_CONTEXT_PATH))) {
                StaticKeys.SERVER_SERVLET_CONTEXT_PATH = "";
                this.servletContext.setAttribute("SERVER_SERVLET_CONTEXT_PATH", "");
            }
            LicenseUtil.sendOutDateMail();
            this.taskUtilService.setDiyMenuNames();
        }
        catch (Exception e) {
            this.logger.error("\u68c0\u6d4blicense\u4efb\u52a1\u9519\u8bef", (Throwable)e);
        }
    }

    @Scheduled(initialDelay=15000L, fixedRate=3600000L)
    public void sumDiskSizeCacheTask() {
        this.logger.info("sumDiskSizeCacheTask------------" + DateUtil.getDateTimeString(new Date()));
        try {
            this.servletContext.setAttribute("sumDiskSizeCache", (Object)this.taskUtilService.sumDiskSizeCache(null));
            if (LicenseUtil.checkEnterpriseVersion()) {
                this.dceInfoService.importExcelPing();
                this.passwdInfoService.importExcelPasswd();
                this.equipmentService.importExcelEquipment();
            }
            HashMap<String, Object> params = new HashMap<String, Object>();
            this.servletContext.setAttribute("warnCountAnHour", (Object)0);
            params.put("hostname", "\u544a\u8b66");
            params.put("startTime", DateUtil.beforeMinutesToNowDate(60));
            params.put("endTime", DateUtil.getCurrentDateTime());
            this.servletContext.setAttribute("warnCountAnHour", (Object)this.logInfoService.countByParams(params));
        }
        catch (Exception e) {
            this.logger.error("\u83b7\u53d6\u6240\u6709\u78c1\u76d8\u603b\u5bb9\u91cf\u4e4b\u548c\u4efb\u52a1\u9519\u8bef", (Throwable)e);
        }
    }

    @Scheduled(initialDelay=20000L, fixedRate=360000L)
    public void initTask() {
        this.logger.info("initTask------------" + DateUtil.getDateTimeString(new Date()));
        try {
            HashMap<String, Object> params = new HashMap<String, Object>();
            List<MailSet> list = this.mailSetService.selectAllByParams(params);
            if (list.size() > 0) {
                MailSet selectMailSet = list.get(0);
                if (!"2".equals(selectMailSet.getActive())) {
                    selectMailSet.setFromPwd(DESUtil.decryptForServerDb(selectMailSet.getFromPwd()));
                    StaticKeys.mailSet = selectMailSet;
                } else {
                    this.logger.info("\u5df2\u8bbe\u7f6e\u6682\u505c\u90ae\u4ef6\u544a\u8b66\u53d1\u9001");
                    StaticKeys.mailSet = null;
                }
            } else {
                StaticKeys.mailSet = null;
            }
            StaticKeys.ACCOUNT_INFO_MAP.clear();
            if ("true".equals(this.commonConfig.getUserInfoManage()) && StaticKeys.LICENSE_STATE.equals("1")) {
                List<AccountInfo> accountInfoList = this.accountInfoService.selectAllByParams(new HashMap<String, Object>());
                for (AccountInfo accountInfo : accountInfoList) {
                    StaticKeys.ACCOUNT_INFO_MAP.put(accountInfo.getAccount(), accountInfo);
                }
            }
            StaticKeys.WARN_CRON_TIME_SIGN = DateUtil.isWarnTime(this.mailConfig.getWarnCronTime());
            DateUtil.checkPwdExp(this.commonConfig.getPwdExpDate());
            StaticKeys.HOST_WARN_MAP.clear();
            HashMap<String, Object> paramsWarnDiy = new HashMap<String, Object>();
            paramsWarnDiy.put("active", "1");
            List<HostWarnDiy> hostWarnDiyList = this.hostWarnDiyService.selectAllByParams(paramsWarnDiy);
            for (HostWarnDiy hostWarnDiy : hostWarnDiyList) {
                hostWarnDiy.setRemark("");
                StaticKeys.HOST_WARN_MAP.put(hostWarnDiy.getHostname(), hostWarnDiy);
            }
        }
        catch (Exception e) {
            this.logger.error("initTask\u9519\u8bef", (Throwable)e);
        }
    }

    @Scheduled(initialDelay=60000L, fixedRateString="${base.heathTimes}000")
    public void heathMonitorTask() {
        if (!"master".equals(this.commonConfig.getNodeType())) {
            this.logger.info("slave\u8282\u70b9\u4e0d\u6267\u884c\u68c0\u6d4b\u670d\u52a1\u63a5\u53e3\u4efb\u52a1");
            return;
        }
        if (DateUtil.isClearTime()) {
            this.logger.info("\u6b63\u5728\u6e05\u7a7a\u5386\u53f2\u6570\u636e\uff0c\u4e0d\u6267\u884c\u670d\u52a1\u63a5\u53e3\u76d1\u63a7----------" + DateUtil.getCurrentDateTime());
            return;
        }
        this.logger.info("heathMonitorTask------------" + DateUtil.getDateTimeString(new Date()));
        HashMap<String, Object> params = new HashMap<String, Object>();
        Date date = new Date();
        try {
            params.put("active", "1");
            List<HeathMonitor> heathMonitorAllList = this.heathMonitorService.selectAllByParams(params);
            if (heathMonitorAllList.size() > 0) {
                if (heathMonitorAllList.size() > 10 && !StaticKeys.LICENSE_STATE.equals("1")) {
                    this.logger.info("\u4e2a\u4eba\u7248\u53ea\u80fd\u76d1\u6d4b10\u4e2a\u670d\u52a1\u63a5\u53e3");
                    heathMonitorAllList = heathMonitorAllList.subList(0, 10);
                }
                for (HeathMonitor heathMonitor : heathMonitorAllList) {
                    if (ServerBackupUtil.isExistHeathMonitorId(heathMonitor.getId())) {
                        this.logger.info("\u6b64\u63a5\u53e3\u7531wgcloud-server-backup\u76d1\u6d4b:" + heathMonitor.getAppName());
                        continue;
                    }
                    Runnable runnable = () -> this.heathMonitorService.taskThreadHandler(heathMonitor, date);
                    ThreadPoolUtil.executor.execute(runnable);
                }
            }
        }
        catch (Exception e) {
            this.logger.error("\u670d\u52a1\u63a5\u53e3\u68c0\u6d4b\u4efb\u52a1\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("\u670d\u52a1\u63a5\u53e3\u68c0\u6d4b\u9519\u8bef", e.toString(), "2");
        }
    }

    @Scheduled(initialDelay=90000L, fixedRateString="${base.dceTimes}000")
    public void dceInfoTask() {
        if (!"master".equals(this.commonConfig.getNodeType())) {
            this.logger.info("slave\u8282\u70b9\u4e0d\u6267\u884c\u68c0\u6d4b\u7f51\u7edc\u8bbe\u5907PING\u76d1\u63a7");
            return;
        }
        if (DateUtil.isClearTime()) {
            this.logger.info("\u6b63\u5728\u6e05\u7a7a\u5386\u53f2\u6570\u636e\uff0c\u4e0d\u6267\u884c\u7f51\u7edc\u8bbe\u5907PING\u76d1\u63a7----------" + DateUtil.getCurrentDateTime());
            return;
        }
        this.logger.info("dceInfoTask------------" + DateUtil.getDateTimeString(new Date()));
        HashMap<String, Object> params = new HashMap<String, Object>();
        Date date = new Date();
        try {
            params.put("active", "1");
            List<DceInfo> dceInfoAllList = this.dceInfoService.selectAllByParams(params);
            if (dceInfoAllList.size() > 0) {
                if (dceInfoAllList.size() > 10 && !StaticKeys.LICENSE_STATE.equals("1")) {
                    this.logger.info("\u4e2a\u4eba\u7248\u53ea\u80fd\u76d1\u6d4b10\u4e2aPING\u8bbe\u5907");
                    dceInfoAllList = dceInfoAllList.subList(0, 10);
                }
                for (DceInfo dceInfo : dceInfoAllList) {
                    if (ServerBackupUtil.isExistDceInfoId(dceInfo.getId())) {
                        this.logger.info("\u6b64\u8bbe\u5907\u7531wgcloud-server-backup\u76d1\u6d4b:" + dceInfo.getHostname());
                        continue;
                    }
                    Runnable runnable = () -> this.dceInfoService.taskThreadHandler(dceInfo, date);
                    ThreadPoolUtil.executor.execute(runnable);
                }
            }
        }
        catch (Exception e) {
            this.logger.error("\u7f51\u7edc\u8bbe\u5907PING\u68c0\u6d4b\u4efb\u52a1\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("\u7f51\u7edc\u8bbe\u5907PING\u68c0\u6d4b\u4efb\u52a1\u9519\u8bef", e.toString(), "2");
        }
    }

    @Scheduled(initialDelay=120000L, fixedRateString="${base.dbTableTimes}000")
    public void tableCountTask() {
        if (!"master".equals(this.commonConfig.getNodeType())) {
            this.logger.info("slave\u8282\u70b9\u4e0d\u6267\u884c\u6570\u636e\u8868\u76d1\u63a7\u4efb\u52a1");
            return;
        }
        if (DateUtil.isClearTime()) {
            this.logger.info("\u6b63\u5728\u6e05\u7a7a\u5386\u53f2\u6570\u636e\uff0c\u4e0d\u6267\u884c\u6570\u636e\u8868\u76d1\u63a7----------" + DateUtil.getCurrentDateTime());
            return;
        }
        this.logger.info("tableCountTask------------" + DateUtil.getDateTimeString(new Date()));
        try {
            this.dbTableService.taskThreadHandler();
        }
        catch (Exception e) {
            this.logger.error("\u6570\u636e\u8868\u76d1\u63a7\u4efb\u52a1\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("\u6570\u636e\u8868\u76d1\u63a7\u4efb\u52a1\u9519\u8bef", e.toString(), "2");
        }
    }

    @Scheduled(initialDelay=140000L, fixedRateString="${base.ftpTimes}000")
    public void ftpInfoTask() {
        if (!"master".equals(this.commonConfig.getNodeType())) {
            this.logger.info("slave\u8282\u70b9\u4e0d\u6267\u884c\u6570\u636e\u8868\u76d1\u63a7\u4efb\u52a1");
            return;
        }
        if (DateUtil.isClearTime()) {
            this.logger.info("\u6b63\u5728\u6e05\u7a7a\u5386\u53f2\u6570\u636e\uff0c\u4e0d\u6267\u884c\u6570\u636e\u8868\u76d1\u63a7----------" + DateUtil.getCurrentDateTime());
            return;
        }
        this.logger.info("ftpInfoTask------------" + DateUtil.getDateTimeString(new Date()));
        try {
            this.ftpInfoService.taskThreadHandler();
        }
        catch (Exception e) {
            this.logger.error("FTP\u76d1\u63a7\u4efb\u52a1\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("FTP\u76d1\u63a7\u4efb\u52a1\u9519\u8bef", e.toString(), "2");
        }
    }

    @Scheduled(initialDelay=150000L, fixedRateString="${base.snmpTimes}000")
    public void snmpInfoTask() {
        if (!"master".equals(this.commonConfig.getNodeType())) {
            this.logger.info("slave\u8282\u70b9\u4e0d\u6267\u884csnmp\u8bbe\u5907\u76d1\u6d4b\u4efb\u52a1");
            return;
        }
        if (DateUtil.isClearTime()) {
            this.logger.info("\u6b63\u5728\u6e05\u7a7a\u5386\u53f2\u6570\u636e\uff0c\u4e0d\u6267\u884csnmp\u8bbe\u5907\u76d1\u63a7----------" + DateUtil.getCurrentDateTime());
            return;
        }
        this.logger.info("snmpInfoTask------------" + DateUtil.getDateTimeString(new Date()));
        HashMap<String, Object> params = new HashMap<String, Object>();
        Date date = new Date();
        try {
            params.put("active", "1");
            List<SnmpInfo> snmpInfoAllList = this.snmpInfoService.selectAllByParams(params);
            if (snmpInfoAllList.size() > 0) {
                if (snmpInfoAllList.size() > 10 && !StaticKeys.LICENSE_STATE.equals("1")) {
                    this.logger.info("\u5f53\u524d\u4e0d\u662f\u4f01\u4e1a\u7248\uff0c\u4e0d\u8fdb\u884cSNMP\u6df1\u5ea6\u76d1\u63a7\u4efb\u52a1");
                    snmpInfoAllList = snmpInfoAllList.subList(0, 10);
                }
                Map<String, String> snmpMap = SnmpUtil.getOnLineList(snmpInfoAllList);
                for (SnmpInfo snmpInfo : snmpInfoAllList) {
                    if (ServerBackupUtil.isExistSnmpInfoId(snmpInfo.getId())) {
                        this.logger.info("\u6b64\u8bbe\u5907\u7531wgcloud-server-backup\u76d1\u6d4b:" + snmpInfo.getHostname());
                        continue;
                    }
                    Runnable runnable = () -> this.snmpInfoService.taskThreadHandler(snmpMap, snmpInfo, date);
                    ThreadPoolUtil.executor.execute(runnable);
                }
            }
        }
        catch (Exception e) {
            this.logger.error("SNMP\u8bbe\u5907\u68c0\u6d4b\u4efb\u52a1\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("SNMP\u8bbe\u5907\u68c0\u6d4b\u4efb\u52a1\u9519\u8bef", e.toString(), "2");
        }
    }

    @Scheduled(initialDelay=200000L, fixedRateString="${base.snmpDeepTimes}000")
    public void snmpDeepInfoTask() {
        if (!"master".equals(this.commonConfig.getNodeType())) {
            this.logger.info("slave\u8282\u70b9\u4e0d\u6267\u884csnmp\u8bbe\u5907\u76d1\u6d4b\u4efb\u52a1");
            return;
        }
        if (DateUtil.isClearTime()) {
            this.logger.info("\u6b63\u5728\u6e05\u7a7a\u5386\u53f2\u6570\u636e\uff0c\u4e0d\u6267\u884csnmp\u8bbe\u5907\u76d1\u63a7----------" + DateUtil.getCurrentDateTime());
            return;
        }
        this.logger.info("snmpDeepInfoTask------------" + DateUtil.getDateTimeString(new Date()));
        HashMap<String, Object> params = new HashMap<String, Object>();
        Date date = new Date();
        try {
            if (!LicenseUtil.checkEnterpriseVersion()) {
                this.logger.info("snmpDeepInfoTask------------" + DateUtil.getDateTimeString(new Date()));
                return;
            }
            params.put("active", "1");
            List<SnmpDeepInfo> snmpInfoAllList = this.snmpDeepInfoService.selectAllByParams(params);
            if (snmpInfoAllList.size() > 0) {
                Map<String, String> snmpMap = SnmpDeepUtil.getOnLineList(snmpInfoAllList);
                for (SnmpDeepInfo snmpDeepInfo : snmpInfoAllList) {
                    if (ServerBackupUtil.isExistSnmpInfoId(snmpDeepInfo.getId())) {
                        this.logger.info("\u6b64\u8bbe\u5907\u7531wgcloud-server-backup\u76d1\u6d4b:" + snmpDeepInfo.getHostname());
                        continue;
                    }
                    Runnable runnable = () -> this.snmpDeepInfoService.taskThreadHandler(snmpMap, snmpDeepInfo, date);
                    ThreadPoolUtil.executor.execute(runnable);
                }
            }
        }
        catch (Exception e) {
            this.logger.error("SNMP\u8bbe\u5907\u6df1\u5ea6\u68c0\u6d4b\u4efb\u52a1\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("SNMP\u8bbe\u5907\u6df1\u5ea6\u68c0\u6d4b\u4efb\u52a1\u9519\u8bef", e.toString(), "2");
        }
    }

    @Scheduled(initialDelay=300000L, fixedRate=300000L)
    public void hostDownCheckTask() {
        if (!"master".equals(this.commonConfig.getNodeType())) {
            this.logger.info("slave\u8282\u70b9\u4e0d\u6267\u884c\u68c0\u6d4b\u4e3b\u673a/\u8fdb\u7a0b/docker/\u7aef\u53e3\u662f\u5426\u6062\u590d\u4efb\u52a1");
            return;
        }
        if (DateUtil.isClearTimeForHost()) {
            this.logger.info("\u6b63\u5728\u6e05\u7a7a\u5386\u53f2\u6570\u636e\uff0c\u4e0d\u6267\u884c\u63d0\u4ea4\u68c0\u6d4b\u4e3b\u673a/\u8fdb\u7a0b/docker/\u7aef\u53e3\u6570\u636e----------" + DateUtil.getCurrentDateTime());
            BatchData.clearAll();
            return;
        }
        this.logger.info("hostDownCheckTask------------" + DateUtil.getDateTimeString(new Date()));
        this.checkHostDown();
        this.checkAppDown();
        this.checkDockerDown();
        this.checkPortDown();
        this.checkFileSafeDown();
        this.checkCustomInfoDown();
    }

    private void checkHostDown() {
        ArrayList<String> downHostNameList = new ArrayList<String>();
        try {
            HashMap<String, Object> params = new HashMap<String, Object>();
            List<SystemInfo> list = this.systemInfoService.selectAllByParams(params);
            if (!CollectionUtil.isEmpty(list)) {
                HostUtil.initHostCacheMap(list);
                for (SystemInfo systemInfo : list) {
                    if ("2".equals(systemInfo.getActive())) continue;
                    this.agentTimeOutHandle(systemInfo, downHostNameList);
                }
                if (downHostNameList.size() > 0) {
                    this.systemInfoService.downByHostName(downHostNameList);
                }
            }
        }
        catch (Exception e) {
            this.logger.error("\u68c0\u6d4b\u4e3b\u673a\u662f\u5426\u79bb\u7ebf\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("\u68c0\u6d4b\u4e3b\u673a\u662f\u5426\u79bb\u7ebf\u9519\u8bef", e.toString(), "2");
        }
    }

    private void agentTimeOutHandle(SystemInfo systemInfo, List<String> downHostNameList) {
        Runnable runnable;
        Date createTime = systemInfo.getCreateTime();
        long diff = System.currentTimeMillis() - createTime.getTime();
        Integer submitSeconds = 180000;
        try {
            Integer hostDownWarnCount = this.mailConfig.getHostDownWarnCount();
            HostWarnDiy hostWarnDiyDto = StaticKeys.HOST_WARN_MAP.get(systemInfo.getHostname());
            if (null != hostWarnDiyDto && !StringUtils.isEmpty((CharSequence)hostWarnDiyDto.getHostDownWarnCount())) {
                hostDownWarnCount = Integer.valueOf(hostWarnDiyDto.getHostDownWarnCount());
            }
            submitSeconds = (Integer.valueOf(systemInfo.getSubmitSeconds()) * hostDownWarnCount + 60) * 1000;
        }
        catch (Exception e) {
            this.logger.error("Integer\u8f6c\u6362\u9519\u8bef", (Throwable)e);
        }
        if (diff >= (long)submitSeconds.intValue()) {
            if (StringUtils.isEmpty((CharSequence)WarnPools.getWarnMark(systemInfo.getId()))) {
                downHostNameList.add(systemInfo.getHostname());
            }
            runnable = () -> WarnMailUtil.sendHostDown(systemInfo, true, false);
            ThreadPoolUtil.executor.execute(runnable);
        } else if (!StringUtils.isEmpty((CharSequence)WarnPools.getWarnMark(systemInfo.getId()))) {
            runnable = () -> WarnMailUtil.sendHostDown(systemInfo, false, false);
            ThreadPoolUtil.executor.execute(runnable);
        }
    }

    private void checkAppDown() {
        try {
            HashMap<String, Object> params = new HashMap<String, Object>();
            params.put("state", "1");
            params.put("active", "1");
            List<AppInfo> list = this.appInfoService.selectAllByParams(params);
            if (!CollectionUtil.isEmpty(list)) {
                for (AppInfo appInfo : list) {
                    if (StringUtils.isEmpty((CharSequence)WarnPools.getWarnMark(appInfo.getId()))) continue;
                    Runnable runnable = () -> WarnMailUtil.sendAppDown(appInfo, false);
                    ThreadPoolUtil.executor.execute(runnable);
                }
            }
        }
        catch (Exception e) {
            this.logger.error("\u68c0\u6d4b\u8fdb\u7a0b\u662f\u5426\u6062\u590d\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("\u68c0\u6d4b\u8fdb\u7a0b\u662f\u5426\u6062\u590d\u9519\u8bef", e.toString(), "2");
        }
    }

    private void checkDockerDown() {
        try {
            HashMap<String, Object> params = new HashMap<String, Object>();
            params.put("state", "1");
            params.put("active", "1");
            List<DockerInfo> list = this.dockerInfoService.selectAllByParams(params);
            if (!CollectionUtil.isEmpty(list)) {
                for (DockerInfo appInfo : list) {
                    if (StringUtils.isEmpty((CharSequence)WarnPools.getWarnMark(appInfo.getId()))) continue;
                    Runnable runnable = () -> WarnMailUtil.sendDockerDown(appInfo, false);
                    ThreadPoolUtil.executor.execute(runnable);
                }
            }
        }
        catch (Exception e) {
            this.logger.error("\u68c0\u6d4bdocker\u662f\u5426\u6062\u590d\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("\u68c0\u6d4bdocker\u662f\u5426\u6062\u590d\u9519\u8bef", e.toString(), "2");
        }
    }

    private void checkPortDown() {
        try {
            HashMap<String, Object> params = new HashMap<String, Object>();
            params.put("state", "1");
            params.put("active", "1");
            List<PortInfo> list = this.portInfoService.selectAllByParams(params);
            if (!CollectionUtil.isEmpty(list)) {
                for (PortInfo appInfo : list) {
                    if (StringUtils.isEmpty((CharSequence)WarnPools.getWarnMark(appInfo.getId()))) continue;
                    Runnable runnable = () -> WarnMailUtil.sendPortDown(appInfo, false);
                    ThreadPoolUtil.executor.execute(runnable);
                }
            }
        }
        catch (Exception e) {
            this.logger.error("\u68c0\u6d4b\u7aef\u53e3\u662f\u5426\u6062\u590d\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("\u68c0\u6d4b\u7aef\u53e3\u662f\u5426\u6062\u590d\u9519\u8bef", e.toString(), "2");
        }
    }

    private void checkFileSafeDown() {
        try {
            HashMap<String, Object> params = new HashMap<String, Object>();
            params.put("state", "1");
            params.put("active", "1");
            List<FileSafe> list = this.fileSafeService.selectAllByParams(params);
            if (!CollectionUtil.isEmpty(list)) {
                for (FileSafe fileSafe : list) {
                    if (StringUtils.isEmpty((CharSequence)WarnPools.getWarnMark(fileSafe.getId()))) continue;
                    Runnable runnable = () -> WarnMailUtil.sendFileSafeDown(fileSafe, false, null);
                    ThreadPoolUtil.executor.execute(runnable);
                }
            }
        }
        catch (Exception e) {
            this.logger.error("\u68c0\u6d4b\u6587\u4ef6\u9632\u7be1\u6539\u76d1\u6d4b\u662f\u5426\u6062\u590d\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("\u68c0\u6d4b\u6587\u4ef6\u9632\u7be1\u6539\u76d1\u6d4b\u662f\u5426\u6062\u590d\u9519\u8bef", e.toString(), "2");
        }
    }

    private void checkCustomInfoDown() {
        try {
            HashMap<String, Object> params = new HashMap<String, Object>();
            params.put("state", "1");
            params.put("active", "1");
            List<CustomInfo> list = this.customInfoService.selectAllByParams(params);
            if (!CollectionUtil.isEmpty(list)) {
                for (CustomInfo customInfo : list) {
                    if (StringUtils.isEmpty((CharSequence)WarnPools.getWarnMark(customInfo.getId()))) continue;
                    Runnable runnable = () -> WarnMailUtil.sendCustomInfoDown(customInfo, false);
                    ThreadPoolUtil.executor.execute(runnable);
                }
            }
        }
        catch (Exception e) {
            this.logger.error("\u68c0\u6d4b\u81ea\u5b9a\u4e49\u76d1\u63a7\u9879\u662f\u5426\u6062\u590d\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("\u68c0\u6d4b\u81ea\u5b9a\u4e49\u76d1\u63a7\u9879\u662f\u5426\u6062\u590d\u9519\u8bef", e.toString(), "2");
        }
    }

    @Scheduled(initialDelay=600000L, fixedRate=3600000L)
    public void sumAgentTimeTask() {
        if (!"master".equals(this.commonConfig.getNodeType())) {
            this.logger.info("slave\u8282\u70b9\u4e0d\u6267\u884c\u8ba1\u7b97\u6bcf\u4e2aagent\u7ec8\u7aef\u7684\u7d2f\u8ba1\u8fd0\u884c\u65f6\u95f4\u4efb\u52a1");
            return;
        }
        this.logger.info("sumAgentTimeTask------------" + DateUtil.getDateTimeString(new Date()));
        try {
            HashMap<String, Object> params = new HashMap<String, Object>();
            List<SystemInfo> systemInfoList = this.systemInfoService.selectAllByParams(params);
            List<AgentRunState> agentRunStateList = this.agentRunStateService.selectAllByParams(params);
            ArrayList<AgentRunState> agentRunStateAddList = new ArrayList<AgentRunState>();
            ArrayList<AgentRunState> agentRunStateUpdateList = new ArrayList<AgentRunState>();
            Date nowDate = new Date();
            CollectionUtil.reverse(systemInfoList);
            for (SystemInfo systemInfo : systemInfoList) {
                boolean isSaved = false;
                for (AgentRunState agentRunState : agentRunStateList) {
                    if (!agentRunState.getHostname().equals(systemInfo.getHostname())) continue;
                    if ("1".equals(systemInfo.getState())) {
                        agentRunState.setOnlineTime(agentRunState.getOnlineTime() + 1);
                    } else {
                        agentRunState.setDownTime(agentRunState.getDownTime() + 1);
                    }
                    agentRunState.setLastTime(systemInfo.getCreateTime());
                    isSaved = true;
                    agentRunStateUpdateList.add(agentRunState);
                    break;
                }
                if (isSaved) continue;
                AgentRunState agentRunState = new AgentRunState();
                if ("1".equals(systemInfo.getState())) {
                    agentRunState.setOnlineTime(1);
                    agentRunState.setDownTime(0);
                } else {
                    agentRunState.setOnlineTime(0);
                    agentRunState.setDownTime(1);
                }
                agentRunState.setHostname(systemInfo.getHostname());
                agentRunState.setLastTime(systemInfo.getCreateTime());
                agentRunState.setCreateTime(nowDate);
                agentRunStateAddList.add(agentRunState);
            }
            this.agentRunStateService.saveRecord(agentRunStateAddList);
            this.agentRunStateService.updateRecord(agentRunStateUpdateList);
        }
        catch (Exception e) {
            this.logger.error("\u8ba1\u7b97\u6bcf\u4e2aagent\u7ec8\u7aef\u7684\u7d2f\u8ba1\u8fd0\u884c\u65f6\u95f4\u9519\u8bef", (Throwable)e);
        }
    }

    @Scheduled(initialDelay=350000L, fixedRate=60000L)
    public void ufmMonitorTask() {
        if (!"master".equals(this.commonConfig.getNodeType())) {
            return;
        }
        this.logger.info("ufmMonitorTask------------" + DateUtil.getDateTimeString(new Date()));
        try {
            java.util.List<com.wgcloud.entity.UfmMonitor> list = com.wgcloud.util.UfmUtil.collectAllData();
            if (list != null && !list.isEmpty()) {
                for (com.wgcloud.entity.UfmMonitor monitor : list) {
                    this.ufmMonitorService.deleteByGuid(monitor.getGuid());
                    this.ufmMonitorService.save(monitor);
                }
                this.logger.info("ufmMonitorTask\u91c7\u96c6\u5b8c\u6210\uff0c\u5171" + list.size() + "\u53f0\u8bbe\u5907");
            }
        }
        catch (Exception e) {
            this.logger.error("UFM\u76d1\u63a7\u6570\u636e\u91c7\u96c6\u4efb\u52a1\u9519\u8bef", (Throwable)e);
        }
    }
}

