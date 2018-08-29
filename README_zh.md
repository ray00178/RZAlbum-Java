<p align="center">
  <img src="https://github.com/ray00178/RZAlbum-Java/blob/master/RZAlbum_Logo.png" alt="RZAlbum" width="450" height="450" />
</p>

![](https://github.com/ray00178/RZAlbum-Java/blob/master/rzalbum_platform.svg) ![](https://github.com/ray00178/RZAlbum-Java/blob/master/rzalbum_version.svg) ![](https://github.com/ray00178/RZAlbum-Java/blob/master/rzalbum_license.svg)

RZAlbum為Android而生的照片選擇庫，相關功能如下：<br/>
* 可支持單選、複選、預覽、照片文件夾切換及內建拍照  
* 對於6.0以上版本，已將權限做很好的處理，故無需擔心要自行處理
* 依照你的專案配色，可自訂StatusBarColor、ToolBarColor、PickColor
* 可依照你的喜好/需求，顯示欄位數量及選取張數限制
* 無論是在Activity、Frangment，都可支持使用
* 對於Android7.0以上，拍照功能透過FileProvider做適配處理<br/>

Screenshots <br/><br/>
![](https://github.com/ray00178/RZAlbum-Java/blob/master/screenshots.png)
Gradle
====
```java
compile 'com.rayzhang.android:rzalbum:1.7.0'
```
Maven
====
```java
<dependency>
  <groupId>com.rayzhang.android</groupId>
  <artifactId>rzalbum</artifactId>
  <version>1.7.0</version>
  <type>pom</type>
</dependency>
```
Usage
====
  1.在Androidmanifest.xml加入以下權限
  ```xml
    <!-- 相機、讀取儲存 -->
    <uses-permission android:name="android.permission.CAMERA" />
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
  ```
  2.調用RZAlbum，有多種使用方法
  ```java
  /**
    * @param ofAppName             : (必要)
    * @param setLimitCount         : (選擇性) (預設:5)
    * @param setSpanCount          : (選擇性) (預設:3)
    * @param setStatusBarColor     : (選擇性) (預設:#ff673ab7)
    * @param setToolBarColor       : (選擇性) (預設:#ff673ab7)
    * @param setToolBarTitle       : (選擇性) (預設:RZAlbum)
     * @param setPickColor          : (選擇性) (預設:#ffffc107)
     * @param setPreviewOrientation : (選擇性) (預設:ORIENTATION_AUTO)
     * @param setAllFolderName      : (選擇性) (預設:All Photos)
     * @param setDialogIcon         : (選擇性)
    * @param showCamera            : (選擇性) (預設:true)
     * @param showGif               : (選擇性) (預設:true)
    * @param start                 : (必要)
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
            .setPickColor(Color.argb(255, 153, 51, 255))
            .setDialogIcon(R.drawable.ic_bird_shape_30_3dp)
            .setPreviewOrientation(RZAlbum.ORIENTATION_PORTRATI)
            .setAllFolderName("Photos")
            .showCamera(false)
            .showGif(false)
            .start(this, REQUEST_RZALBUM);
```
3.Override Activity/Fragment的onActivityResult方法
```java
  @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case RZALBUM_REQUESTCODE:
                    List<AlbumPhoto> paths = RZAlbum.parseResult(data);
                    Log.d("RZAlbum", "GetPath:" + paths);
                    break;
            }
        }
    }
    
    --- AlbumPhoto class 你可以從這個class取得更多照片的相關資訊 如下: ---
        bucketName        --> Download
        photoId           --> 65
        photoDesc         --> null
        photoLat          --> 0.0
        photoLng          --> 0.0
        photoOrientation  --> 0
        photoDateAdded    --> 1530342373
        photoDateModified --> 1509015603
        photoName         --> mobile01_20171026_6.jpg
        photoWidth        --> 720
        photoHeight       --> 480
        photoSize         --> 236972
        photoMimeType     --> image/jpeg
        photoIsPrivate    --> false
        photoPath         --> /storage/emulated/0/Download/mobile01_20171026_6.jpg
```
4.如果想自訂Dialog標題、內容描述及按鈕名稱，請在strings.xml覆蓋下列的名稱，即可
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
  compile 'com.android.support:design:27.1.1'
  compile 'com.android.support:recyclerview-v7:27.1.1'
  // Glide
  compile 'com.github.bumptech.glide:glide:4.7.1'
  annotationProcessor 'com.github.bumptech.glide:compiler:4.7.1'
  ```
  2.如果在build專案時出現下列類似錯誤的訊息
  ```xml
  Manifest merger failed : Attribute application@label value=(@string/app_name) from           
  AndroidManifest.xml:21:9-41 is also present at [com.rayzhang.android:rzalbum:1.7.0] AndroidManifest.xml:14:9-44 value=  
  (@string/rz_app_name).Suggestion: add 'tools:replace="android:label"' to <application> element at 
  AndroidManifest.xml:17:5-44:19 to override.
  ```
  您可以在AndroidMainfest.xml裏，application標籤裡添加「tools:replace="android:label"」即可解決該錯誤訊息
  ```xml
  <application
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:supportsRtl="true"
        android:theme="@style/AppTheme"
        tools:replace="android:label"> --> Here
        <activity android:name=".MainActivity">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />

                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
  </application>
  ```
Update Log
====
- **2018-08-12 Version 1.7.0**
     - 新增 setAllFolderName() & showGif() 方法。
     - 新增 AlbumPhoto class
     - 優化一些class & 整體效能
     - 更新【android support library -> 27.1.1】&【Glide -> 4.7.1】
     
License
====
  ```
MIT License

Copyright (c) 2016 RZAlbum of copyright Ray

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

