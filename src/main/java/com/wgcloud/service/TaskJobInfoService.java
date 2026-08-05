/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wgcloud.entity.HostGroup;
import com.wgcloud.entity.TaskJobInfo;
import com.wgcloud.mapper.TaskJobInfoMapper;
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
public class TaskJobInfoService {
    private static final Logger logger = LoggerFactory.getLogger(TaskJobInfoService.class);
    @Autowired
    private TaskJobInfoMapper taskJobInfoMapper;
    @Resource
    private HostGroupService hostGroupService;
    @Autowired
    private LogInfoService logInfoService;

    public PageInfo selectByParams(Map<String, Object> params, int currPage, int pageSize) throws Exception {
        PageHelper.startPage((int)currPage, (int)pageSize);
        List<TaskJobInfo> list = this.taskJobInfoMapper.selectByParams(params);
        PageInfo pageInfo = new PageInfo(list);
        return pageInfo;
    }

    public void save(TaskJobInfo taskJobInfo, HttpServletRequest request) throws Exception {
        taskJobInfo.setId(UUIDUtil.getUUID());
        Date nowDate = new Date();
        taskJobInfo.setCreateTime(nowDate);
        if (!StringUtils.isEmpty((CharSequence)taskJobInfo.getCronValue())) {
            taskJobInfo.setCronValue(taskJobInfo.getCronValue().trim());
        }
        taskJobInfo.setCallBackState("\u51c6\u5907\u4e2d");
        this.taskJobInfoMapper.save(taskJobInfo);
    }

    public int updateActive(Map<String, Object> params) throws Exception {
        return this.taskJobInfoMapper.updateActive(params);
    }

    public int countByParams(Map<String, Object> params) throws Exception {
        return this.taskJobInfoMapper.countByParams(params);
    }

    @Transactional
    public int deleteById(String[] id) throws Exception {
        return this.taskJobInfoMapper.deleteById(id);
    }

    public void updateById(TaskJobInfo TaskJobInfo2) throws Exception {
        this.taskJobInfoMapper.updateById(TaskJobInfo2);
    }

    public TaskJobInfo selectById(String id) throws Exception {
        return this.taskJobInfoMapper.selectById(id);
    }

    public List<TaskJobInfo> selectAllByParams(Map<String, Object> params) throws Exception {
        return this.taskJobInfoMapper.selectAllByParams(params);
    }

    @Transactional
    public int updateSendByIds(String[] id) throws Exception {
        return this.taskJobInfoMapper.updateSendByIds(id);
    }

    public void saveGroupId(String ids, String[] groupIdsArr, HttpServletRequest request) throws Exception {
        List<HostGroup> hostGroupList = new ArrayList();
        if (null != groupIdsArr && groupIdsArr.length > 0) {
            HashMap<String, Object> params = new HashMap<String, Object>();
            params.put("groupIds", groupIdsArr);
            hostGroupList = this.hostGroupService.selectAllByParams(params, request);
        }
        if (!StringUtils.isEmpty((CharSequence)ids)) {
            TaskJobInfo ho = null;
            for (String id : ids.split(",")) {
                ho = this.selectById(id);
                if (hostGroupList.size() > 0) {
                    ho.setGroupId(StringUtils.join((Object[])groupIdsArr, (String)","));
                } else {
                    ho.setGroupId("");
                }
                this.updateById(ho);
                this.logInfoService.save(HostUtil.getAccountByRequest(request).getAccount() + "\u8bbe\u7f6e\u8ba1\u5212\u4efb\u52a1\u6807\u7b7e\uff1a" + ho.getHostname(), "\u6807\u7b7e\uff1a" + this.hostGroupService.returnGroupNames(hostGroupList), "2");
            }
        }
    }

    public List<HostGroup> setGroupInList(List<TaskJobInfo> recordList, Model model, HttpServletRequest request) throws Exception {
        HashMap<String, Object> params = new HashMap<String, Object>();
        List<HostGroup> hostGroupList = this.hostGroupService.selectAllByParams(params, request);
        model.addAttribute("hostGroupList", hostGroupList);
        HashMap<String, String> hostGroupMap = new HashMap<String, String>();
        for (HostGroup hostGroup : hostGroupList) {
            hostGroupMap.put(hostGroup.getId(), hostGroup.getGroupName());
        }
        for (TaskJobInfo taskJobInfo : recordList) {
            if (StringUtils.isEmpty((CharSequence)taskJobInfo.getGroupId())) continue;
            String groupNames = "";
            for (String groupId : taskJobInfo.getGroupId().split(",")) {
                if (!hostGroupMap.containsKey(groupId)) continue;
                groupNames = groupNames + (String)hostGroupMap.get(groupId) + ",";
            }
            if (groupNames.endsWith(",")) {
                groupNames = groupNames.substring(0, groupNames.length() - 1);
            }
            taskJobInfo.setGroupId(groupNames);
        }
        return hostGroupList;
    }

    public void updateToTargetAccount(Map<String, Object> params) throws Exception {
        this.taskJobInfoMapper.updateToTargetAccount(params);
    }

    public void saveLog(HttpServletRequest request, String action, TaskJobInfo taskJobInfo) {
        if (null == taskJobInfo) {
            return;
        }
        this.logInfoService.save(HostUtil.getAccountByRequest(request).getAccount() + action + "\u8ba1\u5212\u4efb\u52a1\uff1a" + taskJobInfo.getJobName() + "\uff0c" + taskJobInfo.getHostname(), "\u4efb\u52a1\u6267\u884c\u811a\u672c\uff1a" + taskJobInfo.getShell() + "\uff0c" + taskJobInfo.getJobRemark(), "2");
    }
}

