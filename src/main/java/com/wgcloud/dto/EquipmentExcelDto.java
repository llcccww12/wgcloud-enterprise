/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import java.io.Serializable;

public class EquipmentExcelDto
implements Serializable {
    private static final long serialVersionUID = 1L;
    @ExcelProperty(value={"\u8d44\u4ea7\u540d\u79f0"}, index=0)
    @ColumnWidth(value=18)
    private String name;
    @ExcelProperty(value={"\u8d44\u4ea7\u7f16\u7801"}, index=1)
    @ColumnWidth(value=18)
    private String code;
    @ExcelProperty(value={"\u89c4\u683c\u578b\u53f7"}, index=2)
    @ColumnWidth(value=18)
    private String xinghao;
    @ExcelProperty(value={"\u4f7f\u7528\u90e8\u95e8"}, index=3)
    @ColumnWidth(value=18)
    private String dept;
    @ExcelProperty(value={"\u4f7f\u7528\u4eba\u5458"}, index=4)
    @ColumnWidth(value=18)
    private String person;
    @ExcelProperty(value={"\u91c7\u8d2d\u65e5\u671f"}, index=5)
    @ColumnWidth(value=18)
    private String caigouDate;
    @ExcelProperty(value={"\u4f9b\u5e94\u5546"}, index=6)
    @ColumnWidth(value=18)
    private String gongyingshang;
    @ExcelProperty(value={"\u91c7\u8d2d\u4ef7\u683c(\u5143)"}, index=7)
    @ColumnWidth(value=18)
    private String price;
    @ExcelProperty(value={"\u7ef4\u4fdd\u5230\u671f\u65f6\u95f4"}, index=8)
    @ColumnWidth(value=18)
    private String weibaoDate;
    @ExcelProperty(value={"\u5907\u6ce8"}, index=9)
    @ColumnWidth(value=18)
    private String remark;
    @ExcelProperty(value={"\u6240\u5c5e\u8d26\u53f7"}, index=10)
    @ColumnWidth(value=18)
    private String account;
    @ExcelProperty(value={"\u6807\u7b7e"}, index=11)
    @ColumnWidth(value=18)
    private String groupId;
    @ExcelProperty(value={"\u6dfb\u52a0\u65f6\u95f4"}, index=12)
    @ColumnWidth(value=20)
    private String createTime;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getXinghao() {
        return this.xinghao;
    }

    public void setXinghao(String xinghao) {
        this.xinghao = xinghao;
    }

    public String getDept() {
        return this.dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public String getPerson() {
        return this.person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public String getCaigouDate() {
        return this.caigouDate;
    }

    public void setCaigouDate(String caigouDate) {
        this.caigouDate = caigouDate;
    }

    public String getGongyingshang() {
        return this.gongyingshang;
    }

    public void setGongyingshang(String gongyingshang) {
        this.gongyingshang = gongyingshang;
    }

    public String getPrice() {
        return this.price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getWeibaoDate() {
        return this.weibaoDate;
    }

    public void setWeibaoDate(String weibaoDate) {
        this.weibaoDate = weibaoDate;
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

