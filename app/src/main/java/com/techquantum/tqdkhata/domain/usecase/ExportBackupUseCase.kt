package com.techquantum.tqdkhata.domain.usecase

import com.techquantum.tqdkhata.domain.repository.ClientRepository

class ExportBackupUseCase(private val repository: ClientRepository) {
    suspend operator fun invoke(): String = repository.exportDataToJson()
}
