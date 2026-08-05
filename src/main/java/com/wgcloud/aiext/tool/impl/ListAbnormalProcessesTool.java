package com.wgcloud.aiext.tool.impl;

import com.wgcloud.aiext.tool.AiTool;
import com.wgcloud.entity.AppExceptionInfo;
import com.wgcloud.service.AppExceptionInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 工具 5：list_abnormal_processes —— 查异常进程（agent 上报 cpu>60% 或 mem>60% 的进程）。
 */
@Component
public class ListAbnormalProcessesTool implements AiTool {

    private static final SimpleDateFormat TS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired private AppExceptionInfoService appExceptionInfoService;

    @Override public String getName() { return "list_processes"; }

    @Override public String getDescription() {
        return "查询进程列表（来自 APP_EXCEPTION_INFO 表，含主机所有上报进程；agent 也会重点记录 CPU 或内存使用率高的进程）。" +
               "可按主机过滤、按 CPU 阈值过滤（cpuMin=80 只看 CPU>=80% 的进程）。适合 '哪些进程占用资源最高' '某主机有哪些进程'。";
    }

    @Override public String getParametersJsonSchema() {
        return "{"
            + "\"type\":\"object\","
            + "\"properties\":{"
            +   "\"hostname\":{\"type\":\"string\",\"description\":\"主机名精确匹配，留空查全部\"},"
            +   "\"cpuMin\":{\"type\":\"number\",\"description\":\"CPU 使用率下限（%），如 80 表示只看 CPU>=80% 的进程\"},"
            +   "\"limit\":{\"type\":\"integer\",\"description\":\"返回条数，默认 500，最大 3000\"}"
            + "},"
            + "\"required\":[]"
            + "}";
    }

    @Override public String execute(Map<String, Object> args) throws Exception {
        String hostname = ListHostsTool.strArg(args, "hostname", "");
        int limit = ListHostsTool.intArg(args, "limit", 500, 1, 3000);
        Double cpuMin = null;
        Object cm = args == null ? null : args.get("cpuMin");
        if (cm != null) {
            try { cpuMin = Double.valueOf(String.valueOf(cm)); } catch (Exception ignore) {}
        }

        Map<String, Object> params = new HashMap<>();
        if (!hostname.isEmpty()) params.put("hostname", hostname);
        if (cpuMin != null) params.put("cpuPer", cpuMin);

        List<AppExceptionInfo> list = appExceptionInfoService.selectAllByParams(params);
        if (list == null || list.isEmpty()) return "未找到匹配的异常进程。";

        // 按 createTime 倒序
        List<AppExceptionInfo> sorted = new ArrayList<>(list);
        sorted.sort(Comparator.comparing(
                (AppExceptionInfo a) -> a.getCreateTime() == null ? new Date(0) : a.getCreateTime()
        ).reversed());

        StringBuilder sb = new StringBuilder();
        sb.append("匹配 ").append(list.size()).append(" 条");
        if (list.size() > limit) sb.append("，仅展示前 ").append(limit);
        sb.append("\n\n| 时间 | 主机 | 进程 | PID | CPU% | 内存% | 命令行 |\n");
        sb.append("|------|------|------|-----|------|-------|--------|\n");
        int cnt = 0;
        for (AppExceptionInfo a : sorted) {
            if (cnt++ >= limit) break;
            sb.append("| ").append(a.getCreateTime() == null ? "" : TS.format(a.getCreateTime()))
              .append(" | ").append(ListHostsTool.safe(a.getHostname()))
              .append(" | ").append(ListHostsTool.safe(a.getAppName()))
              .append(" | ").append(ListHostsTool.safe(a.getAppPid()))
              .append(" | ").append(a.getCpuPer() == null ? "" : String.format("%.1f", a.getCpuPer()))
              .append(" | ").append(a.getMemPer() == null ? "" : String.format("%.1f", a.getMemPer()))
              .append(" | ").append(truncCell(a.getAppCmdLine(), 80))
              .append(" |\n");
        }
        return ListHostsTool.cap(sb);
    }

    private static String truncCell(String s, int max) {
        if (s == null) return "";
        s = s.replace('\n', ' ').replace('|', '/');
        return s.length() > max ? s.substring(0, max) + "..." : s;
    }
}
