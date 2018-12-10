package com.mindera.skeletoid.dialogs

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.DialogFragment
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

        @JvmField
        val ARG_DIALOG_ID = "ARG_DIALOG_ID"

        @JvmField
        val ARG_OWNER_FRAGMENT = "ARG_OWNER_FRAGMENT"
    }

    enum class DialogState {
        CLICK_POSITIVE,
        CLICK_NEGATIVE,
        CLICK_NEUTRAL,
        DISMISSED,
        CANCELED
    }

    abstract val isShowing: Boolean

    protected var targetActivityRequestCode: Int = 0

    interface DialogFragmentHandler {
        fun onDialogResult(requestCode: Int, stateCode: DialogState, intent: Intent?)
    }

    /**
     * This dialog Id.
     */
    private var dialogId: String? = null
        set(value) {
            field = value
            arguments?.let { arguments ->
                arguments.putString(ARG_DIALOG_ID, value)
                setArguments(arguments)
            }
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
            return@show
        }

        targetFragment?.activity?.let {
            if (it.isFinishing) {
                LOG.e(LOG_TAG, Exception("Check StackTrace -> "),
                        "Fragment.show():: Fragment Activity cannot be finishing...")
                return@show
            }
        }

        activity?.let {
            if (it.isFinishing) {
                LOG.e(LOG_TAG, Exception("Check StackTrace -> "),
                        "Fragment.show():: Activity cannot be null")
                return@show
            }
        }

        // If true allows only one with this tag to avoid multiple dialogs
        if (isSingleTop && fragmentManager.findFragmentByTag(tag) is AbstractDialogFragment) {
            LOG.e(LOG_TAG, "Fragment.show():: Dialog already present for $tag")
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
        LOG.d("Dismissing dialog ", dialogId)
        onDismiss()
        super.dismiss()
    } catch (ex: IllegalStateException) {
        LOG.e(LOG_TAG, ex, "Dismissing dialog $dialogId allowing state loss. Activity = $activity isFinishing = ${activity?.isFinishing
                ?: "null"}")
        super.dismissAllowingStateLoss()
    }

    private fun passEventToParentActivity(state: DialogState): Boolean {
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

    protected fun passEventToParentFragment(state: DialogState): Boolean {
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

    private fun onClick(state: DialogState) {
        if (!passEventToParentFragment(state)) {
            if (!passEventToParentActivity(state)) {
                LOG.e(LOG_TAG, "Could not propagate event for requestCode: $targetRequestCode with resultCode $state")
            }
        }
    }
}
