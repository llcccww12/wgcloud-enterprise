/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.service;

import cn.hutool.core.bean.BeanUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wgcloud.entity.HostGroup;
import com.wgcloud.entity.PortInfo;
import com.wgcloud.mapper.PortInfoMapper;
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
public class PortInfoService {
    private static final Logger logger = LoggerFactory.getLogger(PortInfoService.class);
    @Autowired
    private PortInfoMapper portInfoMapper;
    @Resource
    private HostGroupService hostGroupService;
    @Autowired
    private LogInfoService logInfoService;

    public PageInfo selectByParams(Map<String, Object> params, int currPage, int pageSize) throws Exception {
        PageHelper.startPage((int)currPage, (int)pageSize);
        List<PortInfo> list = this.portInfoMapper.selectByParams(params);
        PageInfo pageInfo = new PageInfo(list);
        return pageInfo;
    }

    public void save(PortInfo portInfo, HttpServletRequest request) throws Exception {
        portInfo.setId(UUIDUtil.getUUID());
        Date nowDate = new Date();
        portInfo.setCreateTime(nowDate);
        if (!StringUtils.isEmpty((CharSequence)portInfo.getTelnetIp())) {
            portInfo.setTelnetIp(portInfo.getTelnetIp().trim());
        }
        if (!StringUtils.isEmpty((CharSequence)portInfo.getPort())) {
            portInfo.setPort(portInfo.getPort().trim());
        }
        if (StringUtils.isEmpty((CharSequence)portInfo.getPort())) {
            return;
        }
        this.portInfoMapper.save(portInfo);
        this.addExtDataForm(portInfo, request, nowDate);
    }

    public int updateActive(Map<String, Object> params) throws Exception {
        return this.portInfoMapper.updateActive(params);
    }

    private void addExtDataForm(PortInfo portInfo, HttpServletRequest request, Date nowDate) throws Exception {
        String dataFromIndex = request.getParameter("dataFromIndex");
        int rowsLen = 0;
        if (!StringUtils.isEmpty((CharSequence)dataFromIndex)) {
            for (int i = 0; i <= Integer.valueOf(dataFromIndex); ++i) {
                String telnetIp = request.getParameter("telnetIp_" + i);
                String port = request.getParameter("port_" + i);
                String portName = request.getParameter("portName_" + i);
                if (StringUtils.isEmpty((CharSequence)port) || StringUtils.isEmpty((CharSequence)portName) || StringUtils.isEmpty((CharSequence)telnetIp)) continue;
                PortInfo portInfoExt = new PortInfo();
                BeanUtil.copyProperties((Object)portInfo, (Object)portInfoExt, (boolean)true);
                portInfoExt.setId(UUIDUtil.getUUID());
                portInfoExt.setCreateTime(nowDate);
                portInfoExt.setPort(port.trim());
                portInfoExt.setTelnetIp(telnetIp.trim());
                portInfoExt.setPortName(portName.trim());
                this.portInfoMapper.save(portInfoExt);
                this.saveLog(request, "\u6dfb\u52a0", portInfoExt);
                ++rowsLen;
            }
        }
    }

    public int deleteByHostName(Map<String, Object> map) throws Exception {
        return this.portInfoMapper.deleteByHostName(map);
    }

    @Transactional
    public void saveRecord(List<PortInfo> recordList) throws Exception {
        if (recordList.size() < 1) {
            return;
        }
        for (PortInfo as : recordList) {
            as.setId(UUIDUtil.getUUID());
        }
        this.portInfoMapper.insertList(recordList);
    }

    public void downByHostName(List<String> recordList) throws Exception {
        if (recordList.size() < 1) {
            return;
        }
        this.portInfoMapper.downByHostName(recordList);
    }

    public int countByParams(Map<String, Object> params) throws Exception {
        return this.portInfoMapper.countByParams(params);
    }

    @Transactional
    public int deleteById(String[] id) throws Exception {
        return this.portInfoMapper.deleteById(id);
    }

    @Transactional
    public void updateRecord(List<PortInfo> recordList) throws Exception {
        if (recordList.size() < 1) {
            return;
        }
        this.portInfoMapper.updateList(recordList);
    }

    public void updateById(PortInfo portInfo) throws Exception {
        if (!StringUtils.isEmpty((CharSequence)portInfo.getTelnetIp())) {
            portInfo.setTelnetIp(portInfo.getTelnetIp().trim());
        }
        if (!StringUtils.isEmpty((CharSequence)portInfo.getPort())) {
            portInfo.setPort(portInfo.getPort().trim());
        }
        this.portInfoMapper.updateById(portInfo);
    }

    public PortInfo selectById(String id) throws Exception {
        return this.portInfoMapper.selectById(id);
    }

    public List<PortInfo> selectAllByParams(Map<String, Object> params) throws Exception {
        return this.portInfoMapper.selectAllByParams(params);
    }

    public void saveGroupId(String ids, String[] groupIdsArr, HttpServletRequest request) throws Exception {
        List<HostGroup> hostGroupList = new ArrayList();
        if (null != groupIdsArr && groupIdsArr.length > 0) {
            HashMap<String, Object> params = new HashMap<String, Object>();
            params.put("groupIds", groupIdsArr);
            hostGroupList = this.hostGroupService.selectAllByParams(params, request);
        }
        if (!StringUtils.isEmpty((CharSequence)ids)) {
            PortInfo ho = null;
            for (String id : ids.split(",")) {
                ho = this.selectById(id);
                if (hostGroupList.size() > 0) {
                    ho.setGroupId(StringUtils.join((Object[])groupIdsArr, (String)","));
                } else {
                    ho.setGroupId("");
                }
                this.updateById(ho);
                this.logInfoService.save(HostUtil.getAccountByRequest(request).getAccount() + "\u8bbe\u7f6e\u7aef\u53e3\u6807\u7b7e\uff1a" + ho.getHostname(), "\u6807\u7b7e\uff1a" + this.hostGroupService.returnGroupNames(hostGroupList), "2");
            }
        }
    }

    public List<HostGroup> setGroupInList(List<PortInfo> recordList, Model model, HttpServletRequest request) throws Exception {
        HashMap<String, Object> params = new HashMap<String, Object>();
        List<HostGroup> hostGroupList = this.hostGroupService.selectAllByParams(params, request);
        model.addAttribute("hostGroupList", hostGroupList);
        HashMap<String, String> hostGroupMap = new HashMap<String, String>();
        for (HostGroup hostGroup : hostGroupList) {
            hostGroupMap.put(hostGroup.getId(), hostGroup.getGroupName());
        }
        for (PortInfo portInfo : recordList) {
            if (StringUtils.isEmpty((CharSequence)portInfo.getGroupId())) continue;
            String groupNames = "";
            for (String groupId : portInfo.getGroupId().split(",")) {
                if (!hostGroupMap.containsKey(groupId)) continue;
                groupNames = groupNames + (String)hostGroupMap.get(groupId) + ",";
            }
            if (groupNames.endsWith(",")) {
                groupNames = groupNames.substring(0, groupNames.length() - 1);
            }
            portInfo.setGroupId(groupNames);
        }
        return hostGroupList;
    }

    public void saveLog(HttpServletRequest request, String action, PortInfo portInfo) {
        if (null == portInfo) {
            return;
        }
        this.logInfoService.save(HostUtil.getAccountByRequest(request).getAccount() + action + "\u7aef\u53e3\u76d1\u6d4b\u4fe1\u606f\uff1a" + portInfo.getHostname() + "\uff0c" + portInfo.getPortName(), "telnet " + portInfo.getTelnetIp() + " " + portInfo.getPort(), "2");
    }
}

