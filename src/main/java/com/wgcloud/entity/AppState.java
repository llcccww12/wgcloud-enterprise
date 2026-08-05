/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.entity;

import com.wgcloud.entity.BaseEntity;
import com.wgcloud.util.DateUtil;
import java.util.Date;
import org.apache.commons.lang3.StringUtils;

public class AppState
extends BaseEntity {
    private static final long serialVersionUID = 1L;
    private String appInfoId;
    private Double cpuPer;
    private Double memPer;
    private String threadsNum;
    private Integer threadsNumInt;
    private String dateStr;
    private Date createTime;
    private String netConnections;
    private Integer netConnectionsInt;

    public Double getCpuPer() {
        return this.cpuPer;
    }

    public void setCpuPer(Double cpuPer) {
        this.cpuPer = cpuPer;
    }

    public String getAppInfoId() {
        return this.appInfoId;
    }

    public void setAppInfoId(String appInfoId) {
        this.appInfoId = appInfoId;
    }

    public Double getMemPer() {
        return this.memPer;
    }

    public void setMemPer(Double memPer) {
        this.memPer = memPer;
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

    public String getThreadsNum() {
        return this.threadsNum;
    }

    public void setThreadsNum(String threadsNum) {
        this.threadsNum = threadsNum;
    }

    public Integer getThreadsNumInt() {
        if (!StringUtils.isEmpty((CharSequence)this.threadsNum)) {
            try {
                this.threadsNumInt = Integer.valueOf(this.threadsNum);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            this.threadsNumInt = 0;
        }
        return this.threadsNumInt;
    }

    public void setThreadsNumInt(Integer threadsNumInt) {
        this.threadsNumInt = threadsNumInt;
    }

    public String getNetConnections() {
        return this.netConnections;
    }

    public void setNetConnections(String netConnections) {
        this.netConnections = netConnections;
    }

    public Integer getNetConnectionsInt() {
        if (!StringUtils.isEmpty((CharSequence)this.netConnections)) {
            try {
                this.netConnectionsInt = Integer.valueOf(this.netConnections);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            this.netConnectionsInt = 0;
        }
        return this.netConnectionsInt;
    }

    public void setNetConnectionsInt(Integer netConnectionsInt) {
        this.netConnectionsInt = netConnectionsInt;
    }
}

