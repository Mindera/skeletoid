package com.mindera.skeletoid.webview.cookies

import android.webkit.CookieManager
import com.mindera.skeletoid.logs.LOG

object CookiesUtils {

    private const val LOG_TAG = "CookiesUtils"

    fun getCookiesFromUrl(url: String): String {
        val cookieManager = CookieManager.getInstance()

        return cookieManager.getCookie(url)
    }

    fun getCookiesFromUrlToMap(url: String): HashMap<String, String> {
        val cookies = getCookiesFromUrl(url)

        return getCookiesToMap(cookies)
    }

    fun getCookieValue(url: String, cookieName: String): String? {
        return getCookiesFromUrlToMap(url).filter { it.key.contains(cookieName) }[cookieName]
    }

    fun getCookiesToMap(cookies: String): HashMap<String, String> {
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