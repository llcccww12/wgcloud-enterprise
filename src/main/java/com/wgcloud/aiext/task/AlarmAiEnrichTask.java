package com.wgcloud.aiext.task;

import com.wgcloud.aiext.config.AiConfig;
import com.wgcloud.aiext.service.AiContextBuilder;
import com.wgcloud.aiext.util.AiClient;
import com.wgcloud.entity.LogInfo;
import com.wgcloud.service.LogInfoService;
import net.jodah.expiringmap.ExpiringMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 二开 Layer 2.3：告警自动 AI 摘要回写任务。
 *
 * 工作流程：
 *  1. 定时（默认每 30 秒）扫 LOG_INFO 表，挑最近 lookbackMinutes 内的 state=1/4 告警；
 *  2. 跳过已含 markerStart 的（已 enrich 过）；
 *  3. 用 hash(hostname + infoContent) 在 ExpiringMap 里去重（dedupMinutes 过期）；
 *  4. 每分钟最多调 AI perMinuteLimit 次（全局限流）；
 *  5. 通过 AiContextBuilder 拼上下文，调 AI 生成 5 行摘要；
 *  6. JdbcTemplate UPDATE LOG_INFO SET INFO_CONTENT = original + '\n\n[AI摘要]...[/AI摘要]' WHERE ID=?。
 *
 * 一定不调用 mapper.update（原项目没有），靠 JdbcTemplate 直接 SQL，避免改 jar 内的 mapper。
 */
@Component
public class AlarmAiEnrichTask {

    private static final Logger logger = LoggerFactory.getLogger(AlarmAiEnrichTask.class);

    private static final SimpleDateFormat TS_FMT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired private AiConfig aiConfig;
    @Autowired private AiClient aiClient;
    @Autowired private AiContextBuilder aiContextBuilder;
    @Autowired private LogInfoService logInfoService;
    @Autowired private JdbcTemplate jdbcTemplate;

    /** 同主机+同告警内容指纹的去重缓存。 */
    private ExpiringMap<String, Boolean> dedupCache;

    /** 当前分钟的全局调用计数器（用 currentMinuteKey 滚动）。 */
    private final AtomicInteger perMinuteCount = new AtomicInteger(0);
    private volatile String currentMinuteKey = "";

    private synchronized ExpiringMap<String, Boolean> dedupCache() {
        if (dedupCache == null) {
            int min = aiConfig.getEnrich() == null || aiConfig.getEnrich().getDedupMinutes() == null
                    ? 5 : aiConfig.getEnrich().getDedupMinutes();
            dedupCache = ExpiringMap.builder()
                    .expiration(min, TimeUnit.MINUTES)
                    .maxSize(2000)
                    .build();
        }
        return dedupCache;
    }

    /** 每 30 秒触发一次（用 fixedDelay 避免重叠执行）。 */
    @Scheduled(fixedDelayString = "#{ ${ai.enrich.scanIntervalSeconds:30} * 1000 }",
               initialDelay = 20000)
    public void scan() {
        if (!aiConfig.isEnabled() || aiConfig.getEnrich() == null || !aiConfig.getEnrich().isEnabled()) {
            return;
        }
        try {
            doScan();
        } catch (Exception e) {
            logger.error("AlarmAiEnrichTask scan fail", e);
        }
    }

    private void doScan() throws Exception {
        AiConfig.Enrich cfg = aiConfig.getEnrich();
        int lookback = nz(cfg.getLookbackMinutes(), 5);
        int batchLimit = nz(cfg.getBatchLimit(), 30);

        // 计算时间窗
        Calendar c = Calendar.getInstance();
        String end = TS_FMT.format(c.getTime());
        c.add(Calendar.MINUTE, -lookback);
        String start = TS_FMT.format(c.getTime());

        // 拉 state=1 和 state=4 两批告警（mapper 不支持 state IN，只能分两次）
        List<LogInfo> candidates = new ArrayList<>();
        loadByState(candidates, "1", start, end);
        loadByState(candidates, "4", start, end);

        if (candidates.isEmpty()) return;

        String marker = cfg.getMarkerStart();
        int processed = 0;
        for (LogInfo li : candidates) {
            if (processed >= batchLimit) break;
            if (li == null) continue;
            String content = li.getInfoContent() == null ? "" : li.getInfoContent();
            // 已经 enrich 过的跳过
            if (marker != null && content.contains(marker)) continue;

            // 去重 key
            String dedupKey = li.getHostname() + "|" + Integer.toHexString(content.hashCode());
            if (dedupCache().containsKey(dedupKey)) continue;

            // 限流
            if (!tryAcquireRate(nz(cfg.getPerMinuteLimit(), 20))) {
                logger.info("AlarmAiEnrich rate-limited (per-minute={})", cfg.getPerMinuteLimit());
                break;
            }

            try {
                enrichOne(li, cfg);
                dedupCache().put(dedupKey, Boolean.TRUE);
                processed++;
            } catch (Exception e) {
                logger.warn("enrichOne fail id={}", li.getId(), e);
            }
        }
        if (processed > 0) {
            logger.info("AlarmAiEnrich processed={} candidates={}", processed, candidates.size());
        }
    }

    private void loadByState(List<LogInfo> out, String state, String start, String end) {
        try {
            Map<String, Object> p = new HashMap<>();
            p.put("state", state);
            p.put("startTime", start);
            p.put("endTime", end);
            List<LogInfo> list = logInfoService.selectAllByParams(p);
            if (list != null) out.addAll(list);
        } catch (Exception e) {
            logger.warn("loadByState fail state={}", state, e);
        }
    }

    private void enrichOne(LogInfo li, AiConfig.Enrich cfg) throws Exception {
        String ctx = aiContextBuilder.buildAlarmContextFromEntity(li, true);
        if (ctx == null || ctx.isEmpty()) return;

        List<AiClient.Msg> msgs = new ArrayList<>(3);
        String sys = aiClient.getDefaultSystemPrompt();
        if (sys != null && !sys.isEmpty()) msgs.add(AiClient.Msg.system(sys));
        msgs.add(AiClient.Msg.user("以下是告警与主机上下文：\n```\n" + ctx + "\n```"));
        msgs.add(AiClient.Msg.user(cfg.getSummaryPrompt()));

        long t0 = System.currentTimeMillis();
        String answer = aiClient.chat(msgs);
        long cost = System.currentTimeMillis() - t0;

        if (answer == null || answer.trim().isEmpty()) return;

        // 拼成 marker 包裹的摘要块
        String block = "\n\n" + cfg.getMarkerStart()
                + "（" + TS_FMT.format(new Date()) + "，耗时 " + cost + "ms）\n"
                + answer.trim() + "\n" + cfg.getMarkerEnd();

        // UPDATE LOG_INFO
        String original = li.getInfoContent() == null ? "" : li.getInfoContent();
        String updated  = original + block;
        int rows = jdbcTemplate.update(
                "UPDATE LOG_INFO SET INFO_CONTENT = ? WHERE ID = ?",
                updated, li.getId());
        logger.info("AlarmAiEnrich id={} rows={} aLen={} cost={}ms",
                li.getId(), rows, answer.length(), cost);
    }

    /** 当前分钟的 token 桶：true=拿到 / false=超限。 */
    private synchronized boolean tryAcquireRate(int perMinuteLimit) {
        if (perMinuteLimit <= 0) return true;
        String key = nowMinuteKey();
        if (!key.equals(currentMinuteKey)) {
            currentMinuteKey = key;
            perMinuteCount.set(0);
        }
        if (perMinuteCount.get() >= perMinuteLimit) return false;
        perMinuteCount.incrementAndGet();
        return true;
    }

    private static String nowMinuteKey() {
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.YEAR) + "-" + c.get(Calendar.DAY_OF_YEAR) + "-"
                + c.get(Calendar.HOUR_OF_DAY) + "-" + c.get(Calendar.MINUTE);
    }

    private static int nz(Integer v, int def) {
        return v == null ? def : v;
    }
}
