package com.mindera.skeletoid.dialogs

import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import com.mindera.skeletoid.dialogs.AbstractDialogFragment.DialogState.CANCELED
import com.mindera.skeletoid.dialogs.AbstractDialogFragment.DialogState.CLICK_NEGATIVE
import com.mindera.skeletoid.dialogs.AbstractDialogFragment.DialogState.CLICK_NEUTRAL
import com.mindera.skeletoid.dialogs.AbstractDialogFragment.DialogState.CLICK_POSITIVE
import com.mindera.skeletoid.dialogs.AbstractDialogFragment.DialogState.DISMISSED
import com.mindera.skeletoid.logs.LOG

abstract class AbstractDialogFragment : DialogFragment() {

    companion object {
        private const val LOG_TAG = "AbstractDialogFragment"
    }

    private var hasReturnedValueAlready = false

    private var args: Bundle? = null

    enum class DialogState {
        CLICK_POSITIVE,
        CLICK_NEGATIVE,
        CLICK_NEUTRAL,
        DISMISSED,
        CANCELED
    }

    abstract val isShowing: Boolean

    private var targetActivityRequestCode: Int = 0

    interface DialogFragmentHandler {
        fun onDialogResult(requestCode: Int, stateCode: DialogState, parameters: Bundle?)
    }

    /**
     * Flag to determine if this Dialog allows only one of its kind (via tag)
     */
    protected open var isSingleTop = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (targetActivityRequestCode > 0 || targetRequestCode > 0) {
            throw IllegalArgumentException("Must define either a targetActivityRequestCode or a targetFragmentRequestCode")
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupRxBindings()
    }

    protected abstract fun setupRxBindings()

    override fun onDetach() {
        super.onDetach()
        disposeRxBindings()
    }

    protected abstract fun disposeRxBindings()


    protected fun getParameters(): Bundle? {
        return args
    }

    protected fun setParameters(bundle: Bundle) {
        if (args == null) {
            args = Bundle()
        }
        args?.let {
            it.putAll(bundle)
        } ?: LOG.e(LOG_TAG, "Unable to set parameters on bundle")
    }

    fun setTargetActivity(requestCode: Int) {
        targetActivityRequestCode = requestCode
    }

    override fun show(fragmentManager: FragmentManager?, tag: String) {

        if (fragmentManager == null) {
            LOG.e(LOG_TAG, Exception("Check StackTrace -> "),
                    "Fragment.show():: FragmentManager cannot be null")
            return
        }

        val activity = targetFragment?.activity ?: activity

        if (isActivityFinishing(activity)) {
            LOG.e(LOG_TAG, Exception("Invalid state for Activity"),
                    "show(): Fragment Activity cannot be finishing or null...")
            return
        }

        // If true allows only one with this tag to avoid multiple dialogs
        if (isSingleTop && fragmentManager.findFragmentByTag(tag) is AbstractDialogFragment) {
            LOG.e(LOG_TAG, "show(): Dialog already present for $tag")
            return
        }

        val ft = fragmentManager.beginTransaction()
        ft.add(this, tag)

        LOG.d(LOG_TAG, "Committing Dialog transaction ", tag, " for dialog ", this.toString())

        try {
            ft.commit()
        } catch (t: Throwable) {
            LOG.e(LOG_TAG, "[Dialog] Failed to show: $tag")
        }
    }

    private fun isActivityFinishing(activity: FragmentActivity?): Boolean {
        return activity?.isFinishing == true
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        onCancel()
    }

    /**
     * Dismiss the dialog normally, but if the state of the activity had already been saved dismiss
     * dialog allowing state loss.
     */
    override fun dismiss() = try {
        LOG.d("Dismissing dialog ", tag)
        onDismiss()
        super.dismiss()
    } catch (ex: IllegalStateException) {
        LOG.e(LOG_TAG, ex, "Dismissing dialog $tag allowing state loss. Activity = $activity isFinishing = ${activity?.isFinishing
                ?: "null"}")
        super.dismissAllowingStateLoss()
    }

    private fun passEventToTargetActivity(state: DialogState): Boolean {
        if (activity is DialogFragmentHandler) {
            if (targetActivityRequestCode == 0) {
                LOG.e(LOG_TAG, "Invalid request code for activity")
            } else {
                (activity as DialogFragmentHandler).onDialogResult(targetActivityRequestCode, state, getParameters())
                return true
            }
        }
        return false
    }

    private fun passEventToTargetFragment(state: DialogState): Boolean {
        if (targetFragment is DialogFragmentHandler) {
            (targetFragment as DialogFragmentHandler).onDialogResult(targetRequestCode, state, getParameters())
            return true
        }
        return false
    }

    protected fun onPositiveClick() {
        onClick(CLICK_POSITIVE)
    }

    protected fun onNegativeClick() {
        onClick(CLICK_NEGATIVE)
    }

    protected fun onNeutralClick() {
        onClick(CLICK_NEUTRAL)
    }

    protected fun onDismiss() {
        onClick(DISMISSED)
    }

    protected fun onCancel() {
        onClick(CANCELED)
    }

    protected fun onClick(state: DialogState) {
        if (hasReturnedValueAlready) {
            return
        }

        hasReturnedValueAlready = true

        if (!passEventToTargetFragment(state) && !passEventToTargetActivity(state)) {
            LOG.e(LOG_TAG, "Could not propagate event for requestCode: $targetRequestCode with resultCode $state")
        }
    }
}
