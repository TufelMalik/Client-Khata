package com.techquantum.tqdkhata.domain.model

data class BackupInfo(
    val version: Int = 1,
    val appName: String = "TQD Khata",
    val exportedAt: Long = System.currentTimeMillis(),
    val clientCount: Int,
    val reminderCount: Int,
    val clients: List<Client>,
    val reminders: List<Reminder>
)
