/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.service;

import cn.hutool.core.bean.BeanUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wgcloud.entity.AppInfo;
import com.wgcloud.entity.HostGroup;
import com.wgcloud.mapper.AppInfoMapper;
import com.wgcloud.mapper.AppStateMapper;
import com.wgcloud.service.HostGroupService;
import com.wgcloud.service.LogInfoService;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

@Service
public class AppInfoService {
    private static final Logger logger = LoggerFactory.getLogger(AppInfoService.class);
    @Autowired
    private AppInfoMapper appInfoMapper;
    @Autowired
    private AppStateMapper appStateMapper;
    @Resource
    private HostGroupService hostGroupService;
    @Autowired
    private LogInfoService logInfoService;

    public PageInfo selectByParams(Map<String, Object> params, int currPage, int pageSize) throws Exception {
        PageHelper.startPage((int)currPage, (int)pageSize);
        List<AppInfo> list = this.appInfoMapper.selectByParams(params);
        PageInfo pageInfo = new PageInfo(list);
        return pageInfo;
    }

    public void save(AppInfo appInfo, HttpServletRequest request) throws Exception {
        appInfo.setId(UUIDUtil.getUUID());
        Date nowDate = new Date();
        appInfo.setCreateTime(nowDate);
        appInfo.setThreadsNum("0");
        appInfo.setNetConnections("0");
        appInfo.setCpuPer(0.0);
        appInfo.setMemPer(0.0);
        appInfo.setGatherPid("");
        appInfo.setReadBytes("0");
        appInfo.setWritesBytes("0");
        appInfo.setAppTimes("");
        if (!StringUtils.isEmpty((CharSequence)appInfo.getAppPid())) {
            appInfo.setAppPid(appInfo.getAppPid().trim());
        }
        this.appInfoMapper.save(appInfo);
        this.addExtDataForm(appInfo, request, nowDate);
    }

    private void addExtDataForm(AppInfo appInfo, HttpServletRequest request, Date nowDate) throws Exception {
        appInfo.setCustomShell("");
        String dataFromIndex = request.getParameter("dataFromIndex");
        int rowsLen = 0;
        if (!StringUtils.isEmpty((CharSequence)dataFromIndex)) {
            for (int i = 0; i <= Integer.valueOf(dataFromIndex); ++i) {
                String appPid = request.getParameter("appPid_" + i);
                String appName = request.getParameter("appName_" + i);
                if (StringUtils.isEmpty((CharSequence)appPid) || StringUtils.isEmpty((CharSequence)appName)) continue;
                AppInfo appInfoExt = new AppInfo();
                BeanUtil.copyProperties((Object)appInfo, (Object)appInfoExt, (boolean)true);
                appInfoExt.setId(UUIDUtil.getUUID());
                appInfoExt.setCreateTime(nowDate);
                appInfoExt.setAppPid(appPid.trim());
                appInfoExt.setAppName(appName.trim());
                this.appInfoMapper.save(appInfoExt);
                this.saveLog(request, "\u6dfb\u52a0", appInfoExt);
                ++rowsLen;
            }
        }
    }

    public int deleteByHostName(Map<String, Object> map) throws Exception {
        return this.appInfoMapper.deleteByHostName(map);
    }

    public int updateActive(Map<String, Object> params) throws Exception {
        return this.appInfoMapper.updateActive(params);
    }

    @Transactional
    public void saveRecord(List<AppInfo> recordList) throws Exception {
        if (recordList.size() < 1) {
            return;
        }
        for (AppInfo as : recordList) {
            as.setId(UUIDUtil.getUUID());
        }
        this.appInfoMapper.insertList(recordList);
    }

    public int countByParams(Map<String, Object> params) throws Exception {
        return this.appInfoMapper.countByParams(params);
    }

    @Transactional
    public int deleteById(String[] id) throws Exception {
        return this.appInfoMapper.deleteById(id);
    }

    @Transactional
    public void updateRecord(List<AppInfo> recordList) throws Exception {
        if (recordList.size() < 1) {
            return;
        }
        this.appInfoMapper.updateList(recordList);
    }

    public void downByHostName(List<String> recordList) throws Exception {
        if (recordList.size() < 1) {
            return;
        }
        this.appInfoMapper.downByHostName(recordList);
    }

    public void updateById(AppInfo appInfo) throws Exception {
        this.appInfoMapper.updateById(appInfo);
    }

    public AppInfo selectById(String id) throws Exception {
        return this.appInfoMapper.selectById(id);
    }

    public List<AppInfo> selectAllByParams(Map<String, Object> params) throws Exception {
        return this.appInfoMapper.selectAllByParams(params);
    }

    public void saveGroupId(String ids, String[] groupIdsArr, HttpServletRequest request) throws Exception {
        List<HostGroup> hostGroupList = new ArrayList();
        if (null != groupIdsArr && groupIdsArr.length > 0) {
            HashMap<String, Object> params = new HashMap<String, Object>();
            params.put("groupIds", groupIdsArr);
            hostGroupList = this.hostGroupService.selectAllByParams(params, request);
        }
        if (!StringUtils.isEmpty((CharSequence)ids)) {
            AppInfo ho = null;
            for (String id : ids.split(",")) {
                ho = this.selectById(id);
                if (hostGroupList.size() > 0) {
                    ho.setGroupId(StringUtils.join((Object[])groupIdsArr, (String)","));
                } else {
                    ho.setGroupId("");
                }
                this.updateById(ho);
                this.logInfoService.save(HostUtil.getAccountByRequest(request).getAccount() + "\u8bbe\u7f6e\u8fdb\u7a0b\u6807\u7b7e\uff1a" + ho.getHostname(), "\u6807\u7b7e\uff1a" + this.hostGroupService.returnGroupNames(hostGroupList), "2");
            }
        }
    }

    public List<HostGroup> setGroupInList(List<AppInfo> recordList, Model model, HttpServletRequest request) throws Exception {
        HashMap<String, Object> params = new HashMap<String, Object>();
        List<HostGroup> hostGroupList = this.hostGroupService.selectAllByParams(params, request);
        model.addAttribute("hostGroupList", hostGroupList);
        HashMap<String, String> hostGroupMap = new HashMap<String, String>();
        for (HostGroup hostGroup : hostGroupList) {
            hostGroupMap.put(hostGroup.getId(), hostGroup.getGroupName());
        }
        for (AppInfo appInfo : recordList) {
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

    public void saveLog(HttpServletRequest request, String action, AppInfo appInfo) {
        if (null == appInfo) {
            return;
        }
        this.logInfoService.save(HostUtil.getAccountByRequest(request).getAccount() + action + "\u8fdb\u7a0b\u76d1\u6d4b\u4fe1\u606f\uff1a" + appInfo.getHostname() + "\uff0c" + appInfo.getAppName(), "\u83b7\u53d6\u8fdb\u7a0b\u65b9\u6cd5\uff1a" + appInfo.getAppPid(), "2");
    }
}

