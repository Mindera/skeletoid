package com.mindera.skeletoid.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Class to validate if we are connected to a network and if we have internet access.
 */
public class Connectivity {

//    private static final String LOG_TAG = "Connectivity";

    /**
     * Validates if we are connected to a network and if we have real internet access
     * NOTE: This will only work if ConnectivityReceiver is added to the app manifest
     *
     * @param context The context
     * @return true if internet access is available, false otherwise.
     */
    public static boolean isConnectedAndWithInternetAvailable(Context context) {
        if (context == null) {
            throw new IllegalArgumentException("Context must not be null");
        }
        return isConnected(context) && ConnectivityReceiver.isInternetAvailable;
    }

    /**
     * Validates if we are connected to a network.
     * WARNING: Being connected to a network DOES NOT imply internet access!
     *
     * @param context The context
     * @return true if we are connected, false otherwise.
     */
    public static boolean isConnected(Context context) {
        if (context == null) {
            throw new IllegalArgumentException("Context must not be null");
        }
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(
                Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null;
    }

    /**
     * Check if we are connected to a WIFI network
     *
     * @param context The context
     * @return true if we are connected, false otherwise.
     */
    public static boolean isConnectedToWIFI(Context context) {
        if (context == null) {
            throw new IllegalArgumentException("Context must not be null");
        }
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(
                Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null && ni.getType() == ConnectivityManager.TYPE_WIFI;
    }
}
