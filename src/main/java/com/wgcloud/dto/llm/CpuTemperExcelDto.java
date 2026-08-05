/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.dto.llm;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import java.io.Serializable;

public class CpuTemperExcelDto
implements Serializable {
    private static final long serialVersionUID = 1L;
    @ExcelProperty(value={"\u4e3b\u673aIP"}, index=0)
    @ColumnWidth(value=20)
    private String hostname;
    @ExcelProperty(value={"cpu\u6807\u8bc6"}, index=1)
    @ColumnWidth(value=30)
    private String core_index;
    @ExcelProperty(value={"\u4e34\u754c\u503c\u6e29\u5ea6"}, index=2)
    @ColumnWidth(value=20)
    private String crit;
    @ExcelProperty(value={"\u5f53\u524d\u6e29\u5ea6"}, index=3)
    @ColumnWidth(value=20)
    private String input;
    @ExcelProperty(value={"\u6700\u9ad8\u6e29\u5ea6"}, index=4)
    @ColumnWidth(value=20)
    private String max;
    @ExcelProperty(value={"\u91c7\u96c6\u4e0a\u62a5\u65f6\u95f4"}, index=5)
    @ColumnWidth(value=20)
    private String createTime;

    public String getHostname() {
        return this.hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getCore_index() {
        return this.core_index;
    }

    public void setCore_index(String core_index) {
        this.core_index = core_index;
    }

    public String getCrit() {
        return this.crit;
    }

    public void setCrit(String crit) {
        this.crit = crit;
    }

    public String getInput() {
        return this.input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getMax() {
        return this.max;
    }

    public void setMax(String max) {
        this.max = max;
    }

    public String getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}

