/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wgcloud.config.CommonConfig;
import com.wgcloud.config.MailConfig;
import com.wgcloud.entity.CpuState;
import com.wgcloud.entity.CpuTemperatures;
import com.wgcloud.entity.DiskIoState;
import com.wgcloud.entity.HostDiskPer;
import com.wgcloud.entity.HostGroup;
import com.wgcloud.entity.MemState;
import com.wgcloud.entity.NetIoState;
import com.wgcloud.entity.ReportInfo;
import com.wgcloud.entity.ReportInstance;
import com.wgcloud.entity.SysLoadState;
import com.wgcloud.entity.SystemInfo;
import com.wgcloud.entity.SystemInfoExt;
import com.wgcloud.mapper.ReportInfoMapper;
import com.wgcloud.service.AccountInfoService;
import com.wgcloud.service.AppExceptionInfoService;
import com.wgcloud.service.AppInfoService;
import com.wgcloud.service.CpuStateService;
import com.wgcloud.service.CpuTemperaturesService;
import com.wgcloud.service.CustomInfoService;
import com.wgcloud.service.DbInfoService;
import com.wgcloud.service.DbTableService;
import com.wgcloud.service.DceInfoService;
import com.wgcloud.service.DiskIoStateService;
import com.wgcloud.service.DockerInfoService;
import com.wgcloud.service.EquipmentService;
import com.wgcloud.service.FileSafeService;
import com.wgcloud.service.FileWarnInfoService;
import com.wgcloud.service.FtpInfoService;
import com.wgcloud.service.HeathMonitorService;
import com.wgcloud.service.HostDiskPerService;
import com.wgcloud.service.HostGroupService;
import com.wgcloud.service.HostWarnDiyService;
import com.wgcloud.service.K8sMonitorService;
import com.wgcloud.service.KafkaMonitorService;
import com.wgcloud.service.LogInfoService;
import com.wgcloud.service.MemStateService;
import com.wgcloud.service.NetIoStateService;
import com.wgcloud.service.PasswdInfoService;
import com.wgcloud.service.PortInfoService;
import com.wgcloud.service.RedisMonitorService;
import com.wgcloud.service.ReportInstanceService;
import com.wgcloud.service.SnmpDeepInfoService;
import com.wgcloud.service.SnmpInfoService;
import com.wgcloud.service.SysLoadStateService;
import com.wgcloud.service.SystemInfoExtService;
import com.wgcloud.service.SystemInfoService;
import com.wgcloud.service.TaskJobInfoService;
import com.wgcloud.service.TaskUtilService;
import com.wgcloud.util.ActivemqUtil;
import com.wgcloud.util.DateUtil;
import com.wgcloud.util.FormatUtil;
import com.wgcloud.util.HostUtil;
import com.wgcloud.util.RabbitmqUtil;
import com.wgcloud.util.ScanIPUtil;
import com.wgcloud.util.ThreadPoolUtil;
import com.wgcloud.util.TomcatUtil;
import com.wgcloud.util.UUIDUtil;
import com.wgcloud.util.msg.WarnOtherUtil;
import com.wgcloud.util.staticvar.StaticKeys;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.ServletContext;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReportInfoService {
    private static final Logger logger = LoggerFactory.getLogger(ReportInfoService.class);
    @Autowired
    private ReportInfoMapper reportInfoMapper;
    @Autowired
    private ReportInstanceService reportInstanceService;
    @Autowired
    private SystemInfoService systemInfoService;
    @Autowired
    private SystemInfoExtService systemInfoExtService;
    @Autowired
    private LogInfoService logInfoService;
    @Autowired
    private AppInfoService appInfoService;
    @Autowired
    private FileSafeService fileSafeService;
    @Autowired
    private DockerInfoService dockerInfoService;
    @Autowired
    private PortInfoService portInfoService;
    @Autowired
    private AppExceptionInfoService appExceptionInfoService;
    @Autowired
    private EquipmentService equipmentService;
    @Autowired
    private AccountInfoService accountInfoService;
    @Resource
    private PasswdInfoService passwdInfoService;
    @Resource
    private HostWarnDiyService hostWarnDiyService;
    @Autowired
    private CustomInfoService customInfoService;
    @Autowired
    CpuStateService cpuStateService;
    @Autowired
    MemStateService memStateService;
    @Autowired
    SysLoadStateService sysLoadStateService;
    @Autowired
    NetIoStateService netIoStateService;
    @Autowired
    private DbTableService dbTableService;
    @Resource
    DbInfoService dbInfoService;
    @Resource
    FileWarnInfoService fileWarnInfoService;
    @Autowired
    private FtpInfoService ftpInfoService;
    @Autowired
    private HostGroupService hostGroupService;
    @Autowired
    private HeathMonitorService heathMonitorService;
    @Autowired
    private DceInfoService dceInfoService;
    @Autowired
    private SnmpInfoService snmpInfoService;
    @Resource
    private HostDiskPerService hostDiskPerService;
    @Autowired
    private ServletContext servletContext;
    @Autowired
    private CommonConfig commonConfig;
    @Autowired
    private TaskUtilService taskUtilService;
    @Resource
    private K8sMonitorService k8sMonitorService;
    @Resource
    private RedisMonitorService redisMonitorService;
    @Resource
    private KafkaMonitorService kafkaMonitorService;
    @Resource
    private CpuTemperaturesService cpuTemperaturesService;
    @Resource
    private TaskJobInfoService taskJobInfoService;
    @Resource
    private DiskIoStateService diskIoStateService;
    @Resource
    private SnmpDeepInfoService snmpDeepInfoService;
    @Autowired
    private MailConfig mailConfig;

    public PageInfo selectByParams(Map<String, Object> params, int currPage, int pageSize) throws Exception {
        PageHelper.startPage((int)currPage, (int)pageSize);
        List<ReportInfo> list = this.reportInfoMapper.selectByParams(params);
        PageInfo pageInfo = new PageInfo(list);
        return pageInfo;
    }

    public void save(ReportInfo reportInfo) {
        reportInfo.setId(UUIDUtil.getUUID());
        reportInfo.setCreateTime(new Date());
        try {
            this.reportInfoMapper.save(reportInfo);
        }
        catch (Exception e) {
            logger.error("\u4fdd\u5b58\u5de1\u68c0\u62a5\u544a\u4fe1\u606f\u5f02\u5e38\uff1a", (Throwable)e);
        }
    }

    public int countByParams(Map<String, Object> params) throws Exception {
        return this.reportInfoMapper.countByParams(params);
    }

    public int deleteById(String[] id) throws Exception {
        return this.reportInfoMapper.deleteById(id);
    }

    public ReportInfo selectById(String id) throws Exception {
        return this.reportInfoMapper.selectById(id);
    }

    public List<ReportInfo> selectAllByParams(Map<String, Object> params) throws Exception {
        return this.reportInfoMapper.selectAllByParams(params);
    }

    public int deleteByDate(Map<String, Object> map) throws Exception {
        return this.reportInfoMapper.deleteByDate(map);
    }

    public void keepLastTenDayForFree() {
        if (StaticKeys.LICENSE_STATE.equals("1")) {
            return;
        }
        try {
            String thrityDayBefore = DateUtil.getDateBefore(10);
            HashMap<String, Object> paramsDel = new HashMap<String, Object>();
            paramsDel.put("endTime", thrityDayBefore);
            this.deleteByDate(paramsDel);
            this.logInfoService.save("\u4e2a\u4eba\u7248\u5de1\u68c0\u62a5\u544a\u5b9a\u65f6\u6e05\u7a7a10\u5929\u524d\u7684\u6570\u636e\uff0c\u4efb\u52a1\u5df2\u5b8c\u6210", "\u4e2a\u4eba\u7248\u5de1\u68c0\u62a5\u544a\u53ea\u80fd\u4fdd\u7559\u6700\u8fd110\u5929(\u5373" + thrityDayBefore + "\u540e)\u7684\u6570\u636e\uff0c\u4e13\u4e1a\u7248\u5219\u65e0\u9650\u5236", "2");
            logger.info("\u4e2a\u4eba\u7248\u5de1\u68c0\u62a5\u544a\u5b9a\u65f6\u6e05\u7a7a10\u5929\u524d\u7684\u6570\u636e\uff0c\u4efb\u52a1\u5df2\u5b8c\u6210\uff0c\u4e2a\u4eba\u7248\u5de1\u68c0\u62a5\u544a\u4fdd\u7559\u6700\u8fd110\u5929(\u5373" + thrityDayBefore + "\u540e)\u7684\u6570\u636e");
        }
        catch (Exception e) {
            logger.error("\u5de1\u68c0\u62a5\u544a\u6e05\u7a7a\u6570\u636e\u9519\u8bef", (Throwable)e);
        }
    }

    public void taskDayThreadHandler() {
        try {
            HashMap<String, Object> params = new HashMap<String, Object>();
            String todayDate = DateUtil.getCurrentDate();
            params.put("startTime", todayDate + " 00:00:00");
            params.put("endTime", todayDate + " 23:59:59");
            logger.info("\u65e5\u62a5\u67e5\u8be2\u6761\u4ef6--------" + ((Object)params).toString());
            int countAll = this.countResourceByGroupId("");
            this.commonReportHandler(todayDate, "3", params, "ALL", countAll);
            if ("true".equals(this.commonConfig.getHostGroup())) {
                HashMap<String, Object> paramsGroup = new HashMap<String, Object>();
                List<HostGroup> hostGroupList = this.hostGroupService.selectAllByParams(paramsGroup, null);
                for (HostGroup hostGroup : hostGroupList) {
                    params.put("groupId", hostGroup.getId());
                    int count = this.countResourceByGroupId(hostGroup.getId());
                    if (count < 1) {
                        logger.info("\u8be5\u6807\u7b7e\u4e0b\u6ca1\u6709\u8d44\u6e90\u4e0d\u751f\u6210\u5de1\u68c0\u62a5\u544a--------" + hostGroup.getId());
                        continue;
                    }
                    this.commonReportHandler(todayDate, "3", params, hostGroup.getGroupName(), count);
                }
            }
        }
        catch (Exception e) {
            logger.error("\u5de1\u68c0\u65e5\u62a5\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("\u5de1\u68c0\u65e5\u62a5\u9519\u8bef", e.toString(), "2");
        }
    }

    public void taskWeekThreadHandler() {
        try {
            HashMap<String, Object> params = new HashMap<String, Object>();
            String timePart = DateUtil.setLastWeekBeginEndTime(params);
            logger.info("\u5468\u62a5\u67e5\u8be2\u6761\u4ef6--------" + ((Object)params).toString());
            HashMap<String, Object> paramsTimePart = new HashMap<String, Object>();
            paramsTimePart.put("timePart", timePart);
            int reportNum = this.countByParams(paramsTimePart);
            if (reportNum > 0) {
                logger.info("\u5df2\u7ecf\u751f\u6210\u8fc7\u5468\u62a5--------" + timePart);
                return;
            }
            int countAll = this.countResourceByGroupId("");
            this.commonReportHandler(timePart, "1", params, "ALL", countAll);
            if ("true".equals(this.commonConfig.getHostGroup())) {
                HashMap<String, Object> paramsGroup = new HashMap<String, Object>();
                List<HostGroup> hostGroupList = this.hostGroupService.selectAllByParams(paramsGroup, null);
                for (HostGroup hostGroup : hostGroupList) {
                    params.put("groupId", hostGroup.getId());
                    int count = this.countResourceByGroupId(hostGroup.getId());
                    if (count < 1) {
                        logger.info("\u8be5\u6807\u7b7e\u4e0b\u6ca1\u6709\u8d44\u6e90\u4e0d\u751f\u6210\u5de1\u68c0\u62a5\u544a--------" + hostGroup.getId());
                        continue;
                    }
                    this.commonReportHandler(timePart, "1", params, hostGroup.getGroupName(), count);
                }
            }
        }
        catch (Exception e) {
            logger.error("\u5de1\u68c0\u5468\u62a5\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("\u5de1\u68c0\u5468\u62a5\u9519\u8bef", e.toString(), "2");
        }
    }

    public void taskMonThreadHandler() {
        try {
            HashMap<String, Object> params = new HashMap<String, Object>();
            String timePart = DateUtil.setLastMonBeginEndTime(params);
            logger.info("\u6708\u62a5\u67e5\u8be2\u6761\u4ef6--------" + ((Object)params).toString());
            HashMap<String, Object> paramsTimePart = new HashMap<String, Object>();
            paramsTimePart.put("timePart", timePart);
            int reportNum = this.countByParams(paramsTimePart);
            if (reportNum > 0) {
                logger.info("\u5df2\u7ecf\u751f\u6210\u8fc7\u6708\u62a5--------" + timePart);
                return;
            }
            int countAll = this.countResourceByGroupId("");
            this.commonReportHandler(timePart, "2", params, "ALL", countAll);
            if ("true".equals(this.commonConfig.getHostGroup())) {
                HashMap<String, Object> paramsGroup = new HashMap<String, Object>();
                List<HostGroup> hostGroupList = this.hostGroupService.selectAllByParams(paramsGroup, null);
                for (HostGroup hostGroup : hostGroupList) {
                    params.put("groupId", hostGroup.getId());
                    int count = this.countResourceByGroupId(hostGroup.getId());
                    if (count < 1) {
                        logger.info("\u8be5\u6807\u7b7e\u4e0b\u6ca1\u6709\u8d44\u6e90\u4e0d\u751f\u6210\u5de1\u68c0\u62a5\u544a--------" + hostGroup.getId());
                        continue;
                    }
                    this.commonReportHandler(timePart, "2", params, hostGroup.getGroupName(), count);
                }
            }
        }
        catch (Exception e) {
            logger.error("\u5de1\u68c0\u6708\u62a5\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("\u5de1\u68c0\u6708\u62a5\u9519\u8bef", e.toString(), "2");
        }
    }

    private void commonReportHandler(String timePart, String reportType, Map<String, Object> params, String groupName, int count) throws Exception {
        Date date = new Date();
        ArrayList<ReportInstance> reportInstanceList = new ArrayList<ReportInstance>();
        ReportInfo reportInfo = new ReportInfo();
        reportInfo.setReportType(reportType);
        reportInfo.setTimePart(timePart);
        reportInfo.setGroupName(groupName + "\uff08\u8d44\u6e90\u603b\u6570\u91cf" + count + "\uff09");
        reportInfo.setCreateTime(date);
        this.save(reportInfo);
        this.monitorDataHandle(reportInstanceList, params, reportInfo);
        this.reportInstanceService.saveRecord(reportInstanceList, date);
    }

    private int countResourceByGroupId(String groupId) {
        int count = 0;
        try {
            HashMap<String, Object> paramsGroup = new HashMap<String, Object>();
            if (!StringUtils.isEmpty((CharSequence)groupId)) {
                paramsGroup.put("groupId", groupId);
            }
            count += this.systemInfoService.countByParams(paramsGroup);
            count += this.appInfoService.countByParams(paramsGroup);
            count += this.portInfoService.countByParams(paramsGroup);
            count += this.fileSafeService.countByParams(paramsGroup);
            count += this.dockerInfoService.countByParams(paramsGroup);
            count += this.customInfoService.countByParams(paramsGroup);
            count += this.dbInfoService.countByParams(paramsGroup);
            count += this.heathMonitorService.countByParams(paramsGroup);
            count += this.dceInfoService.countByParams(paramsGroup);
            count += this.snmpInfoService.countByParams(paramsGroup);
            count += this.ftpInfoService.countByParams(paramsGroup);
            count += this.taskJobInfoService.countByParams(paramsGroup);
            count += this.equipmentService.countByParams(paramsGroup);
            count += this.passwdInfoService.countByParams(paramsGroup);
        }
        catch (Exception e) {
            logger.error("\u7edf\u8ba1\u6807\u7b7e\u4e0b\u7684\u8d44\u6e90\u6570\u91cf\u9519\u8bef", (Throwable)e);
        }
        return count;
    }

    public void monitorDataHandle(List<ReportInstance> reportInstanceList, Map<String, Object> params, ReportInfo reportInfo) throws Exception {
        SysLoadState sysLoadStateMin;
        SysLoadState sysLoadStateAvg;
        NetIoState netIoStateMin;
        NetIoState netIoStateAvg;
        HashMap<String, Object> paramsDown = new HashMap<String, Object>();
        paramsDown.putAll(params);
        paramsDown.put("state", "2");
        paramsDown.put("active", "1");
        int totalSizeApp = this.appInfoService.countByParams(params);
        int totalSizeAppDown = this.appInfoService.countByParams(paramsDown);
        String appResult = "\uff0c\u68c0\u6d4b\u7ed3\u679c\uff1a\u6b63\u5e38";
        if (totalSizeAppDown > 0) {
            appResult = "\uff0c\u68c0\u6d4b\u7ed3\u679c\uff1a\u5f02\u5e38\uff0c\u79bb\u7ebf\u6570\u91cf\uff1a" + totalSizeAppDown;
        }
        this.reportInstanceService.mergeReportInsToList(reportInstanceList, "\u76d1\u63a7\u8fdb\u7a0b", "\u603b\u6570\u91cf\uff1a" + totalSizeApp + appResult, reportInfo.getId());
        int totalSizeAppException = this.appExceptionInfoService.countByParams(params);
        this.reportInstanceService.mergeReportInsToList(reportInstanceList, "\u4e3b\u673a\u5f02\u5e38\u8fdb\u7a0b", "\u603b\u6570\u91cf\uff1a" + totalSizeAppException + "", reportInfo.getId());
        int portSize = this.portInfoService.countByParams(params);
        int totalSizePortDown = this.portInfoService.countByParams(paramsDown);
        String portResult = "\uff0c\u68c0\u6d4b\u7ed3\u679c\uff1a\u6b63\u5e38";
        if (totalSizePortDown > 0) {
            portResult = "\uff0c\u68c0\u6d4b\u7ed3\u679c\uff1a\u5f02\u5e38\uff0c\u79bb\u7ebf\u6570\u91cf\uff1a" + totalSizePortDown;
        }
        this.reportInstanceService.mergeReportInsToList(reportInstanceList, "\u76d1\u63a7\u7aef\u53e3", "\u603b\u6570\u91cf\uff1a" + portSize + portResult, reportInfo.getId());
        int heathSize = this.heathMonitorService.countByParams(params);
        paramsDown.put("heathStatus", "200");
        int heath200Size = this.heathMonitorService.countByParams(paramsDown);
        String heathResult = "\uff0c\u68c0\u6d4b\u7ed3\u679c\uff1a\u6b63\u5e38";
        if (heathSize - heath200Size > 0) {
            heathResult = "\uff0c\u68c0\u6d4b\u7ed3\u679c\uff1a\u5f02\u5e38\uff0c\u79bb\u7ebf\u6570\u91cf\uff1a" + (heathSize - heath200Size);
        }
        this.reportInstanceService.mergeReportInsToList(reportInstanceList, "\u76d1\u63a7\u670d\u52a1\u63a5\u53e3", "\u603b\u6570\u91cf\uff1a" + heathSize + heathResult, reportInfo.getId());
        paramsDown.remove("heathStatus");
        int dockerSize = this.dockerInfoService.countByParams(params);
        int totalSizeDockerDown = this.dockerInfoService.countByParams(paramsDown);
        String dockerResult = "\uff0c\u68c0\u6d4b\u7ed3\u679c\uff1a\u6b63\u5e38";
        if (totalSizeDockerDown > 0) {
            dockerResult = "\uff0c\u68c0\u6d4b\u7ed3\u679c\uff1a\u5f02\u5e38\uff0c\u79bb\u7ebf\u6570\u91cf\uff1a" + totalSizeDockerDown;
        }
        this.reportInstanceService.mergeReportInsToList(reportInstanceList, "\u76d1\u63a7docker", "\u603b\u6570\u91cf\uff1a" + dockerSize + dockerResult, reportInfo.getId());
        int dceSize = this.dceInfoService.countByParams(params);
        paramsDown.put("resTimes", -1);
        int dceDownSize = this.dceInfoService.countByParams(paramsDown);
        String dceResult = "\uff0c\u68c0\u6d4b\u7ed3\u679c\uff1a\u6b63\u5e38";
        if (dceDownSize > 0) {
            dceResult = "\uff0c\u68c0\u6d4b\u7ed3\u679c\uff1a\u5f02\u5e38\uff0c\u79bb\u7ebf\u6570\u91cf\uff1a" + dceDownSize;
        }
        this.reportInstanceService.mergeReportInsToList(reportInstanceList, "\u76d1\u63a7\u7f51\u7edc\u8bbe\u5907PING", "\u603b\u6570\u91cf\uff1a" + dceSize + dceResult, reportInfo.getId());
        paramsDown.remove("resTimes");
        Integer fileWarnSize = this.fileWarnInfoService.countByParams(params);
        this.reportInstanceService.mergeReportInsToList(reportInstanceList, "\u76d1\u63a7\u65e5\u5fd7\u6587\u4ef6\u603b\u6570\u91cf", fileWarnSize + "", reportInfo.getId());
        int dbInfoSize = this.dbInfoService.countByParams(params);
        paramsDown.put("dbState", "2");
        int totalSizeDbInfoDown = this.dbInfoService.countByParams(paramsDown);
        String dbInfoResult = "\uff0c\u68c0\u6d4b\u7ed3\u679c\uff1a\u6b63\u5e38";
        if (totalSizeDbInfoDown > 0) {
            dbInfoResult = "\uff0c\u68c0\u6d4b\u7ed3\u679c\uff1a\u5f02\u5e38\uff0c\u79bb\u7ebf\u6570\u91cf\uff1a" + totalSizeDbInfoDown;
        }
        this.reportInstanceService.mergeReportInsToList(reportInstanceList, "\u76d1\u63a7\u6570\u636e\u5e93", "\u603b\u6570\u91cf\uff1a" + dbInfoSize + dbInfoResult, reportInfo.getId());
        paramsDown.remove("dbState");
        int dbTableSize = this.dbTableService.countByParams(params);
        paramsDown.put("dbState", "2");
        int totalSizeDbTableDown = this.dbTableService.countByParams(paramsDown);
        String dbTableResult = "\uff0c\u68c0\u6d4b\u7ed3\u679c\uff1a\u6b63\u5e38";
        if (totalSizeDbTableDown > 0) {
            dbTableResult = "\uff0c\u68c0\u6d4b\u7ed3\u679c\uff1a\u5f02\u5e38\uff0c\u5f02\u5e38\u6570\u91cf\uff1a" + totalSizeDbTableDown;
        }
        this.reportInstanceService.mergeReportInsToList(reportInstanceList, "\u76d1\u63a7\u6570\u636e\u8868", "\u603b\u6570\u91cf\uff1a" + dbTableSize + dbTableResult, reportInfo.getId());
        paramsDown.remove("dbState");
        int snmpInfoSize = this.snmpInfoService.countByParams(params);
        int totalSizeSnmpDown = this.snmpInfoService.countByParams(paramsDown);
        String snmpResult = "\uff0c\u68c0\u6d4b\u7ed3\u679c\uff1a\u6b63\u5e38";
        if (totalSizeSnmpDown > 0) {
            snmpResult = "\uff0c\u68c0\u6d4b\u7ed3\u679c\uff1a\u5f02\u5e38\uff0c\u79bb\u7ebf\u6570\u91cf\uff1a" + totalSizeSnmpDown;
        }
        this.reportInstanceService.mergeReportInsToList(reportInstanceList, "\u76d1\u63a7SNMP\u8bbe\u5907", "\u603b\u6570\u91cf\uff1a" + snmpInfoSize + snmpResult, reportInfo.getId());
        int customInfoSize = this.customInfoService.countByParams(params);
        int totalSizeCustomInfoDown = this.customInfoService.countByParams(paramsDown);
        String customInfoResult = "\uff0c\u68c0\u6d4b\u7ed3\u679c\uff1a\u6b63\u5e38";
        if (totalSizeCustomInfoDown > 0) {
            customInfoResult = "\uff0c\u68c0\u6d4b\u7ed3\u679c\uff1a\u5f02\u5e38\uff0c\u79bb\u7ebf\u6570\u91cf\uff1a" + totalSizeCustomInfoDown;
        }
        this.reportInstanceService.mergeReportInsToList(reportInstanceList, "\u81ea\u5b9a\u4e49\u76d1\u63a7\u9879", "\u603b\u6570\u91cf\uff1a" + customInfoSize + customInfoResult, reportInfo.getId());
        int fileSafeSize = this.fileSafeService.countByParams(params);
        int totalSizeFileSafeDown = this.fileSafeService.countByParams(paramsDown);
        String fileSafeResult = "\uff0c\u68c0\u6d4b\u7ed3\u679c\uff1a\u6b63\u5e38";
        if (totalSizeFileSafeDown > 0) {
            fileSafeResult = "\uff0c\u68c0\u6d4b\u7ed3\u679c\uff1a\u5f02\u5e38\uff0c\u79bb\u7ebf\u6570\u91cf\uff1a" + totalSizeFileSafeDown;
        }
        this.reportInstanceService.mergeReportInsToList(reportInstanceList, "\u6587\u4ef6\u9632\u7be1\u6539", "\u603b\u6570\u91cf\uff1a" + fileSafeSize + fileSafeResult, reportInfo.getId());
        int ftpInfoSize = this.ftpInfoService.countByParams(params);
        int totalSizeFtpInfoDown = this.ftpInfoService.countByParams(paramsDown);
        String ftpInfoResult = "\uff0c\u68c0\u6d4b\u7ed3\u679c\uff1a\u6b63\u5e38";
        if (totalSizeFtpInfoDown > 0) {
            ftpInfoResult = "\uff0c\u68c0\u6d4b\u7ed3\u679c\uff1a\u5f02\u5e38\uff0c\u79bb\u7ebf\u6570\u91cf\uff1a" + totalSizeFtpInfoDown;
        }
        this.reportInstanceService.mergeReportInsToList(reportInstanceList, "ftp/sftp\u76d1\u6d4b", "\u603b\u6570\u91cf\uff1a" + ftpInfoSize + ftpInfoResult, reportInfo.getId());
        int equipmentSize = this.equipmentService.countByParams(params);
        this.reportInstanceService.mergeReportInsToList(reportInstanceList, "\u8d44\u4ea7\u8bbe\u5907\u7ba1\u7406", "\u603b\u6570\u91cf\uff1a" + equipmentSize, reportInfo.getId());
        if (null == params.get("groupId")) {
            HashMap<String, Object> paramsAccount = new HashMap<String, Object>();
            paramsAccount.put("role", "user");
            int totalSizeAccountUser = this.accountInfoService.countByParams(paramsAccount);
            paramsAccount.put("role", "guest");
            int totalSizeAccountGuest = this.accountInfoService.countByParams(paramsAccount);
            this.reportInstanceService.mergeReportInsToList(reportInstanceList, "\u6210\u5458\u8d26\u53f7", "\u6210\u5458\u8d26\u53f7\u6570\u91cf\uff1a" + totalSizeAccountUser + "\uff0c\u53ea\u8bfb\u8d26\u53f7\u6570\u91cf\uff1a" + totalSizeAccountGuest, reportInfo.getId());
            int totalSizeHostGroup = this.hostGroupService.countByParams(params);
            this.reportInstanceService.mergeReportInsToList(reportInstanceList, "\u6807\u7b7e\u603b\u6570\u91cf", totalSizeHostGroup + "", reportInfo.getId());
        }
        HashMap<String, Object> paramsUptime = new HashMap<String, Object>();
        paramsUptime.put("orderBy", "UPTIME");
        paramsUptime.put("orderType", "DESC");
        paramsUptime.put("groupId", params.get("groupId"));
        List<SystemInfoExt> systemInfoExtList = this.systemInfoExtService.selectAllByParams(paramsUptime);
        if (systemInfoExtList.size() > 0) {
            SystemInfoExt systemInfoMaxUptime = systemInfoExtList.get(0);
            String systemInfoMaxUptimeContent = systemInfoMaxUptime.getHostname() + HostUtil.addRemark(systemInfoMaxUptime.getHostname()) + "\uff0c\u5df2\u8fd0\u884c" + systemInfoMaxUptime.getUptimeStr();
            this.reportInstanceService.mergeReportInsToList(reportInstanceList, "\u8fd0\u884c\u65f6\u95f4\u6700\u957f\u7684\u4e3b\u673a", systemInfoMaxUptimeContent, reportInfo.getId());
        }
        paramsUptime.put("orderType", "ASC");
        systemInfoExtList = this.systemInfoExtService.selectAllByParams(paramsUptime);
        if (systemInfoExtList.size() > 0) {
            SystemInfoExt systemInfoMinUptime = systemInfoExtList.get(0);
            String systemInfoMinUptimeContent = systemInfoMinUptime.getHostname() + HostUtil.addRemark(systemInfoMinUptime.getHostname()) + "\uff0c\u5df2\u8fd0\u884c" + systemInfoMinUptime.getUptimeStr();
            this.reportInstanceService.mergeReportInsToList(reportInstanceList, "\u8fd0\u884c\u65f6\u95f4\u6700\u77ed\u7684\u4e3b\u673a", systemInfoMinUptimeContent, reportInfo.getId());
        }
        HashMap<String, Object> paramsSystemInfo = new HashMap<String, Object>();
        paramsSystemInfo.put("groupId", params.get("groupId"));
        List<SystemInfo> systemInfoList = this.systemInfoService.selectAllByParams(paramsSystemInfo);
        CpuState maxAvgCpuState = this.cpuStateService.selectMaxAvgByHostname(params);
        if (null == maxAvgCpuState) {
            maxAvgCpuState = new CpuState();
            maxAvgCpuState.setSys(0.0);
            maxAvgCpuState.setIdle(0.0);
            maxAvgCpuState.setIowait(0.0);
        }
        String maxCpuHostName = "";
        String minCpuHostName = "";
        try {
            params.put("sys", maxAvgCpuState.getSys());
            PageInfo pageInfoCpuState = this.cpuStateService.selectByParams(params, 1, 1);
            if (!CollectionUtil.isEmpty((Collection)pageInfoCpuState.getList())) {
                maxCpuHostName = ((CpuState)pageInfoCpuState.getList().get(0)).getHostname();
                maxCpuHostName = "\uff0c" + maxCpuHostName + HostUtil.addRemark(maxCpuHostName);
            }
            params.put("sys", maxAvgCpuState.getIowait());
            PageInfo pageInfoCpuState2 = this.cpuStateService.selectByParams(params, 1, 1);
            if (!CollectionUtil.isEmpty((Collection)pageInfoCpuState2.getList())) {
                minCpuHostName = ((CpuState)pageInfoCpuState2.getList().get(0)).getHostname();
                minCpuHostName = "\uff0c" + minCpuHostName + HostUtil.addRemark(minCpuHostName);
            }
        }
        catch (Exception e) {
            logger.error("\u67e5\u627eCPU\u4f7f\u7528\u7387\u6700\u9ad8\u503c\u548c\u6700\u4f4e\u503c\u7684\u4e3b\u673aIP\u9519\u8bef", (Throwable)e);
        }
        params.remove("sys");
        this.reportInstanceService.mergeReportInsToList(reportInstanceList, "\u4e3b\u673aCPU\u4f7f\u7528\u7387\u6700\u9ad8\u503c", maxAvgCpuState.getSys() + "%" + maxCpuHostName, reportInfo.getId());
        this.reportInstanceService.mergeReportInsToList(reportInstanceList, "\u4e3b\u673aCPU\u4f7f\u7528\u7387\u5e73\u5747\u503c", FormatUtil.formatDouble(maxAvgCpuState.getIdle(), 2) + "%", reportInfo.getId());
        this.reportInstanceService.mergeReportInsToList(reportInstanceList, "\u4e3b\u673aCPU\u4f7f\u7528\u7387\u6700\u4f4e\u503c", maxAvgCpuState.getIowait() + "%" + minCpuHostName, reportInfo.getId());
        MemState maxAvgMemState = this.memStateService.selectMaxAvgByHostname(params);
        if (null == maxAvgMemState) {
            maxAvgMemState = new MemState();
            maxAvgMemState.setUsePer(0.0);
            maxAvgMemState.setUsed("0");
            maxAvgMemState.setFree("0");
        }
        String maxMemHostName = "";
        String minMemHostName = "";
        try {
            params.put("usePer", maxAvgMemState.getUsePer());
            PageInfo pageInfoMemState = this.memStateService.selectByParams(params, 1, 1);
            if (!CollectionUtil.isEmpty((Collection)pageInfoMemState.getList())) {
                maxMemHostName = ((MemState)pageInfoMemState.getList().get(0)).getHostname();
                maxMemHostName = "\uff0c" + maxMemHostName + HostUtil.addRemark(maxMemHostName);
            }
            params.put("usePer", maxAvgMemState.getFree());
            PageInfo pageInfoMemState2 = this.memStateService.selectByParams(params, 1, 1);
            if (!CollectionUtil.isEmpty((Collection)pageInfoMemState2.getList())) {
                minMemHostName = ((MemState)pageInfoMemState2.getList().get(0)).getHostname();
                minMemHostName = "\uff0c" + minMemHostName + HostUtil.addRemark(minMemHostName);
            }
        }
        catch (Exception e) {
            logger.error("\u67e5\u627e\u5185\u5b58\u4f7f\u7528\u7387\u6700\u9ad8\u503c\u548c\u6700\u4f4e\u503c\u7684\u4e3b\u673aIP\u9519\u8bef", (Throwable)e);
        }
        params.remove("usePer");
        this.reportInstanceService.mergeReportInsToList(reportInstanceList, "\u4e3b\u673a\u5185\u5b58\u4f7f\u7528\u7387\u6700\u9ad8\u503c", maxAvgMemState.getUsePer() + "%" + maxMemHostName, reportInfo.getId());
        this.reportInstanceService.mergeReportInsToList(reportInstanceList, "\u4e3b\u673a\u5185\u5b58\u4f7f\u7528\u7387\u5e73\u5747\u503c", FormatUtil.formatDouble(Double.valueOf(maxAvgMemState.getUsed()), 2) + "%", reportInfo.getId());
        this.reportInstanceService.mergeReportInsToList(reportInstanceList, "\u4e3b\u673a\u5185\u5b58\u4f7f\u7528\u7387\u6700\u4f4e\u503c", maxAvgMemState.getFree() + "%" + minMemHostName, reportInfo.getId());
        NetIoState netIoStateMax = this.netIoStateService.selectMaxByHostname(params);
        if (null == netIoStateMax) {
            netIoStateMax = new NetIoState();
            netIoStateMax.setTxbyt("0");
            netIoStateMax.setRxbyt("0");
            netIoStateMax.setRxpck("0");
            netIoStateMax.setTxpck("0");
        }
        if (null == (netIoStateAvg = this.netIoStateService.selectAvgByHostname(params))) {
            netIoStateAvg = new NetIoState();
            netIoStateAvg.setTxbyt("0");
            netIoStateAvg.setRxbyt("0");
            netIoStateAvg.setRxpck("0");
            netIoStateAvg.setTxpck("0");
        }
        if (null == (netIoStateMin = this.netIoStateService.selectMinByHostname(params))) {
            netIoStateMin = new NetIoState();
            netIoStateMin.setTxbyt("0");
            netIoStateMin.setRxbyt("0");
            netIoStateMin.setRxpck("0");
            netIoStateMin.setTxpck("0");
        }
        String maxRxbytHostName = "";
        String minRxbytHostName = "";
        String maxTxbytHostName = "";
        String minTxbytHostName = "";
        try {
            params.put("rxbyt", netIoStateMax.getRxbyt());
            PageInfo pageInfoNetIoState = this.netIoStateService.selectByParams(params, 1, 1);
            if (!CollectionUtil.isEmpty((Collection)pageInfoNetIoState.getList())) {
                maxRxbytHostName = ((NetIoState)pageInfoNetIoState.getList().get(0)).getHostname();
                maxRxbytHostName = "\uff0c" + maxRxbytHostName + HostUtil.addRemark(maxRxbytHostName);
            }
            params.put("rxbyt", netIoStateMin.getRxbyt());
            PageInfo pageInfoNetIoState2 = this.netIoStateService.selectByParams(params, 1, 1);
            if (!CollectionUtil.isEmpty((Collection)pageInfoNetIoState2.getList())) {
                minRxbytHostName = ((NetIoState)pageInfoNetIoState2.getList().get(0)).getHostname();
                minRxbytHostName = "\uff0c" + minRxbytHostName + HostUtil.addRemark(minRxbytHostName);
            }
            params.remove("rxbyt");
            params.put("txbyt", netIoStateMax.getTxbyt());
            PageInfo pageInfoNetIoState3 = this.netIoStateService.selectByParams(params, 1, 1);
            if (!CollectionUtil.isEmpty((Collection)pageInfoNetIoState3.getList())) {
                maxTxbytHostName = ((NetIoState)pageInfoNetIoState3.getList().get(0)).getHostname();
                maxTxbytHostName = "\uff0c" + maxTxbytHostName + HostUtil.addRemark(maxTxbytHostName);
            }
            params.put("txbyt", netIoStateMin.getTxbyt());
            PageInfo pageInfoNetIoState4 = this.netIoStateService.selectByParams(params, 1, 1);
            if (!CollectionUtil.isEmpty((Collection)pageInfoNetIoState4.getList())) {
                minTxbytHostName = ((NetIoState)pageInfoNetIoState4.getList().get(0)).getHostname();
                minTxbytHostName = "\uff0c" + minTxbytHostName + HostUtil.addRemark(minTxbytHostName);
            }
        }
        catch (Exception e) {
            logger.error("\u67e5\u627e\u4e0a\u4e0b\u884c\u901f\u7387\u6700\u9ad8\u503c\u548c\u6700\u4f4e\u503c\u7684\u4e3b\u673aIP\u9519\u8bef", (Throwable)e);
        }
        params.remove("rxbyt");
        params.remove("txbyt");
        this.reportInstanceService.mergeReportInsToList(reportInstanceList, "\u4e3b\u673a\u4e0b\u884c\u901f\u7387\u6700\u9ad8\u503c", FormatUtil.kbToM(netIoStateMax.getRxbyt()) + "/s" + maxRxbytHostName, reportInfo.getId());
        this.reportInstanceService.mergeReportInsToList(reportInstanceList, "\u4e3b\u673a\u4e0b\u884c\u901f\u7387\u6700\u4f4e\u503c", FormatUtil.kbToM(netIoStateMin.getRxbyt()) + "/s" + minRxbytHostName, reportInfo.getId());
        this.reportInstanceService.mergeReportInsToList(reportInstanceList, "\u4e3b\u673a\u4e0b\u884c\u901f\u7387\u5e73\u5747\u503c", FormatUtil.kbToM(FormatUtil.formatDouble(netIoStateAvg.getRxbyt(), 2) + "") + "/s", reportInfo.getId());
        this.reportInstanceService.mergeReportInsToList(reportInstanceList, "\u4e3b\u673a\u4e0a\u884c\u901f\u7387\u6700\u9ad8\u503c", FormatUtil.kbToM(netIoStateMax.getTxbyt()) + "/s" + maxTxbytHostName, reportInfo.getId());
        this.reportInstanceService.mergeReportInsToList(reportInstanceList, "\u4e3b\u673a\u4e0a\u884c\u901f\u7387\u6700\u4f4e\u503c", FormatUtil.kbToM(netIoStateMin.getTxbyt()) + "/s" + minTxbytHostName, reportInfo.getId());
        this.reportInstanceService.mergeReportInsToList(reportInstanceList, "\u4e3b\u673a\u4e0a\u884c\u901f\u7387\u5e73\u5747\u503c", FormatUtil.kbToM(FormatUtil.formatDouble(netIoStateAvg.getTxbyt(), 2) + "") + "/s", reportInfo.getId());
        SysLoadState sysLoadStateMax = this.sysLoadStateService.selectMaxByHostname(params);
        if (null == sysLoadStateMax) {
            sysLoadStateMax = new SysLoadState();
            sysLoadStateMax.setOneLoad(0.0);
            sysLoadStateMax.setFiveLoad(0.0);
            sysLoadStateMax.setFifteenLoad(0.0);
        }
        if (null == (sysLoadStateAvg = this.sysLoadStateService.selectAvgByHostname(params))) {
            sysLoadStateAvg = new SysLoadState();
            sysLoadStateAvg.setOneLoad(0.0);
            sysLoadStateAvg.setFiveLoad(0.0);
            sysLoadStateAvg.setFifteenLoad(0.0);
        }
        if (null == (sysLoadStateMin = this.sysLoadStateService.selectMinByHostname(params))) {
            sysLoadStateMin = new SysLoadState();
            sysLoadStateMin.setOneLoad(0.0);
            sysLoadStateMin.setFiveLoad(0.0);
            sysLoadStateMin.setFifteenLoad(0.0);
        }
        String maxOneLoadHostName = "";
        String minxOneLoadHostName = "";
        try {
            params.put("oneLoad", sysLoadStateMax.getOneLoad());
            PageInfo pageInfoSysLoad = this.sysLoadStateService.selectByParams(params, 1, 1);
            if (!CollectionUtil.isEmpty((Collection)pageInfoSysLoad.getList())) {
                maxOneLoadHostName = ((SysLoadState)pageInfoSysLoad.getList().get(0)).getHostname();
                maxOneLoadHostName = "\uff0c" + maxOneLoadHostName + HostUtil.addRemark(maxOneLoadHostName);
            }
            params.put("oneLoad", sysLoadStateMin.getOneLoad());
            PageInfo pageInfoSysLoad2 = this.sysLoadStateService.selectByParams(params, 1, 1);
            if (!CollectionUtil.isEmpty((Collection)pageInfoSysLoad2.getList())) {
                minxOneLoadHostName = ((SysLoadState)pageInfoSysLoad2.getList().get(0)).getHostname();
                minxOneLoadHostName = "\uff0c" + minxOneLoadHostName + HostUtil.addRemark(minxOneLoadHostName);
            }
        }
        catch (Exception e) {
            logger.error("\u67e5\u627e1\u5206\u949f\u7cfb\u7edf\u8d1f\u8f7d\u6700\u9ad8\u503c\u548c\u6700\u4f4e\u503c\u7684\u4e3b\u673aIP\u9519\u8bef", (Throwable)e);
        }
        params.remove("oneLoad");
        this.reportInstanceService.mergeReportInsToList(reportInstanceList, "\u4e3b\u673a1\u5206\u949f\u7cfb\u7edf\u8d1f\u8f7d\u6700\u9ad8\u503c", sysLoadStateMax.getOneLoad() + maxOneLoadHostName, reportInfo.getId());
        this.reportInstanceService.mergeReportInsToList(reportInstanceList, "\u4e3b\u673a1\u5206\u949f\u7cfb\u7edf\u8d1f\u8f7d\u6700\u4f4e\u503c", sysLoadStateMin.getOneLoad() + minxOneLoadHostName, reportInfo.getId());
        this.reportInstanceService.mergeReportInsToList(reportInstanceList, "\u4e3b\u673a1\u5206\u949f\u7cfb\u7edf\u8d1f\u8f7d\u5e73\u5747\u503c", FormatUtil.formatDouble(sysLoadStateAvg.getOneLoad(), 2) + "", reportInfo.getId());
        String maxFiveLoadHostName = "";
        String minFiveLoadHostName = "";
        try {
            params.put("fiveLoad", sysLoadStateMax.getFiveLoad());
            PageInfo pageInfoSysLoad = this.sysLoadStateService.selectByParams(params, 1, 1);
            if (!CollectionUtil.isEmpty((Collection)pageInfoSysLoad.getList())) {
                maxFiveLoadHostName = ((SysLoadState)pageInfoSysLoad.getList().get(0)).getHostname();
                maxFiveLoadHostName = "\uff0c" + maxFiveLoadHostName + HostUtil.addRemark(maxFiveLoadHostName);
            }
            params.put("fiveLoad", sysLoadStateMin.getFiveLoad());
            PageInfo pageInfoSysLoad2 = this.sysLoadStateService.selectByParams(params, 1, 1);
            if (!CollectionUtil.isEmpty((Collection)pageInfoSysLoad2.getList())) {
                minFiveLoadHostName = ((SysLoadState)pageInfoSysLoad2.getList().get(0)).getHostname();
                minFiveLoadHostName = "\uff0c" + minFiveLoadHostName + HostUtil.addRemark(minFiveLoadHostName);
            }
        }
        catch (Exception e) {
            logger.error("\u67e5\u627e5\u5206\u949f\u7cfb\u7edf\u8d1f\u8f7d\u6700\u9ad8\u503c\u548c\u6700\u4f4e\u503c\u7684\u4e3b\u673aIP\u9519\u8bef", (Throwable)e);
        }
        params.remove("fiveLoad");
        this.reportInstanceService.mergeReportInsToList(reportInstanceList, "\u4e3b\u673a5\u5206\u949f\u7cfb\u7edf\u8d1f\u8f7d\u6700\u9ad8\u503c", sysLoadStateMax.getFiveLoad() + maxFiveLoadHostName, reportInfo.getId());
        this.reportInstanceService.mergeReportInsToList(reportInstanceList, "\u4e3b\u673a5\u5206\u949f\u7cfb\u7edf\u8d1f\u8f7d\u6700\u4f4e\u503c", sysLoadStateMin.getFiveLoad() + minFiveLoadHostName, reportInfo.getId());
        this.reportInstanceService.mergeReportInsToList(reportInstanceList, "\u4e3b\u673a5\u5206\u949f\u7cfb\u7edf\u8d1f\u8f7d\u5e73\u5747\u503c", FormatUtil.formatDouble(sysLoadStateAvg.getFiveLoad(), 2) + "", reportInfo.getId());
        String maxFifteenLoadHostName = "";
        String minFifteenLoadHostName = "";
        try {
            params.put("fifteenLoad", sysLoadStateMax.getFifteenLoad());
            PageInfo pageInfoSysLoad = this.sysLoadStateService.selectByParams(params, 1, 1);
            if (!CollectionUtil.isEmpty((Collection)pageInfoSysLoad.getList())) {
                maxFifteenLoadHostName = ((SysLoadState)pageInfoSysLoad.getList().get(0)).getHostname();
                maxFifteenLoadHostName = "\uff0c" + maxFifteenLoadHostName + HostUtil.addRemark(maxFifteenLoadHostName);
            }
            params.put("fifteenLoad", sysLoadStateMin.getFifteenLoad());
            PageInfo pageInfoSysLoad2 = this.sysLoadStateService.selectByParams(params, 1, 1);
            if (!CollectionUtil.isEmpty((Collection)pageInfoSysLoad2.getList())) {
                minFifteenLoadHostName = ((SysLoadState)pageInfoSysLoad2.getList().get(0)).getHostname();
                minFifteenLoadHostName = "\uff0c" + minFifteenLoadHostName + HostUtil.addRemark(minFifteenLoadHostName);
            }
        }
        catch (Exception e) {
            logger.error("\u67e5\u627e15\u5206\u949f\u7cfb\u7edf\u8d1f\u8f7d\u6700\u9ad8\u503c\u548c\u6700\u4f4e\u503c\u7684\u4e3b\u673aIP\u9519\u8bef", (Throwable)e);
        }
        params.remove("fifteenLoad");
        this.reportInstanceService.mergeReportInsToList(reportInstanceList, "\u4e3b\u673a15\u5206\u949f\u7cfb\u7edf\u8d1f\u8f7d\u6700\u9ad8\u503c", sysLoadStateMax.getFifteenLoad() + maxFifteenLoadHostName, reportInfo.getId());
        this.reportInstanceService.mergeReportInsToList(reportInstanceList, "\u4e3b\u673a15\u5206\u949f\u7cfb\u7edf\u8d1f\u8f7d\u6700\u4f4e\u503c", sysLoadStateMin.getFifteenLoad() + minFifteenLoadHostName, reportInfo.getId());
        this.reportInstanceService.mergeReportInsToList(reportInstanceList, "\u4e3b\u673a15\u5206\u949f\u7cfb\u7edf\u8d1f\u8f7d\u5e73\u5747\u503c", FormatUtil.formatDouble(sysLoadStateAvg.getFifteenLoad(), 2) + "", reportInfo.getId());
        Integer maxProcs = 0;
        String maxProcsHostName = "";
        Double avgProcs = 0.0;
        Integer minProcs = 1000;
        String minProcsHostName = "";
        Integer sumProcs = 0;
        Integer maxNetConnections = 0;
        String maxNetConnectionsHostName = "";
        Double avgNetConnections = 0.0;
        Integer minNetConnections = 1000;
        String minNetConnectionsHostName = "";
        Integer sumNetConnections = 0;
        int systemSize = 0;
        int cpuCoresSum = 0;
        double memSum = 0.0;
        int submitSecondsSum = 0;
        double avgSubmitSeconds = 120.0;
        for (SystemInfo systemInfo : systemInfoList) {
            ++systemSize;
            try {
                cpuCoresSum += Integer.valueOf(systemInfo.getCpuCoreNum()).intValue();
            }
            catch (Exception e) {
                logger.error("\u7edf\u8ba1\u6240\u6709\u4e3b\u673a\u603b\u6838\u6570\u9519\u8bef", (Throwable)e);
            }
            try {
                memSum += Double.valueOf(systemInfo.getTotalMem().replace("G", "")).doubleValue();
            }
            catch (Exception e) {
                logger.error("\u7edf\u8ba1\u6240\u6709\u4e3b\u673a\u603b\u5185\u5b58\u9519\u8bef", (Throwable)e);
            }
            try {
                submitSecondsSum += Integer.valueOf(systemInfo.getSubmitSeconds()).intValue();
            }
            catch (Exception e) {
                logger.error("\u7edf\u8ba1\u6240\u6709\u4e3b\u673a\u7ec4\u88c5\u4e0a\u62a5\u6570\u636e\u9891\u7387\u603b\u548c\u9519\u8bef", (Throwable)e);
            }
            if (!StringUtils.isEmpty((CharSequence)systemInfo.getProcs())) {
                if (Integer.valueOf(systemInfo.getProcs()) > maxProcs) {
                    maxProcs = Integer.valueOf(systemInfo.getProcs());
                    maxProcsHostName = "\uff0c" + systemInfo.getHostname() + HostUtil.addRemark(systemInfo.getHostname());
                }
                if (Integer.valueOf(systemInfo.getProcs()) < minProcs) {
                    minProcs = Integer.valueOf(systemInfo.getProcs());
                    minProcsHostName = "\uff0c" + systemInfo.getHostname() + HostUtil.addRemark(systemInfo.getHostname());
                }
                sumProcs = sumProcs + Integer.valueOf(systemInfo.getProcs());
            }
            if (StringUtils.isEmpty((CharSequence)systemInfo.getNetConnections())) continue;
            if (Integer.valueOf(systemInfo.getNetConnections()) > maxNetConnections) {
                maxNetConnections = Integer.valueOf(systemInfo.getNetConnections());
                maxNetConnectionsHostName = "\uff0c" + systemInfo.getHostname() + HostUtil.addRemark(systemInfo.getHostname());
            }
            if (Integer.valueOf(systemInfo.getNetConnections()) < minNetConnections) {
                minNetConnections = Integer.valueOf(systemInfo.getNetConnections());
                minNetConnectionsHostName = "\uff0c" + systemInfo.getHostname() + HostUtil.addRemark(systemInfo.getHostname());
            }
            sumNetConnections = sumNetConnections + Integer.valueOf(systemInfo.getNetConnections());
        }
        if (systemSize > 0) {
            avgSubmitSeconds = (double)submitSecondsSum / (double)systemInfoList.size();
            avgProcs = (double)sumProcs.intValue() / (double)systemInfoList.size();
            avgNetConnections = (double)sumNetConnections.intValue() / (double)systemInfoList.size();
        } else {
            avgProcs = 0.0;
            avgNetConnections = 0.0;
        }
        this.reportInstanceService.mergeReportInsToList(reportInstanceList, "\u76d1\u63a7\u4e3b\u673a\u603b\u6570\u91cf", systemSize + "", reportInfo.getId());
        this.reportInstanceService.mergeReportInsToList(reportInstanceList, "CPU\u6838\u6570\u603b\u548c", cpuCoresSum + "", reportInfo.getId());
        this.reportInstanceService.mergeReportInsToList(reportInstanceList, "\u5185\u5b58\u603b\u548c", FormatUtil.formatDouble(memSum, 2) + "G", reportInfo.getId());
        if (null == params.get("groupId")) {
            this.reportInstanceService.mergeReportInsToList(reportInstanceList, "\u78c1\u76d8\u5bb9\u91cf\u603b\u548c", this.servletContext.getAttribute("sumDiskSizeCache") + "", reportInfo.getId());
        } else {
            this.reportInstanceService.mergeReportInsToList(reportInstanceList, "\u78c1\u76d8\u5bb9\u91cf\u603b\u548c", this.taskUtilService.sumDiskSizeCacheByGroup(params.get("groupId")), reportInfo.getId());
        }
        this.reportInstanceService.mergeReportInsToList(reportInstanceList, "Agent\u5e73\u5747\u76d1\u63a7\u4e0a\u62a5\u9891\u7387", FormatUtil.formatDouble(avgSubmitSeconds, 2) + "\u79d2", reportInfo.getId());
        this.reportInstanceService.mergeReportInsToList(reportInstanceList, "\u4e3b\u673a\u8fde\u63a5\u6570\u91cf\u6700\u9ad8\u503c", maxNetConnections + maxNetConnectionsHostName, reportInfo.getId());
        this.reportInstanceService.mergeReportInsToList(reportInstanceList, "\u4e3b\u673a\u8fde\u63a5\u6570\u91cf\u5e73\u5747\u503c", FormatUtil.formatDouble(avgNetConnections, 2) + "", reportInfo.getId());
        this.reportInstanceService.mergeReportInsToList(reportInstanceList, "\u4e3b\u673a\u8fde\u63a5\u6570\u91cf\u6700\u4f4e\u503c", minNetConnections + minNetConnectionsHostName, reportInfo.getId());
        this.reportInstanceService.mergeReportInsToList(reportInstanceList, "\u4e3b\u673a\u8fd0\u884c\u8fdb\u7a0b\u6570\u91cf\u6700\u9ad8\u503c", maxProcs + maxProcsHostName, reportInfo.getId());
        this.reportInstanceService.mergeReportInsToList(reportInstanceList, "\u4e3b\u673a\u8fd0\u884c\u8fdb\u7a0b\u6570\u91cf\u5e73\u5747\u503c", FormatUtil.formatDouble(avgProcs, 2) + "", reportInfo.getId());
        this.reportInstanceService.mergeReportInsToList(reportInstanceList, "\u4e3b\u673a\u8fd0\u884c\u8fdb\u7a0b\u6570\u91cf\u6700\u4f4e\u503c", minProcs + minProcsHostName, reportInfo.getId());
        if (null == params.get("groupId")) {
            params.put("hostname", "\u5f00\u59cb\u4e0b\u53d1\u6307\u4ee4");
            int shellSize = this.logInfoService.countByParams(params);
            this.reportInstanceService.mergeReportInsToList(reportInstanceList, "\u7d2f\u79ef\u4e0b\u53d1\u6307\u4ee4\u6570\u91cf", shellSize + "", reportInfo.getId());
            params.remove("hostname");
            params.put("state", "1");
            int warnSize = this.logInfoService.countByParams(params);
            this.reportInstanceService.mergeReportInsToList(reportInstanceList, "\u7d2f\u79ef\u544a\u8b66\u6d88\u606f\u6570\u91cf", warnSize + "", reportInfo.getId());
            params.put("state", "3");
            int recoveredSize = this.logInfoService.countByParams(params);
            this.reportInstanceService.mergeReportInsToList(reportInstanceList, "\u7d2f\u79ef\u6062\u590d\u6d88\u606f\u6570\u91cf", recoveredSize + "", reportInfo.getId());
            params.put("state", "4");
            int thirdWarnSize = this.logInfoService.countByParams(params);
            this.reportInstanceService.mergeReportInsToList(reportInstanceList, "\u7b2c\u4e09\u65b9\u544a\u8b66\u6d88\u606f\u6570\u91cf", thirdWarnSize + "", reportInfo.getId());
            params.put("state", "2");
            int xtczLogSize = this.logInfoService.countByParams(params);
            this.reportInstanceService.mergeReportInsToList(reportInstanceList, "\u7cfb\u7edf\u64cd\u4f5c\u8bb0\u5f55\u6570\u91cf", xtczLogSize + "", reportInfo.getId());
            params.remove("state");
        }
        Double maxDiskPer = 0.0;
        String maxDiskPerHostName = "";
        Double minDiskPer = 1000.0;
        String minDiskPerHostName = "";
        Double avgDiskPer = 0.0;
        Double sumDiskPer = 0.0;
        params.remove("hostname");
        List<HostDiskPer> hostDiskPerList = this.hostDiskPerService.selectAllByParams(params);
        for (HostDiskPer hostDiskPer : hostDiskPerList) {
            if (null != hostDiskPer.getDiskSumPer() && hostDiskPer.getDiskSumPer() > maxDiskPer) {
                maxDiskPer = hostDiskPer.getDiskSumPer();
                maxDiskPerHostName = "\uff0c" + hostDiskPer.getHostname() + HostUtil.addRemark(hostDiskPer.getHostname());
            }
            if (null != hostDiskPer.getDiskSumPer() && hostDiskPer.getDiskSumPer() < minDiskPer) {
                minDiskPer = hostDiskPer.getDiskSumPer();
                minDiskPerHostName = "\uff0c" + hostDiskPer.getHostname() + HostUtil.addRemark(hostDiskPer.getHostname());
            }
            if (null == hostDiskPer.getDiskSumPer()) continue;
            sumDiskPer = sumDiskPer + hostDiskPer.getDiskSumPer();
        }
        if (hostDiskPerList.size() > 0) {
            avgDiskPer = sumDiskPer / (double)hostDiskPerList.size();
        } else {
            minDiskPer = 0.0;
        }
        this.reportInstanceService.mergeReportInsToList(reportInstanceList, "\u4e3b\u673a\u78c1\u76d8\u603b\u4f7f\u7528\u7387\u6700\u9ad8\u503c", maxDiskPer + maxDiskPerHostName, reportInfo.getId());
        this.reportInstanceService.mergeReportInsToList(reportInstanceList, "\u4e3b\u673a\u78c1\u76d8\u603b\u4f7f\u7528\u7387\u5e73\u5747\u503c", FormatUtil.formatDouble(avgDiskPer, 2) + "", reportInfo.getId());
        this.reportInstanceService.mergeReportInsToList(reportInstanceList, "\u4e3b\u673a\u78c1\u76d8\u603b\u4f7f\u7528\u7387\u6700\u4f4e\u503c", minDiskPer + minDiskPerHostName, reportInfo.getId());
        DiskIoState diskIoStateMax = this.diskIoStateService.selectMaxByHostname(params);
        if (null != diskIoStateMax) {
            String maxReadIoAvgHostName = "";
            String maxWriteIoAvgHostName = "";
            try {
                params.put("readIoAvg", diskIoStateMax.getReadIoAvg());
                PageInfo pageInfoDiskIoState = this.diskIoStateService.selectByParams(params, 1, 1);
                if (!CollectionUtil.isEmpty((Collection)pageInfoDiskIoState.getList())) {
                    maxReadIoAvgHostName = ((DiskIoState)pageInfoDiskIoState.getList().get(0)).getHostname();
                    maxReadIoAvgHostName = "(" + maxReadIoAvgHostName + HostUtil.addRemark(maxReadIoAvgHostName) + ")";
                }
                params.put("writeIoAvg", diskIoStateMax.getWriteIoAvg());
                PageInfo pageInfoDiskIoState2 = this.diskIoStateService.selectByParams(params, 1, 1);
                if (!CollectionUtil.isEmpty((Collection)pageInfoDiskIoState2.getList())) {
                    maxWriteIoAvgHostName = ((DiskIoState)pageInfoDiskIoState2.getList().get(0)).getHostname();
                    maxWriteIoAvgHostName = "(" + maxWriteIoAvgHostName + HostUtil.addRemark(maxWriteIoAvgHostName) + ")";
                }
            }
            catch (Exception e) {
                logger.error("\u67e5\u627e\u78c1\u76d8\u8bfb\u5199\u901f\u7387\u6700\u9ad8\u503c\u548c\u6700\u4f4e\u503c\u7684\u4e3b\u673aIP\u9519\u8bef", (Throwable)e);
            }
            params.remove("readIoAvg");
            params.remove("writeIoAvg");
            this.reportInstanceService.mergeReportInsToList(reportInstanceList, "\u78c1\u76d8\u8bfb\u5199\u901f\u7387", "\u8bfb\u53d6\u901f\u7387\u6700\u9ad8\uff1a" + diskIoStateMax.getReadIoAvg() + "MB/s" + maxReadIoAvgHostName + "\uff0c\u5199\u5165\u901f\u7387\u6700\u9ad8\uff1a" + diskIoStateMax.getWriteIoAvg() + "MB/s" + maxWriteIoAvgHostName, reportInfo.getId());
        } else {
            this.reportInstanceService.mergeReportInsToList(reportInstanceList, "\u78c1\u76d8\u8bfb\u5199\u901f\u7387", "\u6682\u65e0\u6570\u636e", reportInfo.getId());
        }
        Double maxInput = 0.0;
        String maxInputHostName = "";
        Double minInput = 1000.0;
        String minInputHostName = "";
        Double avgInput = 0.0;
        Double sumInput = 0.0;
        List<CpuTemperatures> cpuTemperaturesList = this.cpuTemperaturesService.selectAllByParams(params);
        for (CpuTemperatures cpuTemperatures : cpuTemperaturesList) {
            try {
                Double inputDouble = Double.valueOf(cpuTemperatures.getInput());
                if (null != inputDouble && inputDouble > maxInput) {
                    maxInput = inputDouble;
                    maxInputHostName = "\uff0c" + cpuTemperatures.getHostname() + HostUtil.addRemark(cpuTemperatures.getHostname());
                }
                if (null != inputDouble && inputDouble < minInput) {
                    minInput = inputDouble;
                    minInputHostName = "\uff0c" + cpuTemperatures.getHostname() + HostUtil.addRemark(cpuTemperatures.getHostname());
                }
                if (null == inputDouble) continue;
                sumInput = sumInput + inputDouble;
            }
            catch (Exception e) {
                logger.error("cpuTemperatures\u8f6c\u6362\u9519\u8bef", (Throwable)e);
            }
        }
        if (cpuTemperaturesList.size() > 0) {
            avgInput = sumInput / (double)cpuTemperaturesList.size();
        } else {
            minInput = 0.0;
        }
        this.reportInstanceService.mergeReportInsToList(reportInstanceList, "cpu\u6e29\u5ea6\u6700\u9ad8\u503c", maxInput + maxInputHostName, reportInfo.getId());
        this.reportInstanceService.mergeReportInsToList(reportInstanceList, "cpu\u6e29\u5ea6\u5e73\u5747\u503c", FormatUtil.formatDouble(avgInput, 2) + "", reportInfo.getId());
        this.reportInstanceService.mergeReportInsToList(reportInstanceList, "cpu\u6e29\u5ea6\u6700\u4f4e\u503c", minInput + minInputHostName, reportInfo.getId());
        HashMap<String, Object> paramsPasswdInfo = new HashMap<String, Object>();
        paramsPasswdInfo.put("groupId", params.get("groupId"));
        int passwdInfoSize = this.passwdInfoService.countByParams(paramsPasswdInfo);
        this.reportInstanceService.mergeReportInsToList(reportInstanceList, "\u8bbe\u5907\u8d26\u53f7\u603b\u6570\u91cf", passwdInfoSize + "", reportInfo.getId());
        HashMap<String, Object> paramsHostWarnDiy = new HashMap<String, Object>();
        paramsHostWarnDiy.put("groupId", params.get("groupId"));
        int hostWarnDiySize = this.hostWarnDiyService.countByParams(paramsHostWarnDiy);
        this.reportInstanceService.mergeReportInsToList(reportInstanceList, "\u81ea\u5b9a\u4e49\u544a\u8b66\u8bbe\u7f6e\u603b\u6570\u91cf", hostWarnDiySize + "", reportInfo.getId());
        if (null == params.get("groupId")) {
            HashMap<String, Object> paramsK8SMonitor = new HashMap<String, Object>();
            paramsK8SMonitor.put("dataType", "node");
            int k8sNodeSize = this.k8sMonitorService.countByParams(paramsK8SMonitor);
            this.reportInstanceService.mergeReportInsToList(reportInstanceList, "K8S Node\u603b\u6570\u91cf", k8sNodeSize + "", reportInfo.getId());
            paramsK8SMonitor.put("dataType", "deployment");
            int k8sDeploymentSize = this.k8sMonitorService.countByParams(paramsK8SMonitor);
            this.reportInstanceService.mergeReportInsToList(reportInstanceList, "K8S Deployment\u603b\u6570\u91cf", k8sDeploymentSize + "", reportInfo.getId());
            paramsK8SMonitor.put("dataType", "namespace");
            int k8sNamespaceSize = this.k8sMonitorService.countByParams(paramsK8SMonitor);
            this.reportInstanceService.mergeReportInsToList(reportInstanceList, "K8S Namespace\u603b\u6570\u91cf", k8sNamespaceSize + "", reportInfo.getId());
            paramsK8SMonitor.put("dataType", "service");
            int k8sServiceSize = this.k8sMonitorService.countByParams(paramsK8SMonitor);
            this.reportInstanceService.mergeReportInsToList(reportInstanceList, "K8S Service\u603b\u6570\u91cf", k8sServiceSize + "", reportInfo.getId());
            paramsK8SMonitor.put("dataType", "pod");
            int k8sPodSize = this.k8sMonitorService.countByParams(paramsK8SMonitor);
            this.reportInstanceService.mergeReportInsToList(reportInstanceList, "K8S Pod\u603b\u6570\u91cf", k8sPodSize + "", reportInfo.getId());
            paramsK8SMonitor.put("dataType", "container");
            int k8sContainerSize = this.k8sMonitorService.countByParams(paramsK8SMonitor);
            this.reportInstanceService.mergeReportInsToList(reportInstanceList, "K8S Container\u603b\u6570\u91cf", k8sContainerSize + "", reportInfo.getId());
            HashMap<String, Object> paramsKafkaMonitor = new HashMap<String, Object>();
            int kafkaSize = this.kafkaMonitorService.countByParams(paramsKafkaMonitor);
            this.reportInstanceService.mergeReportInsToList(reportInstanceList, "\u4e2d\u95f4\u4ef6Kafka\u603b\u8bb0\u5f55", kafkaSize + "", reportInfo.getId());
            HashMap<String, Object> paramsRedisMonitor = new HashMap<String, Object>();
            int redisSize = this.redisMonitorService.countByParams(paramsRedisMonitor);
            this.reportInstanceService.mergeReportInsToList(reportInstanceList, "\u4e2d\u95f4\u4ef6Redis\u603b\u8bb0\u5f55", redisSize + "", reportInfo.getId());
            List<JSONObject> rabbitmqList = RabbitmqUtil.viewRabbitmqHandler();
            this.reportInstanceService.mergeReportInsToList(reportInstanceList, "\u4e2d\u95f4\u4ef6RabbitMQ\u603b\u8bb0\u5f55", rabbitmqList.size() + "", reportInfo.getId());
            List<JSONObject> activemqList = ActivemqUtil.viewActivemqHandler();
            this.reportInstanceService.mergeReportInsToList(reportInstanceList, "\u4e2d\u95f4\u4ef6ActiveMQ\u603b\u8bb0\u5f55", activemqList.size() + "", reportInfo.getId());
            List<JSONObject> tomcatList = TomcatUtil.viewTomcatHandler();
            this.reportInstanceService.mergeReportInsToList(reportInstanceList, "\u4e2d\u95f4\u4ef6Tomcat\u603b\u8bb0\u5f55", tomcatList.size() + "", reportInfo.getId());
            List<JSONObject> scanIPList = ScanIPUtil.viewScanIPHandler();
            this.reportInstanceService.mergeReportInsToList(reportInstanceList, "\u81ea\u52a8\u53d1\u73b0\u8bbe\u5907\u603b\u6570\u91cf", scanIPList.size() + "", reportInfo.getId());
        }
        HashMap<String, Object> paramsTaskJob = new HashMap<String, Object>();
        paramsTaskJob.put("groupId", params.get("groupId"));
        paramsTaskJob.put("active", "1");
        int taskJobInfoSize = this.taskJobInfoService.countByParams(paramsTaskJob);
        paramsTaskJob.put("active", "2");
        int taskJobInfoDownSize = this.taskJobInfoService.countByParams(paramsTaskJob);
        this.reportInstanceService.mergeReportInsToList(reportInstanceList, "\u8ba1\u5212\u4efb\u52a1\u8fd0\u884c\u60c5\u51b5", "\u6b63\u5728\u8fd0\u884c\u7684\u6570\u91cf\uff1a" + taskJobInfoSize + "\uff0c\u5df2\u505c\u6b62\u7684\u6570\u91cf\uff1a" + taskJobInfoDownSize, reportInfo.getId());
        HashMap<String, Object> paramsSnmpDeep = new HashMap<String, Object>();
        paramsSnmpDeep.put("groupId", params.get("groupId"));
        int snmpDeepInfoSize = this.snmpDeepInfoService.countByParams(paramsSnmpDeep);
        this.reportInstanceService.mergeReportInsToList(reportInstanceList, "SNMP\u6df1\u5ea6\u76d1\u63a7\u8bbe\u5907", "SNMP\u6df1\u5ea6\u76d1\u63a7\u8bbe\u5907\u6570\u91cf\uff1a" + snmpDeepInfoSize, reportInfo.getId());
    }

    public void countWarnInfoLastWeekHandler() throws Exception {
        HashMap<String, Object> params = new HashMap<String, Object>();
        String timePart = DateUtil.setLastWeekBeginEndTime(params);
        logger.info("\u7edf\u8ba1\u4e0a\u4e00\u5468\u7684\u544a\u8b66\u6570\u91cf\u548c\u6062\u590d\u6570\u91cf\u67e5\u8be2\u6761\u4ef6--------" + ((Object)params).toString());
        if ("false".equals(this.mailConfig.getAllWarnMail()) || "false".equals(this.mailConfig.getLastWeekWarnMail())) {
            logger.info("\u7edf\u8ba1\u4e0a\u4e00\u5468\u7684\u544a\u8b66\u6570\u91cf\u548c\u6062\u590d\u6570\u91cf\uff0c\u544a\u8b66\u5f00\u5173\u5df2\u914d\u7f6e\u5173\u95ed");
            return;
        }
        params.put("state", "1");
        int warnSize = this.logInfoService.countByParams(params);
        params.put("state", "3");
        int recoveredSize = this.logInfoService.countByParams(params);
        Runnable runnable = () -> {
            try {
                WarnOtherUtil.sendUtil(timePart + "\u544a\u8b66\u6d88\u606f\u6570\u91cf\u548c\u6062\u590d\u6d88\u606f\u6570\u91cf\u7edf\u8ba1\u8be6\u60c5", timePart + "\uff0c\u544a\u8b66\u6d88\u606f\u6570\u91cf\u603b\u8ba1" + warnSize + "\uff0c\u6062\u590d\u6d88\u606f\u6570\u91cf\u603b\u8ba1" + recoveredSize, "", "", true, "ERROR", "");
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        };
        ThreadPoolUtil.executor.execute(runnable);
    }
}

