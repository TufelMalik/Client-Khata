package com.techquantum.tqdkhata.domain.usecase

import com.techquantum.tqdkhata.domain.repository.ClientRepository
import com.techquantum.tqdkhata.model.data.local.ClientEntity
import kotlinx.coroutines.flow.Flow

class GetClientsUseCase(private val repository: ClientRepository) {
    operator fun invoke(): Flow<List<ClientEntity>> = repository.getAllClients()
}
