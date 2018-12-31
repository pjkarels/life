package com.bitbybitlabs.life.ui

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment

private const val ARG_TITLE = "title"
private const val ARG_MESSAGE = "message"

class ErrorDialog : DialogFragment() {

    private lateinit var title: String
    private lateinit var message: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            title = it.getString(ARG_TITLE)
            message = it.getString(ARG_MESSAGE)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(activity as AppCompatActivity)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok) { dialog, which ->
                    dialog.dismiss()
                }
                .create()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param title Dialog title.
         * @param message Dialog message.
         * @return A new instance of fragment ErrorDialog.
         */
        @JvmStatic
        fun newInstance(title: String, message: String) =
                ErrorDialog().apply {
                    arguments = Bundle().apply {
                        putString(ARG_TITLE, title)
                        putString(ARG_MESSAGE, message)
                    }
                }
    }
}
