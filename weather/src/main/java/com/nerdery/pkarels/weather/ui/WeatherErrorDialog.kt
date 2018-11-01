package com.nerdery.pkarels.weather.ui

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity

class WeatherErrorDialog : DialogFragment() {

    companion object {
        fun newInstance() = WeatherErrorDialog()
    }

    private lateinit var listener: OnPositiveButtonClickedListener

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val activity = activity as AppCompatActivity
        return AlertDialog.Builder(activity)
                .setTitle("Error")
                .setPositiveButton("Try Again") { dialog, which ->
                    listener.onPositivieButtonClicked()
                }
                .setNegativeButton(android.R.string.ok) { dialog, which ->
                    dialog.dismiss()
                }
                .create()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if (context != null) {
            if (context is OnPositiveButtonClickedListener) {
                listener = context
            } else {
                throw ClassCastException(context.javaClass.name + "does not implement" + OnPositiveButtonClickedListener::class.java.simpleName)
            }
        }
    }

    interface OnPositiveButtonClickedListener {
        fun onPositivieButtonClicked()
    }
}