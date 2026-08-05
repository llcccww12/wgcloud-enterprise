/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.entity;

import com.wgcloud.entity.BaseEntity;
import java.util.Date;

public class SnmpDeepState
extends BaseEntity {
    private static final long serialVersionUID = 1L;
    private String snmpDeepInfoId;
    private String oidValue;
    private String oidType;
    private String oidName;
    private Integer oidOrderNum;
    private Date createTime;
    private String oidResult;

    public String getSnmpDeepInfoId() {
        return this.snmpDeepInfoId;
    }

    public void setSnmpDeepInfoId(String snmpDeepInfoId) {
        this.snmpDeepInfoId = snmpDeepInfoId;
    }

    public String getOidValue() {
        return this.oidValue;
    }

    public void setOidValue(String oidValue) {
        this.oidValue = oidValue;
    }

    public String getOidType() {
        return this.oidType;
    }

    public void setOidType(String oidType) {
        this.oidType = oidType;
    }

    public String getOidName() {
        return this.oidName;
    }

    public void setOidName(String oidName) {
        this.oidName = oidName;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getOidOrderNum() {
        return this.oidOrderNum;
    }

    public void setOidOrderNum(Integer oidOrderNum) {
        this.oidOrderNum = oidOrderNum;
    }

    public String getOidResult() {
        return this.oidResult;
    }

    public void setOidResult(String oidResult) {
        this.oidResult = oidResult;
    }
}

