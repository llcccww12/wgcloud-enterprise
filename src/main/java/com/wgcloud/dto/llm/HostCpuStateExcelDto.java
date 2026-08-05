/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.dto.llm;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import java.io.Serializable;

public class HostCpuStateExcelDto
implements Serializable {
    private static final long serialVersionUID = 1L;
    @ExcelProperty(value={"\u4e3b\u673aIP"}, index=0)
    @ColumnWidth(value=18)
    private String hostName;
    @ExcelProperty(value={"\u91c7\u96c6\u4e0a\u62a5\u65f6\u95f4"}, index=1)
    @ColumnWidth(value=20)
    private String datetime;
    @ExcelProperty(value={"CPU\u4f7f\u7528\u7387%"}, index=2)
    @ColumnWidth(value=18)
    private Double cpuPer;
    @ExcelProperty(value={"\u4e3b\u673a\u8fd0\u884c\u8fdb\u7a0b\u6570\u91cf"}, index=3)
    @ColumnWidth(value=30)
    private Integer procsNum;

    public String getHostName() {
        return this.hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

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

    public Integer getProcsNum() {
        return this.procsNum;
    }

    public void setProcsNum(Integer procsNum) {
        this.procsNum = procsNum;
    }
}

