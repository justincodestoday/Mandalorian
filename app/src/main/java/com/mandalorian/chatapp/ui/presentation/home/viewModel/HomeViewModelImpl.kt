package com.mandalorian.chatapp.ui.presentation.home.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mandalorian.chatapp.common.Resource
import com.mandalorian.chatapp.data.model.User
import com.mandalorian.chatapp.service.AuthService
import com.mandalorian.chatapp.data.repository.UserRepositoryImpl
import com.mandalorian.chatapp.domain.usecase.GetUsersUseCase
import com.mandalorian.chatapp.ui.presentation.base.viewModel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModelImpl @Inject constructor(
    private val usersUseCase: GetUsersUseCase,
    private val authService: AuthService
) : HomeViewModel, BaseViewModel() {

    override val users: MutableLiveData<List<User>> = MutableLiveData()

    override suspend fun onViewCreated() {
        super.onViewCreated()

        usersUseCase().onEach {
            when (it) {
                is Resource.Loading -> {

                }

                is Resource.Success -> {
                    users.value = it.data?.filter { user ->
                        authService.getUid() != user.id
                    }
                }
                is Resource.Error -> {
                    error.emit(it.message!!)
                }
            }
        }.launchIn(viewModelScope)
    }
}