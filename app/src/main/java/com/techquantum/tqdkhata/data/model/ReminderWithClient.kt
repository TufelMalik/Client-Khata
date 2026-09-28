package com.techquantum.tqdkhata.data.model

data class ReminderWithClient(
    val id: Long,
    val clientId: Long,
    val clientName: String,
    val clientPhone: String,
    val title: String,
    val notes: String?,
    val reminderTimestamp: Long?,
    val isCompleted: Boolean,
    val createdAt: Long
)
