/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.controller;

import cn.hutool.json.JSONUtil;
import com.github.pagehelper.PageInfo;
import com.wgcloud.config.CommonConfig;
import com.wgcloud.config.LevelConfig;
import com.wgcloud.config.MailConfig;
import com.wgcloud.dto.MenuTreeNodeDto;
import com.wgcloud.entity.AccountInfo;
import com.wgcloud.entity.HostWarnDiy;
import com.wgcloud.entity.SystemInfo;
import com.wgcloud.service.AccountInfoService;
import com.wgcloud.service.ExcelExportService;
import com.wgcloud.service.HostWarnDiyService;
import com.wgcloud.service.LogInfoService;
import com.wgcloud.service.SystemInfoService;
import com.wgcloud.util.HostUtil;
import com.wgcloud.util.PageUtil;
import com.wgcloud.util.license.LicenseUtil;
import com.wgcloud.util.staticvar.StaticKeys;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value={"/hostWarnDiy"})
public class HostWarnDiyController {
    private static final Logger logger = LoggerFactory.getLogger(HostWarnDiyController.class);
    @Resource
    private HostWarnDiyService hostWarnDiyService;
    @Resource
    private AccountInfoService accountInfoService;
    @Resource
    private SystemInfoService systemInfoService;
    @Resource
    private LogInfoService logInfoService;
    @Resource
    private ExcelExportService excelExportService;
    @Autowired
    private CommonConfig commonConfig;
    @Autowired
    private MailConfig mailConfig;
    @Autowired
    private LevelConfig levelConfig;

    @RequestMapping(value={"list"})
    public String hostWarnDiyList(HostWarnDiy hostWarnDiy, Model model, HttpServletRequest request) {
        HashMap<String, Object> params = new HashMap<String, Object>();
        try {
            LicenseUtil.maxLicense_10(model, request, hostWarnDiy);
            StringBuffer url = new StringBuffer();
            String hostname = null;
            if (!StringUtils.isEmpty((CharSequence)hostWarnDiy.getHostname())) {
                hostname = hostWarnDiy.getHostname();
                params.put("hostname", hostname.trim());
                url.append("&hostname=").append(hostname);
            }
            if (!StringUtils.isEmpty((CharSequence)hostWarnDiy.getAccount())) {
                params.put("account", hostWarnDiy.getAccount());
                url.append("&account=").append(hostWarnDiy.getAccount());
            }
            if (!StringUtils.isEmpty((CharSequence)hostWarnDiy.getActive())) {
                params.put("active", hostWarnDiy.getActive());
                url.append("&active=").append(hostWarnDiy.getActive());
            }
            HostUtil.addAccountquery(request, params);
            PageInfo pageInfo = this.hostWarnDiyService.selectByParams(params, hostWarnDiy.getPage(), hostWarnDiy.getPageSize());
            for (HostWarnDiy hostWarnDiy1 : (java.util.List<HostWarnDiy>) pageInfo.getList()) {
                hostWarnDiy1.setHostname(hostWarnDiy1.getHostname() + HostUtil.addRemark(hostWarnDiy1.getHostname()));
            }
            PageUtil.initPageNumber(pageInfo, model);
            HostUtil.addAccountListModel(model);
            model.addAttribute("pageUrl", (Object)("/hostWarnDiy/list?1=1" + url.toString()));
            model.addAttribute("page", (Object)pageInfo);
            model.addAttribute("hostWarnDiy", (Object)hostWarnDiy);
            HashMap<String, Object> paramsAccount = new HashMap<String, Object>();
            HostUtil.addAccountquery(request, paramsAccount);
            List<SystemInfo> systemInfoList = this.systemInfoService.selectAllByParams(paramsAccount);
            List<SystemInfo> resultList = this.hostWarnDiyService.getNoAddHostWarnDiyList(systemInfoList, "");
            model.addAttribute("systemInfoList", resultList);
            List<MenuTreeNodeDto> menuTreeNodeDtoList = this.accountInfoService.initMenuTreeList();
            this.accountInfoService.setMenuTreeChecked(menuTreeNodeDtoList, StaticKeys.ADMIN_MENUDIS);
            model.addAttribute("menuTreeNodeDtoList", (Object)JSONUtil.parseArray(menuTreeNodeDtoList));
        }
        catch (Exception e) {
            logger.error("\u67e5\u8be2\u4e3b\u673a\u81ea\u5b9a\u4e49\u544a\u8b66\u4fe1\u606f\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("\u67e5\u8be2\u4e3b\u673a\u81ea\u5b9a\u4e49\u544a\u8b66\u4fe1\u606f\u9519\u8bef", e.toString(), "2");
        }
        return "hostWarnDiy/list";
    }

    @ResponseBody
    @RequestMapping(value={"checkRepeat"})
    public String checkRepeat(Model model, HttpServletRequest request) {
        try {
            HashMap<String, Object> params = new HashMap<String, Object>();
            String hostname = request.getParameter("hostname");
            if (StringUtils.isEmpty((CharSequence)hostname)) {
                return "0";
            }
            params.put("hostname", hostname.trim());
            List<HostWarnDiy> list = this.hostWarnDiyService.selectAllByParams(params);
            if (list.size() > 0) {
                return "1";
            }
            return "0";
        }
        catch (Exception e) {
            logger.error("\u67e5\u8be2\u8be5IP\u662f\u5426\u5df2\u7ecf\u8bbe\u7f6e\u8fc7\u81ea\u5b9a\u4e49\u544a\u8b66\u9519\u8bef", (Throwable)e);
            return "success";
        }
    }

    @RequestMapping(value={"save"})
    public String saveHostWarnDiy(HostWarnDiy hostWarnDiy, Model model, HttpServletRequest request) {
        try {
            if (StringUtils.isEmpty((CharSequence)hostWarnDiy.getId())) {
                AccountInfo accountInfo = HostUtil.getAccountByRequest(request);
                if (!"admin".equals(accountInfo.getRole())) {
                    hostWarnDiy.setAccount(accountInfo.getAccount());
                }
                this.hostWarnDiyService.save(hostWarnDiy);
                this.hostWarnDiyService.saveLog(request, "\u6dfb\u52a0", hostWarnDiy);
            } else {
                this.hostWarnDiyService.updateById(hostWarnDiy);
                this.hostWarnDiyService.saveLog(request, "\u4fee\u6539", hostWarnDiy);
            }
        }
        catch (Exception e) {
            logger.error("\u4fdd\u5b58\u81ea\u5b9a\u4e49\u544a\u8b66\u4fe1\u606f\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("\u4fdd\u5b58\u81ea\u5b9a\u4e49\u544a\u8b66\u4fe1\u606f\u9519\u8bef", e.toString(), "2");
        }
        return "redirect:/hostWarnDiy/list";
    }

    @RequestMapping(value={"saveBatch"})
    public String saveBatchHostWarnDiy(HostWarnDiy hostWarnDiy, Model model, HttpServletRequest request) {
        String errorMsg = "\u6279\u91cf\u4fdd\u5b58\u81ea\u5b9a\u4e49\u544a\u8b66\u4fe1\u606f\u9519\u8bef";
        try {
            String[] hostnames = request.getParameterValues("hostnames");
            if (null == hostnames || hostnames.length < 1) {
                return "redirect:/hostWarnDiy/list";
            }
            AccountInfo accountInfo = HostUtil.getAccountByRequest(request);
            for (String selectedHost : hostnames) {
                hostWarnDiy.setHostname(selectedHost);
                if (!"admin".equals(accountInfo.getRole())) {
                    hostWarnDiy.setAccount(accountInfo.getAccount());
                }
                this.hostWarnDiyService.save(hostWarnDiy);
                this.hostWarnDiyService.saveLog(request, "\u6dfb\u52a0", hostWarnDiy);
            }
        }
        catch (Exception e) {
            logger.error(errorMsg, (Throwable)e);
            this.logInfoService.save(errorMsg, e.toString(), "2");
        }
        return "redirect:/hostWarnDiy/list";
    }

    @RequestMapping(value={"edit"})
    public String edit(Model model, HttpServletRequest request) {
        String errorMsg = "\u6dfb\u52a0\u81ea\u5b9a\u4e49\u544a\u8b66\u4fe1\u606f";
        String id = request.getParameter("id");
        HostWarnDiy hostWarnDiy = new HostWarnDiy();
        try {
            HashMap<String, Object> params = new HashMap<String, Object>();
            HostUtil.addAccountquery(request, params);
            List<SystemInfo> systemInfoList = this.systemInfoService.selectAllByParams(params);
            if (StringUtils.isEmpty((CharSequence)id)) {
                if (!this.isAddContinue()) {
                    return "redirect:/hostWarnDiy/list?liceFlage=1";
                }
                this.hostWarnDiyService.initAddViewValue(hostWarnDiy);
                if (!StringUtils.isEmpty((CharSequence)request.getParameter("hostname"))) {
                    hostWarnDiy.setHostname(request.getParameter("hostname"));
                }
                model.addAttribute("hostWarnDiy", (Object)hostWarnDiy);
                List<SystemInfo> resultList = this.hostWarnDiyService.getNoAddHostWarnDiyList(systemInfoList, "");
                model.addAttribute("systemInfoList", resultList);
                return "hostWarnDiy/add";
            }
            hostWarnDiy = this.hostWarnDiyService.selectById(id);
            model.addAttribute("hostWarnDiy", (Object)hostWarnDiy);
            List<SystemInfo> resultList2 = this.hostWarnDiyService.getNoAddHostWarnDiyList(systemInfoList, hostWarnDiy.getHostname());
            model.addAttribute("systemInfoList", resultList2);
        }
        catch (Exception e) {
            logger.error(errorMsg, (Throwable)e);
            this.logInfoService.save(errorMsg, e.toString(), "2");
        }
        return "hostWarnDiy/add";
    }

    @RequestMapping(value={"editBatch"})
    public String editBatch(Model model, HttpServletRequest request) {
        String errorMsg = "\u6279\u91cf\u6dfb\u52a0\u81ea\u5b9a\u4e49\u544a\u8b66\u4fe1\u606f\u9519\u8bef";
        HostWarnDiy hostWarnDiy = new HostWarnDiy();
        try {
            if (!StaticKeys.LICENSE_STATE.equals("1")) {
                return "redirect:/hostWarnDiy/list?liceFlage=2";
            }
            HashMap<String, Object> params = new HashMap<String, Object>();
            HostUtil.addAccountquery(request, params);
            List<SystemInfo> systemInfoList = this.systemInfoService.selectAllByParams(params);
            List<SystemInfo> resultList = this.hostWarnDiyService.getNoAddHostWarnDiyList(systemInfoList, "");
            model.addAttribute("systemInfoList", resultList);
            this.hostWarnDiyService.initAddViewValue(hostWarnDiy);
            model.addAttribute("hostWarnDiy", (Object)hostWarnDiy);
        }
        catch (Exception e) {
            logger.error(errorMsg, (Throwable)e);
            this.logInfoService.save(errorMsg, e.toString(), "2");
        }
        return "hostWarnDiy/addBatch";
    }

    @RequestMapping(value={"view"})
    public String viewChart(Model model, HttpServletRequest request) {
        String errorMsg = "\u67e5\u770b\u81ea\u5b9a\u4e49\u544a\u8b66\u4fe1\u606f\u9519\u8bef";
        String id = request.getParameter("id");
        HostWarnDiy hostWarnDiy = new HostWarnDiy();
        try {
            hostWarnDiy = this.hostWarnDiyService.selectById(id);
            hostWarnDiy.setHostname(hostWarnDiy.getHostname() + HostUtil.addRemark(hostWarnDiy.getHostname()));
            model.addAttribute("hostWarnDiy", (Object)hostWarnDiy);
        }
        catch (Exception e) {
            logger.error(errorMsg, (Throwable)e);
            this.logInfoService.save(errorMsg, e.toString(), "2");
        }
        return "hostWarnDiy/view";
    }

    @ResponseBody
    @RequestMapping(value={"copyHostWarnDiy"})
    public String copyHostWarnDiy(Model model, HttpServletRequest request) {
        try {
            if (!this.isAddContinue()) {
                return "redirect:/hostWarnDiy/list?liceFlage=1";
            }
            String id = request.getParameter("idCopy");
            String hostname = request.getParameter("hostname");
            if (StringUtils.isEmpty((CharSequence)id) || StringUtils.isEmpty((CharSequence)hostname)) {
                return "";
            }
            HostWarnDiy hostWarnDiy = this.hostWarnDiyService.selectById(id);
            hostWarnDiy.setId(null);
            hostWarnDiy.setHostname(hostname);
            this.hostWarnDiyService.save(hostWarnDiy);
            this.hostWarnDiyService.saveLog(request, "\u6dfb\u52a0", hostWarnDiy);
        }
        catch (Exception e) {
            logger.error("\u590d\u5236\u81ea\u5b9a\u4e49\u544a\u8b66\u6570\u636e\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("\u590d\u5236\u81ea\u5b9a\u4e49\u544a\u8b66\u6570\u636e\u9519\u8bef", e.toString(), "2");
        }
        return "redirect:/hostWarnDiy/list";
    }

    @RequestMapping(value={"del"})
    public String delete(Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        String errorMsg = "\u5220\u9664\u81ea\u5b9a\u4e49\u544a\u8b66\u4fe1\u606f\u9519\u8bef";
        HostWarnDiy hostWarnDiy = new HostWarnDiy();
        try {
            if (!StringUtils.isEmpty((CharSequence)request.getParameter("id"))) {
                String[] ids;
                for (String id : ids = request.getParameter("id").split(",")) {
                    hostWarnDiy = this.hostWarnDiyService.selectById(id);
                    this.hostWarnDiyService.saveLog(request, "\u5220\u9664", hostWarnDiy);
                }
                this.hostWarnDiyService.deleteById(ids);
            }
        }
        catch (Exception e) {
            logger.error(errorMsg, (Throwable)e);
            this.logInfoService.save(errorMsg, e.toString(), "2");
        }
        return "redirect:/hostWarnDiy/list";
    }

    @RequestMapping(value={"updateActive"})
    public String updateActive(Model model, HttpServletRequest request) {
        String errorMsg = "\u6279\u91cf\u542f\u7528\u914d\u7f6e\u548c\u505c\u7528\u914d\u7f6e\u9519\u8bef";
        try {
            if (!StringUtils.isEmpty((CharSequence)request.getParameter("id"))) {
                String[] ids = request.getParameter("id").split(",");
                HashMap<String, Object> params = new HashMap<String, Object>();
                params.put("ids", ids);
                String activeValue = request.getParameter("active");
                params.put("active", activeValue);
                this.hostWarnDiyService.updateActive(params);
            }
        }
        catch (Exception e) {
            logger.error(errorMsg, (Throwable)e);
            this.logInfoService.save(errorMsg, e.toString(), "2");
        }
        return "redirect:/hostWarnDiy/list";
    }

    @RequestMapping(value={"hostListNoAdd"})
    public String hostListNoAdd(Model model, HttpServletRequest request) {
        try {
            HashMap<String, Object> params = new HashMap<String, Object>();
            HostUtil.addAccountquery(request, params);
            List<SystemInfo> systemInfoList = this.systemInfoService.selectAllByParams(params);
            List<SystemInfo> resultList = this.hostWarnDiyService.getNoAddHostWarnDiyList(systemInfoList, "");
            if ("true".equals(this.commonConfig.getHostGroup())) {
                this.systemInfoService.setGroupInList(resultList, model, request);
            }
            model.addAttribute("resultListSize", (Object)resultList.size());
            if (!StaticKeys.LICENSE_STATE.equals("1") && resultList.size() > 10) {
                resultList = resultList.subList(0, 10);
                model.addAttribute("licenseMsg", "\u4e2a\u4eba\u7248\u6700\u591a\u76d1\u63a710\u9879\uff0c\u8bf7\u70b9\u51fb\u9875\u9762\u5e95\u90e8\u7f51\u7ad9\uff0c\u8054\u7cfb\u6211\u4eec\u5347\u7ea7\u5230\u4e13\u4e1a\u7248");
            }
            model.addAttribute("resultList", resultList);
        }
        catch (Exception e) {
            logger.error("\u67e5\u8be2\u6ca1\u6709\u6dfb\u52a0\u81ea\u5b9a\u4e49\u544a\u8b66\u7684\u4e3b\u673a\u5217\u8868\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("\u67e5\u8be2\u6ca1\u6709\u6dfb\u52a0\u81ea\u5b9a\u4e49\u544a\u8b66\u7684\u4e3b\u673a\u5217\u8868\u9519\u8bef", e.toString(), "2");
        }
        return "hostWarnDiy/listNoAdd";
    }

    @RequestMapping(value={"viewServerFile"})
    public String viewServerFile(Model model, HttpServletRequest request) {
        String errorMsg = "\u67e5\u770bserver\u914d\u7f6e\u6587\u4ef6\u5185\u5bb9\u9519\u8bef";
        try {
            AccountInfo accountInfoSession = HostUtil.getAccountByRequest(request);
            if ("guest".equals(accountInfoSession.getRole())) {
                logger.error("\u53ea\u8bfb\u8d26\u53f7\u4e0d\u80fd\u67e5\u770bserver\u914d\u7f6e\u6587\u4ef6\u5185\u5bb9");
                return "error/500";
            }
            StringBuilder fileContent = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new FileReader(StaticKeys.JAR_PATH + "/config/application.yml"));){
                String line;
                while ((line = br.readLine()) != null) {
                    if (line.contains("driver-class-name: ")) {
                        fileContent.append("    driver-class-name: ******").append(System.lineSeparator());
                        continue;
                    }
                    if (line.contains("url: ")) {
                        fileContent.append("    url: ******").append(System.lineSeparator());
                        continue;
                    }
                    if (line.contains("username: ")) {
                        fileContent.append("    username: ******").append(System.lineSeparator());
                        continue;
                    }
                    if (line.contains("password: ")) {
                        fileContent.append("    password: ******").append(System.lineSeparator());
                        continue;
                    }
                    if (line.contains("account: ")) {
                        fileContent.append("  account: ******").append(System.lineSeparator());
                        continue;
                    }
                    if (line.contains("accountPwd: ")) {
                        fileContent.append("  accountPwd: ******").append(System.lineSeparator());
                        continue;
                    }
                    if (line.contains("accountPhone: ")) {
                        fileContent.append("  accountPhone: ******").append(System.lineSeparator());
                        continue;
                    }
                    if (line.contains("wgToken: ")) {
                        fileContent.append("  wgToken: ******").append(System.lineSeparator());
                        continue;
                    }
                    fileContent.append(line).append(System.lineSeparator());
                }
            }
            catch (IOException e) {
                logger.error(errorMsg, (Throwable)e);
            }
            model.addAttribute("serverFileContent", (Object)fileContent);
        }
        catch (Exception e) {
            logger.error(errorMsg, (Throwable)e);
            this.logInfoService.save(errorMsg, e.toString(), "2");
        }
        return "hostWarnDiy/viewServer";
    }

    private boolean isAddContinue() {
        try {
            HashMap<String, Object> params;
            int dbSize;
            if (!StaticKeys.LICENSE_STATE.equals("1") && (dbSize = this.hostWarnDiyService.countByParams(params = new HashMap<String, Object>())) >= 10) {
                return false;
            }
        }
        catch (Exception e) {
            logger.error("isAddContinue error", (Throwable)e);
        }
        return true;
    }
}

