package com.rayzhang.android.rzalbum.utils;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.Shape;
import android.support.annotation.ColorInt;

/**
 * Created by Ray on 2017/8/18.
 * DrawableUtils
 */

public final class DrawableUtils {
    public static ShapeDrawable backgroundDrawable(@ColorInt final int color) {
        return new ShapeDrawable(new Shape() {
            @Override
            public void draw(Canvas canvas, Paint paint) {
                int width = canvas.getWidth();
                int height = canvas.getHeight();
                paint.setAntiAlias(true);
                paint.setDither(true);
                paint.setColor(color);
                paint.setStyle(Paint.Style.FILL);
                RectF rectF = new RectF(0, 0, width, height);
                canvas.drawRoundRect(rectF, height / 2, height / 2, paint);
            }
        });
    }
}
