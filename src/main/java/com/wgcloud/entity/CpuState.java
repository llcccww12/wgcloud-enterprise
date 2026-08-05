/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.entity;

import com.wgcloud.entity.BaseEntity;
import com.wgcloud.util.DateUtil;
import java.util.Date;
import org.apache.commons.lang3.StringUtils;

public class CpuState
extends BaseEntity {
    private static final long serialVersionUID = 1L;
    private String hostname;
    private String userPer;
    private Double sys;
    private Double idle;
    private Double iowait;
    private String irq;
    private String soft;
    private String dateStr;
    private Date createTime;
    private Integer procsNum;

    public String getUserPer() {
        return this.userPer;
    }

    public void setUserPer(String userPer) {
        this.userPer = userPer;
    }

    public Double getSys() {
        return this.sys;
    }

    public void setSys(Double sys) {
        this.sys = sys;
    }

    public Double getIdle() {
        return this.idle;
    }

    public void setIdle(Double idle) {
        this.idle = idle;
    }

    public Double getIowait() {
        return this.iowait;
    }

    public void setIowait(Double iowait) {
        this.iowait = iowait;
    }

    public String getIrq() {
        return this.irq;
    }

    public void setIrq(String irq) {
        this.irq = irq;
    }

    public String getSoft() {
        return this.soft;
    }

    public void setSoft(String soft) {
        this.soft = soft;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getDateStr() {
        String s = DateUtil.getDateTimeString(this.getCreateTime());
        if (!StringUtils.isEmpty((CharSequence)s) && s.length() > 16) {
            return s.substring(5);
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

    public Integer getProcsNum() {
        return this.procsNum;
    }

    public void setProcsNum(Integer procsNum) {
        this.procsNum = procsNum;
    }
}

