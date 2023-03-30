package com.mandalorian.chatapp.ui.presentation.login

import com.mandalorian.chatapp.data.model.User

sealed class LoginEvent {
    data class Login(val user: User): LoginEvent()
}
