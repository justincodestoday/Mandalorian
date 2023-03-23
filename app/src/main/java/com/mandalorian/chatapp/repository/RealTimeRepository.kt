package com.mandalorian.chatapp.repository

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.mandalorian.chatapp.data.model.Message
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class RealTimeRepository {
    val ref = Firebase.database.getReference("message")

    suspend fun addMessage(msg: Message) {
        ref.push().setValue(msg).await()
    }

    fun getAllMessages() = callbackFlow<List<Message>> {
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val messages = mutableListOf<Message>()
                snapshot.children.forEach {
                    val msg = it.getValue<Message>()
                    msg?.let { message ->
                        messages.add(message)
                    }
                }
                trySend(messages)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("debugging", "cancelled", error.toException())
            }
        })
        awaitClose { }
    }
}