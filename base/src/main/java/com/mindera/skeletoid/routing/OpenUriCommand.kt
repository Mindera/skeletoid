package com.mindera.skeletoid.routing

import android.content.Context
import android.content.Intent
import android.net.Uri

class OpenUriCommand(private val context: Context, private val deepLink: String) : IRouteCommand {

    companion object {
        const val IS_INTERNAL_APP_NAVIGATION_EXTRA = "IS_INTERNAL_APP_NAVIGATION_EXTRA"
    }

    override fun navigate() {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(deepLink)
        intent.putExtra(IS_INTERNAL_APP_NAVIGATION_EXTRA, true)
        context.startActivity(intent)
    }
}