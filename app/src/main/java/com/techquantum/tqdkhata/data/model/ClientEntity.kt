package com.techquantum.tqdkhata.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "clients")
data class ClientEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val phone: String,
    val altPhone: String? = null,
    val email: String? = null,
    val businessName: String? = null,
    val businessType: String? = null,
    val requirements: String,
    val budget: String? = null,
    val status: ProjectStatus = ProjectStatus.NEW_LEAD,
    val priority: Priority = Priority.MEDIUM,
    val address: String? = null,
    val city: String? = "Bharuch",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
