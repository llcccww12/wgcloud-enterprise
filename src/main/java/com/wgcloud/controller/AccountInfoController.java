/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.controller;

import cn.hutool.json.JSONUtil;
import com.github.pagehelper.PageInfo;
import com.wgcloud.dto.MenuTreeNodeDto;
import com.wgcloud.entity.AccountInfo;
import com.wgcloud.entity.SystemInfo;
import com.wgcloud.service.AccountInfoService;
import com.wgcloud.service.LogInfoService;
import com.wgcloud.service.SystemInfoService;
import com.wgcloud.util.HostUtil;
import com.wgcloud.util.MD5Utils;
import com.wgcloud.util.PageUtil;
import com.wgcloud.util.staticvar.StaticKeys;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value={"/accountInfo"})
public class AccountInfoController {
    private static final Logger logger = LoggerFactory.getLogger(AccountInfoController.class);
    @Resource
    private AccountInfoService accountInfoService;
    @Resource
    private SystemInfoService systemInfoService;
    @Resource
    private LogInfoService logInfoService;

    private void testThread() {
        new Thread(() -> logger.info("\u542f\u52a8\u5b50\u7ebf\u7a0b\u6d4b\u8bd5")).start();
    }

    @RequestMapping(value={"list"})
    public String AccountInfoList(AccountInfo accountInfo, Model model, HttpServletRequest request) {
        String errorMsg = "\u67e5\u8be2\u6210\u5458\u8d26\u53f7\u4fe1\u606f\u9519\u8bef";
        if (!StaticKeys.LICENSE_STATE.equals("1")) {
            return "daping/error";
        }
        HashMap<String, Object> params = new HashMap<String, Object>();
        try {
            if (!this.isAdminRole(request)) {
                return "error/guestError";
            }
            PageInfo pageInfo = this.accountInfoService.selectByParams(params, accountInfo.getPage(), accountInfo.getPageSize());
            HashMap paramsLogInfo = new HashMap();
            for (AccountInfo accountInfo1 : (java.util.List<AccountInfo>) pageInfo.getList()) {
                HashMap<String, Object> paramsAccount = new HashMap<String, Object>();
                paramsAccount.put("account", accountInfo1.getAccount());
                Integer hostNum = this.systemInfoService.countByParams(paramsAccount);
                accountInfo1.setHostNum(hostNum);
            }
            PageUtil.initPageNumber(pageInfo, model);
            model.addAttribute("page", (Object)pageInfo);
            model.addAttribute("pageUrl", "/accountInfo/list?1=1");
            model.addAttribute("accountInfo", (Object)accountInfo);
            model.addAttribute("accountList", this.accountInfoService.selectAllByParams(new HashMap<String, Object>()));
            List<MenuTreeNodeDto> menuTreeNodeDtoList = this.accountInfoService.initMenuTreeList();
            this.accountInfoService.setMenuTreeChecked(menuTreeNodeDtoList, StaticKeys.ADMIN_MENUDIS);
            model.addAttribute("menuTreeNodeDtoList", (Object)JSONUtil.parseArray(menuTreeNodeDtoList));
        }
        catch (Exception e) {
            logger.error(errorMsg, (Throwable)e);
            this.logInfoService.save(errorMsg, e.toString(), "2");
        }
        return "accountInfo/list";
    }

    @RequestMapping(value={"editPasswd"})
    public String editPasswd(Model model, HttpServletRequest request) {
        String errorMsg = "\u4fee\u6539\u6210\u5458\u5bc6\u7801\u9519\u8bef";
        if (!StaticKeys.LICENSE_STATE.equals("1")) {
            return "error/guestError";
        }
        if (!this.isAdminRole(request)) {
            return "error/guestError";
        }
        String id = request.getParameter("id");
        AccountInfo accountInfo = new AccountInfo();
        try {
            accountInfo = this.accountInfoService.selectById(id);
            model.addAttribute("accountInfo", (Object)accountInfo);
        }
        catch (Exception e) {
            logger.error(errorMsg, (Throwable)e);
            this.logInfoService.save(errorMsg, e.toString(), "2");
        }
        return "accountInfo/passwd";
    }

    @RequestMapping(value={"edit"})
    public String edit(Model model, HttpServletRequest request) {
        String errorMsg = "\u6dfb\u52a0\u6210\u5458\u8d26\u53f7\u4fe1\u606f\u9519\u8bef";
        if (!StaticKeys.LICENSE_STATE.equals("1")) {
            return "error/guestError";
        }
        String id = request.getParameter("id");
        AccountInfo accountInfo = new AccountInfo();
        try {
            HashMap<String, Object> paramsAccount = new HashMap<String, Object>();
            List<SystemInfo> systemInfoList = this.systemInfoService.selectAllByParams(paramsAccount);
            ArrayList<SystemInfo> systemInfoListResult = new ArrayList<SystemInfo>();
            List<MenuTreeNodeDto> menuTreeNodeDtoList = this.accountInfoService.initMenuTreeList();
            if (StringUtils.isEmpty((CharSequence)id)) {
                model.addAttribute("menuTreeNodeDtoList", (Object)JSONUtil.parseArray(menuTreeNodeDtoList));
                model.addAttribute("accountInfo", (Object)accountInfo);
                for (SystemInfo systemInfoTmp : systemInfoList) {
                    if (!StringUtils.isEmpty((CharSequence)systemInfoTmp.getAccount())) continue;
                    systemInfoListResult.add(systemInfoTmp);
                }
                model.addAttribute("systemInfoList", systemInfoListResult);
                return "accountInfo/add";
            }
            accountInfo = this.accountInfoService.selectById(id);
            this.accountInfoService.setMenuTreeChecked(menuTreeNodeDtoList, accountInfo.getMenuIds());
            model.addAttribute("menuTreeNodeDtoList", (Object)JSONUtil.parseArray(menuTreeNodeDtoList));
            for (SystemInfo systemInfoTmp : systemInfoList) {
                if (StringUtils.isEmpty((CharSequence)systemInfoTmp.getAccount())) {
                    systemInfoListResult.add(systemInfoTmp);
                }
                if (StringUtils.isEmpty((CharSequence)accountInfo.getAccount()) || !accountInfo.getAccount().equals(systemInfoTmp.getAccount())) continue;
                systemInfoListResult.add(systemInfoTmp);
            }
            for (SystemInfo systemInfo : systemInfoListResult) {
                if (!accountInfo.getAccount().equals(systemInfo.getAccount())) continue;
                systemInfo.setSelected("selected");
            }
            model.addAttribute("systemInfoList", systemInfoListResult);
            model.addAttribute("accountInfo", (Object)accountInfo);
        }
        catch (Exception e) {
            logger.error(errorMsg, (Throwable)e);
            this.logInfoService.save(errorMsg, e.toString(), "2");
        }
        return "accountInfo/add";
    }

    @RequestMapping(value={"save"})
    public String saveAccountInfo(AccountInfo accountInfo, Model model, HttpServletRequest request) {
        if (!StaticKeys.LICENSE_STATE.equals("1")) {
            return "error/guestError";
        }
        if (!this.isAdminRole(request)) {
            return "error/guestError";
        }
        if (!StringUtils.isEmpty((CharSequence)accountInfo.getPasswd())) {
            accountInfo.setPasswd(MD5Utils.GetMD5Code(accountInfo.getPasswd()));
        }
        try {
            String[] hostnames = request.getParameterValues("hostnames");
            if (StringUtils.isEmpty((CharSequence)accountInfo.getId())) {
                this.accountInfoService.save(accountInfo);
                this.accountInfoService.saveLog(request, "\u6dfb\u52a0", accountInfo);
            } else {
                this.accountInfoService.updateById(accountInfo);
                this.accountInfoService.saveLog(request, "\u4fee\u6539", accountInfo);
            }
            this.accountInfoService.updateHostAccount(accountInfo, hostnames);
        }
        catch (Exception e) {
            logger.error("\u4fdd\u5b58\u6210\u5458\u8d26\u53f7\u4fe1\u606f\u9519\u8bef\uff1a", (Throwable)e);
            this.logInfoService.save("\u4fdd\u5b58\u6210\u5458\u8d26\u53f7\u4fe1\u606f\u9519\u8bef", "\u4fdd\u5b58\u6210\u5458\u8d26\u53f7\u9519\u8bef\uff1a" + e.toString(), "2");
        }
        return "redirect:/accountInfo/list";
    }

    @RequestMapping(value={"savePasswd"})
    public String savePasswd(AccountInfo accountInfo, Model model, HttpServletRequest request) {
        if (!StaticKeys.LICENSE_STATE.equals("1")) {
            return "error/guestError";
        }
        if (!this.isAdminRole(request)) {
            return "error/guestError";
        }
        if (!StringUtils.isEmpty((CharSequence)accountInfo.getPasswd())) {
            accountInfo.setPasswd(MD5Utils.GetMD5Code(accountInfo.getPasswd()));
        }
        try {
            if (!StringUtils.isEmpty((CharSequence)accountInfo.getId())) {
                this.accountInfoService.updateById(accountInfo);
                AccountInfo accountInfoView = this.accountInfoService.selectById(accountInfo.getId());
                this.logInfoService.save(HostUtil.getAccountByRequest(request).getAccount() + "\u4fee\u6539\u767b\u5f55\u5bc6\u7801", accountInfoView.getAccount(), "2");
            }
        }
        catch (Exception e) {
            logger.error("\u4fee\u6539\u6210\u5458\u8d26\u53f7\u5bc6\u7801\u9519\u8bef\uff1a", (Throwable)e);
            this.logInfoService.save("\u4fee\u6539\u6210\u5458\u8d26\u53f7\u5bc6\u7801\u9519\u8bef", "\u4fee\u6539\u6210\u5458\u8d26\u53f7\u5bc6\u7801\u9519\u8bef\uff1a" + e.toString(), "2");
        }
        return "redirect:/accountInfo/list";
    }

    @ResponseBody
    @RequestMapping(value={"moveToAccount"})
    public String moveToAccount(Model model, HttpServletRequest request) {
        try {
            if (!this.isAdminRole(request)) {
                return "error/guestError";
            }
            String accountSource = request.getParameter("idSourceAccount");
            String accountTarget = request.getParameter("account");
            String resourceType = request.getParameter("resourceType");
            if (StringUtils.isEmpty((CharSequence)resourceType)) {
                this.accountInfoService.moveToAccount(accountTarget, accountSource);
            } else {
                this.accountInfoService.moveToAccount(accountTarget, accountSource, resourceType);
            }
        }
        catch (Exception e) {
            logger.error("\u8fc1\u79fb\u8d26\u53f7\u8d44\u6e90\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("\u8fc1\u79fb\u8d26\u53f7\u8d44\u6e90\u9519\u8bef", e.toString(), "2");
        }
        return "redirect:/accountInfo/list";
    }

    @ResponseBody
    @RequestMapping(value={"saveAdminMenus"})
    public String saveAdminMenus(Model model, HttpServletRequest request) {
        try {
            if (!this.isAdminRole(request)) {
                return "error";
            }
            StaticKeys.ADMIN_MENUDIS = request.getParameter("menuIds");
        }
        catch (Exception e) {
            logger.error("\u8bbe\u7f6e\u7ba1\u7406\u5458\u53ef\u4ee5\u64cd\u4f5c\u7684\u83dc\u5355\u9519\u8bef", (Throwable)e);
        }
        return "redirect:/accountInfo/list";
    }

    @RequestMapping(value={"del"})
    public String delete(Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        if (!StaticKeys.LICENSE_STATE.equals("1")) {
            return "error/guestError";
        }
        if (!this.isAdminRole(request)) {
            return "error/guestError";
        }
        String errorMsg = "\u5220\u9664\u6210\u5458\u8d26\u53f7\u9519\u8bef";
        try {
            if (!StringUtils.isEmpty((CharSequence)request.getParameter("id"))) {
                String[] ids;
                for (String id : ids = request.getParameter("id").split(",")) {
                    AccountInfo accountInfo = this.accountInfoService.selectById(id);
                    HashMap<String, Object> params = new HashMap<String, Object>();
                    params.put("account", accountInfo.getAccount());
                    this.accountInfoService.clearOthersAccount(params);
                    this.accountInfoService.saveLog(request, "\u5220\u9664", accountInfo);
                }
                this.accountInfoService.deleteById(ids);
            }
        }
        catch (Exception e) {
            logger.error(errorMsg, (Throwable)e);
            this.logInfoService.save(errorMsg, e.toString(), "2");
        }
        return "redirect:/accountInfo/list";
    }

    private boolean isAdminRole(HttpServletRequest request) {
        if (!StaticKeys.LICENSE_STATE.equals("1")) {
            return false;
        }
        AccountInfo accountInfoSession = HostUtil.getAccountByRequest(request);
        return "admin".equals(accountInfoSession.getRole());
    }
}

