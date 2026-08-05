/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.dapingController;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.github.pagehelper.PageInfo;
import com.wgcloud.config.CommonConfig;
import com.wgcloud.entity.SystemInfo;
import com.wgcloud.service.AccountInfoService;
import com.wgcloud.service.AppExceptionInfoService;
import com.wgcloud.service.AppInfoService;
import com.wgcloud.service.CustomInfoService;
import com.wgcloud.service.DbInfoService;
import com.wgcloud.service.DbTableService;
import com.wgcloud.service.DceInfoService;
import com.wgcloud.service.DiskStateService;
import com.wgcloud.service.DockerInfoService;
import com.wgcloud.service.FileSafeService;
import com.wgcloud.service.FileWarnInfoService;
import com.wgcloud.service.FtpInfoService;
import com.wgcloud.service.HeathMonitorService;
import com.wgcloud.service.K8sMonitorService;
import com.wgcloud.service.KafkaMonitorService;
import com.wgcloud.service.LogInfoService;
import com.wgcloud.service.PortInfoService;
import com.wgcloud.service.RedisMonitorService;
import com.wgcloud.service.ShellInfoService;
import com.wgcloud.service.SnmpDeepInfoService;
import com.wgcloud.service.SnmpInfoService;
import com.wgcloud.service.SystemInfoService;
import com.wgcloud.service.TaskJobInfoService;
import com.wgcloud.util.ActivemqUtil;
import com.wgcloud.util.HostUtil;
import com.wgcloud.util.PowerEnvUtil;
import com.wgcloud.util.RabbitmqUtil;
import com.wgcloud.util.license.LicenseUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value={"/dapingSeven"})
public class DapingSevenController {
    private static final Logger logger = LoggerFactory.getLogger(DapingSevenController.class);
    @Resource
    private DbTableService dbTableService;
    @Resource
    private PortInfoService portInfoService;
    @Resource
    private FileSafeService fileSafeService;
    @Resource
    private DceInfoService dceInfoService;
    @Resource
    private SnmpInfoService snmpInfoService;
    @Resource
    private DbInfoService dbInfoService;
    @Resource
    private SystemInfoService systemInfoService;
    @Resource
    private AppInfoService appInfoService;
    @Resource
    private FileWarnInfoService fileWarnInfoService;
    @Resource
    private CustomInfoService customInfoService;
    @Resource
    private AppExceptionInfoService appExceptionInfoService;
    @Resource
    private DockerInfoService dockerInfoService;
    @Resource
    private LogInfoService logInfoService;
    @Autowired
    private HeathMonitorService heathMonitorService;
    @Autowired
    private ShellInfoService shellInfoService;
    @Resource
    private AccountInfoService accountInfoService;
    @Resource
    private DiskStateService diskStateService;
    @Resource
    private FtpInfoService ftpInfoService;
    @Resource
    private TaskJobInfoService taskJobInfoService;
    @Resource
    private SnmpDeepInfoService snmpDeepInfoService;
    @Resource
    private RedisMonitorService redisMonitorService;
    @Resource
    private KafkaMonitorService kafkaMonitorService;
    @Resource
    private K8sMonitorService k8sMonitorService;
    @Autowired
    private CommonConfig commonConfig;

    @RequestMapping(value={"index"})
    public String index(Model model, HttpServletRequest request) {
        if (!LicenseUtil.checkEnterpriseVersion()) {
            return "daping/error";
        }
        try {
            HashMap<String, Object> params = new HashMap<String, Object>();
            params.put("countBlockNe", "2");
            HostUtil.addAccountqueryDaping(request, params);
            int totalSystemInfoSize = this.systemInfoService.countByParams(params);
            model.addAttribute("totalSystemInfoSize", (Object)totalSystemInfoSize);
            params.put("state", "2");
            int hostDownSize = this.systemInfoService.countByParams(params);
            model.addAttribute("hostDownSize", (Object)hostDownSize);
            params.clear();
            HostUtil.addAccountqueryDaping(request, params);
            int dceSize = this.dceInfoService.countByParams(params);
            params.put("resTimes", -1);
            int dceDownSize = this.dceInfoService.countByParams(params);
            params.clear();
            HostUtil.addAccountqueryDaping(request, params);
            int snmpSize = this.snmpInfoService.countByParams(params);
            params.put("state", "2");
            int snmpDownSize = this.snmpInfoService.countByParams(params);
            params.clear();
            HostUtil.addAccountqueryDaping(request, params);
            int snmpDeepSize = this.snmpDeepInfoService.countByParams(params);
            params.put("state", "2");
            int snmpDeepDownSize = this.snmpDeepInfoService.countByParams(params);
            model.addAttribute("dceSnmpSize", (Object)(dceSize + snmpSize + snmpDeepSize));
            model.addAttribute("dceSnmpDownSize", (Object)(dceDownSize + snmpDownSize + snmpDeepDownSize));
            params.clear();
            HostUtil.addAccountqueryDaping(request, params);
            int dbInfoSize = this.dbInfoService.countByParams(params);
            model.addAttribute("dbInfoSize", (Object)dbInfoSize);
            params.put("dbState", "2");
            int dbInfoDownSize = this.dbInfoService.countByParams(params);
            model.addAttribute("dbInfoDownSize", (Object)dbInfoDownSize);
            this.systemTop10(model, request);
            this.rightTabelData(model, request);
            HostUtil.addDapingTipMsg(request, model);
            return "daping/indexSeven";
        }
        catch (Exception e) {
            logger.error("\u5927\u5c4f\u5c55\u793a\u9519\u8bef", (Throwable)e);
            return "";
        }
    }

    private void systemTop10(Model model, HttpServletRequest request) {
        try {
            HashMap<String, Object> params = new HashMap<String, Object>();
            params.put("orderBy", "CREATE_TIME");
            params.put("orderType", "DESC");
            HostUtil.addAccountqueryDaping(request, params);
            PageInfo pageInfo = this.systemInfoService.selectByParams(params, 1, 10);
            ArrayList<String> systemTop10NameList = new ArrayList<String>();
            ArrayList<Double> cpuTop10ValList = new ArrayList<Double>();
            ArrayList<String> rxbytTop10List = new ArrayList<String>();
            ArrayList<String> txbytTop10List = new ArrayList<String>();
            ArrayList<Double> memTop10ValList = new ArrayList<Double>();
            for (SystemInfo systemInfo : (java.util.List<SystemInfo>) pageInfo.getList()) {
                systemTop10NameList.add(systemInfo.getHostname());
                cpuTop10ValList.add(systemInfo.getCpuPer());
                rxbytTop10List.add(systemInfo.getRxbyt());
                txbytTop10List.add(systemInfo.getTxbyt());
                memTop10ValList.add(systemInfo.getMemPer());
            }
            model.addAttribute("systemTop10NameList", (Object)JSONUtil.parseArray(systemTop10NameList));
            model.addAttribute("cpuTop10ValList", (Object)JSONUtil.parseArray(cpuTop10ValList));
            model.addAttribute("rxbytTop10List", (Object)JSONUtil.parseArray(rxbytTop10List));
            model.addAttribute("txbytTop10List", (Object)JSONUtil.parseArray(txbytTop10List));
            model.addAttribute("memTop10ValList", (Object)JSONUtil.parseArray(memTop10ValList));
        }
        catch (Exception e) {
            logger.error("cpu\u4f7f\u7528\u7387\u6700\u65b0\u4e0a\u62a5\u524d10\u9519\u8bef", (Throwable)e);
        }
    }

    private void rightTabelData(Model model, HttpServletRequest request) {
        try {
            HashMap<String, Object> params = new HashMap<String, Object>();
            HostUtil.addAccountqueryDaping(request, params);
            int totalSizeApp = this.appInfoService.countByParams(params);
            model.addAttribute("totalSizeApp", (Object)totalSizeApp);
            params.put("state", "2");
            int downAppSize = this.appInfoService.countByParams(params);
            model.addAttribute("downAppSize", (Object)downAppSize);
            params.clear();
            HostUtil.addAccountqueryDaping(request, params);
            int dockerSize = this.dockerInfoService.countByParams(params);
            model.addAttribute("dockerSize", (Object)dockerSize);
            params.put("state", "2");
            int downDockerSize = this.dockerInfoService.countByParams(params);
            model.addAttribute("downDockerSize", (Object)downDockerSize);
            params.clear();
            HostUtil.addAccountqueryDaping(request, params);
            int portSize = this.portInfoService.countByParams(params);
            model.addAttribute("portSize", (Object)portSize);
            params.put("state", "2");
            int portDownSize = this.portInfoService.countByParams(params);
            model.addAttribute("portDownSize", (Object)portDownSize);
            params.clear();
            HostUtil.addAccountqueryDaping(request, params);
            int dceSize = this.dceInfoService.countByParams(params);
            model.addAttribute("dceSize", (Object)dceSize);
            params.put("resTimes", -1);
            int dceDownSize = this.dceInfoService.countByParams(params);
            model.addAttribute("dceDownSize", (Object)dceDownSize);
            params.clear();
            HostUtil.addAccountqueryDaping(request, params);
            int snmpSize = this.snmpInfoService.countByParams(params);
            model.addAttribute("snmpSize", (Object)snmpSize);
            params.put("state", "2");
            int snmpDownSize = this.snmpInfoService.countByParams(params);
            model.addAttribute("snmpDownSize", (Object)snmpDownSize);
            params.clear();
            HostUtil.addAccountqueryDaping(request, params);
            Integer fileWarnSize = this.fileWarnInfoService.countByParams(params);
            model.addAttribute("fileWarnSize", (Object)(fileWarnSize == null ? 0 : fileWarnSize));
            params.clear();
            HostUtil.addAccountqueryDaping(request, params);
            Integer customInfoSize = this.customInfoService.countByParams(params);
            model.addAttribute("customInfoSize", (Object)(customInfoSize == null ? 0 : customInfoSize));
            params.put("state", "2");
            int customInfoDownSize = this.customInfoService.countByParams(params);
            model.addAttribute("customInfoDownSize", (Object)customInfoDownSize);
            params.clear();
            HostUtil.addAccountqueryDaping(request, params);
            Integer fileSafeSize = this.fileSafeService.countByParams(params);
            model.addAttribute("fileSafeSize", (Object)(fileSafeSize == null ? 0 : fileSafeSize));
            params.put("state", "2");
            int fileSafeDownSize = this.fileSafeService.countByParams(params);
            model.addAttribute("fileSafeDownSize", (Object)fileSafeDownSize);
            params.clear();
            HostUtil.addAccountqueryDaping(request, params);
            Integer dbTableSize = this.dbTableService.countByParams(params);
            model.addAttribute("dbTableSize", (Object)dbTableSize);
            params.put("state", "2");
            int dbTableDownSize = this.dbTableService.countByParams(params);
            model.addAttribute("dbTableDownSize", (Object)dbTableDownSize);
            params.clear();
            HostUtil.addAccountqueryDaping(request, params);
            int dbInfoSize = this.dbInfoService.countByParams(params);
            model.addAttribute("dbInfoSize", (Object)dbInfoSize);
            params.put("dbState", "2");
            int dbInfoDownSize = this.dbInfoService.countByParams(params);
            model.addAttribute("dbInfoDownSize", (Object)dbInfoDownSize);
            params.clear();
            HostUtil.addAccountqueryDaping(request, params);
            int heathSize = this.heathMonitorService.countByParams(params);
            model.addAttribute("heathSize", (Object)heathSize);
            params.put("heathStatus", "200");
            int heath200Size = this.heathMonitorService.countByParams(params);
            model.addAttribute("heathDownSize", (Object)(heathSize - heath200Size));
            params.clear();
            HostUtil.addAccountqueryDaping(request, params);
            int shellInfoSize = this.shellInfoService.countByParams(params);
            model.addAttribute("shellInfoSize", (Object)shellInfoSize);
            params.clear();
            HostUtil.addAccountqueryDaping(request, params);
            int ftpInfoSize = this.ftpInfoService.countByParams(params);
            model.addAttribute("ftpInfoSize", (Object)ftpInfoSize);
            params.put("state", "2");
            int ftpInfoDownSize = this.ftpInfoService.countByParams(params);
            model.addAttribute("ftpInfoDownSize", (Object)ftpInfoDownSize);
            params.clear();
            HostUtil.addAccountqueryDaping(request, params);
            int taskJobSize = this.taskJobInfoService.countByParams(params);
            model.addAttribute("taskJobSize", (Object)taskJobSize);
            params.clear();
            HostUtil.addAccountqueryDaping(request, params);
            Integer appExceptionSize = this.appExceptionInfoService.countByParams(params);
            model.addAttribute("appExceptionSize", (Object)(appExceptionSize == null ? 0 : appExceptionSize));
            params.clear();
            HostUtil.addAccountqueryDaping(request, params);
            int snmpDeepSize = this.snmpDeepInfoService.countByParams(params);
            model.addAttribute("snmpDeepSize", (Object)snmpDeepSize);
            params.put("state", "2");
            int snmpDeepDownSize = this.snmpDeepInfoService.countByParams(params);
            model.addAttribute("snmpDeepDownSize", (Object)snmpDeepDownSize);
            params.clear();
            int redisMonitorSize = this.redisMonitorService.countByParams(params);
            model.addAttribute("redisMonitorSize", (Object)redisMonitorSize);
            params.put("state", "2");
            int redisMonitorDownSize = this.redisMonitorService.countByParams(params);
            model.addAttribute("redisMonitorDownSize", (Object)redisMonitorDownSize);
            params.clear();
            int kafkaSize = this.kafkaMonitorService.countByParams(params);
            model.addAttribute("kafkaSize", (Object)kafkaSize);
            params.put("state", "2");
            int kafkaDownSize = this.kafkaMonitorService.countByParams(params);
            model.addAttribute("kafkaDownSize", (Object)kafkaDownSize);
            List<JSONObject> powerEnvList = PowerEnvUtil.viewPowerEnvHandler();
            model.addAttribute("powerEnvListSize", (Object)powerEnvList.size());
            List<JSONObject> rabbitmqList = RabbitmqUtil.viewRabbitmqHandler();
            model.addAttribute("rabbitmqListSize", (Object)rabbitmqList.size());
            List<JSONObject> activemqList = ActivemqUtil.viewActivemqHandler();
            model.addAttribute("activemqListSize", (Object)activemqList.size());
            int k8sSize = this.k8sMonitorService.countByParams(params);
            model.addAttribute("k8sSize", (Object)k8sSize);
        }
        catch (Exception e) {
            logger.error("v7\u5927\u5c4f\u6570\u636e\u67e5\u8be2\u9519\u8bef", (Throwable)e);
        }
    }
}

