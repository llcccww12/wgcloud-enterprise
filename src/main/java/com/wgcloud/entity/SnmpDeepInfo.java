/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.entity;

import com.wgcloud.entity.BaseEntity;
import java.util.Date;

public class SnmpDeepInfo
extends BaseEntity {
    private static final long serialVersionUID = 1L;
    private String hostname;
    private String snmpCommunity;
    private String snmpPort;
    private String snmpVersion;
    private String securityName;
    private String authPass;
    private String privPass;
    private String state;
    private String active;
    private String remark;
    private Date createTime;
    private String account;
    private String warnLevel;
    private String groupId;
    private String stateSize;
    private String serverBackupMark;
    private String testErrorMsg;

    public String getHostname() {
        return this.hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
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

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    public String getServerBackupMark() {
        return this.serverBackupMark;
    }

    public void setServerBackupMark(String serverBackupMark) {
        this.serverBackupMark = serverBackupMark;
    }

    public String getTestErrorMsg() {
        return this.testErrorMsg;
    }

    public void setTestErrorMsg(String testErrorMsg) {
        this.testErrorMsg = testErrorMsg;
    }

    public String getStateSize() {
        return this.stateSize;
    }

    public void setStateSize(String stateSize) {
        this.stateSize = stateSize;
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

