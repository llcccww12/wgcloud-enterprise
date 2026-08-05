/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.entity;

import com.wgcloud.entity.BaseEntity;
import com.wgcloud.util.DateUtil;
import java.util.Date;
import org.apache.commons.lang3.StringUtils;

public class CustomState
extends BaseEntity {
    private static final long serialVersionUID = 1L;
    private String customInfoId;
    private String customValue;
    private Double customValueDouble;
    private String dateStr;
    private Date createTime;

    public String getCustomInfoId() {
        return this.customInfoId;
    }

    public void setCustomInfoId(String customInfoId) {
        this.customInfoId = customInfoId;
    }

    public String getCustomValue() {
        return this.customValue;
    }

    public void setCustomValue(String customValue) {
        this.customValue = customValue;
    }

    public String getDateStr() {
        String s = DateUtil.getDateTimeString(this.getCreateTime());
        if (!StringUtils.isEmpty((CharSequence)s) && s.length() > 16) {
            return s.substring(5);
        }
        return this.dateStr;
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Double getCustomValueDouble() {
        if (!StringUtils.isEmpty((CharSequence)this.customValue)) {
            try {
                this.customValueDouble = Double.valueOf(this.customValue);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            this.customValueDouble = 0.0;
        }
        return this.customValueDouble;
    }

    public void setCustomValueDouble(Double customValueDouble) {
        this.customValueDouble = customValueDouble;
    }
}

