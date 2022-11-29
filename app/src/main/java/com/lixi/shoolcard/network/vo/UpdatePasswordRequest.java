package com.lixi.shoolcard.network.vo;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * 修改密码入参
 */
public class UpdatePasswordRequest implements Serializable {
    @SerializedName("card_no")
    private String cardNo;
    @SerializedName("id_no")
    private String idNo;
    @SerializedName("old_pass")
    private String oldPassword;
    @SerializedName("new_pass")
    private String newPassword;
    @SerializedName("student_number")
    private String stuNo;

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getStuNo() {
        return stuNo;
    }

    public void setStuNo(String stuNo) {
        this.stuNo = stuNo;
    }
}
