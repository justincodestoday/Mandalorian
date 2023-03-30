package com.mandalorian.chatapp.ui.presentation.message

import com.mandalorian.chatapp.data.model.Message

sealed class MessageEvent {
    data class SendMessage(
        val uid1: String,
        val uid2: String,
        val msg: Message,
    ) : MessageEvent()

    data class GetMessages(
        val uid1: String,
        val uid2: String,
    ) : MessageEvent()
}
