package com.lixi.shoolcard.base;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.lixi.shoolcard.MainActivity;
import com.lixi.shoolcard.R;

public abstract class BaseActivity extends AppCompatActivity {
    //数据加载的loading弹窗
    private ProgressDialog progressDialog;
    //卡号 只在卡业务及其子页面有效
    protected String cardNo;
    //定时关闭页面的handler
    protected Handler handler = new Handler(Looper.getMainLooper());
    //定时关闭页面的子线程
    protected Runnable runnable;
    //设置3分钟内未操作页面则关闭
    private long time = 1000 * 60 * 3;
    //标记是否主页面
    public boolean isMain = false;

    //隐藏底部导航栏
    protected void hideBottomUIMenu() {
        //全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //隐藏虚拟按键
        Window _window = getWindow();
        WindowManager.LayoutParams params = _window.getAttributes();
        params.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION|View.SYSTEM_UI_FLAG_IMMERSIVE;
        _window.setAttributes(params);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideBottomUIMenu();
        initProgerssDialog();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //页面展示时开始计时
        startAD();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //手指按下屏幕时删除任务
                handler.removeCallbacks(runnable);
                break;
            case MotionEvent.ACTION_UP:
                //手指离开屏幕时开始计时
                startAD();
                break;
        }
        return super.dispatchTouchEvent(event);
    }

    public void startAD() {
        //先移除，重新创建子线程
        handler.removeCallbacks(runnable);
        runnable = new Runnable() {
            @Override
            public void run() {
                //跳到主页面
                if(!isMain) {
                    Intent intent = new Intent(BaseActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };
        //延时操作
        handler.postDelayed(runnable, time);
    }

    //初始化loading弹窗
    private void initProgerssDialog(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(false); //循环滚动
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("loading...");
        progressDialog.setCancelable(false); //false不能取消显示，true可以取消显示
        WindowManager.LayoutParams params = progressDialog.getWindow().getAttributes();
        params.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION|View.SYSTEM_UI_FLAG_IMMERSIVE;
        progressDialog.getWindow().setAttributes(params);
    }

    public void showLoading(){
        //启动
        progressDialog.show();
    }

    public void hideLoading(){
        //关闭
        progressDialog.dismiss();
    }

    //设置页面标题和返回图标
    public void setTitle(String title){
        TextView textView =  findViewById(R.id.info_title);
        textView.setText(title);
        ImageView imageView = findViewById(R.id.backbutton);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //卡业务及其子页面，刷卡登录后有退出卡登录按钮
        TextView loginOutView = findViewById(R.id.login_out);
        loginOutView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cardNo = null;
                Intent intent = new Intent();
                intent.setClass(BaseActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    //展示退出卡登录按钮
    protected void showLoginOut(){
        TextView loginOutView = findViewById(R.id.login_out);
        loginOutView.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //页面不展示时移除子线程
        handler.removeCallbacks(runnable);
    }
}
