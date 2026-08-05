/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.dto.llm;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import java.io.Serializable;

public class HostMacExcelDto
implements Serializable {
    private static final long serialVersionUID = 1L;
    @ExcelProperty(value={"\u4e3b\u673aIP"}, index=0)
    @ColumnWidth(value=18)
    private String hostname;
    @ExcelProperty(value={"\u7f51\u5361\u540d\u79f0"}, index=1)
    @ColumnWidth(value=20)
    private String macName;
    @ExcelProperty(value={"MAC\u5730\u5740"}, index=2)
    @ColumnWidth(value=20)
    private String macAddress;
    @ExcelProperty(value={"\u91c7\u96c6\u4e0a\u62a5\u65f6\u95f4"}, index=3)
    @ColumnWidth(value=20)
    private String createTime;

    public String getHostname() {
        return this.hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getMacName() {
        return this.macName;
    }

    public void setMacName(String macName) {
        this.macName = macName;
    }

    public String getMacAddress() {
        return this.macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}

