package com.mandalorian.chatapp.data.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.util.Log
import com.mandalorian.chatapp.R
import com.mandalorian.chatapp.data.repository.RealTimeRepositoryImpl
import com.mandalorian.chatapp.utils.Constants

class MyBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d(Constants.DEBUG, "Found a broadcast event")
//        if (intent?.action == Intent.ACTION_AIRPLANE_MODE_CHANGED) {
//            Toast.makeText(context, "Airplane mode changed", Toast.LENGTH_LONG).show()
//        }

        val message = intent?.getStringExtra("messages") ?: ""
        Log.d(Constants.DEBUG, message)

        if (intent?.action == "CHAT_APP_ALARM") {
            val mediaPlayer = MediaPlayer.create(context, R.raw.do_you_know_da_wae)
            mediaPlayer.start()
        }

//        if (intent?.action == "CHAT_APP_SEND") {
//            val realtimeRepository = RealTimeRepositoryImpl()
//        }
    }
}