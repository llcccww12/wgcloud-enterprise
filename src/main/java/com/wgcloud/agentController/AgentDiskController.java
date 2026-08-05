/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.agentController;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.wgcloud.config.MailConfig;
import com.wgcloud.entity.AppExceptionInfo;
import com.wgcloud.entity.CpuTemperatures;
import com.wgcloud.entity.DiskIo;
import com.wgcloud.entity.DiskIoState;
import com.wgcloud.entity.DiskSmart;
import com.wgcloud.entity.DiskState;
import com.wgcloud.entity.FileSafe;
import com.wgcloud.entity.GpuState;
import com.wgcloud.entity.HostUsers;
import com.wgcloud.entity.HostWarnDiy;
import com.wgcloud.entity.SystemInfo;
import com.wgcloud.service.DiskIoStateService;
import com.wgcloud.service.DockerInfoService;
import com.wgcloud.service.FileSafeService;
import com.wgcloud.service.SystemInfoService;
import com.wgcloud.util.AgentUtils;
import com.wgcloud.util.DateUtil;
import com.wgcloud.util.FormatUtil;
import com.wgcloud.util.HostUtil;
import com.wgcloud.util.MessageErrorUtils;
import com.wgcloud.util.ThreadPoolUtil;
import com.wgcloud.util.TokenUtils;
import com.wgcloud.util.license.LicenseUtil;
import com.wgcloud.util.msg.WarnMailUtil;
import com.wgcloud.util.staticvar.BatchData;
import com.wgcloud.util.staticvar.StaticKeys;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value={"/agentDiskGo"})
public class AgentDiskController {
    private static final Logger logger = LoggerFactory.getLogger(AgentDiskController.class);
    @Autowired
    private DockerInfoService dockerInfoService;
    @Autowired
    private DiskIoStateService diskIoStateService;
    @Autowired
    private FileSafeService fileSafeService;
    @Autowired
    private SystemInfoService systemInfoService;
    @Autowired
    private TokenUtils tokenUtils;
    @Autowired
    private MailConfig mailConfig;
    @Autowired
    private AgentUtils agentUtils;
    @Autowired
    private MessageErrorUtils messageErrorUtils;

    @ResponseBody
    @RequestMapping(value={"/minTask"})
    public JSONObject minTask(@RequestBody String paramBean) {
        JSONObject agentJsonObject = (JSONObject)JSONUtil.parse((Object)paramBean);
        logger.debug("agent\u4e0a\u62a5\u78c1\u76d8\u6570\u636e-------------" + agentJsonObject.toString());
        JSONObject resultJson = new JSONObject();
        if (!this.tokenUtils.checkAgentToken(agentJsonObject)) {
            logger.error("Token is error");
            resultJson.set("result", "Token is error");
            return resultJson;
        }
        String bindIp = agentJsonObject.getStr("bindIp");
        if (DateUtil.isClearTime()) {
            logger.info("\u6b63\u5728\u6e05\u7a7a\u5386\u53f2\u8d8b\u52bf\u56fe\u6570\u636e\uff0c\u4e0d\u6267\u884c\u63d0\u4ea4\u76d1\u63a7\u6570\u636e----------" + bindIp);
            resultJson.set("result", "Not save data in this time");
            return resultJson;
        }
        if (this.isExists(bindIp)) {
            logger.error("agentDisk multiple times at the same time: " + bindIp);
            resultJson.set("result", (Object)("agentDisk multiple times at the same time: " + bindIp));
            return resultJson;
        }
        JSONObject diskInfoListJson = agentJsonObject.getJSONObject("diskInfoList");
        JSONObject diskIoJson = agentJsonObject.getJSONObject("diskIo");
        JSONObject diskIoJsonEnd = agentJsonObject.getJSONObject("diskIoEnd");
        JSONObject fileSafesJson = agentJsonObject.getJSONObject("fileSafes");
        JSONArray temperaturesJsonArray = agentJsonObject.getJSONArray("temperatures");
        JSONObject processListJson = agentJsonObject.getJSONObject("processList");
        JSONObject processListAllJson = agentJsonObject.getJSONObject("processListAll");
        String gpuDataJson = agentJsonObject.getStr("gpuData");
        String gpuRateDataJson = agentJsonObject.getStr("gpuRateData");
        String fireWallDataJson = agentJsonObject.getStr("fireWallData");
        String portAllDataJson = agentJsonObject.getStr("portListAll");
        String crontabDataJson = agentJsonObject.getStr("crontabData");
        String likeShellDataJson = agentJsonObject.getStr("likeShell");
        String dockerAllListDataJson = agentJsonObject.getStr("dockerAllList");
        String winServicesDataJson = agentJsonObject.getStr("winServices");
        String hostName = agentJsonObject.getStr("hostName");
        Date nowTime = new Date();
        if (StringUtils.isEmpty((CharSequence)(bindIp = this.agentUtils.checkBindIP(bindIp, hostName)))) {
            resultJson.set("result", "error: bindIp is null");
            return resultJson;
        }
        try {
            if (diskInfoListJson != null) {
                this.addDiskState(diskInfoListJson, nowTime, bindIp);
            }
            if (diskIoJson != null) {
                this.addDiskIo(diskIoJson, nowTime, bindIp, diskIoJsonEnd);
            }
            if (fileSafesJson != null) {
                this.addFileSafes(fileSafesJson, nowTime, bindIp);
            }
            if (temperaturesJsonArray != null) {
                this.addCpuTemperatures(temperaturesJsonArray, nowTime, bindIp);
            }
            if (processListJson != null) {
                this.addAppException(processListJson, nowTime, bindIp);
            }
            if (processListAllJson != null) {
                this.addCacheHostAllProcess(processListAllJson, nowTime, bindIp);
            }
            if (gpuDataJson != null) {
                this.addCacheHostGpuInfo(gpuDataJson, nowTime, bindIp);
            }
            if (gpuRateDataJson != null) {
                this.addCacheHostGpuRate(gpuRateDataJson, nowTime, bindIp);
            }
            if (fireWallDataJson != null) {
                this.addCacheHostFireWall(fireWallDataJson, nowTime, bindIp);
            }
            if (winServicesDataJson != null) {
                this.addCacheWinServices(winServicesDataJson, nowTime, bindIp);
            }
            if (portAllDataJson != null) {
                this.addCacheHostPortAll(portAllDataJson, nowTime, bindIp);
            }
            if (crontabDataJson != null) {
                this.addCacheHostCrontab(crontabDataJson, nowTime, bindIp);
            }
            if (likeShellDataJson != null) {
                this.addCacheHostLikeShell(likeShellDataJson, nowTime, bindIp);
            }
            if (dockerAllListDataJson != null) {
                this.addCacheHostDockerAllList(dockerAllListDataJson, nowTime, bindIp);
            }
            resultJson.set("result", "success");
        }
        catch (Exception e) {
            logger.error("\u89e3\u6790\u78c1\u76d8\u4e0a\u62a5\u6570\u636e\u9519\u8bef", (Throwable)e);
            resultJson.set("result", (Object)("error\uff1a" + e.toString()));
        }
        return resultJson;
    }

    @ResponseBody
    @RequestMapping(value={"/minTaskSmart"})
    public JSONObject minTaskSmart(@RequestBody String paramBean) {
        JSONObject agentJsonObject = (JSONObject)JSONUtil.parse((Object)paramBean);
        logger.debug("agent\u4e0a\u62a5\u78c1\u76d8smart\u6570\u636e-------------" + agentJsonObject.toString());
        JSONObject resultJson = new JSONObject();
        if (!this.tokenUtils.checkAgentToken(agentJsonObject)) {
            logger.error("Token is error");
            resultJson.set("result", "Token is error");
            return resultJson;
        }
        String bindIp = agentJsonObject.getStr("bindIp");
        JSONObject diskSmartJson = agentJsonObject.getJSONObject("diskSmart");
        String hostName = agentJsonObject.getStr("hostName");
        Date nowTime = new Date();
        if (StringUtils.isEmpty((CharSequence)(bindIp = this.agentUtils.checkBindIP(bindIp, hostName)))) {
            resultJson.set("result", "error: bindIp is null");
            return resultJson;
        }
        try {
            if (diskSmartJson != null) {
                this.addDiskSmart(diskSmartJson, nowTime, bindIp);
            }
            resultJson.set("result", "success");
        }
        catch (Exception e) {
            logger.error("\u89e3\u6790\u78c1\u76d8\u4e0a\u62a5smart\u6570\u636e\u9519\u8bef", (Throwable)e);
            resultJson.set("result", (Object)("error\uff1a" + e.toString()));
        }
        return resultJson;
    }

    private void addDiskState(JSONObject diskInfoListJson, Date nowTime, String bindIp) {
        try {
            ArrayList<String> keys = new ArrayList<String>(diskInfoListJson.keySet());
            ArrayList<DiskState> willSaveList = new ArrayList<DiskState>();
            for (String diskname : keys) {
                JSONObject diskJson = diskInfoListJson.getJSONObject(diskname);
                DiskState bean = new DiskState();
                bean.setFileSystem(diskname);
                bean.setHostname(bindIp);
                if (this.blockDisk(bean)) continue;
                bean.setCreateTime(nowTime);
                bean.setUsed(FormatUtil.formatDouble((double)diskJson.getLong("used").longValue() / 1024.0 / 1024.0 / 1024.0, 2) + "G");
                bean.setAvail(FormatUtil.formatDouble((double)diskJson.getLong("free").longValue() / 1024.0 / 1024.0 / 1024.0, 2) + "G");
                bean.setDiskSize(FormatUtil.formatDouble((double)diskJson.getLong("total").longValue() / 1024.0 / 1024.0 / 1024.0, 2) + "G");
                bean.setUsePer(FormatUtil.formatDouble(diskJson.getDouble("usedPercent"), 2) + "%");
                willSaveList.add(bean);
                Runnable runnable = () -> WarnMailUtil.sendDiskWarnInfo(bean);
                ThreadPoolUtil.executor.execute(runnable);
            }
            if (!CollectionUtil.isEmpty(willSaveList)) {
                BatchData.DISK_STATE_LIST.addAll(willSaveList);
                SystemInfo systemInfo = new SystemInfo();
                systemInfo.setHostname(bindIp);
                HostUtil.setDiskSumPer(willSaveList, systemInfo);
                systemInfo.setCreateTime(nowTime);
                BatchData.HOST_DISK_SUM_LIST.add(systemInfo);
            }
        }
        catch (Exception e) {
            logger.error("\u89e3\u6790\u78c1\u76d8\u4e0a\u62a5\u6570\u636e\u9519\u8bef", (Throwable)e);
        }
    }

    private boolean blockDisk(DiskState diskState) {
        try {
            String diskBlock = this.mailConfig.getDiskBlockSave();
            HostWarnDiy hostWarnDiyDto = StaticKeys.HOST_WARN_MAP.get(diskState.getHostname());
            if (null != hostWarnDiyDto && "1".equals(hostWarnDiyDto.getActive()) && !StringUtils.isEmpty((CharSequence)hostWarnDiyDto.getDiskBlockSave())) {
                diskBlock = hostWarnDiyDto.getDiskBlockSave();
            }
            if (!StringUtils.isEmpty((CharSequence)diskBlock) && !StringUtils.isEmpty((CharSequence)diskState.getFileSystem())) {
                String[] blocks = diskBlock.split(",");
                AntPathMatcher pm = new AntPathMatcher();
                for (String diskBlcok : blocks) {
                    diskBlcok = diskBlcok.replace("'", "");
                    if ("/".equals(diskState.getFileSystem())) {
                        if (!diskBlcok.equals(diskState.getFileSystem())) continue;
                        return true;
                    }
                    boolean matchStart = pm.matchStart(diskBlcok, diskState.getFileSystem());
                    if (!matchStart) continue;
                    return matchStart;
                }
            }
        }
        catch (Exception e) {
            logger.error("\u5224\u65ad\u662f\u5426\u662f\u4e0d\u9700\u8981\u4fdd\u5b58\u7684\u78c1\u76d8\u9519\u8bef", (Throwable)e);
        }
        return false;
    }

    private void addDiskIo(JSONObject diskIoListJson, Date nowTime, String bindIp, JSONObject diskIoListJsonEnd) {
        try {
            JSONObject diskIoJsonObj;
            long startSumReadBytes = 0L;
            long startSumWriteBytes = 0L;
            long endSumReadBytes = 0L;
            long endSumWriteBytes = 0L;
            long startSumReadCount = 0L;
            long startSumWriteCount = 0L;
            long endSumReadCount = 0L;
            long endSumWriteCount = 0L;
            ArrayList<String> keys = new ArrayList<String>(diskIoListJson.keySet());
            ArrayList<DiskIo> willSaveList = new ArrayList<DiskIo>();
            for (Object diskname : keys) {
                DiskIo ioBean = new DiskIo();
                diskIoJsonObj = diskIoListJson.getJSONObject((String)diskname);
                if (null == diskIoJsonObj) continue;
                ioBean.setFileSystem((String)diskname);
                ioBean.setHostname(bindIp);
                ioBean.setCreateTime(nowTime);
                ioBean.setReadCount(diskIoJsonObj.getLong("readCount") + "");
                startSumReadCount += diskIoJsonObj.getLong("readCount").longValue();
                ioBean.setWriteCount(diskIoJsonObj.getLong("writeCount") + "");
                startSumWriteCount += diskIoJsonObj.getLong("writeCount").longValue();
                ioBean.setReadBytes(diskIoJsonObj.getLong("readBytes") / 1024L / 1024L / 1024L + "G");
                startSumReadBytes += diskIoJsonObj.getLong("readBytes").longValue();
                ioBean.setWriteBytes(diskIoJsonObj.getLong("writeBytes") / 1024L / 1024L / 1024L + "G");
                startSumWriteBytes += diskIoJsonObj.getLong("writeBytes").longValue();
                ioBean.setReadTime(diskIoJsonObj.getLong("readTime") + "");
                ioBean.setWriteTime(diskIoJsonObj.getLong("writeTime") + "");
                String serialNumber = diskIoJsonObj.getStr("serialNumber");
                if (!StringUtils.isEmpty((CharSequence)serialNumber)) {
                    ioBean.setFileSystem((String)diskname + " [" + serialNumber + "]");
                }
                willSaveList.add(ioBean);
            }
            if (!CollectionUtil.isEmpty(willSaveList)) {
                BatchData.DISK_IO_LIST.addAll(willSaveList);
            }
            if (!StaticKeys.LICENSE_STATE.equals("1")) {
                return;
            }
            ArrayList keysEnd = new ArrayList(diskIoListJsonEnd.keySet());
            for (String diskname : (java.util.List<String>)keysEnd) {
                diskIoJsonObj = diskIoListJsonEnd.getJSONObject(diskname);
                if (null == diskIoJsonObj) continue;
                endSumReadBytes += diskIoJsonObj.getLong("readBytes").longValue();
                endSumWriteBytes += diskIoJsonObj.getLong("writeBytes").longValue();
                endSumReadCount += diskIoJsonObj.getLong("readCount").longValue();
                endSumWriteCount += diskIoJsonObj.getLong("writeCount").longValue();
            }
            double readMbAvg = FormatUtil.formatDouble((double)((endSumReadBytes - startSumReadBytes) / 2L) / 1024.0 / 1024.0, 2);
            double writeMbAvg = FormatUtil.formatDouble((double)((endSumWriteBytes - startSumWriteBytes) / 2L) / 1024.0 / 1024.0, 2);
            double readCountAvg = FormatUtil.formatDouble((double)(endSumReadCount - startSumReadCount) / 2.0, 2);
            double writeCountAvg = FormatUtil.formatDouble((double)(endSumWriteCount - startSumWriteCount) / 2.0, 2);
            DiskIoState diskIoState = new DiskIoState();
            diskIoState.setHostname(bindIp);
            diskIoState.setReadIoAvg(readMbAvg + "");
            diskIoState.setWriteIoAvg(writeMbAvg + "");
            diskIoState.setReadIoCountAvg(readCountAvg + "");
            diskIoState.setWriteIoCountAvg(writeCountAvg + "");
            BatchData.DISK_IO_STATE_LIST.add(diskIoState);
            Runnable runnable = () -> WarnMailUtil.sendDiskIoSpeedWarnInfo(diskIoState);
            ThreadPoolUtil.executor.execute(runnable);
        }
        catch (Exception e) {
            logger.error("\u89e3\u6790\u78c1\u76d8IO\u6570\u636e\u9519\u8bef", (Throwable)e);
        }
    }

    private void addDiskSmart(JSONObject diskSmartListJson, Date nowTime, String bindIp) {
        ArrayList<String> keys = new ArrayList<String>(diskSmartListJson.keySet());
        ArrayList<DiskSmart> willSaveList = new ArrayList<DiskSmart>();
        for (String diskname : keys) {
            try {
                DiskSmart smartBean = new DiskSmart();
                smartBean.setFileSystem(diskname);
                smartBean.setHostname(bindIp);
                smartBean.setCreateTime(nowTime);
                String[] smartStrs = diskSmartListJson.getStr(diskname).split(",");
                smartBean.setDiskState(smartStrs[0]);
                smartBean.setPowerHours(smartStrs[1]);
                smartBean.setPowerCount(smartStrs[2]);
                smartBean.setTemperature(smartStrs[3]);
                willSaveList.add(smartBean);
                Runnable runnable = () -> WarnMailUtil.sendDiskSmartWarnInfo(smartBean);
                ThreadPoolUtil.executor.execute(runnable);
            }
            catch (Exception e) {
                logger.error("\u89e3\u6790\u78c1\u76d8smart\u6570\u636e\u9519\u8bef", (Throwable)e);
            }
        }
        if (!CollectionUtil.isEmpty(willSaveList)) {
            BatchData.DISK_SMART_LIST.addAll(willSaveList);
        }
    }

    private void addHostUsers(JSONArray hostUsersListJson, Date nowTime, String bindIp) {
        ArrayList<HostUsers> willSaveList = new ArrayList<HostUsers>();
        for (Object hostUserObj : hostUsersListJson) {
            try {
                HostUsers hostUser = new HostUsers();
                hostUser.setHostname(bindIp);
                hostUser.setCreateTime(nowTime);
                JSONObject hostUserJson = JSONUtil.parseObj(hostUserObj);
                String user = hostUserJson.getStr("user");
                hostUser.setHostUser(user);
                String lastLoginIp = hostUserJson.getStr("host");
                hostUser.setLastLoginIp(lastLoginIp);
                String terminal = hostUserJson.getStr("terminal");
                hostUser.setTerminal(terminal);
                String lastLoginTime = hostUserJson.getStr("started");
                String timeFormat = DateUtil.secondToDate(Long.valueOf(lastLoginTime), "yyyy-MM-dd HH:mm:ss");
                hostUser.setLastLoginTime(timeFormat);
                willSaveList.add(hostUser);
            }
            catch (Exception e) {
                logger.error("\u89e3\u6790\u76d1\u63a7\u4e3b\u673a\u7684\u7cfb\u7edf\u7528\u6237\u4fe1\u606f\u9519\u8bef", (Throwable)e);
            }
        }
        if (!CollectionUtil.isEmpty(willSaveList)) {
            BatchData.HOST_USERS_LIST.addAll(willSaveList);
        }
    }

    private void addFileSafes(JSONObject fileSafeJson, Date nowTime, String bindIp) {
        ArrayList<String> keys = new ArrayList<String>(fileSafeJson.keySet());
        for (String fileSafeId : keys) {
            try {
                JSONObject jsonObject = fileSafeJson.getJSONObject(fileSafeId);
                String state = jsonObject.getStr("state");
                String fileModtime = jsonObject.getStr("fileModtime");
                String errorFileCount = jsonObject.getStr("errorFileCount");
                FileSafe fileSafe = new FileSafe();
                fileSafe.setId(fileSafeId);
                fileSafe.setCreateTime(nowTime);
                fileSafe.setState(state);
                if (!StringUtils.isEmpty((CharSequence)fileModtime)) {
                    fileSafe.setFileModtime(DateUtil.secondToDate(Long.valueOf(fileModtime), "yyyy-MM-dd HH:mm:ss"));
                }
                String errorFileCountFinal = errorFileCount;
                this.messageErrorUtils.setErrorMsgHandler(fileSafeId, errorFileCountFinal);
                if (!"1".equals(state)) {
                    fileSafe.setState("2");
                    fileSafe.setCreateTime(null);
                    BatchData.FILE_SAFE_LIST.add(fileSafe);
                    Runnable runnable = () -> {
                        try {
                            FileSafe fileSafeOld = this.fileSafeService.selectById(fileSafeId);
                            fileSafeOld.setFileModtime(fileSafe.getFileModtime());
                            if (fileSafeOld != null) {
                                WarnMailUtil.sendFileSafeDown(fileSafeOld, true, errorFileCountFinal);
                            }
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                    };
                    ThreadPoolUtil.executor.execute(runnable);
                    continue;
                }
                BatchData.FILE_SAFE_LIST.add(fileSafe);
            }
            catch (Exception e) {
                logger.error("\u89e3\u6790\u6587\u4ef6\u9632\u7be1\u6539\u76d1\u6d4b\u4fe1\u606f\u4e0a\u62a5\u6570\u636e\u9519\u8bef", (Throwable)e);
            }
        }
    }

    private void addCpuTemperatures(JSONArray temperaturesJsonArray, Date nowTime, String bindIp) {
        try {
            logger.debug("addCpuTemperatures--------------" + temperaturesJsonArray.toString());
            ArrayList<CpuTemperatures> willSaveList = new ArrayList<CpuTemperatures>();
            for (Object temperaturesObj : temperaturesJsonArray) {
                CpuTemperatures cpuTemperatures = new CpuTemperatures();
                cpuTemperatures.setHostname(bindIp);
                cpuTemperatures.setCreateTime(nowTime);
                JSONObject temperaturesJson = JSONUtil.parseObj(temperaturesObj);
                String sensor = temperaturesJson.getStr("sensorKey");
                String sensorTemperature = temperaturesJson.getStr("temperature");
                String sensorHigh = temperaturesJson.getStr("sensorHigh");
                String sensorCritical = temperaturesJson.getStr("sensorCritical");
                cpuTemperatures.setCore_index(sensor);
                cpuTemperatures.setCrit(sensorCritical);
                cpuTemperatures.setInput(sensorTemperature);
                cpuTemperatures.setMax(sensorHigh);
                willSaveList.add(cpuTemperatures);
                Runnable runnable = () -> WarnMailUtil.sendCpuTemperatures(cpuTemperatures);
                ThreadPoolUtil.executor.execute(runnable);
            }
            if (!CollectionUtil.isEmpty(willSaveList)) {
                BatchData.CPU_TEMPERATURES_LIST.addAll(willSaveList);
            }
        }
        catch (Exception e) {
            logger.error("\u89e3\u6790cpu\u6e29\u5ea6\u4e0a\u62a5\u6570\u636e\u9519\u8bef", (Throwable)e);
        }
    }

    private void addAppException(JSONObject processListJson, Date nowTime, String bindIp) {
        ArrayList<String> keys = new ArrayList<String>(processListJson.keySet());
        ArrayList<AppExceptionInfo> willSaveList = new ArrayList<AppExceptionInfo>();
        for (String proId : keys) {
            try {
                logger.info("addAppException--------" + processListJson.getStr(proId) + "------------" + bindIp);
                String[] vals = processListJson.getStr(proId).split(",");
                AppExceptionInfo appExceptionInfo = new AppExceptionInfo();
                appExceptionInfo.setHostname(bindIp);
                appExceptionInfo.setCreateTime(nowTime);
                appExceptionInfo.setState("1");
                appExceptionInfo.setMemPer(FormatUtil.formatDouble(Double.valueOf(vals[1]), 2));
                appExceptionInfo.setCpuPer(FormatUtil.formatDouble(Double.valueOf(vals[0]), 2));
                appExceptionInfo.setReadBytes(FormatUtil.formatDouble(Double.valueOf(vals[2]), 2) + "");
                appExceptionInfo.setWritesBytes(FormatUtil.formatDouble(Double.valueOf(vals[3]), 2) + "");
                appExceptionInfo.setThreadsNum(vals[4]);
                appExceptionInfo.setGatherPid(vals[5]);
                String appTimes = DateUtil.millisToDate(vals[6], "yyyy-MM-dd HH:mm:ss");
                appExceptionInfo.setAppTimes(appTimes);
                appExceptionInfo.setProUsername(vals[7]);
                appExceptionInfo.setAppName(vals[10]);
                appExceptionInfo.setNetConnections(vals[8]);
                appExceptionInfo.setAppCmdLine(vals[9]);
                if (!LicenseUtil.checkEnterpriseVersion()) {
                    appExceptionInfo.setAppCmdLine("\u8bf7\u5347\u7ea7\u5230\u4f01\u4e1a\u7248\u67e5\u770b\u6b64\u6570\u636e");
                }
                if (null != appExceptionInfo.getAppName() && appExceptionInfo.getAppName().length() > 100) {
                    appExceptionInfo.setAppName(appExceptionInfo.getAppName().substring(0, 100));
                }
                if (null != appExceptionInfo.getAppCmdLine() && appExceptionInfo.getAppCmdLine().length() > 200) {
                    appExceptionInfo.setAppCmdLine(appExceptionInfo.getAppCmdLine().substring(0, 200));
                }
                willSaveList.add(appExceptionInfo);
            }
            catch (Exception e) {
                logger.error("\u89e3\u6790\u5f02\u5e38\u8fdb\u7a0b\u4e0a\u62a5\u6570\u636e\u9519\u8bef", (Throwable)e);
            }
        }
        if (!CollectionUtil.isEmpty(willSaveList)) {
            BatchData.APP_EXCEPTION_INFO_LIST.addAll(willSaveList);
        }
    }

    private void addCacheHostAllProcess(JSONObject processListJson, Date nowTime, String bindIp) {
        ArrayList<AppExceptionInfo> willCacheList = new ArrayList<AppExceptionInfo>();
        if (!LicenseUtil.checkEnterpriseVersion()) {
            AppExceptionInfo appExceptionInfo = new AppExceptionInfo();
            appExceptionInfo.setMemPer(0.0);
            appExceptionInfo.setCpuPer(0.0);
            appExceptionInfo.setGatherPid("0");
            appExceptionInfo.setAppTimes("1970-01-01 08:00:00");
            appExceptionInfo.setProUsername("nobody");
            appExceptionInfo.setAppName("\u8bf7\u5347\u7ea7\u5230\u4f01\u4e1a\u7248\u67e5\u770b\u6b64\u6570\u636e");
            appExceptionInfo.setAppCmdLine("");
            appExceptionInfo.setReadBytes("0");
            appExceptionInfo.setWritesBytes("0");
            willCacheList.add(appExceptionInfo);
            HostUtil.setAllProcessHandler(bindIp, willCacheList, nowTime);
            return;
        }
        ArrayList<String> keys = new ArrayList<String>(processListJson.keySet());
        for (String proId : keys) {
            try {
                logger.debug("addCacheHostAllProcess--------" + processListJson.getStr(proId));
                String[] vals = processListJson.getStr(proId).split(",");
                AppExceptionInfo appExceptionInfo = new AppExceptionInfo();
                appExceptionInfo.setMemPer(FormatUtil.formatDouble(Double.valueOf(vals[1]), 2));
                appExceptionInfo.setCpuPer(FormatUtil.formatDouble(Double.valueOf(vals[0]), 2));
                appExceptionInfo.setReadBytes(vals[2]);
                appExceptionInfo.setWritesBytes(vals[3]);
                appExceptionInfo.setGatherPid(vals[4]);
                String appTimes = DateUtil.millisToDate(vals[5], "yyyy-MM-dd HH:mm:ss");
                appExceptionInfo.setAppTimes(appTimes);
                appExceptionInfo.setProUsername(vals[6]);
                appExceptionInfo.setAppName(vals[7]);
                appExceptionInfo.setAppCmdLine(vals[8]);
                willCacheList.add(appExceptionInfo);
            }
            catch (Exception e) {
                logger.error("\u89e3\u6790\u4e3b\u673a\u5168\u91cf\u8fdb\u7a0b\u6570\u636e\u9519\u8bef", (Throwable)e);
            }
        }
        if (!CollectionUtil.isEmpty(willCacheList)) {
            HostUtil.setAllProcessHandler(bindIp, willCacheList, nowTime);
        }
    }

    private void addCacheHostGpuInfo(String gpuJson, Date nowTime, String bindIp) {
        try {
            if (!LicenseUtil.checkEnterpriseVersion()) {
                HostUtil.setImportDataHandler(bindIp + "_GPU", "\u8bf7\u5347\u7ea7\u5230\u4f01\u4e1a\u7248\u67e5\u770b\u6b64\u6570\u636e", nowTime);
                return;
            }
            if (null != gpuJson) {
                HostUtil.setImportDataHandler(bindIp + "_GPU", gpuJson, nowTime);
            }
        }
        catch (Exception e) {
            logger.error("\u89e3\u6790\u4e3b\u673aGPU\u91c7\u96c6\u6570\u636e\u9519\u8bef", (Throwable)e);
        }
    }

    private void addCacheHostGpuRate(String gpuRateJson, Date nowTime, String bindIp) {
        try {
            if (!LicenseUtil.checkEnterpriseVersion()) {
                return;
            }
            if (!StringUtils.isEmpty((CharSequence)gpuRateJson)) {
                GpuState gpuState = new GpuState();
                gpuState.setGpuRate(gpuRateJson);
                gpuState.setCreateTime(nowTime);
                gpuState.setHostname(bindIp);
                BatchData.GPU_RATE_LIST.add(gpuState);
            }
        }
        catch (Exception e) {
            logger.error("\u89e3\u6790\u4e3b\u673aGPU\u4f7f\u7528\u7387\u91c7\u96c6\u6570\u636e\u9519\u8bef", (Throwable)e);
        }
    }

    private void addCacheHostFireWall(String fireWallJson, Date nowTime, String bindIp) {
        try {
            if (!LicenseUtil.checkEnterpriseVersion()) {
                HostUtil.setImportDataHandler(bindIp + "_FIREWALL", "\u8bf7\u5347\u7ea7\u5230\u4f01\u4e1a\u7248\u67e5\u770b\u6b64\u6570\u636e", nowTime);
                return;
            }
            if (null != fireWallJson) {
                HostUtil.setImportDataHandler(bindIp + "_FIREWALL", fireWallJson, nowTime);
            }
        }
        catch (Exception e) {
            logger.error("\u89e3\u6790\u4e3b\u673aFireWall\u91c7\u96c6\u6570\u636e\u9519\u8bef", (Throwable)e);
        }
    }

    private void addCacheWinServices(String winServicesJson, Date nowTime, String bindIp) {
        try {
            if (null != winServicesJson) {
                List<String> winServicesList = new ArrayList<String>(Arrays.asList(winServicesJson.split(",")));
                if (!StaticKeys.LICENSE_STATE.equals("1") && winServicesList.size() > 5) {
                    int allSize = winServicesList.size();
                    winServicesList = winServicesList.subList(0, 5);
                    winServicesList.add("\u63d0\u793a: \u5347\u7ea7\u5230\u4e13\u4e1a\u7248\u53ef\u4ee5\u67e5\u770b\u6240\u6709\u6570\u636e\u54e6 (\u5171\u6709" + allSize + "\u4e2a\u7cfb\u7edf\u670d\u52a1)");
                    winServicesJson = String.join((CharSequence)",", winServicesList);
                }
                HostUtil.setImportDataHandler(bindIp + "_WIN_SERVICES", winServicesJson, nowTime);
            }
        }
        catch (Exception e) {
            logger.error("\u89e3\u6790\u4e3b\u673a\u7684windows\u7cfb\u7edf\u670d\u52a1\u91c7\u96c6\u6570\u636e\u9519\u8bef", (Throwable)e);
        }
    }

    private void addCacheHostPortAll(String portAllDataJson, Date nowTime, String bindIp) {
        try {
            if (!LicenseUtil.checkEnterpriseVersion()) {
                HostUtil.setImportDataHandler(bindIp + "_ALLPORT", "\u8bf7\u5347\u7ea7\u5230\u4f01\u4e1a\u7248\u67e5\u770b\u6b64\u6570\u636e", nowTime);
                return;
            }
            if (null != portAllDataJson) {
                HostUtil.setImportDataHandler(bindIp + "_ALLPORT", portAllDataJson, nowTime);
            }
        }
        catch (Exception e) {
            logger.error("\u89e3\u6790\u4e3b\u673a\u5168\u91cf\u7aef\u53e3\u91c7\u96c6\u6570\u636e\u9519\u8bef", (Throwable)e);
        }
    }

    private void addCacheHostCrontab(String crontabJson, Date nowTime, String bindIp) {
        try {
            if (!LicenseUtil.checkEnterpriseVersion()) {
                HostUtil.setImportDataHandler(bindIp + "_CRONTAB", "\u8bf7\u5347\u7ea7\u5230\u4f01\u4e1a\u7248\u67e5\u770b\u6b64\u6570\u636e", nowTime);
                return;
            }
            if (null != crontabJson) {
                HostUtil.setImportDataHandler(bindIp + "_CRONTAB", crontabJson, nowTime);
            }
        }
        catch (Exception e) {
            logger.error("\u89e3\u6790\u4e3b\u673aCRONTAB\u91c7\u96c6\u6570\u636e\u9519\u8bef", (Throwable)e);
        }
    }

    private void addCacheHostLikeShell(String likeShellDataJson, Date nowTime, String bindIp) {
        try {
            if (!LicenseUtil.checkEnterpriseVersion()) {
                HostUtil.setImportDataHandler(bindIp + "_LIKE_SHELL", "\u8bf7\u5347\u7ea7\u5230\u4f01\u4e1a\u7248\u67e5\u770b\u6b64\u6570\u636e", nowTime);
                return;
            }
            if (null != likeShellDataJson) {
                HostUtil.setImportDataHandler(bindIp + "_LIKE_SHELL", likeShellDataJson, nowTime);
            }
        }
        catch (Exception e) {
            logger.error("\u89e3\u6790\u4e3b\u673a\u4e3b\u673a\u4e2a\u6027\u5316\u91c7\u96c6\u547d\u4ee4\u6570\u636e\u9519\u8bef", (Throwable)e);
        }
    }

    private void addCacheHostDockerAllList(String dockerAllListDataJson, Date nowTime, String bindIp) {
        try {
            if (null != dockerAllListDataJson) {
                HostUtil.setImportDataHandler(bindIp + "_ALL_DOCKER", dockerAllListDataJson, nowTime);
            }
        }
        catch (Exception e) {
            logger.error("\u89e3\u6790\u4e3b\u673a\u5168\u91cfdocker\u5217\u8868\u6570\u636e\u9519\u8bef", (Throwable)e);
        }
    }

    private boolean isExists(String bindIp) {
        try {
            if (StringUtils.isEmpty((CharSequence)bindIp)) {
                return true;
            }
            if (CollectionUtil.isEmpty(BatchData.DISK_STATE_LIST) && CollectionUtil.isEmpty(BatchData.DISK_IO_LIST) && CollectionUtil.isEmpty(BatchData.DISK_SMART_LIST)) {
                return false;
            }
            ArrayList<DiskState> diskStateList = new ArrayList<DiskState>();
            diskStateList.addAll(BatchData.DISK_STATE_LIST);
            for (DiskState diskState : diskStateList) {
                if (!bindIp.equals(diskState.getHostname())) continue;
                return true;
            }
            ArrayList<DiskIo> diskIoList = new ArrayList<DiskIo>();
            diskIoList.addAll(BatchData.DISK_IO_LIST);
            for (DiskIo diskIo : diskIoList) {
                if (!bindIp.equals(diskIo.getHostname())) continue;
                return true;
            }
            ArrayList<DiskSmart> arrayList = new ArrayList<DiskSmart>();
            arrayList.addAll(BatchData.DISK_SMART_LIST);
            for (DiskSmart diskSmart : arrayList) {
                if (!bindIp.equals(diskSmart.getHostname())) continue;
                return true;
            }
        }
        catch (Exception e) {
            logger.error("check bindIp\u9519\u8bef", (Throwable)e);
        }
        return false;
    }
}

