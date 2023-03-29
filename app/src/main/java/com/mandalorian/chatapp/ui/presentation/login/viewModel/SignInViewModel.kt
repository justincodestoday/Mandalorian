package com.mandalorian.chatapp.ui.presentation.login.viewModel

import androidx.lifecycle.viewModelScope
import com.mandalorian.chatapp.service.AuthService
import com.mandalorian.chatapp.ui.presentation.base.viewModel.BaseViewModel
import com.mandalorian.chatapp.utils.Utils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(private val authRepo: AuthService) : BaseViewModel() {
    val signInComplete: MutableSharedFlow<Unit> = MutableSharedFlow()
    val signUpComplete: MutableSharedFlow<Unit> = MutableSharedFlow()
    val email: MutableStateFlow<String> = MutableStateFlow("")
    val password: MutableStateFlow<String> = MutableStateFlow("")

    fun signIn() {
        if (Utils.validate(email.value, password.value)) {
            viewModelScope.launch {
                safeApiCall {
                    authRepo.login(email.value, password.value)
                    signInComplete.emit(Unit)
                }
            }
        } else {
            viewModelScope.launch {
                error.emit("Failed to Login, Please try again")
            }
        }
    }

    fun navigateToSignUp() {
        viewModelScope.launch {
            signUpComplete.emit(Unit)
        }
    }
}