package com.mandalorian.chatapp

import android.app.Application
import com.mandalorian.chatapp.data.repository.AuthRepository
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class MyApplication: Application() {
    @Inject
    lateinit var authRepository: AuthRepository
    var username: String? = null

    fun fetchUsername() {
        CoroutineScope(Dispatchers.IO).launch {
            val res = authRepository.getCurrentUser()
            username = res?.username
        }
    }

    override fun onCreate() {
        super.onCreate()
        fetchUsername()
    }
}