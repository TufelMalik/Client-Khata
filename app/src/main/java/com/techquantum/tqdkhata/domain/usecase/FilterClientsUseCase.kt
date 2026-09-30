package com.techquantum.tqdkhata.domain.usecase

import com.techquantum.tqdkhata.domain.repository.ClientRepository
import com.techquantum.tqdkhata.model.data.local.ClientEntity
import com.techquantum.tqdkhata.model.enums.ProjectStatus
import kotlinx.coroutines.flow.Flow

class FilterClientsUseCase(private val repository: ClientRepository) {
    operator fun invoke(
        query: String?,
        city: String?,
        status: ProjectStatus?
    ): Flow<List<ClientEntity>> = repository.filterClients(query, city, status)
}
