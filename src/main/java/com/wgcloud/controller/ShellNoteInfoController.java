/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.controller;

import com.github.pagehelper.PageInfo;
import com.wgcloud.config.CommonConfig;
import com.wgcloud.entity.AccountInfo;
import com.wgcloud.entity.ShellNoteInfo;
import com.wgcloud.service.LogInfoService;
import com.wgcloud.service.ShellNoteInfoService;
import com.wgcloud.util.DateUtil;
import com.wgcloud.util.FileUtils;
import com.wgcloud.util.FormatUtil;
import com.wgcloud.util.HostUtil;
import com.wgcloud.util.PageUtil;
import com.wgcloud.util.ResDataUtils;
import com.wgcloud.util.UUIDUtil;
import com.wgcloud.util.staticvar.StaticKeys;
import java.nio.file.Path;
import java.nio.file.Paths;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping(value={"/shellNoteInfo"})
public class ShellNoteInfoController {
    private static final Logger logger = LoggerFactory.getLogger(ShellNoteInfoController.class);
    @Resource
    private ShellNoteInfoService shellNoteInfoService;
    @Resource
    private LogInfoService logInfoService;
    @Autowired
    private CommonConfig commonConfig;

    @RequestMapping(value={"list"})
    public String shellNoteInfoList(ShellNoteInfo shellNoteInfo, Model model, HttpServletRequest request) {
        HashMap<String, Object> params = new HashMap<String, Object>();
        try {
            AccountInfo accountInfo = HostUtil.getAccountByRequest(request);
            if ("guest".equals(accountInfo.getRole())) {
                return "redirect:/common/error/guestError";
            }
            StringBuffer url = new StringBuffer();
            String shellTitle = null;
            if (!StringUtils.isEmpty((CharSequence)shellNoteInfo.getShellTitle())) {
                shellTitle = shellNoteInfo.getShellTitle();
                params.put("shellTitle", shellTitle.trim());
                url.append("&shellTitle=").append(shellTitle);
            }
            if (!StringUtils.isEmpty((CharSequence)shellNoteInfo.getAccount())) {
                params.put("account", shellNoteInfo.getAccount());
                url.append("&account=").append(shellNoteInfo.getAccount());
            }
            HostUtil.addAccountquery(request, params);
            PageInfo<ShellNoteInfo> pageInfo = this.shellNoteInfoService.selectByParams(params, shellNoteInfo.getPage(), shellNoteInfo.getPageSize());
            PageUtil.initPageNumber(pageInfo, model);
            HostUtil.addAccountListModel(model);
            model.addAttribute("pageUrl", (Object)("/shellNoteInfo/list?1=1" + url.toString()));
            model.addAttribute("page", pageInfo);
            model.addAttribute("shellNoteInfo", (Object)shellNoteInfo);
        }
        catch (Exception e) {
            logger.error("\u67e5\u8be2\u6307\u4ee4\u7b14\u8bb0\u4fe1\u606f\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("\u67e5\u8be2\u6307\u4ee4\u7b14\u8bb0\u4fe1\u606f\u9519\u8bef", e.toString(), "2");
        }
        return "shellNoteInfo/list";
    }

    @RequestMapping(value={"save"})
    public String saveShellNoteInfo(ShellNoteInfo shellNoteInfo, Model model, HttpServletRequest request) {
        try {
            if (StringUtils.isEmpty((CharSequence)shellNoteInfo.getId())) {
                AccountInfo accountInfo = HostUtil.getAccountByRequest(request);
                if (null != accountInfo && !"admin".equals(accountInfo.getRole())) {
                    shellNoteInfo.setAccount(accountInfo.getAccount());
                }
                this.shellNoteInfoService.save(shellNoteInfo);
                this.shellNoteInfoService.saveLog(request, "\u6dfb\u52a0", shellNoteInfo);
            } else {
                this.shellNoteInfoService.updateById(shellNoteInfo);
                this.shellNoteInfoService.saveLog(request, "\u4fee\u6539", shellNoteInfo);
            }
        }
        catch (Exception e) {
            logger.error("\u4fdd\u5b58\u5de5\u4f5c\u7b14\u8bb0\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("\u4fdd\u5b58\u5de5\u4f5c\u7b14\u8bb0\u9519\u8bef", e.toString(), "2");
        }
        return "redirect:/shellNoteInfo/list";
    }

    @RequestMapping(value={"edit"})
    public String edit(Model model, HttpServletRequest request) {
        String errorMsg = "\u6dfb\u52a0\u5de5\u4f5c\u7b14\u8bb0";
        String id = request.getParameter("id");
        ShellNoteInfo shellNoteInfo = new ShellNoteInfo();
        try {
            if (StringUtils.isEmpty((CharSequence)id)) {
                model.addAttribute("shellNoteInfo", (Object)shellNoteInfo);
                return "shellNoteInfo/add";
            }
            shellNoteInfo = this.shellNoteInfoService.selectById(id);
            model.addAttribute("shellNoteInfo", (Object)shellNoteInfo);
        }
        catch (Exception e) {
            logger.error(errorMsg, (Throwable)e);
            this.logInfoService.save(errorMsg, e.toString(), "2");
        }
        return "shellNoteInfo/add";
    }

    @RequestMapping(value={"view"})
    public String viewChart(Model model, HttpServletRequest request) {
        String errorMsg = "\u67e5\u770b\u5de5\u4f5c\u7b14\u8bb0\u4fe1\u606f\u9519\u8bef";
        String id = request.getParameter("id");
        ShellNoteInfo shellNoteInfo = new ShellNoteInfo();
        try {
            shellNoteInfo = this.shellNoteInfoService.selectById(id);
            model.addAttribute("shellNoteInfo", (Object)shellNoteInfo);
        }
        catch (Exception e) {
            logger.error(errorMsg, (Throwable)e);
            this.logInfoService.save(errorMsg, e.toString(), "2");
        }
        return "shellNoteInfo/view";
    }

    @RequestMapping(value={"del"})
    public String delete(Model model, HttpServletRequest request) {
        String errorMsg = "\u5220\u9664\u5de5\u4f5c\u7b14\u8bb0\u4fe1\u606f\u9519\u8bef";
        ShellNoteInfo ShellNoteInfo2 = new ShellNoteInfo();
        try {
            if (!StringUtils.isEmpty((CharSequence)request.getParameter("id"))) {
                String[] ids;
                for (String id : ids = request.getParameter("id").split(",")) {
                    ShellNoteInfo2 = this.shellNoteInfoService.selectById(id);
                    this.shellNoteInfoService.saveLog(request, "\u5220\u9664", ShellNoteInfo2);
                }
                this.shellNoteInfoService.deleteById(ids);
            }
        }
        catch (Exception e) {
            logger.error(errorMsg, (Throwable)e);
            this.logInfoService.save(errorMsg, e.toString(), "2");
        }
        return "redirect:/shellNoteInfo/list";
    }

    @ResponseBody
    @RequestMapping(value={"saveFileAjax"})
    public String saveFileAjax(@RequestParam(value="file") List<MultipartFile> fileList, Model model, HttpServletRequest request) {
        try {
            String imageUrl = "";
            for (MultipartFile file : fileList) {
                String fileSize = FormatUtil.bytesFormatUnit(file.getBytes().length + "", "byte");
                String sourceFileName = file.getOriginalFilename();
                if (StringUtils.isEmpty((CharSequence)sourceFileName)) continue;
                String allowFileTypes = "jpg,jpeg,png,gif";
                String fileType = sourceFileName.substring(sourceFileName.lastIndexOf(".") + 1);
                if (!allowFileTypes.contains(fileType)) {
                    logger.error("\u4e0d\u5141\u8bb8\u4e0a\u4f20\u7684\u56fe\u7247\u7c7b\u578b:" + sourceFileName);
                    continue;
                }
                String day = DateUtil.getCurrentDate().replace("-", "");
                String saveFolderUploadFile = StaticKeys.JAR_PATH + "/uploadFile";
                FileUtils.existsFolder(saveFolderUploadFile);
                String saveFolder = StaticKeys.JAR_PATH + "/uploadFile/" + day;
                FileUtils.existsFolder(saveFolder);
                String newFileName = UUIDUtil.getUUID() + "." + fileType;
                String newFilePath = saveFolder + "/" + newFileName;
                Path path = Paths.get(newFilePath, new String[0]);
                file.transferTo(path);
                imageUrl = "/uploadFile/" + day + "/" + newFileName;
            }
            return ResDataUtils.resetSuccessJson(imageUrl);
        }
        catch (Exception e) {
            logger.error("\u7c98\u8d34\u56fe\u7247\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("\u7c98\u8d34\u56fe\u7247\u9519\u8bef", e.toString(), "2");
            return "error";
        }
    }
}

