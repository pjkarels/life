package com.bitbybitlabs.life.installed.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast

class MyBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.hasExtra(EXTRA_MESSAGE)) {
            Toast.makeText(context, intent.getStringExtra(EXTRA_MESSAGE), Toast.LENGTH_SHORT).show()
        }
        if (intent.hasExtra(EXTRA_TOGGLE_STATE)) {
            Toast.makeText(context, "Toggled:  " + intent.getBooleanExtra(
                    EXTRA_TOGGLE_STATE, false),
                    Toast.LENGTH_LONG).show()
            receivedCount++
            context.contentResolver.notifyChange(sliceUri, null)
        }
    }

    companion object {
        const val EXTRA_TOGGLE_STATE = "EXTRA_TOGGLE_STATE"
        const val EXTRA_MESSAGE = "EXTRA_MESSAGE"

        var receivedCount = 0
        val sliceUri = Uri.parse("content://com.example.android.app/count")
    }
}