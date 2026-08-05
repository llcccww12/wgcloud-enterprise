/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.entity;

import com.wgcloud.entity.BaseEntity;
import java.util.Date;

public class ShellNoteInfo
extends BaseEntity {
    private static final long serialVersionUID = 1L;
    private String shellTitle;
    private String shellContent;
    private String account;
    private Date createTime;

    public String getShellTitle() {
        return this.shellTitle;
    }

    public void setShellTitle(String shellTitle) {
        this.shellTitle = shellTitle;
    }

    public String getShellContent() {
        return this.shellContent;
    }

    public void setShellContent(String shellContent) {
        this.shellContent = shellContent;
    }

    public String getAccount() {
        return this.account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}

