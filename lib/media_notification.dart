import 'package:flutter/services.dart';
import 'dart:async';

/*Currently only supports Android*/

class MediaNotification {
  static const MethodChannel _channel = const MethodChannel('media_notification');
  static EventChannel _eventChannel = EventChannel("event");
  final StreamController _eventController = StreamController.broadcast();


  MediaNotification(){
    _eventChannel.receiveBroadcastStream().listen((data) {
      _eventController.add(data);
    });
  }

  /*event*/
  Stream get onEventChanged => _eventController.stream;

  /*open music_notification*/
  Future<String> get open async {
    final String res = await _channel.invokeMethod('open');
    return res;
  }

  /*close music_notification*/
  Future<String> get close async {
    final String res = await _channel.invokeMethod('close');
    return res;
  }

  /*play*/
  Future<String> play(String audioName, String author, [String image='']) async {
    final String res = await _channel.invokeMethod('play',
        <String,dynamic>{
          'audioName': audioName,
          'author': author,
          'image': image
        }
    );
    return res;
  }

  /*pause*/
  Future<String> pause() async {
    final String res = await _channel.invokeMethod('pause');
    return res;
  }

  /*resume*/
  Future<String> resume() async {
    final String res = await _channel.invokeMethod('resume');
    return res;
  }
}
