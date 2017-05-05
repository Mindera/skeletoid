package com.mindera.skeletoid.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.mindera.skeletoid.generic.StringUtils;

import java.net.MalformedURLException;
import java.net.URI;

/**
 * Class to validate if we are connected to a network and if we have internet access.
 */
public class Connectivity {

//    private static final String LOG_TAG = "Connectivity";


    private Connectivity() {

    }

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

    /**
     * Add a callback to receive connectivity updates
     *
     * @param connectivityCallback
     */
    public static void setConnectivityCallback(ConnectivityReceiver.ConnectivityCallback connectivityCallback) {
        ConnectivityReceiver.mConnectivityCallback = connectivityCallback;
    }

    /**
     * Remove the callback to receive connectivity updates
     */
    public static void removeConnectivityCallback() {
        ConnectivityReceiver.mConnectivityCallback = null;
    }

    /**
     * Update url to check internet status
     *
     * @param uri With a valid URI
     * @throws MalformedURLException When the URI is invalid
     */
    public static void updateConnectivityValidationAddress(URI uri) {
        try {
            //Validate URL
            String host = uri.getHost();
            ConnectivityReceiver.mInternetAddress = "http://" + host;
            ConnectivityReceiver.mInternetHttpValidationAddress = "http://" + host.substring(0, StringUtils.ordinalIndexOf(host, ".", 2));
            ConnectivityReceiver.mInternetHttpsValidationAddress = "https://" + host.substring(0, StringUtils.ordinalIndexOf(host, ".", 2));
        } catch (Exception e) {
            throw new IllegalArgumentException("URI is invalid");
        }
    }
}
