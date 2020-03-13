# media_notification

This is a flutter media notification bar plugin, currently only supports Android


## Getting Started
### Add dependency

```yaml
dev_dependencies:
  media_notification:
    git:
      url: git://github.com/coderkkc/media_notification.git
```

### Super simple to use

```dart
import 'package:media_notification/media_notification.dart';

MediaNotification mediaNotification = MediaNotification();

mediaNotification.play('下山', '要不要买菜', 'http://p1.music.126.net/Aj4X1kpV-C2LLi-e_Xhgvg==/109951164499744148.jpg?param=320y320');
mediaNotification.pause();
mediaNotification.resume();
mediaNotification.onEventChanged.listen((data){
  if(data=="pre"){
    switchPreviousAudio();
  }else if(data=="next"){
    switchNextAudio();
  }else if(data=="resume"){
    resume();
  }else if(data=="pause"){
    pause();
  }
});
```
![Image text](https://github.com/coderkkc/media_notification/blob/master/image/Screenshot_2020-03-13-10-22-18-678_com.chen.ttasm.jpg)
![Image text](https://github.com/coderkkc/media_notification/blob/master/image/Screenshot_2020-03-13-10-22-11-888_com.chen.ttasm.jpg)

This project is a starting point for a Flutter
[plug-in package](https://flutter.dev/developing-packages/),
a specialized package that includes platform-specific implementation code for
Android and/or iOS.

For help getting started with Flutter, view our 
[online documentation](https://flutter.dev/docs), which offers tutorials, 
samples, guidance on mobile development, and a full API reference.
