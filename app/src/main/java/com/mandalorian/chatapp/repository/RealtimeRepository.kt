package com.mandalorian.chatapp.repository

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.ktx.Firebase
import com.mandalorian.chatapp.data.model.Message
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

class RealtimeRepository {
    val ref = Firebase.database.getReference("chat-app")

//    fun getCollectionRef(uid1: String, uid2: String): CollectionReference {
//        return ref.get(uid1+uid2).subColl
//    }

//    fun addMessage(msg: Message) {
//        getCollectionRef.setValue(msg).await()
//    }
//
//    fun getAllMessages() = callbackFlow<List<String>?> {
//        getCollectionRef().addValueEventListener(object: ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                val value = snapshot.getValue<String>()
//                Log.d("debugging", "Message is: $value")
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                Log.d("debugging", "Failed to read messages", error.toException())
//            }
//        })
//        awaitClose()
//    }
}