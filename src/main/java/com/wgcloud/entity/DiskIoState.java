/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.entity;

import com.wgcloud.entity.BaseEntity;
import com.wgcloud.util.DateUtil;
import java.util.Date;
import org.apache.commons.lang3.StringUtils;

public class DiskIoState
extends BaseEntity {
    private static final long serialVersionUID = 1L;
    private String hostname;
    private String readIoAvg;
    private Double readIoAvgDouble;
    private String writeIoAvg;
    private Double writeIoAvgDouble;
    private String readIoCountAvg;
    private Double readIoCountAvgDouble;
    private String writeIoCountAvg;
    private Double writeIoCountAvgDouble;
    private Date createTime;
    private String dateStr;

    public String getHostname() {
        return this.hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getReadIoAvg() {
        return this.readIoAvg;
    }

    public void setReadIoAvg(String readIoAvg) {
        this.readIoAvg = readIoAvg;
    }

    public String getWriteIoAvg() {
        return this.writeIoAvg;
    }

    public void setWriteIoAvg(String writeIoAvg) {
        this.writeIoAvg = writeIoAvg;
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

    public Double getReadIoAvgDouble() {
        if (!StringUtils.isEmpty((CharSequence)this.readIoAvg)) {
            try {
                this.readIoAvgDouble = Double.valueOf(this.readIoAvg);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            this.readIoAvgDouble = 0.0;
        }
        return this.readIoAvgDouble;
    }

    public void setReadIoAvgDouble(Double readIoAvgDouble) {
        this.readIoAvgDouble = readIoAvgDouble;
    }

    public Double getWriteIoAvgDouble() {
        if (!StringUtils.isEmpty((CharSequence)this.writeIoAvg)) {
            try {
                this.writeIoAvgDouble = Double.valueOf(this.writeIoAvg);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            this.writeIoAvgDouble = 0.0;
        }
        return this.writeIoAvgDouble;
    }

    public void setWriteIoAvgDouble(Double writeIoAvgDouble) {
        this.writeIoAvgDouble = writeIoAvgDouble;
    }

    public String getReadIoCountAvg() {
        return this.readIoCountAvg;
    }

    public void setReadIoCountAvg(String readIoCountAvg) {
        this.readIoCountAvg = readIoCountAvg;
    }

    public Double getReadIoCountAvgDouble() {
        if (!StringUtils.isEmpty((CharSequence)this.readIoCountAvg)) {
            try {
                this.readIoCountAvgDouble = Double.valueOf(this.readIoCountAvg);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            this.readIoCountAvgDouble = 0.0;
        }
        return this.readIoCountAvgDouble;
    }

    public void setReadIoCountAvgDouble(Double readIoCountAvgDouble) {
        this.readIoCountAvgDouble = readIoCountAvgDouble;
    }

    public String getWriteIoCountAvg() {
        return this.writeIoCountAvg;
    }

    public void setWriteIoCountAvg(String writeIoCountAvg) {
        this.writeIoCountAvg = writeIoCountAvg;
    }

    public Double getWriteIoCountAvgDouble() {
        if (!StringUtils.isEmpty((CharSequence)this.writeIoCountAvg)) {
            try {
                this.writeIoCountAvgDouble = Double.valueOf(this.writeIoCountAvg);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            this.writeIoCountAvgDouble = 0.0;
        }
        return this.writeIoCountAvgDouble;
    }

    public void setWriteIoCountAvgDouble(Double writeIoCountAvgDouble) {
        this.writeIoCountAvgDouble = writeIoCountAvgDouble;
    }
}

