/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.entity;

import com.wgcloud.entity.BaseEntity;
import com.wgcloud.util.DateUtil;
import com.wgcloud.util.FormatUtil;
import java.util.Date;
import org.apache.commons.lang3.StringUtils;

public class SystemInfoExt
extends BaseEntity {
    private static final long serialVersionUID = 1L;
    private String hostname;
    private String platForm;
    private String platformVersion;
    private Long uptime;
    private String uptimeStr;
    private Long bootTime;
    private String bootTimeStr;
    private String cpuXh;
    private String agentVer;
    private Date createTime;
    private String submitSeconds;
    private String bytesRecv;
    private String bytesSent;
    private String hostnameExt;
    private String totalSwapMem;
    private String swapMemPer;
    private String cpuPhysicalid;
    private String cpuFamily;
    private String cpuMhz;
    private String kernelArch;
    private String netInterfaceName;

    public String getHostname() {
        return this.hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getPlatForm() {
        return this.platForm;
    }

    public void setPlatForm(String platForm) {
        this.platForm = platForm;
    }

    public String getPlatformVersion() {
        return this.platformVersion;
    }

    public void setPlatformVersion(String platformVersion) {
        this.platformVersion = platformVersion;
    }

    public Long getUptime() {
        return this.uptime;
    }

    public void setUptime(Long uptime) {
        this.uptime = uptime;
    }

    public String getUptimeStr() {
        return FormatUtil.timesToDay(this.uptime);
    }

    public void setUptimeStr(String uptimeStr) {
        this.uptimeStr = uptimeStr;
    }

    public Long getBootTime() {
        return this.bootTime;
    }

    public void setBootTime(Long bootTime) {
        this.bootTime = bootTime;
    }

    public String getBootTimeStr() {
        return DateUtil.secondToDate(this.bootTime, "yyyy-MM-dd HH:mm:ss");
    }

    public void setBootTimeStr(String bootTimeStr) {
        this.bootTimeStr = bootTimeStr;
    }

    public String getCpuXh() {
        return this.cpuXh;
    }

    public void setCpuXh(String cpuXh) {
        this.cpuXh = cpuXh;
    }

    public String getAgentVer() {
        return this.agentVer;
    }

    public void setAgentVer(String agentVer) {
        this.agentVer = agentVer;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getSubmitSeconds() {
        if (StringUtils.isEmpty((CharSequence)this.submitSeconds)) {
            this.submitSeconds = "120";
        }
        return this.submitSeconds;
    }

    public void setSubmitSeconds(String submitSeconds) {
        this.submitSeconds = submitSeconds;
    }

    public String getBytesRecv() {
        return this.bytesRecv;
    }

    public void setBytesRecv(String bytesRecv) {
        this.bytesRecv = bytesRecv;
    }

    public String getBytesSent() {
        return this.bytesSent;
    }

    public void setBytesSent(String bytesSent) {
        this.bytesSent = bytesSent;
    }

    public String getHostnameExt() {
        return this.hostnameExt;
    }

    public void setHostnameExt(String hostnameExt) {
        this.hostnameExt = hostnameExt;
    }

    public String getTotalSwapMem() {
        return this.totalSwapMem;
    }

    public void setTotalSwapMem(String totalSwapMem) {
        this.totalSwapMem = totalSwapMem;
    }

    public String getSwapMemPer() {
        return this.swapMemPer;
    }

    public void setSwapMemPer(String swapMemPer) {
        this.swapMemPer = swapMemPer;
    }

    public String getCpuPhysicalid() {
        return this.cpuPhysicalid;
    }

    public void setCpuPhysicalid(String cpuPhysicalid) {
        this.cpuPhysicalid = cpuPhysicalid;
    }

    public String getCpuFamily() {
        return this.cpuFamily;
    }

    public void setCpuFamily(String cpuFamily) {
        this.cpuFamily = cpuFamily;
    }

    public String getCpuMhz() {
        return this.cpuMhz;
    }

    public void setCpuMhz(String cpuMhz) {
        this.cpuMhz = cpuMhz;
    }

    public String getKernelArch() {
        return this.kernelArch;
    }

    public void setKernelArch(String kernelArch) {
        this.kernelArch = kernelArch;
    }

    public String getNetInterfaceName() {
        return this.netInterfaceName;
    }

    public void setNetInterfaceName(String netInterfaceName) {
        this.netInterfaceName = netInterfaceName;
    }
}

