/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.util;

import com.wgcloud.controller.LoginController;
import java.net.InetAddress;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IpUtil {
    private static final String UNKNOWN = "unknown";
    private static final String LOCALHOST = "127.0.0.1";
    private static final String SEPARATOR = ",";
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    public static String getIpAddr(HttpServletRequest request) {
        String ipAddress = "";
        try {
            ipAddress = request.getHeader("x-forwarded-for");
            if (ipAddress == null || ipAddress.length() == 0 || UNKNOWN.equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.length() == 0 || UNKNOWN.equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("WL-Proxy-Client-IP");
            }
            if ((ipAddress == null || ipAddress.length() == 0 || UNKNOWN.equalsIgnoreCase(ipAddress)) && LOCALHOST.equals(ipAddress = request.getRemoteAddr())) {
                InetAddress inet = InetAddress.getLocalHost();
                ipAddress = inet.getHostAddress();
            }
            if (ipAddress != null && ipAddress.length() > 15 && ipAddress.indexOf(SEPARATOR) > 0) {
                ipAddress = ipAddress.substring(0, ipAddress.indexOf(SEPARATOR));
            }
        }
        catch (Exception e) {
            ipAddress = "";
            logger.error("\u83b7\u53d6\u767b\u5f55\u8bf7\u6c42IP\u9519\u8bef", (Throwable)e);
        }
        return ipAddress;
    }
}

