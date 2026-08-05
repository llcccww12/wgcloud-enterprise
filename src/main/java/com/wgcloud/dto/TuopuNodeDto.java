/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.dto;

import java.io.Serializable;

public class TuopuNodeDto
implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private int x;
    private int y;
    private String label;
    private String cpuPer;
    private String memPer;
    private String rxbyt;
    private String txbyt;
    private String fiveLoad;
    private String netConnections;
    private String procs;
    private String img;
    private Integer size;
    private String active;
    private String agentVer;
    private String state;

    public int getX() {
        return this.x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return this.y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public String getLabel() {
        return this.label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getImg() {
        return this.img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public Integer getSize() {
        return this.size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCpuPer() {
        return this.cpuPer;
    }

    public void setCpuPer(String cpuPer) {
        this.cpuPer = cpuPer;
    }

    public String getMemPer() {
        return this.memPer;
    }

    public void setMemPer(String memPer) {
        this.memPer = memPer;
    }

    public String getRxbyt() {
        return this.rxbyt;
    }

    public void setRxbyt(String rxbyt) {
        this.rxbyt = rxbyt;
    }

    public String getTxbyt() {
        return this.txbyt;
    }

    public void setTxbyt(String txbyt) {
        this.txbyt = txbyt;
    }

    public String getFiveLoad() {
        return this.fiveLoad;
    }

    public void setFiveLoad(String fiveLoad) {
        this.fiveLoad = fiveLoad;
    }

    public String getNetConnections() {
        return this.netConnections;
    }

    public void setNetConnections(String netConnections) {
        this.netConnections = netConnections;
    }

    public String getProcs() {
        return this.procs;
    }

    public void setProcs(String procs) {
        this.procs = procs;
    }

    public String getActive() {
        return this.active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getAgentVer() {
        return this.agentVer;
    }

    public void setAgentVer(String agentVer) {
        this.agentVer = agentVer;
    }

    public String getState() {
        return this.state;
    }

    public void setState(String state) {
        this.state = state;
    }
}

