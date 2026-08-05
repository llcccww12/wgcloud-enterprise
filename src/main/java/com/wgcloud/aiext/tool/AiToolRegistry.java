package com.wgcloud.aiext.tool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * AI 工具注册中心。
 * 自动收集所有 Spring 容器里的 AiTool bean，按名字索引。
 * 提供 dispatch(name, args) 入口给 AiClient 用。
 */
@Component
public class AiToolRegistry {

    private static final Logger logger = LoggerFactory.getLogger(AiToolRegistry.class);

    @Autowired(required = false)
    private List<AiTool> toolBeans;

    private final Map<String, AiTool> toolsByName = new LinkedHashMap<>();

    @PostConstruct
    public void init() {
        if (toolBeans != null) {
            for (AiTool t : toolBeans) {
                toolsByName.put(t.getName(), t);
                logger.info("AiToolRegistry registered: {} - {}", t.getName(), t.getDescription());
            }
        }
        logger.info("AiToolRegistry total {} tools", toolsByName.size());
    }

    /** 给 LLM 用的 tools 数组 JSON（用于 chat.completions 请求体）。 */
    public String buildToolsJsonArray() {
        if (toolsByName.isEmpty()) return "[]";
        StringBuilder sb = new StringBuilder(512);
        sb.append('[');
        boolean first = true;
        for (AiTool t : toolsByName.values()) {
            if (!first) sb.append(',');
            sb.append("{\"type\":\"function\",\"function\":{")
              .append("\"name\":\"").append(t.getName()).append("\",")
              .append("\"description\":\"").append(escape(t.getDescription())).append("\",")
              .append("\"parameters\":").append(t.getParametersJsonSchema())
              .append("}}");
            first = false;
        }
        sb.append(']');
        return sb.toString();
    }

    public AiTool find(String name) {
        return toolsByName.get(name);
    }

    public Map<String, AiTool> all() {
        return Collections.unmodifiableMap(toolsByName);
    }

    /** JSON string escape（不带外层引号）。 */
    static String escape(String s) {
        if (s == null) return "";
        StringBuilder sb = new StringBuilder(s.length() + 8);
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            switch (c) {
                case '\\': sb.append("\\\\"); break;
                case '"':  sb.append("\\\""); break;
                case '\n': sb.append("\\n"); break;
                case '\r': sb.append("\\r"); break;
                case '\t': sb.append("\\t"); break;
                case '\b': sb.append("\\b"); break;
                case '\f': sb.append("\\f"); break;
                default:
                    if (c < 0x20) sb.append(String.format("\\u%04x", (int) c));
                    else sb.append(c);
            }
        }
        return sb.toString();
    }
}
