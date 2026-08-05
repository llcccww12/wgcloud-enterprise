/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.dto;

import java.io.Serializable;

public class MenuTreeNodeDto
implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String pId;
    private String name;
    private boolean checked;
    private boolean open;

    public MenuTreeNodeDto() {
    }

    public MenuTreeNodeDto(String id, String pId, String name, boolean checked, boolean open) {
        this.id = id;
        this.pId = pId;
        this.name = name;
        this.checked = checked;
        this.open = open;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getpId() {
        return this.pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isChecked() {
        return this.checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public boolean isOpen() {
        return this.open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }
}

