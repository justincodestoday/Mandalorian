package com.mandalorian.chatapp.viewModel

import androidx.lifecycle.MutableLiveData
import com.mandalorian.chatapp.data.model.User
import com.mandalorian.chatapp.data.service.AuthService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel@Inject constructor (private val auth: AuthService): BaseViewModel(){
    val name: MutableLiveData<String> = MutableLiveData()
    val email: MutableLiveData<String> = MutableLiveData()
    val password: MutableLiveData<String> = MutableLiveData()
    val confirmPassword: MutableLiveData<String> = MutableLiveData()
    val signUp: MutableSharedFlow<Unit> = MutableSharedFlow()

    suspend fun signUp() {
//        if (isFormValid()) {
            try {
                val user = User(
                    name = name.value,
                    email = email.value,
                    password = password.value,
                )
                val res = safeApiCall { auth.register(user) }
                if (res != null) {
                    signUp.emit(Unit)
                    success.emit("Register successful")
                } else {
                    error.emit("User already exists")
                }
            } catch (e: Exception) {
                error.emit(e.message.toString())
            }
//        }
    }

//    private suspend fun isFormValid(): Boolean {
//        formErrors.clear()
//        if (name.value?.trim { it <= ' ' }.isNullOrEmpty()) {
//            formErrors.add(Enums.FormError.MISSING_NAME)
//            kotlin.error.emit(Enums.FormError.MISSING_NAME.name)
//        } else if (email.value?.trim { it <= ' ' }.isNullOrEmpty() ||
//            !Patterns.EMAIL_ADDRESS.matcher("${email.value?.trim { it <= ' ' }}").matches()
//        ) {
//            formErrors.add(Enums.FormError.INVALID_EMAIL)
//            kotlin.error.emit(Enums.FormError.INVALID_EMAIL.name)
//        } else if (password.value?.trim { it <= ' ' }
//                .isNullOrEmpty() || password.value?.trim { it <= ' ' }!!.length < 8) {
//            formErrors.add(Enums.FormError.INVALID_PASSWORD)
//            kotlin.error.emit(Enums.FormError.INVALID_PASSWORD.name)
//        } else if (passwordConfirm.value?.trim { it <= ' ' } != password.value?.trim { it <= ' ' }) {
//            formErrors.add(Enums.FormError.PASSWORDS_NOT_MATCHING)
//            kotlin.error.emit(Enums.FormError.PASSWORDS_NOT_MATCHING.name)
//        }
//        return formErrors.isEmpty()
//    }
}