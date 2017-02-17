package com.rayzhang.android.rzalbum.utils;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.rayzhang.android.rzalbum.module.bean.AlbumFolder;
import com.rayzhang.android.rzalbum.module.bean.AlbumPhoto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Ray on 2016/12/26.
 */

public class AlbumScanner {
    private final String TAG = "AlbumScanner";
    private static AlbumScanner scanner;

    private AlbumScanner() {
    }

    public static AlbumScanner instances() {
        if (scanner == null) {
            synchronized (AlbumScanner.class) {
                if (scanner == null) {
                    scanner = new AlbumScanner();
                }
            }
        }
        return scanner;
    }

    /**
     * 設置獲取圖片的屬性
     */
    private static final String[] STORE_IMAGES = {
            /**
             * 圖片ID。
            */
            MediaStore.Images.Media._ID,
            /**
             * 圖片完整路径。
            */
            MediaStore.Images.Media.DATA,
            /**
             * 文件名稱。
            */
            MediaStore.Images.Media.DISPLAY_NAME,
            /**
             * 被添加到相簿的時間。
            */
            MediaStore.Images.Media.DATE_ADDED,
            /**
             * 目錄ID。
            */
            MediaStore.Images.Media.BUCKET_ID,
            /**
             * 所在文件夾名稱。
            */
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME
    };

    public List<AlbumFolder> getPhotoAlbum(Context context) {
        Cursor mCursor = MediaStore.Images.Media.query(context.getContentResolver(),
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, STORE_IMAGES);

        Map<String, AlbumFolder> albumFolderMap = new HashMap<>();
        /**
         * 所有照片
         */
        AlbumFolder allImageAlbumFolder = new AlbumFolder();
        /**
         * 預設是選中
         */
        allImageAlbumFolder.setCheck(true);
        /**
         * 相簿資料夾名稱
         */
        allImageAlbumFolder.setFolderName("所有照片");

        for (String str : mCursor.getColumnNames()) {
            //Log.d(TAG, "ColumnNames:" + str);
        }
        if (mCursor == null) {
            return null;
        }
        int photoIndex = 0;
        while (mCursor.moveToNext()) {
            int imgID = mCursor.getInt(0);
            String imaPath = mCursor.getString(1);
            String imaName = mCursor.getString(2);
            long addTime = mCursor.getLong(3);
            int bucketID = mCursor.getInt(4);
            String bucketName = mCursor.getString(5);
//            Log.d(TAG, String.format("ID:%d path:%s name:%s addTime:%d bucketID:%d bucketName:%s",
//                    imgID, imaPath, imaName, addTime, bucketID, bucketName));
            /**
             * 建立每張照片資訊
             */
            AlbumPhoto photo = new AlbumPhoto();
            photo.setPhotoID(imgID);
            photo.setPhotoPath(imaPath);
            photo.setPhotoName(imaName);
            photo.setPhotoAddTime(addTime);
            photo.setPhotoIndex(photoIndex);
            /**
             * 加入到所有照片的資料夾
             */
            allImageAlbumFolder.addPhoto(photo);

            /**
             * 取得手機中各別相簿資料夾
             */
            AlbumFolder albumFolder = albumFolderMap.get(bucketName);
            if (albumFolder != null) {
                albumFolder.addPhoto(photo);
            } else {
                albumFolder = new AlbumFolder();
                albumFolder.setFolderID(bucketID);
                albumFolder.setFolderName(bucketName);
                albumFolder.addPhoto(photo);

                albumFolderMap.put(bucketName, albumFolder);
            }
            photoIndex++;
        }
        mCursor.close();
        List<AlbumFolder> list = new ArrayList<>();
        Collections.sort(allImageAlbumFolder.getFolderPhotos());
        list.add(allImageAlbumFolder);
        /**
         * 依照資料夾將照片做分類
         * 每張photo都是同1個物件，不會因為資料夾不同，而不同
         */
        for (Map.Entry<String, AlbumFolder> folderEntry : albumFolderMap.entrySet()) {
            AlbumFolder albumFolder = folderEntry.getValue();
            Collections.sort(albumFolder.getFolderPhotos());
            list.add(albumFolder);
        }
        return list;
    }
}
