/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.entity;

import com.wgcloud.entity.BaseEntity;
import java.util.Date;

public class FtpInfo
extends BaseEntity {
    private static final long serialVersionUID = 1L;
    private String ftpName;
    private String ftpHost;
    private String ftpType;
    private String userName;
    private String port;
    private String passwd;
    private String state;
    private String active;
    private Integer warnCount;
    private String warnQueryWd;
    private String account;
    private String warnType;
    private Integer resTimes;
    private Date createTime;
    private String warnLevel;
    private String groupId;
    private String serverBackupMark;
    private String testErrorMsg;

    public String getFtpHost() {
        return this.ftpHost;
    }

    public void setFtpHost(String ftpHost) {
        this.ftpHost = ftpHost;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPort() {
        return this.port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getPasswd() {
        return this.passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
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

    public String getWarnType() {
        return this.warnType;
    }

    public void setWarnType(String warnType) {
        this.warnType = warnType;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getFtpName() {
        return this.ftpName;
    }

    public void setFtpName(String ftpName) {
        this.ftpName = ftpName;
    }

    public Integer getResTimes() {
        return this.resTimes;
    }

    public void setResTimes(Integer resTimes) {
        this.resTimes = resTimes;
    }

    public String getFtpType() {
        return this.ftpType;
    }

    public void setFtpType(String ftpType) {
        this.ftpType = ftpType;
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
}

