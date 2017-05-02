package com.mindera.skeletoid.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

import com.mindera.skeletoid.logs.LOG;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 * This class updates the state of the connection according to real http requests to Google
 *
 * It's just a helper class and shouldn't be directly accessed, but via the Connectivity class
 *
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
    /**
     * Var to control if the network was reachable before
     */
    private static Boolean mPreviousReachableState = null;
    /**
     * Var that contains the current state of connectivity
     */
    protected static volatile boolean isInternetAvailable = false;

    private static final String mInternetAddress = "http://www.google.com";
    private static final String mInternetHttpValidationAddress = "http://www.google";
    private static final String mInternetHttpsValidationAddress = "https://www.google";
    private static final int mInternetValidationTimeout = 1000;

    private static final int HTTP_200 = 200;
    private static final int HTTP_201 = 201;

    private static final long ONE_SECOND = 1000;

    @Override
    public void onReceive(Context context, Intent intent) {

        final ConnectivityManager connMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        boolean isReachable = networkInfo != null && networkInfo.isConnected();

        if (isReachable) {
            updateInternetStatus(3);
        }
        isInternetAvailable = isReachable;

        if (mPreviousReachableState == null || mPreviousReachableState != isReachable) {

            mPreviousReachableState = isReachable;

            //TODO PUSH LOCAL BROADCAST WITH NEW STATE OR ADD POSSIBILITY TO ADD CALLBACK

            LOG.d(LOG_TAG, "Connectivity changed " + isReachable);
        }
    }

    /**
     * Check if internet is available if we can open a http connection to google
     *
     * @param numberRetries Number of retries of this request.
     */
    public static void updateInternetStatus(final int numberRetries) {
        //Should this be a threadpool instead of an asynctask?
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                int retries = numberRetries;
                do {
                    try {
                        URL url = new URL(mInternetAddress);
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setReadTimeout(mInternetValidationTimeout);
                        isInternetAvailable =
                                conn.getResponseCode() == HTTP_200 || conn.getResponseCode() == HTTP_201;

                        LOG.d(LOG_TAG, "Response code -> " + conn.getResponseCode());

                        isInternetAvailable &= conn
                                .getURL().toString()
                                .startsWith(mInternetHttpValidationAddress) || conn
                                .getURL().toString()
                                .startsWith(mInternetHttpsValidationAddress);

                        LOG.d(LOG_TAG, "URL -> ", conn.getURL().toString());

                        conn.disconnect();

                        if (isInternetAvailable) {
                            LOG.d(LOG_TAG, "INTERNET IS AVAILABLE");
                            retries = 0;
                        } else {
                            LOG.d(LOG_TAG, "INTERNET IS NOT AVAILABLE");
                            retries--;
                            Thread.sleep(ONE_SECOND);
                        }
                    } catch (Exception ex) {
                        LOG.e(LOG_TAG, ex, "ConnectivityReceiver isConnected");
                        isInternetAvailable = false;
                    }

                } while (retries > 0);
                return null;
            }
        }.execute();
    }
}
