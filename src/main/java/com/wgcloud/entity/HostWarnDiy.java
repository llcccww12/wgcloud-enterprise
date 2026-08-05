/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.entity;

import com.wgcloud.entity.BaseEntity;
import java.util.Date;

public class HostWarnDiy
extends BaseEntity {
    private static final long serialVersionUID = 1L;
    private String hostname;
    private Double memWarnVal;
    private Double cpuWarnVal;
    private Double upSpeedVal;
    private Double upSpeedMinVal;
    private Double downSpeedVal;
    private Double downSpeedMinVal;
    private Double cpuTemperatureWarnVal;
    private String memWarnMail;
    private String upSpeedMail;
    private String downSpeedMail;
    private String cpuWarnMail;
    private String cpuTemperatureWarnMail;
    private String diskWarnMail;
    private String smartWarnMail;
    private String diskBlock;
    private Double diskWarnVal;
    private String sysLoadWarnMail;
    private Double sysLoadWarnVal;
    private String hostDownWarnMail;
    private String hostLoginWarnMail;
    private String hostBlockAllWarn;
    private String speedWarnLevel;
    private String memWarnLevel;
    private String sysLoadWarnLevel;
    private String cpuWarnLevel;
    private String cpuTemperatureWarnLevel;
    private String diskWarnLevel;
    private String smartWarnLevel;
    private String hostDownWarnLevel;
    private String hostLoginWarnLevel;
    private String netConnectionsWarnMail;
    private Double netConnectionsWarnVal;
    private String netConnectionsWarnLevel;
    private String diskBlockSave;
    private String hostDownWarnCount;
    private String customWarnMail;
    private String customWarnAccountKey;
    private String diskIoSpeedWarnMail;
    private Double diskIoSpeedWarnVal;
    private Date createTime;
    private String account;
    private String active;
    private String remark;

    public String getHostname() {
        return this.hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public Double getMemWarnVal() {
        return this.memWarnVal;
    }

    public void setMemWarnVal(Double memWarnVal) {
        this.memWarnVal = memWarnVal;
    }

    public Double getCpuWarnVal() {
        return this.cpuWarnVal;
    }

    public void setCpuWarnVal(Double cpuWarnVal) {
        this.cpuWarnVal = cpuWarnVal;
    }

    public Double getUpSpeedVal() {
        return this.upSpeedVal;
    }

    public void setUpSpeedVal(Double upSpeedVal) {
        this.upSpeedVal = upSpeedVal;
    }

    public Double getUpSpeedMinVal() {
        return this.upSpeedMinVal;
    }

    public void setUpSpeedMinVal(Double upSpeedMinVal) {
        this.upSpeedMinVal = upSpeedMinVal;
    }

    public Double getDownSpeedVal() {
        return this.downSpeedVal;
    }

    public void setDownSpeedVal(Double downSpeedVal) {
        this.downSpeedVal = downSpeedVal;
    }

    public Double getDownSpeedMinVal() {
        return this.downSpeedMinVal;
    }

    public void setDownSpeedMinVal(Double downSpeedMinVal) {
        this.downSpeedMinVal = downSpeedMinVal;
    }

    public Double getCpuTemperatureWarnVal() {
        return this.cpuTemperatureWarnVal;
    }

    public void setCpuTemperatureWarnVal(Double cpuTemperatureWarnVal) {
        this.cpuTemperatureWarnVal = cpuTemperatureWarnVal;
    }

    public String getMemWarnMail() {
        return this.memWarnMail;
    }

    public void setMemWarnMail(String memWarnMail) {
        this.memWarnMail = memWarnMail;
    }

    public String getUpSpeedMail() {
        return this.upSpeedMail;
    }

    public void setUpSpeedMail(String upSpeedMail) {
        this.upSpeedMail = upSpeedMail;
    }

    public String getDownSpeedMail() {
        return this.downSpeedMail;
    }

    public void setDownSpeedMail(String downSpeedMail) {
        this.downSpeedMail = downSpeedMail;
    }

    public String getCpuWarnMail() {
        return this.cpuWarnMail;
    }

    public void setCpuWarnMail(String cpuWarnMail) {
        this.cpuWarnMail = cpuWarnMail;
    }

    public String getCpuTemperatureWarnMail() {
        return this.cpuTemperatureWarnMail;
    }

    public void setCpuTemperatureWarnMail(String cpuTemperatureWarnMail) {
        this.cpuTemperatureWarnMail = cpuTemperatureWarnMail;
    }

    public String getDiskWarnMail() {
        return this.diskWarnMail;
    }

    public void setDiskWarnMail(String diskWarnMail) {
        this.diskWarnMail = diskWarnMail;
    }

    public String getSmartWarnMail() {
        return this.smartWarnMail;
    }

    public void setSmartWarnMail(String smartWarnMail) {
        this.smartWarnMail = smartWarnMail;
    }

    public String getDiskBlock() {
        return this.diskBlock;
    }

    public void setDiskBlock(String diskBlock) {
        this.diskBlock = diskBlock;
    }

    public Double getDiskWarnVal() {
        return this.diskWarnVal;
    }

    public void setDiskWarnVal(Double diskWarnVal) {
        this.diskWarnVal = diskWarnVal;
    }

    public String getSysLoadWarnMail() {
        return this.sysLoadWarnMail;
    }

    public void setSysLoadWarnMail(String sysLoadWarnMail) {
        this.sysLoadWarnMail = sysLoadWarnMail;
    }

    public Double getSysLoadWarnVal() {
        return this.sysLoadWarnVal;
    }

    public void setSysLoadWarnVal(Double sysLoadWarnVal) {
        this.sysLoadWarnVal = sysLoadWarnVal;
    }

    public String getHostDownWarnMail() {
        return this.hostDownWarnMail;
    }

    public void setHostDownWarnMail(String hostDownWarnMail) {
        this.hostDownWarnMail = hostDownWarnMail;
    }

    public String getHostLoginWarnMail() {
        return this.hostLoginWarnMail;
    }

    public void setHostLoginWarnMail(String hostLoginWarnMail) {
        this.hostLoginWarnMail = hostLoginWarnMail;
    }

    public String getHostBlockAllWarn() {
        return this.hostBlockAllWarn;
    }

    public void setHostBlockAllWarn(String hostBlockAllWarn) {
        this.hostBlockAllWarn = hostBlockAllWarn;
    }

    public String getSpeedWarnLevel() {
        return this.speedWarnLevel;
    }

    public void setSpeedWarnLevel(String speedWarnLevel) {
        this.speedWarnLevel = speedWarnLevel;
    }

    public String getMemWarnLevel() {
        return this.memWarnLevel;
    }

    public void setMemWarnLevel(String memWarnLevel) {
        this.memWarnLevel = memWarnLevel;
    }

    public String getSysLoadWarnLevel() {
        return this.sysLoadWarnLevel;
    }

    public void setSysLoadWarnLevel(String sysLoadWarnLevel) {
        this.sysLoadWarnLevel = sysLoadWarnLevel;
    }

    public String getCpuWarnLevel() {
        return this.cpuWarnLevel;
    }

    public void setCpuWarnLevel(String cpuWarnLevel) {
        this.cpuWarnLevel = cpuWarnLevel;
    }

    public String getCpuTemperatureWarnLevel() {
        return this.cpuTemperatureWarnLevel;
    }

    public void setCpuTemperatureWarnLevel(String cpuTemperatureWarnLevel) {
        this.cpuTemperatureWarnLevel = cpuTemperatureWarnLevel;
    }

    public String getDiskWarnLevel() {
        return this.diskWarnLevel;
    }

    public void setDiskWarnLevel(String diskWarnLevel) {
        this.diskWarnLevel = diskWarnLevel;
    }

    public String getSmartWarnLevel() {
        return this.smartWarnLevel;
    }

    public void setSmartWarnLevel(String smartWarnLevel) {
        this.smartWarnLevel = smartWarnLevel;
    }

    public String getHostDownWarnLevel() {
        return this.hostDownWarnLevel;
    }

    public void setHostDownWarnLevel(String hostDownWarnLevel) {
        this.hostDownWarnLevel = hostDownWarnLevel;
    }

    public String getHostLoginWarnLevel() {
        return this.hostLoginWarnLevel;
    }

    public void setHostLoginWarnLevel(String hostLoginWarnLevel) {
        this.hostLoginWarnLevel = hostLoginWarnLevel;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getAccount() {
        return this.account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getActive() {
        return this.active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getNetConnectionsWarnMail() {
        return this.netConnectionsWarnMail;
    }

    public void setNetConnectionsWarnMail(String netConnectionsWarnMail) {
        this.netConnectionsWarnMail = netConnectionsWarnMail;
    }

    public Double getNetConnectionsWarnVal() {
        return this.netConnectionsWarnVal;
    }

    public void setNetConnectionsWarnVal(Double netConnectionsWarnVal) {
        this.netConnectionsWarnVal = netConnectionsWarnVal;
    }

    public String getNetConnectionsWarnLevel() {
        return this.netConnectionsWarnLevel;
    }

    public void setNetConnectionsWarnLevel(String netConnectionsWarnLevel) {
        this.netConnectionsWarnLevel = netConnectionsWarnLevel;
    }

    public String getDiskBlockSave() {
        return this.diskBlockSave;
    }

    public void setDiskBlockSave(String diskBlockSave) {
        this.diskBlockSave = diskBlockSave;
    }

    public String getHostDownWarnCount() {
        return this.hostDownWarnCount;
    }

    public void setHostDownWarnCount(String hostDownWarnCount) {
        this.hostDownWarnCount = hostDownWarnCount;
    }

    public String getCustomWarnMail() {
        return this.customWarnMail;
    }

    public void setCustomWarnMail(String customWarnMail) {
        this.customWarnMail = customWarnMail;
    }

    public String getCustomWarnAccountKey() {
        return this.customWarnAccountKey;
    }

    public void setCustomWarnAccountKey(String customWarnAccountKey) {
        this.customWarnAccountKey = customWarnAccountKey;
    }

    public String getDiskIoSpeedWarnMail() {
        return this.diskIoSpeedWarnMail;
    }

    public void setDiskIoSpeedWarnMail(String diskIoSpeedWarnMail) {
        this.diskIoSpeedWarnMail = diskIoSpeedWarnMail;
    }

    public Double getDiskIoSpeedWarnVal() {
        return this.diskIoSpeedWarnVal;
    }

    public void setDiskIoSpeedWarnVal(Double diskIoSpeedWarnVal) {
        this.diskIoSpeedWarnVal = diskIoSpeedWarnVal;
    }
}

