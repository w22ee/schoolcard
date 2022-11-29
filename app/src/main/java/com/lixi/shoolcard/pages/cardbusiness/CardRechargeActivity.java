package com.lixi.shoolcard.pages.cardbusiness;

import android.content.Intent;
import android.os.Bundle;
import android.widget.AbsListView;

import com.google.gson.reflect.TypeToken;
import com.lixi.shoolcard.base.BaseActivity;
import com.lixi.shoolcard.constant.SchoolCardConstant;
import com.lixi.shoolcard.databinding.ActivityCardRechargeBinding;
import com.lixi.shoolcard.utils.DES3Util;
import com.lixi.shoolcard.utils.GsonUtil;
import com.lixi.shoolcard.network.MyCallbcak;
import com.lixi.shoolcard.network.NetWorkUitl;
import com.lixi.shoolcard.network.ShopApi;
import com.lixi.shoolcard.network.vo.ApiRequest;
import com.lixi.shoolcard.network.vo.ApiResult;
import com.lixi.shoolcard.network.vo.CardRechargeVo;
import com.lixi.shoolcard.network.vo.PageListRequest;
import com.lixi.shoolcard.pages.adapter.CardRechargeListAdapter;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

/**
 * 校园卡充值记录查询
 */
public class CardRechargeActivity extends BaseActivity {

    private ActivityCardRechargeBinding binding;
    private CardRechargeListAdapter cardRechargeListAdapter;
    private boolean isLoading = false;
    private boolean isMore = false;
    private boolean hasMore = true;
    private int page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        cardNo = intent.getStringExtra("cardNo");
        binding = ActivityCardRechargeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setTitle("校园卡充值记录查询");
        showLoginOut();

        cardRechargeListAdapter = new CardRechargeListAdapter(CardRechargeActivity.this);
        binding.cardRechargeList.setAdapter(cardRechargeListAdapter);
        loadData();
        binding.cardRechargeList.setOnScrollListener(new AbsListView.OnScrollListener() {
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
//        List<CardRechargeVo> list = new ArrayList<>();
//        for(int i = 0;i<20;i++){
//            CardRechargeVo rechardRecord = new CardRechargeVo();
//            rechardRecord.setUserName("小鱼"+page);
//            rechardRecord.setTransTime("2021"+i);
//            rechardRecord.setAmount("111"+i);
//            rechardRecord.setGradeName("高三");
//            rechardRecord.setClassName("二班");
//            rechardRecord.setAccname("终端存款");
//            rechardRecord.setOddfare("1000"+i);
//            list.add(rechardRecord);
//        }
//        cardRechargeListAdapter.setList(list,isMore);
//        page++;
//        isLoading = false;
//        hideLoading();
//        //测试数据end

        ShopApi shopApi =  NetWorkUitl.getRetrofit().create(ShopApi.class);
        Call<ApiResult<List<CardRechargeVo>>> call =  shopApi.getCardRechardList(NetWorkUitl.initRequestBody(initRequest()));
        call.enqueue(new MyCallbcak<ApiResult<List<CardRechargeVo>>>() {
            @Override
            protected void setData(ApiResult<List<CardRechargeVo>> result) {
                if (result.getData() == null) {
                    hasMore = false;
                    return;
                }

                String decryptStr = DES3Util.desDecrypt(result.getData());
                Type listType = new TypeToken<ArrayList<CardRechargeVo>>(){}.getType();
                List<CardRechargeVo> list = GsonUtil.getGson().fromJson(decryptStr,listType);
                cardRechargeListAdapter.setList(list,isMore);
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
        pageListRequest.setCardNo(cardNo);
        pageListRequest.setIndex(page);
        request.setRequestData(pageListRequest);
        System.out.println("请求参数：："+GsonUtil.getGson().toJson(request));
        return request;
    }
}