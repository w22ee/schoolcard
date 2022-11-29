package com.lixi.shoolcard.utils;

public class StringUtils {

    /**
     * 将byte[]转换为16进制字符串
     *
     * @param
     * @return
     */
    public static String convertBytes2HexStr(byte[] originalBytes) {
        StringBuffer sb = new StringBuffer();
        for (byte bt : originalBytes) {
            // 获取b补码后的八位
            String hex = Integer.toHexString(((int)bt)&0xff);
            if (hex.length() == 1) {
                hex = "0" + hex;
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    /**
     * 将16进制字符串转换为byte[]
     *
     * @param str
     * @return
     */
    public static byte[] convertHexStr2Bytes(String str) {
        if(str == null || str.trim().equals("")) {
            return new byte[0];
        }

        byte[] bytes = new byte[str.length() / 2];
        for(int i = 0; i < str.length() / 2; i++) {
            String subStr = str.substring(i * 2, i * 2 + 2);
            bytes[i] = (byte) Integer.parseInt(subStr, 16);
        }

        return bytes;
    }

}
