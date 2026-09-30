package com.techquantum.tqdkhata.model.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.techquantum.tqdkhata.model.enums.Priority
import com.techquantum.tqdkhata.model.enums.ProjectStatus

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
    val shopImagePath: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
