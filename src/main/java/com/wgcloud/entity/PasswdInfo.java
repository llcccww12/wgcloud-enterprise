/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.entity;

import com.wgcloud.entity.BaseEntity;
import java.util.Date;

public class PasswdInfo
extends BaseEntity {
    private static final long serialVersionUID = 1L;
    private String hostname;
    private String hostMark;
    private String hostAccount;
    private String hostPasswd;
    private String hostRemark;
    private String account;
    private Date createTime;
    private String groupId;

    public String getHostname() {
        return this.hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getHostAccount() {
        return this.hostAccount;
    }

    public void setHostAccount(String hostAccount) {
        this.hostAccount = hostAccount;
    }

    public String getHostPasswd() {
        return this.hostPasswd;
    }

    public void setHostPasswd(String hostPasswd) {
        this.hostPasswd = hostPasswd;
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

    public String getGroupId() {
        return this.groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getHostRemark() {
        return this.hostRemark;
    }

    public void setHostRemark(String hostRemark) {
        this.hostRemark = hostRemark;
    }

    public String getHostMark() {
        return this.hostMark;
    }

    public void setHostMark(String hostMark) {
        this.hostMark = hostMark;
    }
}

