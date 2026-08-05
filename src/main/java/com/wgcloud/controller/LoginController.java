/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.controller;

import com.wgcloud.config.CommonConfig;
import com.wgcloud.entity.AccountInfo;
import com.wgcloud.service.AccountInfoService;
import com.wgcloud.service.LogInfoService;
import com.wgcloud.util.IpUtil;
import com.wgcloud.util.MD5Utils;
import com.wgcloud.util.PhoneUtil;
import com.wgcloud.util.ThreadPoolUtil;
import com.wgcloud.util.license.LicenseUtil;
import com.wgcloud.util.msg.WarnOtherUtil;
import com.wgcloud.util.staticvar.StaticKeys;
import java.util.HashMap;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value={"/login"})
public class LoginController {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
    private static final String USER_BLOCK = "block";
    @Resource
    private LogInfoService logInfoService;
    @Resource
    private AccountInfoService accountInfoService;
    @Resource
    private CommonConfig commonConfig;

    private void testThread() {
        Runnable runnable = () -> logger.info("LoginCotroller----------testThread");
        ThreadPoolUtil.executor.execute(runnable);
    }

    @RequestMapping(value={"toLogin2fa"})
    public String toLogin2fa(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        String phone = (String)session.getAttribute("accountLoginPhone");
        String accountRole = (String)session.getAttribute("login2faAccountRole");
        if (StringUtils.isEmpty((CharSequence)phone) || StringUtils.isEmpty((CharSequence)accountRole)) {
            model.addAttribute("error", "\u8d26\u53f7\u6216\u8005\u5bc6\u7801\u9519\u8bef");
            return "login/login";
        }
        return "login/login2fa";
    }

    @RequestMapping(value={"toLogin"})
    public String toLogin(Model model, HttpServletRequest request) {
        return "login/login";
    }

    @RequestMapping(value={"loginOut"})
    public String loginOut(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.invalidate();
        return "redirect:/login/toLogin";
    }

    @RequestMapping(value={"login"})
    public String login(Model model, HttpServletRequest request) {
        String vercode;
        HttpSession session = request.getSession();
        if ("true".equals(this.commonConfig.getVercodeCheck()) && (StringUtils.isEmpty((CharSequence)(vercode = request.getParameter("validateCode"))) || !vercode.toLowerCase().equals(session.getAttribute("validateCode")))) {
            model.addAttribute("error", "\u9a8c\u8bc1\u7801\u9519\u8bef");
            return "login/login";
        }
        if (StaticKeys.PASSWD_EXP_DATE) {
            model.addAttribute("error", "\u5bc6\u7801\u5df2\u8fc7\u671f");
            return "login/login";
        }
        if ("ERROR".equals(StaticKeys.WGCLOUD_SERVER_RELEASE_MD5STR)) {
            model.addAttribute("error", "\u8bf7\u5c06wgcloud-server-release.jar\u8fd8\u539f\u540e\u518d\u767b\u5f55");
            return "login/login";
        }
        String userName = request.getParameter("userName");
        String userBlock = (String)StaticKeys.LOGIN_BLOCK_MAP.get(userName);
        if (USER_BLOCK.equals(userBlock)) {
            model.addAttribute("error", "\u8bf720\u5206\u949f\u540e\u767b\u5f55\u8be5\u8d26\u53f7\u6216\u8005\u8054\u7cfb\u7ba1\u7406\u5458");
            return "login/login";
        }
        String passwd = request.getParameter("md5pwd");
        session.setAttribute("sidebarCollapse", "sidebar-mini sidebar-collapse");
        session.setAttribute("themeNameSign", "");
        session.setAttribute("themeTableHoverSign", "table-hover");
        try {
            if (!StringUtils.isEmpty((CharSequence)userName) && !StringUtils.isEmpty((CharSequence)passwd)) {
                passwd = passwd.toLowerCase();
                if (MD5Utils.GetMD5Code(this.commonConfig.getAccountPwd()).equals(passwd) && this.commonConfig.getAccount().equals(userName)) {
                    if (!StringUtils.isEmpty((CharSequence)this.commonConfig.getSendPhoneCodeScript()) && !PhoneUtil.checkCookie(request, userName)) {
                        boolean resultBolean = PhoneUtil.encryPhoneToPage(this.commonConfig.getAccountPhone(), session, model, "admin");
                        if (resultBolean) {
                            return "login/login2fa";
                        }
                        return "login/login";
                    }
                    this.loadAccountInfoSession("admin", userName, session, request, StaticKeys.ADMIN_MENUDIS);
                    return "redirect:/dash/main";
                }
                if (StaticKeys.LICENSE_STATE.equals("1") && "true".equals(this.commonConfig.getUserInfoManage())) {
                    HashMap<String, Object> params = new HashMap<String, Object>();
                    params.put("account", userName);
                    params.put("passwd", passwd);
                    List<AccountInfo> userList = this.accountInfoService.selectAllByParams(params);
                    if (userList.size() > 0) {
                        AccountInfo accountInfoFromData = userList.get(0);
                        if (!StringUtils.isEmpty((CharSequence)this.commonConfig.getSendPhoneCodeScript()) && !PhoneUtil.checkCookie(request, userName)) {
                            boolean resultBolean = PhoneUtil.encryPhoneToPage(accountInfoFromData.getAccountPhone(), session, model, accountInfoFromData.getRole());
                            if (resultBolean) {
                                return "login/login2fa";
                            }
                            return "login/login";
                        }
                        this.loadAccountInfoSession(accountInfoFromData.getRole(), userName, session, request, accountInfoFromData.getMenuIds());
                        return "redirect:/dash/main";
                    }
                }
            }
        }
        catch (Exception e) {
            logger.error("\u767b\u5f55\u9519\u8bef", (Throwable)e);
        }
        model.addAttribute("error", "\u8d26\u53f7\u6216\u8005\u5bc6\u7801\u9519\u8bef");
        this.loginErrorHandle(request, model);
        return "login/login";
    }

    @RequestMapping(value={"loginByPhone"})
    public String loginByPhone(Model model, HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        try {
            String passwd = request.getParameter("passwd");
            String phone = (String)session.getAttribute("accountLoginPhone");
            String accountRole = (String)session.getAttribute("login2faAccountRole");
            if (StringUtils.isEmpty((CharSequence)phone) || StringUtils.isEmpty((CharSequence)accountRole)) {
                model.addAttribute("error", "\u8d26\u53f7\u6216\u8005\u5bc6\u7801\u9519\u8bef");
                return "login/login";
            }
            String code = (String)PhoneUtil.LOGIN_PHONE_CODE_MAP.get(phone);
            if (!StringUtils.isEmpty((CharSequence)passwd) && !StringUtils.isEmpty((CharSequence)code) && code.equals(passwd)) {
                session.removeAttribute("accountLoginPhone");
                session.removeAttribute("login2faAccountRole");
                if ("admin".equals(accountRole)) {
                    this.loadAccountInfoSession("admin", this.commonConfig.getAccount(), session, request, StaticKeys.ADMIN_MENUDIS);
                    PhoneUtil.addCookieValue(request, response, this.commonConfig.getAccount());
                    return "redirect:/dash/main";
                }
                if (!"admin".equals(accountRole) && StaticKeys.LICENSE_STATE.equals("1") && "true".equals(this.commonConfig.getUserInfoManage())) {
                    HashMap<String, Object> params = new HashMap<String, Object>();
                    params.put("accountPhone", phone);
                    List<AccountInfo> userList = this.accountInfoService.selectAllByParams(params);
                    if (userList.size() > 0) {
                        AccountInfo accountInfoFromData = userList.get(0);
                        this.loadAccountInfoSession(accountInfoFromData.getRole(), accountInfoFromData.getAccount(), session, request, accountInfoFromData.getMenuIds());
                        PhoneUtil.addCookieValue(request, response, accountInfoFromData.getAccount());
                        return "redirect:/dash/main";
                    }
                }
            }
            PhoneUtil.encryPhoneToPage(phone, session, model, accountRole);
            PhoneUtil.loginErrorHandle(request, model, phone);
        }
        catch (Exception e) {
            logger.error("2fa\u767b\u5f55\u9519\u8bef", (Throwable)e);
        }
        return "login/login2fa";
    }

    @ResponseBody
    @RequestMapping(value={"sendPhoneCode"})
    public String sendPhoneCode(HttpServletRequest request) {
        try {
            HttpSession session = request.getSession();
            PhoneUtil.sendCodeToPhone(this.commonConfig.getSendPhoneCodeScript(), session);
        }
        catch (Exception e) {
            logger.error("\u7ed9\u767b\u5f55\u624b\u673a\u53d1\u9001\u9a8c\u8bc1\u7801\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("\u7ed9\u767b\u5f55\u624b\u673a\u53d1\u9001\u9a8c\u8bc1\u7801\u9519\u8bef", e.toString(), "2");
        }
        return "redirect:/login/toLogin";
    }

    private void loadAccountInfoSession(String role, String userName, HttpSession session, HttpServletRequest request, String menuIds) {
        AccountInfo accountInfo = new AccountInfo();
        accountInfo.setAccount(userName);
        accountInfo.setId(userName);
        accountInfo.setRole(role);
        if (StringUtils.isEmpty((CharSequence)menuIds)) {
            accountInfo.setMenuIds(this.accountInfoService.getAllMenuIdsStr());
        } else {
            accountInfo.setMenuIds(menuIds);
        }
        session.setAttribute("LOGIN_KEY", (Object)accountInfo);
        this.saveLoginData(accountInfo, request);
    }

    private void loginErrorHandle(HttpServletRequest request, Model model) {
        try {
            String requestIp = IpUtil.getIpAddr(request);
            String userName = request.getParameter("userName");
            Integer errorCount = (Integer)StaticKeys.LOGIN_ERROR_MAP.get(userName);
            if (errorCount != null) {
                StaticKeys.LOGIN_ERROR_MAP.put(userName, (errorCount + 1));
            } else {
                errorCount = 1;
                StaticKeys.LOGIN_ERROR_MAP.put(userName, errorCount);
            }
            if (errorCount >= 2) {
                model.addAttribute("error", "\u8d26\u53f7\u6216\u5bc6\u7801\u9519\u8bef\uff0c\u82e5\u8fde\u7eed5\u6b21\u8f93\u5165\u9519\u8bef\uff0c\u9700\u7b4920\u5206\u949f\u540e\u624d\u80fd\u518d\u767b\u5f55");
            }
            if (errorCount >= 5) {
                StaticKeys.LOGIN_BLOCK_MAP.put(userName, USER_BLOCK);
                String errMsg = userName + "\u5bc6\u7801\u5df2\u8fde\u7eed5\u6b21\u8f93\u5165\u9519\u8bef\uff0c20\u5206\u949f\u5185\u7981\u6b62\u767b\u5f55\uff0c\u767b\u5f55IP\uff1a" + requestIp;
                Runnable runnable = () -> {
                    try {
                        WarnOtherUtil.sendUtil(errMsg, errMsg, userName, userName + "_longError", true, "ERROR", "");
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                };
                ThreadPoolUtil.executor.execute(runnable);
                model.addAttribute("error", "\u5bc6\u7801\u5df2\u8fde\u7eed5\u6b21\u8f93\u5165\u9519\u8bef\uff0c\u8bf720\u5206\u949f\u540e\u518d\u767b\u5f55");
                logger.error(errMsg);
            }
        }
        catch (Exception e) {
            logger.error("\u5904\u7406\u8fde\u7eed5\u6b21\u8f93\u5165\u9519\u8bef\u5bc6\u7801\u5f02\u5e38", (Throwable)e);
        }
    }

    @RequestMapping(value={"sso"})
    public String ssoLogin(Model model, HttpServletRequest request) {
        if ("ERROR".equals(StaticKeys.WGCLOUD_SERVER_RELEASE_MD5STR)) {
            model.addAttribute("error", "\u8bf7\u5c06wgcloud-server-release.jar\u8fd8\u539f\u540e\u518d\u767b\u5f55");
            return "login/login";
        }
        String userName = request.getParameter("userName");
        String userBlock = (String)StaticKeys.LOGIN_BLOCK_MAP.get(userName);
        if (USER_BLOCK.equals(userBlock)) {
            model.addAttribute("error", "\u8bf720\u5206\u949f\u540e\u767b\u5f55\u8be5\u8d26\u53f7\u6216\u8005\u8054\u7cfb\u7ba1\u7406\u5458");
            return "login/login";
        }
        if (!LicenseUtil.checkEnterpriseVersion()) {
            model.addAttribute("error", "\u8bf7\u5347\u7ea7\u5230\u4f01\u4e1a\u7248");
            return "login/login";
        }
        if ("true".equals(this.commonConfig.getOpenSSO())) {
            if (StringUtils.isEmpty((CharSequence)userName)) {
                return "redirect:/login/toLogin";
            }
            AccountInfo accountInfoSso = this.ssoHandle(userName, request);
            if (null == accountInfoSso) {
                return "redirect:/login/toLogin";
            }
            return "redirect:/dash/main";
        }
        model.addAttribute("error", "sso\u9519\u8bef");
        this.loginErrorHandle(request, model);
        return "login/login";
    }

    private AccountInfo ssoHandle(String userName, HttpServletRequest request) {
        AccountInfo accountInfo = null;
        try {
            if (this.commonConfig.getAccount().equals(userName)) {
                accountInfo = new AccountInfo();
                accountInfo.setAccount(userName);
                accountInfo.setId(userName);
                accountInfo.setRole("admin");
                if (StringUtils.isEmpty((CharSequence)StaticKeys.ADMIN_MENUDIS)) {
                    accountInfo.setMenuIds(this.accountInfoService.getAllMenuIdsStr());
                } else {
                    accountInfo.setMenuIds(StaticKeys.ADMIN_MENUDIS);
                }
                request.getSession().setAttribute("LOGIN_KEY", (Object)accountInfo);
            }
            if ("true".equals(this.commonConfig.getUserInfoManage())) {
                HashMap<String, Object> params = new HashMap<String, Object>();
                params.put("account", userName);
                List<AccountInfo> userList = this.accountInfoService.selectAllByParams(params);
                if (userList.size() > 0) {
                    accountInfo = new AccountInfo();
                    AccountInfo accountInfoFromData = userList.get(0);
                    accountInfo.setAccount(userName);
                    accountInfo.setId(userName);
                    if ("guest".equals(accountInfoFromData.getRole())) {
                        accountInfo.setRole("guest");
                    } else {
                        accountInfo.setRole("user");
                    }
                    if (StringUtils.isEmpty((CharSequence)accountInfoFromData.getMenuIds())) {
                        accountInfo.setMenuIds(this.accountInfoService.getAllMenuIdsStr());
                    } else {
                        accountInfo.setMenuIds(accountInfoFromData.getMenuIds());
                    }
                    request.getSession().setAttribute("LOGIN_KEY", (Object)accountInfo);
                }
            }
        }
        catch (Exception e) {
            logger.error("sso\u5904\u7406\u9519\u8bef", (Throwable)e);
        }
        return accountInfo;
    }

    private void saveLoginData(AccountInfo accountInfo, HttpServletRequest request) {
        try {
            String requestIp = IpUtil.getIpAddr(request);
            this.logInfoService.save("\u8d26\u53f7" + accountInfo.getAccount() + "\u767b\u5f55\u7cfb\u7edf\u6210\u529f\uff0c\u767b\u5f55IP\uff1a" + requestIp, "\u767b\u5f55IP\uff1a" + requestIp, "2");
        }
        catch (Exception e) {
            logger.error("saveLoginData\u9519\u8bef", (Throwable)e);
        }
    }

    @ResponseBody
    @RequestMapping(value={"ajaxSwapMenuState"})
    public String ajaxSwapMenuState(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Object object = session.getAttribute("sidebarCollapse");
        if (null != object) {
            if ("sidebar-mini sidebar-collapse".equals(object.toString())) {
                session.setAttribute("sidebarCollapse", "sidebar-mini");
            } else {
                session.setAttribute("sidebarCollapse", "sidebar-mini sidebar-collapse");
            }
        } else {
            session.setAttribute("sidebarCollapse", "sidebar-mini sidebar-collapse");
        }
        return "success";
    }
}

