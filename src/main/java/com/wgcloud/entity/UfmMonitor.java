/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.entity;

import com.wgcloud.entity.BaseEntity;
import java.util.Date;

public class UfmMonitor
extends BaseEntity {
    private static final long serialVersionUID = 1L;
    private String guid;
    private String systemName;
    private String deviceType;
    private String deviceState;
    private String severity;
    private String mbOut;
    private String mbIn;
    private String pckOut;
    private String pckIn;
    private String symbolErrors;
    private String linkRecovers;
    private String linkDowned;
    private String rcvErrors;
    private String xmtDiscards;
    private String mbOutRate;
    private String mbInRate;
    private String pckOutRate;
    private String pckInRate;
    private String alarmCount;
    private Date createTime;

    public String getGuid() {
        return this.guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getSystemName() {
        return this.systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public String getDeviceType() {
        return this.deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getDeviceState() {
        return this.deviceState;
    }

    public void setDeviceState(String deviceState) {
        this.deviceState = deviceState;
    }

    public String getSeverity() {
        return this.severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getMbOut() {
        return this.mbOut;
    }

    public void setMbOut(String mbOut) {
        this.mbOut = mbOut;
    }

    public String getMbIn() {
        return this.mbIn;
    }

    public void setMbIn(String mbIn) {
        this.mbIn = mbIn;
    }

    public String getPckOut() {
        return this.pckOut;
    }

    public void setPckOut(String pckOut) {
        this.pckOut = pckOut;
    }

    public String getPckIn() {
        return this.pckIn;
    }

    public void setPckIn(String pckIn) {
        this.pckIn = pckIn;
    }

    public String getSymbolErrors() {
        return this.symbolErrors;
    }

    public void setSymbolErrors(String symbolErrors) {
        this.symbolErrors = symbolErrors;
    }

    public String getLinkRecovers() {
        return this.linkRecovers;
    }

    public void setLinkRecovers(String linkRecovers) {
        this.linkRecovers = linkRecovers;
    }

    public String getLinkDowned() {
        return this.linkDowned;
    }

    public void setLinkDowned(String linkDowned) {
        this.linkDowned = linkDowned;
    }

    public String getRcvErrors() {
        return this.rcvErrors;
    }

    public void setRcvErrors(String rcvErrors) {
        this.rcvErrors = rcvErrors;
    }

    public String getXmtDiscards() {
        return this.xmtDiscards;
    }

    public void setXmtDiscards(String xmtDiscards) {
        this.xmtDiscards = xmtDiscards;
    }

    public String getMbOutRate() {
        return this.mbOutRate;
    }

    public void setMbOutRate(String mbOutRate) {
        this.mbOutRate = mbOutRate;
    }

    public String getMbInRate() {
        return this.mbInRate;
    }

    public void setMbInRate(String mbInRate) {
        this.mbInRate = mbInRate;
    }

    public String getPckOutRate() {
        return this.pckOutRate;
    }

    public void setPckOutRate(String pckOutRate) {
        this.pckOutRate = pckOutRate;
    }

    public String getPckInRate() {
        return this.pckInRate;
    }

    public void setPckInRate(String pckInRate) {
        this.pckInRate = pckInRate;
    }

    public String getAlarmCount() {
        return this.alarmCount;
    }

    public void setAlarmCount(String alarmCount) {
        this.alarmCount = alarmCount;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
