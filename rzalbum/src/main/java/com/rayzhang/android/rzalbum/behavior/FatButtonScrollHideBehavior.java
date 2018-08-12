package com.rayzhang.android.rzalbum.behavior;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Interpolator;

/**
 * Created by Ray on 2016/10/2.
 * FatButtonScrollHideBehavior
 */

class FatButtonScrollHideBehavior extends CoordinatorLayout.Behavior<View> {
    // xml 調用時  所在包名+類別名稱 = com.rayzhang.android.rzalbum.behavior.FatButtonScrollHideBehavior
    private static final String TAG = FatButtonScrollHideBehavior.class.getSimpleName();
    private static final Interpolator INTERPOLATOR = new FastOutLinearInInterpolator();
    // 動畫是否正在進行中
    private boolean isAnimate;
    // 是否為顯示狀態
    private boolean isShow = true;

    public FatButtonScrollHideBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child,
                                       @NonNull View directTargetChild, @NonNull View target, int axes, int type) {
        // 判斷是否是垂直滑動
        return (axes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    @Override
    public void onNestedPreScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child,
                                  @NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
        // dy > 0 是向上滑動 ; dy < 0 是向下滑動
        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
        if (dy >= 10 && !isAnimate && isShow) {
            hide(child, lp.bottomMargin);
        } else if (dy < -10 && !isAnimate && !isShow) {
            show(child, lp.bottomMargin);
        }
    }

    private void hide(final View view, int bottomMargin) {
        ObjectAnimator translationY = ObjectAnimator.ofFloat(view, "translationY",
                0f, view.getHeight() / 2.5f + bottomMargin);
        AnimatorSet set = new AnimatorSet();
        set.play(translationY);
        set.setDuration(200);
        set.setInterpolator(INTERPOLATOR);
        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                isAnimate = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isAnimate = false;
                isShow = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        set.start();
    }

    private void show(final View view, int bottomMargin) {
        ObjectAnimator translationY = ObjectAnimator.ofFloat(view, "translationY", view.getHeight() / 2.5f + bottomMargin, 0f);
        AnimatorSet set = new AnimatorSet();
        set.play(translationY);
        set.setDuration(200);
        set.setInterpolator(INTERPOLATOR);
        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                isAnimate = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isAnimate = false;
                isShow = true;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        set.start();
    }
}
