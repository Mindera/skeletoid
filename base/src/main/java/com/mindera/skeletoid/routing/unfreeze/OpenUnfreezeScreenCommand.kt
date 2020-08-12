package com.mindera.skeletoid.routing.unfreeze

import android.content.Context
import android.content.Intent
import com.mindera.skeletoid.activities.unfreeze.activities.UnfreezeScreenActivity
import com.mindera.skeletoid.routing.interfaces.IRouteCommand

class OpenUnfreezeScreenCommand(private val context: Context) :
    IRouteCommand {

    override fun navigate() {
        val i = Intent(context, UnfreezeScreenActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
        context.startActivity(i)
    }
}