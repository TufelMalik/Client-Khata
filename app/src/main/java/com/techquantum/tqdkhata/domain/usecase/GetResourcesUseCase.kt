package com.techquantum.tqdkhata.domain.usecase

import com.techquantum.tqdkhata.domain.repository.ClientRepository
import com.techquantum.tqdkhata.model.data.local.ClientResourceEntity
import kotlinx.coroutines.flow.Flow

class GetResourcesUseCase(private val repository: ClientRepository) {
    operator fun invoke(clientId: Long): Flow<List<ClientResourceEntity>> =
        repository.getResourcesForClient(clientId)
}
