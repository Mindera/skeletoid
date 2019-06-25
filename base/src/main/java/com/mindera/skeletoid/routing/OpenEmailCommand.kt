package com.mindera.skeletoid.routing

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.mindera.skeletoid.R

class OpenEmailCommand(
    private val context: Context,
    private val emailTo: Int,
    private val emailSubject: Int,
    private val emailBody: Int
) : IRouteCommand {

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

        context.startActivity(intent)
    }
}