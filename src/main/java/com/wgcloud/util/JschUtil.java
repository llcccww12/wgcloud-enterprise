/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.util;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.wgcloud.common.ApplicationContextHelper;
import com.wgcloud.entity.FtpInfo;
import com.wgcloud.service.LogInfoService;
import java.io.IOException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JschUtil {
    private static final Logger logger = LoggerFactory.getLogger(JschUtil.class);
    private static LogInfoService logInfoService = ApplicationContextHelper.getBean(LogInfoService.class);

    public static void closeSession(Session session) {
        if (null != session && session.isConnected()) {
            session.disconnect();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static String testSFtpSession(FtpInfo ftpInfo) {
        String string;
        Session session = null;
        try {
            long startTimes = System.currentTimeMillis();
            JSch jSch = new JSch();
            String privateKeyFile = null;
            String passphrase = null;
            if (!StringUtils.isEmpty(privateKeyFile)) {
                if (!StringUtils.isEmpty(passphrase)) {
                    jSch.addIdentity(privateKeyFile, passphrase);
                } else {
                    jSch.addIdentity(privateKeyFile);
                }
            }
            session = jSch.getSession(ftpInfo.getUserName(), ftpInfo.getFtpHost(), Integer.valueOf(ftpInfo.getPort()).intValue());
            if (!StringUtils.isEmpty((CharSequence)ftpInfo.getPasswd())) {
                session.setPassword(ftpInfo.getPasswd());
            }
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect(20000);
            long endTimes = System.currentTimeMillis();
            String resTimes = endTimes - startTimes + "";
            ftpInfo.setResTimes(Integer.valueOf(resTimes));
            if (null != session && session.isConnected()) {
                ftpInfo.setState("1");
            } else {
                ftpInfo.setState("2");
            }
            string = "success";
            if (null == session) return string;
        }
        catch (Exception e) {
            try {
                ftpInfo.setResTimes(20000);
                ftpInfo.setState("2");
                logger.error("testFtpSession connect error:" + ftpInfo.getFtpHost(), (Throwable)e);
                ftpInfo.setTestErrorMsg(e.toString());
                logInfoService.save("\u6d4b\u8bd5sftp\u8fde\u901a\u9519\u8bef:" + ftpInfo.getFtpHost(), e.toString(), "2");
                if (null == session) return "error";
            }
            catch (Throwable throwable) {
                if (null == session) throw throwable;
                JschUtil.closeSession(session);
                throw throwable;
            }
            JschUtil.closeSession(session);
            return "error";
        }
        JschUtil.closeSession(session);
        return string;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static String testFTPClient(FtpInfo ftpInfo) {
        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.setControlEncoding("UTF-8");
            long startTimes = System.currentTimeMillis();
            ftpClient.setConnectTimeout(20000);
            ftpClient.connect(ftpInfo.getFtpHost(), Integer.valueOf(ftpInfo.getPort()).intValue());
            ftpClient.enterLocalPassiveMode();
            boolean success = ftpClient.login(ftpInfo.getUserName(), ftpInfo.getPasswd());
            long endTimes = System.currentTimeMillis();
            String resTimes = endTimes - startTimes + "";
            ftpInfo.setResTimes(Integer.valueOf(resTimes));
            if (!success) {
                ftpInfo.setState("2");
                logger.error("Could not login to the ftp server:" + ftpInfo.getFtpHost());
                ftpInfo.setTestErrorMsg("Could not login to the ftp server:" + ftpInfo.getFtpHost());
                String string = "error";
                return string;
            }
            ftpInfo.setState("1");
            int reply = ftpClient.getReplyCode();
            String string = "success";
            return string;
        }
        catch (Exception e) {
            ftpInfo.setResTimes(20000);
            ftpInfo.setState("2");
            logger.error("testFTPClient connect error:" + ftpInfo.getFtpHost(), (Throwable)e);
            ftpInfo.setTestErrorMsg(e.toString());
            logInfoService.save("\u6d4b\u8bd5ftp\u8fde\u901a\u9519\u8bef:" + ftpInfo.getFtpHost(), e.toString(), "2");
        }
        finally {
            JschUtil.closeFTPClient(ftpClient);
        }
        return "error";
    }

    public static void closeFTPClient(FTPClient ftp) {
        try {
            ftp.logout();
        }
        catch (Exception e) {
            logger.error("FTP\u5173\u95ed\u5931\u8d25", (Throwable)e);
        }
        finally {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                }
                catch (IOException e) {
                    logger.error("FTP\u5173\u95ed\u5931\u8d25", (Throwable)e);
                }
            }
        }
    }
}

