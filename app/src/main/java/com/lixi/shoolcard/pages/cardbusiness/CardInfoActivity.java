package com.lixi.shoolcard.pages.cardbusiness;

import android.content.Intent;
import android.os.Bundle;

import com.lixi.shoolcard.base.BaseActivity;
import com.lixi.shoolcard.databinding.ActivityCardInfoBinding;
import com.lixi.shoolcard.utils.DES3Util;
import com.lixi.shoolcard.utils.GsonUtil;
import com.lixi.shoolcard.network.MyCallbcak;
import com.lixi.shoolcard.network.NetWorkUitl;
import com.lixi.shoolcard.network.ShopApi;
import com.lixi.shoolcard.utils.ToastUtil;
import com.lixi.shoolcard.network.vo.ApiRequest;
import com.lixi.shoolcard.network.vo.ApiResult;
import com.lixi.shoolcard.network.vo.CardInfoRequest;
import com.lixi.shoolcard.network.vo.CardInfoVo;

import retrofit2.Call;

/**
 * 卡信息查询
 */
public class CardInfoActivity extends BaseActivity {

    private  ActivityCardInfoBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        cardNo = intent.getStringExtra("cardNo");
        binding = ActivityCardInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setTitle("卡信息查询");
        showLoginOut();

        ShopApi shopApi =  NetWorkUitl.getRetrofit().create(ShopApi.class);
        showLoading();
        Call<ApiResult<CardInfoVo>> call =  shopApi.getCardInfo(NetWorkUitl.initRequestBody(initRequest()));
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
                    binding.studentName.setText(cardInfo.getUserName());
                    binding.className.setText(cardInfo.getGradeName() + cardInfo.getClassName());
                    binding.stuNo.setText(cardInfo.getStuNo());
                    binding.totalRemain.setText(cardInfo.getTotalRemain());
                    binding.cardRemain.setText(cardInfo.getCardRemain());
                    binding.elecRemain.setText(cardInfo.getElecRemain());
                }
            }

            @Override
            protected void onFinish() {
                super.onFinish();
                hideLoading();
            }
        });
    }

    private ApiRequest<CardInfoRequest> initRequest(){
        ApiRequest<CardInfoRequest> request = new ApiRequest<>();
        CardInfoRequest cardInfoRequest = new CardInfoRequest();
        cardInfoRequest.setCardNo(cardNo);
        request.setRequestData(cardInfoRequest);
        System.out.println("请求参数：："+GsonUtil.getGson().toJson(request));
        return request;
    }

}