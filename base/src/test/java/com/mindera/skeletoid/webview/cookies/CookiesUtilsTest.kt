package com.mindera.skeletoid.webview.cookies

import android.webkit.CookieManager
import com.mindera.skeletoid.BuildConfig
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.test.assertEquals
import kotlin.test.assertNull

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

    @Test(expected = IllegalStateException::class)
    fun testGetCookiesFromUrlNoCookies() {
        val url = "https://github.com/Mindera/skeletoid/"

        val cookies = CookiesUtils.getCookiesFromUrl(url)

        assertEquals("", cookies)
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
    fun testGetCookieValue() {
        val cookieManager = CookieManager.getInstance()
        val url = "https://github.com/Mindera/skeletoid/"
        cookieManager.setCookie(url, "name=cookie_monster")
        cookieManager.setCookie(url, "type=oreo")
        cookieManager.setCookie(url, "category=biscuit")

        val value = CookiesUtils.getCookieValue(url, "name")

        assertEquals("cookie_monster", value)
    }

    @Test
    fun testGetCookieValueEmptyValue() {
        val cookieManager = CookieManager.getInstance()
        val url = "https://github.com/Mindera/skeletoid/"
        cookieManager.setCookie(url, "name=")

        val value = CookiesUtils.getCookieValue(url, "name")

        assertNull(value)
    }

    @Test
    fun testGetCookieValueEmptyName() {
        val cookieManager = CookieManager.getInstance()
        val url = "https://github.com/Mindera/skeletoid/"
        cookieManager.setCookie(url, "=cookie_monster")

        val value = CookiesUtils.getCookieValue(url, "")

        assertEquals("cookie_monster", value)
    }

    @Test
    fun testGetCookieNonExistingCookie() {
        val cookieManager = CookieManager.getInstance()
        val url = "https://github.com/Mindera/skeletoid/"
        cookieManager.setCookie(url, "name=cookie_monster")

        val value = CookiesUtils.getCookieValue(url, "")

        assertNull(value)
    }

    @Test(expected = IllegalStateException::class)
    fun testGetCookieNoCookies() {
        val url = "https://github.com/Mindera/skeletoid/"

        val value = CookiesUtils.getCookieValue(url, "name")

        assertNull(value)
    }

    @Test
    fun testGetCookieValueEmptyNameAndValue() {
        val cookieManager = CookieManager.getInstance()
        val url = "https://github.com/Mindera/skeletoid/"
        cookieManager.setCookie(url, "=")

        val value = CookiesUtils.getCookieValue(url, "")

        assertNull(value)
    }

    @Test
    fun testGetCookieValueEmptyCookie() {
        val cookieManager = CookieManager.getInstance()
        val url = "https://github.com/Mindera/skeletoid/"
        cookieManager.setCookie(url, "")

        val value = CookiesUtils.getCookieValue(url, "")

        assertNull(value)
    }
}