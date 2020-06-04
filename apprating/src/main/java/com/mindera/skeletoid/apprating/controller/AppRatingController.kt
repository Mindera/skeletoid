package com.mindera.skeletoid.apprating.controller

import android.content.Context
import androidx.fragment.app.FragmentManager
import com.mindera.skeletoid.apprating.callbacks.AppRatingDialogResponse
import com.mindera.skeletoid.apprating.dialogs.AppRatingDialog
import com.mindera.skeletoid.apprating.job.AppRatingJobInitializer
import com.mindera.skeletoid.apprating.store.AppRatingStore
import com.mindera.skeletoid.generic.DateUtils
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
     * Updates the values in the store.
     *
     * @param context Context to access store
     */
    fun updateStore(context: Context) {
        val store = AppRatingStore(context)
        val today = Date()
        store.lastTimePrompted = DateUtils.formatDate(today)

        countsPerTimeInterval?.let { (_, range) ->
            if (store.initialPromptDate.isEmpty() || DateUtils.daysBetween(
                    today,
                    DateUtils.parseDate(store.initialPromptDate)
                ) > range
            ) {
                store.initialPromptDate = DateUtils.formatDate(today)
                //NOTE: This starts with 1 because when it arrives here the dialog was already shown for the first time
                store.promptedCount = 1
            } else {
                store.promptedCount++
            }
        }
    }

    /**
     * Checks if the values in the store matches the conditions to prompt the dialog.
     *
     * @param context Context
     * @return true if the conditions were met
     */
    fun shouldPromptDialog(context: Context): Boolean {
        val store = AppRatingStore(context)

        return !store.alreadyRated &&
                isWithinMaximumCount(store) &&
                hasPassedPromptTimeInterval(store)
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
                schedulePromptDialogJob(context)
            }
        }
    }

    /**
     * Schedules a job to prompt the rating dialog after the promptTimeInterval.
     */
    fun schedulePromptDialogJob(context: Context) {
        promptTimeInterval?.let { AppRatingJobInitializer.schedule(context, it) }
    }

    /**
     * Shows the default app rating dialog.
     *
     * @param fragmentManager Fragment manager used to show dialog
     */
    fun showDefaultDialog(fragmentManager: FragmentManager) {
        AppRatingDialog.newInstance()
            .show(fragmentManager, AppRatingDialog.TAG)
    }

    /**
     * Checks if already reached the maximum amount of times that the rating dialog can be shown in a certain period of time.
     *
     * @param store Store that has the values related with the app rating conditions
     * @return true if the conditions were met
     */
    private fun isWithinMaximumCount(store: AppRatingStore): Boolean {
        val (count, range) = countsPerTimeInterval ?: return true
        if (store.initialPromptDate.isEmpty()) return true

        val initialPromptDate = DateUtils.parseDate(store.initialPromptDate)
        val today = Date()
        val diff = DateUtils.daysBetween(today, initialPromptDate)
        return (diff < range && store.promptedCount < count) || diff > range
    }

    /**
     * Checks if a certain range of time has passed since the last dialog was prompted.
     *
     * @param store Store that has the values related with the app rating conditions
     * @return true if the conditions were met
     */
    private fun hasPassedPromptTimeInterval(store: AppRatingStore): Boolean {
        val timeInterval = promptTimeInterval ?: return true
        if (store.lastTimePrompted.isEmpty()) return true

        val lastTimeDate = DateUtils.parseDate(store.lastTimePrompted)
        val today = Date()

        return DateUtils.daysBetween(today, lastTimeDate) > timeInterval
    }
}
