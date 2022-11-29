package com.lixi.shoolcard.network.vo;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * API返回对象
 * @param <T>
 */
public class ApiResult<T> implements Serializable {
    @SerializedName("random")
    private String random;
    @SerializedName("err_code")
    private Integer errCode;
    @SerializedName("err_msg")
    private String erroeMsg;
    @SerializedName("data")
    private String data;

    private T dataObj;

    public Integer getErrCode() {
        return errCode;
    }

    public void setErrCode(Integer errCode) {
        this.errCode = errCode;
    }

    public String getErroeMsg() {
        return erroeMsg;
    }

    public void setErroeMsg(String erroeMsg) {
        this.erroeMsg = erroeMsg;
    }

    public String getRandom() {
        return random;
    }

    public void setRandom(String random) {
        this.random = random;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public T getDataObj() {
        return dataObj;
    }

    public void setDataObj(T dataObj) {
        this.dataObj = dataObj;
    }
}
