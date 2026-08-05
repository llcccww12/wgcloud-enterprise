/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.util;

import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import com.wgcloud.common.ApplicationContextHelper;
import com.wgcloud.config.CommonConfig;
import com.wgcloud.util.MD5Utils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DESUtil {
    private static final Logger logger = LoggerFactory.getLogger(DESUtil.class);
    private static CommonConfig commonConfig = ApplicationContextHelper.getBean(CommonConfig.class);
    private static SymmetricCrypto des = null;
    private static SymmetricCrypto desForServerDb = null;

    private void testThread() {
        new Thread(() -> logger.info("\u542f\u52a8\u5b50\u7ebf\u7a0b\u6d4b\u8bd5")).start();
    }

    private static void initDes() {
        if (null == des) {
            String wgTokenPack = MD5Utils.GetMD5Code(commonConfig.getWgToken());
            des = new SymmetricCrypto(SymmetricAlgorithm.DES, wgTokenPack.getBytes());
        }
    }

    public static String decrypt(String content) {
        try {
            if (StringUtils.isEmpty((CharSequence)content)) {
                return "";
            }
            if (des == null) {
                DESUtil.initDes();
            }
            return des.decryptStr(content);
        }
        catch (Exception e) {
            logger.error("decrypt", (Throwable)e);
            return "";
        }
    }

    public static String encryption(String content) {
        if (StringUtils.isEmpty((CharSequence)content)) {
            return "";
        }
        if (des == null) {
            DESUtil.initDes();
        }
        return des.encryptHex(content);
    }

    private static void initDesForServerDb() {
        if (null == desForServerDb) {
            String wgTokenPack = MD5Utils.GetMD5Code("wgcloud20230402");
            desForServerDb = new SymmetricCrypto(SymmetricAlgorithm.DES, wgTokenPack.getBytes());
        }
    }

    public static String decryptForServerDb(String content) {
        try {
            if (StringUtils.isEmpty((CharSequence)content)) {
                return "";
            }
            if (desForServerDb == null) {
                DESUtil.initDesForServerDb();
            }
            return desForServerDb.decryptStr(content);
        }
        catch (Exception e) {
            logger.error("decryptForServerDb", (Throwable)e);
            return "";
        }
    }

    public static String encryptionForServerDb(String content) {
        if (StringUtils.isEmpty((CharSequence)content)) {
            return "";
        }
        if (desForServerDb == null) {
            DESUtil.initDesForServerDb();
        }
        return desForServerDb.encryptHex(content);
    }
}

