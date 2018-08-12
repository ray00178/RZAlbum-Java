package com.rayzhang.android.rzalbum.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewTreeObserver;
import android.view.WindowManager;

/**
 * Created by Ray on 2016/10/15.
 * 自訂義可以縮放的Imgview
 */

public class RZZoomImgView extends android.support.v7.widget.AppCompatImageView implements ViewTreeObserver.OnGlobalLayoutListener,
        ScaleGestureDetector.OnScaleGestureListener, View.OnTouchListener {

    private boolean mFirst;     // 第一次加載 才初始化
    private Context context;
    private int density, statusBarHeight;
    /**
     * 整個縮放區間在 mInitScale ~ mMaxScale
     */
    private float mInitScale;   // 初始化的縮放比例值
    private float mMidScale;    // 雙擊時 達到的縮放比例
    private float mMaxScale;    // 放大的最大的值
    /**
     * 透過 matrix 進行 image 的縮放
     */
    private Matrix mScaleMatrix;

    /**
     * 多點觸碰監聽器
     */
    private ScaleGestureDetector mScaleGestureDetector;

    // ------------自由移動------------
    /**
     * 紀錄上一次螢幕上 手指的數量
     */
    private int mLastPointerCount;
    /**
     * 紀錄上一次中心點的位置
     */
    private float mLastCenterX;
    private float mLastCenterY;

    private float mTouchSlop;
    private boolean isCanDrag;
    private boolean isCheckLeftAndRight;  // 檢查左右邊界
    private boolean isCheckTopAndBottom;  // 檢查上下邊界

    // ------------雙擊放大 縮小------------
    private GestureDetector mGestureDetector;

    private boolean isAutoScale;

    // ------------縮小 回彈效果------------
    private float mMinScale;        // 縮小的最小的值
    private float mMaxOverScale;    // 超出最大的最大值

    public RZZoomImgView(Context context) {
        this(context, null);
    }

    public RZZoomImgView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RZZoomImgView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        manager.getDefaultDisplay().getMetrics(metrics);
        density = (int) metrics.density;
        statusBarHeight = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = getResources().getDimensionPixelSize(resourceId);
        }

        mScaleMatrix = new Matrix();
        // 透過 matrix 來進行縮放
        setScaleType(ScaleType.MATRIX);
        this.context = context;
        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDoubleTap(MotionEvent e) {

                // 如果正在放大 就直接返回true 消費事件
                if (isAutoScale) {
                    return true;
                }

                // 取得點擊時的位置 並依這個位置做中心點進行縮放
                float x = e.getX();
                float y = e.getY();
                // 如果當前的縮放比 < mMidScale 則就放大至 mMidScale比例的大小
                if (getScaleValue() < mMidScale) {
                    //mScaleMatrix.postScale(mMidScale / getScaleValue(), mMidScale / getScaleValue(), x, y);
                    //setImageMatrix(mScaleMatrix);
                    postDelayed(new AutoScaleRunnable(mMidScale, x, y), 16);
                } else {
                    //mScaleMatrix.postScale(mInitScale / getScaleValue(), mInitScale / getScaleValue(), x, y);
                    //setImageMatrix(mScaleMatrix);
                    postDelayed(new AutoScaleRunnable(mInitScale, x, y), 16);
                }
                isAutoScale = true;

                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) { // 長按時
                super.onLongPress(e);
            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return super.onSingleTapUp(e);
            }
        });
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        // 當視圖加載至窗口時，註冊監聽
        getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        // 當視圖離開窗口時，取消監聽
        getViewTreeObserver().removeOnGlobalLayoutListener(this);
    }

    @Override
    public void onGlobalLayout() {
        // 當視圖加載完成時 可以取得控件的寬高
        if (!mFirst) {
            // 取得控件的寬高
            int width = getWidth();
            int height = getHeight();
            // 取得當前 image的 寬高
            Drawable d = getDrawable();
            // 如果沒有圖片返回
            if (d == null) return;

            int dw = d.getIntrinsicWidth();
            int dh = d.getIntrinsicHeight();

            // 將控件的寬高 跟 image的寬高 做比較 因為2者的大小不一定一樣
            // 並且將圖片居中顯示
            // 定義一個縮放比例的變數
            float scale = 1.0f;

            /*
             * 如果image的寬度 > 控件的寬度 但卻 < 控件的高度 就依寬度比例縮放
             */
            if (dw > width && dh < height) {
                scale = width * 1.0f / dw;
            }
            /*
             * 如果image的高度 > 控件的高度 但卻 < 控件的寬度 就依高度比例縮放
             */
            if (dw < width && dh > height) {
                scale = height * 1.0f / dh;
            }
            /*
             * 如果image的高度 > 控件的高度 但卻 = 控件的寬度 就依高度比例縮放
             */
            if (dw == width && dh > height) {
                scale = height * 1.0f / dh;
            }
            /*
             * 如果image的寬度 > 控件的寬度 但卻 = 控件的高度 就依寬度比例縮放
             */
            if (dw > width && dh == height) {
                scale = width * 1.0f / dw;
            }
            /*
             * 如果image的寬高 > 或 < 控件的寬高  就依比例最小的來縮放
             */
            if ((dw > width && dh > height) || (dw < width && dh < height)) {
                scale = Math.min(width * 1.0f / dw, height * 1.0f / dh);
            }

            /*
             * 得到初始化的各縮放比例
             */
            mInitScale = scale;
            mMaxScale = mInitScale * 4;
            mMidScale = mInitScale * 2;
            mMinScale = mInitScale / 4;
            mMaxOverScale = mInitScale * 6.5f;

            // 將image 移至螢幕的中心點
            // 先取得移動距離
            int dx = getWidth() / 2 - dw / 2;
            int dy = getHeight() / 2 - dh / 2;
            // 進行平移
            mScaleMatrix.postTranslate(dx, dy);
            // 進行縮放 後面2個參數代表 以哪個中心點為縮放(這邊以控件的中心點)
            mScaleMatrix.postScale(mInitScale, mInitScale, width / 2, height / 2);
            setImageMatrix(mScaleMatrix);

            mFirst = true;
            // 防止加載未完成時滑動螢幕而導致圖片錯亂 故在此作初始化
            mScaleGestureDetector = new ScaleGestureDetector(context, this);
            setOnTouchListener(this);
            // 是一個距離，表示滑動的時候，手的移動要大於這個距離才開始移動控件。如果小於這個距離就不觸發移動控件
            mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        }
    }

    // 實現緩慢縮放
    private class AutoScaleRunnable implements Runnable {
        // 縮放的目標值
        private float mTargetScale;
        // 縮放的中心點
        private float x;
        private float y;

        private final float BIGGER = 1.07f;
        private final float SMALL = 0.93f;

        private float tmpScale;

        private AutoScaleRunnable(float mTargetScale, float x, float y) {
            this.mTargetScale = mTargetScale;
            this.x = x;
            this.y = y;

            if (getScaleValue() < mTargetScale) {
                tmpScale = BIGGER;
            }
            if (getScaleValue() > mTargetScale) {
                tmpScale = SMALL;
            }
        }

        @Override
        public void run() {
            // 進行縮放
            mScaleMatrix.postScale(tmpScale, tmpScale, x, y);
            checkBorderAndCenterWhenScale();
            setImageMatrix(mScaleMatrix);

            float currentScale = getScaleValue();

            if ((tmpScale > 1.0f && currentScale < mTargetScale) || (tmpScale < 1.0f && currentScale > mTargetScale)) {
                // 每16ms進行一次
                postDelayed(this, 16);
            } else {
                float scale = mTargetScale / currentScale;
                mScaleMatrix.postScale(scale, scale, x, y);
                checkBorderAndCenterWhenScale();
                setImageMatrix(mScaleMatrix);

                isAutoScale = false;
            }
        }
    }

    /**
     * 取得當前圖片的縮放比例
     */
    private float getScaleValue() {
        float[] values = new float[9];
        mScaleMatrix.getValues(values);
        // Matrix.MSCALE_X 或是 Matrix.MSCALE_Y 都行 因為都是一樣的縮放比例
        return values[Matrix.MSCALE_X];
    }

    @Override
    public boolean onScale(ScaleGestureDetector detector) { // 縮放進行的時候

        float scale = getScaleValue();
        // 取得目前的縮放比例
        // 當我們兩個手指進行分開操作時，說明我們想要放大，這個scaleFactor是一個稍微大於1的數值
        // 當我們兩個手指進行閉合操作時，說明我們想要縮小，這個scaleFactor是一個稍微小於1的數值
        float scaleFactor = detector.getScaleFactor();

        if (getDrawable() == null) {
            return true;
        }

        // 縮放比例判斷
        // 如果scaleFactor大於1，說明想放大，當前的縮放比例乘以scaleFactor之後小於 最大的縮放比例時，允許放大
        // 如果scaleFactor小於1，說明想縮小，當前的縮放比例乘以scaleFactor之後大於 最小的縮放比例時，允許縮小
        // (scale < mMaxScale && scaleFactor > 1.0f) || (scale > mInitScale && scaleFactor < 1.0f)
        if ((scale < mMaxOverScale && scaleFactor > 1.0f) || (scale > mMinScale && scaleFactor < 1.0f)) {

            // 邊界控制，如果當前縮放比例乘以scaleFactor之後 小於 最小的縮放比例 我們不允許再縮小
//            if (scale * scaleFactor < mInitScale) { // 縮小至原始比例 不回彈
//                // 計算方法 比喻 5 * y = 20 -> y = 20 / 5
//                scaleFactor = mInitScale / scale;
//            }
            if (scale * scaleFactor < mMinScale) {  // 縮小回彈
                scaleFactor = mMinScale / scale;
            }

            // 邊界控制，如果當前縮放比例乘以scaleFactor之後 大於 最大的縮放比例 我們不允許再放大
//            if (scale * scaleFactor > mMaxScale) {
//                scaleFactor = mMaxScale / scale;
//            }
            if (scale * scaleFactor > mMaxOverScale) { // 放大回彈
                scaleFactor = mMaxOverScale / scale;
            }
            // 以控件中心為縮放基準
            //mScaleMatrix.postScale(scaleFactor, scaleFactor, getWidth() / 2, getHeight() / 2);
            // 以手指觸摸中心為準
            mScaleMatrix.postScale(scaleFactor, scaleFactor, detector.getFocusX(), detector.getFocusY());
            checkBorderAndCenterWhenScale();
            setImageMatrix(mScaleMatrix);
        }

        return true;
    }

    // 取得image縮放後的4點座標 & 寬高
    private RectF getMatrixRectF() {
        Matrix matrix = mScaleMatrix;
        RectF rectF = new RectF();

        Drawable d = getDrawable();
        if (d != null) {
            rectF.set(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
            matrix.mapRect(rectF);
        }

        return rectF;
    }

    private void checkBorderAndCenterWhenScale() {
        if (getDrawable() == null) {
            return;
        }

        // 垂直 水平要移動的距離 初始值為0
        float delTaX = 0;
        float delTaY = 0;

        // 取得控件的寬高
        int width = getWidth();
        int height = getHeight();

        // 取得當前image的位置
        RectF rectF = getMatrixRectF();

        // 水平方向的檢查
        if (rectF.width() >= width) {
            // 如果圖片左邊坐標是大於0的，說明圖片左邊離控件左邊有一定距離，左邊會出現一個小白邊
            if (rectF.left > 0) {
                // 我們將圖片向左邊移動
                delTaX = -rectF.left;
            }
            // 如果圖片右邊坐標小於控件寬度，說明圖片右邊離控件右邊有一定距離，右邊會出現一個小白邊
            if (rectF.right < width) {
                // 我們將圖片向右邊移動
                delTaX = width - rectF.right;
            }
        }

        // 垂直方向的檢查
        if (rectF.height() >= height) {
            if (rectF.top > 0) {
                delTaY = -rectF.top;
            }

            if (rectF.bottom < height) {
                delTaY = height - rectF.bottom;
            }
        }
        if (rectF.width() < width) {
            delTaX = width / 2 - rectF.right + rectF.width() / 2;
        }
        // 如果圖片的高度小於控件的高度，我們要對圖片做一個豎直方向的居中
        if (rectF.height() < height) {
            delTaY = height / 2 - rectF.bottom + rectF.height() / 2;
        }

        mScaleMatrix.postTranslate(delTaX, delTaY);
    }

    private void checkBorderWhenTranslate() {


        RectF rectF = getMatrixRectF();
        float delTaX = 0.0f;
        float delTaY = 0.0f;

        int width = getWidth();
        int height = getHeight();

        if (rectF.top > 0 && isCheckTopAndBottom) {
            delTaY = -rectF.top;
        }

        if (rectF.bottom < height && isCheckTopAndBottom) {
            delTaY = height - rectF.bottom;
        }

        if (rectF.left > 0 && isCheckLeftAndRight) {
            delTaX = -rectF.left;
        }

        if (rectF.right < width && isCheckLeftAndRight) {
            delTaX = width - rectF.right;
        }
        mScaleMatrix.postTranslate(delTaX, delTaY);

    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) { // 縮放開始的時候 要設為 true
        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        // 如果在雙擊時 不允許移動
        if (mGestureDetector.onTouchEvent(event)) {
            return true;
        }

        // 將觸摸事件 交給 ScaleGestureDetector 處理
        mScaleGestureDetector.onTouchEvent(event);

        // 儲存中心點的位置
        float centerX = 0.0f;
        float cenetrY = 0.0f;
        // 取得螢幕上手指的數量
        int pointCount = event.getPointerCount();

        for (int i = 0; i < pointCount; i++) {
            // 取得每隻手指的 x y 軸位置
            centerX += event.getX(i);
            cenetrY += event.getY(i);
        }
        // 取得中心點
        centerX /= pointCount;
        cenetrY /= pointCount;
        // 如果手指數量發生變化 就紀錄最後的中心點位置
        if (mLastPointerCount != pointCount) {
            mLastCenterX = centerX;
            mLastCenterY = cenetrY;
            isCanDrag = false;
        }
        mLastPointerCount = pointCount;

        RectF rectF = getMatrixRectF();
        float div = 0.01f; // 誤差值
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                // 當image 大於螢幕的寬或高時 不希望老爸(viewgroup) 去干涉事件
                // 事件而是兒子(view)直接處理這個
                if (rectF.width() > getWidth() + div || rectF.height() > getHeight() + div) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (rectF.width() > getWidth() + div || rectF.height() > getHeight() + div) {
                    //if (getParent() instanceof ViewPager) {
                    //getParent().requestDisallowInterceptTouchEvent(true);
                    //}
                    getParent().requestDisallowInterceptTouchEvent(true);
                }

                // 取得偏移量
                float dx = centerX - mLastCenterX;
                float dy = cenetrY - mLastCenterY;

                if (!isCanDrag) {
                    isCanDrag = isMoveAction(dx, dy);
                }

                if (isCanDrag) {
                    if (getDrawable() != null) {

                        isCheckLeftAndRight = isCheckTopAndBottom = true;

                        if (rectF.width() < getWidth()) {
                            // 如果image 寬度小於 控件的寬度 就不用移動
                            dx = 0;
                            isCheckLeftAndRight = false;
                        }

                        if (rectF.height() < getHeight()) {
                            // 如果image 高度小於 控件的高度 就不用移動
                            dy = 0;
                            isCheckTopAndBottom = false;
                        }

                        mScaleMatrix.postTranslate(dx, dy);
                        checkBorderWhenTranslate();
                        setImageMatrix(mScaleMatrix);
                    }
                }
                // 移動的過程中 不斷紀錄上一次的位置
                mLastCenterX = centerX;
                mLastCenterY = cenetrY;
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                // 手指已離開螢幕 所以回歸預設值
                mLastPointerCount = 0;

                // 如果當前image < 初始的大小
                if (getScaleValue() < mInitScale) {
                    // 自動放大至初始大小
                    post(new AutoScaleRunnable(mInitScale, getWidth() / 2, getHeight() / 2));
                }
                // 如果當前image > 最大的大小
                if (getScaleValue() > mMaxScale) {
                    post(new AutoScaleRunnable(mMaxScale, getWidth() / 2, getHeight() / 2));
                }

                break;
        }

        return true;
    }

    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        Drawable d = getDrawable();
        if (d == null) return;
        // newConfig.screenHeightDp : Subtract statusBar height
        int width = newConfig.screenWidthDp * density;
        int height = newConfig.screenHeightDp * density + statusBarHeight;
        int dw = d.getIntrinsicWidth();
        int dh = d.getIntrinsicHeight();

        float scale = 1.0f;
        if (dw > width && dh < height) {
            scale = width * 1.0f / dw;
        }
        if (dw < width && dh > height) {
            scale = height * 1.0f / dh;
        }
        if (dw == width && dh > height) {
            scale = height * 1.0f / dh;
        }
        if (dw > width && dh == height) {
            scale = width * 1.0f / dw;
        }
        if ((dw > width && dh > height) || (dw < width && dh < height)) {
            scale = Math.min(width * 1.0f / dw, height * 1.0f / dh);
        }

        mInitScale = scale;
        mMaxScale = mInitScale * 4;
        mMidScale = mInitScale * 2;
        mMinScale = mInitScale / 4;
        mMaxOverScale = mInitScale * 6.5f;
        // reset
        mScaleMatrix.reset();
        int dx = (width - dw) / 2;
        int dy = (height - dh) / 2;
        // 進行平移
        mScaleMatrix.postTranslate(dx, dy);
        // 進行縮放
        mScaleMatrix.postScale(mInitScale, mInitScale, width / 2, height / 2);
        setImageMatrix(mScaleMatrix);
    }

    /**
     * 判斷是否移動中
     *
     * @param dx dx
     * @param dy dy
     * @return isMoveAction
     */
    private boolean isMoveAction(float dx, float dy) {
        return Math.sqrt(dx * dx + dy * dy) > mTouchSlop;
    }
}
