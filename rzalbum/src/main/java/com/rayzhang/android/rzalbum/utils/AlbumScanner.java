package com.rayzhang.android.rzalbum.utils;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.rayzhang.android.rzalbum.model.AlbumFolder;
import com.rayzhang.android.rzalbum.model.AlbumPhoto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Ray on 2016/12/26.
 * AlbumScanner
 */

public final class AlbumScanner {
    private static final String TAG = AlbumScanner.class.getSimpleName();
    private static AlbumScanner scanner;
    private int pickColor;

    private AlbumScanner(int pickColor) {
        this.pickColor = pickColor;
    }

    public static AlbumScanner instances(int pickColor) {
        if (scanner == null) {
            synchronized (AlbumScanner.class) {
                if (scanner == null) {
                    scanner = new AlbumScanner(pickColor);
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
        // INTERNAL_CONTENT_URI ; EXTERNAL_CONTENT_URI
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

            /**
             * 建立每張照片資訊
             */
            AlbumPhoto photo = new AlbumPhoto();
            photo.setPhotoID(imgID);
            photo.setPhotoPath(imaPath);
            photo.setPhotoName(imaName);
            photo.setPhotoAddTime(addTime);
            photo.setPhotoIndex(photoIndex);
            photo.setPickNumber(0);
            photo.setPickColor(pickColor);
            /**
             * 加入到所有照片的資料夾
             */
            allImageAlbumFolder.addPhoto(photo);
            allImageAlbumFolder.setPickColor(pickColor);

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
                albumFolder.setPickColor(pickColor);
                albumFolderMap.put(bucketName, albumFolder);
            }
            photoIndex += 1;
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
