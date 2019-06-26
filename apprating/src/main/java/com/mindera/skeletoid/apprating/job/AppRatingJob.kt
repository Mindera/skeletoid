package com.mindera.skeletoid.apprating.job

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.mindera.skeletoid.apprating.AppRatingInitializer
import com.mindera.skeletoid.logs.LOG

class AppRatingJob(private val context: Context, workerParameters: WorkerParameters): Worker(context, workerParameters) {

    companion object {
        /**
         * Tag used in the work. Useful to observe the work status
         */
        const val JOB_TAG = "AppRatingJob"
    }

    override fun doWork(): Result {
        LOG.d(JOB_TAG, "Rating work finishing...")
        AppRatingInitializer.promptDialog(context)
        return Result.success()
    }
}
