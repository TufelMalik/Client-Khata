package com.techquantum.tqdkhata.domain.usecase

import com.techquantum.tqdkhata.domain.repository.ClientRepository
import com.techquantum.tqdkhata.model.response.BackupData
import com.techquantum.tqdkhata.model.response.ImportResult

class ImportBackupUseCase(private val repository: ClientRepository) {
    suspend operator fun invoke(backupData: BackupData): ImportResult =
        repository.importBackupData(backupData)

    suspend operator fun invoke(jsonData: String): ImportResult =
        repository.importDataFromJson(jsonData)
}
