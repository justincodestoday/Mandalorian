package com.mandalorian.chatapp.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.*
import com.mandalorian.chatapp.data.model.Message
import com.mandalorian.chatapp.data.model.User
import com.mandalorian.chatapp.service.AuthService
import com.mandalorian.chatapp.data.repository.RealTimeRepository
import com.mandalorian.chatapp.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MessageViewModel @Inject constructor(
    private val realTimeRepository: RealTimeRepository,
    private val userRepo: UserRepository,
    private val authService: AuthService,
    private val savedStateHandle: SavedStateHandle
) : BaseViewModel() {
    val user: MutableLiveData<User> = MutableLiveData()
    val text: MutableStateFlow<String> = MutableStateFlow("")
    val uid2 = savedStateHandle.get<String>("id") ?: ""
    private val userId = authService.getUid()
    private val timestamp = ServerValue.TIMESTAMP

    init {
        val userRef = FirebaseDatabase.getInstance().getReference("users").child(userId!!)
        val connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected")

        connectedRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val connected = snapshot.getValue(Boolean::class.java) ?: false
                if (connected) {
                    userRef.child("online").setValue(true)
                    userRef.child("lastSeen").setValue(timestamp)
                } else {
                    userRef.child("online").onDisconnect().setValue(false)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("test", "Listener was cancelled")
            }
        })
    }

    fun getAllMessages(uid2: String): Flow<List<Message>> {
        return realTimeRepository.getAllMessages(userId ?: "", uid2)
    }

    fun sendMessage() {
        viewModelScope.launch {
            val user = authService.getCurrentUser()
            if (user != null) {
                val message = Message(
                    name = user.username,
                    message = text.value,
                    timestamp = ServerValue.TIMESTAMP["timestamp"]?.toLong() ?: 0L
                )
                safeApiCall {
                    realTimeRepository.addMessage(userId ?: "", uid2, message)
                }
                text.value = ""
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