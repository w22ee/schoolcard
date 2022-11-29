package com.lixi.shoolcard.pages.model;

import java.util.Date;
import java.util.List;

/**
 * 楼栋 缓存用
 */
public class BuildingRoomVo {
    private Integer buildingId;
    private String buildingName;
    private Integer schoolId;
    private List<FloorRoomVo> floorRoomList;
    private Date cacheDate;

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

    public List<FloorRoomVo> getFloorRoomList() {
        return floorRoomList;
    }

    public void setFloorRoomList(List<FloorRoomVo> floorRoomList) {
        this.floorRoomList = floorRoomList;
    }

    public Date getCacheDate() {
        return cacheDate;
    }

    public void setCacheDate(Date cacheDate) {
        this.cacheDate = cacheDate;
    }
}
