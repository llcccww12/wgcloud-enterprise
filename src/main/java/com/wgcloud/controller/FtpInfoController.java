/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.controller;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.github.pagehelper.PageInfo;
import com.wgcloud.config.CommonConfig;
import com.wgcloud.dto.MessageDto;
import com.wgcloud.entity.AccountInfo;
import com.wgcloud.entity.FtpInfo;
import com.wgcloud.service.FtpInfoService;
import com.wgcloud.service.HostGroupService;
import com.wgcloud.service.LogInfoService;
import com.wgcloud.util.DESUtil;
import com.wgcloud.util.HostUtil;
import com.wgcloud.util.IpUtil;
import com.wgcloud.util.JschUtil;
import com.wgcloud.util.MessageErrorUtils;
import com.wgcloud.util.PageUtil;
import com.wgcloud.util.ResDataUtils;
import com.wgcloud.util.ServerBackupUtil;
import com.wgcloud.util.TokenUtils;
import com.wgcloud.util.license.LicenseUtil;
import com.wgcloud.util.staticvar.StaticKeys;
import java.util.Date;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value={"/ftpInfo"})
public class FtpInfoController {
    private static final Logger logger = LoggerFactory.getLogger(FtpInfoController.class);
    @Resource
    private FtpInfoService ftpInfoService;
    @Resource
    private LogInfoService logInfoService;
    @Resource
    private HostGroupService hostGroupService;
    @Autowired
    private MessageErrorUtils messageErrorUtils;
    @Autowired
    private CommonConfig commonConfig;
    @Autowired
    private TokenUtils tokenUtils;

    @ResponseBody
    @RequestMapping(value={"agentList"})
    public String agentList(@RequestBody String paramBean, HttpServletRequest request) {
        JSONObject agentJsonObject = (JSONObject)JSONUtil.parse((Object)paramBean);
        String checkResult = this.tokenUtils.preOpenDataAPICheck(agentJsonObject);
        if (!StringUtils.isEmpty((CharSequence)checkResult)) {
            return checkResult;
        }
        HashMap<String, Object> params = new HashMap<String, Object>();
        try {
            if (null != agentJsonObject.get("ftpNames") && !StringUtils.isEmpty((CharSequence)agentJsonObject.get("ftpNames").toString())) {
                params.put("ftpNames", agentJsonObject.get("ftpNames").toString().split(","));
            }
            params.put("active", "1");
            List<FtpInfo> ftpInfoList = this.ftpInfoService.selectAllByParams(params);
            for (FtpInfo ftpInfo : ftpInfoList) {
                ftpInfo.setUserName(DESUtil.encryption(ftpInfo.getUserName()));
                ftpInfo.setPasswd(DESUtil.encryption(DESUtil.decryptForServerDb(ftpInfo.getPasswd())));
            }
            if (null != agentJsonObject.get("SERVER_BACKUP_FLAG") && !StringUtils.isEmpty((CharSequence)agentJsonObject.get("SERVER_BACKUP_FLAG").toString())) {
                ServerBackupUtil.cacheSaveFtpInfoId(ftpInfoList);
                logger.info("server-backup request FtpInfo-------" + IpUtil.getIpAddr(request));
            }
            return ResDataUtils.resetSuccessJson(ftpInfoList);
        }
        catch (Exception e) {
            logger.error("agent\u83b7\u53d6\u76d1\u63a7ftp/sftp\u5217\u8868\u4fe1\u606f\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("agent\u83b7\u53d6\u76d1\u63a7ftp/sftp\u5217\u8868\u4fe1\u606f\u9519\u8bef", e.toString(), "2");
            return ResDataUtils.resetErrorJson(e.toString());
        }
    }

    @RequestMapping(value={"list"})
    public String FtpInfoList(FtpInfo ftpInfo, Model model, HttpServletRequest request) {
        HashMap<String, Object> params = new HashMap<String, Object>();
        try {
            LicenseUtil.maxLicense_10(model, request, ftpInfo);
            StringBuffer url = new StringBuffer();
            if (!StringUtils.isEmpty((CharSequence)ftpInfo.getFtpHost())) {
                params.put("ftpHost", ftpInfo.getFtpHost().trim());
                url.append("&ftpHost=").append(ftpInfo.getFtpHost());
            }
            if (!StringUtils.isEmpty((CharSequence)ftpInfo.getAccount())) {
                params.put("account", ftpInfo.getAccount());
                url.append("&account=").append(ftpInfo.getAccount());
            }
            if (!StringUtils.isEmpty((CharSequence)ftpInfo.getGroupId())) {
                params.put("groupId", ftpInfo.getGroupId());
                url.append("&groupId=").append(ftpInfo.getGroupId());
            }
            if (!StringUtils.isEmpty((CharSequence)ftpInfo.getState())) {
                params.put("state", ftpInfo.getState());
                url.append("&state=").append(ftpInfo.getState());
            }
            if (!StringUtils.isEmpty((CharSequence)ftpInfo.getActive())) {
                params.put("active", ftpInfo.getActive());
                url.append("&active=").append(ftpInfo.getActive());
            }
            if (!StringUtils.isEmpty((CharSequence)ftpInfo.getOrderBy())) {
                params.put("orderBy", ftpInfo.getOrderBy());
                params.put("orderType", ftpInfo.getOrderType());
                url.append("&orderBy=").append(ftpInfo.getOrderBy());
                url.append("&orderType=").append(ftpInfo.getOrderType());
            }
            HostUtil.addAccountquery(request, params);
            PageInfo pageInfo = this.ftpInfoService.selectByParams(params, ftpInfo.getPage(), ftpInfo.getPageSize());
            for (FtpInfo ftpInfo1 : (java.util.List<FtpInfo>) pageInfo.getList()) {
                if (!"true".equals(this.commonConfig.getShowWarnCount())) continue;
                String warnQueryWd = ftpInfo1.getFtpHost();
                this.logInfoService.warnQueryHandle(ftpInfo1, warnQueryWd);
            }
            if ("true".equals(this.commonConfig.getHostGroup())) {
                this.ftpInfoService.setGroupInList(pageInfo.getList(), model, request);
            }
            HostUtil.addAccountListModel(model);
            this.ftpInfoService.addServerBackMark(pageInfo.getList());
            PageUtil.initPageNumber(pageInfo, model);
            model.addAttribute("pageUrl", (Object)("/ftpInfo/list?1=1" + url.toString()));
            model.addAttribute("page", (Object)pageInfo);
            model.addAttribute("ftpInfo", (Object)ftpInfo);
        }
        catch (Exception e) {
            logger.error("\u67e5\u8be2ftp\u76d1\u6d4b\u4fe1\u606f\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("\u67e5\u8be2ftp\u76d1\u6d4b\u4fe1\u606f\u9519\u8bef", e.toString(), "2");
        }
        return "ftpInfo/list";
    }

    @ResponseBody
    @RequestMapping(value={"saveGroupId"})
    public String saveGroupId(Model model, HttpServletRequest request) {
        try {
            String ids = request.getParameter("ids");
            String[] groupIdsArr = request.getParameterValues("groupId");
            this.ftpInfoService.saveGroupId(ids, groupIdsArr, request);
        }
        catch (Exception e) {
            logger.error("\u4fdd\u5b58FTP\u6807\u7b7e\u4fe1\u606f\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("\u4fdd\u5b58FTP\u6807\u7b7e\u4fe1\u606f\u9519\u8bef", e.toString(), "2");
        }
        return "redirect:/ftpInfo/list";
    }

    @RequestMapping(value={"save"})
    public String saveFtpInfo(FtpInfo ftpInfo, Model model, HttpServletRequest request) {
        String errorMsg = "\u4fdd\u5b58ftp\u76d1\u6d4b\u4fe1\u606f\u9519\u8bef";
        try {
            ftpInfo.setPasswd(DESUtil.encryptionForServerDb(ftpInfo.getPasswd()));
            if (StringUtils.isEmpty((CharSequence)ftpInfo.getWarnType())) {
                ftpInfo.setWarnType("");
            }
            if (StringUtils.isEmpty((CharSequence)ftpInfo.getId())) {
                AccountInfo accountInfo = HostUtil.getAccountByRequest(request);
                if (null != accountInfo && !"admin".equals(accountInfo.getRole())) {
                    ftpInfo.setAccount(accountInfo.getAccount());
                }
                this.ftpInfoService.save(ftpInfo);
                this.ftpInfoService.saveLog(request, "\u6dfb\u52a0", ftpInfo);
            } else {
                this.ftpInfoService.updateById(ftpInfo);
                this.ftpInfoService.saveLog(request, "\u4fee\u6539", ftpInfo);
            }
        }
        catch (Exception e) {
            logger.error(errorMsg, (Throwable)e);
            this.logInfoService.save(errorMsg, e.toString(), "2");
        }
        return "redirect:/ftpInfo/list";
    }

    @RequestMapping(value={"edit"})
    public String edit(Model model, HttpServletRequest request) {
        String errorMsg = "\u6dfb\u52a0ftp\u76d1\u6d4b\u4fe1\u606f\u9519\u8bef";
        String id = request.getParameter("id");
        FtpInfo ftpInfo = new FtpInfo();
        try {
            HashMap<String, Object> params = new HashMap<String, Object>();
            HostUtil.addAccountquery(request, params);
            if (StringUtils.isEmpty((CharSequence)id)) {
                if (!this.isAddContinue()) {
                    return "redirect:/ftpInfo/list?liceFlage=1";
                }
                ftpInfo.setPort("22");
                model.addAttribute("ftpInfo", (Object)ftpInfo);
                return "ftpInfo/add";
            }
            ftpInfo = this.ftpInfoService.selectById(id);
            ftpInfo.setPasswd(DESUtil.decryptForServerDb(ftpInfo.getPasswd()));
            model.addAttribute("ftpInfo", (Object)ftpInfo);
        }
        catch (Exception e) {
            logger.error(errorMsg, (Throwable)e);
            this.logInfoService.save(errorMsg, e.toString(), "2");
        }
        return "ftpInfo/add";
    }

    @ResponseBody
    @RequestMapping(value={"test"})
    public String test(FtpInfo ftpInfo, Model model, HttpServletRequest request) {
        MessageDto messageDto = new MessageDto();
        try {
            if ("SFTP".equals(ftpInfo.getFtpType())) {
                JschUtil.testSFtpSession(ftpInfo);
            } else {
                JschUtil.testFTPClient(ftpInfo);
            }
            if ("1".equals(ftpInfo.getState())) {
                messageDto.setCode("0");
                messageDto.setMsg("\u8fde\u63a5\u6d4b\u8bd5\u6210\u529f");
            } else {
                messageDto.setCode("1");
                messageDto.setMsg("\u8fde\u63a5\u6d4b\u8bd5\u5931\u8d25\uff0c\u8bf7\u68c0\u67e5\u53c2\u6570\u662f\u5426\u6b63\u786e\u3002" + ftpInfo.getTestErrorMsg());
            }
        }
        catch (Exception e) {
            logger.error("\u6d4b\u8bd5ftp\u8bbe\u7f6e\u4fe1\u606f\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("\u6d4b\u8bd5ftp\u8bbe\u7f6e\u4fe1\u606f\u9519\u8bef", e.toString(), "2");
        }
        return JSONUtil.toJsonStr((Object)messageDto);
    }

    @ResponseBody
    @RequestMapping(value={"testHeathForList"})
    public String testHeathForList(Model model, HttpServletRequest request) {
        String errorMsg = "FTP\u76d1\u63a7\u6d4b\u8bd5\u8fde\u63a5\u9519\u8bef\uff1a";
        String resultMsg = "success";
        try {
            if (!StaticKeys.LICENSE_STATE.equals("1")) {
                return "noPro";
            }
            String id = request.getParameter("id");
            if (!StringUtils.isEmpty((CharSequence)id)) {
                FtpInfo ftpInfo = this.ftpInfoService.selectById(id);
                if (!StringUtils.isEmpty((CharSequence)ftpInfo.getPasswd())) {
                    ftpInfo.setPasswd(DESUtil.decryptForServerDb(ftpInfo.getPasswd()));
                }
                if ("SFTP".equals(ftpInfo.getFtpType())) {
                    JschUtil.testSFtpSession(ftpInfo);
                } else {
                    JschUtil.testFTPClient(ftpInfo);
                }
                if (!"1".equals(ftpInfo.getState())) {
                    resultMsg = "\u8fde\u63a5\u6d4b\u8bd5\u5931\u8d25\uff0c\u8bf7\u68c0\u67e5\u53c2\u6570\u662f\u5426\u6b63\u786e\u3002" + ftpInfo.getTestErrorMsg();
                }
                FtpInfo ftpInfoForUpdate = new FtpInfo();
                ftpInfoForUpdate.setId(ftpInfo.getId());
                ftpInfoForUpdate.setState(ftpInfo.getState());
                ftpInfoForUpdate.setResTimes(ftpInfo.getResTimes());
                if ("2".equals(ftpInfo.getState())) {
                    ftpInfoForUpdate.setCreateTime(null);
                } else {
                    ftpInfoForUpdate.setCreateTime(new Date());
                }
                this.ftpInfoService.updateById(ftpInfoForUpdate);
            } else {
                resultMsg = "\u7ed3\u679c\u597d\u50cf\u662f\u7a7a\u7684";
            }
        }
        catch (Exception e) {
            logger.error(errorMsg, (Throwable)e);
            resultMsg = "\u6d4b\u8bd5\u9519\u8bef:" + e.toString();
        }
        return resultMsg;
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
                this.ftpInfoService.updateActive(params);
            }
        }
        catch (Exception e) {
            logger.error(errorMsg, (Throwable)e);
            this.logInfoService.save(errorMsg, e.toString(), "2");
        }
        return "redirect:/ftpInfo/list";
    }

    @RequestMapping(value={"del"})
    public String delete(Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        String errorMsg = "\u5220\u9664ftp\u76d1\u6d4b\u4fe1\u606f\u9519\u8bef";
        FtpInfo ftpInfo = new FtpInfo();
        try {
            if (!StringUtils.isEmpty((CharSequence)request.getParameter("id"))) {
                String[] ids;
                for (String id : ids = request.getParameter("id").split(",")) {
                    ftpInfo = this.ftpInfoService.selectById(id);
                    this.ftpInfoService.saveLog(request, "\u5220\u9664", ftpInfo);
                }
                this.ftpInfoService.deleteById(ids);
            }
        }
        catch (Exception e) {
            logger.error(errorMsg, (Throwable)e);
            this.logInfoService.save(errorMsg, e.toString(), "2");
        }
        return "redirect:/ftpInfo/list";
    }

    @RequestMapping(value={"view"})
    public String view(Model model, HttpServletRequest request) {
        String errorMsg = "\u67e5\u770bFTP\u76d1\u63a7\u8be6\u60c5";
        String id = request.getParameter("id");
        FtpInfo ftpInfo = new FtpInfo();
        try {
            ftpInfo = this.ftpInfoService.selectById(id);
            model.addAttribute("ftpInfo", (Object)ftpInfo);
            if ("true".equals(this.commonConfig.getHostGroup())) {
                ftpInfo.setGroupId(this.hostGroupService.returnGroupNames(ftpInfo.getGroupId()));
            }
            if ("2".equals(ftpInfo.getState())) {
                model.addAttribute("ftpInfoErrorMsg", (Object)this.messageErrorUtils.viewErrorMsgHandler(id));
            } else {
                model.addAttribute("ftpInfoErrorMsg", "success");
            }
        }
        catch (Exception e) {
            logger.error(errorMsg, (Throwable)e);
            this.logInfoService.save(errorMsg, e.toString(), "2");
        }
        return "ftpInfo/view";
    }

    private boolean isAddContinue() {
        try {
            HashMap<String, Object> params;
            int dbSize;
            if (!StaticKeys.LICENSE_STATE.equals("1") && (dbSize = this.ftpInfoService.countByParams(params = new HashMap<String, Object>())) >= 10) {
                return false;
            }
        }
        catch (Exception e) {
            logger.error("isAddContinue error", (Throwable)e);
        }
        return true;
    }
}

