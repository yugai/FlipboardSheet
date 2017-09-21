# 仿造Flipboard收藏页面
[![](https://jitpack.io/v/HuangGangHust/DragPhotoView.svg)](https://jitpack.io/#HuangGangHust/DragPhotoView)

利用`BottomSheetBehavior`仿造Flipboard制作了一个可用于收藏分享之类的页面。

![image1](https://github.com/yugai/FlipboardSheet/blob/master/image/image1.gif)
![image2](https://github.com/yugai/FlipboardSheet/blob/master/image/image2.gif)

## 依赖

```
   dependencies {
	        compile 'com.github.yugai:FlipboardSheet:1.1.0'
	}
```
## 使用


```xml
<com.mayi.library.FlipboardSheetView 
    android:id="@+id/flipboard_view" 
    xmlns:android="http://schemas.android.com/apk/res/android" 
    xmlns:app="http://schemas.android.com/apk/res-auto" 
    xmlns:tools="http://schemas.android.com/tools" 
    android:layout_width="match_parent" 
    android:layout_height="match_parent" 
    android:background="@android:color/black" 
    app:peek_height="100dp"  
    app:sheet_layout="@layout/bottom_sheet_layout" 
    app:zoom_margin="50dp">  
    <FrameLayout 
        android:layout_width="match_parent" 
        android:layout_height="match_parent" 
        android:background="@android:color/white">  
    <ImageView 
        android:onClick="click" 
        android:layout_width="wrap_content" 
        android:layout_height="wrap_content" 
        android:layout_gravity="center" 
        android:background="@mipmap/ic_launcher_round"/> 
    </FrameLayout>
     </com.mayi.library.FlipboardSheetView>
```

属性 | 作用
------- | -------
peek_height | 收缩模式底部显示高度
sheet_layout | 底部弹出内容视图
zoom_margin | 主视图缩放的间距



方法 | 作用
------- | -------
addStateChangeCallback | 添加状态监听
switchState | 切换缩放模式
setState | 设置指定模式
getState | 获取当前模式
setInterpolator | 设置动画
addSheetLayout | 动态添加底部视图和设置高度

## 联系方式
如有问题请发送至784787081@qq.com或者issue。







