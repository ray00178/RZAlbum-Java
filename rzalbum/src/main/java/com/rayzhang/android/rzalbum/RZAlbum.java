package com.rayzhang.android.rzalbum;

/*
 * Created by Ray on 2017/2/10.
 * RZAlbum
 * 調用入口類
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.rayzhang.android.rzalbum.common.RZConfig;
import com.rayzhang.android.rzalbum.model.AlbumPhoto;

import java.util.Collections;
import java.util.List;

public final class RZAlbum {
    private static final String TAG = RZAlbum.class.getSimpleName();
    private Intent rzIntent;
    private Bundle rzBundle;

    public static RZAlbum ofAppName(@NonNull String yourAppName) {
        return new RZAlbum(yourAppName);
    }

    private RZAlbum(String yourAppName) {
        rzBundle = new Bundle();
        rzBundle.putString(RZConfig.APP_NAME, yourAppName);
    }

    public RZAlbum setLimitCount(int limitCount) {
        rzBundle.putInt(RZConfig.LIMIT_COUNT, limitCount <= 0 ? RZConfig.DEFAULT_LIMIT_COUNT : limitCount);
        return this;
    }

    public RZAlbum setSpanCount(int spanCount) {
        rzBundle.putInt(RZConfig.SPAN_COUNT, spanCount <= 0 ? RZConfig.DEFAULT_SPAN_COUNT : spanCount);
        return this;
    }

    public RZAlbum setStatusBarColor(@ColorInt int statusBarColor) {
        rzBundle.putInt(RZConfig.STATUS_BAR_COLOR, statusBarColor);
        return this;
    }

    public RZAlbum setToolBarColor(@ColorInt int toolBarColor) {
        rzBundle.putInt(RZConfig.TOOLBAR_COLOR, toolBarColor);
        return this;
    }

    public RZAlbum setToolBarTitle(String toolBarTitle) {
        rzBundle.putString(RZConfig.TOOLBAR_TITLE, toolBarTitle);
        return this;
    }

    public RZAlbum setPickerColor(@ColorInt int pickColor) {
        rzBundle.putInt(RZConfig.PICK_COLOR, pickColor);
        return this;
    }

    public RZAlbum setDialogIcon(@DrawableRes int resID) {
        rzBundle.putInt(RZConfig.DIALOG_ICON, resID);
        return this;
    }

    public RZAlbum setAllFolderName(String folderName) {
        rzBundle.putString(RZConfig.ALL_FOLDER_NAME, folderName);
        return this;
    }

    public RZAlbum showCamera(boolean isShow) {
        rzBundle.putBoolean(RZConfig.SHOW_CAMERA, isShow);
        return this;
    }

    public RZAlbum showGif(boolean isShow) {
        rzBundle.putBoolean(RZConfig.SHOW_GIF, isShow);
        return this;
    }

    public RZAlbum setPreviewOrientation(int orientation) {
        if (orientation < 0 || orientation > 1) {
            rzBundle.putInt(RZConfig.PREVIEW_ORIENTATION, RZConfig.ORIENTATION_AUTO);
        } else {
            rzBundle.putInt(RZConfig.PREVIEW_ORIENTATION, orientation);
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

    public static List<AlbumPhoto> parseResult(@NonNull Intent intent) {
        List<AlbumPhoto> photos = intent.getParcelableArrayListExtra(RZConfig.RESULT_PHOTOS);
        if (photos == null) {
            photos = Collections.emptyList();
        }
        return photos;
    }
}
