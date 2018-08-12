package com.rayzhang.android.rzalbum.utils;

import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.Shape;
import android.os.Build;
import android.support.annotation.ColorInt;

/**
 * Created by ray on 2017/12/24.
 * DrawableUtils
 */

public final class DrawableUtils {
    private DrawableUtils() {
        throw new RuntimeException("This class can't be initialized");
    }

    public static ShapeDrawable drawableOfRoundRect(@ColorInt final int color, final float cornerRadius) {
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
                canvas.drawRoundRect(rectF, cornerRadius, cornerRadius, paint);
            }
        });
    }

    /**
     * drawableOfColorStateList
     *
     * @param colors {colorOfCheck ; colorOfNormal}
     * @param state  1.android.R.attr.state_active
     *               2.android.R.attr.state_checked
     *               3.android.R.attr.state_checkable
     *               4.android.R.attr.state_enabled
     *               5.android.R.attr.state_pressed
     *               6.android.R.attr.state_selected
     * @return ColorStateList
     */
    public static ColorStateList drawableOfColorStateList(int[] colors, int state) {
        ColorStateList colorStateList = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && colors.length >= 2) {
            int[][] states = new int[2][];
            states[0] = new int[]{state};
            states[1] = new int[]{};
            colorStateList = new ColorStateList(states, colors);
        }
        return colorStateList;
    }
}
