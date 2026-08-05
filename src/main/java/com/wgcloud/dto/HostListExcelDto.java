/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import java.io.Serializable;

public class HostListExcelDto
implements Serializable {
    private static final long serialVersionUID = 1L;
    @ExcelProperty(value={"\u4e3b\u673aIP"}, index=0)
    @ColumnWidth(value=18)
    private String hostname;
    @ExcelProperty(value={"\u4e3b\u673a\u540d\u79f0"}, index=1)
    @ColumnWidth(value=18)
    private String hostnameExt;
    @ExcelProperty(value={"\u5907\u6ce8"}, index=2)
    @ColumnWidth(value=18)
    private String remark;
    @ExcelProperty(value={"\u5185\u5b58\u4f7f\u7528\u7387%"}, index=3)
    @ColumnWidth(value=18)
    private Double memPer;
    @ExcelProperty(value={"cpu\u4f7f\u7528\u7387%"}, index=4)
    @ColumnWidth(value=18)
    private Double cpuPer;
    @ExcelProperty(value={"\u78c1\u76d8\u603b\u4f7f\u7528\u7387%"}, index=5)
    @ColumnWidth(value=20)
    private Double diskPer;
    @ExcelProperty(value={"\u78c1\u76d8\u603b\u5927\u5c0f"}, index=6)
    @ColumnWidth(value=18)
    private String diskSumSize;
    @ExcelProperty(value={"cpu\u6838\u6570"}, index=7)
    @ColumnWidth(value=18)
    private String cpuCoreNum;
    @ExcelProperty(value={"\u5185\u5b58\u603b\u5927\u5c0f"}, index=8)
    @ColumnWidth(value=18)
    private String totalMem;
    @ExcelProperty(value={"\u4e0b\u884c\u4f20\u8f93\u901f\u7387"}, index=9)
    @ColumnWidth(value=18)
    private String rxbyt;
    @ExcelProperty(value={"\u4e0a\u884c\u4f20\u8f93\u901f\u7387"}, index=10)
    @ColumnWidth(value=18)
    private String txbyt;
    @ExcelProperty(value={"5\u5206\u949f\u7cfb\u7edf\u8d1f\u8f7d"}, index=11)
    @ColumnWidth(value=20)
    private Double fiveLoad;
    @ExcelProperty(value={"15\u5206\u949f\u7cfb\u7edf\u8d1f\u8f7d"}, index=12)
    @ColumnWidth(value=20)
    private Double fifteenLoad;
    @ExcelProperty(value={"\u8fde\u63a5\u6570\u91cf"}, index=13)
    @ColumnWidth(value=18)
    private String netConnections;
    @ExcelProperty(value={"\u6807\u7b7e"}, index=14)
    @ColumnWidth(value=18)
    private String groupId;
    @ExcelProperty(value={"\u544a\u8b66\u6b21\u6570"}, index=15)
    @ColumnWidth(value=18)
    private Integer warnCount;
    @ExcelProperty(value={"\u4e0a\u62a5\u6570\u636e\u9891\u7387\uff08\u79d2\uff09"}, index=16)
    @ColumnWidth(value=25)
    private String submitSeconds;
    @ExcelProperty(value={"\u7d2f\u8ba1\u63a5\u6536\u6d41\u91cfG"}, index=17)
    @ColumnWidth(value=20)
    private String bytesRecv;
    @ExcelProperty(value={"\u7d2f\u8ba1\u53d1\u9001\u6d41\u91cfG"}, index=18)
    @ColumnWidth(value=20)
    private String bytesSent;
    @ExcelProperty(value={"\u7cfb\u7edf\u63cf\u8ff0"}, index=19)
    @ColumnWidth(value=18)
    private String platForm;
    @ExcelProperty(value={"\u7cfb\u7edf\u7248\u672c"}, index=20)
    @ColumnWidth(value=18)
    private String platformVersion;
    @ExcelProperty(value={"\u8fd0\u884c\u65f6\u95f4"}, index=21)
    @ColumnWidth(value=18)
    private String uptimeStr;
    @ExcelProperty(value={"\u542f\u52a8\u65f6\u95f4"}, index=22)
    @ColumnWidth(value=18)
    private String bootTimeStr;
    @ExcelProperty(value={"\u8fd0\u884c\u8fdb\u7a0b\u6570\u91cf"}, index=23)
    @ColumnWidth(value=18)
    private String procs;
    @ExcelProperty(value={"CPU\u578b\u53f7"}, index=24)
    @ColumnWidth(value=18)
    private String cpuXh;
    @ExcelProperty(value={"CPU PhysicalId"}, index=25)
    @ColumnWidth(value=18)
    private String cpuPhysicalid;
    @ExcelProperty(value={"CPU Mhz"}, index=26)
    @ColumnWidth(value=18)
    private String cpuMhz;
    @ExcelProperty(value={"CPU Family"}, index=27)
    @ColumnWidth(value=18)
    private String cpuFamily;
    @ExcelProperty(value={"\u4e3b\u673a\u72b6\u6001"}, index=28)
    @ColumnWidth(value=18)
    private String state;
    @ExcelProperty(value={"agent\u7248\u672c"}, index=29)
    @ColumnWidth(value=18)
    private String agentVer;
    @ExcelProperty(value={"\u4ea4\u6362\u533a\u5185\u5b58\u603b\u5927\u5c0f"}, index=30)
    @ColumnWidth(value=25)
    private String totalSwapMem;
    @ExcelProperty(value={"\u4ea4\u6362\u533a\u5185\u5b58\u4f7f\u7528\u7387%"}, index=31)
    @ColumnWidth(value=25)
    private String swapMemPer;
    @ExcelProperty(value={"\u66f4\u65b0\u65f6\u95f4"}, index=32)
    @ColumnWidth(value=20)
    private String createTime;
    @ExcelProperty(value={"\u6210\u5458\u8d26\u53f7"}, index=33)
    @ColumnWidth(value=18)
    private String account;

    public String getHostname() {
        return this.hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public Double getMemPer() {
        return this.memPer;
    }

    public void setMemPer(Double memPer) {
        this.memPer = memPer;
    }

    public Double getCpuPer() {
        return this.cpuPer;
    }

    public void setCpuPer(Double cpuPer) {
        this.cpuPer = cpuPer;
    }

    public Double getDiskPer() {
        return this.diskPer;
    }

    public void setDiskPer(Double diskPer) {
        this.diskPer = diskPer;
    }

    public String getCpuCoreNum() {
        return this.cpuCoreNum;
    }

    public void setCpuCoreNum(String cpuCoreNum) {
        this.cpuCoreNum = cpuCoreNum;
    }

    public String getTotalMem() {
        return this.totalMem;
    }

    public void setTotalMem(String totalMem) {
        this.totalMem = totalMem;
    }

    public String getSubmitSeconds() {
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

    public String getRxbyt() {
        return this.rxbyt;
    }

    public void setRxbyt(String rxbyt) {
        this.rxbyt = rxbyt;
    }

    public String getTxbyt() {
        return this.txbyt;
    }

    public void setTxbyt(String txbyt) {
        this.txbyt = txbyt;
    }

    public String getHostnameExt() {
        return this.hostnameExt;
    }

    public void setHostnameExt(String hostnameExt) {
        this.hostnameExt = hostnameExt;
    }

    public Double getFiveLoad() {
        return this.fiveLoad;
    }

    public void setFiveLoad(Double fiveLoad) {
        this.fiveLoad = fiveLoad;
    }

    public Double getFifteenLoad() {
        return this.fifteenLoad;
    }

    public void setFifteenLoad(Double fifteenLoad) {
        this.fifteenLoad = fifteenLoad;
    }

    public String getNetConnections() {
        return this.netConnections;
    }

    public void setNetConnections(String netConnections) {
        this.netConnections = netConnections;
    }

    public String getGroupId() {
        return this.groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public Integer getWarnCount() {
        return this.warnCount;
    }

    public void setWarnCount(Integer warnCount) {
        this.warnCount = warnCount;
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

    public String getUptimeStr() {
        return this.uptimeStr;
    }

    public void setUptimeStr(String uptimeStr) {
        this.uptimeStr = uptimeStr;
    }

    public String getBootTimeStr() {
        return this.bootTimeStr;
    }

    public void setBootTimeStr(String bootTimeStr) {
        this.bootTimeStr = bootTimeStr;
    }

    public String getProcs() {
        return this.procs;
    }

    public void setProcs(String procs) {
        this.procs = procs;
    }

    public String getCpuXh() {
        return this.cpuXh;
    }

    public void setCpuXh(String cpuXh) {
        this.cpuXh = cpuXh;
    }

    public String getState() {
        return this.state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getAgentVer() {
        return this.agentVer;
    }

    public void setAgentVer(String agentVer) {
        this.agentVer = agentVer;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
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

    public String getCpuMhz() {
        return this.cpuMhz;
    }

    public void setCpuMhz(String cpuMhz) {
        this.cpuMhz = cpuMhz;
    }

    public String getCpuFamily() {
        return this.cpuFamily;
    }

    public void setCpuFamily(String cpuFamily) {
        this.cpuFamily = cpuFamily;
    }

    public String getAccount() {
        return this.account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getDiskSumSize() {
        return this.diskSumSize;
    }

    public void setDiskSumSize(String diskSumSize) {
        this.diskSumSize = diskSumSize;
    }
}

