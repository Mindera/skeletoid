package com.mindera.skeletoid.apprating

import android.content.Context
import com.mindera.skeletoid.apprating.callbacks.AppRatingDialogCallback
import com.mindera.skeletoid.apprating.callbacks.DialogResponse
import com.mindera.skeletoid.apprating.callbacks.DialogResponseCallback
import com.mindera.skeletoid.apprating.controller.AppRatingController

object AppRatingInitializer {

    private val controller: AppRatingController by lazy {
        AppRatingController()
    }

    fun init(countsPerTimeInterval: Pair<Int, Long>, promptTimeInterval: Long) {
        controller.setupConditions(countsPerTimeInterval, promptTimeInterval)
    }

    fun promptDialog(context: Context, callback: AppRatingDialogCallback, dialogResultCallback: DialogResponseCallback? = null) {
        if (controller.promptDialog(context) && dialogResultCallback != null){
            callback.showRatingDialog()
        }
    }

    fun handleDialogResponse(context: Context, response: DialogResponse) {
        controller.handleDialogResponse(context, response)
    }
}