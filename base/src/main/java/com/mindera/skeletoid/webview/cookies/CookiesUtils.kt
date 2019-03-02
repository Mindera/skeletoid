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
        val cookiesMap = HashMap<String, String>()

        val cookieManager = CookieManager.getInstance()
        val cookies = cookieManager.getCookie(url)

        val temp = cookies.split(";".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

        for (ar1 in temp) {
            val temp1 = ar1.split("=".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

            if (temp1.size == 2) {
                cookiesMap[temp1[0]] = temp1[1]
            } else {
                LOG.e(LOG_TAG, "Cookie is malformed, skipping...")
            }
        }
        return cookiesMap
    }

    fun getCookie(url: String, cookieName: String): String? {
        var cookieValue: String? = null

        val cookieManager = CookieManager.getInstance()
        val cookies = cookieManager.getCookie(url)

        val cookieArray = cookies.split(";".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

        for (nameAndCookie in cookieArray) {
            if (nameAndCookie.contains(cookieName)) {

                val cookie = nameAndCookie.split("=".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

                if(cookie.size == 2){
                    cookieValue = cookie[1]
                }else{
                    LOG.e(LOG_TAG, "$cookieName Cookie is malformed, skipping...")
                }
                break
            }
        }
        return cookieValue
    }
}