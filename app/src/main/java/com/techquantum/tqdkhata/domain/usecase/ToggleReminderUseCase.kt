package com.techquantum.tqdkhata.domain.usecase

import com.techquantum.tqdkhata.domain.repository.ClientRepository

class ToggleReminderUseCase(private val repository: ClientRepository) {
    suspend operator fun invoke(id: Long, isCompleted: Boolean) =
        repository.setReminderCompletion(id, isCompleted)
}
