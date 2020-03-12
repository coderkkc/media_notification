package com.chen.media_notification;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class PlayerReceiver extends BroadcastReceiver {
    public static final String PLAY_PRE = "play_pre";
    public static final String PLAY_NEXT = "play_next";
    public static final String PLAY_RESUME_PAUSE = "play_resume_pause";
    public static final String PLAY_LIKE = "play_like";
    public static final String PLAY_EXIT = "play_exit";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(PLAY_NEXT)) {//PLAY_NEXT
            Log.e("layerReceiver", "通知栏点击了下一首");
        }
        if (intent.getAction().equals(PLAY_PRE)) {
            ;
            Log.e("layerReceiver", "通知栏点击了上一首");
        }
        if (intent.getAction().equals(PLAY_RESUME_PAUSE)) {
            Log.e("layerReceiver", "通知栏点击了播放");
        }
        if (intent.getAction().equals(PLAY_LIKE)) {
            Log.e("layerReceiver", "通知栏点击了喜欢");
        }
        if (intent.getAction().equals(PLAY_EXIT)) {
            Log.e("layerReceiver", "通知栏点击了退出");
        }
    }
}