package com.calculator.ui.animation;

import android.animation.ValueAnimator;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

/**
 * Bottom sheet animation helper
 */
public class BottomSheetAnimator {

    private static final int ANIM_OPEN = 300;     // ms, ease-out
    private static final int ANIM_EXPAND = 250;   // ms, ease-in-out
    private static final int ANIM_COLLAPSE = 250; // ms, ease-in-out
    private static final int ANIM_CLOSE = 300;    // ms, ease-in

    /**
     * Animate bottom sheet slide
     */
    public static void animateSlide(View view, float fromY, float toY, Runnable onComplete) {
        ValueAnimator animator = ValueAnimator.ofFloat(fromY, toY);
        animator.setDuration(ANIM_OPEN);
        animator.setInterpolator(new DecelerateInterpolator());

        animator.addUpdateListener(animation -> {
            float value = (float) animation.getAnimatedValue();
            view.setTranslationY(value);
        });

        animator.addListener(new android.animation.AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(android.animation.Animator animation) {
                if (onComplete != null) {
                    onComplete.run();
                }
            }
        });

        animator.start();
    }

    /**
     * Animate expand to full height
     */
    public static void animateExpand(View view, float fromHeight, float toHeight, Runnable onComplete) {
        ValueAnimator animator = ValueAnimator.ofFloat(fromHeight, toHeight);
        animator.setDuration(ANIM_EXPAND);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());

        animator.addUpdateListener(animation -> {
            float value = (float) animation.getAnimatedValue();
            view.getLayoutParams().height = (int) value;
            view.requestLayout();
        });

        animator.addListener(new android.animation.AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(android.animation.Animator animation) {
                if (onComplete != null) {
                    onComplete.run();
                }
            }
        });

        animator.start();
    }

    /**
     * Animate collapse to partial height
     */
    public static void animateCollapse(View view, float fromHeight, float toHeight, Runnable onComplete) {
        ValueAnimator animator = ValueAnimator.ofFloat(fromHeight, toHeight);
        animator.setDuration(ANIM_COLLAPSE);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());

        animator.addUpdateListener(animation -> {
            float value = (float) animation.getAnimatedValue();
            view.getLayoutParams().height = (int) value;
            view.requestLayout();
        });

        animator.addListener(new android.animation.AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(android.animation.Animator animation) {
                if (onComplete != null) {
                    onComplete.run();
                }
            }
        });

        animator.start();
    }
}
