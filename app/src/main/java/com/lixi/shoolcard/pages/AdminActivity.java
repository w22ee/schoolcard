package com.lixi.shoolcard.pages;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.RadioGroup;

import com.lixi.shoolcard.base.BaseActivity;
import com.lixi.shoolcard.constant.SchoolCardConstant;
import com.lixi.shoolcard.databinding.ActivityAdminBinding;
import com.lixi.shoolcard.network.MyCallbcak;
import com.lixi.shoolcard.network.NetWorkUitl;
import com.lixi.shoolcard.network.ShopApi;
import com.lixi.shoolcard.network.vo.ApiRequest;
import com.lixi.shoolcard.network.vo.ApiResult;
import com.lixi.shoolcard.network.vo.DevicceVo;
import com.lixi.shoolcard.utils.DES3Util;
import com.lixi.shoolcard.utils.GsonUtil;
import com.lixi.shoolcard.utils.Md5Util;
import com.lixi.shoolcard.utils.ToastUtil;
import com.tencent.mmkv.MMKV;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Retrofit;

/**
 * 挂失
 */
public class AdminActivity extends BaseActivity {

    private ActivityAdminBinding binding;
    private MMKV kv = MMKV.defaultMMKV();
    private Retrofit retrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setTitle("管理员页面");

        //kv.clearAll();
        String deviceCode = kv.decodeString(SchoolCardConstant.DEVICE_CODE);
        if(deviceCode != null){
            binding.deviceCode.setText(DES3Util.desDecrypt(deviceCode));
        }
        String serviceUrl = kv.decodeString(SchoolCardConstant.SERVICE_URL);
        if(serviceUrl != null){
            binding.serviceUrl.setText(serviceUrl);
        }

        binding.saveDeviceCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String serviceUrl = generateUrl();
                if(serviceUrl == null) {
                    return;
                }
                String deviceCode = binding.deviceCode.getText().toString();
                if("".equals(deviceCode)) {
                    ToastUtil.showToast("请输入设备编号");
                    return;
                }
                String encryptCode = DES3Util.desEncrypt(deviceCode);
                if("".equals(encryptCode)) {
                    ToastUtil.showToast("设备编号加密出错");
                    return;
                }

//                //测试数据begin
//                kv.encode(SchoolCardConstant.DEVICE_PASS, "123234455511");
//                kv.encode(SchoolCardConstant.SCHOOL_ID,1);
//                ToastUtil.showToast("设置成功");
//                //测试数据end
                String cacheUrl = kv.decodeString(SchoolCardConstant.SERVICE_URL);
                if(cacheUrl == null || !serviceUrl.equals(cacheUrl)){
                    retrofit = NetWorkUitl.recreate(serviceUrl);
                }else{
                    retrofit = NetWorkUitl.getRetrofit();
                }
                //加载设备信息
                ShopApi shopApi = retrofit.create(ShopApi.class);
                Call<ApiResult<DevicceVo>> call = shopApi.getDevice(initDeviceRequest(encryptCode));
                call.enqueue(new MyCallbcak<ApiResult<DevicceVo>>() {
                    @Override
                    protected void setData(ApiResult<DevicceVo> result) {
                    //服务调用成功再设置
                    NetWorkUitl.retrofit = retrofit;
                    kv.encode(SchoolCardConstant.SERVICE_URL, serviceUrl);
                    if (result.getData() != null) {
                        String decryptStr = DES3Util.desDecrypt(result.getData());
                        DevicceVo devicceVo = GsonUtil.getGson().fromJson(decryptStr, DevicceVo.class);
                        if (devicceVo == null || devicceVo.getDevicePass() == null) {
                            ToastUtil.showToast("没有查到设备信息");
                            return;
                        }
                        kv.encode(SchoolCardConstant.DEVICE_CODE, encryptCode);
                        kv.encode(SchoolCardConstant.DEVICE_PASS, Md5Util.md5Encrypt(devicceVo.getDevicePass()));
                        if (devicceVo.getSchoolId() != null) {
                            kv.encode(SchoolCardConstant.SCHOOL_ID, devicceVo.getSchoolId());
                        } else {
                            kv.remove(SchoolCardConstant.SCHOOL_ID);
                        }
                        kv.remove(SchoolCardConstant.CACHE_BUILDING);
                        kv.remove(SchoolCardConstant.CACHE_ROOM);
                        ToastUtil.showToast("设置成功");
                        finish();
                    }
                    }
                });
            }
        });
        binding.sysSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Settings.ACTION_SETTINGS));
            }
        });
        binding.rgNetwork.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                String serviceUrl = binding.serviceUrl.getText().toString();
                int j = serviceUrl.indexOf("://");
                if(j != -1) {
                    serviceUrl = serviceUrl.substring(j+3, serviceUrl.length());
                }
                if(binding.rbHttp.isChecked()){
                    serviceUrl = "http://"+serviceUrl;
                }else{
                    serviceUrl = "https://"+serviceUrl;
                }
                binding.serviceUrl.setText(serviceUrl);
            }
        });
    }

    private RequestBody initDeviceRequest(String encryptCode){
        ApiRequest request = new ApiRequest();
        request.setDeviceCode(encryptCode);
        request.setDevicePass(null);
        System.out.println("请求参数：："+GsonUtil.getGson().toJson(request));
        return NetWorkUitl.initRequestBody(request);
    }

    private String generateUrl(){
        String serviceUrl = binding.serviceUrl.getText().toString();
        if("".equals(serviceUrl)) {
            ToastUtil.showToast("请输入服务器地址");
            return null;
        }
        if(!serviceUrl.startsWith("http://") && !serviceUrl.startsWith("https://")){
            ToastUtil.showToast("服务器地址必须以http或https开头");
            return null;
        }
        if(!serviceUrl.endsWith("/")){
            serviceUrl = serviceUrl + "/";
        }
        return serviceUrl;
    }
}