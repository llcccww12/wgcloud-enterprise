/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import java.io.Serializable;

public class DceInfoListExcelDto
implements Serializable {
    private static final long serialVersionUID = 1L;
    @ExcelProperty(value={"IP"}, index=0)
    @ColumnWidth(value=18)
    private String hostname;
    @ExcelProperty(value={"\u54cd\u5e94\u65f6\u95f4(ms)"}, index=1)
    @ColumnWidth(value=20)
    private String resTimes;
    @ExcelProperty(value={"\u6807\u7b7e"}, index=2)
    @ColumnWidth(value=18)
    private String groupId;
    @ExcelProperty(value={"\u5907\u6ce8"}, index=3)
    @ColumnWidth(value=18)
    private String remark;
    @ExcelProperty(value={"\u6240\u5c5e\u8d26\u53f7"}, index=4)
    @ColumnWidth(value=18)
    private String account;
    @ExcelProperty(value={"\u66f4\u65b0\u65f6\u95f4"}, index=5)
    @ColumnWidth(value=20)
    private String createTime;

    public String getHostname() {
        return this.hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getGroupId() {
        return this.groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getResTimes() {
        return this.resTimes;
    }

    public void setResTimes(String resTimes) {
        this.resTimes = resTimes;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getAccount() {
        return this.account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}

