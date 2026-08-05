/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.service;

import com.alibaba.excel.EasyExcel;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wgcloud.dto.llm.AgentRunStateExcelDto;
import com.wgcloud.entity.AgentRunState;
import com.wgcloud.entity.HostGroup;
import com.wgcloud.entity.SystemInfo;
import com.wgcloud.entity.SystemInfoExt;
import com.wgcloud.mapper.AgentRunStateMapper;
import com.wgcloud.service.HostGroupService;
import com.wgcloud.service.LogInfoService;
import com.wgcloud.service.SystemInfoExtService;
import com.wgcloud.service.SystemInfoService;
import com.wgcloud.util.DateUtil;
import com.wgcloud.util.FormatUtil;
import com.wgcloud.util.HostUtil;
import com.wgcloud.util.UUIDUtil;
import com.wgcloud.util.staticvar.StaticKeys;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

@Service
public class AgentRunStateService {
    private static final Logger logger = LoggerFactory.getLogger(AgentRunStateService.class);
    @Autowired
    private AgentRunStateMapper agentRunStateMapper;
    @Resource
    private HostGroupService hostGroupService;
    @Resource
    private SystemInfoExtService systemInfoExtService;
    @Resource
    private SystemInfoService systemInfoService;
    @Autowired
    private LogInfoService logInfoService;

    public PageInfo selectByParams(Map<String, Object> params, int currPage, int pageSize) throws Exception {
        PageHelper.startPage((int)currPage, (int)pageSize);
        List<AgentRunState> list = this.agentRunStateMapper.selectByParams(params);
        PageInfo pageInfo = new PageInfo(list);
        return pageInfo;
    }

    public void save(AgentRunState agentRunState, HttpServletRequest request) throws Exception {
        agentRunState.setId(UUIDUtil.getUUID());
        Date nowDate = new Date();
        agentRunState.setCreateTime(nowDate);
        this.agentRunStateMapper.save(agentRunState);
    }

    public int deleteByHostName(Map<String, Object> map) throws Exception {
        return this.agentRunStateMapper.deleteByHostName(map);
    }

    @Transactional
    public void saveRecord(List<AgentRunState> recordList) throws Exception {
        if (recordList.size() < 1) {
            return;
        }
        for (AgentRunState as : recordList) {
            as.setId(UUIDUtil.getUUID());
        }
        this.agentRunStateMapper.insertList(recordList);
    }

    public int countByParams(Map<String, Object> params) throws Exception {
        return this.agentRunStateMapper.countByParams(params);
    }

    @Transactional
    public int deleteById(String[] id) throws Exception {
        return this.agentRunStateMapper.deleteById(id);
    }

    @Transactional
    public void updateRecord(List<AgentRunState> recordList) throws Exception {
        if (recordList.size() < 1) {
            return;
        }
        this.agentRunStateMapper.updateList(recordList);
    }

    public void updateById(AgentRunState AgentRunState2) throws Exception {
        this.agentRunStateMapper.updateById(AgentRunState2);
    }

    public AgentRunState selectById(String id) throws Exception {
        return this.agentRunStateMapper.selectById(id);
    }

    public List<AgentRunState> selectAllByParams(Map<String, Object> params) throws Exception {
        return this.agentRunStateMapper.selectAllByParams(params);
    }

    public void setSystemInfoExtForList(List<AgentRunState> agentRunStateList) {
        try {
            HashMap<String, Object> params = new HashMap<String, Object>();
            List<SystemInfoExt> systemInfoExts = this.systemInfoExtService.selectAllByParams(params);
            block2: for (AgentRunState agentRunState : agentRunStateList) {
                for (SystemInfoExt systemInfoExt : systemInfoExts) {
                    if (!agentRunState.getHostname().equals(systemInfoExt.getHostname())) continue;
                    agentRunState.setPlatForm(systemInfoExt.getPlatForm());
                    agentRunState.setPlatformVersion(systemInfoExt.getPlatformVersion());
                    agentRunState.setHostnameExt(systemInfoExt.getHostnameExt());
                    continue block2;
                }
            }
            List<SystemInfo> systemInfoList = this.systemInfoService.selectAllByParams(params);
            block4: for (AgentRunState agentRunState : agentRunStateList) {
                for (SystemInfo systemInfo : systemInfoList) {
                    if (!agentRunState.getHostname().equals(systemInfo.getHostname())) continue;
                    agentRunState.setState(systemInfo.getState());
                    agentRunState.setGroupId(systemInfo.getGroupId());
                    agentRunState.setAccount(systemInfo.getAccount());
                    HostUtil.setSysImage(systemInfo);
                    agentRunState.setImage(systemInfo.getImage());
                    continue block4;
                }
            }
        }
        catch (Exception e) {
            logger.error("setSystemInfoExtForList\u9519\u8bef", (Throwable)e);
        }
    }

    public List<HostGroup> setGroupInList(List<AgentRunState> recordList, Model model, HttpServletRequest request) throws Exception {
        HashMap<String, Object> params = new HashMap<String, Object>();
        List<HostGroup> hostGroupList = this.hostGroupService.selectAllByParams(params, request);
        model.addAttribute("hostGroupList", hostGroupList);
        HashMap<String, String> hostGroupMap = new HashMap<String, String>();
        for (HostGroup hostGroup : hostGroupList) {
            hostGroupMap.put(hostGroup.getId(), hostGroup.getGroupName());
        }
        for (AgentRunState AgentRunState2 : recordList) {
            if (StringUtils.isEmpty((CharSequence)AgentRunState2.getGroupId())) continue;
            String groupNames = "";
            for (String groupId : AgentRunState2.getGroupId().split(",")) {
                if (!hostGroupMap.containsKey(groupId)) continue;
                groupNames = groupNames + (String)hostGroupMap.get(groupId) + ",";
            }
            if (groupNames.endsWith(",")) {
                groupNames = groupNames.substring(0, groupNames.length() - 1);
            }
            AgentRunState2.setGroupId(groupNames);
        }
        return hostGroupList;
    }

    public void exportListExcel(HttpServletRequest request, HttpServletResponse response) {
        try {
            HashMap<String, Object> params = new HashMap<String, Object>();
            HostUtil.addAccountquery(request, params);
            List<AgentRunState> agentRunStateList = this.selectAllByParams(params);
            ArrayList<AgentRunStateExcelDto> excelList = new ArrayList<AgentRunStateExcelDto>();
            for (AgentRunState agentRunState : agentRunStateList) {
                AgentRunStateExcelDto dto = new AgentRunStateExcelDto();
                dto.setCreateTime(DateUtil.getDateTimeString(agentRunState.getCreateTime()));
                dto.setDownTime(FormatUtil.formatDouble((double)agentRunState.getDownTime().intValue() / 24.0, 2) + "\u5929");
                dto.setOnlineTime(FormatUtil.formatDouble((double)agentRunState.getOnlineTime().intValue() / 24.0, 2) + "\u5929");
                dto.setLastTime(DateUtil.getDateTimeString(agentRunState.getLastTime()));
                dto.setHostname(agentRunState.getHostname());
                dto.setRemark(StaticKeys.HOST_MAP.get(agentRunState.getHostname()));
                excelList.add(dto);
            }
            String fileName = DateUtil.getCurrentDateTimeNoChar() + "_Agent\u7ec8\u7aef\u8fd0\u884c\u7edf\u8ba1\u4fe1\u606f.xlsx";
            response.setContentType("application/vnd.ms-exce");
            response.setCharacterEncoding("utf-8");
            String encodedFileName = URLEncoder.encode(fileName, "UTF-8");
            response.addHeader("Content-Disposition", "filename=" + encodedFileName);
            EasyExcel.write((OutputStream)response.getOutputStream(), AgentRunStateExcelDto.class).sheet("sheet").doWrite(excelList);
        }
        catch (Exception e) {
            logger.error("\u5bfc\u51fa\u6240\u6709Agent\u7ec8\u7aef\u8fd0\u884c\u7edf\u8ba1\u4fe1\u606f\u9519\u8bef", (Throwable)e);
        }
    }

    public void saveLog(HttpServletRequest request, String action, AgentRunState agentRunState) {
        if (null == agentRunState) {
            return;
        }
        this.logInfoService.save(HostUtil.getAccountByRequest(request).getAccount() + action + "agent\u7ec8\u7aef\u8fd0\u884c\u72b6\u6001\u4fe1\u606f\uff1a" + agentRunState.getHostname(), agentRunState.getHostname(), "2");
    }
}

