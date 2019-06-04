package com.mindera.skeletoid.apprating.job

import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

object AppRatingJobInitializer {

    const val JOB_TAG = "AppRatingJob"

    fun schedule(duration: Long) {
        val job = OneTimeWorkRequest.Builder(AppRatingDialogJob::class.java)
            .setInitialDelay(duration, TimeUnit.DAYS)
            .addTag(JOB_TAG)
            .build()

        WorkManager.getInstance().enqueue(job)
    }

}