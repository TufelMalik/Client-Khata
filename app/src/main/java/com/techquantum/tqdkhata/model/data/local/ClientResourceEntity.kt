package com.techquantum.tqdkhata.model.data.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.techquantum.tqdkhata.model.enums.ResourceType

@Entity(
    tableName = "client_resources",
    foreignKeys = [
        ForeignKey(
            entity = ClientEntity::class,
            parentColumns = ["id"],
            childColumns = ["clientId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["clientId"])]
)
data class ClientResourceEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val clientId: Long,
    val filePath: String,
    val resourceType: ResourceType = ResourceType.IMAGE,
    val title: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)
