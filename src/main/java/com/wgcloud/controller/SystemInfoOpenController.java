/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.github.pagehelper.PageInfo;
import com.wgcloud.config.CommonConfig;
import com.wgcloud.config.LevelConfig;
import com.wgcloud.dto.NetworkInfoDto;
import com.wgcloud.entity.CpuTemperatures;
import com.wgcloud.entity.DiskIo;
import com.wgcloud.entity.DiskIoState;
import com.wgcloud.entity.DiskSmart;
import com.wgcloud.entity.DiskState;
import com.wgcloud.entity.GpuState;
import com.wgcloud.entity.HostDiskPer;
import com.wgcloud.entity.HostGroup;
import com.wgcloud.entity.HostMacInfo;
import com.wgcloud.entity.HostPciInfo;
import com.wgcloud.entity.KafkaMonitor;
import com.wgcloud.entity.RedisMonitor;
import com.wgcloud.entity.SystemInfo;
import com.wgcloud.service.AgentRunStateService;
import com.wgcloud.service.CpuStateService;
import com.wgcloud.service.CpuTemperaturesService;
import com.wgcloud.service.DiskIoService;
import com.wgcloud.service.DiskIoStateService;
import com.wgcloud.service.DiskSmartService;
import com.wgcloud.service.DiskStateService;
import com.wgcloud.service.GpuStateService;
import com.wgcloud.service.HostDiskPerService;
import com.wgcloud.service.HostGroupService;
import com.wgcloud.service.HostMacInfoService;
import com.wgcloud.service.HostPciInfoService;
import com.wgcloud.service.KafkaMonitorService;
import com.wgcloud.service.LogInfoService;
import com.wgcloud.service.MemStateService;
import com.wgcloud.service.NetIoStateService;
import com.wgcloud.service.RedisMonitorService;
import com.wgcloud.service.SysLoadStateService;
import com.wgcloud.service.SystemInfoService;
import com.wgcloud.util.ActivemqUtil;
import com.wgcloud.util.RabbitmqUtil;
import com.wgcloud.util.ResDataUtils;
import com.wgcloud.util.ScanIPUtil;
import com.wgcloud.util.TokenUtils;
import com.wgcloud.util.TomcatUtil;
import com.wgcloud.util.msg.WarnOtherUtil;
import com.wgcloud.util.redis.RedisDataUtil;
import com.wgcloud.util.staticvar.StaticKeys;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value={"/systemInfoOpen"})
public class SystemInfoOpenController {
    private static final Logger logger = LoggerFactory.getLogger(SystemInfoOpenController.class);
    @Resource
    private LogInfoService logInfoService;
    @Resource
    private CpuStateService cpuStateService;
    @Resource
    private DiskStateService diskStateService;
    @Resource
    private DiskSmartService diskSmartService;
    @Resource
    private DiskIoService diskIoService;
    @Autowired
    private DiskIoStateService diskIoStateService;
    @Autowired
    private GpuStateService gpuStateService;
    @Resource
    private CpuTemperaturesService cpuTemperaturesService;
    @Resource
    private MemStateService memStateService;
    @Autowired
    private HostMacInfoService hostMacInfoService;
    @Resource
    private NetIoStateService netIoStateService;
    @Resource
    private SysLoadStateService sysLoadStateService;
    @Resource
    private SystemInfoService systemInfoService;
    @Resource
    private HostGroupService hostGroupService;
    @Resource
    private RedisMonitorService redisMonitorService;
    @Resource
    private KafkaMonitorService kafkaMonitorService;
    @Resource
    private HostDiskPerService hostDiskPerService;
    @Resource
    private AgentRunStateService agentRunStateService;
    @Autowired
    private HostPciInfoService hostPciInfoService;
    @Autowired
    private CommonConfig commonConfig;
    @Autowired
    private LevelConfig levelConfig;
    @Autowired
    private TokenUtils tokenUtils;

    private void testThread() {
        new Thread(() -> logger.info("\u542f\u52a8\u5b50\u7ebf\u7a0b\u6d4b\u8bd5")).start();
    }

    @ResponseBody
    @RequestMapping(value={"getHostActive"})
    public String getHostActive(@RequestBody String paramBean) {
        JSONObject agentJsonObject = (JSONObject)JSONUtil.parse((Object)paramBean);
        if (!this.tokenUtils.checkAgentToken(agentJsonObject)) {
            logger.error("Token is error");
            return ResDataUtils.resetErrorJson("Token is error");
        }
        HashMap<String, Object> params = new HashMap<String, Object>();
        if (null == agentJsonObject.get("hostname") || StringUtils.isEmpty((CharSequence)agentJsonObject.get("hostname").toString())) {
            return "1";
        }
        params.put("hostnameForAgent", agentJsonObject.get("hostname").toString());
        try {
            List<SystemInfo> list = this.systemInfoService.selectAllByParams(params);
            if (!CollectionUtil.isEmpty(list)) {
                SystemInfo systemInfo = list.get(0);
                if (StringUtils.isEmpty((CharSequence)systemInfo.getActive())) {
                    return "1";
                }
                return systemInfo.getActive();
            }
        }
        catch (Exception e) {
            logger.error("agent\u67e5\u8be2\u6839\u636eip\u67e5\u8be2\u4e3b\u673a\u76d1\u63a7\u72b6\u6001\u9519\u8bef", (Throwable)e);
        }
        return "1";
    }

    @ResponseBody
    @RequestMapping(value={"agentDiskSmartList"})
    public String agentDiskSmartList(@RequestBody String paramBean) {
        JSONObject agentJsonObject = (JSONObject)JSONUtil.parse((Object)paramBean);
        String checkResult = this.tokenUtils.preOpenDataAPICheck(agentJsonObject);
        if (!StringUtils.isEmpty((CharSequence)checkResult)) {
            return checkResult;
        }
        HashMap<String, Object> params = new HashMap<String, Object>();
        if (null == agentJsonObject.get("hostname") || StringUtils.isEmpty((CharSequence)agentJsonObject.get("hostname").toString())) {
            return ResDataUtils.resetErrorJson("Missing require parameters");
        }
        params.put("hostname", agentJsonObject.get("hostname").toString());
        try {
            List<DiskSmart> diskSmarts = this.diskSmartService.selectAllByParams(params);
            return ResDataUtils.resetSuccessJson(diskSmarts);
        }
        catch (Exception e) {
            logger.error("\u6570\u636e\u63a5\u53e3\u67e5\u8be2\u6839\u636eip\u67e5\u8be2\u78c1\u76d8smart\u5217\u8868\u9519\u8bef", (Throwable)e);
            return ResDataUtils.resetErrorJson(e.toString());
        }
    }

    @ResponseBody
    @RequestMapping(value={"agentHostMacInfoList"})
    public String agentHostMacInfoList(@RequestBody String paramBean) {
        JSONObject agentJsonObject = (JSONObject)JSONUtil.parse((Object)paramBean);
        String checkResult = this.tokenUtils.preOpenDataAPICheck(agentJsonObject);
        if (!StringUtils.isEmpty((CharSequence)checkResult)) {
            return checkResult;
        }
        HashMap<String, Object> params = new HashMap<String, Object>();
        if (null == agentJsonObject.get("hostname") || StringUtils.isEmpty((CharSequence)agentJsonObject.get("hostname").toString())) {
            return ResDataUtils.resetErrorJson("Missing require parameters");
        }
        params.put("hostname", agentJsonObject.get("hostname").toString());
        try {
            List<HostMacInfo> hostMacInfoList = this.hostMacInfoService.selectAllByParams(params);
            return ResDataUtils.resetSuccessJson(hostMacInfoList);
        }
        catch (Exception e) {
            logger.error("\u6570\u636e\u63a5\u53e3\u67e5\u8be2\u6839\u636eip\u67e5\u8be2mac\u5730\u5740\u5217\u8868\u9519\u8bef", (Throwable)e);
            return ResDataUtils.resetErrorJson(e.toString());
        }
    }

    @ResponseBody
    @RequestMapping(value={"agentHostNetworkList"})
    public String agentHostNetworkList(@RequestBody String paramBean) {
        JSONObject agentJsonObject = (JSONObject)JSONUtil.parse((Object)paramBean);
        String checkResult = this.tokenUtils.preOpenDataAPICheck(agentJsonObject);
        if (!StringUtils.isEmpty((CharSequence)checkResult)) {
            return checkResult;
        }
        HashMap params = new HashMap();
        if (null == agentJsonObject.get("hostname") || StringUtils.isEmpty((CharSequence)agentJsonObject.get("hostname").toString())) {
            return ResDataUtils.resetErrorJson("Missing require parameters");
        }
        try {
            List allNetworkList = new ArrayList();
            String jsonString = "";
            if (!StringUtils.isEmpty((CharSequence)this.commonConfig.getRedisUrl())) {
                jsonString = RedisDataUtil.getValue(agentJsonObject.get("hostname").toString() + "_ALL_NETWORK");
            } else if (null != StaticKeys.HOST_IMPORT_DATA.get(agentJsonObject.get("hostname").toString() + "_ALL_NETWORK")) {
                jsonString = StaticKeys.HOST_IMPORT_DATA.get(agentJsonObject.get("hostname").toString() + "_ALL_NETWORK").toString();
            }
            if (!StringUtils.isEmpty((CharSequence)jsonString)) {
                JSONArray jsonArray = JSONUtil.parseArray((String)jsonString);
                allNetworkList = JSONUtil.toList((JSONArray)jsonArray, NetworkInfoDto.class);
                return ResDataUtils.resetSuccessJson(allNetworkList);
            }
            return ResDataUtils.resetSuccessJson(allNetworkList);
        }
        catch (Exception e) {
            logger.error("\u6570\u636e\u63a5\u53e3\u67e5\u8be2\u6839\u636eip\u67e5\u8be2\u7f51\u5361\u5217\u8868\u5217\u8868\u9519\u8bef", (Throwable)e);
            return ResDataUtils.resetErrorJson(e.toString());
        }
    }

    @ResponseBody
    @RequestMapping(value={"agentHostAllProcess"})
    public String agentHostAllProcess(@RequestBody String paramBean) {
        JSONObject agentJsonObject = (JSONObject)JSONUtil.parse((Object)paramBean);
        String checkResult = this.tokenUtils.preOpenDataAPICheck(agentJsonObject);
        if (!StringUtils.isEmpty((CharSequence)checkResult)) {
            return checkResult;
        }
        HashMap params = new HashMap();
        if (null == agentJsonObject.get("hostname") || StringUtils.isEmpty((CharSequence)agentJsonObject.get("hostname").toString())) {
            return ResDataUtils.resetErrorJson("Missing require parameters");
        }
        try {
            if (!StringUtils.isEmpty((CharSequence)this.commonConfig.getRedisUrl())) {
                String jsonString = RedisDataUtil.getValue(agentJsonObject.get("hostname").toString() + "_ALL_PROCESS");
                if (!StringUtils.isEmpty((CharSequence)jsonString)) {
                    JSONArray jsonArray = JSONUtil.parseArray((String)jsonString);
                    return ResDataUtils.resetSuccessJson(jsonArray);
                }
            } else {
                List cacheList = (List)StaticKeys.HOST_ALL_PROCESS.get(agentJsonObject.get("hostname").toString());
                return ResDataUtils.resetSuccessJson(cacheList);
            }
            return ResDataUtils.resetSuccessJson(null);
        }
        catch (Exception e) {
            logger.error("\u6570\u636e\u63a5\u53e3\u67e5\u8be2\u6839\u636eip\u67e5\u8be2\u5168\u91cf\u8fdb\u7a0b\u9519\u8bef", (Throwable)e);
            return ResDataUtils.resetErrorJson(e.toString());
        }
    }

    @ResponseBody
    @RequestMapping(value={"agentHostCrontab"})
    public String agentHostCrontab(@RequestBody String paramBean) {
        JSONObject agentJsonObject = (JSONObject)JSONUtil.parse((Object)paramBean);
        String checkResult = this.tokenUtils.preOpenDataAPICheck(agentJsonObject);
        if (!StringUtils.isEmpty((CharSequence)checkResult)) {
            return checkResult;
        }
        HashMap params = new HashMap();
        if (null == agentJsonObject.get("hostname") || StringUtils.isEmpty((CharSequence)agentJsonObject.get("hostname").toString())) {
            return ResDataUtils.resetErrorJson("Missing require parameters");
        }
        Object result = "";
        try {
            result = !StringUtils.isEmpty((CharSequence)this.commonConfig.getRedisUrl()) ? RedisDataUtil.getValue(agentJsonObject.get("hostname").toString() + "_CRONTAB") : StaticKeys.HOST_IMPORT_DATA.get(agentJsonObject.get("hostname").toString() + "_CRONTAB");
            return ResDataUtils.resetSuccessJson(result);
        }
        catch (Exception e) {
            logger.error("\u6570\u636e\u63a5\u53e3\u67e5\u8be2\u6839\u636eip\u67e5\u8be2crontab\u4fe1\u606f\u9519\u8bef", (Throwable)e);
            return ResDataUtils.resetErrorJson(e.toString());
        }
    }

    @ResponseBody
    @RequestMapping(value={"agentHostAllPort"})
    public String agentHostAllPort(@RequestBody String paramBean) {
        JSONObject agentJsonObject = (JSONObject)JSONUtil.parse((Object)paramBean);
        String checkResult = this.tokenUtils.preOpenDataAPICheck(agentJsonObject);
        if (!StringUtils.isEmpty((CharSequence)checkResult)) {
            return checkResult;
        }
        HashMap params = new HashMap();
        if (null == agentJsonObject.get("hostname") || StringUtils.isEmpty((CharSequence)agentJsonObject.get("hostname").toString())) {
            return ResDataUtils.resetErrorJson("Missing require parameters");
        }
        Object result = "";
        try {
            result = !StringUtils.isEmpty((CharSequence)this.commonConfig.getRedisUrl()) ? RedisDataUtil.getValue(agentJsonObject.get("hostname").toString() + "_ALLPORT") : StaticKeys.HOST_IMPORT_DATA.get(agentJsonObject.get("hostname").toString() + "_ALLPORT");
            return ResDataUtils.resetSuccessJson(result);
        }
        catch (Exception e) {
            logger.error("\u6570\u636e\u63a5\u53e3\u67e5\u8be2\u6839\u636eip\u67e5\u8be2\u5168\u91cf\u7aef\u53e3\u4fe1\u606f\u9519\u8bef", (Throwable)e);
            return ResDataUtils.resetErrorJson(e.toString());
        }
    }

    @ResponseBody
    @RequestMapping(value={"agentHostWinServices"})
    public String agentHostWinServices(@RequestBody String paramBean) {
        JSONObject agentJsonObject = (JSONObject)JSONUtil.parse((Object)paramBean);
        String checkResult = this.tokenUtils.preOpenDataAPICheck(agentJsonObject);
        if (!StringUtils.isEmpty((CharSequence)checkResult)) {
            return checkResult;
        }
        if (null == agentJsonObject.get("hostname") || StringUtils.isEmpty((CharSequence)agentJsonObject.get("hostname").toString())) {
            return ResDataUtils.resetErrorJson("Missing require parameters");
        }
        Object result = "";
        try {
            result = !StringUtils.isEmpty((CharSequence)this.commonConfig.getRedisUrl()) ? RedisDataUtil.getValue(agentJsonObject.get("hostname").toString() + "_WIN_SERVICES") : StaticKeys.HOST_IMPORT_DATA.get(agentJsonObject.get("hostname").toString() + "_WIN_SERVICES");
            ArrayList<String> winServicesList = new ArrayList();
            if (null != result) {
                winServicesList = new ArrayList<String>(Arrays.asList(result.toString().split(",")));
            }
            return ResDataUtils.resetSuccessJson(winServicesList);
        }
        catch (Exception e) {
            logger.error("\u6570\u636e\u63a5\u53e3\u67e5\u8be2\u6839\u636eip\u67e5\u8be2windows\u7cfb\u7edf\u670d\u52a1\u4fe1\u606f\u9519\u8bef", (Throwable)e);
            return ResDataUtils.resetErrorJson(e.toString());
        }
    }

    @ResponseBody
    @RequestMapping(value={"agentHostPCIData"})
    public String agentHostPCIData(@RequestBody String paramBean) {
        JSONObject agentJsonObject = (JSONObject)JSONUtil.parse((Object)paramBean);
        String checkResult = this.tokenUtils.preOpenDataAPICheck(agentJsonObject);
        if (!StringUtils.isEmpty((CharSequence)checkResult)) {
            return checkResult;
        }
        HashMap<String, Object> params = new HashMap<String, Object>();
        if (null == agentJsonObject.get("hostname") || StringUtils.isEmpty((CharSequence)agentJsonObject.get("hostname").toString())) {
            return ResDataUtils.resetErrorJson("Missing require parameters");
        }
        try {
            String hostname = agentJsonObject.get("hostname").toString();
            params.put("hostname", hostname);
            List<HostPciInfo> list = this.hostPciInfoService.selectAllByParams(params);
            return ResDataUtils.resetSuccessJson(list);
        }
        catch (Exception e) {
            logger.error("\u6570\u636e\u63a5\u53e3\u67e5\u8be2\u6839\u636eip\u67e5\u8be2PCI\u6570\u636e\u9519\u8bef", (Throwable)e);
            return ResDataUtils.resetErrorJson(e.toString());
        }
    }

    @ResponseBody
    @RequestMapping(value={"agentHostIfconfig"})
    public String agentHostIfconfig(@RequestBody String paramBean) {
        JSONObject agentJsonObject = (JSONObject)JSONUtil.parse((Object)paramBean);
        String checkResult = this.tokenUtils.preOpenDataAPICheck(agentJsonObject);
        if (!StringUtils.isEmpty((CharSequence)checkResult)) {
            return checkResult;
        }
        HashMap params = new HashMap();
        if (null == agentJsonObject.get("hostname") || StringUtils.isEmpty((CharSequence)agentJsonObject.get("hostname").toString())) {
            return ResDataUtils.resetErrorJson("Missing require parameters");
        }
        Object result = "";
        try {
            result = !StringUtils.isEmpty((CharSequence)this.commonConfig.getRedisUrl()) ? RedisDataUtil.getValue(agentJsonObject.get("hostname").toString() + "_IFCONFIG") : StaticKeys.HOST_IMPORT_DATA.get(agentJsonObject.get("hostname").toString() + "_IFCONFIG");
            return ResDataUtils.resetSuccessJson(result);
        }
        catch (Exception e) {
            logger.error("\u6570\u636e\u63a5\u53e3\u67e5\u8be2\u6839\u636eip\u67e5\u8be2\u7f51\u7edc\u914d\u7f6e\u4fe1\u606fifconfig\u9519\u8bef", (Throwable)e);
            return ResDataUtils.resetErrorJson(e.toString());
        }
    }

    @ResponseBody
    @RequestMapping(value={"agentHostLastUser"})
    public String agentHostLastUser(@RequestBody String paramBean) {
        JSONObject agentJsonObject = (JSONObject)JSONUtil.parse((Object)paramBean);
        String checkResult = this.tokenUtils.preOpenDataAPICheck(agentJsonObject);
        if (!StringUtils.isEmpty((CharSequence)checkResult)) {
            return checkResult;
        }
        HashMap params = new HashMap();
        if (null == agentJsonObject.get("hostname") || StringUtils.isEmpty((CharSequence)agentJsonObject.get("hostname").toString())) {
            return ResDataUtils.resetErrorJson("Missing require parameters");
        }
        Object result = "";
        try {
            result = !StringUtils.isEmpty((CharSequence)this.commonConfig.getRedisUrl()) ? RedisDataUtil.getValue(agentJsonObject.get("hostname").toString() + "_LASTUSER") : StaticKeys.HOST_IMPORT_DATA.get(agentJsonObject.get("hostname").toString() + "_LASTUSER");
            return ResDataUtils.resetSuccessJson(result);
        }
        catch (Exception e) {
            logger.error("\u6570\u636e\u63a5\u53e3\u67e5\u8be2\u6839\u636eip\u67e5\u8be2\u7cfb\u7edf\u7528\u6237\u6700\u8fd1\u767b\u5f55\u7684\u91c7\u96c6\u4fe1\u606f\u9519\u8bef", (Throwable)e);
            return ResDataUtils.resetErrorJson(e.toString());
        }
    }

    @ResponseBody
    @RequestMapping(value={"agentHostFireWall"})
    public String agentHostFireWall(@RequestBody String paramBean) {
        JSONObject agentJsonObject = (JSONObject)JSONUtil.parse((Object)paramBean);
        String checkResult = this.tokenUtils.preOpenDataAPICheck(agentJsonObject);
        if (!StringUtils.isEmpty((CharSequence)checkResult)) {
            return checkResult;
        }
        HashMap params = new HashMap();
        if (null == agentJsonObject.get("hostname") || StringUtils.isEmpty((CharSequence)agentJsonObject.get("hostname").toString())) {
            return ResDataUtils.resetErrorJson("Missing require parameters");
        }
        Object result = "";
        try {
            result = !StringUtils.isEmpty((CharSequence)this.commonConfig.getRedisUrl()) ? RedisDataUtil.getValue(agentJsonObject.get("hostname").toString() + "_FIREWALL") : StaticKeys.HOST_IMPORT_DATA.get(agentJsonObject.get("hostname").toString() + "_FIREWALL");
            return ResDataUtils.resetSuccessJson(result);
        }
        catch (Exception e) {
            logger.error("\u6570\u636e\u63a5\u53e3\u67e5\u8be2\u6839\u636eip\u67e5\u8be2\u4e3b\u673a\u9632\u706b\u5899\u72b6\u6001\u9519\u8bef", (Throwable)e);
            return ResDataUtils.resetErrorJson(e.toString());
        }
    }

    @ResponseBody
    @RequestMapping(value={"agentHostGPUData"})
    public String agentHostGPUData(@RequestBody String paramBean) {
        JSONObject agentJsonObject = (JSONObject)JSONUtil.parse((Object)paramBean);
        String checkResult = this.tokenUtils.preOpenDataAPICheck(agentJsonObject);
        if (!StringUtils.isEmpty((CharSequence)checkResult)) {
            return checkResult;
        }
        HashMap<String, Object> params = new HashMap<String, Object>();
        if (null == agentJsonObject.get("hostname") || StringUtils.isEmpty((CharSequence)agentJsonObject.get("hostname").toString())) {
            return ResDataUtils.resetErrorJson("Missing require parameters");
        }
        Object result = "";
        try {
            JSONObject resultJson = new JSONObject();
            result = !StringUtils.isEmpty((CharSequence)this.commonConfig.getRedisUrl()) ? RedisDataUtil.getValue(agentJsonObject.get("hostname").toString() + "_GPU") : StaticKeys.HOST_IMPORT_DATA.get(agentJsonObject.get("hostname").toString() + "_GPU");
            resultJson.set("gpuInfo", result);
            HashMap<String, String> paramsGpuState = new HashMap<String, String>();
            paramsGpuState.put("hostname", agentJsonObject.get("hostname").toString());
            List<GpuState> gpuStateList = this.gpuStateService.selectAllByParams(params);
            resultJson.set("gpuState", gpuStateList);
            return ResDataUtils.resetSuccessJson(resultJson);
        }
        catch (Exception e) {
            logger.error("\u6570\u636e\u63a5\u53e3\u67e5\u8be2\u6839\u636eip\u67e5\u8be2\u4e3b\u673aGPU\u6570\u636e\u9519\u8bef", (Throwable)e);
            return ResDataUtils.resetErrorJson(e.toString());
        }
    }

    @ResponseBody
    @RequestMapping(value={"agentTomcatData"})
    public String agentTomcatData(@RequestBody String paramBean) {
        JSONObject agentJsonObject = (JSONObject)JSONUtil.parse((Object)paramBean);
        String checkResult = this.tokenUtils.preOpenDataAPICheck(agentJsonObject);
        if (!StringUtils.isEmpty((CharSequence)checkResult)) {
            return checkResult;
        }
        try {
            List<JSONObject> tomcatList = TomcatUtil.viewTomcatHandler();
            return ResDataUtils.resetSuccessJson(tomcatList);
        }
        catch (Exception e) {
            logger.error("\u6570\u636e\u63a5\u53e3\u67e5\u8be2Tomcat\u6570\u636e\u9519\u8bef", (Throwable)e);
            return ResDataUtils.resetErrorJson(e.toString());
        }
    }

    @ResponseBody
    @RequestMapping(value={"agentScanIpData"})
    public String agentScanIpData(@RequestBody String paramBean) {
        JSONObject agentJsonObject = (JSONObject)JSONUtil.parse((Object)paramBean);
        String checkResult = this.tokenUtils.preOpenDataAPICheck(agentJsonObject);
        if (!StringUtils.isEmpty((CharSequence)checkResult)) {
            return checkResult;
        }
        try {
            String scanName = agentJsonObject.getStr("scanName");
            List<JSONObject> dataList = ScanIPUtil.viewScanIPHandler();
            List<JSONObject> resultList = new ArrayList();
            if (!StringUtils.isEmpty((CharSequence)scanName)) {
                for (JSONObject jsonObject : dataList) {
                    String autoScanName = jsonObject.getStr("autoScanName");
                    if (StringUtils.isEmpty((CharSequence)autoScanName) || !scanName.equals(autoScanName)) continue;
                    resultList.add(jsonObject);
                }
            } else {
                resultList = dataList;
            }
            return ResDataUtils.resetSuccessJson(resultList);
        }
        catch (Exception e) {
            logger.error("\u6570\u636e\u63a5\u53e3\u67e5\u8be2\u81ea\u52a8\u53d1\u73b0\u7684\u8bbe\u5907IP\u6570\u636e\u9519\u8bef", (Throwable)e);
            return ResDataUtils.resetErrorJson(e.toString());
        }
    }

    @ResponseBody
    @RequestMapping(value={"agentHostLikeShell"})
    public String agentHostLikeShell(@RequestBody String paramBean) {
        JSONObject agentJsonObject = (JSONObject)JSONUtil.parse((Object)paramBean);
        String checkResult = this.tokenUtils.preOpenDataAPICheck(agentJsonObject);
        if (!StringUtils.isEmpty((CharSequence)checkResult)) {
            return checkResult;
        }
        HashMap params = new HashMap();
        if (null == agentJsonObject.get("hostname") || StringUtils.isEmpty((CharSequence)agentJsonObject.get("hostname").toString())) {
            return ResDataUtils.resetErrorJson("Missing require parameters");
        }
        Object result = "";
        try {
            result = !StringUtils.isEmpty((CharSequence)this.commonConfig.getRedisUrl()) ? RedisDataUtil.getValue(agentJsonObject.get("hostname").toString() + "_LIKE_SHELL") : StaticKeys.HOST_IMPORT_DATA.get(agentJsonObject.get("hostname").toString() + "_LIKE_SHELL");
            return ResDataUtils.resetSuccessJson(result);
        }
        catch (Exception e) {
            logger.error("\u6570\u636e\u63a5\u53e3\u67e5\u8be2\u6839\u636eip\u67e5\u8be2\u4e2a\u6027\u5316\u76d1\u6d4b\u6570\u636e\u9519\u8bef", (Throwable)e);
            return ResDataUtils.resetErrorJson(e.toString());
        }
    }

    @ResponseBody
    @RequestMapping(value={"agentAllDockerList"})
    public String agentAllDockerList(@RequestBody String paramBean) {
        JSONObject agentJsonObject = (JSONObject)JSONUtil.parse((Object)paramBean);
        String checkResult = this.tokenUtils.preOpenDataAPICheck(agentJsonObject);
        if (!StringUtils.isEmpty((CharSequence)checkResult)) {
            return checkResult;
        }
        HashMap params = new HashMap();
        if (null == agentJsonObject.get("hostname") || StringUtils.isEmpty((CharSequence)agentJsonObject.get("hostname").toString())) {
            return ResDataUtils.resetErrorJson("Missing require parameters");
        }
        String result = "";
        try {
            if (!StringUtils.isEmpty((CharSequence)this.commonConfig.getRedisUrl())) {
                result = RedisDataUtil.getValue(agentJsonObject.get("hostname").toString() + "_ALL_DOCKER");
            } else {
                Object data = StaticKeys.HOST_IMPORT_DATA.get(agentJsonObject.get("hostname").toString() + "_ALL_DOCKER");
                if (null != data) {
                    result = data.toString();
                }
            }
            return ResDataUtils.resetSuccessJson(result);
        }
        catch (Exception e) {
            logger.error("\u6570\u636e\u63a5\u53e3\u67e5\u8be2\u6839\u636eip\u67e5\u8be2\u4e3b\u673a\u7684\u5168\u91cfDocker\u5bb9\u5668\u6570\u636e\u9519\u8bef", (Throwable)e);
            return ResDataUtils.resetErrorJson(e.toString());
        }
    }

    @ResponseBody
    @RequestMapping(value={"agentDeskStatesList"})
    public String agentDeskStatesList(@RequestBody String paramBean) {
        JSONObject agentJsonObject = (JSONObject)JSONUtil.parse((Object)paramBean);
        String checkResult = this.tokenUtils.preOpenDataAPICheck(agentJsonObject);
        if (!StringUtils.isEmpty((CharSequence)checkResult)) {
            return checkResult;
        }
        HashMap<String, Object> params = new HashMap<String, Object>();
        if (null == agentJsonObject.get("hostname") || StringUtils.isEmpty((CharSequence)agentJsonObject.get("hostname").toString())) {
            return ResDataUtils.resetErrorJson("Missing require parameters");
        }
        params.put("hostname", agentJsonObject.get("hostname").toString());
        try {
            List<DiskState> diskStates = this.diskStateService.selectAllByParams(params);
            this.diskStateService.computeDaysForDisk(diskStates, agentJsonObject.get("hostname").toString());
            return ResDataUtils.resetSuccessJson(diskStates);
        }
        catch (Exception e) {
            logger.error("\u6570\u636e\u63a5\u53e3\u67e5\u8be2\u6839\u636eip\u67e5\u8be2\u78c1\u76d8\u5217\u8868\u9519\u8bef", (Throwable)e);
            return ResDataUtils.resetErrorJson(e.toString());
        }
    }

    @ResponseBody
    @RequestMapping(value={"agentHostDiskPerList"})
    public String agentHostDiskPerList(@RequestBody String paramBean) {
        JSONObject agentJsonObject = (JSONObject)JSONUtil.parse((Object)paramBean);
        String checkResult = this.tokenUtils.preOpenDataAPICheck(agentJsonObject);
        if (!StringUtils.isEmpty((CharSequence)checkResult)) {
            return checkResult;
        }
        HashMap<String, Object> params = new HashMap<String, Object>();
        if (null == agentJsonObject.get("hostname") || StringUtils.isEmpty((CharSequence)agentJsonObject.get("hostname").toString())) {
            return ResDataUtils.resetErrorJson("Missing require parameters");
        }
        params.put("hostname", agentJsonObject.get("hostname").toString());
        try {
            List<HostDiskPer> hostDiskPerList = this.hostDiskPerService.selectAllByParams(params);
            return ResDataUtils.resetSuccessJson(hostDiskPerList);
        }
        catch (Exception e) {
            logger.error("\u6570\u636e\u63a5\u53e3\u67e5\u8be2\u4e3b\u673a\u7684\u78c1\u76d8\u603b\u4f7f\u7528\u7387\u5217\u8868\u9519\u8bef", (Throwable)e);
            return ResDataUtils.resetErrorJson(e.toString());
        }
    }

    @ResponseBody
    @RequestMapping(value={"agentDiskIosList"})
    public String agentDiskIosList(@RequestBody String paramBean) {
        JSONObject agentJsonObject = (JSONObject)JSONUtil.parse((Object)paramBean);
        String checkResult = this.tokenUtils.preOpenDataAPICheck(agentJsonObject);
        if (!StringUtils.isEmpty((CharSequence)checkResult)) {
            return checkResult;
        }
        HashMap<String, Object> params = new HashMap<String, Object>();
        if (null == agentJsonObject.get("hostname") || StringUtils.isEmpty((CharSequence)agentJsonObject.get("hostname").toString())) {
            return ResDataUtils.resetErrorJson("Missing require parameters");
        }
        params.put("hostname", agentJsonObject.get("hostname").toString());
        try {
            List<DiskIo> diskIos = this.diskIoService.selectAllByParams(params);
            return ResDataUtils.resetSuccessJson(diskIos);
        }
        catch (Exception e) {
            logger.error("\u6570\u636e\u63a5\u53e3\u67e5\u8be2\u6839\u636eip\u67e5\u8be2\u78c1\u76d8io\u5217\u8868\u9519\u8bef", (Throwable)e);
            return ResDataUtils.resetErrorJson(e.toString());
        }
    }

    @ResponseBody
    @RequestMapping(value={"agentDiskIoStateList"})
    public String agentDiskIoStateList(@RequestBody String paramBean) {
        JSONObject agentJsonObject = (JSONObject)JSONUtil.parse((Object)paramBean);
        String checkResult = this.tokenUtils.preOpenDataAPICheck(agentJsonObject);
        if (!StringUtils.isEmpty((CharSequence)checkResult)) {
            return checkResult;
        }
        HashMap<String, Object> params = new HashMap<String, Object>();
        if (null == agentJsonObject.get("hostname") || StringUtils.isEmpty((CharSequence)agentJsonObject.get("hostname").toString())) {
            return ResDataUtils.resetErrorJson("Missing require parameters");
        }
        params.put("hostname", agentJsonObject.get("hostname").toString());
        try {
            List<DiskIoState> diskIoStateList = this.diskIoStateService.selectAllByParams(params);
            return ResDataUtils.resetSuccessJson(diskIoStateList);
        }
        catch (Exception e) {
            logger.error("\u6570\u636e\u63a5\u53e3\u67e5\u8be2\u6839\u636eip\u67e5\u8be2\u78c1\u76d8io\u8bfb\u5199\u901f\u7387\u5217\u8868\u9519\u8bef", (Throwable)e);
            return ResDataUtils.resetErrorJson(e.toString());
        }
    }

    @ResponseBody
    @RequestMapping(value={"agentCpuTemperaturesList"})
    public String agentCpuTemperaturesList(@RequestBody String paramBean) {
        JSONObject agentJsonObject = (JSONObject)JSONUtil.parse((Object)paramBean);
        String checkResult = this.tokenUtils.preOpenDataAPICheck(agentJsonObject);
        if (!StringUtils.isEmpty((CharSequence)checkResult)) {
            return checkResult;
        }
        HashMap<String, Object> params = new HashMap<String, Object>();
        if (null == agentJsonObject.get("hostname") || StringUtils.isEmpty((CharSequence)agentJsonObject.get("hostname").toString())) {
            return ResDataUtils.resetErrorJson("Missing require parameters");
        }
        params.put("hostname", agentJsonObject.get("hostname").toString());
        try {
            List<CpuTemperatures> cpuTemperatures = this.cpuTemperaturesService.selectAllByParams(params);
            return ResDataUtils.resetSuccessJson(cpuTemperatures);
        }
        catch (Exception e) {
            logger.error("\u6570\u636e\u63a5\u53e3\u67e5\u8be2\u6839\u636eip\u67e5\u8be2cpu\u6e29\u5ea6\u5217\u8868\u9519\u8bef", (Throwable)e);
            return ResDataUtils.resetErrorJson(e.toString());
        }
    }

    @ResponseBody
    @RequestMapping(value={"agentNetIoStatesList"})
    public String agentNetIoStatesList(@RequestBody String paramBean) {
        JSONObject agentJsonObject = (JSONObject)JSONUtil.parse((Object)paramBean);
        String checkResult = this.tokenUtils.preOpenDataAPICheck(agentJsonObject);
        if (!StringUtils.isEmpty((CharSequence)checkResult)) {
            return checkResult;
        }
        HashMap<String, Object> params = new HashMap<String, Object>();
        if (null == agentJsonObject.get("hostname") || StringUtils.isEmpty((CharSequence)agentJsonObject.get("hostname").toString())) {
            return ResDataUtils.resetErrorJson("Missing require parameters");
        }
        params.put("hostname", agentJsonObject.get("hostname").toString());
        if (null != agentJsonObject.get("startTime")) {
            params.put("startTime", agentJsonObject.get("startTime").toString());
        }
        if (null != agentJsonObject.get("endTime")) {
            params.put("endTime", agentJsonObject.get("endTime").toString());
        }
        if (!StringUtils.isEmpty((CharSequence)agentJsonObject.getStr("orderBy"))) {
            params.put("orderBy", agentJsonObject.getStr("orderBy"));
            params.put("orderType", agentJsonObject.getStr("orderType"));
        }
        try {
            PageInfo netIoStates = this.netIoStateService.selectByParams(params, agentJsonObject.getInt("page"), agentJsonObject.getInt("pageSize"));
            return ResDataUtils.resetSuccessJson(netIoStates);
        }
        catch (Exception e) {
            logger.error("\u6570\u636e\u63a5\u53e3\u67e5\u8be2\u6839\u636eip\u67e5\u8be2\u7cfb\u7edf\u8d1f\u8f7d\u5217\u8868\u9519\u8bef", (Throwable)e);
            return ResDataUtils.resetErrorJson(e.toString());
        }
    }

    @ResponseBody
    @RequestMapping(value={"agentCpuStatesList"})
    public String agentCpuStatesList(@RequestBody String paramBean) {
        JSONObject agentJsonObject = (JSONObject)JSONUtil.parse((Object)paramBean);
        String checkResult = this.tokenUtils.preOpenDataAPICheck(agentJsonObject);
        if (!StringUtils.isEmpty((CharSequence)checkResult)) {
            return checkResult;
        }
        HashMap<String, Object> params = new HashMap<String, Object>();
        if (null == agentJsonObject.get("hostname") || StringUtils.isEmpty((CharSequence)agentJsonObject.get("hostname").toString())) {
            return ResDataUtils.resetErrorJson("Missing require parameters");
        }
        params.put("hostname", agentJsonObject.get("hostname").toString());
        if (null != agentJsonObject.get("startTime")) {
            params.put("startTime", agentJsonObject.get("startTime").toString());
        }
        if (null != agentJsonObject.get("endTime")) {
            params.put("endTime", agentJsonObject.get("endTime").toString());
        }
        if (!StringUtils.isEmpty((CharSequence)agentJsonObject.getStr("orderBy"))) {
            params.put("orderBy", agentJsonObject.getStr("orderBy"));
            params.put("orderType", agentJsonObject.getStr("orderType"));
        }
        try {
            PageInfo cpuStates = this.cpuStateService.selectByParams(params, agentJsonObject.getInt("page"), agentJsonObject.getInt("pageSize"));
            return ResDataUtils.resetSuccessJson(cpuStates);
        }
        catch (Exception e) {
            logger.error("\u6570\u636e\u63a5\u53e3\u67e5\u8be2\u6839\u636eip\u67e5\u8be2\u7cfb\u7edfcpu\u76d1\u63a7\u6570\u636e\u5217\u8868\u9519\u8bef", (Throwable)e);
            return ResDataUtils.resetErrorJson(e.toString());
        }
    }

    @ResponseBody
    @RequestMapping(value={"agentMemStatesList"})
    public String agentMemStatesList(@RequestBody String paramBean) {
        JSONObject agentJsonObject = (JSONObject)JSONUtil.parse((Object)paramBean);
        String checkResult = this.tokenUtils.preOpenDataAPICheck(agentJsonObject);
        if (!StringUtils.isEmpty((CharSequence)checkResult)) {
            return checkResult;
        }
        HashMap<String, Object> params = new HashMap<String, Object>();
        if (null == agentJsonObject.get("hostname") || StringUtils.isEmpty((CharSequence)agentJsonObject.get("hostname").toString())) {
            return ResDataUtils.resetErrorJson("Missing require parameters");
        }
        params.put("hostname", agentJsonObject.get("hostname").toString());
        if (null != agentJsonObject.get("startTime")) {
            params.put("startTime", agentJsonObject.get("startTime").toString());
        }
        if (null != agentJsonObject.get("endTime")) {
            params.put("endTime", agentJsonObject.get("endTime").toString());
        }
        if (!StringUtils.isEmpty((CharSequence)agentJsonObject.getStr("orderBy"))) {
            params.put("orderBy", agentJsonObject.getStr("orderBy"));
            params.put("orderType", agentJsonObject.getStr("orderType"));
        }
        try {
            PageInfo cpuStates = this.memStateService.selectByParams(params, agentJsonObject.getInt("page"), agentJsonObject.getInt("pageSize"));
            return ResDataUtils.resetSuccessJson(cpuStates);
        }
        catch (Exception e) {
            logger.error("\u6570\u636e\u63a5\u53e3\u67e5\u8be2\u6839\u636eip\u67e5\u8be2\u7cfb\u7edf\u5185\u5b58\u76d1\u63a7\u6570\u636e\u5217\u8868\u9519\u8bef", (Throwable)e);
            return ResDataUtils.resetErrorJson(e.toString());
        }
    }

    @ResponseBody
    @RequestMapping(value={"agentSysLoadStatesList"})
    public String agentSysLoadStatesList(@RequestBody String paramBean) {
        JSONObject agentJsonObject = (JSONObject)JSONUtil.parse((Object)paramBean);
        String checkResult = this.tokenUtils.preOpenDataAPICheck(agentJsonObject);
        if (!StringUtils.isEmpty((CharSequence)checkResult)) {
            return checkResult;
        }
        HashMap<String, Object> params = new HashMap<String, Object>();
        if (null == agentJsonObject.get("hostname") || StringUtils.isEmpty((CharSequence)agentJsonObject.get("hostname").toString())) {
            return ResDataUtils.resetErrorJson("Missing require parameters");
        }
        params.put("hostname", agentJsonObject.get("hostname").toString());
        if (null != agentJsonObject.get("startTime")) {
            params.put("startTime", agentJsonObject.get("startTime").toString());
        }
        if (null != agentJsonObject.get("endTime")) {
            params.put("endTime", agentJsonObject.get("endTime").toString());
        }
        if (!StringUtils.isEmpty((CharSequence)agentJsonObject.getStr("orderBy"))) {
            params.put("orderBy", agentJsonObject.getStr("orderBy"));
            params.put("orderType", agentJsonObject.getStr("orderType"));
        }
        try {
            PageInfo cpuStates = this.sysLoadStateService.selectByParams(params, agentJsonObject.getInt("page"), agentJsonObject.getInt("pageSize"));
            return ResDataUtils.resetSuccessJson(cpuStates);
        }
        catch (Exception e) {
            logger.error("\u6570\u636e\u63a5\u53e3\u67e5\u8be2\u6839\u636eip\u67e5\u8be2\u7cfb\u7edf\u8d1f\u8f7d\u76d1\u63a7\u6570\u636e\u5217\u8868\u9519\u8bef", (Throwable)e);
            return ResDataUtils.resetErrorJson(e.toString());
        }
    }

    @ResponseBody
    @RequestMapping(value={"agentHostGroupList"})
    public String agentHostGroupList(@RequestBody String paramBean) {
        JSONObject agentJsonObject = (JSONObject)JSONUtil.parse((Object)paramBean);
        String checkResult = this.tokenUtils.preOpenDataAPICheck(agentJsonObject);
        if (!StringUtils.isEmpty((CharSequence)checkResult)) {
            return checkResult;
        }
        HashMap<String, Object> params = new HashMap<String, Object>();
        if (null != agentJsonObject.get("groupName")) {
            params.put("groupName", agentJsonObject.get("groupName").toString());
        }
        try {
            List<HostGroup> hostGroups = this.hostGroupService.selectAllByParams(params, null);
            return ResDataUtils.resetSuccessJson(hostGroups);
        }
        catch (Exception e) {
            logger.error("\u6570\u636e\u63a5\u53e3\u67e5\u8be2\u6807\u7b7e\u5217\u8868\u9519\u8bef", (Throwable)e);
            return ResDataUtils.resetErrorJson(e.toString());
        }
    }

    @ResponseBody
    @RequestMapping(value={"agentRedisMonitorList"})
    public String agentRedisMonitorList(@RequestBody String paramBean) {
        JSONObject agentJsonObject = (JSONObject)JSONUtil.parse((Object)paramBean);
        String checkResult = this.tokenUtils.preOpenDataAPICheck(agentJsonObject);
        if (!StringUtils.isEmpty((CharSequence)checkResult)) {
            return checkResult;
        }
        HashMap<String, Object> params = new HashMap<String, Object>();
        if (null != agentJsonObject.get("redisName")) {
            params.put("redisName", agentJsonObject.get("redisName").toString());
        }
        try {
            List<RedisMonitor> redisMonitors = this.redisMonitorService.selectAllByParams(params);
            return ResDataUtils.resetSuccessJson(redisMonitors);
        }
        catch (Exception e) {
            logger.error("\u6570\u636e\u63a5\u53e3\u67e5\u8be2\u4e2d\u95f4\u4ef6redis\u76d1\u63a7\u4fe1\u606f\u5217\u8868\u9519\u8bef", (Throwable)e);
            return ResDataUtils.resetErrorJson(e.toString());
        }
    }

    @ResponseBody
    @RequestMapping(value={"agentKafkaMonitorList"})
    public String agentKafkaMonitorList(@RequestBody String paramBean) {
        JSONObject agentJsonObject = (JSONObject)JSONUtil.parse((Object)paramBean);
        String checkResult = this.tokenUtils.preOpenDataAPICheck(agentJsonObject);
        if (!StringUtils.isEmpty((CharSequence)checkResult)) {
            return checkResult;
        }
        HashMap<String, Object> params = new HashMap<String, Object>();
        if (null != agentJsonObject.get("kafkaName")) {
            params.put("kafkaName", agentJsonObject.get("kafkaName").toString());
        }
        try {
            List<KafkaMonitor> kafkaMonitors = this.kafkaMonitorService.selectAllByParams(params);
            return ResDataUtils.resetSuccessJson(kafkaMonitors);
        }
        catch (Exception e) {
            logger.error("\u6570\u636e\u63a5\u53e3\u67e5\u8be2\u4e2d\u95f4\u4ef6kafka\u76d1\u63a7\u4fe1\u606f\u5217\u8868\u9519\u8bef", (Throwable)e);
            return ResDataUtils.resetErrorJson(e.toString());
        }
    }

    @ResponseBody
    @RequestMapping(value={"commonWarnHandle"})
    public String agentCommonWarnHandle(@RequestBody String paramBean) {
        JSONObject agentJsonObject = (JSONObject)JSONUtil.parse((Object)paramBean);
        String checkResult = this.tokenUtils.preOpenDataAPICheck(agentJsonObject);
        if (!StringUtils.isEmpty((CharSequence)checkResult)) {
            return checkResult;
        }
        try {
            String title = "\u516c\u5171\u544a\u8b66\u63a5\u53e3\u901a\u77e5";
            if (null != agentJsonObject.get("title") && !StringUtils.isEmpty((CharSequence)agentJsonObject.get("title").toString())) {
                title = agentJsonObject.get("title").toString();
            }
            String content = agentJsonObject.get("content").toString();
            String warnLevel = "";
            if (null != agentJsonObject.get("warnLevel") && !StringUtils.isEmpty((CharSequence)agentJsonObject.get("warnLevel").toString())) {
                warnLevel = agentJsonObject.get("warnLevel").toString();
            }
            if (!WarnOtherUtil.isSendByWarnLevel(warnLevel)) {
                return ResDataUtils.resetErrorJson("\u672a\u8fbe\u5230\u544a\u8b66\u7ea7\u522b\uff0c\u5f53\u524d\u7cfb\u7edf\u8bbe\u7f6e\u544a\u8b66\u7ea7\u522b\u4e3a" + this.levelConfig.getDefaultWarn());
            }
            WarnOtherUtil.sendUtil("\u3010\u7b2c\u4e09\u65b9\u544a\u8b66\u3011" + title, content, "", "", false, warnLevel, "");
            return ResDataUtils.resetSuccessJson("success");
        }
        catch (Exception e) {
            logger.error("\u516c\u5171\u544a\u8b66\u63a5\u53e3\u9519\u8bef", (Throwable)e);
            return ResDataUtils.resetErrorJson(e.toString());
        }
    }

    @ResponseBody
    @RequestMapping(value={"agentRunStateList"})
    public String agentRunStateList(@RequestBody String paramBean) {
        JSONObject agentJsonObject = (JSONObject)JSONUtil.parse((Object)paramBean);
        String checkResult = this.tokenUtils.preOpenDataAPICheck(agentJsonObject);
        if (!StringUtils.isEmpty((CharSequence)checkResult)) {
            return checkResult;
        }
        try {
            HashMap<String, Object> params = new HashMap<String, Object>();
            if (!StringUtils.isEmpty((CharSequence)agentJsonObject.getStr("hostname"))) {
                params.put("hostname", agentJsonObject.getStr("hostname").trim());
            }
            if (!StringUtils.isEmpty((CharSequence)agentJsonObject.getStr("account"))) {
                params.put("account", agentJsonObject.getStr("account").trim());
            }
            if (!StringUtils.isEmpty((CharSequence)agentJsonObject.getStr("orderBy"))) {
                params.put("orderBy", agentJsonObject.getStr("orderBy"));
                params.put("orderType", agentJsonObject.getStr("orderType"));
            }
            PageInfo pageInfo = this.agentRunStateService.selectByParams(params, agentJsonObject.getInt("page"), agentJsonObject.getInt("pageSize"));
            this.agentRunStateService.setSystemInfoExtForList(pageInfo.getList());
            return ResDataUtils.resetSuccessJson(pageInfo);
        }
        catch (Exception e) {
            logger.error("agent\u83b7\u53d6\u7ec8\u7aef\u8fd0\u884c\u72b6\u6001\u4fe1\u606f\u5217\u8868\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("agent\u83b7\u53d6\u7ec8\u7aef\u8fd0\u884c\u72b6\u6001\u4fe1\u606f\u5217\u8868\u9519\u8bef", e.toString(), "2");
            return ResDataUtils.resetErrorJson(e.toString());
        }
    }

    @ResponseBody
    @RequestMapping(value={"agentActivemqData"})
    public String agentActivemqData(@RequestBody String paramBean) {
        JSONObject agentJsonObject = (JSONObject)JSONUtil.parse((Object)paramBean);
        String checkResult = this.tokenUtils.preOpenDataAPICheck(agentJsonObject);
        if (!StringUtils.isEmpty((CharSequence)checkResult)) {
            return checkResult;
        }
        try {
            List<JSONObject> activemqList = ActivemqUtil.viewActivemqHandler();
            return ResDataUtils.resetSuccessJson(activemqList);
        }
        catch (Exception e) {
            logger.error("\u6570\u636e\u63a5\u53e3\u67e5\u8be2activemq\u6570\u636e\u9519\u8bef", (Throwable)e);
            return ResDataUtils.resetErrorJson(e.toString());
        }
    }

    @ResponseBody
    @RequestMapping(value={"agentRabbitmqData"})
    public String agentRabbitmqData(@RequestBody String paramBean) {
        JSONObject agentJsonObject = (JSONObject)JSONUtil.parse((Object)paramBean);
        String checkResult = this.tokenUtils.preOpenDataAPICheck(agentJsonObject);
        if (!StringUtils.isEmpty((CharSequence)checkResult)) {
            return checkResult;
        }
        try {
            List<JSONObject> rabbitmqList = RabbitmqUtil.viewRabbitmqHandler();
            return ResDataUtils.resetSuccessJson(rabbitmqList);
        }
        catch (Exception e) {
            logger.error("\u6570\u636e\u63a5\u53e3\u67e5\u8be2rabbitmq\u6570\u636e\u9519\u8bef", (Throwable)e);
            return ResDataUtils.resetErrorJson(e.toString());
        }
    }
}

