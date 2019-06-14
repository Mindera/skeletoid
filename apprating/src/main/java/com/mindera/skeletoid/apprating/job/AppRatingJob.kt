package com.mindera.skeletoid.apprating.job

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.mindera.skeletoid.logs.LOG

class AppRatingJob(context: Context, workerParameters: WorkerParameters): Worker(context, workerParameters) {

    companion object {
        /**
         * Tag used in the work. Useful to observe the work status
         */
        const val JOB_TAG = "AppRatingJob"
    }

    override fun doWork(): Result {
        //For now this is doing nothing, just acting like a timer
        //for the activity to listen to the state and prompt a dialog whenever it finishes
        LOG.d(JOB_TAG, "Rating work finishing...")
        return Result.success()
    }
}
