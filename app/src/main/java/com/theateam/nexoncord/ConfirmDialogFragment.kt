package com.theateam.nexoncord

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class ConfirmDialogFragment : DialogFragment() {
    private var title: String = ""
    private var message: String = ""
    private var positiveButtonText: String = ""
    private var negativeButtonText: String = ""
    private var listener: OnConfirmListener? = null

    interface OnConfirmListener {
        fun onConfirmPositive()
        fun onConfirmNegative()
    }

    companion object {
        fun newInstance(
            title: String,
            message: String,
            positiveButtonText: String = "Yes",
            negativeButtonText: String = "No"
        ): ConfirmDialogFragment {
            val fragment = ConfirmDialogFragment()
            fragment.title = title
            fragment.message = message
            fragment.positiveButtonText = positiveButtonText
            fragment.negativeButtonText = negativeButtonText
            return fragment
        }
    }

    fun setOnConfirmListener(listener: OnConfirmListener) {
        this.listener = listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(positiveButtonText) { _, _ ->
                listener?.onConfirmPositive()
            }
            .setNegativeButton(negativeButtonText) { _, _ ->
                listener?.onConfirmNegative()
            }
            .create()
    }
}