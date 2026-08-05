/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.controller;

import com.github.pagehelper.PageInfo;
import com.wgcloud.config.CommonConfig;
import com.wgcloud.entity.RedisMonitor;
import com.wgcloud.service.LogInfoService;
import com.wgcloud.service.RedisMonitorService;
import com.wgcloud.util.FormatUtil;
import com.wgcloud.util.MessageErrorUtils;
import com.wgcloud.util.PageUtil;
import com.wgcloud.util.TokenUtils;
import com.wgcloud.util.license.LicenseUtil;
import java.util.HashMap;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value={"/redisMonitor"})
public class RedisMonitorController {
    private static final Logger logger = LoggerFactory.getLogger(RedisMonitorController.class);
    @Resource
    private RedisMonitorService redisMonitorService;
    @Resource
    private LogInfoService logInfoService;
    @Autowired
    private MessageErrorUtils messageErrorUtils;
    @Autowired
    private CommonConfig commonConfig;
    @Autowired
    private TokenUtils tokenUtils;

    @RequestMapping(value={"list"})
    public String kafkaMonitorList(RedisMonitor redisMonitor, Model model, HttpServletRequest request) {
        HashMap<String, Object> params = new HashMap<String, Object>();
        try {
            LicenseUtil.maxLicense_10(model, request, redisMonitor);
            StringBuffer url = new StringBuffer();
            if (!StringUtils.isEmpty((CharSequence)redisMonitor.getState())) {
                params.put("state", redisMonitor.getState());
                url.append("&state=").append(redisMonitor.getState());
            }
            if (!StringUtils.isEmpty((CharSequence)redisMonitor.getRedisName())) {
                params.put("redisName", redisMonitor.getRedisName());
                url.append("&redisName=").append(redisMonitor.getRedisName());
            }
            PageInfo pageInfo = this.redisMonitorService.selectByParams(params, redisMonitor.getPage(), redisMonitor.getPageSize());
            PageUtil.initPageNumber(pageInfo, model);
            model.addAttribute("pageUrl", (Object)("/redisMonitor/list?1=1" + url.toString()));
            model.addAttribute("page", (Object)pageInfo);
            model.addAttribute("redisMonitor", (Object)redisMonitor);
        }
        catch (Exception e) {
            logger.error("\u67e5\u8be2redis\u76d1\u63a7\u4fe1\u606f\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("\u67e5\u8be2redis\u76d1\u63a7\u4fe1\u606f\u9519\u8bef", e.toString(), "2");
        }
        return "redis/list";
    }

    @RequestMapping(value={"view"})
    public String viewRedisMonitor(Model model, HttpServletRequest request) {
        String id = request.getParameter("id");
        RedisMonitor redisMonitor = new RedisMonitor();
        try {
            redisMonitor = this.redisMonitorService.selectById(id);
            String content = "";
            content = content + "Redis\u670d\u52a1\u5668\u7248\u672c: " + redisMonitor.getRedisVersion() + "</br>";
            content = content + "\u670d\u52a1\u5668\u7684\u6a21\u5f0f: " + redisMonitor.getRedisMode() + "</br>";
            content = content + "\u670d\u52a1\u5668\u8fdb\u7a0b\u7684PID: " + redisMonitor.getProcessId() + "</br>";
            content = content + "TCP/IP\u76d1\u542c\u7aef\u53e3: " + redisMonitor.getTcpPort() + "</br>";
            content = content + "\u81eaRedis\u670d\u52a1\u5668\u542f\u52a8\u4ee5\u6765,\u7ecf\u8fc7\u7684\u5929\u6570: " + redisMonitor.getUptimeInDays() + "</br>";
            content = content + "\u6267\u884c\u6587\u4ef6: " + redisMonitor.getExecutable() + "</br>";
            content = content + "\u914d\u7f6e\u6587\u4ef6\u8def\u5f84: " + redisMonitor.getConfigFile() + "</br>";
            content = content + "\u5df2\u8fde\u63a5\u5ba2\u6237\u7aef\u7684\u6570\u91cf: " + redisMonitor.getConnectedClients() + "</br>";
            content = content + "\u6b63\u5728\u7b49\u5f85\u963b\u585e\u547d\u4ee4(BLPOP\u3001BRPOP\u3001BRPOPLPUSH)\u7684\u5ba2\u6237\u7aef\u7684\u6570\u91cf: " + redisMonitor.getBlockedClients() + "</br>";
            content = content + "Redis\u5206\u914d\u7684\u5185\u5b58\u603b\u91cf: " + redisMonitor.getUsedMemoryHuman() + "</br>";
            content = content + "Redis\u7684\u5185\u5b58\u6d88\u8017\u5cf0\u503c: " + redisMonitor.getUsedMemoryPeakHuman() + "</br>";
            content = content + "Redis\u5b9e\u4f8b\u7684\u6700\u5927\u5185\u5b58\u914d\u7f6e: " + redisMonitor.getMaxMemoryHuman() + "</br>";
            content = content + "\u662f\u5426\u5f00\u542f\u4e86aof: " + redisMonitor.getAofEnabled() + "</br>";
            content = content + "\u6700\u8fd1\u4e00\u6b21rdb\u6301\u4e45\u5316\u662f\u5426\u6210\u529f: " + redisMonitor.getRdbLastBgsaveStatus() + "</br>";
            content = content + "\u6700\u8fd1\u4e00\u6b21\u6210\u529f\u751f\u6210rdb\u6587\u4ef6\u8017\u65f6\u79d2\u6570: " + redisMonitor.getRdbLastBgsaveTimeSec() + "</br>";
            content = content + "redis\u5f53\u524d\u7684qps,redis\u5185\u90e8\u8f83\u5b9e\u65f6\u7684\u6bcf\u79d2\u6267\u884c\u7684\u547d\u4ee4\u6570: " + redisMonitor.getInstantaneousOpsPerSec() + "</br>";
            content = content + "redis\u7f51\u7edc\u5165\u53e3\u6d41\u91cf\u5b57\u8282\u6570: " + FormatUtil.bytesFormatUnit(redisMonitor.getTotalNetInputBytes(), "byte") + "</br>";
            content = content + "redis\u7f51\u7edc\u51fa\u53e3\u6d41\u91cf\u5b57\u8282\u6570: " + FormatUtil.bytesFormatUnit(redisMonitor.getTotalNetOutputBytes(), "byte") + "</br>";
            content = content + "\u62d2\u7edd\u7684\u8fde\u63a5\u4e2a\u6570: " + redisMonitor.getRejectedConnections() + "</br>";
            content = content + "\u5b9e\u4f8b\u7684\u89d2\u8272: " + redisMonitor.getRedisRole() + "</br>";
            content = content + "\u8fde\u63a5\u7684slave\u5b9e\u4f8b\u4e2a\u6570: " + redisMonitor.getConnectedSlaves() + "</br>";
            content = content + "\u5c06\u6240\u6709redis\u4e3b\u8fdb\u7a0b\u5728\u6838\u5fc3\u6001\u6240\u5360\u7528\u7684CPU\u65f6\u6c42\u548c\u7d2f\u8ba1\u8d77\u6765: " + redisMonitor.getUsedCpuSys() + "</br>";
            content = content + "\u8fd0\u884c\u4ee5\u6765\u8fc7\u671f\u7684key\u7684\u6570\u91cf: " + redisMonitor.getExpiredKeys() + "</br>";
            content = content + "\u8fd0\u884c\u4ee5\u6765\u5254\u9664(\u8d85\u8fc7\u4e86maxmemory\u540e)\u7684key\u7684\u6570\u91cf: " + redisMonitor.getEvictedKeys() + "</br>";
            content = content + "\u547d\u4e2d\u6b21\u6570: " + redisMonitor.getKeyspaceHits() + "</br>";
            content = content + "\u6ca1\u547d\u4e2d\u6b21\u6570: " + redisMonitor.getKeyspaceMisses() + "</br>";
            content = content + "\u5f53\u524d\u4f7f\u7528\u4e2d\u7684\u9891\u9053\u6570\u91cf: " + redisMonitor.getPubsubChannels() + "</br>";
            content = content + "\u5f53\u524d\u4f7f\u7528\u7684\u6a21\u5f0f\u7684\u6570\u91cf: " + redisMonitor.getPubsubPatterns() + "</br>";
            content = content + "\u5b9e\u4f8b\u662f\u5426\u542f\u7528\u96c6\u7fa4\u6a21\u5f0f: " + redisMonitor.getClusterEnabled() + "</br>";
            model.addAttribute("redisMonitor", (Object)redisMonitor);
            model.addAttribute("redisMonitorContent", (Object)content);
            model.addAttribute("redisMonitorErrorMsg", (Object)this.messageErrorUtils.viewErrorMsgHandler(redisMonitor.getRedisName()));
        }
        catch (Exception e) {
            logger.error("\u67e5\u770bredis\u4fe1\u606f\u9519\u8bef", (Throwable)e);
        }
        return "redis/view";
    }

    @RequestMapping(value={"del"})
    public String delete(Model model, HttpServletRequest request) {
        String errorMsg = "\u5220\u9664Redis\u4fe1\u606f\u9519\u8bef";
        RedisMonitor redisMonitor = new RedisMonitor();
        try {
            if (!StringUtils.isEmpty((CharSequence)request.getParameter("id"))) {
                String[] ids;
                for (String id : ids = request.getParameter("id").split(",")) {
                    redisMonitor = this.redisMonitorService.selectById(id);
                    this.redisMonitorService.saveLog(request, "\u5220\u9664", redisMonitor);
                }
                this.redisMonitorService.deleteById(ids);
            }
        }
        catch (Exception e) {
            logger.error(errorMsg, (Throwable)e);
            this.logInfoService.save(errorMsg, e.toString(), "2");
        }
        return "redirect:/redisMonitor/list";
    }
}

