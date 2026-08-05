/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.util;

import com.wgcloud.controller.LoginController;
import com.wgcloud.util.DESUtil;
import com.wgcloud.util.IpUtil;
import com.wgcloud.util.ThreadPoolUtil;
import com.wgcloud.util.msg.WarnOtherUtil;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.jodah.expiringmap.ExpirationPolicy;
import net.jodah.expiringmap.ExpiringMap;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;

public class PhoneUtil {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
    public static final String LOGIN_PHONE_KEY = "accountLoginPhone";
    public static final String LOGIN_2FA_ACCOUNT_ROLE = "login2faAccountRole";
    public static ExpiringMap<String, String> LOGIN_PHONE_CODE_MAP = ExpiringMap.builder().expiration(600L, TimeUnit.SECONDS).expirationPolicy(ExpirationPolicy.CREATED).build();

    public static boolean encryPhoneToPage(String phone, HttpSession session, Model model, String role) {
        try {
            if (StringUtils.isEmpty((CharSequence)phone) || phone.length() < 5) {
                model.addAttribute("error", "\u624b\u673a\u53f7\u7801\u683c\u5f0f\u9519\u8bef");
                return false;
            }
            session.setAttribute(LOGIN_PHONE_KEY, (Object)phone);
            session.setAttribute(LOGIN_2FA_ACCOUNT_ROLE, (Object)role);
            String char1 = phone.substring(0, 1);
            String char2 = StringUtils.right((String)phone, (int)4);
            model.addAttribute("accountPhone", (Object)(char1 + "******" + char2));
            return true;
        }
        catch (Exception e) {
            logger.error("2fa\u767b\u5f55\u9a8c\u8bc1\u7801\u5904\u7406\u9519\u8bef", (Throwable)e);
            model.addAttribute("error", "\u767b\u5f55\u9a8c\u8bc1\u7801\u5904\u7406\u9519\u8bef");
            return false;
        }
    }

    public static void sendCodeToPhone(String warnScriptPath, HttpSession session) {
        try {
            String phone = (String)session.getAttribute(LOGIN_PHONE_KEY);
            if (StringUtils.isEmpty((CharSequence)phone)) {
                logger.error("\u53d1\u9001\u767b\u5f55\u9a8c\u8bc1\u7801\u9519\u8bef: \u624b\u673a\u53f7\u4e3a\u7a7a");
                return;
            }
            if (StringUtils.isEmpty((CharSequence)warnScriptPath)) {
                logger.error("\u53d1\u9001\u767b\u5f55\u9a8c\u8bc1\u7801\u9519\u8bef: \u53d1\u9001\u811a\u672c\u4e3a\u7a7a");
                return;
            }
            Random random = new Random();
            int code = 100000 + random.nextInt(900000);
            LOGIN_PHONE_CODE_MAP.put(phone, (code + ""));
            String execCmdStr = warnScriptPath + " \"" + phone + "\" \"" + code + "\"";
            Process process = Runtime.getRuntime().exec(execCmdStr);
            logger.info("\u767b\u5f55\u53d1\u9001\u9a8c\u8bc1\u7801\u811a\u672c\u6267\u884c\u5b8c\u6210-----------" + execCmdStr);
        }
        catch (Exception e) {
            logger.error("2fa\u53d1\u9001\u767b\u5f55\u9a8c\u8bc1\u7801\u9519\u8bef", (Throwable)e);
        }
    }

    public static void loginErrorHandle(HttpServletRequest request, Model model, String phone) {
        try {
            String requestIp = IpUtil.getIpAddr(request);
            String errMsg = "\u767b\u5f55\u624b\u673a\u53f7" + phone + "\u9a8c\u8bc1\u7801\u8f93\u5165\u9519\u8bef\uff0c\u767b\u5f55IP\uff1a" + requestIp;
            Runnable runnable = () -> {
                try {
                    WarnOtherUtil.sendUtil(errMsg, errMsg, "", phone + "_longError", true, "ERROR", "");
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            };
            ThreadPoolUtil.executor.execute(runnable);
            model.addAttribute("error", "\u9a8c\u8bc1\u7801\u9519\u8bef");
            logger.error(errMsg);
        }
        catch (Exception e) {
            logger.error("2fa\u9a8c\u8bc1\u7801\u8f93\u5165\u9519\u8bef", (Throwable)e);
        }
    }

    public static void addCookieValue(HttpServletRequest request, HttpServletResponse response, String username) {
        try {
            String[] sevenNoPhone = request.getParameterValues("sevenNoPhone");
            if (sevenNoPhone.length > 0) {
                Cookie usernameCookie = new Cookie("username", DESUtil.encryption(username));
                usernameCookie.setMaxAge(604800);
                usernameCookie.setPath("/");
                response.addCookie(usernameCookie);
            }
        }
        catch (Exception e) {
            logger.error("\u8bbe\u7f6ecookie\u9519\u8bef", (Throwable)e);
        }
    }

    public static boolean checkCookie(HttpServletRequest request, String sourceUserName) {
        try {
            Cookie[] cookies = request.getCookies();
            String username = "";
            if (cookies != null && cookies.length > 0) {
                for (Cookie cookie : cookies) {
                    if (!cookie.getName().equals("username")) continue;
                    username = cookie.getValue();
                    if (!sourceUserName.equals(username = DESUtil.decrypt(username))) continue;
                    return true;
                }
            }
        }
        catch (Exception e) {
            logger.error("\u89e3\u5bc6cookie\u9519\u8bef", (Throwable)e);
        }
        return false;
    }
}

