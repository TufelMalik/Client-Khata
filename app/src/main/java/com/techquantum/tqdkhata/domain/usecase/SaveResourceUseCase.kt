package com.techquantum.tqdkhata.domain.usecase

import com.techquantum.tqdkhata.domain.repository.ClientRepository
import com.techquantum.tqdkhata.model.data.local.ClientResourceEntity

class SaveResourceUseCase(private val repository: ClientRepository) {
    suspend operator fun invoke(resource: ClientResourceEntity): Long = repository.saveResource(resource)
}
