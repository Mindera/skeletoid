package com.mindera.skeletoid.logs.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v4.content.FileProvider
import com.mindera.skeletoid.BuildConfig
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.eq
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.powermock.api.mockito.PowerMockito.mockStatic
import org.powermock.core.classloader.annotations.PowerMockIgnore
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.rule.PowerMockRule
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import kotlin.test.assertEquals

@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class)
@PowerMockIgnore("org.mockito.*", "org.robolectric.*", "android.*")
@PrepareForTest(FileProvider::class)
class ShareLogFilesUtilsUnitTests {

    @Rule
    @JvmField
    public var rule = PowerMockRule()

    @Test(expected = UnsupportedOperationException::class)
    fun testConstructor() {
        ShareLogFilesUtils()
    }

    @Test
    fun testGetFileLogPath() {
        val context = mock(Context::class.java)

        val file = mock(File::class.java)
        `when`(context.filesDir).thenReturn(file)
        `when`(file.path).thenReturn("/com/mindera/skeletoid")

        assertEquals("/com/mindera/skeletoid", ShareLogFilesUtils.getFileLogPath(context))
    }

    @Test
    fun testSendLogsSingle() {
        val activity = Robolectric.buildActivity(TestActivity::class.java!!)
            .create()
            .resume()
            .get()

        val uri = mock(Uri::class.java)

        mockStatic(FileProvider::class.java)
        `when`(
            FileProvider.getUriForFile(
                eq(activity),
                any(String::class.java),
                any(File::class.java)
            )
        ).thenReturn(uri)

        val shadowActivity = shadowOf(activity)

        ShareLogFilesUtils.sendLogs(
            activity,
            "intentChooserTitle",
            "subject",
            "bodyText",
            null,
            File.createTempFile("prefix", "suffix")
        )

        val intent = shadowActivity.nextStartedActivity
        assertEquals(Intent.ACTION_CHOOSER, intent.action)
        assertEquals("intentChooserTitle", intent.getStringExtra(Intent.EXTRA_TITLE))
        val extraIntent = intent.getParcelableExtra<Intent>(Intent.EXTRA_INTENT)
        assertEquals(Intent.ACTION_SEND, extraIntent.action)
        assertEquals(uri, extraIntent?.extras?.get(Intent.EXTRA_STREAM))
        assertEquals("subject", extraIntent.getStringExtra(Intent.EXTRA_SUBJECT))
        assertEquals("bodyText", extraIntent.getStringExtra(Intent.EXTRA_TEXT))
    }

    @Test
    fun testSendLogsEmail() {
        val activity = Robolectric.buildActivity(TestActivity::class.java!!)
            .create()
            .resume()
            .get()

        val uri = mock(Uri::class.java)

        mockStatic(FileProvider::class.java)
        `when`(
            FileProvider.getUriForFile(
                eq(activity),
                any(String::class.java),
                any(File::class.java)
            )
        ).thenReturn(uri)

        val shadowActivity = shadowOf(activity)

        val path = ShareLogFilesUtils.getFileLogPath(activity)
        val file = File(path, "newFile")
        file.createNewFile()

        var fos: FileOutputStream? = null
        try {
            fos = FileOutputStream(file.absolutePath)
            val buffer = "Hello mindera!".toByteArray()
            fos.write(buffer, 0, buffer.size)
            fos.close()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            fos?.close()
        }

        ShareLogFilesUtils.sendLogsEmail(
            activity,
            "intentChooserTitle",
            "subject",
            "bodyText",
            arrayOf("user@user.com")
        )

        val intent = shadowActivity.nextStartedActivity
        assertEquals(Intent.ACTION_CHOOSER, intent.action)
        assertEquals("intentChooserTitle", intent.getStringExtra(Intent.EXTRA_TITLE))
        val extraIntent = intent.getParcelableExtra<Intent>(Intent.EXTRA_INTENT)
        assertEquals(Intent.ACTION_SEND, extraIntent.action)
        assertEquals(1, extraIntent.getStringArrayExtra(Intent.EXTRA_EMAIL).size)
        assertEquals("user@user.com", extraIntent.getStringArrayExtra(Intent.EXTRA_EMAIL)[0])
        assertEquals(uri, extraIntent?.extras?.get(Intent.EXTRA_STREAM))
        assertEquals("subject", extraIntent.getStringExtra(Intent.EXTRA_SUBJECT))
        assertEquals("bodyText", extraIntent.getStringExtra(Intent.EXTRA_TEXT))
    }
}
