/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.dto;

import com.wgcloud.entity.BaseEntity;

public class HeathMonitorResDto
extends BaseEntity {
    private static final long serialVersionUID = 1L;
    private Integer resTimes;
    private Integer heathStatus;
    private String errorMsg;
    private String bodyInfo;
    private String responseBodySize;

    public Integer getResTimes() {
        return this.resTimes;
    }

    public void setResTimes(Integer resTimes) {
        this.resTimes = resTimes;
    }

    public Integer getHeathStatus() {
        return this.heathStatus;
    }

    public void setHeathStatus(Integer heathStatus) {
        this.heathStatus = heathStatus;
    }

    public String getErrorMsg() {
        return this.errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getBodyInfo() {
        return this.bodyInfo;
    }

    public void setBodyInfo(String bodyInfo) {
        this.bodyInfo = bodyInfo;
    }

    public String getResponseBodySize() {
        return this.responseBodySize;
    }

    public void setResponseBodySize(String responseBodySize) {
        this.responseBodySize = responseBodySize;
    }
}

