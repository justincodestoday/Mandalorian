package com.mandalorian.chatapp.service

import android.app.NotificationManager
import android.app.RemoteInput
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.getSystemService
import com.google.protobuf.Empty
import com.mandalorian.chatapp.utils.Contants
import com.mandalorian.chatapp.utils.NotificationUtils

class NotificationService : NotificationListenerService() {

    override fun onCreate() {
        super.onCreate()
        Log.d(Contants.DEBUG, "Running")
    }

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        super.onNotificationPosted(sbn)

        Log.d(Contants.DEBUG, "Found a notification")

        val wNotification = NotificationUtils.getWearableNotification(sbn) ?: return

        val title = wNotification.bundle?.getString("android.title") ?: "Empty"
        val msg = wNotification.bundle?.getString("android.text") ?: "Empty"

        if (title.contains("You") || title == "Empty") return

        Log.d(Contants.DEBUG, "Title: $title\n Body: $msg")

        val intent = Intent()
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val bundle = Bundle()

        val replyText = "Hello, I'm implementing a reply bot"

        if (msg.contains(Regex("hi|hello")))

            bundle.putCharSequence(wNotification.remoteInputs[0].resultKey, replyText)

        RemoteInput.addResultsToIntent(
            wNotification.remoteInputs.toTypedArray(), intent, bundle
        )

        try {
            wNotification.pendingIntent?.let {
                it.send(this, 0, intent)
                if (sbn?.id != null) {
                    NotificationManagerCompat.from(this).cancel(sbn.id)
                } else {
                    cancelNotification(sbn?.key.toString())
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}