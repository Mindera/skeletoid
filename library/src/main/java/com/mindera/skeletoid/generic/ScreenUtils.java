package com.mindera.skeletoid.generic;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by neteinstein on 19/05/2017.
 */

public class ScreenUtils {

    public static int GetPixelFromDips(Context context, float pixels) {
        if (context == null) {
            throw new RuntimeException("Context cannot be null");
        }

        // Get the screen's density scale
        final float scale = context.getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (pixels * scale + 0.5f);
    }

    /**
     * Get the current width of the screen (according to portrait or landscape)
     *
     * @return width
     */
    public static int getWidth(Activity activity) {

        if (activity == null) {
            throw new RuntimeException("Activity cannot be null");
        }

        if (activity == null) {
            return 0;
        }
        final DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return metrics.widthPixels;
    }

    /**
     * Get the current height of the screen (according to portrait or landscape)
     *
     * @return height
     */
    public static int getHeight(Activity activity) {
        if (activity == null) {
            throw new RuntimeException("Activity cannot be null");
        }

        final DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return metrics.heightPixels;
    }
}
