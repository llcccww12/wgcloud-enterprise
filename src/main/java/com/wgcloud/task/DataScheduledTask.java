/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.task;

import cn.hutool.core.collection.CollectionUtil;
import com.wgcloud.common.NettytHandler;
import com.wgcloud.config.CommonConfig;
import com.wgcloud.entity.AppExceptionInfo;
import com.wgcloud.entity.AppInfo;
import com.wgcloud.entity.AppState;
import com.wgcloud.entity.CpuState;
import com.wgcloud.entity.CpuTemperatures;
import com.wgcloud.entity.CustomInfo;
import com.wgcloud.entity.CustomState;
import com.wgcloud.entity.DbTableCount;
import com.wgcloud.entity.DceState;
import com.wgcloud.entity.DiskIo;
import com.wgcloud.entity.DiskIoState;
import com.wgcloud.entity.DiskSmart;
import com.wgcloud.entity.DiskState;
import com.wgcloud.entity.DockerInfo;
import com.wgcloud.entity.DockerState;
import com.wgcloud.entity.FileSafe;
import com.wgcloud.entity.FileWarnInfo;
import com.wgcloud.entity.FileWarnState;
import com.wgcloud.entity.GpuState;
import com.wgcloud.entity.HeathState;
import com.wgcloud.entity.HostDiskPer;
import com.wgcloud.entity.HostMacInfo;
import com.wgcloud.entity.HostPciInfo;
import com.wgcloud.entity.HostUsers;
import com.wgcloud.entity.MemState;
import com.wgcloud.entity.NetIoState;
import com.wgcloud.entity.PortInfo;
import com.wgcloud.entity.SnmpState;
import com.wgcloud.entity.SysLoadState;
import com.wgcloud.entity.SystemInfo;
import com.wgcloud.entity.SystemInfoExt;
import com.wgcloud.service.AppExceptionInfoService;
import com.wgcloud.service.AppInfoService;
import com.wgcloud.service.AppStateService;
import com.wgcloud.service.CpuStateService;
import com.wgcloud.service.CpuTemperaturesService;
import com.wgcloud.service.CustomInfoService;
import com.wgcloud.service.CustomStateService;
import com.wgcloud.service.DbTableCountService;
import com.wgcloud.service.DceStateService;
import com.wgcloud.service.DiskIoService;
import com.wgcloud.service.DiskIoStateService;
import com.wgcloud.service.DiskSmartService;
import com.wgcloud.service.DiskStateService;
import com.wgcloud.service.DockerInfoService;
import com.wgcloud.service.DockerStateService;
import com.wgcloud.service.FileSafeService;
import com.wgcloud.service.FileWarnInfoService;
import com.wgcloud.service.FileWarnStateService;
import com.wgcloud.service.GpuStateService;
import com.wgcloud.service.HeathStateService;
import com.wgcloud.service.HostDiskPerService;
import com.wgcloud.service.HostMacInfoService;
import com.wgcloud.service.HostPciInfoService;
import com.wgcloud.service.HostUsersService;
import com.wgcloud.service.K8sMonitorService;
import com.wgcloud.service.KafkaMonitorService;
import com.wgcloud.service.LargeModelService;
import com.wgcloud.service.LogInfoService;
import com.wgcloud.service.UfmMonitorService;
import com.wgcloud.service.MemStateService;
import com.wgcloud.service.NetIoStateService;
import com.wgcloud.service.PortInfoService;
import com.wgcloud.service.RedisMonitorService;
import com.wgcloud.service.ReportInfoService;
import com.wgcloud.service.ShellStateService;
import com.wgcloud.service.SnmpStateService;
import com.wgcloud.service.SysLoadStateService;
import com.wgcloud.service.SystemInfoExtService;
import com.wgcloud.service.SystemInfoService;
import com.wgcloud.service.TaskUtilService;
import com.wgcloud.util.ActivemqUtil;
import com.wgcloud.util.DateUtil;
import com.wgcloud.util.HostUtil;
import com.wgcloud.util.PhoneUtil;
import com.wgcloud.util.PowerEnvUtil;
import com.wgcloud.util.RabbitmqUtil;
import com.wgcloud.util.ScanIPUtil;
import com.wgcloud.util.ServerBackupUtil;
import com.wgcloud.util.ThreadPoolUtil;
import com.wgcloud.util.TomcatUtil;
import com.wgcloud.util.license.LicenseUtil;
import com.wgcloud.util.msg.WarnMailUtil;
import com.wgcloud.util.msg.WarnPools;
import com.wgcloud.util.staticvar.BatchData;
import com.wgcloud.util.staticvar.StaticKeys;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class DataScheduledTask {
    private Logger logger = LoggerFactory.getLogger(DataScheduledTask.class);
    @Autowired
    private SystemInfoService systemInfoService;
    @Autowired
    private DiskStateService diskStateService;
    @Autowired
    private HostMacInfoService hostMacInfoService;
    @Autowired
    private DiskIoService diskIoService;
    @Autowired
    private HostPciInfoService hostPciInfoService;
    @Autowired
    private DiskIoStateService diskIoStateService;
    @Autowired
    private DiskSmartService diskSmartService;
    @Autowired
    private HostUsersService hostUsersService;
    @Autowired
    private HostDiskPerService hostDiskPerService;
    @Autowired
    private CpuTemperaturesService cpuTemperaturesService;
    @Autowired
    private SystemInfoExtService systemInfoExtService;
    @Autowired
    private AppExceptionInfoService appExceptionInfoService;
    @Autowired
    private LogInfoService logInfoService;
    @Autowired
    private AppInfoService appInfoService;
    @Autowired
    private CustomInfoService customInfoService;
    @Autowired
    private FileWarnInfoService fileWarnInfoService;
    @Autowired
    private DockerInfoService dockerInfoService;
    @Autowired
    private PortInfoService portInfoService;
    @Autowired
    private FileSafeService fileSafeService;
    @Autowired
    private CpuStateService cpuStateService;
    @Autowired
    private MemStateService memStateService;
    @Autowired
    private NetIoStateService netIoStateService;
    @Autowired
    private SysLoadStateService sysLoadStateService;
    @Autowired
    private AppStateService appStateService;
    @Autowired
    private CustomStateService customStateService;
    @Autowired
    private FileWarnStateService fileWarnStateService;
    @Autowired
    private ShellStateService shellStateService;
    @Autowired
    private HeathStateService heathStateService;
    @Autowired
    private DceStateService dceStateService;
    @Autowired
    private SnmpStateService snmpStateService;
    @Autowired
    private DockerStateService dockerStateService;
    @Autowired
    private DbTableCountService dbTableCountService;
    @Autowired
    private ReportInfoService reportInfoService;
    @Autowired
    private K8sMonitorService k8sMonitorService;
    @Autowired
    private RedisMonitorService redisMonitorService;
    @Autowired
    private KafkaMonitorService kafkaMonitorService;
    @Autowired
    private TaskUtilService taskUtilService;
    @Autowired
    private CommonConfig commonConfig;
    @Autowired
    private LargeModelService largeModelService;
    @Autowired
    private GpuStateService gpuStateService;
    @Autowired
    private UfmMonitorService ufmMonitorService;

    @Scheduled(initialDelay=9000L, fixedRate=9000L)
    public synchronized void commitTask() {
        if (DateUtil.isClearTime()) {
            this.logger.info("\u6b63\u5728\u6e05\u7a7a\u5386\u53f2\u8d8b\u52bf\u56fe\u6570\u636e\uff0c\u4e0d\u6267\u884c\u63d0\u4ea4\u76d1\u63a7\u6570\u636e----------" + DateUtil.getCurrentDateTime());
            BatchData.clearAll();
            return;
        }
        this.logger.info("\u6279\u91cf\u63d0\u4ea4\u76d1\u63a7\u6570\u636e\u4efb\u52a1\u5f00\u59cb----------" + DateUtil.getCurrentDateTime());
        try {
            HashMap<String, Object> params = new HashMap<String, Object>();
            List<SystemInfo> savedList = this.systemInfoService.selectAllHostNameByParams(params);
            if (BatchData.SYSTEM_INFO_LIST.size() > 0) {
                this.batchCommitSystemInfo(savedList);
            }
            if (BatchData.APP_INFO_LIST.size() > 0) {
                this.batchCommitAppInfo();
            }
            if (BatchData.CUSTOM_INFO_LIST.size() > 0) {
                this.batchCommitCustomInfo();
            }
            if (BatchData.FILEWARN_INFO_LIST.size() > 0) {
                this.batchCommitFileWarnInfo();
            }
            if (BatchData.PORT_INFO_LIST.size() > 0) {
                this.batchCommitPortInfo();
            }
            if (BatchData.FILE_SAFE_LIST.size() > 0) {
                this.batchCommitFileSafes();
            }
            if (BatchData.DOCKER_INFO_LIST.size() > 0) {
                this.batchCommitDockerInfo();
            }
            if (BatchData.FILEWARN_STATE_LIST.size() > 0) {
                ArrayList<FileWarnState> FILEWARN_STATE_LIST = new ArrayList<FileWarnState>();
                FILEWARN_STATE_LIST.addAll(BatchData.FILEWARN_STATE_LIST);
                BatchData.FILEWARN_STATE_LIST.clear();
                this.fileWarnStateService.saveRecord(FILEWARN_STATE_LIST);
            }
            if (BatchData.APP_STATE_LIST.size() > 0) {
                ArrayList<AppState> APP_STATE_LIST = new ArrayList<AppState>();
                APP_STATE_LIST.addAll(BatchData.APP_STATE_LIST);
                BatchData.APP_STATE_LIST.clear();
                this.appStateService.saveRecord(APP_STATE_LIST);
            }
            if (BatchData.CUSTOM_STATE_LIST.size() > 0) {
                ArrayList<CustomState> CUSTOM_STATE_LIST = new ArrayList<CustomState>();
                CUSTOM_STATE_LIST.addAll(BatchData.CUSTOM_STATE_LIST);
                BatchData.CUSTOM_STATE_LIST.clear();
                this.customStateService.saveRecord(CUSTOM_STATE_LIST);
            }
            if (BatchData.HEATH_STATE_LIST.size() > 0) {
                ArrayList<HeathState> HEATH_STATE_LIST = new ArrayList<HeathState>();
                HEATH_STATE_LIST.addAll(BatchData.HEATH_STATE_LIST);
                BatchData.HEATH_STATE_LIST.clear();
                this.heathStateService.saveRecord(HEATH_STATE_LIST);
            }
            if (BatchData.DCE_STATE_LIST.size() > 0) {
                ArrayList<DceState> DCE_STATE_LIST = new ArrayList<DceState>();
                DCE_STATE_LIST.addAll(BatchData.DCE_STATE_LIST);
                BatchData.DCE_STATE_LIST.clear();
                this.dceStateService.saveRecord(DCE_STATE_LIST);
            }
            if (BatchData.DBTABLE_COUNT_LIST.size() > 0) {
                ArrayList<DbTableCount> DBTABLE_COUNT_LIST = new ArrayList<DbTableCount>();
                DBTABLE_COUNT_LIST.addAll(BatchData.DBTABLE_COUNT_LIST);
                BatchData.DBTABLE_COUNT_LIST.clear();
                this.dbTableCountService.saveRecord(DBTABLE_COUNT_LIST);
            }
            if (BatchData.SNMP_STATE_LIST.size() > 0) {
                ArrayList<SnmpState> SNMP_STATE_LIST = new ArrayList<SnmpState>();
                SNMP_STATE_LIST.addAll(BatchData.SNMP_STATE_LIST);
                BatchData.SNMP_STATE_LIST.clear();
                this.snmpStateService.saveRecord(SNMP_STATE_LIST);
            }
            if (BatchData.DOCKER_STATE_LIST.size() > 0) {
                ArrayList<DockerState> DOCKER_STATE_LIST = new ArrayList<DockerState>();
                DOCKER_STATE_LIST.addAll(BatchData.DOCKER_STATE_LIST);
                BatchData.DOCKER_STATE_LIST.clear();
                this.dockerStateService.saveRecord(DOCKER_STATE_LIST);
            }
            if (BatchData.CPU_STATE_LIST.size() > 0) {
                ArrayList<CpuState> CPU_STATE_LIST = new ArrayList<CpuState>();
                CPU_STATE_LIST.addAll(BatchData.CPU_STATE_LIST);
                BatchData.CPU_STATE_LIST.clear();
                this.cpuStateService.saveRecord(CPU_STATE_LIST);
            }
            if (BatchData.MEM_STATE_LIST.size() > 0) {
                ArrayList<MemState> MEM_STATE_LIST = new ArrayList<MemState>();
                MEM_STATE_LIST.addAll(BatchData.MEM_STATE_LIST);
                BatchData.MEM_STATE_LIST.clear();
                this.memStateService.saveRecord(MEM_STATE_LIST);
            }
            if (BatchData.NETIO_STATE_LIST.size() > 0) {
                ArrayList<NetIoState> NETIO_STATE_LIST = new ArrayList<NetIoState>();
                NETIO_STATE_LIST.addAll(BatchData.NETIO_STATE_LIST);
                BatchData.NETIO_STATE_LIST.clear();
                this.netIoStateService.saveRecord(NETIO_STATE_LIST);
            }
            if (BatchData.SYSLOAD_STATE_LIST.size() > 0) {
                ArrayList<SysLoadState> SYSLOAD_STATE_LIST = new ArrayList<SysLoadState>();
                SYSLOAD_STATE_LIST.addAll(BatchData.SYSLOAD_STATE_LIST);
                BatchData.SYSLOAD_STATE_LIST.clear();
                this.sysLoadStateService.saveRecord(SYSLOAD_STATE_LIST);
            }
            if (BatchData.DISK_STATE_LIST.size() > 0) {
                this.batchCommitDiskState();
            }
            if (BatchData.DISK_IO_LIST.size() > 0) {
                this.batchCommitDiskIo();
            }
            if (BatchData.HOST_PCI_LIST.size() > 0) {
                this.batchCommitHostPCI();
            }
            if (BatchData.DISK_SMART_LIST.size() > 0) {
                this.batchCommitDiskSmart();
            }
            if (BatchData.CPU_TEMPERATURES_LIST.size() > 0) {
                this.batchCommitCpuTemperatures();
            }
            if (BatchData.SYSTEM_INFO_EXT_LIST.size() > 0) {
                this.batchCommitSystemInfoExt();
            }
            if (BatchData.HOST_USERS_LIST.size() > 0) {
                this.batchCommitHostUsers();
            }
            if (BatchData.APP_EXCEPTION_INFO_LIST.size() > 0) {
                this.batchCommitAppExceptionInfos();
            }
            if (BatchData.HOST_MAC_LIST.size() > 0) {
                this.batchCommitMacAddress();
            }
            if (BatchData.DISK_IO_STATE_LIST.size() > 0) {
                ArrayList<DiskIoState> DISK_IO_STATE_LIST = new ArrayList<DiskIoState>();
                DISK_IO_STATE_LIST.addAll(BatchData.DISK_IO_STATE_LIST);
                BatchData.DISK_IO_STATE_LIST.clear();
                this.diskIoStateService.saveRecord(DISK_IO_STATE_LIST);
            }
            if (BatchData.GPU_RATE_LIST.size() > 0) {
                ArrayList<GpuState> GPU_STATE_LIST = new ArrayList<GpuState>();
                GPU_STATE_LIST.addAll(BatchData.GPU_RATE_LIST);
                BatchData.GPU_RATE_LIST.clear();
                this.gpuStateService.saveRecord(GPU_STATE_LIST);
            }
            if (BatchData.HOST_DISK_SUM_LIST.size() > 0) {
                this.diskStateService.addToUpdateSystemDisk(savedList);
            }
        }
        catch (Exception e) {
            this.logger.error("\u6279\u91cf\u63d0\u4ea4\u76d1\u63a7\u6570\u636e\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("\u6279\u91cf\u63d0\u4ea4\u76d1\u63a7\u6570\u636e\u9519\u8bef", e.toString(), "2");
        }
        this.logger.info("\u6279\u91cf\u63d0\u4ea4\u76d1\u63a7\u6570\u636e\u4efb\u52a1\u7ed3\u675f----------" + DateUtil.getCurrentDateTime());
    }

    private void batchCommitCpuTemperatures() {
        try {
            ArrayList<CpuTemperatures> CPU_TEMPERATURES_LIST = new ArrayList<CpuTemperatures>();
            CPU_TEMPERATURES_LIST.addAll(BatchData.CPU_TEMPERATURES_LIST);
            BatchData.CPU_TEMPERATURES_LIST.clear();
            ArrayList<String> hostnameList = new ArrayList<String>();
            for (CpuTemperatures deskState : CPU_TEMPERATURES_LIST) {
                if (hostnameList.contains(deskState.getHostname())) continue;
                hostnameList.add(deskState.getHostname());
            }
            if (!CollectionUtil.isEmpty(hostnameList)) {
                this.cpuTemperaturesService.deleteByAccHname(hostnameList);
            }
            this.cpuTemperaturesService.saveRecord(CPU_TEMPERATURES_LIST);
        }
        catch (Exception e) {
            this.logger.error("batchCommitCpuTemperatures error", (Throwable)e);
        }
    }

    private void batchCommitSystemInfoExt() {
        try {
            ArrayList<SystemInfoExt> SYSTEM_INFO_EXT_LIST = new ArrayList<SystemInfoExt>();
            SYSTEM_INFO_EXT_LIST.addAll(BatchData.SYSTEM_INFO_EXT_LIST);
            BatchData.SYSTEM_INFO_EXT_LIST.clear();
            ArrayList<String> hostnameList = new ArrayList<String>();
            for (SystemInfoExt systemInfoExt : SYSTEM_INFO_EXT_LIST) {
                if (hostnameList.contains(systemInfoExt.getHostname())) continue;
                hostnameList.add(systemInfoExt.getHostname());
            }
            if (!CollectionUtil.isEmpty(hostnameList)) {
                this.systemInfoExtService.deleteByHostname(hostnameList);
            }
            this.systemInfoExtService.saveRecord(SYSTEM_INFO_EXT_LIST);
        }
        catch (Exception e) {
            this.logger.error("batchCommitSystemInfoExt error", (Throwable)e);
        }
    }

    private void batchCommitAppExceptionInfos() {
        try {
            ArrayList<AppExceptionInfo> APP_EXCEPTION_INFO_LIST = new ArrayList<AppExceptionInfo>();
            APP_EXCEPTION_INFO_LIST.addAll(BatchData.APP_EXCEPTION_INFO_LIST);
            BatchData.APP_EXCEPTION_INFO_LIST.clear();
            ArrayList<String> hostnameList = new ArrayList<String>();
            for (AppExceptionInfo appExceptionInfo : APP_EXCEPTION_INFO_LIST) {
                if (hostnameList.contains(appExceptionInfo.getHostname())) continue;
                hostnameList.add(appExceptionInfo.getHostname());
            }
            if (!CollectionUtil.isEmpty(hostnameList)) {
                this.appExceptionInfoService.deleteByHostName(hostnameList);
            }
            this.appExceptionInfoService.saveRecord(APP_EXCEPTION_INFO_LIST);
        }
        catch (Exception e) {
            this.logger.error("batchCommitAppExceptionInfos error", (Throwable)e);
        }
    }

    private void batchCommitDiskSmart() {
        try {
            ArrayList<DiskSmart> DISK_SMART_LIST = new ArrayList<DiskSmart>();
            DISK_SMART_LIST.addAll(BatchData.DISK_SMART_LIST);
            BatchData.DISK_SMART_LIST.clear();
            ArrayList<String> hostnameList = new ArrayList<String>();
            for (DiskSmart diskSmart : DISK_SMART_LIST) {
                if (hostnameList.contains(diskSmart.getHostname())) continue;
                hostnameList.add(diskSmart.getHostname());
            }
            if (!CollectionUtil.isEmpty(hostnameList)) {
                this.diskSmartService.deleteByAccHname(hostnameList);
            }
            this.diskSmartService.saveRecord(DISK_SMART_LIST);
        }
        catch (Exception e) {
            this.logger.error("batchCommitDiskSmart error", (Throwable)e);
        }
    }

    private void batchCommitHostUsers() {
        try {
            ArrayList<HostUsers> HOST_USERS_LIST = new ArrayList<HostUsers>();
            HOST_USERS_LIST.addAll(BatchData.HOST_USERS_LIST);
            BatchData.HOST_USERS_LIST.clear();
            ArrayList<String> hostnameList = new ArrayList<String>();
            for (HostUsers hostUsers : HOST_USERS_LIST) {
                if (hostnameList.contains(hostUsers.getHostname())) continue;
                hostnameList.add(hostUsers.getHostname());
            }
            if (!CollectionUtil.isEmpty(hostnameList)) {
                this.hostUsersService.deleteByAccHname(hostnameList);
            }
            this.hostUsersService.saveRecord(HOST_USERS_LIST);
        }
        catch (Exception e) {
            this.logger.error("batchCommitHostUsers error", (Throwable)e);
        }
    }

    private void batchCommitDiskIo() {
        try {
            ArrayList<DiskIo> DISK_IO_LIST = new ArrayList<DiskIo>();
            DISK_IO_LIST.addAll(BatchData.DISK_IO_LIST);
            BatchData.DISK_IO_LIST.clear();
            ArrayList<String> hostnameList = new ArrayList<String>();
            for (DiskIo diskState : DISK_IO_LIST) {
                if (hostnameList.contains(diskState.getHostname())) continue;
                hostnameList.add(diskState.getHostname());
            }
            if (!CollectionUtil.isEmpty(hostnameList)) {
                this.diskIoService.deleteByAccHname(hostnameList);
            }
            this.diskIoService.saveRecord(DISK_IO_LIST);
        }
        catch (Exception e) {
            this.logger.error("batchCommitDiskIo error", (Throwable)e);
        }
    }

    private void batchCommitHostPCI() {
        try {
            ArrayList<HostPciInfo> HOST_PCI_LIST = new ArrayList<HostPciInfo>();
            HOST_PCI_LIST.addAll(BatchData.HOST_PCI_LIST);
            BatchData.HOST_PCI_LIST.clear();
            ArrayList<String> hostnameList = new ArrayList<String>();
            for (HostPciInfo hostPciInfo : HOST_PCI_LIST) {
                if (hostnameList.contains(hostPciInfo.getHostname())) continue;
                hostnameList.add(hostPciInfo.getHostname());
            }
            if (!CollectionUtil.isEmpty(hostnameList)) {
                this.hostPciInfoService.deleteByHostname(hostnameList);
            }
            this.hostPciInfoService.saveRecord(HOST_PCI_LIST);
        }
        catch (Exception e) {
            this.logger.error("batchCommitHostPCI error", (Throwable)e);
        }
    }

    private void batchCommitDiskState() {
        try {
            ArrayList<DiskState> DISK_STATE_LIST = new ArrayList<DiskState>();
            DISK_STATE_LIST.addAll(BatchData.DISK_STATE_LIST);
            BatchData.DISK_STATE_LIST.clear();
            ArrayList<String> hostnameList = new ArrayList<String>();
            for (DiskState diskState : DISK_STATE_LIST) {
                if (hostnameList.contains(diskState.getHostname())) continue;
                hostnameList.add(diskState.getHostname());
            }
            if (!CollectionUtil.isEmpty(hostnameList)) {
                this.diskStateService.deleteByAccHname(hostnameList);
            }
            this.diskStateService.saveRecord(DISK_STATE_LIST);
        }
        catch (Exception e) {
            this.logger.error("batchCommitDeskState error", (Throwable)e);
        }
    }

    private void batchCommitMacAddress() {
        try {
            ArrayList<HostMacInfo> HOST_MAC_LIST = new ArrayList<HostMacInfo>();
            HOST_MAC_LIST.addAll(BatchData.HOST_MAC_LIST);
            BatchData.HOST_MAC_LIST.clear();
            ArrayList<String> hostnameList = new ArrayList<String>();
            for (HostMacInfo hostMacInfo : HOST_MAC_LIST) {
                if (hostnameList.contains(hostMacInfo.getHostname())) continue;
                hostnameList.add(hostMacInfo.getHostname());
            }
            if (!CollectionUtil.isEmpty(hostnameList)) {
                this.hostMacInfoService.deleteByAccHname(hostnameList);
            }
            this.hostMacInfoService.saveRecord(HOST_MAC_LIST);
        }
        catch (Exception e) {
            this.logger.error("batchCommitMacAddress error", (Throwable)e);
        }
    }

    private void batchCommitDockerInfo() {
        try {
            ArrayList<DockerInfo> DOCKER_INFO_LIST = new ArrayList<DockerInfo>();
            DOCKER_INFO_LIST.addAll(BatchData.DOCKER_INFO_LIST);
            BatchData.DOCKER_INFO_LIST.clear();
            this.dockerInfoService.updateRecord(DOCKER_INFO_LIST);
        }
        catch (Exception e) {
            this.logger.error("batchCommitDockerInfo error", (Throwable)e);
        }
    }

    private void batchCommitAppInfo() {
        try {
            ArrayList<AppInfo> APP_INFO_LIST = new ArrayList<AppInfo>();
            APP_INFO_LIST.addAll(BatchData.APP_INFO_LIST);
            BatchData.APP_INFO_LIST.clear();
            this.appInfoService.updateRecord(APP_INFO_LIST);
        }
        catch (Exception e) {
            this.logger.error("batchCommitAppInfo error", (Throwable)e);
        }
    }

    private void batchCommitCustomInfo() {
        try {
            ArrayList<CustomInfo> CUSTOM_INFO_LIST = new ArrayList<CustomInfo>();
            CUSTOM_INFO_LIST.addAll(BatchData.CUSTOM_INFO_LIST);
            BatchData.CUSTOM_INFO_LIST.clear();
            this.customInfoService.updateRecord(CUSTOM_INFO_LIST);
        }
        catch (Exception e) {
            this.logger.error("batchCommitCustomInfo error", (Throwable)e);
        }
    }

    private void batchCommitFileWarnInfo() {
        try {
            ArrayList<FileWarnInfo> FILEWARN_INFO_LIST = new ArrayList<FileWarnInfo>();
            FILEWARN_INFO_LIST.addAll(BatchData.FILEWARN_INFO_LIST);
            BatchData.FILEWARN_INFO_LIST.clear();
            this.fileWarnInfoService.updateRecord(FILEWARN_INFO_LIST);
        }
        catch (Exception e) {
            this.logger.error("batchCommitSystemInfo error", (Throwable)e);
        }
    }

    private void batchCommitPortInfo() {
        try {
            ArrayList<PortInfo> PORT_INFO_LIST = new ArrayList<PortInfo>();
            PORT_INFO_LIST.addAll(BatchData.PORT_INFO_LIST);
            BatchData.PORT_INFO_LIST.clear();
            this.portInfoService.updateRecord(PORT_INFO_LIST);
        }
        catch (Exception e) {
            this.logger.error("batchCommitSystemInfo error", (Throwable)e);
        }
    }

    private void batchCommitFileSafes() {
        try {
            ArrayList<FileSafe> FILE_SAFE_LIST = new ArrayList<FileSafe>();
            FILE_SAFE_LIST.addAll(BatchData.FILE_SAFE_LIST);
            BatchData.FILE_SAFE_LIST.clear();
            this.fileSafeService.updateRecord(FILE_SAFE_LIST);
        }
        catch (Exception e) {
            this.logger.error("batchCommitSystemInfo error", (Throwable)e);
        }
    }

    private void batchCommitSystemInfo(List<SystemInfo> savedList) {
        try {
            ArrayList<SystemInfo> SYSTEM_INFO_LIST = new ArrayList<SystemInfo>();
            SYSTEM_INFO_LIST.addAll(BatchData.SYSTEM_INFO_LIST);
            BatchData.SYSTEM_INFO_LIST.clear();
            ArrayList<SystemInfo> updateList = new ArrayList<SystemInfo>();
            ArrayList<SystemInfo> insertList = new ArrayList<SystemInfo>();
            for (SystemInfo systemInfo : SYSTEM_INFO_LIST) {
                if (StringUtils.isEmpty((CharSequence)systemInfo.getHostname())) continue;
                boolean isSaved = false;
                for (SystemInfo systemInfoS : savedList) {
                    if (!systemInfoS.getHostname().equals(systemInfo.getHostname())) continue;
                    systemInfo.setId(systemInfoS.getId());
                    updateList.add(systemInfo);
                    isSaved = true;
                    break;
                }
                if (isSaved) continue;
                insertList.add(systemInfo);
            }
            this.systemInfoService.updateRecord(updateList);
            this.systemInfoService.saveRecord(insertList);
            for (SystemInfo systemInfoNew : insertList) {
                Runnable runnable = () -> WarnMailUtil.sendHostDown(systemInfoNew, false, true);
                ThreadPoolUtil.executor.execute(runnable);
            }
        }
        catch (Exception e) {
            this.logger.error("batchCommitSystemInfo error", (Throwable)e);
        }
    }

    private void addHostDiskPer() {
        try {
            this.logger.info("\u6240\u6709\u4e3b\u673a\u90fd\u65b0\u589e\u4e00\u6761\u78c1\u76d8\u603b\u4f7f\u7528\u7387\u4fe1\u606f");
            Timestamp nowtime = DateUtil.getNowTime();
            ArrayList<HostDiskPer> hostDiskPerList = new ArrayList<HostDiskPer>();
            HashMap<String, Object> params = new HashMap<String, Object>();
            List<SystemInfo> systemInfoList = this.systemInfoService.selectAllByParams(params);
            for (SystemInfo systemInfo1 : systemInfoList) {
                HostDiskPer hostDiskPer = new HostDiskPer();
                hostDiskPer.setCreateTime(nowtime);
                hostDiskPer.setDiskSumPer(systemInfo1.getDiskPer());
                hostDiskPer.setHostname(systemInfo1.getHostname());
                hostDiskPerList.add(hostDiskPer);
            }
            this.hostDiskPerService.saveRecord(hostDiskPerList);
        }
        catch (Exception e) {
            this.logger.error("addHostDiskPer error", (Throwable)e);
        }
    }

    @Scheduled(cron="55 24 6 * * ?")
    public void refreshCommitDate() {
        try {
            if (!StaticKeys.HISTORY_DATA_TIME_OVER) {
                StaticKeys.HISTORY_DATA_TIME_OVER = true;
                if (!"master".equals(this.commonConfig.getNodeType())) {
                    this.logger.info("slave\u8282\u70b9\u4e0d\u6267\u884c\u5237\u65b0\u76d1\u63a7\u6570\u636e\u66f4\u65b0\u65f6\u95f4\u4efb\u52a1");
                    return;
                }
                this.taskUtilService.refreshCommitDate();
            }
        }
        catch (Exception e) {
            this.logger.error("\u5237\u65b0\u76d1\u63a7\u6570\u636e\u66f4\u65b0\u65f6\u95f4\u9519\u8bef", (Throwable)e);
        }
    }

    @Scheduled(cron="0 5 6 * * ?")
    public void clearHisDataTask() {
        this.logger.info("\u5b9a\u65f6\u6e05\u7a7a\u5386\u53f2\u8d8b\u52bf\u56fe\u6570\u636e\u4efb\u52a1\u5f00\u59cb----------" + DateUtil.getCurrentDateTime());
        this.resetCacheData();
        if (!"master".equals(this.commonConfig.getNodeType())) {
            this.logger.info("slave\u8282\u70b9\u4e0d\u6267\u884c\u6e05\u7a7a\u5386\u53f2\u8d8b\u52bf\u56fe\u6570\u636e\u4efb\u52a1");
            return;
        }
        int historyDay = this.commonConfig.getHistoryDataOut();
        if (!StaticKeys.LICENSE_STATE.equals("1") && historyDay > 5) {
            this.logger.info("\u4e2a\u4eba\u7248\u4fdd\u7559\u6700\u8fd15\u5929\u7684\u6570\u636e");
            historyDay = 5;
        }
        this.logger.info("\u6e05\u7a7a" + historyDay + "\u5929\u524d\u7684\u76d1\u63a7\u5386\u53f2\u8d8b\u52bf\u56fe\u6570\u636e");
        String thrityDayBefore = DateUtil.getDateBefore(historyDay);
        HashMap<String, Object> paramsDel = new HashMap<String, Object>();
        try {
            StaticKeys.HISTORY_DATA_TIME_OVER = false;
            paramsDel.put("endTime", thrityDayBefore);
            if (paramsDel.get("endTime") != null && !"".equals(paramsDel.get("endTime"))) {
                this.cpuStateService.deleteByDate(paramsDel);
                this.memStateService.deleteByDate(paramsDel);
                this.netIoStateService.deleteByDate(paramsDel);
                this.sysLoadStateService.deleteByDate(paramsDel);
                this.appStateService.deleteByDate(paramsDel);
                this.appExceptionInfoService.deleteByDate(paramsDel);
                this.dockerStateService.deleteByDate(paramsDel);
                this.diskStateService.deleteByDate(paramsDel);
                this.hostMacInfoService.deleteByDate(paramsDel);
                this.diskIoService.deleteByDate(paramsDel);
                this.hostPciInfoService.deleteByDate(paramsDel);
                this.diskSmartService.deleteByDate(paramsDel);
                this.hostUsersService.deleteByDate(paramsDel);
                this.cpuTemperaturesService.deleteByDate(paramsDel);
                this.heathStateService.deleteByDate(paramsDel);
                this.dceStateService.deleteByDate(paramsDel);
                this.logInfoService.deleteByDate(paramsDel);
                this.dbTableCountService.deleteByDate(paramsDel);
                this.fileWarnStateService.deleteByDate(paramsDel);
                this.hostDiskPerService.deleteByDate(paramsDel);
                this.snmpStateService.deleteByDate(paramsDel);
                this.customStateService.deleteByDate(paramsDel);
                this.k8sMonitorService.deleteByDate(paramsDel);
                this.kafkaMonitorService.deleteByDate(paramsDel);
                this.redisMonitorService.deleteByDate(paramsDel);
                this.ufmMonitorService.deleteByDate(paramsDel);
                if (historyDay < 30) {
                    String thrityDayBeforeForShell = DateUtil.getDateBefore(30);
                    HashMap<String, Object> paramsDelForShell = new HashMap<String, Object>();
                    paramsDelForShell.put("endTime", thrityDayBeforeForShell);
                    this.shellStateService.deleteByDate(paramsDelForShell);
                } else {
                    this.shellStateService.deleteByDate(paramsDel);
                }
                if (historyDay > 7) {
                    String thrityDayBeforeForDiskIoState = DateUtil.getDateBefore(7);
                    HashMap<String, Object> paramsDelForDiskIoState = new HashMap<String, Object>();
                    paramsDelForDiskIoState.put("endTime", thrityDayBeforeForDiskIoState);
                    this.diskIoStateService.deleteByDate(paramsDelForDiskIoState);
                } else {
                    this.diskIoStateService.deleteByDate(paramsDel);
                }
                String hoursAgoTimeFor24 = DateUtil.beforeHourToNowDate(23);
                HashMap<String, Object> paramsDelFoGpuState = new HashMap<String, Object>();
                paramsDelFoGpuState.put("endTime", hoursAgoTimeFor24);
                this.gpuStateService.deleteByDate(paramsDelFoGpuState);
                this.addHostDiskPer();
                this.reportInfoService.keepLastTenDayForFree();
                this.logInfoService.save("\u5b9a\u65f6\u6e05\u7a7a\u5386\u53f2\u8d8b\u52bf\u56fe\u6570\u636e\u5b8c\u6210", "\u6bcf\u59296:05\u6e05\u7a7a" + historyDay + "\u5929(" + thrityDayBefore + ")\u524d\u7684\u76d1\u63a7\u5386\u53f2\u8d8b\u52bf\u56fe\u6570\u636e\uff0c\u4efb\u52a1\u5b8c\u6210", "2");
            }
        }
        catch (Exception e) {
            this.logger.error("\u5b9a\u65f6\u6e05\u7a7a\u5386\u53f2\u8d8b\u52bf\u56fe\u6570\u636e\u4efb\u52a1\u51fa\u9519", (Throwable)e);
            this.logInfoService.save("\u5b9a\u65f6\u6e05\u7a7a\u5386\u53f2\u8d8b\u52bf\u56fe\u6570\u636e\u9519\u8bef", e.toString(), "2");
        }
        StaticKeys.HISTORY_DATA_TIME_OVER = true;
        this.logger.info("\u5b9a\u65f6\u6e05\u7a7a\u5386\u53f2\u8d8b\u52bf\u56fe\u6570\u636e\u4efb\u52a1\u7ed3\u675f----------" + DateUtil.getCurrentDateTime());
    }

    private void resetCacheData() {
        this.logger.info("\u6e05\u7a7a\u544a\u8b66\u6807\u8bb0");
        WarnPools.clearOldData();
        this.logger.info("\u6e05\u7a7a\u767b\u5f55\u8d26\u53f7\u767b\u5f55\u9519\u8bef\u7f13\u5b58\u8bb0\u5f55");
        StaticKeys.LOGIN_BLOCK_MAP.clear();
        StaticKeys.LOGIN_ERROR_MAP.clear();
        PhoneUtil.LOGIN_PHONE_CODE_MAP.clear();
        this.logger.info("\u6e05\u7a7aweb ssh\u7ec8\u7aef\u7f13\u5b58");
        NettytHandler.clearOldData();
        HostUtil.clearCacheMap();
        ServerBackupUtil.clearCacheIdList();
        StaticKeys.HOST_ALL_PROCESS.clear();
        StaticKeys.HOST_IMPORT_DATA.clear();
        PowerEnvUtil.POWER_ENV_DATA_MAP.clear();
        StaticKeys.MESSAGE_ERROR_MAP.clear();
        RabbitmqUtil.RABBITMQ_DATA_MAP.clear();
        ActivemqUtil.ACTIVEMQ_DATA_MAP.clear();
        TomcatUtil.TOMCAT_DATA_MAP.clear();
        ScanIPUtil.SCAN_IP_DATA_MAP.clear();
        ScanIPUtil.SCAN_NAME_DATA_MAP.clear();
    }

    @Scheduled(cron="0 0 5 ? * MON")
    public void diskCacheHandleTask() {
        this.logger.info("\u5b9a\u65f6\u5904\u7406\u78c1\u76d8\u7a7a\u95f4\u7684\u7f13\u5b58\u6570\u636e\u4efb\u52a1----------" + DateUtil.getCurrentDateTime());
        try {
            this.diskStateService.initDiskComputeCache();
        }
        catch (Exception e) {
            this.logger.error("\u5b9a\u65f6\u5904\u7406\u78c1\u76d8\u7a7a\u95f4\u7684\u7f13\u5b58\u6570\u636e\u4efb\u52a1\u9519\u8bef", (Throwable)e);
        }
    }

    @Scheduled(cron="0 55 23 * * ?")
    public void generateReportDayTask() {
        if (!"master".equals(this.commonConfig.getNodeType())) {
            this.logger.info("slave\u8282\u70b9\u4e0d\u6267\u884c\u751f\u6210\u5de1\u68c0\u65e5\u62a5\u4efb\u52a1");
            return;
        }
        this.logger.info("\u5b9a\u65f6\u751f\u6210\u5de1\u68c0\u65e5\u62a5\u4efb\u52a1----------" + DateUtil.getCurrentDateTime());
        try {
            this.reportInfoService.taskDayThreadHandler();
        }
        catch (Exception e) {
            this.logger.error("\u5b9a\u65f6\u751f\u6210\u5de1\u68c0\u65e5\u62a5\u4efb\u52a1\u9519\u8bef", (Throwable)e);
        }
    }

    @Scheduled(cron="0 0 4 ? * MON-FRI")
    public void generateReportWeekTask() {
        if (!"master".equals(this.commonConfig.getNodeType())) {
            this.logger.info("slave\u8282\u70b9\u4e0d\u6267\u884c\u751f\u6210\u5de1\u68c0\u5468\u62a5\u4efb\u52a1");
            return;
        }
        this.logger.info("\u5b9a\u65f6\u751f\u6210\u5de1\u68c0\u5468\u62a5\u4efb\u52a1----------" + DateUtil.getCurrentDateTime());
        try {
            this.reportInfoService.taskWeekThreadHandler();
        }
        catch (Exception e) {
            this.logger.error("\u5b9a\u65f6\u751f\u6210\u5de1\u68c0\u5468\u62a5\u4efb\u52a1\u9519\u8bef", (Throwable)e);
        }
    }

    @Scheduled(cron="0 0 1 1-8 * ?")
    public void generateReportMonTask() {
        if (!"master".equals(this.commonConfig.getNodeType())) {
            this.logger.info("slave\u8282\u70b9\u4e0d\u6267\u884c\u751f\u6210\u5de1\u68c0\u6708\u62a5\u4efb\u52a1");
            return;
        }
        this.logger.info("\u5b9a\u65f6\u751f\u6210\u5de1\u68c0\u6708\u62a5\u4efb\u52a1----------" + DateUtil.getCurrentDateTime());
        try {
            this.reportInfoService.taskMonThreadHandler();
        }
        catch (Exception e) {
            this.logger.error("\u5b9a\u65f6\u751f\u6210\u5de1\u68c0\u6708\u62a5\u4efb\u52a1\u9519\u8bef", (Throwable)e);
        }
    }

    @Scheduled(cron="0 0 11 ? * MON")
    public void countWarnInfoLastWeekTask() {
        if (!"master".equals(this.commonConfig.getNodeType())) {
            this.logger.info("slave\u8282\u70b9\u4e0d\u6267\u884c\u751f\u6210\u5de1\u68c0\u5468\u62a5\u4efb\u52a1");
            return;
        }
        this.logger.info("\u5b9a\u65f6\u751f\u6210\u4e0a\u4e00\u5468\u7684\u544a\u8b66\u6b21\u6570\u548c\u6062\u590d\u6b21\u6570\u4efb\u52a1----------" + DateUtil.getCurrentDateTime());
        try {
            this.reportInfoService.countWarnInfoLastWeekHandler();
        }
        catch (Exception e) {
            this.logger.error("\u5b9a\u65f6\u751f\u6210\u5de1\u68c0\u5468\u62a5\u4efb\u52a1\u9519\u8bef", (Throwable)e);
        }
    }

    @Scheduled(cron="0 30 1 * * ?")
    public void generateReportLLMTask() {
        if (!"master".equals(this.commonConfig.getNodeType())) {
            this.logger.info("slave\u8282\u70b9\u4e0d\u6267\u884c\u751f\u6210LLM\u6570\u636e\u6587\u4ef6\u4efb\u52a1");
            return;
        }
        this.logger.info("\u5b9a\u65f6\u751f\u6210LLM\u6570\u636e\u6587\u4ef6\u4efb\u52a1\u5f00\u59cb----------" + DateUtil.getCurrentDateTime());
        if (!"true".equals(this.commonConfig.getLlmData())) {
            this.logger.info("LLM\u6570\u636e\u6587\u4ef6\u5b9a\u65f6\u751f\u6210\u4efb\u52a1\u5df2\u5173\u95ed");
            return;
        }
        try {
            if (!LicenseUtil.checkEnterpriseVersion()) {
                this.logger.info("\u5f53\u524d\u4e0d\u662f\u4f01\u4e1a\u7248\uff0c\u6240\u4ee5\u4e0d\u751f\u6210LLM\u6570\u636e\u6587\u4ef6");
                return;
            }
            this.largeModelService.exportHostListSnapExcel();
            this.largeModelService.exportDiskStateExcel();
            this.largeModelService.exportDceStateExcel();
            this.largeModelService.exportHostAllProcsExcel();
            this.logger.info("\u5b9a\u65f6\u751f\u6210LLM\u6570\u636e\u6587\u4ef6\u4efb\u52a1\u7ed3\u675f----------" + DateUtil.getCurrentDateTime());
        }
        catch (Exception e) {
            this.logger.error("\u5b9a\u65f6\u751f\u6210LLM\u6570\u636e\u6587\u4ef6\u4efb\u52a1\u9519\u8bef", (Throwable)e);
        }
    }
}

