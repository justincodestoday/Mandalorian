package com.mandalorian.chatapp.ui.presentation.login.viewModel

import androidx.lifecycle.viewModelScope
import com.mandalorian.chatapp.common.Resource
import com.mandalorian.chatapp.data.model.User
import com.mandalorian.chatapp.domain.usecase.LoginUseCase
import com.mandalorian.chatapp.service.AuthService
import com.mandalorian.chatapp.utils.Utils
import com.mandalorian.chatapp.ui.presentation.base.viewModel.BaseViewModel
import com.mandalorian.chatapp.ui.presentation.login.LoginEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : BaseViewModel() {
    val loginFinish: MutableSharedFlow<Unit> = MutableSharedFlow()
    val signUpComplete: MutableSharedFlow<Unit> = MutableSharedFlow()
    val email: MutableStateFlow<String> = MutableStateFlow("")
    val password: MutableStateFlow<String> = MutableStateFlow("")

    fun login() {
        viewModelScope.launch {
            safeApiCall {
                loginUseCase(
                    LoginEvent.Login(
                        User(email = email.value, password = password.value)
                    )
                ).onEach {
                    when (it) {
                        is Resource.Loading -> {

                        }

                        is Resource.Success -> {
                            loginFinish.emit(Unit)
                        }

                        is Resource.Error -> {
                            error.emit(it.message!!)
                        }
                    }
                }.launchIn(viewModelScope)
            }
        }
    }

    fun navigateToSignUp() {
        viewModelScope.launch {
            signUpComplete.emit(Unit)
        }
    }
}