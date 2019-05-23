package com.mindera.skeletoid.webview.cookies

import android.webkit.CookieManager
import com.mindera.skeletoid.BuildConfig
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.test.assertEquals

@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class, manifest = Config.NONE)
class CookiesUtilsTest {

    @Test
    fun testGetCookiesFromUrl() {
        val cookieManager = CookieManager.getInstance()
        val url = "https://github.com/Mindera/skeletoid/"
        cookieManager.setCookie(url, "name=cookie_monster")
        cookieManager.setCookie(url, "type=oreo")
        cookieManager.setCookie(url, "category=biscuit")

        val cookies = CookiesUtils.getCookiesFromUrl(url)

        assertEquals("name=cookie_monster; type=oreo; category=biscuit", cookies)
    }

    @Test
    fun testGetCookiesFromUrlMalformedCookie() {
        val cookieManager = CookieManager.getInstance()
        val url = "https://github.com/Mindera/skeletoid/"
        cookieManager.setCookie(url, "name:cookie_monster")

        val cookies = CookiesUtils.getCookiesFromUrl(url)

        assertEquals("name:cookie_monster", cookies)
    }

    @Test
    fun testGetCookiesFromUrlToMap() {
        val cookieManager = CookieManager.getInstance()
        val url = "https://github.com/Mindera/skeletoid/"
        cookieManager.setCookie(url, "name=cookie_monster")
        cookieManager.setCookie(url, "type=oreo")
        cookieManager.setCookie(url, "category=biscuit")

        val cookies = CookiesUtils.getCookiesFromUrlToMap(url)

        assertEquals(3, cookies.size)
        assertEquals("cookie_monster", cookies["name"])
        assertEquals("oreo", cookies["type"])
        assertEquals("biscuit", cookies["category"])
    }

    @Test
    fun testGetCookiesFromUrlToMapMalformedCookie() {
        val cookieManager = CookieManager.getInstance()
        val url = "https://github.com/Mindera/skeletoid/"
        cookieManager.setCookie(url, "name:cookie_monster")

        val cookies = CookiesUtils.getCookiesFromUrlToMap(url)

        assertEquals(0, cookies.size)
    }

    @Test
    fun testGetCookieFromUrlToMap() {
        val cookieManager = CookieManager.getInstance()
        val url = "https://github.com/Mindera/skeletoid/"
        cookieManager.setCookie(url, "name=cookie_monster")
        cookieManager.setCookie(url, "type=oreo")
        cookieManager.setCookie(url, "category=biscuit")

        val cookies = CookiesUtils.getCookieFromUrlToMap(url, "name")

        assertEquals(1, cookies.size)
        assertEquals("cookie_monster", cookies["name"])
    }

    @Test
    fun testGetEmptyValueCookieFromUrlToMap() {
        val cookieManager = CookieManager.getInstance()
        val url = "https://github.com/Mindera/skeletoid/"
        cookieManager.setCookie(url, "name=")

        val cookies = CookiesUtils.getCookieFromUrlToMap(url, "name")

        assertEquals(0, cookies.size)
    }

    @Test
    fun testGetEmptyNameCookieFromUrlToMap() {
        val cookieManager = CookieManager.getInstance()
        val url = "https://github.com/Mindera/skeletoid/"
        cookieManager.setCookie(url, "=cookie_monster")

        val cookies = CookiesUtils.getCookieFromUrlToMap(url, "")

        assertEquals(1, cookies.size)
        assertEquals("cookie_monster", cookies[""])
    }

    @Test
    fun testGetEmptyNameAndValueCookieFromUrlToMap() {
        val cookieManager = CookieManager.getInstance()
        val url = "https://github.com/Mindera/skeletoid/"
        cookieManager.setCookie(url, "=")

        val cookies = CookiesUtils.getCookieFromUrlToMap(url, "")

        assertEquals(0, cookies.size)
    }

    @Test
    fun testGetEmptyCookieFromUrlToMap() {
        val cookieManager = CookieManager.getInstance()
        val url = "https://github.com/Mindera/skeletoid/"
        cookieManager.setCookie(url, "")

        val cookies = CookiesUtils.getCookieFromUrlToMap(url, "")

        assertEquals(0, cookies.size)
    }
}