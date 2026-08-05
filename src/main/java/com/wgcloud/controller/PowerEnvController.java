/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.controller;

import cn.hutool.json.JSONObject;
import com.wgcloud.config.CommonConfig;
import com.wgcloud.service.LogInfoService;
import com.wgcloud.service.RedisMonitorService;
import com.wgcloud.util.PowerEnvUtil;
import com.wgcloud.util.TokenUtils;
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
@RequestMapping(value={"/powerEnvMonitor"})
public class PowerEnvController {
    private static final Logger logger = LoggerFactory.getLogger(PowerEnvController.class);
    @Resource
    private RedisMonitorService redisMonitorService;
    @Resource
    private LogInfoService logInfoService;
    @Autowired
    private CommonConfig commonConfig;
    @Autowired
    private TokenUtils tokenUtils;

    @RequestMapping(value={"list"})
    public String powerEnvMonitorList(Model model, HttpServletRequest request) {
        HashMap params = new HashMap();
        try {
            List<JSONObject> powerEnvList = PowerEnvUtil.viewPowerEnvHandler();
            model.addAttribute("powerEnvList", powerEnvList);
        }
        catch (Exception e) {
            logger.error("\u67e5\u8be2\u52a8\u73af\u76d1\u63a7\u4fe1\u606f\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("\u67e5\u8be2\u52a8\u73af\u76d1\u63a7\u4fe1\u606f\u9519\u8bef", e.toString(), "2");
        }
        return "powerEnv/list";
    }

    @RequestMapping(value={"view"})
    public String viewPowerEnvMonitor(Model model, HttpServletRequest request) {
        try {
            String name = request.getParameter("name");
            List<JSONObject> powerEnvList = PowerEnvUtil.viewPowerEnvHandler();
            for (JSONObject jsonObject : powerEnvList) {
                if (!name.equals(jsonObject.getStr("name"))) continue;
                model.addAttribute("powerEnvInfo", (Object)jsonObject);
                break;
            }
        }
        catch (Exception e) {
            logger.error("\u67e5\u770b\u52a8\u73af\u4fe1\u606f\u9519\u8bef", (Throwable)e);
        }
        return "powerEnv/view";
    }
}

