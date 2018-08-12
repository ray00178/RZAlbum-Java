package com.rayzhang.android.rzalbum.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;

import com.rayzhang.android.rzalbum.common.RZConfig;
import com.rayzhang.android.rzalbum.model.AlbumFolder;
import com.rayzhang.android.rzalbum.model.AlbumPhoto;

import java.util.ArrayList;
import java.util.Arrays;
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

    private String[] PROJECTTION_IMAGES = new String[]{
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Images.Media.BUCKET_ID,
            MediaStore.Images.Media.DATE_TAKEN,
            MediaStore.Images.Media.DESCRIPTION,
            MediaStore.Images.Media.IS_PRIVATE,
            MediaStore.Images.Media.LATITUDE,
            MediaStore.Images.Media.LONGITUDE,
            MediaStore.Images.Media.MINI_THUMB_MAGIC,
            MediaStore.Images.Media.ORIENTATION,
            MediaStore.Images.Media.PICASA_ID,
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.DATE_ADDED,
            MediaStore.Images.Media.DATE_MODIFIED,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.HEIGHT,
            MediaStore.Images.Media.MIME_TYPE,
            MediaStore.Images.Media.WIDTH,
            MediaStore.Images.Media.SIZE,
            MediaStore.Images.Media.TITLE,
            MediaStore.Images.Media._ID
    };
    private static AlbumScanner scanner;
    private int pickColor;
    private boolean showGif;

    private AlbumScanner(int pickColor, boolean showGif) {
        this.pickColor = pickColor;
        this.showGif = showGif;
    }

    public static AlbumScanner instances(int pickColor, boolean showGif) {
        if (scanner == null) {
            synchronized (AlbumScanner.class) {
                if (scanner == null) {
                    scanner = new AlbumScanner(pickColor, showGif);
                }
            }
        }
        return scanner;
    }

    public List<AlbumFolder> getPhotoAlbum(@NonNull Context context, String allFolderName) {
        // INTERNAL_CONTENT_URI ; EXTERNAL_CONTENT_URI
        Cursor mCursor = MediaStore.Images.Media.query(
                context.getContentResolver(),
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                PROJECTTION_IMAGES, null, MediaStore.Images.ImageColumns._ID);
        if (mCursor == null) return new ArrayList<>();

        Map<String, AlbumFolder> albumFolderMap = new HashMap<>();
        AlbumFolder allAlbumFolder = new AlbumFolder();
        allAlbumFolder.setFolderName(allFolderName);
        allAlbumFolder.setPickColor(pickColor);
        allAlbumFolder.setCheck(true);

        while (mCursor.moveToNext()) {
            String imgBucketDisplayName = mCursor.getString(0);
            int imgBucketId = mCursor.getInt(1);
            int imgDateToken = mCursor.getInt(2);
            String imgDescription = mCursor.getString(3) == null ? "" : mCursor.getString(3);
            int imgIsPrivate = mCursor.getInt(4);
            double imgLat = mCursor.getDouble(5);
            double imgLng = mCursor.getDouble(6);
            int imgMiniThumbMagic = mCursor.getInt(7);
            int imgOrientation = mCursor.getInt(8);
            String imgPicasaId = mCursor.getString(9);
            String imgData = mCursor.getString(10);
            long imgDateAdded = mCursor.getLong(11);
            long imgDateModified = mCursor.getLong(12);
            String imgDisplayName = mCursor.getString(13);
            int imgHeight = mCursor.getInt(14);
            String imgMimeType = mCursor.getString(15);
            int imgWidth = mCursor.getInt(16);
            long imgSize = mCursor.getLong(17);
            String imgTitle = mCursor.getString(18);
            int imgId = mCursor.getInt(19);
            /*
                imgBucketDisplayName --> Download
                imgBucketId          --> 540528482
                imgDateToken         --> 1482082104
                imgDescription       --> null
                imgIsPrivate         --> 0
                imgLat               --> 0.0
                imgLon               --> 0.0
                imgMiniThumbMagic    --> 0
                imgOrientation       --> 0
                imgPicasaId          --> null
                imgData              --> /storage/emulated/0/Download/mobile01_20171026_6.jpg
                imgDateAdded         --> 1530342373
                imgDateModified      --> 1509015603
                imgDisplayName       --> mobile01_20171026_6.jpg
                imgHeight            --> 480
                imgMimeType          --> image/jpeg
                imgWidth             --> 720
                imgSize              --> 236972
                imgTitle             --> mobile01_20171026_6
                imgId                --> 65
            */
            // 建立每張照片資訊
            AlbumPhoto photo = new AlbumPhoto(imgBucketDisplayName, imgId, imgDescription, imgLat, imgLng,
                    imgOrientation, imgDateAdded, imgDateModified, imgDisplayName,
                    imgWidth, imgHeight, imgSize, imgMimeType, imgIsPrivate == 1,
                    imgData, false, 0, pickColor);
            // 加入到所有照片的資料夾
            // 是否要顯示.gif
            if (imgMimeType.equals(RZConfig.GIF) && !showGif) continue;
            allAlbumFolder.getFolderPhotos().add(photo);

            // 取得手機中各別相簿的資料夾
            AlbumFolder albumFolder = albumFolderMap.get(imgBucketDisplayName);
            if (albumFolder == null) {
                albumFolder = new AlbumFolder();
                albumFolder.setFolderId(imgBucketId);
                albumFolder.setFolderName(imgBucketDisplayName);
                albumFolder.getFolderPhotos().add(photo);
                albumFolder.setPickColor(pickColor);
                albumFolderMap.put(imgBucketDisplayName, albumFolder);
            } else {
                albumFolder.getFolderPhotos().add(photo);
            }
        }

        mCursor.close();
        List<AlbumFolder> list = new ArrayList<>();
        list.add(allAlbumFolder);

        // 依照資料夾將照片做分類
        // 每張photo都是同1個物件，不會因為資料夾不同，而不同
        for (Map.Entry<String, AlbumFolder> folderEntry : albumFolderMap.entrySet()) {
            AlbumFolder albumFolder = folderEntry.getValue();
            list.add(albumFolder);
        }
        return list;
    }

    public AlbumPhoto getSinglePhoto(@NonNull Context context, @NonNull Uri uri) {
        Cursor mCursor = MediaStore.Images.Media.query(
                context.getContentResolver(),
                uri, PROJECTTION_IMAGES, null, MediaStore.Images.ImageColumns._ID);
        if (mCursor == null) return null;

        AlbumPhoto photo = null;
        while (mCursor.moveToNext()) {
            String imgBucketDisplayName = mCursor.getString(0);
            int imgBucketId = mCursor.getInt(1);
            int imgDateToken = mCursor.getInt(2);
            String imgDescription = mCursor.getString(3) == null ? "" : mCursor.getString(3);
            int imgIsPrivate = mCursor.getInt(4);
            double imgLat = mCursor.getDouble(5);
            double imgLng = mCursor.getDouble(6);
            int imgMiniThumbMagic = mCursor.getInt(7);
            int imgOrientation = mCursor.getInt(8);
            String imgPicasaId = mCursor.getString(9);
            String imgData = mCursor.getString(10);
            long imgDateAdded = mCursor.getLong(11);
            long imgDateModified = mCursor.getLong(12);
            String imgDisplayName = mCursor.getString(13);
            int imgHeight = mCursor.getInt(14);
            String imgMimeType = mCursor.getString(15);
            int imgWidth = mCursor.getInt(16);
            long imgSize = mCursor.getLong(17);
            String imgTitle = mCursor.getString(18);
            int imgId = mCursor.getInt(19);

            photo = new AlbumPhoto(imgBucketDisplayName, imgId, imgDescription, imgLat, imgLng,
                    imgOrientation, imgDateAdded, imgDateModified, imgDisplayName,
                    imgWidth, imgHeight, imgSize, imgMimeType, imgIsPrivate == 1,
                    imgData, false, 0, pickColor);
        }
        mCursor.close();
        return photo;
    }
}
