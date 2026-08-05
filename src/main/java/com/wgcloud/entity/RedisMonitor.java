/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.entity;

import com.wgcloud.entity.BaseEntity;
import java.util.Date;

public class RedisMonitor
extends BaseEntity {
    private static final long serialVersionUID = 1L;
    private String redisName;
    private String redisVersion;
    private String redisMode;
    private String processId;
    private String tcpPort;
    private String uptimeInDays;
    private String executable;
    private String configFile;
    private String connectedClients;
    private String blockedClients;
    private String usedMemoryHuman;
    private String usedMemoryPeakHuman;
    private String maxMemoryHuman;
    private String aofEnabled;
    private String rdbLastBgsaveStatus;
    private String rdbLastBgsaveTimeSec;
    private String instantaneousOpsPerSec;
    private String totalNetInputBytes;
    private String totalNetOutputBytes;
    private String rejectedConnections;
    private String redisRole;
    private String connectedSlaves;
    private String usedCpuSys;
    private String expiredKeys;
    private String evictedKeys;
    private String keyspaceHits;
    private String keyspaceMisses;
    private String pubsubChannels;
    private String pubsubPatterns;
    private String clusterEnabled;
    private String state;
    private String redisNodeInfo;
    private Date createTime;

    public String getRedisName() {
        return this.redisName;
    }

    public void setRedisName(String redisName) {
        this.redisName = redisName;
    }

    public String getRedisVersion() {
        return this.redisVersion;
    }

    public void setRedisVersion(String redisVersion) {
        this.redisVersion = redisVersion;
    }

    public String getRedisMode() {
        return this.redisMode;
    }

    public void setRedisMode(String redisMode) {
        this.redisMode = redisMode;
    }

    public String getProcessId() {
        return this.processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public String getTcpPort() {
        return this.tcpPort;
    }

    public void setTcpPort(String tcpPort) {
        this.tcpPort = tcpPort;
    }

    public String getUptimeInDays() {
        return this.uptimeInDays;
    }

    public void setUptimeInDays(String uptimeInDays) {
        this.uptimeInDays = uptimeInDays;
    }

    public String getExecutable() {
        return this.executable;
    }

    public void setExecutable(String executable) {
        this.executable = executable;
    }

    public String getConfigFile() {
        return this.configFile;
    }

    public void setConfigFile(String configFile) {
        this.configFile = configFile;
    }

    public String getConnectedClients() {
        return this.connectedClients;
    }

    public void setConnectedClients(String connectedClients) {
        this.connectedClients = connectedClients;
    }

    public String getBlockedClients() {
        return this.blockedClients;
    }

    public void setBlockedClients(String blockedClients) {
        this.blockedClients = blockedClients;
    }

    public String getUsedMemoryHuman() {
        return this.usedMemoryHuman;
    }

    public void setUsedMemoryHuman(String usedMemoryHuman) {
        this.usedMemoryHuman = usedMemoryHuman;
    }

    public String getUsedMemoryPeakHuman() {
        return this.usedMemoryPeakHuman;
    }

    public void setUsedMemoryPeakHuman(String usedMemoryPeakHuman) {
        this.usedMemoryPeakHuman = usedMemoryPeakHuman;
    }

    public String getMaxMemoryHuman() {
        return this.maxMemoryHuman;
    }

    public void setMaxMemoryHuman(String maxMemoryHuman) {
        this.maxMemoryHuman = maxMemoryHuman;
    }

    public String getAofEnabled() {
        return this.aofEnabled;
    }

    public void setAofEnabled(String aofEnabled) {
        this.aofEnabled = aofEnabled;
    }

    public String getRdbLastBgsaveStatus() {
        return this.rdbLastBgsaveStatus;
    }

    public void setRdbLastBgsaveStatus(String rdbLastBgsaveStatus) {
        this.rdbLastBgsaveStatus = rdbLastBgsaveStatus;
    }

    public String getRdbLastBgsaveTimeSec() {
        return this.rdbLastBgsaveTimeSec;
    }

    public void setRdbLastBgsaveTimeSec(String rdbLastBgsaveTimeSec) {
        this.rdbLastBgsaveTimeSec = rdbLastBgsaveTimeSec;
    }

    public String getInstantaneousOpsPerSec() {
        return this.instantaneousOpsPerSec;
    }

    public void setInstantaneousOpsPerSec(String instantaneousOpsPerSec) {
        this.instantaneousOpsPerSec = instantaneousOpsPerSec;
    }

    public String getTotalNetInputBytes() {
        return this.totalNetInputBytes;
    }

    public void setTotalNetInputBytes(String totalNetInputBytes) {
        this.totalNetInputBytes = totalNetInputBytes;
    }

    public String getTotalNetOutputBytes() {
        return this.totalNetOutputBytes;
    }

    public void setTotalNetOutputBytes(String totalNetOutputBytes) {
        this.totalNetOutputBytes = totalNetOutputBytes;
    }

    public String getRejectedConnections() {
        return this.rejectedConnections;
    }

    public void setRejectedConnections(String rejectedConnections) {
        this.rejectedConnections = rejectedConnections;
    }

    public String getRedisRole() {
        return this.redisRole;
    }

    public void setRedisRole(String redisRole) {
        this.redisRole = redisRole;
    }

    public String getConnectedSlaves() {
        return this.connectedSlaves;
    }

    public void setConnectedSlaves(String connectedSlaves) {
        this.connectedSlaves = connectedSlaves;
    }

    public String getUsedCpuSys() {
        return this.usedCpuSys;
    }

    public void setUsedCpuSys(String usedCpuSys) {
        this.usedCpuSys = usedCpuSys;
    }

    public String getExpiredKeys() {
        return this.expiredKeys;
    }

    public void setExpiredKeys(String expiredKeys) {
        this.expiredKeys = expiredKeys;
    }

    public String getEvictedKeys() {
        return this.evictedKeys;
    }

    public void setEvictedKeys(String evictedKeys) {
        this.evictedKeys = evictedKeys;
    }

    public String getKeyspaceHits() {
        return this.keyspaceHits;
    }

    public void setKeyspaceHits(String keyspaceHits) {
        this.keyspaceHits = keyspaceHits;
    }

    public String getKeyspaceMisses() {
        return this.keyspaceMisses;
    }

    public void setKeyspaceMisses(String keyspaceMisses) {
        this.keyspaceMisses = keyspaceMisses;
    }

    public String getPubsubChannels() {
        return this.pubsubChannels;
    }

    public void setPubsubChannels(String pubsubChannels) {
        this.pubsubChannels = pubsubChannels;
    }

    public String getPubsubPatterns() {
        return this.pubsubPatterns;
    }

    public void setPubsubPatterns(String pubsubPatterns) {
        this.pubsubPatterns = pubsubPatterns;
    }

    public String getClusterEnabled() {
        return this.clusterEnabled;
    }

    public void setClusterEnabled(String clusterEnabled) {
        this.clusterEnabled = clusterEnabled;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getState() {
        return this.state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getRedisNodeInfo() {
        return this.redisNodeInfo;
    }

    public void setRedisNodeInfo(String redisNodeInfo) {
        this.redisNodeInfo = redisNodeInfo;
    }
}

