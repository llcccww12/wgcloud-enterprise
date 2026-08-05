/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.util.msg;

import com.wgcloud.common.ApplicationContextHelper;
import com.wgcloud.config.CommonConfig;
import com.wgcloud.config.LevelConfig;
import com.wgcloud.config.MailConfig;
import com.wgcloud.entity.AppInfo;
import com.wgcloud.entity.CpuState;
import com.wgcloud.entity.CpuTemperatures;
import com.wgcloud.entity.CustomInfo;
import com.wgcloud.entity.DiskIoState;
import com.wgcloud.entity.DiskSmart;
import com.wgcloud.entity.DiskState;
import com.wgcloud.entity.DockerInfo;
import com.wgcloud.entity.FileSafe;
import com.wgcloud.entity.FileWarnInfo;
import com.wgcloud.entity.FileWarnState;
import com.wgcloud.entity.HostWarnDiy;
import com.wgcloud.entity.MemState;
import com.wgcloud.entity.NetIoState;
import com.wgcloud.entity.PortInfo;
import com.wgcloud.entity.SysLoadState;
import com.wgcloud.entity.SystemInfo;
import com.wgcloud.service.LogInfoService;
import com.wgcloud.service.SystemInfoService;
import com.wgcloud.util.FormatUtil;
import com.wgcloud.util.HostUtil;
import com.wgcloud.util.msg.WarnOtherUtil;
import com.wgcloud.util.msg.WarnPools;
import com.wgcloud.util.staticvar.StaticKeys;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.AntPathMatcher;

public class WarnMailUtil {
    private static final Logger logger = LoggerFactory.getLogger(WarnMailUtil.class);
    private static CommonConfig commonConfig = ApplicationContextHelper.getBean(CommonConfig.class);
    private static LogInfoService logInfoService = ApplicationContextHelper.getBean(LogInfoService.class);
    private static MailConfig mailConfig = ApplicationContextHelper.getBean(MailConfig.class);
    private static LevelConfig levelConfig = ApplicationContextHelper.getBean(LevelConfig.class);
    private static SystemInfoService systemInfoService = ApplicationContextHelper.getBean(SystemInfoService.class);

    private static boolean preWarnInit(String hostname, String warnMail, String warnKey) {
        if ("false".equals(mailConfig.getAllWarnMail()) || "false".equals(warnMail)) {
            return false;
        }
        return !WarnPools.checkWarnCacheTimes(warnKey);
    }

    public static boolean sendWarnInfo(MemState memState) {
        String key = memState.getHostname() + "_mem";
        boolean sign = WarnMailUtil.preWarnInit(memState.getHostname(), mailConfig.getMemWarnMail(), key);
        if (!sign) {
            return false;
        }
        Double memWarnVal = mailConfig.getMemWarnVal();
        String warnLevel = levelConfig.getMemWarn();
        HostWarnDiy hostWarnDiyDto = StaticKeys.HOST_WARN_MAP.get(memState.getHostname());
        if (null != hostWarnDiyDto && "1".equals(hostWarnDiyDto.getActive())) {
            if ("no".equals(hostWarnDiyDto.getMemWarnMail()) || "yes".equals(hostWarnDiyDto.getHostBlockAllWarn())) {
                return false;
            }
            if (null != hostWarnDiyDto.getMemWarnVal()) {
                memWarnVal = hostWarnDiyDto.getMemWarnVal();
            }
            if (!StringUtils.isEmpty((CharSequence)hostWarnDiyDto.getMemWarnLevel())) {
                warnLevel = hostWarnDiyDto.getMemWarnLevel();
            }
        }
        if (memState.getUsePer() != null && memState.getUsePer() >= memWarnVal) {
            try {
                if (!WarnMailUtil.isOutWarnCount(key, mailConfig.getMemWarnCount())) {
                    logger.info(memState.getHostname() + "---\u5185\u5b58\u4f7f\u7528\u7387\u6ca1\u6709\u8fbe\u5230\u544a\u8b66\u6b21\u6570---" + WarnPools.HOST_WARN_COUNT_MAP.get(key));
                    return false;
                }
                String remark = HostUtil.addRemark(memState.getHostname());
                String title = "\u5185\u5b58\u544a\u8b66\uff1a" + memState.getHostname() + remark;
                String commContent = "\u4e3b\u673a\uff1a" + memState.getHostname() + remark + "\uff0c\u5f53\u524d\u5185\u5b58\u4f7f\u7528\u7387\u4e3a" + Double.valueOf(memState.getUsePer()) + "\uff0c\u5df2\u8fde\u7eed" + mailConfig.getMemWarnCount() + "\u6b21\u8d85\u8fc7\u544a\u8b66\u503c" + memWarnVal;
                String account = WarnMailUtil.getAccount(memState.getHostname());
                WarnMailUtil.sendUtil(title, commContent, account, key, true, warnLevel, WarnMailUtil.getHostGroups(memState.getHostname(), ""), memState.getHostname());
            }
            catch (Exception e) {
                logger.error("\u53d1\u9001\u5185\u5b58\u544a\u8b66\u90ae\u4ef6\u9519\u8bef", (Throwable)e);
                logInfoService.save("\u53d1\u9001\u5185\u5b58\u544a\u8b66\u90ae\u4ef6\u9519\u8bef", e.toString(), "1");
            }
        }
        return false;
    }

    public static boolean sendSysLoadWarnInfo(SysLoadState sysLoadState) {
        String key = sysLoadState.getHostname() + "_load";
        boolean sign = WarnMailUtil.preWarnInit(sysLoadState.getHostname(), mailConfig.getSysLoadWarnMail(), key);
        if (!sign) {
            return false;
        }
        Double sysLoadWarnVal = mailConfig.getSysLoadWarnVal();
        String warnLevel = levelConfig.getSysLoadWarn();
        HostWarnDiy hostWarnDiyDto = StaticKeys.HOST_WARN_MAP.get(sysLoadState.getHostname());
        if (null != hostWarnDiyDto && "1".equals(hostWarnDiyDto.getActive())) {
            if ("no".equals(hostWarnDiyDto.getSysLoadWarnMail()) || "yes".equals(hostWarnDiyDto.getHostBlockAllWarn())) {
                return false;
            }
            if (null != hostWarnDiyDto.getSysLoadWarnVal()) {
                sysLoadWarnVal = hostWarnDiyDto.getSysLoadWarnVal();
            }
            if (!StringUtils.isEmpty((CharSequence)hostWarnDiyDto.getSysLoadWarnLevel())) {
                warnLevel = hostWarnDiyDto.getSysLoadWarnLevel();
            }
        }
        if (sysLoadState.getFiveLoad() != null && sysLoadState.getFiveLoad() >= sysLoadWarnVal) {
            try {
                String remark = HostUtil.addRemark(sysLoadState.getHostname());
                String title = "\u7cfb\u7edf\u8d1f\u8f7d(5\u5206\u949f)\u544a\u8b66\uff1a" + sysLoadState.getHostname() + remark;
                String commContent = "\u4e3b\u673a\uff1a" + sysLoadState.getHostname() + remark + "\uff0c\u5f53\u524d\u7cfb\u7edf\u8d1f\u8f7d(5\u5206\u949f)\u4e3a" + Double.valueOf(sysLoadState.getFiveLoad()) + "\uff0c\u8d85\u8fc7\u544a\u8b66\u503c" + sysLoadWarnVal;
                String account = WarnMailUtil.getAccount(sysLoadState.getHostname());
                WarnMailUtil.sendUtil(title, commContent, account, key, true, warnLevel, WarnMailUtil.getHostGroups(sysLoadState.getHostname(), ""), sysLoadState.getHostname());
            }
            catch (Exception e) {
                logger.error("\u53d1\u9001\u7cfb\u7edf\u8d1f\u8f7d(5\u5206\u949f)\u544a\u8b66\u90ae\u4ef6\u9519\u8bef", (Throwable)e);
                logInfoService.save("\u53d1\u9001\u7cfb\u7edf\u8d1f\u8f7d(5\u5206\u949f)\u544a\u8b66\u90ae\u4ef6\u9519\u8bef", e.toString(), "1");
            }
        }
        return false;
    }

    public static boolean sendCpuWarnInfo(CpuState cpuState) {
        String key = cpuState.getHostname() + "_cpu";
        boolean sign = WarnMailUtil.preWarnInit(cpuState.getHostname(), mailConfig.getCpuWarnMail(), key);
        if (!sign) {
            return false;
        }
        Double cpuWarnVal = mailConfig.getCpuWarnVal();
        String warnLevel = levelConfig.getCpuWarn();
        HostWarnDiy hostWarnDiyDto = StaticKeys.HOST_WARN_MAP.get(cpuState.getHostname());
        if (null != hostWarnDiyDto && "1".equals(hostWarnDiyDto.getActive())) {
            if ("no".equals(hostWarnDiyDto.getCpuWarnMail()) || "yes".equals(hostWarnDiyDto.getHostBlockAllWarn())) {
                return false;
            }
            if (null != hostWarnDiyDto.getCpuWarnVal()) {
                cpuWarnVal = hostWarnDiyDto.getCpuWarnVal();
            }
            if (!StringUtils.isEmpty((CharSequence)hostWarnDiyDto.getCpuWarnLevel())) {
                warnLevel = hostWarnDiyDto.getCpuWarnLevel();
            }
        }
        if (cpuState.getSys() != null && cpuState.getSys() >= cpuWarnVal) {
            try {
                if (!WarnMailUtil.isOutWarnCount(key, mailConfig.getCpuWarnCount())) {
                    logger.info(cpuState.getHostname() + "---CPU\u4f7f\u7528\u7387\u6ca1\u6709\u8fbe\u5230\u544a\u8b66\u6b21\u6570---" + WarnPools.HOST_WARN_COUNT_MAP.get(key));
                    return false;
                }
                String remark = HostUtil.addRemark(cpuState.getHostname());
                String title = "CPU\u544a\u8b66\uff1a" + cpuState.getHostname() + remark;
                String commContent = "\u4e3b\u673a\uff1a" + cpuState.getHostname() + remark + "\uff0c\u5f53\u524dCPU\u4f7f\u7528\u7387\u4e3a" + Double.valueOf(cpuState.getSys()) + "\uff0c\u5df2\u8fde\u7eed" + mailConfig.getCpuWarnCount() + "\u6b21\u8d85\u8fc7\u544a\u8b66\u503c" + cpuWarnVal;
                String account = WarnMailUtil.getAccount(cpuState.getHostname());
                WarnMailUtil.sendUtil(title, commContent, account, key, true, warnLevel, WarnMailUtil.getHostGroups(cpuState.getHostname(), ""), cpuState.getHostname());
            }
            catch (Exception e) {
                logger.error("\u53d1\u9001CPU\u544a\u8b66\u90ae\u4ef6\u9519\u8bef", (Throwable)e);
                logInfoService.save("\u53d1\u9001CPU\u544a\u8b66\u90ae\u4ef6\u9519\u8bef", e.toString(), "1");
            }
        }
        return false;
    }

    public static boolean sendNetConnectionsWarnInfo(NetIoState netIoState) {
        Integer netConnections;
        String key = netIoState.getHostname() + "_netConnections";
        boolean sign = WarnMailUtil.preWarnInit(netIoState.getHostname(), mailConfig.getNetConnectionsWarnMail(), key);
        if (!sign) {
            return false;
        }
        Double netConnectionsWarnVal = mailConfig.getNetConnectionsWarnVal();
        String warnLevel = levelConfig.getNetConnectionsWarn();
        HostWarnDiy hostWarnDiyDto = StaticKeys.HOST_WARN_MAP.get(netIoState.getHostname());
        if (null != hostWarnDiyDto && "1".equals(hostWarnDiyDto.getActive())) {
            if ("no".equals(hostWarnDiyDto.getNetConnectionsWarnMail()) || "yes".equals(hostWarnDiyDto.getHostBlockAllWarn())) {
                return false;
            }
            if (null != hostWarnDiyDto.getNetConnectionsWarnVal()) {
                netConnectionsWarnVal = hostWarnDiyDto.getNetConnectionsWarnVal();
            }
            if (!StringUtils.isEmpty((CharSequence)hostWarnDiyDto.getNetConnectionsWarnLevel())) {
                warnLevel = hostWarnDiyDto.getNetConnectionsWarnLevel();
            }
        }
        if ((double)(netConnections = Integer.valueOf(netIoState.getNetConnections())).intValue() >= netConnectionsWarnVal) {
            try {
                String remark = HostUtil.addRemark(netIoState.getHostname());
                String title = "\u4e3b\u673a\u8fde\u63a5\u6570\u91cf\u544a\u8b66\uff1a" + netIoState.getHostname() + remark;
                String commContent = "\u4e3b\u673a\uff1a" + netIoState.getHostname() + remark + "\uff0c\u5f53\u524d\u8fde\u63a5\u6570\u91cf\u4e3a" + netConnections + "\uff0c\u8d85\u8fc7\u544a\u8b66\u503c" + netConnectionsWarnVal;
                String account = WarnMailUtil.getAccount(netIoState.getHostname());
                WarnMailUtil.sendUtil(title, commContent, account, key, true, warnLevel, WarnMailUtil.getHostGroups(netIoState.getHostname(), ""), netIoState.getHostname());
            }
            catch (Exception e) {
                logger.error("\u53d1\u9001\u4e3b\u673a\u8fde\u63a5\u6570\u91cf\u544a\u8b66\u90ae\u4ef6\u9519\u8bef", (Throwable)e);
                logInfoService.save("\u53d1\u9001\u4e3b\u673a\u8fde\u63a5\u6570\u91cf\u544a\u8b66\u90ae\u4ef6\u9519\u8bef", e.toString(), "1");
            }
        }
        return false;
    }

    public static boolean sendUpSpeedWarnInfo(NetIoState netIoState) {
        String account;
        String commContent;
        String title;
        String remark;
        boolean sign;
        String key = netIoState.getHostname() + "_txbyt";
        Double upSpeedVal = mailConfig.getUpSpeedVal();
        Double upSpeedMinVal = mailConfig.getUpSpeedMinVal();
        String warnLevel = levelConfig.getSpeedWarn();
        HostWarnDiy hostWarnDiyDto = StaticKeys.HOST_WARN_MAP.get(netIoState.getHostname());
        if (null != hostWarnDiyDto && "1".equals(hostWarnDiyDto.getActive())) {
            if ("no".equals(hostWarnDiyDto.getUpSpeedMail()) || "yes".equals(hostWarnDiyDto.getHostBlockAllWarn())) {
                return false;
            }
            if (null != hostWarnDiyDto.getUpSpeedVal()) {
                upSpeedVal = hostWarnDiyDto.getUpSpeedVal();
            }
            if (null != hostWarnDiyDto.getUpSpeedMinVal()) {
                upSpeedMinVal = hostWarnDiyDto.getUpSpeedMinVal();
            }
            if (!StringUtils.isEmpty((CharSequence)hostWarnDiyDto.getSpeedWarnLevel())) {
                warnLevel = hostWarnDiyDto.getSpeedWarnLevel();
            }
        }
        if (!StringUtils.isEmpty((CharSequence)netIoState.getTxbyt()) && Double.valueOf(netIoState.getTxbyt()) >= upSpeedVal) {
            try {
                sign = WarnMailUtil.preWarnInit(netIoState.getHostname(), mailConfig.getUpSpeedMail(), key + "_max");
                if (!sign) {
                    return false;
                }
                remark = HostUtil.addRemark(netIoState.getHostname());
                title = "\u8d85\u8fc7\u4e0a\u884c\u4f20\u8f93\u901f\u7387\u544a\u8b66\uff1a" + netIoState.getHostname() + remark;
                commContent = "\u4e3b\u673a\uff1a" + netIoState.getHostname() + remark + "\uff0c\u5f53\u524d\u4e0a\u884c\u4f20\u8f93\u901f\u7387\u4e3a" + FormatUtil.kbToM(netIoState.getTxbyt()) + "/s\uff0c\u8d85\u8fc7\u544a\u8b66\u503c" + FormatUtil.kbToM(upSpeedVal + "") + "/s";
                account = WarnMailUtil.getAccount(netIoState.getHostname());
                WarnMailUtil.sendUtil(title, commContent, account, key + "_max", true, warnLevel, WarnMailUtil.getHostGroups(netIoState.getHostname(), ""), netIoState.getHostname());
            }
            catch (Exception e) {
                logger.error("\u53d1\u9001\u8d85\u8fc7\u4e0a\u884c\u4f20\u8f93\u901f\u7387\u544a\u8b66\u90ae\u4ef6\u9519\u8bef", (Throwable)e);
                logInfoService.save("\u53d1\u9001\u8d85\u8fc7\u4e0a\u884c\u4f20\u8f93\u901f\u7387\u544a\u8b66\u90ae\u4ef6\u9519\u8bef", e.toString(), "1");
            }
        }
        if (!StringUtils.isEmpty((CharSequence)netIoState.getTxbyt()) && Double.valueOf(netIoState.getTxbyt()) < upSpeedMinVal) {
            try {
                sign = WarnMailUtil.preWarnInit(netIoState.getHostname(), mailConfig.getUpSpeedMail(), key + "_min");
                if (!sign) {
                    return false;
                }
                remark = HostUtil.addRemark(netIoState.getHostname());
                title = "\u4f4e\u4e8e\u4e0a\u884c\u4f20\u8f93\u901f\u7387\u544a\u8b66\uff1a" + netIoState.getHostname() + remark;
                commContent = "\u4e3b\u673a\uff1a" + netIoState.getHostname() + remark + "\uff0c\u5f53\u524d\u4e0a\u884c\u4f20\u8f93\u901f\u7387\u4e3a" + FormatUtil.kbToM(netIoState.getTxbyt()) + "/s\uff0c\u4f4e\u4e8e\u544a\u8b66\u503c" + FormatUtil.kbToM(upSpeedMinVal + "") + "/s";
                account = WarnMailUtil.getAccount(netIoState.getHostname());
                WarnMailUtil.sendUtil(title, commContent, account, key + "_min", true, warnLevel, WarnMailUtil.getHostGroups(netIoState.getHostname(), ""), netIoState.getHostname());
            }
            catch (Exception e) {
                logger.error("\u53d1\u9001\u4f4e\u4e8e\u4e0a\u884c\u4f20\u8f93\u901f\u7387\u544a\u8b66\u90ae\u4ef6\u9519\u8bef", (Throwable)e);
                logInfoService.save("\u53d1\u9001\u4f4e\u4e8e\u4e0a\u884c\u4f20\u8f93\u901f\u7387\u544a\u8b66\u90ae\u4ef6\u9519\u8bef", e.toString(), "1");
            }
        }
        return false;
    }

    public static boolean sendDownSpeedWarnInfo(NetIoState netIoState) {
        String account;
        String commContent;
        String title;
        String remark;
        boolean sign;
        String key = netIoState.getHostname() + "_rxbyt";
        Double downSpeedVal = mailConfig.getDownSpeedVal();
        Double downSpeedMinVal = mailConfig.getDownSpeedMinVal();
        String warnLevel = levelConfig.getSpeedWarn();
        HostWarnDiy hostWarnDiyDto = StaticKeys.HOST_WARN_MAP.get(netIoState.getHostname());
        if (null != hostWarnDiyDto && "1".equals(hostWarnDiyDto.getActive())) {
            if ("no".equals(hostWarnDiyDto.getDownSpeedMail()) || "yes".equals(hostWarnDiyDto.getHostBlockAllWarn())) {
                return false;
            }
            if (null != hostWarnDiyDto.getDownSpeedVal()) {
                downSpeedVal = hostWarnDiyDto.getDownSpeedVal();
            }
            if (null != hostWarnDiyDto.getDownSpeedMinVal()) {
                downSpeedMinVal = hostWarnDiyDto.getDownSpeedMinVal();
            }
            if (!StringUtils.isEmpty((CharSequence)hostWarnDiyDto.getSpeedWarnLevel())) {
                warnLevel = hostWarnDiyDto.getSpeedWarnLevel();
            }
        }
        if (!StringUtils.isEmpty((CharSequence)netIoState.getRxbyt()) && Double.valueOf(netIoState.getRxbyt()) >= downSpeedVal) {
            try {
                sign = WarnMailUtil.preWarnInit(netIoState.getHostname(), mailConfig.getDownSpeedMail(), key + "_max");
                if (!sign) {
                    return false;
                }
                remark = HostUtil.addRemark(netIoState.getHostname());
                title = "\u8d85\u8fc7\u4e0b\u884c\u4f20\u8f93\u901f\u7387\u544a\u8b66\uff1a" + netIoState.getHostname() + remark;
                commContent = "\u4e3b\u673a\uff1a" + netIoState.getHostname() + remark + "\uff0c\u5f53\u524d\u4e0b\u884c\u4f20\u8f93\u901f\u7387\u4e3a" + FormatUtil.kbToM(netIoState.getRxbyt()) + "/s\uff0c\u8d85\u8fc7\u544a\u8b66\u503c" + FormatUtil.kbToM(downSpeedVal + "") + "/s";
                account = WarnMailUtil.getAccount(netIoState.getHostname());
                WarnMailUtil.sendUtil(title, commContent, account, key + "_max", true, warnLevel, WarnMailUtil.getHostGroups(netIoState.getHostname(), ""), netIoState.getHostname());
            }
            catch (Exception e) {
                logger.error("\u53d1\u9001\u8d85\u8fc7\u4e0b\u884c\u4f20\u8f93\u901f\u7387\u544a\u8b66\u90ae\u4ef6\u9519\u8bef", (Throwable)e);
                logInfoService.save("\u53d1\u9001\u8d85\u8fc7\u4e0b\u884c\u4f20\u8f93\u901f\u7387\u544a\u8b66\u90ae\u4ef6\u9519\u8bef", e.toString(), "1");
            }
        }
        if (!StringUtils.isEmpty((CharSequence)netIoState.getRxbyt()) && Double.valueOf(netIoState.getRxbyt()) < downSpeedMinVal) {
            try {
                sign = WarnMailUtil.preWarnInit(netIoState.getHostname(), mailConfig.getDownSpeedMail(), key + "_min");
                if (!sign) {
                    return false;
                }
                remark = HostUtil.addRemark(netIoState.getHostname());
                title = "\u4f4e\u4e8e\u4e0b\u884c\u4f20\u8f93\u901f\u7387\u544a\u8b66\uff1a" + netIoState.getHostname() + remark;
                commContent = "\u4e3b\u673a\uff1a" + netIoState.getHostname() + remark + "\uff0c\u5f53\u524d\u4e0b\u884c\u4f20\u8f93\u901f\u7387\u4e3a" + FormatUtil.kbToM(netIoState.getRxbyt()) + "/s\uff0c\u4f4e\u4e8e\u544a\u8b66\u503c" + FormatUtil.kbToM(downSpeedMinVal + "") + "/s";
                account = WarnMailUtil.getAccount(netIoState.getHostname());
                WarnMailUtil.sendUtil(title, commContent, account, key + "_min", true, warnLevel, WarnMailUtil.getHostGroups(netIoState.getHostname(), ""), netIoState.getHostname());
            }
            catch (Exception e) {
                logger.error("\u53d1\u9001\u4f4e\u4e8e\u4e0b\u884c\u4f20\u8f93\u901f\u7387\u544a\u8b66\u90ae\u4ef6\u9519\u8bef", (Throwable)e);
                logInfoService.save("\u53d1\u9001\u4f4e\u4e8e\u4e0b\u884c\u4f20\u8f93\u901f\u7387\u544a\u8b66\u90ae\u4ef6\u9519\u8bef", e.toString(), "1");
            }
        }
        return false;
    }

    public static boolean sendDiskSmartWarnInfo(DiskSmart smartBean) {
        String key = smartBean.getHostname() + "_smart";
        boolean sign = WarnMailUtil.preWarnInit(smartBean.getHostname(), mailConfig.getSmartWarnMail(), key);
        if (!sign) {
            return false;
        }
        String warnLevel = levelConfig.getSmartWarn();
        HostWarnDiy hostWarnDiyDto = StaticKeys.HOST_WARN_MAP.get(smartBean.getHostname());
        if (null != hostWarnDiyDto && "1".equals(hostWarnDiyDto.getActive())) {
            if ("no".equals(hostWarnDiyDto.getSmartWarnMail()) || "yes".equals(hostWarnDiyDto.getHostBlockAllWarn())) {
                return false;
            }
            if (!StringUtils.isEmpty((CharSequence)hostWarnDiyDto.getSmartWarnLevel())) {
                warnLevel = hostWarnDiyDto.getSmartWarnLevel();
            }
        }
        if (!StringUtils.isEmpty((CharSequence)smartBean.getDiskState()) && "FAILED".equals(smartBean.getDiskState())) {
            try {
                String remark = HostUtil.addRemark(smartBean.getHostname());
                String title = "\u78c1\u76d8\u544a\u8b66SMART\uff1a" + smartBean.getHostname() + remark;
                String commContent = "\u4e3b\u673a\uff1a" + smartBean.getHostname() + remark + "\uff0c\u78c1\u76d8" + smartBean.getFileSystem() + "\uff0cSMART\u5065\u5eb7\u68c0\u6d4b\u7ed3\u679c\u4e3a" + "FAILED";
                String account = WarnMailUtil.getAccount(smartBean.getHostname());
                WarnMailUtil.sendUtil(title, commContent, account, key, true, warnLevel, WarnMailUtil.getHostGroups(smartBean.getHostname(), ""), smartBean.getHostname());
            }
            catch (Exception e) {
                logger.error("\u53d1\u9001\u78c1\u76d8\u544a\u8b66SMART\u90ae\u4ef6\u9519\u8bef", (Throwable)e);
                logInfoService.save("\u53d1\u9001\u78c1\u76d8\u544a\u8b66SMART\u90ae\u4ef6\u9519\u8bef", e.toString(), "1");
            }
        }
        return false;
    }

    public static boolean sendDiskWarnInfo(DiskState deskState) {
        Double usePer;
        logger.debug("\u544a\u8b66\u78c1\u76d8-------------" + deskState.getFileSystem());
        String key = deskState.getHostname() + "_disk_" + deskState.getFileSystem();
        Double diskWarnVal = mailConfig.getDiskWarnVal();
        String warnLevel = levelConfig.getDiskWarn();
        HostWarnDiy hostWarnDiyDto = StaticKeys.HOST_WARN_MAP.get(deskState.getHostname());
        if (null != hostWarnDiyDto && "1".equals(hostWarnDiyDto.getActive())) {
            if ("no".equals(hostWarnDiyDto.getDiskWarnMail()) || "yes".equals(hostWarnDiyDto.getHostBlockAllWarn())) {
                return false;
            }
            if (null != hostWarnDiyDto.getDiskWarnVal()) {
                diskWarnVal = hostWarnDiyDto.getDiskWarnVal();
            }
            if (!StringUtils.isEmpty((CharSequence)hostWarnDiyDto.getDiskWarnLevel())) {
                warnLevel = hostWarnDiyDto.getDiskWarnLevel();
            }
        }
        if ((usePer = Double.valueOf(deskState.getUsePer().replace("%", ""))) != null && usePer < diskWarnVal && !StringUtils.isEmpty((CharSequence)WarnPools.getWarnMark(key))) {
            try {
                String remark = HostUtil.addRemark(deskState.getHostname());
                String title = "\u78c1\u76d8\u5df2\u6062\u590d\uff1a" + deskState.getHostname() + remark;
                String commContent = "\u4e3b\u673a\u78c1\u76d8\u5df2\u6062\u590d\uff1a" + deskState.getHostname() + remark + "\uff0c\u78c1\u76d8" + deskState.getFileSystem() + "\u4f7f\u7528\u7387\u4e3a" + usePer + "\uff0c\u672a\u8fbe\u5230\u544a\u8b66\u503c" + diskWarnVal;
                String account = WarnMailUtil.getAccount(deskState.getHostname());
                WarnMailUtil.sendUtil(title, commContent, account, key, false, warnLevel, WarnMailUtil.getHostGroups(deskState.getHostname(), ""), deskState.getHostname());
            }
            catch (Exception e) {
                logger.error("\u53d1\u9001\u78c1\u76d8\u5df2\u6062\u590d\u90ae\u4ef6\u9519\u8bef", (Throwable)e);
                logInfoService.save("\u53d1\u9001\u78c1\u76d8\u5df2\u6062\u590d\u90ae\u4ef6\u9519\u8bef", e.toString(), "1");
            }
            return false;
        }
        boolean sign = WarnMailUtil.preWarnInit(deskState.getHostname(), mailConfig.getDiskWarnMail(), key);
        if (!sign) {
            return false;
        }
        if (WarnMailUtil.blockDisk(deskState)) {
            return false;
        }
        if (usePer != null && usePer >= diskWarnVal) {
            try {
                String remark = HostUtil.addRemark(deskState.getHostname());
                String title = "\u78c1\u76d8\u544a\u8b66\uff1a" + deskState.getHostname() + remark;
                String commContent = "\u4e3b\u673a\u78c1\u76d8\u544a\u8b66\uff1a" + deskState.getHostname() + remark + "\uff0c\u78c1\u76d8" + deskState.getFileSystem() + "\u4f7f\u7528\u7387\u4e3a" + usePer + "\uff0c\u8d85\u8fc7\u544a\u8b66\u503c" + diskWarnVal;
                String account = WarnMailUtil.getAccount(deskState.getHostname());
                WarnMailUtil.sendUtil(title, commContent, account, key, true, warnLevel, WarnMailUtil.getHostGroups(deskState.getHostname(), ""), deskState.getHostname());
            }
            catch (Exception e) {
                logger.error("\u53d1\u9001\u78c1\u76d8\u544a\u8b66\u90ae\u4ef6\u9519\u8bef", (Throwable)e);
                logInfoService.save("\u53d1\u9001\u78c1\u76d8\u544a\u8b66\u90ae\u4ef6\u9519\u8bef", e.toString(), "1");
            }
        }
        return false;
    }

    private static boolean blockDisk(DiskState deskState) {
        String diskBlock = mailConfig.getDiskBlock();
        HostWarnDiy hostWarnDiyDto = StaticKeys.HOST_WARN_MAP.get(deskState.getHostname());
        if (null != hostWarnDiyDto && "1".equals(hostWarnDiyDto.getActive()) && !StringUtils.isEmpty((CharSequence)hostWarnDiyDto.getDiskBlock()) && "no".equals(hostWarnDiyDto.getHostBlockAllWarn())) {
            diskBlock = hostWarnDiyDto.getDiskBlock();
        }
        if (!StringUtils.isEmpty((CharSequence)diskBlock)) {
            String[] blocks = diskBlock.split(",");
            AntPathMatcher pm = new AntPathMatcher();
            for (String diskBlcok : blocks) {
                diskBlcok = diskBlcok.replace("'", "");
                if ("/".equals(deskState.getFileSystem())) {
                    if (!diskBlcok.equals(deskState.getFileSystem())) continue;
                    return true;
                }
                boolean matchStart = pm.matchStart(diskBlcok, deskState.getFileSystem());
                if (!matchStart) continue;
                return matchStart;
            }
        }
        return false;
    }

    public static boolean sendCpuTemperatures(CpuTemperatures cpuTemperatures) {
        Double inputVal;
        String key = cpuTemperatures.getHostname() + "_temperatures";
        boolean sign = WarnMailUtil.preWarnInit(cpuTemperatures.getHostname(), mailConfig.getCpuTemperatureWarnMail(), key);
        if (!sign) {
            return false;
        }
        Double cpuTemperatureWarnVal = mailConfig.getCpuTemperatureWarnVal();
        String warnLevel = levelConfig.getCpuTemperatureWarn();
        HostWarnDiy hostWarnDiyDto = StaticKeys.HOST_WARN_MAP.get(cpuTemperatures.getHostname());
        if (null != hostWarnDiyDto && "1".equals(hostWarnDiyDto.getActive())) {
            if ("no".equals(hostWarnDiyDto.getCpuTemperatureWarnMail()) || "yes".equals(hostWarnDiyDto.getHostBlockAllWarn())) {
                return false;
            }
            if (null != hostWarnDiyDto.getCpuTemperatureWarnVal()) {
                cpuTemperatureWarnVal = hostWarnDiyDto.getCpuTemperatureWarnVal();
            }
            if (!StringUtils.isEmpty((CharSequence)hostWarnDiyDto.getCpuTemperatureWarnLevel())) {
                warnLevel = hostWarnDiyDto.getCpuTemperatureWarnLevel();
            }
        }
        if ((inputVal = Double.valueOf(cpuTemperatures.getInput().replace("\u2103", "").replace("+", ""))) != null && inputVal >= cpuTemperatureWarnVal) {
            try {
                String remark = HostUtil.addRemark(cpuTemperatures.getHostname());
                String title = "CPU\u6e29\u5ea6\u544a\u8b66\uff1a" + cpuTemperatures.getHostname() + remark;
                String commContent = "\u4e3b\u673a\uff1a" + cpuTemperatures.getHostname() + remark + "\uff0cCPU\u5f53\u524d\u6e29\u5ea6\u4e3a" + cpuTemperatures.getInput() + "\uff0c\u8d85\u8fc7\u544a\u8b66\u503c" + cpuTemperatureWarnVal + "\u2103";
                String account = WarnMailUtil.getAccount(cpuTemperatures.getHostname());
                WarnMailUtil.sendUtil(title, commContent, account, key, true, warnLevel, WarnMailUtil.getHostGroups(cpuTemperatures.getHostname(), ""), cpuTemperatures.getHostname());
            }
            catch (Exception e) {
                logger.error("\u53d1\u9001CPU\u6e29\u5ea6\u544a\u8b66\u90ae\u4ef6\u9519\u8bef", (Throwable)e);
                logInfoService.save("\u53d1\u9001CPU\u6e29\u5ea6\u544a\u8b66\u90ae\u4ef6\u9519\u8bef", e.toString(), "1");
            }
        }
        return false;
    }

    public static boolean sendDiskIoSpeedWarnInfo(DiskIoState diskIoState) {
        String key = diskIoState.getHostname() + "_diskIoSpeed";
        boolean sign = WarnMailUtil.preWarnInit(diskIoState.getHostname(), mailConfig.getDiskIoSpeedWarnMail(), key);
        if (!sign) {
            return false;
        }
        Double diskIoSpeedWarnVal = mailConfig.getDiskIoSpeedWarnVal();
        String warnLevel = levelConfig.getDiskWarn();
        HostWarnDiy hostWarnDiyDto = StaticKeys.HOST_WARN_MAP.get(diskIoState.getHostname());
        if (null != hostWarnDiyDto && "1".equals(hostWarnDiyDto.getActive())) {
            if ("no".equals(hostWarnDiyDto.getDiskIoSpeedWarnMail()) || "yes".equals(hostWarnDiyDto.getHostBlockAllWarn())) {
                return false;
            }
            if (null != hostWarnDiyDto.getDiskIoSpeedWarnVal()) {
                diskIoSpeedWarnVal = hostWarnDiyDto.getDiskIoSpeedWarnVal();
            }
            if (!StringUtils.isEmpty((CharSequence)hostWarnDiyDto.getDiskWarnLevel())) {
                warnLevel = hostWarnDiyDto.getDiskWarnLevel();
            }
        }
        Double readIoAvg = Double.valueOf(diskIoState.getReadIoAvg());
        Double writeIoAvg = Double.valueOf(diskIoState.getWriteIoAvg());
        if (readIoAvg >= diskIoSpeedWarnVal || writeIoAvg >= diskIoSpeedWarnVal) {
            try {
                String remark = HostUtil.addRemark(diskIoState.getHostname());
                String title = "\u78c1\u76d8IO\u8bfb\u5199\u901f\u7387\u544a\u8b66\uff1a" + diskIoState.getHostname() + remark;
                String centerMsg = "\uff0c";
                if (readIoAvg >= diskIoSpeedWarnVal) {
                    centerMsg = centerMsg + "\u5f53\u524d\u78c1\u76d8IO\u8bfb\u53d6\u901f\u7387" + readIoAvg + "MB/s\uff0c";
                }
                if (writeIoAvg >= diskIoSpeedWarnVal) {
                    centerMsg = centerMsg + "\u5f53\u524d\u78c1\u76d8IO\u5199\u5165\u901f\u7387" + writeIoAvg + "MB/s\uff0c";
                }
                String commContent = "\u4e3b\u673a\uff1a" + diskIoState.getHostname() + remark + centerMsg + "\u8d85\u8fc7\u544a\u8b66\u503c" + diskIoSpeedWarnVal + "MB/s";
                String account = WarnMailUtil.getAccount(diskIoState.getHostname());
                WarnMailUtil.sendUtil(title, commContent, account, key, true, warnLevel, WarnMailUtil.getHostGroups(diskIoState.getHostname(), ""), diskIoState.getHostname());
            }
            catch (Exception e) {
                logger.error("\u53d1\u9001\u4e3b\u673a\u78c1\u76d8IO\u8bfb\u5199\u901f\u7387\u544a\u8b66\u90ae\u4ef6\u9519\u8bef", (Throwable)e);
                logInfoService.save("\u53d1\u9001\u4e3b\u673a\u78c1\u76d8IO\u8bfb\u5199\u901f\u7387\u544a\u8b66\u90ae\u4ef6\u9519\u8bef", e.toString(), "1");
            }
        }
        return false;
    }

    public static boolean sendHostDown(SystemInfo systemInfo, boolean isDown, boolean newHost) {
        if ("false".equals(mailConfig.getAllWarnMail()) || "false".equals(mailConfig.getHostDownWarnMail())) {
            return false;
        }
        String hostDownWarnCount = "";
        hostDownWarnCount = "\uff0c\u5df2\u8fde\u7eed" + mailConfig.getHostDownWarnCount() + "\u6b21\u672a\u4e0a\u62a5\u6570\u636e";
        String warnLevel = levelConfig.getHostDownWarn();
        HostWarnDiy hostWarnDiyDto = StaticKeys.HOST_WARN_MAP.get(systemInfo.getHostname());
        if (null != hostWarnDiyDto && "1".equals(hostWarnDiyDto.getActive())) {
            if ("no".equals(hostWarnDiyDto.getHostDownWarnMail()) || "yes".equals(hostWarnDiyDto.getHostBlockAllWarn())) {
                return false;
            }
            if (!StringUtils.isEmpty((CharSequence)hostWarnDiyDto.getHostDownWarnLevel())) {
                warnLevel = hostWarnDiyDto.getHostDownWarnLevel();
            }
            if (!StringUtils.isEmpty((CharSequence)hostWarnDiyDto.getHostDownWarnCount())) {
                hostDownWarnCount = "\uff0c\u5df2\u8fde\u7eed" + hostWarnDiyDto.getHostDownWarnCount() + "\u6b21\u672a\u4e0a\u62a5\u6570\u636e";
            }
        }
        String key = systemInfo.getId();
        String remark = HostUtil.addRemark(systemInfo.getHostname());
        if (isDown) {
            if (WarnPools.checkWarnCacheTimes(key)) {
                return false;
            }
            try {
                String title = "\u4e3b\u673a\u79bb\u7ebf\u544a\u8b66\uff1a" + systemInfo.getHostname() + remark;
                String commContent = "\u4e3b\u673a\u5df2\u79bb\u7ebf\uff1a" + systemInfo.getHostname() + remark + hostDownWarnCount;
                WarnMailUtil.sendUtil(title, commContent, systemInfo.getAccount(), key, isDown, warnLevel, WarnMailUtil.getHostGroups(systemInfo.getHostname(), ""), systemInfo.getHostname());
            }
            catch (Exception e) {
                logger.error("\u53d1\u9001\u4e3b\u673a\u79bb\u7ebf\u544a\u8b66\u90ae\u4ef6\u5931\u8d25", (Throwable)e);
                logInfoService.save("\u53d1\u9001\u4e3b\u673a\u79bb\u7ebf\u544a\u8b66\u90ae\u4ef6\u9519\u8bef", e.toString(), "1");
            }
        } else {
            try {
                String title = "\u4e3b\u673a\u5df2\u6062\u590d\u4e0a\u7ebf\uff1a" + systemInfo.getHostname() + remark;
                String commContent = "\u4e3b\u673a\u5df2\u6062\u590d\u4e0a\u7ebf\uff1a" + systemInfo.getHostname() + remark;
                if (newHost) {
                    title = title + "\uff08\u53d1\u73b0\u65b0\u4e3b\u673a\uff09";
                    commContent = commContent + "\uff08\u53d1\u73b0\u65b0\u4e3b\u673a\uff09";
                }
                WarnMailUtil.sendUtil(title, commContent, systemInfo.getAccount(), key, isDown, warnLevel, WarnMailUtil.getHostGroups(systemInfo.getHostname(), ""), systemInfo.getHostname());
            }
            catch (Exception e) {
                logger.error("\u53d1\u9001\u4e3b\u673a\u6062\u590d\u4e0a\u7ebf\u901a\u77e5\u90ae\u4ef6\u9519\u8bef", (Throwable)e);
                logInfoService.save("\u53d1\u9001\u4e3b\u673a\u6062\u590d\u4e0a\u7ebf\u901a\u77e5\u90ae\u4ef6\u9519\u8bef", e.toString(), "1");
            }
        }
        return false;
    }

    public static boolean sendAppDown(AppInfo appInfo, boolean isDown) {
        if ("false".equals(mailConfig.getAllWarnMail()) || "false".equals(mailConfig.getAppDownWarnMail())) {
            return false;
        }
        HostWarnDiy hostWarnDiyDto = StaticKeys.HOST_WARN_MAP.get(appInfo.getHostname());
        if (null != hostWarnDiyDto && "1".equals(hostWarnDiyDto.getActive()) && "yes".equals(hostWarnDiyDto.getHostBlockAllWarn())) {
            return false;
        }
        String key = appInfo.getId();
        String remark = HostUtil.addRemark(appInfo.getHostname());
        if (isDown) {
            if (WarnPools.checkWarnCacheTimes(key)) {
                return false;
            }
            try {
                String title = "\u8fdb\u7a0b\u79bb\u7ebf\u544a\u8b66\uff1a" + appInfo.getAppName() + "\uff0c" + appInfo.getHostname() + remark;
                String commContent = "\u8fdb\u7a0b\u5df2\u79bb\u7ebf\uff1a" + appInfo.getHostname() + remark + "\uff0c\u8fdb\u7a0b\uff1a" + appInfo.getAppName();
                String account = WarnMailUtil.getAccount(appInfo.getHostname());
                WarnMailUtil.sendUtil(title, commContent, account, key, isDown, appInfo.getWarnLevel(), WarnMailUtil.getHostGroups(appInfo.getHostname(), appInfo.getGroupId()), appInfo.getHostname());
            }
            catch (Exception e) {
                logger.error("\u53d1\u9001\u8fdb\u7a0b\u79bb\u7ebf\u544a\u8b66\u90ae\u4ef6\u5931\u8d25\uff1a", (Throwable)e);
                logInfoService.save("\u53d1\u9001\u8fdb\u7a0b\u79bb\u7ebf\u544a\u8b66\u9519\u8bef", e.toString(), "1");
            }
        } else {
            try {
                String title = "\u8fdb\u7a0b\u5df2\u6062\u590d\u4e0a\u7ebf\uff1a" + appInfo.getAppName() + "\uff0c" + appInfo.getHostname() + remark;
                String commContent = "\u8fdb\u7a0b\u5df2\u6062\u590d\u4e0a\u7ebf\uff1a" + appInfo.getHostname() + remark + "\uff0c\u8fdb\u7a0b\uff1a" + appInfo.getAppName();
                String account = WarnMailUtil.getAccount(appInfo.getHostname());
                WarnMailUtil.sendUtil(title, commContent, account, key, isDown, appInfo.getWarnLevel(), WarnMailUtil.getHostGroups(appInfo.getHostname(), appInfo.getGroupId()), appInfo.getHostname());
            }
            catch (Exception e) {
                logger.error("\u53d1\u9001\u8fdb\u7a0b\u6062\u590d\u4e0a\u7ebf\u901a\u77e5\u90ae\u4ef6\u5931\u8d25", (Throwable)e);
                logInfoService.save("\u53d1\u9001\u8fdb\u7a0b\u6062\u590d\u4e0a\u7ebf\u901a\u77e5\u9519\u8bef", e.toString(), "1");
            }
        }
        return false;
    }

    public static boolean sendDockerDown(DockerInfo dockerInfo, boolean isDown) {
        if ("false".equals(mailConfig.getAllWarnMail()) || "false".equals(mailConfig.getDockerDownWarnMail())) {
            return false;
        }
        HostWarnDiy hostWarnDiyDto = StaticKeys.HOST_WARN_MAP.get(dockerInfo.getHostname());
        if (null != hostWarnDiyDto && "1".equals(hostWarnDiyDto.getActive()) && "yes".equals(hostWarnDiyDto.getHostBlockAllWarn())) {
            return false;
        }
        String key = dockerInfo.getId();
        String remark = HostUtil.addRemark(dockerInfo.getHostname());
        String dockerTypeStr = "CONTAINER ID";
        if ("2".equals(dockerInfo.getAppType())) {
            dockerTypeStr = "CONTAINER NAME";
        }
        if (isDown) {
            if (WarnPools.checkWarnCacheTimes(key)) {
                return false;
            }
            try {
                String title = "docker\u79bb\u7ebf\u544a\u8b66\uff1a" + dockerInfo.getDockerName() + "\uff0c" + dockerInfo.getHostname() + remark;
                String commContent = "docker\u5df2\u79bb\u7ebf\uff1a" + dockerInfo.getHostname() + remark + "\uff0c\u540d\u79f0\uff1a" + dockerInfo.getDockerName() + "\uff0c" + dockerTypeStr + "\uff1a" + dockerInfo.getDockerId();
                String account = WarnMailUtil.getAccount(dockerInfo.getHostname());
                WarnMailUtil.sendUtil(title, commContent, account, key, isDown, dockerInfo.getWarnLevel(), WarnMailUtil.getHostGroups(dockerInfo.getHostname(), dockerInfo.getGroupId()), dockerInfo.getHostname());
            }
            catch (Exception e) {
                logger.error("\u53d1\u9001docker\u79bb\u7ebf\u544a\u8b66\u90ae\u4ef6\u5931\u8d25\uff1a", (Throwable)e);
                logInfoService.save("\u53d1\u9001docker\u79bb\u7ebf\u544a\u8b66\u9519\u8bef", e.toString(), "1");
            }
        } else {
            WarnPools.removeWarnMark(key);
            try {
                String title = "docker\u5df2\u6062\u590d\u4e0a\u7ebf\uff1a" + dockerInfo.getDockerName() + "\uff0c" + dockerInfo.getHostname() + remark;
                String commContent = "docker\u5df2\u6062\u590d\u4e0a\u7ebf\uff1a" + dockerInfo.getHostname() + remark + "\uff0c\u540d\u79f0\uff1a" + dockerInfo.getDockerName() + "\uff0c" + dockerTypeStr + "\uff1a" + dockerInfo.getDockerId();
                String account = WarnMailUtil.getAccount(dockerInfo.getHostname());
                WarnMailUtil.sendUtil(title, commContent, account, key, isDown, dockerInfo.getWarnLevel(), WarnMailUtil.getHostGroups(dockerInfo.getHostname(), dockerInfo.getGroupId()), dockerInfo.getHostname());
            }
            catch (Exception e) {
                logger.error("\u53d1\u9001docker\u5df2\u6062\u590d\u4e0a\u7ebf\u90ae\u4ef6\u5931\u8d25", (Throwable)e);
                logInfoService.save("\u53d1\u9001docker\u5df2\u6062\u590d\u4e0a\u7ebf\u9519\u8bef", e.toString(), "1");
            }
        }
        return false;
    }

    public static boolean sendPortDown(PortInfo portInfo, boolean isDown) {
        if ("false".equals(mailConfig.getAllWarnMail()) || "false".equals(mailConfig.getPortWarnMail())) {
            return false;
        }
        HostWarnDiy hostWarnDiyDto = StaticKeys.HOST_WARN_MAP.get(portInfo.getHostname());
        if (null != hostWarnDiyDto && "1".equals(hostWarnDiyDto.getActive()) && "yes".equals(hostWarnDiyDto.getHostBlockAllWarn())) {
            return false;
        }
        String key = portInfo.getId();
        String remark = HostUtil.addRemark(portInfo.getHostname());
        String telnetIp = portInfo.getTelnetIp();
        if (!StringUtils.isEmpty((CharSequence)telnetIp) && telnetIp.length() > 50) {
            telnetIp = telnetIp.substring(0, 50);
        }
        if (isDown) {
            if (WarnPools.checkWarnCacheTimes(key)) {
                return false;
            }
            if (!WarnMailUtil.isOutWarnCount(key, mailConfig.getPortWarnCount())) {
                logger.info(portInfo.getHostname() + "---\u7aef\u53e3\u76d1\u63a7\u5931\u8d25\u6ca1\u6709\u8fbe\u5230\u544a\u8b66\u6b21\u6570---" + WarnPools.HOST_WARN_COUNT_MAP.get(key));
                return false;
            }
            try {
                String title = "\u7aef\u53e3telnet\u4e0d\u901a\u544a\u8b66\uff1a" + portInfo.getPortName() + "\uff0ctelnet-" + telnetIp + "-" + portInfo.getPort() + "\uff0c" + portInfo.getHostname() + remark;
                String commContent = "\u7aef\u53e3\u5df2\u8fde\u7eed" + mailConfig.getPortWarnCount() + "\u6b21telnet\u4e0d\u901a\uff0c\u540d\u79f0\uff1a" + portInfo.getPortName() + "\uff0ctelnet-" + telnetIp + "-" + portInfo.getPort() + "\uff0c\u76d1\u63a7\u4e3b\u673a\uff1a" + portInfo.getHostname() + remark;
                String account = WarnMailUtil.getAccount(portInfo.getHostname());
                WarnMailUtil.sendUtil(title, commContent, account, key, isDown, portInfo.getWarnLevel(), WarnMailUtil.getHostGroups(portInfo.getHostname(), portInfo.getGroupId()), portInfo.getHostname());
            }
            catch (Exception e) {
                logger.error("\u53d1\u9001\u7aef\u53e3telnet\u4e0d\u901a\u544a\u8b66\u90ae\u4ef6\u9519\u8bef\uff1a", (Throwable)e);
                logInfoService.save("\u53d1\u9001\u7aef\u53e3telnet\u4e0d\u901a\u544a\u8b66\u90ae\u4ef6\u9519\u8bef", e.toString(), "1");
            }
        } else {
            WarnPools.removeWarnMark(key);
            try {
                String title = "\u7aef\u53e3\u5df2\u6062\u590d\u4e0a\u7ebf\uff1a" + portInfo.getPortName() + "\uff0ctelnet-" + telnetIp + "-" + portInfo.getPort() + "\uff0c" + portInfo.getHostname() + remark;
                String commContent = "\u7aef\u53e3\u5df2\u6062\u590d\u4e0a\u7ebf\uff0c\u540d\u79f0\uff1a" + portInfo.getPortName() + "\uff0ctelnet-" + telnetIp + "-" + portInfo.getPort() + "\uff0c\u76d1\u63a7\u4e3b\u673a\uff1a" + portInfo.getHostname() + remark;
                String account = WarnMailUtil.getAccount(portInfo.getHostname());
                WarnMailUtil.sendUtil(title, commContent, account, key, isDown, portInfo.getWarnLevel(), WarnMailUtil.getHostGroups(portInfo.getHostname(), portInfo.getGroupId()), portInfo.getHostname());
            }
            catch (Exception e) {
                logger.error("\u53d1\u9001\u7aef\u53e3telnet\u5df2\u6062\u590d\u544a\u8b66\u90ae\u4ef6\u9519\u8bef", (Throwable)e);
                logInfoService.save("\u53d1\u9001\u7aef\u53e3telnet\u5df2\u6062\u590d\u544a\u8b66\u90ae\u4ef6\u9519\u8bef", e.toString(), "1");
            }
        }
        return false;
    }

    public static boolean sendFileSafeDown(FileSafe fileSafe, boolean isDown, String errorFileCount) {
        if ("false".equals(mailConfig.getAllWarnMail()) || "false".equals(mailConfig.getFileSafeWarnMail())) {
            return false;
        }
        HostWarnDiy hostWarnDiyDto = StaticKeys.HOST_WARN_MAP.get(fileSafe.getHostname());
        if (null != hostWarnDiyDto && "1".equals(hostWarnDiyDto.getActive()) && "yes".equals(hostWarnDiyDto.getHostBlockAllWarn())) {
            return false;
        }
        String key = fileSafe.getId();
        String remark = HostUtil.addRemark(fileSafe.getHostname());
        String errorFileCountStr = "";
        if (null != errorFileCount) {
            errorFileCountStr = "\uff0c" + errorFileCount;
        }
        if (isDown) {
            if (WarnPools.checkWarnCacheTimes(key)) {
                return false;
            }
            try {
                String title = "\u6587\u4ef6\u9632\u7be1\u6539\u76d1\u6d4b\u544a\u8b66\uff1a" + fileSafe.getFileName() + "\uff0c" + fileSafe.getHostname() + remark;
                String commContent = "\u6587\u4ef6\u9632\u7be1\u6539\u76d1\u6d4b\u5f02\u5e38\uff1a" + fileSafe.getHostname() + remark + "\uff0c\u6587\u4ef6\uff1a" + fileSafe.getFileName() + "\uff0c\u6587\u4ef6\u8def\u5f84\uff1a" + fileSafe.getFilePath() + "\uff0c\u6587\u4ef6\u6700\u540e\u4fee\u6539\u65f6\u95f4\uff1a" + fileSafe.getFileModtime() + errorFileCountStr;
                String account = WarnMailUtil.getAccount(fileSafe.getHostname());
                WarnMailUtil.sendUtil(title, commContent, account, key, isDown, fileSafe.getWarnLevel(), WarnMailUtil.getHostGroups(fileSafe.getHostname(), fileSafe.getGroupId()), fileSafe.getHostname());
            }
            catch (Exception e) {
                logger.error("\u6587\u4ef6\u9632\u7be1\u6539\u76d1\u6d4b\u544a\u8b66\u90ae\u4ef6\u9519\u8bef\uff1a", (Throwable)e);
                logInfoService.save("\u6587\u4ef6\u9632\u7be1\u6539\u76d1\u6d4b\u544a\u8b66\u90ae\u4ef6\u9519\u8bef", e.toString(), "1");
            }
        } else {
            WarnPools.removeWarnMark(key);
            try {
                String title = "\u6587\u4ef6\u9632\u7be1\u6539\u76d1\u6d4b\u5df2\u6062\u590d\uff1a" + fileSafe.getFileName() + "\uff0c" + fileSafe.getHostname() + remark;
                String commContent = "\u6587\u4ef6\u9632\u7be1\u6539\u76d1\u6d4b\u5df2\u6062\u590d\uff1a" + fileSafe.getHostname() + remark + "\uff0c\u6587\u4ef6\uff1a" + fileSafe.getFileName() + "\uff0c\u6587\u4ef6\u8def\u5f84\uff1a" + fileSafe.getFilePath() + "\uff0c\u6587\u4ef6\u6700\u540e\u4fee\u6539\u65f6\u95f4\uff1a" + fileSafe.getFileModtime();
                String account = WarnMailUtil.getAccount(fileSafe.getHostname());
                WarnMailUtil.sendUtil(title, commContent, account, key, isDown, fileSafe.getWarnLevel(), WarnMailUtil.getHostGroups(fileSafe.getHostname(), fileSafe.getGroupId()), fileSafe.getHostname());
            }
            catch (Exception e) {
                logger.error("\u6587\u4ef6\u9632\u7be1\u6539\u76d1\u6d4b\u5df2\u6062\u590d\u544a\u8b66\u90ae\u4ef6\u9519\u8bef", (Throwable)e);
                logInfoService.save("\u6587\u4ef6\u9632\u7be1\u6539\u76d1\u6d4b\u5df2\u6062\u590d\u8b66\u90ae\u4ef6\u9519\u8bef", e.toString(), "1");
            }
        }
        return false;
    }

    public static boolean sendCustomInfoDown(CustomInfo customInfo, boolean isDown) {
        if ("false".equals(mailConfig.getAllWarnMail()) || "false".equals(mailConfig.getCustomInfoWarnMail())) {
            return false;
        }
        HostWarnDiy hostWarnDiyDto = StaticKeys.HOST_WARN_MAP.get(customInfo.getHostname());
        if (null != hostWarnDiyDto && "1".equals(hostWarnDiyDto.getActive()) && "yes".equals(hostWarnDiyDto.getHostBlockAllWarn())) {
            return false;
        }
        String key = customInfo.getId();
        String remark = HostUtil.addRemark(customInfo.getHostname());
        try {
            if (isDown) {
                if (WarnPools.checkWarnCacheTimes(key)) {
                    return false;
                }
                String title = "\u81ea\u5b9a\u4e49\u76d1\u63a7\u9879\u544a\u8b66\uff1a" + customInfo.getCustomName() + "\uff0c" + customInfo.getHostname() + remark;
                String commContent = "\u81ea\u5b9a\u4e49\u76d1\u63a7\u9879\u544a\u8b66\uff1a" + customInfo.getHostname() + remark + "\uff0c" + customInfo.getCustomName() + "\uff0c\u544a\u8b66\u8868\u8fbe\u5f0f\u6210\u7acb\uff1a" + customInfo.getResultExp() + "\uff0cresult\u5f53\u524d\u503c\u4e3a\uff1a" + customInfo.getCustomValue();
                String account = WarnMailUtil.getAccount(customInfo.getHostname());
                WarnMailUtil.sendUtil(title, commContent, account, key, true, customInfo.getWarnLevel(), WarnMailUtil.getHostGroups(customInfo.getHostname(), customInfo.getGroupId()), customInfo.getHostname());
            } else {
                WarnPools.removeWarnMark(key);
                String title = "\u81ea\u5b9a\u4e49\u76d1\u63a7\u9879\u5df2\u6062\u590d\uff1a" + customInfo.getCustomName() + "\uff0c" + customInfo.getHostname() + remark;
                String commContent = "\u81ea\u5b9a\u4e49\u76d1\u63a7\u9879\u5df2\u6062\u590d\uff1a" + customInfo.getHostname() + remark + "\uff0c" + customInfo.getCustomName() + "\uff0c\u544a\u8b66\u8868\u8fbe\u5f0f\u4e0d\u6210\u7acb\uff1a" + customInfo.getResultExp() + "\uff0cresult\u5f53\u524d\u503c\u4e3a\uff1a" + customInfo.getCustomValue();
                String account = WarnMailUtil.getAccount(customInfo.getHostname());
                WarnMailUtil.sendUtil(title, commContent, account, key, isDown, customInfo.getWarnLevel(), WarnMailUtil.getHostGroups(customInfo.getHostname(), customInfo.getGroupId()), customInfo.getHostname());
            }
        }
        catch (Exception e) {
            logger.error("\u53d1\u9001\u81ea\u5b9a\u4e49\u76d1\u63a7\u9879\u544a\u8b66\u90ae\u4ef6\u9519\u8bef", (Throwable)e);
            logInfoService.save("\u53d1\u9001\u81ea\u5b9a\u4e49\u76d1\u63a7\u9879\u544a\u8b66\u90ae\u4ef6\u9519\u8bef", e.toString(), "1");
        }
        return false;
    }

    public static boolean sendHostMacInfoDown(String bindIp, String delMacAddress) {
        if ("false".equals(mailConfig.getAllWarnMail()) || "false".equals(mailConfig.getMacInfoWarnMail())) {
            return false;
        }
        String remark = HostUtil.addRemark(bindIp);
        try {
            String title = "\u4e3b\u673aMAC\u5730\u5740\u53d8\u5316\u544a\u8b66\uff1a" + bindIp + remark;
            String commContent = "\u4e3b\u673aMAC\u5730\u5740\u53d8\u5316\u544a\u8b66\uff1a" + bindIp + remark + "\uff0c\u4e22\u5931\u7684MAC\u5730\u5740\u5305\u62ec\uff1a" + delMacAddress;
            String account = WarnMailUtil.getAccount(bindIp);
            WarnMailUtil.sendUtil(title, commContent, account, "", true, "WARN", WarnMailUtil.getHostGroups(bindIp, ""), bindIp);
        }
        catch (Exception e) {
            logger.error("\u53d1\u9001mac\u5730\u5740\u53d8\u5316\u544a\u8b66\u90ae\u4ef6\u9519\u8bef", (Throwable)e);
            logInfoService.save("\u53d1\u9001mac\u5730\u5740\u53d8\u5316\u544a\u8b66\u90ae\u4ef6\u9519\u8bef", e.toString(), "1");
        }
        return false;
    }

    public static boolean sendFileWarnDown(FileWarnInfo fileWarnInfo, FileWarnState state, String filePath, String warnContent, boolean isDown) {
        if ("false".equals(mailConfig.getAllWarnMail()) || "false".equals(mailConfig.getFileLogWarnMail())) {
            return false;
        }
        HostWarnDiy hostWarnDiyDto = StaticKeys.HOST_WARN_MAP.get(fileWarnInfo.getHostname());
        if (null != hostWarnDiyDto && "1".equals(hostWarnDiyDto.getActive()) && "yes".equals(hostWarnDiyDto.getHostBlockAllWarn())) {
            return false;
        }
        String key = fileWarnInfo.getId();
        String remark = HostUtil.addRemark(fileWarnInfo.getHostname());
        String fileRemark = "\u65e0";
        if (!StringUtils.isEmpty((CharSequence)fileWarnInfo.getRemark())) {
            fileRemark = fileWarnInfo.getRemark();
        }
        if (isDown) {
            try {
                String title = "\u65e5\u5fd7\u76d1\u63a7\u544a\u8b66\uff1a" + fileWarnInfo.getHostname() + remark;
                String commContent = "\u65e5\u5fd7\u76d1\u63a7\u544a\u8b66\uff1a" + fileWarnInfo.getHostname() + remark + "\uff0c\u65e5\u5fd7\u5907\u6ce8\uff1a" + fileRemark + "\uff0c\u65e5\u5fd7\u6587\u4ef6\uff1a" + filePath + "\uff0c\u672c\u6b21\u626b\u63cf\u65e5\u5fd7\u884c\u6570" + state.getRowsCount() + "\uff0c\u672c\u6b21\u544a\u8b66\u65e5\u5fd7\u884c\u6570" + state.getRowsGatherCount() + "\uff0c" + warnContent;
                String account = WarnMailUtil.getAccount(fileWarnInfo.getHostname());
                WarnMailUtil.sendUtil(title, commContent, account, "", false, fileWarnInfo.getWarnLevel(), WarnMailUtil.getHostGroups(fileWarnInfo.getHostname(), ""), fileWarnInfo.getHostname());
            }
            catch (Exception e) {
                logger.error("\u53d1\u9001\u65e5\u5fd7\u76d1\u63a7\u544a\u8b66\u90ae\u4ef6\u9519\u8bef", (Throwable)e);
                logInfoService.save("\u53d1\u9001\u65e5\u5fd7\u76d1\u63a7\u544a\u8b66\u9519\u8bef", e.toString(), "1");
            }
        }
        return false;
    }

    public static String getAccount(String hostname) {
        if ("true".equals(commonConfig.getUserInfoManage()) && !StringUtils.isEmpty((CharSequence)hostname)) {
            return StaticKeys.HOST_ACCOUNT_MAP.get(hostname);
        }
        return "";
    }

    public static void sendUtil(String title, String commContent, String account, String key, boolean isDown, String warnLevel, String groupIds, String hostName) {
        HostWarnDiy hostWarnDiyDto = StaticKeys.HOST_WARN_MAP.get(hostName);
        if (!(null == hostWarnDiyDto || !"1".equals(hostWarnDiyDto.getActive()) || StringUtils.isEmpty((CharSequence)hostWarnDiyDto.getCustomWarnMail()) && StringUtils.isEmpty((CharSequence)hostWarnDiyDto.getCustomWarnAccountKey()))) {
            WarnOtherUtil.sendUtilToCustomMail(title, commContent, hostWarnDiyDto.getCustomWarnMail(), hostWarnDiyDto.getCustomWarnAccountKey(), key, isDown, warnLevel, groupIds);
            return;
        }
        WarnOtherUtil.sendUtil(title, commContent, account, key, isDown, warnLevel, groupIds);
    }

    public static String getHostGroups(String bindIp, String childGroupId) {
        if (!StringUtils.isEmpty((CharSequence)childGroupId)) {
            return childGroupId;
        }
        try {
            SystemInfo systemInfo = systemInfoService.selectByHostname(bindIp);
            if (null != systemInfo) {
                return systemInfo.getGroupId();
            }
        }
        catch (Exception e) {
            logger.error("\u67e5\u8be2\u4e3b\u673a\u6807\u7b7e\u9519\u8bef", (Throwable)e);
        }
        return "";
    }

    private static boolean isOutWarnCount(String key, Integer warnCount) {
        if (WarnPools.HOST_WARN_COUNT_MAP.get(key) == null) {
            WarnPools.HOST_WARN_COUNT_MAP.put(key, 1);
        } else {
            WarnPools.HOST_WARN_COUNT_MAP.put(key, ((Integer)WarnPools.HOST_WARN_COUNT_MAP.get(key) + 1));
        }
        return (Integer)WarnPools.HOST_WARN_COUNT_MAP.get(key) >= warnCount;
    }
}

