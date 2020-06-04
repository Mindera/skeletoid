package com.mindera.skeletoid.rxjava.handlers

import com.mindera.skeletoid.logs.LOG
import io.reactivex.functions.Consumer
import io.reactivex.plugins.RxJavaPlugins

class RxDefaultCrashHandler(
    private val crashHandlerConfigurator: CrashConfigurator,
    debugMode: Boolean = false
) :
    Consumer<Throwable> {

    interface CrashConfigurator{
        fun logNonFatalException(t: Throwable)
    }

    companion object {
        private const val LOG_TAG = "RxDefaultCrashHandler"
    }

    init {
        //More info:
        //https://medium.com/@bherbst/the-rxjava2-default-error-handler-e50e0cab6f9f
        //https://blog.danlew.net/2015/12/08/error-handling-in-rxjava/
        RxJavaPlugins.setErrorHandler(this)

        // Disabled since this seems to cause crashes.
        // In debug in will pinpoint the source of the error.. but if the stack if too large it will crash
//        if (debugMode) {
            // https://github.com/akaita/RxJava2Debug
//             RxJava2Debug.enableRxJava2AssemblyTracking(arrayOf(BuildConfig.APPLICATION_ID, "com.smartbox.partner"))
//        }
    }

    override fun accept(t: Throwable?) {
        //Send info to Crash handler as non fatal exception
        t?.let {
            crashHandlerConfigurator.logNonFatalException(t)

            LOG.e(
                LOG_TAG, t, "RxJava FATAL ERROR - caught to avoid crash"
            )
        } ?: LOG.e(
            LOG_TAG, "RxJava FATAL ERROR - caught to avoid crash | NO EXCEPTION!!!"
        )
    }

}
