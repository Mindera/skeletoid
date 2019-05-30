package com.mindera.skeletoid.apprating.controller

import android.content.Context
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

    fun promptDialog(context: Context): Boolean {
        val store = AppRatingStore(context)
        return shouldPromptDialog(context, store).also {
            if (it) {
                store.promptedCount++
                store.lastTimePrompted = DateUtils.formatDate(Date())
            }
        }
    }

    fun updateRated(context: Context) {
        AppRatingStore(context).apply {
            alreadyRated = true
        }
    }

    private fun shouldPromptDialog(context: Context, store: AppRatingStore): Boolean {
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

            if (DateUtils.daysBetween(today, initialPromptDate) < range) {
                store.promptedCount < count
            } else {
                true
            }
        } ?: true
    }

    private fun checkPromptTimeIntervalCondition(store: AppRatingStore): Boolean {
        return promptTimeInterval?.let {
            val lastTimeDate = DateUtils.parseDate(store.lastTimePrompted)
            val today = Date()

            DateUtils.daysBetween(today, lastTimeDate) > it
        } ?: true
    }
}