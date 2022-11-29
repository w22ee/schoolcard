package com.lixi.shoolcard;

import android.app.Application;
import android.content.Context;

import com.lixi.shoolcard.base.ReadCard;
import com.tencent.mmkv.MMKV;

public class AppApplication extends Application {

    public static Context applicationContex;
    public  ReadCard readCard;

    @Override
    public void onCreate() {
        super.onCreate();
        String rootDir = MMKV.initialize(this);
        System.out.println("mmkv root: " + rootDir);
        applicationContex =  getApplicationContext();
        readCard =  ReadCard.getReadCard();
    }



}
