/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import java.io.Serializable;

public class SnmpExcelChartDto
implements Serializable {
    private static final long serialVersionUID = 1L;
    @ExcelProperty(value={"\u4e0a\u62a5\u65f6\u95f4"}, index=0)
    @ColumnWidth(value=18)
    private String datetime;
    @ExcelProperty(value={"\u4e0b\u884c\u901f\u7387(MB/s)"}, index=1)
    @ColumnWidth(value=18)
    private String recvAvg;
    @ExcelProperty(value={"\u4e0a\u884c\u901f\u7387(MB/s)"}, index=2)
    @ColumnWidth(value=18)
    private String sentAvg;
    @ExcelProperty(value={"CPU\u4f7f\u7528\u7387%"}, index=3)
    @ColumnWidth(value=18)
    private String cpuPer;
    @ExcelProperty(value={"\u5185\u5b58\u4f7f\u7528\u7387%"}, index=4)
    @ColumnWidth(value=18)
    private String memPer;
    @ExcelProperty(value={"\u8bbe\u5907\u6e29\u5ea6\u2103"}, index=5)
    @ColumnWidth(value=18)
    private String temperatureValue;

    public String getDatetime() {
        return this.datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
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

    public String getCpuPer() {
        return this.cpuPer;
    }

    public void setCpuPer(String cpuPer) {
        this.cpuPer = cpuPer;
    }

    public String getMemPer() {
        return this.memPer;
    }

    public void setMemPer(String memPer) {
        this.memPer = memPer;
    }

    public String getTemperatureValue() {
        return this.temperatureValue;
    }

    public void setTemperatureValue(String temperatureValue) {
        this.temperatureValue = temperatureValue;
    }
}

