/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.controller;

import com.wgcloud.config.MailConfig;
import com.wgcloud.service.LogInfoService;
import com.wgcloud.util.ExecUtil;
import com.wgcloud.util.HostUtil;
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

@Controller
@RequestMapping(value={"/warnScript"})
public class WarnScriptController {
    private static final Logger logger = LoggerFactory.getLogger(WarnScriptController.class);
    @Resource
    private LogInfoService logInfoService;
    @Autowired
    private MailConfig mailConfig;

    @RequestMapping(value={"list"})
    public String list(Model model, HttpServletRequest request) {
        model.addAttribute("warnScript", (Object)this.mailConfig.getWarnScript());
        model.addAttribute("recoverScript", (Object)this.mailConfig.getRecoverScript());
        return "mail/warnScript";
    }

    @ResponseBody
    @RequestMapping(value={"test"})
    public String test(Model model, HttpServletRequest request) {
        String result = "success";
        try {
            String scriptContent = request.getParameter("scriptContent");
            if (!StringUtils.isEmpty((CharSequence)scriptContent)) {
                result = ExecUtil.runScript(scriptContent, "", "\u91cd\u8981", "", "");
                this.logInfoService.save(HostUtil.getAccountByRequest(request).getAccount() + "\u544a\u8b66\u811a\u672c\u6d4b\u8bd5\u53d1\u9001\u4fe1\u606f", "\u6d4b\u8bd5\u4fe1\u606f\uff1a" + scriptContent, "2");
                String recoverScriptPath = this.mailConfig.getRecoverScript();
                if (!StringUtils.isEmpty((CharSequence)recoverScriptPath)) {
                    result = ExecUtil.runScript(scriptContent + "-" + "\u5df2\u6062\u590d", "", "\u91cd\u8981", "", "\u5df2\u6062\u590d");
                }
            }
        }
        catch (Exception e) {
            logger.error("\u544a\u8b66\u811a\u672c\u6d4b\u8bd5\u53d1\u9001\u4fe1\u606f\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("\u544a\u8b66\u811a\u672c\u6d4b\u8bd5\u53d1\u9001\u4fe1\u606f\u9519\u8bef", e.toString(), "2");
        }
        if ("".equals(result)) {
            result = "success";
        }
        return result;
    }
}

