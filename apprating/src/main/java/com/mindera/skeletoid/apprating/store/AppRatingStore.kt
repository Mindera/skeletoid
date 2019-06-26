package com.mindera.skeletoid.apprating.store

import android.content.Context
import android.content.SharedPreferences

class AppRatingStore {

    companion object {
        private const val SHARED_PREFS_FILE = "google_play_review_preferences"

        private const val HAS_REVIEWED_KEY = "has_reviewed_key"
        private const val INITIAL_PROMPT_DATE_KEY = "initial_prompt_date_key"
        private const val PROMPTED_COUNT_KEY = "prompted_count_key"
        private const val LAST_TIME_PROMPTED_KEY = "last_time_prompted_key"
    }

    private val sharedPreferences: SharedPreferences

    constructor(context: Context) {
        this.sharedPreferences = instantiateSharedPreferences(context)
    }

    constructor(sharedPreferences: SharedPreferences) {
        this.sharedPreferences = sharedPreferences
    }

    private fun instantiateSharedPreferences(context: Context): SharedPreferences = context.getSharedPreferences(SHARED_PREFS_FILE, Context.MODE_PRIVATE)

    var alreadyRated: Boolean
        get() = sharedPreferences.getBoolean(HAS_REVIEWED_KEY, false)
        set(hasReviewed) {
            val editor = sharedPreferences.edit()
            editor.putBoolean(HAS_REVIEWED_KEY, hasReviewed)
            editor.apply()
        }

    var initialPromptDate: String
        get() = sharedPreferences.getString(INITIAL_PROMPT_DATE_KEY, "").orEmpty()
        set(initialPromptDate) {
            val editor = sharedPreferences.edit()
            editor.putString(INITIAL_PROMPT_DATE_KEY, initialPromptDate)
            editor.apply()
        }

    var promptedCount: Int
        get() = sharedPreferences.getInt(PROMPTED_COUNT_KEY, 0)
        set(promptedCount) {
            val editor = sharedPreferences.edit()
            editor.putInt(PROMPTED_COUNT_KEY, promptedCount)
            editor.apply()
        }

    var lastTimePrompted: String
        get() = sharedPreferences.getString(LAST_TIME_PROMPTED_KEY, "").orEmpty()
        set(lastTimePrompted) {
            val editor = sharedPreferences.edit()
            editor.putString(LAST_TIME_PROMPTED_KEY, lastTimePrompted)
            editor.apply()
        }
}
