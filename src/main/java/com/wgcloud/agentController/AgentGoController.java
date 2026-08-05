/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.agentController;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.wgcloud.entity.AppInfo;
import com.wgcloud.entity.AppState;
import com.wgcloud.entity.CpuState;
import com.wgcloud.entity.DockerInfo;
import com.wgcloud.entity.DockerState;
import com.wgcloud.entity.MemState;
import com.wgcloud.entity.NetIoState;
import com.wgcloud.entity.PortInfo;
import com.wgcloud.entity.SysLoadState;
import com.wgcloud.entity.SystemInfo;
import com.wgcloud.entity.SystemInfoExt;
import com.wgcloud.service.AppInfoService;
import com.wgcloud.service.DockerInfoService;
import com.wgcloud.service.PortInfoService;
import com.wgcloud.util.AgentUtils;
import com.wgcloud.util.DateUtil;
import com.wgcloud.util.FormatUtil;
import com.wgcloud.util.ThreadPoolUtil;
import com.wgcloud.util.TokenUtils;
import com.wgcloud.util.license.LicenseUtil;
import com.wgcloud.util.msg.WarnMailUtil;
import com.wgcloud.util.staticvar.BatchData;
import com.wgcloud.util.staticvar.StaticKeys;
import java.util.ArrayList;
import java.util.Date;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value={"/agentGo"})
public class AgentGoController {
    private static final Logger logger = LoggerFactory.getLogger(AgentGoController.class);
    @Autowired
    private DockerInfoService dockerInfoService;
    @Autowired
    private AppInfoService appInfoService;
    @Autowired
    private PortInfoService portInfoService;
    @Autowired
    private TokenUtils tokenUtils;
    @Autowired
    private AgentUtils agentUtils;

    @ResponseBody
    @RequestMapping(value={"/minTask"})
    public JSONObject minTask(@RequestBody String paramBean) {
        JSONObject agentJsonObject = (JSONObject)JSONUtil.parse((Object)paramBean);
        JSONObject resultJson = new JSONObject();
        if (null == agentJsonObject) {
            logger.error("agentJsonObject is null");
            resultJson.set("result", "agentJsonObject is null");
            return resultJson;
        }
        logger.debug("agent\u4e0a\u62a5\u6570\u636e-------------" + agentJsonObject.toString());
        if (!this.tokenUtils.checkAgentToken(agentJsonObject)) {
            logger.error("Token is error---------" + agentJsonObject.getStr("bindIp"));
            resultJson.set("result", "Token is error");
            return resultJson;
        }
        String bindIp = agentJsonObject.getStr("bindIp");
        if (this.isExists(bindIp)) {
            logger.error("agent multiple times at the same time: " + bindIp);
            resultJson.set("result", (Object)("agent multiple times at the same time: " + bindIp));
            return resultJson;
        }
        String hostName = agentJsonObject.getStr("hostName");
        String cpuCoresNum = agentJsonObject.getStr("cpuCoresNum");
        Integer hostProcsNum = agentJsonObject.getInt("hostProcsNum");
        JSONObject loadJson = agentJsonObject.getJSONObject("load");
        String netConnections = agentJsonObject.getStr("netConnections");
        Double cpuPercentJson = agentJsonObject.getDouble("cpuPercent");
        JSONObject virtualMemoryJson = agentJsonObject.getJSONObject("virtualMemory");
        JSONObject netIOCountersJson = agentJsonObject.getJSONObject("netIOCounters");
        Double cpuPercentVal = 0.0;
        Double memTotalVal = 0.0;
        Double memPercentVal = 0.0;
        Date nowtime = new Date();
        if (StringUtils.isEmpty((CharSequence)(bindIp = this.agentUtils.checkBindIP(bindIp, hostName)))) {
            resultJson.set("result", "error: bindIp is null");
            return resultJson;
        }
        try {
            if (cpuPercentJson != null) {
                cpuPercentVal = FormatUtil.formatDouble(cpuPercentJson, 2);
                this.addCpuState(cpuPercentJson, nowtime, bindIp, hostProcsNum);
            }
            if (virtualMemoryJson != null) {
                memTotalVal = virtualMemoryJson.getLong("total") != null ? Double.valueOf(FormatUtil.formatDouble((double)virtualMemoryJson.getLong("total").longValue() / 1024.0 / 1024.0 / 1024.0, 2)) : Double.valueOf(0.0);
                memPercentVal = virtualMemoryJson.getDouble("usedPercent") != null ? Double.valueOf(FormatUtil.formatDouble(virtualMemoryJson.getDouble("usedPercent"), 2)) : Double.valueOf(0.0);
                this.addMemState(memPercentVal, nowtime, bindIp);
            }
            SysLoadState sysLoadState = null;
            if (loadJson != null) {
                sysLoadState = this.addLoadState(loadJson, nowtime, bindIp);
            }
            NetIoState netIoState = null;
            if (netIOCountersJson != null) {
                netIoState = this.addNetIoState(netIOCountersJson, nowtime, bindIp, netConnections);
            }
            if (hostProcsNum != null) {
                this.addSystemInfo(hostProcsNum, cpuPercentVal, memPercentVal, memTotalVal, nowtime, bindIp, netIoState, cpuCoresNum, sysLoadState, netConnections);
            }
            resultJson.set("result", "success");
        }
        catch (Exception e) {
            logger.error("\u89e3\u6790\u4e0a\u62a5\u6570\u636e\u9519\u8bef", (Throwable)e);
            resultJson.set("result", (Object)("error\uff1a" + e.toString()));
        }
        return resultJson;
    }

    @ResponseBody
    @RequestMapping(value={"/minTaskHostExt"})
    public JSONObject minTaskHostExt(@RequestBody String paramBean) {
        JSONObject agentJsonObject = (JSONObject)JSONUtil.parse((Object)paramBean);
        JSONObject resultJson = new JSONObject();
        if (null == agentJsonObject) {
            logger.error("agentJsonObject is null");
            resultJson.set("result", "agentJsonObject is null");
            return resultJson;
        }
        logger.debug("agent\u4e0a\u62a5\u6570\u636e-------------" + agentJsonObject.toString());
        if (!this.tokenUtils.checkAgentToken(agentJsonObject)) {
            logger.error("Token is error---------" + agentJsonObject.getStr("bindIp"));
            resultJson.set("result", "Token is error");
            return resultJson;
        }
        String bindIp = agentJsonObject.getStr("bindIp");
        String hostName = agentJsonObject.getStr("hostName");
        String recvBytes = agentJsonObject.getStr("recvBytes");
        String sentBytes = agentJsonObject.getStr("sentBytes");
        String agentVer = agentJsonObject.getStr("agentVer");
        JSONObject hostInfoJson = agentJsonObject.getJSONObject("hostInfo");
        String submitSeconds = agentJsonObject.getStr("submitSeconds");
        JSONArray cpuInfoJson = this.initCpuJson(agentJsonObject);
        JSONObject swapMemoryStatJson = agentJsonObject.getJSONObject("swapMemoryStat");
        String netInterfaceNames = agentJsonObject.getStr("netInterface");
        Date nowtime = new Date();
        if (StringUtils.isEmpty((CharSequence)(bindIp = this.agentUtils.checkBindIP(bindIp, hostName)))) {
            resultJson.set("result", "error: bindIp is null");
            return resultJson;
        }
        try {
            if (hostInfoJson != null && cpuInfoJson != null) {
                this.addSystemInfoExt(hostInfoJson, cpuInfoJson.getJSONObject(0), nowtime, bindIp, submitSeconds, agentVer, recvBytes, sentBytes, hostName, swapMemoryStatJson, netInterfaceNames);
            }
            resultJson.set("result", "success");
        }
        catch (Exception e) {
            logger.error("\u89e3\u6790\u4e0a\u62a5\u6570\u636e\u9519\u8bef", (Throwable)e);
            resultJson.set("result", (Object)("error\uff1a" + e.toString()));
        }
        return resultJson;
    }

    @ResponseBody
    @RequestMapping(value={"/minTaskChild"})
    public JSONObject minTaskChild(@RequestBody String paramBean) {
        JSONObject agentJsonObject = (JSONObject)JSONUtil.parse((Object)paramBean);
        JSONObject resultJson = new JSONObject();
        if (null == agentJsonObject) {
            logger.error("agentJsonObject is null");
            resultJson.set("result", "agentJsonObject is null");
            return resultJson;
        }
        logger.debug("agent\u4e0a\u62a5\u6570\u636e-------------" + agentJsonObject.toString());
        if (!this.tokenUtils.checkAgentToken(agentJsonObject)) {
            logger.error("Token is error---------" + agentJsonObject.getStr("bindIp"));
            resultJson.set("result", "Token is error");
            return resultJson;
        }
        JSONObject dockersJson = agentJsonObject.getJSONObject("dockers");
        JSONObject portInfosJson = agentJsonObject.getJSONObject("portInfos");
        JSONObject processListJson = agentJsonObject.getJSONObject("processList");
        Date nowtime = new Date();
        try {
            if (processListJson != null) {
                this.addAppState(processListJson, nowtime);
            }
            if (dockersJson != null) {
                this.addDockerState(dockersJson, nowtime);
            }
            if (portInfosJson != null) {
                this.addPortInfos(portInfosJson, nowtime);
            }
            resultJson.set("result", "success");
        }
        catch (Exception e) {
            logger.error("\u89e3\u6790\u4e0a\u62a5\u6570\u636e\u9519\u8bef", (Throwable)e);
            resultJson.set("result", (Object)("error\uff1a" + e.toString()));
        }
        return resultJson;
    }

    private JSONArray initCpuJson(JSONObject agentJsonObject) {
        JSONArray cpuInfoJson = null;
        try {
            cpuInfoJson = agentJsonObject.getJSONArray("cpuInfo");
            if (cpuInfoJson.size() < 1) {
                JSONObject cpuInfoJsonTmp = new JSONObject();
                cpuInfoJsonTmp.set("cores", (Object)0);
                cpuInfoJsonTmp.set("modelName", "?");
                cpuInfoJson = new JSONArray();
                cpuInfoJson.add((Object)cpuInfoJsonTmp);
            }
        }
        catch (Exception e) {
            logger.error("cpuInfoJson is error : ", (Throwable)e);
            JSONObject cpuInfoJsonTmp = new JSONObject();
            cpuInfoJsonTmp.set("cores", (Object)0);
            cpuInfoJsonTmp.set("modelName", "?");
            cpuInfoJson = new JSONArray();
            cpuInfoJson.add((Object)cpuInfoJsonTmp);
        }
        return cpuInfoJson;
    }

    private void addMemState(Double memPercentJson, Date nowtime, String bindIp) {
        try {
            MemState bean = new MemState();
            bean.setUsePer(memPercentJson);
            bean.setCreateTime(nowtime);
            bean.setHostname(bindIp);
            BatchData.MEM_STATE_LIST.add(bean);
            Runnable runnable = () -> WarnMailUtil.sendWarnInfo(bean);
            ThreadPoolUtil.executor.execute(runnable);
        }
        catch (Exception e) {
            logger.error("\u89e3\u6790\u5185\u5b58\u4f7f\u7528\u7387\u4e0a\u62a5\u6570\u636e\u9519\u8bef", (Throwable)e);
        }
    }

    private void addCpuState(Double cpuPercentJson, Date nowtime, String bindIp, Integer hostProcsNum) {
        try {
            CpuState bean = new CpuState();
            bean.setHostname(bindIp);
            Double cpuPercentVal = FormatUtil.formatDouble(cpuPercentJson, 2);
            bean.setSys(cpuPercentVal);
            bean.setCreateTime(nowtime);
            bean.setProcsNum(hostProcsNum);
            BatchData.CPU_STATE_LIST.add(bean);
            Runnable runnable = () -> WarnMailUtil.sendCpuWarnInfo(bean);
            ThreadPoolUtil.executor.execute(runnable);
        }
        catch (Exception e) {
            logger.error("\u89e3\u6790cpu\u4f7f\u7528\u7387\u4e0a\u62a5\u6570\u636e\u9519\u8bef", (Throwable)e);
        }
    }

    private SysLoadState addLoadState(JSONObject loadJson, Date nowtime, String bindIp) {
        SysLoadState bean = new SysLoadState();
        try {
            bean.setHostname(bindIp);
            bean.setOneLoad(FormatUtil.formatDouble(loadJson.getDouble("load1"), 2));
            bean.setFiveLoad(FormatUtil.formatDouble(loadJson.getDouble("load5"), 2));
            bean.setFifteenLoad(FormatUtil.formatDouble(loadJson.getDouble("load15"), 2));
            bean.setCreateTime(nowtime);
            BatchData.SYSLOAD_STATE_LIST.add(bean);
            Runnable runnable = () -> WarnMailUtil.sendSysLoadWarnInfo(bean);
            ThreadPoolUtil.executor.execute(runnable);
        }
        catch (Exception e) {
            logger.error("\u89e3\u6790\u7cfb\u7edf\u8d1f\u8f7d\u4e0a\u62a5\u6570\u636e\u9519\u8bef", (Throwable)e);
        }
        return bean;
    }

    private NetIoState addNetIoState(JSONObject netIOCountersJson, Date nowtime, String bindIp, String netConnections) {
        NetIoState bean = new NetIoState();
        try {
            bean.setNetConnections(netConnections);
            if (netIOCountersJson.getDouble("res_recv_bytes") != null) {
                bean.setRxbyt(FormatUtil.formatDouble(netIOCountersJson.getDouble("res_recv_bytes"), 2) + "");
                if ("9.999E7".equals(bean.getRxbyt())) {
                    bean.setRxbyt("0");
                }
            } else {
                bean.setRxbyt("0");
            }
            if (netIOCountersJson.getDouble("res_sent_bytes") != null) {
                bean.setTxbyt(FormatUtil.formatDouble(netIOCountersJson.getDouble("res_sent_bytes"), 2) + "");
                if ("9.999E7".equals(bean.getTxbyt())) {
                    bean.setTxbyt("0");
                }
            } else {
                bean.setTxbyt("0");
            }
            if (netIOCountersJson.getDouble("res_sent_packets") != null) {
                bean.setTxpck(FormatUtil.formatDouble(netIOCountersJson.getDouble("res_sent_packets"), 2) + "");
                if ("9.999E7".equals(bean.getTxpck())) {
                    bean.setTxpck("0");
                }
            } else {
                bean.setTxpck("0");
            }
            if (netIOCountersJson.getDouble("res_recv_packets") != null) {
                bean.setRxpck(FormatUtil.formatDouble(netIOCountersJson.getDouble("res_recv_packets"), 2) + "");
                if ("9.999E7".equals(bean.getRxpck())) {
                    bean.setRxpck("0");
                }
            } else {
                bean.setRxpck("0");
            }
            if (netIOCountersJson.getDouble("res_dropin_packets") != null) {
                bean.setDropin(FormatUtil.formatDouble(netIOCountersJson.getDouble("res_dropin_packets"), 2) + "");
            } else {
                bean.setDropin("0");
            }
            if (netIOCountersJson.getDouble("res_dropout_packets") != null) {
                bean.setDropout(FormatUtil.formatDouble(netIOCountersJson.getDouble("res_dropout_packets"), 2) + "");
            } else {
                bean.setDropout("0");
            }
            bean.setCreateTime(nowtime);
            bean.setHostname(bindIp);
            BatchData.NETIO_STATE_LIST.add(bean);
            Runnable runnable = () -> {
                try {
                    if (bean != null) {
                        WarnMailUtil.sendDownSpeedWarnInfo(bean);
                        WarnMailUtil.sendUpSpeedWarnInfo(bean);
                        WarnMailUtil.sendNetConnectionsWarnInfo(bean);
                    }
                }
                catch (Exception e) {
                    logger.error("\u4e3b\u673a\u6d41\u91cf\u901f\u7387\u6216\u8fde\u63a5\u6570\u91cf\u544a\u8b66\u901a\u77e5\u53d1\u9001\u9519\u8bef", (Throwable)e);
                }
            };
            ThreadPoolUtil.executor.execute(runnable);
        }
        catch (Exception e) {
            logger.error("\u89e3\u6790\u7f51\u7edc\u6d41\u91cf\u4e0a\u62a5\u6570\u636e\u9519\u8bef", (Throwable)e);
        }
        return bean;
    }

    private void addAppState(JSONObject processListJson, Date nowtime) {
        ArrayList<String> keys = new ArrayList<String>(processListJson.keySet());
        for (String proId : keys) {
            try {
                String[] vals = processListJson.getStr(proId).split(",");
                AppInfo appInfo = new AppInfo();
                appInfo.setId(proId);
                appInfo.setCreateTime(nowtime);
                appInfo.setState("1");
                appInfo.setMemPer(FormatUtil.formatDouble(Double.valueOf(vals[1]), 2));
                appInfo.setCpuPer(FormatUtil.formatDouble(Double.valueOf(vals[0]), 2));
                appInfo.setReadBytes(FormatUtil.formatDouble(Double.valueOf(vals[2]), 2) + "");
                appInfo.setWritesBytes(FormatUtil.formatDouble(Double.valueOf(vals[3]), 2) + "");
                appInfo.setThreadsNum(vals[4]);
                appInfo.setGatherPid(vals[5]);
                String appTimes = DateUtil.millisToDate(vals[6], "yyyy-MM-dd HH:mm:ss");
                appInfo.setAppTimes(appTimes);
                appInfo.setProUsername(vals[7]);
                appInfo.setNetConnections(vals[8]);
                appInfo.setAppCmdLine(vals[9]);
                if (!LicenseUtil.checkEnterpriseVersion()) {
                    appInfo.setAppCmdLine("\u8bf7\u5347\u7ea7\u5230\u4f01\u4e1a\u7248\u67e5\u770b\u6b64\u6570\u636e");
                }
                if (null != appInfo.getAppCmdLine() && appInfo.getAppCmdLine().length() > 200) {
                    appInfo.setAppCmdLine(appInfo.getAppCmdLine().substring(0, 200));
                }
                if (appInfo.getCpuPer() < 0.0) {
                    appInfo.setState("2");
                    appInfo.setCreateTime(null);
                    appInfo.setAppTimes(null);
                    BatchData.APP_INFO_LIST.add(appInfo);
                    Runnable runnable = () -> {
                        try {
                            AppInfo appInfoW = this.appInfoService.selectById(proId);
                            if (appInfoW != null) {
                                WarnMailUtil.sendAppDown(appInfoW, true);
                            }
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                    };
                    ThreadPoolUtil.executor.execute(runnable);
                    continue;
                }
                BatchData.APP_INFO_LIST.add(appInfo);
                AppState appState = new AppState();
                appState.setAppInfoId(proId);
                appState.setCreateTime(nowtime);
                appState.setCpuPer(FormatUtil.formatDouble(Double.valueOf(vals[0]), 2));
                appState.setMemPer(FormatUtil.formatDouble(Double.valueOf(vals[1]), 2));
                appState.setThreadsNum(vals[4]);
                appState.setNetConnections(vals[8]);
                BatchData.APP_STATE_LIST.add(appState);
            }
            catch (Exception e) {
                logger.error("\u89e3\u6790\u8fdb\u7a0b\u4e0a\u62a5\u6570\u636e\u9519\u8bef", (Throwable)e);
            }
        }
    }

    private void addDockerState(JSONObject dockersJson, Date nowTime) {
        ArrayList<String> keys = new ArrayList<String>(dockersJson.keySet());
        for (String dockerId : keys) {
            try {
                JSONObject valJson = dockersJson.getJSONObject(dockerId);
                DockerInfo dockerInfo = new DockerInfo();
                dockerInfo.setId(dockerId);
                dockerInfo.setCreateTime(nowTime);
                dockerInfo.setState("1");
                dockerInfo.setMemPer(FormatUtil.formatDouble(valJson.getDouble("memToM"), 2));
                dockerInfo.setDockerCommand(valJson.getStr("command"));
                dockerInfo.setDockerImage(valJson.getStr("image"));
                dockerInfo.setDockerCreated(valJson.getStr("created"));
                dockerInfo.setDockerPort(valJson.getStr("portStr"));
                dockerInfo.setDockerSize(FormatUtil.formatDouble(valJson.getDouble("sizeRootFs"), 2) + "");
                dockerInfo.setDockerStatus(valJson.getStr("status"));
                dockerInfo.setGatherDockerNames(valJson.getStr("gatherDockerNames"));
                dockerInfo.setDockerState(valJson.getStr("containerState"));
                if (StaticKeys.LICENSE_STATE.equals("1")) {
                    dockerInfo.setUserTime(valJson.getDouble("userTime") + "");
                }
                if ("exited".equals(FormatUtil.toLowerStr(dockerInfo.getDockerState()))) {
                    dockerInfo.setState("2");
                    dockerInfo.setCreateTime(null);
                    BatchData.DOCKER_INFO_LIST.add(dockerInfo);
                    Runnable runnable = () -> {
                        try {
                            DockerInfo dockerInfoW = this.dockerInfoService.selectById(dockerId);
                            if (dockerInfoW != null) {
                                WarnMailUtil.sendDockerDown(dockerInfoW, true);
                            }
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                    };
                    ThreadPoolUtil.executor.execute(runnable);
                    continue;
                }
                BatchData.DOCKER_INFO_LIST.add(dockerInfo);
                DockerState dockerState = new DockerState();
                dockerState.setDockerInfoId(dockerId);
                dockerState.setCreateTime(nowTime);
                dockerState.setMemPer(dockerInfo.getMemPer());
                if (StaticKeys.LICENSE_STATE.equals("1") && !StringUtils.isEmpty((CharSequence)dockerInfo.getUserTime())) {
                    dockerState.setCpuPer(FormatUtil.formatDouble(valJson.getDouble("userTime"), 2));
                }
                BatchData.DOCKER_STATE_LIST.add(dockerState);
            }
            catch (Exception e) {
                logger.error("\u89e3\u6790docker\u4e0a\u62a5\u6570\u636e\u9519\u8bef", (Throwable)e);
            }
        }
    }

    private void addSystemInfo(Integer hostProcsNum, Double cpuPercentVal, Double memPercentVal, Double memTotalVal, Date nowtime, String bindIp, NetIoState netIoState, String cpuCoresNum, SysLoadState sysLoadState, String netConnections) throws Exception {
        SystemInfo bean = new SystemInfo();
        bean.setState("1");
        bean.setHostname(bindIp);
        bean.setNetConnections(netConnections);
        bean.setCreateTime(nowtime);
        bean.setCpuPer(cpuPercentVal);
        bean.setMemPer(memPercentVal);
        bean.setProcs(hostProcsNum + "");
        bean.setCpuCoreNum(cpuCoresNum);
        bean.setTotalMem(memTotalVal + "G");
        if (null == netIoState) {
            netIoState = new NetIoState();
        }
        bean.setRxbyt(netIoState.getRxbyt());
        bean.setTxbyt(netIoState.getTxbyt());
        if (null == sysLoadState) {
            sysLoadState = new SysLoadState();
        }
        bean.setFiveLoad(sysLoadState.getFiveLoad());
        bean.setFifteenLoad(sysLoadState.getFifteenLoad());
        BatchData.SYSTEM_INFO_LIST.add(bean);
    }

    private void addSystemInfoExt(JSONObject hostInfoJson, JSONObject cpuInfoJson, Date nowtime, String bindIp, String submitSeconds, String agentVer, String recvBytes, String sentBytes, String hostNameExt, JSONObject swapMemoryStatJson, String netInterfaceNames) {
        SystemInfoExt bean = new SystemInfoExt();
        bean.setSubmitSeconds(submitSeconds);
        bean.setAgentVer(agentVer);
        bean.setHostname(bindIp);
        bean.setHostnameExt(hostNameExt);
        bean.setCreateTime(nowtime);
        try {
            if (!StringUtils.isEmpty((CharSequence)netInterfaceNames)) {
                bean.setNetInterfaceName(netInterfaceNames);
            }
            bean.setBootTime(hostInfoJson.getLong("bootTime"));
            bean.setUptime(hostInfoJson.getLong("uptime"));
            bean.setCpuXh(cpuInfoJson.getStr("modelName"));
            bean.setCpuFamily(cpuInfoJson.getStr("family"));
            bean.setCpuMhz(cpuInfoJson.getStr("mhz"));
            bean.setCpuPhysicalid(cpuInfoJson.getStr("physicalId"));
            bean.setPlatForm(hostInfoJson.getStr("platform"));
            bean.setPlatformVersion(hostInfoJson.getStr("platformVersion"));
            bean.setKernelArch(hostInfoJson.getStr("kernelArch"));
            if (!StringUtils.isEmpty((CharSequence)recvBytes)) {
                bean.setBytesRecv(FormatUtil.formatDouble(Double.valueOf(recvBytes), 2) + "");
            } else {
                bean.setBytesRecv("0");
            }
            if (!StringUtils.isEmpty((CharSequence)sentBytes)) {
                bean.setBytesSent(FormatUtil.formatDouble(Double.valueOf(sentBytes), 2) + "");
            } else {
                bean.setBytesSent("0");
            }
            this.setSwapMemInfo(bean, swapMemoryStatJson);
            BatchData.SYSTEM_INFO_EXT_LIST.add(bean);
        }
        catch (Exception e) {
            logger.error("addSystemInfoExt\u9519\u8bef", (Throwable)e);
            BatchData.SYSTEM_INFO_EXT_LIST.add(bean);
        }
    }

    private void addPortInfos(JSONObject portInfoJson, Date nowtime) {
        ArrayList<String> keys = new ArrayList<String>(portInfoJson.keySet());
        for (String portId : keys) {
            try {
                String state = portInfoJson.getStr(portId);
                PortInfo portInfo = new PortInfo();
                portInfo.setId(portId);
                portInfo.setCreateTime(nowtime);
                portInfo.setState(state);
                if (!"1".equals(state)) {
                    portInfo.setState("2");
                    portInfo.setCreateTime(null);
                    BatchData.PORT_INFO_LIST.add(portInfo);
                    Runnable runnable = () -> {
                        try {
                            PortInfo portInfoW = this.portInfoService.selectById(portId);
                            if (portInfoW != null) {
                                WarnMailUtil.sendPortDown(portInfoW, true);
                            }
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                    };
                    ThreadPoolUtil.executor.execute(runnable);
                    continue;
                }
                BatchData.PORT_INFO_LIST.add(portInfo);
            }
            catch (Exception e) {
                logger.error("\u89e3\u6790\u7aef\u53e3\u4e0a\u62a5\u6570\u636e\u9519\u8bef", (Throwable)e);
            }
        }
    }

    private void setSwapMemInfo(SystemInfoExt bean, JSONObject swapMemoryStatJson) {
        try {
            Double memTotalVal = 0.0;
            Double memPercentVal = 0.0;
            if (swapMemoryStatJson != null) {
                if (swapMemoryStatJson.getLong("total") != null) {
                    memTotalVal = FormatUtil.formatDouble((double)swapMemoryStatJson.getLong("total").longValue() / 1024.0 / 1024.0 / 1024.0, 2);
                }
                if (swapMemoryStatJson.getDouble("usedPercent") != null) {
                    memPercentVal = FormatUtil.formatDouble(swapMemoryStatJson.getDouble("usedPercent"), 2);
                }
                bean.setSwapMemPer(String.valueOf(memPercentVal));
                bean.setTotalSwapMem(memTotalVal + "G");
            }
        }
        catch (Exception e) {
            logger.error("\u89e3\u6790\u4ea4\u6362\u533a\u5185\u5b58\u4fe1\u606f\u6570\u636e\u9519\u8bef", (Throwable)e);
        }
    }

    private boolean isExists(String bindIp) {
        try {
            if (StringUtils.isEmpty((CharSequence)bindIp)) {
                return true;
            }
            if (CollectionUtil.isEmpty(BatchData.SYSTEM_INFO_LIST)) {
                return false;
            }
            ArrayList<SystemInfo> systemInfoList = new ArrayList<SystemInfo>();
            systemInfoList.addAll(BatchData.SYSTEM_INFO_LIST);
            for (SystemInfo systemInfo : systemInfoList) {
                if (!bindIp.equals(systemInfo.getHostname())) continue;
                return true;
            }
        }
        catch (Exception e) {
            logger.error("check bindIp\u9519\u8bef", (Throwable)e);
        }
        return false;
    }
}

