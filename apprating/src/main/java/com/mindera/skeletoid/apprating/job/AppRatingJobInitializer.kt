package com.mindera.skeletoid.apprating.job

import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

object AppRatingJobInitializer {

    /**
     * Tag used in the work. Useful to observe the work status
     */
    const val JOB_TAG = "AppRatingJob"

    /**
     * Schedules a work that runs after a delay
     *
     * @param delay Delay used to start the work
     */
    fun schedule(delay: Long) {
        val job = OneTimeWorkRequest.Builder(AppRatingJob::class.java)
            .setInitialDelay(delay, TimeUnit.DAYS)
            .addTag(JOB_TAG)
            .build()

        WorkManager.getInstance().enqueue(job)
    }
}
