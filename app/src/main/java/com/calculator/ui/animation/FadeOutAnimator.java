package com.calculator.ui.animation;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.View;

/**
 * Fade out animator for equal button press
 */
public class FadeOutAnimator {

    private static final int FADE_DURATION = 200; // ms

    /**
     * Animate fade out effect
     */
    public static void fadeOut(View view, Animator.AnimatorListener listener) {
        ObjectAnimator fadeOut = ObjectAnimator.ofFloat(view, "alpha", 1f, 0f);
        fadeOut.setDuration(FADE_DURATION);
        if (listener != null) {
            fadeOut.addListener(listener);
        }
        fadeOut.start();
    }

    /**
     * Animate fade out then in (for result display)
     */
    public static void fadeOutIn(View view, Runnable onComplete) {
        ObjectAnimator fadeOut = ObjectAnimator.ofFloat(view, "alpha", 1f, 0f);
        fadeOut.setDuration(FADE_DURATION / 2);

        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f);
        fadeIn.setDuration(FADE_DURATION / 2);

        fadeOut.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (onComplete != null) {
                    onComplete.run();
                }
                fadeIn.start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });

        fadeOut.start();
    }
}
