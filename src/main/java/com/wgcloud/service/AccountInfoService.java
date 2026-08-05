/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wgcloud.dto.MenuTreeNodeDto;
import com.wgcloud.entity.AccountInfo;
import com.wgcloud.mapper.AccountInfoMapper;
import com.wgcloud.service.DbInfoService;
import com.wgcloud.service.DceInfoService;
import com.wgcloud.service.EquipmentService;
import com.wgcloud.service.FtpInfoService;
import com.wgcloud.service.HeathMonitorService;
import com.wgcloud.service.HostWarnDiyService;
import com.wgcloud.service.LogInfoService;
import com.wgcloud.service.PasswdInfoService;
import com.wgcloud.service.ShellNoteInfoService;
import com.wgcloud.service.SnmpInfoService;
import com.wgcloud.service.SystemInfoService;
import com.wgcloud.service.TaskJobInfoService;
import com.wgcloud.service.TaskUtilService;
import com.wgcloud.util.HostUtil;
import com.wgcloud.util.UUIDUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccountInfoService {
    @Autowired
    private AccountInfoMapper accountInfoMapper;
    @Autowired
    private LogInfoService logInfoService;
    @Resource
    private SystemInfoService systemInfoService;
    @Resource
    private DbInfoService dbInfoService;
    @Resource
    private HeathMonitorService heathMonitorService;
    @Resource
    private PasswdInfoService passwdInfoService;
    @Resource
    private SnmpInfoService snmpInfoService;
    @Resource
    private DceInfoService dceInfoService;
    @Resource
    private FtpInfoService ftpInfoService;
    @Autowired
    private ShellNoteInfoService shellNoteInfoService;
    @Autowired
    private EquipmentService equipmentService;
    @Resource
    private HostWarnDiyService hostWarnDiyService;
    @Resource
    private TaskJobInfoService taskJobInfoService;
    @Autowired
    private TaskUtilService taskUtilService;

    public PageInfo selectByParams(Map<String, Object> params, int currPage, int pageSize) throws Exception {
        PageHelper.startPage((int)currPage, (int)pageSize);
        List<AccountInfo> list = this.accountInfoMapper.selectByParams(params);
        PageInfo pageInfo = new PageInfo(list);
        return pageInfo;
    }

    public void save(AccountInfo accountInfo) throws Exception {
        accountInfo.setId(UUIDUtil.getUUID());
        accountInfo.setCreateTime(new Date());
        this.accountInfoMapper.save(accountInfo);
    }

    public void updateHostAccount(AccountInfo accountInfo, String[] hostnames) throws Exception {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("account", accountInfo.getAccount());
        params.put("accountTarget", "");
        this.systemInfoService.updateToTargetAccount(params);
        if (null == hostnames || hostnames.length == 0) {
            return;
        }
        params.put("hostnames", hostnames);
        this.systemInfoService.updateAccountByHostName(params);
    }

    @Transactional
    public int deleteById(String[] id) throws Exception {
        return this.accountInfoMapper.deleteById(id);
    }

    public void updateById(AccountInfo accountInfo) throws Exception {
        this.accountInfoMapper.updateById(accountInfo);
    }

    public AccountInfo selectById(String id) throws Exception {
        return this.accountInfoMapper.selectById(id);
    }

    public List<AccountInfo> selectAllByParams(Map<String, Object> params) throws Exception {
        return this.accountInfoMapper.selectAllByParams(params);
    }

    public int countByParams(Map<String, Object> params) throws Exception {
        return this.accountInfoMapper.countByParams(params);
    }

    public void clearOthersAccount(Map<String, Object> params) throws Exception {
        params.put("accountTarget", "");
        this.systemInfoService.updateToTargetAccount(params);
        this.dbInfoService.updateToTargetAccount(params);
        this.heathMonitorService.updateToTargetAccount(params);
        this.passwdInfoService.updateToTargetAccount(params);
        this.snmpInfoService.updateToTargetAccount(params);
        this.dceInfoService.updateToTargetAccount(params);
        this.ftpInfoService.updateToTargetAccount(params);
        this.hostWarnDiyService.updateToTargetAccount(params);
    }

    public void moveToAccount(String accountTarget, String account) throws Exception {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("accountTarget", accountTarget);
        params.put("account", account);
        this.systemInfoService.updateToTargetAccount(params);
        this.dbInfoService.updateToTargetAccount(params);
        this.heathMonitorService.updateToTargetAccount(params);
        this.passwdInfoService.updateToTargetAccount(params);
        this.snmpInfoService.updateToTargetAccount(params);
        this.dceInfoService.updateToTargetAccount(params);
        this.ftpInfoService.updateToTargetAccount(params);
        this.equipmentService.updateToTargetAccount(params);
        this.hostWarnDiyService.updateToTargetAccount(params);
        this.taskJobInfoService.updateToTargetAccount(params);
    }

    public void moveToAccount(String accountTarget, String ids, String resourceType) throws Exception {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("accountTarget", accountTarget);
        params.put("ids", ids.split(","));
        if ("1".equals(resourceType)) {
            this.systemInfoService.updateToTargetAccount(params);
        }
        if ("2".equals(resourceType)) {
            this.heathMonitorService.updateToTargetAccount(params);
        }
        if ("3".equals(resourceType)) {
            this.dbInfoService.updateToTargetAccount(params);
        }
        if ("4".equals(resourceType)) {
            this.ftpInfoService.updateToTargetAccount(params);
        }
        if ("5".equals(resourceType)) {
            this.dceInfoService.updateToTargetAccount(params);
        }
        if ("6".equals(resourceType)) {
            this.snmpInfoService.updateToTargetAccount(params);
        }
        if ("7".equals(resourceType)) {
            this.passwdInfoService.updateToTargetAccount(params);
        }
        if ("8".equals(resourceType)) {
            this.equipmentService.updateToTargetAccount(params);
        }
        if ("9".equals(resourceType)) {
            this.shellNoteInfoService.updateToTargetAccount(params);
        }
        if ("10".equals(resourceType)) {
            this.hostWarnDiyService.updateToTargetAccount(params);
        }
        if ("11".equals(resourceType)) {
            this.taskJobInfoService.updateToTargetAccount(params);
        }
    }

    public void setMenuTreeChecked(List<MenuTreeNodeDto> dtoList, String menuIds) {
        if (StringUtils.isEmpty((CharSequence)menuIds)) {
            return;
        }
        for (MenuTreeNodeDto dto : dtoList) {
            if (menuIds.contains("," + dto.getId() + ",")) continue;
            dto.setChecked(false);
        }
    }

    public String getAllMenuIdsStr() {
        String menuIdsStr = "";
        List<MenuTreeNodeDto> dtoList = this.initMenuTreeList();
        for (MenuTreeNodeDto dto : dtoList) {
            menuIdsStr = menuIdsStr + dto.getId() + ",";
        }
        return "," + menuIdsStr;
    }

    public List<MenuTreeNodeDto> initMenuTreeList() {
        ArrayList<MenuTreeNodeDto> list = new ArrayList<MenuTreeNodeDto>();
        MenuTreeNodeDto dto1 = new MenuTreeNodeDto("1", "0", this.taskUtilService.getValFromContext("ziYuanGuanLi", "\u8d44\u6e90\u7ba1\u7406"), true, true);
        list.add(dto1);
        MenuTreeNodeDto dto2 = new MenuTreeNodeDto("12", "1", this.taskUtilService.getValFromContext("zhuJiGuanLi", "\u4e3b\u673a\u7ba1\u7406"), true, true);
        list.add(dto2);
        MenuTreeNodeDto dto3a = new MenuTreeNodeDto("19", "1", this.taskUtilService.getValFromContext("yiChangJinCheng", "\u4e3b\u673a\u5f02\u5e38\u8fdb\u7a0b"), true, true);
        list.add(dto3a);
        MenuTreeNodeDto dto3 = new MenuTreeNodeDto("13", "1", this.taskUtilService.getValFromContext("jinChengJianCe", "\u8fdb\u7a0b\u76d1\u6d4b"), true, true);
        list.add(dto3);
        MenuTreeNodeDto dto4 = new MenuTreeNodeDto("15", "1", this.taskUtilService.getValFromContext("duanKouJianCe", "\u7aef\u53e3\u76d1\u6d4b"), true, true);
        list.add(dto4);
        MenuTreeNodeDto dto5 = new MenuTreeNodeDto("16", "1", this.taskUtilService.getValFromContext("riZhiJianKong", "\u65e5\u5fd7\u76d1\u63a7"), true, true);
        list.add(dto5);
        MenuTreeNodeDto dto6 = new MenuTreeNodeDto("17", "1", this.taskUtilService.getValFromContext("wenJianFangCuanGai", "\u6587\u4ef6\u9632\u7be1\u6539"), true, true);
        list.add(dto6);
        MenuTreeNodeDto dto7 = new MenuTreeNodeDto("14", "1", this.taskUtilService.getValFromContext("dockerJianCe", "DOCKER\u76d1\u6d4b"), true, true);
        list.add(dto7);
        MenuTreeNodeDto dto8 = new MenuTreeNodeDto("18", "1", this.taskUtilService.getValFromContext("ziDingYiJianKongXiang", "\u81ea\u5b9a\u4e49\u76d1\u63a7\u9879"), true, true);
        list.add(dto8);
        MenuTreeNodeDto dto9 = new MenuTreeNodeDto("4", "0", this.taskUtilService.getValFromContext("shuJuJianKong", "\u6570\u636e\u76d1\u63a7"), true, true);
        list.add(dto9);
        MenuTreeNodeDto dto10 = new MenuTreeNodeDto("41", "4", this.taskUtilService.getValFromContext("shuJuKuGuanLi", "\u6570\u636e\u5e93\u7ba1\u7406"), true, true);
        list.add(dto10);
        MenuTreeNodeDto dto11 = new MenuTreeNodeDto("42", "4", this.taskUtilService.getValFromContext("shuJuBiaoJianCe", "\u6570\u636e\u8868\u76d1\u6d4b"), true, true);
        list.add(dto11);
        MenuTreeNodeDto dto12 = new MenuTreeNodeDto("51", "0", this.taskUtilService.getValFromContext("fuWuJieKouJianCe", "\u670d\u52a1\u63a5\u53e3\u76d1\u6d4b"), true, true);
        list.add(dto12);
        MenuTreeNodeDto dto13 = new MenuTreeNodeDto("b1", "0", this.taskUtilService.getValFromContext("zhanShiKanBan", "\u5c55\u793a\u770b\u677f"), true, true);
        list.add(dto13);
        MenuTreeNodeDto dto14 = new MenuTreeNodeDto("6", "0", this.taskUtilService.getValFromContext("wangLuoSheBeiJianCe", "\u7f51\u7edc\u8bbe\u5907\u76d1\u6d4b"), true, true);
        list.add(dto14);
        MenuTreeNodeDto dto15 = new MenuTreeNodeDto("61", "6", this.taskUtilService.getValFromContext("pingJianCe", "PING\u76d1\u6d4b"), true, true);
        list.add(dto15);
        MenuTreeNodeDto dto16 = new MenuTreeNodeDto("62", "6", this.taskUtilService.getValFromContext("snmpJianCe", "SNMP\u76d1\u6d4b"), true, true);
        list.add(dto16);
        MenuTreeNodeDto dto16a = new MenuTreeNodeDto("63", "6", this.taskUtilService.getValFromContext("snmpDeepJianCe", "SNMP\u6df1\u5ea6\u76d1\u63a7"), true, true);
        list.add(dto16a);
        MenuTreeNodeDto dto17 = new MenuTreeNodeDto("a1", "0", this.taskUtilService.getValFromContext("ftpJianCe", "FTP/SFTP\u76d1\u6d4b"), true, true);
        list.add(dto17);
        MenuTreeNodeDto dtok = new MenuTreeNodeDto("k", "0", this.taskUtilService.getValFromContext("zhongJianJianJianCe", "\u4e2d\u95f4\u4ef6\u76d1\u6d4b"), true, true);
        list.add(dtok);
        MenuTreeNodeDto dtok8 = new MenuTreeNodeDto("k8", "k", this.taskUtilService.getValFromContext("redis", "Redis"), true, true);
        list.add(dtok8);
        MenuTreeNodeDto dtok14 = new MenuTreeNodeDto("k14", "k", this.taskUtilService.getValFromContext("tomcat", "Tomcat"), true, true);
        list.add(dtok14);
        MenuTreeNodeDto dtok29 = new MenuTreeNodeDto("k10", "k", this.taskUtilService.getValFromContext("dongHuanJianCe", "\u52a8\u73af\u76d1\u6d4b"), true, true);
        list.add(dtok29);
        MenuTreeNodeDto dtok9 = new MenuTreeNodeDto("k9", "k", this.taskUtilService.getValFromContext("nginxRiZhiJianCe", "Nginx\u65e5\u5fd7\u68c0\u6d4b"), true, true);
        list.add(dtok9);
        MenuTreeNodeDto dtok7 = new MenuTreeNodeDto("k7", "k", this.taskUtilService.getValFromContext("kafka", "Kafka"), true, true);
        list.add(dtok7);
        MenuTreeNodeDto dtok11 = new MenuTreeNodeDto("k11", "k", this.taskUtilService.getValFromContext("rabbitmq", "RabbitMQ"), true, true);
        list.add(dtok11);
        MenuTreeNodeDto dtok12 = new MenuTreeNodeDto("k12", "k", this.taskUtilService.getValFromContext("activemq", "ActiveMQ"), true, true);
        list.add(dtok12);
        MenuTreeNodeDto dtok1 = new MenuTreeNodeDto("k1", "k", this.taskUtilService.getValFromContext("k8sNode", "K8S-Node"), true, true);
        list.add(dtok1);
        MenuTreeNodeDto dtok6 = new MenuTreeNodeDto("k6", "k", this.taskUtilService.getValFromContext("k8sDeployment", "K8S-Deployment"), true, true);
        list.add(dtok6);
        MenuTreeNodeDto dtok2 = new MenuTreeNodeDto("k2", "k", this.taskUtilService.getValFromContext("k8sNamespace", "K8S-Namespace"), true, true);
        list.add(dtok2);
        MenuTreeNodeDto dtok3 = new MenuTreeNodeDto("k3", "k", this.taskUtilService.getValFromContext("k8sPod", "K8S-Pod"), true, true);
        list.add(dtok3);
        MenuTreeNodeDto dtok4 = new MenuTreeNodeDto("k4", "k", this.taskUtilService.getValFromContext("k8sService", "K8S-Service"), true, true);
        list.add(dtok4);
        MenuTreeNodeDto dtok5 = new MenuTreeNodeDto("k5", "k", this.taskUtilService.getValFromContext("k8sContainer", "K8S-Container"), true, true);
        list.add(dtok5);
        MenuTreeNodeDto dtokServerBackup = new MenuTreeNodeDto("k13", "k", this.taskUtilService.getValFromContext("serverBackupName", "Server-Backup"), true, true);
        list.add(dtokServerBackup);
        MenuTreeNodeDto dto18 = new MenuTreeNodeDto("7", "0", this.taskUtilService.getValFromContext("tuopuTu", "\u62d3\u6251\u56fe"), true, true);
        list.add(dto18);
        MenuTreeNodeDto dto19 = new MenuTreeNodeDto("71", "7", this.taskUtilService.getValFromContext("zhuJiTuopuTu", "\u4e3b\u673a\u62d3\u6251\u56fe"), true, true);
        list.add(dto19);
        MenuTreeNodeDto dto20 = new MenuTreeNodeDto("72", "7", this.taskUtilService.getValFromContext("pingTuopuTu", "PING\u62d3\u6251\u56fe"), true, true);
        list.add(dto20);
        MenuTreeNodeDto dto21 = new MenuTreeNodeDto("73", "7", this.taskUtilService.getValFromContext("snmpTuopuTu", "SNMP\u62d3\u6251\u56fe"), true, true);
        list.add(dto21);
        MenuTreeNodeDto dto21a = new MenuTreeNodeDto("74", "7", this.taskUtilService.getValFromContext("snmpDeepTuopuTu", "SNMP\u6df1\u5ea6\u62d3\u6251"), true, true);
        list.add(dto21a);
        MenuTreeNodeDto dto22 = new MenuTreeNodeDto("91", "0", this.taskUtilService.getValFromContext("xunJianBaoGao", "\u5de1\u68c0\u62a5\u544a"), true, true);
        list.add(dto22);
        MenuTreeNodeDto dtoE1 = new MenuTreeNodeDto("e1", "0", this.taskUtilService.getValFromContext("jiHuaRenWu", "\u8ba1\u5212\u4efb\u52a1"), true, true);
        list.add(dtoE1);
        MenuTreeNodeDto dto23 = new MenuTreeNodeDto("2", "0", this.taskUtilService.getValFromContext("xiTongGuanLi", "\u7cfb\u7edf\u7ba1\u7406"), true, true);
        list.add(dto23);
        MenuTreeNodeDto dto24 = new MenuTreeNodeDto("21", "2", this.taskUtilService.getValFromContext("xiTongRiZhi", "\u7cfb\u7edf\u65e5\u5fd7"), true, true);
        list.add(dto24);
        MenuTreeNodeDto dto26 = new MenuTreeNodeDto("23", "2", this.taskUtilService.getValFromContext("zhiLingXiaFa", "\u6307\u4ee4\u4e0b\u53d1"), true, true);
        list.add(dto26);
        MenuTreeNodeDto dto27 = new MenuTreeNodeDto("24", "2", this.taskUtilService.getValFromContext("biaoQianGuanLi", "\u6807\u7b7e\u7ba1\u7406"), true, true);
        list.add(dto27);
        MenuTreeNodeDto dto28 = new MenuTreeNodeDto("25", "2", this.taskUtilService.getValFromContext("chengYuanZhangHao", "\u6210\u5458\u8d26\u53f7"), true, true);
        list.add(dto28);
        MenuTreeNodeDto dtoWarn = new MenuTreeNodeDto("d", "0", this.taskUtilService.getValFromContext("gaoJingSheZhi", "\u544a\u8b66\u8bbe\u7f6e"), true, true);
        list.add(dtoWarn);
        MenuTreeNodeDto dto25 = new MenuTreeNodeDto("d1", "d", this.taskUtilService.getValFromContext("youJianGaoJing", "\u90ae\u4ef6\u544a\u8b66"), true, true);
        list.add(dto25);
        MenuTreeNodeDto dtoWarnScript = new MenuTreeNodeDto("d2", "d", this.taskUtilService.getValFromContext("jiaoBenGaoJing", "\u811a\u672c\u544a\u8b66"), true, true);
        list.add(dtoWarnScript);
        MenuTreeNodeDto dto30 = new MenuTreeNodeDto("d3", "d", this.taskUtilService.getValFromContext("ziDingYiGaoJing", "\u81ea\u5b9a\u4e49\u544a\u8b66"), true, true);
        list.add(dto30);
        MenuTreeNodeDto dto31 = new MenuTreeNodeDto("81", "0", this.taskUtilService.getValFromContext("ziChanGuanLi", "\u8d44\u4ea7\u7ba1\u7406"), true, true);
        list.add(dto31);
        MenuTreeNodeDto dto29 = new MenuTreeNodeDto("31", "0", this.taskUtilService.getValFromContext("sheBeiZhangHao", "\u8bbe\u5907\u8d26\u53f7\u7ba1\u7406"), true, true);
        list.add(dto29);
        MenuTreeNodeDto dtoc1 = new MenuTreeNodeDto("c1", "0", this.taskUtilService.getValFromContext("gongZuoBiJi", "\u5de5\u4f5c\u7b14\u8bb0"), true, true);
        list.add(dtoc1);
        MenuTreeNodeDto dtof1 = new MenuTreeNodeDto("f1", "0", this.taskUtilService.getValFromContext("zhongDuanYunXing", "\u7ec8\u7aef\u8fd0\u884c\u7edf\u8ba1"), true, true);
        list.add(dtof1);
        MenuTreeNodeDto dtoh1 = new MenuTreeNodeDto("h1", "0", this.taskUtilService.getValFromContext("ziDongFaXian", "\u81ea\u52a8\u53d1\u73b0"), true, true);
        list.add(dtoh1);
        MenuTreeNodeDto dtoufm = new MenuTreeNodeDto("u1", "0", this.taskUtilService.getValFromContext("ufmJianCe", "UFM\u76d1\u63a7"), true, true);
        list.add(dtoufm);
        MenuTreeNodeDto dtog1 = new MenuTreeNodeDto("g1", "0", this.taskUtilService.getValFromContext("aillm", "AI-LLM"), true, true);
        list.add(dtog1);
        return list;
    }

    public void saveLog(HttpServletRequest request, String action, AccountInfo accountInfo) {
        if (null == accountInfo) {
            return;
        }
        this.logInfoService.save(HostUtil.getAccountByRequest(request).getAccount() + action + "\u6210\u5458\u8d26\u53f7\uff1a" + accountInfo.getAccount(), "\u6210\u5458\u8d26\u53f7\uff1a" + accountInfo.getRemark(), "2");
    }
}

