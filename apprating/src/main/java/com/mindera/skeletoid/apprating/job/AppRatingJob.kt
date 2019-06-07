package com.mindera.skeletoid.apprating.job

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

class AppRatingJob(context: Context, workerParameters: WorkerParameters): Worker(context, workerParameters) {

    override fun doWork(): Result {
        //For now this is doing nothing, just acting like a timer
        //for the activity to listen to the state and prompt a dialog whenever it finishes
        return Result.success()
    }
}
