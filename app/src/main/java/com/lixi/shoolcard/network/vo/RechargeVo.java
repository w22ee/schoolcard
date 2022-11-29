package com.lixi.shoolcard.network.vo;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * 卡充值电费查询结果
 * 电控缴费查询结果
 */
public class RechargeVo implements Serializable {
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
    //电费是否到账，0到账，1未到账
    @SerializedName("status")
    private Boolean status;
    @SerializedName("room_id")
    private Integer roomId;
    @SerializedName("room")
    private String roomName;
    @SerializedName("loudong_id")
    private Integer buildingId;
    @SerializedName("loudong")
    private String buildingName;

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

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Integer getRoomId() {
        return roomId;
    }

    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public Integer getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(Integer buildingId) {
        this.buildingId = buildingId;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }
}
