package com.lixi.shoolcard.network.vo;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * 楼栋查询入参
 */
public class FloorRequest implements Serializable {
    //校区ID 必传
    @SerializedName("xiaoqu_id")
    private Integer schoolId;
    @SerializedName("loudong_id")
    private Integer buildingId;

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
}
