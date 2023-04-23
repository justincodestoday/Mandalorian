package com.mandalorian.chatapp.data.repository

import com.mandalorian.chatapp.data.model.User

interface AuthRepository {
    suspend fun register(user:User)
    suspend fun login(email:String, password: String): Boolean
    suspend fun getCurrentUser(): User?
    fun isLoggedIn(): Boolean
    fun signOut()
    fun getUid(): String?
}