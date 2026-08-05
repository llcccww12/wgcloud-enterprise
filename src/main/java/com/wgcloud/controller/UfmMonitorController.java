/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.controller;

import com.github.pagehelper.PageInfo;
import com.wgcloud.config.CommonConfig;
import com.wgcloud.entity.UfmMonitor;
import com.wgcloud.service.UfmMonitorService;
import com.wgcloud.service.LogInfoService;
import com.wgcloud.util.MessageErrorUtils;
import com.wgcloud.util.PageUtil;
import com.wgcloud.util.TokenUtils;
import com.wgcloud.util.license.LicenseUtil;
import java.util.HashMap;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value={"/ufmMonitor"})
public class UfmMonitorController {
    private static final Logger logger = LoggerFactory.getLogger(UfmMonitorController.class);
    @Resource
    private UfmMonitorService ufmMonitorService;
    @Resource
    private LogInfoService logInfoService;
    @Autowired
    private MessageErrorUtils messageErrorUtils;
    @Autowired
    private CommonConfig commonConfig;
    @Autowired
    private TokenUtils tokenUtils;

    @RequestMapping(value={"list"})
    public String list(UfmMonitor ufmMonitor, Model model, HttpServletRequest request) {
        HashMap<String, Object> params = new HashMap<String, Object>();
        try {
            LicenseUtil.maxLicense_10(model, request, ufmMonitor);
            StringBuffer url = new StringBuffer();
            if (!StringUtils.isEmpty(ufmMonitor.getSystemName())) {
                params.put("systemName", ufmMonitor.getSystemName());
                url.append("&systemName=").append(ufmMonitor.getSystemName());
            }
            if (!StringUtils.isEmpty(ufmMonitor.getSeverity())) {
                params.put("severity", ufmMonitor.getSeverity());
                url.append("&severity=").append(ufmMonitor.getSeverity());
            }
            if (!StringUtils.isEmpty(ufmMonitor.getDeviceType())) {
                params.put("deviceType", ufmMonitor.getDeviceType());
                url.append("&deviceType=").append(ufmMonitor.getDeviceType());
            }
            PageInfo pageInfo = this.ufmMonitorService.selectByParams(params, ufmMonitor.getPage(), ufmMonitor.getPageSize());
            PageUtil.initPageNumber(pageInfo, model);
            model.addAttribute("pageUrl", (Object)("/ufmMonitor/list?1=1" + url.toString()));
            model.addAttribute("page", (Object)pageInfo);
            model.addAttribute("ufmMonitor", (Object)ufmMonitor);
        }
        catch (Exception e) {
            logger.error("\u67e5\u8be2UFM\u76d1\u63a7\u4fe1\u606f\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("\u67e5\u8be2UFM\u76d1\u63a7\u4fe1\u606f\u9519\u8bef", e.toString(), "2");
        }
        return "ufm/list";
    }

    @RequestMapping(value={"view"})
    public String view(Model model, HttpServletRequest request) {
        try {
            String id = request.getParameter("id");
            if (!StringUtils.isEmpty(id)) {
                UfmMonitor ufmMonitor = this.ufmMonitorService.selectById(id);
                model.addAttribute("ufmMonitor", (Object)ufmMonitor);
            }
        }
        catch (Exception e) {
            logger.error("\u67e5\u8be2UFM\u8bbe\u5907\u8be6\u60c5\u9519\u8bef", (Throwable)e);
        }
        return "ufm/view";
    }

    @RequestMapping(value={"del"})
    public String delete(Model model, HttpServletRequest request) {
        String errorMsg = "\u5220\u9664UFM\u4fe1\u606f\u9519\u8bef";
        UfmMonitor ufmMonitor = new UfmMonitor();
        try {
            if (!StringUtils.isEmpty(request.getParameter("id"))) {
                String[] ids = request.getParameter("id").split(",");
                for (String id : ids) {
                    ufmMonitor = this.ufmMonitorService.selectById(id);
                    this.ufmMonitorService.saveLog(request, "\u5220\u9664", ufmMonitor);
                }
                this.ufmMonitorService.deleteById(ids);
            }
        }
        catch (Exception e) {
            logger.error(errorMsg, (Throwable)e);
            this.logInfoService.save(errorMsg, e.toString(), "2");
        }
        return "redirect:/ufmMonitor/list";
    }
}
