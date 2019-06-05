package com.mindera.skeletoid.fonts

import android.content.Context
import android.content.res.AssetManager
import android.graphics.Typeface
import android.text.style.CharacterStyle
import android.widget.TextView

interface CustomFontProvider {

    fun initialise(context: Context?)

    fun getTypeface(assetManager: AssetManager, fontPath: String): Typeface?

    fun getTypefaceSpan(assetManager: AssetManager, fontPath: String): CharacterStyle?

    fun getRegularFontSpan(context: Context?): CharacterStyle?

    fun getLightFontSpan(context: Context?): CharacterStyle?

    fun getBoldFontSpan(context: Context?): CharacterStyle?

    fun getSemiBoldFontSpan(context: Context?): CharacterStyle?

    fun applyFontToTextView(context: Context?, textView: TextView, fontPath: String)

}