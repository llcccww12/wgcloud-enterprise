/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.dto.llm;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import java.io.Serializable;

public class HostNetStateExcelDto
implements Serializable {
    private static final long serialVersionUID = 1L;
    @ExcelProperty(value={"\u4e3b\u673aIP"}, index=0)
    @ColumnWidth(value=18)
    private String hostName;
    @ExcelProperty(value={"\u91c7\u96c6\u4e0a\u62a5\u65f6\u95f4"}, index=1)
    @ColumnWidth(value=20)
    private String datetime;
    @ExcelProperty(value={"\u63a5\u6536\u6570\u636e\u5305/\u79d2"}, index=2)
    @ColumnWidth(value=20)
    private String rxpck;
    @ExcelProperty(value={"\u53d1\u9001\u6570\u636e\u5305/\u79d2"}, index=3)
    @ColumnWidth(value=20)
    private String txpck;
    @ExcelProperty(value={"\u4e0b\u884c\u901f\u7387KB/s"}, index=4)
    @ColumnWidth(value=18)
    private String rxbyt;
    @ExcelProperty(value={"\u4e0a\u884c\u901f\u7387KB/s"}, index=5)
    @ColumnWidth(value=18)
    private String txbyt;
    @ExcelProperty(value={"\u4e22\u5f03\u4f20\u5165\u6570\u636e\u5305/\u79d2"}, index=6)
    @ColumnWidth(value=24)
    private String dropin;
    @ExcelProperty(value={"\u4e22\u5f03\u4f20\u51fa\u6570\u636e\u5305/\u79d2"}, index=7)
    @ColumnWidth(value=24)
    private String dropout;
    @ExcelProperty(value={"\u4e3b\u673a\u8fde\u63a5\u6570\u91cf"}, index=8)
    @ColumnWidth(value=20)
    private String netConnections;

    public String getHostName() {
        return this.hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getDatetime() {
        return this.datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getRxpck() {
        return this.rxpck;
    }

    public void setRxpck(String rxpck) {
        this.rxpck = rxpck;
    }

    public String getTxpck() {
        return this.txpck;
    }

    public void setTxpck(String txpck) {
        this.txpck = txpck;
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

    public String getDropin() {
        return this.dropin;
    }

    public void setDropin(String dropin) {
        this.dropin = dropin;
    }

    public String getDropout() {
        return this.dropout;
    }

    public void setDropout(String dropout) {
        this.dropout = dropout;
    }

    public String getNetConnections() {
        return this.netConnections;
    }

    public void setNetConnections(String netConnections) {
        this.netConnections = netConnections;
    }
}

