/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wgcloud.entity.CustomInfo;
import com.wgcloud.entity.HostGroup;
import com.wgcloud.mapper.CustomInfoMapper;
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
public class CustomInfoService {
    private static final Logger logger = LoggerFactory.getLogger(CustomInfoService.class);
    @Autowired
    private CustomInfoMapper customInfoMapper;
    @Resource
    private HostGroupService hostGroupService;
    @Autowired
    private LogInfoService logInfoService;

    public PageInfo selectByParams(Map<String, Object> params, int currPage, int pageSize) throws Exception {
        PageHelper.startPage((int)currPage, (int)pageSize);
        List<CustomInfo> list = this.customInfoMapper.selectByParams(params);
        PageInfo pageInfo = new PageInfo(list);
        return pageInfo;
    }

    public void save(CustomInfo customInfo, HttpServletRequest request) throws Exception {
        customInfo.setId(UUIDUtil.getUUID());
        Date nowDate = new Date();
        customInfo.setCreateTime(nowDate);
        customInfo.setState("1");
        this.customInfoMapper.save(customInfo);
    }

    public int deleteByHostName(Map<String, Object> map) throws Exception {
        return this.customInfoMapper.deleteByHostName(map);
    }

    @Transactional
    public void saveRecord(List<CustomInfo> recordList) throws Exception {
        if (recordList.size() < 1) {
            return;
        }
        for (CustomInfo as : recordList) {
            as.setId(UUIDUtil.getUUID());
        }
        this.customInfoMapper.insertList(recordList);
    }

    public int countByParams(Map<String, Object> params) throws Exception {
        return this.customInfoMapper.countByParams(params);
    }

    @Transactional
    public int deleteById(String[] id) throws Exception {
        return this.customInfoMapper.deleteById(id);
    }

    @Transactional
    public void updateRecord(List<CustomInfo> recordList) throws Exception {
        if (recordList.size() < 1) {
            return;
        }
        for (CustomInfo customInfo : recordList) {
            if (StringUtils.isEmpty((CharSequence)customInfo.getCustomValue()) || customInfo.getCustomValue().length() <= 50) continue;
            customInfo.setCustomValue(customInfo.getCustomValue().substring(0, 50));
        }
        this.customInfoMapper.updateList(recordList);
    }

    public int updateActive(Map<String, Object> params) throws Exception {
        return this.customInfoMapper.updateActive(params);
    }

    public void downByHostName(List<String> recordList) throws Exception {
        if (recordList.size() < 1) {
            return;
        }
        this.customInfoMapper.downByHostName(recordList);
    }

    public void updateById(CustomInfo CustomInfo2) throws Exception {
        this.customInfoMapper.updateById(CustomInfo2);
    }

    public CustomInfo selectById(String id) throws Exception {
        return this.customInfoMapper.selectById(id);
    }

    public List<CustomInfo> selectAllByParams(Map<String, Object> params) throws Exception {
        return this.customInfoMapper.selectAllByParams(params);
    }

    public void saveGroupId(String ids, String[] groupIdsArr, HttpServletRequest request) throws Exception {
        List<HostGroup> hostGroupList = new ArrayList();
        if (null != groupIdsArr && groupIdsArr.length > 0) {
            HashMap<String, Object> params = new HashMap<String, Object>();
            params.put("groupIds", groupIdsArr);
            hostGroupList = this.hostGroupService.selectAllByParams(params, request);
        }
        if (!StringUtils.isEmpty((CharSequence)ids)) {
            CustomInfo ho = null;
            for (String id : ids.split(",")) {
                ho = this.selectById(id);
                if (hostGroupList.size() > 0) {
                    ho.setGroupId(StringUtils.join((Object[])groupIdsArr, (String)","));
                } else {
                    ho.setGroupId("");
                }
                this.updateById(ho);
                this.logInfoService.save(HostUtil.getAccountByRequest(request).getAccount() + "\u8bbe\u7f6e\u81ea\u5b9a\u4e49\u76d1\u63a7\u9879\u6807\u7b7e\uff1a" + ho.getHostname(), "\u6807\u7b7e\uff1a" + this.hostGroupService.returnGroupNames(hostGroupList), "2");
            }
        }
    }

    public List<HostGroup> setGroupInList(List<CustomInfo> recordList, Model model, HttpServletRequest request) throws Exception {
        HashMap<String, Object> params = new HashMap<String, Object>();
        List<HostGroup> hostGroupList = this.hostGroupService.selectAllByParams(params, request);
        model.addAttribute("hostGroupList", hostGroupList);
        HashMap<String, String> hostGroupMap = new HashMap<String, String>();
        for (HostGroup hostGroup : hostGroupList) {
            hostGroupMap.put(hostGroup.getId(), hostGroup.getGroupName());
        }
        for (CustomInfo customInfo : recordList) {
            if (StringUtils.isEmpty((CharSequence)customInfo.getGroupId())) continue;
            String groupNames = "";
            for (String groupId : customInfo.getGroupId().split(",")) {
                if (!hostGroupMap.containsKey(groupId)) continue;
                groupNames = groupNames + (String)hostGroupMap.get(groupId) + ",";
            }
            if (groupNames.endsWith(",")) {
                groupNames = groupNames.substring(0, groupNames.length() - 1);
            }
            customInfo.setGroupId(groupNames);
        }
        return hostGroupList;
    }

    public void saveLog(HttpServletRequest request, String action, CustomInfo customInfo) {
        if (null == customInfo) {
            return;
        }
        this.logInfoService.save(HostUtil.getAccountByRequest(request).getAccount() + action + "\u81ea\u5b9a\u4e49\u76d1\u63a7\u9879\u4fe1\u606f\uff1a" + customInfo.getHostname() + "\uff0c" + customInfo.getCustomName(), "\u81ea\u5b9a\u4e49\u76d1\u63a7\u9879\u4fe1\u606f\uff1a" + customInfo.getCustomName(), "2");
    }
}

