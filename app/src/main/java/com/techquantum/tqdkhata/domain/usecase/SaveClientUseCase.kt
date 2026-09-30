package com.techquantum.tqdkhata.domain.usecase

import com.techquantum.tqdkhata.domain.repository.ClientRepository
import com.techquantum.tqdkhata.model.data.local.ClientEntity

class SaveClientUseCase(private val repository: ClientRepository) {
    suspend operator fun invoke(client: ClientEntity): Long = repository.saveClient(client)
}
