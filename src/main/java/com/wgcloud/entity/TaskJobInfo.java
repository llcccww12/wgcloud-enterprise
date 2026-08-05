/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.entity;

import com.wgcloud.entity.BaseEntity;
import java.util.Date;

public class TaskJobInfo
extends BaseEntity {
    private static final long serialVersionUID = 1L;
    private String hostname;
    private String cronValue;
    private String jobName;
    private String jobRemark;
    private String active;
    private String account;
    private Date createTime;
    private Date callBackTime;
    private String callBackState;
    private String groupId;
    private String shell;
    private String callBackResult;

    public String getHostname() {
        return this.hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getCronValue() {
        return this.cronValue;
    }

    public void setCronValue(String cronValue) {
        this.cronValue = cronValue;
    }

    public String getJobName() {
        return this.jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobRemark() {
        return this.jobRemark;
    }

    public void setJobRemark(String jobRemark) {
        this.jobRemark = jobRemark;
    }

    public String getActive() {
        return this.active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getAccount() {
        return this.account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getCallBackTime() {
        return this.callBackTime;
    }

    public void setCallBackTime(Date callBackTime) {
        this.callBackTime = callBackTime;
    }

    public String getCallBackState() {
        return this.callBackState;
    }

    public void setCallBackState(String callBackState) {
        this.callBackState = callBackState;
    }

    public String getGroupId() {
        return this.groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getShell() {
        return this.shell;
    }

    public void setShell(String shell) {
        this.shell = shell;
    }

    public String getCallBackResult() {
        return this.callBackResult;
    }

    public void setCallBackResult(String callBackResult) {
        this.callBackResult = callBackResult;
    }
}

