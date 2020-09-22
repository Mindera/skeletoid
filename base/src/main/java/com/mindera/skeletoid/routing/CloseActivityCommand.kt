package com.mindera.skeletoid.routing

import android.app.Activity
import com.mindera.skeletoid.routing.interfaces.IRouteCommand

/**
 * Closes the activity passed in the constructor
 */
class CloseActivityCommand @JvmOverloads constructor(private val activity: Activity, private val enterAnimation: Int = 0, private val exitAnimation: Int = 0) :
    IRouteCommand {

    override fun navigate() {
        activity.finish()
        if (enterAnimation != 0 || exitAnimation != 0) {
            activity.overridePendingTransition(enterAnimation, exitAnimation)
        }
    }
}
