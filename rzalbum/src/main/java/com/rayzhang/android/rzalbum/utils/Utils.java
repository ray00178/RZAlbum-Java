package com.rayzhang.android.rzalbum.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by Ray on 2017/1/7.
 * Utils
 */

public final class Utils {
    private Utils() {
        throw new RuntimeException("This class can't be initialized");
    }

    public static void setStatusBarColor(Activity activity, @ColorInt int color) {
        setStatusBarColor(activity, color, false);
    }

    public static void setStatusBarColor(Activity activity, @ColorInt int color, boolean isLight) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            if (window != null) {
                // 改變statusbar 圖標顏色
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    int flags = window.getDecorView().getSystemUiVisibility();
                    if (isLight) {
                        // 淺色背景搭配灰色文字、圖示
                        flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                    } else {
                        // 深色背景搭配預設(白色)文字、圖示
                        flags = 0;
                    }
                    window.getDecorView().setSystemUiVisibility(flags);
                }

                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(color);
                window.setNavigationBarColor(Color.BLACK);
            }
        }
    }

    @SuppressLint("ObsoleteSdkInt")
    public static void setStatusBarTransparent(Activity activity) {
        // 參考資料 : http://blog.csdn.net/sinyu890807/article/details/51763825
        // 沈浸式Status bar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            View decorView = activity.getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams localLayoutParams = activity.getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }
    }

    public static void goAppSettingPage(FragmentActivity activity) {
        // 前往App設定頁面
        goAppSettingPage(activity, 0);
    }

    public static void goAppSettingPage(FragmentActivity activity, int requestCode) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                .setData(Uri.fromParts("package", activity.getPackageName(), null));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (requestCode == 0) {
            activity.startActivity(intent);
        } else {
            activity.startActivityForResult(intent, requestCode);
        }
    }
}
