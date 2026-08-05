/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import java.io.Serializable;

public class TjbbExcelChartDto
implements Serializable {
    private static final long serialVersionUID = 1L;
    @ExcelProperty(value={"\u5de1\u68c0\u9879"}, index=0)
    @ColumnWidth(value=30)
    private String infoKey;
    @ExcelProperty(value={"\u63cf\u8ff0"}, index=1)
    @ColumnWidth(value=50)
    private String infoContent;

    public String getInfoKey() {
        return this.infoKey;
    }

    public void setInfoKey(String infoKey) {
        this.infoKey = infoKey;
    }

    public String getInfoContent() {
        return this.infoContent;
    }

    public void setInfoContent(String infoContent) {
        this.infoContent = infoContent;
    }
}

