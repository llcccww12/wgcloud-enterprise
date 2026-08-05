/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.controller;

import cn.hutool.json.JSONObject;
import com.wgcloud.config.CommonConfig;
import com.wgcloud.dto.TuopuEdgeDto;
import com.wgcloud.dto.TuopuNodeDto;
import com.wgcloud.entity.AccountInfo;
import com.wgcloud.entity.DceInfo;
import com.wgcloud.entity.HostGroup;
import com.wgcloud.entity.SnmpDeepInfo;
import com.wgcloud.entity.SnmpInfo;
import com.wgcloud.entity.SystemInfo;
import com.wgcloud.service.AccountInfoService;
import com.wgcloud.service.DceInfoService;
import com.wgcloud.service.HostGroupService;
import com.wgcloud.service.LogInfoService;
import com.wgcloud.service.SnmpDeepInfoService;
import com.wgcloud.service.SnmpInfoService;
import com.wgcloud.service.SystemInfoService;
import com.wgcloud.util.FormatUtil;
import com.wgcloud.util.HostUtil;
import com.wgcloud.util.ThreadPoolUtil;
import com.wgcloud.util.UUIDUtil;
import com.wgcloud.util.staticvar.StaticKeys;
import java.util.ArrayList;
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

@Controller
@RequestMapping(value={"/tuopu"})
public class TuopuController {
    private static final Logger logger = LoggerFactory.getLogger(TuopuController.class);
    private static final String HOST_ON_IMG = "/static/js/tuopu/img/server.png";
    private static final String HOST_DOWN_IMG = "/static/js/tuopu/img/server2.png";
    private static final String SERVER_IMG = "/static/js/tuopu/img/server_database.png";
    @Resource
    private SystemInfoService systemInfoService;
    @Resource
    private DceInfoService dceInfoService;
    @Resource
    private SnmpInfoService snmpInfoService;
    @Resource
    private SnmpDeepInfoService snmpDeepInfoService;
    @Resource
    private LogInfoService logInfoService;
    @Resource
    private HostGroupService hostGroupService;
    @Autowired
    private CommonConfig commonConfig;
    @Resource
    private AccountInfoService accountInfoService;

    private void testThread() {
        Runnable runnable = () -> logger.info("TuopuController----------testThread");
        ThreadPoolUtil.executor.execute(runnable);
    }

    @RequestMapping(value={"tuopuListHost"})
    public String tuopuListHost(SystemInfo systemInfoParam, Model model, HttpServletRequest request) {
        HashMap<String, Object> params = new HashMap<String, Object>();
        try {
            ArrayList<TuopuNodeDto> nodeList = new ArrayList<TuopuNodeDto>();
            ArrayList<TuopuEdgeDto> lineList = new ArrayList<TuopuEdgeDto>();
            String serverNodeId = UUIDUtil.getUUID();
            HostUtil.addAccountquery(request, params);
            if (!StringUtils.isEmpty((CharSequence)systemInfoParam.getState())) {
                params.put("state", systemInfoParam.getState());
            }
            if (!StringUtils.isEmpty((CharSequence)systemInfoParam.getAccount())) {
                params.put("account", systemInfoParam.getAccount());
                model.addAttribute("account", (Object)systemInfoParam.getAccount());
            }
            if (!StringUtils.isEmpty((CharSequence)systemInfoParam.getGroupId())) {
                params.put("groupId", systemInfoParam.getGroupId());
                model.addAttribute("groupId", (Object)systemInfoParam.getGroupId());
            }
            List<SystemInfo> pageInfo = this.systemInfoService.selectAllByParams(params);
            model.addAttribute("nodeListSize", (Object)pageInfo.size());
            if (!StaticKeys.LICENSE_STATE.equals("1") && pageInfo.size() > 10) {
                pageInfo = pageInfo.subList(0, 10);
                model.addAttribute("licenseMsg", "\u4e2a\u4eba\u7248\u6700\u591a\u76d1\u63a710\u9879\uff0c\u8bf7\u70b9\u51fb\u9875\u9762\u5e95\u90e8\u7f51\u7ad9\uff0c\u8054\u7cfb\u6211\u4eec\u5347\u7ea7\u5230\u4e13\u4e1a\u7248");
            }
            for (SystemInfo systemInfo : pageInfo) {
                TuopuNodeDto dto = new TuopuNodeDto();
                dto.setLabel(systemInfo.getHostname() + HostUtil.addRemark(systemInfo.getHostname()));
                if ("2".equals(systemInfo.getState())) {
                    dto.setImg(StaticKeys.SERVER_SERVLET_CONTEXT_PATH + HOST_DOWN_IMG);
                } else {
                    dto.setImg(StaticKeys.SERVER_SERVLET_CONTEXT_PATH + HOST_ON_IMG);
                }
                dto.setId(systemInfo.getId());
                dto.setCpuPer(systemInfo.getCpuPer() + "%");
                dto.setMemPer(systemInfo.getMemPer() + "%");
                dto.setRxbyt(FormatUtil.kbToM(systemInfo.getRxbyt()) + "/s");
                dto.setTxbyt(FormatUtil.kbToM(systemInfo.getTxbyt()) + "/s");
                dto.setFiveLoad(systemInfo.getFiveLoad() + "");
                dto.setNetConnections(systemInfo.getNetConnections());
                dto.setProcs(systemInfo.getProcs());
                nodeList.add(dto);
                TuopuEdgeDto tuopuEdgeDto = new TuopuEdgeDto();
                tuopuEdgeDto.setSource(dto.getId());
                tuopuEdgeDto.setTarget(serverNodeId);
                if ("2".equals(systemInfo.getState())) {
                    JSONObject styleJson = new JSONObject();
                    styleJson.set("stroke", "#dc3545");
                    tuopuEdgeDto.setStyle(styleJson);
                }
                lineList.add(tuopuEdgeDto);
            }
            TuopuNodeDto dtoServer = new TuopuNodeDto();
            dtoServer.setSize(50);
            dtoServer.setLabel("Server");
            dtoServer.setImg(StaticKeys.SERVER_SERVLET_CONTEXT_PATH + SERVER_IMG);
            dtoServer.setId(serverNodeId);
            nodeList.add(dtoServer);
            JSONObject resultJson = new JSONObject();
            resultJson.set("nodes", nodeList);
            resultJson.set("edges", lineList);
            model.addAttribute("nodeList", (Object)resultJson);
            model.addAttribute("pageFlag", "host");
            params.clear();
            List<HostGroup> hostGroupList = this.hostGroupService.selectAllByParams(params, request);
            model.addAttribute("hostGroupList", hostGroupList);
            if ("true".equals(this.commonConfig.getUserInfoManage())) {
                params.clear();
                List<AccountInfo> accountInfoList = this.accountInfoService.selectAllByParams(params);
                model.addAttribute("accountList", accountInfoList);
            }
        }
        catch (Exception e) {
            logger.error("\u4e3b\u673a\u62d3\u6251\u56fe\u751f\u6210\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("\u4e3b\u673a\u62d3\u6251\u56fe\u751f\u6210\u9519\u8bef", e.toString(), "2");
        }
        return "tuopu/tuopu";
    }

    @RequestMapping(value={"tuopuListSt"})
    public String tuopuListSt(SystemInfo systemInfoParam, Model model, HttpServletRequest request) {
        HashMap<String, Object> params = new HashMap<String, Object>();
        try {
            List<TuopuNodeDto> nodeList = new ArrayList();
            ArrayList<TuopuEdgeDto> lineList = new ArrayList<TuopuEdgeDto>();
            String serverNodeId = UUIDUtil.getUUID();
            HostUtil.addAccountquery(request, params);
            if (!StringUtils.isEmpty((CharSequence)systemInfoParam.getState())) {
                if ("2".equals(systemInfoParam.getState())) {
                    params.put("resTimes", -1);
                }
                if ("1".equals(systemInfoParam.getState())) {
                    params.put("resTimesGt", -1);
                }
            }
            if (!StringUtils.isEmpty((CharSequence)systemInfoParam.getAccount())) {
                params.put("account", systemInfoParam.getAccount());
                model.addAttribute("account", (Object)systemInfoParam.getAccount());
            }
            if (!StringUtils.isEmpty((CharSequence)systemInfoParam.getGroupId())) {
                params.put("groupId", systemInfoParam.getGroupId());
                model.addAttribute("groupId", (Object)systemInfoParam.getGroupId());
            }
            List<DceInfo> pageInfoDce = this.dceInfoService.selectAllByParams(params);
            for (DceInfo dceInfo : pageInfoDce) {
                TuopuNodeDto dto = new TuopuNodeDto();
                if (!StringUtils.isEmpty((CharSequence)dceInfo.getRemark())) {
                    dto.setLabel(dceInfo.getHostname() + "(" + dceInfo.getRemark() + ")");
                } else {
                    dto.setLabel(dceInfo.getHostname());
                }
                if (dceInfo.getResTimes() < 0) {
                    dto.setImg(StaticKeys.SERVER_SERVLET_CONTEXT_PATH + HOST_DOWN_IMG);
                } else {
                    dto.setImg(StaticKeys.SERVER_SERVLET_CONTEXT_PATH + HOST_ON_IMG);
                }
                dto.setId(dceInfo.getId());
                dto.setCpuPer(dceInfo.getResTimes() + "ms");
                nodeList.add(dto);
                TuopuEdgeDto tuopuEdgeDto = new TuopuEdgeDto();
                tuopuEdgeDto.setSource(dto.getId());
                tuopuEdgeDto.setTarget(serverNodeId);
                if (dceInfo.getResTimes() < 0) {
                    JSONObject styleJson = new JSONObject();
                    styleJson.set("stroke", "#dc3545");
                    tuopuEdgeDto.setStyle(styleJson);
                }
                lineList.add(tuopuEdgeDto);
            }
            if (!StaticKeys.LICENSE_STATE.equals("1") && nodeList.size() > 10) {
                nodeList = nodeList.subList(0, 10);
                model.addAttribute("licenseMsg", "\u4e2a\u4eba\u7248\u6700\u591a\u76d1\u63a710\u9879\uff0c\u8bf7\u70b9\u51fb\u9875\u9762\u5e95\u90e8\u7f51\u7ad9\uff0c\u8054\u7cfb\u6211\u4eec\u5347\u7ea7\u5230\u4e13\u4e1a\u7248");
            }
            TuopuNodeDto dtoServer = new TuopuNodeDto();
            dtoServer.setSize(50);
            dtoServer.setLabel("Server");
            dtoServer.setImg(StaticKeys.SERVER_SERVLET_CONTEXT_PATH + SERVER_IMG);
            dtoServer.setId(serverNodeId);
            nodeList.add(dtoServer);
            JSONObject resultJson = new JSONObject();
            resultJson.set("nodes", nodeList);
            resultJson.set("edges", lineList);
            model.addAttribute("nodeList", (Object)resultJson);
            model.addAttribute("nodeListSize", (Object)pageInfoDce.size());
            model.addAttribute("pageFlag", "ping");
            params.clear();
            List<HostGroup> hostGroupList = this.hostGroupService.selectAllByParams(params, request);
            model.addAttribute("hostGroupList", hostGroupList);
            if ("true".equals(this.commonConfig.getUserInfoManage())) {
                params.clear();
                List<AccountInfo> accountInfoList = this.accountInfoService.selectAllByParams(params);
                model.addAttribute("accountList", accountInfoList);
            }
        }
        catch (Exception e) {
            logger.error("\u7f51\u7edc\u8bbe\u5907\u62d3\u6251\u56feping\u751f\u6210\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("\u7f51\u7edc\u8bbe\u5907\u62d3\u6251\u56feping\u751f\u6210\u9519\u8bef", e.toString(), "2");
        }
        return "tuopu/tuopu";
    }

    @RequestMapping(value={"tuopuListSnmp"})
    public String tuopuListSnmp(SystemInfo systemInfoParam, Model model, HttpServletRequest request) {
        HashMap<String, Object> params = new HashMap<String, Object>();
        try {
            List<TuopuNodeDto> nodeList = new ArrayList();
            ArrayList<TuopuEdgeDto> lineList = new ArrayList<TuopuEdgeDto>();
            String serverNodeId = UUIDUtil.getUUID();
            HostUtil.addAccountquery(request, params);
            if (!StringUtils.isEmpty((CharSequence)systemInfoParam.getState())) {
                params.put("state", systemInfoParam.getState());
            }
            if (!StringUtils.isEmpty((CharSequence)systemInfoParam.getAccount())) {
                params.put("account", systemInfoParam.getAccount());
                model.addAttribute("account", (Object)systemInfoParam.getAccount());
            }
            if (!StringUtils.isEmpty((CharSequence)systemInfoParam.getGroupId())) {
                params.put("groupId", systemInfoParam.getGroupId());
                model.addAttribute("groupId", (Object)systemInfoParam.getGroupId());
            }
            List<SnmpInfo> pageInfoSnmp = this.snmpInfoService.selectAllByParams(params);
            for (SnmpInfo snmpInfo : pageInfoSnmp) {
                TuopuNodeDto dto = new TuopuNodeDto();
                if (!StringUtils.isEmpty((CharSequence)snmpInfo.getRemark())) {
                    dto.setLabel(snmpInfo.getHostname() + "(" + snmpInfo.getRemark() + ")");
                } else {
                    dto.setLabel(snmpInfo.getHostname());
                }
                if ("2".equals(snmpInfo.getState())) {
                    dto.setImg(StaticKeys.SERVER_SERVLET_CONTEXT_PATH + HOST_DOWN_IMG);
                } else {
                    dto.setImg(StaticKeys.SERVER_SERVLET_CONTEXT_PATH + HOST_ON_IMG);
                }
                dto.setRxbyt(FormatUtil.bytesFormatUnit(snmpInfo.getBytesRecv(), snmpInfo.getSnmpUnit()));
                dto.setTxbyt(FormatUtil.bytesFormatUnit(snmpInfo.getBytesSent(), snmpInfo.getSnmpUnit()));
                dto.setId(snmpInfo.getId());
                nodeList.add(dto);
                TuopuEdgeDto tuopuEdgeDto = new TuopuEdgeDto();
                tuopuEdgeDto.setSource(dto.getId());
                tuopuEdgeDto.setTarget(serverNodeId);
                if ("2".equals(snmpInfo.getState())) {
                    JSONObject styleJson = new JSONObject();
                    styleJson.set("stroke", "#dc3545");
                    tuopuEdgeDto.setStyle(styleJson);
                }
                lineList.add(tuopuEdgeDto);
            }
            if (!StaticKeys.LICENSE_STATE.equals("1") && nodeList.size() > 10) {
                nodeList = nodeList.subList(0, 10);
                model.addAttribute("licenseMsg", "\u4e2a\u4eba\u7248\u6700\u591a\u76d1\u63a710\u9879\uff0c\u8bf7\u70b9\u51fb\u9875\u9762\u5e95\u90e8\u7f51\u7ad9\uff0c\u8054\u7cfb\u6211\u4eec\u5347\u7ea7\u5230\u4e13\u4e1a\u7248");
            }
            TuopuNodeDto dtoServer = new TuopuNodeDto();
            dtoServer.setSize(50);
            dtoServer.setLabel("Server");
            dtoServer.setImg(StaticKeys.SERVER_SERVLET_CONTEXT_PATH + SERVER_IMG);
            dtoServer.setId(serverNodeId);
            nodeList.add(dtoServer);
            JSONObject resultJson = new JSONObject();
            resultJson.set("nodes", nodeList);
            resultJson.set("edges", lineList);
            model.addAttribute("nodeList", (Object)resultJson);
            model.addAttribute("nodeListSize", (Object)pageInfoSnmp.size());
            model.addAttribute("pageFlag", "snmp");
            params.clear();
            List<HostGroup> hostGroupList = this.hostGroupService.selectAllByParams(params, request);
            model.addAttribute("hostGroupList", hostGroupList);
            if ("true".equals(this.commonConfig.getUserInfoManage())) {
                params.clear();
                List<AccountInfo> accountInfoList = this.accountInfoService.selectAllByParams(params);
                model.addAttribute("accountList", accountInfoList);
            }
        }
        catch (Exception e) {
            logger.error("\u7f51\u7edc\u8bbe\u5907\u62d3\u6251\u56fesnmp\u751f\u6210\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("\u7f51\u7edc\u8bbe\u5907\u62d3\u6251\u56fesnmp\u751f\u6210\u9519\u8bef", e.toString(), "2");
        }
        return "tuopu/tuopu";
    }

    @RequestMapping(value={"tuopuListHostGrid"})
    public String tuopuListHostGrid(SystemInfo systemInfoParam, Model model, HttpServletRequest request) {
        HashMap<String, Object> params = new HashMap<String, Object>();
        try {
            ArrayList<TuopuNodeDto> nodeList = new ArrayList<TuopuNodeDto>();
            HostUtil.addAccountquery(request, params);
            if (!StringUtils.isEmpty((CharSequence)systemInfoParam.getState())) {
                params.put("state", systemInfoParam.getState());
            }
            if (!StringUtils.isEmpty((CharSequence)systemInfoParam.getAccount())) {
                params.put("account", systemInfoParam.getAccount());
                model.addAttribute("account", (Object)systemInfoParam.getAccount());
            }
            if (!StringUtils.isEmpty((CharSequence)systemInfoParam.getGroupId())) {
                params.put("groupId", systemInfoParam.getGroupId());
                model.addAttribute("groupId", (Object)systemInfoParam.getGroupId());
            }
            List<SystemInfo> pageInfo = this.systemInfoService.selectAllByParams(params);
            model.addAttribute("nodeListSize", (Object)pageInfo.size());
            if (!StaticKeys.LICENSE_STATE.equals("1") && pageInfo.size() > 10) {
                pageInfo = pageInfo.subList(0, 10);
                model.addAttribute("licenseMsg", "\u4e2a\u4eba\u7248\u6700\u591a\u76d1\u63a710\u9879\uff0c\u8bf7\u70b9\u51fb\u9875\u9762\u5e95\u90e8\u7f51\u7ad9\uff0c\u8054\u7cfb\u6211\u4eec\u5347\u7ea7\u5230\u4e13\u4e1a\u7248");
            }
            for (SystemInfo systemInfo : pageInfo) {
                TuopuNodeDto dto = new TuopuNodeDto();
                dto.setLabel(systemInfo.getHostname() + HostUtil.addRemark(systemInfo.getHostname()));
                dto.setId(systemInfo.getId());
                dto.setCpuPer(systemInfo.getCpuPer() + "%");
                dto.setMemPer(systemInfo.getMemPer() + "%");
                dto.setRxbyt(FormatUtil.kbToM(systemInfo.getRxbyt()) + "/s");
                dto.setTxbyt(FormatUtil.kbToM(systemInfo.getTxbyt()) + "/s");
                dto.setFiveLoad(systemInfo.getFiveLoad() + "");
                dto.setNetConnections(systemInfo.getNetConnections());
                dto.setProcs(systemInfo.getProcs());
                dto.setAgentVer(systemInfo.getAgentVer());
                dto.setActive(systemInfo.getActive());
                dto.setState(systemInfo.getState());
                nodeList.add(dto);
            }
            model.addAttribute("nodeList", nodeList);
            model.addAttribute("pageFlag", "host");
            params.clear();
            List<HostGroup> hostGroupList = this.hostGroupService.selectAllByParams(params, request);
            model.addAttribute("hostGroupList", hostGroupList);
            if ("true".equals(this.commonConfig.getUserInfoManage())) {
                params.clear();
                List<AccountInfo> accountInfoList = this.accountInfoService.selectAllByParams(params);
                model.addAttribute("accountList", accountInfoList);
            }
        }
        catch (Exception e) {
            logger.error("\u4e3b\u673a\u62d3\u6251\u56fe\u751f\u6210\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("\u4e3b\u673a\u62d3\u6251\u56fe\u751f\u6210\u9519\u8bef", e.toString(), "2");
        }
        return "tuopu/tuopuGrid";
    }

    @RequestMapping(value={"tuopuListStGrid"})
    public String tuopuListStGrid(SystemInfo systemInfoParam, Model model, HttpServletRequest request) {
        HashMap<String, Object> params = new HashMap<String, Object>();
        try {
            List<TuopuNodeDto> nodeList = new ArrayList();
            HostUtil.addAccountquery(request, params);
            if (!StringUtils.isEmpty((CharSequence)systemInfoParam.getState())) {
                if ("2".equals(systemInfoParam.getState())) {
                    params.put("resTimes", -1);
                }
                if ("1".equals(systemInfoParam.getState())) {
                    params.put("resTimesGt", -1);
                }
            }
            if (!StringUtils.isEmpty((CharSequence)systemInfoParam.getAccount())) {
                params.put("account", systemInfoParam.getAccount());
                model.addAttribute("account", (Object)systemInfoParam.getAccount());
            }
            if (!StringUtils.isEmpty((CharSequence)systemInfoParam.getGroupId())) {
                params.put("groupId", systemInfoParam.getGroupId());
                model.addAttribute("groupId", (Object)systemInfoParam.getGroupId());
            }
            List<DceInfo> pageInfoDce = this.dceInfoService.selectAllByParams(params);
            for (DceInfo dceInfo : pageInfoDce) {
                TuopuNodeDto dto = new TuopuNodeDto();
                if (!StringUtils.isEmpty((CharSequence)dceInfo.getRemark())) {
                    dto.setLabel(dceInfo.getHostname() + "(" + dceInfo.getRemark() + ")");
                } else {
                    dto.setLabel(dceInfo.getHostname());
                }
                if (dceInfo.getResTimes() < 0) {
                    dto.setState("2");
                } else {
                    dto.setState("1");
                }
                dto.setId(dceInfo.getId());
                dto.setCpuPer(dceInfo.getResTimes() + "ms");
                dto.setActive(dceInfo.getActive());
                nodeList.add(dto);
            }
            if (!StaticKeys.LICENSE_STATE.equals("1") && nodeList.size() > 10) {
                nodeList = nodeList.subList(0, 10);
                model.addAttribute("licenseMsg", "\u4e2a\u4eba\u7248\u6700\u591a\u76d1\u63a710\u9879\uff0c\u8bf7\u70b9\u51fb\u9875\u9762\u5e95\u90e8\u7f51\u7ad9\uff0c\u8054\u7cfb\u6211\u4eec\u5347\u7ea7\u5230\u4e13\u4e1a\u7248");
            }
            model.addAttribute("nodeList", nodeList);
            model.addAttribute("nodeListSize", (Object)pageInfoDce.size());
            model.addAttribute("pageFlag", "ping");
            params.clear();
            List<HostGroup> hostGroupList = this.hostGroupService.selectAllByParams(params, request);
            model.addAttribute("hostGroupList", hostGroupList);
            if ("true".equals(this.commonConfig.getUserInfoManage())) {
                params.clear();
                List<AccountInfo> accountInfoList = this.accountInfoService.selectAllByParams(params);
                model.addAttribute("accountList", accountInfoList);
            }
        }
        catch (Exception e) {
            logger.error("\u7f51\u7edc\u8bbe\u5907\u62d3\u6251\u56feping\u751f\u6210\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("\u7f51\u7edc\u8bbe\u5907\u62d3\u6251\u56feping\u751f\u6210\u9519\u8bef", e.toString(), "2");
        }
        return "tuopu/tuopuGrid";
    }

    @RequestMapping(value={"tuopuListSnmpGrid"})
    public String tuopuListSnmpGrid(SystemInfo systemInfoParam, Model model, HttpServletRequest request) {
        HashMap<String, Object> params = new HashMap<String, Object>();
        try {
            List<TuopuNodeDto> nodeList = new ArrayList();
            HostUtil.addAccountquery(request, params);
            if (!StringUtils.isEmpty((CharSequence)systemInfoParam.getState())) {
                params.put("state", systemInfoParam.getState());
            }
            if (!StringUtils.isEmpty((CharSequence)systemInfoParam.getAccount())) {
                params.put("account", systemInfoParam.getAccount());
                model.addAttribute("account", (Object)systemInfoParam.getAccount());
            }
            if (!StringUtils.isEmpty((CharSequence)systemInfoParam.getGroupId())) {
                params.put("groupId", systemInfoParam.getGroupId());
                model.addAttribute("groupId", (Object)systemInfoParam.getGroupId());
            }
            List<SnmpInfo> pageInfoSnmp = this.snmpInfoService.selectAllByParams(params);
            for (SnmpInfo snmpInfo : pageInfoSnmp) {
                TuopuNodeDto dto = new TuopuNodeDto();
                if (!StringUtils.isEmpty((CharSequence)snmpInfo.getRemark())) {
                    dto.setLabel(snmpInfo.getHostname() + "(" + snmpInfo.getRemark() + ")");
                } else {
                    dto.setLabel(snmpInfo.getHostname());
                }
                dto.setRxbyt(FormatUtil.bytesFormatUnit(snmpInfo.getBytesRecv(), snmpInfo.getSnmpUnit()));
                dto.setTxbyt(FormatUtil.bytesFormatUnit(snmpInfo.getBytesSent(), snmpInfo.getSnmpUnit()));
                dto.setId(snmpInfo.getId());
                dto.setActive(snmpInfo.getActive());
                dto.setState(snmpInfo.getState());
                nodeList.add(dto);
            }
            if (!StaticKeys.LICENSE_STATE.equals("1") && nodeList.size() > 10) {
                nodeList = nodeList.subList(0, 10);
                model.addAttribute("licenseMsg", "\u4e2a\u4eba\u7248\u6700\u591a\u76d1\u63a710\u9879\uff0c\u8bf7\u70b9\u51fb\u9875\u9762\u5e95\u90e8\u7f51\u7ad9\uff0c\u8054\u7cfb\u6211\u4eec\u5347\u7ea7\u5230\u4e13\u4e1a\u7248");
            }
            model.addAttribute("nodeList", nodeList);
            model.addAttribute("nodeListSize", (Object)pageInfoSnmp.size());
            model.addAttribute("pageFlag", "snmp");
            params.clear();
            List<HostGroup> hostGroupList = this.hostGroupService.selectAllByParams(params, request);
            model.addAttribute("hostGroupList", hostGroupList);
            if ("true".equals(this.commonConfig.getUserInfoManage())) {
                params.clear();
                List<AccountInfo> accountInfoList = this.accountInfoService.selectAllByParams(params);
                model.addAttribute("accountList", accountInfoList);
            }
        }
        catch (Exception e) {
            logger.error("\u7f51\u7edc\u8bbe\u5907\u62d3\u6251\u56fesnmp\u751f\u6210\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("\u7f51\u7edc\u8bbe\u5907\u62d3\u6251\u56fesnmp\u751f\u6210\u9519\u8bef", e.toString(), "2");
        }
        return "tuopu/tuopuGrid";
    }

    @RequestMapping(value={"tuopuListSnmpDeep"})
    public String tuopuListSnmpDeep(SystemInfo systemInfoParam, Model model, HttpServletRequest request) {
        HashMap<String, Object> params = new HashMap<String, Object>();
        try {
            List<TuopuNodeDto> nodeList = new ArrayList();
            ArrayList<TuopuEdgeDto> lineList = new ArrayList<TuopuEdgeDto>();
            String serverNodeId = UUIDUtil.getUUID();
            HostUtil.addAccountquery(request, params);
            if (!StringUtils.isEmpty((CharSequence)systemInfoParam.getState())) {
                params.put("state", systemInfoParam.getState());
            }
            if (!StringUtils.isEmpty((CharSequence)systemInfoParam.getAccount())) {
                params.put("account", systemInfoParam.getAccount());
                model.addAttribute("account", (Object)systemInfoParam.getAccount());
            }
            if (!StringUtils.isEmpty((CharSequence)systemInfoParam.getGroupId())) {
                params.put("groupId", systemInfoParam.getGroupId());
                model.addAttribute("groupId", (Object)systemInfoParam.getGroupId());
            }
            List<SnmpDeepInfo> pageInfoSnmp = this.snmpDeepInfoService.selectAllByParams(params);
            for (SnmpDeepInfo snmpInfo : pageInfoSnmp) {
                TuopuNodeDto dto = new TuopuNodeDto();
                if (!StringUtils.isEmpty((CharSequence)snmpInfo.getRemark())) {
                    dto.setLabel(snmpInfo.getHostname() + "(" + snmpInfo.getRemark() + ")");
                } else {
                    dto.setLabel(snmpInfo.getHostname());
                }
                if ("2".equals(snmpInfo.getState())) {
                    dto.setImg(StaticKeys.SERVER_SERVLET_CONTEXT_PATH + HOST_DOWN_IMG);
                } else {
                    dto.setImg(StaticKeys.SERVER_SERVLET_CONTEXT_PATH + HOST_ON_IMG);
                }
                dto.setId(snmpInfo.getId());
                nodeList.add(dto);
                TuopuEdgeDto tuopuEdgeDto = new TuopuEdgeDto();
                tuopuEdgeDto.setSource(dto.getId());
                tuopuEdgeDto.setTarget(serverNodeId);
                if ("2".equals(snmpInfo.getState())) {
                    JSONObject styleJson = new JSONObject();
                    styleJson.set("stroke", "#dc3545");
                    tuopuEdgeDto.setStyle(styleJson);
                }
                lineList.add(tuopuEdgeDto);
            }
            if (!StaticKeys.LICENSE_STATE.equals("1") && nodeList.size() > 10) {
                nodeList = nodeList.subList(0, 10);
                model.addAttribute("licenseMsg", "\u4e2a\u4eba\u7248\u6700\u591a\u76d1\u63a710\u9879\uff0c\u8bf7\u70b9\u51fb\u9875\u9762\u5e95\u90e8\u7f51\u7ad9\uff0c\u8054\u7cfb\u6211\u4eec\u5347\u7ea7\u5230\u4e13\u4e1a\u7248");
            }
            TuopuNodeDto dtoServer = new TuopuNodeDto();
            dtoServer.setSize(50);
            dtoServer.setLabel("Server");
            dtoServer.setImg(StaticKeys.SERVER_SERVLET_CONTEXT_PATH + SERVER_IMG);
            dtoServer.setId(serverNodeId);
            nodeList.add(dtoServer);
            JSONObject resultJson = new JSONObject();
            resultJson.set("nodes", nodeList);
            resultJson.set("edges", lineList);
            model.addAttribute("nodeList", (Object)resultJson);
            model.addAttribute("nodeListSize", (Object)pageInfoSnmp.size());
            model.addAttribute("pageFlag", "snmpDeep");
            params.clear();
            List<HostGroup> hostGroupList = this.hostGroupService.selectAllByParams(params, request);
            model.addAttribute("hostGroupList", hostGroupList);
            if ("true".equals(this.commonConfig.getUserInfoManage())) {
                params.clear();
                List<AccountInfo> accountInfoList = this.accountInfoService.selectAllByParams(params);
                model.addAttribute("accountList", accountInfoList);
            }
        }
        catch (Exception e) {
            logger.error("\u7f51\u7edc\u8bbe\u5907\u62d3\u6251\u56fesnmp\u6df1\u5ea6\u751f\u6210\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("\u7f51\u7edc\u8bbe\u5907\u62d3\u6251\u56fesnmp\u6df1\u5ea6\u751f\u6210\u9519\u8bef", e.toString(), "2");
        }
        return "tuopu/tuopu";
    }

    @RequestMapping(value={"tuopuListSnmpDeepGrid"})
    public String tuopuListSnmpDeepGrid(SystemInfo systemInfoParam, Model model, HttpServletRequest request) {
        HashMap<String, Object> params = new HashMap<String, Object>();
        try {
            List<TuopuNodeDto> nodeList = new ArrayList();
            HostUtil.addAccountquery(request, params);
            if (!StringUtils.isEmpty((CharSequence)systemInfoParam.getState())) {
                params.put("state", systemInfoParam.getState());
            }
            if (!StringUtils.isEmpty((CharSequence)systemInfoParam.getAccount())) {
                params.put("account", systemInfoParam.getAccount());
                model.addAttribute("account", (Object)systemInfoParam.getAccount());
            }
            if (!StringUtils.isEmpty((CharSequence)systemInfoParam.getGroupId())) {
                params.put("groupId", systemInfoParam.getGroupId());
                model.addAttribute("groupId", (Object)systemInfoParam.getGroupId());
            }
            List<SnmpDeepInfo> pageInfoSnmp = this.snmpDeepInfoService.selectAllByParams(params);
            for (SnmpDeepInfo snmpInfo : pageInfoSnmp) {
                TuopuNodeDto dto = new TuopuNodeDto();
                if (!StringUtils.isEmpty((CharSequence)snmpInfo.getRemark())) {
                    dto.setLabel(snmpInfo.getHostname() + "(" + snmpInfo.getRemark() + ")");
                } else {
                    dto.setLabel(snmpInfo.getHostname());
                }
                dto.setId(snmpInfo.getId());
                dto.setActive(snmpInfo.getActive());
                dto.setState(snmpInfo.getState());
                nodeList.add(dto);
            }
            if (!StaticKeys.LICENSE_STATE.equals("1") && nodeList.size() > 10) {
                nodeList = nodeList.subList(0, 10);
                model.addAttribute("licenseMsg", "\u4e2a\u4eba\u7248\u6700\u591a\u76d1\u63a710\u9879\uff0c\u8bf7\u70b9\u51fb\u9875\u9762\u5e95\u90e8\u7f51\u7ad9\uff0c\u8054\u7cfb\u6211\u4eec\u5347\u7ea7\u5230\u4e13\u4e1a\u7248");
            }
            model.addAttribute("nodeList", nodeList);
            model.addAttribute("nodeListSize", (Object)pageInfoSnmp.size());
            model.addAttribute("pageFlag", "snmpDeep");
            params.clear();
            List<HostGroup> hostGroupList = this.hostGroupService.selectAllByParams(params, request);
            model.addAttribute("hostGroupList", hostGroupList);
            if ("true".equals(this.commonConfig.getUserInfoManage())) {
                params.clear();
                List<AccountInfo> accountInfoList = this.accountInfoService.selectAllByParams(params);
                model.addAttribute("accountList", accountInfoList);
            }
        }
        catch (Exception e) {
            logger.error("\u7f51\u7edc\u8bbe\u5907\u62d3\u6251\u56fesnmp\u6df1\u5ea6\u751f\u6210\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("\u7f51\u7edc\u8bbe\u5907\u62d3\u6251\u56fesnmp\u6df1\u5ea6\u751f\u6210\u9519\u8bef", e.toString(), "2");
        }
        return "tuopu/tuopuGrid";
    }
}

