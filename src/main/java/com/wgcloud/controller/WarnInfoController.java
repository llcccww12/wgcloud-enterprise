/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.controller;

import com.wgcloud.config.CommonConfig;
import com.wgcloud.util.RestUtil;
import com.wgcloud.util.license.LicenseUtil;
import com.wgcloud.util.msg.WarnPools;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value={"/warnInfo"})
public class WarnInfoController {
    private static final Logger logger = LoggerFactory.getLogger(WarnInfoController.class);
    @Autowired
    private RestUtil restUtil;
    @Autowired
    private CommonConfig commonConfig;

    @ResponseBody
    @RequestMapping(value={"warnCountAjaxHandle"})
    public String warnCountAjaxHandle(Model model, HttpServletRequest request) {
        String timerWarnSound = request.getParameter("timerWarnSound");
        request.getSession().setAttribute("timerWarnSound", (Object)timerWarnSound);
        return "";
    }

    @ResponseBody
    @RequestMapping(value={"warnCountAjax"})
    public String warnCountAjax(HttpServletRequest request) {
        String timerWarnSound = (String)request.getSession().getAttribute("timerWarnSound");
        if ("1".equals(timerWarnSound)) {
            int count = WarnPools.WARN_COUNT_LIST.size();
            WarnPools.WARN_COUNT_LIST.clear();
            return count + "";
        }
        return "0";
    }

    @ResponseBody
    @RequestMapping(value={"switchTheme"})
    public String switchThemeAjaxHandle(Model model, HttpServletRequest request) {
        String themeName = request.getParameter("themeName");
        request.getSession().setAttribute("themeName", (Object)themeName);
        if ("1".equals(themeName)) {
            request.getSession().setAttribute("themeNameSign", "");
            request.getSession().setAttribute("themeTableHoverSign", "table-hover");
            request.getSession().setAttribute("themePageFooterSign", "");
        } else if ("2".equals(themeName)) {
            request.getSession().setAttribute("themeNameSign", "navbar-dark bg-dark darkbg-border-color");
            request.getSession().setAttribute("themeTableHoverSign", "darkbg-border-color");
            request.getSession().setAttribute("themePageFooterSign", "navbar-dark bg-dark darkbg-border-color");
        } else if ("4".equals(themeName)) {
            request.getSession().setAttribute("themeNameSign", "navbar-teal bg-teal");
            request.getSession().setAttribute("themeTableHoverSign", "table-hover");
            request.getSession().setAttribute("themePageFooterSign", "");
        } else if ("5".equals(themeName)) {
            if (!LicenseUtil.checkEnterpriseVersion()) {
                return "\u8bf7\u5347\u7ea7\u5230\u4f01\u4e1a\u7248\u67e5\u770b\u6b64\u6570\u636e";
            }
            request.getSession().setAttribute("themeNameSign", "navbar-dark bg-indigo");
            request.getSession().setAttribute("themeTableHoverSign", "table-hover");
            request.getSession().setAttribute("themePageFooterSign", "");
        } else if ("6".equals(themeName)) {
            if (!LicenseUtil.checkEnterpriseVersion()) {
                return "\u8bf7\u5347\u7ea7\u5230\u4f01\u4e1a\u7248\u67e5\u770b\u6b64\u6570\u636e";
            }
            request.getSession().setAttribute("themeNameSign", "navbar-dark bg-purple");
            request.getSession().setAttribute("themeTableHoverSign", "table-hover");
            request.getSession().setAttribute("themePageFooterSign", "");
        } else if ("7".equals(themeName)) {
            if (!LicenseUtil.checkEnterpriseVersion()) {
                return "\u8bf7\u5347\u7ea7\u5230\u4f01\u4e1a\u7248\u67e5\u770b\u6b64\u6570\u636e";
            }
            request.getSession().setAttribute("themeNameSign", "navbar-dark bg-navy darkbg-border-color");
            request.getSession().setAttribute("themeTableHoverSign", "darkbg-border-color");
            request.getSession().setAttribute("themePageFooterSign", "navbar-dark bg-navy darkbg-border-color");
        } else {
            request.getSession().setAttribute("themeNameSign", "navbar-success bg-success");
            request.getSession().setAttribute("themeTableHoverSign", "table-hover");
            request.getSession().setAttribute("themePageFooterSign", "");
        }
        return "success";
    }
}

