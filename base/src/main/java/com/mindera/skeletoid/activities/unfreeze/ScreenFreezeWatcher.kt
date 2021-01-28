package com.mindera.skeletoid.activities.unfreeze

import android.app.Activity
import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import com.mindera.skeletoid.routing.unfreeze.OpenUnfreezeScreenCommand

//There is an issue in Android Pie that affects some devices (like the Pixel 1).
//These are the steps needed to reproduce it:
//- user is on Activity "A".
//- a translucent Activity "T" is open because of a user interaction.
//- user locks the screen on Activity "T". Then unlocks it.
//- user goes back to Activity "A".
//- Activity "A" view is now frozen. It still register user input but the screen does not update
//
//This is due to a corrupted state in the WindowManager. And this class works as an work-around
//to restore the "frozen" activity view to a normal working one.
//The recovering of the corrupted state is achieved by just opening a dumb activity that is automatically closed
//after interacting with itself - that is performing a click in its content view.
class ScreenFreezeWatcher(val application : Application, val activityInRisk: Activity) {

    var isWatcherActive: Boolean = true

    fun watch() {
        //TODO do not forget to validate if this is still needed when a new version cames out
        if (Build.VERSION.SDK_INT != Build.VERSION_CODES.P) {
            return
        }

        application.registerActivityLifecycleCallbacks(activityLifecycleCallback)
        registerScreenOnReceiver()
    }

    fun unwatch() {
        //TODO do not forget to validate if this is still needed when a new version cames out
        if (Build.VERSION.SDK_INT != Build.VERSION_CODES.P) {
            return
        }

        application.unregisterActivityLifecycleCallbacks(activityLifecycleCallback)
        unregisterScreenOnReceiver()
    }

    private fun registerScreenOnReceiver() {
        val intentFilter = IntentFilter(Intent.ACTION_SCREEN_ON)
        activityInRisk.registerReceiver(screenOnReceiver, intentFilter)
    }

    private fun unregisterScreenOnReceiver() {
        activityInRisk.unregisterReceiver(screenOnReceiver)
    }

    private val screenOnReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (isWatcherActive && intent?.action.equals(Intent.ACTION_SCREEN_ON)) {
                OpenUnfreezeScreenCommand(activityInRisk).navigate()
            }
        }

    }

    private val activityLifecycleCallback = object : Application.ActivityLifecycleCallbacks {
        override fun onActivityStarted(activity: Activity) {
            if (activityInRisk == activity) {
                isWatcherActive = true
            }
        }

        override fun onActivityStopped(activity: Activity) {
            if (activityInRisk == activity) {
                isWatcherActive = false
            }
        }

        override fun onActivityCreated(activity: Activity, bundle: Bundle?) {}
        override fun onActivityResumed(activity: Activity) {}
        override fun onActivityPaused(activity: Activity) {}
        override fun onActivitySaveInstanceState(activity: Activity, bundle: Bundle) {}
        override fun onActivityDestroyed(activity: Activity) {}
    }
}