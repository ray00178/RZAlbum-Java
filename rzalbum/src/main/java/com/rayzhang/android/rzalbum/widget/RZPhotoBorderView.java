package com.rayzhang.android.rzalbum.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Ray on 2017/8/19.
 * PhotoView 外框
 */

public class RZPhotoBorderView extends View {
    private Paint mPaint;
    private Path mBorderPath;
    // Draw border
    private boolean isDraw = false;
    // Border StrokeWidth : 5dp
    private float strokeWidth = dp2px(2.5f);
    // Border Color
    private int borderColor = Color.argb(255, 255, 153, 0);

    public RZPhotoBorderView(Context context) {
        this(context, null);
    }

    public RZPhotoBorderView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RZPhotoBorderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public RZPhotoBorderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mBorderPath = new Path();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float width = getWidth();
        float height = getHeight();

        if (isDraw) {
            // draw background
            paintReset();
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setColor(Color.argb(140, 0, 0, 0));
            canvas.drawRect(0, 0, width, height, mPaint);
            // draw border use path
            paintReset();
            mPaint.setColor(borderColor);
            // top
            mBorderPath.addRect(0, 0, width, strokeWidth, Path.Direction.CCW);
            // bottom
            mBorderPath.addRect(0, height - strokeWidth, width, height, Path.Direction.CCW);
            // left
            mBorderPath.addRect(0, strokeWidth, strokeWidth, height - strokeWidth, Path.Direction.CCW);
            // right
            mBorderPath.addRect(width - strokeWidth, strokeWidth, width, height - strokeWidth, Path.Direction.CCW);
            mBorderPath.addRect(0, strokeWidth, strokeWidth, height, Path.Direction.CCW);
            canvas.drawPath(mBorderPath, mPaint);
        } else {
            mPaint.setAlpha(0);
            canvas.drawRect(0, 0, width, height, mPaint);
        }
    }

    private void paintReset() {
        mPaint.reset();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
    }

    private int dp2px(float dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public void setDraw(boolean isDraw, int borderColor) {
        this.borderColor = borderColor;
        this.isDraw = isDraw;
        invalidate();
    }
}
