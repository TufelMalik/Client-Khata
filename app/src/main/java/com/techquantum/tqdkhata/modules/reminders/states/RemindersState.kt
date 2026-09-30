package com.techquantum.tqdkhata.modules.reminders.states

import com.techquantum.tqdkhata.model.data.local.ReminderWithClient

data class RemindersState(
    val reminders: List<ReminderWithClient> = emptyList(),
    val filterPendingOnly: Boolean = true,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
