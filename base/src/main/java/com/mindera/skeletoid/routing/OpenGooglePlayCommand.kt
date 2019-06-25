package com.mindera.skeletoid.routing

import android.content.Context
import android.content.Intent
import android.net.Uri

class OpenGooglePlayCommand(
    private val context: Context,
    private val googlePlayUrl: Int,
    private val packageName: Int
) : IRouteCommand {

    override fun navigate() {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(context.getString(googlePlayUrl, context.getString(packageName)))
        }

        context.startActivity(intent)
    }
}
