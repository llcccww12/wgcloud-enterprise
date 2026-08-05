/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.controller;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.github.pagehelper.PageInfo;
import com.wgcloud.config.CommonConfig;
import com.wgcloud.dto.ShellCheckDto;
import com.wgcloud.entity.FileWarnInfo;
import com.wgcloud.entity.FileWarnState;
import com.wgcloud.entity.SystemInfo;
import com.wgcloud.service.ExcelExportService;
import com.wgcloud.service.FileWarnInfoService;
import com.wgcloud.service.FileWarnStateService;
import com.wgcloud.service.LogInfoService;
import com.wgcloud.service.ShellInfoService;
import com.wgcloud.service.SystemInfoService;
import com.wgcloud.util.FormatUtil;
import com.wgcloud.util.HostUtil;
import com.wgcloud.util.MessageErrorUtils;
import com.wgcloud.util.PageUtil;
import com.wgcloud.util.ResDataUtils;
import com.wgcloud.util.TokenUtils;
import com.wgcloud.util.license.LicenseUtil;
import com.wgcloud.util.staticvar.StaticKeys;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value={"/fileWarnInfo"})
public class FileWarnInfoController {
    private static final Logger logger = LoggerFactory.getLogger(FileWarnInfoController.class);
    @Resource
    private FileWarnInfoService fileWarnInfoService;
    @Resource
    private FileWarnStateService fileWarnStateService;
    @Resource
    private LogInfoService logInfoService;
    @Resource
    private SystemInfoService systemInfoService;
    @Resource
    private ExcelExportService excelExportService;
    @Resource
    private ShellInfoService shellInfoService;
    @Autowired
    private TokenUtils tokenUtils;
    @Autowired
    private CommonConfig commonConfig;
    @Autowired
    private MessageErrorUtils messageErrorUtils;

    @ResponseBody
    @RequestMapping(value={"agentList"})
    public String agentList(@RequestBody String paramBean) {
        JSONObject agentJsonObject = (JSONObject)JSONUtil.parse((Object)paramBean);
        if (!this.tokenUtils.checkAgentToken(agentJsonObject)) {
            logger.error("Token is error");
            return ResDataUtils.resetErrorJson("Token is error");
        }
        if (!this.tokenUtils.checkAllowOpenData(agentJsonObject)) {
            logger.error("The module needs to professional version. Please contact us at www.wgstart.com");
            return ResDataUtils.resetErrorJson("The module needs to professional version. Please contact us at www.wgstart.com");
        }
        HashMap<String, Object> params = new HashMap<String, Object>();
        if (null == agentJsonObject.get("hostname") || StringUtils.isEmpty((CharSequence)agentJsonObject.get("hostname").toString())) {
            return "";
        }
        params.put("hostname", agentJsonObject.get("hostname").toString());
        try {
            params.put("active", "1");
            List<FileWarnInfo> fileWarnInfoList = this.fileWarnInfoService.selectAllByParams(params);
            ShellCheckDto shellCheckDto = this.shellInfoService.getShellCheckDto(agentJsonObject.get("hostname").toString());
            String cmdSplitChar = shellCheckDto.getCmdSplitChar();
            String blockKey = "";
            ArrayList<FileWarnInfo> fileWarnListResult = new ArrayList<FileWarnInfo>();
            for (FileWarnInfo fileWarnInfo : fileWarnInfoList) {
                if (StringUtils.isEmpty((CharSequence)fileWarnInfo.getCustomShell())) {
                    fileWarnInfo.setCustomShell("");
                }
                if (!StringUtils.isEmpty((CharSequence)(blockKey = FormatUtil.haveBlockDanger(fileWarnInfo.getCustomShell(), this.commonConfig.getShellToRunBlock())))) {
                    logger.error(fileWarnInfo.getCustomShell() + "\u65e5\u5fd7\u76d1\u63a7\u5904\u7406\u6307\u4ee4\u542b\u6709\u654f\u611f\u5b57\u7b26" + blockKey + "\uff0c\u4e0d\u8fdb\u884c\u4e0b\u53d1");
                    continue;
                }
                fileWarnInfo.setCustomShell(fileWarnInfo.getCustomShell().replaceAll("\\r\\n", cmdSplitChar));
                fileWarnListResult.add(fileWarnInfo);
            }
            return ResDataUtils.resetSuccessJson(fileWarnListResult);
        }
        catch (Exception e) {
            logger.error("agent\u83b7\u53d6\u65e5\u5fd7\u76d1\u63a7\u4fe1\u606f\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("agent\u83b7\u53d6\u65e5\u5fd7\u76d1\u63a7\u4fe1\u606f\u9519\u8bef", e.toString(), "2");
            return ResDataUtils.resetErrorJson(e.toString());
        }
    }

    @ResponseBody
    @RequestMapping(value={"agentStateList"})
    public String agentStateList(@RequestBody String paramBean) {
        JSONObject agentJsonObject = (JSONObject)JSONUtil.parse((Object)paramBean);
        String checkResult = this.tokenUtils.preOpenDataAPICheck(agentJsonObject);
        if (!StringUtils.isEmpty((CharSequence)checkResult)) {
            return checkResult;
        }
        HashMap<String, Object> params = new HashMap<String, Object>();
        if (null == agentJsonObject.get("fileWarnId") || StringUtils.isEmpty((CharSequence)agentJsonObject.get("fileWarnId").toString())) {
            return ResDataUtils.resetErrorJson("Missing require parameters");
        }
        params.put("fileWarnId", agentJsonObject.get("fileWarnId").toString());
        try {
            PageInfo pageInfo = this.fileWarnStateService.selectByParams(params, agentJsonObject.getInt("page"), agentJsonObject.getInt("pageSize"));
            JSONObject pageJson = new JSONObject();
            pageJson.set("total", (Object)pageInfo.getTotal());
            pageJson.set("pages", (Object)pageInfo.getPages());
            pageJson.set("page", (Object)agentJsonObject.getInt("page"));
            pageJson.set("pageSize", (Object)agentJsonObject.getInt("pageSize"));
            pageJson.set("list", (Object)pageInfo.getList());
            return ResDataUtils.resetSuccessJson(pageJson);
        }
        catch (Exception e) {
            logger.error("agent\u83b7\u53d6\u65e5\u5fd7\u76d1\u63a7\u72b6\u6001\u4fe1\u606f\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("agent\u83b7\u53d6\u65e5\u5fd7\u76d1\u63a7\u72b6\u6001\u4fe1\u606f\u9519\u8bef", e.toString(), "2");
            return ResDataUtils.resetErrorJson(e.toString());
        }
    }

    @RequestMapping(value={"list"})
    public String fileWarnInfoList(FileWarnInfo fileWarnInfo, Model model, HttpServletRequest request) {
        HashMap<String, Object> params = new HashMap<String, Object>();
        try {
            LicenseUtil.maxLicense_10(model, request, fileWarnInfo);
            StringBuffer url = new StringBuffer();
            String hostname = null;
            if (!StringUtils.isEmpty((CharSequence)fileWarnInfo.getHostname())) {
                hostname = fileWarnInfo.getHostname();
                params.put("hostname", hostname.trim());
                url.append("&hostname=").append(hostname);
            }
            if (!StringUtils.isEmpty((CharSequence)fileWarnInfo.getAccount())) {
                params.put("account", fileWarnInfo.getAccount());
                url.append("&account=").append(fileWarnInfo.getAccount());
            }
            if (!StringUtils.isEmpty((CharSequence)fileWarnInfo.getActive())) {
                params.put("active", fileWarnInfo.getActive());
                url.append("&active=").append(fileWarnInfo.getActive());
            }
            if (!StringUtils.isEmpty((CharSequence)fileWarnInfo.getOrderBy())) {
                params.put("orderBy", fileWarnInfo.getOrderBy());
                params.put("orderType", fileWarnInfo.getOrderType());
                url.append("&orderBy=").append(fileWarnInfo.getOrderBy());
                url.append("&orderType=").append(fileWarnInfo.getOrderType());
            }
            HostUtil.addAccountquery(request, params);
            PageInfo pageInfo = this.fileWarnInfoService.selectByParams(params, fileWarnInfo.getPage(), fileWarnInfo.getPageSize());
            for (FileWarnInfo fileWarnInfo1 : (java.util.List<FileWarnInfo>) pageInfo.getList()) {
                if ("true".equals(this.commonConfig.getUserInfoManage())) {
                    fileWarnInfo1.setAccount(HostUtil.getAccount(fileWarnInfo1.getHostname()));
                }
                fileWarnInfo1.setHostState(HostUtil.getHostState(fileWarnInfo1.getHostname()));
                fileWarnInfo1.setHostname(fileWarnInfo1.getHostname() + HostUtil.addRemark(fileWarnInfo1.getHostname()));
                if (StringUtils.isEmpty((CharSequence)fileWarnInfo1.getFileSize())) continue;
                String fileFormatSize = FormatUtil.bytesFormatUnit(fileWarnInfo1.getFileSize(), "byte");
                fileWarnInfo1.setFileSize(fileFormatSize);
            }
            HostUtil.addAccountListModel(model);
            PageUtil.initPageNumber(pageInfo, model);
            model.addAttribute("pageUrl", (Object)("/fileWarnInfo/list?1=1" + url.toString()));
            model.addAttribute("page", (Object)pageInfo);
            model.addAttribute("fileWarnInfo", (Object)fileWarnInfo);
        }
        catch (Exception e) {
            logger.error("\u67e5\u8be2\u65e5\u5fd7\u76d1\u63a7\u4fe1\u606f\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("\u67e5\u8be2\u65e5\u5fd7\u76d1\u63a7\u4fe1\u606f\u9519\u8bef", e.toString(), "2");
        }
        return "file/list";
    }

    @RequestMapping(value={"save"})
    public String saveFileWarnInfo(FileWarnInfo fileWarnInfo, Model model, HttpServletRequest request) {
        try {
            String blockKey = FormatUtil.haveBlockDanger(fileWarnInfo.getCustomShell(), this.commonConfig.getShellToRunBlock());
            if (!StringUtils.isEmpty((CharSequence)blockKey)) {
                model.addAttribute("fileWarnInfo", (Object)fileWarnInfo);
                model.addAttribute("msg", (Object)("\u81ea\u52a8\u5904\u7406\u6307\u4ee4\u542b\u6709\u654f\u611f\u5b57\u7b26" + blockKey + "\uff0c\u8bf7\u68c0\u67e5"));
                this.shellInfoService.getBlockStr(model);
                HashMap<String, Object> params = new HashMap<String, Object>();
                HostUtil.addAccountquery(request, params);
                List<SystemInfo> systemInfoList = this.systemInfoService.selectAllByParams(params);
                model.addAttribute("systemInfoList", systemInfoList);
                return "file/add";
            }
            if (StringUtils.isEmpty((CharSequence)fileWarnInfo.getId())) {
                fileWarnInfo.setWarnRows("0");
                fileWarnInfo.setFileSize("0");
                this.fileWarnInfoService.save(fileWarnInfo);
                this.fileWarnInfoService.saveLog(request, "\u6dfb\u52a0", fileWarnInfo);
            } else {
                this.fileWarnInfoService.updateById(fileWarnInfo);
                this.fileWarnInfoService.saveLog(request, "\u4fee\u6539", fileWarnInfo);
            }
        }
        catch (Exception e) {
            logger.error("\u4fdd\u5b58\u65e5\u5fd7\u76d1\u63a7\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("\u4fdd\u5b58\u65e5\u5fd7\u76d1\u63a7\u9519\u8bef", e.toString(), "2");
        }
        return "redirect:/fileWarnInfo/list";
    }

    @RequestMapping(value={"saveBatch"})
    public String saveBatchFileWarnInfo(FileWarnInfo fileWarnInfo, Model model, HttpServletRequest request) {
        try {
            String[] hostnames = request.getParameterValues("hostnames");
            if (null == hostnames || hostnames.length < 1) {
                return "redirect:/fileWarnInfo/list";
            }
            String blockKey = FormatUtil.haveBlockDanger(fileWarnInfo.getCustomShell(), this.commonConfig.getShellToRunBlock());
            if (!StringUtils.isEmpty((CharSequence)blockKey)) {
                model.addAttribute("fileWarnInfo", (Object)fileWarnInfo);
                model.addAttribute("selectedHosts", (Object)hostnames);
                List<SystemInfo> systemInfoList = this.systemInfoService.selectAllByParams(new HashMap<String, Object>());
                for (SystemInfo systemInfo : systemInfoList) {
                    for (String selectedHost : hostnames) {
                        if (!selectedHost.equals(systemInfo.getHostname())) continue;
                        systemInfo.setSelected("selected");
                    }
                }
                model.addAttribute("systemInfoList", systemInfoList);
                model.addAttribute("msg", (Object)("\u81ea\u52a8\u5904\u7406\u6307\u4ee4\u542b\u6709\u654f\u611f\u5b57\u7b26" + blockKey + "\uff0c\u8bf7\u68c0\u67e5"));
                this.shellInfoService.getBlockStr(model);
                return "file/addBatch";
            }
            for (String selectedHost : hostnames) {
                fileWarnInfo.setHostname(selectedHost);
                fileWarnInfo.setWarnRows("0");
                fileWarnInfo.setFileSize("0");
                this.fileWarnInfoService.save(fileWarnInfo);
            }
        }
        catch (Exception e) {
            logger.error("\u6279\u91cf\u4fdd\u5b58\u65e5\u5fd7\u76d1\u63a7\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("\u6279\u91cf\u4fdd\u5b58\u65e5\u5fd7\u76d1\u63a7\u9519\u8bef", e.toString(), "2");
        }
        return "redirect:/fileWarnInfo/list";
    }

    @RequestMapping(value={"edit"})
    public String edit(Model model, HttpServletRequest request) {
        String errorMsg = "\u6dfb\u52a0\u65e5\u5fd7\u76d1\u63a7";
        String id = request.getParameter("id");
        FileWarnInfo fileWarnInfo = new FileWarnInfo();
        try {
            this.shellInfoService.getBlockStr(model);
            HashMap<String, Object> paramsAccount = new HashMap<String, Object>();
            HostUtil.addAccountquery(request, paramsAccount);
            List<SystemInfo> systemInfoList = this.systemInfoService.selectAllByParams(paramsAccount);
            model.addAttribute("systemInfoList", systemInfoList);
            if (StringUtils.isEmpty((CharSequence)id)) {
                fileWarnInfo.setFileType("1");
                model.addAttribute("fileWarnInfo", (Object)fileWarnInfo);
                if (!this.isAddContinue()) {
                    return "redirect:/fileWarnInfo/list?liceFlage=1";
                }
                return "file/add";
            }
            fileWarnInfo = this.fileWarnInfoService.selectById(id);
            model.addAttribute("fileWarnInfo", (Object)fileWarnInfo);
        }
        catch (Exception e) {
            logger.error(errorMsg, (Throwable)e);
            this.logInfoService.save(errorMsg, e.toString(), "2");
        }
        return "file/add";
    }

    @RequestMapping(value={"updateActive"})
    public String updateActive(Model model, HttpServletRequest request) {
        String errorMsg = "\u6279\u91cf\u5f00\u59cb\u76d1\u63a7\u548c\u505c\u6b62\u76d1\u63a7\u9519\u8bef";
        try {
            if (!StringUtils.isEmpty((CharSequence)request.getParameter("id"))) {
                String[] ids = request.getParameter("id").split(",");
                HashMap<String, Object> params = new HashMap<String, Object>();
                params.put("ids", ids);
                String activeValue = request.getParameter("active");
                params.put("active", activeValue);
                this.fileWarnInfoService.updateActive(params);
            }
        }
        catch (Exception e) {
            logger.error(errorMsg, (Throwable)e);
            this.logInfoService.save(errorMsg, e.toString(), "2");
        }
        return "redirect:/fileWarnInfo/list";
    }

    @RequestMapping(value={"editBatch"})
    public String editBatch(Model model, HttpServletRequest request) {
        String errorMsg = "\u6279\u91cf\u6dfb\u52a0\u65e5\u5fd7\u76d1\u63a7";
        FileWarnInfo fileWarnInfo = new FileWarnInfo();
        try {
            if (!StaticKeys.LICENSE_STATE.equals("1")) {
                return "redirect:/fileWarnInfo/list?liceFlage=2";
            }
            this.shellInfoService.getBlockStr(model);
            List<SystemInfo> systemInfoList = this.systemInfoService.selectAllByParams(new HashMap<String, Object>());
            model.addAttribute("systemInfoList", systemInfoList);
            fileWarnInfo.setFileType("1");
            model.addAttribute("fileWarnInfo", (Object)fileWarnInfo);
        }
        catch (Exception e) {
            logger.error(errorMsg, (Throwable)e);
            this.logInfoService.save(errorMsg, e.toString(), "2");
        }
        return "file/addBatch";
    }

    @RequestMapping(value={"view"})
    public String view(Model model, HttpServletRequest request) {
        String errorMsg = "\u67e5\u770b\u65e5\u5fd7\u76d1\u63a7";
        String id = request.getParameter("id");
        FileWarnInfo fileWarnInfo = new FileWarnInfo();
        try {
            fileWarnInfo = this.fileWarnInfoService.selectById(id);
            fileWarnInfo.setHostname(fileWarnInfo.getHostname() + HostUtil.addRemark(fileWarnInfo.getHostname()));
            model.addAttribute("fileWarnInfo", (Object)fileWarnInfo);
            model.addAttribute("fileWarnErrorMsg", (Object)this.messageErrorUtils.viewErrorMsgHandler(id));
            this.messageErrorUtils.getCallBackMsgForShell(model, id, fileWarnInfo.getCustomShell());
        }
        catch (Exception e) {
            logger.error(errorMsg, (Throwable)e);
            this.logInfoService.save(errorMsg, e.toString(), "2");
        }
        return "file/view";
    }

    @RequestMapping(value={"stateList"})
    public String stateList(FileWarnState fileWarnState, Model model, HttpServletRequest request) {
        HashMap<String, Object> params = new HashMap<String, Object>();
        try {
            StringBuffer url = new StringBuffer();
            FileWarnInfo fileWarnInfo = null;
            if (!StringUtils.isEmpty((CharSequence)fileWarnState.getFileWarnId())) {
                fileWarnInfo = this.fileWarnInfoService.selectById(fileWarnState.getFileWarnId());
                params.put("fileWarnId", fileWarnState.getFileWarnId());
                url.append("&fileWarnId=").append(fileWarnState.getFileWarnId());
            }
            PageInfo pageInfo = this.fileWarnStateService.selectByParams(params, fileWarnState.getPage(), fileWarnState.getPageSize());
            PageUtil.initPageNumber(pageInfo, model);
            model.addAttribute("pageUrl", (Object)("/fileWarnInfo/stateList?1=1" + url.toString()));
            model.addAttribute("page", (Object)pageInfo);
            fileWarnInfo.setHostname(fileWarnInfo.getHostname() + HostUtil.addRemark(fileWarnInfo.getHostname()));
            model.addAttribute("fileWarnInfo", (Object)fileWarnInfo);
        }
        catch (Exception e) {
            logger.error("\u67e5\u8be2\u65e5\u5fd7\u76d1\u63a7\u4fe1\u606f\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("\u67e5\u8be2\u65e5\u5fd7\u76d1\u63a7\u4fe1\u606f\u9519\u8bef", e.toString(), "2");
        }
        return "file/stateList";
    }

    @RequestMapping(value={"chartExcel"})
    public void chartExcel(Model model, HttpServletRequest request, HttpServletResponse response) {
        String errorMsg = "\u65e5\u5fd7\u76d1\u63a7\u4fe1\u606f\u5bfc\u51faexcel\u9519\u8bef";
        String id = request.getParameter("id");
        try {
            if (!StaticKeys.LICENSE_STATE.equals("1")) {
                response.setContentType("text/html;charset=UTF-8");
                response.getOutputStream().write("The module needs to professional version. Please contact us at www.wgstart.com".getBytes());
                return;
            }
            HashMap<String, Object> params = new HashMap<String, Object>();
            if (StringUtils.isEmpty((CharSequence)id)) {
                response.setContentType("text/html;charset=UTF-8");
                response.getOutputStream().write("Missing require parameters".getBytes());
                return;
            }
            FileWarnInfo fileWarnInfo = this.fileWarnInfoService.selectById(id);
            params.put("fileWarnId", id);
            this.excelExportService.exportFileWarnStateExcel(fileWarnInfo, params, response);
        }
        catch (Exception e) {
            logger.error(errorMsg, (Throwable)e);
            this.logInfoService.save(errorMsg, e.toString(), "2");
        }
    }

    @RequestMapping(value={"stateView"})
    public String stateView(FileWarnState fileWarnState, Model model, HttpServletRequest request) {
        HashMap params = new HashMap();
        try {
            fileWarnState = this.fileWarnStateService.selectById(fileWarnState.getId());
            model.addAttribute("fileWarnState", (Object)fileWarnState);
            FileWarnInfo fileWarnInfo = this.fileWarnInfoService.selectById(fileWarnState.getFileWarnId());
            fileWarnInfo.setHostname(fileWarnInfo.getHostname() + HostUtil.addRemark(fileWarnInfo.getHostname()));
            model.addAttribute("fileWarnInfo", (Object)fileWarnInfo);
        }
        catch (Exception e) {
            logger.error("\u67e5\u8be2\u65e5\u5fd7\u76d1\u63a7\u4fe1\u606f\u8be6\u60c5\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("\u67e5\u8be2\u65e5\u5fd7\u76d1\u63a7\u4fe1\u606f\u8be6\u60c5\u9519\u8bef", e.toString(), "2");
        }
        return "file/stateView";
    }

    @RequestMapping(value={"del"})
    public String delete(Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        String errorMsg = "\u5220\u9664\u65e5\u5fd7\u76d1\u63a7\u4fe1\u606f\u9519\u8bef";
        FileWarnInfo FileWarnInfo2 = new FileWarnInfo();
        try {
            if (!StringUtils.isEmpty((CharSequence)request.getParameter("id"))) {
                String[] ids;
                for (String id : ids = request.getParameter("id").split(",")) {
                    FileWarnInfo2 = this.fileWarnInfoService.selectById(id);
                    this.logInfoService.save("\u5220\u9664\u65e5\u5fd7\u76d1\u63a7\uff1a" + FileWarnInfo2.getHostname(), "\u5220\u9664\u65e5\u5fd7\u76d1\u63a7\uff1a" + FileWarnInfo2.getHostname(), "2");
                }
                this.fileWarnInfoService.deleteById(ids);
            }
        }
        catch (Exception e) {
            logger.error(errorMsg, (Throwable)e);
            this.logInfoService.save(errorMsg, e.toString(), "2");
        }
        return "redirect:/fileWarnInfo/list";
    }

    private boolean isAddContinue() {
        try {
            HashMap<String, Object> params;
            int dbSize;
            if (!StaticKeys.LICENSE_STATE.equals("1") && (dbSize = this.fileWarnInfoService.countByParams(params = new HashMap<String, Object>()).intValue()) >= 10) {
                return false;
            }
        }
        catch (Exception e) {
            logger.error("isAddContinue error", (Throwable)e);
        }
        return true;
    }
}

