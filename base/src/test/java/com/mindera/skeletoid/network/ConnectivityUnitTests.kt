package com.mindera.skeletoid.network

import android.content.Context
import junit.framework.Assert.assertFalse
import org.junit.Test
import org.mockito.Mockito.mock

class ConnectivityUnitTests {

    @Test(expected = IllegalArgumentException::class)
    fun testIsConnectedWithNullArguments() {
        Connectivity.isConnected(null)

    }

    @Test
    fun testIsConnected() {
        val context = mock(Context::class.java)
        assertFalse(Connectivity.isConnected(context))
    }

    @Test(expected = IllegalArgumentException::class)
    fun testIsConnectedToWIFIWithNullArguments() {
        Connectivity.isConnectedToWIFI(null)

    }

    @Test
    fun testIsConnectedToWIFI() {
        val context = mock(Context::class.java)
        assertFalse(Connectivity.isConnectedToWIFI(context))
    }
}
