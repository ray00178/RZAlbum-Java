RZAlbum ![](http://arminray.ga/image/rzalbum_planform.svg) ![](http://arminray.ga/image/rzalbum_version.svg) ![](http://arminray.ga/image/rzalbum_license.svg)
====
The RZAlbum for android to select the photo library. And usage：<br/>
* Support Single choice、Multiple choice、Preview、Folder switch and take pictures.  
* For __6.0 or later__, The permissions have been handled very well，So don't worry about their own.
* According to your project color, Setting ur StatusBarColor、ToolBarColor.
* According to your preferences / needs, Show the number of fields and select the number of restrictions.
* In Activity or Frangment, Can support the use.
* For __Android7.0 or later, the camera function through the FileProvider do adaptation processing.__<br/>

Screenshots <br/><br/>
![](https://github.com/ray00178/RayZhangAlbum/blob/master/Screenshot_1.jpg)
![](https://github.com/ray00178/RayZhangAlbum/blob/master/Screenshot_2.jpg)<br/>
<img src="https://github.com/ray00178/RayZhangAlbum/blob/master/Screenshot_3.gif" alt="Demo_gif" title="Demo_gif" width="300" height="500" /><br/><br/>
Gradle
====
```java
compile 'com.rayzhang.android:rzalbum:1.1.1'
```
Maven
====
```java
<dependency>
  <groupId>com.rayzhang.android</groupId>
  <artifactId>rzalbum</artifactId>
  <version>1.1.1</version>
  <type>pom</type>
</dependency>
```
Usage
====
  1.Androidmanifest.xml, Add the following code.
  ```xml
  <!-- android:theme = Set according to your style
  <style name="AppTheme" parent="Theme.AppCompat.Light.DarkActionBar">
      <item name="colorPrimary">@color/colorPrimary</item>
      <item name="colorPrimaryDark">@color/colorPrimaryDark</item>
      <item name="colorAccent">@color/colorAccent</item>
  </style>

  <style name="AppNoActionBar" parent="AppTheme">
      <item name="windowActionBar">false</item>
      <item name="windowNoTitle">true</item>
  </style> -->
  <activity
     android:name="com.rayzhang.android.rzalbum.RZAlbumActivity"
      android:configChanges="orientation|keyboardHidden|screenSize"
     android:screenOrientation="portrait"
      android:theme="@style/AppNoActionBar"
      android:windowSoftInputMode="stateAlwaysHidden|stateHidden"/>
  ```
  2.Androidmanifest.xml, Add the following permissions.
  ```xml
    <uses-permission android:name="android.permission.CAMERA" />
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
  ```
  3.Use RZAlbum. There are many ways to call.
  ```java
  /**
    * @param ofAppName : (required)
    * @param setLimitCount : (choose) (default:5)     
    * @param setSpanCount : (choose) (default:3) 
    * @param setStatusBarColor : (choose) (default:#0a7e07)
    * @param setToolBarColor : (choose)  (default:#259b24)
    * @param setToolBarTitle : (choose)  (default:RZAlbum)
    * @param setDialogIcon : (choose)
    * @param showCamera : (choose)  (default:true)
    * @param start : (required)
    */
    RZAlbum.ofAppName("RZAlbum")
            .start(this, REQUEST_RZALBUM);
    /**
      * Or Like this
      */
    RZAlbum.ofAppName("RZAlbum")
            .setLimitCount(2)
            .setSpanCount(3)
            .setStatusBarColor(Color.parseColor("#AD1457"))
            .setToolBarColor(Color.parseColor("#D81B60"))
            .setToolBarTitle("Album")
            .setDialogIcon(R.drawable.ic_bird_shape_30_3dp)
            .showCamera(false)
            .start(this, REQUEST_RZALBUM);
  ```
  4.Override Activity's/Fragment's onActivityResult method.
  ```java
  @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case RZALBUM_REQUESTCODE:
                    List<String> paths = RZAlbum.parseResult(data);
                    Log.d("RZAlbum", "GetPath:" + paths);
                    break;
            }
        }
    }
  ```
  5.If you want to customize the Dialog title, description, and button name, please overwrite the following names in strings.xml.
  ```xml
  <string name="rz_album_dia_read_description">(Enter something that u want)</string>
  <string name="rz_album_dia_read_message">(Enter something that u want)</string>
  <string name="rz_album_dia_camera_description">(Enter something that u want)</string>
  <string name="rz_album_dia_camera_message">(Enter something that u want)</string>
  <string name="rz_album_dia_ok">(Enter something that u want)</string>
  <string name="rz_album_dia_cancel">(Enter something that u want)</string>
  ```
Notice
====
  Due to support Material Design style and handle the image cache, So the library references the following categories.
  ```xml
  compile 'com.android.support:design:25.3.1'
  compile 'com.android.support:recyclerview-v7:25.3.1'
  // Glide
  compile 'com.github.bumptech.glide:glide:3.7.0'
  ```
License
====
  ```
Copyright 2017 RayZhang

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
  ```
Chinese description
====
[中文說明](https://github.com/ray00178/RayZhangAlbum/blob/master/README_zh.md)
