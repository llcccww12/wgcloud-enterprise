/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wgcloud.entity.FtpInfo;
import com.wgcloud.entity.HostGroup;
import com.wgcloud.mapper.FtpInfoMapper;
import com.wgcloud.service.HostGroupService;
import com.wgcloud.service.LogInfoService;
import com.wgcloud.util.DESUtil;
import com.wgcloud.util.HostUtil;
import com.wgcloud.util.JschUtil;
import com.wgcloud.util.MessageErrorUtils;
import com.wgcloud.util.ServerBackupUtil;
import com.wgcloud.util.ThreadPoolUtil;
import com.wgcloud.util.UUIDUtil;
import com.wgcloud.util.msg.WarnOtherUtil;
import com.wgcloud.util.msg.WarnPools;
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
public class FtpInfoService {
    private static final Logger logger = LoggerFactory.getLogger(FtpInfoService.class);
    @Autowired
    private FtpInfoMapper ftpInfoMapper;
    @Autowired
    private LogInfoService logInfoService;
    @Resource
    private HostGroupService hostGroupService;
    @Autowired
    private MessageErrorUtils messageErrorUtils;

    public PageInfo selectByParams(Map<String, Object> params, int currPage, int pageSize) throws Exception {
        PageHelper.startPage((int)currPage, (int)pageSize);
        List<FtpInfo> list = this.ftpInfoMapper.selectByParams(params);
        PageInfo pageInfo = new PageInfo(list);
        return pageInfo;
    }

    public void save(FtpInfo ftpInfo) throws Exception {
        ftpInfo.setId(UUIDUtil.getUUID());
        ftpInfo.setCreateTime(new Date());
        if (!StringUtils.isEmpty((CharSequence)ftpInfo.getFtpHost())) {
            ftpInfo.setFtpHost(ftpInfo.getFtpHost().trim());
        }
        if (!StringUtils.isEmpty((CharSequence)ftpInfo.getPort())) {
            ftpInfo.setPort(ftpInfo.getPort().trim());
        }
        if (StringUtils.isEmpty((CharSequence)ftpInfo.getUserName())) {
            ftpInfo.setUserName(ftpInfo.getUserName().trim());
        }
        if (StringUtils.isEmpty((CharSequence)ftpInfo.getPasswd())) {
            ftpInfo.setPasswd(ftpInfo.getPasswd().trim());
        }
        this.ftpInfoMapper.save(ftpInfo);
    }

    @Transactional
    public void saveRecord(List<FtpInfo> recordList) throws Exception {
        if (recordList.size() < 1) {
            return;
        }
        for (FtpInfo as : recordList) {
            as.setId(UUIDUtil.getUUID());
        }
        this.ftpInfoMapper.insertList(recordList);
    }

    public int countByParams(Map<String, Object> params) throws Exception {
        return this.ftpInfoMapper.countByParams(params);
    }

    @Transactional
    public int deleteById(String[] id) throws Exception {
        return this.ftpInfoMapper.deleteById(id);
    }

    public int updateActive(Map<String, Object> params) throws Exception {
        return this.ftpInfoMapper.updateActive(params);
    }

    @Transactional
    public void updateRecord(List<FtpInfo> recordList) throws Exception {
        if (recordList.size() < 1) {
            return;
        }
        this.ftpInfoMapper.updateList(recordList);
    }

    public void updateById(FtpInfo ftpInfo) throws Exception {
        this.ftpInfoMapper.updateById(ftpInfo);
    }

    public FtpInfo selectById(String id) throws Exception {
        return this.ftpInfoMapper.selectById(id);
    }

    public List<FtpInfo> selectAllByParams(Map<String, Object> params) throws Exception {
        return this.ftpInfoMapper.selectAllByParams(params);
    }

    public void addServerBackMark(List<FtpInfo> list) throws Exception {
        for (FtpInfo ftpInfo : list) {
            if (ServerBackupUtil.isExistFtpInfoId(ftpInfo.getId())) {
                ftpInfo.setServerBackupMark("1");
                continue;
            }
            ftpInfo.setServerBackupMark("2");
        }
    }

    public void taskThreadHandler() {
        HashMap<String, Object> params = new HashMap<String, Object>();
        Date date = new Date();
        try {
            params.put("active", "1");
            List<FtpInfo> ftpInfos = this.ftpInfoMapper.selectAllByParams(params);
            if (ftpInfos.size() > 10 && !StaticKeys.LICENSE_STATE.equals("1")) {
                logger.info("\u4e2a\u4eba\u7248\u53ea\u80fd\u76d1\u6d4b10\u4e2aFTP");
                ftpInfos = ftpInfos.subList(0, 10);
            }
            for (FtpInfo ftpInfo : ftpInfos) {
                if (ServerBackupUtil.isExistFtpInfoId(ftpInfo.getId())) {
                    logger.info("\u6b64ftp/sftp\u7531wgcloud-server-backup\u76d1\u6d4b:" + ftpInfo.getFtpHost());
                    continue;
                }
                if (!StringUtils.isEmpty((CharSequence)ftpInfo.getPasswd())) {
                    ftpInfo.setPasswd(DESUtil.decryptForServerDb(ftpInfo.getPasswd()));
                }
                Runnable runnable = () -> {
                    if ("SFTP".equals(ftpInfo.getFtpType())) {
                        JschUtil.testSFtpSession(ftpInfo);
                    } else {
                        JschUtil.testFTPClient(ftpInfo);
                    }
                    try {
                        FtpInfo ftpInfoForUpdate = new FtpInfo();
                        ftpInfoForUpdate.setId(ftpInfo.getId());
                        ftpInfoForUpdate.setState(ftpInfo.getState());
                        ftpInfoForUpdate.setResTimes(ftpInfo.getResTimes());
                        if ("2".equals(ftpInfo.getState())) {
                            ftpInfoForUpdate.setCreateTime(null);
                        } else {
                            ftpInfoForUpdate.setCreateTime(date);
                        }
                        this.ftpInfoMapper.updateById(ftpInfoForUpdate);
                        this.messageErrorUtils.setErrorMsgHandler(ftpInfo.getId(), ftpInfo.getTestErrorMsg());
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                    if ("2".equals(ftpInfo.getState())) {
                        WarnOtherUtil.sendFtpInfo(ftpInfo, true);
                    } else if (!StringUtils.isEmpty((CharSequence)WarnPools.getWarnMark(ftpInfo.getId()))) {
                        WarnOtherUtil.sendFtpInfo(ftpInfo, false);
                    }
                };
                ThreadPoolUtil.executor.execute(runnable);
            }
        }
        catch (Exception e) {
            logger.error("FTP\u76d1\u63a7\u4efb\u52a1\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("FTP\u76d1\u63a7\u4efb\u52a1\u9519\u8bef", e.toString(), "2");
        }
    }

    public void saveLog(HttpServletRequest request, String action, FtpInfo ftpInfo) {
        if (null == ftpInfo) {
            return;
        }
        this.logInfoService.save(HostUtil.getAccountByRequest(request).getAccount() + action + "ftp\u76d1\u6d4b\u4fe1\u606f\uff1a" + ftpInfo.getFtpName(), "\u4e3b\u673a\uff1a" + ftpInfo.getFtpHost() + "\uff0c\u7aef\u53e3\uff1a" + ftpInfo.getPort(), "2");
    }

    public void updateToTargetAccount(Map<String, Object> params) throws Exception {
        this.ftpInfoMapper.updateToTargetAccount(params);
    }

    public List<HostGroup> setGroupInList(List<FtpInfo> recordList, Model model, HttpServletRequest request) throws Exception {
        HashMap<String, Object> params = new HashMap<String, Object>();
        List<HostGroup> hostGroupList = this.hostGroupService.selectAllByParams(params, request);
        model.addAttribute("hostGroupList", hostGroupList);
        HashMap<String, String> hostGroupMap = new HashMap<String, String>();
        for (HostGroup hostGroup : hostGroupList) {
            hostGroupMap.put(hostGroup.getId(), hostGroup.getGroupName());
        }
        for (FtpInfo appInfo : recordList) {
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
            FtpInfo ho = null;
            for (String id : ids.split(",")) {
                ho = this.selectById(id);
                if (hostGroupList.size() > 0) {
                    ho.setGroupId(StringUtils.join((Object[])groupIdsArr, (String)","));
                } else {
                    ho.setGroupId("");
                }
                this.updateById(ho);
                this.logInfoService.save(HostUtil.getAccountByRequest(request).getAccount() + "\u8bbe\u7f6eFTP\u6807\u7b7e\uff1a" + ho.getFtpName(), "\u6807\u7b7e\uff1a" + this.hostGroupService.returnGroupNames(hostGroupList), "2");
            }
        }
    }
}

