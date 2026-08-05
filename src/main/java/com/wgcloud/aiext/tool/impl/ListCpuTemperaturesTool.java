package com.wgcloud.aiext.tool.impl;

import com.wgcloud.aiext.tool.AiTool;
import com.wgcloud.entity.CpuTemperatures;
import com.wgcloud.service.CpuTemperaturesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 工具：list_cpu_temperatures —— 列出主机 CPU 核心温度（来自 CPU_TEMPERATURES 表）。
 */
@Component
public class ListCpuTemperaturesTool implements AiTool {

    private static final SimpleDateFormat TS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired private CpuTemperaturesService cpuTemperaturesService;

    @Override public String getName() { return "list_cpu_temperatures"; }

    @Override public String getDescription() {
        return "列出主机 CPU 各核心温度（℃）。含输入温度 input、临界温度 crit、最大温度 max。" +
               "适合 '哪台主机 CPU 温度过高' '查询 xxx 主机的 CPU 温度'。";
    }

    @Override public String getParametersJsonSchema() {
        return "{"
            + "\"type\":\"object\","
            + "\"properties\":{"
            +   "\"hostname\":{\"type\":\"string\",\"description\":\"主机名精确匹配，留空查全部\"},"
            +   "\"minTempC\":{\"type\":\"number\",\"description\":\"输入温度下限℃，例如 70 表示只看 >=70℃ 的核心\"},"
            +   "\"limit\":{\"type\":\"integer\",\"description\":\"返回行数，默认 500，最大 3000\"}"
            + "},"
            + "\"required\":[]"
            + "}";
    }

    @Override public String execute(Map<String, Object> args) throws Exception {
        String hostname = ListHostsTool.strArg(args, "hostname", "");
        Double minTemp  = ListDiskUsageTool.numArg(args, "minTempC");
        int limit       = ListHostsTool.intArg(args, "limit", 500, 1, 3000);

        Map<String, Object> params = new HashMap<>();
        if (!hostname.isEmpty()) params.put("hostname", hostname);

        List<CpuTemperatures> list = cpuTemperaturesService.selectAllByParams(params);
        if (list == null || list.isEmpty()) return "未找到 CPU 温度数据。";

        // 过滤 + 按 input 倒序
        List<CpuTemperatures> filtered = new ArrayList<>();
        for (CpuTemperatures t : list) {
            if (minTemp != null) {
                Double in = parseD(t.getInput());
                if (in == null || in < minTemp) continue;
            }
            filtered.add(t);
        }
        filtered.sort(Comparator.comparing((CpuTemperatures t) -> {
            Double d = parseD(t.getInput());
            return d == null ? -1.0 : d;
        }).reversed());

        if (filtered.isEmpty()) return "应用过滤后无匹配数据。";

        StringBuilder sb = new StringBuilder();
        sb.append("共 ").append(filtered.size()).append(" 条");
        if (filtered.size() > limit) sb.append("，仅展示前 ").append(limit);
        sb.append("\n\n| 主机 | 核心 | 当前温度℃ | 最大温度℃ | 临界温度℃ | 采集时间 |\n");
        sb.append("|------|------|-----------|-----------|-----------|----------|\n");
        int cnt = 0;
        for (CpuTemperatures t : filtered) {
            if (cnt++ >= limit) break;
            sb.append("| ").append(ListHostsTool.safe(t.getHostname()))
              .append(" | ").append(ListHostsTool.safe(t.getCore_index()))
              .append(" | ").append(ListHostsTool.safe(t.getInput()))
              .append(" | ").append(ListHostsTool.safe(t.getMax()))
              .append(" | ").append(ListHostsTool.safe(t.getCrit()))
              .append(" | ").append(t.getCreateTime() == null ? "" : TS.format(t.getCreateTime()))
              .append(" |\n");
        }
        return ListHostsTool.cap(sb);
    }

    private static Double parseD(String s) {
        if (s == null || s.isEmpty()) return null;
        try { return Double.valueOf(s); } catch (Exception e) { return null; }
    }
}
