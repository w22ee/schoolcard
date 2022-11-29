package com.lixi.shoolcard.network.vo;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * 设备查询结果
 */
public class DevicceVo implements Serializable {
    @SerializedName("device_code")
    private String deviceCode;
    @SerializedName("school_info_id")
    private Integer schoolId;
    @SerializedName("device_name")
    private String deviceName;
    @SerializedName("ip")
    private String ipAddress;
    @SerializedName("location")
    private String location;
    @SerializedName("auto_start_time")
    private String autoStartTime;
    @SerializedName("auto_close_time")
    private String autoCloseTime;
    @SerializedName("device_pass")
    private String devicePass;

    public String getDeviceCode() {
        return deviceCode;
    }

    public void setDeviceCode(String deviceCode) {
        this.deviceCode = deviceCode;
    }

    public Integer getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(Integer schoolId) {
        this.schoolId = schoolId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAutoStartTime() {
        return autoStartTime;
    }

    public void setAutoStartTime(String autoStartTime) {
        this.autoStartTime = autoStartTime;
    }

    public String getAutoCloseTime() {
        return autoCloseTime;
    }

    public void setAutoCloseTime(String autoCloseTime) {
        this.autoCloseTime = autoCloseTime;
    }

    public String getDevicePass() {
        return devicePass;
    }

    public void setDevicePass(String devicePass) {
        this.devicePass = devicePass;
    }
}
