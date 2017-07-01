package com.rayzhang.android.rzalbum;

/**
 * Created by Ray on 2017/2/10.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import java.util.Collections;
import java.util.List;

/**
 * 調用入口類
 */
public class RZAlbum {
    private static final String TAG = RZAlbum.class.getSimpleName();
    protected static final String ALBUM_APP_NAME = "ALBUM_APP_NAME";
    protected static final String ALBUM_LIMIT_COUNT = "ALBUM_LIMIT_COUNT";
    protected static final String ALBUM_SPAN_COUNT = "ALBUM_SPAN_COUNT";
    protected static final String ALBUM_STATUSBAR_COLOR = "ALBUM_STATUSBAR_COLOR";
    protected static final String ALBUM_TOOLBAR_TITLE = "ALBUM_TOOLBAR_TITLE";
    protected static final String ALBUM_TOOLBAR_COLOR = "ALBUM_TOOLBAR_COLOR";
    protected static final String ALBUM_DIALOG_ICON = "ALBUM_DIALOG_ICON";
    protected static final String ALBUM_IMAGE_PATH_LIST = "ALBUM_IMAGE_PATH_LIST";
    protected static final String ALBUN_SHOW_CAMERA = "ALBUN_SHOW_CAMERA";

    private static final int MAX_COUNT = 5;
    private Intent rzIntent;
    private Bundle rzBundle;

    /**
     * Init
     *
     * @param yourappName
     * @return
     */
    public static RZAlbum ofAppName(@NonNull String yourappName) {
        return new RZAlbum(yourappName);
    }

    private RZAlbum(String yourappName) {
        rzBundle = new Bundle();
        rzBundle.putString(ALBUM_APP_NAME, yourappName);
    }

    /**
     * Init
     *
     * @param limitCount
     * @return
     */
    public RZAlbum setLimitCount(@NonNull int limitCount) {
        if (limitCount <= 0) limitCount = MAX_COUNT;
        rzBundle.putInt(ALBUM_LIMIT_COUNT, limitCount);
        return this;
    }

    /**
     * set Adapter spanCount
     *
     * @param spanCount
     * @return
     */
    public RZAlbum setSpanCount(int spanCount) {
        rzBundle.putInt(ALBUM_SPAN_COUNT, spanCount);
        return this;
    }

    /**
     * set statusBar color
     *
     * @param statusBarColor
     * @return
     */
    public RZAlbum setStatusBarColor(@ColorInt int statusBarColor) {
        rzBundle.putInt(ALBUM_STATUSBAR_COLOR, statusBarColor);
        return this;
    }

    /**
     * set toolBar color
     *
     * @param toolBarColor
     * @return
     */
    public RZAlbum setToolBarColor(@ColorInt int toolBarColor) {
        rzBundle.putInt(ALBUM_TOOLBAR_COLOR, toolBarColor);
        return this;
    }

    /**
     * set ToolBar Title
     *
     * @param toolBarTitle
     * @return
     */
    public RZAlbum setToolBarTitle(String toolBarTitle) {
        rzBundle.putString(ALBUM_TOOLBAR_TITLE, toolBarTitle);
        return this;
    }

    /**
     * set Dialog icon
     *
     * @param resID
     * @return
     */
    public RZAlbum setDialogIcon(@DrawableRes int resID) {
        rzBundle.putInt(ALBUM_DIALOG_ICON, resID);
        return this;
    }

    public RZAlbum showCamera(boolean isShow) {
        rzBundle.putBoolean(ALBUN_SHOW_CAMERA, isShow);
        return this;
    }

    public void start(@NonNull Activity activity, @NonNull int requestCode) {
        rzIntent = new Intent(activity, RZAlbumActivity.class);
        rzIntent.putExtras(rzBundle);
        activity.startActivityForResult(rzIntent, requestCode);
    }

    public void start(@NonNull android.app.Fragment fragment, @NonNull int requestCode) {
        rzIntent = new Intent(fragment.getActivity(), RZAlbumActivity.class);
        rzIntent.putExtras(rzBundle);
        fragment.startActivityForResult(rzIntent, requestCode);
    }

    public void start(@NonNull Fragment fragment, @NonNull int requestCode) {
        rzIntent = new Intent(fragment.getActivity(), RZAlbumActivity.class);
        rzIntent.putExtras(rzBundle);
        fragment.startActivityForResult(rzIntent, requestCode);
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
