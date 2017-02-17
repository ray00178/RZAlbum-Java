package com.rayzhang.android.rzalbum.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * Created by Ray on 2017/1/22.
 */

public class DisplayUtils {

    private static boolean isInitialize = false;
    public static int screenWidth;
    public static int screenHeight;
    public static int screenDpi;
    public static float density = 1;

    public static void initScreen(Context context) {
        if (isInitialize) return;
        isInitialize = true;
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metric = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(metric);
        screenWidth = metric.widthPixels;
        screenHeight = metric.heightPixels;
        screenDpi = metric.densityDpi;
        density = metric.density;
    }

    public static int px2dip(float inParam) {
        return (int) (inParam / density + 0.5f);
    }

    public static int dip2px(float inParam) {
        return (int) (inParam * density + 0.5f);
    }

    public static int px2sp(float inParam) {
        return (int) (inParam / density + 0.5f);
    }

    public static int sp2px(float inParam) {
        return (int) (inParam * density + 0.5f);
    }
}
