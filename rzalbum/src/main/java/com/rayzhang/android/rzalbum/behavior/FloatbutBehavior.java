package com.rayzhang.android.rzalbum.behavior;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Interpolator;

/**
 * Created by Ray on 2016/10/2.
 * FloatbutBehavior
 */

public class FloatbutBehavior extends CoordinatorLayout.Behavior<View> {
    // xml 調用時  所在包名+類別名稱 = com.rayzhang.android.rzalbum.behavior.FloatbutBehavior
    private static final String TAG = FloatbutBehavior.class.getSimpleName();
    private static final Interpolator INTERPOLATOR = new FastOutLinearInInterpolator();
    // 動畫是否正在進行中
    private boolean isAnimate;
    // 是否為顯示狀態
    private boolean isShow = true;

    public FloatbutBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, View child, View directTargetChild, View target,
                                       int nestedScrollAxes) {
        // 判斷是否是垂直滑動
        return (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, View child, View target, int dx, int dy, int[] consumed) {
        // dy > 0 是向上滑動 ; dy < 0 是向下滑動
        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
        if (dy >= 10 && !isAnimate && isShow) {
            hide(child, lp.bottomMargin);
        } else if (dy < -10 && !isAnimate && !isShow) {
            show(child, lp.bottomMargin);
        }
    }

    private void hide(final View view, int bottomMargin) {
        ObjectAnimator translationY = ObjectAnimator.ofFloat(view, "translationY", 0f, view.getHeight() / 2.5f + bottomMargin);
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
