/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.util;

import cn.hutool.core.text.UnicodeUtil;
import com.wgcloud.common.ApplicationContextHelper;
import com.wgcloud.config.LevelConfig;
import com.wgcloud.config.MailConfig;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExecUtil {
    private static final Logger logger = LoggerFactory.getLogger(ExecUtil.class);
    private static MailConfig mailConfig = ApplicationContextHelper.getBean(MailConfig.class);
    private static LevelConfig levelConfig = ApplicationContextHelper.getBean(LevelConfig.class);

    public static String runScript(String content, String accountKey, String warnLevel, String groupNames, String title) {
        String warnScriptPath = mailConfig.getWarnScript();
        String recoverScriptPath = mailConfig.getRecoverScript();
        String path = warnScriptPath;
        if (title.indexOf("\u5df2\u6062\u590d") > -1 && !StringUtils.isEmpty((CharSequence)recoverScriptPath)) {
            path = recoverScriptPath;
        }
        if (StringUtils.isEmpty((CharSequence)path)) {
            return "";
        }
        try {
            content = content.replace("</br>", "\\n");
            content = ExecUtil.getScriptContentPrefix(warnLevel, groupNames) + content;
            content = "true".equals(mailConfig.getWarnToUnicode()) ? UnicodeUtil.toUnicode((String)content, (boolean)false) : content.replace(" ", "\\u0020");
            if (StringUtils.isEmpty((CharSequence)accountKey)) {
                accountKey = "NULL";
            }
            if (StringUtils.isEmpty((CharSequence)warnLevel)) {
                warnLevel = "ERROR";
            }
            if (StringUtils.isEmpty((CharSequence)groupNames)) {
                groupNames = "NULL";
            }
            String execCmdStr = path + " \"" + content + "\" \"" + accountKey + "\" \"" + warnLevel + "\" \"" + groupNames + "\"";
            String[] paramArr = path.split(" ");
            ArrayList<String> paramList = new ArrayList<String>();
            for (String param : paramArr) {
                paramList.add(param);
            }
            paramList.add(content);
            paramList.add(accountKey);
            paramList.add(warnLevel);
            paramList.add(groupNames);
            ProcessBuilder pb = new ProcessBuilder(paramList);
            Process process = pb.start();
            logger.info("\u6267\u884c\u544a\u8b66\u811a\u672c\u5b8c\u6210-----------" + execCmdStr);
            try {
                process.waitFor(10L, TimeUnit.SECONDS);
            }
            catch (Exception e) {
                logger.error("\u7b49\u5f85\u811a\u672c\u6267\u884c\u7ed3\u675f\u9519\u8bef", (Throwable)e);
            }
            process.destroy();
        }
        catch (Exception e) {
            logger.error("\u6267\u884c\u544a\u8b66\u811a\u672c\u9519\u8bef", (Throwable)e);
            return e.toString();
        }
        return "";
    }

    public static String getScriptContentPrefix(String warnLevelName, String groupsName) {
        String scriptContentPrefix = "";
        if ("true".equals(levelConfig.getAddToWarnContent())) {
            if (!StringUtils.isEmpty((CharSequence)warnLevelName)) {
                scriptContentPrefix = scriptContentPrefix + "\u544a\u8b66\u7ea7\u522b\uff1a" + warnLevelName + "\uff0c";
            }
            if (!StringUtils.isEmpty((CharSequence)groupsName)) {
                scriptContentPrefix = scriptContentPrefix + "\u6807\u7b7e\uff1a" + groupsName + "\uff0c";
            }
        }
        return scriptContentPrefix;
    }
}

