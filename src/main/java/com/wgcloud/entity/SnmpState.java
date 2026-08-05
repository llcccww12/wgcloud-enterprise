/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.entity;

import com.wgcloud.entity.BaseEntity;
import com.wgcloud.util.DateUtil;
import java.util.Date;
import org.apache.commons.lang3.StringUtils;

public class SnmpState
extends BaseEntity {
    private static final long serialVersionUID = 1L;
    private String snmpInfoId;
    private String recvAvg;
    private Double recvAvgDouble;
    private String sentAvg;
    private Double sentAvgDouble;
    private String cpuPer;
    private Double cpuPerDouble;
    private String memPer;
    private Double memPerDouble;
    private String voltageValue;
    private Double voltageValueDouble;
    private String temperatureValue;
    private Double temperatureValueDouble;
    private String dateStr;
    private Date createTime;

    public String getSnmpInfoId() {
        return this.snmpInfoId;
    }

    public void setSnmpInfoId(String snmpInfoId) {
        this.snmpInfoId = snmpInfoId;
    }

    public String getRecvAvg() {
        return this.recvAvg;
    }

    public void setRecvAvg(String recvAvg) {
        this.recvAvg = recvAvg;
    }

    public String getSentAvg() {
        return this.sentAvg;
    }

    public void setSentAvg(String sentAvg) {
        this.sentAvg = sentAvg;
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

    public Double getRecvAvgDouble() {
        if (!StringUtils.isEmpty((CharSequence)this.recvAvg)) {
            try {
                this.recvAvgDouble = Double.valueOf(this.recvAvg);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            this.recvAvgDouble = 0.0;
        }
        return this.recvAvgDouble;
    }

    public void setRecvAvgDouble(Double recvAvgDouble) {
        this.recvAvgDouble = recvAvgDouble;
    }

    public Double getSentAvgDouble() {
        if (!StringUtils.isEmpty((CharSequence)this.sentAvg)) {
            try {
                this.sentAvgDouble = Double.valueOf(this.sentAvg);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            this.sentAvgDouble = 0.0;
        }
        return this.sentAvgDouble;
    }

    public void setSentAvgDouble(Double sentAvgDouble) {
        this.sentAvgDouble = sentAvgDouble;
    }

    public String getCpuPer() {
        return this.cpuPer;
    }

    public void setCpuPer(String cpuPer) {
        this.cpuPer = cpuPer;
    }

    public Double getCpuPerDouble() {
        if (!StringUtils.isEmpty((CharSequence)this.cpuPer)) {
            try {
                this.cpuPerDouble = Double.valueOf(this.cpuPer);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            this.cpuPerDouble = 0.0;
        }
        return this.cpuPerDouble;
    }

    public void setCpuPerDouble(Double cpuPerDouble) {
        this.cpuPerDouble = cpuPerDouble;
    }

    public String getMemPer() {
        return this.memPer;
    }

    public void setMemPer(String memPer) {
        this.memPer = memPer;
    }

    public Double getMemPerDouble() {
        if (!StringUtils.isEmpty((CharSequence)this.memPer)) {
            try {
                this.memPerDouble = Double.valueOf(this.memPer);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            this.memPerDouble = 0.0;
        }
        return this.memPerDouble;
    }

    public void setMemPerDouble(Double memPerDouble) {
        this.memPerDouble = memPerDouble;
    }

    public String getVoltageValue() {
        return this.voltageValue;
    }

    public void setVoltageValue(String voltageValue) {
        this.voltageValue = voltageValue;
    }

    public Double getVoltageValueDouble() {
        if (!StringUtils.isEmpty((CharSequence)this.voltageValue)) {
            try {
                this.voltageValueDouble = Double.valueOf(this.voltageValue);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            this.voltageValueDouble = 0.0;
        }
        return this.voltageValueDouble;
    }

    public void setVoltageValueDouble(Double voltageValueDouble) {
        this.voltageValueDouble = voltageValueDouble;
    }

    public String getTemperatureValue() {
        return this.temperatureValue;
    }

    public void setTemperatureValue(String temperatureValue) {
        this.temperatureValue = temperatureValue;
    }

    public Double getTemperatureValueDouble() {
        if (!StringUtils.isEmpty((CharSequence)this.temperatureValue)) {
            try {
                this.temperatureValueDouble = Double.valueOf(this.temperatureValue);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            this.temperatureValueDouble = 0.0;
        }
        return this.temperatureValueDouble;
    }

    public void setTemperatureValueDouble(Double temperatureValueDouble) {
        this.temperatureValueDouble = temperatureValueDouble;
    }
}

