package com.mindera.skeletoid.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import com.mindera.skeletoid.utils.extensions.mock
import org.junit.Test
import org.mockito.Mockito
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ConnectivityTest {

    @Test
    fun testIsNotConnected() {
        val context = mock<Context>()
        assertFalse(Connectivity.isConnected(context))
    }

    @Test
    fun testIsNotConnectedForNoNetworkInfo() {
        val context = mock<Context>()
        val connectivityManager = mock<ConnectivityManager>()
        Mockito.`when`(context.getSystemService(Context.CONNECTIVITY_SERVICE)).thenReturn(connectivityManager)
        assertFalse(Connectivity.isConnected(context))
    }

    @Test
    fun testIsConnected() {
        val context = mock<Context>()
        val connectivityManager = mock<ConnectivityManager>()
        val activeNetworkInfo = mock<NetworkInfo>()
        Mockito.`when`(connectivityManager.activeNetworkInfo).thenReturn(activeNetworkInfo)
        Mockito.`when`(context.getSystemService(Context.CONNECTIVITY_SERVICE)).thenReturn(connectivityManager)
        assertTrue(Connectivity.isConnected(context))
    }

    @Test
    fun testIsNotConnectedToWIFI() {
        val context = mock<Context>()
        assertFalse(Connectivity.isConnectedToWifi(context))
    }

    @Test
    fun testIsNotConnectedToWIFIForNoNetworkInfo() {
        val context = mock<Context>()
        val connectivityManager = mock<ConnectivityManager>()
        Mockito.`when`(context.getSystemService(Context.CONNECTIVITY_SERVICE)).thenReturn(connectivityManager)
        assertFalse(Connectivity.isConnectedToWifi(context))
    }

    @Test
    fun testIsConnectedToWIFI() {
        val context = mock<Context>()
        val connectivityManager = mock<ConnectivityManager>()
        val activeNetworkInfo = mock<NetworkInfo>()
        Mockito.`when`(activeNetworkInfo.type).thenReturn(ConnectivityManager.TYPE_WIFI)
        Mockito.`when`(connectivityManager.activeNetworkInfo).thenReturn(activeNetworkInfo)
        Mockito.`when`(context.getSystemService(Context.CONNECTIVITY_SERVICE)).thenReturn(connectivityManager)
        assertTrue(Connectivity.isConnectedToWifi(context))
    }
}