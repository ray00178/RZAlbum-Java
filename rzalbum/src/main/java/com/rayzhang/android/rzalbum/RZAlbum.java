package com.rayzhang.android.rzalbum;

/*
 * Created by Ray on 2017/2/10.
 * RZAlbum
 * 調用入口類
 */

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import java.util.Collections;
import java.util.List;

public class RZAlbum {
    private static final String TAG = RZAlbum.class.getSimpleName();
    static final String ALBUM_APP_NAME = "ALBUM_APP_NAME";
    static final String ALBUM_LIMIT_COUNT = "ALBUM_LIMIT_COUNT";
    static final String ALBUM_SPAN_COUNT = "ALBUM_SPAN_COUNT";
    static final String ALBUM_STATUSBAR_COLOR = "ALBUM_STATUSBAR_COLOR";
    static final String ALBUM_TOOLBAR_TITLE = "ALBUM_TOOLBAR_TITLE";
    static final String ALBUM_TOOLBAR_COLOR = "ALBUM_TOOLBAR_COLOR";
    static final String ALBUM_PICK_COLOR = "ALBUM_PICK_COLOR";
    static final String ALBUM_DIALOG_ICON = "ALBUM_DIALOG_ICON";
    static final String ALBUN_SHOW_CAMERA = "ALBUN_SHOW_CAMERA";
    static final String ALBUM_PREVIEW_ORIENTATION = "ALBUM_PREVIEW_ORIENTATION";
    static final String ALBUM_IMAGE_PATH_LIST = "ALBUM_IMAGE_PATH_LIST";

    private static final int DEFAULT_LIMIT_COUNT = 5;
    private static final int DEFAULT_SPAN_COUNT = 3;
    public static final int ORIENTATION_LANDSCAPE = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
    public static final int ORIENTATION_PORTRATI = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
    public static final int ORIENTATION_AUTO = 99999;
    private Intent rzIntent;
    private Bundle rzBundle;


    /*
     * Init
     *
     * @param yourAppName app_name
     * @return RZAlbum
     */
    public static RZAlbum ofAppName(@NonNull String yourAppName) {
        return new RZAlbum(yourAppName);
    }

    private RZAlbum(String yourAppName) {
        rzBundle = new Bundle();
        rzBundle.putString(ALBUM_APP_NAME, yourAppName);
    }

    /*
     * Init
     *
     * @param limitCount limit Count
     * @return RZAlbum
     */
    public RZAlbum setLimitCount(int limitCount) {
        rzBundle.putInt(ALBUM_LIMIT_COUNT, limitCount <= 0 ? DEFAULT_LIMIT_COUNT : limitCount);
        return this;
    }

    /*
     * set Adapter spanCount
     *
     * @param spanCount span Count
     * @return RZAlbum
     */
    public RZAlbum setSpanCount(int spanCount) {
        rzBundle.putInt(ALBUM_SPAN_COUNT, spanCount <= 0 ? DEFAULT_SPAN_COUNT : spanCount);
        return this;
    }

    /*
     * set statusBar color
     *
     * @param statusBarColor statusBar color
     * @return RZAlbum
     */
    public RZAlbum setStatusBarColor(@ColorInt int statusBarColor) {
        rzBundle.putInt(ALBUM_STATUSBAR_COLOR, statusBarColor);
        return this;
    }

    /*
     * set toolBar color
     *
     * @param toolBarColor toolBar color
     * @return RZAlbum
     */
    public RZAlbum setToolBarColor(@ColorInt int toolBarColor) {
        rzBundle.putInt(ALBUM_TOOLBAR_COLOR, toolBarColor);
        return this;
    }

    /*
     * set ToolBar Title
     *
     * @param toolBarTitle title
     * @return RZAlbum
     */
    public RZAlbum setToolBarTitle(String toolBarTitle) {
        rzBundle.putString(ALBUM_TOOLBAR_TITLE, toolBarTitle);
        return this;
    }

    /*
     * set pick color
     *
     * @param pickColor pick color
     * @return RZAlbum
     */
    public RZAlbum setPickColor(@ColorInt int pickColor) {
        rzBundle.putInt(ALBUM_PICK_COLOR, pickColor);
        return this;
    }

    /*
     * set Dialog icon
     *
     * @param resID icon resource
     * @return RZAlbum
     */
    public RZAlbum setDialogIcon(@DrawableRes int resID) {
        rzBundle.putInt(ALBUM_DIALOG_ICON, resID);
        return this;
    }

    /*
     * show Camera icon
     *
     * @param isShow isShow
     * @return RZAlbum
     */
    public RZAlbum showCamera(boolean isShow) {
        rzBundle.putBoolean(ALBUN_SHOW_CAMERA, isShow);
        return this;
    }

    /*
     * set Preview Orientation
     *
     * @param orientation orientation
     * @return RZAlbum
     */
    public RZAlbum setPreviewOrientation(int orientation) {
        if (orientation < 0 || orientation > 1) {
            rzBundle.putInt(ALBUM_PREVIEW_ORIENTATION, ORIENTATION_AUTO);
        } else {
            rzBundle.putInt(ALBUM_PREVIEW_ORIENTATION, orientation);
        }
        return this;
    }

    public void start(@NonNull Activity activity, int requestCode) {
        rzIntent = new Intent(activity, RZAlbumActivity.class);
        rzIntent.putExtras(rzBundle);
        activity.startActivityForResult(rzIntent, requestCode);
    }

    public void start(@NonNull android.app.Fragment fragment, int requestCode) {
        rzIntent = new Intent(fragment.getActivity(), RZAlbumActivity.class);
        rzIntent.putExtras(rzBundle);
        fragment.startActivityForResult(rzIntent, requestCode);
    }

    public void start(@NonNull Fragment fragment, int requestCode) {
        rzIntent = new Intent(fragment.getActivity(), RZAlbumActivity.class);
        rzIntent.putExtras(rzBundle);
        fragment.startActivityForResult(rzIntent, requestCode);
    }

    /*
     * 取得照片路徑
     *
     * @param intent intent
     * @return List<String>
     */
    public static List<String> parseResult(@NonNull Intent intent) {
        List<String> pathList = intent.getStringArrayListExtra(ALBUM_IMAGE_PATH_LIST);
        if (pathList == null) {
            pathList = Collections.emptyList();
        }
        return pathList;
    }
}
