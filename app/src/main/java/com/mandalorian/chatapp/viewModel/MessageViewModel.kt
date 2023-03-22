package com.mandalorian.chatapp.viewModel

import androidx.lifecycle.viewModelScope
import com.mandalorian.chatapp.data.model.Message
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

    fun getAllMessages(): Flow<List<Message>> {
        return realTimeRepository.getAllMessages()
    }

    fun sendMessage(msg: String) {
        viewModelScope.launch {
            val message = Message(name = authService.getCurrentUser()?.username ?: "", message = msg)
            safeApiCall { realTimeRepository.addMessage(message) }
        }
    }
}