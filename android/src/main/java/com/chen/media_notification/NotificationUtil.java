package com.chen.media_notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;


/**
 * @author ckk
 */
public class NotificationUtil extends BroadcastReceiver {
    private static PlayBroadcastReceiver playBroadcastReceiver;
    private static NotificationManager notificationManager;
    private static NotificationChannel mChannel;
    private static Notification notification;
    private static RemoteViews mRemoteViews;
    private static RemoteViews mBigRemoteViews;
    private static Context mContext;
    private static String audioName, author, image;
    private static boolean isPlay = true;
    private static String id = "channel";

    public static final int NOTIFICATION_ID = 10006;
    public static final String PLAY_PRE = "play_pre";
    public static final String PLAY_NEXT = "play_next";
    public static final String PLAY_RESUME_PAUSE = "play_resume_pause";
    public static final String PLAY_LIKE = "play_like";
    public static final String PLAY_EXIT = "play_exit";

    public class PlayBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

        }
    }

    public NotificationUtil(){

    }

    public NotificationUtil(Context mContext, String audioName, String author, String image) {
        playBroadcastReceiver = new PlayBroadcastReceiver();
        this.mContext = mContext;
        this.audioName = audioName;
        this.author = author;
        this.image = image;
        final IntentFilter filter = new IntentFilter();
        filter.addAction("pre");
        filter.addAction("next");
        filter.addAction("resume");
        filter.addAction("pause");
        mContext.registerReceiver(playBroadcastReceiver, filter);
        notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent play_intent = new Intent();
        if (intent.getAction().equals(PLAY_NEXT)) {
            play_intent.setAction("next");
            mContext.sendBroadcast(play_intent);
        }
        if (intent.getAction().equals(PLAY_PRE)) {
            play_intent.setAction("pre");
            mContext.sendBroadcast(play_intent);
        }
        if (intent.getAction().equals(PLAY_RESUME_PAUSE)) {
            isPlay = !isPlay;
            play_intent.setAction(isPlay? "resume": "pause");
            mContext.sendBroadcast(play_intent);
            operate(isPlay? "resume": "pause");
        }
        if (intent.getAction().equals(PLAY_LIKE)) {

        }
        if (intent.getAction().equals(PLAY_EXIT)) {
            exit();
        }

    }

    /*resume & pause operation*/
    public void operate(String operate) {
        if(operate.equals("resume")){
            mRemoteViews.setImageViewResource(R.id.btn_custom_play, R.mipmap.ic_pause);
            mBigRemoteViews.setImageViewResource(R.id.btn_custom_play, R.mipmap.ic_pause);
        }else{
            mRemoteViews.setImageViewResource(R.id.btn_custom_play, R.mipmap.ic_play);
            mBigRemoteViews.setImageViewResource(R.id.btn_custom_play, R.mipmap.ic_play);
        }
        notification.contentView = mRemoteViews;
        notification.bigContentView = mBigRemoteViews;
        notificationManager.notify(NOTIFICATION_ID, notification);
    }

    /*close notification*/
    public void exit(){
        notificationManager.cancel(NOTIFICATION_ID);
    }

    /*show notification*/
    public void showNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(new NotificationChannel("music", "Music", NotificationManager.IMPORTANCE_HIGH));
            mChannel = new NotificationChannel(id, "media", NotificationManager.IMPORTANCE_LOW);
            mChannel.setDescription("music notification");
            mChannel.enableLights(false);
            mChannel.setLightColor(Color.BLUE);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            notificationManager.createNotificationChannel(mChannel);
            notification = new NotificationCompat.Builder(mContext, id)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setWhen(System.currentTimeMillis())
                    .setContentIntent(getDefaultIntent(Notification.FLAG_ONGOING_EVENT))
                    .setCustomBigContentView(getContentView(true))
                    .setCustomContentView(getContentView(false))
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setTicker("正在播放")
                    .setOngoing(true)
                    .setChannelId(mChannel.getId())
                    .build();
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            notification = new NotificationCompat.Builder(mContext, id)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentIntent(getDefaultIntent(Notification.FLAG_ONGOING_EVENT))
                    .setCustomBigContentView(getContentView(true))
                    .setCustomContentView(getContentView(false))
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setTicker("正在播放")
                    .setOngoing(true)
                    .build();
        } else {
            notification = new NotificationCompat.Builder(mContext, id)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentIntent(getDefaultIntent(Notification.FLAG_ONGOING_EVENT))
                    .setContent(getContentView(false))
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setTicker("正在播放")
                    .setOngoing(true)
                    .build();
        }
        notificationManager.notify(NOTIFICATION_ID, notification);
    }

    private PendingIntent getDefaultIntent(int flags) {
        return PendingIntent.getActivity(mContext, 1, new Intent(), flags);
    }

    /*content view*/
    private RemoteViews getContentView(boolean showBigView) {
        mRemoteViews = new RemoteViews(mContext.getPackageName(), showBigView ? R.layout.view_notify_big : R.layout.view_notify_small);
        mRemoteViews.setTextViewText(R.id.tv_custom_song_name, audioName);
        mRemoteViews.setTextViewText(R.id.tv_custom_song_singer, author);
        mRemoteViews.setImageViewResource(R.id.btn_custom_play, R.mipmap.ic_pause);
        if (!image.equals("")) {
            MyAsyncTask task=new MyAsyncTask();
            task.execute(image);//async load
        }

        //listen switch pre
        Intent pre = new Intent(mContext, NotificationUtil.class);
        pre.setAction(PLAY_PRE);
        PendingIntent intent_pre = PendingIntent.getBroadcast(mContext, 1, pre, PendingIntent.FLAG_UPDATE_CURRENT);
        mRemoteViews.setOnClickPendingIntent(R.id.btn_custom_prev, intent_pre);

        //listen switch next
        Intent next = new Intent(mContext, NotificationUtil.class);
        next.setAction(PLAY_NEXT);
        PendingIntent intent_next = PendingIntent.getBroadcast(mContext, 1, next, PendingIntent.FLAG_UPDATE_CURRENT);
        mRemoteViews.setOnClickPendingIntent(R.id.btn_custom_next, intent_next);

        //listen switch resume/pause
        Intent resumePause = new Intent(mContext, NotificationUtil.class);
        resumePause.setAction(PLAY_RESUME_PAUSE);
        PendingIntent intent_resume_pause = PendingIntent.getBroadcast(mContext, 1, resumePause, PendingIntent.FLAG_UPDATE_CURRENT);
        mRemoteViews.setOnClickPendingIntent(R.id.btn_custom_play, intent_resume_pause);

        //listen exit
        Intent exit = new Intent(mContext, NotificationUtil.class);
        exit.setAction(PLAY_EXIT);
        PendingIntent intent_exit = PendingIntent.getBroadcast(mContext, 1, exit, PendingIntent.FLAG_UPDATE_CURRENT);
        mRemoteViews.setOnClickPendingIntent(R.id.btn_custom_close, intent_exit);

        if(showBigView){
            mBigRemoteViews = mRemoteViews;
        }
        return mRemoteViews;
    }


    class MyAsyncTask extends AsyncTask<String,Void,Bitmap> {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Bitmap bitmap)
        {
            super.onPostExecute(bitmap);
            if (bitmap != null) {
                mRemoteViews.setImageViewBitmap(R.id.custom_song_icon, bitmap);
                mBigRemoteViews.setImageViewBitmap(R.id.custom_song_icon, bitmap);
                notification.bigContentView = mBigRemoteViews;
                notification.contentView = mRemoteViews;
                notificationManager.notify(NOTIFICATION_ID, notification);
            }
        }

        @Override
        protected Bitmap doInBackground(String... params)
        {
            String url=params[0];
            Bitmap bitmap=null;
            URLConnection conn;
            InputStream is;
            try
            {
                conn=new URL(url).openConnection();
                is=conn.getInputStream();
                BufferedInputStream bis=new BufferedInputStream(is);
                bitmap=BitmapFactory.decodeStream(bis);
                is.close();bis.close();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            return bitmap;
        }
    }
}
