package com.mindera.skeletoid.network

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
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
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            doesActiveNetworkHasTransportTypeAndNetworkCapability(
                context,
                emptyList(),
                listOf(
                    NetworkCapabilities.NET_CAPABILITY_INTERNET,
                    NetworkCapabilities.NET_CAPABILITY_VALIDATED
                )
            )
        } else {
            val networkInfo = context.getSystemService(
                Context.CONNECTIVITY_SERVICE
            ) as? ConnectivityManager
            networkInfo?.activeNetworkInfo != null
        }
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

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            doesActiveNetworkHasTransportTypeAndNetworkCapability(
                context,
                listOf(NetworkCapabilities.TRANSPORT_WIFI),
                listOf(NetworkCapabilities.NET_CAPABILITY_INTERNET,
                    NetworkCapabilities.NET_CAPABILITY_VALIDATED)
            )
        } else {
            val networkInfo = context.getSystemService(
                Context.CONNECTIVITY_SERVICE
            ) as? ConnectivityManager
            networkInfo?.activeNetworkInfo != null
        }
    }

    @SuppressLint("MissingPermission")
    private fun doesActiveNetworkHasTransportTypeAndNetworkCapability(
        context: Context,
        transportTypes: List<Int>,
        networkCapabilities: List<Int>
    ): Boolean {
        val connectivityManager = getConnectivityManager(context) ?: return false

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork
            val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false

            transportTypes.forEach {
                if (!capabilities.hasTransport(it)) {
                    return false
                }
            }
            networkCapabilities.forEach {
                if (!capabilities.hasCapability(it)) {
                    return false
                }
            }

            return true
        }

        return connectivityManager.activeNetworkInfo != null
    }

    private fun getConnectivityManager(context: Context): ConnectivityManager? {
        return context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
    }
}
