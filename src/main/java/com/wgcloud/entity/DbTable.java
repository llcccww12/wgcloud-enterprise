/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.entity;

import com.wgcloud.entity.BaseEntity;
import com.wgcloud.util.DateUtil;
import java.util.Date;
import org.apache.commons.lang3.StringUtils;

public class DbTable
extends BaseEntity {
    private static final long serialVersionUID = 1L;
    private String dbInfoId;
    private String tableName;
    private String whereVal;
    private String remark;
    private String tableCount;
    private String resultExp;
    private String dateStr;
    private String active;
    private String state;
    private String warnLevel;
    private Date createTime;
    private String remarkExt;
    private Integer resTimes;
    private String resTimesSecond;
    private String account;
    private String image;
    private String testErrorMsg;
    private String sqlTableHtml;

    public String getTableName() {
        return this.tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getWhereVal() {
        return this.whereVal;
    }

    public void setWhereVal(String whereVal) {
        this.whereVal = whereVal;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getTableCount() {
        return this.tableCount;
    }

    public void setTableCount(String tableCount) {
        this.tableCount = tableCount;
    }

    public String getDateStr() {
        String s = DateUtil.getDateTimeString(this.getCreateTime());
        if (!StringUtils.isEmpty((CharSequence)s) && s.length() > 16) {
            return s.substring(5);
        }
        return this.dateStr;
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getDbInfoId() {
        return this.dbInfoId;
    }

    public void setDbInfoId(String dbInfoId) {
        this.dbInfoId = dbInfoId;
    }

    public String getActive() {
        return this.active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getResultExp() {
        return this.resultExp;
    }

    public void setResultExp(String resultExp) {
        this.resultExp = resultExp;
    }

    public String getAccount() {
        return this.account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getImage() {
        return this.image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getState() {
        return this.state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getWarnLevel() {
        return this.warnLevel;
    }

    public void setWarnLevel(String warnLevel) {
        this.warnLevel = warnLevel;
    }

    public String getRemarkExt() {
        return this.remarkExt;
    }

    public void setRemarkExt(String remarkExt) {
        this.remarkExt = remarkExt;
    }

    public Integer getResTimes() {
        return this.resTimes;
    }

    public void setResTimes(Integer resTimes) {
        this.resTimes = resTimes;
    }

    public String getResTimesSecond() {
        return this.resTimesSecond;
    }

    public void setResTimesSecond(String resTimesSecond) {
        this.resTimesSecond = resTimesSecond;
    }

    public String getTestErrorMsg() {
        return this.testErrorMsg;
    }

    public void setTestErrorMsg(String testErrorMsg) {
        this.testErrorMsg = testErrorMsg;
    }

    public String getSqlTableHtml() {
        return this.sqlTableHtml;
    }

    public void setSqlTableHtml(String sqlTableHtml) {
        this.sqlTableHtml = sqlTableHtml;
    }
}

