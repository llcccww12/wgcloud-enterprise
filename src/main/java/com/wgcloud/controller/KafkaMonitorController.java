/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.controller;

import cn.hutool.json.JSONObject;
import com.github.pagehelper.PageInfo;
import com.wgcloud.config.CommonConfig;
import com.wgcloud.entity.KafkaMonitor;
import com.wgcloud.service.KafkaMonitorService;
import com.wgcloud.service.LogInfoService;
import com.wgcloud.util.ActivemqUtil;
import com.wgcloud.util.MessageErrorUtils;
import com.wgcloud.util.PageUtil;
import com.wgcloud.util.RabbitmqUtil;
import com.wgcloud.util.TokenUtils;
import com.wgcloud.util.license.LicenseUtil;
import com.wgcloud.util.staticvar.StaticKeys;
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
@RequestMapping(value={"/kafkaMonitor"})
public class KafkaMonitorController {
    private static final Logger logger = LoggerFactory.getLogger(KafkaMonitorController.class);
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
    public String kafkaMonitorList(KafkaMonitor kafkaMonitor, Model model, HttpServletRequest request) {
        HashMap<String, Object> params = new HashMap<String, Object>();
        try {
            LicenseUtil.maxLicense_10(model, request, kafkaMonitor);
            StringBuffer url = new StringBuffer();
            if (!StringUtils.isEmpty((CharSequence)kafkaMonitor.getState())) {
                params.put("state", kafkaMonitor.getState());
                url.append("&state=").append(kafkaMonitor.getState());
            }
            if (!StringUtils.isEmpty((CharSequence)kafkaMonitor.getKafkaName())) {
                params.put("kafkaName", kafkaMonitor.getKafkaName());
                url.append("&kafkaName=").append(kafkaMonitor.getKafkaName());
            }
            PageInfo pageInfo = this.kafkaMonitorService.selectByParams(params, kafkaMonitor.getPage(), kafkaMonitor.getPageSize());
            for (KafkaMonitor kafkaMonitorTemp : (java.util.List<KafkaMonitor>) pageInfo.getList()) {
                kafkaMonitorTemp.setTestErrorMsg(this.messageErrorUtils.viewErrorMsgHandler(kafkaMonitorTemp.getKafkaName()));
            }
            PageUtil.initPageNumber(pageInfo, model);
            model.addAttribute("pageUrl", (Object)("/kafkaMonitor/list?1=1" + url.toString()));
            model.addAttribute("page", (Object)pageInfo);
            model.addAttribute("kafkaMonitor", (Object)kafkaMonitor);
        }
        catch (Exception e) {
            logger.error("\u67e5\u8be2kafka\u76d1\u63a7\u4fe1\u606f\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("\u67e5\u8be2kafka\u76d1\u63a7\u4fe1\u606f\u9519\u8bef", e.toString(), "2");
        }
        return "kafka/list";
    }

    @RequestMapping(value={"del"})
    public String delete(Model model, HttpServletRequest request) {
        String errorMsg = "\u5220\u9664Kafka\u4fe1\u606f\u9519\u8bef";
        KafkaMonitor kafkaMonitor = new KafkaMonitor();
        try {
            if (!StringUtils.isEmpty((CharSequence)request.getParameter("id"))) {
                String[] ids;
                for (String id : ids = request.getParameter("id").split(",")) {
                    kafkaMonitor = this.kafkaMonitorService.selectById(id);
                    this.kafkaMonitorService.saveLog(request, "\u5220\u9664", kafkaMonitor);
                }
                this.kafkaMonitorService.deleteById(ids);
            }
        }
        catch (Exception e) {
            logger.error(errorMsg, (Throwable)e);
            this.logInfoService.save(errorMsg, e.toString(), "2");
        }
        return "redirect:/kafkaMonitor/list";
    }

    @RequestMapping(value={"rabbitmqList"})
    public String rabbitmqList(Model model, HttpServletRequest request) {
        HashMap params = new HashMap();
        try {
            List<JSONObject> rabbitmqList = RabbitmqUtil.viewRabbitmqHandler();
            if (rabbitmqList.size() > 10 && !StaticKeys.LICENSE_STATE.equals("1")) {
                logger.info("rabbitmq\u4e2a\u4eba\u7248\u53ea\u80fd\u76d1\u6d4b10\u9879");
                model.addAttribute("msg", (Object)("\u4e2a\u4eba\u7248\u53ea\u80fd\u76d1\u6d4b10\u9879\uff08\u603b\u6570\u91cf" + rabbitmqList.size() + "\uff09"));
                rabbitmqList = rabbitmqList.subList(0, 10);
            }
            model.addAttribute("rabbitmqList", rabbitmqList);
        }
        catch (Exception e) {
            logger.error("\u67e5\u8be2rabbitmq\u76d1\u63a7\u4fe1\u606f\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("\u67e5\u8be2rabbitmq\u76d1\u63a7\u4fe1\u606f\u9519\u8bef", e.toString(), "2");
        }
        return "kafka/rabbitmqList";
    }

    @RequestMapping(value={"activemqList"})
    public String activemqList(Model model, HttpServletRequest request) {
        HashMap params = new HashMap();
        try {
            List<JSONObject> activemqList = ActivemqUtil.viewActivemqHandler();
            if (activemqList.size() > 10 && !StaticKeys.LICENSE_STATE.equals("1")) {
                logger.info("activemq\u4e2a\u4eba\u7248\u53ea\u80fd\u76d1\u6d4b10\u9879");
                model.addAttribute("msg", (Object)("\u4e2a\u4eba\u7248\u53ea\u80fd\u76d1\u6d4b10\u9879\uff08\u603b\u6570\u91cf" + activemqList.size() + "\uff09"));
                activemqList = activemqList.subList(0, 10);
            }
            model.addAttribute("activemqList", activemqList);
        }
        catch (Exception e) {
            logger.error("\u67e5\u8be2activemq\u76d1\u63a7\u4fe1\u606f\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("\u67e5\u8be2activemq\u76d1\u63a7\u4fe1\u606f\u9519\u8bef", e.toString(), "2");
        }
        return "kafka/activemqList";
    }
}

