package com.mindera.skeletoid.webview.cookies

import android.webkit.CookieManager
import com.mindera.skeletoid.logs.LOG

object CookiesUtils {

    private const val LOG_TAG = "CookiesUtils"

    fun getCookiesString(url: String): String {
        val cookieManager = CookieManager.getInstance()

        return cookieManager.getCookie(url)
    }

    fun getCookiesMap(url: String): HashMap<String, String> {
        val cookieManager = CookieManager.getInstance()
        val cookies = cookieManager.getCookie(url)

        return stringCookiesToMap(cookies)
    }

    fun getCookie(url: String, cookieName: String): Map<String, String> {
        var cookieValue: String? = null

        val cookieManager = CookieManager.getInstance()
        val cookies = cookieManager.getCookie(url)
        return stringCookiesToMap(cookies).filter { it.key.contains(cookieName) }
    }

    fun stringCookiesToMap(cookies: String): HashMap<String, String> {
        val cookiesMap = HashMap<String, String>()

        val cookieList = cookies.split(";").map { it.trim() }.dropLastWhile { it.isEmpty() }
            .toTypedArray()

        for (cookiePair in cookieList) {
            val cookiePairAsList = cookiePair.split("=", limit = 2).dropLastWhile { it.isEmpty() }.toTypedArray()

            if (cookiePairAsList.size == 2) {
                cookiesMap[cookiePairAsList[0]] = cookiePairAsList[1]
            } else {
                LOG.e(LOG_TAG, "Cookie is malformed, skipping: $cookiePairAsList")
            }
        }

        return cookiesMap
    }

}