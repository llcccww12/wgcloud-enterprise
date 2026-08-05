/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.dto.llm;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import java.io.Serializable;

public class DiskIoStateExcelDto
implements Serializable {
    private static final long serialVersionUID = 1L;
    @ExcelProperty(value={"\u4e3b\u673aIP"}, index=0)
    @ColumnWidth(value=20)
    private String hostname;
    @ExcelProperty(value={"\u78c1\u76d8\u603b\u8bfb\u53d6\u901f\u7387/\u79d2"}, index=1)
    @ColumnWidth(value=25)
    private String readIoAvg;
    @ExcelProperty(value={"\u78c1\u76d8\u603b\u5199\u5165\u901f\u7387/\u79d2"}, index=2)
    @ColumnWidth(value=25)
    private String writeIoAvg;
    @ExcelProperty(value={"\u78c1\u76d8\u8bfb\u53d6\u6b21\u6570/\u79d2"}, index=3)
    @ColumnWidth(value=25)
    private String readIoCountAvg;
    @ExcelProperty(value={"\u78c1\u76d8\u5199\u5165\u6b21\u6570/\u79d2"}, index=4)
    @ColumnWidth(value=25)
    private String writeIoCountAvg;
    @ExcelProperty(value={"\u91c7\u96c6\u4e0a\u62a5\u65f6\u95f4"}, index=5)
    @ColumnWidth(value=20)
    private String createTime;

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

    public String getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getReadIoCountAvg() {
        return this.readIoCountAvg;
    }

    public void setReadIoCountAvg(String readIoCountAvg) {
        this.readIoCountAvg = readIoCountAvg;
    }

    public String getWriteIoCountAvg() {
        return this.writeIoCountAvg;
    }

    public void setWriteIoCountAvg(String writeIoCountAvg) {
        this.writeIoCountAvg = writeIoCountAvg;
    }
}

