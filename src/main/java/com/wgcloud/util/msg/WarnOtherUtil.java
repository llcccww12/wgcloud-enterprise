/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.util.msg;

import com.sun.mail.util.MailSSLSocketFactory;
import com.wgcloud.common.ApplicationContextHelper;
import com.wgcloud.config.CommonConfig;
import com.wgcloud.config.LevelConfig;
import com.wgcloud.config.MailConfig;
import com.wgcloud.entity.DbInfo;
import com.wgcloud.entity.DbTable;
import com.wgcloud.entity.DceInfo;
import com.wgcloud.entity.FtpInfo;
import com.wgcloud.entity.HeathMonitor;
import com.wgcloud.entity.HostWarnDiy;
import com.wgcloud.entity.MailSet;
import com.wgcloud.entity.ShellInfo;
import com.wgcloud.entity.SnmpDeepInfo;
import com.wgcloud.entity.SnmpInfo;
import com.wgcloud.service.HostGroupService;
import com.wgcloud.service.LogInfoService;
import com.wgcloud.util.DateUtil;
import com.wgcloud.util.ExecUtil;
import com.wgcloud.util.FormatUtil;
import com.wgcloud.util.HostUtil;
import com.wgcloud.util.msg.WarnMailUtil;
import com.wgcloud.util.msg.WarnPools;
import com.wgcloud.util.staticvar.StaticKeys;
import java.util.Date;
import java.util.Properties;
import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.HtmlEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WarnOtherUtil {
    private static final Logger logger = LoggerFactory.getLogger(WarnMailUtil.class);
    private static CommonConfig commonConfig = ApplicationContextHelper.getBean(CommonConfig.class);
    private static LogInfoService logInfoService = ApplicationContextHelper.getBean(LogInfoService.class);
    private static MailConfig mailConfig = ApplicationContextHelper.getBean(MailConfig.class);
    private static LevelConfig levelConfig = ApplicationContextHelper.getBean(LevelConfig.class);
    private static HostGroupService hostGroupService = ApplicationContextHelper.getBean(HostGroupService.class);

    public static boolean sendHeathInfo(HeathMonitor heathMonitor, boolean isDown) {
        if ("false".equals(mailConfig.getAllWarnMail()) || "false".equals(mailConfig.getHeathWarnMail())) {
            return false;
        }
        String key = heathMonitor.getId();
        if (isDown) {
            if (WarnPools.checkWarnCacheTimes(key)) {
                return false;
            }
            try {
                if (WarnPools.HEATH_WARN_COUNT_MAP.get(key) == null) {
                    WarnPools.HEATH_WARN_COUNT_MAP.put(key, 1);
                } else {
                    WarnPools.HEATH_WARN_COUNT_MAP.put(key, ((Integer)WarnPools.HEATH_WARN_COUNT_MAP.get(key) + 1));
                }
                if ((Integer)WarnPools.HEATH_WARN_COUNT_MAP.get(key) < mailConfig.getHeathWarnCount()) {
                    logger.info(heathMonitor.getAppName() + "---\u670d\u52a1\u63a5\u53e3\u6ca1\u6709\u8fbe\u5230\u544a\u8b66\u6b21\u6570---" + WarnPools.HEATH_WARN_COUNT_MAP.get(key));
                    return false;
                }
                String testErrorMsg = "";
                if (!StringUtils.isEmpty((CharSequence)heathMonitor.getErrorMsg())) {
                    testErrorMsg = "\uff0c" + heathMonitor.getErrorMsg();
                }
                String title = "\u670d\u52a1\u63a5\u53e3\u68c0\u6d4b\u544a\u8b66\uff1a" + heathMonitor.getAppName();
                String commContent = "\u670d\u52a1\u63a5\u53e3\u5df2\u8fde\u7eed" + mailConfig.getHeathWarnCount() + "\u6b21\u68c0\u6d4b\u5931\u8d25\uff1a" + heathMonitor.getAppName() + "\uff0curl\uff1a" + heathMonitor.getHeathUrl() + "\uff0c\u54cd\u5e94\u72b6\u6001\u7801\u4e3a" + heathMonitor.getHeathStatus() + testErrorMsg;
                WarnOtherUtil.sendUtil(title, commContent, heathMonitor.getAccount(), key, isDown, heathMonitor.getWarnLevel(), heathMonitor.getGroupId());
            }
            catch (Exception e) {
                logger.error("\u53d1\u9001\u670d\u52a1\u63a5\u53e3\u68c0\u6d4b\u544a\u8b66\u90ae\u4ef6\u9519\u8bef", (Throwable)e);
                logInfoService.save("\u53d1\u9001\u670d\u52a1\u63a5\u53e3\u68c0\u6d4b\u544a\u8b66\u90ae\u4ef6\u9519\u8bef", e.toString(), "1");
            }
        } else {
            try {
                String title = "\u670d\u52a1\u63a5\u53e3\u5df2\u6062\u590d\uff1a" + heathMonitor.getAppName();
                String commContent = "\u670d\u52a1\u63a5\u53e3\u5df2\u6062\u590d\uff1a" + heathMonitor.getAppName() + "\uff0curl\uff1a" + heathMonitor.getHeathUrl() + "\uff0c\u54cd\u5e94\u72b6\u6001\u7801\u4e3a" + heathMonitor.getHeathStatus() + "";
                WarnOtherUtil.sendUtil(title, commContent, heathMonitor.getAccount(), key, isDown, heathMonitor.getWarnLevel(), heathMonitor.getGroupId());
            }
            catch (Exception e) {
                logger.error("\u53d1\u9001\u670d\u52a1\u63a5\u53e3\u5df2\u6062\u590d\u90ae\u4ef6\u9519\u8bef\uff1a", (Throwable)e);
                logInfoService.save("\u53d1\u9001\u670d\u52a1\u63a5\u53e3\u5df2\u6062\u590d\u90ae\u4ef6\u9519\u8bef", e.toString(), "1");
            }
        }
        return false;
    }

    public static boolean sendDceInfo(DceInfo dceInfo, boolean isDown) {
        if ("false".equals(mailConfig.getAllWarnMail()) || "false".equals(mailConfig.getDceWarnMail())) {
            return false;
        }
        String key = dceInfo.getId();
        String remark = dceInfo.getRemark();
        remark = StringUtils.isEmpty((CharSequence)remark) ? "" : "(" + dceInfo.getRemark() + ")";
        if (isDown) {
            if (WarnPools.checkWarnCacheTimes(key)) {
                return false;
            }
            try {
                if (WarnPools.PING_WARN_COUNT_MAP.get(key) == null) {
                    WarnPools.PING_WARN_COUNT_MAP.put(key, 1);
                } else {
                    WarnPools.PING_WARN_COUNT_MAP.put(key, ((Integer)WarnPools.PING_WARN_COUNT_MAP.get(key) + 1));
                }
                if ((Integer)WarnPools.PING_WARN_COUNT_MAP.get(key) < mailConfig.getDceWarnCount()) {
                    logger.info(dceInfo.getHostname() + "---\u7f51\u7edc\u8bbe\u5907PING\u8d85\u65f6\u6ca1\u6709\u8fbe\u5230\u544a\u8b66\u6b21\u6570---" + WarnPools.PING_WARN_COUNT_MAP.get(key));
                    return false;
                }
                String title = "PING\u8d85\u65f6\u544a\u8b66\uff1a" + dceInfo.getHostname() + remark;
                String commContent = "\u7f51\u7edc\u8bbe\u5907\u5df2\u8fde\u7eed" + mailConfig.getDceWarnCount() + "\u6b21PING\u68c0\u6d4b\u8d85\u65f6\u6216\u5931\u8d25\uff1a" + dceInfo.getHostname() + remark;
                WarnOtherUtil.sendUtil(title, commContent, dceInfo.getAccount(), key, isDown, dceInfo.getWarnLevel(), dceInfo.getGroupId());
            }
            catch (Exception e) {
                logger.error("\u53d1\u9001\u7f51\u7edc\u8bbe\u5907PING\u544a\u8b66\u90ae\u4ef6\u9519\u8bef", (Throwable)e);
                logInfoService.save("\u53d1\u9001\u7f51\u7edc\u8bbe\u5907PING\u544a\u8b66\u90ae\u4ef6\u9519\u8bef", e.toString(), "1");
            }
        } else {
            try {
                String title = "PING\u5df2\u6062\u590d\uff1a" + dceInfo.getHostname() + remark;
                String commContent = "\u7f51\u7edc\u8bbe\u5907PING\u5df2\u6062\u590d\uff1a" + dceInfo.getHostname() + remark + "\uff0c\u54cd\u5e94\u65f6\u95f4ms\uff1a" + dceInfo.getResTimes();
                WarnOtherUtil.sendUtil(title, commContent, dceInfo.getAccount(), key, isDown, dceInfo.getWarnLevel(), dceInfo.getGroupId());
            }
            catch (Exception e) {
                logger.error("\u53d1\u9001\u7f51\u7edc\u8bbe\u5907PING\u5df2\u6062\u590d\u90ae\u4ef6\u9519\u8bef", (Throwable)e);
                logInfoService.save("\u53d1\u9001\u7f51\u7edc\u8bbe\u5907PING\u5df2\u6062\u590d\u90ae\u4ef6\u9519\u8bef", e.toString(), "1");
            }
        }
        return false;
    }

    public static boolean sendSnmpInfo(SnmpInfo snmpInfo, boolean isDown) {
        if ("false".equals(mailConfig.getAllWarnMail()) || "false".equals(mailConfig.getSnmpWarnMail())) {
            return false;
        }
        String key = snmpInfo.getId();
        String remark = snmpInfo.getRemark();
        remark = StringUtils.isEmpty((CharSequence)remark) ? "" : "(" + snmpInfo.getRemark() + ")";
        if (isDown) {
            if (WarnPools.checkWarnCacheTimes(key)) {
                return false;
            }
            try {
                if (WarnPools.SNMP_WARN_COUNT_MAP.get(key) == null) {
                    WarnPools.SNMP_WARN_COUNT_MAP.put(key, 1);
                } else {
                    WarnPools.SNMP_WARN_COUNT_MAP.put(key, ((Integer)WarnPools.SNMP_WARN_COUNT_MAP.get(key) + 1));
                }
                if ((Integer)WarnPools.SNMP_WARN_COUNT_MAP.get(key) < mailConfig.getSnmpWarnCount()) {
                    logger.info(snmpInfo.getHostname() + "---\u7f51\u7edc\u8bbe\u5907SNMP\u8d85\u65f6\u6ca1\u6709\u8fbe\u5230\u544a\u8b66\u6b21\u6570---" + WarnPools.SNMP_WARN_COUNT_MAP.get(key));
                    return false;
                }
                String title = "SNMP\u8bbe\u5907\u76d1\u6d4b\u544a\u8b66\uff1a" + snmpInfo.getHostname() + remark;
                String commContent = "SNMP\u8bbe\u5907\uff1a" + snmpInfo.getHostname() + remark + "\uff0c\u5df2\u8fde\u7eed" + mailConfig.getSnmpWarnCount() + "\u6b21PING\u68c0\u6d4b\u8d85\u65f6\u6216\u5931\u8d25";
                WarnOtherUtil.sendUtil(title, commContent, snmpInfo.getAccount(), key, isDown, snmpInfo.getWarnLevel(), snmpInfo.getGroupId());
            }
            catch (Exception e) {
                logger.error("\u53d1\u9001snmp\u8bbe\u5907\u76d1\u6d4b\u544a\u8b66\u90ae\u4ef6\u9519\u8bef", (Throwable)e);
                logInfoService.save("\u53d1\u9001snmp\u8bbe\u5907\u76d1\u6d4b\u544a\u8b66\u90ae\u4ef6\u9519\u8bef", e.toString(), "1");
            }
        } else {
            try {
                String title = "SNMP\u8bbe\u5907\u76d1\u6d4b\u5df2\u6062\u590d\uff1a" + snmpInfo.getHostname() + remark;
                String commContent = "SNMP\u8bbe\u5907\u76d1\u6d4b\u5df2\u6062\u590d\uff1a" + snmpInfo.getHostname() + remark;
                WarnOtherUtil.sendUtil(title, commContent, snmpInfo.getAccount(), key, isDown, snmpInfo.getWarnLevel(), snmpInfo.getGroupId());
            }
            catch (Exception e) {
                logger.error("\u53d1\u9001snmp\u8bbe\u5907\u76d1\u6d4b\u5df2\u6062\u590d\u90ae\u4ef6\u9519\u8bef", (Throwable)e);
                logInfoService.save("\u53d1\u9001snmp\u8bbe\u5907\u76d1\u6d4b\u5df2\u6062\u590d\u90ae\u4ef6\u9519\u8bef", e.toString(), "1");
            }
        }
        return false;
    }

    public static boolean sendSnmpDeepInfo(SnmpDeepInfo snmpInfo, boolean isDown) {
        if ("false".equals(mailConfig.getAllWarnMail()) || "false".equals(mailConfig.getSnmpWarnMail())) {
            return false;
        }
        String key = snmpInfo.getId();
        String remark = snmpInfo.getRemark();
        remark = StringUtils.isEmpty((CharSequence)remark) ? "" : "(" + snmpInfo.getRemark() + ")";
        if (isDown) {
            if (WarnPools.checkWarnCacheTimes(key)) {
                return false;
            }
            try {
                if (WarnPools.SNMP_WARN_COUNT_MAP.get(key) == null) {
                    WarnPools.SNMP_WARN_COUNT_MAP.put(key, 1);
                } else {
                    WarnPools.SNMP_WARN_COUNT_MAP.put(key, ((Integer)WarnPools.SNMP_WARN_COUNT_MAP.get(key) + 1));
                }
                if ((Integer)WarnPools.SNMP_WARN_COUNT_MAP.get(key) < mailConfig.getSnmpWarnCount()) {
                    logger.info(snmpInfo.getHostname() + "---\u7f51\u7edc\u8bbe\u5907SNMP\u6df1\u5ea6\u76d1\u63a7\u8d85\u65f6\u6ca1\u6709\u8fbe\u5230\u544a\u8b66\u6b21\u6570---" + WarnPools.SNMP_WARN_COUNT_MAP.get(key));
                    return false;
                }
                String title = "SNMP\u8bbe\u5907\u6df1\u5ea6\u76d1\u6d4b\u544a\u8b66\uff1a" + snmpInfo.getHostname() + remark;
                String commContent = "SNMP\u8bbe\u5907\u6df1\u5ea6\u76d1\u6d4b\uff1a" + snmpInfo.getHostname() + remark + "\uff0c\u5df2\u79bb\u7ebf\uff0c\u5df2\u8fde\u7eed" + mailConfig.getSnmpWarnCount() + "\u6b21PING\u68c0\u6d4b\u8d85\u65f6\u6216\u5931\u8d25";
                WarnOtherUtil.sendUtil(title, commContent, snmpInfo.getAccount(), key, isDown, snmpInfo.getWarnLevel(), snmpInfo.getGroupId());
            }
            catch (Exception e) {
                logger.error("\u53d1\u9001snmp\u8bbe\u5907\u6df1\u5ea6\u76d1\u6d4b\u544a\u8b66\u90ae\u4ef6\u9519\u8bef", (Throwable)e);
                logInfoService.save("\u53d1\u9001snmp\u8bbe\u5907\u6df1\u5ea6\u76d1\u6d4b\u544a\u8b66\u90ae\u4ef6\u9519\u8bef", e.toString(), "1");
            }
        } else {
            try {
                String title = "SNMP\u8bbe\u5907\u6df1\u5ea6\u76d1\u6d4b\u5df2\u6062\u590d\uff1a" + snmpInfo.getHostname() + remark;
                String commContent = "SNMP\u8bbe\u5907\u6df1\u5ea6\u76d1\u6d4b\u5df2\u6062\u590d\uff1a" + snmpInfo.getHostname() + remark;
                WarnOtherUtil.sendUtil(title, commContent, snmpInfo.getAccount(), key, isDown, snmpInfo.getWarnLevel(), snmpInfo.getGroupId());
            }
            catch (Exception e) {
                logger.error("\u53d1\u9001snmp\u8bbe\u5907\u6df1\u5ea6\u76d1\u6d4b\u5df2\u6062\u590d\u90ae\u4ef6\u9519\u8bef", (Throwable)e);
                logInfoService.save("\u53d1\u9001snmp\u8bbe\u5907\u6df1\u5ea6\u76d1\u6d4b\u5df2\u6062\u590d\u90ae\u4ef6\u9519\u8bef", e.toString(), "1");
            }
        }
        return false;
    }

    public static boolean sendDbDown(DbInfo dbInfo, boolean isDown) {
        if ("false".equals(mailConfig.getAllWarnMail()) || "false".equals(mailConfig.getDbDownWarnMail())) {
            return false;
        }
        String key = dbInfo.getId();
        if (isDown) {
            if (WarnPools.checkWarnCacheTimes(key)) {
                return false;
            }
            try {
                String title = "\u6570\u636e\u5e93\u8fde\u63a5\u5931\u8d25\u544a\u8b66\uff1a" + dbInfo.getAliasName();
                String commContent = "\u6570\u636e\u5e93\u8fde\u63a5\u5931\u8d25\uff1a" + dbInfo.getAliasName();
                WarnOtherUtil.sendUtil(title, commContent, dbInfo.getAccount(), key, isDown, dbInfo.getWarnLevel(), dbInfo.getGroupId());
            }
            catch (Exception e) {
                logger.error("\u53d1\u9001\u6570\u636e\u5e93\u8fde\u63a5\u5931\u8d25\u90ae\u4ef6\u9519\u8bef", (Throwable)e);
                logInfoService.save("\u53d1\u9001\u6570\u636e\u5e93\u8fde\u63a5\u5931\u8d25\u544a\u8b66\u9519\u8bef", e.toString(), "1");
            }
        } else if (!StringUtils.isEmpty((CharSequence)WarnPools.getWarnMark(key))) {
            WarnPools.removeWarnMark(key);
            try {
                String title = "\u6570\u636e\u5e93\u5df2\u6062\u590d\u4e0a\u7ebf\uff1a" + dbInfo.getAliasName();
                String commContent = "\u6570\u636e\u5e93\u5df2\u6062\u590d\u4e0a\u7ebf\uff1a" + dbInfo.getAliasName() + "\uff0c\u8fde\u63a5\u7528\u65f6" + dbInfo.getResTimes() + "ms";
                WarnOtherUtil.sendUtil(title, commContent, dbInfo.getAccount(), key, isDown, dbInfo.getWarnLevel(), dbInfo.getGroupId());
            }
            catch (Exception e) {
                logger.error("\u53d1\u9001\u6570\u636e\u5e93\u5df2\u6062\u590d\u4e0a\u7ebf\u90ae\u4ef6\u9519\u8bef", (Throwable)e);
                logInfoService.save("\u53d1\u9001\u6570\u636e\u5e93\u5df2\u6062\u590d\u4e0a\u7ebf\u9519\u8bef", e.toString(), "1");
            }
        }
        return false;
    }

    public static boolean sendDbTableDown(DbTable dbTable, String account, Boolean result) {
        if ("false".equals(mailConfig.getAllWarnMail()) || "false".equals(mailConfig.getDbDownWarnMail())) {
            return false;
        }
        String key = dbTable.getId();
        try {
            if (!result.booleanValue() && !StringUtils.isEmpty((CharSequence)WarnPools.getWarnMark(key))) {
                String title = "\u6570\u636e\u8868\u5df2\u6062\u590d\uff1a" + dbTable.getRemark();
                String commContent = "\u6570\u636e\u8868\u5df2\u6062\u590d\uff1a" + dbTable.getRemark() + "\uff0c\u544a\u8b66\u8868\u8fbe\u5f0f\u4e0d\u6210\u7acb\uff1a" + dbTable.getResultExp() + "\uff0cresult\u5f53\u524d\u503c\u4e3a\uff1a" + dbTable.getTableCount() + "\uff0cSQL\u6267\u884c\u65f6\u95f4\u4e3a" + dbTable.getResTimes() + "ms";
                WarnOtherUtil.sendUtil(title, commContent, account, key, false, dbTable.getWarnLevel(), "");
                return false;
            }
            if (WarnPools.checkWarnCacheTimes(key)) {
                return false;
            }
            if (result.booleanValue()) {
                String testErrorMsg = "";
                if (!StringUtils.isEmpty((CharSequence)dbTable.getTestErrorMsg())) {
                    testErrorMsg = "\uff0c" + dbTable.getTestErrorMsg();
                }
                String title = "\u6570\u636e\u8868\u544a\u8b66\uff1a" + dbTable.getRemark();
                String commContent = "\u6570\u636e\u8868\u544a\u8b66\uff1a" + dbTable.getRemark() + "\uff0c\u544a\u8b66\u8868\u8fbe\u5f0f\u6210\u7acb\uff1a" + dbTable.getResultExp() + "\uff0cresult\u5f53\u524d\u503c\u4e3a\uff1a" + dbTable.getTableCount() + "\uff0cSQL\u6267\u884c\u65f6\u95f4\u4e3a" + dbTable.getResTimes() + "ms" + testErrorMsg;
                WarnOtherUtil.sendUtil(title, commContent, account, key, true, dbTable.getWarnLevel(), "");
            }
        }
        catch (Exception e) {
            logger.error("\u53d1\u9001\u6570\u636e\u8868\u544a\u8b66\u90ae\u4ef6\u9519\u8bef", (Throwable)e);
            logInfoService.save("\u53d1\u9001\u6570\u636e\u8868\u544a\u8b66\u90ae\u4ef6\u9519\u8bef", e.toString(), "1");
        }
        return false;
    }

    public static boolean sendShellInfo(ShellInfo shellInfo, String titlePrefix, HttpServletRequest request) {
        String continueDays;
        if ("false".equals(mailConfig.getAllWarnMail()) || "false".equals(mailConfig.getShellWarnMail())) {
            return false;
        }
        String suffixStr = "";
        if (null != request && !StringUtils.isEmpty((CharSequence)(continueDays = request.getParameter("continueDays")))) {
            suffixStr = "\uff0c\u6307\u4ee4\u5c06\u8fde\u7eed" + continueDays + "\u5929\u5728\u6b64\u65f6\u95f4\u70b9\u6267\u884c";
        }
        try {
            String blockKey;
            String shell = shellInfo.getShell();
            if (!StringUtils.isEmpty((CharSequence)shell) && !StringUtils.isEmpty((CharSequence)(blockKey = FormatUtil.haveBlockDanger(shell = shell.replaceAll("\\r\\n", " && "), commonConfig.getShellToRunBlock())))) {
                shell = shell + "\uff0c\u4e0b\u53d1\u6307\u4ee4\u542b\u6709\u654f\u611f\u5b57\u7b26" + blockKey + "\u4e0d\u8fdb\u884c\u4e0b\u53d1";
            }
            String shellTime = "\u7acb\u5373\u4e0b\u53d1";
            if ("2".equals(shellInfo.getShellType())) {
                shellTime = shellInfo.getShellTime();
            }
            String title = titlePrefix + "\uff1a" + shellInfo.getShellName();
            String commContent = titlePrefix + "\uff1a" + shellInfo.getShellName() + "\uff0c\u4e0b\u53d1\u65f6\u95f4\uff1a" + shellTime + "\uff0c\u6307\u4ee4\u5185\u5bb9\uff1a" + shell + suffixStr;
            WarnOtherUtil.sendUtil(title, commContent, shellInfo.getAccount(), "", false, levelConfig.getShellWarn(), "");
        }
        catch (Exception e) {
            logger.error("\u4e0b\u53d1\u6307\u4ee4\u4fe1\u606f\u90ae\u4ef6\u9519\u8bef", (Throwable)e);
            logInfoService.save("\u4e0b\u53d1\u6307\u4ee4\u4fe1\u606f\u9519\u8bef", e.toString(), "2");
        }
        return false;
    }

    public static boolean sendLastlogWarnInfo(String lastlogWarnInfos, String bindIp) {
        if ("false".equals(mailConfig.getAllWarnMail()) || "false".equals(mailConfig.getHostLoginWarnMail())) {
            return false;
        }
        String warnLevel = levelConfig.getHostLoginWarn();
        HostWarnDiy hostWarnDiyDto = StaticKeys.HOST_WARN_MAP.get(bindIp);
        if (null != hostWarnDiyDto && "1".equals(hostWarnDiyDto.getActive())) {
            if ("no".equals(hostWarnDiyDto.getHostLoginWarnMail()) || "yes".equals(hostWarnDiyDto.getHostBlockAllWarn())) {
                return false;
            }
            if (!StringUtils.isEmpty((CharSequence)hostWarnDiyDto.getHostLoginWarnLevel())) {
                warnLevel = hostWarnDiyDto.getHostLoginWarnLevel();
            }
        }
        try {
            String remark = HostUtil.addRemark(bindIp);
            String title = "\u4e3b\u673a\u767b\u5f55\u4fe1\u606f\u63d0\u9192\uff1a" + bindIp + remark;
            String commContent = "\u4e3b\u673a\u767b\u5f55\u4fe1\u606f\u63d0\u9192\uff1a" + bindIp + remark + "\uff0c" + lastlogWarnInfos;
            WarnOtherUtil.sendUtil(title, commContent, WarnMailUtil.getAccount(bindIp), "", false, warnLevel, WarnMailUtil.getHostGroups(bindIp, ""));
        }
        catch (Exception e) {
            logger.error("\u4e3b\u673a\u767b\u5f55\u4fe1\u606f\u63d0\u9192\u90ae\u4ef6\u9519\u8bef", (Throwable)e);
            logInfoService.save("\u4e3b\u673a\u767b\u5f55\u4fe1\u606f\u63d0\u9192\u9519\u8bef", e.toString(), "2");
        }
        return false;
    }

    public static boolean sendFtpInfo(FtpInfo ftpInfo, boolean isDown) {
        if ("false".equals(mailConfig.getAllWarnMail()) || "false".equals(mailConfig.getFtpWarnMail())) {
            return false;
        }
        String key = ftpInfo.getId();
        if (isDown) {
            if (WarnPools.checkWarnCacheTimes(key)) {
                return false;
            }
            try {
                String title = "FTP\u68c0\u6d4b\u544a\u8b66\uff1a" + ftpInfo.getFtpName();
                String commContent = "FTP\u670d\u52a1\u8fde\u63a5\u5931\u8d25\uff1a" + ftpInfo.getFtpName() + "\uff0c" + ftpInfo.getFtpHost() + ":" + ftpInfo.getPort();
                WarnOtherUtil.sendUtil(title, commContent, ftpInfo.getAccount(), key, isDown, ftpInfo.getWarnLevel(), ftpInfo.getGroupId());
            }
            catch (Exception e) {
                logger.error("\u53d1\u9001FTP\u68c0\u6d4b\u544a\u8b66\u90ae\u4ef6\u9519\u8bef", (Throwable)e);
                logInfoService.save("\u53d1\u9001FTP\u68c0\u6d4b\u544a\u8b66\u90ae\u4ef6\u9519\u8bef", e.toString(), "1");
            }
        } else {
            try {
                String title = "FTP\u68c0\u6d4b\u5df2\u6062\u590d\uff1a" + ftpInfo.getFtpName();
                String commContent = "FTP\u670d\u52a1\u8fde\u63a5\u5df2\u6062\u590d\uff1a" + ftpInfo.getFtpName() + "\uff0c" + ftpInfo.getFtpHost() + ":" + ftpInfo.getPort() + "\uff0c\u54cd\u5e94\u65f6\u95f4\u4e3a" + ftpInfo.getResTimes() + "ms";
                WarnOtherUtil.sendUtil(title, commContent, ftpInfo.getAccount(), key, isDown, ftpInfo.getWarnLevel(), ftpInfo.getGroupId());
            }
            catch (Exception e) {
                logger.error("\u53d1\u9001FTP\u5df2\u6062\u590d\u90ae\u4ef6\u9519\u8bef", (Throwable)e);
                logInfoService.save("\u53d1\u9001FTP\u53e3\u5df2\u6062\u590d\u90ae\u4ef6\u9519\u8bef", e.toString(), "1");
            }
        }
        return false;
    }

    public static boolean sendMiddlewareInfo(String key, boolean isDown) {
        if ("false".equals(mailConfig.getAllWarnMail()) || "false".equals(mailConfig.getMiddlewareWarnMail())) {
            return false;
        }
        String account = "";
        String warnLevel = "ERROR";
        String groupIds = "";
        if (isDown) {
            if (WarnPools.checkWarnCacheTimes(key)) {
                return false;
            }
            try {
                String title = "\u4e2d\u95f4\u4ef6\u8fde\u63a5\u5931\u8d25\u544a\u8b66: " + key;
                String commContent = "\u4e2d\u95f4\u4ef6\u8fde\u63a5\u5931\u8d25\u544a\u8b66: " + key;
                WarnOtherUtil.sendUtil(title, commContent, account, key, isDown, warnLevel, groupIds);
            }
            catch (Exception e) {
                logger.error("\u53d1\u9001\u4e2d\u95f4\u4ef6\u8fde\u63a5\u5931\u8d25\u544a\u8b66\u90ae\u4ef6\u9519\u8bef", (Throwable)e);
                logInfoService.save("\u53d1\u9001\u4e2d\u95f4\u4ef6\u8fde\u63a5\u5931\u8d25\u544a\u8b66\u90ae\u4ef6\u9519\u8bef", e.toString(), "1");
            }
        } else {
            try {
                if (!StringUtils.isEmpty((CharSequence)WarnPools.getWarnMark(key))) {
                    String title = "\u4e2d\u95f4\u4ef6\u8fde\u63a5\u5df2\u6062\u590d\uff1a" + key;
                    String commContent = "\u4e2d\u95f4\u4ef6\u8fde\u63a5\u5df2\u6062\u590d\uff1a" + key;
                    WarnOtherUtil.sendUtil(title, commContent, account, key, isDown, warnLevel, groupIds);
                }
            }
            catch (Exception e) {
                logger.error("\u53d1\u9001\u4e2d\u95f4\u4ef6\u8fde\u63a5\u5931\u8d25\u544a\u8b66\u90ae\u4ef6\u9519\u8bef", (Throwable)e);
                logInfoService.save("\u53d1\u9001\u4e2d\u95f4\u4ef6\u8fde\u63a5\u5931\u8d25\u544a\u8b66\u90ae\u4ef6\u9519\u8bef", e.toString(), "1");
            }
        }
        return false;
    }

    private static boolean sendUtilCheck() {
        if (!StaticKeys.WARN_CRON_TIME_SIGN) {
            logger.info("\u5f53\u524d\u4e0d\u5728\u544a\u8b66\u65f6\u95f4\u6bb5\u5185\uff0c\u4e0d\u53d1\u544a\u8b66\u901a\u77e5");
            return false;
        }
        if (!StaticKeys.WARN_LICENSE_CHECK_SIGN) {
            String cacheKey = "WARN_LICENSE_CHECK_SIGN_TRUE";
            if (!WarnPools.checkWarnCacheTimes(cacheKey)) {
                String licenseErrorMsg = "\u5f53\u524d\u76d1\u63a7\u4e3b\u673a\u6570\u91cf\u5df2\u8d85\u8fc7\u5141\u8bb8\u76d1\u63a7\u7684\u6570\u91cf\uff0c\u7cfb\u7edf\u5df2\u505c\u6b62\u53d1\u9001\u544a\u8b66\uff0c\u8bf7\u9002\u5f53\u51cf\u5c11\u4e3b\u673a\u6570\u91cf\u6216\u8005\u5347\u7ea7\u6388\u6743";
                logger.info(licenseErrorMsg);
                logInfoService.save(licenseErrorMsg, licenseErrorMsg, "2");
                WarnPools.addWarnMark(cacheKey);
            }
            return false;
        }
        return true;
    }

    public static void sendUtil(String title, String commContent, String account, String key, boolean isDown, String warnLevel, String groupIds) {
        if (!WarnOtherUtil.sendUtilCheck()) {
            return;
        }
        WarnOtherUtil.saveToLog(title, commContent, warnLevel);
        if (!WarnOtherUtil.isSendByWarnLevel(warnLevel)) {
            return;
        }
        if (!StringUtils.isEmpty((CharSequence)title) && title.indexOf("\u544a\u8b66") > -1) {
            WarnPools.WARN_COUNT_LIST.add(1);
        }
        String warnLevelName = WarnOtherUtil.getWarnLevelName(warnLevel);
        String groupsName = WarnOtherUtil.setGroupInList(groupIds);
        MailSet mailSet = StaticKeys.mailSet;
        String mailContentPrefix = WarnOtherUtil.getMailContentPrefix(warnLevelName, groupsName);
        if (StringUtils.isEmpty((CharSequence)account) || !"true".equals(commonConfig.getUserInfoManage()) || !StaticKeys.LICENSE_STATE.equals("1")) {
            if (StaticKeys.mailSet != null) {
                WarnOtherUtil.sendMail(mailSet.getToMail(), title, mailContentPrefix + commContent);
            }
            ExecUtil.runScript(commContent, "", warnLevelName, groupsName, title);
        } else if ("true".equals(commonConfig.getUserInfoManage())) {
            String accountMail = "";
            String accountKey = "";
            if (null != StaticKeys.ACCOUNT_INFO_MAP.get(account)) {
                accountMail = StaticKeys.ACCOUNT_INFO_MAP.get(account).getEmail();
                accountKey = StaticKeys.ACCOUNT_INFO_MAP.get(account).getAccountKey();
            }
            if (StaticKeys.mailSet != null) {
                String addMail = (StringUtils.isEmpty((CharSequence)accountMail) ? "" : ";") + accountMail;
                WarnOtherUtil.sendMail(mailSet.getToMail() + addMail, title, mailContentPrefix + commContent);
            }
            ExecUtil.runScript(commContent, accountKey, warnLevelName, groupsName, title);
        }
        if (!StringUtils.isEmpty((CharSequence)key)) {
            if (isDown) {
                WarnPools.addWarnMark(key);
            } else {
                WarnPools.removeWarnMark(key);
            }
        }
    }

    public static void sendUtilToCustomMail(String title, String commContent, String customMail, String accountKey, String key, boolean isDown, String warnLevel, String groupIds) {
        if (!WarnOtherUtil.sendUtilCheck()) {
            return;
        }
        WarnOtherUtil.saveToLog(title, commContent, warnLevel);
        if (!WarnOtherUtil.isSendByWarnLevel(warnLevel)) {
            return;
        }
        if (!StringUtils.isEmpty((CharSequence)title) && title.indexOf("\u544a\u8b66") > -1) {
            WarnPools.WARN_COUNT_LIST.add(1);
        }
        String warnLevelName = WarnOtherUtil.getWarnLevelName(warnLevel);
        String groupsName = WarnOtherUtil.setGroupInList(groupIds);
        String mailContentPrefix = WarnOtherUtil.getMailContentPrefix(warnLevelName, groupsName);
        if (StaticKeys.mailSet != null) {
            if (!StringUtils.isEmpty((CharSequence)customMail)) {
                WarnOtherUtil.sendMail(customMail, title, mailContentPrefix + commContent);
            } else {
                WarnOtherUtil.sendMail(StaticKeys.mailSet.getToMail(), title, mailContentPrefix + commContent);
            }
        }
        ExecUtil.runScript(commContent, accountKey, warnLevelName, groupsName, title);
        if (!StringUtils.isEmpty((CharSequence)key)) {
            if (isDown) {
                WarnPools.addWarnMark(key);
            } else {
                WarnPools.removeWarnMark(key);
            }
        }
    }

    private static void saveToLog(String title, String commContent, String warnLevel) {
        if (StringUtils.isEmpty((CharSequence)title)) {
            return;
        }
        String warnLevelName = "";
        if (!StringUtils.isEmpty((CharSequence)warnLevel)) {
            warnLevelName = "\u3010" + WarnOtherUtil.getWarnLevelName(warnLevel) + "\u3011";
        }
        if (title.startsWith("\u3010\u7b2c\u4e09\u65b9\u544a\u8b66\u3011")) {
            logInfoService.save(title, commContent, "4");
            return;
        }
        if (title.indexOf("\u5df2\u6062\u590d") > -1) {
            logInfoService.save(warnLevelName + title, commContent, "3");
        } else {
            logInfoService.save(warnLevelName + title, commContent, "1");
        }
    }

    public static String sendMail(String mails, String mailTitle, String mailContent) {
        if (StringUtils.isEmpty((CharSequence)mails)) {
            return "error";
        }
        if ("2".equals(StaticKeys.mailSet.getActive())) {
            return "error";
        }
        if (mails.startsWith(";")) {
            mails = mails.substring(1);
        }
        if ("true".equals(StaticKeys.mailSet.getJavaxMail())) {
            return WarnOtherUtil.sendMailByJavax(mails, mailTitle, mailContent);
        }
        try {
            String mailTitlePrefix = "[WGCLOUD] ";
            String mailContentSuffix = "<p><p><p><a target='_blank' href='http://www.wgstart.com'>WGCLOUD</a>\u656c\u4e0a";
            if (StaticKeys.LICENSE_STATE.equals("1")) {
                mailTitlePrefix = commonConfig.getMailTitlePrefix();
                mailContentSuffix = commonConfig.getMailContentSuffix();
            }
            HtmlEmail email = new HtmlEmail();
            email.setHostName(StaticKeys.mailSet.getSmtpHost());
            email.setSmtpPort(Integer.valueOf(StaticKeys.mailSet.getSmtpPort()).intValue());
            if ("1".equals(StaticKeys.mailSet.getSmtpSSL())) {
                email.setSSLOnConnect(true);
            }
            email.setAuthenticator((Authenticator)new DefaultAuthenticator(StaticKeys.mailSet.getFromMailName(), StaticKeys.mailSet.getFromPwd()));
            email.setFrom(StaticKeys.mailSet.getFromMailName());
            email.setSubject(mailTitlePrefix + mailTitle);
            email.setCharset("UTF-8");
            email.setHtmlMsg(mailContent + "<p><p><p><p>" + DateUtil.getCurrentDateTime() + mailContentSuffix);
            email.addTo(mails.split(";"));
            email.setSentDate(new Date());
            email.send();
            return "success";
        }
        catch (Exception e) {
            logger.error("\u53d1\u9001\u90ae\u4ef6\u9519\u8bef", (Throwable)e);
            logInfoService.save("\u53d1\u9001\u90ae\u4ef6\u9519\u8bef", e.toString(), "1");
            return "error";
        }
    }

    private static String sendMailByJavax(String mails, String mailTitle, String mailContent) {
        if (StringUtils.isEmpty((CharSequence)mails)) {
            return "error";
        }
        if (mails.startsWith(";")) {
            mails = mails.substring(1);
        }
        try {
            String mailTitlePrefix = "[WGCLOUD] ";
            String mailContentSuffix = "<p><p><p><a target='_blank' href='http://www.wgstart.com'>WGCLOUD</a>\u656c\u4e0a";
            if (StaticKeys.LICENSE_STATE.equals("1")) {
                mailTitlePrefix = commonConfig.getMailTitlePrefix();
                mailContentSuffix = commonConfig.getMailContentSuffix();
            }
            Properties prop = new Properties();
            prop.setProperty("mail.host", StaticKeys.mailSet.getSmtpHost());
            prop.setProperty("mail.transport.protocol", "smtp");
            prop.setProperty("mail.smtp.auth", "true");
            prop.setProperty("mail.smtp.port", StaticKeys.mailSet.getSmtpPort());
            if ("1".equals(StaticKeys.mailSet.getSmtpSSL())) {
                prop.setProperty("mail.smtp.starttls.enable", "true");
            } else {
                prop.setProperty("mail.smtp.starttls.enable", "false");
            }
            MailSSLSocketFactory sf = new MailSSLSocketFactory();
            sf.setTrustAllHosts(true);
            prop.put("mail.smtp.ssl.socketFactory", sf);
            prop.put("mail.smtp.ssl.protocols", "TLSv1.2");
            Session session = Session.getInstance((Properties)prop, null);
            session.setDebug(false);
            Transport ts = session.getTransport();
            ts.connect(StaticKeys.mailSet.getSmtpHost(), StaticKeys.mailSet.getFromMailName(), StaticKeys.mailSet.getFromPwd());
            MimeMessage message = new MimeMessage(session);
            message.setFrom((Address)new InternetAddress(StaticKeys.mailSet.getFromMailName()));
            String[] mailArr = mails.split(";");
            InternetAddress[] internetAddressArr = new InternetAddress[mailArr.length];
            for (int i = 0; i < mailArr.length; ++i) {
                internetAddressArr[i] = new InternetAddress(mailArr[i]);
            }
            message.setRecipients(Message.RecipientType.TO, (Address[])internetAddressArr);
            message.setSubject(mailTitlePrefix + mailTitle);
            message.setContent((Object)(mailContent + "<p><p><p><p>" + DateUtil.getCurrentDateTime() + mailContentSuffix), "text/html;charset=UTF-8");
            message.saveChanges();
            ts.sendMessage((Message)message, message.getAllRecipients());
            ts.close();
            return "success";
        }
        catch (Exception e) {
            logger.error("\u53d1\u9001\u90ae\u4ef6\u9519\u8bef", (Throwable)e);
            logInfoService.save("\u53d1\u9001\u90ae\u4ef6\u9519\u8bef", e.toString(), "1");
            return "error";
        }
    }

    public static boolean isSendByWarnLevel(String warnLevel) {
        if (StringUtils.isEmpty((CharSequence)warnLevel)) {
            return true;
        }
        if ("INFO".equals(levelConfig.getDefaultWarn())) {
            return true;
        }
        if ("ERROR".equals(levelConfig.getDefaultWarn())) {
            return "ERROR".equals(warnLevel);
        }
        if ("WARN".equals(levelConfig.getDefaultWarn())) {
            return "ERROR".equals(warnLevel) || "WARN".equals(warnLevel);
        }
        return true;
    }

    public static String getWarnLevelName(String warnLevel) {
        if (StringUtils.isEmpty((CharSequence)warnLevel)) {
            return "\u91cd\u8981";
        }
        if ("INFO".equals(warnLevel)) {
            return "\u4fe1\u606f";
        }
        if ("WARN".equals(warnLevel)) {
            return "\u4e00\u822c";
        }
        if ("ERROR".equals(warnLevel)) {
            return "\u91cd\u8981";
        }
        return "\u91cd\u8981";
    }

    public static String setGroupInList(String groupIds) {
        return hostGroupService.returnGroupNames(groupIds);
    }

    public static String getMailContentPrefix(String warnLevelName, String groupsName) {
        String mailContentPrefix = "";
        if (StaticKeys.mailSet != null && "true".equals(levelConfig.getAddToWarnContent())) {
            if (!StringUtils.isEmpty((CharSequence)warnLevelName)) {
                mailContentPrefix = mailContentPrefix + "<font style=\"line-height:1.5\">\u544a\u8b66\u7ea7\u522b\uff1a" + warnLevelName + "</font><p>";
            }
            if (!StringUtils.isEmpty((CharSequence)groupsName)) {
                mailContentPrefix = mailContentPrefix + "<font style=\"line-height:1.5\">\u6807\u7b7e\uff1a" + groupsName + "</font><p>";
            }
        }
        return mailContentPrefix;
    }
}

