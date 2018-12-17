package com.mindera.skeletoid.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle

class AlertDialogFragment : AbstractDialogFragment() {

    companion object {
        private const val LOG_TAG = "AlertDialogFragment"

        /**
         * Arguments for Bundle
         */
        private const val ARG_TITLE = "ARG_TITLE"
        private const val ARG_MESSAGE = "ARG_MESSAGE"
        private const val ARG_POSITIVE_BUTTON_TEXT = "ARG_POSITIVE_BUTTON_TEXT"
        private const val ARG_NEGATIVE_BUTTON_TEXT = "ARG_NEGATIVE_BUTTON_TEXT"
        private const val ARG_NEUTRAL_BUTTON_TEXT = "ARG_NEUTRAL_BUTTON_TEXT"
        private const val ARG_CANCELLABLE = "ARG_CANCELLABLE"


        fun newInstance(title: String? = null,
                        message: String,
                        positiveButtonText: String?,
                        negativeButtonText: String? = null,
                        neutralButtonText: String? = null,
                        cancellable: Boolean = true,
                        args: Bundle = Bundle()): AlertDialogFragment {

            val frag = AlertDialogFragment()
            args.putString(ARG_TITLE, title)
            args.putString(ARG_MESSAGE, message)
            args.putString(ARG_POSITIVE_BUTTON_TEXT, positiveButtonText)
            args.putString(ARG_NEGATIVE_BUTTON_TEXT, negativeButtonText)
            args.putString(ARG_NEUTRAL_BUTTON_TEXT, neutralButtonText)
            args.putBoolean(ARG_CANCELLABLE, cancellable)

            //Dialog related parameters
            frag.arguments = args

            //Parameters to send onDialogResult
            frag.setParameters(bundle = args)

            return frag
        }
    }

    private var title: String? = null
    private lateinit var message: String
    private var positiveButtonText: String? = null
    private var negativeButtonText: String? = null
    private var neutralButtonText: String? = null
    private var cancellable: Boolean? = null

    override var isSingleTop = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            title = it.getString(ARG_TITLE)
            message = it.getString(ARG_MESSAGE, "") //Since the constructor does not allow nulls, this won't happen, but..
            positiveButtonText = it.getString(ARG_POSITIVE_BUTTON_TEXT)
            negativeButtonText = it.getString(ARG_NEGATIVE_BUTTON_TEXT)
            neutralButtonText = it.getString(ARG_NEUTRAL_BUTTON_TEXT)
            cancellable = it.getBoolean(ARG_CANCELLABLE)
        }
    }

    override fun setupRxBindings() {}

    override fun disposeRxBindings() {}

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val builder = AlertDialog.Builder(activity)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(positiveButtonText) { _, _ ->
                    onPositiveClick()
                }

        cancellable?.let { builder.setCancelable(it) }

        negativeButtonText?.let {
            builder.setNegativeButton(it) { _, _ ->
                onNegativeClick()
            }
        }

        neutralButtonText?.let {
            builder.setNeutralButton(it) { _, _ ->
                onNeutralClick()
            }
        }

        return builder.create()
    }

    override val isShowing: Boolean
        get() = dialog?.isShowing == true
}
