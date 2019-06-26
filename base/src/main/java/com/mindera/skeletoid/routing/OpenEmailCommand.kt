package com.mindera.skeletoid.routing

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.mindera.skeletoid.R
import com.mindera.skeletoid.logs.LOG

class OpenEmailCommand(
    private val context: Context,
    private val emailTo: Int,
    private val emailSubject: Int,
    private val emailBody: Int
) : IRouteCommand {

    companion object {
        private const val LOG_TAG = "EmailRouting"
    }

    override fun navigate() {
        val mailData = context.getString(
                R.string.email_uri_data,
                Uri.encode(context.getString(emailTo)),
                Uri.encode(context.getString(emailSubject)),
                Uri.encode(context.getString(emailBody))
            )

        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse(mailData)
        }

        try {
            context.startActivity(intent)
        } catch (exception: ActivityNotFoundException) {
            LOG.e(LOG_TAG, exception, "No applications available to send an email!")
        }
    }
}