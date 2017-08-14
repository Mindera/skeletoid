package com.mindera.skeletoid.network;

import com.mindera.skeletoid.logs.LOG;
import com.mindera.skeletoid.threads.threadpools.ThreadPoolUtils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * This class updates the state of the connection according to real http requests to Google
 * It's just a helper class and shouldn't be directly accessed, but via the Connectivity class
 * To use it must have a Receiver defined in the Manifest:
 * <pre>
 * {@code
 * <receiver android:name="com.mindera.skeletoid.network.ConnectivityReceiver">
 * <intent-filter>
 * <action android:name="android.net.conn.CONNECTIVITY_CHANGE" />
 * <action android:name="android.net.wifi.STATE_CHANGE"/>
 * <action android:name="android.net.wifi.supplicant.CONNECTION_CHANGE"/>
 * </intent-filter>
 * </receiver>
 * }
 * </pre>
 */
public class ConnectivityReceiver extends BroadcastReceiver {

    private static final String LOG_TAG = "ConnectivityReceiver";

    private static ThreadPoolExecutor threadPool = ThreadPoolUtils
            .getFixedThreadPool("Connectivity Worker", 1);

    /**
     * Var to control if the network was reachable before
     */
    private static Boolean mPreviousReachableState = null;
    /**
     * Var that contains the current state of connectivity
     */
    protected static volatile boolean isInternetAvailable = false;
    protected static volatile boolean isInWiFi = false;

    protected static String mInternetAddress = "http://www.google.com";
    protected static String mInternetHttpValidationAddress = "http://www.google";
    protected static String mInternetHttpsValidationAddress = "https://www.google";
    private static final int mInternetValidationTimeout = 1000;

    private static final int HTTP_200 = 200;
    private static final int HTTP_201 = 201;

    private static final long ONE_SECOND = 1000;

    protected static ConnectivityCallback mConnectivityCallback = null;

    public interface ConnectivityCallback {

        void connectivityUpdate(boolean isConnectedToANetwork, boolean networkHasInternetAccess,
                boolean isNetworkWiFi);
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        final ConnectivityManager connMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = null;
        if (connMgr != null) {
            networkInfo = connMgr.getActiveNetworkInfo();
        }

        boolean isReachable = networkInfo != null && networkInfo.isConnected();

        // This variable can change from true to false in background via validateInternetStatus()
        // Use case: You have connection, but no internet
        isInternetAvailable = false;
        isInWiFi = networkInfo != null ? networkInfo.getType() == ConnectivityManager.TYPE_WIFI
                : false;

        if (mPreviousReachableState == null || mPreviousReachableState != isReachable) {

            mPreviousReachableState = isReachable;

            LOG.d(LOG_TAG, "Connectivity changed " + isReachable);
        }

        if (isReachable) {
            validateInternetStatus(3);
        } else {
            notifyConnectionStatus();
        }


    }

    /**
     * Invoke the connectivity callback if one is set
     */
    private static void notifyConnectionStatus() {
        if (mConnectivityCallback != null) {
            mConnectivityCallback
                    .connectivityUpdate(mPreviousReachableState, isInternetAvailable, isInWiFi);
        }
    }

    /**
     * Check if internet is available if we can open a http connection to google
     *
     * @param numberRetries Number of retries of this request.
     */
    public static void validateInternetStatus(final int numberRetries) {
        //Avoid having multiple internet updates running, let just the last run
        BlockingQueue<Runnable> tasksQueue = threadPool.getQueue();
        if (!tasksQueue.isEmpty()) {
            tasksQueue.clear();
        }

        threadPool.execute(new Runnable() {
            @Override
            public void run() {

                int retries = numberRetries;
                do {
                    try {
                        URL url = new URL(mInternetAddress);
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setReadTimeout(mInternetValidationTimeout);
                        isInternetAvailable =
                                conn.getResponseCode() == HTTP_200
                                        || conn.getResponseCode() == HTTP_201;

                        LOG.v(LOG_TAG, "Response code -> " + conn.getResponseCode());

                        isInternetAvailable &= conn
                                .getURL().toString()
                                .startsWith(mInternetHttpValidationAddress) || conn
                                .getURL().toString()
                                .startsWith(mInternetHttpsValidationAddress);

                        LOG.v(LOG_TAG, "URL -> ", conn.getURL().toString());

                        conn.disconnect();

                        if (isInternetAvailable) {
                            LOG.v(LOG_TAG, "INTERNET IS AVAILABLE");
                            retries = 0;
                        } else {
                            LOG.v(LOG_TAG, "INTERNET IS NOT AVAILABLE");
                            retries--;
                            Thread.sleep(ONE_SECOND);
                        }
                    } catch (Exception ex) {
                        LOG.e(LOG_TAG, ex, "ConnectivityReceiver isConnected");
                        isInternetAvailable = false;
                    }

                } while (retries > 0);

                notifyConnectionStatus();
            }

        });
    }
}
