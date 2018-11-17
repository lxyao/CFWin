package com.cfwin.base.utils.encoded;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Administrator on 2018/9/28.
 */
public class AESUtil {
    /**
     * @param rawKey
     *            密钥
     * @param clearPwd
     *            明文字符串
     * @return 密文字节数组
     */
    public static byte[] encrypt(byte[] rawKey, String clearPwd) {
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(rawKey, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            byte[] encypted = cipher.doFinal(clearPwd.getBytes());
            return encypted;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * @param encrypted
     *            密文字节数组
     * @param rawKey
     *            密钥
     * @return 解密后的字符串
     */
    public static byte[] decrypt(byte[] encrypted, byte[] rawKey) {
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(rawKey, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
            byte[] decrypted = cipher.doFinal(encrypted);
            return decrypted;
            //return new String(decrypted);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * @param seed种子数据
     * @return 密钥数据
     */
    public static byte[] getRawKey(byte[] seed) {
        byte[] rawKey = null;
        try {
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            secureRandom.setSeed(seed);
            // AES加密数据块分组长度必须为128比特，密钥长度可以是128比特、192比特、256比特中的任意一个
            kgen.init(128, secureRandom);
            SecretKey secretKey = kgen.generateKey();
            rawKey = secretKey.getEncoded();
        } catch (NoSuchAlgorithmException e) {
        }
        return rawKey;
    }
}
