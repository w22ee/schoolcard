package com.lixi.shoolcard.network.vo;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * 校园卡充值记录查询结果
 */
public class CardRechargeVo implements Serializable {
    @SerializedName("user_name")
    private String userName;
    @SerializedName("grade_name")
    private String gradeName;
    @SerializedName("class_name")
    private String className;
    //充值金额
    @SerializedName("fee")
    private String amount;
    @SerializedName("trans_time")
    private String transTime; //yyyy-MM-dd HH:mm:ss
    //余额
    @SerializedName("oddfare")
    private String oddfare;
    //钱包类型（0卡片，2在线）
    @SerializedName("wallet_type")
    private Integer walletType;
    //支付方式
    @SerializedName("accname")
    private String accname;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getGradeName() {
        return gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTransTime() {
        return transTime;
    }

    public void setTransTime(String transTime) {
        this.transTime = transTime;
    }

    public String getOddfare() {
        return oddfare;
    }

    public void setOddfare(String oddfare) {
        this.oddfare = oddfare;
    }

    public Integer getWalletType() {
        return walletType;
    }

    public void setWalletType(Integer walletType) {
        this.walletType = walletType;
    }

    public String getAccname() {
        return accname;
    }

    public void setAccname(String accname) {
        this.accname = accname;
    }
}
