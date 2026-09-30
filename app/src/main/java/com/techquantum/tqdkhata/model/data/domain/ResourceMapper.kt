package com.techquantum.tqdkhata.model.data.domain

import com.techquantum.tqdkhata.domain.model.ClientResource
import com.techquantum.tqdkhata.model.data.local.ClientResourceEntity

fun ClientResourceEntity.toDomain(): ClientResource = ClientResource(
    id = id,
    clientId = clientId,
    filePath = filePath,
    resourceType = resourceType,
    title = title,
    createdAt = createdAt
)

fun ClientResource.toEntity(): ClientResourceEntity = ClientResourceEntity(
    id = id,
    clientId = clientId,
    filePath = filePath,
    resourceType = resourceType,
    title = title,
    createdAt = createdAt
)
