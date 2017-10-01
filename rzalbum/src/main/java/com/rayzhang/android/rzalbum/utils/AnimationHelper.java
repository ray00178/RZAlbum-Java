package com.rayzhang.android.rzalbum.utils;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.view.View;

/**
 * Created by Ray on 2017/8/19.
 * AnimationHelper
 */

public final class AnimationHelper {
    private static final int MARGIN = 10;

    public static void showFabButAnimation(View view, boolean isLeft) {
        view.setAlpha(0f);
        view.setVisibility(View.VISIBLE);
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "ScaleX", 0f, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "ScaleY", 0f, 1f);
        ObjectAnimator alpha = ObjectAnimator.ofFloat(view, "Alpha", 0f, 1f);
        ObjectAnimator translateY = ObjectAnimator.ofFloat(view, "translationY", 0f, -view.getHeight() - MARGIN);
        ObjectAnimator translateX = ObjectAnimator.ofFloat(view, "translationX", 0f, -view.getWidth() - MARGIN);
        if (!isLeft) {
            translateX = ObjectAnimator.ofFloat(view, "translationX", 0f, view.getWidth() + MARGIN);
        }
        AnimatorSet set = new AnimatorSet();
        set.playTogether(scaleX, scaleY, alpha, translateX, translateY);
        set.setDuration(300);
        set.setStartDelay(100);
        set.setInterpolator(new FastOutSlowInInterpolator());
        set.start();
    }

    public static void hideFabButAnimation(final View view, boolean isLeft) {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "ScaleX", 1f, 0f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "ScaleY", 1f, 0f);
        ObjectAnimator alpha = ObjectAnimator.ofFloat(view, "Alpha", 1f, 0f);
        ObjectAnimator translateY = ObjectAnimator.ofFloat(view, "translationY", -view.getHeight() - MARGIN, 0f);
        ObjectAnimator translateX = ObjectAnimator.ofFloat(view, "translationX", -view.getWidth() - MARGIN, 0f);
        if (!isLeft) {
            translateX = ObjectAnimator.ofFloat(view, "translationX", view.getWidth() + MARGIN, 0f);
        }
        AnimatorSet set = new AnimatorSet();
        set.playTogether(scaleX, scaleY, alpha, translateX, translateY);
        set.setDuration(300);
        set.setStartDelay(100);
        set.setInterpolator(new FastOutSlowInInterpolator());
        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                view.setVisibility(View.GONE);
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
