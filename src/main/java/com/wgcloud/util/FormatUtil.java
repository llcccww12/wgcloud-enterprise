/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.util;

import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.Expression;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FormatUtil {
    private static Logger logger = LoggerFactory.getLogger(FormatUtil.class);

    public static String timesToDay(Long l) {
        if (l == null) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        long seconds = 1L;
        long minutes = 60L * seconds;
        long hours = 60L * minutes;
        long days = 24L * hours;
        if (l / days >= 1L) {
            sb.append((int)(l / days) + "\u5929");
        }
        if (l % days / hours >= 1L) {
            sb.append((int)(l % days / hours) + "\u5c0f\u65f6");
        }
        if (l % days % hours / minutes >= 1L) {
            sb.append((int)(l % days % hours / minutes) + "\u5206\u949f");
        }
        return sb.toString();
    }

    public static String secondsToDays(Long l) {
        if (l == null) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        long seconds = 1L;
        long minutes = 60L * seconds;
        long hours = 60L * minutes;
        long days = 24L * hours;
        if (l / days >= 1L) {
            sb.append((int)(l / days) + "\u5929");
        }
        return sb.toString();
    }

    public static String secondsToDays(String l) {
        if (StringUtils.isEmpty((CharSequence)l)) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        try {
            if (!FormatUtil.isNumber(l)) {
                return l;
            }
            Double nums = Double.valueOf(l);
            double days = FormatUtil.formatDouble(nums / 86400.0, 2);
            sb.append(days + "\u5929");
        }
        catch (Exception e) {
            logger.error("\u79d2\u8f6c\u4e3a\u5929\u9519\u8bef", (Throwable)e);
        }
        return sb.toString();
    }

    public static String getString(String str, int len) {
        if (StringUtils.isEmpty((CharSequence)str)) {
            return "";
        }
        if (str.length() <= len) {
            return str;
        }
        return str.substring(0, len);
    }

    public static String kbToM(String str) {
        if (StringUtils.isEmpty((CharSequence)str)) {
            return "0KB";
        }
        double result = 0.0;
        double mod = 1024.0;
        try {
            double strDouble = Double.valueOf(str);
            if (strDouble > 1024.0) {
                result = strDouble / mod;
                return FormatUtil.formatDouble(result, 2) + "MB";
            }
        }
        catch (Exception e) {
            logger.error("kb\u8f6c\u4e3aM\u9519\u8bef\uff1a", (Throwable)e);
            return str + "KB";
        }
        return str + "KB";
    }

    public static double kbToMDouble(String str) {
        if (StringUtils.isEmpty((CharSequence)str)) {
            return 0.0;
        }
        double result = 0.0;
        double mod = 1024.0;
        try {
            double strDouble = Double.valueOf(str);
            result = strDouble / mod;
            return FormatUtil.formatDouble(result, 2);
        }
        catch (Exception e) {
            logger.error("kb\u8f6c\u4e3aM\u9519\u8bef\uff1a", (Throwable)e);
            return 0.0;
        }
    }

    public static String mToG(String str) {
        if (StringUtils.isEmpty((CharSequence)str)) {
            return "0MB";
        }
        double result = 0.0;
        double mod = 1024.0;
        try {
            double strDouble = Double.valueOf(str);
            if (strDouble > 1024.0) {
                result = strDouble / mod;
                return FormatUtil.formatDouble(result, 2) + "GB";
            }
        }
        catch (Exception e) {
            logger.error("m\u8f6c\u4e3ag\u9519\u8bef\uff1a", (Throwable)e);
            return str + "MB";
        }
        return str + "MB";
    }

    public static String gToT(String str) {
        if (StringUtils.isEmpty((CharSequence)str)) {
            return "0G";
        }
        double result = 0.0;
        double mod = 1024.0;
        try {
            double strDouble = Double.valueOf(str);
            if (strDouble > 1024.0) {
                result = strDouble / mod;
                return FormatUtil.formatDouble(result, 1) + "T";
            }
        }
        catch (Exception e) {
            logger.error("G\u8f6c\u4e3aT\u9519\u8bef\uff1a", (Throwable)e);
            return str + "G";
        }
        return FormatUtil.formatDouble(Double.valueOf(str), 1) + "G";
    }

    public static String bytesFormatUnit(String str, String snmpUnit) {
        if (StringUtils.isEmpty((CharSequence)str) || "0".equals(str)) {
            return "0B";
        }
        if (str.indexOf(".") > -1) {
            str = str.substring(0, str.indexOf("."));
        }
        try {
            long bytes = Long.valueOf(str);
            if (!"byte".equals(snmpUnit)) {
                bytes *= 1024L;
            }
            int k = 1024;
            String[] sizes = new String[]{"B", "KB", "MB", "GB", "T", "P", "E", "Z", "Y"};
            int i = (int)Math.floor(Math.log(bytes) / Math.log(k));
            if (i > sizes.length - 1) {
                i = sizes.length - 1;
            }
            return FormatUtil.formatDouble((double)bytes / Math.pow(k, i), 2) + sizes[i];
        }
        catch (Exception e) {
            logger.error("bytesFormatUnit\u9519\u8bef", (Throwable)e);
            return str + "B";
        }
    }

    public static String byteToM(String str, String snmpUnit) {
        if (StringUtils.isEmpty((CharSequence)str)) {
            return "0B";
        }
        double result = 0.0;
        double mod = 1024.0;
        try {
            double strDouble = Double.valueOf(str);
            if (!"byte".equals(snmpUnit)) {
                strDouble *= 1024.0;
            }
            result = strDouble / mod / mod;
            return FormatUtil.formatDouble(result, 2) + "MB";
        }
        catch (Exception e) {
            logger.error("kb\u8f6c\u4e3aM\u9519\u8bef\uff1a", (Throwable)e);
            return str + "KB";
        }
    }

    public static double formatDouble(Double str, int num) {
        if (str == null) {
            return 0.0;
        }
        try {
            BigDecimal b = new BigDecimal(str);
            double myNum3 = b.setScale(num, 4).doubleValue();
            return myNum3;
        }
        catch (Exception e) {
            logger.error("formatDouble\u9519\u8bef\uff1a", (Throwable)e);
            return 0.0;
        }
    }

    public static double formatDouble(String str, int num) {
        if (StringUtils.isEmpty((CharSequence)str)) {
            return 0.0;
        }
        Double strDou = Double.valueOf(str);
        BigDecimal b = new BigDecimal(strDou);
        double myNum3 = b.setScale(num, 4).doubleValue();
        return myNum3;
    }

    public static String toLowerStr(String str) {
        if (StringUtils.isEmpty((CharSequence)str)) {
            return "";
        }
        return str.toLowerCase();
    }

    public static boolean isNumber(String str) {
        if (StringUtils.isEmpty((CharSequence)str)) {
            return false;
        }
        Boolean strResult = str.matches("-?[0-9]+.?[0-9]*");
        return strResult == true;
    }

    public static Double strToDouble(String str) {
        Double result = 0.0;
        try {
            result = Double.valueOf(str);
        }
        catch (Exception e) {
            logger.error("\u5b57\u7b26\u4e32\u8f6c\u4e3adouble\u7c7b\u578b\u9519\u8bef", (Throwable)e);
        }
        return result;
    }

    public static String haveSqlDanger(String sql, String sqlInKeys) {
        String[] sqlinkeys;
        if (StringUtils.isEmpty((CharSequence)sql)) {
            return "";
        }
        sql = sql.toLowerCase();
        sqlInKeys = sqlInKeys.toLowerCase();
        for (String sqlinkey : sqlinkeys = sqlInKeys.split(",")) {
            if (sql.indexOf(sqlinkey) <= -1) continue;
            return sqlinkey;
        }
        return "";
    }

    public static String haveBlockDanger(String shell, String shellBlock) {
        String[] blocks;
        if (StringUtils.isEmpty((CharSequence)shell)) {
            return "";
        }
        if (StringUtils.isEmpty((CharSequence)shellBlock)) {
            return "";
        }
        shell = shell.toLowerCase();
        shellBlock = shellBlock.toLowerCase();
        for (String blockStr : blocks = shellBlock.split(",")) {
            if (shell.indexOf(blockStr) <= -1) continue;
            return blockStr;
        }
        return "";
    }

    public static boolean validateExpression(String expression, Object value) {
        if (StringUtils.isEmpty((CharSequence)expression) || null == value) {
            return false;
        }
        logger.info("\u6570\u636e\u76d1\u63a7\u544a\u8b66\u8868\u8fbe\u5f0f---------" + expression);
        Expression compiledExp = AviatorEvaluator.compile((String)expression);
        HashMap<String, Object> env = new HashMap<String, Object>();
        env.put("result", value);
        Boolean result = (Boolean)compiledExp.execute(env);
        return result;
    }

    public static boolean validateExpression(String expression, Map<String, Object> env) {
        if (StringUtils.isEmpty((CharSequence)expression) || null == env) {
            return false;
        }
        logger.info("\u81ea\u5b9a\u4e49\u76d1\u63a7\u544a\u8b66\u8868\u8fbe\u5f0f---------" + expression);
        Expression compiledExp = AviatorEvaluator.compile((String)expression);
        Boolean result = (Boolean)compiledExp.execute(env);
        return result;
    }

    public static int getRandInt() {
        Random random = new Random();
        int randomNum = random.nextInt(900) + 100;
        return randomNum;
    }

    public static Double getCustomValue(int customValueIndex, String[] customValueArr) {
        Double customValueDouble = 0.0;
        try {
            if (customValueIndex < customValueArr.length) {
                if (!StringUtils.isEmpty((CharSequence)customValueArr[customValueIndex]) && FormatUtil.isNumber(customValueArr[customValueIndex])) {
                    customValueDouble = FormatUtil.strToDouble(customValueArr[customValueIndex]);
                }
                return customValueDouble;
            }
        }
        catch (Exception e) {
            logger.error("\u83b7\u53d6\u6570\u7ec4\u4e2d\u4e0b\u6807\u503c\u9519\u8bef", (Throwable)e);
        }
        return customValueDouble;
    }

    public static String msToSecond(Integer msValue) {
        Double result = 0.0;
        if (msValue == null || msValue == 0) {
            return result + "s";
        }
        try {
            result = (double)msValue.intValue() / 1000.0;
            result = FormatUtil.formatDouble(result, 2);
        }
        catch (Exception e) {
            logger.error("\u6beb\u79d2\u8f6c\u4e3a\u79d2\u9519\u8bef", (Throwable)e);
        }
        return result + "s";
    }

    public static Map<String, Object> getExpressionEnv(String values) {
        HashMap<String, Object> env = new HashMap<String, Object>();
        if (StringUtils.isEmpty((CharSequence)values)) {
            return null;
        }
        try {
            String[] value = values.split(",");
            if (value.length == 1) {
                if (FormatUtil.isNumber(value[0])) {
                    env.put("result", FormatUtil.strToDouble(value[0]));
                } else {
                    env.put("result", value[0]);
                }
            } else {
                for (int i = 0; i < value.length; ++i) {
                    if (FormatUtil.isNumber(value[i])) {
                        env.put("result" + i, FormatUtil.strToDouble(value[i]));
                        continue;
                    }
                    env.put("result" + i, value[i]);
                }
            }
        }
        catch (Exception e) {
            logger.error("\u7ec4\u88c5\u81ea\u5b9a\u4e49\u76d1\u63a7\u9879\u544a\u8b66\u8868\u8fbe\u5f0f\u7684\u53d8\u91cf\u548c\u503c", (Throwable)e);
        }
        return env;
    }
}

