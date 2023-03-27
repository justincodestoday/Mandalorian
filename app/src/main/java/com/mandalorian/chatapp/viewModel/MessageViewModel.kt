package com.mandalorian.chatapp.viewModel

import androidx.lifecycle.viewModelScope
import com.mandalorian.chatapp.data.model.Message
import com.mandalorian.chatapp.data.service.AuthService
import com.mandalorian.chatapp.repository.RealtimeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MessageViewModel @Inject constructor(
    private val realTimeRepository: RealtimeRepository,
    private val authService: AuthService
) : BaseViewModel() {

    fun getAllMessages(): Flow<List<Message>> {
        val user = authService.getUid()
        return realTimeRepository.getAllMessages("WbqlnuoUTHZPxyIRy3BEhKtaC9d2", "8dqvebiIXDhYWb5NhPGHAGhGMPG3")
    }

    fun sendMessage(msg: String) {
        viewModelScope.launch {
            val user = authService.getCurrentUser()
            val userUID = authService.getUid()
            if (user != null && userUID != null) {
                val message = Message(name = user.username, message = msg)
                safeApiCall {
                    realTimeRepository.addMessage(
                        "WbqlnuoUTHZPxyIRy3BEhKtaC9d2",
                        "8dqvebiIXDhYWb5NhPGHAGhGMPG3",
                        message
                    )
                }
            }
        }
    }
}