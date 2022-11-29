package com.lixi.shoolcard.network.vo;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * 卡信息查询结果
 */
public class CardInfoVo implements Serializable {
    @SerializedName("user_name")
    private String userName;
    @SerializedName("grade_name")
    private String gradeName;
    @SerializedName("class_name")
    private String className;
    @SerializedName("total_remain")
    private String totalRemain;
    @SerializedName("card_remain")
    private String cardRemain;
    @SerializedName("elec_remain")
    private String elecRemain;
    @SerializedName("student_number")
    private String stuNo;

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

    public String getTotalRemain() {
        return totalRemain;
    }

    public void setTotalRemain(String totalRemain) {
        this.totalRemain = totalRemain;
    }

    public String getCardRemain() {
        return cardRemain;
    }

    public void setCardRemain(String cardRemain) {
        this.cardRemain = cardRemain;
    }

    public String getElecRemain() {
        return elecRemain;
    }

    public void setElecRemain(String elecRemain) {
        this.elecRemain = elecRemain;
    }

    public String getStuNo() {
        return stuNo;
    }

    public void setStuNo(String stuNo) {
        this.stuNo = stuNo;
    }
}
