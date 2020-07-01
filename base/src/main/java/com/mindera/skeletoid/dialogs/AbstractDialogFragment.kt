package com.mindera.skeletoid.dialogs

import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.mindera.skeletoid.dialogs.AbstractDialogFragment.DialogState.CANCELED
import com.mindera.skeletoid.dialogs.AbstractDialogFragment.DialogState.CLICK_NEGATIVE
import com.mindera.skeletoid.dialogs.AbstractDialogFragment.DialogState.CLICK_NEUTRAL
import com.mindera.skeletoid.dialogs.AbstractDialogFragment.DialogState.CLICK_POSITIVE
import com.mindera.skeletoid.dialogs.AbstractDialogFragment.DialogState.DISMISSED
import com.mindera.skeletoid.logs.LOG
import org.jetbrains.annotations.NotNull

abstract class AbstractDialogFragment : IntermediateDialog() {

    companion object {
        private const val LOG_TAG = "AbstractDialogFragment"

        private const val HAS_RETURNED_VALUE_KEY = "HAS_RETURNED_VALUE_KEY"
        private const val ARGS_KEY = "ARGS_KEY"
        private const val TARGET_ACTIVITY_KEY = "TARGET_ACTIVITY_KEY"
    }

    abstract val isShowing: Boolean

    private var hasReturnedValueAlready = false

    //This is a bundle of parameters that will be resent out of the dialog via onDialogResult.
    //They are kept in a different bundle to maintain this bundle just with user related values
    private var args: Bundle? = null

    private var targetActivityRequestCode: Int = -1

    enum class DialogState {
        CLICK_POSITIVE,     //Positive button clicked on dialog
        CLICK_NEGATIVE,     //Negative button clicked on dialog
        CLICK_NEUTRAL,      //Neutral button clicked on dialog
        DISMISSED,          //Dialog dismissed
        CANCELED,           //Dialog cancelled
    }

    interface DialogFragmentHandler {
        fun onDialogResult(requestCode: Int, stateCode: DialogState, parameters: Bundle?)
    }

    /**
     * Flag to determine if this Dialog allows only one of its kind (via tag)
     */
    protected open var isSingleTop = false

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        savedInstanceState?.let { savedInstance ->
            hasReturnedValueAlready = savedInstance.getBoolean(HAS_RETURNED_VALUE_KEY)
            args = savedInstance.getBundle(ARGS_KEY)
            targetActivityRequestCode = savedInstance.getInt(TARGET_ACTIVITY_KEY)
        }
        setupRxBindings()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putBoolean(HAS_RETURNED_VALUE_KEY, hasReturnedValueAlready)
        outState.putBundle(ARGS_KEY, args)
        outState.putInt(TARGET_ACTIVITY_KEY, targetActivityRequestCode)
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
        args?.putAll(bundle) ?: LOG.e(LOG_TAG, "Unable to set parameters on bundle")
    }

    fun setTargetActivity(requestCode: Int) {
        targetActivityRequestCode = requestCode
    }

    override fun show(@NotNull fragmentManager: FragmentManager, tag: String) {

        require(!(!hasValidTargetFragment() && !hasValidTargetActivity())) { "Must define either a targetActivityRequestCode or a targetFragmentRequestCode" }

        if (targetFragment?.isVisible == false) {
            LOG.e(LOG_TAG, "Fragment is not visible, ignoring to avoid crash...")
            return
        }

        val activity = targetFragment?.activity ?: activity

        if (isActivityFinishing(activity)) {
            LOG.e(
                LOG_TAG, Exception("Invalid state for Activity"),
                "show(): Fragment Activity cannot be finishing or null..."
            )
            return
        }

        // If true allows only one with this tag to avoid multiple dialogs
        if (isSingleTop && fragmentManager.findFragmentByTag(tag) is AbstractDialogFragment) {
            LOG.e(LOG_TAG, "show(): Dialog already present for $tag")
            return
        }


        LOG.d(LOG_TAG, "Showing dialog ", tag, " for dialog ", this.toString())

        try {
            super.show(fragmentManager, tag)
        } catch (t: Throwable) {
            LOG.e(LOG_TAG, "[Dialog] Failed to show: $tag")
        }
    }

    private fun hasValidTargetFragment(): Boolean {
        return targetFragment != null && targetRequestCode >= 0
    }

    private fun hasValidTargetActivity(): Boolean {
        return targetActivityRequestCode >= 0
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
        LOG.d("Dismissing dialog ", tag ?: "UNKNOWN Fragment Tag")
        onDismiss()
        super.dismiss()
    } catch (ex: IllegalStateException) {
        LOG.e(LOG_TAG, ex, "Dismissing dialog $tag allowing state loss. Activity = $activity isFinishing = ${activity?.isFinishing
                ?: "null"}")
        super.dismissAllowingStateLoss()
    }

    private fun passEventToTargetActivity(state: DialogState): Boolean {
        if (activity is DialogFragmentHandler) {
            if (targetActivityRequestCode >= 0) {
                (activity as DialogFragmentHandler).onDialogResult(targetActivityRequestCode, state, getParameters())
                return true
            } else {
                //This logs should use getTag()
                LOG.e(LOG_TAG, "Invalid targetActivityRequestCode: $targetActivityRequestCode")
            }
        } else {
            LOG.e(LOG_TAG, "Activity is not DialogFragmentHandler, ignoring event...")
        }
        return false
    }

    private fun passEventToTargetFragment(state: DialogState): Boolean {
        if (targetFragment is DialogFragmentHandler) {
            (targetFragment as DialogFragmentHandler).onDialogResult(targetRequestCode, state, getParameters())
            return true
        } else {
            LOG.e(LOG_TAG, "Fragment is not DialogFragmentHandler, ignoring event...")
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
