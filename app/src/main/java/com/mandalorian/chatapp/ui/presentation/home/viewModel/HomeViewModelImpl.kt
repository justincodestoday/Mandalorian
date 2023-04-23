package com.mandalorian.chatapp.ui.presentation.home.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mandalorian.chatapp.common.Resource
import com.mandalorian.chatapp.data.model.User
import com.mandalorian.chatapp.data.repository.AuthRepository
import com.mandalorian.chatapp.domain.useCase.GetUsersUseCase
import com.mandalorian.chatapp.ui.presentation.base.viewModel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class HomeViewModelImpl @Inject constructor(
    private val usersUseCase: GetUsersUseCase,
    private val authRepository: AuthRepository
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
                        authRepository.getUid() != user.id
                    }
                }
                is Resource.Error -> {
                    error.emit(it.message!!)
                }
            }
        }.launchIn(viewModelScope)
    }
}