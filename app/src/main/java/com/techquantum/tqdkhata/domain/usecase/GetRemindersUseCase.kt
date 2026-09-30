package com.techquantum.tqdkhata.domain.usecase

import com.techquantum.tqdkhata.domain.repository.ClientRepository
import com.techquantum.tqdkhata.model.data.local.ReminderEntity
import com.techquantum.tqdkhata.model.data.local.ReminderWithClient
import kotlinx.coroutines.flow.Flow

class GetRemindersUseCase(private val repository: ClientRepository) {
    fun getAllRemindersWithClient(): Flow<List<ReminderWithClient>> =
        repository.getAllRemindersWithClient()

    fun getPendingRemindersWithClient(): Flow<List<ReminderWithClient>> =
        repository.getPendingRemindersWithClient()

    fun getRemindersForClient(clientId: Long): Flow<List<ReminderEntity>> =
        repository.getRemindersForClient(clientId)
}
