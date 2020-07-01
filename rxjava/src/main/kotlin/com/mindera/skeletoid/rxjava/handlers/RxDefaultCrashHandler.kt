package com.mindera.skeletoid.rxjava.handlers

import com.akaita.java.rxjava2debug.RxJava2Debug
import com.mindera.skeletoid.logs.LOG
import io.reactivex.functions.Consumer
import io.reactivex.plugins.RxJavaPlugins

class RxDefaultCrashHandler(
    private val crashHandlerConfigurator: CrashConfigurator,
    packageNames: List<String>,
    debugMode: Boolean = false
) : Consumer<Throwable> {

    interface CrashConfigurator {
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

        // Beware this can cause crashes.
        // In debug in will pinpoint the source of the error.. but if the stack if too large it will crash
        if (debugMode) {
            // https://github.com/akaita/RxJava2Debug
            RxJava2Debug.enableRxJava2AssemblyTracking(packageNames.toTypedArray())
        }
    }

    override fun accept(throwable: Throwable?) {
        // Send info to crash handler as non fatal exception
        when (throwable) {
            null -> LOG.e(LOG_TAG, "RxJava FATAL ERROR - caught to avoid crash | NO EXCEPTION")
            else -> {
                crashHandlerConfigurator.logNonFatalException(throwable)
                LOG.e(LOG_TAG, throwable, "RxJava FATAL ERROR - caught to avoid crash")
            }
        }
    }

}
