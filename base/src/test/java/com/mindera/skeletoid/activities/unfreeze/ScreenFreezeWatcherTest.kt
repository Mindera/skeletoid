package com.mindera.skeletoid.activities.unfreeze

import android.app.Activity
import android.app.Application
import android.content.BroadcastReceiver
import android.content.IntentFilter
import android.os.Build
import com.mindera.skeletoid.utils.extensions.mock
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.any
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner
import org.powermock.reflect.Whitebox

@RunWith(PowerMockRunner::class)
@PrepareForTest(Build.VERSION::class)
class ScreenFreezeWatcherTest {

    @Before
    fun setUp() {
        Whitebox.setInternalState(Build.VERSION::class.java, "SDK_INT", 0)
    }

    @Test
    fun testWatchPie() {
        val application = mock<Application>()
        val activity = mock<Activity>()
        Whitebox.setInternalState(Build.VERSION::class.java, "SDK_INT", 28)

        val watcher = ScreenFreezeWatcher(application, activity)
        watcher.watch()

        verify(
            application,
            times(1)
        ).registerActivityLifecycleCallbacks(any(Application.ActivityLifecycleCallbacks::class.java))
        verify(activity, times(1)).registerReceiver(any(BroadcastReceiver::class.java), any(IntentFilter::class.java))
    }

    @Test
    fun testWatchNonPie() {
        val application = mock<Application>()
        val activity = mock<Activity>()
        Whitebox.setInternalState(Build.VERSION::class.java, "SDK_INT", 25)

        val watcher = ScreenFreezeWatcher(application, activity)
        watcher.watch()

        verify(
            application,
            times(0)
        ).registerActivityLifecycleCallbacks(any(Application.ActivityLifecycleCallbacks::class.java))
        verify(activity, times(0)).registerReceiver(any(BroadcastReceiver::class.java), any(IntentFilter::class.java))
    }

    @Test
    fun testUnwatchPie() {
        val application = mock<Application>()
        val activity = mock<Activity>()
        Whitebox.setInternalState(Build.VERSION::class.java, "SDK_INT", 28)

        val watcher = ScreenFreezeWatcher(application, activity)
        watcher.unwatch()

        verify(
            application,
            times(1)
        ).unregisterActivityLifecycleCallbacks(any(Application.ActivityLifecycleCallbacks::class.java))
        verify(activity, times(1)).unregisterReceiver(any(BroadcastReceiver::class.java))
    }
}