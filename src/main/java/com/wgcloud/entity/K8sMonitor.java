/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.entity;

import com.wgcloud.entity.BaseEntity;
import java.util.Date;

public class K8sMonitor
extends BaseEntity {
    private static final long serialVersionUID = 1L;
    private String dataType;
    private String k8sName;
    private String dataJson;
    private Date createTime;

    public String getDataType() {
        return this.dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getK8sName() {
        return this.k8sName;
    }

    public void setK8sName(String k8sName) {
        this.k8sName = k8sName;
    }

    public String getDataJson() {
        return this.dataJson;
    }

    public void setDataJson(String dataJson) {
        this.dataJson = dataJson;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}

