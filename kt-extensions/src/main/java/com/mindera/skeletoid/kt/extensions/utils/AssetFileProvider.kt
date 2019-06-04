package com.mindera.skeletoid.kt.extensions.utils

import android.content.Context
import java.nio.charset.Charset

object AssetFileProvider {

    fun readFile(context: Context, assetFile: String): String {
        return context.assets
            .open(assetFile)
            .readBytes()
            .toString(Charset.defaultCharset())
    }
}