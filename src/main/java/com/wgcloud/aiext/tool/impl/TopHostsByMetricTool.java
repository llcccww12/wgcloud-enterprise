package com.wgcloud.aiext.tool.impl;

import com.wgcloud.aiext.tool.AiTool;
import com.wgcloud.entity.SystemInfo;
import com.wgcloud.service.SystemInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 工具 3：top_hosts_by_metric —— 按指定指标 Top N。
 * metric 白名单防 SQL 注入（orderBy 走 ${}）。
 */
@Component
public class TopHostsByMetricTool implements AiTool {

    @Autowired private SystemInfoService systemInfoService;

    /** metric → DB 列名 白名单。绝不允许 AI 直接传列名。 */
    private static final Map<String, String> METRIC_COLUMN = new HashMap<>();
    static {
        METRIC_COLUMN.put("cpu",  "CPU_PER");
        METRIC_COLUMN.put("mem",  "MEM_PER");
        METRIC_COLUMN.put("disk", "DISK_PER");
        METRIC_COLUMN.put("load", "FIVE_LOAD");
    }

    @Override public String getName() { return "top_hosts_by_metric"; }

    @Override public String getDescription() {
        return "查询某项指标排名 Top N 的主机（按降序）。适合 '哪几台主机内存最高' '最忙的5台机器' 这类问法。";
    }

    @Override public String getParametersJsonSchema() {
        return "{"
            + "\"type\":\"object\","
            + "\"properties\":{"
            +   "\"metric\":{\"type\":\"string\",\"enum\":[\"cpu\",\"mem\",\"disk\",\"load\"],\"description\":\"指标：cpu=CPU使用率, mem=内存使用率, disk=磁盘使用率, load=5分钟系统负载\"},"
            +   "\"n\":{\"type\":\"integer\",\"description\":\"返回 Top N，默认 10，最大 100\"},"
            +   "\"onlineOnly\":{\"type\":\"boolean\",\"description\":\"是否只看在线主机，默认 false（含离线）\"}"
            + "},"
            + "\"required\":[\"metric\"]"
            + "}";
    }

    @Override public String execute(Map<String, Object> args) throws Exception {
        String metric = ListHostsTool.strArg(args, "metric", "").toLowerCase();
        String col = METRIC_COLUMN.get(metric);
        if (col == null) return "metric 必须是 cpu/mem/disk/load 之一";
        int n = ListHostsTool.intArg(args, "n", 10, 1, 100);
        // onlineOnly 默认改为 false：用户问"哪些机器内存高"时通常不希望排除离线
        boolean onlineOnly = "true".equalsIgnoreCase(ListHostsTool.strArg(args, "onlineOnly", "false"));

        Map<String, Object> params = new HashMap<>();
        if (onlineOnly) params.put("state", "1");
        params.put("orderBy", col);    // 已白名单
        params.put("orderType", "DESC");

        List<SystemInfo> list = systemInfoService.selectAllByParams(params);
        if (list == null || list.isEmpty()) return "没有匹配的主机数据。";

        StringBuilder sb = new StringBuilder();
        sb.append("按 ").append(metric).append(" 降序，Top ").append(n)
          .append(onlineOnly ? "（仅在线主机）" : "（全部）").append("：\n\n");
        sb.append("| # | 主机名 | 状态 | CPU% | 内存% | 磁盘% | 5min负载 |\n");
        sb.append("|---|--------|------|------|-------|-------|----------|\n");
        int cnt = 0;
        for (SystemInfo si : list) {
            if (cnt >= n) break;
            cnt++;
            sb.append("| ").append(cnt)
              .append(" | ").append(ListHostsTool.safe(si.getHostname()))
              .append(" | ").append("1".equals(si.getState()) ? "在线" : "离线")
              .append(" | ").append(ListHostsTool.pct(si.getCpuPer()))
              .append(" | ").append(ListHostsTool.pct(si.getMemPer()))
              .append(" | ").append(ListHostsTool.pct(si.getDiskPer()))
              .append(" | ").append(si.getFiveLoad())
              .append(" |\n");
        }
        return sb.toString();
    }
}
