/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.dto.llm;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import java.io.Serializable;

public class HostLoadStateExcelDto
implements Serializable {
    private static final long serialVersionUID = 1L;
    @ExcelProperty(value={"\u4e3b\u673aIP"}, index=0)
    @ColumnWidth(value=18)
    private String hostName;
    @ExcelProperty(value={"\u91c7\u96c6\u4e0a\u62a5\u65f6\u95f4"}, index=1)
    @ColumnWidth(value=20)
    private String datetime;
    @ExcelProperty(value={"1\u5206\u949f\u8d1f\u8f7d\u503c"}, index=2)
    @ColumnWidth(value=18)
    private Double oneLoad;
    @ExcelProperty(value={"5\u5206\u949f\u8d1f\u8f7d\u503c"}, index=3)
    @ColumnWidth(value=18)
    private Double fiveLoad;
    @ExcelProperty(value={"15\u5206\u949f\u8d1f\u8f7d\u503c"}, index=4)
    @ColumnWidth(value=18)
    private Double fifteenLoad;

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

    public Double getOneLoad() {
        return this.oneLoad;
    }

    public void setOneLoad(Double oneLoad) {
        this.oneLoad = oneLoad;
    }

    public Double getFiveLoad() {
        return this.fiveLoad;
    }

    public void setFiveLoad(Double fiveLoad) {
        this.fiveLoad = fiveLoad;
    }

    public Double getFifteenLoad() {
        return this.fifteenLoad;
    }

    public void setFifteenLoad(Double fifteenLoad) {
        this.fifteenLoad = fifteenLoad;
    }
}

