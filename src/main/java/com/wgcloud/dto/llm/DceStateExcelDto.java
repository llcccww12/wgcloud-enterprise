/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.dto.llm;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import java.io.Serializable;

public class DceStateExcelDto
implements Serializable {
    private static final long serialVersionUID = 1L;
    @ExcelProperty(value={"\u8bbe\u5907IP"}, index=0)
    @ColumnWidth(value=20)
    private String hostname;
    @ExcelProperty(value={"\u91c7\u96c6\u4e0a\u62a5\u65f6\u95f4"}, index=1)
    @ColumnWidth(value=20)
    private String createTime;
    @ExcelProperty(value={"\u54cd\u5e94\u65f6\u95f4(ms)"}, index=2)
    @ColumnWidth(value=20)
    private Integer resTimes;

    public String getHostname() {
        return this.hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Integer getResTimes() {
        return this.resTimes;
    }

    public void setResTimes(Integer resTimes) {
        this.resTimes = resTimes;
    }
}

