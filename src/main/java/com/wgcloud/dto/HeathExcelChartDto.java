/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import java.io.Serializable;

public class HeathExcelChartDto
implements Serializable {
    private static final long serialVersionUID = 1L;
    @ExcelProperty(value={"\u4e0a\u62a5\u65f6\u95f4"}, index=0)
    @ColumnWidth(value=18)
    private String datetime;
    @ExcelProperty(value={"\u54cd\u5e94\u65f6\u95f4(ms)"}, index=1)
    @ColumnWidth(value=18)
    private Integer resTimes;

    public String getDatetime() {
        return this.datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public Integer getResTimes() {
        return this.resTimes;
    }

    public void setResTimes(Integer resTimes) {
        this.resTimes = resTimes;
    }
}

