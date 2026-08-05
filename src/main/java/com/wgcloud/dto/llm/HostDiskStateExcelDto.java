/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.dto.llm;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import java.io.Serializable;

public class HostDiskStateExcelDto
implements Serializable {
    private static final long serialVersionUID = 1L;
    @ExcelProperty(value={"\u4e3b\u673aIP"}, index=0)
    @ColumnWidth(value=18)
    private String hostName;
    @ExcelProperty(value={"\u91c7\u96c6\u4e0a\u62a5\u65f6\u95f4"}, index=1)
    @ColumnWidth(value=20)
    private String datetime;
    @ExcelProperty(value={"\u78c1\u76d8\u540d\u79f0"}, index=2)
    @ColumnWidth(value=18)
    private String fileSystem;
    @ExcelProperty(value={"\u78c1\u76d8\u7a7a\u95f4\u5927\u5c0f"}, index=3)
    @ColumnWidth(value=18)
    private String diskSize;
    @ExcelProperty(value={"\u5df2\u4f7f\u7528\u5927\u5c0f"}, index=4)
    @ColumnWidth(value=18)
    private String used;
    @ExcelProperty(value={"\u53ef\u7528\u5927\u5c0f"}, index=5)
    @ColumnWidth(value=18)
    private String avail;
    @ExcelProperty(value={"\u5df2\u4f7f\u7528\u767e\u5206\u6bd4%"}, index=6)
    @ColumnWidth(value=20)
    private String usePer;

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

    public String getFileSystem() {
        return this.fileSystem;
    }

    public void setFileSystem(String fileSystem) {
        this.fileSystem = fileSystem;
    }

    public String getDiskSize() {
        return this.diskSize;
    }

    public void setDiskSize(String diskSize) {
        this.diskSize = diskSize;
    }

    public String getUsed() {
        return this.used;
    }

    public void setUsed(String used) {
        this.used = used;
    }

    public String getAvail() {
        return this.avail;
    }

    public void setAvail(String avail) {
        this.avail = avail;
    }

    public String getUsePer() {
        return this.usePer;
    }

    public void setUsePer(String usePer) {
        this.usePer = usePer;
    }
}

