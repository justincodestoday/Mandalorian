package com.mandalorian.chatapp.service

import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.mandalorian.chatapp.data.model.Token
import com.mandalorian.chatapp.utils.Constants

class MyFirebaseMessagingService: FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)

        Log.d(Constants.DEBUG, "Token: $token")

        Firebase.firestore.collection("tokens").add(Token(token))
            .addOnSuccessListener {
                Log.d(Constants.DEBUG, "Token saved successfully")
            }
            .addOnFailureListener {
                Log.d(Constants.DEBUG, "Failed to save token")
                it.printStackTrace()
            }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        Log.d(Constants.DEBUG, "Notification ${message.from}")
    }
}