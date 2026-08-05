/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.entity;

import com.wgcloud.entity.BaseEntity;
import com.wgcloud.util.DateUtil;
import java.util.Date;
import org.apache.commons.lang3.StringUtils;

public class GpuState
extends BaseEntity {
    private static final long serialVersionUID = 1L;
    private String hostname;
    private String gpuRate;
    private String dateStr;
    private Date createTime;

    public String getHostname() {
        return this.hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getGpuRate() {
        return this.gpuRate;
    }

    public void setGpuRate(String gpuRate) {
        this.gpuRate = gpuRate;
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

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}

