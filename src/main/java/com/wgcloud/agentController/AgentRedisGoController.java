/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.agentController;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.wgcloud.config.CommonConfig;
import com.wgcloud.entity.RedisMonitor;
import com.wgcloud.service.LogInfoService;
import com.wgcloud.service.RedisMonitorService;
import com.wgcloud.util.IpUtil;
import com.wgcloud.util.MessageErrorUtils;
import com.wgcloud.util.ResDataUtils;
import com.wgcloud.util.ThreadPoolUtil;
import com.wgcloud.util.TokenUtils;
import com.wgcloud.util.msg.WarnOtherUtil;
import java.util.Date;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value={"/agentRedisGo"})
public class AgentRedisGoController {
    private static final Logger logger = LoggerFactory.getLogger(AgentRedisGoController.class);
    @Resource
    private LogInfoService logInfoService;
    @Resource
    private RedisMonitorService redisMonitorService;
    @Autowired
    private MessageErrorUtils messageErrorUtils;
    @Autowired
    private TokenUtils tokenUtils;
    @Autowired
    private CommonConfig commonConfig;

    @ResponseBody
    @RequestMapping(value={"/minTask"})
    public String minTask(@RequestBody String paramBean, HttpServletRequest request) {
        logger.info("server-backup request Redis-------" + IpUtil.getIpAddr(request));
        JSONObject agentJsonObject = (JSONObject)JSONUtil.parse((Object)paramBean);
        boolean checkResult = this.tokenUtils.checkAgentToken(agentJsonObject);
        if (!checkResult) {
            logger.error("Token is error");
            return ResDataUtils.resetErrorJson("Token is error");
        }
        logger.debug("server-backup\u76d1\u63a7redis\u4e0a\u62a5\u6570\u636e-------------" + agentJsonObject.toString());
        Date nowtime = new Date();
        try {
            JSONArray dataJsonArr = agentJsonObject.getJSONArray("data");
            if (null != dataJsonArr) {
                for (Object obj : dataJsonArr) {
                    JSONObject redisJson = JSONUtil.parseObj(obj);
                    RedisMonitor redisMonitor = new RedisMonitor();
                    String redisName = redisJson.getStr("redisName");
                    redisMonitor.setRedisName(redisName);
                    String redisNodeInfo = redisJson.getStr("redisNodeInfo");
                    redisMonitor.setRedisNodeInfo(redisNodeInfo);
                    String code = redisJson.getStr("code");
                    if ("2".equals(code)) {
                        this.redisMonitorService.downByRedisName(redisName);
                        String testErrorMsg = redisJson.getStr("testErrorMsg");
                        if (!StringUtils.isEmpty((CharSequence)testErrorMsg)) {
                            this.messageErrorUtils.setErrorMsgHandler(redisName, testErrorMsg);
                        }
                        Runnable runnable = () -> WarnOtherUtil.sendMiddlewareInfo("Redis-" + redisName, true);
                        ThreadPoolUtil.executor.execute(runnable);
                        continue;
                    }
                    redisMonitor.setState(code);
                    redisMonitor.setCreateTime(nowtime);
                    String redis_version = redisJson.getStr("redis_version");
                    redisMonitor.setRedisVersion(redis_version);
                    String redis_mode = redisJson.getStr("redis_mode");
                    redisMonitor.setRedisMode(redis_mode);
                    String process_id = redisJson.getStr("process_id");
                    redisMonitor.setProcessId(process_id);
                    String tcp_port = redisJson.getStr("tcp_port");
                    redisMonitor.setTcpPort(tcp_port);
                    String uptime_in_days = redisJson.getStr("uptime_in_days");
                    redisMonitor.setUptimeInDays(uptime_in_days);
                    String executable = redisJson.getStr("executable");
                    redisMonitor.setExecutable(executable);
                    String config_file = redisJson.getStr("config_file");
                    redisMonitor.setConfigFile(config_file);
                    String connected_clients = redisJson.getStr("connected_clients");
                    redisMonitor.setConnectedClients(connected_clients);
                    String blocked_clients = redisJson.getStr("blocked_clients");
                    redisMonitor.setBlockedClients(blocked_clients);
                    String used_memory_human = redisJson.getStr("used_memory_human");
                    redisMonitor.setUsedMemoryHuman(used_memory_human);
                    String used_memory_peak_human = redisJson.getStr("used_memory_peak_human");
                    redisMonitor.setUsedMemoryPeakHuman(used_memory_peak_human);
                    String maxmemory_human = redisJson.getStr("maxmemory_human");
                    redisMonitor.setMaxMemoryHuman(maxmemory_human);
                    String aof_enabled = redisJson.getStr("aof_enabled");
                    if ("1".equals(aof_enabled)) {
                        redisMonitor.setAofEnabled("yes");
                    } else {
                        redisMonitor.setAofEnabled("no");
                    }
                    String rdb_last_bgsave_status = redisJson.getStr("rdb_last_bgsave_status");
                    redisMonitor.setRdbLastBgsaveStatus(rdb_last_bgsave_status);
                    String rdb_last_bgsave_time_sec = redisJson.getStr("rdb_last_bgsave_time_sec");
                    redisMonitor.setRdbLastBgsaveTimeSec(rdb_last_bgsave_time_sec);
                    String instantaneous_ops_per_sec = redisJson.getStr("instantaneous_ops_per_sec");
                    redisMonitor.setInstantaneousOpsPerSec(instantaneous_ops_per_sec);
                    String total_net_input_bytes = redisJson.getStr("total_net_input_bytes");
                    redisMonitor.setTotalNetInputBytes(total_net_input_bytes);
                    String total_net_output_bytes = redisJson.getStr("total_net_output_bytes");
                    redisMonitor.setTotalNetOutputBytes(total_net_output_bytes);
                    String rejected_connections = redisJson.getStr("rejected_connections");
                    redisMonitor.setRejectedConnections(rejected_connections);
                    String role = redisJson.getStr("role");
                    redisMonitor.setRedisRole(role);
                    String connected_slaves = redisJson.getStr("connected_slaves");
                    redisMonitor.setConnectedSlaves(connected_slaves);
                    String used_cpu_sys = redisJson.getStr("used_cpu_sys");
                    redisMonitor.setUsedCpuSys(used_cpu_sys);
                    String expired_keys = redisJson.getStr("expired_keys");
                    redisMonitor.setExpiredKeys(expired_keys);
                    String evicted_keys = redisJson.getStr("evicted_keys");
                    redisMonitor.setEvictedKeys(evicted_keys);
                    String keyspace_hits = redisJson.getStr("keyspace_hits");
                    redisMonitor.setKeyspaceHits(keyspace_hits);
                    String keyspace_misses = redisJson.getStr("keyspace_misses");
                    redisMonitor.setKeyspaceMisses(keyspace_misses);
                    String pubsub_channels = redisJson.getStr("pubsub_channels");
                    redisMonitor.setPubsubChannels(pubsub_channels);
                    String pubsub_patterns = redisJson.getStr("pubsub_patterns");
                    redisMonitor.setPubsubPatterns(pubsub_patterns);
                    String cluster_enabled = redisJson.getStr("cluster_enabled");
                    if ("1".equals(cluster_enabled)) {
                        redisMonitor.setClusterEnabled("yes");
                    } else {
                        redisMonitor.setClusterEnabled("no");
                    }
                    this.redisMonitorService.deleteByRedisName(redisName);
                    this.redisMonitorService.save(redisMonitor);
                }
            }
        }
        catch (Exception e) {
            logger.error("\u89e3\u6790server-backup\u76d1\u63a7redis\u4e0a\u62a5\u6570\u636e\u9519\u8bef", (Throwable)e);
            return ResDataUtils.resetErrorJson(e.toString());
        }
        return ResDataUtils.resetSuccessJson(null);
    }
}

