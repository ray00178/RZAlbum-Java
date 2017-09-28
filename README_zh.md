RZAlbum ![](http://arminray.ga/image/rzalbum_planform.svg) ![](http://arminray.ga/image/rzalbum_version.svg) ![](http://arminray.ga/image/rzalbum_license.svg)
====
RZAlbum用於Android使用的照片選擇庫，相關功能如下：<br/>
* 可支持單選、複選、預覽、照片文件夾切換及內建拍照  
* 對於__ 6.0以上版本 __，已將權限做很好的處理，故無需擔心要自行處理
* 依照你的專案配色，可自訂StatusBarColor、ToolBarColor
* 可依照你的喜好/需求，顯示欄位數量及選取張數限制
* 無論是在Activity、Frangment，都可支持使用
* 對於__ Android7.0以上，拍照功能透過FileProvider做適配處理 __<br/>

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
  1.在Androidmanifest.xml加入以下程式碼
  ```xml
  <!-- android:theme = 根據你的風格設定
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
  2.在Androidmanifest.xml加入以下權限
  ```xml
    <!-- 相機、讀取儲存 -->
    <uses-permission android:name="android.permission.CAMERA" />
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
  ```
  3.調用RZAlbum，有多種使用方法
  ```java
  /**
    * @param ofAppName : (必要)
    * @param setLimitCount : (選擇性) (預設:5)
    * @param setSpanCount : (選擇性)  (預設:3)
    * @param setStatusBarColor : (選擇性) (預設:#0a7e07)
    * @param setToolBarColor : (選擇性)   (預設:#259b24)
    * @param setToolBarTitle : (選擇性)   (預設:RZAlbum)
    * @param setDialogIcon : (選擇性)
    * @param showCamera : (選擇性)   (預設:true)
    * @param start : (必要)
    */
    RZAlbum.ofAppName("RZAlbum")
            .start(this, REQUEST_RZALBUM);
  /**
    * 或者搭配使用(如下)
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
4.Override Activity/Fragment的onActivityResult方法
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
5.如果想自訂Dialog標題、內容描述及按鈕名稱，請在strings.xml覆蓋下列的名稱，即可
```xml
  <string name="rz_album_dia_read_description">讀取權限允許說明</string>
  <string name="rz_album_dia_read_message">選擇照片 必須要取得您的同意，才可以使用。是否可以允許取得？</string>
  <string name="rz_album_dia_camera_description">拍照權限允許說明</string>
  <string name="rz_album_dia_camera_message">拍攝照片 必須要取得您的同意，才可以使用。是否可以允許取得？</string>
  <string name="rz_album_dia_ok">允許</string>
  <string name="rz_album_dia_cancel">不要</string>
```
Notice
====
  由於支援Material Design的風格及處理圖片的緩存，故該庫引用下列類別庫
  ```xml
  compile 'com.android.support:design:25.3.1'
  compile 'com.android.support:recyclerview-v7:25.3.1'
  // Glide
  compile 'com.github.bumptech.glide:glide:3.7.0'
  ```
License
====
  ```
MIT License

Copyright (c) [2016] [RZAlbum]

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
  ```

