package com.lixi.shoolcard.pages.cardbusiness;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.lixi.shoolcard.R;
import com.lixi.shoolcard.base.BaseActivity;
import com.lixi.shoolcard.base.ReadEvent;
import com.lixi.shoolcard.databinding.ActivityTransactionBinding;
import com.lixi.shoolcard.utils.DES3Util;
import com.lixi.shoolcard.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * 交易查询
 */
public class TransactionActivity extends BaseActivity {
    private AlertDialog alertDialog;
    ActivityTransactionBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTransactionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setTitle("交易查询");
        initDialog();
        initListener();
    }

    private void initDialog(){
        LayoutInflater inflater = LayoutInflater.from(TransactionActivity.this);
        View dialogView = inflater.inflate(R.layout.btn_layout, null);
        TextView cancel = dialogView.findViewById(R.id.cancel_btn);
        TextView content = dialogView.findViewById(R.id.content);
        alertDialog = new AlertDialog.Builder(TransactionActivity.this)
                .setTitle("")
                .setView(dialogView)
                .setCancelable(false)
                .create();
        content.setText("查询后请及时关闭页面防止信息泄漏");
        WindowManager.LayoutParams params = alertDialog.getWindow().getAttributes();
        params.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE;
        alertDialog.getWindow().setAttributes(params);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                finish();
            }
        });
        alertDialog.show();
    }

    private void initListener(){
        //卡信息查询
        binding.cardinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cardNo == null){
                    ToastUtil.showToast("请先刷卡");
                    return;
                }
                Intent intent = new Intent();
                intent.setClass(TransactionActivity.this, CardInfoActivity.class);
                intent.putExtra("cardNo",cardNo);
                startActivity(intent);
            }
        });
        //卡缴费记录查询
        binding.rechargebill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cardNo == null){
                    ToastUtil.showToast("请先刷卡");
                    return;
                }
                Intent intent = new Intent();
                intent.setClass(TransactionActivity.this, RechargeBillActivity.class);
                intent.putExtra("cardNo",cardNo);
                startActivity(intent);
            }
        });
        //卡充值查询
        binding.cardrechargerecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cardNo == null){
                    ToastUtil.showToast("请先刷卡");
                    return;
                }
                Intent intent = new Intent();
                intent.setClass(TransactionActivity.this, CardRechargeActivity.class);
                intent.putExtra("cardNo",cardNo);
                startActivity(intent);
            }
        });
        //卡消费查询
        binding.paymentrecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cardNo == null){
                    ToastUtil.showToast("请先刷卡");
                    return;
                }
                Intent intent = new Intent();
                intent.setClass(TransactionActivity.this, PaymentRecordActivity.class);
                intent.putExtra("cardNo",cardNo);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        //接收刷卡信息
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //接收刷卡信息
        EventBus.getDefault().unregister(this);
    }

    //读卡
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReadEvent(ReadEvent event) {
        cardNo = DES3Util.desEncrypt(event.message);
        //刷卡后再显示
        showLoginOut();
        if(alertDialog != null) {
            alertDialog.dismiss();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(alertDialog != null) {
            alertDialog.dismiss();
        }
    }
}