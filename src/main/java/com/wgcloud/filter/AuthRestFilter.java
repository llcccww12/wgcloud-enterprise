/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.filter;

import com.wgcloud.config.CommonConfig;
import com.wgcloud.entity.AccountInfo;
import com.wgcloud.util.staticvar.StaticKeys;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

@WebFilter(filterName="authRestFilter", urlPatterns={"/*"})
public class AuthRestFilter
implements Filter {
    static Logger log = LoggerFactory.getLogger(AuthRestFilter.class);
    @Autowired
    CommonConfig commonConfig;
    String[] static_resource = new String[]{"/fileSafe/agentList", "/shellInfo/agentList", "/shellInfo/shellCallback", "/fileWarnInfo/agentStateList", "/fileWarnInfo/agentList", "/heathMonitor/agentList", "/dbTable/agentList", "/systemInfoOpen/", "/systemInfo/agentList", "/agentLogGo/minTask", "/agentGo/minTask", "/agentDiskGo/minTask", "/dceInfo/agentList", "/login/sendPhoneCode", "/login/toLogin", "/login/login", "/appInfo/agentList", "/dockerInfo/agentList", "/portInfo/agentList", "/license/", "/snmpDeepInfo/agentList", "/serverBackupMonitor/agentList", "/agentCallBackGo/callBackMsg", "/static/", "/resources/", "/log/agentList", "/customInfo/agentList", "/agentCustomGo/minTask", "/dbInfo/agentList", "/agentDbTableGo/minTask", "/report/agentReportInstance", "/report/agentList", "/agentScanIPGo/minTask", "/agentHeathMonitorGo/minTask", "/agentDceInfoGo/minTask", "/agentSnmpInfoGo/minTask", "/snmpInfo/agentList", "/agentHostNetworkGo/minTask", "/agentHostNetworkGo/ifconfigTask", "/agentLastlogInfoGo/lastUserShowTask", "/ftpInfo/agentList", "/agentFtpInfoGo/minTask", "/agentLastlogInfoGo/minTask", "/appExceptionInfo/agentList", "/vercode/get", "/taskJobInfo/agentList", "/taskJobInfo/jobCallback", "/agentK8sGo/minTask", "/k8sMonitor/agentList", "/login/sso", "/agentHostMacGo/minTask", "/agentRedisGo/minTask", "/agentKafkaGo/minTask", "/agentPowerEnvGo/minTask", "/agentTomcatGo/minTask", "/agentBiosBoardGo/minTask"};
    String[] dash_views = new String[]{"/dash/main", "/systemInfo/systemInfoList", "/systemInfo/systemInfoListAjax", "/systemInfo/detail", "/systemInfo/chart", "/warnInfo/warnCountAjax", "/warnInfo/warnCountAjaxHandle"};
    String[] daping_views = new String[]{"/daping/index", "/dapingNew/index", "/dapingThree/index", "/dapingFour/index", "/dapingFive/index", "/dapingSix/index", "/dapingSeven/index", "/dapingEight/index", "/dapingNine/index", "/dapingTen/index"};
    String[] guest_no_views = new String[]{"/del", "/save", "/edit", "/editBatch", "/cancel", "/test", "/encryptPasswdForHistoryVer", "/importHosts", "/saveGroupId", "/saveBatch", "/saveWinConsole", "/updateActive", "/editPasswd", "/savePasswd", "/moveToAccount", "/copyPasswdInfo", "/copyEquipment", "/run", "/cancelProcessAjax", "/uploadLog"};

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse)servletResponse;
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        HttpSession session = request.getSession();
        AccountInfo accountInfo = (AccountInfo)session.getAttribute("LOGIN_KEY");
        String uri = request.getRequestURI();
        log.debug("uri----" + uri);
        String servletPath = request.getServletPath();
        log.debug("servletPath----" + servletPath);
        for (String ss : this.static_resource) {
            if (!servletPath.startsWith(ss)) continue;
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        this.menuActive(session, uri, request);
        if (accountInfo != null && (servletPath.startsWith("/login/toLogin") || servletPath.equals("/") || servletPath.equals("/login/sso"))) {
            response.sendRedirect(StaticKeys.SERVER_SERVLET_CONTEXT_PATH + "/dash/main");
            return;
        }
        if (accountInfo != null && "guest".equals(accountInfo.getRole())) {
            for (String ss : this.guest_no_views) {
                if (!servletPath.endsWith(ss)) continue;
                response.sendRedirect(StaticKeys.SERVER_SERVLET_CONTEXT_PATH + "/common/error/guestError");
                return;
            }
        }
        if (accountInfo == null) {
            for (String ss : this.dash_views) {
                if (!servletPath.startsWith(ss) || !"true".equals(this.commonConfig.getDashView()) || request.getParameter("dashView") == null) continue;
                filterChain.doFilter(servletRequest, servletResponse);
                return;
            }
        }
        if (accountInfo == null) {
            for (String ss : this.daping_views) {
                if (!servletPath.startsWith(ss) || !"true".equals(this.commonConfig.getDapingView())) continue;
                filterChain.doFilter(servletRequest, servletResponse);
                return;
            }
        }
        if (accountInfo == null) {
            response.sendRedirect(StaticKeys.SERVER_SERVLET_CONTEXT_PATH + "/login/toLogin");
            return;
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    public void menuActive(HttpSession session, String uri, HttpServletRequest request) {
        if (uri.endsWith(".js") || uri.endsWith(".css") || uri.endsWith(".mp3") || uri.endsWith(".png")) {
            return;
        }
        if (uri.indexOf("/log/") > -1) {
            session.setAttribute("menuActive", "21");
            return;
        }
        if (uri.indexOf("/dash/main") > -1) {
            session.setAttribute("menuActive", "01");
            return;
        }
        if (uri.indexOf("/systemInfo/systemInfoList") > -1 || uri.indexOf("/systemInfo/detail") > -1 || uri.indexOf("/systemInfo/chart") > -1 || uri.indexOf("/dash/hostDraw") > -1 || uri.indexOf("/systemInfo/viewAllProcess") > -1 || uri.indexOf("/systemInfo/viewImportInfo") > -1 || uri.indexOf("/systemInfo/viewAllPortInfo") > -1 || uri.indexOf("/systemInfo/viewIfconfigInfo") > -1 || uri.indexOf("/systemInfo/viewPCIData") > -1) {
            session.setAttribute("menuActive", "12");
            return;
        }
        if (uri.indexOf("/appInfo") > -1) {
            session.setAttribute("menuActive", "13");
            return;
        }
        if (uri.indexOf("/dockerInfo") > -1) {
            session.setAttribute("menuActive", "14");
            return;
        }
        if (uri.indexOf("/portInfo") > -1) {
            session.setAttribute("menuActive", "15");
            return;
        }
        if (uri.indexOf("/fileWarnInfo") > -1) {
            session.setAttribute("menuActive", "16");
            return;
        }
        if (uri.indexOf("/fileSafe") > -1) {
            session.setAttribute("menuActive", "17");
            return;
        }
        if (uri.indexOf("/customInfo") > -1) {
            session.setAttribute("menuActive", "18");
            return;
        }
        if (uri.indexOf("/appExceptionInfo") > -1) {
            session.setAttribute("menuActive", "19");
            return;
        }
        if (uri.indexOf("/mailset") > -1) {
            session.setAttribute("menuActive", "d1");
            return;
        }
        if (uri.indexOf("/shellInfo") > -1) {
            session.setAttribute("menuActive", "23");
            return;
        }
        if (uri.indexOf("/hostGroup") > -1) {
            session.setAttribute("menuActive", "24");
            return;
        }
        if (uri.indexOf("/accountInfo") > -1) {
            session.setAttribute("menuActive", "25");
            return;
        }
        if (uri.indexOf("/passwdInfo") > -1) {
            session.setAttribute("menuActive", "31");
            return;
        }
        if (uri.indexOf("/hostWarnDiy") > -1) {
            session.setAttribute("menuActive", "d3");
            return;
        }
        if (uri.indexOf("/warnScript") > -1) {
            session.setAttribute("menuActive", "d2");
            return;
        }
        if (uri.indexOf("/dbInfo") > -1) {
            session.setAttribute("menuActive", "41");
            return;
        }
        if (uri.indexOf("/dbTable") > -1) {
            session.setAttribute("menuActive", "42");
            return;
        }
        if (uri.indexOf("/heathMonitor") > -1) {
            session.setAttribute("menuActive", "51");
            return;
        }
        if (uri.indexOf("/dceInfo") > -1) {
            session.setAttribute("menuActive", "61");
            return;
        }
        if (uri.indexOf("/snmpInfo") > -1) {
            session.setAttribute("menuActive", "62");
            return;
        }
        if (uri.indexOf("/snmpDeepInfo") > -1) {
            session.setAttribute("menuActive", "63");
            return;
        }
        if (uri.indexOf("/tuopu/tuopuListHost") > -1) {
            session.setAttribute("menuActive", "71");
            return;
        }
        if (uri.indexOf("/tuopu/tuopuListSt") > -1) {
            session.setAttribute("menuActive", "72");
            return;
        }
        if (uri.indexOf("/tuopu/tuopuListSnmp") > -1) {
            if (uri.indexOf("/tuopu/tuopuListSnmpDeep") > -1) {
                session.setAttribute("menuActive", "74");
                return;
            }
            session.setAttribute("menuActive", "73");
            return;
        }
        if (uri.indexOf("/equipment") > -1) {
            session.setAttribute("menuActive", "81");
            return;
        }
        if (uri.indexOf("/report") > -1) {
            session.setAttribute("menuActive", "91");
            return;
        }
        if (uri.indexOf("/ftpInfo") > -1) {
            session.setAttribute("menuActive", "a1");
            return;
        }
        if (uri.indexOf("/daping") > -1) {
            session.setAttribute("menuActive", "b1");
            return;
        }
        if (uri.indexOf("/k8sMonitor/list") > -1) {
            session.setAttribute("menuActive", "k1");
            if (request.getParameter("dataType") != null && "namespace".equals(request.getParameter("dataType"))) {
                session.setAttribute("menuActive", "k2");
            }
            if (request.getParameter("dataType") != null && "pod".equals(request.getParameter("dataType"))) {
                session.setAttribute("menuActive", "k3");
            }
            if (request.getParameter("dataType") != null && "service".equals(request.getParameter("dataType"))) {
                session.setAttribute("menuActive", "k4");
            }
            if (request.getParameter("dataType") != null && "container".equals(request.getParameter("dataType"))) {
                session.setAttribute("menuActive", "k5");
            }
            if (request.getParameter("dataType") != null && "deployment".equals(request.getParameter("dataType"))) {
                session.setAttribute("menuActive", "k6");
            }
            return;
        }
        if (uri.indexOf("/k8sMonitor/viewContainer") > -1) {
            session.setAttribute("menuActive", "k5");
            return;
        }
        if (uri.indexOf("/redisMonitor") > -1) {
            session.setAttribute("menuActive", "k8");
            return;
        }
        if (uri.indexOf("/powerEnvMonitor") > -1) {
            session.setAttribute("menuActive", "k10");
            return;
        }
        if (uri.indexOf("/kafkaMonitor/rabbitmqList") > -1) {
            session.setAttribute("menuActive", "k11");
            return;
        }
        if (uri.indexOf("/kafkaMonitor/activemqList") > -1) {
            session.setAttribute("menuActive", "k12");
            return;
        }
        if (uri.indexOf("/serverBackupMonitor/list") > -1) {
            session.setAttribute("menuActive", "k13");
            return;
        }
        if (uri.indexOf("/tomcatMonitor/list") > -1) {
            session.setAttribute("menuActive", "k14");
            return;
        }
        if (uri.indexOf("/scanIPMonitor/list") > -1) {
            session.setAttribute("menuActive", "h1");
            return;
        }
        if (uri.indexOf("/nginxMonitor") > -1) {
            session.setAttribute("menuActive", "k9");
            return;
        }
        if (uri.indexOf("/kafkaMonitor/list") > -1) {
            session.setAttribute("menuActive", "k7");
            return;
        }
        if (uri.indexOf("/shellNoteInfo") > -1) {
            session.setAttribute("menuActive", "c1");
            return;
        }
        if (uri.indexOf("/taskJobInfo") > -1) {
            session.setAttribute("menuActive", "e1");
            return;
        }
        if (uri.indexOf("/agentRunState") > -1) {
            session.setAttribute("menuActive", "f1");
            return;
        }
        if (uri.indexOf("/largeModel") > -1) {
            session.setAttribute("menuActive", "g1");
            return;
        }
        session.setAttribute("menuActive", "01");
    }
}

