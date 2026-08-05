/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wgcloud.entity.Equipment;
import com.wgcloud.entity.HostGroup;
import com.wgcloud.mapper.EquipmentMapper;
import com.wgcloud.service.HostGroupService;
import com.wgcloud.service.LogInfoService;
import com.wgcloud.util.DateUtil;
import com.wgcloud.util.FileUtils;
import com.wgcloud.util.FormatUtil;
import com.wgcloud.util.HostUtil;
import com.wgcloud.util.UUIDUtil;
import com.wgcloud.util.staticvar.StaticKeys;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

@Service
public class EquipmentService {
    private static final Logger logger = LoggerFactory.getLogger(EquipmentService.class);
    @Autowired
    private EquipmentMapper equipmentMapper;
    @Autowired
    private LogInfoService logInfoService;
    @Resource
    private HostGroupService hostGroupService;

    public PageInfo selectByParams(Map<String, Object> params, int currPage, int pageSize) throws Exception {
        PageHelper.startPage((int)currPage, (int)pageSize);
        List<Equipment> list = this.equipmentMapper.selectByParams(params);
        PageInfo pageInfo = new PageInfo(list);
        return pageInfo;
    }

    public void save(Equipment equipment) throws Exception {
        equipment.setId(UUIDUtil.getUUID());
        equipment.setCreateTime(new Date());
        this.equipmentMapper.save(equipment);
    }

    @Transactional
    public int deleteById(String[] id) throws Exception {
        return this.equipmentMapper.deleteById(id);
    }

    public void updateById(Equipment equipment) throws Exception {
        this.equipmentMapper.updateById(equipment);
    }

    public Equipment selectById(String id) throws Exception {
        return this.equipmentMapper.selectById(id);
    }

    public int countByParams(Map<String, Object> params) throws Exception {
        return this.equipmentMapper.countByParams(params);
    }

    public List<HostGroup> setGroupInList(List<Equipment> recordList, Model model, HttpServletRequest request) throws Exception {
        HashMap<String, Object> params = new HashMap<String, Object>();
        List<HostGroup> hostGroupList = this.hostGroupService.selectAllByParams(params, request);
        model.addAttribute("hostGroupList", hostGroupList);
        HashMap<String, String> hostGroupMap = new HashMap<String, String>();
        for (HostGroup hostGroup : hostGroupList) {
            hostGroupMap.put(hostGroup.getId(), hostGroup.getGroupName());
        }
        for (Equipment appInfo : recordList) {
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
            Equipment ho = null;
            for (String id : ids.split(",")) {
                ho = this.selectById(id);
                if (hostGroupList.size() > 0) {
                    ho.setGroupId(StringUtils.join((Object[])groupIdsArr, (String)","));
                } else {
                    ho.setGroupId("");
                }
                this.updateById(ho);
                this.logInfoService.save(HostUtil.getAccountByRequest(request).getAccount() + "\u8bbe\u7f6e\u8d44\u4ea7\u6807\u7b7e\uff1a" + ho.getName(), "\u6807\u7b7e\uff1a" + this.hostGroupService.returnGroupNames(hostGroupList), "2");
            }
        }
    }

    public void updateToTargetAccount(Map<String, Object> params) throws Exception {
        this.equipmentMapper.updateToTargetAccount(params);
    }

    public void saveAttachFile(MultipartFile file, Equipment equipment) {
        try {
            String allowFileTypes = "jpg,jpeg,png,zip,log,tar.gz,doc,docx,ppt,pptx,xls,xlsx,pdf,txt,html";
            if (file.isEmpty()) {
                logger.error("\u4e0a\u4f20\u8d44\u4ea7\u8bbe\u5907\u9644\u4ef6\u4e3a\u7a7a");
                return;
            }
            String fileSize = FormatUtil.bytesFormatUnit(file.getBytes().length + "", "byte");
            String sourceFileName = file.getOriginalFilename();
            if (StringUtils.isEmpty((CharSequence)sourceFileName)) {
                return;
            }
            String fileType = sourceFileName.substring(sourceFileName.lastIndexOf(".") + 1);
            if (!allowFileTypes.contains(fileType)) {
                logger.error("\u4e0d\u5141\u8bb8\u4e0a\u4f20\u7684\u8d44\u4ea7\u8bbe\u5907\u9644\u4ef6\u7c7b\u578b:" + sourceFileName);
                return;
            }
            String day = DateUtil.getCurrentDate().replace("-", "");
            String saveFolderUploadFile = StaticKeys.JAR_PATH + "/uploadFile";
            FileUtils.existsFolder(saveFolderUploadFile);
            String saveFolder = StaticKeys.JAR_PATH + "/uploadFile/" + day;
            FileUtils.existsFolder(saveFolder);
            String newFileName = DateUtil.getCurrentDateTimeNoChar() + FormatUtil.getRandInt() + "." + fileType;
            String newFilePath = saveFolder + "/" + newFileName;
            Path path = Paths.get(newFilePath, new String[0]);
            file.transferTo(path);
            equipment.setFileName(sourceFileName);
            equipment.setFilePath("/uploadFile/" + day + "/" + newFileName);
            equipment.setFileSize(fileSize);
        }
        catch (Exception e) {
            logger.error("\u8d44\u4ea7\u8bbe\u5907\u4e0a\u4f20\u6587\u4ef6\u9519\u8bef", (Throwable)e);
        }
    }

    public void saveLog(HttpServletRequest request, String action, Equipment equipment) {
        if (null == equipment) {
            return;
        }
        this.logInfoService.save(HostUtil.getAccountByRequest(request).getAccount() + action + "\u8d44\u4ea7\u4fe1\u606f\uff1a" + equipment.getName(), "\u89c4\u683c\u8bf4\u660e\uff1a" + equipment.getXinghao(), "2");
    }

    public void importExcelEquipment() {
        try {
            XSSFRow row;
            String filePath = StaticKeys.JAR_PATH + "/template/equipment.xlsx";
            if (!FileUtils.existsFile(filePath)) {
                return;
            }
            logger.info("\u5f00\u59cb\u4eceexcel\u6587\u4ef6\u5bfc\u5165\u8d44\u4ea7\u8bbe\u5907\u6570\u636e-----" + filePath);
            XSSFWorkbook workbook = new XSSFWorkbook(filePath);
            XSSFSheet sheet = workbook.getSheetAt(0);
            int firstRowNum = sheet.getFirstRowNum();
            int lastRowNum = sheet.getLastRowNum();
            for (int rowNum = firstRowNum; rowNum <= lastRowNum && null != (row = sheet.getRow(rowNum)); ++rowNum) {
                if (null == row.getCell(0)) continue;
                String cellName = row.getCell(0).getStringCellValue();
                if (!StringUtils.isEmpty((CharSequence)cellName)) {
                    cellName = cellName.trim();
                }
                String cellCode = "";
                if (null != row.getCell(1)) {
                    cellCode = row.getCell(1).getStringCellValue();
                }
                if (!StringUtils.isEmpty((CharSequence)cellCode)) {
                    cellCode = cellCode.trim();
                }
                String cellXinghao = "";
                if (null != row.getCell(2)) {
                    cellXinghao = row.getCell(2).getStringCellValue();
                }
                if (!StringUtils.isEmpty((CharSequence)cellXinghao)) {
                    cellXinghao = cellXinghao.trim();
                }
                String cellDept = "";
                if (null != row.getCell(3)) {
                    cellDept = row.getCell(3).getStringCellValue();
                }
                if (!StringUtils.isEmpty((CharSequence)cellDept)) {
                    cellDept = cellDept.trim();
                }
                String cellPerson = "";
                if (null != row.getCell(4)) {
                    cellPerson = row.getCell(4).getStringCellValue();
                }
                if (!StringUtils.isEmpty((CharSequence)cellPerson)) {
                    cellPerson = cellPerson.trim();
                }
                String cellCaigouDate = "";
                if (null != row.getCell(5)) {
                    cellCaigouDate = row.getCell(5).getStringCellValue();
                }
                if (!StringUtils.isEmpty((CharSequence)cellCaigouDate)) {
                    cellCaigouDate = cellCaigouDate.trim();
                }
                String cellGongyingshang = "";
                if (null != row.getCell(6)) {
                    cellGongyingshang = row.getCell(6).getStringCellValue();
                }
                if (!StringUtils.isEmpty((CharSequence)cellGongyingshang)) {
                    cellGongyingshang = cellGongyingshang.trim();
                }
                String cellPrice = "0";
                if (null != row.getCell(7)) {
                    if (row.getCell(7).getCellType() == CellType.STRING) {
                        cellPrice = row.getCell(7).getStringCellValue();
                    }
                    if (row.getCell(7).getCellType() == CellType.NUMERIC) {
                        cellPrice = row.getCell(7).getNumericCellValue() + "";
                    }
                }
                if (!StringUtils.isEmpty((CharSequence)cellPrice)) {
                    cellPrice = cellPrice.trim();
                }
                String cellWeibaoDate = "";
                if (null != row.getCell(8)) {
                    cellWeibaoDate = row.getCell(8).getStringCellValue();
                }
                if (!StringUtils.isEmpty((CharSequence)cellWeibaoDate)) {
                    cellWeibaoDate = cellWeibaoDate.trim();
                }
                String cellRemark = "";
                if (null != row.getCell(9)) {
                    cellRemark = row.getCell(9).getStringCellValue();
                }
                if (!StringUtils.isEmpty((CharSequence)cellRemark)) {
                    cellRemark = cellRemark.trim();
                }
                if (StringUtils.isEmpty((CharSequence)cellName)) continue;
                Equipment equipment = new Equipment();
                equipment.setName(cellName);
                equipment.setCode(cellCode);
                equipment.setXinghao(cellXinghao);
                equipment.setDept(cellDept);
                equipment.setPerson(cellPerson);
                equipment.setCaigouDate(cellCaigouDate);
                equipment.setGongyingshang(cellGongyingshang);
                try {
                    equipment.setPrice(Double.valueOf(cellPrice));
                }
                catch (Exception e) {
                    logger.error("\u8d44\u4ea7\u8bbe\u5907\u5bfc\u5165excel\u9519\u8bef", (Throwable)e);
                }
                equipment.setWeibaoDate(cellWeibaoDate);
                equipment.setRemark(cellRemark);
                this.save(equipment);
            }
            File oldFile = new File(filePath);
            File newFile = new File(StaticKeys.JAR_PATH + "/template/equipment_" + DateUtil.getCurrentDateTimeNoChar() + ".xlsx");
            oldFile.renameTo(newFile);
            logger.info("\u5bfc\u5165\u8d44\u4ea7\u8bbe\u5907\u6570\u636e\u5b8c\u6210\uff0c\u4fee\u6539\u6587\u4ef6\u540d\u79f0\u4e3a-----" + newFile.getName());
        }
        catch (Exception e) {
            logger.error("\u8d44\u4ea7\u8bbe\u5907\u5bfc\u5165excel\u9519\u8bef", (Throwable)e);
        }
    }
}

