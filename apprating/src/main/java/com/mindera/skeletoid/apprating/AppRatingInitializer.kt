package com.mindera.skeletoid.apprating

import android.content.Context
import com.mindera.skeletoid.apprating.callbacks.AppRatingDialogCallback
import com.mindera.skeletoid.apprating.callbacks.AppRatingDialogResponse
import com.mindera.skeletoid.apprating.callbacks.AppRatingDialogResponseCallback
import com.mindera.skeletoid.apprating.controller.AppRatingController

/**
 * Entry point to setup and call the app rating dialog
 */
object AppRatingInitializer {

    private val controller: AppRatingController by lazy {
        AppRatingController()
    }

    /**
     * Sets up the conditions to prompt the dialog.
     * If it's not called, promptDialog will prompt the rating dialog immediately.
     *
     * @param countsPerTimeInterval Pair<Int, Long> values with the maximum number of times the dialog can be prompt per time range (in days)
     * @param promptTimeInterval Time distance between prompts (in days)
     */
    fun init(countsPerTimeInterval: Pair<Int, Long>, promptTimeInterval: Long) {
        controller.setupConditions(countsPerTimeInterval, promptTimeInterval)
    }

    /**
     * Checks if it can prompt the rating dialog, and if it can, shows the default dialog/prompts the callback to show the dialog.
     * Only one of the callbacks can be null.
     *
     * @param context Context
     * @param callback Callback to show the rating dialog. Null if it uses the default dialog
     * @param dialogResultCallback Callback to handle responses to the default dialog. Null if it uses a custom dialog.
     */
    fun promptDialog(context: Context, callback: AppRatingDialogCallback? = null, dialogResultCallback: AppRatingDialogResponseCallback? = null) {
        if (controller.promptDialog(context)){
            when {
                callback != null && dialogResultCallback == null -> callback.showRatingDialog()
                dialogResultCallback != null && callback == null -> TODO("IMPLEMENT DEFAULT DIALOG")
                else -> throw IllegalArgumentException("Should have one non-nullable callback")
            }
        }
    }

    /**
     * Handles the response from the custom dialog, updating the store.
     *
     * @param context Context
     * @param response Response to the dialog
     */
    fun handleDialogResponse(context: Context, response: AppRatingDialogResponse) {
        controller.handleDialogResponse(context, response)
    }
}