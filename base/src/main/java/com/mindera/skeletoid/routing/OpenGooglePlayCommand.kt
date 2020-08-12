package com.mindera.skeletoid.routing

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.mindera.skeletoid.R
import com.mindera.skeletoid.routing.interfaces.IRouteCommand

class OpenGooglePlayCommand(
    private val context: Context,
    private val packageName: Int
) : IRouteCommand {

    override fun navigate() {
        var url = context.getString(R.string.play_store_app_deep_link, context.getString(packageName))

        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            context.startActivity(intent)
        } catch (e: Exception) {
            url = context.getString(R.string.play_store_app_url, context.getString(packageName))
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            context.startActivity(intent)
        }
    }
}
