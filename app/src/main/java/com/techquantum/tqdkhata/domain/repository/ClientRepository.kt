package com.techquantum.tqdkhata.domain.repository

import com.techquantum.tqdkhata.model.data.local.ClientEntity
import com.techquantum.tqdkhata.model.data.local.ClientResourceEntity
import com.techquantum.tqdkhata.model.data.local.ReminderEntity
import com.techquantum.tqdkhata.model.data.local.ReminderWithClient
import com.techquantum.tqdkhata.model.enums.ProjectStatus
import com.techquantum.tqdkhata.model.response.BackupData
import com.techquantum.tqdkhata.model.response.ImportResult
import kotlinx.coroutines.flow.Flow

interface ClientRepository {
    fun filterClients(query: String?, city: String?, status: ProjectStatus?): Flow<List<ClientEntity>>
    fun getAllClients(): Flow<List<ClientEntity>>
    fun getClientById(id: Long): Flow<ClientEntity?>
    fun getAllDistinctCities(): Flow<List<String>>
    fun getTotalClientCount(): Flow<Int>
    fun getCountByStatus(status: ProjectStatus): Flow<Int>
    suspend fun saveClient(client: ClientEntity): Long
    suspend fun deleteClient(client: ClientEntity)
    suspend fun deleteClientById(id: Long)

    // Reminders
    fun getRemindersForClient(clientId: Long): Flow<List<ReminderEntity>>
    fun getAllRemindersWithClient(): Flow<List<ReminderWithClient>>
    fun getPendingRemindersWithClient(): Flow<List<ReminderWithClient>>
    fun getTodaysRemindersWithClient(): Flow<List<ReminderWithClient>>
    fun getTodaysFollowUpCount(): Flow<Int>
    suspend fun saveReminder(reminder: ReminderEntity): Long
    suspend fun deleteReminder(reminder: ReminderEntity)
    suspend fun setReminderCompletion(id: Long, completed: Boolean)

    // Media & Resources
    fun getResourcesForClient(clientId: Long): Flow<List<ClientResourceEntity>>
    suspend fun saveResource(resource: ClientResourceEntity): Long
    suspend fun deleteResource(resource: ClientResourceEntity)
    suspend fun cleanupAllVideoResources()

    // Export & Import
    suspend fun getAllClientsList(): List<ClientEntity>
    suspend fun getAllRemindersList(): List<ReminderEntity>
    suspend fun exportDataToJson(): String
    suspend fun importBackupData(backupData: BackupData): ImportResult
    suspend fun importDataFromJson(jsonData: String): ImportResult
}
