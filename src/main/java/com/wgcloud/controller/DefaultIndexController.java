/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.controller;

import com.wgcloud.util.staticvar.StaticKeys;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class DefaultIndexController {
    private static final Logger logger = LoggerFactory.getLogger(DefaultIndexController.class);

    @RequestMapping(value={"/"})
    public String defaultPath() {
        return "login/login";
    }

    @RequestMapping(value={"/wgcloud"})
    public String defaultPathWgcloud(Model model, HttpServletRequest request, HttpServletResponse response) {
        try {
            if (StaticKeys.SERVER_SERVLET_CONTEXT_PATH.equals("")) {
                response.setContentType("text/html;charset=UTF-8");
                response.getOutputStream().write("Please remove the string /wgcloud from the access URL and access it again".getBytes());
                return null;
            }
        }
        catch (Exception e) {
            logger.error("defaultPathWgcloud\u9519\u8bef", (Throwable)e);
        }
        return "login/login";
    }
}

