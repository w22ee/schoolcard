package com.lixi.shoolcard.pages.electric;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.lixi.shoolcard.R;
import com.lixi.shoolcard.base.ReadEvent;
import com.lixi.shoolcard.constant.SchoolCardConstant;
import com.lixi.shoolcard.databinding.ActivityRechargeBinding;
import com.lixi.shoolcard.pages.adapter.RechardFilterAdaper;
import com.lixi.shoolcard.utils.DES3Util;
import com.lixi.shoolcard.utils.GsonUtil;
import com.lixi.shoolcard.utils.InputUtil;
import com.lixi.shoolcard.network.MyCallbcak;
import com.lixi.shoolcard.network.NetWorkUitl;
import com.lixi.shoolcard.network.ShopApi;
import com.lixi.shoolcard.utils.TimeUtil;
import com.lixi.shoolcard.utils.ToastUtil;
import com.lixi.shoolcard.network.vo.ApiRequest;
import com.lixi.shoolcard.network.vo.ApiResult;
import com.lixi.shoolcard.network.vo.CardInfoRequest;
import com.lixi.shoolcard.network.vo.CardInfoVo;
import com.lixi.shoolcard.network.vo.RechargeRequest;
import com.lixi.shoolcard.network.vo.RoomRequest;
import com.lixi.shoolcard.network.vo.RoomVo;
import com.lixi.shoolcard.pages.model.BuildingRoomVo;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

/**
 * 电控缴费
 */
public class RechargeActivity extends BuildingRoomActivity {
    private ActivityRechargeBinding binding;
    private boolean isWaitingCard = false;
    private AlertDialog alertDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        binding = ActivityRechargeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setTitle("电控缴费");

        adapter = new RechardFilterAdaper(new ArrayList<>(),RechargeActivity.this);
        binding.inputRoom.setAdapter(adapter);
        initListener();
        initDialog();
    }

    protected void initListener() {
        mOptions.setAnchorView(binding.selectBu);
        fOptions.setAnchorView(binding.selectFloor);
        mOptions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                System.out.println("option bu: " + i);
                BuildingRoomVo buildingRoomVo = cacheData.get(i);
                binding.selectBu.setText(buildingRoomVo.getBuildingName());
                resetFloorContent();
                resetRoomContent();
                dismissOption();
                setMoneyData();
                getFloors(i);
                fOptions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        System.out.println("option floor: " + i);
                        binding.selectFloor.setText(floors.get(i));
                        binding.inputRoom.setText("");
                        resetRoomContent();
                        dismissOption();
                        setMoneyData();
                        getRooms(buildingRoomVo,i);
                        binding.inputRoom.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                System.out.println("onItemClick " + i);
                                RoomVo roomVo =  (RoomVo)view.getTag();
                                selectedRoom = roomVo;
                                binding.inputRoom.setText(roomVo.getRoomName());
                                InputUtil.closeInputMethod(RechargeActivity.this,binding.inputRoom);
                                setMoneyData();
                            }
                        });
                    }
                });
            }
        });

        binding.selectBu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOptions.show();
            }
        });
        binding.selectFloor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fOptions.show();
            }
        });
        binding.recharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recharge(null);
            }
        });
    }

    private void initDialog(){
        LayoutInflater inflater = LayoutInflater.from(RechargeActivity.this);
        View dialogView = inflater.inflate(R.layout.btn_layout, null);
        TextView cancel = dialogView.findViewById(R.id.cancel_btn);
        TextView content = dialogView.findViewById(R.id.content);
        alertDialog = new AlertDialog.Builder(RechargeActivity.this)
                .setTitle("")
                .setView(dialogView)
                .setCancelable(false)
                .create();
        content.setText("缴费后房间电费余额如未更新，请稍后再进行查询");
        WindowManager.LayoutParams params = alertDialog.getWindow().getAttributes();
        params.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE;
        alertDialog.getWindow().setAttributes(params);
        //setViewFontSize(alertDialog.getWindow().getDecorView());
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
    }

    private void resetFloorContent(){
        binding.selectFloor.setText("");
        fAdapter.setList(new ArrayList<>());
    }

    private void resetRoomContent(){
        binding.inputRoom.setText("");
        selectedRoom = null;
        adapter.setKeys(new ArrayList<>());
    }

    @Override
    protected void setFloorDtaToView(List<String> list) {
        fAdapter.setList(floors);
    }

    @Override
    protected void setRoomDataToView(List<RoomVo> list) {
        //设置数组适配器
        adapter.setKeys(list);
    }

    private void recharge(String rechargeCardNo) {
        if(selectedRoom == null){
            ToastUtil.showToast("请选择要缴费的房间");
            return;
        }

        String amount = binding.inputMonney.getText().toString();
        System.out.println("input amount "+amount);
        if("".equals(amount)){
            ToastUtil.showToast("请输入缴费金额");
            return;
        }

        //没有传卡号时弹窗刷卡
        if(rechargeCardNo == null){
            isWaitingCard = true;
            alertDialog.show();
            return;
        }

        String cardAmount = binding.elecRemain.getText().toString();
        if(new BigDecimal(amount).compareTo(new BigDecimal(cardAmount)) > 0){
            ToastUtil.showToast("缴费金额不能大于在线金额");
            return;
        }

        ShopApi shopApi =  NetWorkUitl.getRetrofit().create(ShopApi.class);
        showLoading();
        Call<ApiResult> call =  shopApi.recharge(NetWorkUitl.initRequestBody(initRechargeRequest(rechargeCardNo)));
        call.enqueue(new MyCallbcak<ApiResult>() {
            @Override
            protected void setData(ApiResult result) {
                ToastUtil.showToast("缴费成功 "+amount+"元");
                getCardAmount(rechargeCardNo);
                setMoneyData();
                //5秒后关闭页面
                handler.postDelayed(runnable, 1000 * 5);
            }

            @Override
            protected void onFinish() {
                super.onFinish();
                hideLoading();
            }
        });
    }

    private void setMoneyData() {
        if(selectedRoom == null){
            binding.leftElc.setText("");
            return;
        }

        //测试数据begin
//        binding.leftElc.setText(selectedRoom.getUsedAmp());
//        binding.leftMoney.setText(selectedRoom.getAllAmp());
        //测试数据end

        //重新拉取一次数据，电量是实时的
        ApiRequest<RoomRequest> roomRequest = initRoomRequest(selectedRoom.getBuildingId(),selectedRoom.getFloorId());
        ShopApi shopApi =  NetWorkUitl.getRetrofit().create(ShopApi.class);
        showLoading();
        Call<ApiResult<List<RoomVo>>> call =  shopApi.getRoomList(NetWorkUitl.initRequestBody(roomRequest));
        call.enqueue(new MyCallbcak<ApiResult<List<RoomVo>>>() {
            @Override
            protected void setData(ApiResult<List<RoomVo>> result) {
                if(result.getData() != null) {
                    String decryptStr = DES3Util.desDecrypt(result.getData());
                    Type listType = new TypeToken<ArrayList<RoomVo>>(){}.getType();
                    List<RoomVo> rooms = GsonUtil.getGson().fromJson(decryptStr,listType);
                    if(rooms != null && rooms.size() > 0) {
                        RoomVo roomVo = rooms.get(0);
                        binding.leftElc.setText(roomVo.getRemainAmp());
                    }
                }
            }

            @Override
            protected void onFinish() {
                super.onFinish();
                hideLoading();
            }
        });
    }

    private ApiRequest<RechargeRequest> initRechargeRequest(String cardNo){
        ApiRequest<RechargeRequest> request = new ApiRequest<>();
        RechargeRequest rechargeRequest = new RechargeRequest();
        rechargeRequest.setSchoolId(kv.decodeInt(SchoolCardConstant.SCHOOL_ID,-1));
        rechargeRequest.setRoomId(selectedRoom.getRoomId());
        rechargeRequest.setAmoount(binding.inputMonney.getText().toString());
        rechargeRequest.setCardNo(cardNo);
        rechargeRequest.setTransTime(TimeUtil.formatDate());
        request.setRequestData(rechargeRequest);
        System.out.println("请求参数：："+GsonUtil.getGson().toJson(request));
        return request;
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReadEvent(ReadEvent event) {
        binding.putCardll.setVisibility(View.VISIBLE);
        getCardAmount(DES3Util.desEncrypt(event.message));
    }

    //刷卡后更新页面余额
    private void getCardAmount(String cardNo){
        //测试数据
        //binding.putCard.setText("100.00");
        ShopApi shopApi =  NetWorkUitl.getRetrofit().create(ShopApi.class);
        showLoading();
        Call<ApiResult<CardInfoVo>> call =  shopApi.getCardInfo(NetWorkUitl.initRequestBody(initCardRequest(cardNo)));
        call.enqueue(new MyCallbcak<ApiResult<CardInfoVo>>() {
            @Override
            protected void setData(ApiResult<CardInfoVo> result) {
                if (result.getData() == null) {
                    ToastUtil.showToast("信息查询失败");
                    return;
                }
                String decryptStr = DES3Util.desDecrypt(result.getData());
                CardInfoVo cardInfo = GsonUtil.getGson().fromJson(decryptStr,CardInfoVo.class);
                if(cardInfo != null) {
                    binding.cardRemain.setText(cardInfo.getCardRemain());
                    binding.elecRemain.setText(cardInfo.getElecRemain());
                    //如果是缴费时刷卡，则继续缴费流程
                    if(isWaitingCard){
                        isWaitingCard = false;
                        recharge(cardNo);
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResult<CardInfoVo>> call, Throwable t) {
                super.onFailure(call, t);
                //如果插叙失败了，下一次刷卡也不走缴费流程
                isWaitingCard = false;
            }

            @Override
            protected void onFinish() {
                super.onFinish();
                hideLoading();
                if(alertDialog != null) {
                    alertDialog.dismiss();
                }
            }
        });
    }

    private ApiRequest<CardInfoRequest> initCardRequest(String cardNo){
        ApiRequest<CardInfoRequest> request = new ApiRequest<>();
        CardInfoRequest cardInfoRequest = new CardInfoRequest();
        cardInfoRequest.setCardNo(cardNo);
        request.setRequestData(cardInfoRequest);
        System.out.println("请求参数：："+GsonUtil.getGson().toJson(request));
        return request;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(alertDialog != null) {
            alertDialog.dismiss();
        }
    }
}