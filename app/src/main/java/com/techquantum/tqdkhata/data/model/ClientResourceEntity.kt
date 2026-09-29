package com.techquantum.tqdkhata.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

enum class ResourceType(val label: String) {
    IMAGE("Image"),
    VIDEO("Video");

    companion object {
        fun fromString(value: String?): ResourceType {
            return entries.firstOrNull { it.name.equals(value, ignoreCase = true) } ?: IMAGE
        }
    }
}

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
