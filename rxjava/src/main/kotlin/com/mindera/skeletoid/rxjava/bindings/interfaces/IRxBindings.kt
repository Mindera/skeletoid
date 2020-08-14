package com.mindera.skeletoid.rxjava.bindings.interfaces

interface IRxBindings {

    fun setupRxBindings()

    /**
     * Clear is meant to be used onPause for example, just to clear current disposables that can
     * be reused. This follows the same pattern as the clear() method of disposables
     */
    fun clearRxBindings()

    /**
     * Dispose is meant to be used on final states: like onStop / onDestroy it's irreversible
     * This follows the same pattern as the dispose() method of disposables
     */
    fun disposeRxBindings()
}