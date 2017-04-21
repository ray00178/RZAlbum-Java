RZAlbum
====
The RZAlbum for android to select the photo library. And usage：<br/>
* Support Single choice、Multiple choice、Preview、Folder switch and take pictures.  
* For __6.0 or later__, The permissions have been handled very well，So don't worry about their own.
* According to your project color, Setting ur StatusBarColor、ToolBarColor.
* According to your preferences / needs, Show the number of fields and select the number of restrictions.
* In Activity or Frangment, Can support the use.<br/>

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
  1.Androidmanifest.xml, Add the following code, label is title names.
  ```xml
  <activity
      android:name="com.rayzhang.android.rzalbum.RZAlbumActivity"
      android:configChanges="orientation|keyboardHidden|screenSize"
      android:label="RZ照片"
      android:screenOrientation="portrait"
      android:theme="@style/Theme.AppCompat.DayNight.NoActionBar"
      android:windowSoftInputMode="stateAlwaysHidden|stateHidden" />
  ```
  2.Androidmanifest.xml, Add the following permissions.
  ```xml
    <!-- 相機、讀取儲存 -->
    <uses-permission android:name="android.permission.CAMERA" />
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
  ```
  3.Use RZAlbum. There are five ways to call.
  ```java
  /**
    * @param activity    
    * @param requestCode
    */
    RZAlbum.startAlbum(this, RZALBUM_REQUESTCODE);
  
  /**
    * @param activity    
    * @param requestCode 
    * @param limitCount
    */
    RZAlbum.startAlbum(this, RZALBUM_REQUESTCODE, 5);
  
  /**
    * @param activity    
    * @param requestCode 
    * @param limitCount  
    * @param spanCount  
    */
    RZAlbum.startAlbum(this, RZALBUM_REQUESTCODE, 5, 3);
    
  /**
    * @param activity     
    * @param requestCode  
    * @param limitCount   
    * @param spanCount    
    * @param toolbarTitle 
    */
    RZAlbum.startAlbum(this, RZALBUM_REQUESTCODE, 5, 3, "RZAlbum");
    
  /**
    * @param activity       
    * @param requestCode   
    * @param limitCount     
    * @param spanCount      
    * @param toolbarTitle   
    * @param toolbarColor   
    * @param statusBarColor 
    */
    RZAlbum.startAlbum(this, RZALBUM_REQUESTCODE, 5, 3, "RZAlbum", Color.parseColor("#e91e63"), Color.parseColor("#c2185b"));
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
Notice
====
  Due to support Material Design style and handle the image cache, So the library references the following categories.
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
