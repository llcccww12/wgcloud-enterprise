/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.entity;

import com.wgcloud.entity.BaseEntity;
import java.util.Date;

public class ReportInfo
extends BaseEntity {
    private static final long serialVersionUID = 1L;
    private String timePart;
    private String reportType;
    private String groupName;
    private Date createTime;

    public String getTimePart() {
        return this.timePart;
    }

    public void setTimePart(String timePart) {
        this.timePart = timePart;
    }

    public String getReportType() {
        return this.reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getGroupName() {
        return this.groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}

