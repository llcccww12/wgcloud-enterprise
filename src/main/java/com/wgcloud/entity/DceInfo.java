/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.entity;

import com.wgcloud.entity.BaseEntity;
import java.util.Date;

public class DceInfo
extends BaseEntity {
    private static final long serialVersionUID = 1L;
    private String hostname;
    private String remark;
    private String groupId;
    private Integer resTimes;
    private String active;
    private Integer warnCount;
    private String warnQueryWd;
    private Date createTime;
    private String account;
    private String warnLevel;
    private Integer orderNum;
    private String serverBackupMark;
    private String testErrorMsg;

    public String getHostname() {
        return this.hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getResTimes() {
        return this.resTimes;
    }

    public void setResTimes(Integer resTimes) {
        this.resTimes = resTimes;
    }

    public String getActive() {
        return this.active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getWarnCount() {
        return this.warnCount;
    }

    public void setWarnCount(Integer warnCount) {
        this.warnCount = warnCount;
    }

    public String getGroupId() {
        return this.groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
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

    public String getWarnLevel() {
        return this.warnLevel;
    }

    public void setWarnLevel(String warnLevel) {
        this.warnLevel = warnLevel;
    }

    public Integer getOrderNum() {
        return this.orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
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

