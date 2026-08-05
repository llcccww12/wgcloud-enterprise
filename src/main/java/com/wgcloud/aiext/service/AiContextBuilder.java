package com.wgcloud.aiext.service;

import com.wgcloud.entity.AppExceptionInfo;
import com.wgcloud.entity.HostDiskPer;
import com.wgcloud.entity.LogInfo;
import com.wgcloud.entity.SystemInfo;
import com.wgcloud.service.AppExceptionInfoService;
import com.wgcloud.service.HostDiskPerService;
import com.wgcloud.service.LogInfoService;
import com.wgcloud.service.SystemInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * AI 上下文拼装服务（二开 - Layer 2）。
 *
 * 所有 AI 诊断 / 摘要 / 自然语言查询的共用上游：
 * 把异构监控数据组装成"对 LLM 友好"的 Markdown 文本。
 *
 * 设计原则：
 *  1. 输出大小可控（hard cap 8KB），避免 token 爆炸
 *  2. 输出可读（Markdown 段落 / 列表），LLM 解析效果好
 *  3. 不抛异常 — 任何子查询失败都用 [N/A] 顶住，避免单点拖垮整体诊断
 */
@Service
public class AiContextBuilder {

    private static final Logger logger = LoggerFactory.getLogger(AiContextBuilder.class);

    private static final SimpleDateFormat TS_FMT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /** 单次主机上下文最大字节数（按字符近似）。 */
    public static final int HOST_CTX_MAX_CHARS = 8000;

    /** 单次告警上下文最大字节数。 */
    public static final int ALARM_CTX_MAX_CHARS = 8000;

    /** IP / hostname 提取正则。优先识别 IPv4，其次主机名（含中文外的常见命名）。 */
    private static final Pattern IPV4_PAT = Pattern.compile(
            "\\b(?:(?:25[0-5]|2[0-4]\\d|[01]?\\d?\\d)\\.){3}(?:25[0-5]|2[0-4]\\d|[01]?\\d?\\d)\\b");
    private static final Pattern HOSTNAME_PAT = Pattern.compile(
            "[A-Za-z][A-Za-z0-9\\-_.]{2,62}");

    @Autowired private SystemInfoService systemInfoService;
    @Autowired private AppExceptionInfoService appExceptionInfoService;
    @Autowired private LogInfoService logInfoService;
    @Autowired private HostDiskPerService hostDiskPerService;

    /** 构建主机诊断上下文。返回 Markdown 文本；若主机不存在返回 null。 */
    public String buildHostContext(String hostId) {
        SystemInfo si;
        try {
            si = systemInfoService.selectById(hostId);
        } catch (Exception e) {
            logger.error("buildHostContext selectById fail hostId={}", hostId, e);
            return null;
        }
        if (si == null) return null;

        StringBuilder sb = new StringBuilder(2048);
        appendHostSnapshot(sb, si);
        appendDiskUsage(sb, si.getHostname());
        appendRecentAlarms(sb, si.getHostname(), 24);
        appendAbnormalProcesses(sb, si.getHostname());

        if (sb.length() > HOST_CTX_MAX_CHARS) {
            sb.setLength(HOST_CTX_MAX_CHARS);
            sb.append("\n...（上下文已截断，超过 ").append(HOST_CTX_MAX_CHARS).append(" 字符限制）");
        }
        return sb.toString();
    }

    /**
     * 构建告警分析上下文。
     * 输入告警 logId，自动：
     *  1. 拼装告警本身详情
     *  2. 从告警文本中提取主机/IP，匹配 SYSTEM_INFO 拿主机快照（若拿到）
     *  3. 取告警时刻前后 30 分钟、同主机的其他告警作为上下文
     * 返回 null 表示告警不存在。
     */
    public String buildAlarmContext(String logId) {
        LogInfo li;
        try {
            li = logInfoService.selectById(logId);
        } catch (Exception e) {
            logger.error("buildAlarmContext selectById fail logId={}", logId, e);
            return null;
        }
        if (li == null) return null;
        return buildAlarmContextFromEntity(li, false);
    }

    /**
     * 给"摘要回写"场景用：基于已加载的 LogInfo entity 构建紧凑上下文。
     * summaryMode=true 时省略 nearby alarms（更短、回写场景不需要那么多）。
     */
    public String buildAlarmContextFromEntity(LogInfo li, boolean summaryMode) {
        if (li == null) return null;
        StringBuilder sb = new StringBuilder(1024);
        appendAlarmDetail(sb, li);

        String matchedHostname = resolveHostnameFromAlarm(li);
        if (matchedHostname != null) {
            sb.append("> 关联主机：`").append(matchedHostname).append("`\n\n");
            SystemInfo si = trySelectByHostname(matchedHostname);
            if (si != null) {
                appendHostSnapshot(sb, si);
            }
            if (!summaryMode) {
                appendNearbyAlarms(sb, matchedHostname, li.getCreateTime(), 30);
            }
        }

        int cap = summaryMode ? 4000 : ALARM_CTX_MAX_CHARS;
        if (sb.length() > cap) {
            sb.setLength(cap);
            sb.append("\n...(截断)");
        }
        return sb.toString();
    }

    // -------------------------------------------------------------------------
    // 内部辅助
    // -------------------------------------------------------------------------

    private void appendHostSnapshot(StringBuilder sb, SystemInfo si) {
        sb.append("## 主机基础信息\n\n");
        sb.append("| 字段 | 值 |\n");
        sb.append("|------|----|\n");
        kv(sb, "主机名", safeStr(si.getHostname()));
        kv(sb, "操作系统", safeStr(si.getPlatForm()) + " " + safeStr(si.getPlatformVersion())
                + " (" + safeStr(si.getKernelArch()) + ")");
        kv(sb, "CPU 型号", safeStr(si.getCpuXh()) + " · " + safeStr(si.getCpuCoreNum()) + " 核");
        kv(sb, "物理内存", safeStr(si.getTotalMem()));
        kv(sb, "Agent 版本", safeStr(si.getAgentVer()) + " · 上报间隔 " + safeStr(si.getSubmitSeconds()) + "s");
        kv(sb, "启动时间", safeStr(si.getBootTimeStr()));
        kv(sb, "运行时长", safeStr(si.getUptimeStr()));
        kv(sb, "备注", safeStr(si.getRemark()));

        sb.append("\n## 当前实时指标\n\n");
        sb.append("| 指标 | 值 |\n|------|----|\n");
        kv(sb, "在线状态", "1".equals(si.getState()) ? "✅ 在线" : "❌ 离线（state=" + safeStr(si.getState()) + "）");
        kv(sb, "CPU 使用率",  pctOrNa(si.getCpuPer()));
        kv(sb, "内存使用率",  pctOrNa(si.getMemPer()));
        kv(sb, "磁盘总使用率", pctOrNa(si.getDiskPer()));
        kv(sb, "Swap 使用率", safeStr(si.getSwapMemPer()) + " %");
        kv(sb, "5 分钟系统负载",  String.valueOf(si.getFiveLoad()));
        kv(sb, "15 分钟系统负载", String.valueOf(si.getFifteenLoad()));
        kv(sb, "运行进程数",     safeStr(si.getProcs()));
        kv(sb, "网络连接数",     safeStr(si.getNetConnections()));
        kv(sb, "总接收流量(GB)", safeStr(si.getBytesRecv()));
        kv(sb, "总发送流量(GB)", safeStr(si.getBytesSent()));
        kv(sb, "上行速率",       safeStr(si.getTxbyt()));
        kv(sb, "下行速率",       safeStr(si.getRxbyt()));
        sb.append('\n');
    }

    private void appendDiskUsage(StringBuilder sb, String hostname) {
        if (isEmpty(hostname)) return;
        sb.append("## 磁盘分区使用率（最近上报快照）\n\n");
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("hostname", hostname);
            List<HostDiskPer> list = hostDiskPerService.selectAllByParams(params);
            if (list == null || list.isEmpty()) {
                sb.append("_（无磁盘数据）_\n\n");
                return;
            }
            sb.append("| 时间 | 磁盘汇总使用率 |\n|------|--------------|\n");
            int cnt = 0;
            for (HostDiskPer d : list) {
                if (cnt++ >= 5) break;
                sb.append("| ").append(safeStr(d.getDateStr()))
                  .append(" | ").append(d.getDiskSumPer() == null ? "N/A" : String.format("%.2f %%", d.getDiskSumPer()))
                  .append(" |\n");
            }
            sb.append('\n');
        } catch (Exception e) {
            logger.warn("appendDiskUsage fail hostname={}", hostname, e);
            sb.append("_（磁盘数据查询失败：").append(e.getMessage()).append("）_\n\n");
        }
    }

    private void appendRecentAlarms(StringBuilder sb, String hostname, int hours) {
        if (isEmpty(hostname)) return;
        sb.append("## 最近 ").append(hours).append(" 小时告警（LOG_INFO）\n\n");
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("hostname", hostname);
            // startTime/endTime 在原 mapper 里走 commonSqlUtil.createTimeQueryParams（接受字符串）
            Calendar c = Calendar.getInstance();
            String end = TS_FMT.format(c.getTime());
            c.add(Calendar.HOUR_OF_DAY, -hours);
            String start = TS_FMT.format(c.getTime());
            params.put("startTime", start);
            params.put("endTime", end);
            List<LogInfo> list = logInfoService.selectAllByParams(params);
            if (list == null || list.isEmpty()) {
                sb.append("_（无告警）_\n\n");
                return;
            }
            int total = list.size();
            sb.append("共 ").append(total).append(" 条。前 20 条：\n\n");
            sb.append("| 时间 | 状态 | 内容 |\n|------|------|------|\n");
            int cnt = 0;
            for (LogInfo li : list) {
                if (cnt++ >= 20) break;
                String time = li.getCreateTime() == null ? "" : TS_FMT.format(li.getCreateTime());
                sb.append("| ").append(time)
                  .append(" | ").append(safeStr(li.getState()))
                  .append(" | ").append(truncCell(li.getInfoContent(), 120))
                  .append(" |\n");
            }
            sb.append('\n');
        } catch (Exception e) {
            logger.warn("appendRecentAlarms fail hostname={}", hostname, e);
            sb.append("_（告警查询失败：").append(e.getMessage()).append("）_\n\n");
        }
    }

    private void appendAbnormalProcesses(StringBuilder sb, String hostname) {
        if (isEmpty(hostname)) return;
        sb.append("## 最近异常进程（APP_EXCEPTION_INFO）\n\n");
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("hostname", hostname);
            List<AppExceptionInfo> list = appExceptionInfoService.selectAllByParams(params);
            if (list == null || list.isEmpty()) {
                sb.append("_（无异常进程）_\n\n");
                return;
            }
            // 按 createTime 倒序取前 15
            List<AppExceptionInfo> sorted = new ArrayList<>(list);
            sorted.sort(Comparator.comparing(
                    (AppExceptionInfo a) -> a.getCreateTime() == null ? new Date(0) : a.getCreateTime()
            ).reversed());

            sb.append("共 ").append(list.size()).append(" 条。前 15 条：\n\n");
            sb.append("| 时间 | 进程 | PID | CPU% | 内存% | 命令行 |\n|------|------|----|------|-------|--------|\n");
            int cnt = 0;
            for (AppExceptionInfo a : sorted) {
                if (cnt++ >= 15) break;
                String time = a.getCreateTime() == null ? "" : TS_FMT.format(a.getCreateTime());
                sb.append("| ").append(time)
                  .append(" | ").append(safeStr(a.getAppName()))
                  .append(" | ").append(safeStr(a.getAppPid()))
                  .append(" | ").append(a.getCpuPer() == null ? "" : String.format("%.1f", a.getCpuPer()))
                  .append(" | ").append(a.getMemPer() == null ? "" : String.format("%.1f", a.getMemPer()))
                  .append(" | ").append(truncCell(a.getAppCmdLine(), 80))
                  .append(" |\n");
            }
            sb.append('\n');
        } catch (Exception e) {
            logger.warn("appendAbnormalProcesses fail hostname={}", hostname, e);
            sb.append("_（异常进程查询失败：").append(e.getMessage()).append("）_\n\n");
        }
    }

    // -------------------------------------------------------------------------
    // 工具
    // -------------------------------------------------------------------------

    private static void kv(StringBuilder sb, String k, String v) {
        sb.append("| ").append(k).append(" | ").append(v == null ? "" : v).append(" |\n");
    }

    private static String safeStr(String s) {
        if (s == null) return "";
        return s.replace('\n', ' ').replace('|', '/');
    }

    private static String pctOrNa(Double d) {
        return d == null ? "N/A" : String.format("%.2f %%", d);
    }

    private static String truncCell(String s, int max) {
        if (s == null) return "";
        String x = s.replace('\n', ' ').replace('|', '/');
        if (x.length() > max) return x.substring(0, max) + "...";
        return x;
    }

    private static boolean isEmpty(String s) {
        return s == null || s.isEmpty();
    }

    // -------------------------------------------------------------------------
    // Layer 2.2: 告警上下文
    // -------------------------------------------------------------------------

    private void appendAlarmDetail(StringBuilder sb, LogInfo li) {
        sb.append("## 告警详情\n\n");
        sb.append("| 字段 | 值 |\n|------|----|\n");
        kv(sb, "告警 ID", li.getId() == null ? "" : li.getId());
        kv(sb, "时间",     li.getCreateTime() == null ? "" : TS_FMT.format(li.getCreateTime()));
        kv(sb, "类型",     stateText(li.getState()));
        kv(sb, "摘要",     safeStr(li.getHostname()));
        sb.append("\n**告警内容：**\n\n```\n")
          .append(li.getInfoContent() == null ? "" : li.getInfoContent())
          .append("\n```\n\n");
    }

    /**
     * 从告警的 hostname(摘要) 和 infoContent 中提取真实主机标识。
     * 顺序：
     *  1. 摘要本身就是 SYSTEM_INFO.HOST_NAME → 直接返回
     *  2. 摘要 + 内容里抓 IPv4 → 若存在 SYSTEM_INFO 记录 → 返回
     *  3. 抓主机名候选词 → 若存在 → 返回
     *  4. 都失败 → null
     */
    private String resolveHostnameFromAlarm(LogInfo li) {
        if (li == null) return null;
        // 1) 摘要自身
        String hn = li.getHostname();
        if (!isEmpty(hn) && trySelectByHostname(hn) != null) {
            return hn;
        }
        String haystack = (hn == null ? "" : hn) + " " + (li.getInfoContent() == null ? "" : li.getInfoContent());
        // 2) IPv4
        Matcher mi = IPV4_PAT.matcher(haystack);
        while (mi.find()) {
            String ip = mi.group();
            if (trySelectByHostname(ip) != null) return ip;
        }
        // 3) hostname-like
        Matcher mh = HOSTNAME_PAT.matcher(haystack);
        while (mh.find()) {
            String cand = mh.group();
            // 忽略明显是日志关键字的候选
            String lower = cand.toLowerCase();
            if (lower.startsWith("info") || lower.startsWith("warn")
                    || lower.startsWith("error") || lower.startsWith("alarm")
                    || lower.startsWith("agent")) continue;
            if (trySelectByHostname(cand) != null) return cand;
        }
        return null;
    }

    private SystemInfo trySelectByHostname(String hostname) {
        if (isEmpty(hostname)) return null;
        try {
            return systemInfoService.selectByHostname(hostname);
        } catch (Exception e) {
            logger.debug("trySelectByHostname fail hostname={}", hostname);
            return null;
        }
    }

    /** 告警发生时刻前后 minutes 分钟内，同主机其他告警。 */
    private void appendNearbyAlarms(StringBuilder sb, String hostname, Date around, int minutes) {
        if (isEmpty(hostname) || around == null) return;
        sb.append("## 告警前后 ").append(minutes).append(" 分钟内同主机其他告警\n\n");
        try {
            Calendar c = Calendar.getInstance();
            c.setTime(around);
            c.add(Calendar.MINUTE, -minutes);
            String start = TS_FMT.format(c.getTime());
            c.setTime(around);
            c.add(Calendar.MINUTE, minutes);
            String end = TS_FMT.format(c.getTime());

            Map<String, Object> params = new HashMap<>();
            params.put("hostname", hostname);
            params.put("startTime", start);
            params.put("endTime", end);
            List<LogInfo> list = logInfoService.selectAllByParams(params);
            if (list == null || list.isEmpty()) {
                sb.append("_（窗口内无其他告警）_\n\n");
                return;
            }
            sb.append("共 ").append(list.size()).append(" 条。\n\n");
            sb.append("| 时间 | 类型 | 内容 |\n|------|------|------|\n");
            int cnt = 0;
            for (LogInfo x : list) {
                if (cnt++ >= 30) break;
                String t = x.getCreateTime() == null ? "" : TS_FMT.format(x.getCreateTime());
                sb.append("| ").append(t)
                  .append(" | ").append(stateText(x.getState()))
                  .append(" | ").append(truncCell(x.getInfoContent(), 120))
                  .append(" |\n");
            }
            sb.append('\n');
        } catch (Exception e) {
            logger.warn("appendNearbyAlarms fail hostname={}", hostname, e);
            sb.append("_（同段告警查询失败：").append(e.getMessage()).append("）_\n\n");
        }
    }

    private static String stateText(String state) {
        if (state == null) return "";
        switch (state) {
            case "1": return "业务告警";
            case "2": return "系统操作";
            case "3": return "告警恢复";
            case "4": return "第三方告警";
            default: return state;
        }
    }
}
