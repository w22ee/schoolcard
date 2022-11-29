package com.lixi.shoolcard.network.vo;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * 校园卡消费记录查询结果
 */
public class CardPayVo implements Serializable {
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
    @SerializedName("remain")
    private String remain;
    //钱包类型（0卡片，2在线）
    @SerializedName("wallet_type")
    private Integer walletType;
    //消费终端
    @SerializedName("consume_mch_name")
    private String onsumeMchName;

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

    public String getRemain() {
        return remain;
    }

    public void setRemain(String remain) {
        this.remain = remain;
    }

    public Integer getWalletType() {
        return walletType;
    }

    public void setWalletType(Integer walletType) {
        this.walletType = walletType;
    }

    public String getOnsumeMchName() {
        return onsumeMchName;
    }

    public void setOnsumeMchName(String onsumeMchName) {
        this.onsumeMchName = onsumeMchName;
    }

}
