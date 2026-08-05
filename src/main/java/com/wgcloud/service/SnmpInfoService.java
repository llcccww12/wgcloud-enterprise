/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.service;

import cn.hutool.json.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wgcloud.entity.HostGroup;
import com.wgcloud.entity.SnmpInfo;
import com.wgcloud.entity.SnmpState;
import com.wgcloud.mapper.SnmpInfoMapper;
import com.wgcloud.service.HostGroupService;
import com.wgcloud.service.LogInfoService;
import com.wgcloud.service.SnmpStateService;
import com.wgcloud.util.HostUtil;
import com.wgcloud.util.MessageErrorUtils;
import com.wgcloud.util.ServerBackupUtil;
import com.wgcloud.util.SnmpUtil;
import com.wgcloud.util.UUIDUtil;
import com.wgcloud.util.msg.WarnOtherUtil;
import com.wgcloud.util.msg.WarnPools;
import com.wgcloud.util.staticvar.BatchData;
import com.wgcloud.util.staticvar.StaticKeys;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

@Service
public class SnmpInfoService {
    private static final Logger logger = LoggerFactory.getLogger(SnmpInfoService.class);
    @Autowired
    private SnmpInfoMapper snmpInfoMapper;
    @Autowired
    private LogInfoService logInfoService;
    @Resource
    private HostGroupService hostGroupService;
    @Resource
    private SnmpStateService snmpStateService;
    @Autowired
    private MessageErrorUtils messageErrorUtils;

    public PageInfo selectByParams(Map<String, Object> params, int currPage, int pageSize) throws Exception {
        PageHelper.startPage((int)currPage, (int)pageSize);
        List<SnmpInfo> list = this.snmpInfoMapper.selectByParams(params);
        PageInfo pageInfo = new PageInfo(list);
        return pageInfo;
    }

    public void save(SnmpInfo snmpInfo) throws Exception {
        snmpInfo.setId(UUIDUtil.getUUID());
        snmpInfo.setCreateTime(new Date());
        snmpInfo.setState("1");
        snmpInfo.setSnmpUnit("byte");
        snmpInfo.setBytesSent("0");
        snmpInfo.setBytesRecv("0");
        snmpInfo.setSentAvg("");
        snmpInfo.setRecvAvg("");
        snmpInfo.setCpuPer("0");
        snmpInfo.setMemPer("0");
        snmpInfo.setTemperatureValue("0");
        snmpInfo.setVoltageValue("0");
        if (!StringUtils.isEmpty((CharSequence)snmpInfo.getHostname())) {
            snmpInfo.setHostname(snmpInfo.getHostname().trim());
        }
        if (!StringUtils.isEmpty((CharSequence)snmpInfo.getRecvOID())) {
            snmpInfo.setRecvOID(snmpInfo.getRecvOID().trim());
        }
        if (!StringUtils.isEmpty((CharSequence)snmpInfo.getSentOID())) {
            snmpInfo.setSentOID(snmpInfo.getSentOID().trim());
        }
        if (!StringUtils.isEmpty((CharSequence)snmpInfo.getCpuPerOID())) {
            snmpInfo.setCpuPerOID(snmpInfo.getCpuPerOID().trim());
        }
        if (!StringUtils.isEmpty((CharSequence)snmpInfo.getMemSizeOID())) {
            snmpInfo.setMemSizeOID(snmpInfo.getMemSizeOID().trim());
        }
        if (!StringUtils.isEmpty((CharSequence)snmpInfo.getTemperatureOid())) {
            snmpInfo.setTemperatureOid(snmpInfo.getTemperatureOid().trim());
        }
        if (!StringUtils.isEmpty((CharSequence)snmpInfo.getVoltageOid())) {
            snmpInfo.setVoltageOid(snmpInfo.getVoltageOid().trim());
        }
        this.snmpInfoMapper.save(snmpInfo);
    }

    public int countByParams(Map<String, Object> params) throws Exception {
        return this.snmpInfoMapper.countByParams(params);
    }

    public int updateActive(Map<String, Object> params) throws Exception {
        return this.snmpInfoMapper.updateActive(params);
    }

    @Transactional
    public int deleteById(String[] id) throws Exception {
        return this.snmpInfoMapper.deleteById(id);
    }

    public void updateById(SnmpInfo snmpInfo) throws Exception {
        if (!StringUtils.isEmpty((CharSequence)snmpInfo.getHostname())) {
            snmpInfo.setHostname(snmpInfo.getHostname().trim());
        }
        this.snmpInfoMapper.updateById(snmpInfo);
    }

    public SnmpInfo selectById(String id) throws Exception {
        return this.snmpInfoMapper.selectById(id);
    }

    @Transactional
    public void updateRecord(List<SnmpInfo> recordList) throws Exception {
        this.snmpInfoMapper.updateList(recordList);
    }

    public List<SnmpInfo> selectAllByParams(Map<String, Object> params) throws Exception {
        return this.snmpInfoMapper.selectAllByParams(params);
    }

    public void countAllIfOperStatus(SnmpInfo snmpInfo) {
        try {
            if (StringUtils.isEmpty((CharSequence)snmpInfo.getIfOperStatusOid()) || StringUtils.isEmpty((CharSequence)snmpInfo.getIfOperStatusValue())) {
                snmpInfo.setIfOperStatusValue("NaN");
            } else {
                String[] datas;
                int up = 0;
                int down = 0;
                int unknown = 0;
                for (String value : datas = snmpInfo.getIfOperStatusValue().split(",")) {
                    if (StringUtils.isEmpty((CharSequence)value)) continue;
                    String[] status = value.split(":");
                    if ("1".equals(status[1]) || "up".equals(status[1])) {
                        ++up;
                        continue;
                    }
                    if ("2".equals(status[1]) || "down".equals(status[1])) {
                        ++down;
                        continue;
                    }
                    ++unknown;
                }
                snmpInfo.setIfOperStatusValue("<span style='font-weight:700;color:#259425!important;'>" + up + "</span>/<span style='font-weight:700;color:#dc3545!important;'>" + down + "</span>/<span style='font-weight:700;color:#6c757d!important;'>" + unknown + "</span>");
            }
        }
        catch (Exception e) {
            logger.error("countAllIfOperStatus\u9519\u8bef", (Throwable)e);
        }
    }

    public void displayAllIfOperStatus(SnmpInfo snmpInfo, Model model) {
        List<JSONObject> list = new ArrayList();
        try {
            int dataSize = 0;
            if (!StringUtils.isEmpty((CharSequence)snmpInfo.getIfOperStatusOid()) && !StringUtils.isEmpty((CharSequence)snmpInfo.getIfOperStatusValue())) {
                String[] datas;
                for (String value : datas = snmpInfo.getIfOperStatusValue().split(",")) {
                    if (StringUtils.isEmpty((CharSequence)value)) continue;
                    JSONObject jsonObject = new JSONObject();
                    String[] status = value.split(":");
                    jsonObject.set("indexOper", (Object)status[0]);
                    String valueTemp = status[1];
                    valueTemp = valueTemp.toLowerCase();
                    if ("up".equals(valueTemp)) {
                        valueTemp = "1";
                    }
                    if ("down".equals(valueTemp)) {
                        valueTemp = "2";
                    }
                    jsonObject.set("value", (Object)valueTemp);
                    list.add(jsonObject);
                }
            }
            if (!StaticKeys.LICENSE_STATE.equals("1") && list.size() > 10) {
                dataSize = list.size();
                list = list.subList(0, 10);
                model.addAttribute("allIfOperStatusListMsg", (Object)("Tip \uff1aTotal " + dataSize + " interfaces need to display. " + "Display all data, please upgrade to the professional version. Please contact us www.wgstart.com"));
            }
        }
        catch (Exception e) {
            logger.error("displayAllIfOperStatus\u9519\u8bef", (Throwable)e);
        }
        model.addAttribute("allIfOperStatusList", list);
    }

    public void displayAllIfOperSpeed(SnmpInfo snmpInfo, Model model) {
        List<JSONObject> list = new ArrayList();
        try {
            int dataSize = 0;
            if (!StringUtils.isEmpty((CharSequence)snmpInfo.getRecvAvg()) && !StringUtils.isEmpty((CharSequence)snmpInfo.getSentAvg())) {
                String[] dataRecvAvg = snmpInfo.getRecvAvg().split(",");
                String[] dataSentAvg = snmpInfo.getSentAvg().split(",");
                ArrayList<String> indexOperList = new ArrayList<String>();
                HashMap<String, String> recvAvgMap = new HashMap<String, String>();
                for (String value : dataRecvAvg) {
                    String[] status = value.split(":");
                    recvAvgMap.put(status[0], status[1]);
                    indexOperList.add(status[0]);
                }
                HashMap<String, String> sentAvgMap = new HashMap<String, String>();
                for (String value : dataSentAvg) {
                    String[] status = value.split(":");
                    sentAvgMap.put(status[0], status[1]);
                }
                for (String value : indexOperList) {
                    if (StringUtils.isEmpty((CharSequence)value)) continue;
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.set("indexOper", (Object)value);
                    Double recvAvg = Double.valueOf((String)recvAvgMap.get(value));
                    String recvAvgStr = recvAvg + "KB/s";
                    if (recvAvg > 1024.0) {
                        recvAvgStr = this.snmpStateService.avgSpeedToMB(recvAvg, "kb") + "MB/s";
                    }
                    jsonObject.set("recvAvg", (Object)recvAvgStr);
                    Double sentAvg = Double.valueOf((String)sentAvgMap.get(value));
                    String sentAvgStr = sentAvg + "KB/s";
                    if (sentAvg > 1024.0) {
                        sentAvgStr = this.snmpStateService.avgSpeedToMB(sentAvg, "kb") + "MB/s";
                    }
                    jsonObject.set("sentAvg", (Object)sentAvgStr);
                    list.add(jsonObject);
                }
            }
            if (!StaticKeys.LICENSE_STATE.equals("1") && list.size() > 10) {
                dataSize = list.size();
                list = list.subList(0, 10);
                model.addAttribute("allIfOperSpeedListMsg", (Object)("Tip \uff1aTotal " + dataSize + " interfaces need to display. " + "Display all data, please upgrade to the professional version. Please contact us www.wgstart.com"));
            }
        }
        catch (Exception e) {
            logger.error("displayAllIfOperSpeed\u9519\u8bef", (Throwable)e);
        }
        model.addAttribute("allIfOperSpeedList", list);
    }

    public void addServerBackMark(List<SnmpInfo> list) throws Exception {
        for (SnmpInfo snmpInfo : list) {
            if (ServerBackupUtil.isExistSnmpInfoId(snmpInfo.getId())) {
                snmpInfo.setServerBackupMark("1");
                continue;
            }
            snmpInfo.setServerBackupMark("2");
        }
    }

    public void taskThreadHandler(Map<String, String> snmpMap, SnmpInfo h, Date date) {
        SnmpInfo snmpInfoForUpdate = new SnmpInfo();
        snmpInfoForUpdate.setId(h.getId());
        SnmpInfo snmpInfo = null;
        if ("1".equals(snmpMap.get(h.getHostname()))) {
            snmpInfo = SnmpUtil.getAvgSnmpInfo(h);
        }
        snmpInfoForUpdate.setCreateTime(date);
        snmpInfoForUpdate.setState("1");
        if (null != snmpInfo) {
            snmpInfoForUpdate.setRecvAvg(snmpInfo.getRecvAvg());
            snmpInfoForUpdate.setSentAvg(snmpInfo.getSentAvg());
            snmpInfoForUpdate.setBytesSent(snmpInfo.getBytesSent());
            snmpInfoForUpdate.setBytesRecv(snmpInfo.getBytesRecv());
            snmpInfoForUpdate.setMemPer(snmpInfo.getMemPer());
            snmpInfoForUpdate.setCpuPer(snmpInfo.getCpuPer());
            snmpInfoForUpdate.setDiskPer(snmpInfo.getDiskPer());
            snmpInfoForUpdate.setTemperatureValue(snmpInfo.getTemperatureValue());
            snmpInfoForUpdate.setVoltageValue(snmpInfo.getVoltageValue());
            snmpInfoForUpdate.setIfOperStatusValue(snmpInfo.getIfOperStatusValue());
            snmpInfoForUpdate.setSysDescVal(snmpInfo.getSysDescVal());
            this.messageErrorUtils.setErrorMsgHandler(snmpInfoForUpdate.getId(), snmpInfo.getTestErrorMsg());
        }
        if (null == snmpInfo) {
            snmpInfoForUpdate.setCreateTime(null);
            snmpInfoForUpdate.setState("2");
            this.messageErrorUtils.setErrorMsgHandler(snmpInfoForUpdate.getId(), "Cannot ping device");
        }
        try {
            this.updateById(snmpInfoForUpdate);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        if ("1".equals(snmpInfoForUpdate.getState())) {
            SnmpState snmpState = new SnmpState();
            if (!StringUtils.isEmpty((CharSequence)snmpInfo.getRecvAvgSum())) {
                snmpState.setRecvAvg(snmpInfo.getRecvAvgSum());
            } else {
                snmpState.setRecvAvg("0");
            }
            if (!StringUtils.isEmpty((CharSequence)snmpInfo.getSentAvgSum())) {
                snmpState.setSentAvg(snmpInfo.getSentAvgSum());
            } else {
                snmpState.setSentAvg("0");
            }
            snmpState.setCpuPer(snmpInfoForUpdate.getCpuPer());
            snmpState.setMemPer(snmpInfoForUpdate.getMemPer());
            snmpState.setSnmpInfoId(snmpInfoForUpdate.getId());
            snmpState.setTemperatureValue(snmpInfoForUpdate.getTemperatureValue());
            snmpState.setCreateTime(date);
            BatchData.SNMP_STATE_LIST.add(snmpState);
        }
        if ("2".equals(snmpInfoForUpdate.getState())) {
            WarnOtherUtil.sendSnmpInfo(h, true);
        } else if (!StringUtils.isEmpty((CharSequence)WarnPools.getWarnMark(h.getId()))) {
            WarnOtherUtil.sendSnmpInfo(h, false);
        }
    }

    public void saveLog(HttpServletRequest request, String action, SnmpInfo snmpInfo) {
        if (null == snmpInfo) {
            return;
        }
        this.logInfoService.save(HostUtil.getAccountByRequest(request).getAccount() + action + "snmp\u8bbe\u5907\u76d1\u6d4b\u4fe1\u606f\uff1a" + snmpInfo.getHostname(), "snmp\u8bbe\u5907\uff1a" + snmpInfo.getRemark(), "2");
    }

    public void updateToTargetAccount(Map<String, Object> params) throws Exception {
        this.snmpInfoMapper.updateToTargetAccount(params);
    }

    public List<HostGroup> setGroupInList(List<SnmpInfo> recordList, Model model, HttpServletRequest request) throws Exception {
        HashMap<String, Object> params = new HashMap<String, Object>();
        List<HostGroup> hostGroupList = this.hostGroupService.selectAllByParams(params, request);
        model.addAttribute("hostGroupList", hostGroupList);
        HashMap<String, String> hostGroupMap = new HashMap<String, String>();
        for (HostGroup hostGroup : hostGroupList) {
            hostGroupMap.put(hostGroup.getId(), hostGroup.getGroupName());
        }
        for (SnmpInfo appInfo : recordList) {
            if (StringUtils.isEmpty((CharSequence)appInfo.getGroupId())) continue;
            String groupNames = "";
            for (String groupId : appInfo.getGroupId().split(",")) {
                if (!hostGroupMap.containsKey(groupId)) continue;
                groupNames = groupNames + (String)hostGroupMap.get(groupId) + ",";
            }
            if (groupNames.endsWith(",")) {
                groupNames = groupNames.substring(0, groupNames.length() - 1);
            }
            appInfo.setGroupId(groupNames);
        }
        return hostGroupList;
    }

    public void saveGroupId(String ids, String[] groupIdsArr, HttpServletRequest request) throws Exception {
        List<HostGroup> hostGroupList = new ArrayList();
        if (null != groupIdsArr && groupIdsArr.length > 0) {
            HashMap<String, Object> params = new HashMap<String, Object>();
            params.put("groupIds", groupIdsArr);
            hostGroupList = this.hostGroupService.selectAllByParams(params, request);
        }
        if (!StringUtils.isEmpty((CharSequence)ids)) {
            SnmpInfo ho = null;
            for (String id : ids.split(",")) {
                ho = this.selectById(id);
                if (hostGroupList.size() > 0) {
                    ho.setGroupId(StringUtils.join((Object[])groupIdsArr, (String)","));
                } else {
                    ho.setGroupId("");
                }
                this.updateById(ho);
                this.logInfoService.save(HostUtil.getAccountByRequest(request).getAccount() + "\u8bbe\u7f6eSNMP\u8bbe\u5907\u6807\u7b7e\uff1a" + ho.getHostname(), "\u6807\u7b7e\uff1a" + this.hostGroupService.returnGroupNames(hostGroupList), "2");
            }
        }
    }
}

