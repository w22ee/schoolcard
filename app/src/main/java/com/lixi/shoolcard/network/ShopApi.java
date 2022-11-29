package com.lixi.shoolcard.network;

import com.lixi.shoolcard.network.vo.ApiResult;
import com.lixi.shoolcard.network.vo.BuildingVo;
import com.lixi.shoolcard.network.vo.CardInfoVo;
import com.lixi.shoolcard.network.vo.CardPayVo;
import com.lixi.shoolcard.network.vo.CardRechargeVo;
import com.lixi.shoolcard.network.vo.DevicceVo;
import com.lixi.shoolcard.network.vo.FloorVo;
import com.lixi.shoolcard.network.vo.RechargeVo;
import com.lixi.shoolcard.network.vo.RoomVo;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ShopApi {
    @POST("dkPad/device/getDevice")
    Call<ApiResult<DevicceVo>> getDevice(@Body RequestBody route);

    @POST("dkPad/building/getBuildingList")
    Call<ApiResult<List<BuildingVo>>> getBuildingList(@Body RequestBody route);

    @POST("dkPad/building/getFloorList")
    Call<ApiResult<List<FloorVo>>> getFloorList(@Body RequestBody route);

    @POST("dkPad/building/getRoomList")
    Call<ApiResult<List<RoomVo>>> getRoomList(@Body RequestBody route);

    @POST("dkPad/order/diankongOrder")
    Call<ApiResult> recharge(@Body RequestBody route);

    @POST("dkPad/card/getCardInfo")
    Call<ApiResult<CardInfoVo>> getCardInfo(@Body RequestBody route);

    //房间购电记录
    @POST("dkPad/record/roomOrderList")
    Call<ApiResult<List<RechargeVo>>> getRechargeList(@Body RequestBody route);

    //用户购电记录
    @POST("dkPad/record/userOrderList")
    Call<ApiResult<List<RechargeVo>>> getRechargeBillList(@Body RequestBody route);

    //校园卡充值记录
    @POST("dkPad/record/getRechargeList")
    Call<ApiResult<List<CardRechargeVo>>> getCardRechardList(@Body RequestBody route);

    //用户消费记录
    @POST("dkPad/record/getConsumeList")
    Call<ApiResult<List<CardPayVo>>> getCardPayList(@Body RequestBody route);

    //修改密码
    @POST("dkPad/card/cardLose")
    Call<ApiResult> reportLoss(@Body RequestBody route);

    //修改密码
    @POST("dkPad/card/changePass")
    Call<ApiResult> updatePassword(@Body RequestBody route);
}
