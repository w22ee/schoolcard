package com.lixi.shoolcard.network.vo;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 电控缴费入参
 */
public class RechargeRequest implements Serializable {
    @SerializedName("xiaoqu_id")
    private Integer schoolId;
    @SerializedName("room_id")
    private Integer roomId;
    @SerializedName("card_no")
    private String cardNo;
    @SerializedName("order_fee")
    private String amoount;
    @SerializedName("trans_time")
    private String transTime; //yyyy-MM-dd HH:mm:ss

    public Integer getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(Integer schoolId) {
        this.schoolId = schoolId;
    }

    public Integer getRoomId() {
        return roomId;
    }

    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getAmoount() {
        return amoount;
    }

    public void setAmoount(String amoount) {
        this.amoount = amoount;
    }

    public String getTransTime() {
        return transTime;
    }

    public void setTransTime(String transTime) {
        this.transTime = transTime;
    }
}
