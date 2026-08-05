/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.controller;

import com.github.pagehelper.PageInfo;
import com.wgcloud.entity.AccountInfo;
import com.wgcloud.entity.HostGroup;
import com.wgcloud.service.HostGroupService;
import com.wgcloud.service.LogInfoService;
import com.wgcloud.util.HostUtil;
import com.wgcloud.util.PageUtil;
import com.wgcloud.util.license.LicenseUtil;
import com.wgcloud.util.staticvar.StaticKeys;
import java.util.HashMap;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value={"/hostGroup"})
public class HostGroupController {
    private static final Logger logger = LoggerFactory.getLogger(HostGroupController.class);
    @Resource
    private HostGroupService hostGroupService;
    @Resource
    private LogInfoService logInfoService;

    @RequestMapping(value={"list"})
    public String hostGroupList(HostGroup hostGroup, Model model, HttpServletRequest request) {
        String errorMsg = "\u67e5\u8be2\u6807\u7b7e\u4fe1\u606f\u9519\u8bef";
        LicenseUtil.maxLicense_10(model, request, hostGroup);
        HashMap<String, Object> params = new HashMap<String, Object>();
        try {
            AccountInfo accountInfo;
            StringBuffer url = new StringBuffer();
            if (!StringUtils.isEmpty((CharSequence)hostGroup.getGroupName())) {
                params.put("groupName", hostGroup.getGroupName());
                url.append("&groupName=").append(hostGroup.getGroupName());
            }
            if (!StringUtils.isEmpty((CharSequence)hostGroup.getAccount())) {
                params.put("account", hostGroup.getAccount());
                url.append("&account=").append(hostGroup.getAccount());
            }
            if (null != (accountInfo = HostUtil.getAccountByRequest(request)) && !"admin".equals(accountInfo.getRole())) {
                params.put("account", accountInfo.getAccount());
            }
            PageInfo pageInfo = this.hostGroupService.selectByParams(params, hostGroup.getPage(), hostGroup.getPageSize());
            PageUtil.initPageNumber(pageInfo, model);
            HostUtil.addAccountListModel(model);
            model.addAttribute("page", (Object)pageInfo);
            model.addAttribute("pageUrl", (Object)("/hostGroup/list?1=1" + url.toString()));
            model.addAttribute("hostGroup", (Object)hostGroup);
        }
        catch (Exception e) {
            logger.error(errorMsg, (Throwable)e);
            this.logInfoService.save(errorMsg, e.toString(), "2");
        }
        return "hostGroup/list";
    }

    @RequestMapping(value={"edit"})
    public String edit(Model model, HttpServletRequest request) {
        String errorMsg = "\u6dfb\u52a0\u6807\u7b7e\u4fe1\u606f\u9519\u8bef";
        String id = request.getParameter("id");
        HostGroup hostGroup = new HostGroup();
        try {
            if (StringUtils.isEmpty((CharSequence)id)) {
                model.addAttribute("hostGroup", (Object)hostGroup);
                if (!this.isAddContinue()) {
                    return "redirect:/hostGroup/list?liceFlage=1";
                }
                return "hostGroup/add";
            }
            hostGroup = this.hostGroupService.selectById(id);
            model.addAttribute("hostGroup", (Object)hostGroup);
        }
        catch (Exception e) {
            logger.error(errorMsg, (Throwable)e);
            this.logInfoService.save(errorMsg, e.toString(), "2");
        }
        return "hostGroup/add";
    }

    @RequestMapping(value={"save"})
    public String saveHostGroup(HostGroup hostGroup, Model model, HttpServletRequest request) {
        try {
            if (StringUtils.isEmpty((CharSequence)hostGroup.getId())) {
                AccountInfo accountInfo = HostUtil.getAccountByRequest(request);
                if (null != accountInfo && !"admin".equals(accountInfo.getRole())) {
                    hostGroup.setAccount(accountInfo.getAccount());
                }
                this.hostGroupService.save(hostGroup);
                this.hostGroupService.saveLog(request, "\u6dfb\u52a0", hostGroup);
            } else {
                this.hostGroupService.updateById(hostGroup);
                this.hostGroupService.saveLog(request, "\u4fee\u6539", hostGroup);
            }
        }
        catch (Exception e) {
            logger.error("\u4fdd\u5b58\u6807\u7b7e\u9519\u8bef\uff1a", (Throwable)e);
            this.logInfoService.save("\u4fdd\u5b58\u6807\u7b7e\u9519\u8bef", e.toString(), "2");
        }
        return "redirect:/hostGroup/list";
    }

    @RequestMapping(value={"del"})
    public String delete(Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        String errorMsg = "\u5220\u9664\u6807\u7b7e\u9519\u8bef";
        try {
            if (!StringUtils.isEmpty((CharSequence)request.getParameter("id"))) {
                String[] ids;
                for (String id : ids = request.getParameter("id").split(",")) {
                    HostGroup hostGroup = this.hostGroupService.selectById(id);
                    this.hostGroupService.deleteById(request.getParameter("id").split(","));
                    this.hostGroupService.saveLog(request, "\u5220\u9664", hostGroup);
                }
            }
        }
        catch (Exception e) {
            logger.error(errorMsg, (Throwable)e);
            this.logInfoService.save(errorMsg, e.toString(), "2");
        }
        return "redirect:/hostGroup/list";
    }

    private boolean isAddContinue() {
        try {
            HashMap<String, Object> params;
            int Size;
            if (!StaticKeys.LICENSE_STATE.equals("1") && (Size = this.hostGroupService.countByParams(params = new HashMap<String, Object>())) >= 10) {
                return false;
            }
        }
        catch (Exception e) {
            logger.error("isAddContinue error", (Throwable)e);
        }
        return true;
    }
}

