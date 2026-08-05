/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.controller;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.ShearCaptcha;
import com.wgcloud.config.CommonConfig;
import java.awt.Color;
import java.awt.Font;
import java.io.IOException;
import java.io.OutputStream;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value={"/vercode"})
public class CodeController {
    private static final Logger logger = LoggerFactory.getLogger(CodeController.class);
    @Resource
    private CommonConfig commonConfig;
    private static final Color BACKGROUND = Color.WHITE;
    private static final Font FONT = new Font("Arial", 1, 36);

    @RequestMapping(value={"get"})
    public void hostInfoList(Model model, HttpServletRequest request, HttpServletResponse response) {
        if (!"true".equals(this.commonConfig.getVercodeCheck())) {
            return;
        }
        try {
            ShearCaptcha captcha = CaptchaUtil.createShearCaptcha((int)150, (int)40, (int)4, (int)4);
            captcha.setBackground(BACKGROUND);
            captcha.setFont(FONT);
            captcha.write((OutputStream)response.getOutputStream());
            String verifyCode = captcha.getCode();
            request.getSession().setAttribute("validateCode", (Object)verifyCode.toLowerCase());
        }
        catch (IOException e) {
            logger.error("\u751f\u6210\u9a8c\u8bc1\u7801\u5f02\u5e38", (Throwable)e);
        }
    }
}

