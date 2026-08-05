/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.service;

import cn.hutool.core.bean.BeanUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wgcloud.entity.DceInfo;
import com.wgcloud.entity.DceState;
import com.wgcloud.entity.HostGroup;
import com.wgcloud.mapper.DceInfoMapper;
import com.wgcloud.service.HostGroupService;
import com.wgcloud.service.LogInfoService;
import com.wgcloud.util.DateUtil;
import com.wgcloud.util.FileUtils;
import com.wgcloud.util.HostUtil;
import com.wgcloud.util.MessageErrorUtils;
import com.wgcloud.util.PingUtil;
import com.wgcloud.util.ServerBackupUtil;
import com.wgcloud.util.UUIDUtil;
import com.wgcloud.util.msg.WarnOtherUtil;
import com.wgcloud.util.msg.WarnPools;
import com.wgcloud.util.staticvar.BatchData;
import com.wgcloud.util.staticvar.StaticKeys;
import java.io.File;
import java.sql.Timestamp;
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
public class DceInfoService {
    private static final Logger logger = LoggerFactory.getLogger(DceInfoService.class);
    @Autowired
    private DceInfoMapper dceInfoMapper;
    @Resource
    private HostGroupService hostGroupService;
    @Autowired
    private LogInfoService logInfoService;
    @Autowired
    private MessageErrorUtils messageErrorUtils;

    public PageInfo selectByParams(Map<String, Object> params, int currPage, int pageSize) throws Exception {
        PageHelper.startPage((int)currPage, (int)pageSize);
        List<DceInfo> list = this.dceInfoMapper.selectByParams(params);
        PageInfo pageInfo = new PageInfo(list);
        return pageInfo;
    }

    public void save(DceInfo dceInfo, HttpServletRequest request) throws Exception {
        dceInfo.setId(UUIDUtil.getUUID());
        dceInfo.setResTimes(1);
        Date nowDate = new Date();
        dceInfo.setCreateTime(nowDate);
        if (!StringUtils.isEmpty((CharSequence)dceInfo.getHostname())) {
            dceInfo.setHostname(dceInfo.getHostname().trim());
        }
        this.dceInfoMapper.save(dceInfo);
        this.addExtDataForm(dceInfo, request, nowDate);
    }

    private void addExtDataForm(DceInfo dceInfo, HttpServletRequest request, Date nowDate) throws Exception {
        String dataFromIndex = request.getParameter("dataFromIndex");
        if (!StringUtils.isEmpty((CharSequence)dataFromIndex)) {
            for (int i = 0; i <= Integer.valueOf(dataFromIndex); ++i) {
                String hostname = request.getParameter("hostname_" + i);
                String remark = request.getParameter("remark_" + i);
                String orderNum = request.getParameter("orderNum_" + i);
                if (StringUtils.isEmpty((CharSequence)hostname)) continue;
                DceInfo dceInfoExt = new DceInfo();
                BeanUtil.copyProperties((Object)dceInfo, (Object)dceInfoExt, (boolean)true);
                dceInfoExt.setId(UUIDUtil.getUUID());
                dceInfoExt.setCreateTime(nowDate);
                dceInfoExt.setHostname(hostname);
                dceInfoExt.setRemark(remark);
                if (!StringUtils.isEmpty((CharSequence)orderNum)) {
                    dceInfoExt.setOrderNum(Integer.valueOf(orderNum));
                }
                this.dceInfoMapper.save(dceInfoExt);
                this.saveLog(request, "\u6dfb\u52a0", dceInfoExt);
            }
        }
    }

    @Transactional
    public void saveRecord(List<DceInfo> recordList) throws Exception {
        if (recordList.size() < 1) {
            return;
        }
        Timestamp now = DateUtil.getNowTime();
        for (DceInfo as : recordList) {
            as.setResTimes(1);
            as.setId(UUIDUtil.getUUID());
            as.setCreateTime(now);
        }
        this.dceInfoMapper.insertList(recordList);
    }

    public int countByParams(Map<String, Object> params) throws Exception {
        return this.dceInfoMapper.countByParams(params);
    }

    @Transactional
    public int deleteById(String[] id) throws Exception {
        return this.dceInfoMapper.deleteById(id);
    }

    public void updateById(DceInfo dceInfo) throws Exception {
        if (!StringUtils.isEmpty((CharSequence)dceInfo.getHostname())) {
            dceInfo.setHostname(dceInfo.getHostname().trim());
        }
        this.dceInfoMapper.updateById(dceInfo);
    }

    public DceInfo selectById(String id) throws Exception {
        return this.dceInfoMapper.selectById(id);
    }

    public int updateActive(Map<String, Object> params) throws Exception {
        return this.dceInfoMapper.updateActive(params);
    }

    public void addServerBackMark(List<DceInfo> list) throws Exception {
        for (DceInfo dceInfo : list) {
            if (ServerBackupUtil.isExistDceInfoId(dceInfo.getId())) {
                dceInfo.setServerBackupMark("1");
                continue;
            }
            dceInfo.setServerBackupMark("2");
        }
    }

    public void updateOrderNum(HttpServletRequest request) throws Exception {
        if (!StringUtils.isEmpty((CharSequence)request.getParameter("id"))) {
            String id = request.getParameter("id");
            String orderNum = request.getParameter("orderNum");
            DceInfo dceInfo = new DceInfo();
            dceInfo.setId(id);
            if (!StringUtils.isEmpty((CharSequence)orderNum)) {
                dceInfo.setOrderNum(Integer.valueOf(orderNum));
            }
            this.dceInfoMapper.updateOrderNum(dceInfo);
        }
    }

    @Transactional
    public void updateRecord(List<DceInfo> recordList) throws Exception {
        this.dceInfoMapper.updateList(recordList);
    }

    public List<DceInfo> selectAllByParams(Map<String, Object> params) throws Exception {
        return this.dceInfoMapper.selectAllByParams(params);
    }

    public void saveGroupId(String ids, String[] groupIdsArr, HttpServletRequest request) throws Exception {
        List<HostGroup> hostGroupList = new ArrayList();
        if (null != groupIdsArr && groupIdsArr.length > 0) {
            HashMap<String, Object> params = new HashMap<String, Object>();
            params.put("groupIds", groupIdsArr);
            hostGroupList = this.hostGroupService.selectAllByParams(params, request);
        }
        if (!StringUtils.isEmpty((CharSequence)ids)) {
            DceInfo ho = null;
            for (String id : ids.split(",")) {
                ho = this.selectById(id);
                if (hostGroupList.size() > 0) {
                    ho.setGroupId(StringUtils.join((Object[])groupIdsArr, (String)","));
                } else {
                    ho.setGroupId("");
                }
                this.updateById(ho);
                this.logInfoService.save(HostUtil.getAccountByRequest(request).getAccount() + "\u8bbe\u7f6ePING\u6807\u7b7e\uff1a" + ho.getHostname(), "\u6807\u7b7e\uff1a" + this.hostGroupService.returnGroupNames(hostGroupList), "2");
            }
        }
    }

    public List<HostGroup> setGroupInList(List<DceInfo> recordList, Model model, HttpServletRequest request) throws Exception {
        HashMap<String, Object> params = new HashMap<String, Object>();
        List<HostGroup> hostGroupList = this.hostGroupService.selectAllByParams(params, request);
        model.addAttribute("hostGroupList", hostGroupList);
        HashMap<String, String> hostGroupMap = new HashMap<String, String>();
        for (HostGroup hostGroup : hostGroupList) {
            hostGroupMap.put(hostGroup.getId(), hostGroup.getGroupName());
        }
        for (DceInfo dceInfo1 : recordList) {
            if (StringUtils.isEmpty((CharSequence)dceInfo1.getGroupId())) continue;
            String groupNames = "";
            for (String groupId : dceInfo1.getGroupId().split(",")) {
                if (!hostGroupMap.containsKey(groupId)) continue;
                groupNames = groupNames + (String)hostGroupMap.get(groupId) + ",";
            }
            if (groupNames.endsWith(",")) {
                groupNames = groupNames.substring(0, groupNames.length() - 1);
            }
            dceInfo1.setGroupId(groupNames);
        }
        return hostGroupList;
    }

    public void saveLog(HttpServletRequest request, String action, DceInfo dceInfo) {
        if (null == dceInfo) {
            return;
        }
        this.logInfoService.save(HostUtil.getAccountByRequest(request).getAccount() + action + "\u7f51\u7edc\u8bbe\u5907PING\u76d1\u6d4b\u4fe1\u606f\uff1a" + dceInfo.getHostname(), "\u7f51\u7edc\u8bbe\u5907\uff1a" + dceInfo.getRemark(), "2");
    }

    public DceInfo testDceInfo(DceInfo dceInfo, Date date) {
        DceInfo dceInfoForUpdate = new DceInfo();
        dceInfoForUpdate.setId(dceInfo.getId());
        long resTimes = PingUtil.pingDceInfo(dceInfo, dceInfo.getHostname(), 1, 4);
        if (!StringUtils.isEmpty((CharSequence)dceInfo.getTestErrorMsg())) {
            this.messageErrorUtils.setErrorMsgHandler(dceInfo.getId(), dceInfo.getTestErrorMsg());
        }
        dceInfoForUpdate.setCreateTime(date);
        if (resTimes < 0L && dceInfo.getResTimes() < 0) {
            dceInfoForUpdate.setCreateTime(null);
        }
        dceInfoForUpdate.setResTimes(Integer.valueOf(resTimes + ""));
        dceInfo.setResTimes(dceInfoForUpdate.getResTimes());
        try {
            this.updateById(dceInfoForUpdate);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return dceInfoForUpdate;
    }

    public void taskThreadHandler(DceInfo dceInfo, Date date) {
        DceInfo dceInfoForUpdate = this.testDceInfo(dceInfo, date);
        if (dceInfoForUpdate.getResTimes() > 0) {
            DceState dceState = new DceState();
            dceState.setResTimes(dceInfoForUpdate.getResTimes());
            dceState.setDceId(dceInfoForUpdate.getId());
            dceState.setCreateTime(date);
            BatchData.DCE_STATE_LIST.add(dceState);
        }
        if (dceInfo.getResTimes() < 0) {
            WarnOtherUtil.sendDceInfo(dceInfo, true);
        } else if (!StringUtils.isEmpty((CharSequence)WarnPools.getWarnMark(dceInfo.getId()))) {
            WarnOtherUtil.sendDceInfo(dceInfo, false);
        }
    }

    public void importExcelPing() {
        try {
            XSSFRow row;
            String filePath = StaticKeys.JAR_PATH + "/template/ping.xlsx";
            if (!FileUtils.existsFile(filePath)) {
                return;
            }
            logger.info("\u5f00\u59cb\u4eceexcel\u6587\u4ef6\u5bfc\u5165ping\u6570\u636e-----" + filePath);
            HashMap<String, Object> map = new HashMap<String, Object>();
            List<DceInfo> dceInfoQueryList = this.selectAllByParams(map);
            XSSFWorkbook workbook = new XSSFWorkbook(filePath);
            XSSFSheet sheet = workbook.getSheetAt(0);
            int firstRowNum = sheet.getFirstRowNum();
            int lastRowNum = sheet.getLastRowNum();
            ArrayList<DceInfo> pingList = new ArrayList<DceInfo>();
            for (int rowNum = firstRowNum; rowNum <= lastRowNum && null != (row = sheet.getRow(rowNum)); ++rowNum) {
                if (null == row.getCell(0)) continue;
                String cellHostname = row.getCell(0).getStringCellValue();
                if (!StringUtils.isEmpty((CharSequence)cellHostname)) {
                    cellHostname = cellHostname.trim();
                }
                String cellRemark = "";
                if (null != row.getCell(1)) {
                    cellRemark = row.getCell(1).getStringCellValue();
                }
                if (!StringUtils.isEmpty((CharSequence)cellRemark)) {
                    cellRemark = cellRemark.trim();
                }
                if (StringUtils.isEmpty((CharSequence)cellHostname)) continue;
                boolean sign = false;
                for (DceInfo d : dceInfoQueryList) {
                    if (!d.getHostname().equals(cellHostname)) continue;
                    logger.info("ping\u76d1\u6d4b\u91cc\u5df2\u7ecf\u6709\u6b64ip\u4e86\uff0c\u4e0d\u5bfc\u5165-----" + cellHostname);
                    sign = true;
                    break;
                }
                if (sign) continue;
                DceInfo dceInfo = new DceInfo();
                dceInfo.setActive("1");
                dceInfo.setRemark(cellRemark);
                dceInfo.setHostname(cellHostname);
                dceInfo.setWarnLevel("ERROR");
                pingList.add(dceInfo);
            }
            this.saveRecord(pingList);
            File oldFile = new File(filePath);
            File newFile = new File(StaticKeys.JAR_PATH + "/template/ping_" + DateUtil.getCurrentDateTimeNoChar() + ".xlsx");
            oldFile.renameTo(newFile);
            logger.info("\u5bfc\u5165ping\u6570\u636e\u5b8c\u6210\uff0c\u4fee\u6539\u6587\u4ef6\u540d\u79f0\u4e3a-----" + newFile.getName());
        }
        catch (Exception e) {
            logger.error("\u7f51\u7edc\u8bbe\u5907PING\u5bfc\u5165excel\u9519\u8bef", (Throwable)e);
        }
    }

    public void updateToTargetAccount(Map<String, Object> params) throws Exception {
        this.dceInfoMapper.updateToTargetAccount(params);
    }
}

