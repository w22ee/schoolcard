package com.lixi.shoolcard.base;

/**
 * 串口读取到卡号后发送事件通知
 */
public class ReadEvent {
    public final String message;

    public ReadEvent(String message) {
        this.message = message;
    }
}
