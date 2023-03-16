package com.mandalorian.chatapp.data.service

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.mandalorian.chatapp.data.model.User
import kotlinx.coroutines.tasks.await

class AuthService(private val auth: FirebaseAuth, private val ref: CollectionReference) {
    suspend fun createUser(user: User) {
        val res = auth.createUserWithEmailAndPassword(user.email, user.password).await()

        if (res.user != null) {
            ref.document(user.email).set(user).await()
        }
    }

    suspend fun login(email: String, password: String): Boolean? {
        val res = auth.signInWithEmailAndPassword(email, password).await()
        return res.user?.uid != null
    }

    fun isAuthenticate(): Boolean {
        val user = auth.currentUser
        if (user == null) {
            return false
        }
        return true
    }

    fun deAuthenticate() {
        auth.signOut()
    }

    suspend fun getCurrentUser(): User? {
        return auth.currentUser?.email?.let {
            ref.document(it).get().await().toObject(User::class.java)
        }
    }

    fun getUid(): String? {
        return auth.uid
    }
}
