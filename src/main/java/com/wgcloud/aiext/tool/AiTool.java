package com.wgcloud.aiext.tool;

import java.util.Map;

/**
 * AI 工具接口（二开 Layer 3）。
 *
 * 每个实现 = 一个可被 LLM 调用的 function。
 * 严格只读，禁止任何写库 / 发指令 / 调用 agent。
 */
public interface AiTool {

    /** 工具名，传给 LLM 用，必须是 [a-z_]+ 格式，不能与其他工具重名。 */
    String getName();

    /** 工具描述，会被 LLM 看到，要写清"何时该调用"。 */
    String getDescription();

    /**
     * JSON Schema (object 字面量片段，不带最外层 type/properties 标签)。
     * 例如：
     *   {
     *     "type":"object",
     *     "properties":{
     *       "city":{"type":"string","description":"城市名"}
     *     },
     *     "required":["city"]
     *   }
     * 我们要求实现直接返回完整 schema 字符串（带 type:object 外层），便于注册时拼到 tools 数组里。
     */
    String getParametersJsonSchema();

    /**
     * 执行工具。
     * @param args   LLM 解析后的参数 map（key 来自 schema 中 properties）
     * @return       工具输出，自由格式字符串（Markdown 推荐），用于回喂给 LLM
     */
    String execute(Map<String, Object> args) throws Exception;
}
