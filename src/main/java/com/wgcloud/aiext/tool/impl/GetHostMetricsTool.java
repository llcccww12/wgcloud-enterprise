package com.wgcloud.aiext.tool.impl;

import com.wgcloud.aiext.service.AiContextBuilder;
import com.wgcloud.aiext.tool.AiTool;
import com.wgcloud.entity.SystemInfo;
import com.wgcloud.service.SystemInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 工具 2：get_host_metrics —— 查单台主机的详细指标快照（复用 Layer 2.1 的 AiContextBuilder）。
 */
@Component
public class GetHostMetricsTool implements AiTool {

    @Autowired private SystemInfoService systemInfoService;
    @Autowired private AiContextBuilder aiContextBuilder;

    @Override public String getName() { return "get_host_metrics"; }

    @Override public String getDescription() {
        return "查询单台主机当前的详细指标（CPU、内存、磁盘、负载、网络、最近告警、异常进程）。" +
               "适合用户问 '某某主机怎么样了' '某IP现在状态如何'。";
    }

    @Override public String getParametersJsonSchema() {
        return "{"
            + "\"type\":\"object\","
            + "\"properties\":{"
            +   "\"hostname\":{\"type\":\"string\",\"description\":\"主机名或 IP，必须精确匹配 SYSTEM_INFO.HOST_NAME\"}"
            + "},"
            + "\"required\":[\"hostname\"]"
            + "}";
    }

    @Override public String execute(Map<String, Object> args) throws Exception {
        String hostname = ListHostsTool.strArg(args, "hostname", "");
        if (hostname.isEmpty()) return "请提供 hostname 参数";

        SystemInfo si = systemInfoService.selectByHostname(hostname);
        if (si == null) return "未找到主机 hostname=" + hostname + "。可先用 list_hosts(nameLike=...) 模糊查找。";

        String ctx = aiContextBuilder.buildHostContext(si.getId());
        if (ctx == null) return "构建主机上下文失败";
        // 截断防爆
        if (ctx.length() > 3500) ctx = ctx.substring(0, 3500) + "\n...(截断)";
        return ctx;
    }
}
