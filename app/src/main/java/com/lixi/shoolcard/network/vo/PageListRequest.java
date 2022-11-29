package com.lixi.shoolcard.network.vo;

import com.google.gson.annotations.SerializedName;
import com.lixi.shoolcard.constant.SchoolCardConstant;
import com.lixi.shoolcard.utils.TimeUtil;

import java.io.Serializable;

/**
 * 卡业务查询列表入参（包括电费缴费记录，校园卡充值记录，校园卡消费记录）
 * 电控缴费记录查询入参
 */
public class PageListRequest implements Serializable {
    @SerializedName("card_no")
    private String cardNo;
    @SerializedName("room_id")
    private Integer roomId;
    @SerializedName("page")
    private Integer index = 1;
    @SerializedName("size")
    private Integer pageSize = SchoolCardConstant.PAGE_SIZE;
    @SerializedName("start_date")
    private String startDate;
    @SerializedName("end_date")
    private String endDate;
    @SerializedName("sort")
    private Integer sort = 0;

    public PageListRequest(){
        setStartDate(TimeUtil.formatDate(-30));
        setEndDate(TimeUtil.formatDate());
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public Integer getRoomId() {
        return roomId;
    }

    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }
}
