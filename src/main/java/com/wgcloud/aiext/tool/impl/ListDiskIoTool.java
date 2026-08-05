package com.wgcloud.aiext.tool.impl;

import com.wgcloud.aiext.tool.AiTool;
import com.wgcloud.entity.DiskIoState;
import com.wgcloud.service.DiskIoStateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 工具：list_disk_io —— 列出主机磁盘 IO 读写速率（来自 DISK_IO_STATE 表）。
 * 对应原 LLM 文件"全量主机磁盘IO速率信息"。
 */
@Component
public class ListDiskIoTool implements AiTool {

    private static final SimpleDateFormat TS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired private DiskIoStateService diskIoStateService;

    @Override public String getName() { return "list_disk_io"; }

    @Override public String getDescription() {
        return "列出主机磁盘 IO 读写速率（最近 N 小时采样数据）。每行一个采样点，含主机、读速率、写速率、读次数、写次数。" +
               "适合 '哪台主机磁盘 IO 高' '某主机最近 IO 情况'。";
    }

    @Override public String getParametersJsonSchema() {
        return "{"
            + "\"type\":\"object\","
            + "\"properties\":{"
            +   "\"hostname\":{\"type\":\"string\",\"description\":\"主机名精确匹配，留空查全部\"},"
            +   "\"hours\":{\"type\":\"integer\",\"description\":\"最近多少小时（默认 1，最大 168=7天）\"},"
            +   "\"limit\":{\"type\":\"integer\",\"description\":\"返回行数，默认 500，最大 3000\"}"
            + "},"
            + "\"required\":[]"
            + "}";
    }

    @Override public String execute(Map<String, Object> args) throws Exception {
        String hostname = ListHostsTool.strArg(args, "hostname", "");
        int hours = ListHostsTool.intArg(args, "hours", 1, 1, 168);
        int limit = ListHostsTool.intArg(args, "limit", 500, 1, 3000);

        Calendar c = Calendar.getInstance();
        String end = TS.format(c.getTime());
        c.add(Calendar.HOUR_OF_DAY, -hours);
        String start = TS.format(c.getTime());

        Map<String, Object> params = new HashMap<>();
        if (!hostname.isEmpty()) params.put("hostname", hostname);
        params.put("startTime", start);
        params.put("endTime", end);

        List<DiskIoState> list = diskIoStateService.selectAllByParams(params);
        if (list == null || list.isEmpty()) {
            return "时间窗（最近 " + hours + " 小时）内未找到磁盘 IO 数据。";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("时间窗：最近 ").append(hours).append(" 小时\n");
        sb.append("共 ").append(list.size()).append(" 条");
        if (list.size() > limit) sb.append("，仅展示前 ").append(limit);
        sb.append("\n\n| 时间 | 主机 | 读速率(MB/s) | 写速率(MB/s) | 读次数/s | 写次数/s |\n");
        sb.append("|------|------|--------------|--------------|----------|----------|\n");
        int cnt = 0;
        for (DiskIoState d : list) {
            if (cnt++ >= limit) break;
            sb.append("| ").append(d.getCreateTime() == null ? "" : TS.format(d.getCreateTime()))
              .append(" | ").append(ListHostsTool.safe(d.getHostname()))
              .append(" | ").append(d.getReadIoAvgDouble() == null ? "" : String.format("%.2f", d.getReadIoAvgDouble()))
              .append(" | ").append(d.getWriteIoAvgDouble() == null ? "" : String.format("%.2f", d.getWriteIoAvgDouble()))
              .append(" | ").append(d.getReadIoCountAvgDouble() == null ? "" : String.format("%.0f", d.getReadIoCountAvgDouble()))
              .append(" | ").append(d.getWriteIoCountAvgDouble() == null ? "" : String.format("%.0f", d.getWriteIoCountAvgDouble()))
              .append(" |\n");
        }
        return ListHostsTool.cap(sb);
    }
}
