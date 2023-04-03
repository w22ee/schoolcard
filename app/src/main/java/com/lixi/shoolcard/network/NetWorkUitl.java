package com.lixi.shoolcard.network;

import com.google.gson.Gson;
import com.lixi.shoolcard.constant.SchoolCardConstant;
import com.lixi.shoolcard.network.vo.ApiRequest;
import com.lixi.shoolcard.utils.GsonUtil;
import com.tencent.mmkv.MMKV;

import java.util.Date;

import okhttp3.RequestBody;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;


public class NetWorkUitl {
    //public static String baseUrl = "http://xx.floatfd.cn/rest/open/";
    public static Retrofit retrofit = null;
    private static MMKV kv = MMKV.defaultMMKV();

    public static Retrofit getRetrofit(){
        if(retrofit==null){
            String serviceUrl = kv.decodeString(SchoolCardConstant.SERVICE_URL,"");
            retrofit = new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .baseUrl(serviceUrl) //网络请求的url地址 解析器，解析json
                    .build();
        }

        return retrofit;
    };

    //当配置修改时重新创建Retrofit 如果调用服务成功再更新缓存
    public static Retrofit recreate(String url){
        Retrofit newRetrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .baseUrl(url) //网络请求的url地址 解析器，解析json
                .build();
        return newRetrofit;
    }

    public static RequestBody initRequestBody(ApiRequest request){
        Gson gson= GsonUtil.getGson();
        String route= gson.toJson(request);//通过Gson将Bean转化为Json字符串形式
        RequestBody body=RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),route);
        if(kv.getBoolean("open_log",false)){
            String aalog =  kv.getString("aalog","");
            StringBuilder stringBuilder = new StringBuilder(aalog);
            stringBuilder.append(new Date());
            stringBuilder.append("\r\n");
            stringBuilder.append("\r\n");
            stringBuilder.append("请求参数：：" + GsonUtil.getGson().toJson(request));
            stringBuilder.append("\r\n");
            stringBuilder.append("\r\n");
            kv.putString("aalog",stringBuilder.toString());
        }
        return body;
    }
}
