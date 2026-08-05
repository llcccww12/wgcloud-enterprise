/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.dto.llm;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import java.io.Serializable;

public class NetworkExcelDto
implements Serializable {
    private static final long serialVersionUID = 1L;
    @ExcelProperty(value={"\u4e3b\u673aIP"}, index=0)
    @ColumnWidth(value=18)
    private String hostName;
    @ExcelProperty(value={"\u7f51\u5361\u540d\u79f0"}, index=1)
    @ColumnWidth(value=18)
    private String name;
    @ExcelProperty(value={"\u603b\u8ba1\u53d1\u9001\u6d41\u91cfGB"}, index=2)
    @ColumnWidth(value=25)
    private String bytesSent;
    @ExcelProperty(value={"\u603b\u8ba1\u63a5\u6536\u6d41\u91cfGB"}, index=3)
    @ColumnWidth(value=25)
    private String bytesRecv;

    public String getHostName() {
        return this.hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBytesSent() {
        return this.bytesSent;
    }

    public void setBytesSent(String bytesSent) {
        this.bytesSent = bytesSent;
    }

    public String getBytesRecv() {
        return this.bytesRecv;
    }

    public void setBytesRecv(String bytesRecv) {
        this.bytesRecv = bytesRecv;
    }
}

