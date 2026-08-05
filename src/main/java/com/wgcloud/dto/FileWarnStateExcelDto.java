/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import java.io.Serializable;

public class FileWarnStateExcelDto
implements Serializable {
    private static final long serialVersionUID = 1L;
    @ExcelProperty(value={"\u65f6\u95f4"}, index=0)
    @ColumnWidth(value=20)
    private String datetime;
    @ExcelProperty(value={"\u544a\u8b66\u5185\u5bb9"}, index=1)
    @ColumnWidth(value=30)
    private String warContent;

    public String getDatetime() {
        return this.datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getWarContent() {
        return this.warContent;
    }

    public void setWarContent(String warContent) {
        this.warContent = warContent;
    }
}

