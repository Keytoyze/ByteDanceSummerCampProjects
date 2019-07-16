# CH5 Android多媒体基础
apk地址：[这里](https://github.com/Keytoyze/ByteDanceSummerCampProjects/blob/master/day7/app-debug.apk)

## 图片显示
- 使用Kotlin重写了demo项目代码。
- 修改了部分UI，去除ActionBar和StatusBar，背景色改为纯黑色。
- 使用[PermissionDispatcher](https://github.com/permissions-dispatcher/PermissionsDispatcher)框架来处理读写权限。第一次进入界面时，将向用户申请读写权限。如果成功获得，则从图库内所有图片中取出修改时间最早的五张图片，显示在最前面。否则就不显示这五张图片，只显示本地的资源图片和网络图片。
- 使用[PhotoView](https://github.com/chrisbanes/PhotoView)来处理图片手势缩放操作。双指缩放可以调整图片尺寸。
- 使用PageAdapter实现左右滑动操作。
- 使用[Glide](https://github.com/bumptech/glide)来进行图片处理。

### 截图
![有权限时](https://github.com/Keytoyze/ByteDanceSummerCampProjects/blob/master/day7/screenshot/photo1.jpg)

![无权限时](https://github.com/Keytoyze/ByteDanceSummerCampProjects/blob/master/day7/screenshot/photo2.jpg)

## 视频播放器
- 使用Kotlin重写了demo项目代码。
- 修改了部分UI，增宽进度条，背景色改为纯黑色。竖屏时视频居中。
- 使用[ijkplayer](https://github.com/bilibili/ijkplayer)处理音频。
- 使用[RxJava2](https://github.com/ReactiveX/RxJava/tree/2.x)替代Handler进行定时刷新进度任务，解决内存泄漏问题。
- 自定义音量调整UI，并使用AudioManager调整音量。
- 播放结束后，可以回到开始重新进行播放。

### 截图
![横屏](https://github.com/Keytoyze/ByteDanceSummerCampProjects/blob/master/day7/screenshot/video1.jpg)
![竖屏](https://github.com/Keytoyze/ByteDanceSummerCampProjects/blob/master/day7/screenshot/video2.jpg)