/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.dapingController;

import com.wgcloud.config.CommonConfig;
import com.wgcloud.config.MailConfig;
import com.wgcloud.service.HostDiskPerService;
import com.wgcloud.service.LogInfoService;
import com.wgcloud.service.SystemInfoService;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value={"/AIPage"})
public class AIPageController {
    private static final Logger logger = LoggerFactory.getLogger(AIPageController.class);
    @Resource
    private SystemInfoService systemInfoService;
    @Resource
    private LogInfoService logInfoService;
    @Resource
    private HostDiskPerService hostDiskPerService;
    @Autowired
    private MailConfig mailConfig;
    @Autowired
    private CommonConfig commonConfig;

    @RequestMapping(value={"list"})
    public String list(Model model, HttpServletRequest request) {
        if (StringUtils.isEmpty((CharSequence)this.mailConfig.getAiAnalyzeScript())) {
            return "error/404";
        }
        return "AIPage/list";
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @ResponseBody
    @RequestMapping(value={"analyze"})
    public String analyze(Model model, HttpServletRequest request) {
        String result = "";
        Process process = null;
        try {
            String questionData = request.getParameter("questionData");
            if (!StringUtils.isEmpty((CharSequence)questionData) && !StringUtils.isEmpty((CharSequence)this.mailConfig.getAiAnalyzeScript())) {
                String line;
                String charsetValue = "UTF-8";
                if (!StringUtils.isEmpty((CharSequence)this.commonConfig.getDceCharset())) {
                    charsetValue = this.commonConfig.getDceCharset();
                }
                String execCmdStr = this.mailConfig.getAiAnalyzeScript() + " \"" + questionData + "\"";
                String[] paramArr = this.mailConfig.getAiAnalyzeScript().split(" ");
                ArrayList<String> paramList = new ArrayList<String>();
                for (String param : paramArr) {
                    paramList.add(param);
                }
                paramList.add(questionData);
                logger.info("AI\u811a\u672c\u6267\u884c---------" + execCmdStr);
                ProcessBuilder pb = new ProcessBuilder(paramList);
                process = pb.start();
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), charsetValue));
                StringBuilder sb = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                result = sb.toString();
            }
        }
        catch (Exception e) {
            logger.error("AI\u811a\u672c\u6267\u884c\u9519\u8bef", (Throwable)e);
            String string = e.toString();
            return string;
        }
        finally {
            if (process != null) {
                process.destroy();
            }
        }
        return result;
    }

    private void readStream(InputStream is, StringBuilder sb) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));){
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append(System.lineSeparator());
            }
        }
        catch (IOException e) {
            sb.append("\u8bfb\u53d6\u6d41\u5931\u8d25\uff1a").append(e.getMessage());
        }
    }
}

