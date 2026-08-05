/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.controller;

import cn.hutool.json.JSONObject;
import com.wgcloud.config.CommonConfig;
import com.wgcloud.service.KafkaMonitorService;
import com.wgcloud.service.LogInfoService;
import com.wgcloud.util.MessageErrorUtils;
import com.wgcloud.util.TokenUtils;
import com.wgcloud.util.TomcatUtil;
import com.wgcloud.util.staticvar.StaticKeys;
import java.util.HashMap;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value={"/tomcatMonitor"})
public class TomcatMonitorController {
    private static final Logger logger = LoggerFactory.getLogger(TomcatMonitorController.class);
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
    public String tomcatMonitorList(Model model, HttpServletRequest request) {
        HashMap params = new HashMap();
        try {
            List<JSONObject> tomcatList = TomcatUtil.viewTomcatHandler();
            if (tomcatList.size() > 10 && !StaticKeys.LICENSE_STATE.equals("1")) {
                logger.info("tomcat\u4e2a\u4eba\u7248\u53ea\u80fd\u76d1\u6d4b10\u9879");
                model.addAttribute("msg", (Object)("\u4e2a\u4eba\u7248\u53ea\u80fd\u76d1\u6d4b10\u9879\uff08\u603b\u6570\u91cf" + tomcatList.size() + "\uff09"));
                tomcatList = tomcatList.subList(0, 10);
            }
            model.addAttribute("tomcatList", tomcatList);
        }
        catch (Exception e) {
            logger.error("\u67e5\u8be2tomcat\u76d1\u63a7\u4fe1\u606f\u9519\u8bef", (Throwable)e);
        }
        return "tomcat/list";
    }
}

