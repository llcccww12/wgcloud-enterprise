package com.wgcloud.aiext.tool.impl;

import com.wgcloud.aiext.tool.AiTool;
import com.wgcloud.entity.HostMacInfo;
import com.wgcloud.service.HostMacInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 工具：list_mac_info —— 列出主机的网卡 MAC 地址（来自 HOST_MAC_INFO 表）。
 */
@Component
public class ListMacInfoTool implements AiTool {

    private static final SimpleDateFormat TS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired private HostMacInfoService hostMacInfoService;

    @Override public String getName() { return "list_mac_info"; }

    @Override public String getDescription() {
        return "列出主机的 MAC 地址清单（网卡名 + MAC 地址）。可按主机精确过滤。适合 '某主机的网卡 MAC 是什么' 这类问题。";
    }

    @Override public String getParametersJsonSchema() {
        return "{"
            + "\"type\":\"object\","
            + "\"properties\":{"
            +   "\"hostname\":{\"type\":\"string\",\"description\":\"主机名精确匹配，留空查全部\"},"
            +   "\"limit\":{\"type\":\"integer\",\"description\":\"返回行数，默认 500，最大 3000\"}"
            + "},"
            + "\"required\":[]"
            + "}";
    }

    @Override public String execute(Map<String, Object> args) throws Exception {
        String hostname = ListHostsTool.strArg(args, "hostname", "");
        int limit = ListHostsTool.intArg(args, "limit", 500, 1, 3000);

        Map<String, Object> params = new HashMap<>();
        if (!hostname.isEmpty()) params.put("hostname", hostname);

        List<HostMacInfo> list = hostMacInfoService.selectAllByParams(params);
        if (list == null || list.isEmpty()) return "未找到 MAC 信息。";

        StringBuilder sb = new StringBuilder();
        sb.append("共 ").append(list.size()).append(" 条");
        if (list.size() > limit) sb.append("，仅展示前 ").append(limit);
        sb.append("\n\n| 主机 | 网卡 | MAC 地址 | 采集时间 |\n");
        sb.append("|------|------|----------|----------|\n");
        int cnt = 0;
        for (HostMacInfo m : list) {
            if (cnt++ >= limit) break;
            sb.append("| ").append(ListHostsTool.safe(m.getHostname()))
              .append(" | ").append(ListHostsTool.safe(m.getMacName()))
              .append(" | ").append(ListHostsTool.safe(m.getMacAddress()))
              .append(" | ").append(m.getCreateTime() == null ? "" : TS.format(m.getCreateTime()))
              .append(" |\n");
        }
        return ListHostsTool.cap(sb);
    }
}
