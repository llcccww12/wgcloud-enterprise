package com.wgcloud.aiext.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * AI 大模型接入配置（二开 - Layer 1）。
 * 对应 application.yml 中 ai.* 段。
 */
@Configuration
@ConfigurationProperties(prefix = "ai")
public class AiConfig {

    /** 总开关 yes/no（沿用项目风格用字符串） */
    private String enabled = "no";

    /** API 基地址，不带尾斜杠 */
    private String baseUrl = "";

    /** API Key (Bearer Token) */
    private String apiKey = "";

    /** 模型名 */
    private String model = "";

    /** 采样温度 */
    private Double temperature = 0.3;

    /** 最大输出 token，0 表示不限制 */
    private Integer maxTokens = 2048;

    /** 单次请求超时秒 */
    private Integer timeoutSeconds = 120;

    /** 默认 system prompt */
    private String systemPrompt = "你是 cac 智能运维监控平台的助手，回答简洁、专业、用中文。";

    /** 单次请求最大字符数（输入截断保护） */
    private Integer maxPromptChars = 60000;

    /** 告警摘要回写嵌套配置。 */
    private Enrich enrich = new Enrich();

    /** 嵌套配置 ai.enrich.* */
    public static class Enrich {
        private String enabled = "no";
        private Integer scanIntervalSeconds = 30;
        private Integer lookbackMinutes = 5;
        private Integer batchLimit = 30;
        private Integer dedupMinutes = 5;
        private Integer perMinuteLimit = 20;
        private String summaryPrompt = "请用最多5行中文简要分析告警。";
        private String markerStart = "[AI摘要]";
        private String markerEnd = "[/AI摘要]";

        public boolean isEnabled() {
            if (enabled == null) return false;
            String v = enabled.trim().toLowerCase();
            return "yes".equals(v) || "true".equals(v) || "on".equals(v) || "1".equals(v);
        }
        public String getEnabled() { return enabled; }
        public void setEnabled(String enabled) { this.enabled = enabled; }
        public Integer getScanIntervalSeconds() { return scanIntervalSeconds; }
        public void setScanIntervalSeconds(Integer v) { this.scanIntervalSeconds = v; }
        public Integer getLookbackMinutes() { return lookbackMinutes; }
        public void setLookbackMinutes(Integer v) { this.lookbackMinutes = v; }
        public Integer getBatchLimit() { return batchLimit; }
        public void setBatchLimit(Integer v) { this.batchLimit = v; }
        public Integer getDedupMinutes() { return dedupMinutes; }
        public void setDedupMinutes(Integer v) { this.dedupMinutes = v; }
        public Integer getPerMinuteLimit() { return perMinuteLimit; }
        public void setPerMinuteLimit(Integer v) { this.perMinuteLimit = v; }
        public String getSummaryPrompt() { return summaryPrompt; }
        public void setSummaryPrompt(String v) { this.summaryPrompt = v; }
        public String getMarkerStart() { return markerStart; }
        public void setMarkerStart(String v) { this.markerStart = v; }
        public String getMarkerEnd() { return markerEnd; }
        public void setMarkerEnd(String v) { this.markerEnd = v; }
    }

    public Enrich getEnrich() { return enrich; }
    public void setEnrich(Enrich enrich) { this.enrich = enrich; }

    public boolean isEnabled() {
        if (enabled == null) return false;
        String v = enabled.trim().toLowerCase();
        return "yes".equals(v) || "true".equals(v) || "on".equals(v) || "1".equals(v);
    }

    public String getEnabled() { return enabled; }
    public void setEnabled(String enabled) { this.enabled = enabled; }
    public String getBaseUrl() { return baseUrl; }
    public void setBaseUrl(String baseUrl) { this.baseUrl = baseUrl; }
    public String getApiKey() { return apiKey; }
    public void setApiKey(String apiKey) { this.apiKey = apiKey; }
    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }
    public Double getTemperature() { return temperature; }
    public void setTemperature(Double temperature) { this.temperature = temperature; }
    public Integer getMaxTokens() { return maxTokens; }
    public void setMaxTokens(Integer maxTokens) { this.maxTokens = maxTokens; }
    public Integer getTimeoutSeconds() { return timeoutSeconds; }
    public void setTimeoutSeconds(Integer timeoutSeconds) { this.timeoutSeconds = timeoutSeconds; }
    public String getSystemPrompt() { return systemPrompt; }
    public void setSystemPrompt(String systemPrompt) { this.systemPrompt = systemPrompt; }
    public Integer getMaxPromptChars() { return maxPromptChars; }
    public void setMaxPromptChars(Integer maxPromptChars) { this.maxPromptChars = maxPromptChars; }
}
