package com.mindera.skeletoid.network

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build


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
    fun isConnected(context: Context): Boolean {
        val connectivityManager: ConnectivityManager? = context.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as? ConnectivityManager

        connectivityManager?.let { conManager ->

            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val network = conManager.activeNetwork
                val capabilities = conManager
                    .getNetworkCapabilities(network)

                capabilities != null && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                        && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
            } else {
                val networkInfo = conManager.activeNetworkInfo
                networkInfo != null
            }
        }

        return false
    }

    /**
     * Check if we are connected to a WIFI network
     *
     * @param context The context
     * @return true if we are connected, false otherwise.
     */
    @JvmStatic
    @SuppressLint("MissingPermission")
    fun isConnectedToWifi(context: Context): Boolean {

        val connectivityManager: ConnectivityManager? = context.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as? ConnectivityManager

        connectivityManager?.let { conManager ->

            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val network = conManager.activeNetwork
                val capabilities = conManager
                    .getNetworkCapabilities(network)

                capabilities != null && capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                        && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
            } else {
                val networkInfo = conManager.activeNetworkInfo
                networkInfo != null
            }
        }

        return false
    }
}
