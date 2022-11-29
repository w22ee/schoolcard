package com.lixi.shoolcard.pages.cardbusiness;

import android.content.Intent;
import android.os.Bundle;
import android.widget.AbsListView;

import com.google.gson.reflect.TypeToken;
import com.lixi.shoolcard.base.BaseActivity;
import com.lixi.shoolcard.constant.SchoolCardConstant;
import com.lixi.shoolcard.databinding.ActivityPaymentRecordBinding;
import com.lixi.shoolcard.utils.DES3Util;
import com.lixi.shoolcard.utils.GsonUtil;
import com.lixi.shoolcard.network.MyCallbcak;
import com.lixi.shoolcard.network.NetWorkUitl;
import com.lixi.shoolcard.network.ShopApi;
import com.lixi.shoolcard.network.vo.ApiRequest;
import com.lixi.shoolcard.network.vo.ApiResult;
import com.lixi.shoolcard.network.vo.CardPayVo;
import com.lixi.shoolcard.network.vo.PageListRequest;
import com.lixi.shoolcard.pages.adapter.PayListAdapter;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

/**
 * 消费记录查询
 */
public class PaymentRecordActivity extends BaseActivity {

    private ActivityPaymentRecordBinding binding;
    private PayListAdapter payListAdapter;
    private boolean isLoading = false;
    private boolean isMore = false;
    private boolean hasMore = true;
    private int page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        cardNo = intent.getStringExtra("cardNo");
        binding = ActivityPaymentRecordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setTitle("消费记录查询");
        showLoginOut();

        payListAdapter = new PayListAdapter(PaymentRecordActivity.this);
        binding.payList.setAdapter(payListAdapter);
        loadData();
        binding.payList.setOnScrollListener(new AbsListView.OnScrollListener() {
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

    private void loadData() {
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
//        List<CardPayVo> list = new ArrayList<>();
//        for(int i = 0;i<20;i++){
//            CardPayVo payMentRecord = new CardPayVo();
//            payMentRecord.setUserName("小鱼"+page);
//            payMentRecord.setGradeName("高三");
//            payMentRecord.setClassName("二班");
//            payMentRecord.setTransTime("2021"+i);
//            payMentRecord.setAmount("111"+i);
//            payMentRecord.setRemain("10000"+i);
//            payMentRecord.setWalletType(1);
//            payMentRecord.setOnsumeMchName("食堂");
//            list.add(payMentRecord);
//        }
//        payListAdapter.setList(list,isMore);
//        page++;
//        isLoading = false;
//        hideLoading();
//        //测试数据end

        ShopApi shopApi = NetWorkUitl.getRetrofit().create(ShopApi.class);
        Call<ApiResult<List<CardPayVo>>> call = shopApi.getCardPayList(NetWorkUitl.initRequestBody(initRequest()));
        call.enqueue(new MyCallbcak<ApiResult<List<CardPayVo>>>() {
            @Override
            protected void setData(ApiResult<List<CardPayVo>> result) {
                if (result.getData() == null) {
                    hasMore = false;
                    return;
                }

                String decryptStr = DES3Util.desDecrypt(result.getData());
                Type listType = new TypeToken<ArrayList<CardPayVo>>(){}.getType();
                List<CardPayVo> list = GsonUtil.getGson().fromJson(decryptStr,listType);
                payListAdapter.setList(list,isMore);
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

    private ApiRequest<PageListRequest> initRequest() {
        ApiRequest<PageListRequest> request = new ApiRequest<>();
        PageListRequest pageListRequest = new PageListRequest();
        pageListRequest.setCardNo(cardNo);
        pageListRequest.setIndex(page);
        request.setRequestData(pageListRequest);
        System.out.println("请求参数：："+GsonUtil.getGson().toJson(request));
        return request;
    }
}