package com.rayzhang.android.rzalbum.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * Created by ray on 2017/12/24.
 * DisplayUtil
 */

public final class DisplayUtils {
    public static int screenW;
    public static int screenH;
    public static int screenDpi;
    public static float density = 1f;
    public static int statusBarHeight;
    public static int navigationBarHeight;

    private DisplayUtils() {
        throw new RuntimeException("This class can't be initialized");
    }

    public static void instance(Context context) {
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (manager != null) {
            DisplayMetrics metric = new DisplayMetrics();
            manager.getDefaultDisplay().getMetrics(metric);

            screenW = metric.widthPixels;
            screenH = metric.heightPixels;
            screenDpi = metric.densityDpi;
            density = metric.density;
        }

        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = context.getResources().getDimensionPixelSize(resourceId);
        }

        resourceId = context.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            navigationBarHeight = context.getResources().getDimensionPixelSize(resourceId);
        }
    }

    public static int px2dp(float pxValue) {
        return (int) (pxValue / density + 0.5f);
    }

    public static int dp2px(float dpValue) {
        return (int) (dpValue * density + 0.5f);
    }

    public static int px2sp(float pxValue) {
        return (int) (pxValue / density + 0.5f);
    }

    public static int sp2px(float spValue) {
        return (int) (spValue * density + 0.5f);
    }
}

