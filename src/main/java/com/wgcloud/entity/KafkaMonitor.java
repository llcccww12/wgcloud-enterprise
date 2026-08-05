/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.entity;

import com.wgcloud.entity.BaseEntity;
import java.util.Date;

public class KafkaMonitor
extends BaseEntity {
    private static final long serialVersionUID = 1L;
    private String groupId;
    private String topicName;
    private String partition;
    private String offset;
    private String lagCount;
    private String kafkaName;
    private String state;
    private Date createTime;
    private String testErrorMsg;

    public String getGroupId() {
        return this.groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getTopicName() {
        return this.topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public String getPartition() {
        return this.partition;
    }

    public void setPartition(String partition) {
        this.partition = partition;
    }

    public String getOffset() {
        return this.offset;
    }

    public void setOffset(String offset) {
        this.offset = offset;
    }

    public String getLagCount() {
        return this.lagCount;
    }

    public void setLagCount(String lagCount) {
        this.lagCount = lagCount;
    }

    public String getKafkaName() {
        return this.kafkaName;
    }

    public void setKafkaName(String kafkaName) {
        this.kafkaName = kafkaName;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getState() {
        return this.state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTestErrorMsg() {
        return this.testErrorMsg;
    }

    public void setTestErrorMsg(String testErrorMsg) {
        this.testErrorMsg = testErrorMsg;
    }
}

