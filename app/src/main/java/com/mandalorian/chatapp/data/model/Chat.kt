package com.mandalorian.chatapp.data.model

data class Chat (
    val id: String = "",
    val user1: String = "",
    val user2: String = "",
    val messages: List<Message> = listOf()
)