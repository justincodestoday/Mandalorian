package com.mandalorian.chatapp

import android.app.Application
import com.mandalorian.chatapp.service.AuthService
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class MyApplication: Application() {
    @Inject
    lateinit var authService: AuthService
    var username: String? = null

    fun fetchUsername() {
        CoroutineScope(Dispatchers.IO).launch {
            val res = authService.getCurrentUser()
            username = res?.username
        }
    }

    override fun onCreate() {
        super.onCreate()
        fetchUsername()
    }
}