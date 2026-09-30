package com.techquantum.tqdkhata.domain.usecase

import com.techquantum.tqdkhata.domain.repository.ClientRepository
import com.techquantum.tqdkhata.model.data.local.ClientResourceEntity

class DeleteResourceUseCase(private val repository: ClientRepository) {
    suspend operator fun invoke(resource: ClientResourceEntity) = repository.deleteResource(resource)
}
