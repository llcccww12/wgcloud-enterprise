/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class AgentUtils {
    private static final Logger logger = LoggerFactory.getLogger(AgentUtils.class);

    public String checkBindIP(String bindIp, String hostName) {
        if (StringUtils.isEmpty((CharSequence)bindIp)) {
            bindIp = hostName;
            logger.error("bindIp is null");
            if (StringUtils.isEmpty((CharSequence)bindIp)) {
                return "";
            }
        }
        if (bindIp.length() > 50) {
            return bindIp.substring(0, 50);
        }
        return bindIp;
    }
}

