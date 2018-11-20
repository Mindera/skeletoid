@file:Suppress("NOTHING_TO_INLINE")

package com.mindera.skeletoid.rxbindings.extensions

import android.view.inputmethod.EditorInfo
import android.widget.EditText
import com.jakewharton.rxbinding2.widget.editorActionEvents
import io.reactivex.Observable


inline fun EditText.imeActionDone(): Observable<Unit> {
    return editorActionEvents()
            .filter { it.actionId() == EditorInfo.IME_ACTION_DONE }
            .map { Unit }
}



