package com.techquantum.tqdkhata.domain.usecase

import com.techquantum.tqdkhata.domain.repository.ClientRepository

class CleanupVideosUseCase(private val repository: ClientRepository) {
    suspend operator fun invoke() = repository.cleanupAllVideoResources()
}
