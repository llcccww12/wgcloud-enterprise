package com.wgcloud.aiext.util;

import com.wgcloud.aiext.config.AiConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * OpenAI 兼容大模型客户端（二开 - Layer 1）。
 *
 * 支持：
 *  - 非流式 chat（同步返回字符串）
 *  - 流式 chat（SSE 边到边推到 Consumer<String>）
 *
 * 不引入新依赖，使用 JDK 11 自带的 java.net.http 与项目已存在的 Jackson。
 */
@Component
public class AiClient {

    private static final Logger logger = LoggerFactory.getLogger(AiClient.class);

    @Autowired
    private AiConfig aiConfig;

    private HttpClient httpClient;

    private synchronized HttpClient httpClient() {
        if (httpClient == null) {
            httpClient = HttpClient.newBuilder()
                    .connectTimeout(Duration.ofSeconds(15))
                    .version(HttpClient.Version.HTTP_1_1)
                    .build();
        }
        return httpClient;
    }

    /** 简单消息对象，避免引入额外类。 */
    public static class Msg {
        public final String role;
        public final String content;
        public Msg(String role, String content) {
            this.role = role;
            this.content = content;
        }
        public static Msg system(String c)   { return new Msg("system", c); }
        public static Msg user(String c)     { return new Msg("user", c); }
        public static Msg assistant(String c){ return new Msg("assistant", c); }
    }

    /** 同步调用，返回完整回答。 */
    public String chat(List<Msg> messages) throws Exception {
        HttpRequest req = buildRequest(messages, false);
        HttpResponse<String> resp = httpClient().send(req, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        if (resp.statusCode() / 100 != 2) {
            throw new RuntimeException("LLM HTTP " + resp.statusCode() + ": " + resp.body());
        }
        // 极简提取：从 "content":"..." 第一次出现的位置取字符串值
        return extractContent(resp.body(), "\"message\"");
    }

    /**
     * 流式调用。每收到一个 token/片段就把内容推给 onDelta。
     * 返回总字符数（用于日志/计费）。
     */
    public int chatStream(List<Msg> messages, Consumer<String> onDelta) throws Exception {
        HttpRequest req = buildRequest(messages, true);
        HttpResponse<InputStream> resp = httpClient().send(req, HttpResponse.BodyHandlers.ofInputStream());
        if (resp.statusCode() / 100 != 2) {
            StringBuilder errBody = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new InputStreamReader(resp.body(), StandardCharsets.UTF_8))) {
                String line;
                while ((line = br.readLine()) != null) errBody.append(line).append('\n');
            }
            throw new RuntimeException("LLM HTTP " + resp.statusCode() + ": " + errBody);
        }

        int totalChars = 0;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(resp.body(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.isEmpty()) continue;
                if (!line.startsWith("data:")) continue;
                String data = line.substring(5).trim();
                if ("[DONE]".equals(data)) break;
                try {
                    String piece = extractContent(data, "\"delta\"");
                    if (piece != null && !piece.isEmpty()) {
                        onDelta.accept(piece);
                        totalChars += piece.length();
                    }
                } catch (Exception parseEx) {
                    logger.warn("AI stream parse fail line={}", line, parseEx);
                }
            }
        }
        return totalChars;
    }

    private HttpRequest buildRequest(List<Msg> messages, boolean stream) {
        if (!aiConfig.isEnabled()) {
            throw new IllegalStateException("AI 助手未启用（application.yml: ai.enabled=no）");
        }
        String base = aiConfig.getBaseUrl();
        if (base == null || base.isEmpty()) {
            throw new IllegalStateException("ai.baseUrl 未配置");
        }
        if (base.endsWith("/")) base = base.substring(0, base.length() - 1);
        String url = base + "/chat/completions";

        // 手写 JSON，避开项目内 jackson-core/jackson-databind 版本不一致的坑。
        StringBuilder json = new StringBuilder(256 + messages.size() * 64);
        json.append('{');
        json.append("\"model\":").append(jsonString(aiConfig.getModel())).append(',');
        json.append("\"temperature\":").append(aiConfig.getTemperature() == null ? 0.3 : aiConfig.getTemperature()).append(',');
        if (aiConfig.getMaxTokens() != null && aiConfig.getMaxTokens() > 0) {
            json.append("\"max_tokens\":").append(aiConfig.getMaxTokens()).append(',');
        }
        json.append("\"stream\":").append(stream).append(',');
        json.append("\"messages\":[");

        int maxChars = aiConfig.getMaxPromptChars() == null ? 60000 : aiConfig.getMaxPromptChars();
        int used = 0;
        boolean first = true;
        boolean truncated = false;
        for (Msg m : messages) {
            String c = m.content == null ? "" : m.content;
            if (used + c.length() > maxChars) {
                int remain = Math.max(0, maxChars - used);
                if (remain > 0) {
                    c = c.substring(0, remain) + "\n...（已截断，超过 ai.maxPromptChars 限制）";
                } else {
                    truncated = true;
                    break;
                }
                truncated = true;
            }
            if (!first) json.append(',');
            json.append("{\"role\":").append(jsonString(m.role))
                .append(",\"content\":").append(jsonString(c)).append('}');
            used += c.length();
            first = false;
            if (truncated) break;
        }
        json.append("]}");

        String payload = json.toString();
        if (truncated) {
            logger.warn("AI prompt truncated, finalLen={} limit={}", payload.length(), maxChars);
        }

        HttpRequest.Builder b = HttpRequest.newBuilder(URI.create(url))
                .timeout(Duration.ofSeconds(aiConfig.getTimeoutSeconds() == null ? 120 : aiConfig.getTimeoutSeconds()))
                .header("Content-Type", "application/json; charset=utf-8")
                .header("Accept", stream ? "text/event-stream" : "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(payload, StandardCharsets.UTF_8));
        if (aiConfig.getApiKey() != null && !aiConfig.getApiKey().isEmpty()) {
            b.header("Authorization", "Bearer " + aiConfig.getApiKey());
        }
        if (logger.isDebugEnabled()) {
            logger.debug("AI POST {} payloadLen={} stream={}", url, payload.length(), stream);
        }
        return b.build();
    }

    /** 把字符串转成合法 JSON string 字面量（带双引号）。 */
    private static String jsonString(String s) {
        if (s == null) return "\"\"";
        StringBuilder sb = new StringBuilder(s.length() + 8);
        sb.append('"');
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
                    if (c < 0x20) {
                        sb.append(String.format("\\u%04x", (int) c));
                    } else {
                        sb.append(c);
                    }
            }
        }
        sb.append('"');
        return sb.toString();
    }

    /** 给调用方拿到默认 systemPrompt（避免到处取 aiConfig）。 */
    public String getDefaultSystemPrompt() {
        return aiConfig.getSystemPrompt();
    }

    public AiConfig getConfig() {
        return aiConfig;
    }

    /**
     * 极简 JSON content 提取器。
     * 在 json 中找到 anchor（"\"delta\"" 或 "\"message\""）的位置后，
     * 再向后找 "content":"...", 返回反转义后的字符串。
     * 找不到返回 ""。不引入 Jackson，避免 jackson-core 版本冲突。
     */
    static String extractContent(String json, String anchor) {
        if (json == null || json.isEmpty()) return "";
        int p = json.indexOf(anchor);
        if (p < 0) return "";
        int c = json.indexOf("\"content\"", p);
        if (c < 0) return "";
        int colon = json.indexOf(':', c);
        if (colon < 0) return "";
        // 跳过空白
        int i = colon + 1;
        while (i < json.length() && Character.isWhitespace(json.charAt(i))) i++;
        if (i >= json.length()) return "";
        // null
        if (json.charAt(i) == 'n') return "";
        if (json.charAt(i) != '"') return "";
        i++; // 跳过开头引号
        StringBuilder sb = new StringBuilder();
        while (i < json.length()) {
            char ch = json.charAt(i);
            if (ch == '\\') {
                if (i + 1 >= json.length()) break;
                char nx = json.charAt(i + 1);
                switch (nx) {
                    case '"':  sb.append('"');  i += 2; break;
                    case '\\': sb.append('\\'); i += 2; break;
                    case '/':  sb.append('/');  i += 2; break;
                    case 'n':  sb.append('\n'); i += 2; break;
                    case 'r':  sb.append('\r'); i += 2; break;
                    case 't':  sb.append('\t'); i += 2; break;
                    case 'b':  sb.append('\b'); i += 2; break;
                    case 'f':  sb.append('\f'); i += 2; break;
                    case 'u':
                        if (i + 5 < json.length()) {
                            try {
                                int code = Integer.parseInt(json.substring(i + 2, i + 6), 16);
                                sb.append((char) code);
                            } catch (NumberFormatException ex) { /* ignore */ }
                            i += 6;
                        } else {
                            i = json.length();
                        }
                        break;
                    default:
                        sb.append(nx);
                        i += 2;
                }
            } else if (ch == '"') {
                break;
            } else {
                sb.append(ch);
                i++;
            }
        }
        return sb.toString();
    }

    // ========================================================================
    // Layer 3: Tool Calling
    // ========================================================================

    /** 一次性收集到的 tool_call 项（流式 SSE 累加完成后产物）。 */
    public static class ToolCall {
        public String id;          // chatcmpl-tool-xxx
        public String name;        // 函数名
        public StringBuilder args; // 参数 JSON 字符串（多个 chunk 累加）
        public ToolCall() { this.args = new StringBuilder(); }
    }

    /**
     * 流式调用，支持 tool_calls。
     *
     * 行为：
     *  - 调用一次 LLM（stream=true）。
     *  - 边读 SSE：assistant.delta.content 推给 onDelta；assistant.delta.tool_calls 累加。
     *  - 流结束后返回 (assistant 文本, 工具调用列表)，由调用方决定是否再发一轮。
     *
     * @param messages   历史消息（含 system / user / 已发生的 assistant、tool）
     * @param toolsJson  形如 [{"type":"function","function":{...}}, ...] 的 JSON 字符串；为空字符串表示不带工具（普通流式）
     * @param onDelta    每次 content 增量回调
     * @return           本轮收尾时收集到的 tool_calls；若空则表示本轮无工具调用，可以结束
     */
    public List<ToolCall> chatStreamWithTools(List<Msg> messages, String toolsJson,
                                               Consumer<String> onDelta) throws Exception {
        HttpRequest req = buildRequestWithTools(messages, true, toolsJson);
        HttpResponse<InputStream> resp = httpClient().send(req, HttpResponse.BodyHandlers.ofInputStream());
        if (resp.statusCode() / 100 != 2) {
            StringBuilder err = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new InputStreamReader(resp.body(), StandardCharsets.UTF_8))) {
                String l; while ((l = br.readLine()) != null) err.append(l).append('\n');
            }
            throw new RuntimeException("LLM HTTP " + resp.statusCode() + ": " + err);
        }

        // index → ToolCall（OpenAI 协议里 tool_calls 是数组，每个 chunk 带 index 标识位置）
        Map<Integer, ToolCall> tcMap = new LinkedHashMap<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(resp.body(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.isEmpty()) continue;
                if (!line.startsWith("data:")) continue;
                String data = line.substring(5).trim();
                if ("[DONE]".equals(data)) break;
                // 普通文本片段
                String piece = extractContent(data, "\"delta\"");
                if (piece != null && !piece.isEmpty()) {
                    onDelta.accept(piece);
                }
                // 工具调用片段（可能多 chunk，需累加 arguments）
                parseToolCallChunk(data, tcMap);
            }
        }
        return new ArrayList<>(tcMap.values());
    }

    /** 把 tool_calls chunk 解析并合并进 map（index -> ToolCall）。 */
    private static void parseToolCallChunk(String json, Map<Integer, ToolCall> tcMap) {
        // 寻找 "tool_calls":[...]
        int p = json.indexOf("\"tool_calls\"");
        if (p < 0) return;
        int arrStart = json.indexOf('[', p);
        if (arrStart < 0) return;
        // 简单暴力地用 "index" 关键字定位每个 tool_call 元素
        int idx = arrStart;
        while (true) {
            int iIdx = json.indexOf("\"index\"", idx);
            if (iIdx < 0) break;
            int colon = json.indexOf(':', iIdx);
            int e1 = nextNumberEnd(json, colon + 1);
            int index;
            try { index = Integer.parseInt(json.substring(colon + 1, e1).trim()); }
            catch (Exception ex) { return; }
            // 该元素 JSON 边界：从 iIdx 往前找 '{'，往后找平衡 '}'
            int objStart = lastIndexOfBefore(json, '{', iIdx);
            int objEnd   = matchBrace(json, objStart);
            if (objStart < 0 || objEnd < 0) return;
            String elem = json.substring(objStart, objEnd + 1);

            ToolCall tc = tcMap.computeIfAbsent(index, k -> new ToolCall());
            // id
            String id = extractStringField(elem, "id");
            if (id != null && !id.isEmpty()) tc.id = id;
            // function.name
            int fp = elem.indexOf("\"function\"");
            if (fp >= 0) {
                String name = extractStringField(elem.substring(fp), "name");
                if (name != null && !name.isEmpty()) tc.name = name;
                String argsPiece = extractStringField(elem.substring(fp), "arguments");
                if (argsPiece != null) tc.args.append(argsPiece);
            }
            idx = objEnd + 1;
            if (idx >= json.length()) break;
        }
    }

    /** 提取字符串字段 "key":"...val..."；返回反转义后的 val；不存在返回 null。 */
    private static String extractStringField(String json, String key) {
        String anchor = "\"" + key + "\"";
        int p = json.indexOf(anchor);
        if (p < 0) return null;
        int colon = json.indexOf(':', p);
        if (colon < 0) return null;
        int i = colon + 1;
        while (i < json.length() && Character.isWhitespace(json.charAt(i))) i++;
        if (i >= json.length()) return null;
        if (json.charAt(i) == 'n') return "";
        if (json.charAt(i) != '"') return null;
        i++;
        StringBuilder sb = new StringBuilder();
        while (i < json.length()) {
            char c = json.charAt(i);
            if (c == '\\' && i + 1 < json.length()) {
                char nx = json.charAt(i + 1);
                switch (nx) {
                    case '"':  sb.append('"');  i += 2; break;
                    case '\\': sb.append('\\'); i += 2; break;
                    case '/':  sb.append('/');  i += 2; break;
                    case 'n':  sb.append('\n'); i += 2; break;
                    case 'r':  sb.append('\r'); i += 2; break;
                    case 't':  sb.append('\t'); i += 2; break;
                    case 'u':
                        if (i + 5 < json.length()) {
                            try { sb.append((char) Integer.parseInt(json.substring(i + 2, i + 6), 16)); }
                            catch (NumberFormatException ex) {}
                            i += 6;
                        } else i = json.length();
                        break;
                    default: sb.append(nx); i += 2;
                }
            } else if (c == '"') break;
            else { sb.append(c); i++; }
        }
        return sb.toString();
    }

    private static int nextNumberEnd(String s, int start) {
        int i = start;
        while (i < s.length() && Character.isWhitespace(s.charAt(i))) i++;
        while (i < s.length() && (Character.isDigit(s.charAt(i)) || s.charAt(i) == '-')) i++;
        return i;
    }
    private static int lastIndexOfBefore(String s, char c, int before) {
        for (int i = before; i >= 0; i--) if (s.charAt(i) == c) return i;
        return -1;
    }
    /** 从 s[start]='{' 开始找平衡 '}'，返回 '}' 下标；考虑字符串中的 { } 不计入。 */
    private static int matchBrace(String s, int start) {
        if (start < 0 || start >= s.length() || s.charAt(start) != '{') return -1;
        int depth = 0;
        boolean inStr = false;
        boolean esc = false;
        for (int i = start; i < s.length(); i++) {
            char c = s.charAt(i);
            if (inStr) {
                if (esc) { esc = false; }
                else if (c == '\\') esc = true;
                else if (c == '"')  inStr = false;
            } else {
                if (c == '"') inStr = true;
                else if (c == '{') depth++;
                else if (c == '}') {
                    depth--;
                    if (depth == 0) return i;
                }
            }
        }
        return -1;
    }

    /** 带 tools 的请求构造。 */
    private HttpRequest buildRequestWithTools(List<Msg> messages, boolean stream, String toolsJson) {
        if (!aiConfig.isEnabled()) {
            throw new IllegalStateException("AI 助手未启用");
        }
        String base = aiConfig.getBaseUrl();
        if (base == null || base.isEmpty()) throw new IllegalStateException("ai.baseUrl 未配置");
        if (base.endsWith("/")) base = base.substring(0, base.length() - 1);
        String url = base + "/chat/completions";

        StringBuilder json = new StringBuilder(512);
        json.append('{');
        json.append("\"model\":").append(jsonString(aiConfig.getModel())).append(',');
        json.append("\"temperature\":").append(aiConfig.getTemperature() == null ? 0.3 : aiConfig.getTemperature()).append(',');
        if (aiConfig.getMaxTokens() != null && aiConfig.getMaxTokens() > 0) {
            json.append("\"max_tokens\":").append(aiConfig.getMaxTokens()).append(',');
        }
        json.append("\"stream\":").append(stream).append(',');

        // tools 数组（如果非空）
        if (toolsJson != null && !toolsJson.isEmpty() && !"[]".equals(toolsJson)) {
            json.append("\"tools\":").append(toolsJson).append(',');
            json.append("\"tool_choice\":\"auto\",");
        }

        json.append("\"messages\":[");
        boolean first = true;
        for (Msg m : messages) {
            if (!first) json.append(',');
            // assistant 可能携带 tool_calls 字段；如果 m.content 以 ${TOOL_CALLS:} 开头则视为带 tool_calls 的占位 assistant 消息
            // tool 消息形如 role=tool / tool_call_id=xxx / content=
            json.append(serializeMessage(m));
            first = false;
        }
        json.append("]}");

        return HttpRequest.newBuilder(URI.create(url))
                .timeout(Duration.ofSeconds(aiConfig.getTimeoutSeconds() == null ? 120 : aiConfig.getTimeoutSeconds()))
                .header("Content-Type", "application/json; charset=utf-8")
                .header("Accept", stream ? "text/event-stream" : "application/json")
                .header(aiConfig.getApiKey() == null || aiConfig.getApiKey().isEmpty() ? "X-No-Auth" : "Authorization",
                        aiConfig.getApiKey() == null || aiConfig.getApiKey().isEmpty() ? "1" : ("Bearer " + aiConfig.getApiKey()))
                .POST(HttpRequest.BodyPublishers.ofString(json.toString(), StandardCharsets.UTF_8))
                .build();
    }

    /** 序列化一条消息：支持 role=system/user/assistant/tool，扩展元数据通过 ToolMsg 子类。 */
    private static String serializeMessage(Msg m) {
        if (m instanceof ToolResultMsg) {
            ToolResultMsg t = (ToolResultMsg) m;
            return "{\"role\":\"tool\",\"tool_call_id\":" + jsonString(t.toolCallId)
                    + ",\"content\":" + jsonString(t.content) + "}";
        }
        if (m instanceof AssistantWithToolCalls) {
            AssistantWithToolCalls a = (AssistantWithToolCalls) m;
            StringBuilder sb = new StringBuilder();
            sb.append("{\"role\":\"assistant\",\"content\":")
              .append(jsonString(a.content == null ? "" : a.content));
            if (a.toolCalls != null && !a.toolCalls.isEmpty()) {
                sb.append(",\"tool_calls\":[");
                boolean f = true;
                for (ToolCall tc : a.toolCalls) {
                    if (!f) sb.append(',');
                    sb.append("{\"id\":").append(jsonString(tc.id == null ? "" : tc.id))
                      .append(",\"type\":\"function\",\"function\":{\"name\":").append(jsonString(tc.name == null ? "" : tc.name))
                      .append(",\"arguments\":").append(jsonString(tc.args == null ? "" : tc.args.toString()))
                      .append("}}");
                    f = false;
                }
                sb.append("]");
            }
            sb.append('}');
            return sb.toString();
        }
        return "{\"role\":" + jsonString(m.role) + ",\"content\":" + jsonString(m.content == null ? "" : m.content) + "}";
    }

    /** tool 角色消息（携带 tool_call_id）。 */
    public static class ToolResultMsg extends Msg {
        public final String toolCallId;
        public ToolResultMsg(String toolCallId, String content) {
            super("tool", content);
            this.toolCallId = toolCallId;
        }
    }

    /** assistant 消息携带 tool_calls 字段（用于第二轮请求里的历史）。 */
    public static class AssistantWithToolCalls extends Msg {
        public final List<ToolCall> toolCalls;
        public AssistantWithToolCalls(String content, List<ToolCall> toolCalls) {
            super("assistant", content);
            this.toolCalls = toolCalls;
        }
    }
}
