package com.mandalorian.chatapp.data.model

data class User(
    val id: String = "",
    val username: String = "",
    val email: String = "",
    val password: String = "",
    var online: Boolean = false,
    val lastSeen: Long = 0L
)