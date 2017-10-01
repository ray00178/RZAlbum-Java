package com.rayzhang.android.rzalbum.utils;

import android.os.Handler;
import android.os.Looper;

/**
 * Created by Ray on 2017/1/22.
 * MainHandler
 */

public final class MainHandler extends Handler {
    private static MainHandler instance;

    public static MainHandler instances() {
        if (instance == null)
            synchronized (MainHandler.class) {
                if (instance == null)
                    instance = new MainHandler();
            }
        return instance;
    }

    private MainHandler() {
        super(Looper.getMainLooper());
    }
}
