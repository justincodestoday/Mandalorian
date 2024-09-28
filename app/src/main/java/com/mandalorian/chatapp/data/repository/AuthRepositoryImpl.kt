package com.mandalorian.chatapp.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.mandalorian.chatapp.data.model.User
import kotlinx.coroutines.tasks.await

class AuthRepositoryImpl(private val auth: FirebaseAuth, private val ref: CollectionReference):
    AuthRepository {

    override suspend fun register(user: User) {
        val res = auth.createUserWithEmailAndPassword(user.email, user.password).await()
        if (res.user != null) {
            res.user?.uid?.let { ref.document(it).set(user.copy(id = it)) }
        }
    }

    override suspend fun login(email: String, password: String): Boolean {
        val res = auth.signInWithEmailAndPassword(email, password).await()
        return res.user?.uid != null
    }

    override fun isLoggedIn(): Boolean {
        auth.currentUser ?: return false
        return true
    }

    override fun signOut() {
        auth.signOut()
    }

    override suspend fun getCurrentUser(): User? {
        return auth.uid?.let {
            ref.document(it).get().await().toObject(User::class.java)
        }
    }

    override fun getUid(): String? {
        return auth.uid
    }
}
