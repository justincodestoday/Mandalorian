package com.mandalorian.chatapp.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mandalorian.chatapp.data.service.AuthService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor (private val auth: AuthService): BaseViewModel() {
    val email: MutableLiveData<String> = MutableLiveData()
    val password: MutableLiveData<String> = MutableLiveData()
    val signIn: MutableSharedFlow<Unit> = MutableSharedFlow()
    suspend fun signIn() {
//        if (isFormValid()) {
            try {
                val res = safeApiCall { auth.login(email.value!!, password.value!!) }
                if (res == true) {
                    signIn.emit(Unit)
                    success.emit("Login successful")
                } else {
                    error.emit("Login failed")
                }
            } catch (e: Exception) {
                error.emit(e.message.toString())
            }
//        }
    }
}