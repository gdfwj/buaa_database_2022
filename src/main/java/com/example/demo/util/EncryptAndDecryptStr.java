package com.example.demo.util;

import java.security.Key;
import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
/**
 * 对指定字符串进行加密或解密
 * @author rmling
 */
public class EncryptAndDecryptStr {
    private static Key key;
    /**
     * 对指定字符串进行加密,返回加密后的字符串
     * @param key 加密的key
     * @param strMing 待加密的字符串
     * @return
     */
    public static String encryptStr(String key,String strMing) {
        getKey(key);
        byte[] byteMi = null;
        byte[] byteMing = null;
        String strMi = "";
        BASE64Encoder base64en = new BASE64Encoder();
        try {
            byteMing = strMing.getBytes("UTF8");
            byteMi = encToByte(byteMing);
            strMi = base64en.encode(byteMi);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            base64en = null;
            byteMing = null;
            byteMi = null;
        }
        return strMi;
    }
    /**
     * 对指定字符串进行解密,返回解密后的字符串
     * @param key 加密的key
     * @param strDecry 待解密的字符串
     * @return
     */
    public static String decryptStr(String key,String strDecry) {
        getKey(key);
        BASE64Decoder base64De = new BASE64Decoder();
        byte[] byteMing = null;
        byte[] byteMi = null;
        String strMing = "";
        try {
            byteMi = base64De.decodeBuffer(strDecry);
            byteMing = byteToEnc(byteMi);
            strMing = new String(byteMing, "UTF8");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            base64De = null;
            byteMing = null;
            byteMi = null;
        }
        return strMing;
    }
    private static void getKey(String strKey) {
        if(strKey == null || "".equals(strKey)){
            strKey = "ytxsoft";
        }
        try {
            KeyGenerator _generator = KeyGenerator.getInstance("DES");
//			SecureRandom 实现完全随操作系统本身的內部状态，除非调用方在调用 getInstance 方法，然后调用 setSeed 方法；该实现在 windows 上每次生成的 key 都相同，但是在 solaris 或部分 linux 系统上则不同。
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            random.setSeed(strKey.getBytes());
            _generator.init(random);
            key = _generator.generateKey();
            _generator = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static byte[] encToByte(byte[] byteS) {
        byte[] byteFina = null;
        Cipher cipher;

        try {
            cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byteFina = cipher.doFinal(byteS);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cipher = null;
        }
        return byteFina;
    }

    private static byte[] byteToEnc(byte[] byteD) {
        Cipher cipher;
        byte[] byteFina = null;
        try {
            cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byteFina = cipher.doFinal(byteD);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cipher = null;
        }
        return byteFina;
    }
}

