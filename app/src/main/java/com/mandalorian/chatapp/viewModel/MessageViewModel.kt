package com.mandalorian.chatapp.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mandalorian.chatapp.data.model.Message
import com.mandalorian.chatapp.data.model.User
import com.mandalorian.chatapp.data.service.AuthService
import com.mandalorian.chatapp.repository.RealTimeRepository
import com.mandalorian.chatapp.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MessageViewModel @Inject constructor(
    private val realTimeRepository: RealTimeRepository,
    private val userRepo: UserRepository,
    private val authService: AuthService
) : BaseViewModel() {
    val user: MutableLiveData<User> = MutableLiveData()
    private val timestamp = System.currentTimeMillis()

    fun getAllMessages(uid2: String): Flow<List<Message>> {
        return realTimeRepository.getAllMessages(authService.getUid() ?: "", uid2)
    }

    fun sendMessage(uid2: String, msg: String) {
        viewModelScope.launch {
            val user = authService.getCurrentUser()
            if (user != null) {
                val message = Message(name = user.username, message = msg, timestamp = timestamp)
                safeApiCall {
                    realTimeRepository.addMessage(authService.getUid() ?: "", uid2, message)
                }
            }
        }
    }

    fun getUser(uid: String) {
        viewModelScope.launch {
            val res = safeApiCall { userRepo.getUser(uid) }
            res.let {
                user.value = it
            }
        }
    }
}