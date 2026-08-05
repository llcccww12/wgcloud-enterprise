/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.dto.llm;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import java.io.Serializable;

public class LogInfoExcelDto
implements Serializable {
    private static final long serialVersionUID = 1L;
    @ExcelProperty(value={"\u65e5\u5fd7\u6458\u8981\u6807\u9898"}, index=0)
    @ColumnWidth(value=50)
    private String hostname;
    @ExcelProperty(value={"\u7cfb\u7edf\u65e5\u5fd7\u7c7b\u578b"}, index=1)
    @ColumnWidth(value=20)
    private String state;
    @ExcelProperty(value={"\u6dfb\u52a0\u65f6\u95f4"}, index=2)
    @ColumnWidth(value=20)
    private String createTime;
    @ExcelProperty(value={"\u7cfb\u7edf\u65e5\u5fd7\u5185\u5bb9"}, index=3)
    @ColumnWidth(value=100)
    private String infoContent;

    public String getHostname() {
        return this.hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getState() {
        return this.state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getInfoContent() {
        return this.infoContent;
    }

    public void setInfoContent(String infoContent) {
        this.infoContent = infoContent;
    }
}

