package com.mindera.skeletoid.generic

import android.os.Build
import android.text.Html
import android.text.Spanned

object HtmlUtils {

    @Suppress("DEPRECATION")
    fun fromHtml(source: String?): Spanned {
        val formattedResult = source?.replace("\r", "<br>")
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(formattedResult, Html.FROM_HTML_MODE_LEGACY)
        } else {
            Html.fromHtml(formattedResult)
        }
    }
}