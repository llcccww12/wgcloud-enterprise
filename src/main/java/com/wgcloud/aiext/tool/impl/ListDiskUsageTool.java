package com.wgcloud.aiext.tool.impl;

import com.wgcloud.aiext.tool.AiTool;
import com.wgcloud.entity.DiskState;
import com.wgcloud.service.DiskStateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 工具：list_disk_usage —— 列出主机各分区磁盘使用情况（来自 DISK_STATE 实时表）。
 * 对应原 LLM 文件"全量主机磁盘使用率数据"。
 */
@Component
public class ListDiskUsageTool implements AiTool {

    private static final SimpleDateFormat TS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired private DiskStateService diskStateService;

    @Override public String getName() { return "list_disk_usage"; }

    @Override public String getDescription() {
        return "列出各主机的磁盘分区使用率明细。每行一个分区，含主机IP、分区路径、总大小、已使用、使用率%。" +
               "适合 '哪些磁盘快满了' '某主机磁盘使用情况'。可按主机精确过滤、按使用率阈值过滤。";
    }

    @Override public String getParametersJsonSchema() {
        return "{"
            + "\"type\":\"object\","
            + "\"properties\":{"
            +   "\"hostname\":{\"type\":\"string\",\"description\":\"主机名精确匹配，留空查全部\"},"
            +   "\"minUsePer\":{\"type\":\"number\",\"description\":\"使用率%下限，例如 80 表示只看 >=80% 的分区\"},"
            +   "\"sortByUsage\":{\"type\":\"boolean\",\"description\":\"是否按使用率降序排序，默认 true\"},"
            +   "\"limit\":{\"type\":\"integer\",\"description\":\"最多返回多少行，默认 500，最大 3000\"}"
            + "},"
            + "\"required\":[]"
            + "}";
    }

    @Override public String execute(Map<String, Object> args) throws Exception {
        String hostname  = ListHostsTool.strArg(args, "hostname", "");
        Double minUsePer = numArg(args, "minUsePer");
        boolean sortByUsage = !"false".equalsIgnoreCase(ListHostsTool.strArg(args, "sortByUsage", "true"));
        int limit = ListHostsTool.intArg(args, "limit", 500, 1, 3000);

        Map<String, Object> params = new HashMap<>();
        if (!hostname.isEmpty()) params.put("hostname", hostname);

        List<DiskState> list = diskStateService.selectAllByParams(params);
        if (list == null || list.isEmpty()) return "未找到磁盘数据。";

        // 内存过滤 minUsePer + 排序
        List<DiskState> filtered = new ArrayList<>(list.size());
        for (DiskState d : list) {
            if (minUsePer != null && d.getUsePerDouble() != null && d.getUsePerDouble() < minUsePer) continue;
            filtered.add(d);
        }
        if (sortByUsage) {
            filtered.sort(Comparator.comparing(
                (DiskState d) -> d.getUsePerDouble() == null ? -1.0 : d.getUsePerDouble()
            ).reversed());
        }
        if (filtered.isEmpty()) return "应用过滤后无匹配数据。";

        StringBuilder sb = new StringBuilder();
        sb.append("共 ").append(filtered.size()).append(" 条");
        if (filtered.size() > limit) sb.append("，仅展示前 ").append(limit);
        sb.append("\n\n| 主机 | 分区 | 总大小 | 已用 | 剩余 | 使用率% | 采集时间 |\n");
        sb.append("|------|------|--------|------|------|---------|----------|\n");
        int cnt = 0;
        for (DiskState d : filtered) {
            if (cnt++ >= limit) break;
            sb.append("| ").append(ListHostsTool.safe(d.getHostname()))
              .append(" | ").append(ListHostsTool.safe(d.getFileSystem()))
              .append(" | ").append(ListHostsTool.safe(d.getDiskSize()))
              .append(" | ").append(ListHostsTool.safe(d.getUsed()))
              .append(" | ").append(ListHostsTool.safe(d.getAvail()))
              .append(" | ").append(d.getUsePerDouble() == null ? "" : String.format("%.1f", d.getUsePerDouble()))
              .append(" | ").append(d.getCreateTime() == null ? "" : TS.format(d.getCreateTime()))
              .append(" |\n");
        }
        return ListHostsTool.cap(sb);
    }

    static Double numArg(Map<String,Object> a, String k) {
        Object v = a == null ? null : a.get(k);
        if (v == null) return null;
        try { return Double.valueOf(String.valueOf(v)); } catch (Exception e) { return null; }
    }
}
