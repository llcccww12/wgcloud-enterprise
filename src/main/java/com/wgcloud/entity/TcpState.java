/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.entity;

import com.wgcloud.entity.BaseEntity;
import java.util.Date;
import org.apache.commons.lang3.StringUtils;

public class TcpState
extends BaseEntity {
    private static final long serialVersionUID = 1L;
    private String hostname;
    private String active;
    private String passive;
    private String retrans;
    private String dateStr;
    private Date createTime;

    public String getActive() {
        return this.active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getPassive() {
        return this.passive;
    }

    public void setPassive(String passive) {
        this.passive = passive;
    }

    public String getRetrans() {
        return this.retrans;
    }

    public void setRetrans(String retrans) {
        this.retrans = retrans;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getDateStr() {
        if (!StringUtils.isEmpty((CharSequence)this.dateStr) && this.dateStr.length() > 16) {
            return this.dateStr.substring(5);
        }
        return this.dateStr;
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }

    public String getHostname() {
        return this.hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }
}

