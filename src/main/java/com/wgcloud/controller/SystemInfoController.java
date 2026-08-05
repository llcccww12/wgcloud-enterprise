/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.controller;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.github.pagehelper.PageInfo;
import com.wgcloud.config.CommonConfig;
import com.wgcloud.dto.NetIoStateDto;
import com.wgcloud.dto.SubtitleDto;
import com.wgcloud.entity.AccountInfo;
import com.wgcloud.entity.AppExceptionInfo;
import com.wgcloud.entity.CpuTemperatures;
import com.wgcloud.entity.DiskIo;
import com.wgcloud.entity.DiskIoState;
import com.wgcloud.entity.DiskSmart;
import com.wgcloud.entity.DiskState;
import com.wgcloud.entity.GpuState;
import com.wgcloud.entity.HostDiskPer;
import com.wgcloud.entity.HostMacInfo;
import com.wgcloud.entity.HostPciInfo;
import com.wgcloud.entity.SystemInfo;
import com.wgcloud.entity.CpuState;
import com.wgcloud.entity.MemState;
import com.wgcloud.entity.SysLoadState;
import com.wgcloud.entity.NetIoState;
import com.wgcloud.service.AccountInfoService;
import com.wgcloud.service.CpuStateService;
import com.wgcloud.service.CpuTemperaturesService;
import com.wgcloud.service.DashboardService;
import com.wgcloud.service.DiskIoService;
import com.wgcloud.service.DiskIoStateService;
import com.wgcloud.service.DiskSmartService;
import com.wgcloud.service.DiskStateService;
import com.wgcloud.service.ExcelExportService;
import com.wgcloud.service.GpuStateService;
import com.wgcloud.service.HostDiskPerService;
import com.wgcloud.service.HostGroupService;
import com.wgcloud.service.HostMacInfoService;
import com.wgcloud.service.HostPciInfoService;
import com.wgcloud.service.HostUsersService;
import com.wgcloud.service.HostWarnDiyService;
import com.wgcloud.service.LogInfoService;
import com.wgcloud.service.MemStateService;
import com.wgcloud.service.NetIoStateService;
import com.wgcloud.service.SysLoadStateService;
import com.wgcloud.service.SystemInfoExtService;
import com.wgcloud.service.SystemInfoService;
import com.wgcloud.util.DateUtil;
import com.wgcloud.util.FormatUtil;
import com.wgcloud.util.HostUtil;
import com.wgcloud.util.PageUtil;
import com.wgcloud.util.ResDataUtils;
import com.wgcloud.util.ThreadPoolUtil;
import com.wgcloud.util.TokenUtils;
import com.wgcloud.util.license.LicenseUtil;
import com.wgcloud.util.staticvar.StaticKeys;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
@RequestMapping(value={"/systemInfo"})
public class SystemInfoController {
    private static final Logger logger = LoggerFactory.getLogger(SystemInfoController.class);
    @Resource
    private SystemInfoService systemInfoService;
    @Resource
    private LogInfoService logInfoService;
    @Resource
    private DashboardService dashboardService;
    @Resource
    private CpuStateService cpuStateService;
    @Resource
    private DiskStateService diskStateService;
    @Resource
    private DiskIoService diskIoService;
    @Autowired
    private DiskIoStateService diskIoStateService;
    @Resource
    private HostUsersService hostUsersService;
    @Autowired
    private HostMacInfoService hostMacInfoService;
    @Resource
    private DiskSmartService diskSmartService;
    @Resource
    private CpuTemperaturesService cpuTemperaturesService;
    @Resource
    private HostDiskPerService hostDiskPerService;
    @Resource
    private MemStateService memStateService;
    @Resource
    private NetIoStateService netIoStateService;
    @Resource
    private SysLoadStateService sysLoadStateService;
    @Resource
    private ExcelExportService excelExportService;
    @Resource
    private SystemInfoExtService systemInfoExtService;
    @Resource
    private HostWarnDiyService hostWarnDiyService;
    @Resource
    private AccountInfoService accountInfoService;
    @Resource
    private HostGroupService hostGroupService;
    @Autowired
    private GpuStateService gpuStateService;
    @Autowired
    private HostPciInfoService hostPciInfoService;
    @Autowired
    private TokenUtils tokenUtils;
    @Autowired
    private CommonConfig commonConfig;

    private void testThread() {
        Runnable runnable = () -> logger.info("SystemInfoController----------testThread");
        ThreadPoolUtil.executor.execute(runnable);
    }

    @ResponseBody
    @RequestMapping(value={"agentList"})
    public String agentList(@RequestBody String paramBean) {
        JSONObject agentJsonObject = (JSONObject)JSONUtil.parse((Object)paramBean);
        String checkResult = this.tokenUtils.preOpenDataAPICheck(agentJsonObject);
        if (!StringUtils.isEmpty((CharSequence)checkResult)) {
            return checkResult;
        }
        try {
            HashMap<String, Object> params = new HashMap<String, Object>();
            if (!StringUtils.isEmpty((CharSequence)agentJsonObject.getStr("hostname"))) {
                params.put("hostname", agentJsonObject.getStr("hostname").trim());
            }
            if (!StringUtils.isEmpty((CharSequence)agentJsonObject.getStr("account"))) {
                params.put("account", agentJsonObject.getStr("account").trim());
            }
            if (!StringUtils.isEmpty((CharSequence)agentJsonObject.getStr("orderBy"))) {
                params.put("orderBy", agentJsonObject.getStr("orderBy"));
                params.put("orderType", agentJsonObject.getStr("orderType"));
            }
            PageInfo pageInfo = this.systemInfoService.selectByParams(params, agentJsonObject.getInt("page"), agentJsonObject.getInt("pageSize"));
            for (SystemInfo systemInfo1 : (java.util.List<SystemInfo>) pageInfo.getList()) {
                HostUtil.viewBiosBoardHandler(systemInfo1);
            }
            return ResDataUtils.resetSuccessJson(pageInfo);
        }
        catch (Exception e) {
            logger.error("agent\u83b7\u53d6\u4e3b\u673a\u5217\u8868\u4fe1\u606f\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("agent\u83b7\u53d6\u4e3b\u673a\u5217\u8868\u4fe1\u606f\u9519\u8bef", e.toString(), "2");
            return ResDataUtils.resetErrorJson(e.toString());
        }
    }

    @ResponseBody
    @RequestMapping(value={"save"})
    public String saveSystemInfo(SystemInfo SystemInfo2, Model model, HttpServletRequest request) {
        try {
            if (!StringUtils.isEmpty((CharSequence)SystemInfo2.getId())) {
                SystemInfo ho = this.systemInfoService.selectById(SystemInfo2.getId());
                ho.setRemark(SystemInfo2.getRemark());
                this.systemInfoService.updateById(ho);
                this.logInfoService.save(HostUtil.getAccountByRequest(request).getAccount() + "\u4fee\u6539\u4e3b\u673a\u5907\u6ce8\uff1a" + ho.getHostname(), "\u4e3b\u673a\u5907\u6ce8\uff1a" + ho.getRemark(), "2");
            }
        }
        catch (Exception e) {
            logger.error("\u4fdd\u5b58\u4e3b\u673a\u5907\u6ce8\u4fe1\u606f\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("\u4fdd\u5b58\u4e3b\u673a\u5907\u6ce8\u4fe1\u606f\u9519\u8bef", e.toString(), "2");
        }
        return "redirect:/systemInfo/systemInfoList";
    }

    @ResponseBody
    @RequestMapping(value={"saveWinConsole"})
    public String saveWinConsole(SystemInfo SystemInfo2, Model model, HttpServletRequest request) {
        try {
            if (!StringUtils.isEmpty((CharSequence)SystemInfo2.getId())) {
                SystemInfo ho = this.systemInfoService.selectById(SystemInfo2.getId());
                ho.setWinConsole(SystemInfo2.getWinConsole());
                this.systemInfoService.updateById(ho);
                this.logInfoService.save(HostUtil.getAccountByRequest(request).getAccount() + "\u4fee\u6539\u4e3b\u673a\u670d\u52a1\uff1a" + ho.getHostname(), "\u4e3b\u673a\u670d\u52a1\uff1a" + ho.getWinConsole(), "2");
            }
        }
        catch (Exception e) {
            logger.error("\u4fdd\u5b58\u4e3b\u673a\u670d\u52a1url\u4fe1\u606f\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("\u4fdd\u5b58\u4e3b\u673a\u4e3b\u673a\u670d\u52a1url\u4fe1\u606f\u9519\u8bef", e.toString(), "2");
        }
        return SystemInfo2.getWinConsole();
    }

    @ResponseBody
    @RequestMapping(value={"saveHostListHideCols"})
    public String saveHostListHideCols(Model model, HttpServletRequest request) {
        try {
            Object[] hostListHideCols = request.getParameterValues("hostListHideCols");
            if (null != hostListHideCols) {
                request.getSession().setAttribute("HostListHideColsInfo", (Object)StringUtils.join((Object[])hostListHideCols, (String)","));
            } else {
                request.getSession().setAttribute("HostListHideColsInfo", "");
            }
        }
        catch (Exception e) {
            logger.error("\u4fdd\u5b58\u4e3b\u673a\u5217\u8868\u9700\u8981\u9690\u85cf\u7684\u5217\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("\u4fdd\u5b58\u4e3b\u673a\u5217\u8868\u9700\u8981\u9690\u85cf\u7684\u5217\u9519\u8bef", e.toString(), "2");
        }
        return "redirect:/systemInfo/systemInfoList";
    }

    @ResponseBody
    @RequestMapping(value={"saveGroupId"})
    public String saveGroupId(Model model, HttpServletRequest request) {
        try {
            String ids = request.getParameter("ids");
            String[] groupIdsArr = request.getParameterValues("groupId");
            this.systemInfoService.saveGroupId(ids, groupIdsArr, request);
        }
        catch (Exception e) {
            logger.error("\u4fdd\u5b58\u4e3b\u673a\u6807\u7b7e\u4fe1\u606f\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("\u4fdd\u5b58\u4e3b\u673a\u6807\u7b7e\u4fe1\u606f\u9519\u8bef", e.toString(), "2");
        }
        return "redirect:/systemInfo/systemInfoList";
    }

    @RequestMapping(value={"updateActive"})
    public String updateActive(Model model, HttpServletRequest request) {
        String errorMsg = "\u4e3b\u673a\u5f00\u59cb\u76d1\u63a7\u548c\u505c\u6b62\u76d1\u63a7\u9519\u8bef";
        try {
            this.systemInfoService.updateActive(request);
        }
        catch (Exception e) {
            logger.error(errorMsg, (Throwable)e);
            this.logInfoService.save(errorMsg, e.toString(), "2");
        }
        return "redirect:/systemInfo/systemInfoList";
    }

    @RequestMapping(value={"updateOrderNum"})
    public String updateOrderNum(Model model, HttpServletRequest request) {
        String errorMsg = "\u8bbe\u7f6e\u4e3b\u673a\u6392\u5e8f\u5e8f\u53f7\u9519\u8bef";
        try {
            this.systemInfoService.updateOrderNum(request);
        }
        catch (Exception e) {
            logger.error(errorMsg, (Throwable)e);
            this.logInfoService.save(errorMsg, e.toString(), "2");
        }
        return "redirect:/systemInfo/systemInfoList";
    }

    @RequestMapping(value={"updateCountBlock"})
    public String updateCountBlock(Model model, HttpServletRequest request) {
        String errorMsg = "\u4e3b\u673a\u52a0\u5165\u770b\u677f\u7edf\u8ba1\u64cd\u4f5c\u9519\u8bef";
        try {
            this.systemInfoService.updateCountBlock(request);
        }
        catch (Exception e) {
            logger.error(errorMsg, (Throwable)e);
            this.logInfoService.save(errorMsg, e.toString(), "2");
        }
        return "redirect:/systemInfo/systemInfoList";
    }

    @RequestMapping(value={"systemInfoList"})
    public String systemInfoList(SystemInfo systemInfo, Model model, HttpServletRequest request) {
        HashMap<String, Object> params = new HashMap<String, Object>();
        try {
            StringBuffer url = new StringBuffer();
            String hostname = null;
            if (!StringUtils.isEmpty((CharSequence)systemInfo.getHostname())) {
                hostname = systemInfo.getHostname();
                params.put("hostname", hostname.trim());
                url.append("&hostname=").append(hostname);
            }
            if (!StringUtils.isEmpty((CharSequence)systemInfo.getState())) {
                params.put("state", systemInfo.getState());
                url.append("&state=").append(systemInfo.getState());
            }
            if (!StringUtils.isEmpty((CharSequence)systemInfo.getActive())) {
                if ("1".equals(systemInfo.getActive())) {
                    params.put("activeNe", "2");
                } else {
                    params.put("active", systemInfo.getActive());
                }
                url.append("&active=").append(systemInfo.getActive());
            }
            if (!StringUtils.isEmpty((CharSequence)systemInfo.getAccount())) {
                params.put("account", systemInfo.getAccount());
                url.append("&account=").append(systemInfo.getAccount());
            }
            if (!StringUtils.isEmpty((CharSequence)systemInfo.getGroupId())) {
                params.put("groupId", systemInfo.getGroupId());
                url.append("&groupId=").append(systemInfo.getGroupId());
            }
            if (!StringUtils.isEmpty((CharSequence)systemInfo.getOrderBy())) {
                params.put("orderBy", systemInfo.getOrderBy());
                params.put("orderType", systemInfo.getOrderType());
                url.append("&orderBy=").append(systemInfo.getOrderBy());
                url.append("&orderType=").append(systemInfo.getOrderType());
            }
            if (request.getParameter("dashView") != null) {
                url.append("&dashView=1");
            }
            LicenseUtil.checkHostList(systemInfo, model);
            HostUtil.addAccountquery(request, params);
            PageInfo pageInfo = this.systemInfoService.selectByParams(params, systemInfo.getPage(), systemInfo.getPageSize());
            if ("true".equals(this.commonConfig.getUserInfoManage())) {
                params.clear();
                List<AccountInfo> accountInfoList = this.accountInfoService.selectAllByParams(params);
                model.addAttribute("accountList", accountInfoList);
            }
            this.systemInfoService.hostAddVal((PageInfo<SystemInfo>)pageInfo, model, request);
            if (request.getParameter("dashView") != null) {
                this.systemInfoService.hideLeftIp((PageInfo<SystemInfo>)pageInfo, request);
            }
            PageUtil.initPageNumber(pageInfo, model);
            model.addAttribute("pageUrl", (Object)("/systemInfo/systemInfoList?1=1" + url.toString()));
            model.addAttribute("page", (Object)pageInfo);
            model.addAttribute("systemInfo", (Object)systemInfo);
            if (null == request.getSession().getAttribute("HostListHideColsInfo")) {
                request.getSession().setAttribute("HostListHideColsInfo", "18");
            }
        }
        catch (Exception e) {
            logger.error("\u67e5\u8be2\u4e3b\u673a\u5217\u8868\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("\u67e5\u8be2\u4e3b\u673a\u5217\u8868\u9519\u8bef", e.toString(), "2");
        }
        if (request.getParameter("dashView") != null) {
            model.addAttribute("dashViewListAutoData", (Object)this.commonConfig.getDashViewListAutoData());
            return "dashView/list";
        }
        return "host/list";
    }

    @ResponseBody
    @RequestMapping(value={"systemInfoListAjax"})
    public String systemInfoListAjax(SystemInfo systemInfo, Model model, HttpServletRequest request) {
        HashMap<String, Object> params = new HashMap<String, Object>();
        try {
            List<SystemInfo> systemInfoList = this.systemInfoService.selectAllByParamsForTask(params);
            for (SystemInfo sys : systemInfoList) {
                if ("2".equals(sys.getState())) {
                    sys.setHostname("<span  class='badge bg-danger'>" + FormatUtil.getString(sys.getHostname(), 20) + "</span>");
                }
                if ("1".equals(sys.getState())) {
                    sys.setHostname("<span  class='badge bg-success'>" + FormatUtil.getString(sys.getHostname(), 20) + "</span>");
                }
                if (sys.getMemPer() >= 90.0) {
                    sys.setImage("<span class='badge bg-danger'>" + sys.getMemPer() + "</span>");
                }
                if (sys.getMemPer() >= 70.0 && sys.getMemPer() < 90.0) {
                    sys.setImage("<span class='badge bg-warning'>" + sys.getMemPer() + "</span>");
                }
                if (sys.getMemPer() < 70.0) {
                    sys.setImage("<span class='badge bg-primary'>" + sys.getMemPer() + "</span>");
                }
                if (sys.getCpuPer() >= 90.0) {
                    sys.setHostnameExt("<span class='badge bg-danger'>" + sys.getCpuPer() + "</span>");
                }
                if (sys.getCpuPer() >= 70.0 && sys.getCpuPer() < 90.0) {
                    sys.setHostnameExt("<span class='badge bg-warning'>" + sys.getCpuPer() + "</span>");
                }
                if (sys.getCpuPer() < 70.0) {
                    sys.setHostnameExt("<span class='badge bg-primary'>" + sys.getCpuPer() + "</span>");
                }
                sys.setRxbyt(FormatUtil.kbToM(sys.getRxbyt()) + "/s");
                sys.setTxbyt(FormatUtil.kbToM(sys.getTxbyt()) + "/s");
                sys.setRemark(DateUtil.getDateTimeString(sys.getCreateTime()));
            }
            return JSONUtil.toJsonStr(systemInfoList);
        }
        catch (Exception e) {
            logger.error("ajax\u67e5\u8be2\u4e3b\u673a\u5217\u8868\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("ajax\u67e5\u8be2\u4e3b\u673a\u5217\u8868\u9519\u8bef", e.toString(), "2");
            return "";
        }
    }

    @RequestMapping(value={"detail"})
    public String hostDetail(Model model, HttpServletRequest request) {
        String id = request.getParameter("id");
        if (StringUtils.isEmpty((CharSequence)id)) {
            return "error/500";
        }
        try {
            SystemInfo systemInfo = this.systemInfoService.selectById(id);
            model.addAttribute("systemInfo", (Object)systemInfo);
            HostUtil.setSysFontAwesome(systemInfo);
            HashMap<String, Object> params = new HashMap<String, Object>();
            params.put("hostname", systemInfo.getHostname());
            List<DiskState> diskStateList = this.diskStateService.selectAllByParams(params);
            model.addAttribute("diskStateList", diskStateList);
            this.diskStateService.computeDaysForDisk(diskStateList, systemInfo.getHostname());
            HostUtil.setDiskListSumSize(diskStateList);
            this.diskStateService.setWarnDisk(systemInfo, model);
            List<DiskIo> diskIoList = this.diskIoService.selectAllByParams(params);
            model.addAttribute("diskIoList", diskIoList);
            List<DiskIoState> diskIoStateList = this.diskIoStateService.selectAllByParamsAndDay(systemInfo.getHostname(), model, request);
            if (!StaticKeys.LICENSE_STATE.equals("1")) {
                diskIoStateList = new ArrayList<DiskIoState>();
            }
            model.addAttribute("diskIoStateList", diskIoStateList);
            this.diskIoStateService.findMaxVal(diskIoStateList, model);
            this.diskIoStateService.setWarnValue(systemInfo, model);
            List<DiskSmart> diskSmartList = this.diskSmartService.selectAllByParams(params);
            model.addAttribute("diskSmartList", diskSmartList);
            List<CpuTemperatures> cpuTemperaturesList = this.cpuTemperaturesService.selectAllByParams(params);
            model.addAttribute("cpuTemperaturesList", cpuTemperaturesList);
            this.cpuTemperaturesService.setWarnValue(systemInfo, model);
            HostUtil.viewLastUserInfoHandler(model, id);
            List<HostMacInfo> hostMacInfoList = this.hostMacInfoService.selectAllByParams(params);
            model.addAttribute("hostMacInfoList", hostMacInfoList);
            List<HostDiskPer> hostDiskPerList = this.hostDiskPerService.selectAllByParams(params);
            this.hostDiskPerService.setSubtitle(model, hostDiskPerList);
            model.addAttribute("hostDiskPerList", (Object)JSONUtil.parseArray(hostDiskPerList));
            HostUtil.viewAllNetworkHandler(model, systemInfo.getHostname());
            HostUtil.viewBiosBoardHandler(systemInfo);
            if (request.getParameter("dashView") != null) {
                this.systemInfoService.hideLeftIp(systemInfo, request);
            }
            if ("true".equals(this.commonConfig.getHostGroup())) {
                systemInfo.setGroupId(this.hostGroupService.returnGroupNames(systemInfo.getGroupId()));
            }
        }
        catch (Exception e) {
            logger.error("\u4e3b\u673a\u8be6\u7ec6\u4fe1\u606f\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("\u4e3b\u673a\u8be6\u7ec6\u4fe1\u606f\u9519\u8bef", e.toString(), "2");
        }
        if (request.getParameter("dashView") != null) {
            return "dashView/view";
        }
        return "host/view";
    }

    @RequestMapping(value={"viewAllProcess"})
    public String viewAllProcess(AppExceptionInfo appExceptionInfo, Model model, HttpServletRequest request) {
        String id = request.getParameter("id");
        if (StringUtils.isEmpty((CharSequence)id)) {
            return "error/500";
        }
        try {
            HostUtil.viewAllProcessHandler(appExceptionInfo, model, id);
        }
        catch (Exception e) {
            logger.error("\u4e3b\u673a\u5168\u91cf\u8fdb\u7a0b\u4fe1\u606f\u67e5\u770b\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("\u4e3b\u673a\u5168\u91cf\u8fdb\u7a0b\u4fe1\u606f\u67e5\u770b\u9519\u8bef", e.toString(), "2");
        }
        return "host/viewAllProcess";
    }

    @RequestMapping(value={"viewImportInfo"})
    public String viewImportInfo(Model model, HttpServletRequest request) {
        String id = request.getParameter("id");
        if (StringUtils.isEmpty((CharSequence)id)) {
            return "error/500";
        }
        try {
            HostUtil.viewImportInfoHandler(model, id);
            SystemInfo systemInfo = this.systemInfoService.selectById(id);
            if (null != systemInfo) {
                this.gpuStateService.loadChartData(systemInfo.getHostname(), model);
            }
        }
        catch (Exception e) {
            logger.error("\u4e3b\u673aGPU\u3001\u9632\u706b\u5899\u3001CRONTAB\u4fe1\u606f\u67e5\u770b\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("\u4e3b\u673aGPU\u3001\u9632\u706b\u5899\u3001CRONTAB\u4fe1\u606f\u67e5\u770b\u9519\u8bef", e.toString(), "2");
        }
        return "host/viewGpuInfo";
    }

    @RequestMapping(value={"viewAllPortInfo"})
    public String viewAllPortInfo(Model model, HttpServletRequest request) {
        String id = request.getParameter("id");
        if (StringUtils.isEmpty((CharSequence)id)) {
            return "error/500";
        }
        try {
            HostUtil.viewAllPortInfoHandler(model, id);
        }
        catch (Exception e) {
            logger.error("\u4e3b\u673a\u5168\u91cf\u7aef\u53e3\u4fe1\u606f\u67e5\u770b\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("\u4e3b\u673a\u5168\u91cf\u7aef\u53e3\u4fe1\u606f\u67e5\u770b\u9519\u8bef", e.toString(), "2");
        }
        return "host/viewAllPort";
    }

    @RequestMapping(value={"viewWinServices"})
    public String viewWinServices(Model model, HttpServletRequest request) {
        String id = request.getParameter("id");
        if (StringUtils.isEmpty((CharSequence)id)) {
            return "error/500";
        }
        try {
            HostUtil.viewWinServicesHandler(model, id);
        }
        catch (Exception e) {
            logger.error("\u4e3b\u673awindows\u7cfb\u7edf\u670d\u52a1\u4fe1\u606f\u67e5\u770b\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("\u4e3b\u673awindows\u7cfb\u7edf\u670d\u52a1\u4fe1\u606f\u67e5\u770b\u9519\u8bef", e.toString(), "2");
        }
        return "host/viewWinServices";
    }

    @RequestMapping(value={"viewPCIData"})
    public String viewPCIData(Model model, HttpServletRequest request) {
        String id = request.getParameter("id");
        if (StringUtils.isEmpty((CharSequence)id)) {
            return "error/500";
        }
        try {
            SystemInfo systemInfo = this.systemInfoService.selectById(id);
            if (null != systemInfo) {
                model.addAttribute("systemInfo", (Object)systemInfo);
                HostUtil.setSysFontAwesome(systemInfo);
                HashMap<String, Object> params = new HashMap<String, Object>();
                params.put("hostname", systemInfo.getHostname());
                List<HostPciInfo> list = this.hostPciInfoService.selectAllByParams(params);
                model.addAttribute("pciDataList", list);
                model.addAttribute("cacheListSize", (Object)list.size());
            }
        }
        catch (Exception e) {
            logger.error("\u4e3b\u673aPCI\u6570\u636e\u67e5\u770b\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("\u4e3b\u673aPCI\u6570\u636e\u67e5\u770b\u9519\u8bef", e.toString(), "2");
        }
        return "host/viewPCIData";
    }

    @RequestMapping(value={"viewIfconfigInfo"})
    public String viewIfconfigInfo(Model model, HttpServletRequest request) {
        String id = request.getParameter("id");
        if (StringUtils.isEmpty((CharSequence)id)) {
            return "error/500";
        }
        try {
            HostUtil.viewIfconfigInfoHandler(model, id);
        }
        catch (Exception e) {
            logger.error("\u4e3b\u673aifconfig\u4fe1\u606f\u67e5\u770b\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("\u4e3b\u673aifconfig\u4fe1\u606f\u67e5\u770b\u9519\u8bef", e.toString(), "2");
        }
        return "host/viewIfconfig";
    }

    @RequestMapping(value={"del"})
    public String delete(Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        String errorMsg = "\u5220\u9664\u4e3b\u673a\u4fe1\u606f\u9519\u8bef";
        try {
            ArrayList<String> hostnameList = new ArrayList<String>();
            if (!StringUtils.isEmpty((CharSequence)request.getParameter("id"))) {
                String[] ids;
                for (String id : ids = request.getParameter("id").split(",")) {
                    SystemInfo sys = this.systemInfoService.selectById(id);
                    hostnameList.add(sys.getHostname());
                    this.logInfoService.save(HostUtil.getAccountByRequest(request).getAccount() + "\u5220\u9664\u4e3b\u673a\uff1a" + sys.getHostname(), "\u4e3b\u673a\uff1a" + sys.getRemark(), "2");
                }
                this.systemInfoService.deleteById(ids);
                this.systemInfoExtService.deleteByHostname(hostnameList);
            }
        }
        catch (Exception e) {
            logger.error(errorMsg, (Throwable)e);
            this.logInfoService.save(errorMsg, e.toString(), "2");
        }
        return "redirect:/systemInfo/systemInfoList";
    }

    @RequestMapping(value={"chart"})
    public String hostChart(Model model, HttpServletRequest request) {
        String id = request.getParameter("id");
        String startTime = request.getParameter("startTime");
        String endTime = request.getParameter("endTime");
        String am = request.getParameter("am");
        if (StringUtils.isEmpty((CharSequence)id)) {
            logger.error("hostChart id is  null");
            return "error/500";
        }
        try {
            SystemInfo systemInfo = this.systemInfoService.selectById(id);
            model.addAttribute("systemInfo", (Object)systemInfo);
            HostUtil.setSysFontAwesome(systemInfo);
            HashMap<String, Object> params = new HashMap<String, Object>();
            params.put("hostname", systemInfo.getHostname());
            this.dashboardService.setDateParam(am, startTime, endTime, params, model);
            model.addAttribute("amList", this.dashboardService.getAmList());
            List<CpuState> cpuStateList = this.cpuStateService.selectAllByParams(params);
            List<CpuState> cpuStateCompressList = HostUtil.compressChartListData(cpuStateList, model);
            model.addAttribute("cpuStateList", (Object)JSONUtil.parseArray(cpuStateCompressList));
            List<MemState> memStateList = this.memStateService.selectAllByParams(params);
            List<MemState> memStateCompressList = HostUtil.compressChartListData(memStateList, model);
            model.addAttribute("memStateList", (Object)JSONUtil.parseArray(memStateCompressList));
            List<SysLoadState> sysLoadStateList = this.sysLoadStateService.selectAllByParams(params);
            List<SysLoadState> sysLoadStateCompressList = HostUtil.compressChartListData(sysLoadStateList, model);
            model.addAttribute("sysLoadStateList", (Object)JSONUtil.parseArray(sysLoadStateCompressList));
            this.systemInfoService.findLoadMaxVal(sysLoadStateCompressList, model);
            List<NetIoState> netIoStateList = this.netIoStateService.selectAllByParams(params);
            List<NetIoState> netIoStateCompressList = HostUtil.compressChartListData(netIoStateList, model);
            List<NetIoStateDto> netIoStateDtoList = this.systemInfoService.toNetIoStateDto(netIoStateCompressList);
            model.addAttribute("netIoStateList", (Object)JSONUtil.parseArray(netIoStateDtoList));
            this.systemInfoService.findNetIoStateBytMaxVal(netIoStateDtoList, model);
            this.systemInfoService.setSubtitle(model, cpuStateCompressList, memStateCompressList);
            // 注入 GPU 折线图数据（gpuStateList 为 [{gpuName,gpuValue,dateStr},...]，gpuSubtitleDto 为统计副标题）
            this.loadGpuChartData(systemInfo.getHostname(), params, model);
            if (request.getParameter("dashView") != null) {
                this.systemInfoService.hideLeftIp(systemInfo, request);
            }
            this.systemInfoService.setChartWarnElement(systemInfo, model);
        }
        catch (Exception e) {
            logger.error("\u4e3b\u673a\u56fe\u5f62\u62a5\u8868\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("\u4e3b\u673a\u56fe\u5f62\u62a5\u8868\u9519\u8bef", e.toString(), "2");
        }
        if (request.getParameter("dashView") != null) {
            return "dashView/viewChart";
        }
        return "host/viewChart";
    }

    @RequestMapping(value={"chartExcel"})
    public void hostChartExcel(Model model, HttpServletRequest request, HttpServletResponse response) {
        String id = request.getParameter("id");
        String startTime = request.getParameter("startTime");
        String endTime = request.getParameter("endTime");
        String am = request.getParameter("am");
        if (StringUtils.isEmpty((CharSequence)id)) {
            return;
        }
        try {
            if (!StaticKeys.LICENSE_STATE.equals("1")) {
                response.setContentType("text/html;charset=UTF-8");
                response.getOutputStream().write("The module needs to professional version. Please contact us at www.wgstart.com".getBytes());
                return;
            }
            SystemInfo systemInfo = this.systemInfoService.selectById(id);
            model.addAttribute("systemInfo", (Object)systemInfo);
            HashMap<String, Object> params = new HashMap<String, Object>();
            params.put("hostname", systemInfo.getHostname());
            this.dashboardService.setDateParam(am, startTime, endTime, params, model);
            this.excelExportService.exportExcel(params, response);
        }
        catch (Exception e) {
            logger.error("\u4e3b\u673a\u56fe\u5f62\u62a5\u8868\u5bfc\u51faexcel\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("\u4e3b\u673a\u56fe\u5f62\u62a5\u8868\u5bfc\u51faexcel\u9519\u8bef", e.toString(), "2");
        }
    }

    @RequestMapping(value={"hostListExcel"})
    public void hostListExcel(SystemInfo systemInfo, Model model, HttpServletRequest request, HttpServletResponse response) {
        try {
            if (!StaticKeys.LICENSE_STATE.equals("1")) {
                response.setContentType("text/html;charset=UTF-8");
                response.getOutputStream().write("The module needs to professional version. Please contact us at www.wgstart.com".getBytes());
                return;
            }
            HashMap<String, Object> params = new HashMap<String, Object>();
            if (!StringUtils.isEmpty((CharSequence)systemInfo.getState())) {
                params.put("state", systemInfo.getState());
            }
            if (!StringUtils.isEmpty((CharSequence)systemInfo.getGroupId())) {
                params.put("groupId", systemInfo.getGroupId());
            }
            if (!StringUtils.isEmpty((CharSequence)systemInfo.getOrderBy())) {
                params.put("orderBy", systemInfo.getOrderBy());
                params.put("orderType", systemInfo.getOrderType());
            }
            PageInfo pageInfo = new PageInfo();
            if (!StringUtils.isEmpty((CharSequence)request.getParameter("id"))) {
                String[] ids = request.getParameter("id").split(",");
                List<SystemInfo> list = this.systemInfoService.selectByIds(ids);
                pageInfo.setList(list);
            } else {
                HostUtil.addAccountquery(request, params);
                pageInfo = this.systemInfoService.selectByParams(params, 1, 20000);
            }
            this.systemInfoService.hostAddVal((PageInfo<SystemInfo>)pageInfo, model, request);
            this.excelExportService.exportHostListExcel(pageInfo.getList(), response);
        }
        catch (Exception e) {
            logger.error("\u6240\u6709\u4e3b\u673a\u5217\u8868\u5bfc\u51faexcel\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("\u6240\u6709\u4e3b\u673a\u5217\u8868\u5bfc\u51faexcel\u9519\u8bef", e.toString(), "2");
        }
    }

    /**
     * 主机详情图表页装载 GPU 折线图数据。
     * 将 GpuState.gpuRate（多卡逗号分隔）拆为多个 {gpuName,gpuValue,dateStr} 数据点，
     * 注入 model 属性 gpuStateList 和 gpuSubtitleDto，
     * 避免与已存在的 subtitleDto/cpuSubtitleDto 等键冲突。
     */
    private void loadGpuChartData(String hostname, java.util.Map<String, Object> params, Model model) {
        try {
            java.util.HashMap<String, Object> gpuParams = new java.util.HashMap<>();
            gpuParams.put("hostname", hostname);
            if (params.get("startTime") != null) {
                gpuParams.put("startTime", params.get("startTime"));
            }
            if (params.get("endTime") != null) {
                gpuParams.put("endTime", params.get("endTime"));
            }
            List<GpuState> gpuStateList = this.gpuStateService.selectAllByParams(gpuParams);
            ArrayList<JSONObject> resultList = new ArrayList<>();
            Double maxValue = 0.0;
            Double minValue = 1000.0;
            Double sumValue = 0.0;
            int count = 0;
            int maxCardIndex = -1;
            if (gpuStateList != null) {
                for (GpuState gpuState : gpuStateList) {
                    String dataStr = gpuState.getGpuRate();
                    if (StringUtils.isEmpty((CharSequence) dataStr)) continue;
                    String[] dataArray = dataStr.split(",");
                    for (int i = 0; i < dataArray.length; ++i) {
                        if (StringUtils.isEmpty((CharSequence) dataArray[i])) continue;
                        double v;
                        try {
                            v = Double.parseDouble(dataArray[i]);
                        } catch (Exception ex) {
                            continue;
                        }
                        if (v > maxValue) maxValue = v;
                        if (v < minValue) minValue = v;
                        sumValue = sumValue + v;
                        ++count;
                        if (i > maxCardIndex) maxCardIndex = i;
                        JSONObject jo = new JSONObject();
                        jo.set("gpuName", (Object) ("GPU-" + i));
                        jo.set("gpuValue", (Object) v);
                        jo.set("dateStr", (Object) gpuState.getDateStr());
                        resultList.add(jo);
                    }
                }
            }
            Double avgValue;
            if (count > 0) {
                avgValue = sumValue / (double) count;
            } else {
                avgValue = 0.0;
                minValue = 0.0;
                maxValue = 100.0;
            }
            model.addAttribute("gpuStateList", (Object) JSONUtil.parseArray(resultList));
            SubtitleDto gpuSubtitleDto = new SubtitleDto();
            gpuSubtitleDto.setAvgValue(FormatUtil.formatDouble(avgValue, 2) + "");
            gpuSubtitleDto.setMaxValue(maxValue + "");
            gpuSubtitleDto.setMinValue(minValue + "");
            model.addAttribute("gpuSubtitleDto", (Object) gpuSubtitleDto);
            model.addAttribute("gpuCardCount", (Object) (maxCardIndex + 1));
        } catch (Exception e) {
            logger.error("\u88c5\u8f7d GPU \u56fe\u8868\u6570\u636e\u9519\u8bef", (Throwable) e);
            model.addAttribute("gpuStateList", (Object) JSONUtil.parseArray(new ArrayList<JSONObject>()));
            SubtitleDto gpuSubtitleDto = new SubtitleDto();
            gpuSubtitleDto.setAvgValue("0");
            gpuSubtitleDto.setMaxValue("0");
            gpuSubtitleDto.setMinValue("0");
            model.addAttribute("gpuSubtitleDto", (Object) gpuSubtitleDto);
            model.addAttribute("gpuCardCount", (Object) 0);
        }
    }
}

