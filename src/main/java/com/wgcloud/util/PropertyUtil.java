/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.util;

import com.wgcloud.util.FileUtils;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PropertyUtil {
    private static final Logger logger = LoggerFactory.getLogger(PropertyUtil.class);

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static Map<String, String> getMenuNames(String filePath) {
        if (!FileUtils.existsFile(filePath)) {
            return null;
        }
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(filePath);
            if (null == inputStream) {
                Map<String, String> map2 = null;
                return map2;
            }
            Properties properties = new Properties();
            properties.load(new InputStreamReader((InputStream)inputStream, "UTF-8"));
            Map<String, String> map = new HashMap<String, String>();
            for (Object k : properties.keySet()) {
                map.put(String.valueOf(k), properties.getProperty(String.valueOf(k)));
            }
            return map;
        }
        catch (Exception e) {
            logger.error("\u8bfb\u53d6\u6587\u4ef6menusNameDiy.properties\u9519\u8bef", (Throwable)e);
        }
        finally {
            if (inputStream != null) {
                try {
                    ((InputStream)inputStream).close();
                }
                catch (IOException e) {
                    logger.error("\u8bfb\u53d6\u6587\u4ef6menusNameDiy.properties\u9519\u8bef", (Throwable)e);
                }
            }
        }
        return null;
    }

    public static Set<String> getKeys(String fileName) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream in = classLoader.getResourceAsStream(fileName);
        Properties p = new Properties();
        Set<String> keySet = null;
        try {
            p.load(in);
            keySet = p.stringPropertyNames();
            in.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return keySet;
    }

    public static String get(String fileName, String propertyName) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream in = classLoader.getResourceAsStream(fileName);
        Properties p = new Properties();
        String msg = "";
        try {
            p.load(in);
            msg = (String)p.get(propertyName);
            in.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return msg;
    }
}

