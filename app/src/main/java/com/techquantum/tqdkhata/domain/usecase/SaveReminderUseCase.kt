package com.techquantum.tqdkhata.domain.usecase

import com.techquantum.tqdkhata.domain.repository.ClientRepository
import com.techquantum.tqdkhata.model.data.local.ReminderEntity

class SaveReminderUseCase(private val repository: ClientRepository) {
    suspend operator fun invoke(reminder: ReminderEntity): Long = repository.saveReminder(reminder)
}
