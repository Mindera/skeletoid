package com.mindera.skeletoid.apprating.job

import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.mindera.skeletoid.apprating.job.AppRatingJob.Companion.JOB_TAG
import java.util.concurrent.TimeUnit

object AppRatingJobInitializer {

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
