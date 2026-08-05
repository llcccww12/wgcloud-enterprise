package com.wgcloud.aiext.controller;

import com.wgcloud.aiext.service.AiContextBuilder;
import com.wgcloud.aiext.tool.AiTool;
import com.wgcloud.aiext.tool.AiToolRegistry;
import com.wgcloud.aiext.util.AiClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * AI 聊天端点（二开 - Layer 1）。
 *
 * 提供两种调用方式：
 *  - POST /ai/chat            同步，返回完整字符串（向后兼容旧 /AIPage/analyze）
 *  - GET  /ai/chatStream      流式（同步线程 + chunked transfer + 自定义 SSE 文本协议），
 *                              避免依赖 servlet async（项目内 AuthRestFilter 未声明 asyncSupported）
 *
 * 自定义 SSE 文本协议（与浏览器原生 EventSource 完全兼容）：
 *   data: <token>
 *
 *   event: done
 *   data: <totalChars>
 *
 *   event: error
 *   data: <msg>
 */
@Controller
@RequestMapping("/ai")
public class AiChatController {

    private static final Logger logger = LoggerFactory.getLogger(AiChatController.class);

    @Autowired
    private AiClient aiClient;

    @Autowired
    private AiContextBuilder aiContextBuilder;

    @Autowired
    private AiToolRegistry aiToolRegistry;

    /** 渲染独立 AI 聊天页面（不依赖原 AIPage 的 aiAnalyzeScript 检查）。 */
    @GetMapping("/chatPage")
    public String chatPage(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        // 高亮侧边栏 i1 菜单项
        session.setAttribute("menuActive", "i1");
        return "ai/chat";
    }

    /** 同步聊天：表单 question + 可选 context（先发 system 再发 user）。 */
    @PostMapping("/chat")
    @ResponseBody
    public String chat(@RequestParam("question") String question,
                       @RequestParam(value = "context", required = false) String context) {
        if (question == null || question.trim().isEmpty()) {
            return "请输入问题";
        }
        try {
            List<AiClient.Msg> msgs = buildMessages(question, context);
            String answer = aiClient.chat(msgs);
            logger.info("AI chat ok qLen={} aLen={}", question.length(), answer.length());
            return answer;
        } catch (Exception e) {
            logger.error("AI chat fail", e);
            return "AI 调用失败：" + e.getMessage();
        }
    }

    /** 同步线程的 SSE 流：用 chunked + flush 推送，浏览器 EventSource 可直接解析。 */
    @GetMapping(value = "/chatStream", produces = "text/event-stream;charset=UTF-8")
    public void chatStream(@RequestParam("question") String question,
                            @RequestParam(value = "context", required = false) String context,
                            HttpServletResponse response) {
        response.setContentType("text/event-stream;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Cache-Control", "no-cache, no-transform");
        response.setHeader("X-Accel-Buffering", "no");   // 关闭 nginx 缓冲
        response.setHeader("Connection", "keep-alive");

        PrintWriter w;
        try {
            w = response.getWriter();
        } catch (Exception e) {
            logger.error("AI stream getWriter fail", e);
            return;
        }

        if (question == null || question.trim().isEmpty()) {
            writeEvent(w, "error", "请输入问题");
            return;
        }

        try {
            List<AiClient.Msg> msgs = buildMessages(question, context);
            int chars = aiClient.chatStream(msgs, piece -> {
                writeEvent(w, null, piece);
                w.flush();
            });
            writeEvent(w, "done", String.valueOf(chars));
            w.flush();
            logger.info("AI stream ok qLen={} aLen={}", question.length(), chars);
        } catch (Exception e) {
            logger.error("AI stream fail", e);
            writeEvent(w, "error", "AI 调用失败：" + safeOneLine(e.getMessage()));
            try { w.flush(); } catch (Exception ignore) {}
        }
    }

    /** AI 状态自检接口，便于前端/排查时确认配置。 */
    @GetMapping("/ping")
    @ResponseBody
    public String ping() {
        try {
            return "enabled=" + aiClient.getConfig().isEnabled()
                    + ", model=" + aiClient.getConfig().getModel()
                    + ", baseUrl=" + aiClient.getConfig().getBaseUrl();
        } catch (Exception e) {
            return "AI ping fail: " + e.getMessage();
        }
    }

    /**
     * 把一段（可能多行）数据按 SSE 协议写出来。
     * SSE 协议要求 data 字段如果含换行需要拆成多个 data: 行。
     */
    private static void writeEvent(PrintWriter w, String eventName, String data) {
        if (eventName != null && !eventName.isEmpty()) {
            w.print("event: ");
            w.print(eventName);
            w.print('\n');
        }
        if (data == null) data = "";
        // 按行拆，每行一个 "data: " 前缀
        int from = 0;
        for (int i = 0; i < data.length(); i++) {
            char c = data.charAt(i);
            if (c == '\n') {
                w.print("data: ");
                w.write(data, from, i - from);
                w.print('\n');
                from = i + 1;
            }
        }
        w.print("data: ");
        w.write(data, from, data.length() - from);
        w.print("\n\n");
    }

    private static String safeOneLine(String s) {
        if (s == null) return "";
        return s.replace('\n', ' ').replace('\r', ' ');
    }

    private List<AiClient.Msg> buildMessages(String question, String context) {
        List<AiClient.Msg> msgs = new ArrayList<>(3);
        String sys = aiClient.getDefaultSystemPrompt();
        if (sys != null && !sys.isEmpty()) {
            msgs.add(AiClient.Msg.system(sys));
        }
        if (context != null && !context.trim().isEmpty()) {
            msgs.add(AiClient.Msg.user("以下是参考上下文，仅供你回答时使用：\n```\n" + context + "\n```"));
        }
        msgs.add(AiClient.Msg.user(question));
        return msgs;
    }

    // ====================================================================
    // Layer 2.1: 主机 AI 诊断
    // ====================================================================

    /** 诊断 prompt — 用户消息层（叠加到默认 systemPrompt 之后）。 */
    private static final String HOST_DIAGNOSE_PROMPT =
            "请基于上面的监控数据，对这台主机的健康状况做诊断，要求：\n" +
            "1. 先给出整体健康评分（0-100）和一句话结论；\n" +
            "2. 列出 \"可疑指标\" — 哪些数据偏离正常范围，分析可能原因；\n" +
            "3. 列出 \"建议排查动作\" — 给出可在 Linux/Windows 执行的具体命令；\n" +
            "4. 若没有可疑问题，明确说明 \"未发现异常\"；\n" +
            "5. 全程使用中文，结构化输出（带小标题与列表）。";

    /** 同步：主机 AI 诊断，返回完整 Markdown 答复。 */
    @PostMapping("/diagnoseHost")
    @ResponseBody
    public String diagnoseHost(@RequestParam("hostId") String hostId) {
        if (hostId == null || hostId.trim().isEmpty()) {
            return "缺少 hostId 参数";
        }
        String ctx;
        try {
            ctx = aiContextBuilder.buildHostContext(hostId);
        } catch (Exception e) {
            logger.error("diagnoseHost build context fail hostId={}", hostId, e);
            return "构建主机上下文失败：" + e.getMessage();
        }
        if (ctx == null) return "未找到主机 (hostId=" + hostId + ")";

        try {
            List<AiClient.Msg> msgs = buildMessages(HOST_DIAGNOSE_PROMPT, ctx);
            String answer = aiClient.chat(msgs);
            logger.info("AI diagnoseHost ok hostId={} aLen={}", hostId, answer.length());
            return answer;
        } catch (Exception e) {
            logger.error("AI diagnoseHost fail hostId={}", hostId, e);
            return "AI 调用失败：" + e.getMessage();
        }
    }

    /** 流式：主机 AI 诊断，浏览器 EventSource 实时接收。 */
    @GetMapping(value = "/diagnoseHostStream", produces = "text/event-stream;charset=UTF-8")
    public void diagnoseHostStream(@RequestParam("hostId") String hostId,
                                    HttpServletResponse response) {
        response.setContentType("text/event-stream;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Cache-Control", "no-cache, no-transform");
        response.setHeader("X-Accel-Buffering", "no");
        response.setHeader("Connection", "keep-alive");

        PrintWriter w;
        try {
            w = response.getWriter();
        } catch (Exception e) {
            logger.error("diagnoseHostStream getWriter fail", e);
            return;
        }

        if (hostId == null || hostId.trim().isEmpty()) {
            writeEvent(w, "error", "缺少 hostId 参数");
            return;
        }

        String ctx;
        try {
            ctx = aiContextBuilder.buildHostContext(hostId);
        } catch (Exception e) {
            logger.error("diagnoseHostStream build context fail hostId={}", hostId, e);
            writeEvent(w, "error", "构建主机上下文失败：" + safeOneLine(e.getMessage()));
            return;
        }
        if (ctx == null) {
            writeEvent(w, "error", "未找到主机 (hostId=" + hostId + ")");
            return;
        }

        // 先把构建好的上下文摘要推给前端（让用户看到 AI 看的是什么）
        writeEvent(w, "context", "已采集上下文 " + ctx.length() + " 字符，发送给 AI 分析中...");
        try { w.flush(); } catch (Exception ignore) {}

        try {
            List<AiClient.Msg> msgs = buildMessages(HOST_DIAGNOSE_PROMPT, ctx);
            int chars = aiClient.chatStream(msgs, piece -> {
                writeEvent(w, null, piece);
                w.flush();
            });
            writeEvent(w, "done", String.valueOf(chars));
            w.flush();
            logger.info("AI diagnoseHost stream ok hostId={} ctxLen={} aLen={}", hostId, ctx.length(), chars);
        } catch (Exception e) {
            logger.error("AI diagnoseHost stream fail hostId={}", hostId, e);
            writeEvent(w, "error", "AI 调用失败：" + safeOneLine(e.getMessage()));
            try { w.flush(); } catch (Exception ignore) {}
        }
    }

    // ====================================================================
    // Layer 2.2: 告警 AI 分析
    // ====================================================================

    private static final String ALARM_ANALYZE_PROMPT =
            "请基于上面的告警与主机上下文，进行根因分析：\n" +
            "1. 一句话定性：这是什么类型的问题（性能 / 资源 / 网络 / 进程 / 配置 / 误报）？严重程度 1-5。\n" +
            "2. 列出 3-5 个最可能的根因，按可能性从高到低排序，每个简述判断依据。\n" +
            "3. 给出可立即执行的排查命令清单（Linux/macOS/Windows 按主机平台选择）。\n" +
            "4. 给出短期缓解方案 与 长期根治方案。\n" +
            "5. 全程中文，结构化（小标题 + 列表）。若信息不足以判断，明确说明缺什么数据。";

    /** 同步：告警 AI 分析。 */
    @PostMapping("/analyzeAlarm")
    @ResponseBody
    public String analyzeAlarm(@RequestParam("logId") String logId) {
        if (logId == null || logId.trim().isEmpty()) return "缺少 logId 参数";
        String ctx;
        try {
            ctx = aiContextBuilder.buildAlarmContext(logId);
        } catch (Exception e) {
            logger.error("analyzeAlarm build context fail logId={}", logId, e);
            return "构建告警上下文失败：" + e.getMessage();
        }
        if (ctx == null) return "未找到告警 (logId=" + logId + ")";

        try {
            List<AiClient.Msg> msgs = buildMessages(ALARM_ANALYZE_PROMPT, ctx);
            String answer = aiClient.chat(msgs);
            logger.info("AI analyzeAlarm ok logId={} aLen={}", logId, answer.length());
            return answer;
        } catch (Exception e) {
            logger.error("AI analyzeAlarm fail logId={}", logId, e);
            return "AI 调用失败：" + e.getMessage();
        }
    }

    /** 流式：告警 AI 分析。 */
    @GetMapping(value = "/analyzeAlarmStream", produces = "text/event-stream;charset=UTF-8")
    public void analyzeAlarmStream(@RequestParam("logId") String logId,
                                    HttpServletResponse response) {
        response.setContentType("text/event-stream;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Cache-Control", "no-cache, no-transform");
        response.setHeader("X-Accel-Buffering", "no");
        response.setHeader("Connection", "keep-alive");

        PrintWriter w;
        try { w = response.getWriter(); }
        catch (Exception e) { logger.error("analyzeAlarmStream getWriter fail", e); return; }

        if (logId == null || logId.trim().isEmpty()) {
            writeEvent(w, "error", "缺少 logId 参数");
            return;
        }
        String ctx;
        try {
            ctx = aiContextBuilder.buildAlarmContext(logId);
        } catch (Exception e) {
            logger.error("analyzeAlarmStream build ctx fail logId={}", logId, e);
            writeEvent(w, "error", "构建告警上下文失败：" + safeOneLine(e.getMessage()));
            return;
        }
        if (ctx == null) {
            writeEvent(w, "error", "未找到告警 (logId=" + logId + ")");
            return;
        }
        writeEvent(w, "context", "已采集上下文 " + ctx.length() + " 字符，发送给 AI 分析中...");
        try { w.flush(); } catch (Exception ignore) {}

        try {
            List<AiClient.Msg> msgs = buildMessages(ALARM_ANALYZE_PROMPT, ctx);
            int chars = aiClient.chatStream(msgs, piece -> {
                writeEvent(w, null, piece);
                w.flush();
            });
            writeEvent(w, "done", String.valueOf(chars));
            w.flush();
            logger.info("AI analyzeAlarm stream ok logId={} ctxLen={} aLen={}", logId, ctx.length(), chars);
        } catch (Exception e) {
            logger.error("AI analyzeAlarm stream fail logId={}", logId, e);
            writeEvent(w, "error", "AI 调用失败：" + safeOneLine(e.getMessage()));
            try { w.flush(); } catch (Exception ignore) {}
        }
    }

    // ====================================================================
    // Layer 3: Function Calling 自然语言查询
    // ====================================================================

    /** 多轮 tool calling 上限。 */
    private static final int MAX_TOOL_ROUNDS = 5;

    private static final String TOOLS_SYSTEM_PROMPT =
            "你是 cac 智能运维监控平台的查询助手。当你需要数据时，请调用可用工具。当前提供的工具：\n" +
            "- list_hosts(nameLike, stateFilter, limit) — 列主机清单\n" +
            "- get_host_metrics(hostname) — 单机详细快照（含告警/进程）\n" +
            "- top_hosts_by_metric(metric=cpu|mem|disk|load, n, onlineOnly) — Top N 主机\n" +
            "- list_alarms(hostnameLike, hours, stateFilter, limit) — 告警查询\n" +
            "- list_processes(hostname, cpuMin, limit) — 进程/异常进程查询\n" +
            "- list_disk_usage(hostname, minUsePer, sortByUsage, limit) — 磁盘分区使用率明细\n" +
            "- list_disk_history(hostname, hours, limit) — 磁盘汇总使用率历史趋势\n" +
            "- list_disk_io(hostname, hours, limit) — 磁盘 IO 速率\n" +
            "- list_ping_results(hostname, onlyTimeout, limit) — PING 监测\n" +
            "- list_mac_info(hostname, limit) — 主机 MAC 地址\n" +
            "- list_cpu_temperatures(hostname, minTempC, limit) — CPU 温度\n" +
            "所有工具只读、可安全调用。" +
            "拿到工具结果后，请用中文给出简洁、结构化的答案；如果数据已经是表格，直接展示，不要重复抄一遍。" +
            "若用户问题不需要数据查询，直接用普通文字回答。";

    /** 流式 + 工具调用聊天端点。 */
    @GetMapping(value = "/chatWithToolsStream", produces = "text/event-stream;charset=UTF-8")
    public void chatWithToolsStream(@RequestParam("question") String question,
                                     HttpServletResponse response) {
        response.setContentType("text/event-stream;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Cache-Control", "no-cache, no-transform");
        response.setHeader("X-Accel-Buffering", "no");
        response.setHeader("Connection", "keep-alive");

        PrintWriter w;
        try { w = response.getWriter(); }
        catch (Exception e) { logger.error("chatWithTools getWriter fail", e); return; }

        if (question == null || question.trim().isEmpty()) {
            writeEvent(w, "error", "请输入问题");
            return;
        }

        String toolsJson = aiToolRegistry.buildToolsJsonArray();
        writeEvent(w, "tools", "可用工具 " + aiToolRegistry.all().size() + " 个：" + String.join(", ", aiToolRegistry.all().keySet()));
        try { w.flush(); } catch (Exception ignore) {}

        // 构造初始消息
        List<AiClient.Msg> msgs = new ArrayList<>();
        msgs.add(AiClient.Msg.system(TOOLS_SYSTEM_PROMPT));
        msgs.add(AiClient.Msg.user(question));

        long startedAt = System.currentTimeMillis();
        int totalChars = 0;
        int round = 0;
        try {
            while (round < MAX_TOOL_ROUNDS) {
                round++;
                final int currentRound = round;
                StringBuilder roundText = new StringBuilder();
                List<AiClient.ToolCall> calls = aiClient.chatStreamWithTools(msgs, toolsJson, piece -> {
                    try {
                        writeEvent(w, null, piece);
                        w.flush();
                    } catch (Exception ignore) {}
                    roundText.append(piece);
                });
                totalChars += roundText.length();

                if (calls == null || calls.isEmpty()) {
                    // 没有工具调用了，结束
                    break;
                }

                // 把 assistant + tool_calls 放回 messages
                msgs.add(new AiClient.AssistantWithToolCalls(roundText.toString(), calls));

                // 执行每个工具
                for (AiClient.ToolCall tc : calls) {
                    String toolName = tc.name == null ? "" : tc.name;
                    String toolArgs = tc.args == null ? "" : tc.args.toString();
                    writeEvent(w, "tool_call", "[第 " + currentRound + " 轮] 调用 " + toolName + "(" + toolArgs + ")");
                    try { w.flush(); } catch (Exception ignore) {}

                    String result;
                    AiTool tool = aiToolRegistry.find(toolName);
                    if (tool == null) {
                        result = "ERROR: 未知工具 " + toolName;
                    } else {
                        try {
                            Map<String, Object> argMap = parseJsonObject(toolArgs);
                            result = tool.execute(argMap);
                            if (result == null) result = "(空结果)";
                        } catch (Exception ex) {
                            logger.error("tool exec fail name={} args={}", toolName, toolArgs, ex);
                            result = "ERROR: 工具执行失败 " + ex.getMessage();
                        }
                    }
                    // 把工具结果作为 role=tool 消息放回，等下一轮 LLM 继续
                    msgs.add(new AiClient.ToolResultMsg(tc.id == null ? "" : tc.id, result));
                    writeEvent(w, "tool_result", "[第 " + currentRound + " 轮] " + toolName + " 返回 " + result.length() + " 字符");
                    try { w.flush(); } catch (Exception ignore) {}
                }
                // 进入下一轮
            }

            long cost = System.currentTimeMillis() - startedAt;
            writeEvent(w, "done", "rounds=" + round + " chars=" + totalChars + " cost_ms=" + cost);
            w.flush();
            logger.info("AI chatWithTools ok qLen={} rounds={} aLen={} cost={}ms",
                    question.length(), round, totalChars, cost);
        } catch (Exception e) {
            logger.error("AI chatWithTools fail", e);
            writeEvent(w, "error", "AI 调用失败：" + safeOneLine(e.getMessage()));
            try { w.flush(); } catch (Exception ignore) {}
        }
    }

    /** 极简 JSON 对象 → Map<String,Object>。仅支持顶层 string/number/boolean，不支持嵌套。 */
    private static Map<String, Object> parseJsonObject(String json) {
        Map<String, Object> map = new HashMap<>();
        if (json == null) return map;
        json = json.trim();
        if (json.isEmpty() || !json.startsWith("{") || !json.endsWith("}")) return map;
        // 解析 key
        int i = 1;
        while (i < json.length() - 1) {
            while (i < json.length() && Character.isWhitespace(json.charAt(i))) i++;
            if (i >= json.length() || json.charAt(i) == '}') break;
            if (json.charAt(i) != '"') { i++; continue; }
            // key
            int keyEnd = i + 1;
            StringBuilder k = new StringBuilder();
            while (keyEnd < json.length() && json.charAt(keyEnd) != '"') {
                if (json.charAt(keyEnd) == '\\' && keyEnd + 1 < json.length()) {
                    k.append(json.charAt(keyEnd + 1));
                    keyEnd += 2;
                } else {
                    k.append(json.charAt(keyEnd));
                    keyEnd++;
                }
            }
            i = keyEnd + 1;
            // ':'
            while (i < json.length() && (Character.isWhitespace(json.charAt(i)) || json.charAt(i) == ':')) i++;
            if (i >= json.length()) break;
            // value
            char vc = json.charAt(i);
            Object val;
            if (vc == '"') {
                StringBuilder v = new StringBuilder();
                i++;
                while (i < json.length() && json.charAt(i) != '"') {
                    if (json.charAt(i) == '\\' && i + 1 < json.length()) {
                        v.append(json.charAt(i + 1));
                        i += 2;
                    } else { v.append(json.charAt(i)); i++; }
                }
                i++;
                val = v.toString();
            } else if (vc == 't' || vc == 'f') {
                boolean b = vc == 't';
                while (i < json.length() && Character.isLetter(json.charAt(i))) i++;
                val = b;
            } else if (vc == 'n') {
                while (i < json.length() && Character.isLetter(json.charAt(i))) i++;
                val = null;
            } else if (Character.isDigit(vc) || vc == '-' || vc == '.') {
                int s = i;
                while (i < json.length() && (Character.isDigit(json.charAt(i)) || json.charAt(i) == '.' || json.charAt(i) == '-' || json.charAt(i) == 'e' || json.charAt(i) == 'E' || json.charAt(i) == '+')) i++;
                String num = json.substring(s, i);
                try {
                    if (num.contains(".") || num.contains("e") || num.contains("E")) val = Double.parseDouble(num);
                    else val = Long.parseLong(num);
                } catch (Exception ex) { val = num; }
            } else {
                // 数组/对象暂不支持，跳过到逗号或 '}'
                int depth = 0;
                int s = i;
                while (i < json.length()) {
                    char c = json.charAt(i);
                    if (c == '[' || c == '{') depth++;
                    else if (c == ']' || c == '}') {
                        if (depth == 0) break;
                        depth--;
                    } else if (c == ',' && depth == 0) break;
                    i++;
                }
                val = json.substring(s, i);
            }
            map.put(k.toString(), val);
            // 跳过 ',' 或空格
            while (i < json.length() && (Character.isWhitespace(json.charAt(i)) || json.charAt(i) == ',')) i++;
        }
        return map;
    }
}
