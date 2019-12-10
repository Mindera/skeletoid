package com.mindera.skeletoid.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import com.mindera.skeletoid.logs.LOG

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
        private const val ARG_IGNORE_BACK_PRESS = "ARG_IGNORE_BACK_PRESS"


        fun newInstance(title: String? = null,
                        message: String,
                        positiveButtonText: String?,
                        negativeButtonText: String? = null,
                        neutralButtonText: String? = null,
                        cancellable: Boolean = true,
                        ignoreBackPress: Boolean = false,
                        args: Bundle = Bundle()): AlertDialogFragment {

            val frag = AlertDialogFragment()

            //Parameters to be resend on onDialogResult to the target Fragment or Activity
            val parameters = Bundle()
            parameters.putAll(args)
            frag.setParameters(parameters)

            args.putString(ARG_TITLE, title)
            args.putString(ARG_MESSAGE, message)
            args.putString(ARG_POSITIVE_BUTTON_TEXT, positiveButtonText)
            args.putString(ARG_NEGATIVE_BUTTON_TEXT, negativeButtonText)
            args.putString(ARG_NEUTRAL_BUTTON_TEXT, neutralButtonText)
            args.putBoolean(ARG_CANCELLABLE, cancellable)
            args.putBoolean(ARG_IGNORE_BACK_PRESS, ignoreBackPress)

            //Dialog related parameters
            frag.arguments = args

            return frag
        }
    }

    private var title: String? = null
    private lateinit var message: String
    private var positiveButtonText: String? = null
    private var negativeButtonText: String? = null
    private var neutralButtonText: String? = null
    private var cancellable: Boolean? = null
    private var ignoreBackPress: Boolean? = null

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
            ignoreBackPress = it.getBoolean(ARG_IGNORE_BACK_PRESS)
        }

        isCancelable = cancellable ?: true
        ignoreBackPress = ignoreBackPress ?: false
    }

    override fun setupRxBindings() {
        //unused
    }

    override fun disposeRxBindings() {
        //unused
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val builder = AlertDialog.Builder(activity)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(positiveButtonText) { _, _ ->
                    onPositiveClick()
                }

        cancellable?.let { cancellable ->
            builder.setCancelable(cancellable)

            ignoreBackPress?.let { ignoreBackPress ->
                if (!cancellable && !ignoreBackPress) {
                    //on cancellable == true, set this to listen to the back button
                    builder.setOnKeyListener { _, keyCode, event ->
                        if (keyCode == KeyEvent.KEYCODE_BACK &&
                            event.action == KeyEvent.ACTION_UP
                        ) {
                            Log.v(TAG, "keyPressed")
                            onBackPressed()
                            true
                        } else {
                            false
                        }
                    }
                }
            }
        }

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
