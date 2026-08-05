/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import java.io.Serializable;

public class AppExcelChartDto
implements Serializable {
    private static final long serialVersionUID = 1L;
    @ExcelProperty(value={"\u4e0a\u62a5\u65f6\u95f4"}, index=0)
    @ColumnWidth(value=18)
    private String datetime;
    @ExcelProperty(value={"cpu\u4f7f\u7528\u7387%"}, index=1)
    @ColumnWidth(value=18)
    private Double cpuPer;
    @ExcelProperty(value={"\u5185\u5b58\u4f7f\u7528\u7387%"}, index=2)
    @ColumnWidth(value=18)
    private Double memPer;
    @ExcelProperty(value={"\u7ebf\u7a0b\u6570"}, index=3)
    @ColumnWidth(value=18)
    private String threadsNum;
    @ExcelProperty(value={"\u8fde\u63a5\u6570"}, index=4)
    @ColumnWidth(value=18)
    private String netConnections;

    public String getDatetime() {
        return this.datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public Double getCpuPer() {
        return this.cpuPer;
    }

    public void setCpuPer(Double cpuPer) {
        this.cpuPer = cpuPer;
    }

    public Double getMemPer() {
        return this.memPer;
    }

    public void setMemPer(Double memPer) {
        this.memPer = memPer;
    }

    public String getThreadsNum() {
        return this.threadsNum;
    }

    public void setThreadsNum(String threadsNum) {
        this.threadsNum = threadsNum;
    }

    public String getNetConnections() {
        return this.netConnections;
    }

    public void setNetConnections(String netConnections) {
        this.netConnections = netConnections;
    }
}

