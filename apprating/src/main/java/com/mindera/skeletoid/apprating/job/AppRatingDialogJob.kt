package com.mindera.skeletoid.apprating.job

import androidx.work.Worker

class AppRatingDialogJob: Worker() {

    override fun doWork(): Result {
        return Result.SUCCESS
    }
}