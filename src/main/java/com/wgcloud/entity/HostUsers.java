/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.entity;

import com.wgcloud.entity.BaseEntity;
import java.util.Date;

public class HostUsers
extends BaseEntity {
    private static final long serialVersionUID = 1L;
    private String hostname;
    private String hostUser;
    private String lastLoginIp;
    private String terminal;
    private String lastLoginTime;
    private Date createTime;

    public String getHostname() {
        return this.hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getHostUser() {
        return this.hostUser;
    }

    public void setHostUser(String hostUser) {
        this.hostUser = hostUser;
    }

    public String getLastLoginIp() {
        return this.lastLoginIp;
    }

    public void setLastLoginIp(String lastLoginIp) {
        this.lastLoginIp = lastLoginIp;
    }

    public String getLastLoginTime() {
        return this.lastLoginTime;
    }

    public void setLastLoginTime(String lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getTerminal() {
        return this.terminal;
    }

    public void setTerminal(String terminal) {
        this.terminal = terminal;
    }
}

