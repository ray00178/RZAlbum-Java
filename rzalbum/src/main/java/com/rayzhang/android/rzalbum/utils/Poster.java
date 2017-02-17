package com.rayzhang.android.rzalbum.utils;

import android.os.Handler;
import android.os.Looper;

/**
 * Created by Ray on 2017/1/22.
 */

public class Poster extends Handler {
    private static Poster instance;

    public static Poster instances() {
        if (instance == null)
            synchronized (Poster.class) {
                if (instance == null)
                    instance = new Poster();
            }
        return instance;
    }

    private Poster() {
        super(Looper.getMainLooper());
    }
}
