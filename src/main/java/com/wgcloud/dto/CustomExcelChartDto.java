/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import java.io.Serializable;

public class CustomExcelChartDto
implements Serializable {
    private static final long serialVersionUID = 1L;
    @ExcelProperty(value={"\u4e0a\u62a5\u65f6\u95f4"}, index=0)
    @ColumnWidth(value=18)
    private String datetime;
    @ExcelProperty(value={"\u81ea\u5b9a\u4e49\u76d1\u63a7\u9879\u503c"}, index=1)
    @ColumnWidth(value=18)
    private String customValue;

    public String getDatetime() {
        return this.datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getCustomValue() {
        return this.customValue;
    }

    public void setCustomValue(String customValue) {
        this.customValue = customValue;
    }
}

