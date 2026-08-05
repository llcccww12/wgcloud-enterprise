/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.dto;

import com.wgcloud.entity.BaseEntity;

public class ShellCheckDto
extends BaseEntity {
    private static final long serialVersionUID = 1L;
    private String cmdSplitChar;

    public String getCmdSplitChar() {
        return this.cmdSplitChar;
    }

    public void setCmdSplitChar(String cmdSplitChar) {
        this.cmdSplitChar = cmdSplitChar;
    }
}

