package com.mandalorian.chatapp.domain.useCase

import com.mandalorian.chatapp.common.Resource
import com.mandalorian.chatapp.data.repository.AuthRepository
import com.mandalorian.chatapp.ui.presentation.login.LoginEvent
import com.mandalorian.chatapp.utils.Utils
import kotlinx.coroutines.flow.flow

class LoginUseCase(
    private val authRepository: AuthRepository
) {
    operator fun invoke(event: LoginEvent) = flow {
        try {
            when (event) {
                is LoginEvent.Login -> {
                    val (_, _, email, pass) = event.user
                    if (Utils.validate(email, pass)) {
                        emit(Resource.Loading(true))
                        val res = authRepository.login(event.user.email, event.user.password)
                        emit(Resource.Success(res))
                    } else {
                        emit(Resource.Error("Validation failed"))
                    }
                }
            }
        } catch (e: Exception) {
            emit(Resource.Error("Something went wrong"))
        }
    }
}