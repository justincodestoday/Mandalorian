package com.mandalorian.chatapp.domain.repository

import com.mandalorian.chatapp.data.model.User

interface UserRepository {
    suspend fun getUsers(): List<User>

    suspend fun getUser(uid: String): User?
}