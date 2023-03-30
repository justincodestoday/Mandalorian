package com.mandalorian.chatapp.ui.presentation.home.viewModel

import androidx.lifecycle.MutableLiveData
import com.mandalorian.chatapp.data.model.User

interface HomeViewModel {
    val users: MutableLiveData<List<User>>
}