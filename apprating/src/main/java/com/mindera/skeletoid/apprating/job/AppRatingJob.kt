package com.mindera.skeletoid.apprating.job

import androidx.work.Worker

class AppRatingJob: Worker() {

    override fun doWork(): Result {
        //For now this is doing nothing, just acting like a timer
        //for the activity to listen to the state and prompt a dialog whenever it finishes
        return Result.SUCCESS
    }
}