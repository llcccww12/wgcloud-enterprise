/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.util.license;

import cn.hutool.core.codec.Base64;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class RSAEncrypt {
    public static RSAPublicKey loadPublicKeyByStr(String publicKeyStr) throws Exception {
        try {
            byte[] buffer = Base64.decode((CharSequence)publicKeyStr);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
            return (RSAPublicKey)keyFactory.generatePublic(keySpec);
        }
        catch (NoSuchAlgorithmException e) {
            throw new Exception("\u65e0\u6b64\u7b97\u6cd5");
        }
        catch (InvalidKeySpecException e) {
            throw new Exception("\u516c\u94a5\u975e\u6cd5");
        }
        catch (NullPointerException e) {
            throw new Exception("\u516c\u94a5\u6570\u636e\u4e3a\u7a7a");
        }
    }

    public static byte[] decrypt(RSAPublicKey publicKey, byte[] cipherData) throws Exception {
        if (publicKey == null) {
            throw new Exception("\u89e3\u5bc6\u516c\u94a5\u4e3a\u7a7a, \u8bf7\u8bbe\u7f6e");
        }
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance("RSA");
            cipher.init(2, publicKey);
            byte[] output = cipher.doFinal(cipherData);
            return output;
        }
        catch (NoSuchAlgorithmException e) {
            throw new Exception("\u65e0\u6b64\u89e3\u5bc6\u7b97\u6cd5");
        }
        catch (NoSuchPaddingException e) {
            e.printStackTrace();
            return null;
        }
        catch (InvalidKeyException e) {
            throw new Exception("\u89e3\u5bc6\u516c\u94a5\u975e\u6cd5,\u8bf7\u68c0\u67e5");
        }
        catch (IllegalBlockSizeException e) {
            throw new Exception("\u5bc6\u6587\u957f\u5ea6\u975e\u6cd5");
        }
        catch (BadPaddingException e) {
            throw new Exception("\u5bc6\u6587\u6570\u636e\u5df2\u635f\u574f");
        }
    }
}

