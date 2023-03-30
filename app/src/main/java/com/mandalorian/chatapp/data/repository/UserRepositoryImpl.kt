package com.mandalorian.chatapp.data.repository

import com.google.firebase.firestore.CollectionReference
import com.mandalorian.chatapp.data.model.User
<<<<<<<< HEAD:app/src/main/java/com/mandalorian/chatapp/data/repository/UserRepository.kt
========
import com.mandalorian.chatapp.domain.repository.UserRepository
>>>>>>>> aa5984ec9d582d8935daa1b2ecf2d70711aa3061:app/src/main/java/com/mandalorian/chatapp/data/repository/UserRepositoryImpl.kt
import kotlinx.coroutines.tasks.await

class UserRepositoryImpl(private val ref: CollectionReference): UserRepository {
   override suspend fun getUsers(): List<User> {
        val res = ref.get().await()
        val users = mutableListOf<User>()
        res.documents.forEach {
            it.toObject(User::class.java)?.let { user ->
                users.add(user)
            }
        }
        return users
    }

    override suspend fun getUser(uid: String): User? {
        val res = ref.document(uid).get().await()
        return res.toObject(User::class.java)
    }
}

// Model, View, ViewModel