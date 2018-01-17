package com.mindera.skeletoid.generic;

import android.content.Context;
import android.support.annotation.VisibleForTesting;

public class UIUtils {

    @VisibleForTesting
    UIUtils() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns the status bar height in pixels.
     *
     * @param context       App context
     * @return status bar height or 0
     */
    public static int getStatusBarHeighPixels(Context context) {
        int resourceId = context.getResources()
                .getIdentifier("status_bar_height", "dimen", "android");

        return resourceId > 0 ? context.getResources().getDimensionPixelSize(resourceId) : 0;
    }
}
