/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.controller;

import com.sun.mail.util.MailSSLSocketFactory;
import com.wgcloud.config.CommonConfig;
import com.wgcloud.config.MailConfig;
import com.wgcloud.entity.MailSet;
import com.wgcloud.service.DbInfoService;
import com.wgcloud.service.FtpInfoService;
import com.wgcloud.service.LogInfoService;
import com.wgcloud.service.MailSetService;
import com.wgcloud.util.DESUtil;
import com.wgcloud.util.DateUtil;
import com.wgcloud.util.staticvar.StaticKeys;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import javax.annotation.Resource;
import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.HtmlEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value={"/mailset"})
public class MailSetController {
    private static final Logger logger = LoggerFactory.getLogger(MailSetController.class);
    @Resource
    private MailSetService mailSetService;
    @Resource
    private LogInfoService logInfoService;
    @Resource
    private FtpInfoService ftpInfoService;
    @Resource
    private DbInfoService dbInfoService;
    @Autowired
    private CommonConfig commonConfig;
    @Autowired
    private MailConfig mailConfig;

    @RequestMapping(value={"list"})
    public String MailSetList(MailSet MailSet2, Model model, HttpServletRequest request) {
        HashMap<String, Object> params = new HashMap<String, Object>();
        try {
            List<MailSet> list = this.mailSetService.selectAllByParams(params);
            if (list.size() > 0) {
                MailSet selectMailSet = list.get(0);
                selectMailSet.setFromPwd(DESUtil.decryptForServerDb(selectMailSet.getFromPwd()));
                model.addAttribute("mailSet", (Object)selectMailSet);
            } else {
                MailSet mailSetTemp = new MailSet();
                mailSetTemp.setFromMailName("");
                model.addAttribute("mailSet", (Object)mailSetTemp);
            }
        }
        catch (Exception e) {
            logger.error("\u67e5\u8be2\u90ae\u4ef6\u8bbe\u7f6e\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("\u67e5\u8be2\u90ae\u4ef6\u8bbe\u7f6e\u9519\u8bef", e.toString(), "2");
        }
        String msg = request.getParameter("msg");
        if (!StringUtils.isEmpty((CharSequence)msg)) {
            if ("save".equals(msg)) {
                model.addAttribute("msg", "\u4fdd\u5b58\u6210\u529f");
            } else if ("setActive".equals(msg)) {
                model.addAttribute("msg", "\u8bbe\u7f6e\u6210\u529f");
            } else {
                model.addAttribute("msg", "\u5220\u9664\u6210\u529f");
            }
        } else {
            model.addAttribute("msg", "");
        }
        return "mail/view";
    }

    @RequestMapping(value={"save"})
    public String saveMailSet(MailSet mailSet, Model model, HttpServletRequest request) {
        try {
            mailSet.setFromPwd(DESUtil.encryptionForServerDb(mailSet.getFromPwd()));
            if (StringUtils.isEmpty((CharSequence)mailSet.getId())) {
                this.mailSetService.save(mailSet);
                this.mailSetService.saveLog(request, "\u6dfb\u52a0", mailSet);
            } else {
                this.mailSetService.updateById(mailSet);
                this.mailSetService.saveLog(request, "\u4fee\u6539", mailSet);
            }
        }
        catch (Exception e) {
            logger.error("\u4fdd\u5b58\u90ae\u4ef6\u8bbe\u7f6e\u4fe1\u606f\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("\u4fdd\u5b58\u90ae\u4ef6\u8bbe\u7f6e\u4fe1\u606f\u9519\u8bef", e.toString(), "2");
        }
        return "redirect:/mailset/list?msg=save";
    }

    @ResponseBody
    @RequestMapping(value={"test"})
    public String test(MailSet mailSet, Model model, HttpServletRequest request) {
        String result = "success";
        try {
            if (StringUtils.isEmpty((CharSequence)mailSet.getFromMailName()) || StringUtils.isEmpty((CharSequence)mailSet.getFromPwd()) || StringUtils.isEmpty((CharSequence)mailSet.getSmtpHost()) || StringUtils.isEmpty((CharSequence)mailSet.getSmtpPort()) || StringUtils.isEmpty((CharSequence)mailSet.getToMail())) {
                return "\u7f3a\u5c11\u5fc5\u586b\u53c2\u6570";
            }
            mailSet.setFromPwd(DESUtil.encryptionForServerDb(mailSet.getFromPwd()));
            if (StringUtils.isEmpty((CharSequence)mailSet.getId())) {
                this.mailSetService.save(mailSet);
                this.mailSetService.saveLog(request, "\u6dfb\u52a0", mailSet);
            } else {
                this.mailSetService.updateById(mailSet);
                this.mailSetService.saveLog(request, "\u4fee\u6539", mailSet);
            }
            HashMap<String, Object> params = new HashMap<String, Object>();
            List<MailSet> list = this.mailSetService.selectAllByParams(params);
            if (list.size() > 0) {
                mailSet = list.get(0);
            }
            if (!StringUtils.isEmpty((CharSequence)mailSet.getFromPwd())) {
                mailSet.setFromPwd(DESUtil.decryptForServerDb(mailSet.getFromPwd()));
            }
            StaticKeys.mailSet = mailSet;
            result = this.sendMail(mailSet.getToMail(), "\u6d4b\u8bd5\u90ae\u4ef6\u53d1\u9001", "\u6d4b\u8bd5\u90ae\u4ef6\u53d1\u9001");
        }
        catch (Exception e) {
            logger.error("\u6d4b\u8bd5\u90ae\u4ef6\u8bbe\u7f6e\u4fe1\u606f\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("\u6d4b\u8bd5\u90ae\u4ef6\u8bbe\u7f6e\u4fe1\u606f\u9519\u8bef", e.toString(), "2");
            result = e.toString();
        }
        return result;
    }

    @RequestMapping(value={"del"})
    public String delete(Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        String errorMsg = "\u5220\u9664\u544a\u8b66\u90ae\u4ef6\u8bbe\u7f6e\u9519\u8bef";
        try {
            if (!StringUtils.isEmpty((CharSequence)request.getParameter("id"))) {
                this.mailSetService.deleteById(request.getParameter("id").split(","));
                this.mailSetService.saveLog(request, "\u5220\u9664", new MailSet());
            }
        }
        catch (Exception e) {
            logger.error(errorMsg, (Throwable)e);
            this.logInfoService.save(errorMsg, e.toString(), "2");
        }
        return "redirect:/mailset/list?msg=del";
    }

    @RequestMapping(value={"setActive"})
    public String setActive(Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        String errorMsg = "\u6682\u505c/\u542f\u7528\u544a\u8b66\u90ae\u4ef6\u8bbe\u7f6e\u9519\u8bef";
        try {
            if (!StringUtils.isEmpty((CharSequence)request.getParameter("id"))) {
                MailSet mailSet = new MailSet();
                mailSet.setActive(request.getParameter("active"));
                mailSet.setId(request.getParameter("id"));
                this.mailSetService.updateById(mailSet);
                this.mailSetService.saveLog(request, "\u4fee\u6539", new MailSet());
            }
        }
        catch (Exception e) {
            logger.error(errorMsg, (Throwable)e);
            this.logInfoService.save(errorMsg, e.toString(), "2");
        }
        return "redirect:/mailset/list?msg=setActive";
    }

    private String sendMail(String mails, String mailTitle, String mailContent) {
        if (StringUtils.isEmpty((CharSequence)mails)) {
            return "error";
        }
        if ("2".equals(StaticKeys.mailSet.getActive())) {
            return "error";
        }
        if (mails.startsWith(";")) {
            mails = mails.substring(1);
        }
        if ("true".equals(StaticKeys.mailSet.getJavaxMail())) {
            return this.sendMailByJavax(mails, mailTitle, mailContent);
        }
        try {
            String mailTitlePrefix = "[\u667a\u7b97\u8fd0\u7ef4\u5e73\u53f0] ";
            String mailContentSuffix = "<p><p><p>\u667a\u7b97\u8fd0\u7ef4\u5e73\u53f0\u656c\u4e0a";
            if (StaticKeys.LICENSE_STATE.equals("1")) {
                mailTitlePrefix = this.commonConfig.getMailTitlePrefix();
                mailContentSuffix = this.commonConfig.getMailContentSuffix();
            }
            HtmlEmail email = new HtmlEmail();
            email.setHostName(StaticKeys.mailSet.getSmtpHost());
            email.setSmtpPort(Integer.valueOf(StaticKeys.mailSet.getSmtpPort()).intValue());
            if ("1".equals(StaticKeys.mailSet.getSmtpSSL())) {
                email.setSSLOnConnect(true);
            }
            email.setAuthenticator((Authenticator)new DefaultAuthenticator(StaticKeys.mailSet.getFromMailName(), StaticKeys.mailSet.getFromPwd()));
            email.setFrom(StaticKeys.mailSet.getFromMailName());
            email.setSubject(mailTitlePrefix + mailTitle);
            email.setCharset("UTF-8");
            email.setHtmlMsg(mailContent + "<p><p><p><p>" + DateUtil.getCurrentDateTime() + mailContentSuffix);
            email.addTo(mails.split(";"));
            email.setSentDate(new Date());
            email.send();
            return "success";
        }
        catch (Exception e) {
            logger.error("\u6d4b\u8bd5\u53d1\u9001\u90ae\u4ef6\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("\u6d4b\u8bd5\u53d1\u9001\u90ae\u4ef6\u9519\u8bef", e.toString(), "1");
            return e.toString();
        }
    }

    private String sendMailByJavax(String mails, String mailTitle, String mailContent) {
        if (StringUtils.isEmpty((CharSequence)mails)) {
            return "error";
        }
        if (mails.startsWith(";")) {
            mails = mails.substring(1);
        }
        try {
            String mailTitlePrefix = "[\u667a\u7b97\u8fd0\u7ef4\u5e73\u53f0] ";
            String mailContentSuffix = "<p><p><p>\u667a\u7b97\u8fd0\u7ef4\u5e73\u53f0\u656c\u4e0a";
            if (StaticKeys.LICENSE_STATE.equals("1")) {
                mailTitlePrefix = this.commonConfig.getMailTitlePrefix();
                mailContentSuffix = this.commonConfig.getMailContentSuffix();
            }
            Properties prop = new Properties();
            prop.setProperty("mail.host", StaticKeys.mailSet.getSmtpHost());
            prop.setProperty("mail.transport.protocol", "smtp");
            prop.setProperty("mail.smtp.auth", "true");
            prop.setProperty("mail.smtp.port", StaticKeys.mailSet.getSmtpPort());
            if ("1".equals(StaticKeys.mailSet.getSmtpSSL())) {
                prop.setProperty("mail.smtp.starttls.enable", "true");
            } else {
                prop.setProperty("mail.smtp.starttls.enable", "false");
            }
            MailSSLSocketFactory sf = new MailSSLSocketFactory();
            sf.setTrustAllHosts(true);
            prop.put("mail.smtp.ssl.socketFactory", sf);
            prop.put("mail.smtp.ssl.protocols", "TLSv1.2");
            Session session = Session.getInstance((Properties)prop, null);
            session.setDebug(false);
            Transport ts = session.getTransport();
            ts.connect(StaticKeys.mailSet.getSmtpHost(), StaticKeys.mailSet.getFromMailName(), StaticKeys.mailSet.getFromPwd());
            MimeMessage message = new MimeMessage(session);
            message.setFrom((Address)new InternetAddress(StaticKeys.mailSet.getFromMailName()));
            String[] mailArr = mails.split(";");
            InternetAddress[] internetAddressArr = new InternetAddress[mailArr.length];
            for (int i = 0; i < mailArr.length; ++i) {
                internetAddressArr[i] = new InternetAddress(mailArr[i]);
            }
            message.setRecipients(Message.RecipientType.TO, (Address[])internetAddressArr);
            message.setSubject(mailTitlePrefix + mailTitle);
            message.setContent((Object)(mailContent + "<p><p><p><p>" + DateUtil.getCurrentDateTime() + mailContentSuffix), "text/html;charset=UTF-8");
            message.saveChanges();
            ts.sendMessage((Message)message, message.getAllRecipients());
            ts.close();
            return "success";
        }
        catch (Exception e) {
            logger.error("\u6d4b\u8bd5\u53d1\u9001\u90ae\u4ef6\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("\u6d4b\u8bd5\u53d1\u9001\u90ae\u4ef6\u9519\u8bef", e.toString(), "1");
            return e.toString();
        }
    }
}

