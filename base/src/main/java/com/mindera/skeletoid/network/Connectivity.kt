package com.mindera.skeletoid.network

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager

/**
 * Class to validate if we are connected to a network and if we have internet access.
 */
object Connectivity {

    /**
     * Validates if we are connected to a network.
     * WARNING: Being connected to a network DOES NOT imply internet access!
     *
     * @param context The context
     * @return true if we are connected, false otherwise.
     */
    @SuppressLint("MissingPermission")
    fun isConnected(context: Context?): Boolean {
        if (context == null) {
            throw IllegalArgumentException("Context must not be null")
        }
        val connectivityManager: ConnectivityManager? = context.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as? ConnectivityManager

        val networkInfo = connectivityManager?.activeNetworkInfo
        return networkInfo != null
    }

    /**
     * Check if we are connected to a WIFI network
     *
     * @param context The context
     * @return true if we are connected, false otherwise.
     */
    @JvmStatic
    @SuppressLint("MissingPermission")
    fun isConnectedToWifi(context: Context?): Boolean {
        if (context == null) {
            throw IllegalArgumentException("Context must not be null")
        }
        val connectivityManager: ConnectivityManager? = context.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as? ConnectivityManager

        val networkInfo = connectivityManager?.activeNetworkInfo
        return networkInfo != null && networkInfo.type == ConnectivityManager.TYPE_WIFI
    }
}
