package com.techquantum.tqdkhata.domain.model

data class Reminder(
    val id: Long = 0,
    val clientId: Long,
    val title: String,
    val notes: String? = null,
    val reminderTimestamp: Long? = null,
    val isCompleted: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)
