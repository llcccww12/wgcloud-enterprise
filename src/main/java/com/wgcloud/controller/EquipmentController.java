/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.controller;

import com.github.pagehelper.PageInfo;
import com.wgcloud.config.CommonConfig;
import com.wgcloud.entity.AccountInfo;
import com.wgcloud.entity.Equipment;
import com.wgcloud.entity.SystemInfo;
import com.wgcloud.service.AccountInfoService;
import com.wgcloud.service.EquipmentService;
import com.wgcloud.service.ExcelExportService;
import com.wgcloud.service.HostGroupService;
import com.wgcloud.service.LogInfoService;
import com.wgcloud.service.SystemInfoService;
import com.wgcloud.util.HostUtil;
import com.wgcloud.util.PageUtil;
import com.wgcloud.util.license.LicenseUtil;
import com.wgcloud.util.staticvar.StaticKeys;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value={"/equipment"})
public class EquipmentController {
    private static final Logger logger = LoggerFactory.getLogger(EquipmentController.class);
    @Resource
    private EquipmentService equipmentService;
    @Resource
    private AccountInfoService accountInfoService;
    @Resource
    private LogInfoService logInfoService;
    @Resource
    private ExcelExportService excelExportService;
    @Resource
    private SystemInfoService systemInfoService;
    @Resource
    private HostGroupService hostGroupService;
    @Autowired
    private CommonConfig commonConfig;

    @RequestMapping(value={"list"})
    public String equipmentList(Equipment equipment, Model model, HttpServletRequest request) {
        HashMap<String, Object> params = new HashMap<String, Object>();
        try {
            LicenseUtil.maxLicense_10(model, request, equipment);
            StringBuffer url = new StringBuffer();
            String hostname = null;
            if (!StringUtils.isEmpty((CharSequence)equipment.getName())) {
                hostname = equipment.getName();
                params.put("name", hostname.trim());
                url.append("&name=").append(hostname);
            }
            if (!StringUtils.isEmpty((CharSequence)equipment.getAccount())) {
                params.put("account", equipment.getAccount());
                url.append("&account=").append(equipment.getAccount());
            }
            if (!StringUtils.isEmpty((CharSequence)equipment.getGroupId())) {
                params.put("groupId", equipment.getGroupId());
                url.append("&groupId=").append(equipment.getGroupId());
            }
            if (!StringUtils.isEmpty((CharSequence)equipment.getHostname())) {
                hostname = equipment.getHostname();
                params.put("hostname", hostname.trim());
                url.append("&hostname=").append(hostname);
            }
            HostUtil.addAccountquery(request, params);
            PageInfo pageInfo = this.equipmentService.selectByParams(params, equipment.getPage(), equipment.getPageSize());
            PageUtil.initPageNumber(pageInfo, model);
            if ("true".equals(this.commonConfig.getHostGroup())) {
                this.equipmentService.setGroupInList(pageInfo.getList(), model, request);
            }
            HostUtil.addAccountListModel(model);
            model.addAttribute("pageUrl", (Object)("/equipment/list?1=1" + url.toString()));
            model.addAttribute("page", (Object)pageInfo);
            model.addAttribute("equipment", (Object)equipment);
        }
        catch (Exception e) {
            logger.error("\u67e5\u8be2\u8d44\u4ea7\u4fe1\u606f\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("\u67e5\u8be2\u8d44\u4ea7\u4fe1\u606f\u9519\u8bef", e.toString(), "2");
        }
        return "equipment/list";
    }

    @RequestMapping(value={"save"})
    public String saveEquipment(@RequestParam(value="file") MultipartFile file, Equipment equipment, Model model, HttpServletRequest request) {
        try {
            if (StringUtils.isEmpty((CharSequence)equipment.getId())) {
                AccountInfo accountInfo = HostUtil.getAccountByRequest(request);
                if (null != accountInfo && !"admin".equals(accountInfo.getRole())) {
                    equipment.setAccount(accountInfo.getAccount());
                }
                this.equipmentService.saveAttachFile(file, equipment);
                this.equipmentService.save(equipment);
                this.equipmentService.saveLog(request, "\u6dfb\u52a0", equipment);
            } else {
                this.equipmentService.saveAttachFile(file, equipment);
                this.equipmentService.updateById(equipment);
                this.equipmentService.saveLog(request, "\u4fee\u6539", equipment);
            }
        }
        catch (Exception e) {
            logger.error("\u4fdd\u5b58\u8d44\u4ea7\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("\u4fdd\u5b58\u8d44\u4ea7\u9519\u8bef", e.toString(), "2");
        }
        return "redirect:/equipment/list";
    }

    @RequestMapping(value={"edit"})
    public String edit(Model model, HttpServletRequest request) {
        String errorMsg = "\u6dfb\u52a0\u8d44\u4ea7";
        String id = request.getParameter("id");
        Equipment Equipment2 = new Equipment();
        try {
            HashMap<String, Object> params = new HashMap<String, Object>();
            List<SystemInfo> systemInfoList = this.systemInfoService.selectAllByParams(params);
            model.addAttribute("systemInfoList", systemInfoList);
            if (StringUtils.isEmpty((CharSequence)id)) {
                model.addAttribute("equipment", (Object)Equipment2);
                if (!this.isAddContinue()) {
                    return "redirect:/equipment/list?liceFlage=1";
                }
                return "equipment/add";
            }
            Equipment2 = this.equipmentService.selectById(id);
            model.addAttribute("equipment", (Object)Equipment2);
        }
        catch (Exception e) {
            logger.error(errorMsg, (Throwable)e);
            this.logInfoService.save(errorMsg, e.toString(), "2");
        }
        return "equipment/add";
    }

    @RequestMapping(value={"copyEquipment"})
    public String copyEquipment(Model model, HttpServletRequest request) {
        String errorMsg = "\u6dfb\u52a0\u8d44\u4ea7";
        String id = request.getParameter("id");
        try {
            if (StringUtils.isEmpty((CharSequence)id)) {
                return "redirect:/equipment/list";
            }
            if (!this.isAddContinue()) {
                return "redirect:/equipment/list?liceFlage=1";
            }
            Equipment equipment = this.equipmentService.selectById(id);
            equipment.setId(null);
            equipment.setName(equipment.getName() + "_1");
            this.equipmentService.save(equipment);
            this.equipmentService.saveLog(request, "\u6dfb\u52a0", equipment);
        }
        catch (Exception e) {
            logger.error(errorMsg, (Throwable)e);
            this.logInfoService.save(errorMsg, e.toString(), "2");
        }
        return "redirect:/equipment/list";
    }

    @ResponseBody
    @RequestMapping(value={"saveGroupId"})
    public String saveGroupId(Model model, HttpServletRequest request) {
        try {
            String ids = request.getParameter("ids");
            String[] groupIdsArr = request.getParameterValues("groupId");
            this.equipmentService.saveGroupId(ids, groupIdsArr, request);
        }
        catch (Exception e) {
            logger.error("\u4fdd\u5b58\u8d44\u4ea7\u8bbe\u5907\u6807\u7b7e\u4fe1\u606f\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("\u4fdd\u5b58\u8d44\u4ea7\u8bbe\u5907\u6807\u7b7e\u4fe1\u606f\u9519\u8bef", e.toString(), "2");
        }
        return "redirect:/equipment/list";
    }

    @RequestMapping(value={"view"})
    public String viewChart(Model model, HttpServletRequest request) {
        String errorMsg = "\u67e5\u770b\u8d44\u4ea7\u4fe1\u606f\u9519\u8bef";
        String id = request.getParameter("id");
        Equipment equipment = new Equipment();
        try {
            equipment = this.equipmentService.selectById(id);
            if ("true".equals(this.commonConfig.getHostGroup())) {
                equipment.setGroupId(this.hostGroupService.returnGroupNames(equipment.getGroupId()));
            }
            model.addAttribute("equipment", (Object)equipment);
        }
        catch (Exception e) {
            logger.error(errorMsg, (Throwable)e);
            this.logInfoService.save(errorMsg, e.toString(), "2");
        }
        return "equipment/view";
    }

    @RequestMapping(value={"del"})
    public String delete(Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        String errorMsg = "\u5220\u9664\u8d44\u4ea7\u4fe1\u606f\u9519\u8bef";
        Equipment Equipment2 = new Equipment();
        try {
            if (!StringUtils.isEmpty((CharSequence)request.getParameter("id"))) {
                String[] ids;
                for (String id : ids = request.getParameter("id").split(",")) {
                    Equipment2 = this.equipmentService.selectById(id);
                    this.equipmentService.saveLog(request, "\u5220\u9664", Equipment2);
                }
                this.equipmentService.deleteById(ids);
            }
        }
        catch (Exception e) {
            logger.error(errorMsg, (Throwable)e);
            this.logInfoService.save(errorMsg, e.toString(), "2");
        }
        return "redirect:/equipment/list";
    }

    @RequestMapping(value={"exportListExcel"})
    public void exportListExcel(Equipment equipment, Model model, HttpServletRequest request, HttpServletResponse response) {
        try {
            if (!StaticKeys.LICENSE_STATE.equals("1")) {
                response.setContentType("text/html;charset=UTF-8");
                response.getOutputStream().write("The module needs to professional version. Please contact us at www.wgstart.com".getBytes());
                return;
            }
            HashMap<String, Object> params = new HashMap<String, Object>();
            String hostname = null;
            if (!StringUtils.isEmpty((CharSequence)equipment.getName())) {
                hostname = equipment.getName();
                params.put("name", hostname.trim());
            }
            if (!StringUtils.isEmpty((CharSequence)equipment.getAccount())) {
                params.put("account", equipment.getAccount());
            }
            if (!StringUtils.isEmpty((CharSequence)equipment.getGroupId())) {
                params.put("groupId", equipment.getGroupId());
            }
            HostUtil.addAccountquery(request, params);
            PageInfo pageInfo = this.equipmentService.selectByParams(params, 1, 20000);
            if ("true".equals(this.commonConfig.getHostGroup())) {
                this.equipmentService.setGroupInList(pageInfo.getList(), model, request);
            }
            this.excelExportService.exportEquipmentListExcel(pageInfo.getList(), response);
        }
        catch (Exception e) {
            logger.error("\u6240\u6709\u8d44\u4ea7\u5217\u8868\u5bfc\u51faexcel\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("\u6240\u6709\u8d44\u4ea7\u5217\u8868\u5bfc\u51faexcel\u9519\u8bef", e.toString(), "2");
        }
    }

    private boolean isAddContinue() {
        try {
            HashMap<String, Object> params;
            int dbSize;
            if (!StaticKeys.LICENSE_STATE.equals("1") && (dbSize = this.equipmentService.countByParams(params = new HashMap<String, Object>())) >= 10) {
                return false;
            }
        }
        catch (Exception e) {
            logger.error("isAddContinue error", (Throwable)e);
        }
        return true;
    }
}

