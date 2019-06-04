package com.mindera.skeletoid.apprating.controller

import android.content.Context
import com.mindera.skeletoid.apprating.callbacks.AppRatingDialogResponse
import com.mindera.skeletoid.apprating.callbacks.AppRatingDialogResponseCallback
import com.mindera.skeletoid.apprating.job.AppRatingJobInitializer
import com.mindera.skeletoid.apprating.store.AppRatingStore
import com.mindera.skeletoid.apprating.utils.DateUtils
import java.util.Date

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
        return shouldPromptDialog(store).also {
            if (it) {
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
     *
     * @param context Context
     * @param response Selected response to the dialog
     */
    fun handleDialogResponse(context: Context, response: AppRatingDialogResponse) {
        val store = AppRatingStore(context)
        when (response) {
            AppRatingDialogResponse.RATE, AppRatingDialogResponse.NEVER_RATE -> {
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
                    checkCountPerTimeCondition(store) &&
                    checkPromptTimeIntervalCondition(store)
        }
    }

    /**
     * Checks if already reached the maximum amount of time that the rating dialog can be shown in a certain period of time.
     *
     * @param store Store that has the values related with the app rating conditions
     * @return true if the conditions were met
     */
    private fun checkCountPerTimeCondition(store: AppRatingStore): Boolean {
        return countsPerTimeInterval?.let { (count, range) ->
            val initialPromptDate = DateUtils.parseDate(store.initialPromptDate)
            val today = Date()

            if (DateUtils.daysBetween(initialPromptDate, today) < range) {
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
    private fun checkPromptTimeIntervalCondition(store: AppRatingStore): Boolean {
        return promptTimeInterval?.let {
            val lastTimeDate = DateUtils.parseDate(store.lastTimePrompted)
            val today = Date()

            DateUtils.daysBetween(lastTimeDate, today) > it
        } ?: true
    }
}