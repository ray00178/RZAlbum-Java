<p align="center">
  <img src="https://github.com/ray00178/RZAlbum-Java/blob/master/RZAlbum_Logo.png" alt="RZAlbum" width="450" height="450" />
</p>

![](https://github.com/ray00178/RZAlbum-Java/blob/master/rzalbum_platform.svg) ![](https://github.com/ray00178/RZAlbum-Java/blob/master/rzalbum_version.svg) ![](https://github.com/ray00178/RZAlbum-Java/blob/master/rzalbum_license.svg)

The RZAlbum for android to select the photo library. And usage：<br/>
* Support Single choice、Multiple choice、Preview、Folder switch and take pictures.  
* For __6.0 or later__, The permissions have been handled very well，So don't worry about their own.
* According to your project color, Setting ur StatusBarColor、ToolBarColor、PickColor.
* According to your preferences / needs, Show the number of fields and select the number of restrictions.
* In Activity or Frangment, Can support the use.
* For __Android7.0 or later, the camera function through the FileProvider do adaptation processing.__<br/>

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
  1.Androidmanifest.xml, Add the following permissions.
  ```xml
    <uses-permission android:name="android.permission.CAMERA" />
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
  ```
  2.Use RZAlbum. There are many ways to call.
  ```java
  /**
    * @param ofAppName             : (required)
    * @param setLimitCount         : (choose)   (default:5)     
    * @param setSpanCount          : (choose)   (default:3) 
    * @param setStatusBarColor     : (choose)   (default:#ff673ab7)
    * @param setToolBarColor       : (choose)   (default:#ff673ab7)
    * @param setToolBarTitle       : (choose)   (default:RZAlbum)
    * @param setPickColor          : (choose)   (default:#ffffc107)
    * @param setPreviewOrientation : (choose)   (default:ORIENTATION_AUTO)
    * @param setAllFolderName      : (choose)   (default:All Photos)
    * @param setDialogIcon         : (choose)   (default:none)
    * @param showCamera            : (choose)   (default:true)
    * @param showGif               : (choose)   (default:true)
    * @param start                 : (required)
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
            .setPickColor(Color.argb(255, 153, 51, 255))
            .setDialogIcon(R.drawable.ic_bird_shape_30_3dp)
            .setPreviewOrientation(RZAlbum.ORIENTATION_PORTRATI)
            .setAllFolderName("Photos")
            .showCamera(false)
            .showGif(false)
            .start(this, REQUEST_RZALBUM);
  ```
  3.Override Activity's/Fragment's onActivityResult method.
  ```java
  @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case RZALBUM_REQUESTCODE:
                    List<AlbumPhoto> paths = RZAlbum.parseResult(data);
                    Log.d("RZAlbum", "Photos:" + paths);
                    break;
            }
        }
    }
    
    --- You can get many photo information from AlbumPhoto class. like this: ---
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
  4.If you want to customize the Dialog title, description, and button name, please overwrite the following names in strings.xml.
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
  1.Due to support Material Design style and handle the image cache, So the library references the following categories.
  ```xml
  compile 'com.android.support:design:27.1.1'
  compile 'com.android.support:recyclerview-v7:27.1.1'
  // Glide
  compile 'com.github.bumptech.glide:glide:4.7.1'
  annotationProcessor 'com.github.bumptech.glide:compiler:4.7.1'
  ```
  2.If u build project then see error log like this.
  ```xml
  Manifest merger failed : Attribute application@label value=(@string/app_name) from           
  AndroidManifest.xml:21:9-41 is also present at [com.rayzhang.android:rzalbum:1.7.0] AndroidManifest.xml:14:9-44 value=  
  (@string/rz_app_name).Suggestion: add 'tools:replace="android:label"' to <application> element at 
  AndroidManifest.xml:17:5-44:19 to override.
  ```
  You can in ur AndroidMainfest.xml add「tools:replace="android:label"」in application level.
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
     - add setAllFolderName() & showGif() function
     - add AlbumPhoto class
     - optimize performance
     - update【android support library -> 27.1.1】&【Glide -> 4.7.1】
     
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
Chinese description
====
[中文說明](https://github.com/ray00178/RayZhangAlbum/blob/master/README_zh.md)
