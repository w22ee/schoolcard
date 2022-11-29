package com.lixi.shoolcard.utils;

public class ConvertUtil {
    public static String convertWalletType(Integer walletType){
        if(walletType != null){
            if(walletType == 0){
                return "电子钱包";
            }
            if(walletType == 2){
                return "在线余额";
            }
        }
        return "";
    }

    //todo 转换文本
    public static String convertConsumeType(){
        return "";
    }
}
