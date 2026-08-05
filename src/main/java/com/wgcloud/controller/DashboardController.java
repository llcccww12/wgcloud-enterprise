/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.controller;

import com.wgcloud.config.CommonConfig;
import com.wgcloud.entity.AccountInfo;
import com.wgcloud.service.DashboardService;
import com.wgcloud.service.DbTableService;
import com.wgcloud.service.LogInfoService;
import com.wgcloud.service.SystemInfoService;
import com.wgcloud.service.TaskUtilService;
import com.wgcloud.util.FormatUtil;
import com.wgcloud.util.HostUtil;
import com.wgcloud.util.ThreadPoolUtil;
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
@RequestMapping(value={"/dash"})
public class DashboardController {
    private static final Logger logger = LoggerFactory.getLogger(DashboardController.class);
    @Resource
    private DbTableService dbTableService;
    @Resource
    private SystemInfoService systemInfoService;
    @Resource
    private LogInfoService logInfoService;
    @Autowired
    private TaskUtilService taskUtilService;
    @Resource
    private DashboardService dashboardService;
    @Autowired
    CommonConfig commonConfig;

    private void testThread() {
        Runnable runnable = () -> logger.info("DashboardCotroller----------testThread");
        ThreadPoolUtil.executor.execute(runnable);
    }

    @RequestMapping(value={"main"})
    public String mainList(Model model, HttpServletRequest request) {
        HashMap<String, Object> params = new HashMap<String, Object>();
        int totalSystemInfoSize = 0;
        try {
            HostUtil.addAccountquery(request, params);
            params.put("countBlockNe", "2");
            totalSystemInfoSize = this.systemInfoService.countByParams(params);
            model.addAttribute("totalSystemInfoSize", (Object)totalSystemInfoSize);
            params.put("state", "1");
            int hostOnLineSize = this.systemInfoService.countByParams(params);
            model.addAttribute("hostOnLineSize", (Object)hostOnLineSize);
            double hostOnLinePer = 0.0;
            if (totalSystemInfoSize != 0) {
                hostOnLinePer = (double)hostOnLineSize / (double)totalSystemInfoSize;
            }
            model.addAttribute("hostOnLinePer", (Object)FormatUtil.formatDouble(hostOnLinePer * 100.0, 2));
            this.dashboardService.setPieChart(model, totalSystemInfoSize, request);
            this.dashboardService.setMiddleData(model, request);
            this.dashboardService.setMiddleApplicationData(model);
            model.addAttribute("dbTableList", (Object)this.dashboardService.dbDceHeathInfoHandle(request));
            AccountInfo accountInfo = HostUtil.getAccountByRequest(request);
            if (null != accountInfo && "user".equals(accountInfo.getRole())) {
                model.addAttribute("sumDiskSizeCache", (Object)this.taskUtilService.sumDiskSizeCache(request));
            }
            this.dashboardService.setDashboardTopData(model, totalSystemInfoSize, request);
        }
        catch (Exception e) {
            logger.error("\u76d1\u63a7\u6982\u8981\u9875\u9762\u4fe1\u606f\u5f02\u5e38", (Throwable)e);
            this.logInfoService.save("\u76d1\u63a7\u6982\u8981\u9875\u9762\u4fe1\u606f\u5f02\u5e38", e.toString(), "2");
        }
        if (request.getParameter("dashView") != null) {
            model.addAttribute("dashViewAutoData", (Object)this.commonConfig.getDashViewAutoData());
            return "dashView/index";
        }
        LicenseUtil.outDateAlter(model, totalSystemInfoSize);
        return "index";
    }

    @RequestMapping(value={"hostDraw"})
    public String hostDraw(Model model, HttpServletRequest request) {
        String id = request.getParameter("id");
        if (StringUtils.isEmpty((CharSequence)id)) {
            return "error/500";
        }
        try {
            this.dashboardService.hostDraw(model, id, request);
        }
        catch (Exception e) {
            logger.error("\u4e3b\u673a\u753b\u50cf\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("\u4e3b\u673a\u753b\u50cf\u9519\u8bef", e.toString(), "2");
        }
        return "hostDraw/view";
    }
}

