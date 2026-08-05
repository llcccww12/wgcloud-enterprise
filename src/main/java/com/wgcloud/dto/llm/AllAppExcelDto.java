/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.dto.llm;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import java.io.Serializable;

public class AllAppExcelDto
implements Serializable {
    private static final long serialVersionUID = 1L;
    @ExcelProperty(value={"\u4e3b\u673aIP"}, index=0)
    @ColumnWidth(value=20)
    private String hostname;
    @ExcelProperty(value={"\u4e0a\u62a5\u65f6\u95f4"}, index=1)
    @ColumnWidth(value=20)
    private String datetime;
    @ExcelProperty(value={"\u8fdb\u7a0b\u540d\u79f0"}, index=2)
    @ColumnWidth(value=20)
    private String appName;
    @ExcelProperty(value={"\u8fdb\u7a0bPID"}, index=3)
    @ColumnWidth(value=20)
    private String gatherPid;
    @ExcelProperty(value={"\u8fdb\u7a0b\u542f\u52a8\u65f6\u95f4"}, index=4)
    @ColumnWidth(value=20)
    private String appTimes;
    @ExcelProperty(value={"\u8fdb\u7a0b\u6240\u6709\u8005"}, index=5)
    @ColumnWidth(value=20)
    private String proUsername;
    @ExcelProperty(value={"cpu\u4f7f\u7528\u7387%"}, index=6)
    @ColumnWidth(value=18)
    private Double cpuPer;
    @ExcelProperty(value={"\u5185\u5b58\u4f7f\u7528\u7387%"}, index=7)
    @ColumnWidth(value=18)
    private Double memPer;
    @ExcelProperty(value={"\u8fdb\u7a0b\u542f\u52a8\u547d\u4ee4"}, index=8)
    @ColumnWidth(value=60)
    private String appCmdLine;

    public String getDatetime() {
        return this.datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getAppName() {
        return this.appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getGatherPid() {
        return this.gatherPid;
    }

    public void setGatherPid(String gatherPid) {
        this.gatherPid = gatherPid;
    }

    public String getAppTimes() {
        return this.appTimes;
    }

    public void setAppTimes(String appTimes) {
        this.appTimes = appTimes;
    }

    public String getProUsername() {
        return this.proUsername;
    }

    public void setProUsername(String proUsername) {
        this.proUsername = proUsername;
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

    public String getAppCmdLine() {
        return this.appCmdLine;
    }

    public void setAppCmdLine(String appCmdLine) {
        this.appCmdLine = appCmdLine;
    }

    public String getHostname() {
        return this.hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }
}

