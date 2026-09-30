package com.techquantum.tqdkhata.model.response

import com.techquantum.tqdkhata.model.data.local.ClientEntity
import com.techquantum.tqdkhata.model.data.local.ReminderEntity

data class BackupData(
    val version: Int = 1,
    val appName: String = "TQD Khata",
    val exportedAt: Long = System.currentTimeMillis(),
    val clientCount: Int = clients.size,
    val reminderCount: Int = reminders.size,
    val clients: List<ClientEntity>,
    val reminders: List<ReminderEntity>
)

data class ImportResult(
    val clientsImported: Int,
    val remindersImported: Int
)
