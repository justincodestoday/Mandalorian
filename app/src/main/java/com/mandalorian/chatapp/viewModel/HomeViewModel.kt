package com.mandalorian.chatapp.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mandalorian.chatapp.model.model.User
import com.mandalorian.chatapp.service.AuthService
import com.mandalorian.chatapp.model.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val authService: AuthService
) :
    BaseViewModel() {
    val users: MutableLiveData<List<User>> = MutableLiveData()

    override fun onViewCreated() {
        super.onViewCreated()
        viewModelScope.launch {
            val user = authService.getCurrentUser()
            val res = safeApiCall { userRepository.getUsers() }
            res?.let {
                users.value = it.filterNot { person ->
                    person.username == user?.username
                }
            }
        }
    }
}