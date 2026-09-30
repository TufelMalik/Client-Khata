package com.techquantum.tqdkhata.domain.usecase

import com.techquantum.tqdkhata.domain.repository.ClientRepository
import com.techquantum.tqdkhata.model.data.local.ClientEntity

class DeleteClientUseCase(private val repository: ClientRepository) {
    suspend operator fun invoke(client: ClientEntity) = repository.deleteClient(client)
    suspend operator fun invoke(id: Long) = repository.deleteClientById(id)
}
