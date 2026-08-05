/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.util.license;

import com.wgcloud.common.ApplicationContextHelper;
import com.wgcloud.entity.SystemInfo;
import com.wgcloud.service.LogInfoService;
import com.wgcloud.util.DateUtil;
import com.wgcloud.util.ThreadPoolUtil;
import com.wgcloud.util.msg.WarnOtherUtil;
import com.wgcloud.util.staticvar.StaticKeys;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;

public class LicenseUtil {
    private static final Logger logger = LoggerFactory.getLogger(LicenseUtil.class);
    private static LogInfoService logInfoService = ApplicationContextHelper.getBean(LogInfoService.class);
    public static final String RPO_MSG = "\u4e2a\u4eba\u7248\u6700\u591a\u76d1\u63a710\u9879\uff0c\u8bf7\u70b9\u51fb\u9875\u9762\u5e95\u90e8\u7f51\u7ad9\uff0c\u8054\u7cfb\u6211\u4eec\u5347\u7ea7\u5230\u4e13\u4e1a\u7248";
    public static final String RPO_OUT_TIME_MSG = "\u6388\u6743\u5df2\u8fc7\u671f\uff0c\u8bf7\u70b9\u51fb\u9875\u9762\u5e95\u90e8\u7f51\u7ad9\uff0c\u8054\u7cfb\u6211\u4eec\u83b7\u53d6\u6388\u6743";
    public static final String RPO_REQUEST_MSG = "\u6b64\u529f\u80fd\u9700\u5347\u7ea7\u5230\u4e13\u4e1a\u7248\uff0c\u8bf7\u70b9\u51fb\u9875\u9762\u5e95\u90e8\u7f51\u7ad9\u8054\u7cfb\u6211\u4eec";
    public static final String PRO_OUT_NUM_MSG = "\u76d1\u63a7\u8282\u70b9\u6570\u91cf\u5df2\u8d85\u8fc7\u6388\u6743\u8282\u70b9\u6570\u91cf\uff0c\u6388\u6743\u8282\u70b9\u6570\u91cf" + StaticKeys.LICENSE_NUM + "\uff0c\u8bf7\u70b9\u51fb\u9875\u9762\u5e95\u90e8\u7f51\u7ad9\uff0c\u8054\u7cfb\u6211\u4eec\u5347\u7ea7\u6388\u6743";
    public static final String PLUS_REQUEST_MSG = "\u6b64\u529f\u80fd\u9700\u5347\u7ea7\u5230\u4f01\u4e1a\u7248\uff0c\u8bf7\u70b9\u51fb\u9875\u9762\u5e95\u90e8\u7f51\u7ad9\u8054\u7cfb\u6211\u4eec";

    private void testThread() {
        new Thread(() -> logger.info("\u542f\u52a8\u5b50\u7ebf\u7a0b\u6d4b\u8bd5")).start();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private static String readLicFile(String path) {
        File file = new File(path);
        if (!file.exists()) {
            logger.info("\u672a\u68c0\u6d4b\u5230\u6388\u6743\u6587\u4ef6----------------");
            return "";
        }
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(path));
            String readLine = null;
            StringBuilder sb = new StringBuilder();
            while ((readLine = br.readLine()) != null) {
                sb.append(readLine);
            }
            String string = sb.toString().trim();
            return string;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (br != null) {
                try {
                    br.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return "";
    }

    public static String validateLicense(int n, int n2, int n3, int n4) {
        StaticKeys.LICENSE_STATE = "1";
        StaticKeys.LICENSE_NUM = 99999;
        StaticKeys.LICENSE_DATE = "20991231";
        StaticKeys.LICENSE_NAME = "S_professional";
        return "1";
    }

    public static void checkHostList(SystemInfo systemInfo, Model model) {
    }

    public static void maxLicense_10(Model model, HttpServletRequest httpServletRequest, Object object) {
    }

    public static void sendOutDateMail() {
        try {
            Long days;
            if (StaticKeys.LICENSE_STATE.equals("1") && (days = LicenseUtil.getOutDays()) <= 7L) {
                Runnable runnable = () -> WarnOtherUtil.sendUtil("WGCLOUD\u6388\u6743\u5373\u5c06\u5230\u671f\u63d0\u9192", "\u4f60\u597d\uff0cWGCLOUD\u6388\u6743\u5c06\u4e8e\u3010" + StaticKeys.LICENSE_DATE + "\u3011\u5230\u671f\uff0c\u8bf7\u8054\u7cfb\u6211\u4eec\u8fdb\u884c\u7eed\u8d39www.wgstart.com", "", "", false, "ERROR", "");
                ThreadPoolUtil.executor.execute(runnable);
            }
        }
        catch (Exception e) {
            logger.error("\u53d1\u9001\u6388\u6743\u5373\u5c06\u5230\u671f\u63d0\u9192\u90ae\u4ef6\u9519\u8bef", (Throwable)e);
        }
    }

    public static Long getOutDays() throws ParseException {
        String formatLicenseOutDate = StaticKeys.LICENSE_DATE.substring(0, 4) + "-" + StaticKeys.LICENSE_DATE.substring(4, 6) + "-" + StaticKeys.LICENSE_DATE.substring(6);
        Date licenseOutDate = DateUtil.getDate(formatLicenseOutDate, "yyyy-MM-dd");
        Date nowDate = new Date();
        Long starTime = nowDate.getTime();
        Long endTime = licenseOutDate.getTime();
        Long num = endTime - starTime;
        Long days = num / 24L / 60L / 60L / 1000L;
        return days;
    }

    public static void outDateAlter(Model model, int n) {
    }

    public static void footerLicenseHandle(ServletContext servletContext, String showVersion) {
        if (!StaticKeys.LICENSE_STATE.equals("1")) {
            return;
        }
        servletContext.setAttribute("LICENSE_SHOW_VERSION", "true");
        String licenseType = "\u4e13\u4e1a\u7248";
        String licenseDate = StaticKeys.LICENSE_DATE;
        if (StaticKeys.LICENSE_DATE.startsWith("2099")) {
            licenseDate = "\u6c38\u4e45\u6388\u6743";
        }
        if (LicenseUtil.checkEnterpriseVersion()) {
            licenseType = "\u4f01\u4e1a\u7248";
            servletContext.setAttribute("LICENSE_SHOW_VERSION", (Object)showVersion);
        }
        String footerLicenseInfo = "\u5f53\u524d\u4e3a" + licenseType + "\uff0c\u5230\u671f\u65f6\u95f4\uff1a" + licenseDate + " \u8282\u70b9\u6570\u91cf\uff1a" + StaticKeys.LICENSE_NUM + " \u7528\u6237\uff1a" + StaticKeys.LICENSE_NAME;
        servletContext.setAttribute("footerLicenseInfo", (Object)footerLicenseInfo);
        String footerLicenseInfoDashView = "\u5f53\u524d\u4e3a" + licenseType;
        servletContext.setAttribute("footerLicenseInfoDashView", (Object)footerLicenseInfoDashView);
    }

    public static void sendStopWarnMail(int n) {
        StaticKeys.WARN_LICENSE_CHECK_SIGN = true;
    }

    public static boolean checkEnterpriseVersion() {
        return StaticKeys.LICENSE_STATE.equals("1") && StaticKeys.LICENSE_NAME.startsWith("S");
    }
}

