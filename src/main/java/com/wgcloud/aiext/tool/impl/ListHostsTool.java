package com.wgcloud.aiext.tool.impl;

import com.wgcloud.aiext.tool.AiTool;
import com.wgcloud.entity.SystemInfo;
import com.wgcloud.service.SystemInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 工具 1：list_hosts —— 列出所有/部分主机。
 * 适合 "我有几台机器" / "查找名字包含 xxx 的主机" 这类问法。
 */
@Component
public class ListHostsTool implements AiTool {

    private static final Logger logger = LoggerFactory.getLogger(ListHostsTool.class);

    @Autowired private SystemInfoService systemInfoService;

    @Override public String getName() { return "list_hosts"; }

    @Override public String getDescription() {
        return "列出主机清单。可按主机名模糊匹配或在线状态过滤。返回主机名、状态、CPU%、内存%、磁盘%、5分钟负载等概览。";
    }

    @Override public String getParametersJsonSchema() {
        return "{"
            + "\"type\":\"object\","
            + "\"properties\":{"
            +   "\"nameLike\":{\"type\":\"string\",\"description\":\"主机名模糊匹配，留空查全部\"},"
            +   "\"stateFilter\":{\"type\":\"string\",\"enum\":[\"online\",\"offline\",\"all\"],\"description\":\"在线状态过滤，默认 all\"},"
            +   "\"limit\":{\"type\":\"integer\",\"description\":\"返回行数，默认 500，最大 3000\"}"
            + "},"
            + "\"required\":[]"
            + "}";
    }

    @Override public String execute(Map<String, Object> args) throws Exception {
        String nameLike = strArg(args, "nameLike", "");
        String state    = strArg(args, "stateFilter", "all");
        int limit       = intArg(args, "limit", 500, 1, 3000);

        Map<String, Object> params = new HashMap<>();
        if (!nameLike.isEmpty()) params.put("hostname", nameLike);
        if ("online".equalsIgnoreCase(state))  params.put("state", "1");
        if ("offline".equalsIgnoreCase(state)) params.put("state", "2");

        List<SystemInfo> list = systemInfoService.selectAllByParams(params);
        if (list == null || list.isEmpty()) return "没有匹配的主机。";

        StringBuilder sb = new StringBuilder();
        sb.append("共 ").append(list.size()).append(" 台。");
        if (list.size() > limit) sb.append("仅展示前 ").append(limit).append(" 台。");
        sb.append("\n\n| 主机名 | 状态 | CPU% | 内存% | 磁盘% | 5min负载 | OS |\n");
        sb.append("|--------|------|------|-------|-------|----------|----|\n");
        int cnt = 0;
        for (SystemInfo si : list) {
            if (cnt++ >= limit) break;
            sb.append("| ").append(safe(si.getHostname()))
              .append(" | ").append("1".equals(si.getState()) ? "在线" : "离线")
              .append(" | ").append(pct(si.getCpuPer()))
              .append(" | ").append(pct(si.getMemPer()))
              .append(" | ").append(pct(si.getDiskPer()))
              .append(" | ").append(si.getFiveLoad())
              .append(" | ").append(safe(si.getPlatForm()))
              .append(" |\n");
        }
        return cap(sb);
    }

    static String strArg(Map<String,Object> a, String k, String def) {
        Object v = a == null ? null : a.get(k);
        return v == null ? def : String.valueOf(v).trim();
    }
    static int intArg(Map<String,Object> a, String k, int def, int min, int max) {
        Object v = a == null ? null : a.get(k);
        if (v == null) return def;
        try {
            int n = (v instanceof Number) ? ((Number) v).intValue() : Integer.parseInt(String.valueOf(v));
            if (n < min) n = min;
            if (n > max) n = max;
            return n;
        } catch (Exception e) { return def; }
    }
    static String safe(String s) {
        if (s == null) return "";
        return s.replace('\n',' ').replace('|','/');
    }
    static String pct(Double d) { return d == null ? "N/A" : String.format("%.1f%%", d); }

    /** 单个工具输出硬上限（字符数）。超过则截断并附加提示。 */
    static final int SINGLE_TOOL_OUTPUT_MAX = 80000;
    static String cap(StringBuilder sb) {
        if (sb.length() <= SINGLE_TOOL_OUTPUT_MAX) return sb.toString();
        return sb.substring(0, SINGLE_TOOL_OUTPUT_MAX)
                + "\n\n_（已截断：输出超过 " + SINGLE_TOOL_OUTPUT_MAX + " 字符上限，请用 limit 参数减少返回数据）_";
    }
}
