package com.rayzhang.android.rzalbum.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import com.rayzhang.android.rzalbum.R;
import com.rayzhang.android.rzalbum.utils.DisplayUtils;


/**
 * Created by Ray on 2016/11/9.
 */

public class RZIconTextView extends android.support.v7.widget.AppCompatTextView {
    /**
     *
     * 自訂義 左上右 有icon的textView
     * 參考資料 : http://www.jianshu.com/p/d3027acf475a
     *
     */

    /**
     * drawUnderLine  : 是否要畫底線
     * drawLineColor  : 底線顏色
     * leftDrawable   : 左邊Icon
     * topDrawable    : 上方Icon
     * rightDrawable  : 右邊Icon
     */
    private boolean drawUnderLine;
    private int drawLineColor;
    private Drawable leftDrawable;
    private Drawable topDrawable;
    private Drawable rightDrawable;
    private int leftWidth;
    private int leftHeight;
    private int topWidth;
    private int topHeight;
    private int rightWidth;
    private int rightHeight;
    private Paint mPaint;

    public RZIconTextView(Context context) {
        this(context, null);
    }

    public RZIconTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RZIconTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        DisplayUtils.initScreen(context);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RZIconTextView, defStyleAttr, 0);
        drawUnderLine = a.getBoolean(R.styleable.RZIconTextView_drawUnderLine, false);
        leftDrawable = a.getDrawable(R.styleable.RZIconTextView_leftDrawable);
        if (leftDrawable != null) {
            leftWidth = a.getDimensionPixelOffset(R.styleable.RZIconTextView_leftDrawableWidth, 25);
            leftHeight = a.getDimensionPixelOffset(R.styleable.RZIconTextView_leftDrawableHeight, 25);
        }
        topDrawable = a.getDrawable(R.styleable.RZIconTextView_topDrawable);
        if (topDrawable != null) {
            topWidth = a.getDimensionPixelOffset(R.styleable.RZIconTextView_topDrawableWidth, 25);
            topHeight = a.getDimensionPixelOffset(R.styleable.RZIconTextView_topDrawableHeight, 25);
        }
        rightDrawable = a.getDrawable(R.styleable.RZIconTextView_rightDrawable);
        if (rightDrawable != null) {
            rightWidth = a.getDimensionPixelOffset(R.styleable.RZIconTextView_rightDrawableWidth, 25);
            rightHeight = a.getDimensionPixelOffset(R.styleable.RZIconTextView_rightDrawableHeight, 25);
        }
        a.recycle();

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.parseColor("#d9d9d9"));
        mPaint.setDither(true);
        mPaint.setStrokeWidth(DisplayUtils.dip2px(1));
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStyle(Paint.Style.FILL);
        setCompoundDrawablePadding(DisplayUtils.dip2px(15));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (leftDrawable != null) {
            leftDrawable.setBounds(0, 0, leftWidth, leftHeight);
        }
        if (topDrawable != null) {
            topDrawable.setBounds(0, 0, topWidth, topHeight);
        }
        if (rightDrawable != null) {
            rightDrawable.setBounds(0, 0, rightWidth, rightHeight);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        setAllDrawable();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (drawUnderLine) {
            int lineHeight = DisplayUtils.dip2px(1);
            if (leftDrawable != null) {
                int leftPad = getCompoundDrawablePadding() + getPaddingLeft();
                canvas.drawLine(leftWidth + leftPad, getHeight() - lineHeight, getWidth(), getHeight() - lineHeight, mPaint);
            } else {
                canvas.drawLine(0, getHeight() - lineHeight, getWidth(), getHeight() - lineHeight, mPaint);
            }
        }
    }

    private void setAllDrawable() {
        this.setCompoundDrawables(leftDrawable, topDrawable, rightDrawable, null);
    }

    public void setDrawablePad(int pad) {
        setCompoundDrawablePadding(DisplayUtils.dip2px(pad));
    }

    public void setDrawablweColor(int Color) {
        Drawable[] drawables = getCompoundDrawables();
        if (drawables[0] != null) {
            drawables[0].setColorFilter(Color, PorterDuff.Mode.SRC_IN);
        }
        if (drawables[1] != null) {
            drawables[1].setColorFilter(Color, PorterDuff.Mode.SRC_IN);
        }
        if (drawables[2] != null) {
            drawables[2].setColorFilter(Color, PorterDuff.Mode.SRC_IN);
        }
    }

    public boolean isDrawUnderLine() {
        return drawUnderLine;
    }

    public void setDrawUnderLine(boolean drawUnderLine) {
        this.drawUnderLine = drawUnderLine;
        postInvalidate();
    }

    public int getDrawLineColor() {
        return drawLineColor;
    }

    public void setDrawLineColor(int drawLineColor) {
        this.drawLineColor = drawLineColor;
        mPaint.setColor(drawLineColor);
        postInvalidate();
    }

    public Drawable getLeftDrawable() {
        return leftDrawable;
    }

    public void setLeftDrawable(Drawable leftDrawable) {
        this.leftDrawable = leftDrawable;
        setAllDrawable();
    }

    public void setLeftDrawable(int resID) {
        this.leftDrawable = getContext().getResources().getDrawable(resID);
        setAllDrawable();
    }

    public Drawable getTopDrawable() {
        return topDrawable;
    }

    public void setTopDrawable(Drawable topDrawable) {
        this.topDrawable = topDrawable;
        setAllDrawable();
    }

    public void setTopDrawable(int resID) {
        this.topDrawable = getContext().getResources().getDrawable(resID);
        setAllDrawable();
    }

    public Drawable getRightDrawable() {
        return rightDrawable;
    }

    public void setRightDrawable(Drawable rightDrawable) {
        this.rightDrawable = rightDrawable;
        setAllDrawable();
    }

    public void setRightDrawable(int resID) {
        this.rightDrawable = getContext().getResources().getDrawable(resID);
        setAllDrawable();
    }
}
