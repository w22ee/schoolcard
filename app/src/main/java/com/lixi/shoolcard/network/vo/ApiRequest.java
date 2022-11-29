package com.lixi.shoolcard.network.vo;

import com.google.gson.annotations.SerializedName;
import com.lixi.shoolcard.constant.SchoolCardConstant;
import com.tencent.mmkv.MMKV;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Random;

/**
 * API请求对象
 * @param <T>
 */
public class ApiRequest <T> implements Serializable {
    @SerializedName("random")
    private String random;
    @SerializedName("timestamp")
    private Long timestamp;
    @SerializedName("device_code")
    private String deviceCode;
    @SerializedName("device_pass")
    private String devicePass;
    @SerializedName("request_data")
    private T requestData;

    public String getRandom() {
        return random;
    }

    public void setRandom(String random) {
        this.random = random;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getDeviceCode() {
        return deviceCode;
    }

    public void setDeviceCode(String deviceCode) {
        this.deviceCode = deviceCode;
    }

    public String getDevicePass() {
        return devicePass;
    }

    public void setDevicePass(String devicePass) {
        this.devicePass = devicePass;
    }

    public T getRequestData() {
        return requestData;
    }

    public void setRequestData(T requestData) {
        this.requestData = requestData;
    }

    public ApiRequest() {
        MMKV kv = MMKV.defaultMMKV();
        setDeviceCode(kv.decodeString(SchoolCardConstant.DEVICE_CODE));
        setDevicePass(kv.decodeString(SchoolCardConstant.DEVICE_PASS));
        setRandom(getRandomString(10));
        setTimestamp(System.currentTimeMillis());
    }

    /**
     * 生成随机串
     * @param length
     * @return
     */
    public static String getRandomString(int length) {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();

    }

}
