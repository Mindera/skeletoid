package com.mindera.skeletoid.webview.cookies

import android.os.Build
import android.webkit.CookieManager
import android.webkit.CookieSyncManager
import org.junit.After
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.powermock.api.mockito.PowerMockito.`when`
import org.powermock.api.mockito.PowerMockito.mockStatic
import org.powermock.core.classloader.annotations.PowerMockIgnore
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.rule.PowerMockRule
import org.powermock.reflect.Whitebox
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1])
@PowerMockIgnore("org.mockito.*", "org.robolectric.*", "android.*")
@PrepareForTest(CookieManager::class, Build.VERSION::class)
class WebViewCookiesTest {

    @Rule
    @JvmField
    var rule = PowerMockRule()

    @After
    fun tearDown() {
        Whitebox.setInternalState(Build.VERSION::class.java, "SDK_INT", 0)
    }

    @Test
    fun testClearWebViewCookiesApiEqualOrGreaterThan22() {
        Whitebox.setInternalState(Build.VERSION::class.java, "SDK_INT", 27)
        // RuntimeEnvironment.application is deprecated, we need to use ApplicationProvider.getApplicationContext()
        // but for some reason it's throwing an error whenever I try that, we need to go over this again in a future PR
        // https://stackoverflow.com/questions/32957741/android-illegalstateexception-no-instrumentation-registered-must-run-under-a-re
        val context = RuntimeEnvironment.application.applicationContext
        val cookieManager = mock(CookieManager::class.java)
        mockStatic(CookieManager::class.java)
        `when`(CookieManager.getInstance()).thenReturn(cookieManager)
        WebViewCookies.clearWebViewCookies(context)

        verify(cookieManager).removeAllCookies(null)
        verify(cookieManager).flush()
    }

    @Suppress("DEPRECATION")
    @Test
    @Ignore
    fun testClearWebViewCookiesApiLowerThan22() {
        Whitebox.setInternalState(Build.VERSION::class.java, "SDK_INT", 21)
        // RuntimeEnvironment.application is deprecated, we need to use ApplicationProvider.getApplicationContext()
        // but for some reason it's throwing an error whenever I try that, we need to go over this again in a future PR
        // https://stackoverflow.com/questions/32957741/android-illegalstateexception-no-instrumentation-registered-must-run-under-a-re
        val context = RuntimeEnvironment.application.applicationContext
        val cookieManager = mock(CookieManager::class.java)
        mockStatic(CookieManager::class.java)
        `when`(CookieManager.getInstance()).thenReturn(cookieManager)
        val cookieSyncManager = mock(CookieSyncManager::class.java)
        mockStatic(CookieSyncManager::class.java)
        `when`(CookieSyncManager.createInstance(context)).thenReturn(cookieSyncManager)

        WebViewCookies.clearWebViewCookies(context)

        verify(cookieManager).removeAllCookie()
        verify(cookieManager).removeSessionCookie()

        verify(cookieSyncManager).startSync()
        verify(cookieSyncManager).stopSync()
        verify(cookieSyncManager).sync()
        cookieManager.flush()
    }
}