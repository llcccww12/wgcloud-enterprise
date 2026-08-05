/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.dto;

import com.wgcloud.entity.BaseEntity;

public class ChartDapingNewInfo
extends BaseEntity {
    private static final long serialVersionUID = 1L;
    private String name;
    private Integer value;
    private String state;
    private String createTime;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getValue() {
        return this.value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getState() {
        return this.state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}

