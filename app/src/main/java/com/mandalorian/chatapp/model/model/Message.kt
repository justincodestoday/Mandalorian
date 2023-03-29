package com.mandalorian.chatapp.model.model

import com.google.firebase.database.IgnoreExtraProperties
import com.google.firebase.database.ServerValue

@IgnoreExtraProperties
data class Message(
    val id: String = "",
    val name: String = "",
    val message: String = "",
    var timestamp: Any? = ServerValue.TIMESTAMP
)