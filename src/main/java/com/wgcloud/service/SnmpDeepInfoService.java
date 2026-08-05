/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wgcloud.config.CommonConfig;
import com.wgcloud.entity.HostGroup;
import com.wgcloud.entity.SnmpDeepInfo;
import com.wgcloud.entity.SnmpDeepState;
import com.wgcloud.mapper.SnmpDeepInfoMapper;
import com.wgcloud.mapper.SnmpDeepStateMapper;
import com.wgcloud.service.HostGroupService;
import com.wgcloud.service.LogInfoService;
import com.wgcloud.util.HostUtil;
import com.wgcloud.util.MessageErrorUtils;
import com.wgcloud.util.ServerBackupUtil;
import com.wgcloud.util.SnmpDeepUtil;
import com.wgcloud.util.UUIDUtil;
import com.wgcloud.util.msg.WarnOtherUtil;
import com.wgcloud.util.msg.WarnPools;
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
public class SnmpDeepInfoService {
    private static final Logger logger = LoggerFactory.getLogger(SnmpDeepInfoService.class);
    @Autowired
    private SnmpDeepInfoMapper snmpDeepInfoMapper;
    @Autowired
    private SnmpDeepStateMapper snmpDeepStateMapper;
    @Resource
    private HostGroupService hostGroupService;
    @Autowired
    private MessageErrorUtils messageErrorUtils;
    @Autowired
    private LogInfoService logInfoService;
    @Autowired
    private CommonConfig commonConfig;

    public PageInfo selectByParams(Map<String, Object> params, int currPage, int pageSize) throws Exception {
        PageHelper.startPage((int)currPage, (int)pageSize);
        List<SnmpDeepInfo> list = this.snmpDeepInfoMapper.selectByParams(params);
        PageInfo pageInfo = new PageInfo(list);
        return pageInfo;
    }

    public void save(SnmpDeepInfo snmpDeepInfo, HttpServletRequest request) throws Exception {
        Date nowDate = new Date();
        snmpDeepInfo.setId(UUIDUtil.getUUID());
        snmpDeepInfo.setCreateTime(nowDate);
        this.snmpDeepInfoMapper.save(snmpDeepInfo);
        this.addExtDataForm(snmpDeepInfo, request, nowDate);
    }

    private void addExtDataForm(SnmpDeepInfo snmpDeepInfo, HttpServletRequest request, Date nowDate) throws Exception {
        String dataFromIndex = request.getParameter("dataFromIndex");
        int rowsLen = 0;
        if (!StringUtils.isEmpty((CharSequence)dataFromIndex)) {
            for (int i = 0; i <= Integer.valueOf(dataFromIndex); ++i) {
                String oidName = request.getParameter("oidName_" + i);
                String oidValue = request.getParameter("oidValue_" + i);
                String oidType = request.getParameter("oidType_" + i);
                if (StringUtils.isEmpty((CharSequence)oidName) || StringUtils.isEmpty((CharSequence)oidValue) || StringUtils.isEmpty((CharSequence)oidType)) continue;
                SnmpDeepState snmpDeepState = new SnmpDeepState();
                snmpDeepState.setId(UUIDUtil.getUUID());
                snmpDeepState.setCreateTime(nowDate);
                snmpDeepState.setSnmpDeepInfoId(snmpDeepInfo.getId());
                snmpDeepState.setOidName(oidName.trim());
                snmpDeepState.setOidOrderNum(i);
                snmpDeepState.setOidValue(oidValue);
                snmpDeepState.setOidType(oidType);
                this.snmpDeepStateMapper.save(snmpDeepState);
                ++rowsLen;
            }
        }
    }

    public int countByParams(Map<String, Object> params) throws Exception {
        return this.snmpDeepInfoMapper.countByParams(params);
    }

    public List<SnmpDeepState> selectSnmpDeepStateList(String snmpDeepInfoId) throws Exception {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("snmpDeepInfoId", snmpDeepInfoId);
        List<SnmpDeepState> list = this.snmpDeepStateMapper.selectAllByParams(params);
        return list;
    }

    public List<SnmpDeepState> selectSnmpDeepStateList(Map<String, Object> params) throws Exception {
        List<SnmpDeepState> list = this.snmpDeepStateMapper.selectAllByParams(params);
        return list;
    }

    public Integer countBySnmpDeepInfoId(String snmpDeepInfoId) {
        Integer list = 0;
        try {
            HashMap<String, Object> params = new HashMap<String, Object>();
            params.put("snmpDeepInfoId", snmpDeepInfoId);
            list = this.snmpDeepStateMapper.countByParams(params);
        }
        catch (Exception e) {
            logger.error("\u67e5\u8be2\u4ece\u8868\u6570\u636e\u91cf\u9519\u8bef", (Throwable)e);
        }
        return list;
    }

    public int updateActive(Map<String, Object> params) throws Exception {
        return this.snmpDeepInfoMapper.updateActive(params);
    }

    @Transactional
    public int deleteById(String[] id) throws Exception {
        return this.snmpDeepInfoMapper.deleteById(id);
    }

    public void updateById(SnmpDeepInfo snmpDeepInfo, HttpServletRequest request) throws Exception {
        if (!StringUtils.isEmpty((CharSequence)snmpDeepInfo.getHostname())) {
            snmpDeepInfo.setHostname(snmpDeepInfo.getHostname().trim());
        }
        this.snmpDeepInfoMapper.updateById(snmpDeepInfo);
        this.snmpDeepStateMapper.deleteBySnmpDeepInfoId(snmpDeepInfo.getId());
        this.addExtDataForm(snmpDeepInfo, request, new Date());
    }

    public void updateById(SnmpDeepInfo snmpDeepInfo) throws Exception {
        if (!StringUtils.isEmpty((CharSequence)snmpDeepInfo.getHostname())) {
            snmpDeepInfo.setHostname(snmpDeepInfo.getHostname().trim());
        }
        this.snmpDeepInfoMapper.updateById(snmpDeepInfo);
    }

    public SnmpDeepInfo selectById(String id) throws Exception {
        return this.snmpDeepInfoMapper.selectById(id);
    }

    @Transactional
    public void updateRecord(List<SnmpDeepInfo> recordList) throws Exception {
        this.snmpDeepInfoMapper.updateList(recordList);
    }

    public List<SnmpDeepInfo> selectAllByParams(Map<String, Object> params) throws Exception {
        return this.snmpDeepInfoMapper.selectAllByParams(params);
    }

    public void addServerBackMark(List<SnmpDeepInfo> list) throws Exception {
        for (SnmpDeepInfo snmpDeepInfo : list) {
            if (ServerBackupUtil.isExistSnmpInfoId(snmpDeepInfo.getId())) {
                snmpDeepInfo.setServerBackupMark("1");
                continue;
            }
            snmpDeepInfo.setServerBackupMark("2");
        }
    }

    public void taskThreadHandler(Map<String, String> snmpMap, SnmpDeepInfo snmpDeepInfo, Date date) {
        try {
            SnmpDeepInfo snmpInfoForUpdate = new SnmpDeepInfo();
            snmpInfoForUpdate.setId(snmpDeepInfo.getId());
            if ("1".equals(snmpMap.get(snmpDeepInfo.getHostname()))) {
                List<SnmpDeepState> snmpDeepStateList = this.selectSnmpDeepStateList(snmpDeepInfo.getId());
                ArrayList<SnmpDeepState> getList = new ArrayList<SnmpDeepState>();
                ArrayList<SnmpDeepState> walkList = new ArrayList<SnmpDeepState>();
                for (SnmpDeepState snmpDeepState : snmpDeepStateList) {
                    if ("SNMPGET".equals(snmpDeepState.getOidType())) {
                        getList.add(snmpDeepState);
                        continue;
                    }
                    walkList.add(snmpDeepState);
                }
                String errorMsgGet = SnmpDeepUtil.snmpGet(snmpDeepInfo, getList);
                String errorMsgWalk = SnmpDeepUtil.walkSnmp(snmpDeepInfo, walkList);
                if (!StringUtils.isEmpty((CharSequence)errorMsgGet) || !StringUtils.isEmpty((CharSequence)errorMsgWalk)) {
                    this.messageErrorUtils.setErrorMsgHandler(snmpInfoForUpdate.getId(), errorMsgGet + errorMsgWalk);
                }
                snmpInfoForUpdate.setCreateTime(date);
                snmpInfoForUpdate.setState("1");
            } else {
                snmpInfoForUpdate.setCreateTime(null);
                snmpInfoForUpdate.setState("2");
                this.messageErrorUtils.setErrorMsgHandler(snmpInfoForUpdate.getId(), "Cannot ping device");
            }
            this.updateById(snmpInfoForUpdate);
            if ("2".equals(snmpInfoForUpdate.getState())) {
                WarnOtherUtil.sendSnmpDeepInfo(snmpDeepInfo, true);
            } else if (!StringUtils.isEmpty((CharSequence)WarnPools.getWarnMark(snmpDeepInfo.getId()))) {
                WarnOtherUtil.sendSnmpDeepInfo(snmpDeepInfo, false);
            }
        }
        catch (Exception e) {
            logger.error("snmp\u8bbe\u5907\u6df1\u5ea6\u68c0\u6d4b\u4efb\u52a1\u9519\u8bef", (Throwable)e);
        }
    }

    public void saveLog(HttpServletRequest request, String action, SnmpDeepInfo snmpDeepInfo) {
        if (null == snmpDeepInfo) {
            return;
        }
        this.logInfoService.save(HostUtil.getAccountByRequest(request).getAccount() + action + "snmp\u8bbe\u5907\u6df1\u5ea6\u76d1\u63a7\u4fe1\u606f\uff1a" + snmpDeepInfo.getHostname(), "snmp\u8bbe\u5907\u5907\u6ce8\uff1a" + snmpDeepInfo.getRemark(), "2");
    }

    public void updateToTargetAccount(Map<String, Object> params) throws Exception {
        this.snmpDeepInfoMapper.updateToTargetAccount(params);
    }

    public List<HostGroup> setGroupInList(List<SnmpDeepInfo> recordList, Model model, HttpServletRequest request) throws Exception {
        HashMap<String, Object> params = new HashMap<String, Object>();
        List<HostGroup> hostGroupList = this.hostGroupService.selectAllByParams(params, request);
        model.addAttribute("hostGroupList", hostGroupList);
        HashMap<String, String> hostGroupMap = new HashMap<String, String>();
        for (HostGroup hostGroup : hostGroupList) {
            hostGroupMap.put(hostGroup.getId(), hostGroup.getGroupName());
        }
        for (SnmpDeepInfo appInfo : recordList) {
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
            SnmpDeepInfo ho = null;
            for (String id : ids.split(",")) {
                ho = this.selectById(id);
                if (hostGroupList.size() > 0) {
                    ho.setGroupId(StringUtils.join((Object[])groupIdsArr, (String)","));
                } else {
                    ho.setGroupId("");
                }
                this.updateById(ho);
                this.logInfoService.save(HostUtil.getAccountByRequest(request).getAccount() + "\u8bbe\u7f6eSNMP\u6df1\u5ea6\u76d1\u63a7\u8bbe\u5907\u6807\u7b7e\uff1a" + ho.getHostname(), "\u6807\u7b7e\uff1a" + this.hostGroupService.returnGroupNames(hostGroupList), "2");
            }
        }
    }

    public void copyData(String id, String hostname, HttpServletRequest request) throws Exception {
        Date nowDate = new Date();
        SnmpDeepInfo snmpDeepInfo = this.selectById(id);
        snmpDeepInfo.setId(UUIDUtil.getUUID());
        snmpDeepInfo.setHostname(hostname);
        snmpDeepInfo.setCreateTime(nowDate);
        this.snmpDeepInfoMapper.save(snmpDeepInfo);
        List<SnmpDeepState> snmpDeepStateList = this.selectSnmpDeepStateList(id);
        for (SnmpDeepState state : snmpDeepStateList) {
            state.setId(UUIDUtil.getUUID());
            state.setSnmpDeepInfoId(snmpDeepInfo.getId());
            this.snmpDeepStateMapper.save(state);
        }
        this.saveLog(request, "\u6dfb\u52a0", snmpDeepInfo);
    }
}

