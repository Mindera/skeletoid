package com.mindera.skeletoid.apprating.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.mindera.skeletoid.apprating.R
import com.mindera.skeletoid.apprating.callbacks.AppRatingDialogResponseCallback
import com.mindera.skeletoid.logs.LOG

class AppRatingDialog: DialogFragment() {

    companion object {
        const val TAG = "AppRatingDialog"

        private const val ARGUMENT_TITLE = "ARGUMENT_TITLE"
        private const val ARGUMENT_MESSAGE = "ARGUMENT_MESSAGE"
        private const val ARGUMENT_POSITIVE_TEXT = "ARGUMENT_POSITIVE_TEXT"
        private const val ARGUMENT_NEUTRAL_TEXT = "ARGUMENT_NEUTRAL_TEXT"
        private const val ARGUMENT_NEGATIVE_TEXT = "ARGUMENT_NEGATIVE_TEXT"

        fun newInstance(
            title: Int = R.string.dialog_title,
            message: Int = R.string.dialog_message,
            positiveButtonText: Int = R.string.dialog_positive_button_text,
            neutralButtonText: Int = R.string.dialog_neutral_button_text,
            negativeButtonText: Int = R.string.dialog_negative_button_text
        ): AppRatingDialog {
            return AppRatingDialog().apply {
                arguments = Bundle().apply {
                    putInt(ARGUMENT_TITLE, title)
                    putInt(ARGUMENT_MESSAGE, message)
                    putInt(ARGUMENT_POSITIVE_TEXT, positiveButtonText)
                    putInt(ARGUMENT_NEUTRAL_TEXT, neutralButtonText)
                    putInt(ARGUMENT_NEGATIVE_TEXT, negativeButtonText)
                }
            }
        }
    }

    private var title: Int = 0
    private var message: Int = 0
    private var positiveButtonText: Int = 0
    private var neutralButtonText: Int = 0
    private var negativeButtonText: Int = 0

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        readArguments()

        return AlertDialog.Builder(activity)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(positiveButtonText) { _, _ ->
                if (activity is AppRatingDialogResponseCallback) {
                    (activity as AppRatingDialogResponseCallback).onRateNowClick()
                } else {
                    LOG.e(TAG, "The activity that calls the prompt should implement AppRatingDialogResponseCallback")
                }
            }
            .setNeutralButton(neutralButtonText) { _, _ ->
                if (activity is AppRatingDialogResponseCallback) {
                    (activity as AppRatingDialogResponseCallback).onRateLaterClick()
                } else {
                    LOG.e(TAG, "The activity that calls the prompt should implement AppRatingDialogResponseCallback")
                }
            }
            .setNegativeButton(negativeButtonText) { _, _ ->
                if (activity is AppRatingDialogResponseCallback) {
                    (activity as AppRatingDialogResponseCallback).onNeverRateClick()
                } else {
                    LOG.e(TAG, "The activity that calls the prompt should implement AppRatingDialogResponseCallback")
                }
            }
            .create()
    }

    private fun readArguments() {
        arguments?.let {
            title = it.getInt(ARGUMENT_TITLE)
            message = it.getInt(ARGUMENT_MESSAGE)
            positiveButtonText = it.getInt(ARGUMENT_POSITIVE_TEXT)
            neutralButtonText = it.getInt(ARGUMENT_NEUTRAL_TEXT)
            negativeButtonText = it.getInt(ARGUMENT_NEGATIVE_TEXT)
        }
    }
}
