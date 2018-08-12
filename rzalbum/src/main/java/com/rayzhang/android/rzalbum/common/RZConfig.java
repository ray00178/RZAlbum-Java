package com.rayzhang.android.rzalbum.common;

import android.content.pm.ActivityInfo;
import android.graphics.Color;

public final class RZConfig {
    public static final String APP_NAME = "APP_NAME";
    public static final String LIMIT_COUNT = "LIMIT_COUNT";
    public static final String SPAN_COUNT = "SPAN_COUNT";
    public static final String STATUS_BAR_COLOR = "STATUS_BAR_COLOR";
    public static final String TOOLBAR_COLOR = "TOOLBAR_COLOR";
    public static final String TOOLBAR_TITLE = "TOOLBAR_TITLE";
    public static final String PICK_COLOR = "PICK_COLOR";
    public static final String DIALOG_ICON = "DIALOG_ICON";
    public static final String SHOW_CAMERA = "SHOW_CAMERA";
    public static final String SHOW_GIF = "SHOW_GIF";
    public static final String PREVIEW_ORIENTATION = "PREVIEW_ORIENTATION";
    public static final String ALL_FOLDER_NAME = "ALL_FOLDER_NAME";
    public static final String RESULT_PHOTOS = "RESULT_PHOTOS";

    public static final int ORIENTATION_LANDSCAPE = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
    public static final int ORIENTATION_PORTRATI = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
    public static final int ORIENTATION_AUTO = 99999;

    public static final String PREVIEW_ADD_PHOTOS = "PREVIEW_ADD_PHOTOS";
    public static final String PREVIEW_DELETE_PHOTOS = "PREVIEW_DELETE_PHOTOS";
    public static final String PREVIEW_ALL_PHOTOS = "PREVIEW_ALL_PHOTOS";
    public static final String PREVIEW_ITEM_POSITION = "PREVIEW_ITEM_POSITION";
    public static final String PREVIEW_PICK_COLOR = "PREVIEW_PICK_COLOR";
    public static final String PREVIEW_LIMIT_COUNT = "PREVIEW_LIMIT_COUNT";

    public static final int DEFAULT_STATUS_BAR_COLOR = Color.parseColor("#ff673ab7");
    public static final int DEFAULT_TOOLBAR_COLOR = Color.parseColor("#ff673ab7");
    public static final int DEFAULT_PICK_COLOR = Color.parseColor("#ffffc107");
    public static final int DEFAULT_LIMIT_COUNT = 5;
    public static final int DEFAULT_SPAN_COUNT = 3;
    public static final String DEFAULT_APP_NAME = "RZAlbum";
    public static final String DEFAULT_TOOLBAR_TITLE = "RZAlbum";
    public static final boolean DEFAULT_SHOW_CAMERA = true;
    public static final boolean DEFAULT_SHOW_GIF = true;
    public static final int DEFAULT_ORIENTATION = ORIENTATION_AUTO;

    public static final String JPEG = "image/jpeg";
    public static final String GIF = "image/gif";
}
