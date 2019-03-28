package com.mindera.skeletoid.routing

import android.content.Context
import android.content.Intent
import android.net.Uri

class OpenUriCommand(private val context: Context, private val deepLink: String) : IRouteCommand {

    override fun navigate() {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(deepLink)
        context.startActivity(intent)
    }
}