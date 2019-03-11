package com.mindera.skeletoid.network

import android.content.Context
import android.net.ConnectivityManager

/**
 * Class to validate if we are connected to a network and if we have internet access.
 */
object Connectivity {
//    private static final String LOG_TAG = "Connectivity";

    /**
     * Validates if we are connected to a network.
     * WARNING: Being connected to a network DOES NOT imply internet access!
     *
     * @param context The context
     * @return true if we are connected, false otherwise.
     */
    fun isConnected(context: Context?): Boolean {
        if (context == null) {
            throw IllegalArgumentException("Context must not be null")
        }
        val cm = context.getSystemService(
            Context.CONNECTIVITY_SERVICE
        )

        return if (cm is ConnectivityManager) {
            val ni = cm.activeNetworkInfo
            ni != null
        } else {
            false
        }
    }

    /**
     * Check if we are connected to a WIFI network
     *
     * @param context The context
     * @return true if we are connected, false otherwise.
     */
    fun isConnectedToWIFI(context: Context?): Boolean {
        if (context == null) {
            throw IllegalArgumentException("Context must not be null")
        }
        val cm = context.getSystemService(
            Context.CONNECTIVITY_SERVICE
        )

        return if (cm is ConnectivityManager) {
            val ni = cm.activeNetworkInfo
            ni != null && ni.type == ConnectivityManager.TYPE_WIFI
        } else {
            false
        }

    }

}
