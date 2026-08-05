/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.service;

import cn.hutool.core.bean.BeanUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wgcloud.config.CommonConfig;
import com.wgcloud.dto.ShellCheckDto;
import com.wgcloud.entity.HostGroup;
import com.wgcloud.entity.ShellInfo;
import com.wgcloud.entity.ShellState;
import com.wgcloud.entity.SystemInfo;
import com.wgcloud.mapper.ShellInfoMapper;
import com.wgcloud.mapper.ShellStateMapper;
import com.wgcloud.service.HostGroupService;
import com.wgcloud.service.ShellStateService;
import com.wgcloud.service.SystemInfoService;
import com.wgcloud.util.DateUtil;
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
public class ShellInfoService {
    private static final Logger logger = LoggerFactory.getLogger(ShellInfoService.class);
    @Autowired
    private ShellInfoMapper shellInfoMapper;
    @Autowired
    private ShellStateMapper shellStateMapper;
    @Autowired
    private ShellStateService shellStateService;
    @Resource
    private SystemInfoService systemInfoService;
    @Resource
    private HostGroupService hostGroupService;
    @Autowired
    private CommonConfig commonConfig;

    public PageInfo selectByParams(Map<String, Object> params, int currPage, int pageSize) throws Exception {
        PageHelper.startPage((int)currPage, (int)pageSize);
        List<ShellInfo> list = this.shellInfoMapper.selectByParams(params);
        PageInfo pageInfo = new PageInfo(list);
        return pageInfo;
    }

    public void save(ShellInfo shellInfo, String[] hostnames, HttpServletRequest request) throws Exception {
        Date nowDate = new Date();
        shellInfo.setId(UUIDUtil.getUUID());
        shellInfo.setState("1");
        shellInfo.setCreateTime(nowDate);
        if (!StringUtils.isEmpty((CharSequence)shellInfo.getShell())) {
            shellInfo.setShell(shellInfo.getShell().trim());
            if (hostnames.length > 1 && shellInfo.getShell().startsWith("setBindIpAjax:")) {
                logger.error("\u4e0b\u53d1\u6307\u4ee4\u4fee\u6539agent\u7684\u53c2\u6570bindIp\uff0c\u6bcf\u6b21\u53ea\u80fd\u4fee\u6539\u4e00\u4e2a\u4e3b\u673a\uff0c\u672c\u6b21\u9009\u62e9\u4e3b\u673a\u6570\u91cf\u8d85\u8fc71\uff0c\u4e0d\u4e0b\u53d1\u6307\u4ee4");
                return;
            }
        }
        ArrayList<ShellState> shellStateList = new ArrayList<ShellState>();
        for (String hostname : hostnames) {
            ShellState shellState = new ShellState();
            shellState.setShellId(shellInfo.getId());
            shellState.setHostname(hostname);
            shellState.setCreateTime(nowDate);
            shellState.setShell(shellInfo.getShell());
            shellState.setState("1");
            if ("2".equals(shellInfo.getShellType())) {
                shellState.setShellTime(DateUtil.getDate(shellInfo.getShellTime()).getTime() + "");
            } else {
                shellState.setShellTime("0");
            }
            shellStateList.add(shellState);
        }
        this.shellInfoMapper.save(shellInfo);
        this.shellStateService.saveRecord(shellStateList);
        if ("2".equals(shellInfo.getShellType())) {
            this.addExtDataForm(shellInfo, request, nowDate, hostnames);
        }
    }

    private void addExtDataForm(ShellInfo shellInfo, HttpServletRequest request, Date nowDate, String[] hostnames) throws Exception {
        String continueDays = request.getParameter("continueDays");
        if (!StringUtils.isEmpty((CharSequence)continueDays)) {
            int continueDaysInt = Integer.valueOf(continueDays);
            List<String> dateList = DateUtil.getDateAfterList(shellInfo.getShellTime(), continueDaysInt);
            for (int i = 0; i < dateList.size(); ++i) {
                ShellInfo shellInfoExt = new ShellInfo();
                BeanUtil.copyProperties((Object)shellInfo, (Object)shellInfoExt, (boolean)true);
                shellInfoExt.setId(UUIDUtil.getUUID());
                shellInfoExt.setCreateTime(nowDate);
                shellInfoExt.setShellName(shellInfoExt.getShellName() + "_" + dateList.get(i).substring(0, 10));
                shellInfoExt.setShellTime(dateList.get(i));
                ArrayList<ShellState> shellStateList = new ArrayList<ShellState>();
                for (String hostname : hostnames) {
                    ShellState shellState = new ShellState();
                    shellState.setShellId(shellInfoExt.getId());
                    shellState.setHostname(hostname);
                    shellState.setCreateTime(nowDate);
                    shellState.setShell(shellInfoExt.getShell());
                    shellState.setState("1");
                    shellState.setShellTime(DateUtil.getDate(shellInfoExt.getShellTime()).getTime() + "");
                    shellStateList.add(shellState);
                }
                this.shellInfoMapper.save(shellInfoExt);
                this.shellStateService.saveRecord(shellStateList);
            }
        }
    }

    @Transactional
    public void saveRecord(List<ShellInfo> recordList) throws Exception {
        if (recordList.size() < 1) {
            return;
        }
        for (ShellInfo as : recordList) {
            as.setId(UUIDUtil.getUUID());
        }
        this.shellInfoMapper.insertList(recordList);
    }

    public int countByParams(Map<String, Object> params) throws Exception {
        return this.shellInfoMapper.countByParams(params);
    }

    @Transactional
    public int deleteById(String[] id) throws Exception {
        for (String shellInfoId : id) {
            this.shellStateMapper.deleteByShellId(shellInfoId);
        }
        return this.shellInfoMapper.deleteById(id);
    }

    @Transactional
    public void updateRecord(List<ShellInfo> recordList) throws Exception {
        if (recordList.size() < 1) {
            return;
        }
        this.shellInfoMapper.updateList(recordList);
    }

    public void updateById(ShellInfo ShellInfo2) throws Exception {
        this.shellInfoMapper.updateById(ShellInfo2);
    }

    @Transactional
    public void cancelShell(String id) throws Exception {
        ShellInfo shellInfo = this.shellInfoMapper.selectById(id);
        shellInfo.setState("2");
        this.shellInfoMapper.updateById(shellInfo);
        this.shellStateMapper.cancelByShellId(id);
    }

    @Transactional
    public void restart(String id) throws Exception {
        ShellInfo shellInfo = this.shellInfoMapper.selectById(id);
        shellInfo.setState("1");
        shellInfo.setCreateTime(new Date());
        this.shellInfoMapper.updateById(shellInfo);
        String shellTime = "0";
        if ("2".equals(shellInfo.getShellType())) {
            shellTime = DateUtil.getDate(shellInfo.getShellTime()).getTime() + "";
        }
        this.shellStateMapper.restartByShellId(id, shellTime);
    }

    public ShellInfo selectById(String id) throws Exception {
        return this.shellInfoMapper.selectById(id);
    }

    public List<ShellInfo> selectAllByParams(Map<String, Object> params) throws Exception {
        return this.shellInfoMapper.selectAllByParams(params);
    }

    public void getBlockStr(Model model) {
        String blockStr = "";
        if (!StringUtils.isEmpty((CharSequence)this.commonConfig.getShellToRunBlock())) {
            blockStr = "[\u6307\u4ee4\u4e0d\u80fd\u5305\u542b\u654f\u611f\u5b57\u7b26:" + this.commonConfig.getShellToRunBlock() + "]";
        }
        model.addAttribute("blockStr", (Object)blockStr);
    }

    public ShellCheckDto getShellCheckDto(String hostname) throws Exception {
        ShellCheckDto shellCheckDto = new ShellCheckDto();
        String cmdSplitChar = " && ";
        shellCheckDto.setCmdSplitChar(cmdSplitChar);
        return shellCheckDto;
    }

    public void setGroupList(List<SystemInfo> recordList, Model model, HttpServletRequest request) {
        try {
            String displayGroupHref = request.getParameter("displayGroupHref");
            model.addAttribute("displayGroupHref", (Object)displayGroupHref);
            HashMap<String, Object> params = new HashMap<String, Object>();
            List<HostGroup> hostGroupList = this.hostGroupService.selectAllByParams(params, request);
            model.addAttribute("hostGroupList", hostGroupList);
            HashMap<String, String> hostGroupMap = new HashMap<String, String>();
            for (HostGroup hostGroup : hostGroupList) {
                hostGroupMap.put(hostGroup.getId(), hostGroup.getGroupName());
            }
            for (SystemInfo systemInfo : recordList) {
                String remark = systemInfo.getRemark();
                if (StringUtils.isEmpty((CharSequence)remark)) {
                    remark = "";
                }
                if ("1".equals(displayGroupHref)) {
                    if (!StringUtils.isEmpty((CharSequence)systemInfo.getGroupId())) {
                        String groupNames = "";
                        for (String groupId : systemInfo.getGroupId().split(",")) {
                            if (!hostGroupMap.containsKey(groupId)) continue;
                            groupNames = groupNames + (String)hostGroupMap.get(groupId) + ",";
                        }
                        if (groupNames.endsWith(",")) {
                            groupNames = groupNames.substring(0, groupNames.length() - 1);
                        }
                        remark = !StringUtils.isEmpty((CharSequence)remark) ? remark + "\uff0c" + groupNames : groupNames;
                    }
                } else {
                    remark = !StringUtils.isEmpty((CharSequence)remark) ? remark + "\uff0c" + systemInfo.getPlatForm() : systemInfo.getPlatForm();
                }
                systemInfo.setRemark(remark);
            }
        }
        catch (Exception e) {
            logger.error("\u8bbe\u7f6e\u6307\u4ee4\u4e0b\u53d1\u6dfb\u52a0\u9875\u9762\u7684\u6807\u7b7e\u5217\u8868\u9519\u8bef", (Throwable)e);
        }
    }
}

