/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.entity;

import com.wgcloud.entity.BaseEntity;
import java.util.Date;
import org.apache.commons.lang3.StringUtils;

public class MailSet
extends BaseEntity {
    private static final long serialVersionUID = -8284741180883299533L;
    private String sendMail;
    private String fromMailName;
    private String fromPwd;
    private String smtpHost;
    private String smtpPort;
    private String smtpSSL;
    private String toMail;
    private String cpuPer;
    private String active;
    private String javaxMail;
    private Date createTime;

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getSendMail() {
        return this.sendMail;
    }

    public void setSendMail(String sendMail) {
        this.sendMail = sendMail;
    }

    public String getFromMailName() {
        return this.fromMailName;
    }

    public void setFromMailName(String fromMailName) {
        this.fromMailName = fromMailName;
    }

    public String getFromPwd() {
        return this.fromPwd;
    }

    public void setFromPwd(String fromPwd) {
        this.fromPwd = fromPwd;
    }

    public String getSmtpHost() {
        return this.smtpHost;
    }

    public void setSmtpHost(String smtpHost) {
        this.smtpHost = smtpHost;
    }

    public String getSmtpPort() {
        if (StringUtils.isEmpty((CharSequence)this.smtpPort)) {
            this.smtpPort = "465";
        }
        return this.smtpPort;
    }

    public void setSmtpPort(String smtpPort) {
        this.smtpPort = smtpPort;
    }

    public String getSmtpSSL() {
        if (StringUtils.isEmpty((CharSequence)this.smtpSSL)) {
            this.smtpSSL = "1";
        }
        return this.smtpSSL;
    }

    public void setSmtpSSL(String smtpSSL) {
        this.smtpSSL = smtpSSL;
    }

    public String getToMail() {
        return this.toMail;
    }

    public void setToMail(String toMail) {
        this.toMail = toMail;
    }

    public String getCpuPer() {
        return this.cpuPer;
    }

    public void setCpuPer(String cpuPer) {
        this.cpuPer = cpuPer;
    }

    public String getActive() {
        return this.active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getJavaxMail() {
        return this.javaxMail;
    }

    public void setJavaxMail(String javaxMail) {
        this.javaxMail = javaxMail;
    }
}

