/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.config;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix="mail")
public class MailConfig {
    private Double memWarnVal = 95.0;
    private Double cpuWarnVal = 95.0;
    private Double netConnectionsWarnVal = 1000.0;
    private Double upSpeedVal = 10240.0;
    private Double upSpeedMinVal = 0.0;
    private Double downSpeedVal = 10240.0;
    private Double downSpeedMinVal = 0.0;
    private Double cpuTemperatureWarnVal = 90.0;
    private Double diskWarnVal = 95.0;
    private String memWarnMail;
    private String upSpeedMail;
    private String downSpeedMail;
    private String cpuWarnMail;
    private String netConnectionsWarnMail;
    private String cpuTemperatureWarnMail;
    private String diskWarnMail;
    private String smartWarnMail;
    private String hostDownWarnMail;
    private Integer hostDownWarnCount;
    private String appDownWarnMail;
    private String dockerDownWarnMail;
    private String dbDownWarnMail;
    private String heathWarnMail;
    private String ftpWarnMail;
    private Integer heathWarnCount;
    private String dceWarnMail;
    private Integer dceWarnCount;
    private String snmpWarnMail;
    private Integer snmpWarnCount;
    private String allWarnMail;
    private String warnCronTime;
    private String warnScript;
    private String recoverScript;
    private String warnToUnicode;
    private String diskBlock;
    private String fileLogWarnMail;
    private String portWarnMail;
    private String fileSafeWarnMail;
    private String sysLoadWarnMail;
    private Double sysLoadWarnVal = 10.0;
    private String shellWarnMail;
    private String customInfoWarnMail;
    private String hostLoginWarnMail;
    private String macInfoWarnMail;
    private String javaXmail;
    private Integer memWarnCount;
    private Integer cpuWarnCount;
    private Integer portWarnCount;
    private String diskBlockSave;
    private String middlewareWarnMail;
    private String lastWeekWarnMail;
    private String diskIoSpeedWarnMail;
    private Double diskIoSpeedWarnVal = 200.0;
    private String autoCallBackWarnMail;
    private String aiAnalyzeScript;

    public Double getMemWarnVal() {
        if (this.memWarnVal == null) {
            return 95.0;
        }
        return this.memWarnVal;
    }

    public void setMemWarnVal(Double memWarnVal) {
        this.memWarnVal = memWarnVal;
    }

    public Double getCpuWarnVal() {
        if (this.cpuWarnVal == null) {
            return 95.0;
        }
        return this.cpuWarnVal;
    }

    public void setCpuWarnVal(Double cpuWarnVal) {
        this.cpuWarnVal = cpuWarnVal;
    }

    public String getMemWarnMail() {
        if (StringUtils.isEmpty((CharSequence)this.memWarnMail)) {
            return "true";
        }
        return this.memWarnMail;
    }

    public void setMemWarnMail(String memWarnMail) {
        this.memWarnMail = memWarnMail;
    }

    public String getCpuWarnMail() {
        if (StringUtils.isEmpty((CharSequence)this.cpuWarnMail)) {
            return "true";
        }
        return this.cpuWarnMail;
    }

    public void setCpuWarnMail(String cpuWarnMail) {
        this.cpuWarnMail = cpuWarnMail;
    }

    public String getHostDownWarnMail() {
        if (StringUtils.isEmpty((CharSequence)this.hostDownWarnMail)) {
            return "true";
        }
        return this.hostDownWarnMail;
    }

    public void setHostDownWarnMail(String hostDownWarnMail) {
        this.hostDownWarnMail = hostDownWarnMail;
    }

    public String getAppDownWarnMail() {
        if (StringUtils.isEmpty((CharSequence)this.appDownWarnMail)) {
            return "true";
        }
        return this.appDownWarnMail;
    }

    public void setAppDownWarnMail(String appDownWarnMail) {
        this.appDownWarnMail = appDownWarnMail;
    }

    public String getDockerDownWarnMail() {
        if (StringUtils.isEmpty((CharSequence)this.dockerDownWarnMail)) {
            return "true";
        }
        return this.dockerDownWarnMail;
    }

    public void setDockerDownWarnMail(String dockerDownWarnMail) {
        this.dockerDownWarnMail = dockerDownWarnMail;
    }

    public String getHeathWarnMail() {
        if (StringUtils.isEmpty((CharSequence)this.heathWarnMail)) {
            return "true";
        }
        return this.heathWarnMail;
    }

    public void setHeathWarnMail(String heathWarnMail) {
        this.heathWarnMail = heathWarnMail;
    }

    public String getAllWarnMail() {
        if (StringUtils.isEmpty((CharSequence)this.allWarnMail)) {
            return "true";
        }
        return this.allWarnMail;
    }

    public void setAllWarnMail(String allWarnMail) {
        this.allWarnMail = allWarnMail;
    }

    public String getDbDownWarnMail() {
        if (StringUtils.isEmpty((CharSequence)this.dbDownWarnMail)) {
            return "true";
        }
        return this.dbDownWarnMail;
    }

    public void setDbDownWarnMail(String dbDownWarnMail) {
        this.dbDownWarnMail = dbDownWarnMail;
    }

    public String getWarnScript() {
        return this.warnScript;
    }

    public void setWarnScript(String warnScript) {
        this.warnScript = warnScript;
    }

    public String getWarnToUnicode() {
        if (StringUtils.isEmpty((CharSequence)this.warnToUnicode)) {
            this.warnToUnicode = "false";
        }
        return this.warnToUnicode;
    }

    public void setWarnToUnicode(String warnToUnicode) {
        this.warnToUnicode = warnToUnicode;
    }

    public Double getDiskWarnVal() {
        if (this.diskWarnVal == null) {
            return 95.0;
        }
        return this.diskWarnVal;
    }

    public void setDiskWarnVal(Double diskWarnVal) {
        this.diskWarnVal = diskWarnVal;
    }

    public String getDiskWarnMail() {
        if (StringUtils.isEmpty((CharSequence)this.diskWarnMail)) {
            return "true";
        }
        return this.diskWarnMail;
    }

    public void setDiskWarnMail(String diskWarnMail) {
        this.diskWarnMail = diskWarnMail;
    }

    public String getDiskBlock() {
        return this.diskBlock;
    }

    public void setDiskBlock(String diskBlock) {
        this.diskBlock = diskBlock;
    }

    public String getFileLogWarnMail() {
        if (StringUtils.isEmpty((CharSequence)this.fileLogWarnMail)) {
            return "true";
        }
        return this.fileLogWarnMail;
    }

    public void setFileLogWarnMail(String fileLogWarnMail) {
        this.fileLogWarnMail = fileLogWarnMail;
    }

    public String getPortWarnMail() {
        if (StringUtils.isEmpty((CharSequence)this.portWarnMail)) {
            return "true";
        }
        return this.portWarnMail;
    }

    public void setPortWarnMail(String portWarnMail) {
        this.portWarnMail = portWarnMail;
    }

    public Double getCpuTemperatureWarnVal() {
        if (this.cpuTemperatureWarnVal == null) {
            return 90.0;
        }
        return this.cpuTemperatureWarnVal;
    }

    public void setCpuTemperatureWarnVal(Double cpuTemperatureWarnVal) {
        this.cpuTemperatureWarnVal = cpuTemperatureWarnVal;
    }

    public String getCpuTemperatureWarnMail() {
        if (StringUtils.isEmpty((CharSequence)this.cpuTemperatureWarnMail)) {
            return "false";
        }
        return this.cpuTemperatureWarnMail;
    }

    public void setCpuTemperatureWarnMail(String cpuTemperatureWarnMail) {
        this.cpuTemperatureWarnMail = cpuTemperatureWarnMail;
    }

    public Double getUpSpeedVal() {
        if (this.upSpeedVal == null) {
            return 10240.0;
        }
        return this.upSpeedVal;
    }

    public void setUpSpeedVal(Double upSpeedVal) {
        this.upSpeedVal = upSpeedVal;
    }

    public Double getDownSpeedVal() {
        if (this.downSpeedVal == null) {
            return 10240.0;
        }
        return this.downSpeedVal;
    }

    public void setDownSpeedVal(Double downSpeedVal) {
        this.downSpeedVal = downSpeedVal;
    }

    public String getUpSpeedMail() {
        if (StringUtils.isEmpty((CharSequence)this.upSpeedMail)) {
            return "true";
        }
        return this.upSpeedMail;
    }

    public void setUpSpeedMail(String upSpeedMail) {
        this.upSpeedMail = upSpeedMail;
    }

    public String getDownSpeedMail() {
        if (StringUtils.isEmpty((CharSequence)this.downSpeedMail)) {
            return "true";
        }
        return this.downSpeedMail;
    }

    public void setDownSpeedMail(String downSpeedMail) {
        this.downSpeedMail = downSpeedMail;
    }

    public String getDceWarnMail() {
        if (StringUtils.isEmpty((CharSequence)this.dceWarnMail)) {
            return "true";
        }
        return this.dceWarnMail;
    }

    public void setDceWarnMail(String dceWarnMail) {
        this.dceWarnMail = dceWarnMail;
    }

    public String getSysLoadWarnMail() {
        if (StringUtils.isEmpty((CharSequence)this.sysLoadWarnMail)) {
            return "true";
        }
        return this.sysLoadWarnMail;
    }

    public void setSysLoadWarnMail(String sysLoadWarnMail) {
        this.sysLoadWarnMail = sysLoadWarnMail;
    }

    public Double getSysLoadWarnVal() {
        if (this.sysLoadWarnVal == null) {
            return 10.0;
        }
        return this.sysLoadWarnVal;
    }

    public void setSysLoadWarnVal(Double sysLoadWarnVal) {
        this.sysLoadWarnVal = sysLoadWarnVal;
    }

    public String getSmartWarnMail() {
        if (StringUtils.isEmpty((CharSequence)this.smartWarnMail)) {
            return "true";
        }
        return this.smartWarnMail;
    }

    public void setSmartWarnMail(String smartWarnMail) {
        this.smartWarnMail = smartWarnMail;
    }

    public String getSnmpWarnMail() {
        if (StringUtils.isEmpty((CharSequence)this.snmpWarnMail)) {
            return "true";
        }
        return this.snmpWarnMail;
    }

    public void setSnmpWarnMail(String snmpWarnMail) {
        this.snmpWarnMail = snmpWarnMail;
    }

    public String getWarnCronTime() {
        return this.warnCronTime;
    }

    public void setWarnCronTime(String warnCronTime) {
        this.warnCronTime = warnCronTime;
    }

    public String getShellWarnMail() {
        if (StringUtils.isEmpty((CharSequence)this.shellWarnMail)) {
            return "true";
        }
        return this.shellWarnMail;
    }

    public void setShellWarnMail(String shellWarnMail) {
        this.shellWarnMail = shellWarnMail;
    }

    public String getCustomInfoWarnMail() {
        if (StringUtils.isEmpty((CharSequence)this.customInfoWarnMail)) {
            return "true";
        }
        return this.customInfoWarnMail;
    }

    public void setCustomInfoWarnMail(String customInfoWarnMail) {
        this.customInfoWarnMail = customInfoWarnMail;
    }

    public String getFileSafeWarnMail() {
        if (StringUtils.isEmpty((CharSequence)this.fileSafeWarnMail)) {
            return "true";
        }
        return this.fileSafeWarnMail;
    }

    public void setFileSafeWarnMail(String fileSafeWarnMail) {
        this.fileSafeWarnMail = fileSafeWarnMail;
    }

    public Integer getHeathWarnCount() {
        if (this.heathWarnCount == null) {
            return 1;
        }
        return this.heathWarnCount;
    }

    public void setHeathWarnCount(Integer heathWarnCount) {
        this.heathWarnCount = heathWarnCount;
    }

    public Integer getDceWarnCount() {
        if (this.dceWarnCount == null) {
            return 1;
        }
        return this.dceWarnCount;
    }

    public void setDceWarnCount(Integer dceWarnCount) {
        this.dceWarnCount = dceWarnCount;
    }

    public String getJavaXmail() {
        if (StringUtils.isEmpty((CharSequence)this.javaXmail)) {
            return "flase";
        }
        return this.javaXmail;
    }

    public void setJavaXmail(String javaXmail) {
        this.javaXmail = javaXmail;
    }

    public Double getUpSpeedMinVal() {
        if (this.upSpeedMinVal == null) {
            return 0.0;
        }
        return this.upSpeedMinVal;
    }

    public void setUpSpeedMinVal(Double upSpeedMinVal) {
        this.upSpeedMinVal = upSpeedMinVal;
    }

    public Double getDownSpeedMinVal() {
        if (this.downSpeedMinVal == null) {
            return 0.0;
        }
        return this.downSpeedMinVal;
    }

    public void setDownSpeedMinVal(Double downSpeedMinVal) {
        this.downSpeedMinVal = downSpeedMinVal;
    }

    public String getFtpWarnMail() {
        if (StringUtils.isEmpty((CharSequence)this.ftpWarnMail)) {
            return "true";
        }
        return this.ftpWarnMail;
    }

    public void setFtpWarnMail(String ftpWarnMail) {
        this.ftpWarnMail = ftpWarnMail;
    }

    public String getHostLoginWarnMail() {
        if (StringUtils.isEmpty((CharSequence)this.hostLoginWarnMail)) {
            return "true";
        }
        return this.hostLoginWarnMail;
    }

    public void setHostLoginWarnMail(String hostLoginWarnMail) {
        this.hostLoginWarnMail = hostLoginWarnMail;
    }

    public String getMacInfoWarnMail() {
        if (StringUtils.isEmpty((CharSequence)this.macInfoWarnMail)) {
            return "true";
        }
        return this.macInfoWarnMail;
    }

    public void setMacInfoWarnMail(String macInfoWarnMail) {
        this.macInfoWarnMail = macInfoWarnMail;
    }

    public Integer getMemWarnCount() {
        if (this.memWarnCount == null) {
            return 2;
        }
        return this.memWarnCount;
    }

    public void setMemWarnCount(Integer memWarnCount) {
        this.memWarnCount = memWarnCount;
    }

    public Integer getCpuWarnCount() {
        if (this.cpuWarnCount == null) {
            return 2;
        }
        return this.cpuWarnCount;
    }

    public void setCpuWarnCount(Integer cpuWarnCount) {
        this.cpuWarnCount = cpuWarnCount;
    }

    public Integer getPortWarnCount() {
        if (this.portWarnCount == null) {
            return 2;
        }
        return this.portWarnCount;
    }

    public void setPortWarnCount(Integer portWarnCount) {
        this.portWarnCount = portWarnCount;
    }

    public String getRecoverScript() {
        return this.recoverScript;
    }

    public void setRecoverScript(String recoverScript) {
        this.recoverScript = recoverScript;
    }

    public Double getNetConnectionsWarnVal() {
        return this.netConnectionsWarnVal;
    }

    public void setNetConnectionsWarnVal(Double netConnectionsWarnVal) {
        this.netConnectionsWarnVal = netConnectionsWarnVal;
    }

    public String getNetConnectionsWarnMail() {
        if (StringUtils.isEmpty((CharSequence)this.netConnectionsWarnMail)) {
            return "true";
        }
        return this.netConnectionsWarnMail;
    }

    public void setNetConnectionsWarnMail(String netConnectionsWarnMail) {
        this.netConnectionsWarnMail = netConnectionsWarnMail;
    }

    public String getDiskBlockSave() {
        return this.diskBlockSave;
    }

    public void setDiskBlockSave(String diskBlockSave) {
        this.diskBlockSave = diskBlockSave;
    }

    public Integer getHostDownWarnCount() {
        if (this.hostDownWarnCount == null) {
            return 1;
        }
        return this.hostDownWarnCount;
    }

    public void setHostDownWarnCount(Integer hostDownWarnCount) {
        this.hostDownWarnCount = hostDownWarnCount;
    }

    public String getMiddlewareWarnMail() {
        if (StringUtils.isEmpty((CharSequence)this.middlewareWarnMail)) {
            return "true";
        }
        return this.middlewareWarnMail;
    }

    public void setMiddlewareWarnMail(String middlewareWarnMail) {
        this.middlewareWarnMail = middlewareWarnMail;
    }

    public String getLastWeekWarnMail() {
        if (StringUtils.isEmpty((CharSequence)this.lastWeekWarnMail)) {
            return "true";
        }
        return this.lastWeekWarnMail;
    }

    public void setLastWeekWarnMail(String lastWeekWarnMail) {
        this.lastWeekWarnMail = lastWeekWarnMail;
    }

    public String getDiskIoSpeedWarnMail() {
        if (StringUtils.isEmpty((CharSequence)this.diskIoSpeedWarnMail)) {
            return "true";
        }
        return this.diskIoSpeedWarnMail;
    }

    public void setDiskIoSpeedWarnMail(String diskIoSpeedWarnMail) {
        this.diskIoSpeedWarnMail = diskIoSpeedWarnMail;
    }

    public Double getDiskIoSpeedWarnVal() {
        if (this.diskIoSpeedWarnVal == null) {
            return 200.0;
        }
        return this.diskIoSpeedWarnVal;
    }

    public void setDiskIoSpeedWarnVal(Double diskIoSpeedWarnVal) {
        this.diskIoSpeedWarnVal = diskIoSpeedWarnVal;
    }

    public String getAutoCallBackWarnMail() {
        if (StringUtils.isEmpty((CharSequence)this.autoCallBackWarnMail)) {
            return "true";
        }
        return this.autoCallBackWarnMail;
    }

    public void setAutoCallBackWarnMail(String autoCallBackWarnMail) {
        this.autoCallBackWarnMail = autoCallBackWarnMail;
    }

    public Integer getSnmpWarnCount() {
        if (this.snmpWarnCount == null) {
            return 1;
        }
        return this.snmpWarnCount;
    }

    public void setSnmpWarnCount(Integer snmpWarnCount) {
        this.snmpWarnCount = snmpWarnCount;
    }

    public String getAiAnalyzeScript() {
        if (StringUtils.isEmpty((CharSequence)this.aiAnalyzeScript)) {
            return "";
        }
        return this.aiAnalyzeScript;
    }

    public void setAiAnalyzeScript(String aiAnalyzeScript) {
        this.aiAnalyzeScript = aiAnalyzeScript;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof MailConfig)) {
            return false;
        }
        MailConfig other = (MailConfig)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Double this$memWarnVal = this.getMemWarnVal();
        Double other$memWarnVal = other.getMemWarnVal();
        if (this$memWarnVal == null ? other$memWarnVal != null : !((Object)this$memWarnVal).equals(other$memWarnVal)) {
            return false;
        }
        Double this$cpuWarnVal = this.getCpuWarnVal();
        Double other$cpuWarnVal = other.getCpuWarnVal();
        if (this$cpuWarnVal == null ? other$cpuWarnVal != null : !((Object)this$cpuWarnVal).equals(other$cpuWarnVal)) {
            return false;
        }
        Double this$netConnectionsWarnVal = this.getNetConnectionsWarnVal();
        Double other$netConnectionsWarnVal = other.getNetConnectionsWarnVal();
        if (this$netConnectionsWarnVal == null ? other$netConnectionsWarnVal != null : !((Object)this$netConnectionsWarnVal).equals(other$netConnectionsWarnVal)) {
            return false;
        }
        Double this$upSpeedVal = this.getUpSpeedVal();
        Double other$upSpeedVal = other.getUpSpeedVal();
        if (this$upSpeedVal == null ? other$upSpeedVal != null : !((Object)this$upSpeedVal).equals(other$upSpeedVal)) {
            return false;
        }
        Double this$upSpeedMinVal = this.getUpSpeedMinVal();
        Double other$upSpeedMinVal = other.getUpSpeedMinVal();
        if (this$upSpeedMinVal == null ? other$upSpeedMinVal != null : !((Object)this$upSpeedMinVal).equals(other$upSpeedMinVal)) {
            return false;
        }
        Double this$downSpeedVal = this.getDownSpeedVal();
        Double other$downSpeedVal = other.getDownSpeedVal();
        if (this$downSpeedVal == null ? other$downSpeedVal != null : !((Object)this$downSpeedVal).equals(other$downSpeedVal)) {
            return false;
        }
        Double this$downSpeedMinVal = this.getDownSpeedMinVal();
        Double other$downSpeedMinVal = other.getDownSpeedMinVal();
        if (this$downSpeedMinVal == null ? other$downSpeedMinVal != null : !((Object)this$downSpeedMinVal).equals(other$downSpeedMinVal)) {
            return false;
        }
        Double this$cpuTemperatureWarnVal = this.getCpuTemperatureWarnVal();
        Double other$cpuTemperatureWarnVal = other.getCpuTemperatureWarnVal();
        if (this$cpuTemperatureWarnVal == null ? other$cpuTemperatureWarnVal != null : !((Object)this$cpuTemperatureWarnVal).equals(other$cpuTemperatureWarnVal)) {
            return false;
        }
        Double this$diskWarnVal = this.getDiskWarnVal();
        Double other$diskWarnVal = other.getDiskWarnVal();
        if (this$diskWarnVal == null ? other$diskWarnVal != null : !((Object)this$diskWarnVal).equals(other$diskWarnVal)) {
            return false;
        }
        Integer this$hostDownWarnCount = this.getHostDownWarnCount();
        Integer other$hostDownWarnCount = other.getHostDownWarnCount();
        if (this$hostDownWarnCount == null ? other$hostDownWarnCount != null : !((Object)this$hostDownWarnCount).equals(other$hostDownWarnCount)) {
            return false;
        }
        Integer this$heathWarnCount = this.getHeathWarnCount();
        Integer other$heathWarnCount = other.getHeathWarnCount();
        if (this$heathWarnCount == null ? other$heathWarnCount != null : !((Object)this$heathWarnCount).equals(other$heathWarnCount)) {
            return false;
        }
        Integer this$dceWarnCount = this.getDceWarnCount();
        Integer other$dceWarnCount = other.getDceWarnCount();
        if (this$dceWarnCount == null ? other$dceWarnCount != null : !((Object)this$dceWarnCount).equals(other$dceWarnCount)) {
            return false;
        }
        Integer this$snmpWarnCount = this.getSnmpWarnCount();
        Integer other$snmpWarnCount = other.getSnmpWarnCount();
        if (this$snmpWarnCount == null ? other$snmpWarnCount != null : !((Object)this$snmpWarnCount).equals(other$snmpWarnCount)) {
            return false;
        }
        Double this$sysLoadWarnVal = this.getSysLoadWarnVal();
        Double other$sysLoadWarnVal = other.getSysLoadWarnVal();
        if (this$sysLoadWarnVal == null ? other$sysLoadWarnVal != null : !((Object)this$sysLoadWarnVal).equals(other$sysLoadWarnVal)) {
            return false;
        }
        Integer this$memWarnCount = this.getMemWarnCount();
        Integer other$memWarnCount = other.getMemWarnCount();
        if (this$memWarnCount == null ? other$memWarnCount != null : !((Object)this$memWarnCount).equals(other$memWarnCount)) {
            return false;
        }
        Integer this$cpuWarnCount = this.getCpuWarnCount();
        Integer other$cpuWarnCount = other.getCpuWarnCount();
        if (this$cpuWarnCount == null ? other$cpuWarnCount != null : !((Object)this$cpuWarnCount).equals(other$cpuWarnCount)) {
            return false;
        }
        Integer this$portWarnCount = this.getPortWarnCount();
        Integer other$portWarnCount = other.getPortWarnCount();
        if (this$portWarnCount == null ? other$portWarnCount != null : !((Object)this$portWarnCount).equals(other$portWarnCount)) {
            return false;
        }
        Double this$diskIoSpeedWarnVal = this.getDiskIoSpeedWarnVal();
        Double other$diskIoSpeedWarnVal = other.getDiskIoSpeedWarnVal();
        if (this$diskIoSpeedWarnVal == null ? other$diskIoSpeedWarnVal != null : !((Object)this$diskIoSpeedWarnVal).equals(other$diskIoSpeedWarnVal)) {
            return false;
        }
        String this$memWarnMail = this.getMemWarnMail();
        String other$memWarnMail = other.getMemWarnMail();
        if (this$memWarnMail == null ? other$memWarnMail != null : !this$memWarnMail.equals(other$memWarnMail)) {
            return false;
        }
        String this$upSpeedMail = this.getUpSpeedMail();
        String other$upSpeedMail = other.getUpSpeedMail();
        if (this$upSpeedMail == null ? other$upSpeedMail != null : !this$upSpeedMail.equals(other$upSpeedMail)) {
            return false;
        }
        String this$downSpeedMail = this.getDownSpeedMail();
        String other$downSpeedMail = other.getDownSpeedMail();
        if (this$downSpeedMail == null ? other$downSpeedMail != null : !this$downSpeedMail.equals(other$downSpeedMail)) {
            return false;
        }
        String this$cpuWarnMail = this.getCpuWarnMail();
        String other$cpuWarnMail = other.getCpuWarnMail();
        if (this$cpuWarnMail == null ? other$cpuWarnMail != null : !this$cpuWarnMail.equals(other$cpuWarnMail)) {
            return false;
        }
        String this$netConnectionsWarnMail = this.getNetConnectionsWarnMail();
        String other$netConnectionsWarnMail = other.getNetConnectionsWarnMail();
        if (this$netConnectionsWarnMail == null ? other$netConnectionsWarnMail != null : !this$netConnectionsWarnMail.equals(other$netConnectionsWarnMail)) {
            return false;
        }
        String this$cpuTemperatureWarnMail = this.getCpuTemperatureWarnMail();
        String other$cpuTemperatureWarnMail = other.getCpuTemperatureWarnMail();
        if (this$cpuTemperatureWarnMail == null ? other$cpuTemperatureWarnMail != null : !this$cpuTemperatureWarnMail.equals(other$cpuTemperatureWarnMail)) {
            return false;
        }
        String this$diskWarnMail = this.getDiskWarnMail();
        String other$diskWarnMail = other.getDiskWarnMail();
        if (this$diskWarnMail == null ? other$diskWarnMail != null : !this$diskWarnMail.equals(other$diskWarnMail)) {
            return false;
        }
        String this$smartWarnMail = this.getSmartWarnMail();
        String other$smartWarnMail = other.getSmartWarnMail();
        if (this$smartWarnMail == null ? other$smartWarnMail != null : !this$smartWarnMail.equals(other$smartWarnMail)) {
            return false;
        }
        String this$hostDownWarnMail = this.getHostDownWarnMail();
        String other$hostDownWarnMail = other.getHostDownWarnMail();
        if (this$hostDownWarnMail == null ? other$hostDownWarnMail != null : !this$hostDownWarnMail.equals(other$hostDownWarnMail)) {
            return false;
        }
        String this$appDownWarnMail = this.getAppDownWarnMail();
        String other$appDownWarnMail = other.getAppDownWarnMail();
        if (this$appDownWarnMail == null ? other$appDownWarnMail != null : !this$appDownWarnMail.equals(other$appDownWarnMail)) {
            return false;
        }
        String this$dockerDownWarnMail = this.getDockerDownWarnMail();
        String other$dockerDownWarnMail = other.getDockerDownWarnMail();
        if (this$dockerDownWarnMail == null ? other$dockerDownWarnMail != null : !this$dockerDownWarnMail.equals(other$dockerDownWarnMail)) {
            return false;
        }
        String this$dbDownWarnMail = this.getDbDownWarnMail();
        String other$dbDownWarnMail = other.getDbDownWarnMail();
        if (this$dbDownWarnMail == null ? other$dbDownWarnMail != null : !this$dbDownWarnMail.equals(other$dbDownWarnMail)) {
            return false;
        }
        String this$heathWarnMail = this.getHeathWarnMail();
        String other$heathWarnMail = other.getHeathWarnMail();
        if (this$heathWarnMail == null ? other$heathWarnMail != null : !this$heathWarnMail.equals(other$heathWarnMail)) {
            return false;
        }
        String this$ftpWarnMail = this.getFtpWarnMail();
        String other$ftpWarnMail = other.getFtpWarnMail();
        if (this$ftpWarnMail == null ? other$ftpWarnMail != null : !this$ftpWarnMail.equals(other$ftpWarnMail)) {
            return false;
        }
        String this$dceWarnMail = this.getDceWarnMail();
        String other$dceWarnMail = other.getDceWarnMail();
        if (this$dceWarnMail == null ? other$dceWarnMail != null : !this$dceWarnMail.equals(other$dceWarnMail)) {
            return false;
        }
        String this$snmpWarnMail = this.getSnmpWarnMail();
        String other$snmpWarnMail = other.getSnmpWarnMail();
        if (this$snmpWarnMail == null ? other$snmpWarnMail != null : !this$snmpWarnMail.equals(other$snmpWarnMail)) {
            return false;
        }
        String this$allWarnMail = this.getAllWarnMail();
        String other$allWarnMail = other.getAllWarnMail();
        if (this$allWarnMail == null ? other$allWarnMail != null : !this$allWarnMail.equals(other$allWarnMail)) {
            return false;
        }
        String this$warnCronTime = this.getWarnCronTime();
        String other$warnCronTime = other.getWarnCronTime();
        if (this$warnCronTime == null ? other$warnCronTime != null : !this$warnCronTime.equals(other$warnCronTime)) {
            return false;
        }
        String this$warnScript = this.getWarnScript();
        String other$warnScript = other.getWarnScript();
        if (this$warnScript == null ? other$warnScript != null : !this$warnScript.equals(other$warnScript)) {
            return false;
        }
        String this$recoverScript = this.getRecoverScript();
        String other$recoverScript = other.getRecoverScript();
        if (this$recoverScript == null ? other$recoverScript != null : !this$recoverScript.equals(other$recoverScript)) {
            return false;
        }
        String this$warnToUnicode = this.getWarnToUnicode();
        String other$warnToUnicode = other.getWarnToUnicode();
        if (this$warnToUnicode == null ? other$warnToUnicode != null : !this$warnToUnicode.equals(other$warnToUnicode)) {
            return false;
        }
        String this$diskBlock = this.getDiskBlock();
        String other$diskBlock = other.getDiskBlock();
        if (this$diskBlock == null ? other$diskBlock != null : !this$diskBlock.equals(other$diskBlock)) {
            return false;
        }
        String this$fileLogWarnMail = this.getFileLogWarnMail();
        String other$fileLogWarnMail = other.getFileLogWarnMail();
        if (this$fileLogWarnMail == null ? other$fileLogWarnMail != null : !this$fileLogWarnMail.equals(other$fileLogWarnMail)) {
            return false;
        }
        String this$portWarnMail = this.getPortWarnMail();
        String other$portWarnMail = other.getPortWarnMail();
        if (this$portWarnMail == null ? other$portWarnMail != null : !this$portWarnMail.equals(other$portWarnMail)) {
            return false;
        }
        String this$fileSafeWarnMail = this.getFileSafeWarnMail();
        String other$fileSafeWarnMail = other.getFileSafeWarnMail();
        if (this$fileSafeWarnMail == null ? other$fileSafeWarnMail != null : !this$fileSafeWarnMail.equals(other$fileSafeWarnMail)) {
            return false;
        }
        String this$sysLoadWarnMail = this.getSysLoadWarnMail();
        String other$sysLoadWarnMail = other.getSysLoadWarnMail();
        if (this$sysLoadWarnMail == null ? other$sysLoadWarnMail != null : !this$sysLoadWarnMail.equals(other$sysLoadWarnMail)) {
            return false;
        }
        String this$shellWarnMail = this.getShellWarnMail();
        String other$shellWarnMail = other.getShellWarnMail();
        if (this$shellWarnMail == null ? other$shellWarnMail != null : !this$shellWarnMail.equals(other$shellWarnMail)) {
            return false;
        }
        String this$customInfoWarnMail = this.getCustomInfoWarnMail();
        String other$customInfoWarnMail = other.getCustomInfoWarnMail();
        if (this$customInfoWarnMail == null ? other$customInfoWarnMail != null : !this$customInfoWarnMail.equals(other$customInfoWarnMail)) {
            return false;
        }
        String this$hostLoginWarnMail = this.getHostLoginWarnMail();
        String other$hostLoginWarnMail = other.getHostLoginWarnMail();
        if (this$hostLoginWarnMail == null ? other$hostLoginWarnMail != null : !this$hostLoginWarnMail.equals(other$hostLoginWarnMail)) {
            return false;
        }
        String this$macInfoWarnMail = this.getMacInfoWarnMail();
        String other$macInfoWarnMail = other.getMacInfoWarnMail();
        if (this$macInfoWarnMail == null ? other$macInfoWarnMail != null : !this$macInfoWarnMail.equals(other$macInfoWarnMail)) {
            return false;
        }
        String this$javaXmail = this.getJavaXmail();
        String other$javaXmail = other.getJavaXmail();
        if (this$javaXmail == null ? other$javaXmail != null : !this$javaXmail.equals(other$javaXmail)) {
            return false;
        }
        String this$diskBlockSave = this.getDiskBlockSave();
        String other$diskBlockSave = other.getDiskBlockSave();
        if (this$diskBlockSave == null ? other$diskBlockSave != null : !this$diskBlockSave.equals(other$diskBlockSave)) {
            return false;
        }
        String this$middlewareWarnMail = this.getMiddlewareWarnMail();
        String other$middlewareWarnMail = other.getMiddlewareWarnMail();
        if (this$middlewareWarnMail == null ? other$middlewareWarnMail != null : !this$middlewareWarnMail.equals(other$middlewareWarnMail)) {
            return false;
        }
        String this$lastWeekWarnMail = this.getLastWeekWarnMail();
        String other$lastWeekWarnMail = other.getLastWeekWarnMail();
        if (this$lastWeekWarnMail == null ? other$lastWeekWarnMail != null : !this$lastWeekWarnMail.equals(other$lastWeekWarnMail)) {
            return false;
        }
        String this$diskIoSpeedWarnMail = this.getDiskIoSpeedWarnMail();
        String other$diskIoSpeedWarnMail = other.getDiskIoSpeedWarnMail();
        if (this$diskIoSpeedWarnMail == null ? other$diskIoSpeedWarnMail != null : !this$diskIoSpeedWarnMail.equals(other$diskIoSpeedWarnMail)) {
            return false;
        }
        String this$autoCallBackWarnMail = this.getAutoCallBackWarnMail();
        String other$autoCallBackWarnMail = other.getAutoCallBackWarnMail();
        if (this$autoCallBackWarnMail == null ? other$autoCallBackWarnMail != null : !this$autoCallBackWarnMail.equals(other$autoCallBackWarnMail)) {
            return false;
        }
        String this$aiAnalyzeScript = this.getAiAnalyzeScript();
        String other$aiAnalyzeScript = other.getAiAnalyzeScript();
        return !(this$aiAnalyzeScript == null ? other$aiAnalyzeScript != null : !this$aiAnalyzeScript.equals(other$aiAnalyzeScript));
    }

    protected boolean canEqual(Object other) {
        return other instanceof MailConfig;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Double $memWarnVal = this.getMemWarnVal();
        result = result * 59 + ($memWarnVal == null ? 43 : ((Object)$memWarnVal).hashCode());
        Double $cpuWarnVal = this.getCpuWarnVal();
        result = result * 59 + ($cpuWarnVal == null ? 43 : ((Object)$cpuWarnVal).hashCode());
        Double $netConnectionsWarnVal = this.getNetConnectionsWarnVal();
        result = result * 59 + ($netConnectionsWarnVal == null ? 43 : ((Object)$netConnectionsWarnVal).hashCode());
        Double $upSpeedVal = this.getUpSpeedVal();
        result = result * 59 + ($upSpeedVal == null ? 43 : ((Object)$upSpeedVal).hashCode());
        Double $upSpeedMinVal = this.getUpSpeedMinVal();
        result = result * 59 + ($upSpeedMinVal == null ? 43 : ((Object)$upSpeedMinVal).hashCode());
        Double $downSpeedVal = this.getDownSpeedVal();
        result = result * 59 + ($downSpeedVal == null ? 43 : ((Object)$downSpeedVal).hashCode());
        Double $downSpeedMinVal = this.getDownSpeedMinVal();
        result = result * 59 + ($downSpeedMinVal == null ? 43 : ((Object)$downSpeedMinVal).hashCode());
        Double $cpuTemperatureWarnVal = this.getCpuTemperatureWarnVal();
        result = result * 59 + ($cpuTemperatureWarnVal == null ? 43 : ((Object)$cpuTemperatureWarnVal).hashCode());
        Double $diskWarnVal = this.getDiskWarnVal();
        result = result * 59 + ($diskWarnVal == null ? 43 : ((Object)$diskWarnVal).hashCode());
        Integer $hostDownWarnCount = this.getHostDownWarnCount();
        result = result * 59 + ($hostDownWarnCount == null ? 43 : ((Object)$hostDownWarnCount).hashCode());
        Integer $heathWarnCount = this.getHeathWarnCount();
        result = result * 59 + ($heathWarnCount == null ? 43 : ((Object)$heathWarnCount).hashCode());
        Integer $dceWarnCount = this.getDceWarnCount();
        result = result * 59 + ($dceWarnCount == null ? 43 : ((Object)$dceWarnCount).hashCode());
        Integer $snmpWarnCount = this.getSnmpWarnCount();
        result = result * 59 + ($snmpWarnCount == null ? 43 : ((Object)$snmpWarnCount).hashCode());
        Double $sysLoadWarnVal = this.getSysLoadWarnVal();
        result = result * 59 + ($sysLoadWarnVal == null ? 43 : ((Object)$sysLoadWarnVal).hashCode());
        Integer $memWarnCount = this.getMemWarnCount();
        result = result * 59 + ($memWarnCount == null ? 43 : ((Object)$memWarnCount).hashCode());
        Integer $cpuWarnCount = this.getCpuWarnCount();
        result = result * 59 + ($cpuWarnCount == null ? 43 : ((Object)$cpuWarnCount).hashCode());
        Integer $portWarnCount = this.getPortWarnCount();
        result = result * 59 + ($portWarnCount == null ? 43 : ((Object)$portWarnCount).hashCode());
        Double $diskIoSpeedWarnVal = this.getDiskIoSpeedWarnVal();
        result = result * 59 + ($diskIoSpeedWarnVal == null ? 43 : ((Object)$diskIoSpeedWarnVal).hashCode());
        String $memWarnMail = this.getMemWarnMail();
        result = result * 59 + ($memWarnMail == null ? 43 : $memWarnMail.hashCode());
        String $upSpeedMail = this.getUpSpeedMail();
        result = result * 59 + ($upSpeedMail == null ? 43 : $upSpeedMail.hashCode());
        String $downSpeedMail = this.getDownSpeedMail();
        result = result * 59 + ($downSpeedMail == null ? 43 : $downSpeedMail.hashCode());
        String $cpuWarnMail = this.getCpuWarnMail();
        result = result * 59 + ($cpuWarnMail == null ? 43 : $cpuWarnMail.hashCode());
        String $netConnectionsWarnMail = this.getNetConnectionsWarnMail();
        result = result * 59 + ($netConnectionsWarnMail == null ? 43 : $netConnectionsWarnMail.hashCode());
        String $cpuTemperatureWarnMail = this.getCpuTemperatureWarnMail();
        result = result * 59 + ($cpuTemperatureWarnMail == null ? 43 : $cpuTemperatureWarnMail.hashCode());
        String $diskWarnMail = this.getDiskWarnMail();
        result = result * 59 + ($diskWarnMail == null ? 43 : $diskWarnMail.hashCode());
        String $smartWarnMail = this.getSmartWarnMail();
        result = result * 59 + ($smartWarnMail == null ? 43 : $smartWarnMail.hashCode());
        String $hostDownWarnMail = this.getHostDownWarnMail();
        result = result * 59 + ($hostDownWarnMail == null ? 43 : $hostDownWarnMail.hashCode());
        String $appDownWarnMail = this.getAppDownWarnMail();
        result = result * 59 + ($appDownWarnMail == null ? 43 : $appDownWarnMail.hashCode());
        String $dockerDownWarnMail = this.getDockerDownWarnMail();
        result = result * 59 + ($dockerDownWarnMail == null ? 43 : $dockerDownWarnMail.hashCode());
        String $dbDownWarnMail = this.getDbDownWarnMail();
        result = result * 59 + ($dbDownWarnMail == null ? 43 : $dbDownWarnMail.hashCode());
        String $heathWarnMail = this.getHeathWarnMail();
        result = result * 59 + ($heathWarnMail == null ? 43 : $heathWarnMail.hashCode());
        String $ftpWarnMail = this.getFtpWarnMail();
        result = result * 59 + ($ftpWarnMail == null ? 43 : $ftpWarnMail.hashCode());
        String $dceWarnMail = this.getDceWarnMail();
        result = result * 59 + ($dceWarnMail == null ? 43 : $dceWarnMail.hashCode());
        String $snmpWarnMail = this.getSnmpWarnMail();
        result = result * 59 + ($snmpWarnMail == null ? 43 : $snmpWarnMail.hashCode());
        String $allWarnMail = this.getAllWarnMail();
        result = result * 59 + ($allWarnMail == null ? 43 : $allWarnMail.hashCode());
        String $warnCronTime = this.getWarnCronTime();
        result = result * 59 + ($warnCronTime == null ? 43 : $warnCronTime.hashCode());
        String $warnScript = this.getWarnScript();
        result = result * 59 + ($warnScript == null ? 43 : $warnScript.hashCode());
        String $recoverScript = this.getRecoverScript();
        result = result * 59 + ($recoverScript == null ? 43 : $recoverScript.hashCode());
        String $warnToUnicode = this.getWarnToUnicode();
        result = result * 59 + ($warnToUnicode == null ? 43 : $warnToUnicode.hashCode());
        String $diskBlock = this.getDiskBlock();
        result = result * 59 + ($diskBlock == null ? 43 : $diskBlock.hashCode());
        String $fileLogWarnMail = this.getFileLogWarnMail();
        result = result * 59 + ($fileLogWarnMail == null ? 43 : $fileLogWarnMail.hashCode());
        String $portWarnMail = this.getPortWarnMail();
        result = result * 59 + ($portWarnMail == null ? 43 : $portWarnMail.hashCode());
        String $fileSafeWarnMail = this.getFileSafeWarnMail();
        result = result * 59 + ($fileSafeWarnMail == null ? 43 : $fileSafeWarnMail.hashCode());
        String $sysLoadWarnMail = this.getSysLoadWarnMail();
        result = result * 59 + ($sysLoadWarnMail == null ? 43 : $sysLoadWarnMail.hashCode());
        String $shellWarnMail = this.getShellWarnMail();
        result = result * 59 + ($shellWarnMail == null ? 43 : $shellWarnMail.hashCode());
        String $customInfoWarnMail = this.getCustomInfoWarnMail();
        result = result * 59 + ($customInfoWarnMail == null ? 43 : $customInfoWarnMail.hashCode());
        String $hostLoginWarnMail = this.getHostLoginWarnMail();
        result = result * 59 + ($hostLoginWarnMail == null ? 43 : $hostLoginWarnMail.hashCode());
        String $macInfoWarnMail = this.getMacInfoWarnMail();
        result = result * 59 + ($macInfoWarnMail == null ? 43 : $macInfoWarnMail.hashCode());
        String $javaXmail = this.getJavaXmail();
        result = result * 59 + ($javaXmail == null ? 43 : $javaXmail.hashCode());
        String $diskBlockSave = this.getDiskBlockSave();
        result = result * 59 + ($diskBlockSave == null ? 43 : $diskBlockSave.hashCode());
        String $middlewareWarnMail = this.getMiddlewareWarnMail();
        result = result * 59 + ($middlewareWarnMail == null ? 43 : $middlewareWarnMail.hashCode());
        String $lastWeekWarnMail = this.getLastWeekWarnMail();
        result = result * 59 + ($lastWeekWarnMail == null ? 43 : $lastWeekWarnMail.hashCode());
        String $diskIoSpeedWarnMail = this.getDiskIoSpeedWarnMail();
        result = result * 59 + ($diskIoSpeedWarnMail == null ? 43 : $diskIoSpeedWarnMail.hashCode());
        String $autoCallBackWarnMail = this.getAutoCallBackWarnMail();
        result = result * 59 + ($autoCallBackWarnMail == null ? 43 : $autoCallBackWarnMail.hashCode());
        String $aiAnalyzeScript = this.getAiAnalyzeScript();
        result = result * 59 + ($aiAnalyzeScript == null ? 43 : $aiAnalyzeScript.hashCode());
        return result;
    }

    public String toString() {
        return "MailConfig(memWarnVal=" + this.getMemWarnVal() + ", cpuWarnVal=" + this.getCpuWarnVal() + ", netConnectionsWarnVal=" + this.getNetConnectionsWarnVal() + ", upSpeedVal=" + this.getUpSpeedVal() + ", upSpeedMinVal=" + this.getUpSpeedMinVal() + ", downSpeedVal=" + this.getDownSpeedVal() + ", downSpeedMinVal=" + this.getDownSpeedMinVal() + ", cpuTemperatureWarnVal=" + this.getCpuTemperatureWarnVal() + ", diskWarnVal=" + this.getDiskWarnVal() + ", memWarnMail=" + this.getMemWarnMail() + ", upSpeedMail=" + this.getUpSpeedMail() + ", downSpeedMail=" + this.getDownSpeedMail() + ", cpuWarnMail=" + this.getCpuWarnMail() + ", netConnectionsWarnMail=" + this.getNetConnectionsWarnMail() + ", cpuTemperatureWarnMail=" + this.getCpuTemperatureWarnMail() + ", diskWarnMail=" + this.getDiskWarnMail() + ", smartWarnMail=" + this.getSmartWarnMail() + ", hostDownWarnMail=" + this.getHostDownWarnMail() + ", hostDownWarnCount=" + this.getHostDownWarnCount() + ", appDownWarnMail=" + this.getAppDownWarnMail() + ", dockerDownWarnMail=" + this.getDockerDownWarnMail() + ", dbDownWarnMail=" + this.getDbDownWarnMail() + ", heathWarnMail=" + this.getHeathWarnMail() + ", ftpWarnMail=" + this.getFtpWarnMail() + ", heathWarnCount=" + this.getHeathWarnCount() + ", dceWarnMail=" + this.getDceWarnMail() + ", dceWarnCount=" + this.getDceWarnCount() + ", snmpWarnMail=" + this.getSnmpWarnMail() + ", snmpWarnCount=" + this.getSnmpWarnCount() + ", allWarnMail=" + this.getAllWarnMail() + ", warnCronTime=" + this.getWarnCronTime() + ", warnScript=" + this.getWarnScript() + ", recoverScript=" + this.getRecoverScript() + ", warnToUnicode=" + this.getWarnToUnicode() + ", diskBlock=" + this.getDiskBlock() + ", fileLogWarnMail=" + this.getFileLogWarnMail() + ", portWarnMail=" + this.getPortWarnMail() + ", fileSafeWarnMail=" + this.getFileSafeWarnMail() + ", sysLoadWarnMail=" + this.getSysLoadWarnMail() + ", sysLoadWarnVal=" + this.getSysLoadWarnVal() + ", shellWarnMail=" + this.getShellWarnMail() + ", customInfoWarnMail=" + this.getCustomInfoWarnMail() + ", hostLoginWarnMail=" + this.getHostLoginWarnMail() + ", macInfoWarnMail=" + this.getMacInfoWarnMail() + ", javaXmail=" + this.getJavaXmail() + ", memWarnCount=" + this.getMemWarnCount() + ", cpuWarnCount=" + this.getCpuWarnCount() + ", portWarnCount=" + this.getPortWarnCount() + ", diskBlockSave=" + this.getDiskBlockSave() + ", middlewareWarnMail=" + this.getMiddlewareWarnMail() + ", lastWeekWarnMail=" + this.getLastWeekWarnMail() + ", diskIoSpeedWarnMail=" + this.getDiskIoSpeedWarnMail() + ", diskIoSpeedWarnVal=" + this.getDiskIoSpeedWarnVal() + ", autoCallBackWarnMail=" + this.getAutoCallBackWarnMail() + ", aiAnalyzeScript=" + this.getAiAnalyzeScript() + ")";
    }
}

