/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.dto;

import com.wgcloud.entity.BaseEntity;

public class NetworkInfoDto
extends BaseEntity {
    private static final long serialVersionUID = 1L;
    private String name;
    private String bytesSent;
    private String bytesRecv;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBytesSent() {
        return this.bytesSent;
    }

    public void setBytesSent(String bytesSent) {
        this.bytesSent = bytesSent;
    }

    public String getBytesRecv() {
        return this.bytesRecv;
    }

    public void setBytesRecv(String bytesRecv) {
        this.bytesRecv = bytesRecv;
    }
}

