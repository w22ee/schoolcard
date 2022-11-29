package com.lixi.shoolcard.utils;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class DES3Util {
    public static final String PASSWORD_CRYPT_KEY = "QXbpruew1&HD2Zi*";
    private static final String KEY_ALGORITHM = "DESede";//定义 加密算法
    private static final String ENCODE = "UTF-8";
    private static SecretKey securekey;
    private static Cipher cipher;

    static {
        try {
            securekey = new SecretKeySpec(getByte24(PASSWORD_CRYPT_KEY),KEY_ALGORITHM);
            cipher = Cipher.getInstance(KEY_ALGORITHM);
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public static String desEncrypt(String str) {
        //初始化加密方法
        try {
            cipher.init(Cipher.ENCRYPT_MODE, securekey);
            byte[] src = str.getBytes(ENCODE);
            // 加密，结果保存进cipherByte
            byte[] encrypted = cipher.doFinal(src);
            String result = StringUtils.convertBytes2HexStr(encrypted);
            System.out.println("【3DES】待加密字符串::"+str);
            System.out.println("【3DES】加密后：："+result);
            return result;
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String desDecrypt(String str) {
        //初始化解密方法
        try {
            cipher.init(Cipher.DECRYPT_MODE, securekey);
            byte[] src = StringUtils.convertHexStr2Bytes(str);
            // 解密，结果保存进cipherByte
            String result = new String(cipher.doFinal(src),ENCODE);
            System.out.println("【3DES】待解密字符串：："+str);
            System.out.println("【3DES】解密后：："+result);
            return result;
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    //密码转成24位byte数组
    private static byte[] getByte24(String str) throws UnsupportedEncodingException {
        byte[] b = str.getBytes(ENCODE);
        return Arrays.copyOf(b,24);
    }
}
