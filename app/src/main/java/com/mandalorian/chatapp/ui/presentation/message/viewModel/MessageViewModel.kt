package com.mandalorian.chatapp.ui.presentation.message.viewModel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.mandalorian.chatapp.common.Resource
import com.mandalorian.chatapp.data.model.Message
import com.mandalorian.chatapp.data.model.User
import com.mandalorian.chatapp.data.repository.AuthRepository
import com.mandalorian.chatapp.domain.repository.UserRepository
import com.mandalorian.chatapp.domain.useCase.GetMessagesUseCase
import com.mandalorian.chatapp.ui.presentation.base.viewModel.BaseViewModel
import com.mandalorian.chatapp.ui.presentation.message.MessageEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class MessageViewModel @Inject constructor(
    private val userRepo: UserRepository,
    private val authRepository: AuthRepository,
    savedStateHandler: SavedStateHandle,
    private val getMessagesUseCase: GetMessagesUseCase,
) : BaseViewModel() {

    val user: MutableLiveData<User> = MutableLiveData()
    val person: MutableLiveData<User> = MutableLiveData()
    private val userId = authRepository.getUid() ?: ""
    private val timestamp = ServerValue.TIMESTAMP

    val txt: MutableStateFlow<String> = MutableStateFlow("")
    val messages: MutableStateFlow<List<Message>> = MutableStateFlow(listOf())
    private val uid2 = savedStateHandler.get<String>("id") ?: ""

    override suspend fun onViewCreated() {
        super.onViewCreated()
        getAllMessages()
    }


    fun initializeUserStatus() {
        val userRef = Firebase.database.getReference("users").child(userId)
        val connectedRef = Firebase.database.getReference(".info/connected")

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

    private fun getAllMessages() {
        getMessagesUseCase(
            MessageEvent.GetMessages(
                authRepository.getUid() ?: "",
                uid2
            )
        ).onEach {
            when (it) {
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    messages.value = it.data!!
                }
                is Resource.Error -> {
                    error.emit(it.message!!)
                }
            }
        }.launchIn(viewModelScope)
    }

    private suspend fun uploadImageAndGetUrl(imageUri: Uri): String {
        val storageRef = FirebaseStorage.getInstance().getReference("images")
        val imageRef = storageRef.child(imageUri.lastPathSegment ?: "image.jpg")
        val uploadTask = imageRef.putFile(imageUri)
        uploadTask.await()
        return imageRef.downloadUrl.await().toString()
    }

    fun sendMessage() {
        viewModelScope.launch {
            val user = authRepository.getCurrentUser()
            if (user != null) {
                val message = Message(name = user.username, message = txt.value)
                getMessagesUseCase(
                    MessageEvent.SendMessage(
                        authRepository.getUid() ?: "",
                        uid2,
                        message
                    )
                ).onEach {
                    when (it) {
                        is Resource.Loading -> {
                            setTypingIndicator(false)
                        }
                        is Resource.Success -> {
                            txt.value = ""
                            setTypingIndicator(false)
                        }
                        is Resource.Error -> {
                            error.emit(it.message!!)
                            setTypingIndicator(false)
                        }
                    }
                }.launchIn(viewModelScope)
            }
        }
    }

    fun setTypingIndicator(isTyping: Boolean) {
        val userRef = Firebase.database.getReference("users").child(userId)
        userRef.updateChildren(mapOf("isTyping" to isTyping))
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
                        val isTyping =
                            snapshot.child("isTyping").getValue(Boolean::class.java) ?: false
                        val updatedUser = User(
                            online = connected,
                            lastSeen = lastSeen,
                            isTyping = isTyping
                        )
                        user.value = updatedUser
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