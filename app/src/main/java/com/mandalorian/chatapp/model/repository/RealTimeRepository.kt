package com.mandalorian.chatapp.model.repository

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.mandalorian.chatapp.model.model.Message
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class RealTimeRepository {
    private val ref = Firebase.database.getReference("chats")

    private fun getCombinedUid(uid1: String, uid2: String): String {
        return if (uid1 < uid2) {
            uid1 + uid2
        } else {
            uid2 + uid1
        }
    }

//    suspend fun addMessage(msg: Message) {
//        ref.push().setValue(msg).await()
//    }

    suspend fun addMessage(uid1: String, uid2: String, msg: Message) {
       ref.child(getCombinedUid(uid1, uid2)+"/messages")
           .push()
           .setValue(msg)
           .await()
    }

    fun getAllMessages(uid1: String, uid2: String) = callbackFlow<List<Message>> {
        ref.child(getCombinedUid(uid1, uid2)+"/messages").addValueEventListener(object : ValueEventListener {
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

//    fun getAllMessages() = callbackFlow<List<Message>> {
//        ref.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                val messages = mutableListOf<Message>()
//                snapshot.children.forEach {
//                    val msg = it.getValue<Message>()
//                    msg?.let { message ->
//                        messages.add(message)
//                    }
//                }
//                trySend(messages)
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                Log.d("debugging", "cancelled", error.toException())
//            }
//        })
//        awaitClose { }
//    }
}