/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.controller;

import com.wgcloud.config.CommonConfig;
import com.wgcloud.entity.AccountInfo;
import com.wgcloud.service.LogInfoService;
import com.wgcloud.util.DateUtil;
import com.wgcloud.util.FileUtils;
import com.wgcloud.util.HostUtil;
import com.wgcloud.util.TokenUtils;
import com.wgcloud.util.msg.WarnPools;
import com.wgcloud.util.staticvar.StaticKeys;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping(value={"/nginxMonitor"})
public class NginxMonitorController {
    private static final Logger logger = LoggerFactory.getLogger(NginxMonitorController.class);
    @Resource
    private LogInfoService logInfoService;
    @Autowired
    private CommonConfig commonConfig;
    @Autowired
    private TokenUtils tokenUtils;
    private static final String sqlChars = "xp_cmdshell,%20xor,%20and,%20AND,%20or,%20OR,select%20,%20and%201=1,%20and%201=2,%20from,%27exec,information_schema.tables,load_file,benchmark,substring,table_name,table_schema,%20where%20,%20union%20,%20UNION%20,concat(,concat_ws(,%20group%20,0x5f,0x7e,0x7c,0x27,%20limit,current_user,%20LIMIT,version%28,version(,database%28,database(,user%28,user(,%20extractvalue,%updatexml,rand(0)*2,%20group%20by%20x,%20NULL%2C,sqlmap";
    private static final String scanChars = "acunetix,by_wvs,nikto,netsparker,HP404,nsfocus,WebCruiser,owasp,nmap,nessus,HEAD /,AppScan,burpsuite,w3af,ZAP,openVAS,.+avij,.+angolin,360webscan,webscan,XSS@HERE,XSS%40HERE,NOSEC.JSky,wwwscan,wscan,antSword,WebVulnScan,WebInspect,ltx71,masscan,python-requests,Python-urllib,WinHttpRequest";
    String spider = "spider,pider,Googlebot";

    @RequestMapping(value={"toAddView"})
    public String toAddView(Model model, HttpServletRequest request) {
        model.addAttribute("NGINX_CHECK_DAY_COUNT", (Object)WarnPools.NGINX_CHECK_DAY_COUNT);
        return "nginx/add";
    }

    @RequestMapping(value={"/uploadLog"})
    public String singleFileUpload(@RequestParam(value="file") MultipartFile file, Model model, HttpServletRequest request) {
        AccountInfo accountInfo = HostUtil.getAccountByRequest(request);
        if ("guest".equals(accountInfo.getRole())) {
            return "redirect:/common/error/guestError";
        }
        model.addAttribute("NGINX_CHECK_DAY_COUNT", (Object)WarnPools.NGINX_CHECK_DAY_COUNT);
        if (!StaticKeys.LICENSE_STATE.equals("1") && WarnPools.NGINX_CHECK_DAY_COUNT > 0) {
            model.addAttribute("msg", "\u4e2a\u4eba\u7248\u6bcf\u5929\u53ea\u80fd\u68c0\u6d4b1\u6b21\uff0c\u8bf7\u8054\u7cfb\u6211\u4eec\u5347\u7ea7\u4e3a\u4e13\u4e1a\u7248");
            return "nginx/add";
        }
        if (StaticKeys.LICENSE_STATE.equals("1") && WarnPools.NGINX_CHECK_DAY_COUNT > StaticKeys.LICENSE_NUM) {
            model.addAttribute("msg", (Object)("\u8be5\u4e13\u4e1a\u7248\u6388\u6743\u6bcf\u5929\u6700\u591a\u68c0\u6d4b" + StaticKeys.LICENSE_NUM + "\u6b21\uff0c\u8bf7\u8054\u7cfb\u6211\u4eec\u5347\u7ea7\u6388\u6743"));
            return "nginx/add";
        }
        if (file.isEmpty()) {
            logger.error("\u6587\u4ef6\u4e3a\u7a7a,\u8bf7\u9009\u62e9Nginx Log\u6587\u4ef6\u4e0a\u4f20");
            model.addAttribute("msg", "\u6587\u4ef6\u4e3a\u7a7a,\u8bf7\u9009\u62e9\u4f60\u7684\u6587\u4ef6\u4e0a\u4f20");
            return "nginx/add";
        }
        if (!file.getOriginalFilename().endsWith(".log")) {
            logger.error("\u8bf7\u4e0a\u4f20.log\u6587\u4ef6");
            model.addAttribute("msg", "\u8bf7\u4e0a\u4f20Nginx\u7684.log\u6587\u4ef6");
            return "nginx/add";
        }
        String sourceFileName = file.getOriginalFilename();
        model.addAttribute("accessLogName", (Object)sourceFileName);
        String saveFolder = StaticKeys.JAR_PATH + "/nginxLog";
        FileUtils.existsFolder(saveFolder);
        String newFileName = saveFolder + "/" + DateUtil.getCurrentDateTimeNoChar() + ".log";
        Path path = Paths.get(newFileName, new String[0]);
        try {
            file.transferTo(path);
            this.checkNgingLog(newFileName, model);
            this.logInfoService.save(HostUtil.getAccountByRequest(request).getAccount() + "\u4e0a\u4f20\u5e76\u68c0\u6d4bNingx\u65e5\u5fd7\u6587\u4ef6: " + sourceFileName, newFileName, "2");
            WarnPools.NGINX_CHECK_DAY_COUNT = WarnPools.NGINX_CHECK_DAY_COUNT + 1;
        }
        catch (Exception e) {
            logger.error("\u4e0a\u4f20nginx\u65e5\u5fd7\u6587\u4ef6\u9519\u8bef", (Throwable)e);
            model.addAttribute("msg", (Object)e.toString());
        }
        return "nginx/view";
    }

    private void checkNgingLog(String fileName, Model model) {
        try {
            File file = new File(fileName);
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String[] sqlCharArr = sqlChars.split(",");
            String[] scanCharArr = scanChars.split(",");
            String[] spiderCharArr = this.spider.split(",");
            String resultContent = "";
            String strLine = null;
            int code404 = 0;
            int code408 = 0;
            int code413 = 0;
            int code414 = 0;
            int code400 = 0;
            int code401 = 0;
            int code403 = 0;
            int code301 = 0;
            int code500 = 0;
            int code502 = 0;
            int code503 = 0;
            int sqlCount = 0;
            int scanCount = 0;
            int spiderCount = 0;
            HashSet<String> setIp = new HashSet<String>();
            HashMap<String, Integer> ipCountMap = new HashMap<String, Integer>();
            block2: while (null != (strLine = bufferedReader.readLine())) {
                if (StringUtils.isEmpty((CharSequence)strLine)) continue;
                String ip = strLine.substring(0, strLine.indexOf("-"));
                setIp.add(ip.trim());
                if (ipCountMap.get(ip) == null) {
                    ipCountMap.put(ip, 1);
                } else {
                    ipCountMap.put(ip, (Integer)ipCountMap.get(ip) + 1);
                }
                if (strLine.contains(" 400 ")) {
                    ++code400;
                }
                if (strLine.contains(" 401 ")) {
                    ++code401;
                }
                if (strLine.contains(" 403 ")) {
                    ++code403;
                }
                if (strLine.contains(" 404 ")) {
                    ++code404;
                }
                if (strLine.contains(" 408 ")) {
                    ++code408;
                }
                if (strLine.contains(" 413 ")) {
                    ++code413;
                }
                if (strLine.contains(" 414 ")) {
                    ++code414;
                }
                if (strLine.contains(" 301 ")) {
                    ++code301;
                }
                if (strLine.contains(" 500 ")) {
                    ++code500;
                }
                if (strLine.contains(" 502 ")) {
                    ++code502;
                }
                if (strLine.contains(" 503 ")) {
                    ++code503;
                }
                for (String sqlCharTemp : sqlCharArr) {
                    if (!strLine.contains(sqlCharTemp)) continue;
                    ++sqlCount;
                    break;
                }
                for (String scanCharTemp : scanCharArr) {
                    if (!strLine.contains(scanCharTemp)) continue;
                    ++scanCount;
                    break;
                }
                for (String spiderCharTemp : spiderCharArr) {
                    if (!strLine.contains(spiderCharTemp)) continue;
                    ++spiderCount;
                    continue block2;
                }
            }
            resultContent = resultContent + "IP\u603b\u6570\u91cf: " + setIp.size() + "</br>";
            resultContent = resultContent + "\u54cd\u5e94\u72b6\u6001\u7801400\u5171\u51fa\u73b0\u6b21\u6570: " + code400 + "</br>";
            resultContent = resultContent + "\u54cd\u5e94\u72b6\u6001\u7801401\u5171\u51fa\u73b0\u6b21\u6570: " + code401 + "</br>";
            resultContent = resultContent + "\u54cd\u5e94\u72b6\u6001\u7801403\u5171\u51fa\u73b0\u6b21\u6570: " + code403 + "</br>";
            resultContent = resultContent + "\u54cd\u5e94\u72b6\u6001\u7801404\u5171\u51fa\u73b0\u6b21\u6570: " + code404 + "</br>";
            resultContent = resultContent + "\u54cd\u5e94\u72b6\u6001\u7801408\u5171\u51fa\u73b0\u6b21\u6570: " + code408 + "</br>";
            resultContent = resultContent + "\u54cd\u5e94\u72b6\u6001\u7801413\u5171\u51fa\u73b0\u6b21\u6570: " + code413 + "</br>";
            resultContent = resultContent + "\u54cd\u5e94\u72b6\u6001\u7801414\u5171\u51fa\u73b0\u6b21\u6570: " + code414 + "</br>";
            resultContent = resultContent + "\u54cd\u5e94\u72b6\u6001\u7801301\u5171\u51fa\u73b0\u6b21\u6570: " + code301 + "</br>";
            resultContent = resultContent + "\u54cd\u5e94\u72b6\u6001\u7801500\u5171\u51fa\u73b0\u6b21\u6570: " + code500 + "</br>";
            resultContent = resultContent + "\u54cd\u5e94\u72b6\u6001\u7801502\u5171\u51fa\u73b0\u6b21\u6570: " + code502 + "</br>";
            resultContent = resultContent + "\u54cd\u5e94\u72b6\u6001\u7801503\u5171\u51fa\u73b0\u6b21\u6570: " + code503 + "</br>";
            resultContent = resultContent + "SQL\u6ce8\u5165\u653b\u51fb\u603b\u6b21\u6570: " + sqlCount + "</br>";
            resultContent = resultContent + "\u68c0\u6d4b\u5230\u626b\u63cf\u653b\u51fb\u603b\u6b21\u6570: " + scanCount + "</br>";
            resultContent = resultContent + "\u641c\u7d22\u5f15\u64ce\u8718\u86db\u722c\u53d6\u603b\u6b21\u6570: " + spiderCount + "</br>";
            resultContent = resultContent + "</br>\u8bbf\u95ee\u6b21\u6570\u6700\u9ad8\u524d20\u4e2aIP\u5982\u4e0b (IP\u548c\u8bbf\u95ee\u6b21\u6570)</br>";
            ArrayList ipCountMapIds = new ArrayList(ipCountMap.entrySet());
            Collections.sort(ipCountMapIds, new Comparator<Map.Entry<String, Integer>>(){

                @Override
                public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                    return o2.getValue() - o1.getValue();
                }
            });
            if (ipCountMapIds.size() <= 20) {
                for (int i = 0; i < ipCountMapIds.size(); ++i) {
                    resultContent = resultContent + (String)((Map.Entry)ipCountMapIds.get(i)).getKey() + "\u8bbf\u95ee\u6b21\u6570 " + ((Map.Entry)ipCountMapIds.get(i)).getValue() + "</br>";
                }
            } else {
                for (int i = 0; i < 20; ++i) {
                    resultContent = resultContent + (String)((Map.Entry)ipCountMapIds.get(i)).getKey() + "\u8bbf\u95ee\u6b21\u6570 " + ((Map.Entry)ipCountMapIds.get(i)).getValue() + "</br>";
                }
            }
            model.addAttribute("nginxMonitorContent", (Object)resultContent);
        }
        catch (Exception e) {
            logger.error("nginx\u65e5\u5fd7\u6587\u4ef6\u89e3\u6790\u9519\u8bef", (Throwable)e);
        }
    }
}

