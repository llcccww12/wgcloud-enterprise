/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.entity;

import com.wgcloud.entity.BaseEntity;
import java.util.Date;

public class AppInfo
extends BaseEntity {
    private static final long serialVersionUID = 1L;
    private String hostname;
    private String appPid;
    private String appType;
    private String appName;
    private String threadsNum;
    private Double memPer;
    private Double cpuPer;
    private String state;
    private String active;
    private String readBytes;
    private String writesBytes;
    private Integer warnCount;
    private String warnQueryWd;
    private String account;
    private String hostState;
    private Date createTime;
    private String gatherPid;
    private String groupId;
    private String appTimes;
    private String proUsername;
    private String customShell;
    private String warnLevel;
    private String netConnections;
    private String appCmdLine;

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getAppPid() {
        return this.appPid;
    }

    public void setAppPid(String appPid) {
        this.appPid = appPid;
    }

    public String getHostname() {
        return this.hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getAppName() {
        return this.appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
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

    public String getAppType() {
        return this.appType;
    }

    public void setAppType(String appType) {
        this.appType = appType;
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

    public String getReadBytes() {
        return this.readBytes;
    }

    public void setReadBytes(String readBytes) {
        this.readBytes = readBytes;
    }

    public String getWritesBytes() {
        return this.writesBytes;
    }

    public void setWritesBytes(String writesBytes) {
        this.writesBytes = writesBytes;
    }

    public Integer getWarnCount() {
        return this.warnCount;
    }

    public void setWarnCount(Integer warnCount) {
        this.warnCount = warnCount;
    }

    public String getThreadsNum() {
        return this.threadsNum;
    }

    public void setThreadsNum(String threadsNum) {
        this.threadsNum = threadsNum;
    }

    public String getGatherPid() {
        return this.gatherPid;
    }

    public void setGatherPid(String gatherPid) {
        this.gatherPid = gatherPid;
    }

    public String getWarnQueryWd() {
        return this.warnQueryWd;
    }

    public void setWarnQueryWd(String warnQueryWd) {
        this.warnQueryWd = warnQueryWd;
    }

    public String getGroupId() {
        return this.groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getAccount() {
        return this.account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getAppTimes() {
        return this.appTimes;
    }

    public void setAppTimes(String appTimes) {
        this.appTimes = appTimes;
    }

    public String getProUsername() {
        return this.proUsername;
    }

    public void setProUsername(String proUsername) {
        this.proUsername = proUsername;
    }

    public String getCustomShell() {
        return this.customShell;
    }

    public void setCustomShell(String customShell) {
        this.customShell = customShell;
    }

    public String getWarnLevel() {
        return this.warnLevel;
    }

    public void setWarnLevel(String warnLevel) {
        this.warnLevel = warnLevel;
    }

    public String getNetConnections() {
        return this.netConnections;
    }

    public void setNetConnections(String netConnections) {
        this.netConnections = netConnections;
    }

    public String getAppCmdLine() {
        return this.appCmdLine;
    }

    public void setAppCmdLine(String appCmdLine) {
        this.appCmdLine = appCmdLine;
    }

    public String getHostState() {
        return this.hostState;
    }

    public void setHostState(String hostState) {
        this.hostState = hostState;
    }
}

