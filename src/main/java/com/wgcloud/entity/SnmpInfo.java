/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.entity;

import com.wgcloud.entity.BaseEntity;
import java.util.Date;
import org.apache.commons.lang3.StringUtils;

public class SnmpInfo
extends BaseEntity {
    private static final long serialVersionUID = 1L;
    private String hostname;
    private String bytesRecv;
    private String bytesSent;
    private String recvAvg;
    private String sentAvg;
    private String recvOID;
    private String sentOID;
    private String snmpUnit;
    private String snmpCommunity;
    private String snmpPort;
    private String snmpVersion;
    private String state;
    private String active;
    private String remark;
    private Integer warnCount;
    private String warnQueryWd;
    private Date createTime;
    private String account;
    private String cpuPerOID;
    private String cpuPer;
    private Double cpuPerDouble;
    private String memSizeOID;
    private String memSize;
    private String memPer;
    private Double memPerDouble;
    private String temperatureOid;
    private String temperatureValue;
    private String diskPerOid;
    private String diskPer;
    private String sysDescVal;
    private String sysDescOid;
    private String warnLevel;
    private String groupId;
    private String voltageOid;
    private String voltageValue;
    private String securityName;
    private String authPass;
    private String privPass;
    private String serverBackupMark;
    private String ifOperStatusOid;
    private String ifOperStatusValue;
    private String recvAvgSum;
    private String sentAvgSum;
    private String testErrorMsg;

    public String getHostname() {
        return this.hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
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

    public String getRecvAvg() {
        return this.recvAvg;
    }

    public void setRecvAvg(String recvAvg) {
        this.recvAvg = recvAvg;
    }

    public String getSentAvg() {
        return this.sentAvg;
    }

    public void setSentAvg(String sentAvg) {
        this.sentAvg = sentAvg;
    }

    public String getSnmpUnit() {
        return this.snmpUnit;
    }

    public void setSnmpUnit(String snmpUnit) {
        this.snmpUnit = snmpUnit;
    }

    public String getState() {
        return this.state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getActive() {
        return this.active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public Integer getWarnCount() {
        return this.warnCount;
    }

    public void setWarnCount(Integer warnCount) {
        this.warnCount = warnCount;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRecvOID() {
        return this.recvOID;
    }

    public void setRecvOID(String recvOID) {
        this.recvOID = recvOID;
    }

    public String getSentOID() {
        return this.sentOID;
    }

    public void setSentOID(String sentOID) {
        this.sentOID = sentOID;
    }

    public String getSnmpCommunity() {
        return this.snmpCommunity;
    }

    public void setSnmpCommunity(String snmpCommunity) {
        this.snmpCommunity = snmpCommunity;
    }

    public String getSnmpPort() {
        return this.snmpPort;
    }

    public void setSnmpPort(String snmpPort) {
        this.snmpPort = snmpPort;
    }

    public String getSnmpVersion() {
        return this.snmpVersion;
    }

    public void setSnmpVersion(String snmpVersion) {
        this.snmpVersion = snmpVersion;
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

    public String getCpuPerOID() {
        return this.cpuPerOID;
    }

    public void setCpuPerOID(String cpuPerOID) {
        this.cpuPerOID = cpuPerOID;
    }

    public String getCpuPer() {
        return this.cpuPer;
    }

    public void setCpuPer(String cpuPer) {
        this.cpuPer = cpuPer;
    }

    public String getMemSizeOID() {
        return this.memSizeOID;
    }

    public void setMemSizeOID(String memSizeOID) {
        this.memSizeOID = memSizeOID;
    }

    public String getTemperatureOid() {
        return this.temperatureOid;
    }

    public void setTemperatureOid(String temperatureOid) {
        this.temperatureOid = temperatureOid;
    }

    public Double getCpuPerDouble() {
        if (!StringUtils.isEmpty((CharSequence)this.cpuPer)) {
            try {
                this.cpuPerDouble = Double.valueOf(this.cpuPer);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            this.cpuPerDouble = 0.0;
        }
        return this.cpuPerDouble;
    }

    public void setCpuPerDouble(Double cpuPerDouble) {
        this.cpuPerDouble = cpuPerDouble;
    }

    public String getMemPer() {
        return this.memPer;
    }

    public void setMemPer(String memPer) {
        this.memPer = memPer;
    }

    public String getMemSize() {
        return this.memSize;
    }

    public void setMemSize(String memSize) {
        this.memSize = memSize;
    }

    public Double getMemPerDouble() {
        if (!StringUtils.isEmpty((CharSequence)this.memPer)) {
            try {
                this.memPerDouble = Double.valueOf(this.memPer);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            this.memPerDouble = 0.0;
        }
        return this.memPerDouble;
    }

    public void setMemPerDouble(Double memPerDouble) {
        this.memPerDouble = memPerDouble;
    }

    public String getDiskPerOid() {
        return this.diskPerOid;
    }

    public void setDiskPerOid(String diskPerOid) {
        this.diskPerOid = diskPerOid;
    }

    public String getDiskPer() {
        return this.diskPer;
    }

    public void setDiskPer(String diskPer) {
        this.diskPer = diskPer;
    }

    public String getSysDescVal() {
        return this.sysDescVal;
    }

    public void setSysDescVal(String sysDescVal) {
        this.sysDescVal = sysDescVal;
    }

    public String getSysDescOid() {
        return this.sysDescOid;
    }

    public void setSysDescOid(String sysDescOid) {
        this.sysDescOid = sysDescOid;
    }

    public String getWarnLevel() {
        return this.warnLevel;
    }

    public void setWarnLevel(String warnLevel) {
        this.warnLevel = warnLevel;
    }

    public String getGroupId() {
        return this.groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getTemperatureValue() {
        return this.temperatureValue;
    }

    public void setTemperatureValue(String temperatureValue) {
        this.temperatureValue = temperatureValue;
    }

    public String getVoltageOid() {
        return this.voltageOid;
    }

    public void setVoltageOid(String voltageOid) {
        this.voltageOid = voltageOid;
    }

    public String getVoltageValue() {
        return this.voltageValue;
    }

    public void setVoltageValue(String voltageValue) {
        this.voltageValue = voltageValue;
    }

    public String getServerBackupMark() {
        return this.serverBackupMark;
    }

    public void setServerBackupMark(String serverBackupMark) {
        this.serverBackupMark = serverBackupMark;
    }

    public String getIfOperStatusOid() {
        return this.ifOperStatusOid;
    }

    public void setIfOperStatusOid(String ifOperStatusOid) {
        this.ifOperStatusOid = ifOperStatusOid;
    }

    public String getIfOperStatusValue() {
        return this.ifOperStatusValue;
    }

    public void setIfOperStatusValue(String ifOperStatusValue) {
        this.ifOperStatusValue = ifOperStatusValue;
    }

    public String getRecvAvgSum() {
        return this.recvAvgSum;
    }

    public void setRecvAvgSum(String recvAvgSum) {
        this.recvAvgSum = recvAvgSum;
    }

    public String getSentAvgSum() {
        return this.sentAvgSum;
    }

    public void setSentAvgSum(String sentAvgSum) {
        this.sentAvgSum = sentAvgSum;
    }

    public String getTestErrorMsg() {
        if (StringUtils.isEmpty((CharSequence)this.testErrorMsg)) {
            return "";
        }
        return this.testErrorMsg;
    }

    public void setTestErrorMsg(String testErrorMsg) {
        this.testErrorMsg = testErrorMsg;
    }

    public String getSecurityName() {
        return this.securityName;
    }

    public void setSecurityName(String securityName) {
        this.securityName = securityName;
    }

    public String getAuthPass() {
        return this.authPass;
    }

    public void setAuthPass(String authPass) {
        this.authPass = authPass;
    }

    public String getPrivPass() {
        return this.privPass;
    }

    public void setPrivPass(String privPass) {
        this.privPass = privPass;
    }
}

