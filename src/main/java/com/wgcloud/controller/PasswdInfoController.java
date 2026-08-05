/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.controller;

import com.github.pagehelper.PageInfo;
import com.wgcloud.config.CommonConfig;
import com.wgcloud.entity.AccountInfo;
import com.wgcloud.entity.PasswdInfo;
import com.wgcloud.service.AccountInfoService;
import com.wgcloud.service.ExcelExportService;
import com.wgcloud.service.HostGroupService;
import com.wgcloud.service.LogInfoService;
import com.wgcloud.service.PasswdInfoService;
import com.wgcloud.util.DESUtil;
import com.wgcloud.util.HostUtil;
import com.wgcloud.util.PageUtil;
import com.wgcloud.util.license.LicenseUtil;
import com.wgcloud.util.staticvar.StaticKeys;
import java.util.HashMap;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value={"/passwdInfo"})
public class PasswdInfoController {
    private static final Logger logger = LoggerFactory.getLogger(PasswdInfoController.class);
    @Resource
    private PasswdInfoService passwdInfoService;
    @Resource
    private AccountInfoService accountInfoService;
    @Resource
    private LogInfoService logInfoService;
    @Resource
    private ExcelExportService excelExportService;
    @Resource
    private HostGroupService hostGroupService;
    @Autowired
    private CommonConfig commonConfig;

    @RequestMapping(value={"list"})
    public String passwdInfoList(PasswdInfo passwdInfo, Model model, HttpServletRequest request) {
        HashMap<String, Object> params = new HashMap<String, Object>();
        try {
            LicenseUtil.maxLicense_10(model, request, passwdInfo);
            AccountInfo accountInfo = HostUtil.getAccountByRequest(request);
            if ("guest".equals(accountInfo.getRole())) {
                return "redirect:/common/error/guestError";
            }
            StringBuffer url = new StringBuffer();
            String hostname = null;
            if (!StringUtils.isEmpty((CharSequence)passwdInfo.getHostname())) {
                hostname = passwdInfo.getHostname();
                params.put("hostname", hostname.trim());
                url.append("&hostname=").append(hostname);
            }
            if (!StringUtils.isEmpty((CharSequence)passwdInfo.getAccount())) {
                params.put("account", passwdInfo.getAccount());
                url.append("&account=").append(passwdInfo.getAccount());
            }
            if (!StringUtils.isEmpty((CharSequence)passwdInfo.getGroupId())) {
                params.put("groupId", passwdInfo.getGroupId());
                url.append("&groupId=").append(passwdInfo.getGroupId());
            }
            if (!StringUtils.isEmpty((CharSequence)passwdInfo.getHostMark())) {
                params.put("hostMark", passwdInfo.getHostMark());
                url.append("&hostMark=").append(passwdInfo.getHostMark());
            }
            HostUtil.addAccountquery(request, params);
            PageInfo<PasswdInfo> pageInfo = this.passwdInfoService.selectByParams(params, passwdInfo.getPage(), passwdInfo.getPageSize());
            for (PasswdInfo passwdInfo1 : (java.util.List<PasswdInfo>) pageInfo.getList()) {
                passwdInfo1.setHostPasswd(DESUtil.decryptForServerDb(passwdInfo1.getHostPasswd()));
            }
            PageUtil.initPageNumber(pageInfo, model);
            if ("true".equals(this.commonConfig.getHostGroup())) {
                this.passwdInfoService.setGroupInList(pageInfo.getList(), model, request);
            }
            HostUtil.addAccountListModel(model);
            model.addAttribute("pageUrl", (Object)("/passwdInfo/list?1=1" + url.toString()));
            model.addAttribute("page", pageInfo);
            model.addAttribute("passwdInfo", (Object)passwdInfo);
        }
        catch (Exception e) {
            logger.error("\u67e5\u8be2\u8bbe\u5907\u8d26\u53f7\u4fe1\u606f\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("\u67e5\u8be2\u8bbe\u5907\u8d26\u53f7\u4fe1\u606f\u9519\u8bef", e.toString(), "2");
        }
        return "passwdInfo/list";
    }

    @RequestMapping(value={"save"})
    public String savePasswdInfo(PasswdInfo passwdInfo, Model model, HttpServletRequest request) {
        try {
            passwdInfo.setHostPasswd(DESUtil.encryptionForServerDb(passwdInfo.getHostPasswd()));
            if (StringUtils.isEmpty((CharSequence)passwdInfo.getId())) {
                AccountInfo accountInfo = HostUtil.getAccountByRequest(request);
                if (null != accountInfo && !"admin".equals(accountInfo.getRole())) {
                    passwdInfo.setAccount(accountInfo.getAccount());
                }
                this.passwdInfoService.save(passwdInfo);
                this.passwdInfoService.saveLog(request, "\u6dfb\u52a0", passwdInfo);
            } else {
                this.passwdInfoService.updateById(passwdInfo);
                this.passwdInfoService.saveLog(request, "\u4fee\u6539", passwdInfo);
            }
        }
        catch (Exception e) {
            logger.error("\u4fdd\u5b58\u8bbe\u5907\u8d26\u53f7\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("\u4fdd\u5b58\u8bbe\u5907\u8d26\u53f7\u9519\u8bef", e.toString(), "2");
        }
        return "redirect:/passwdInfo/list";
    }

    @RequestMapping(value={"edit"})
    public String edit(Model model, HttpServletRequest request) {
        String errorMsg = "\u6dfb\u52a0\u8bbe\u5907\u8d26\u53f7";
        String id = request.getParameter("id");
        PasswdInfo passwdInfo = new PasswdInfo();
        try {
            if (StringUtils.isEmpty((CharSequence)id)) {
                model.addAttribute("passwdInfo", (Object)passwdInfo);
                if (!this.isAddContinue()) {
                    return "redirect:/passwdInfo/list?liceFlage=1";
                }
                return "passwdInfo/add";
            }
            passwdInfo = this.passwdInfoService.selectById(id);
            passwdInfo.setHostPasswd(DESUtil.decryptForServerDb(passwdInfo.getHostPasswd()));
            model.addAttribute("passwdInfo", (Object)passwdInfo);
        }
        catch (Exception e) {
            logger.error(errorMsg, (Throwable)e);
            this.logInfoService.save(errorMsg, e.toString(), "2");
        }
        return "passwdInfo/add";
    }

    @ResponseBody
    @RequestMapping(value={"saveGroupId"})
    public String saveGroupId(Model model, HttpServletRequest request) {
        try {
            String ids = request.getParameter("ids");
            String[] groupIdsArr = request.getParameterValues("groupId");
            this.passwdInfoService.saveGroupId(ids, groupIdsArr, request);
        }
        catch (Exception e) {
            logger.error("\u4fdd\u5b58\u8bbe\u5907\u8d26\u53f7\u6807\u7b7e\u4fe1\u606f\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("\u4fdd\u5b58\u8bbe\u5907\u8d26\u53f7\u6807\u7b7e\u4fe1\u606f\u9519\u8bef", e.toString(), "2");
        }
        return "redirect:/passwdInfo/list";
    }

    @RequestMapping(value={"view"})
    public String viewChart(Model model, HttpServletRequest request) {
        String errorMsg = "\u67e5\u770b\u8bbe\u5907\u8d26\u53f7\u4fe1\u606f\u9519\u8bef";
        String id = request.getParameter("id");
        PasswdInfo passwdInfo = new PasswdInfo();
        try {
            passwdInfo = this.passwdInfoService.selectById(id);
            passwdInfo.setHostPasswd(DESUtil.decryptForServerDb(passwdInfo.getHostPasswd()));
            model.addAttribute("passwdInfo", (Object)passwdInfo);
            if ("true".equals(this.commonConfig.getHostGroup())) {
                passwdInfo.setGroupId(this.hostGroupService.returnGroupNames(passwdInfo.getGroupId()));
            }
        }
        catch (Exception e) {
            logger.error(errorMsg, (Throwable)e);
            this.logInfoService.save(errorMsg, e.toString(), "2");
        }
        return "passwdInfo/view";
    }

    @RequestMapping(value={"del"})
    public String delete(Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        String errorMsg = "\u5220\u9664\u8bbe\u5907\u8d26\u53f7\u4fe1\u606f\u9519\u8bef";
        PasswdInfo passwdInfo = new PasswdInfo();
        try {
            if (!StringUtils.isEmpty((CharSequence)request.getParameter("id"))) {
                String[] ids;
                for (String id : ids = request.getParameter("id").split(",")) {
                    passwdInfo = this.passwdInfoService.selectById(id);
                    this.passwdInfoService.saveLog(request, "\u5220\u9664", passwdInfo);
                }
                this.passwdInfoService.deleteById(ids);
            }
        }
        catch (Exception e) {
            logger.error(errorMsg, (Throwable)e);
            this.logInfoService.save(errorMsg, e.toString(), "2");
        }
        return "redirect:/passwdInfo/list";
    }

    @RequestMapping(value={"exportListExcel"})
    public void exportListExcel(PasswdInfo passwdInfo, Model model, HttpServletRequest request, HttpServletResponse response) {
        try {
            if (!StaticKeys.LICENSE_STATE.equals("1")) {
                response.setContentType("text/html;charset=UTF-8");
                response.getOutputStream().write("The module needs to professional version. Please contact us at www.wgstart.com".getBytes());
                return;
            }
            HashMap<String, Object> params = new HashMap<String, Object>();
            String hostname = null;
            if (!StringUtils.isEmpty((CharSequence)passwdInfo.getHostname())) {
                hostname = passwdInfo.getHostname();
                params.put("hostname", hostname.trim());
            }
            if (!StringUtils.isEmpty((CharSequence)passwdInfo.getAccount())) {
                params.put("account", passwdInfo.getAccount());
            }
            if (!StringUtils.isEmpty((CharSequence)passwdInfo.getGroupId())) {
                params.put("groupId", passwdInfo.getGroupId());
            }
            HostUtil.addAccountquery(request, params);
            PageInfo<PasswdInfo> pageInfo = this.passwdInfoService.selectByParams(params, 1, 20000);
            for (PasswdInfo passwdInfo1 : (java.util.List<PasswdInfo>) pageInfo.getList()) {
                passwdInfo1.setHostPasswd(DESUtil.decryptForServerDb(passwdInfo1.getHostPasswd()));
            }
            if ("true".equals(this.commonConfig.getHostGroup())) {
                this.passwdInfoService.setGroupInList(pageInfo.getList(), model, request);
            }
            this.excelExportService.exportPasswdInfoListExcel(pageInfo.getList(), response);
        }
        catch (Exception e) {
            logger.error("\u6240\u6709\u8bbe\u5907\u8d26\u53f7\u5217\u8868\u5bfc\u51faexcel\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("\u6240\u6709\u8bbe\u5907\u8d26\u53f7\u5217\u8868\u5bfc\u51faexcel\u9519\u8bef", e.toString(), "2");
        }
    }

    @RequestMapping(value={"copyPasswdInfo"})
    public String copyPasswdInfo(Model model, HttpServletRequest request) {
        String errorMsg = "\u6dfb\u52a0\u8bbe\u5907\u8d26\u53f7";
        String id = request.getParameter("id");
        try {
            if (StringUtils.isEmpty((CharSequence)id)) {
                return "redirect:/passwdInfo/list";
            }
            if (!this.isAddContinue()) {
                return "redirect:/passwdInfo/list?liceFlage=1";
            }
            PasswdInfo passwdInfo = this.passwdInfoService.selectById(id);
            passwdInfo.setId(null);
            passwdInfo.setHostname(passwdInfo.getHostname() + "_1");
            this.passwdInfoService.save(passwdInfo);
            this.passwdInfoService.saveLog(request, "\u6dfb\u52a0", passwdInfo);
        }
        catch (Exception e) {
            logger.error(errorMsg, (Throwable)e);
            this.logInfoService.save(errorMsg, e.toString(), "2");
        }
        return "redirect:/passwdInfo/list";
    }

    private boolean isAddContinue() {
        try {
            HashMap<String, Object> params;
            int dbSize;
            if (!StaticKeys.LICENSE_STATE.equals("1") && (dbSize = this.passwdInfoService.countByParams(params = new HashMap<String, Object>())) >= 10) {
                return false;
            }
        }
        catch (Exception e) {
            logger.error("isAddContinue error", (Throwable)e);
        }
        return true;
    }
}

