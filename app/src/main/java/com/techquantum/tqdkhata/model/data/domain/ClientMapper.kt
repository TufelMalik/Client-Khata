package com.techquantum.tqdkhata.model.data.domain

import com.techquantum.tqdkhata.domain.model.Client
import com.techquantum.tqdkhata.model.data.local.ClientEntity

fun ClientEntity.toDomain(): Client = Client(
    id = id,
    name = name,
    phone = phone,
    altPhone = altPhone,
    email = email,
    businessName = businessName,
    businessType = businessType,
    requirements = requirements,
    budget = budget,
    status = status,
    priority = priority,
    address = address,
    city = city,
    createdAt = createdAt,
    updatedAt = updatedAt
)

fun Client.toEntity(): ClientEntity = ClientEntity(
    id = id,
    name = name,
    phone = phone,
    altPhone = altPhone,
    email = email,
    businessName = businessName,
    businessType = businessType,
    requirements = requirements,
    budget = budget,
    status = status,
    priority = priority,
    address = address,
    city = city,
    createdAt = createdAt,
    updatedAt = updatedAt
)
