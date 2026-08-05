/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class IndexController
implements WebMvcConfigurer {
    private static final Logger logger = LoggerFactory.getLogger(IndexController.class);

    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        ApplicationHome h = new ApplicationHome(this.getClass());
        String jarPath = h.getSource().getParentFile().toString();
        logger.debug("jar\u5305\u8def\u5f84-----------------" + jarPath);
        registry.addResourceHandler(new String[]{"/resources/**"}).addResourceLocations(new String[]{"file:" + jarPath + "/logo/"});
        registry.addResourceHandler(new String[]{"/uploadFile/**"}).addResourceLocations(new String[]{"file:" + jarPath + "/uploadFile/"});
    }
}

