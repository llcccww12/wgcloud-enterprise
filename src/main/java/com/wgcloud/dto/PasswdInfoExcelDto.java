/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import java.io.Serializable;

public class PasswdInfoExcelDto
implements Serializable {
    private static final long serialVersionUID = 1L;
    @ExcelProperty(value={"\u8bbe\u5907\u540d\u79f0"}, index=0)
    @ColumnWidth(value=18)
    private String hostname;
    @ExcelProperty(value={"\u8bbe\u5907\u6807\u8bc6"}, index=1)
    @ColumnWidth(value=18)
    private String hostMark;
    @ExcelProperty(value={"\u8bbe\u5907\u8d26\u53f7"}, index=2)
    @ColumnWidth(value=18)
    private String hostAccount;
    @ExcelProperty(value={"\u8bbe\u5907\u5bc6\u7801"}, index=3)
    @ColumnWidth(value=18)
    private String hostPasswd;
    @ExcelProperty(value={"\u5907\u6ce8"}, index=4)
    @ColumnWidth(value=18)
    private String hostRemark;
    @ExcelProperty(value={"\u6240\u5c5e\u8d26\u53f7"}, index=5)
    @ColumnWidth(value=18)
    private String account;
    @ExcelProperty(value={"\u6807\u7b7e"}, index=6)
    @ColumnWidth(value=18)
    private String groupId;
    @ExcelProperty(value={"\u6dfb\u52a0\u65f6\u95f4"}, index=7)
    @ColumnWidth(value=20)
    private String createTime;

    public String getHostname() {
        return this.hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getHostMark() {
        return this.hostMark;
    }

    public void setHostMark(String hostMark) {
        this.hostMark = hostMark;
    }

    public String getHostAccount() {
        return this.hostAccount;
    }

    public void setHostAccount(String hostAccount) {
        this.hostAccount = hostAccount;
    }

    public String getHostPasswd() {
        return this.hostPasswd;
    }

    public void setHostPasswd(String hostPasswd) {
        this.hostPasswd = hostPasswd;
    }

    public String getHostRemark() {
        return this.hostRemark;
    }

    public void setHostRemark(String hostRemark) {
        this.hostRemark = hostRemark;
    }

    public String getAccount() {
        return this.account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getGroupId() {
        return this.groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}

