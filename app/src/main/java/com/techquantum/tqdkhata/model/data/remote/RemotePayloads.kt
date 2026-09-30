package com.techquantum.tqdkhata.model.data.remote

data class ClientSyncPayload(
    val syncId: String,
    val timestamp: Long,
    val clientsCount: Int
)

data class ReminderSyncPayload(
    val syncId: String,
    val timestamp: Long,
    val remindersCount: Int
)
