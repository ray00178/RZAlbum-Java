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
 */

public class ScrollHideBehavior extends CoordinatorLayout.Behavior<View> {
    // xml 調用時  所在包名+類別名稱 = com.rayzhang.android.rzalbum.behavior.ScrollHideBehavior
    private static final String TAG = ScrollHideBehavior.class.getSimpleName();
    private static final Interpolator INTERPOLATOR = new FastOutLinearInInterpolator();
    // 動畫是否正在進行中
    private boolean isAnimate;

    public ScrollHideBehavior(Context context, AttributeSet attrs) {
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
        if (dy >= 10 && !isAnimate && child.getVisibility() == View.VISIBLE) {
            hide(child);
        } else if (dy < -10 && !isAnimate && child.getVisibility() == View.INVISIBLE) {
            show(child);
        }
    }

    private void hide(final View view) {
        ObjectAnimator translationY = ObjectAnimator.ofFloat(view, "translationY", 0f, view.getHeight());
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
                view.setVisibility(View.INVISIBLE);
                isAnimate = false;
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

    private void show(final View view) {
        ObjectAnimator translationY = ObjectAnimator.ofFloat(view, "translationY", view.getHeight(), 0f);
        AnimatorSet set = new AnimatorSet();
        set.play(translationY);
        set.setDuration(200);
        set.setInterpolator(INTERPOLATOR);
        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                view.setVisibility(View.VISIBLE);
                isAnimate = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isAnimate = false;
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
