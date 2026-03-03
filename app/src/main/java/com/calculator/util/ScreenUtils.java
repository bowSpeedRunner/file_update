package com.calculator.util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * Screen dimension utility
 */
public final class ScreenUtils {
    private ScreenUtils() {
    }

    /**
     * Get screen width in pixels
     */
    public static int getScreenWidth(Context context) {
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (wm != null) {
            wm.getDefaultDisplay().getMetrics(metrics);
            return metrics.widthPixels;
        }
        return 0;
    }

    /**
     * Get screen height in pixels
     */
    public static int getScreenHeight(Context context) {
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (wm != null) {
            wm.getDefaultDisplay().getMetrics(metrics);
            return metrics.heightPixels;
        }
        return 0;
    }

    /**
     * Convert dp to pixels
     */
    public static int dpToPx(Context context, float dp) {
        return (int) (dp * context.getResources().getDisplayMetrics().density);
    }

    /**
     * Convert pixels to dp
     */
    public static float pxToDp(Context context, int px) {
        return px / context.getResources().getDisplayMetrics().density;
    }

    /**
     * Get screen density
     */
    public static float getDensity(Context context) {
        return context.getResources().getDisplayMetrics().density;
    }
}
