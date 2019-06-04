package com.mindera.skeletoid.apprating.job

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

class AppRatingDialogJob(context: Context, workerParameters: WorkerParameters): Worker(context, workerParameters) {

    override fun doWork(): Result {
        return Result.success()
    }
}