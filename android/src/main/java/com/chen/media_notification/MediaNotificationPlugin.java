package com.chen.media_notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import androidx.annotation.NonNull;

import io.flutter.Log;
import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.EventChannel;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;

/** MediaNotificationPlugin */
public class MediaNotificationPlugin implements FlutterPlugin, MethodCallHandler {
  public static Context context;
  public static LocationReceiver locationReceiver;
  public static EventChannel.EventSink eventSink;
  public static NotificationUtil notificationUtil;

  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
    if(context==null){
      context = flutterPluginBinding.getApplicationContext();
    }
    final EventChannel eventChannel = new EventChannel(flutterPluginBinding.getBinaryMessenger(), "event");
    eventChannel.setStreamHandler(new EventChannel.StreamHandler() {
      @Override
      public void onListen(Object o, EventChannel.EventSink eventSinks) {
        eventSink = eventSinks;
      }

      @Override
      public void onCancel(Object o) {
        eventSink = null;
      }
    });
    if(locationReceiver==null){
      locationReceiver = new LocationReceiver();
      IntentFilter filter = new IntentFilter();
      filter.addAction("pre");
      filter.addAction("next");
      filter.addAction("resume");
      filter.addAction("pause");
      flutterPluginBinding.getApplicationContext().registerReceiver(locationReceiver, filter);
      System.out.println("入口程序");
    }
    final MethodChannel channel = new MethodChannel(flutterPluginBinding.getFlutterEngine().getDartExecutor(), "media_notification");
    channel.setMethodCallHandler(new MediaNotificationPlugin());
  }


  public static void registerWith(Registrar registrar) {
    final MethodChannel channel = new MethodChannel(registrar.messenger(), "media_notification");
    channel.setMethodCallHandler(new MediaNotificationPlugin());
  }

  public class LocationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
      String intentAction = intent.getAction();
      if(eventSink!=null){
        eventSink.success(intentAction);
      }
    }
  }

  @Override
  public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
    if(call.method.equals("play")) {
      String audioName = call.argument("audioName");
      String author = call.argument("author");
      String image = call.argument("image");
      notificationUtil = new NotificationUtil(context, audioName, author, image);
      notificationUtil.showNotification();
    }else if(call.method.equals("resume")){
      if(notificationUtil!=null){
        notificationUtil.operate("resume");
      }
    }else if(call.method.equals("pause")){
      if(notificationUtil!=null){
        notificationUtil.operate("pause");
      }
    }else if(call.method.equals("close")) {
      notificationUtil.exit();
    }else{
      result.notImplemented();
    }
  }

  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
  }
}
