package com.mandalorian.chatapp.viewModel


import androidx.lifecycle.viewModelScope
import com.mandalorian.chatapp.data.model.User
import com.mandalorian.chatapp.data.service.AuthService
import com.mandalorian.chatapp.utils.Utils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(private val authRepo: AuthService) : BaseViewModel() {
    val signupFinish: MutableSharedFlow<Unit> = MutableSharedFlow()

    fun signUp(name: String, email: String, password: String) {
        if (Utils.validate(name, email, password)) {
            viewModelScope.launch {
                safeApiCall {
                    authRepo.register(User(name, email, password))
                    signupFinish.emit(Unit)
                }
            }
        } else {
            viewModelScope.launch {
                error.emit("Failed to Register, Please fill in all information")
            }
        }
    }
}