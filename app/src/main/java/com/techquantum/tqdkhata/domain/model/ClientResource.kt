package com.techquantum.tqdkhata.domain.model

import com.techquantum.tqdkhata.model.enums.ResourceType

data class ClientResource(
    val id: Long = 0,
    val clientId: Long,
    val filePath: String,
    val resourceType: ResourceType = ResourceType.IMAGE,
    val title: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)
