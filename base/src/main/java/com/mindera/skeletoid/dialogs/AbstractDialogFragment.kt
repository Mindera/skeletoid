package com.mindera.skeletoid.dialogs

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentManager
import com.mindera.skeletoid.logs.LOG

abstract class AbstractDialogFragment : DialogFragment() {

    companion object {
        private const val LOG_TAG = "AbstractDialogFragment"

        @JvmField
        val ARG_DIALOG_ID = "ARG_DIALOG_ID"

        @JvmField
        val ARG_OWNER_FRAGMENT = "ARG_OWNER_FRAGMENT"

        const val RESULT_POSITIVE = -1
        const val RESULT_CANCELED = 0
        const val RESULT_NEUTRAL = 1
        const val RESULT_DISMISS = 2
        const val RESULT_NEGATIVE = 3
    }

    abstract val isShowing: Boolean

    interface DialogFragmentHandler {
        fun onDialogResult(requestCode: Int, resultCode: Int, intent: Intent?)
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
     * The fragment owner of this dialog (null if the Activity is the handler)
     */
    private var ownerFragment: String? = null

    /**
     * Flag to determine if this Fragment allows only one of its kind (via tag)
     */
    protected open var isSingleTop = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ownerFragment = arguments?.getString(ARG_OWNER_FRAGMENT)
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

    private fun passEventToParentActivity(result: Int): Boolean {
        if (activity is DialogFragmentHandler) {
            (activity as DialogFragmentHandler).onDialogResult(targetRequestCode, result, getContentIntent())
            return true
        }

        return false
    }

    protected fun passEventToParentFragment(result: Int): Boolean {
        if (targetFragment is DialogFragmentHandler) {
            (targetFragment as DialogFragmentHandler).onDialogResult(targetRequestCode, result, getContentIntent())
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
        onClick(RESULT_POSITIVE)
    }

    protected fun onNegativeClick() {
        onClick(RESULT_NEGATIVE)
    }

    protected fun onNeutralClick() {
        onClick(RESULT_NEUTRAL)
    }

    protected fun onDismiss() {
        onClick(RESULT_DISMISS)
    }

    protected fun onCancel() {
        onClick(RESULT_CANCELED)
    }

    private fun onClick(result: Int) {
        if (!passEventToParentFragment(result)) {
            if (!passEventToParentActivity(result)) {
                LOG.e(LOG_TAG, "Could not propagate event for requestCode: $targetRequestCode with resultCode $result")
            }
        }
    }



}
