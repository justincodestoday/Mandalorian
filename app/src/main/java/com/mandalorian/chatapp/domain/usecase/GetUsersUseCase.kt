package com.mandalorian.chatapp.domain.usecase

import com.mandalorian.chatapp.common.Resource
import com.mandalorian.chatapp.data.model.User
import com.mandalorian.chatapp.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetUsersUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): Flow<Resource<List<User>>> = flow {
        try {
            emit(Resource.Loading())
            val res = userRepository.getUsers()
            emit(Resource.Success(res))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: ""))
        }
    }
}