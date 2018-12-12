package com.mindera.skeletoid.dialogs

import android.content.DialogInterface
import android.content.Intent
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
        fun onDialogResult(requestCode: Int, stateCode: DialogState, intent: Intent?)
    }

    /**
     * Flag to determine if this Dialog allows only one of its kind (via tag)
     */
    protected open var isSingleTop = false

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

        if (isActivityFinishingOrNull(activity)) {
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

    private fun isActivityFinishingOrNull(activity: FragmentActivity?): Boolean {
        return activity?.isFinishing ?: true
    }

    override fun onDismiss(dialog: DialogInterface?) {
        super.onDismiss(dialog)
        onDismiss()
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
                (activity as DialogFragmentHandler).onDialogResult(targetActivityRequestCode, state, getContentIntent())
                return true
            }
        }

        return false
    }

    protected fun passEventToTargetFragment(state: DialogState): Boolean {
        if (targetFragment is DialogFragmentHandler) {
            (targetFragment as DialogFragmentHandler).onDialogResult(targetRequestCode, state, getContentIntent())
            return true
        }
        return false
    }

    private fun getContentIntent(): Intent? {
        val intent = activity?.intent
        arguments?.let {
            intent?.putExtras(it)
        }
        return intent
    }

    protected fun setParameters(bundle: Bundle) {
        activity?.intent?.putExtras(bundle) ?: LOG.e(LOG_TAG, "setParameters: intent is null, unable to add bundle")
    }

    private fun onPositiveClick() {
        onClick(CLICK_POSITIVE)
    }

    private fun onNegativeClick() {
        onClick(CLICK_NEGATIVE)
    }

    private fun onNeutralClick() {
        onClick(CLICK_NEUTRAL)
    }

    private fun onDismiss() {
        onClick(DISMISSED)
    }

    private fun onCancel() {
        onClick(CANCELED)
    }

    private fun onClick(state: DialogState) {
        if (!passEventToTargetFragment(state) && !passEventToTargetActivity(state)) {
            LOG.e(LOG_TAG, "Could not propagate event for requestCode: $targetRequestCode with resultCode $state")
        }
    }
}