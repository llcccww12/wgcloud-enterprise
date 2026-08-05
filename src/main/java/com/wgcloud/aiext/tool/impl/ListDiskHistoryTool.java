package com.wgcloud.aiext.tool.impl;

import com.wgcloud.aiext.tool.AiTool;
import com.wgcloud.entity.HostDiskPer;
import com.wgcloud.service.HostDiskPerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 工具：list_disk_history —— 列出主机磁盘汇总使用率的历史数据点（来自 HOST_DISK_PER 表）。
 * 用于看趋势：某台主机磁盘最近 7 天怎么变化的。
 */
@Component
public class ListDiskHistoryTool implements AiTool {

    private static final SimpleDateFormat TS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired private HostDiskPerService hostDiskPerService;

    @Override public String getName() { return "list_disk_history"; }

    @Override public String getDescription() {
        return "查询主机磁盘汇总使用率的历史数据（按时间）。适合 '某主机最近一周磁盘使用变化' '磁盘空间增长趋势'。" +
               "和 list_disk_usage 不同：本工具看的是历史时序，list_disk_usage 看的是当前各分区明细。";
    }

    @Override public String getParametersJsonSchema() {
        return "{"
            + "\"type\":\"object\","
            + "\"properties\":{"
            +   "\"hostname\":{\"type\":\"string\",\"description\":\"主机名精确匹配，留空查全部\"},"
            +   "\"hours\":{\"type\":\"integer\",\"description\":\"最近多少小时（默认 168=7天，最大 720=30天）\"},"
            +   "\"limit\":{\"type\":\"integer\",\"description\":\"返回行数，默认 500，最大 3000\"}"
            + "},"
            + "\"required\":[]"
            + "}";
    }

    @Override public String execute(Map<String, Object> args) throws Exception {
        String hostname = ListHostsTool.strArg(args, "hostname", "");
        int hours = ListHostsTool.intArg(args, "hours", 168, 1, 720);
        int limit = ListHostsTool.intArg(args, "limit", 500, 1, 3000);

        Calendar c = Calendar.getInstance();
        String end = TS.format(c.getTime());
        c.add(Calendar.HOUR_OF_DAY, -hours);
        String start = TS.format(c.getTime());

        Map<String, Object> params = new HashMap<>();
        if (!hostname.isEmpty()) params.put("hostname", hostname);
        params.put("startTime", start);
        params.put("endTime", end);

        List<HostDiskPer> list = hostDiskPerService.selectAllByParams(params);
        if (list == null || list.isEmpty()) {
            return "时间窗（最近 " + hours + " 小时）内未找到磁盘历史数据。";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("时间窗：最近 ").append(hours).append(" 小时\n");
        sb.append("共 ").append(list.size()).append(" 条");
        if (list.size() > limit) sb.append("，仅展示前 ").append(limit);
        sb.append("\n\n| 时间 | 主机 | 磁盘汇总使用率% |\n");
        sb.append("|------|------|-----------------|\n");
        int cnt = 0;
        for (HostDiskPer h : list) {
            if (cnt++ >= limit) break;
            sb.append("| ").append(h.getCreateTime() == null ? ListHostsTool.safe(h.getDateStr()) : TS.format(h.getCreateTime()))
              .append(" | ").append(ListHostsTool.safe(h.getHostname()))
              .append(" | ").append(h.getDiskSumPer() == null ? "" : String.format("%.2f", h.getDiskSumPer()))
              .append(" |\n");
        }
        return ListHostsTool.cap(sb);
    }
}
