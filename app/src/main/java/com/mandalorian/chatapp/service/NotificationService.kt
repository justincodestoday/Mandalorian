package com.mandalorian.chatapp.service

import android.content.Intent
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import com.mandalorian.chatapp.utils.Contants

class NotificationService: NotificationListenerService() {

    override fun onCreate() {
        super.onCreate()
        Log.d(Contants.DEBUG, "Running")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        super.onNotificationPosted(sbn)

        Log.d(Contants.DEBUG, "Found a notification")
    }
}