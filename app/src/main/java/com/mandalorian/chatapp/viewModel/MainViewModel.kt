package com.mandalorian.chatapp.viewModel

import androidx.lifecycle.ViewModel
import com.mandalorian.chatapp.repository.FireStoreTestRepository

class MainViewModel(private val repo: FireStoreTestRepository): ViewModel() {
    fun fetchData() {
        repo.fetchData()
    }
}