/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.entity;

import com.wgcloud.entity.BaseEntity;
import com.wgcloud.util.DateUtil;
import com.wgcloud.util.FormatUtil;
import java.util.Date;
import org.apache.commons.lang3.StringUtils;

public class SystemInfo
extends BaseEntity {
    private static final long serialVersionUID = 1L;
    private String hostname;
    private String platForm;
    private String platformVersion;
    private Long uptime;
    private String uptimeStr;
    private Long bootTime;
    private String bootTimeStr;
    private String procs;
    private Double memPer;
    private String cpuCoreNum;
    private Double cpuPer;
    private String cpuXh;
    private String state;
    private String agentVer;
    private Date createTime;
    private String remark;
    private String totalMem;
    private String submitSeconds;
    private String bytesRecv;
    private String bytesSent;
    private String rxbyt;
    private String txbyt;
    private String winConsole;
    private String hostnameExt;
    private Double fiveLoad;
    private Double fifteenLoad;
    private String netConnections;
    private String groupId;
    private String account;
    private String totalSwapMem;
    private String swapMemPer;
    private String cpuPhysicalid;
    private String cpuFamily;
    private String cpuMhz;
    private String kernelArch;
    private String netInterfaceName;
    private String macAddress;
    private String active;
    private String countBlock;
    private Integer orderNum;
    private String image;
    private Double diskPer;
    private String diskSumSize;
    private String selected;
    private Integer warnCount;
    private String warnQueryWd;
    private String biosInfo;
    private String baseBoard;
    // GPU 最新使用率（逗号分隔的多卡字符串，由 hostAddVal 注入；瞬态展示字段，不入库）
    private String gpuRate;

    public String getGpuRate() {
        return this.gpuRate;
    }

    public void setGpuRate(String gpuRate) {
        this.gpuRate = gpuRate;
    }

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

    public Long getBootTime() {
        return this.bootTime;
    }

    public void setBootTime(Long bootTime) {
        this.bootTime = bootTime;
    }

    public Double getMemPer() {
        if (null == this.memPer) {
            this.memPer = 0.0;
        }
        return this.memPer;
    }

    public void setMemPer(Double memPer) {
        this.memPer = memPer;
    }

    public String getCpuCoreNum() {
        return this.cpuCoreNum;
    }

    public void setCpuCoreNum(String cpuCoreNum) {
        this.cpuCoreNum = cpuCoreNum;
    }

    public Double getCpuPer() {
        if (null == this.cpuPer) {
            this.cpuPer = 0.0;
        }
        return this.cpuPer;
    }

    public void setCpuPer(Double cpuPer) {
        this.cpuPer = cpuPer;
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

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getGroupId() {
        return this.groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getProcs() {
        return this.procs;
    }

    public void setProcs(String procs) {
        this.procs = procs;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getTotalMem() {
        return this.totalMem;
    }

    public void setTotalMem(String totalMem) {
        this.totalMem = totalMem;
    }

    public String getUptimeStr() {
        return FormatUtil.timesToDay(this.uptime);
    }

    public void setUptimeStr(String uptimeStr) {
        this.uptimeStr = uptimeStr;
    }

    public String getBootTimeStr() {
        return DateUtil.secondToDate(this.bootTime, "yyyy-MM-dd HH:mm:ss");
    }

    public void setBootTimeStr(String bootTimeStr) {
        this.bootTimeStr = bootTimeStr;
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

    public String getAgentVer() {
        return this.agentVer;
    }

    public void setAgentVer(String agentVer) {
        this.agentVer = agentVer;
    }

    public String getImage() {
        return this.image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Double getDiskPer() {
        return this.diskPer;
    }

    public void setDiskPer(Double diskPer) {
        this.diskPer = diskPer;
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
        if (StringUtils.isEmpty((CharSequence)this.rxbyt)) {
            this.rxbyt = "0";
        }
        return this.rxbyt;
    }

    public void setRxbyt(String rxbyt) {
        this.rxbyt = rxbyt;
    }

    public String getTxbyt() {
        if (StringUtils.isEmpty((CharSequence)this.txbyt)) {
            this.txbyt = "0";
        }
        return this.txbyt;
    }

    public void setTxbyt(String txbyt) {
        this.txbyt = txbyt;
    }

    public String getWinConsole() {
        return this.winConsole;
    }

    public void setWinConsole(String winConsole) {
        this.winConsole = winConsole;
    }

    public String getSelected() {
        return this.selected;
    }

    public void setSelected(String selected) {
        this.selected = selected;
    }

    public String getHostnameExt() {
        return this.hostnameExt;
    }

    public void setHostnameExt(String hostnameExt) {
        this.hostnameExt = hostnameExt;
    }

    public Double getFiveLoad() {
        if (null == this.fiveLoad) {
            this.fiveLoad = 0.0;
        }
        return this.fiveLoad;
    }

    public void setFiveLoad(Double fiveLoad) {
        this.fiveLoad = fiveLoad;
    }

    public Double getFifteenLoad() {
        if (null == this.fifteenLoad) {
            this.fifteenLoad = 0.0;
        }
        return this.fifteenLoad;
    }

    public void setFifteenLoad(Double fifteenLoad) {
        this.fifteenLoad = fifteenLoad;
    }

    public Integer getWarnCount() {
        return this.warnCount;
    }

    public void setWarnCount(Integer warnCount) {
        this.warnCount = warnCount;
    }

    public String getNetConnections() {
        return this.netConnections;
    }

    public void setNetConnections(String netConnections) {
        this.netConnections = netConnections;
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

    public String getWarnQueryWd() {
        return this.warnQueryWd;
    }

    public void setWarnQueryWd(String warnQueryWd) {
        this.warnQueryWd = warnQueryWd;
    }

    public String getAccount() {
        return this.account;
    }

    public void setAccount(String account) {
        this.account = account;
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

    public String getActive() {
        return this.active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getCountBlock() {
        return this.countBlock;
    }

    public void setCountBlock(String countBlock) {
        this.countBlock = countBlock;
    }

    public Integer getOrderNum() {
        return this.orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    public String getNetInterfaceName() {
        return this.netInterfaceName;
    }

    public void setNetInterfaceName(String netInterfaceName) {
        this.netInterfaceName = netInterfaceName;
    }

    public String getMacAddress() {
        return this.macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getDiskSumSize() {
        return this.diskSumSize;
    }

    public void setDiskSumSize(String diskSumSize) {
        this.diskSumSize = diskSumSize;
    }

    public String getBiosInfo() {
        return this.biosInfo;
    }

    public void setBiosInfo(String biosInfo) {
        this.biosInfo = biosInfo;
    }

    public String getBaseBoard() {
        return this.baseBoard;
    }

    public void setBaseBoard(String baseBoard) {
        this.baseBoard = baseBoard;
    }
}

