/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.controller;

import cn.hutool.json.JSONObject;
import com.wgcloud.config.CommonConfig;
import com.wgcloud.service.KafkaMonitorService;
import com.wgcloud.service.LogInfoService;
import com.wgcloud.util.MessageErrorUtils;
import com.wgcloud.util.ScanIPUtil;
import com.wgcloud.util.TokenUtils;
import java.util.ArrayList;
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
@RequestMapping(value={"/scanIPMonitor"})
public class ScanIPMonitorController {
    private static final Logger logger = LoggerFactory.getLogger(ScanIPMonitorController.class);
    @Resource
    private KafkaMonitorService kafkaMonitorService;
    @Resource
    private LogInfoService logInfoService;
    @Autowired
    private MessageErrorUtils messageErrorUtils;
    @Autowired
    private CommonConfig commonConfig;
    @Autowired
    private TokenUtils tokenUtils;

    @RequestMapping(value={"list"})
    public String scanMonitorList(Model model, HttpServletRequest request) {
        try {
            String scanName = request.getParameter("scanName");
            List<JSONObject> dataList = ScanIPUtil.viewScanIPHandler();
            List<JSONObject> resultList = new ArrayList();
            if (!StringUtils.isEmpty((CharSequence)scanName)) {
                for (JSONObject jsonObject : dataList) {
                    String autoScanName = jsonObject.getStr("autoScanName");
                    if (StringUtils.isEmpty((CharSequence)autoScanName) || !scanName.equals(autoScanName)) continue;
                    resultList.add(jsonObject);
                }
                model.addAttribute("scanName", (Object)scanName);
            } else {
                resultList = dataList;
            }
            List<String> nameList = ScanIPUtil.viewScanNameHandler();
            model.addAttribute("dataList", resultList);
            model.addAttribute("nameList", nameList);
        }
        catch (Exception e) {
            logger.error("\u67e5\u8be2\u53d1\u73b0\u8bbe\u5907IP\u76d1\u63a7\u4fe1\u606f\u9519\u8bef", (Throwable)e);
        }
        return "scan/list";
    }
}

