/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.service;

import com.wgcloud.entity.SystemInfo;
import com.wgcloud.service.AppInfoService;
import com.wgcloud.service.DiskStateService;
import com.wgcloud.service.DockerInfoService;
import com.wgcloud.service.PortInfoService;
import com.wgcloud.service.SystemInfoService;
import com.wgcloud.util.FormatUtil;
import com.wgcloud.util.HostUtil;
import com.wgcloud.util.PropertyUtil;
import com.wgcloud.util.license.LicenseUtil;
import com.wgcloud.util.staticvar.StaticKeys;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TaskUtilService {
    private Logger logger = LoggerFactory.getLogger(TaskUtilService.class);
    @Autowired
    SystemInfoService systemInfoService;
    @Autowired
    AppInfoService appInfoService;
    @Autowired
    DockerInfoService dockerInfoService;
    @Autowired
    PortInfoService portInfoService;
    @Autowired
    DiskStateService diskStateService;
    @Autowired
    private ServletContext servletContext;

    @Transactional
    public void refreshCommitDate() throws Exception {
        Date nowDate = new Date();
        this.logger.info("\u5237\u65b0\u76d1\u63a7\u6570\u636e\u66f4\u65b0\u65f6\u95f4\uff1a" + nowDate);
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("state", "1");
        List<SystemInfo> systemInfolist = this.systemInfoService.selectAllByParamsForTask(params);
        ArrayList<SystemInfo> systemInfoUpdateList = new ArrayList<SystemInfo>();
        for (SystemInfo systemInfo : systemInfolist) {
            SystemInfo systemInfoUpdate = new SystemInfo();
            systemInfoUpdate.setCreateTime(nowDate);
            systemInfoUpdate.setId(systemInfo.getId());
            systemInfoUpdateList.add(systemInfoUpdate);
        }
        this.systemInfoService.updateRecord(systemInfoUpdateList);
    }

    public String sumDiskSizeCache(HttpServletRequest request) throws Exception {
        HashMap<String, Object> params = new HashMap<String, Object>();
        HostUtil.addAccountquery(request, params);
        params.put("countBlockNe", "2");
        List<SystemInfo> hostList = this.systemInfoService.selectAllByParams(params);
        BigDecimal sumSize = new BigDecimal(0);
        for (SystemInfo systemInfo : hostList) {
            if (StringUtils.isEmpty((CharSequence)systemInfo.getDiskSumSize())) continue;
            try {
                sumSize = sumSize.add(new BigDecimal(systemInfo.getDiskSumSize().replace("G", "")));
            }
            catch (Exception e) {
                this.logger.error("double\u7c7b\u578b\u8f6c\u6362\u9519\u8bef", (Throwable)e);
            }
        }
        String sumSizeStr = String.valueOf(sumSize);
        if (sumSizeStr.indexOf(".") > 0) {
            sumSizeStr = sumSizeStr.substring(0, sumSizeStr.lastIndexOf("."));
        }
        return FormatUtil.gToT(sumSizeStr);
    }

    public String sumDiskSizeCacheByGroup(Object groupId) {
        try {
            HashMap<String, Object> params = new HashMap<String, Object>();
            params.put("groupId", groupId);
            params.put("countBlockNe", "2");
            List<SystemInfo> hostList = this.systemInfoService.selectAllByParams(params);
            BigDecimal sumSize = new BigDecimal(0);
            for (SystemInfo systemInfo : hostList) {
                if (StringUtils.isEmpty((CharSequence)systemInfo.getDiskSumSize())) continue;
                try {
                    sumSize = sumSize.add(new BigDecimal(systemInfo.getDiskSumSize().replace("G", "")));
                }
                catch (Exception e) {
                    this.logger.error("double\u7c7b\u578b\u8f6c\u6362\u9519\u8bef", (Throwable)e);
                }
            }
            String sumSizeStr = String.valueOf(sumSize);
            if (sumSizeStr.indexOf(".") > 0) {
                sumSizeStr = sumSizeStr.substring(0, sumSizeStr.lastIndexOf("."));
            }
            return FormatUtil.gToT(sumSizeStr);
        }
        catch (Exception e) {
            this.logger.error("\u6839\u636e\u6807\u7b7e\u7edf\u8ba1\u78c1\u76d8\u5bb9\u91cf\u603b\u548c\u9519\u8bef", (Throwable)e);
            return "";
        }
    }

    public String getValFromContext(String key, String defaultVal) {
        if (StringUtils.isEmpty((CharSequence)key)) {
            return defaultVal;
        }
        Object obj = this.servletContext.getAttribute(key);
        if (null == obj) {
            return defaultVal;
        }
        return obj.toString();
    }

    public void initMenuNames() {
        try {
            this.servletContext.setAttribute("jianKongGaiYao", "\u76d1\u63a7\u6982\u8981");
            this.servletContext.setAttribute("ziYuanGuanLi", "\u8d44\u6e90\u7ba1\u7406");
            this.servletContext.setAttribute("zhuJiGuanLi", "\u4e3b\u673a\u7ba1\u7406");
            this.servletContext.setAttribute("yiChangJinCheng", "\u4e3b\u673a\u5f02\u5e38\u8fdb\u7a0b");
            this.servletContext.setAttribute("jinChengJianCe", "\u8fdb\u7a0b\u76d1\u6d4b");
            this.servletContext.setAttribute("duanKouJianCe", "\u7aef\u53e3\u76d1\u6d4b");
            this.servletContext.setAttribute("riZhiJianKong", "\u65e5\u5fd7\u76d1\u63a7");
            this.servletContext.setAttribute("wenJianFangCuanGai", "\u6587\u4ef6\u9632\u7be1\u6539");
            this.servletContext.setAttribute("dockerJianCe", "DOCKER\u76d1\u6d4b");
            this.servletContext.setAttribute("ziDingYiJianKongXiang", "\u81ea\u5b9a\u4e49\u76d1\u63a7\u9879");
            this.servletContext.setAttribute("shuJuJianKong", "\u6570\u636e\u76d1\u63a7");
            this.servletContext.setAttribute("shuJuKuGuanLi", "\u6570\u636e\u5e93\u7ba1\u7406");
            this.servletContext.setAttribute("shuJuBiaoJianCe", "\u6570\u636e\u8868\u76d1\u6d4b");
            this.servletContext.setAttribute("fuWuJieKouJianCe", "\u670d\u52a1\u63a5\u53e3\u76d1\u6d4b");
            this.servletContext.setAttribute("zhanShiKanBan", "\u5c55\u793a\u770b\u677f");
            this.servletContext.setAttribute("wangLuoSheBeiJianCe", "\u7f51\u7edc\u8bbe\u5907\u76d1\u6d4b");
            this.servletContext.setAttribute("pingJianCe", "PING\u76d1\u6d4b");
            this.servletContext.setAttribute("snmpJianCe", "SNMP\u76d1\u6d4b");
            this.servletContext.setAttribute("snmpDeepJianCe", "SNMP\u6df1\u5ea6\u76d1\u63a7");
            this.servletContext.setAttribute("ftpJianCe", "FTP/SFTP\u76d1\u6d4b");
            this.servletContext.setAttribute("zhongJianJianJianCe", "\u4e2d\u95f4\u4ef6\u76d1\u6d4b");
            this.servletContext.setAttribute("redis", "Redis");
            this.servletContext.setAttribute("tomcat", "Tomcat");
            this.servletContext.setAttribute("dongHuanJianCe", "\u52a8\u73af\u76d1\u6d4b");
            this.servletContext.setAttribute("nginxRiZhiJianCe", "Nginx\u65e5\u5fd7\u68c0\u6d4b");
            this.servletContext.setAttribute("kafka", "Kafka");
            this.servletContext.setAttribute("rabbitmq", "RabbitMQ");
            this.servletContext.setAttribute("activemq", "ActiveMQ");
            this.servletContext.setAttribute("k8sNode", "K8S-Node");
            this.servletContext.setAttribute("k8sDeployment", "K8S-Deployment");
            this.servletContext.setAttribute("k8sNamespace", "K8S-Namespace");
            this.servletContext.setAttribute("k8sPod", "K8S-Pod");
            this.servletContext.setAttribute("k8sService", "K8S-Service");
            this.servletContext.setAttribute("k8sContainer", "K8S-Container");
            this.servletContext.setAttribute("serverBackupName", "Server-Backup");
            this.servletContext.setAttribute("ufmJianCe", "UFM\u76d1\u63a7");
            this.servletContext.setAttribute("tuopuTu", "\u62d3\u6251\u56fe");
            this.servletContext.setAttribute("zhuJiTuopuTu", "\u4e3b\u673a\u62d3\u6251\u56fe");
            this.servletContext.setAttribute("pingTuopuTu", "PING\u62d3\u6251\u56fe");
            this.servletContext.setAttribute("snmpTuopuTu", "SNMP\u62d3\u6251\u56fe");
            this.servletContext.setAttribute("snmpDeepTuopuTu", "SNMP\u6df1\u5ea6\u62d3\u6251");
            this.servletContext.setAttribute("xunJianBaoGao", "\u5de1\u68c0\u62a5\u544a");
            this.servletContext.setAttribute("jiHuaRenWu", "\u8ba1\u5212\u4efb\u52a1");
            this.servletContext.setAttribute("xiTongGuanLi", "\u7cfb\u7edf\u7ba1\u7406");
            this.servletContext.setAttribute("xiTongRiZhi", "\u7cfb\u7edf\u65e5\u5fd7");
            this.servletContext.setAttribute("zhiLingXiaFa", "\u6307\u4ee4\u4e0b\u53d1");
            this.servletContext.setAttribute("biaoQianGuanLi", "\u6807\u7b7e\u7ba1\u7406");
            this.servletContext.setAttribute("chengYuanZhangHao", "\u6210\u5458\u8d26\u53f7");
            this.servletContext.setAttribute("gaoJingSheZhi", "\u544a\u8b66\u8bbe\u7f6e");
            this.servletContext.setAttribute("youJianGaoJing", "\u90ae\u4ef6\u544a\u8b66");
            this.servletContext.setAttribute("jiaoBenGaoJing", "\u811a\u672c\u544a\u8b66");
            this.servletContext.setAttribute("ziDingYiGaoJing", "\u81ea\u5b9a\u4e49\u544a\u8b66");
            this.servletContext.setAttribute("ziChanGuanLi", "\u8d44\u4ea7\u7ba1\u7406");
            this.servletContext.setAttribute("sheBeiZhangHao", "\u8bbe\u5907\u8d26\u53f7\u7ba1\u7406");
            this.servletContext.setAttribute("gongZuoBiJi", "\u5de5\u4f5c\u7b14\u8bb0");
            this.servletContext.setAttribute("zhongDuanYunXing", "\u7ec8\u7aef\u8fd0\u884c\u7edf\u8ba1");
            this.servletContext.setAttribute("ziDongFaXian", "\u81ea\u52a8\u53d1\u73b0");
            this.servletContext.setAttribute("aillm", "AI-LLM");
        }
        catch (Exception e) {
            this.logger.error("\u521d\u59cb\u5316\u8bbe\u7f6e\u83dc\u5355\u540d\u79f0\u9519\u8bef", (Throwable)e);
        }
    }

    public void setDiyMenuNames() {
        try {
            Map<String, String> mapColNames = PropertyUtil.getMenuNames(StaticKeys.JAR_PATH + "/config/menusNameDiy.properties");
            if (null != mapColNames) {
                if (!LicenseUtil.checkEnterpriseVersion()) {
                    this.logger.info("\u5f53\u524d\u5df2\u4f7f\u7528\u7cfb\u7edf\u9ed8\u8ba4\u914d\u7f6e\u7684\u83dc\u5355\u540d\u79f0");
                    return;
                }
                for (Map.Entry<String, String> entry : mapColNames.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    this.servletContext.setAttribute(key, (Object)value);
                }
                this.logger.info("\u5f53\u524d\u5df2\u4f7f\u7528\u81ea\u5b9a\u4e49\u914d\u7f6e\u7684\u83dc\u5355\u540d\u79f0");
            }
        }
        catch (Exception e) {
            this.logger.error("\u81ea\u5b9a\u4e49\u8bbe\u7f6e\u83dc\u5355\u540d\u79f0\u9519\u8bef", (Throwable)e);
        }
    }
}

