package com.mindera.skeletoid.routing

import android.app.Activity

/**
 * Closes the activity passed in the constructor
 */
class CloseActivityCommand(private val activity: Activity, private val enterAnimation: Int = 0, private val exitAnimation: Int = 0) : IRouteCommand {

    constructor(context: Activity) : this(activity = context)

    override fun navigate() {
        activity.finish()
        if (enterAnimation != 0 || exitAnimation != 0) {
            activity.overridePendingTransition(enterAnimation, exitAnimation)
        }
    }
}
