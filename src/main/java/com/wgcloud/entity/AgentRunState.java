/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.entity;

import com.wgcloud.entity.BaseEntity;
import java.util.Date;

public class AgentRunState
extends BaseEntity {
    private static final long serialVersionUID = 1L;
    private String hostname;
    private Integer onlineTime;
    private String onlineTimeStr;
    private Integer downTime;
    private String downTimeStr;
    private Date lastTime;
    private Date createTime;
    private String account;
    private String state;
    private String groupId;
    private String platForm;
    private String platformVersion;
    private String hostnameExt;
    private String image;

    public String getHostname() {
        return this.hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public Integer getOnlineTime() {
        return this.onlineTime;
    }

    public void setOnlineTime(Integer onlineTime) {
        this.onlineTime = onlineTime;
    }

    public Integer getDownTime() {
        return this.downTime;
    }

    public void setDownTime(Integer downTime) {
        this.downTime = downTime;
    }

    public Date getLastTime() {
        return this.lastTime;
    }

    public void setLastTime(Date lastTime) {
        this.lastTime = lastTime;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getAccount() {
        return this.account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getState() {
        return this.state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getGroupId() {
        return this.groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getPlatForm() {
        return this.platForm;
    }

    public void setPlatForm(String platForm) {
        this.platForm = platForm;
    }

    public String getPlatformVersion() {
        return this.platformVersion;
    }

    public void setPlatformVersion(String platformVersion) {
        this.platformVersion = platformVersion;
    }

    public String getHostnameExt() {
        return this.hostnameExt;
    }

    public void setHostnameExt(String hostnameExt) {
        this.hostnameExt = hostnameExt;
    }

    public String getImage() {
        return this.image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getOnlineTimeStr() {
        return this.onlineTimeStr;
    }

    public void setOnlineTimeStr(String onlineTimeStr) {
        this.onlineTimeStr = onlineTimeStr;
    }

    public String getDownTimeStr() {
        return this.downTimeStr;
    }

    public void setDownTimeStr(String downTimeStr) {
        this.downTimeStr = downTimeStr;
    }
}

