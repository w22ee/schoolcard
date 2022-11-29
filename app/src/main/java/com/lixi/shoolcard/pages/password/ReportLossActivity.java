package com.lixi.shoolcard.pages.password;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;

import com.lixi.shoolcard.base.BaseActivity;
import com.lixi.shoolcard.databinding.ActivityReportLossBinding;
import com.lixi.shoolcard.utils.DES3Util;
import com.lixi.shoolcard.utils.GsonUtil;
import com.lixi.shoolcard.network.MyCallbcak;
import com.lixi.shoolcard.network.NetWorkUitl;
import com.lixi.shoolcard.network.ShopApi;
import com.lixi.shoolcard.utils.ToastUtil;
import com.lixi.shoolcard.network.vo.ApiRequest;
import com.lixi.shoolcard.network.vo.ApiResult;
import com.lixi.shoolcard.network.vo.ReportLossRequest;

import retrofit2.Call;

/**
 * 挂失
 */
public class ReportLossActivity extends BaseActivity {

    private ActivityReportLossBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityReportLossBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setTitle("挂失校园卡");

        binding.reportloss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!checkInput()){
                    return;
                }
                showLoading();
                ShopApi shopApi =  NetWorkUitl.getRetrofit().create(ShopApi.class);
                Call<ApiResult> call =  shopApi.reportLoss(NetWorkUitl.initRequestBody(initRequest()));
                call.enqueue(new MyCallbcak<ApiResult>() {
                    @Override
                    protected void setData(ApiResult result) {
                        ToastUtil.showToast("挂失成功");
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
        });
        binding.rgPassType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                binding.passport.setText("");
                if(binding.rbIdentity.isChecked()) {
                    binding.passText.setText("身份证号");
                    binding.passport.setHint("请输入身份证号");
                }else{
                    binding.passText.setText("学号");
                    binding.passport.setHint("请输入学号");
                }
            }
        });
    }

    private ApiRequest<ReportLossRequest> initRequest(){
        ApiRequest<ReportLossRequest> request = new ApiRequest<>();
        ReportLossRequest reportLossRequest = new ReportLossRequest();
        if(binding.rbIdentity.isChecked()) {
            reportLossRequest.setIdNo(binding.passport.getText().toString().trim());
        }else{
            reportLossRequest.setStuNo(binding.passport.getText().toString().trim());
        }
        reportLossRequest.setPassword(DES3Util.desEncrypt(binding.password.getText().toString()));
        request.setRequestData(reportLossRequest);
        System.out.println("请求参数：："+GsonUtil.getGson().toJson(request));
        return request;
    }

    private boolean checkInput(){
        if(binding.passport.getText() == null || "".equals(binding.passport.getText().toString().trim())){
            ToastUtil.showToast("请输入身份证号/学号");
            return false;
        }
        if(binding.password.getText() == null || "".equals(binding.password.getText().toString())){
            ToastUtil.showToast("请输入密码");
            return false;
        }
        return true;
    }
}