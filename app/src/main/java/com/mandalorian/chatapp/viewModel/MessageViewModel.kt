package com.mandalorian.chatapp.viewModel

import androidx.lifecycle.viewModelScope
import com.mandalorian.chatapp.data.model.Message
import com.mandalorian.chatapp.data.model.User
import com.mandalorian.chatapp.data.service.AuthService
import com.mandalorian.chatapp.repository.RealTimeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MessageViewModel @Inject constructor(
    private val realTimeRepository: RealTimeRepository,
    private val authService: AuthService
) : BaseViewModel() {

    private val timestamp = System.currentTimeMillis()

    fun getAllMessages(uid2: String): Flow<List<Message>> {
        return realTimeRepository.getAllMessages(
            authService.getUid() ?: "", uid2
        )
    }

    fun sendMessage(uid2: String, msg: String) {
        viewModelScope.launch {
            val user = authService.getCurrentUser()
            user?.let {
                val message = Message(name = user.username, message = msg, timestamp = timestamp)
                safeApiCall {
                    realTimeRepository.addMessage(
                        authService.getUid() ?: "",
                        uid2,
                        message
                    )
                }
            }
        }
    }
}