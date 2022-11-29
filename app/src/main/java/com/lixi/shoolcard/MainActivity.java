package com.lixi.shoolcard;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.lixi.shoolcard.base.BaseActivity;
import com.lixi.shoolcard.constant.SchoolCardConstant;
import com.lixi.shoolcard.databinding.ActivityMainBinding;
import com.lixi.shoolcard.utils.DES3Util;
import com.lixi.shoolcard.utils.GsonUtil;
import com.lixi.shoolcard.utils.Md5Util;
import com.lixi.shoolcard.network.MyCallbcak;
import com.lixi.shoolcard.network.NetWorkUitl;
import com.lixi.shoolcard.network.ShopApi;
import com.lixi.shoolcard.utils.ToastUtil;
import com.lixi.shoolcard.network.vo.ApiRequest;
import com.lixi.shoolcard.network.vo.ApiResult;
import com.lixi.shoolcard.network.vo.DevicceVo;
import com.lixi.shoolcard.pages.AdminActivity;
import com.lixi.shoolcard.pages.cardbusiness.TransactionActivity;
import com.lixi.shoolcard.pages.electric.RechardRecordActivity;
import com.lixi.shoolcard.pages.electric.RechargeActivity;
import com.lixi.shoolcard.pages.password.PasswordActivity;
import com.lixi.shoolcard.pages.password.ReportLossActivity;
import com.tencent.mmkv.MMKV;

import okhttp3.RequestBody;
import retrofit2.Call;

public class MainActivity extends BaseActivity {
    ActivityMainBinding binding;

    MMKV kv = MMKV.defaultMMKV();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getDeviceData();
        initListener();
        isMain = true;
    }

    private void getDeviceData() {
        //测试数据begin
//        kv.clearAll();
//        kv.encode(SchoolCardConstant.DEVICE_CODE, "AAAAAAA");
//        kv.encode(SchoolCardConstant.DEVICE_PASS, "123234455511");
//        kv.encode(SchoolCardConstant.SCHOOL_ID,1);
        //测试数据end

        //是否已缓存设备密码和校区id
        if (kv.decodeString(SchoolCardConstant.DEVICE_PASS) != null) {
            return;
        }

        String serviceUrl = kv.decodeString(SchoolCardConstant.SERVICE_URL);
        if (serviceUrl == null) {
            ToastUtil.showToast("没有配置服务器地址");
            return;
        }

        //是否已配置设备编号
        String deviceCode = kv.decodeString(SchoolCardConstant.DEVICE_CODE);
        if (deviceCode == null) {
            ToastUtil.showToast("没有配置设备编号");
            return;
        }
        //去服务器查询
        ShopApi shopApi = NetWorkUitl.getRetrofit().create(ShopApi.class);
        showLoading();
        Call<ApiResult<DevicceVo>> call = shopApi.getDevice(initDeviceRequest());
        call.enqueue(new MyCallbcak<ApiResult<DevicceVo>>() {
            @Override
            protected void setData(ApiResult<DevicceVo> result) {
                if (result.getData() != null) {
                    String decryptStr = DES3Util.desDecrypt(result.getData());
                    DevicceVo devicceVo = GsonUtil.getGson().fromJson(decryptStr, DevicceVo.class);
                    if (devicceVo == null || devicceVo.getDevicePass() == null) {
                        ToastUtil.showToast("没有查到设备信息");
                        return;
                    }
                    kv.encode(SchoolCardConstant.DEVICE_PASS, Md5Util.md5Encrypt(devicceVo.getDevicePass()));
                    if (devicceVo.getSchoolId() != null) {
                        kv.encode(SchoolCardConstant.SCHOOL_ID, devicceVo.getSchoolId());
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

    private void initListener() {
        //电控缴费
        binding.recharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (kv.decodeString(SchoolCardConstant.SERVICE_URL) == null) {
                    ToastUtil.showToast("没有配置服务器地址");
                    return;
                }
                if (kv.decodeString(SchoolCardConstant.DEVICE_PASS) == null) {
                    ToastUtil.showToast("没有配置设备信息");
                    return;
                }
                if (kv.decodeInt(SchoolCardConstant.SCHOOL_ID, -1) == -1) {
                    ToastUtil.showToast("没有查到校区信息");
                    return;
                }
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, RechargeActivity.class);
                startActivity(intent);
            }
        });
        //电控缴费记录查询
        binding.rechargerecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (kv.decodeString(SchoolCardConstant.SERVICE_URL) == null) {
                    ToastUtil.showToast("没有配置服务器地址");
                    return;
                }
                if (kv.decodeString(SchoolCardConstant.DEVICE_PASS) == null) {
                    ToastUtil.showToast("没有配置设备信息");
                    return;
                }
                if (kv.decodeInt(SchoolCardConstant.SCHOOL_ID, -1) == -1) {
                    ToastUtil.showToast("没有查到校区信息");
                    return;
                }
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, RechardRecordActivity.class);
                startActivity(intent);
            }
        });
        //交易查询
        binding.transactionrecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (kv.decodeString(SchoolCardConstant.SERVICE_URL) == null) {
                    ToastUtil.showToast("没有配置服务器地址");
                    return;
                }
                if (kv.decodeString(SchoolCardConstant.DEVICE_PASS) == null) {
                    ToastUtil.showToast("没有配置设备信息");
                    return;
                }
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, TransactionActivity.class);
                startActivity(intent);
            }
        });
        //挂失
        binding.reportloss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (kv.decodeString(SchoolCardConstant.SERVICE_URL) == null) {
                    ToastUtil.showToast("没有配置服务器地址");
                    return;
                }
                if (kv.decodeString(SchoolCardConstant.DEVICE_PASS) == null) {
                    ToastUtil.showToast("没有配置设备信息");
                    return;
                }
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, ReportLossActivity.class);
                startActivity(intent);
            }
        });
        //修改密码
        binding.changepassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (kv.decodeString(SchoolCardConstant.SERVICE_URL) == null) {
                    ToastUtil.showToast("没有配置服务器地址");
                    return;
                }
                if (kv.decodeString(SchoolCardConstant.DEVICE_PASS) == null) {
                    ToastUtil.showToast("没有配置设备信息");
                    return;
                }
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, PasswordActivity.class);
                startActivity(intent);
            }
        });
        //管理员页面
        binding.admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String adminPass = kv.decodeString(SchoolCardConstant.ADMIN_PASS);
                String text = "请输入管理员密码";
                if (adminPass == null) {
                    text = "请设置管理员密码";
                }

                LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
                View dialogView = inflater.inflate(R.layout.input_layout, null);
                TextView confirm = dialogView.findViewById(R.id.confirm);
                TextView cancel = dialogView.findViewById(R.id.cancel_btn);
                EditText input = dialogView.findViewById(R.id.input);
                input.setHint(text);
                Dialog alertDialog = new AlertDialog.Builder(MainActivity.this)
                        //.setTitle("系统配置")
                        .setView(dialogView)
                        .setCancelable(false)
                        .create();

                WindowManager.LayoutParams params = alertDialog.getWindow().getAttributes();
                params.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE;
                alertDialog.getWindow().setAttributes(params);
                //setViewFontSize(alertDialog.getWindow().getDecorView());
                confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CharSequence text = input.getText();
                        if (text != null && text.length() > 0) {
                            if (adminPass == null) {
                                kv.encode(SchoolCardConstant.ADMIN_PASS, text.toString());
                            } else if (!adminPass.equals(text.toString())) {
                                ToastUtil.showToast("密码不正确");
                                return;
                            }
                            input.setText("");
                            alertDialog.dismiss();
                            Intent intent = new Intent();
                            intent.setClass(MainActivity.this, AdminActivity.class);
                            startActivity(intent);
                        } else {
                            ToastUtil.showToast("请输入管理员密码");
                        }
                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        input.setText("");
                        alertDialog.dismiss();
                    }
                });
                alertDialog.show();
            }
        });
    }

    private RequestBody initDeviceRequest() {
        ApiRequest request = new ApiRequest();
        request.setDevicePass(null);
        System.out.println("请求参数：：" + GsonUtil.getGson().toJson(request));
        return NetWorkUitl.initRequestBody(request);
    }
}