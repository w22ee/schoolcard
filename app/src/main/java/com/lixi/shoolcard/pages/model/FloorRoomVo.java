package com.lixi.shoolcard.pages.model;

import com.lixi.shoolcard.network.vo.RoomVo;

import java.util.Date;
import java.util.List;

/**
 * 楼栋 缓存用
 */
public class FloorRoomVo {
    private Integer floorId;
    private String floorName;
    private Integer schoolId;
    private Integer buildingId;
    //private List<RoomVo> roomList;

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

//    public List<RoomVo> getRoomList() {
//        return roomList;
//    }
//
//    public void setRoomList(List<RoomVo> roomList) {
//        this.roomList = roomList;
//    }
}
