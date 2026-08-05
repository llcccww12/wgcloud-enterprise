package com.wgcloud.aiext.tool.impl;

import com.wgcloud.aiext.tool.AiTool;
import com.wgcloud.entity.LogInfo;
import com.wgcloud.service.LogInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 工具 4：list_alarms —— 查告警/事件日志。
 */
@Component
public class ListAlarmsTool implements AiTool {

    private static final SimpleDateFormat TS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired private LogInfoService logInfoService;

    @Override public String getName() { return "list_alarms"; }

    @Override public String getDescription() {
        return "查询告警 / 日志记录。可按主机模糊匹配、时间窗（小时）、告警类型过滤。" +
               "stateFilter 含义：1=业务告警 2=系统操作 3=告警恢复 4=第三方告警 all=全部。";
    }

    @Override public String getParametersJsonSchema() {
        return "{"
            + "\"type\":\"object\","
            + "\"properties\":{"
            +   "\"hostnameLike\":{\"type\":\"string\",\"description\":\"主机名模糊匹配，留空查全部\"},"
            +   "\"hours\":{\"type\":\"integer\",\"description\":\"最近多少小时内（默认 24，最大 168=7天）\"},"
            +   "\"stateFilter\":{\"type\":\"string\",\"enum\":[\"1\",\"2\",\"3\",\"4\",\"all\"],\"description\":\"告警状态过滤，默认 1（业务告警）\"},"
            +   "\"limit\":{\"type\":\"integer\",\"description\":\"返回条数，默认 500，最大 3000\"}"
            + "},"
            + "\"required\":[]"
            + "}";
    }

    @Override public String execute(Map<String, Object> args) throws Exception {
        String hostnameLike = ListHostsTool.strArg(args, "hostnameLike", "");
        int hours  = ListHostsTool.intArg(args, "hours",  24, 1, 168);
        String state = ListHostsTool.strArg(args, "stateFilter", "1");
        int limit  = ListHostsTool.intArg(args, "limit", 500, 1, 3000);

        Calendar c = Calendar.getInstance();
        String end = TS.format(c.getTime());
        c.add(Calendar.HOUR_OF_DAY, -hours);
        String start = TS.format(c.getTime());

        Map<String, Object> params = new HashMap<>();
        if (!hostnameLike.isEmpty()) params.put("hostname", hostnameLike);
        params.put("startTime", start);
        params.put("endTime", end);
        if (!"all".equalsIgnoreCase(state)) params.put("state", state);

        List<LogInfo> list = logInfoService.selectAllByParams(params);
        if (list == null || list.isEmpty()) {
            return "时间窗（最近 " + hours + " 小时）内没有匹配告警。";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("时间窗：最近 ").append(hours).append(" 小时（").append(start).append(" ~ ").append(end).append("）\n");
        sb.append("总数：").append(list.size()).append(" 条");
        if (list.size() > limit) sb.append("，仅展示前 ").append(limit).append(" 条");
        sb.append("\n\n| 时间 | 类型 | 主机/摘要 | 内容 |\n|------|------|----------|------|\n");
        int cnt = 0;
        for (LogInfo li : list) {
            if (cnt++ >= limit) break;
            sb.append("| ").append(li.getCreateTime() == null ? "" : TS.format(li.getCreateTime()))
              .append(" | ").append(stateText(li.getState()))
              .append(" | ").append(trunc(li.getHostname(), 30))
              .append(" | ").append(trunc(stripAiSummary(li.getInfoContent()), 100))
              .append(" |\n");
        }
        return sb.toString();
    }

    /** 输出时把已 enrich 的 [AI摘要]...[/AI摘要] 段剥掉，避免噪音。 */
    private static String stripAiSummary(String s) {
        if (s == null) return "";
        int a = s.indexOf("[AI摘要]");
        if (a < 0) return s;
        int b = s.indexOf("[/AI摘要]", a);
        if (b < 0) return s.substring(0, a);
        return s.substring(0, a) + s.substring(b + "[/AI摘要]".length());
    }

    private static String stateText(String s) {
        if (s == null) return "";
        switch (s) {
            case "1": return "业务告警";
            case "2": return "系统操作";
            case "3": return "告警恢复";
            case "4": return "第三方告警";
            default: return s;
        }
    }
    private static String trunc(String s, int max) {
        if (s == null) return "";
        s = s.replace('\n',' ').replace('|','/');
        return s.length() > max ? s.substring(0, max) + "..." : s;
    }
}
