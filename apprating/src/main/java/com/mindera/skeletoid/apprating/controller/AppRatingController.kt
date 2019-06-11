package com.mindera.skeletoid.apprating.controller

import android.content.Context
import com.mindera.skeletoid.apprating.callbacks.AppRatingDialogResponse
import com.mindera.skeletoid.apprating.callbacks.AppRatingDialogResponseCallback
import com.mindera.skeletoid.apprating.job.AppRatingJobInitializer
import com.mindera.skeletoid.apprating.store.AppRatingStore
import com.mindera.skeletoid.apprating.utils.DateUtils
import java.util.Date

/**
 * Controller responsible for handling the stored data and the dialog itself
 */
class AppRatingController {

    private var countsPerTimeInterval: Pair<Int, Long>? = null
    private var promptTimeInterval: Long? = null

    /**
     * Sets up the conditions to prompt the dialog.
     *
     * @param countsPerTimeInterval Pair<Int, Long> values with the maximum number of times the dialog can be prompt per time range (in days)
     * @param promptTimeInterval Time distance between prompts (in days)
     */
    fun setupConditions(countsPerTimeInterval: Pair<Int, Long>, promptTimeInterval: Long) {
        this.countsPerTimeInterval = countsPerTimeInterval
        this.promptTimeInterval = promptTimeInterval
    }

    /**
     * Checks if the data in the store matches the conditions and updates the values in the store.
     *
     * @param context Context
     * @param dialogResultCallback Callback to handle default dialog response
     * @return true if the conditions were met, and the dialog can be prompted.
     */
    fun promptDialog(context: Context, dialogResultCallback: AppRatingDialogResponseCallback? = null): Boolean {
        val store = AppRatingStore(context)
        return shouldPromptDialog(store).also { shouldPromptDialog ->
            if (shouldPromptDialog) {
                dialogResultCallback?.let {
                    TODO("IMPLEMENT DEFAULT DIALOG")
                }
                store.promptedCount++
                store.lastTimePrompted = DateUtils.formatDate(Date())
            }
        }
    }

    /**
     * Handles the response of the dialog by updating the values in the store.
     * If response is RATE_LATER it should schedule a one time work that starts after a delay equal to the promptTimeInterval (in days).
     * If response is RATE_NOW or NEVER_RATE it simply updates the store.
     *
     * @param context Context
     * @param response Selected response to the dialog
     */
    fun handleDialogResponse(context: Context, response: AppRatingDialogResponse) {
        val store = AppRatingStore(context)
        when (response) {
            AppRatingDialogResponse.RATE_NOW, AppRatingDialogResponse.NEVER_RATE -> {
                store.alreadyRated = true
            }
            AppRatingDialogResponse.RATE_LATER -> {
                promptTimeInterval?.let { AppRatingJobInitializer.schedule(it) }
            }
        }
    }

    /**
     * Checks if the values in the store matches the conditions to prompt the dialog.
     *
     * @param store Store that has the values related with the app rating conditions
     * @return true if the conditions were met
     */
    private fun shouldPromptDialog(store: AppRatingStore): Boolean {
        return if (store.initialPromptDate.isEmpty()) {
            store.initialPromptDate = DateUtils.formatDate(Date())
            store.promptedCount = 0
            store.alreadyRated = false
            true
        } else {
            !store.alreadyRated &&
                    isWithinMaximumCount(store) &&
                    hasPassedPromptTimeInterval(store)
        }
    }

    /**
     * Checks if already reached the maximum amount of times that the rating dialog can be shown in a certain period of time.
     *
     * @param store Store that has the values related with the app rating conditions
     * @return true if the conditions were met
     */
    private fun isWithinMaximumCount(store: AppRatingStore): Boolean {
        return countsPerTimeInterval?.let { (count, range) ->
            val initialPromptDate = DateUtils.parseDate(store.initialPromptDate)
            val today = Date()

            if (DateUtils.daysBetween(today, initialPromptDate) < range) {
                store.promptedCount < count
            } else {
                store.initialPromptDate = DateUtils.formatDate(Date())
                store.promptedCount = 0
                true
            }
        } ?: true
    }

    /**
     * Checks if a certain range of time has passed since the last dialog was prompted.
     *
     * @param store Store that has the values related with the app rating conditions
     * @return true if the conditions were met
     */
    private fun hasPassedIPromptTimeInterval(store: AppRatingStore): Boolean {
        return promptTimeInterval?.let {
            val lastTimeDate = DateUtils.parseDate(store.lastTimePrompted)
            val today = Date()

            DateUtils.daysBetween(today, lastTimeDate) > it
        } ?: true
    }
}
