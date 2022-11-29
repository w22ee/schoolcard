package com.lixi.shoolcard.pages.electric;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;

import com.google.gson.reflect.TypeToken;
import com.lixi.shoolcard.constant.SchoolCardConstant;
import com.lixi.shoolcard.databinding.ActivityRechardRecordBinding;
import com.lixi.shoolcard.pages.adapter.RechardFilterAdaper;
import com.lixi.shoolcard.pages.adapter.RechargeListAdapter;
import com.lixi.shoolcard.utils.DES3Util;
import com.lixi.shoolcard.utils.GsonUtil;
import com.lixi.shoolcard.utils.InputUtil;
import com.lixi.shoolcard.network.MyCallbcak;
import com.lixi.shoolcard.network.NetWorkUitl;
import com.lixi.shoolcard.network.ShopApi;
import com.lixi.shoolcard.network.vo.ApiRequest;
import com.lixi.shoolcard.network.vo.ApiResult;
import com.lixi.shoolcard.network.vo.PageListRequest;
import com.lixi.shoolcard.network.vo.RechargeVo;
import com.lixi.shoolcard.network.vo.RoomVo;
import com.lixi.shoolcard.pages.model.BuildingRoomVo;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

/**
 * 电控充值记录查询
 */
public class RechardRecordActivity extends BuildingRoomActivity {

    private ActivityRechardRecordBinding binding;
    private RechargeListAdapter rechargeListAdapter;
    private boolean isLoading = false;
    private boolean isMore = false;
    private boolean hasMore = true;
    private int page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        binding = ActivityRechardRecordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setTitle("充值记录查询");

        adapter = new RechardFilterAdaper(new ArrayList<>(),RechardRecordActivity.this);
        binding.inputRoom.setAdapter(adapter);
        rechargeListAdapter = new RechargeListAdapter(RechardRecordActivity.this);
        binding.rechargeList.setAdapter(rechargeListAdapter);
        initListener();
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
                rechargeListAdapter.setList(new ArrayList<>(),false);
                getFloors(i);
                fOptions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        System.out.println("option floor: " + i);
                        binding.selectFloor.setText(floors.get(i));
                        resetRoomContent();
                        dismissOption();
                        rechargeListAdapter.setList(new ArrayList<>(),false);
                        getRooms(buildingRoomVo,i);

                        binding.inputRoom.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                System.out.println("onItemClick " + i);
                                RoomVo roomVo =  (RoomVo)view.getTag();
                                selectedRoom = roomVo;
                                binding.inputRoom.setText(roomVo.getRoomName());
                                page=1;
                                hasMore=true;
                                isMore=false;
                                isLoading=false;
                                InputUtil.closeInputMethod(RechardRecordActivity.this,binding.inputRoom);
                                loadData();
                            }
                        });
                        fOptions.dismiss();
                    }


                });
                mOptions.dismiss();
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
        binding.rechargeList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    if (absListView.getLastVisiblePosition() == absListView.getCount() - 1) {
                        loadData();
                    }
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {
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
        adapter.setKeys(list);
    }

    private void loadData(){
        if (isLoading) {
            return;
        }
        if(!hasMore){
            return;
        }
        showLoading();
        isLoading = true;
        if(page>1){
            isMore = true;
        }

//        //测试数据begin
//        List<RechargeVo> list = new ArrayList<>();
//        for(int i = 0;i<20;i++){
//            RechargeVo rechardBill = new RechargeVo();
//            rechardBill.setBuildingName(selectedRoom.getBuildingName());
//            rechardBill.setRoomName(selectedRoom.getRoomName());
//            rechardBill.setUserName("小鱼"+page);
//            rechardBill.setTransTime("2021"+i);
//            rechardBill.setAmount("111"+i);
//            rechardBill.setGradeName("高三");
//            rechardBill.setClassName("二班");
//            rechardBill.setStatus(1);
//            list.add(rechardBill);
//        }
//        rechargeListAdapter.setList(list,isMore);
//        page++;
//        isLoading = false;
//        hideLoading();
//        //测试数据end

        ShopApi shopApi =  NetWorkUitl.getRetrofit().create(ShopApi.class);
        showLoading();
        Call<ApiResult<List<RechargeVo>>> call =  shopApi.getRechargeList(NetWorkUitl.initRequestBody(initRequest()));
        call.enqueue(new MyCallbcak<ApiResult<List<RechargeVo>>>() {
            @Override
            protected void setData(ApiResult<List<RechargeVo>> result) {
                if (result.getData() == null) {
                    hasMore = false;
                    return;
                }

                String decryptStr = DES3Util.desDecrypt(result.getData());
                Type listType = new TypeToken<ArrayList<RechargeVo>>(){}.getType();
                List<RechargeVo> list = GsonUtil.getGson().fromJson(decryptStr,listType);
                rechargeListAdapter.setList(list,isMore);
                if(list.size() < SchoolCardConstant.PAGE_SIZE){
                    hasMore = false;
                }
                page++;
            }

            @Override
            protected void onFinish() {
                super.onFinish();
                isLoading = false;
                hideLoading();
            }
        });
    }

    private ApiRequest<PageListRequest> initRequest(){
        ApiRequest<PageListRequest> request = new ApiRequest<>();
        PageListRequest pageListRequest = new PageListRequest();
        pageListRequest.setRoomId(selectedRoom.getRoomId());
        pageListRequest.setIndex(page);
        request.setRequestData(pageListRequest);
        System.out.println("请求参数：："+GsonUtil.getGson().toJson(request));
        return request;
    }

}