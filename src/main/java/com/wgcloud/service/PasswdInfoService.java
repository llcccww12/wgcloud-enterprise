/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wgcloud.entity.HostGroup;
import com.wgcloud.entity.PasswdInfo;
import com.wgcloud.mapper.PasswdInfoMapper;
import com.wgcloud.service.HostGroupService;
import com.wgcloud.service.LogInfoService;
import com.wgcloud.util.DESUtil;
import com.wgcloud.util.DateUtil;
import com.wgcloud.util.FileUtils;
import com.wgcloud.util.HostUtil;
import com.wgcloud.util.UUIDUtil;
import com.wgcloud.util.staticvar.StaticKeys;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

@Service
public class PasswdInfoService {
    private static final Logger logger = LoggerFactory.getLogger(PasswdInfoService.class);
    @Autowired
    private PasswdInfoMapper passwdInfoMapper;
    @Autowired
    private LogInfoService logInfoService;
    @Resource
    private HostGroupService hostGroupService;

    public PageInfo<PasswdInfo> selectByParams(Map<String, Object> params, int currPage, int pageSize) throws Exception {
        PageHelper.startPage((int)currPage, (int)pageSize);
        List<PasswdInfo> list = this.passwdInfoMapper.selectByParams(params);
        PageInfo pageInfo = new PageInfo(list);
        return pageInfo;
    }

    public void save(PasswdInfo passwdInfo) throws Exception {
        passwdInfo.setId(UUIDUtil.getUUID());
        passwdInfo.setCreateTime(new Date());
        this.passwdInfoMapper.save(passwdInfo);
    }

    @Transactional
    public int deleteById(String[] id) throws Exception {
        return this.passwdInfoMapper.deleteById(id);
    }

    public void updateById(PasswdInfo PasswdInfo2) throws Exception {
        this.passwdInfoMapper.updateById(PasswdInfo2);
    }

    public PasswdInfo selectById(String id) throws Exception {
        return this.passwdInfoMapper.selectById(id);
    }

    public int countByParams(Map<String, Object> params) throws Exception {
        return this.passwdInfoMapper.countByParams(params);
    }

    public List<HostGroup> setGroupInList(List<PasswdInfo> recordList, Model model, HttpServletRequest request) throws Exception {
        HashMap<String, Object> params = new HashMap<String, Object>();
        List<HostGroup> hostGroupList = this.hostGroupService.selectAllByParams(params, request);
        model.addAttribute("hostGroupList", hostGroupList);
        HashMap<String, String> hostGroupMap = new HashMap<String, String>();
        for (HostGroup hostGroup : hostGroupList) {
            hostGroupMap.put(hostGroup.getId(), hostGroup.getGroupName());
        }
        for (PasswdInfo appInfo : recordList) {
            if (StringUtils.isEmpty((CharSequence)appInfo.getGroupId())) continue;
            String groupNames = "";
            for (String groupId : appInfo.getGroupId().split(",")) {
                if (!hostGroupMap.containsKey(groupId)) continue;
                groupNames = groupNames + (String)hostGroupMap.get(groupId) + ",";
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
            PasswdInfo ho = null;
            for (String id : ids.split(",")) {
                ho = this.selectById(id);
                if (hostGroupList.size() > 0) {
                    ho.setGroupId(StringUtils.join((Object[])groupIdsArr, (String)","));
                } else {
                    ho.setGroupId("");
                }
                this.updateById(ho);
            }
        }
    }

    public void saveLog(HttpServletRequest request, String action, PasswdInfo passwdInfo) {
        if (null == passwdInfo) {
            return;
        }
        this.logInfoService.save(HostUtil.getAccountByRequest(request).getAccount() + action + "\u8bbe\u5907\u8d26\u53f7\uff1a" + passwdInfo.getHostname(), "\u8bbe\u5907\u8d26\u53f7\uff1a" + passwdInfo.getHostname(), "2");
    }

    public void updateToTargetAccount(Map<String, Object> params) throws Exception {
        this.passwdInfoMapper.updateToTargetAccount(params);
    }

    public void importExcelPasswd() {
        try {
            XSSFRow row;
            String filePath = StaticKeys.JAR_PATH + "/template/passwd.xlsx";
            if (!FileUtils.existsFile(filePath)) {
                return;
            }
            logger.info("\u5f00\u59cb\u4eceexcel\u6587\u4ef6\u5bfc\u5165\u8bbe\u5907\u8d26\u53f7\u6570\u636e-----" + filePath);
            HashMap map = new HashMap();
            XSSFWorkbook workbook = new XSSFWorkbook(filePath);
            XSSFSheet sheet = workbook.getSheetAt(0);
            int firstRowNum = sheet.getFirstRowNum();
            int lastRowNum = sheet.getLastRowNum();
            for (int rowNum = firstRowNum; rowNum <= lastRowNum && null != (row = sheet.getRow(rowNum)); ++rowNum) {
                String cellHostRemark;
                if (null == row.getCell(0)) continue;
                String cellHostname = row.getCell(0).getStringCellValue();
                if (!StringUtils.isEmpty((CharSequence)cellHostname)) {
                    cellHostname = cellHostname.trim();
                }
                String cellHostMark = "";
                if (null != row.getCell(1)) {
                    cellHostMark = row.getCell(1).getStringCellValue();
                }
                if (!StringUtils.isEmpty((CharSequence)cellHostMark)) {
                    cellHostMark = cellHostMark.trim();
                }
                String cellHostAccount = "";
                if (null != row.getCell(2)) {
                    cellHostAccount = row.getCell(2).getStringCellValue();
                }
                if (!StringUtils.isEmpty((CharSequence)cellHostAccount)) {
                    cellHostAccount = cellHostAccount.trim();
                }
                String cellHostPasswd = "";
                if (null != row.getCell(3)) {
                    cellHostPasswd = row.getCell(3).getStringCellValue();
                }
                if (!StringUtils.isEmpty((CharSequence)cellHostPasswd)) {
                    cellHostPasswd = cellHostPasswd.trim();
                    cellHostPasswd = DESUtil.encryptionForServerDb(cellHostPasswd);
                }
                if (!StringUtils.isEmpty((CharSequence)(cellHostRemark = row.getCell(4).getStringCellValue()))) {
                    cellHostRemark = cellHostRemark.trim();
                }
                if (StringUtils.isEmpty((CharSequence)cellHostname)) continue;
                PasswdInfo passwdInfo = new PasswdInfo();
                passwdInfo.setHostname(cellHostname);
                passwdInfo.setHostMark(cellHostMark);
                passwdInfo.setHostAccount(cellHostAccount);
                passwdInfo.setHostPasswd(cellHostPasswd);
                passwdInfo.setHostRemark(cellHostRemark);
                this.save(passwdInfo);
            }
            File oldFile = new File(filePath);
            File newFile = new File(StaticKeys.JAR_PATH + "/template/passwd_" + DateUtil.getCurrentDateTimeNoChar() + ".xlsx");
            oldFile.renameTo(newFile);
            logger.info("\u8bbe\u5907\u8d26\u53f7\u6570\u636e\u5bfc\u5165\u5b8c\u6210\uff0c\u4fee\u6539\u6587\u4ef6\u540d\u79f0\u4e3a-----" + newFile.getName());
        }
        catch (Exception e) {
            logger.error("\u8bbe\u5907\u8d26\u53f7\u5bfc\u5165excel\u9519\u8bef", (Throwable)e);
        }
    }
}

