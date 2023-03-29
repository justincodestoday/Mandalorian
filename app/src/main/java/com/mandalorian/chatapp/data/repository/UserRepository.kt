package com.mandalorian.chatapp.data.repository

import com.google.firebase.firestore.CollectionReference
import com.mandalorian.chatapp.data.model.User
import kotlinx.coroutines.tasks.await

class UserRepository(private val ref: CollectionReference) {
    suspend fun getUsers(): List<User> {
        val res = ref.get().await()
        val users = mutableListOf<User>()
        res.documents.forEach {
            it.toObject(User::class.java)?.let { user ->
                users.add(user)
            }
        }
        return users
    }

    suspend fun getUser(uid: String): User? {
        val res = ref.document(uid).get().await()
        return res.toObject(User::class.java)
    }
}

// Model, View, ViewModel