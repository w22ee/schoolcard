package com.lixi.shoolcard.pages.password;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;

import com.lixi.shoolcard.base.BaseActivity;
import com.lixi.shoolcard.base.ReadEvent;
import com.lixi.shoolcard.databinding.ActivityPasswordBinding;
import com.lixi.shoolcard.utils.DES3Util;
import com.lixi.shoolcard.utils.GsonUtil;
import com.lixi.shoolcard.utils.Md5Util;
import com.lixi.shoolcard.network.MyCallbcak;
import com.lixi.shoolcard.network.NetWorkUitl;
import com.lixi.shoolcard.network.ShopApi;
import com.lixi.shoolcard.utils.ToastUtil;
import com.lixi.shoolcard.network.vo.ApiRequest;
import com.lixi.shoolcard.network.vo.ApiResult;
import com.lixi.shoolcard.network.vo.CardInfoRequest;
import com.lixi.shoolcard.network.vo.CardInfoVo;
import com.lixi.shoolcard.network.vo.UpdatePasswordRequest;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import retrofit2.Call;

/**
 * 修改密码
 */
public class PasswordActivity extends BaseActivity {

    private ActivityPasswordBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setTitle("修改密码");

        binding.updatepassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!checkInput()){
                    return;
                }
                showLoading();
                ShopApi shopApi =  NetWorkUitl.getRetrofit().create(ShopApi.class);
                Call<ApiResult> call =  shopApi.updatePassword(NetWorkUitl.initRequestBody(initRequest()));
                call.enqueue(new MyCallbcak<ApiResult>() {
                    @Override
                    protected void setData(ApiResult result) {
                        ToastUtil.showToast("修改成功");
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
        getStuNo(DES3Util.desEncrypt(event.message));
    }

    private void getStuNo(String cardNo){
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
                    binding.rbStudentNo.setChecked(true);
                    binding.passport.setText(cardInfo.getStuNo());
                }
            }

            @Override
            protected void onFinish() {
                super.onFinish();
                hideLoading();
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

    private ApiRequest<UpdatePasswordRequest> initRequest(){
        ApiRequest<UpdatePasswordRequest> request = new ApiRequest<>();
        UpdatePasswordRequest updatePasswordRequest = new UpdatePasswordRequest();
        if(binding.rbIdentity.isChecked()) {
            updatePasswordRequest.setIdNo(binding.passport.getText().toString().trim());
        }else{
            updatePasswordRequest.setStuNo(binding.passport.getText().toString().trim());
        }
        updatePasswordRequest.setOldPassword(DES3Util.desEncrypt(binding.oldpassword.getText().toString()));
        updatePasswordRequest.setNewPassword(DES3Util.desEncrypt(binding.newpassword.getText().toString()));
        request.setRequestData(updatePasswordRequest);
        System.out.println("请求参数：："+ GsonUtil.getGson().toJson(request));
        return request;
    }

    private boolean checkInput(){
        if(binding.passport.getText() == null || "".equals(binding.passport.getText().toString().trim())){
            ToastUtil.showToast("请输入身份证号/学号");
            return false;
        }
        if(binding.oldpassword.getText() == null || "".equals(binding.oldpassword.getText().toString())){
            ToastUtil.showToast("请输入旧密码");
            return false;
        }
        if(binding.newpassword.getText() == null || "".equals(binding.newpassword.getText().toString())){
            ToastUtil.showToast("请输入新密码");
            return false;
        }
        if(binding.confirmpassword.getText() == null || "".equals(binding.confirmpassword.getText().toString())){
            ToastUtil.showToast("请再次输入新密码");
            return false;
        }
        if(!binding.newpassword.getText().toString().equals(binding.confirmpassword.getText().toString())){
            ToastUtil.showToast("两次输入的新密码必须相同");
            return false;
        }
        return true;
    }
}