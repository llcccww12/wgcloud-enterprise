/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.service;

import cn.hutool.core.bean.BeanUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wgcloud.config.CommonConfig;
import com.wgcloud.entity.FileSafe;
import com.wgcloud.entity.HostGroup;
import com.wgcloud.mapper.FileSafeMapper;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

@Service
public class FileSafeService {
    @Autowired
    private FileSafeMapper fileSafeMapper;
    @Autowired
    private LogInfoService logInfoService;
    @Resource
    private HostGroupService hostGroupService;
    @Autowired
    private CommonConfig commonConfig;

    public PageInfo selectByParams(Map<String, Object> params, int currPage, int pageSize) throws Exception {
        PageHelper.startPage((int)currPage, (int)pageSize);
        List<FileSafe> list = this.fileSafeMapper.selectByParams(params);
        PageInfo pageInfo = new PageInfo(list);
        return pageInfo;
    }

    public void save(FileSafe fileSafe, HttpServletRequest request) throws Exception {
        Date nowDate = new Date();
        fileSafe.setId(UUIDUtil.getUUID());
        fileSafe.setCreateTime(nowDate);
        if (!StringUtils.isEmpty((CharSequence)fileSafe.getFileSign())) {
            fileSafe.setFileSign(fileSafe.getFileSign().trim());
        }
        this.fileSafeMapper.save(fileSafe);
        this.addExtDataForm(fileSafe, request, nowDate);
    }

    public int updateActive(Map<String, Object> params) throws Exception {
        return this.fileSafeMapper.updateActive(params);
    }

    private void addExtDataForm(FileSafe fileSafe, HttpServletRequest request, Date nowDate) throws Exception {
        String dataFromIndex = request.getParameter("dataFromIndex");
        if (!StringUtils.isEmpty((CharSequence)dataFromIndex)) {
            for (int i = 0; i <= Integer.valueOf(dataFromIndex); ++i) {
                String fileName = request.getParameter("fileName_" + i);
                String filePath = request.getParameter("filePath_" + i);
                String fileSign = request.getParameter("fileSign_" + i);
                if (StringUtils.isEmpty((CharSequence)fileName) || StringUtils.isEmpty((CharSequence)filePath)) continue;
                FileSafe fileSafeExt = new FileSafe();
                BeanUtil.copyProperties((Object)fileSafe, (Object)fileSafeExt, (boolean)true);
                fileSafeExt.setId(UUIDUtil.getUUID());
                fileSafeExt.setCreateTime(nowDate);
                fileSafeExt.setFileName(fileName);
                fileSafeExt.setFilePath(filePath);
                fileSafeExt.setFileSign(fileSign);
                this.fileSafeMapper.save(fileSafeExt);
                this.saveLog(request, "\u6dfb\u52a0", fileSafeExt);
            }
        }
    }

    public int deleteByHostName(Map<String, Object> map) throws Exception {
        return this.fileSafeMapper.deleteByHostName(map);
    }

    @Transactional
    public void saveRecord(List<FileSafe> recordList) throws Exception {
        if (recordList.size() < 1) {
            return;
        }
        for (FileSafe as : recordList) {
            as.setId(UUIDUtil.getUUID());
        }
        this.fileSafeMapper.insertList(recordList);
    }

    public void downByHostName(List<String> recordList) throws Exception {
        if (recordList.size() < 1) {
            return;
        }
        this.fileSafeMapper.downByHostName(recordList);
    }

    public int countByParams(Map<String, Object> params) throws Exception {
        return this.fileSafeMapper.countByParams(params);
    }

    @Transactional
    public int deleteById(String[] id) throws Exception {
        return this.fileSafeMapper.deleteById(id);
    }

    @Transactional
    public void updateRecord(List<FileSafe> recordList) throws Exception {
        if (recordList.size() < 1) {
            return;
        }
        this.fileSafeMapper.updateList(recordList);
    }

    public void updateById(FileSafe fileSafe) throws Exception {
        this.fileSafeMapper.updateById(fileSafe);
    }

    public FileSafe selectById(String id) throws Exception {
        return this.fileSafeMapper.selectById(id);
    }

    public List<FileSafe> selectAllByParams(Map<String, Object> params) throws Exception {
        return this.fileSafeMapper.selectAllByParams(params);
    }

    public void saveLog(HttpServletRequest request, String action, FileSafe fileSafe) {
        if (null == fileSafe) {
            return;
        }
        this.logInfoService.save(HostUtil.getAccountByRequest(request).getAccount() + action + "\u6587\u4ef6\u9632\u7be1\u6539\u76d1\u6d4b\u4fe1\u606f\uff1a" + fileSafe.getHostname() + "\uff0c" + fileSafe.getFileName(), "\u6587\u4ef6\u540d\u79f0\uff1a" + fileSafe.getFileName() + "\uff0c\u6587\u4ef6\u8def\u5f84\uff1a" + fileSafe.getFilePath(), "2");
    }

    public List<HostGroup> setGroupInList(List<FileSafe> recordList, Model model, HttpServletRequest request) throws Exception {
        HashMap<String, Object> params = new HashMap<String, Object>();
        List<HostGroup> hostGroupList = this.hostGroupService.selectAllByParams(params, request);
        model.addAttribute("hostGroupList", hostGroupList);
        HashMap<String, String> hostGroupMap = new HashMap<String, String>();
        for (HostGroup hostGroup : hostGroupList) {
            hostGroupMap.put(hostGroup.getId(), hostGroup.getGroupName());
        }
        for (FileSafe appInfo : recordList) {
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
            FileSafe ho = null;
            for (String id : ids.split(",")) {
                ho = this.selectById(id);
                if (hostGroupList.size() > 0) {
                    ho.setGroupId(StringUtils.join((Object[])groupIdsArr, (String)","));
                } else {
                    ho.setGroupId("");
                }
                this.updateById(ho);
                this.logInfoService.save(HostUtil.getAccountByRequest(request).getAccount() + "\u8bbe\u7f6e\u6587\u4ef6\u9632\u7be1\u6539\u6807\u7b7e\uff1a" + ho.getHostname(), "\u6807\u7b7e\uff1a" + this.hostGroupService.returnGroupNames(hostGroupList), "2");
            }
        }
    }

    public void refreshState(String ids, HttpServletRequest request) throws Exception {
        if (!StringUtils.isEmpty((CharSequence)ids)) {
            FileSafe ho = null;
            for (String id : ids.split(",")) {
                ho = this.selectById(id);
                ho.setFileSign("refresh");
                ho.setState("1");
                this.updateById(ho);
                this.logInfoService.save(HostUtil.getAccountByRequest(request).getAccount() + "\u91cd\u65b0\u76d1\u63a7\u6587\u4ef6\u5939\uff1a" + ho.getHostname(), "\u6587\u4ef6\u5939\uff1a" + ho.getFileName() + "\uff0c\u6587\u4ef6\u8def\u5f84\uff1a" + ho.getFilePath(), "2");
            }
        }
    }
}

