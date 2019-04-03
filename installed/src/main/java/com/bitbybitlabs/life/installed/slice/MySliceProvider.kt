package com.bitbybitlabs.life.installed.slice

import android.app.PendingIntent
import android.content.Intent
import android.net.Uri
import androidx.core.graphics.drawable.IconCompat
import androidx.slice.Slice
import androidx.slice.SliceProvider
import androidx.slice.builders.ListBuilder
import androidx.slice.builders.SliceAction
import androidx.slice.builders.inputRange
import androidx.slice.builders.list
import androidx.slice.builders.row
import com.bitbybitlabs.life.R
import com.bitbybitlabs.life.installed.receiver.MyBroadcastReceiver
import com.bitbybitlabs.life.ui.MainActivity

class MySliceProvider : SliceProvider() {
    override fun onCreateSliceProvider(): Boolean {
        return true
    }

    override fun onBindSlice(sliceUri: Uri): Slice? {
        val activityAction = createActivityAction()
        return if (sliceUri.path == "/hello") {
            createSlice(sliceUri)
        } else if (sliceUri.path == "/bright") {
            createBrightnessSlice(sliceUri)
        } else if (sliceUri.path == "/count") {
            createDynamicSlice(sliceUri)
        } else {
            list(context, sliceUri, ListBuilder.INFINITY) {
                row {
                    title = "URI not recognized"
                    primaryAction = activityAction
                }
            }
        }
    }

    private fun createSlice(sliceUri: Uri): Slice {
        val activityAction = createActivityAction()
        return list(context, sliceUri, ListBuilder.INFINITY) {
            row {
                title = "Hello Slice World"
                primaryAction = activityAction
            }
        }
    }

    private fun createBrightnessSlice(sliceUri: Uri): Slice {
        val toggleAction =
                SliceAction.createToggle(
                        createToggleIntent("Item clicked"),
                        "Toggle adaptive brightness",
                        false
                )
        return list(context, sliceUri, ListBuilder.INFINITY) {
            row {
                title = "Adaptive birghtness"
                subtitle = "Optimizes brightness for available light"
                primaryAction = toggleAction
            }
            inputRange {
                inputAction = createToastAndIncrement("Item clicked")
                max = 100
                value = 80
            }
        }
    }

    private fun createSliceWithBuilder(sliceUri: Uri): Slice {
        val activityAction = createActivityAction()
        return ListBuilder(context, sliceUri, ListBuilder.INFINITY)
                .addRow {
                    it.title = "Hello Slice World Builder"
                    it.primaryAction = activityAction
                }
                .build()
    }

    private fun createDynamicSlice(sliceUri: Uri): Slice {
        val toastAndIncrementAction = SliceAction.create(
                createToastAndIncrement("Item clicked"),
                IconCompat.createWithResource(context, R.drawable.ic_launcher_foreground),
                ListBuilder.ICON_IMAGE,
                "Increment."
        )
        return list(context, sliceUri, ListBuilder.INFINITY) {
            row {
                primaryAction = toastAndIncrementAction
                title = "Count: ${MyBroadcastReceiver.receivedCount}"
                subtitle = "Click me"
            }
        }
    }

    private fun createToggleIntent(s: String): PendingIntent {
        val intent = Intent(context, MyBroadcastReceiver::class.java)
                .putExtra(MyBroadcastReceiver.EXTRA_MESSAGE, s)
        return PendingIntent.getBroadcast(context, 0, intent, 0)
    }

    private fun createToastAndIncrement(s: String): PendingIntent {
        val intent = Intent(context, MyBroadcastReceiver::class.java)
//            .putExtra(MyBroadcastReceiver.EXTRA_MESSAGE, s)
                .putExtra(MyBroadcastReceiver.EXTRA_TOGGLE_STATE, true)
        return PendingIntent.getBroadcast(context, 0, intent, 0)
    }

    private fun createActivityAction(): SliceAction {
        val intent = Intent(context, MainActivity::class.java)
        return SliceAction.create(
                PendingIntent.getActivity(context, 0, intent, 0),
                IconCompat.createWithResource(context.resources, "Icon", R.drawable.ic_launcher_foreground),
                ListBuilder.ICON_IMAGE,
                "Enter app"
        )
    }
}