package com.lixi.shoolcard.network.vo;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * 房间查询结果
 */
public class RoomVo implements Serializable {
    @SerializedName("room_id")
    private Integer roomId;
    @SerializedName("room")
    private String roomName;
    @SerializedName("louceng_id")
    private Integer floorId;
    @SerializedName("louceng")
    private String floorName;
    @SerializedName("loudong_id")
    private Integer buildingId;
    @SerializedName("loudong")
    private String buildingName;
    @SerializedName("xiaoqu_id")
    private Integer schoolId;
    @SerializedName("xiaoqu")
    private String schoolName;
    //房间当前用电金额
    @SerializedName("used_amp")
    private String usedAmp;
    //房间购电总金额（正或者零，-1表示出错）
    @SerializedName("all_amp")
    private String allAmp;
    //房间电费余额
    @SerializedName("remain_amp")
    private String remainAmp;

    public Integer getFloorId() {
        return floorId;
    }

    public void setFloorId(Integer floorId) {
        this.floorId = floorId;
    }

    public String getFloorName() {
        return floorName;
    }

    public void setFloorName(String floorName) {
        this.floorName = floorName;
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

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getUsedAmp() {
        return usedAmp;
    }

    public void setUsedAmp(String usedAmp) {
        this.usedAmp = usedAmp;
    }

    public String getAllAmp() {
        return allAmp;
    }

    public void setAllAmp(String allAmp) {
        this.allAmp = allAmp;
    }

    public String getRemainAmp() {
        return remainAmp;
    }

    public void setRemainAmp(String remainAmp) {
        this.remainAmp = remainAmp;
    }
}
