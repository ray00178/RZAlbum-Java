package com.rayzhang.android.rzalbum.adapter.itemdecoration;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.ColorInt;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Ray on 2017/6/10.
 * RecycleItemDecoration
 */

public class RecycleItemDecoration extends RecyclerView.ItemDecoration {
    private static final String TAG = RecycleItemDecoration.class.getSimpleName();
    private static final int NONE = Integer.MAX_VALUE;
    public static final int BOTH = Integer.MAX_VALUE - 1;
    public static final int TOP_LEFT = Integer.MAX_VALUE - 2;
    public static final int BOTTOM_RIGHT = Integer.MAX_VALUE - 3;
    private int mDividerSize;
    private int mLinearMode = NONE;
    private Paint mPaint;

    public RecycleItemDecoration(int dividerSize, int dividerColor) {
        mDividerSize = dividerSize;
        initPaint(dividerColor);
    }

    public RecycleItemDecoration(int dividerSize, int dividerColor, int linearMode) {
        mDividerSize = dividerSize;
        mLinearMode = linearMode;
        initPaint(dividerColor);
    }

    private void initPaint(int dividerColor) {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(dividerColor);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        RecyclerView.LayoutManager manager = parent.getLayoutManager();

        // 判斷目前的manager是 GridLayoutManager or LinearLayoutManager
        if (manager instanceof GridLayoutManager) {
            setGridItemOutRect(outRect, view, parent);
        } else if (manager instanceof LinearLayoutManager) {
            // 判斷目前的Orientation是 VERTICAL or HORIZONTAL
            if (((LinearLayoutManager) manager).getOrientation() == LinearLayoutManager.VERTICAL) {
                setVerticalItemOutRect(outRect, view, parent);
            } else {
                setHorizontalItemOutRect(outRect, view, parent);
            }
        }
    }

    private void setVerticalItemOutRect(Rect outRect, View view, RecyclerView parent) {
        int count = parent.getAdapter().getItemCount();
        int position = parent.getChildAdapterPosition(view);
        switch (mLinearMode) {
            case NONE:
                // 如果不是最後一個Item
                if (position != count - 1) outRect.set(0, 0, 0, mDividerSize);
                break;
            case BOTH:
                if (position == 0) {
                    // 如果是第一個Item
                    outRect.set(0, mDividerSize, 0, mDividerSize);
                } else {
                    outRect.set(0, 0, 0, mDividerSize);
                }
                break;
            case TOP_LEFT:
                if (position == 0) {
                    outRect.set(0, mDividerSize, 0, mDividerSize);
                } else if (position != count - 1) {
                    outRect.set(0, 0, 0, mDividerSize);
                }
                break;
            case BOTTOM_RIGHT:
                outRect.set(0, 0, 0, mDividerSize);
                break;
        }
    }

    private void setHorizontalItemOutRect(Rect outRect, View view, RecyclerView parent) {
        int count = parent.getAdapter().getItemCount();
        int position = parent.getChildAdapterPosition(view);
        switch (mLinearMode) {
            case NONE:
                if (position != count - 1) outRect.set(0, 0, mDividerSize, 0);
                break;
            case BOTH:
                if (position == 0) {
                    outRect.set(mDividerSize, 0, mDividerSize, 0);
                } else {
                    outRect.set(0, 0, mDividerSize, 0);
                }
                break;
            case TOP_LEFT:
                if (position == 0) {
                    outRect.set(mDividerSize, 0, mDividerSize, 0);
                } else if (position != count - 1) {
                    outRect.set(0, 0, mDividerSize, 0);
                }
                break;
            case BOTTOM_RIGHT:
                outRect.set(0, 0, mDividerSize, 0);
                break;
        }
    }

    private void setGridItemOutRect(Rect outRect, View view, RecyclerView parent) {
        int right = mDividerSize;
        int bottom = mDividerSize;
        if (isLastSpan(view, parent)) {
            right = 0;
        }

        if (isLastRow(view, parent)) {
            bottom = 0;
        }
        outRect.set(0, 0, right, bottom);
    }

    private Boolean isLastRow(View view, RecyclerView parent) {
        // 是否為最底部的Item
        int count = parent.getAdapter().getItemCount();
        int position = parent.getChildAdapterPosition(view);
        int spanCount = ((GridLayoutManager) parent.getLayoutManager()).getSpanCount();
        return (count - position - 1) < spanCount;
    }

    private Boolean isLastSpan(View view, RecyclerView parent) {
        // 是否為最右邊的Item
        int position = parent.getChildAdapterPosition(view);
        int spanCount = ((GridLayoutManager) parent.getLayoutManager()).getSpanCount();
        return position % spanCount == (spanCount - 1);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);

        RecyclerView.LayoutManager manager = parent.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            drawVerticalDivider(c, parent);
            drawHorizontalDivider(c, parent);
        } else if (manager instanceof LinearLayoutManager) {
            if (((LinearLayoutManager) manager).getOrientation() == LinearLayoutManager.VERTICAL) {
                drawVerticalDivider(c, parent);
            } else {
                drawHorizontalDivider(c, parent);
            }
        }
    }

    private void drawVerticalDivider(Canvas c, RecyclerView parent) {
        int childCount = parent.getChildCount();
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();

        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            if (i == 0 && mLinearMode == BOTH || i == 0 && mLinearMode == TOP_LEFT) {
                // 如果是第一個Item而且LinearMode為：BOTH or TOP_LEFT
                int top = parent.getPaddingTop() + params.topMargin;
                int bottom = top + mDividerSize;
                c.drawRect(left, top, right, bottom, mPaint);
            }
            // 其他Item
            int top = child.getBottom() + params.bottomMargin;
            int bottom = top + mDividerSize;
            c.drawRect(left, top, right, bottom, mPaint);
        }
    }

    private void drawHorizontalDivider(Canvas c, RecyclerView parent) {
        int childCount = parent.getChildCount();
        int top = parent.getPaddingTop();
        int bottom = parent.getHeight() - parent.getPaddingBottom();

        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            if (i == 0 && mLinearMode == BOTH || i == 0 && mLinearMode == TOP_LEFT) {
                // 如果是第一個Item而且LinearMode為：BOTH or TOP_LEFT
                int left = parent.getPaddingLeft() + params.leftMargin;
                int right = left + mDividerSize;
                c.drawRect(left, top, right, bottom, mPaint);
            }
            int left = child.getRight() + params.rightMargin;
            int right = left + mDividerSize;
            c.drawRect(left, top, right, bottom, mPaint);
        }
    }
}
