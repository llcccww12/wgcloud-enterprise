/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.util;

import com.wgcloud.common.ApplicationContextHelper;
import com.wgcloud.config.CommonConfig;
import com.wgcloud.entity.DceInfo;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PingUtil {
    private static final Logger logger = LoggerFactory.getLogger(PingUtil.class);
    private static CommonConfig commonConfig = ApplicationContextHelper.getBean(CommonConfig.class);

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static long pingDceInfo(DceInfo dceInfo, String ipAddress, int count, int timeOut) {
        if (StringUtils.isEmpty((CharSequence)ipAddress)) {
            return -1L;
        }
        String charsetValue = "UTF-8";
        if (!StringUtils.isEmpty((CharSequence)commonConfig.getDceCharset())) {
            charsetValue = commonConfig.getDceCharset();
        }
        BufferedReader in = null;
        Runtime r = Runtime.getRuntime();
        String osName = System.getProperty("os.name");
        String pingCommand = (osName = osName.toLowerCase()).contains("windows") ? "ping " + ipAddress + " -n " + count + " -w " + timeOut * 1000 : "ping  -c " + count + " -w " + timeOut + " " + ipAddress;
        // 强制使用英文环境，确保 ping 输出能被正确解析
        String[] envp = {"LANG=C", "LC_ALL=C"};
        Integer diffTimes = -1;
        StringBuilder fileContent = new StringBuilder();
        try {
            String line;
            Process p = r.exec(pingCommand, envp);
            if (p == null) {
                long l = -1L;
                return l;
            }
            in = new BufferedReader(new InputStreamReader(p.getInputStream(), Charset.forName(charsetValue)));
            while ((line = in.readLine()) != null) {
                fileContent.append(line).append(System.lineSeparator());
                diffTimes = osName.contains("windows") ? PingUtil.getCheckResultWindows(line) : PingUtil.getCheckResult(line);
                if (diffTimes == -1) continue;
                // 匹配到结果后跳出循环，避免被后续统计行覆盖
                break;
            }
            dceInfo.setTestErrorMsg(fileContent.toString());
        }
        catch (Exception ex) {
            logger.error("\u7f51\u7edc\u8bbe\u5907ping\u9519\u8bef", (Throwable)ex);
            long l = -1L;
            return l;
        }
        finally {
            try {
                in.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        return diffTimes.intValue();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static long ping(String ipAddress, int count, int timeOut) {
        if (StringUtils.isEmpty((CharSequence)ipAddress)) {
            return -1L;
        }
        BufferedReader in = null;
        Runtime r = Runtime.getRuntime();
        String osName = System.getProperty("os.name");
        String pingCommand = (osName = osName.toLowerCase()).contains("windows") ? "ping " + ipAddress + " -n " + count + " -w " + timeOut * 1000 : "ping  -c " + count + " -w " + timeOut + " " + ipAddress;
        // 强制使用英文环境，确保 ping 输出能被正确解析
        String[] envp = {"LANG=C", "LC_ALL=C"};
        Integer diffTimes = -1;
        try {
            String line;
            Process p = r.exec(pingCommand, envp);
            if (p == null) {
                long l = -1L;
                return l;
            }
            in = new BufferedReader(new InputStreamReader(p.getInputStream()));
            while ((line = in.readLine()) != null && (diffTimes = osName.contains("windows") ? PingUtil.getCheckResultWindows(line) : PingUtil.getCheckResult(line)) == -1) {
            }
        }
        catch (Exception ex) {
            logger.error("\u7f51\u7edc\u8bbe\u5907ping\u9519\u8bef", (Throwable)ex);
            long l = -1L;
            return l;
        }
        finally {
            try {
                in.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        return diffTimes.intValue();
    }

    private static Integer getCheckResult(String line) {
        Integer times = -1;
        if (StringUtils.isEmpty((CharSequence)line)) {
            return times;
        }
        logger.debug("PING--------------" + line);
        line = line.toLowerCase();
        if (line.contains("ttl=")) {
            line = line.replace(" ", "");
            String regInt = "(=\\d+ms)";
            Pattern patternInt = Pattern.compile(regInt);
            Matcher matcherInt = patternInt.matcher(line);
            if (matcherInt.find()) {
                String groupStr = matcherInt.group(1);
                logger.debug(line + "--------------" + groupStr);
                groupStr = groupStr.replace("=", "").replace("ms", "");
                times = new Double(groupStr).intValue();
                if (times < 1) {
                    times = 1;
                }
                return times;
            }
            String regDouble = "(=\\d+.\\d+ms)";
            Pattern patternDouble = Pattern.compile(regDouble);
            Matcher matcherDouble = patternDouble.matcher(line);
            if (matcherDouble.find()) {
                String groupStr = matcherDouble.group(1);
                logger.debug(line + "--------------" + groupStr);
                groupStr = groupStr.replace("=", "").replace("ms", "");
                times = new Double(groupStr).intValue();
                if (times < 1) {
                    times = 1;
                }
                return times;
            }
            times = 1;
        }
        return times;
    }

    private static Integer getCheckResultWindows(String line) {
        Integer times = -1;
        if (StringUtils.isEmpty((CharSequence)line)) {
            return times;
        }
        logger.debug("PING--------------" + line);
        line = line.toLowerCase();
        if (line.contains("ms")) {
            line = line.replace(" ", "");
            String regInt = "(=\\d+ms)";
            Pattern patternInt = Pattern.compile(regInt);
            Matcher matcherInt = patternInt.matcher(line);
            if (matcherInt.find()) {
                String groupStr = matcherInt.group(1);
                logger.debug(line + "--------------" + groupStr);
                groupStr = groupStr.replace("=", "").replace("ms", "");
                times = new Double(groupStr).intValue();
                if (times < 1) {
                    times = 1;
                }
                return times;
            }
            String regDouble = "(=\\d+.\\d+ms)";
            Pattern patternDouble = Pattern.compile(regDouble);
            Matcher matcherDouble = patternDouble.matcher(line);
            if (matcherDouble.find()) {
                String groupStr = matcherDouble.group(1);
                logger.debug(line + "--------------" + groupStr);
                groupStr = groupStr.replace("=", "").replace("ms", "");
                times = new Double(groupStr).intValue();
                if (times < 1) {
                    times = 1;
                }
                return times;
            }
            times = 1;
        }
        return times;
    }
}

