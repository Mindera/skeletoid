package com.mindera.skeletoid.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import java.io.ByteArrayOutputStream

object BitmapUtils {

    fun encodeToBase64(bitmap: Bitmap): String {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        val byteArray = stream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    fun decodeFromBase64(bitmap64: String): Bitmap {
        val byteArray = Base64.decode(bitmap64, 0)
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }
}