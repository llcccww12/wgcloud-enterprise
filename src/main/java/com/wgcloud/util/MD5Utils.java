/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Comparator;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MD5Utils {
    private static final Logger logger = LoggerFactory.getLogger(MD5Utils.class);
    private static final String[] strDigits = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};
    private static final char[] hexdigits = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static String getMD5ForFile(String filePath) {
        FileInputStream fis = null;
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
            File file = new File(filePath);
            if (!file.exists()) {
                String string = "";
                return string;
            }
            fis = new FileInputStream(file);
            byte[] buffer = new byte[4096];
            int length = -1;
            while ((length = fis.read(buffer)) != -1) {
                md.update(buffer, 0, length);
            }
            byte[] b = md.digest();
            String string = MD5Utils.byteToHexString(b);
            return string;
        }
        catch (Exception ex) {
            logger.error("\u83b7\u53d6MD5\u4fe1\u606f\u53d1\u751f\u5f02\u5e38\uff01" + ex.toString());
            String string = null;
            return string;
        }
        finally {
            try {
                if (null != fis) {
                    fis.close();
                }
            }
            catch (IOException e) {
                logger.error("\u83b7\u53d6MD5\u4fe1\u606f\u53d1\u751f\u5f02\u5e38\uff01" + e.toString());
            }
        }
    }

    private static String byteToHexString(byte[] tmp) {
        char[] str = new char[32];
        int k = 0;
        for (int i = 0; i < 16; ++i) {
            byte byte0 = tmp[i];
            str[k++] = hexdigits[byte0 >>> 4 & 0xF];
            str[k++] = hexdigits[byte0 & 0xF];
        }
        String s = new String(str);
        return s;
    }

    private static String byteToArrayString(byte bByte) {
        int iRet = bByte;
        if (iRet < 0) {
            iRet += 256;
        }
        int iD1 = iRet / 16;
        int iD2 = iRet % 16;
        return strDigits[iD1] + strDigits[iD2];
    }

    private static String byteToNum(byte bByte) {
        int iRet = bByte;
        System.out.println("iRet1=" + iRet);
        if (iRet < 0) {
            iRet += 256;
        }
        return String.valueOf(iRet);
    }

    private static String byteToString(byte[] bByte) {
        StringBuffer sBuffer = new StringBuffer();
        for (int i = 0; i < bByte.length; ++i) {
            sBuffer.append(MD5Utils.byteToArrayString(bByte[i]));
        }
        return sBuffer.toString();
    }

    public static String GetMD5Code(String strObj) {
        if (StringUtils.isEmpty((CharSequence)strObj)) {
            return "";
        }
        String resultString = null;
        try {
            resultString = new String(strObj);
            MessageDigest md = MessageDigest.getInstance("MD5");
            resultString = MD5Utils.byteToString(md.digest(strObj.getBytes()));
            if (!StringUtils.isEmpty((CharSequence)resultString)) {
                resultString = resultString.toLowerCase();
            }
        }
        catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        }
        return resultString;
    }

    public static String goLambdaFunc() {
        String[] str = new String[]{"a", "c", "ba", "cc", "dd"};
        Arrays.sort(str, new Comparator<String>(){

            @Override
            public int compare(String o1, String o2) {
                return Integer.compare(o1.length(), o2.length());
            }
        });
        for (String s : str) {
            logger.info(s);
        }
        String[] str1 = new String[]{"a", "c", "ba", "cc", "dd"};
        Arrays.sort(str1, (o1, o2) -> Integer.compare(o1.length(), o1.length()));
        logger.info("----------------");
        for (String s : str1) {
            logger.info(s);
        }
        return "";
    }
}

