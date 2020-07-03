package com.mindera.skeletoid.threads.utils

import java.lang.ref.WeakReference

class WeakRunnable(block: () -> Unit) : Runnable {
    private val it: WeakReference<() -> Unit> = WeakReference(block)
    override fun run() { it.get()?.invoke() }
}