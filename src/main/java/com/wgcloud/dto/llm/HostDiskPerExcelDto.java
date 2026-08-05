/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.dto.llm;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import java.io.Serializable;

public class HostDiskPerExcelDto
implements Serializable {
    private static final long serialVersionUID = 1L;
    @ExcelProperty(value={"\u4e3b\u673aIP"}, index=0)
    @ColumnWidth(value=20)
    private String hostname;
    @ExcelProperty(value={"\u78c1\u76d8\u603b\u4f7f\u7528\u7387%"}, index=1)
    @ColumnWidth(value=20)
    private Double diskSumPer;
    @ExcelProperty(value={"\u78c1\u76d8\u603b\u5927\u5c0f"}, index=2)
    @ColumnWidth(value=18)
    private String diskSumSize;
    @ExcelProperty(value={"\u91c7\u96c6\u4e0a\u62a5\u65f6\u95f4"}, index=3)
    @ColumnWidth(value=20)
    private String createTime;

    public String getHostname() {
        return this.hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public Double getDiskSumPer() {
        return this.diskSumPer;
    }

    public void setDiskSumPer(Double diskSumPer) {
        this.diskSumPer = diskSumPer;
    }

    public String getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getDiskSumSize() {
        return this.diskSumSize;
    }

    public void setDiskSumSize(String diskSumSize) {
        this.diskSumSize = diskSumSize;
    }
}

