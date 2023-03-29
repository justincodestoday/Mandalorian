package com.mandalorian.chatapp.ui.presentation.message.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.mandalorian.chatapp.data.model.Message
import com.mandalorian.chatapp.data.model.User
import com.mandalorian.chatapp.service.AuthService
import com.mandalorian.chatapp.data.repository.RealtimeRepository
import com.mandalorian.chatapp.data.repository.UserRepository
import com.mandalorian.chatapp.ui.presentation.base.viewModel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MessageViewModel @Inject constructor(
    private val realTimeRepository: RealtimeRepository,
    private val userRepo: UserRepository,
    private val authService: AuthService,
    private val savedStateHandler: SavedStateHandle
) : BaseViewModel() {

    val user: MutableLiveData<User> = MutableLiveData()
    val person: MutableLiveData<User> = MutableLiveData()
    private val userId = authService.getUid()
    private val timestamp = ServerValue.TIMESTAMP

    val text: MutableStateFlow<String> = MutableStateFlow("")
    val uid2 = savedStateHandler.get<String>("id") ?: ""


    fun initializeUserStatus() {
        val userRef = Firebase.database.getReference("users").child(userId!!)
        val connectedRef = Firebase.database.getReference(".info/connected")

        connectedRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val connected = snapshot.getValue(Boolean::class.java) ?: false
                Log.d("MessageViewModel", "User connected: $connected")
                if (connected) {
                    userRef.child("online").setValue(true)
                    userRef.child("lastSeen").setValue(timestamp)
                    Log.d("MessageViewModel", "User is online")
                } else {
                    userRef.child("online").onDisconnect().setValue(false)
                    Log.d("MessageViewModel", "User is offline")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("test", "Listener was cancelled")
            }
        })
    }

    fun getAllMessages(uid2: String): Flow<List<Message>> {
        return realTimeRepository.getAllMessages(userId!!, uid2)
    }

    fun sendMessage() {
        viewModelScope.launch {
            val user = authService.getCurrentUser()
            if (user != null) {
                val message = Message(name = user.username, message = text.value)
                safeApiCall {
                    realTimeRepository.addMessage(userId!!, uid2, message)
                }
                text.value = ""
            }
        }
    }

    fun getUser(uid: String) {
        viewModelScope.launch {
            val res = safeApiCall { userRepo.getUser(uid) }
            res.let {
                person.value = it
                val userRef = FirebaseDatabase.getInstance().getReference("users").child(uid)
                userRef.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val connected =
                            snapshot.child("online").getValue(Boolean::class.java) ?: false
                        val lastSeen = snapshot.child("lastSeen").getValue(Long::class.java) ?: 0L
                        val updatedUser = User(
                            online = connected,
                            lastSeen = lastSeen
                        )
                        user.value = updatedUser
                        Log.d("MessageFragment", "User online status: $connected")
                        // Update the user LiveData with the online status of the user
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.w("test", "Listener was cancelled")
                    }
                })
            }
        }
    }
}