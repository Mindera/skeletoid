package com.mindera.skeletoid.threads.utils

import java.lang.ref.WeakReference

class WeakRunnable(runnable: Runnable) : Runnable {
    private val delegateRunnable: WeakReference<Runnable> = WeakReference(runnable)
    override fun run() {
        val runnable = delegateRunnable.get()
        runnable?.run()
    }

}