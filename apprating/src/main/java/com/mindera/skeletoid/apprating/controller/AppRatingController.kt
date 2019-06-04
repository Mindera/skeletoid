package com.mindera.skeletoid.apprating.controller

import android.content.Context
import com.mindera.skeletoid.apprating.callbacks.DialogResponse
import com.mindera.skeletoid.apprating.callbacks.DialogResponseCallback
import com.mindera.skeletoid.apprating.job.AppRatingJobInitializer
import com.mindera.skeletoid.apprating.store.AppRatingStore
import com.mindera.skeletoid.apprating.utils.DateUtils
import java.util.Date

class AppRatingController {

    private var countsPerTimeInterval: Pair<Int, Long>? = null
    private var promptTimeInterval: Long? = null

    fun setupConditions(countsPerTimeInterval: Pair<Int, Long>, promptTimeInterval: Long) {
        this.countsPerTimeInterval = countsPerTimeInterval
        this.promptTimeInterval = promptTimeInterval
    }

    fun promptDialog(context: Context, dialogResultCallback: DialogResponseCallback? = null): Boolean {
        val store = AppRatingStore(context)
        return shouldPromptDialog(store).also {
            if (it) {
                dialogResultCallback?.let {
                    //TODO: SHOW DEFAULT DIALOG
                }
                store.promptedCount++
                store.lastTimePrompted = DateUtils.formatDate(Date())
            }
        }
    }

    fun handleDialogResponse(context: Context, response: DialogResponse) {
        val store = AppRatingStore(context)
        when (response) {
            DialogResponse.RATE, DialogResponse.NEVER_RATE -> {
                store.alreadyRated = true
            }
            DialogResponse.RATE_LATER -> {
                promptTimeInterval?.let { AppRatingJobInitializer.schedule(it) }
            }
        }
    }

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

    private fun checkPromptTimeIntervalCondition(store: AppRatingStore): Boolean {
        return promptTimeInterval?.let {
            val lastTimeDate = DateUtils.parseDate(store.lastTimePrompted)
            val today = Date()

            DateUtils.daysBetween(lastTimeDate, today) > it
        } ?: true
    }
}