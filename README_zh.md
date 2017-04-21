RZAlbum
====
RZAlbum用於Android使用的照片選擇庫，相關功能如下：<br/>
* 可支持單選、複選、預覽、照片文件夾切換及內建拍照  
* 對於__6.0以上版本__，已將權限做很好的處理，故無需擔心要自行處理
* 依照你的專案配色，可自訂StatusBarColor、ToolBarColor
* 可依照你的喜好/需求，顯示欄位數量及選取張數限制
* 無論是在Activity、Frangment，都可支持使用<br/>

Screenshots 
====
<img src="https://github.com/ray00178/RayZhangAlbum/blob/master/app/src/main/res/drawable/Demo_1.jpg" alt="Demo_1" title="Demo_1" width="300" height="500" /><br/>
<img src="https://github.com/ray00178/RayZhangAlbum/blob/master/app/src/main/res/drawable/Demo_2.jpg" alt="Demo_2" title="Demo_2" width="300" height="500" /><br/>
<img src="https://github.com/ray00178/RayZhangAlbum/blob/master/app/src/main/res/drawable/Demo_gif.gif" alt="Demo_gif" title="Demo_gif" width="300" height="500" /><br/>
Gradle
====
```java
compile 'com.rayzhang.android:rzalbum:1.0.1'
```
Maven
====
```java
<dependency>
  <groupId>com.rayzhang.android</groupId>
  <artifactId>rzalbum</artifactId>
  <version>1.0.1</version>
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
      android:label="RZ照片"
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
  3.調用RZAlbum，有5種調用方法
  ```java
  /**
    * @param activity    接受文件的Activity。
    * @param requestCode 請求碼。
    */
    RZAlbum.startAlbum(this, RZALBUM_REQUESTCODE);
  
  /**
    * @param activity    接受文件的Activity。
    * @param requestCode 請求碼。
    * @param limitCount  選擇張數限制。
    */
    RZAlbum.startAlbum(this, RZALBUM_REQUESTCODE, 5);
  
  /**
    * @param activity    接受文件的Activity。
    * @param requestCode 請求碼。
    * @param limitCount  選擇張數限制。
    * @param spanCount   顯示幾欄。
    */
    RZAlbum.startAlbum(this, RZALBUM_REQUESTCODE, 5, 3);
    
  /**
    * @param activity     接受文件的Activity。
    * @param requestCode  請求碼。
    * @param limitCount   選擇張數限制。
    * @param spanCount    顯示幾欄。
    * @param toolbarTitle Toolbar 文字。
    */
    RZAlbum.startAlbum(this, RZALBUM_REQUESTCODE, 5, 3, "RZAlbum");
    
  /**
    * @param activity       接受文件的Activity。
    * @param requestCode    請求碼。
    * @param limitCount     選擇張數限制。
    * @param spanCount      顯示幾欄。
    * @param toolbarTitle   Toolbar 文字。
    * @param toolbarColor   Toolbar 颜色。
    * @param statusBarColor statusBar 颜色。
    */
    RZAlbum.startAlbum(this, RZALBUM_REQUESTCODE, 5, 3, "RZAlbum", Color.parseColor("#e91e63"), Color.parseColor("#c2185b"));
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
Notice
====
  由於支援Material Design的風格及處理圖片的緩存，故該庫引用下列類別庫
  ```xml
  compile 'com.android.support:design:25.1.1'
  compile 'com.android.support:recyclerview-v7:25.1.1'
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

