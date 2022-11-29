package com.lixi.shoolcard.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5Util {
    public static MessageDigest md5;

    static {
        try {
            md5 = MessageDigest.getInstance("md5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public static String md5Encrypt(String str){
        byte[] src = str.getBytes();
        byte[] encrypted = md5.digest(src);
        String result = StringUtils.convertBytes2HexStr(encrypted);
        System.out.println("【MD5】待加密字符串::"+str+",加密后：："+result);
        return result;
    }

}
