package com.mindera.skeletoid.apprating

import android.content.Context
import com.mindera.skeletoid.apprating.callback.AppRatingDialogCallback
import com.mindera.skeletoid.apprating.controller.AppRatingController

object AppRatingInitializer {

    private val controller: AppRatingController by lazy {
        AppRatingController()
    }

    fun init(countsPerTimeInterval: Pair<Int, Long>, promptTimeInterval: Long) {
        controller.setupConditions(countsPerTimeInterval, promptTimeInterval)
    }

    fun promptDialog(context: Context, callback: AppRatingDialogCallback) {
        if (controller.promptDialog(context)){
            callback.showRatingDialog()
        }
    }

    fun ratedDialog(context: Context) {
        //TODO: update store; setup job to update flag in 30 days
        controller.updateRated(context)
    }
}