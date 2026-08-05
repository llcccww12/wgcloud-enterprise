/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONObject;
import com.wgcloud.config.CommonConfig;
import com.wgcloud.entity.ReportInfo;
import com.wgcloud.service.LargeModelService;
import com.wgcloud.service.LogInfoService;
import com.wgcloud.util.DateUtil;
import com.wgcloud.util.FileUtils;
import com.wgcloud.util.license.LicenseUtil;
import com.wgcloud.util.staticvar.StaticKeys;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value={"/largeModel"})
public class LargeModelController {
    private static final Logger logger = LoggerFactory.getLogger(LargeModelController.class);
    @Autowired
    private LargeModelService largeModelService;
    @Resource
    private LogInfoService logInfoService;
    @Autowired
    private CommonConfig commonConfig;

    @RequestMapping(value={"list"})
    public String llmFileList(ReportInfo reportInfo, Model model, HttpServletRequest request) {
        try {
            List<String> llmFileList = FileUtils.getFileList(StaticKeys.JAR_PATH + "/uploadFile/llm");
            if (CollectionUtil.isEmpty(llmFileList)) {
                llmFileList = new ArrayList<String>();
            }
            HashSet<String> dateStrSet = new HashSet<String>();
            for (String fileName : llmFileList) {
                String dateStr = fileName.split("_")[0];
                dateStrSet.add(dateStr);
            }
            ArrayList dateList = new ArrayList(dateStrSet);
            Collections.sort(dateList, Comparator.reverseOrder());
            ArrayList<JSONObject> resultList = new ArrayList<JSONObject>();
            for (String dateStr : (java.util.List<String>)dateList) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.set("dateStr", (Object)dateStr);
                jsonObject.set("diskData", (Object)("/uploadFile/llm/" + dateStr + "_\u5168\u91cf\u4e3b\u673a\u78c1\u76d8\u4f7f\u7528\u7387\u6570\u636e.xlsx"));
                if (!FileUtils.existsFile(StaticKeys.JAR_PATH + "/uploadFile/llm/" + dateStr + "_\u5168\u91cf\u4e3b\u673a\u76d1\u63a7\u6570\u636e\u5feb\u7167.xlsx")) continue;
                jsonObject.set("hostListSnapData", (Object)("/uploadFile/llm/" + dateStr + "_\u5168\u91cf\u4e3b\u673a\u76d1\u63a7\u6570\u636e\u5feb\u7167.xlsx"));
                jsonObject.set("pingData", (Object)("/uploadFile/llm/" + dateStr + "_\u5168\u91cfPING\u76d1\u63a7\u5feb\u7167.xlsx"));
                jsonObject.set("allProcData", (Object)("/uploadFile/llm/" + dateStr + "_\u5168\u91cf\u4e3b\u673a\u8fdb\u7a0b\u5feb\u7167.xlsx"));
                resultList.add(jsonObject);
            }
            if (!LicenseUtil.checkEnterpriseVersion()) {
                model.addAttribute("msg", "\u63d0\u793a\uff1a\u751f\u6210\u6240\u6709\u4e3b\u673a\u76d1\u63a7\u6570\u636eExcel\u6587\u4ef6\uff0c\u9700\u8981\u5347\u7ea7\u5230\u4f01\u4e1a\u7248 www.wgstart.com");
                if (CollectionUtil.isEmpty(resultList)) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.set("dateStr", (Object)DateUtil.getCurrentDate());
                    jsonObject.set("diskData", "/uploadFile/llm/_\u5168\u91cf\u4e3b\u673a\u78c1\u76d8\u4f7f\u7528\u7387\u6570\u636e.xlsx");
                    jsonObject.set("hostListSnapData", "/uploadFile/llm/_\u5168\u91cf\u4e3b\u673a\u76d1\u63a7\u6570\u636e\u5feb\u7167.xlsx");
                    jsonObject.set("pingData", "/uploadFile/llm/_\u5168\u91cfPING\u76d1\u63a7\u5feb\u7167.xlsx");
                    jsonObject.set("allProcData", "/uploadFile/llm/_\u5168\u91cf\u4e3b\u673a\u8fdb\u7a0b\u5feb\u7167.xlsx");
                    resultList.add(jsonObject);
                }
            }
            model.addAttribute("resultList", resultList);
        }
        catch (Exception e) {
            logger.error("\u67e5\u8be2\u5927\u6a21\u578b\u751f\u6210\u7684excel\u6587\u4ef6\u5217\u8868\u9519\u8bef", (Throwable)e);
        }
        return "llm/list";
    }

    @RequestMapping(value={"hostMacListExcel"})
    public void hostMacListExcel(Model model, HttpServletRequest request, HttpServletResponse response) {
        try {
            if (!StaticKeys.LICENSE_STATE.equals("1")) {
                response.setContentType("text/html;charset=UTF-8");
                response.getOutputStream().write("The module needs to professional version. Please contact us at www.wgstart.com".getBytes());
                return;
            }
            this.largeModelService.exportMacListExcel(response);
        }
        catch (Exception e) {
            logger.error("\u6240\u6709\u4e3b\u673a\u7684mac\u5730\u5740\u5bfc\u51faexcel\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("\u6240\u6709\u4e3b\u673a\u7684mac\u5730\u5740\u5bfc\u51faexcel\u9519\u8bef", e.toString(), "2");
        }
    }

    @RequestMapping(value={"zipDownload"})
    public void zipDownloadMultipleFiles(HttpServletRequest request, HttpServletResponse response) {
        try {
            if (!StaticKeys.LICENSE_STATE.equals("1")) {
                response.setContentType("text/html;charset=UTF-8");
                response.getOutputStream().write("The module needs to professional version. Please contact us at www.wgstart.com".getBytes());
                return;
            }
            if (StringUtils.isEmpty((CharSequence)request.getParameter("id"))) {
                response.setContentType("text/html;charset=UTF-8");
                response.getOutputStream().write("Missing require parameters".getBytes());
                return;
            }
            String[] dateStrArr = request.getParameter("id").split(",");
            ArrayList<String> filePaths = new ArrayList<String>();
            List<String> llmFileList = FileUtils.getFileList(StaticKeys.JAR_PATH + "/uploadFile/llm");
            if (!CollectionUtil.isEmpty(llmFileList)) {
                block20: for (String fileName : llmFileList) {
                    for (String dateStr : dateStrArr) {
                        if (!fileName.startsWith(dateStr)) continue;
                        filePaths.add(StaticKeys.JAR_PATH + "/uploadFile/llm/" + fileName);
                        continue block20;
                    }
                }
            }
            if (filePaths.isEmpty()) {
                response.setContentType("text/html;charset=UTF-8");
                response.getOutputStream().write("File is empty".getBytes());
                return;
            }
            String zipFileName = DateUtil.getCurrentDate() + "_LLM\u6587\u4ef6.zip";
            response.setContentType("application/zip");
            String encodedFileName = URLEncoder.encode(zipFileName, "UTF-8");
            response.addHeader("Content-Disposition", "filename=" + encodedFileName);
            try (ZipOutputStream zos = new ZipOutputStream((OutputStream)response.getOutputStream());){
                byte[] buffer = new byte[1024];
                for (String filePath : filePaths) {
                    File file = new File(filePath);
                    if (!file.exists() || !file.isFile()) continue;
                    ZipEntry zipEntry = new ZipEntry(file.getName());
                    zos.putNextEntry(zipEntry);
                    try (FileInputStream fis = new FileInputStream(file);){
                        int bytesRead;
                        while ((bytesRead = fis.read(buffer)) != -1) {
                            zos.write(buffer, 0, bytesRead);
                        }
                    }
                    zos.closeEntry();
                }
            }
        }
        catch (Exception e) {
            logger.error("\u4e0b\u8f7d\u538b\u7f29\u6587\u4ef6\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("\u4e0b\u8f7d\u538b\u7f29\u6587\u4ef6\u9519\u8bef", e.toString(), "2");
        }
    }

    @RequestMapping(value={"hostNetworkNameListExcel"})
    public void hostNetworkNameListExcel(Model model, HttpServletRequest request, HttpServletResponse response) {
        try {
            if (!StaticKeys.LICENSE_STATE.equals("1")) {
                response.setContentType("text/html;charset=UTF-8");
                response.getOutputStream().write("The module needs to professional version. Please contact us at www.wgstart.com".getBytes());
                return;
            }
            this.largeModelService.exportNetworkNameListExcel(response);
        }
        catch (Exception e) {
            logger.error("\u6240\u6709\u4e3b\u673a\u7684\u7f51\u5361\u4fe1\u606f\u5bfc\u51faexcel\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("\u6240\u6709\u4e3b\u673a\u7684\u7f51\u5361\u4fe1\u606f\u5bfc\u51faexcel\u9519\u8bef", e.toString(), "2");
        }
    }

    @RequestMapping(value={"hostCpuTemperListExcel"})
    public void hostCpuTemperListExcel(Model model, HttpServletRequest request, HttpServletResponse response) {
        try {
            if (!StaticKeys.LICENSE_STATE.equals("1")) {
                response.setContentType("text/html;charset=UTF-8");
                response.getOutputStream().write("The module needs to professional version. Please contact us at www.wgstart.com".getBytes());
                return;
            }
            this.largeModelService.exportCpuTemperListExcel(response);
        }
        catch (Exception e) {
            logger.error("\u6240\u6709\u4e3b\u673a\u7684cpu\u6e29\u5ea6\u4fe1\u606f\u5bfc\u51faexcel\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("\u6240\u6709\u4e3b\u673a\u7684cpu\u6e29\u5ea6\u4fe1\u606f\u5bfc\u51faexcel\u9519\u8bef", e.toString(), "2");
        }
    }

    @RequestMapping(value={"hostDiskPerListExcel"})
    public void hostDiskPerListExcel(Model model, HttpServletRequest request, HttpServletResponse response) {
        try {
            if (!StaticKeys.LICENSE_STATE.equals("1")) {
                response.setContentType("text/html;charset=UTF-8");
                response.getOutputStream().write("The module needs to professional version. Please contact us at www.wgstart.com".getBytes());
                return;
            }
            this.largeModelService.exportHostDiskPerExcel(response);
        }
        catch (Exception e) {
            logger.error("\u6240\u6709\u4e3b\u673a\u7684\u78c1\u76d8\u603b\u4f7f\u7528\u7387\u5bfc\u51faexcel\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("\u6240\u6709\u4e3b\u673a\u7684\u78c1\u76d8\u603b\u4f7f\u7528\u7387\u5bfc\u51faexcel\u9519\u8bef", e.toString(), "2");
        }
    }

    @RequestMapping(value={"hostDiskIoStateListExcel"})
    public void hostDiskIoStateListExcel(Model model, HttpServletRequest request, HttpServletResponse response) {
        try {
            if (!StaticKeys.LICENSE_STATE.equals("1")) {
                response.setContentType("text/html;charset=UTF-8");
                response.getOutputStream().write("The module needs to professional version. Please contact us at www.wgstart.com".getBytes());
                return;
            }
            this.largeModelService.exportHostDiskIoStateExcel(response);
        }
        catch (Exception e) {
            logger.error("\u6240\u6709\u4e3b\u673a\u7684\u78c1\u76d8io\u8bfb\u5199\u901f\u7387\u5bfc\u51faexcel\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("\u6240\u6709\u4e3b\u673a\u7684\u78c1\u76d8io\u8bfb\u5199\u901f\u7387\u5bfc\u51faexcel\u9519\u8bef", e.toString(), "2");
        }
    }
}

