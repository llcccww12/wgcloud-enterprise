/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import java.io.Serializable;

public class DockerExcelChartDto
implements Serializable {
    private static final long serialVersionUID = 1L;
    @ExcelProperty(value={"\u4e0a\u62a5\u65f6\u95f4"}, index=0)
    @ColumnWidth(value=18)
    private String datetime;
    @ExcelProperty(value={"\u5185\u5b58\u4f7f\u7528MB"}, index=1)
    @ColumnWidth(value=18)
    private Double memPer;
    @ExcelProperty(value={"cpu\u4f7f\u7528\u7387%"}, index=2)
    @ColumnWidth(value=18)
    private Double cpuPer;

    public String getDatetime() {
        return this.datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public Double getMemPer() {
        return this.memPer;
    }

    public void setMemPer(Double memPer) {
        this.memPer = memPer;
    }

    public Double getCpuPer() {
        return this.cpuPer;
    }

    public void setCpuPer(Double cpuPer) {
        this.cpuPer = cpuPer;
    }
}

