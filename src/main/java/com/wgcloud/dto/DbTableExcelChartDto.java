/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import java.io.Serializable;

public class DbTableExcelChartDto
implements Serializable {
    private static final long serialVersionUID = 1L;
    @ExcelProperty(value={"\u65f6\u95f4"}, index=0)
    @ColumnWidth(value=20)
    private String datetime;
    @ExcelProperty(value={"\u6570\u636e\u91cf"}, index=1)
    @ColumnWidth(value=18)
    private String tableCount;

    public String getDatetime() {
        return this.datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getTableCount() {
        return this.tableCount;
    }

    public void setTableCount(String tableCount) {
        this.tableCount = tableCount;
    }
}

