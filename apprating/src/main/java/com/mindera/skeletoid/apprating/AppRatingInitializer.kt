package com.mindera.skeletoid.apprating

import android.content.Context
import androidx.fragment.app.FragmentManager
import com.mindera.skeletoid.apprating.callbacks.AppRatingDialogCallback
import com.mindera.skeletoid.apprating.callbacks.AppRatingDialogResponse
import com.mindera.skeletoid.apprating.callbacks.AppRatingDialogResponseCallback
import com.mindera.skeletoid.apprating.controller.AppRatingController
import com.mindera.skeletoid.logs.LOG

/**
 * Entry point to setup and call the app rating dialog
 */
object AppRatingInitializer {

    private const val LOG_TAG = "AppRatingInitializer"

    private val controller: AppRatingController by lazy {
        AppRatingController()
    }

    private var callback: AppRatingDialogCallback? = null

    /**
     * Sets up the conditions to prompt the dialog.
     * If it's not called, promptDialog will prompt the rating dialog immediately.
     *
     * @param context Context
     * @param callback Callback to show the rating dialog. Null if it uses the default dialog
     * @param countsPerTimeInterval Pair<Int, Long> values with the maximum number of times the dialog can be prompt per time range (in days)
     * @param promptTimeInterval Time distance between prompts (in days)
     * @param shouldSchedulePrompt Flag to init a time driven prompt
     */
    fun init(
        context: Context,
        callback: AppRatingDialogCallback? = null,
        countsPerTimeInterval: Pair<Int, Long> = Pair(
            context.resources.getInteger(R.integer.maximum_counts),
            context.resources.getInteger(R.integer.maximum_counts_time_interval).toLong()
        ),
        promptTimeInterval: Long = context.resources.getInteger(R.integer.minimum_time_between_prompt).toLong(),
        shouldSchedulePrompt: Boolean = false
    ) {
        callback?.let { this.callback = it }
        controller.setupConditions(countsPerTimeInterval, promptTimeInterval)
        if (shouldSchedulePrompt) {
            controller.schedulePromptDialogJob()
        }
    }

    /**
     * Checks if it can prompt the rating dialog, and if it can, shows the default dialog/prompts the callback to show the dialog.
     * Only one of the callbacks can be null.
     *
     * @param context Context
     * @param fragmentManager Fragment Manager to show default dialog
     */
    fun promptDialog(
        context: Context,
        fragmentManager: FragmentManager? = null
    ) {
        if (controller.shouldPromptDialog(context)) {
            controller.updateStore(context)
            when {
                callback != null && fragmentManager == null -> callback.showRatingDialog()
                callback == null && fragmentManager != null -> controller.showDefaultDialog(fragmentManager)
                else -> throw IllegalArgumentException("Should have one nullable and one non-nullable callback")
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
