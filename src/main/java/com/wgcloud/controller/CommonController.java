/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.controller;

import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value={"/common/error"})
public class CommonController {
    @RequestMapping(value={"404"})
    public String to404(Model model, HttpServletRequest request) {
        return "error/404";
    }

    @RequestMapping(value={"500"})
    public String to500(Model model, HttpServletRequest request) {
        return "error/500";
    }

    @RequestMapping(value={"guestError"})
    public String toGuestError(Model model, HttpServletRequest request) {
        return "error/guestError";
    }
}

