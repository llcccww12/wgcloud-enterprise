/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.dto.llm;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import java.io.Serializable;

public class AgentRunStateExcelDto
implements Serializable {
    private static final long serialVersionUID = 1L;
    @ExcelProperty(value={"\u4e3b\u673aIP"}, index=0)
    @ColumnWidth(value=20)
    private String hostname;
    @ExcelProperty(value={"\u7d2f\u8ba1\u5728\u7ebf\u65f6\u957f"}, index=1)
    @ColumnWidth(value=20)
    private String onlineTime;
    @ExcelProperty(value={"\u7d2f\u8ba1\u79bb\u7ebf\u65f6\u957f"}, index=2)
    @ColumnWidth(value=20)
    private String downTime;
    @ExcelProperty(value={"\u6700\u540e\u5728\u7ebf\u65f6\u95f4"}, index=3)
    @ColumnWidth(value=20)
    private String lastTime;
    @ExcelProperty(value={"\u66f4\u65b0\u65f6\u95f4"}, index=4)
    @ColumnWidth(value=20)
    private String createTime;
    @ExcelProperty(value={"\u5907\u6ce8\u8bf4\u660e"}, index=5)
    @ColumnWidth(value=30)
    private String remark;

    public String getHostname() {
        return this.hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getOnlineTime() {
        return this.onlineTime;
    }

    public void setOnlineTime(String onlineTime) {
        this.onlineTime = onlineTime;
    }

    public String getDownTime() {
        return this.downTime;
    }

    public void setDownTime(String downTime) {
        this.downTime = downTime;
    }

    public String getLastTime() {
        return this.lastTime;
    }

    public void setLastTime(String lastTime) {
        this.lastTime = lastTime;
    }

    public String getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}

