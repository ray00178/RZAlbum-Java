RZAlbum
====
RZAlbum用於Android使用的照片選擇庫，相關功能如下：<br/>
* 可支持單選、複選、預覽、照片文件夾切換及內建拍照  
* 對於__ 6.0以上版本 __，已將權限做很好的處理，故無需擔心要自行處理
* 依照你的專案配色，可自訂StatusBarColor、ToolBarColor
* 可依照你的喜好/需求，顯示欄位數量及選取張數限制
* 無論是在Activity、Frangment，都可支持使用
* 對於__ Android7.0以上，拍照功能透過FileProvider做適配處理 __<br/>

Screenshots 
====
<img src="https://github.com/ray00178/RayZhangAlbum/blob/master/Screenshot_1.jpg" alt="Demo_1" title="Demo_1" width="300" height="500" /><br/>
<img src="https://github.com/ray00178/RayZhangAlbum/blob/master/Screenshot_2.jpg" alt="Demo_2" title="Demo_2" width="300" height="500" /><br/>
<img src="https://github.com/ray00178/RayZhangAlbum/blob/master/Screenshot_3.gif" alt="Demo_gif" title="Demo_gif" width="300" height="500" /><br/>
Gradle
====
```java
compile 'com.rayzhang.android:rzalbum:1.0.5'
```
Maven
====
```java
<dependency>
  <groupId>com.rayzhang.android</groupId>
  <artifactId>rzalbum</artifactId>
  <version>1.0.5</version>
  <type>pom</type>
</dependency>
```
Usage
====
  1.在Androidmanifest.xml加入以下程式碼，其中label為自訂義標題名稱
  ```xml
  <activity
      android:name="com.rayzhang.android.rzalbum.RZAlbumActivity"
      android:configChanges="orientation|keyboardHidden|screenSize"
      android:screenOrientation="portrait"
      android:theme="@style/Theme.AppCompat.DayNight.NoActionBar"
      android:windowSoftInputMode="stateAlwaysHidden|stateHidden" />
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
   * @param ofLimitCount : (必要)
   * @param ofSpanCount : (選擇性)
    * @param withStatusBarColor : (選擇性)
    * @param withToolBarColor : (選擇性)
    * @param withToolBarTitle : (選擇性)
    * @param start : (必要)
    */
    RZAlbum.ofLimitCount(2)
            .ofSpanCount(3)
            .withStatusBarColor(Color.parseColor("#AD1457"))
            .withToolBarColor(Color.parseColor("#D81B60"))
            .withToolBarTitle("Album")
            .start(this, REQUEST_RZALBUM);
  /**
    * 或者簡單使用(如下)
    */
    RZAlbum.ofLimitCount(2)
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
  <string name="rz_album_dia_read_message">此功能(選擇照片)必須要取得您的同意，才可以使用。是否可以允許取得？</string>
  <string name="rz_album_dia_camera_description">拍照權限允許說明</string>
  <string name="rz_album_dia_camera_message">此功能(拍攝照片)必須要取得您的同意，才可以使用。是否可以允許取得？</string>
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

