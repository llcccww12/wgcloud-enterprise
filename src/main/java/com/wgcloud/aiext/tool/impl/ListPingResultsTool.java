package com.wgcloud.aiext.tool.impl;

import com.wgcloud.aiext.tool.AiTool;
import com.wgcloud.entity.DceInfo;
import com.wgcloud.service.DceInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 工具：list_ping_results —— 列出网络设备 PING 监测结果（来自 DCE_INFO 表）。
 * 对应原 LLM 文件"全量PING监控快照"。
 */
@Component
public class ListPingResultsTool implements AiTool {

    private static final SimpleDateFormat TS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired private DceInfoService dceInfoService;

    @Override public String getName() { return "list_ping_results"; }

    @Override public String getDescription() {
        return "列出网络设备 PING 监测情况（IP、最近响应时间ms、告警次数、备注、状态）。" +
               "适合 '哪些设备 ping 超时了' '查一下 xxx 这个 IP 的 ping 状态'。";
    }

    @Override public String getParametersJsonSchema() {
        return "{"
            + "\"type\":\"object\","
            + "\"properties\":{"
            +   "\"hostname\":{\"type\":\"string\",\"description\":\"IP 或主机名模糊匹配，留空查全部\"},"
            +   "\"onlyTimeout\":{\"type\":\"boolean\",\"description\":\"是否只看 PING 超时设备（resTimes<=0 表示无响应或 -1），默认 false\"},"
            +   "\"limit\":{\"type\":\"integer\",\"description\":\"返回行数，默认 500，最大 3000\"}"
            + "},"
            + "\"required\":[]"
            + "}";
    }

    @Override public String execute(Map<String, Object> args) throws Exception {
        String hostname = ListHostsTool.strArg(args, "hostname", "");
        boolean onlyTimeout = "true".equalsIgnoreCase(ListHostsTool.strArg(args, "onlyTimeout", "false"));
        int limit = ListHostsTool.intArg(args, "limit", 500, 1, 3000);

        Map<String, Object> params = new HashMap<>();
        if (!hostname.isEmpty()) params.put("hostname", hostname);

        List<DceInfo> list = dceInfoService.selectAllByParams(params);
        if (list == null || list.isEmpty()) return "未找到 PING 监测数据。";

        StringBuilder sb = new StringBuilder();
        int total = list.size();
        sb.append("共 ").append(total).append(" 条");
        if (onlyTimeout) sb.append("（仅展示 PING 超时）");
        sb.append("\n\n| IP/主机名 | 响应时间ms | 告警次数 | 备注 | 标签 | 上报时间 |\n");
        sb.append("|----------|-----------|---------|------|------|---------|\n");
        int cnt = 0, shown = 0;
        for (DceInfo d : list) {
            if (cnt++ >= 5000) break; // 内部硬保护
            Integer rt = d.getResTimes();
            if (onlyTimeout && (rt == null || rt > 0)) continue;
            if (shown >= limit) break;
            shown++;
            sb.append("| ").append(ListHostsTool.safe(d.getHostname()))
              .append(" | ").append(rt == null ? "N/A" : rt.toString())
              .append(" | ").append(d.getWarnCount() == null ? 0 : d.getWarnCount())
              .append(" | ").append(ListHostsTool.safe(d.getRemark()))
              .append(" | ").append(ListHostsTool.safe(d.getGroupId()))
              .append(" | ").append(d.getCreateTime() == null ? "" : TS.format(d.getCreateTime()))
              .append(" |\n");
        }
        if (shown == 0) sb.append("\n_（过滤后无匹配）_\n");
        return ListHostsTool.cap(sb);
    }
}
