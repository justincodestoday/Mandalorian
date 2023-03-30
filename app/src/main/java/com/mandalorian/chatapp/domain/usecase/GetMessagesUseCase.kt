package com.mandalorian.chatapp.domain.usecase

import com.mandalorian.chatapp.common.Resource
import com.mandalorian.chatapp.data.model.Message
import com.mandalorian.chatapp.domain.repository.RealTimeRepository
import com.mandalorian.chatapp.ui.presentation.message.MessageEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetMessagesUseCase @Inject constructor(
    private val realTimeRepository: RealTimeRepository
) {
    operator fun invoke(event: MessageEvent): Flow<Resource<List<Message>>> {
        return when (event) {
            is MessageEvent.GetMessages -> {
                realTimeRepository.getAllMessages(event.uid1, event.uid2)
                    .map { Resource.Success(it) }
            }
            is MessageEvent.SendMessage -> {
                val (uid1, uid2, msg) = event
                flow {
                    realTimeRepository.addMessage(uid1, uid2, msg)
                    emit(Resource.Success(listOf(), null))
                }
            }
        }
    }

    operator fun invoke(uid1: String, uid2: String): Flow<List<Message>> {
        return realTimeRepository.getAllMessages(uid1, uid2)
    }

    suspend operator fun invoke(uid1: String, uid2: String, msg: Message) {
        realTimeRepository.addMessage(uid1, uid2, msg)
    }
}