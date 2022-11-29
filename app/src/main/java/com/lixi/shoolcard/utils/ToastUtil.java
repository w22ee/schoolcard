package com.lixi.shoolcard.utils;

import android.app.Application;
import android.widget.Toast;

import com.lixi.shoolcard.AppApplication;

public class ToastUtil {
    public static void showToast(String msg){
        Toast.makeText(AppApplication.applicationContex, msg, Toast.LENGTH_LONG).show();
    }
}
