package com.lixi.shoolcard.network.vo;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * 房间查询入参
 */
public class RoomRequest implements Serializable {
    //校区ID 必传
    @SerializedName("xiaoqu_id")
    private Integer schoolId;
    @SerializedName("loudong_id")
    private Integer buildingId;
    @SerializedName("louceng_id")
    private Integer floorId;
    @SerializedName("room_id")
    private Integer roomId;
    @SerializedName("filter_name")
    private String filterName;

    public Integer getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(Integer schoolId) {
        this.schoolId = schoolId;
    }

    public Integer getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(Integer buildingId) {
        this.buildingId = buildingId;
    }

    public Integer getFloorId() {
        return floorId;
    }

    public void setFloorId(Integer floorId) {
        this.floorId = floorId;
    }

    public Integer getRoomId() {
        return roomId;
    }

    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }

    public String getFilterName() {
        return filterName;
    }

    public void setFilterName(String filterName) {
        this.filterName = filterName;
    }
}
