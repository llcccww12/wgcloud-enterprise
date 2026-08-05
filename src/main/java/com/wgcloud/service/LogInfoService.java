/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.service;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wgcloud.dto.llm.LogInfoExcelDto;
import com.wgcloud.entity.LogInfo;
import com.wgcloud.mapper.LogInfoMapper;
import com.wgcloud.util.DateUtil;
import com.wgcloud.util.UUIDUtil;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LogInfoService {
    private static final Logger logger = LoggerFactory.getLogger(LogInfoService.class);
    @Autowired
    private LogInfoMapper logInfoMapper;

    public PageInfo selectByParams(Map<String, Object> params, int currPage, int pageSize) throws Exception {
        PageHelper.startPage((int)currPage, (int)pageSize);
        List<LogInfo> list = this.logInfoMapper.selectByParams(params);
        PageInfo pageInfo = new PageInfo(list);
        return pageInfo;
    }

    public PageInfo selectByParamsNoContent(Map<String, Object> params, int currPage, int pageSize) throws Exception {
        PageHelper.startPage((int)currPage, (int)pageSize);
        List<LogInfo> list = this.logInfoMapper.selectByParamsNoContent(params);
        PageInfo pageInfo = new PageInfo(list);
        return pageInfo;
    }

    public void saveRecord(List<LogInfo> recordList) {
        try {
            if (recordList.size() < 1) {
                return;
            }
            Date date = new Date();
            for (LogInfo as : recordList) {
                as.setId(UUIDUtil.getUUID());
                as.setCreateTime(date);
            }
            this.logInfoMapper.insertList(recordList);
        }
        catch (Exception e) {
            logger.error("\u6279\u91cf\u4fdd\u5b58\u65e5\u5fd7\u4fe1\u606f\u5f02\u5e38", (Throwable)e);
        }
    }

    public void save(String hostname, String infoContent, String state) {
        LogInfo logInfo = new LogInfo();
        logInfo.setHostname(hostname);
        logInfo.setInfoContent(infoContent);
        logInfo.setState(state);
        logInfo.setId(UUIDUtil.getUUID());
        logInfo.setCreateTime(new Date());
        try {
            this.logInfoMapper.save(logInfo);
        }
        catch (Exception e) {
            logger.error("\u4fdd\u5b58\u65e5\u5fd7\u4fe1\u606f\u5f02\u5e38", (Throwable)e);
        }
    }

    public void warnQueryHandle(Object object, String warnQueryWd) {
        try {
            HashMap<String, Object> paramsLogInfo = new HashMap<String, Object>();
            paramsLogInfo.put("hostname", warnQueryWd);
            paramsLogInfo.put("hostnameNe", "\u5df2\u6062\u590d");
            Integer resultCount = this.countByParams(paramsLogInfo);
            Class<?> superClazz = object.getClass();
            Method warnCountSetMethod = superClazz.getDeclaredMethod("setWarnCount", Integer.class);
            Method warnQueryWdSetMethod = superClazz.getDeclaredMethod("setWarnQueryWd", String.class);
            warnCountSetMethod.invoke(object, resultCount);
            warnQueryWdSetMethod.invoke(object, warnQueryWd);
        }
        catch (Exception e) {
            logger.error("warnQueryHandle", (Throwable)e);
        }
    }

    public int countByParams(Map<String, Object> params) throws Exception {
        return this.logInfoMapper.countByParams(params);
    }

    public int deleteById(String[] id) throws Exception {
        return this.logInfoMapper.deleteById(id);
    }

    public LogInfo selectById(String id) throws Exception {
        return this.logInfoMapper.selectById(id);
    }

    public List<LogInfo> selectAllByParams(Map<String, Object> params) throws Exception {
        return this.logInfoMapper.selectAllByParams(params);
    }

    public int deleteByDate(Map<String, Object> map) throws Exception {
        return this.logInfoMapper.deleteByDate(map);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void exportListExcel(Map<String, Object> params, HttpServletResponse response) {
        ExcelWriter excelWriter = null;
        try {
            int totalCount = this.countByParams(params);
            if (totalCount <= 0) {
                return;
            }
            int pageSize = 10000;
            int totalPages = (totalCount + pageSize - 1) / pageSize;
            excelWriter = EasyExcel.write((OutputStream)response.getOutputStream(), LogInfoExcelDto.class).build();
            WriteSheet writeSheet = EasyExcel.writerSheet((String)"\u7cfb\u7edf\u65e5\u5fd7\u6570\u636e").build();
            String fileName = DateUtil.getCurrentDateTimeNoChar() + "_\u7cfb\u7edf\u65e5\u5fd7\u4fe1\u606f.xlsx";
            response.setContentType("application/vnd.ms-exce");
            response.setCharacterEncoding("utf-8");
            String encodedFileName = URLEncoder.encode(fileName, "UTF-8");
            response.addHeader("Content-Disposition", "filename=" + encodedFileName);
            for (int i = 1; i <= totalPages; ++i) {
                PageInfo logInfoList = this.selectByParams(params, i, pageSize);
                ArrayList<LogInfoExcelDto> excelList = new ArrayList<LogInfoExcelDto>();
                String typeStr = "";
                for (LogInfo logInfo : (java.util.List<LogInfo>) logInfoList.getList()) {
                    LogInfoExcelDto dto = new LogInfoExcelDto();
                    dto.setCreateTime(DateUtil.getDateTimeString(logInfo.getCreateTime()));
                    if (!StringUtils.isEmpty((CharSequence)logInfo.getInfoContent()) && logInfo.getInfoContent().length() > Short.MAX_VALUE) {
                        dto.setInfoContent(logInfo.getInfoContent().substring(0, Short.MAX_VALUE));
                    } else {
                        dto.setInfoContent(logInfo.getInfoContent());
                    }
                    typeStr = "1".equals(logInfo.getState()) ? "\u4e1a\u52a1\u544a\u8b66" : ("3".equals(logInfo.getState()) ? "\u4e1a\u52a1\u544a\u8b66\u6062\u590d" : ("4".equals(logInfo.getState()) ? "\u7b2c\u4e09\u65b9\u544a\u8b66" : "\u7cfb\u7edf\u64cd\u4f5c"));
                    dto.setState(typeStr);
                    dto.setHostname(logInfo.getHostname());
                    excelList.add(dto);
                }
                excelWriter.write(excelList, writeSheet);
            }
        }
        catch (Exception e) {
            logger.error("\u5bfc\u51fa\u7cfb\u7edf\u65e5\u5fd7\u4fe1\u606f\u5217\u8868excel\u9519\u8bef", (Throwable)e);
        }
        finally {
            if (excelWriter != null) {
                excelWriter.finish();
            }
        }
    }
}

