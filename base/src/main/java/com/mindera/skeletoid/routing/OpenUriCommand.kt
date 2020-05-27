package com.mindera.skeletoid.routing

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.mindera.skeletoid.logs.LOG

class OpenUriCommand(private val context: Context, private val deepLink: String) : IRouteCommand {

    companion object {
        private const val LOG_TAG = "OpenUriCommand"
        const val IS_INTERNAL_APP_NAVIGATION_EXTRA = "IS_INTERNAL_APP_NAVIGATION_EXTRA"
    }

    override fun navigate() {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(deepLink)
        intent.putExtra(IS_INTERNAL_APP_NAVIGATION_EXTRA, true)
        try {
            context.startActivity(intent)
        } catch (exception: ActivityNotFoundException) {
            LOG.e(LOG_TAG, exception, "Could not find an activity to handle $intent")
        }
    }
}
