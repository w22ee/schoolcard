package com.lixi.shoolcard.base;

import android.util.Log;

import com.lixi.shoolcard.utils.GsonUtil;
import com.tencent.mmkv.MMKV;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

import android_serialport_api.SerialPort;

/**
 * 读卡操作
 */
public class ReadCard {
    //串口
    private SerialPort mSerialPort;
    private InputStream mInputStream;
    private OutputStream mOutputStream;
    private ReadThread mReadThread;

    private static class ReadHolder{
        private static ReadCard  readCard = new ReadCard();
    }

    public  static ReadCard getReadCard(){
        return ReadHolder.readCard;
    }

    public ReadCard() {
        try {
            mSerialPort = new SerialPort(new File("/dev/ttyS3"), 9600, 0);//这里串口地址和比特率记得改成你板子要求的值。
        } catch (IOException e) {
            e.printStackTrace();
        }
        mInputStream = mSerialPort.getInputStream();
        mOutputStream = mSerialPort.getOutputStream();
        mReadThread = new ReadThread();
        mReadThread.start();
    }

    private class ReadThread extends Thread {
        @Override
        public void run() {
            super.run();
            while(!isInterrupted()) {
                int size;
                Log.v("debug", "接收线程"+mReadThread.toString()+"已经开启");
                try {
                    byte[] buffer = new byte[64];

                    if (mInputStream == null)
                        return;

                    size = mInputStream.read(buffer);

                    if (size > 0)
                    {
                        onDataReceived(buffer, size);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
            }
        }
    }

    protected void onDataReceived(final byte[] buffer, final int size) {
        String info = bytesToHex(buffer, 0, size);
        Log.v("debug", "线程"+mReadThread.toString()+"接收到串口信息======>" + info);
        MMKV kv = MMKV.defaultMMKV();
        if(kv.getBoolean("open_log",false)){
            String aalog =  kv.getString("aalog","");
            StringBuilder stringBuilder = new StringBuilder(aalog);
            stringBuilder.append(new Date());
            stringBuilder.append("\r\n");
            stringBuilder.append("\r\n");
            stringBuilder.append("接收到串口信息======>" + info);
            stringBuilder.append("\r\n");
            stringBuilder.append("\r\n");
            kv.putString("aalog",stringBuilder.toString());
        }
        EventBus.getDefault().post(new ReadEvent(info));
    }

    private String bytesToHex(byte[] bytes,int offset,int size) {
        StringBuilder sb = new StringBuilder();
        for (int i = offset; i < offset+size; i++) {
            byte b = bytes[i];
            sb.append(String.format("%02x", b));
        }
        if(sb.length() == 14){
            return sb.substring(4,sb.length()-2);
        }
        return sb.toString();
    }

    public void close(){
        mReadThread.interrupt();
        try {
            if(mInputStream != null)
                mInputStream.close();
            if (mOutputStream != null)
                mOutputStream.close();
            if (mSerialPort != null) {
                mSerialPort.close();
                mSerialPort = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
