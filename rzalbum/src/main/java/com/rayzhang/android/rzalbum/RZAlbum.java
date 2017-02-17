package com.rayzhang.android.rzalbum;

/**
 * Created by Ray on 2017/2/10.
 */

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.ColorInt;
import android.support.v4.app.Fragment;

import java.util.Collections;
import java.util.List;

/**
 * 調用入口類
 */
public class RZAlbum {
    public final static String ALBUM_LIMIT_COUNT = "ALBUM_LIMIT_COUNT";
    public final static String ALBUM_SPAN_COUNT = "ALBUM_SPAN_COUNT";
    public final static String ALBUM_TOOLBAR_TITLE = "ALBUM_TOOLBAR_TITLE";
    public final static String ALBUM_TOOLBAR_COLOR = "ALBUM_TOOLBAR_COLOR";
    public final static String ALBUM_STATUS_COLOR = "ALBUM_STATUS_COLOR";
    public final static String ALBUM_IMAGE_PATH_LIST = "ALBUM_IMAGE_PATH_LIST";

    /**
     * 最多選擇30張
     */
    public final static int MAXCOUNT = 30;

    /**
     * @param activity    接受文件的Activity。
     * @param requestCode 請求碼。
     */
    public static void startAlbum(Activity activity, int requestCode) {
        Intent intent = new Intent(activity, RZAlbumActivity.class);
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * @param activity    接受文件的Activity。
     * @param requestCode 請求碼。
     * @param limitCount  選擇張數限制。
     */
    public static void startAlbum(Activity activity, int requestCode, int limitCount) {
        Intent intent = new Intent(activity, RZAlbumActivity.class);
        intent.putExtra(ALBUM_LIMIT_COUNT, limitCount);
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * @param activity    接受文件的Activity。
     * @param requestCode 請求碼。
     * @param limitCount  選擇張數限制。
     * @param spanCount   顯示幾欄。
     */
    public static void startAlbum(Activity activity, int requestCode, int limitCount, int spanCount) {
        Intent intent = new Intent(activity, RZAlbumActivity.class);
        if (limitCount > MAXCOUNT) {
            limitCount = MAXCOUNT;
        }
        intent.putExtra(ALBUM_LIMIT_COUNT, limitCount);
        intent.putExtra(ALBUM_SPAN_COUNT, spanCount);
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * @param activity     接受文件的Activity。
     * @param requestCode  請求碼。
     * @param limitCount   選擇張數限制。
     * @param spanCount    顯示幾欄。
     * @param toolbarTitle Toolbar 文字。
     */
    public static void startAlbum(Activity activity, int requestCode, int limitCount, int spanCount, String toolbarTitle) {
        Intent intent = new Intent(activity, RZAlbumActivity.class);
        if (limitCount > MAXCOUNT) {
            limitCount = MAXCOUNT;
        }
        intent.putExtra(ALBUM_LIMIT_COUNT, limitCount);
        intent.putExtra(ALBUM_SPAN_COUNT, spanCount);
        intent.putExtra(ALBUM_TOOLBAR_TITLE, toolbarTitle);
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * @param activity       接受文件的Activity。
     * @param requestCode    請求碼。
     * @param limitCount     選擇張數限制。
     * @param spanCount      顯示幾欄。
     * @param toolbarTitle   Toolbar 文字。
     * @param toolbarColor   Toolbar 颜色。
     * @param statusBarColor statusBar 颜色。
     */
    public static void startAlbum(Activity activity, int requestCode, int limitCount, int spanCount,
                                  String toolbarTitle, @ColorInt int toolbarColor, @ColorInt int statusBarColor) {
        if (limitCount > MAXCOUNT) {
            limitCount = MAXCOUNT;
        }
        Intent intent = new Intent(activity, RZAlbumActivity.class);
        intent.putExtra(ALBUM_LIMIT_COUNT, limitCount);
        intent.putExtra(ALBUM_SPAN_COUNT, spanCount);
        intent.putExtra(ALBUM_TOOLBAR_TITLE, toolbarTitle);
        intent.putExtra(ALBUM_TOOLBAR_COLOR, toolbarColor);
        intent.putExtra(ALBUM_STATUS_COLOR, statusBarColor);
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * @param fragment    接受文件的Fragment。
     * @param requestCode 請求碼。
     */
    public static void startAlbum(Fragment fragment, int requestCode) {
        Intent intent = new Intent(fragment.getContext(), RZAlbumActivity.class);
        fragment.startActivityForResult(intent, requestCode);
    }

    /**
     * @param fragment    接受文件的Fragment。
     * @param requestCode 請求碼。
     * @param limitCount  選擇張數限制。
     */
    public static void startAlbum(Fragment fragment, int requestCode, int limitCount) {
        Intent intent = new Intent(fragment.getContext(), RZAlbumActivity.class);
        if (limitCount > MAXCOUNT) {
            limitCount = MAXCOUNT;
        }
        intent.putExtra(ALBUM_LIMIT_COUNT, limitCount);
        fragment.startActivityForResult(intent, requestCode);
    }

    /**
     * @param fragment    接受文件的Fragment。
     * @param requestCode 請求碼。
     * @param limitCount  選擇張數限制。
     * @param spanCount   顯示幾欄。
     */
    public static void startAlbum(Fragment fragment, int requestCode, int limitCount, int spanCount) {
        Intent intent = new Intent(fragment.getContext(), RZAlbumActivity.class);
        if (limitCount > MAXCOUNT) {
            limitCount = MAXCOUNT;
        }
        intent.putExtra(ALBUM_LIMIT_COUNT, limitCount);
        intent.putExtra(ALBUM_SPAN_COUNT, spanCount);
        fragment.startActivityForResult(intent, requestCode);
    }

    /**
     * @param fragment     接受文件的Fragment。
     * @param requestCode  請求碼。
     * @param limitCount   選擇張數限制。
     * @param spanCount    顯示幾欄。
     * @param toolbarTitle Toolbar 文字。
     */
    public static void startAlbum(Fragment fragment, int requestCode, int limitCount, int spanCount,
                                  String toolbarTitle) {
        Intent intent = new Intent(fragment.getContext(), RZAlbumActivity.class);
        if (limitCount > MAXCOUNT) {
            limitCount = MAXCOUNT;
        }
        intent.putExtra(ALBUM_LIMIT_COUNT, limitCount);
        intent.putExtra(ALBUM_SPAN_COUNT, spanCount);
        intent.putExtra(ALBUM_TOOLBAR_TITLE, toolbarTitle);
        fragment.startActivityForResult(intent, requestCode);
    }

    /**
     * @param fragment       接受文件的Fragment。
     * @param requestCode    請求碼。
     * @param limitCount     選擇張數限制。
     * @param spanCount      顯示幾欄。
     * @param toolbarTitle   Toolbar 文字。
     * @param toolbarColor   Toolbar 颜色。
     * @param statusBarColor statusBar 颜色。
     */
    public static void startAlbum(Fragment fragment, int requestCode, int limitCount, int spanCount,
                                  String toolbarTitle, @ColorInt int toolbarColor, @ColorInt int statusBarColor) {
        if (limitCount > MAXCOUNT) {
            limitCount = MAXCOUNT;
        }
        Intent intent = new Intent(fragment.getContext(), RZAlbumActivity.class);
        intent.putExtra(ALBUM_LIMIT_COUNT, limitCount);
        intent.putExtra(ALBUM_SPAN_COUNT, spanCount);
        intent.putExtra(ALBUM_TOOLBAR_TITLE, toolbarTitle);
        intent.putExtra(ALBUM_TOOLBAR_COLOR, toolbarColor);
        intent.putExtra(ALBUM_STATUS_COLOR, statusBarColor);
        fragment.startActivityForResult(intent, requestCode);
    }

    /**
     * 取得照片路徑
     *
     * @param intent
     * @return
     */
    public static List<String> parseResult(Intent intent) {
        List<String> pathList = intent.getStringArrayListExtra(ALBUM_IMAGE_PATH_LIST);
        if (pathList == null)
            pathList = Collections.emptyList();
        return pathList;
    }
}
