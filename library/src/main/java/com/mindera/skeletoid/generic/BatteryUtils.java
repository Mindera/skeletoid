package com.mindera.skeletoid.generic;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.support.annotation.VisibleForTesting;
import android.util.Log;

/**
 * Utility class for management of device's battery usage.
 */
public class BatteryUtils {

    private final static String LOG_TAG = "BatteryUtils";

    @VisibleForTesting
    BatteryUtils() {
        throw new UnsupportedOperationException();
    }

    /**
     * Check if battery is above the value defined as minimum
     *
     * @param context Application context
     * @return battery level
     */
    public static int getBatteryLevel(Context context) {
        if (context == null) {
            throw new RuntimeException("Context is null");
        }
        final IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        final Intent batteryStatus = context.registerReceiver(null, intentFilter);

        // Get battery level percentage
        final int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);

        Log.v(LOG_TAG, "Current battery level: " + level + "%");

        return level;
    }

    /**
     * Check is currently charging
     *
     * @param context Application context
     * @return true if the battery level is above the minimum set in LOW_BATTERY_LEVEL_PERCENTAGE
     */
    public static boolean isBatteryCharging(Context context) {
        if (context == null) {
            throw new RuntimeException("Context is null");
        }
        final IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        final Intent batteryStatus = context.registerReceiver(null, ifilter);

        final int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        final boolean pluggedIn = batteryStatus.getIntExtra(BatteryManager.EXTRA_PLUGGED, 0)
                != 0;

        // Check if battery is charging
        final boolean isCharging = pluggedIn && (
                status == BatteryManager.BATTERY_STATUS_CHARGING
                        || status == BatteryManager.BATTERY_STATUS_FULL);

        Log.v(LOG_TAG, "Is battery plugged=" + pluggedIn + ", status=" + status + ", charging="
                + isCharging);

        return isCharging;
    }


}
