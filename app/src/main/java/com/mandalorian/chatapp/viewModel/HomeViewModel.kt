package com.mandalorian.chatapp.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mandalorian.chatapp.data.model.Chat
import com.mandalorian.chatapp.data.model.Message
import com.mandalorian.chatapp.data.model.User
import com.mandalorian.chatapp.data.service.AuthService
import com.mandalorian.chatapp.repository.RealTimeRepository
import com.mandalorian.chatapp.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val realtimeRepository: RealTimeRepository,
    private val userRepository: UserRepository,
    private val authService: AuthService,
) : BaseViewModel() {

    val users: MutableLiveData<List<User>> = MutableLiveData()

    override fun onViewCreated() {
        super.onViewCreated()
        viewModelScope.launch {
            val user = authService.getCurrentUser()
            val res = safeApiCall { userRepository.getUsers() }
            res?.let {
                users.value = it.filterNot { it.username == user?.username }
            }
        }
    }
}