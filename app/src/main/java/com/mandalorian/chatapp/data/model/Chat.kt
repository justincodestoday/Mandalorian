package com.mandalorian.chatapp.data.model

data class Chat (
    val id: String = "",
    val name: String,
    val message: String,
    val messages: List<Message> = listOf()
)