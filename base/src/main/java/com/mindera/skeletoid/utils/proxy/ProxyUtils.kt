package com.mindera.skeletoid.utils.proxy

import com.mindera.skeletoid.logs.LOG

object ProxyUtils {
    private const val LOG_TAG = "ProxyUtils"

    fun getProxyDetails(): String? {
        var proxyAddress: StringBuilder? = null
        try {
            System.getProperty("http.proxyHost")?.run { proxyAddress = StringBuilder(this) }
            proxyAddress?.append(":${System.getProperty("http.proxyPort")}")
        } catch (exception: Exception) {
            LOG.e(LOG_TAG, exception, "Get proxy details exception: ${exception.message}")
        }
        return proxyAddress?.toString()
    }
}