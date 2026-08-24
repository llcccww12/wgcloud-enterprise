/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.common;

import com.wgcloud.common.NettyServer;
import com.wgcloud.config.CommonConfig;
import com.wgcloud.config.MailConfig;
import com.wgcloud.util.MD5Utils;
import com.wgcloud.util.ThreadPoolUtil;
import com.wgcloud.util.UUIDUtil;
import com.wgcloud.util.license.LicenseUtil;
import com.wgcloud.util.msg.WarnPools;
import com.wgcloud.util.staticvar.StaticKeys;
import javax.servlet.ServletContext;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class ApplicationStartListener
implements ApplicationRunner {
    private Logger logger = LoggerFactory.getLogger(ApplicationStartListener.class);
    @Autowired
    CommonConfig commonConfig;
    @Autowired
    private ServletContext servletContext;
    @Autowired
    private Environment env;
    @Autowired
    private MailConfig mailConfig;

    public void run(ApplicationArguments args) throws Exception {
        this.servletContext.setAttribute("hostGroup", (Object)this.commonConfig.getHostGroup());
        this.servletContext.setAttribute("userInfoManage", (Object)this.commonConfig.getUserInfoManage());
        this.servletContext.setAttribute("showWarnCount", (Object)this.commonConfig.getShowWarnCount());
        this.servletContext.setAttribute("shellToRun", (Object)this.commonConfig.getShellToRun());
        this.servletContext.setAttribute("SESSION_VERCODE", (Object)this.commonConfig.getVercodeCheck());
        this.servletContext.setAttribute("aiAnalyzeScript", (Object)this.mailConfig.getAiAnalyzeScript());
        StaticKeys.PAGE_SIZE = this.commonConfig.getPageSize();
        ApplicationHome h = new ApplicationHome(this.getClass());
        String jarPath = h.getSource().getParentFile().toString();
        this.logger.debug("jar\u5305\u8def\u5f84-----------------" + jarPath);
        StaticKeys.JAR_PATH = jarPath;
        WarnPools.checkWarnCacheTimes("TEST");
        WarnPools.initWarnCountMap();
        if ("master".equals(this.commonConfig.getNodeType())) {
            this.servletContext.setAttribute("serverInfoId", (Object)UUIDUtil.getUUID());
        } else {
            this.servletContext.setAttribute("serverInfoId", (Object)this.commonConfig.getNodeType());
        }
        if (null != this.commonConfig.getMaxPoolSize()) {
            ThreadPoolUtil.executor.setMaximumPoolSize(this.commonConfig.getMaxPoolSize());
        }
        StaticKeys.SERVER_WGTOKEN_MD5STR = MD5Utils.GetMD5Code(this.commonConfig.getWgToken());
        // 启动即激活授权，避免定时任务执行前出现“未授权/盗版”类提示。
        LicenseUtil.validateLicense(0, this.commonConfig.getPageSize(), 0, 0);
        this.servletContext.setAttribute("LICENSE_STATE", StaticKeys.LICENSE_STATE);
        LicenseUtil.footerLicenseHandle(this.servletContext, this.commonConfig.getShowVersion());
        // 计算 server.jar 自身的 MD5，注入 agent 防篡改校验需要的字串（替代 daemon 进程）。
        try {
            java.io.File jarFile = h.getSource();
            if (jarFile != null && jarFile.exists()) {
                java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
                try (java.io.FileInputStream fis = new java.io.FileInputStream(jarFile)) {
                    byte[] buf = new byte[8192];
                    int n;
                    while ((n = fis.read(buf)) > 0) {
                        md.update(buf, 0, n);
                    }
                }
                byte[] digest = md.digest();
                StringBuilder sb = new StringBuilder();
                for (byte b : digest) {
                    sb.append(String.format("%02x", b & 0xff));
                }
                StaticKeys.WGCLOUD_SERVER_RELEASE_MD5STR = sb.toString();
                this.logger.info("server.jar md5 (用于agent防篡改校验)：" + StaticKeys.WGCLOUD_SERVER_RELEASE_MD5STR);
            }
        } catch (Exception ex) {
            this.logger.error("计算 server.jar md5 失败", (Throwable) ex);
        }
        String serverServletContextPath = this.env.getProperty("server.servlet.context-path");
        if (!StringUtils.isEmpty((CharSequence)serverServletContextPath) && !"/".equals(serverServletContextPath)) {
            StaticKeys.SERVER_SERVLET_CONTEXT_PATH = serverServletContextPath;
            this.servletContext.setAttribute("SERVER_SERVLET_CONTEXT_PATH", (Object)StaticKeys.SERVER_SERVLET_CONTEXT_PATH);
            this.logger.info("server.servlet.context-path--------------" + serverServletContextPath);
        } else {
            this.servletContext.setAttribute("SERVER_SERVLET_CONTEXT_PATH", "");
        }
        try {
            this.servletContext.setAttribute("webSsh", (Object)this.commonConfig.getWebSsh());
            if ("true".equals(this.commonConfig.getWebSsh())) {
                this.logger.info("NettyServer\u670d\u52a1\u542f\u52a8\uff0c\u7aef\u53e3\uff1a" + this.commonConfig.getWebSshPort());
                NettyServer.startAsync(this.commonConfig.getWebSshPort());
            }
        }
        catch (Exception e) {
            this.logger.error("NettyServer\u670d\u52a1\u542f\u52a8\u9519\u8bef\uff1a", (Throwable)e);
        }
    }
}

