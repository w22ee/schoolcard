package com.lixi.shoolcard.network;


import com.lixi.shoolcard.network.vo.ApiResult;
import com.lixi.shoolcard.utils.DES3Util;
import com.lixi.shoolcard.utils.GsonUtil;
import com.lixi.shoolcard.utils.ToastUtil;
import com.tencent.mmkv.MMKV;

import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * API请求后的回调
 * @param <T>
 */
public abstract class MyCallbcak<T> implements Callback<T>  {
    @Override
    public void onResponse(Call<T> call, Response<T> response) {

        onFinish();
        if(response == null || response.body() == null
                || response.body() instanceof ApiResult == false){
            ToastUtil.showToast("未知错误:"+ (response == null ? "" : response.message()));
            System.out.println("返回值：："+(response == null ? "" : response.message()));
            return;
        }
        System.out.println("返回值：："+ GsonUtil.getGson().toJson(response.body()));
        MMKV kv = MMKV.defaultMMKV();
        String aalog =  kv.getString("aalog","");
        StringBuilder stringBuilder = new StringBuilder(aalog);
        stringBuilder.append(new Date());
        stringBuilder.append("\r\n");
        stringBuilder.append("\r\n");
        stringBuilder.append("返回值：："+ GsonUtil.getGson().toJson(response.body()));
        stringBuilder.append("\r\n");
        stringBuilder.append("\r\n");


        ApiResult result = (ApiResult)response.body();
        String decryptStr = "";
        if(result.getData() != null){
             decryptStr = DES3Util.desDecrypt(result.getData());
        }
        stringBuilder.append("解密data返回值：："+decryptStr);
        stringBuilder.append("\r\n");
        stringBuilder.append("\r\n");
        if(kv.getBoolean("open_log",false)){
            kv.putString("aalog",stringBuilder.toString());
        }
        if(result.getErrCode() != 0){
            ToastUtil.showToast(result.getErroeMsg());
            return;
        }

        //可以在这里统一解密，但是获取不到泛型的Class
//        if(result.getData() != null){
//            String decryptStr = DES3Util.desDecrypt(result.getData());
//            Class<T> tClass =
//            GsonUtil.getGson().fromJson(decryptStr,);
//        }

        setData(response.body());
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        onFinish();
        ToastUtil.showToast("系统错误:"+t.getMessage());
        System.out.println(t.getMessage());
    }

    protected abstract void setData(T result);

    protected  void onFinish(){

    };
}
